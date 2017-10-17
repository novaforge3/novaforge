/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.tools.requirements.common.internal.connectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.eresource.EresourcePackage;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.net4j.CDOSession;
import org.eclipse.emf.cdo.net4j.CDOSessionConfiguration;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.channel.IChannel;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.novaforge.forge.commons.technical.historization.annotations.HistorizableParam;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventLevel;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.tools.requirements.common.connectors.ExternalRepositoryRequirementConnector;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementConnectorException;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementFactoryException;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.factories.RequirementFactory;
import org.novaforge.forge.tools.requirements.common.model.EDirectoryLevel;
import org.novaforge.forge.tools.requirements.common.model.ERepositoryType;
import org.novaforge.forge.tools.requirements.common.model.ERequirementType;
import org.novaforge.forge.tools.requirements.common.model.IDirectory;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.tools.requirements.common.model.IRequirementVersion;
import org.novaforge.forge.tools.requirements.common.services.RequirementManagerService;
import org.novaforge.forge.tools.requirements.common.services.RequirementRepositoryService;
import org.obeonetwork.dsl.requirement.Category;
import org.obeonetwork.dsl.requirement.Repository;
import org.obeonetwork.dsl.requirement.Requirement;
import org.obeonetwork.dsl.requirement.RequirementPackage;
import org.obeonetwork.dsl.requirement.RequirementType;
import org.obeonetwork.graal.GraalPackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author sbenoist
 */
public class CDORequirementConnectorImpl extends AbstractRequirementConnector implements
    ExternalRepositoryRequirementConnector
{
  private static final Log               LOGGER = LogFactory.getLog(CDORequirementConnectorImpl.class);

  private static String                  repository;

  private static String                  host;

  private static String                  port;
  private static CDOSessionConfiguration cdoConfiguration;
  private RequirementFactory             requirementFactory;
  private RequirementManagerService      requirementManagerService;
  private RequirementRepositoryService   requirementRepositoryService;
  private HistorizationService           historizationService;

  public CDORequirementConnectorImpl()
  {
    super();
    Net4jUtil.prepareContainer(IPluginContainer.INSTANCE);
    TCPUtil.prepareContainer(IPluginContainer.INSTANCE);

    cdoConfiguration = CDONet4jUtil.createSessionConfiguration();
    cdoConfiguration.setActivateOnOpen(true);
  }

  /**
   * This method allows to update properties for directories and requirements on CDO referential, to move and
   * to delete directories and requirements moved and deleted on CDO referential {@inheritDoc}
   *
   * @param pRepository
   * @param pProjectId
   */
  private void synchronizeRepo(final IRepository pRepository, final String pProjectId)
      throws RequirementConnectorException
  {
    // check mandatory parameters
    if ((repository == null) || (repository.trim().length() == 0) || (host == null)
        || (host.trim().length() == 0) || (port == null) || (port.trim().length() == 0))
    {
      throw new RequirementConnectorException(
          "one of the mandatory parameters like repository, host or port is not set to connect in TCP to CDO Repository");
    }

    CDOSession cdoSession = null;
    try
    {
      // Get a CDO Session
      cdoSession = getSession();

      // Get all the directories from this repository uri to merge them
      final Set<IDirectory> dirsToMerge = requirementManagerService
          .loadAllRootDirectoryTreesByRepository(pRepository);

      // Get the connected repository in the CDO server
      final List<Repository> repositories = getAllRepositories(cdoSession, pRepository.getURI());
      for (final Repository repository : repositories)
      {
        final String reference = buildForgeReference(pProjectId, repository.cdoID().toString());
        final IDirectory dirToMerge = getDirectoryByReference(dirsToMerge, reference);
        if (dirToMerge != null)
        {
          // build a root directory to forge model
          toRootDirectory(dirToMerge, repository, pRepository, pProjectId, reference);
          // remove the elements removed from CDO
          removedUnreferencedElements(dirToMerge, repository);
          // update the root directory
          requirementManagerService.updateDirectory(dirToMerge);
        }
        // create the new directories
        else
        {
          final IDirectory dirToSave = requirementFactory.buildNewDirectory(EDirectoryLevel.ROOT);
          toRootDirectory(dirToSave, repository, pRepository, pProjectId, reference);

          // store the root directory
          requirementManagerService.createDirectory(dirToSave);
        }
      }

      removeUnreferencedRootDirs(repositories, dirsToMerge);
    }
    catch (final RequirementManagerServiceException e)
    {
      throw new RequirementConnectorException(String.format(
          "unable to update root directories from repository with uri=%s", pRepository.getURI()), e);
    }
    catch (final RequirementFactoryException e)
    {
      throw new RequirementConnectorException(
          String.format(
              "unable to build new instances for directory, requirement and requirementVersion for repository with uri=%s",
              pRepository.getURI()), e);
    }
    catch (final Exception e)
    {
      throw new RequirementConnectorException(String.format(
          "unable to synchronize for repository with uri=%s on CDO repository", pRepository.getURI()), e);
    }
    finally
    {
      closeSession(cdoSession);
    }
  }

  private void removeUnreferencedRootDirs(final List<Repository> pSourceDirs,
      final Set<IDirectory> pTargetDirs) throws RequirementConnectorException
  {
    // get the source references
    final Set<String> refs = new HashSet<String>();
    for (final Repository sourceRepository : pSourceDirs)
    {
      refs.add(sourceRepository.cdoID().toString());
    }

    for (final IDirectory dir : pTargetDirs)
    {
      if (!refs.contains(buildCDOReference(dir.getReference())))
      {
        try
        {
          requirementManagerService.deleteRootDirectoryTree(dir);
        }
        catch (final RequirementManagerServiceException e)
        {
          throw new RequirementConnectorException(String.format(
              "unable to delete root directory with reference=%s", dir.getReference()), e);
        }
      }
    }

  }

  private IDirectory getDirectoryByReference(final Set<IDirectory> directories, final String pReference)
  {
    IDirectory returned = null;
    for (final IDirectory directory : directories)
    {
      if (directory.getReference().equals(pReference))
      {
        returned = directory;
        break;
      }
    }

    return returned;
  }

  /**
   * The rootDirectory is equal to the repository object in CDO Model. All the subdirectories are equals to
   * maincategories and subcategories
   *
   * @param IDirectory
   * @param pForgeRepository
   * @param pProjectId
   * @param pRepositoryURI
   * @throws RequirementFactoryException
   * @throws RequirementManagerServiceException
   */
  private void toRootDirectory(final IDirectory pRootDirectory, final Repository pRepository,
      final IRepository pForgeRepository, final String pProjectId, final String pReference)
      throws RequirementFactoryException, RequirementManagerServiceException
  {
    pRootDirectory.setName(pRepository.getName());
    pRootDirectory.setRepository(pForgeRepository);
    pRootDirectory.setReference(pReference);

    // get the maindirectories
    final EList<Category> categories = pRepository.getMainCategories();
    for (final Category category : categories)
    {
      if (category.getId() != null)
      {
        final String reference = buildForgeReference(pProjectId, category.cdoID().toString());

        final IDirectory mainDirectory = findOrCreateDirectory(pRootDirectory, pRootDirectory, reference);
        mainDirectory.setName(category.getId());
        mainDirectory.setDescription(category.getName());
        mainDirectory.setReference(reference);
        // Get all the subdirectories
        addChildren(pRootDirectory, mainDirectory, category, pProjectId);
      }
    }
  }

  /**
   * This method allows to add the children of a directory (ie subdirectories and requirements )
   *
   * @param pParentDirectory
   * @param pCategory
   * @param pProjectId
   * @throws RequirementFactoryException
   * @throws RequirementManagerServiceException
   */
  private void addChildren(final IDirectory pRootDirectory, final IDirectory pParentDirectory,
      final Category pCategory, final String pProjectId) throws RequirementFactoryException,
      RequirementManagerServiceException
  {
    final List<Category> subcategories = pCategory.getSubCategories();
    for (final Category subcategory : subcategories)
    {
      if (subcategory.getId() != null)
      {
        final String reference = buildForgeReference(pProjectId, subcategory.cdoID().toString());

        final IDirectory directory = findOrCreateDirectory(pRootDirectory, pParentDirectory, reference);
        directory.setName(subcategory.getId());
        directory.setDescription(subcategory.getName());
        directory.setReference(reference);
        addChildren(pRootDirectory, directory, subcategory, pProjectId);
      }
    }

    addRequirements(pRootDirectory, pParentDirectory, pCategory, pProjectId);
  }

  private IDirectory findOrCreateDirectory(final IDirectory pRootDirectory,
      final IDirectory pParentDirectory, final String pReference) throws RequirementFactoryException,
      RequirementManagerServiceException
  {
    // check first if the subdirectory exists in its parent directory
    IDirectory directory = pParentDirectory.findDirectoryByReference(pReference);
    if (directory == null)
    {
      // check if the directory reference exists anywhere else and move it
      directory = findDeepDirectoryByReference(pRootDirectory, pReference);
      if (directory != null)
      {
        directory.setParent(pParentDirectory);
      }
      else
      {
        directory = requirementFactory.buildNewDirectory(EDirectoryLevel.LEAF);
        pParentDirectory.addDirectory(directory);
      }

    }

    return directory;
  }

  private IRequirement findOrCreateRequirement(final IDirectory pRootDirectory, final IDirectory pDirectory,
      final String pReference, final String pProjectId) throws RequirementManagerServiceException,
      RequirementFactoryException
  {
    // check first if the requirement exists in its parent directory
    IRequirement requirement = pDirectory.findRequirementByReference(pReference);
    if (requirement == null)
    {
      // check if the requirement reference exists anywhere else and move it
      requirement = findDeepRequirementByReference(pRootDirectory, pReference);
      if (requirement != null)
      {
        requirement.setDirectory(pDirectory);
      }
      else
      {
        // create new reference
        requirement = requirementFactory.buildNewRequirement();
        requirement.setProjectId(pProjectId);
        pDirectory.addRequirement(requirement);
      }
    }

    return requirement;
  }

  // look for the requirement in the full tree recursively
  private IRequirement findDeepRequirementByReference(final IDirectory pDirectory, final String pReference)
  {
    IRequirement returned = pDirectory.findRequirementByReference(pReference);

    if (returned == null)
    {
      final Set<IDirectory> directories = pDirectory.getChildrenDirectories();
      for (final Iterator<IDirectory> it = directories.iterator(); it.hasNext() && (returned == null);)
      {
        returned = findDeepRequirementByReference(it.next(), pReference);
      }
    }

    return returned;
  }

  // look for the requirement in the full tree recursively
  private IDirectory findDeepDirectoryByReference(final IDirectory pDirectory, final String pReference)
  {
    IDirectory returned = pDirectory.findDirectoryByReference(pReference);

    if (returned == null)
    {
      final Set<IDirectory> directories = pDirectory.getChildrenDirectories();
      for (final Iterator<IDirectory> it = directories.iterator(); it.hasNext() && (returned == null);)
      {
        returned = findDeepDirectoryByReference(it.next(), pReference);
      }
    }

    return returned;
  }

  private IRequirementVersion findOrCreateRequirementVersion(final IRequirement pRequirement,
      final int pVersion) throws RequirementManagerServiceException, RequirementFactoryException
  {
    // load the versions if it already exists
    IRequirementVersion requirementVersion = pRequirement.findRequirementVersion(pVersion);

    if (requirementVersion == null)
    {
      requirementVersion = requirementFactory.buildNewRequirementVersion();
      requirementVersion.setCurrentVersion(pVersion);
      pRequirement.addRequirementVersion(requirementVersion);
    }

    return requirementVersion;
  }

  private String buildForgeReference(final String pProjectId, final String pExternalKey)
  {
    // The reference contains projectId to allow multiple projects to refer to the same requirement
    return pProjectId + ":" + pExternalKey;
  }

  /**
   * This method allows to get the requirements of a directory
   *
   * @param pDirectory
   * @param pCategory
   * @param pProjectId
   * @throws RequirementFactoryException
   * @throws RequirementManagerServiceException
   */
  private void addRequirements(final IDirectory pRootDirectory, final IDirectory pDirectory,
      final Category pCategory, final String pProjectId) throws RequirementFactoryException,
      RequirementManagerServiceException
  {
    final List<Requirement> requirements = pCategory.getRequirements();
    for (final Requirement sourceRequirement : requirements)
    {
      final String reference = buildForgeReference(pProjectId, sourceRequirement.cdoID().toString());

      final IRequirement requirement = findOrCreateRequirement(pRootDirectory, pDirectory, reference,
          pProjectId);
      requirement.setAcceptanceCriteria(sourceRequirement.getAcceptanceCriteria());
      requirement.setName(sourceRequirement.getId());
      requirement.setDescription(sourceRequirement.getName());
      requirement.setRationale(sourceRequirement.getRationale());
      requirement.setSubType(sourceRequirement.getSubtype());
      requirement.setReference(reference);

      if (RequirementType.FUNCTIONAL.equals(sourceRequirement.getType()))
      {
        requirement.setType(ERequirementType.FONCTIONAL.getLabel());
      }
      else if (RequirementType.TECHNICAL.equals(sourceRequirement.getType()))
      {
        requirement.setType(ERequirementType.TECHNICAL.getLabel());
      }
      else
      {
        requirement.setType(ERequirementType.UNDEFINED.getLabel());
      }

      // add the version
      final IRequirementVersion reqVersion = findOrCreateRequirementVersion(requirement,
          sourceRequirement.getVersion());
      reqVersion.setStatement(sourceRequirement.getStatement());

      // Get the Graal referenced objects (not implemented in this version
      // EList<EObject> referencedObjects = sourceRequirement.getReferencedObject();
      /*
       * for (EObject object : referencedObjects) { System.out.println(object.getClass().getName()); }
       */
    }
  }

  /**
   * This is to remove the elements which are no more present next to the synchro
   *
   * @param pRootDirectory
   * @param pRepository
   * @throws RequirementManagerServiceException
   */
  private void removedUnreferencedElements(final IDirectory pRootDirectory, final Repository pRepository)
      throws RequirementManagerServiceException
  {
    // we get all the references of each directory and rquirement of the CDO tree
    final Set<String> refs = toRefs(pRepository);

    // we loop on the rootDirectory and look if each directory and each requirement has a reference in the
    // refs set
    final LinkedList<IDirectory> unrefsDirectories = new LinkedList<IDirectory>();
    final Set<IRequirement> unrefsRequirements = new HashSet<IRequirement>();
    getUnrefs(pRootDirectory, refs, unrefsDirectories, unrefsRequirements);

    // delete first all requirements
    for (final IRequirement requirement : unrefsRequirements)
    {
      final IDirectory parent = requirement.getDirectory();
      parent.deleteRequirement(requirement);
      LOGGER.info("delete requirement = " + requirement.getName());
      requirementManagerService.deleteRequirementTree(requirement);
    }

    // delete directories for children to parents
    for (final Iterator<IDirectory> it = unrefsDirectories.descendingIterator(); it.hasNext();)
    {
      final IDirectory directory = it.next();
      final IDirectory parent = directory.getParent();
      parent.deleteDirectory(directory);
      LOGGER.info("delete directory = " + directory.getName());
      requirementManagerService.updateDirectory(parent);
    }
  }

  private String buildCDOReference(final String pReference)
  {
    return pReference.split(":")[1];
  }

  private void getUnrefs(final IDirectory pDirectory, final Set<String> pRefs,
      final List<IDirectory> pUnrefDirectories, final Set<IRequirement> pUnrefRequirements)
      throws RequirementManagerServiceException
  {
    final Set<IRequirement> requirements = pDirectory.getRequirements();
    for (final IRequirement requirement : requirements)
    {
      if (!pRefs.contains(buildCDOReference(requirement.getReference())))
      {
        pUnrefRequirements.add(requirement);
      }
    }

    final Set<IDirectory> directories = pDirectory.getChildrenDirectories();
    for (final IDirectory directory : directories)
    {
      if (!pRefs.contains(buildCDOReference(directory.getReference())))
      {
        pUnrefDirectories.add(directory);
      }

      getUnrefs(directory, pRefs, pUnrefDirectories, pUnrefRequirements);
    }
  }

  private Set<String> toRefs(final Repository pRepository)
  {
    final Set<String> refs = new TreeSet<String>();

    final List<Category> mains = pRepository.getMainCategories();
    for (final Category main : mains)
    {
      flatTheTree(main, refs);
    }
    return refs;
  }

  private void flatTheTree(final Category pCategory, final Set<String> pSet)
  {
    pSet.add(pCategory.cdoID().toString());

    final List<Category> subcategories = pCategory.getSubCategories();
    for (final Category subcategory : subcategories)
    {
      flatTheTree(subcategory, pSet);
    }

    final List<Requirement> reqs = pCategory.getRequirements();
    for (final Requirement req : reqs)
    {
      pSet.add(req.cdoID().toString());
    }
  }

  private List<Repository> getAllRepositories(final CDOSession pCDOSession, final String pRepositoryURI)
      throws Exception
  {
    // get a view and make the query
    final CDOView view = pCDOSession.openView();
    final CDOResource resource = view.getResource(pRepositoryURI);

    // get all the repositories into the resource
    final EList<EObject> objects = resource.getContents();
    final List<Repository> repositories = new ArrayList<Repository>();
    for (final EObject object : objects)
    {
      if (object instanceof Repository)
      {
        repositories.add((Repository) object);
      }
    }

    if ((repositories == null) || (repositories.isEmpty()))
    {
      throw new RequirementConnectorException(String.format(
          "unable to find any requirement's repository object into  CDOResource with uri=%s", pRepositoryURI));
    }

    return repositories;
  }

  @Override
  public String getRepositoryLocation()
  {
    return "cdo://" + host + ":" + port + "/" + repository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean validate(final IRepository pRepository)
  {
    final boolean result = true;
    if ((pRepository != null) && ERepositoryType.OBEO.equals(pRepository.getType()))
    {
      // TODO: Validate the repository
    }
    return result;
  }

  @Override
  @Historization(type = EventType.SYNCHRONIZATION_EXTERNAL_REQUIREMENTS)
  public void synchronize(@HistorizableParam(label = "ProjectId") final String pProjectID, String pCurrentUser)
      throws RequirementConnectorException
  {
    Map<String, Object> maps = new HashMap<>();
    maps.put("ProjectId", pProjectID);
    historizationService.registerEvent(pCurrentUser, EventType.SYNCHRONIZATION_EXTERNAL_REQUIREMENTS, EventLevel.ENTRY, maps);
    try
    {
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("CDO synchronization for project=%s", pProjectID));
      }

      final Set<IRepository> repositories = requirementRepositoryService.findRepositoriesByType(pProjectID,
          ERepositoryType.OBEO);
      for (final IRepository repository : repositories)
      {
        if (LOGGER.isDebugEnabled())
        {
          LOGGER.debug(String.format("CDO synchronization for repository=%s", repository.getURI()));
        }
        synchronizeRepo(repository, pProjectID);
      }

    }
    catch (final RequirementManagerServiceException e)
    {
      throw new RequirementConnectorException(String.format("unable to get project with projectID=%s",
          pProjectID), e);
    }
    historizationService.registerEvent(pCurrentUser, EventType.SYNCHRONIZATION_EXTERNAL_REQUIREMENTS, EventLevel.EXIT, new HashMap<String, Object>());
  }

  @Override
  protected EventType getEventType()
  {
    return EventType.SYNCHRONIZATION_EXTERNAL_REQUIREMENTS;
  }

  @Override
  protected HistorizationService getHistorizationService()
  {
    return historizationService;
  }

  public void setHistorizationService(final HistorizationService historizationService)
  {
    this.historizationService = historizationService;
  }

  private CDOSession getSession() throws RequirementConnectorException
  {
    LOGGER.info("get a CDO session...");
    CDOSession session;
    try
    {
      cdoConfiguration.setConnector(getConnector());
      session = cdoConfiguration.openSession();
      // Register useful packages because the generated packages emulation does not work...
      // session.options().setGeneratedPackageEmulationEnabled(true);
      session.getPackageRegistry().putEPackage(EresourcePackage.eINSTANCE);
      session.getPackageRegistry().putEPackage(RequirementPackage.eINSTANCE);
      session.getPackageRegistry().putEPackage(GraalPackage.eINSTANCE);
      return session;
    }
    catch (final Exception e)
    {
      throw new RequirementConnectorException("an error occured during getting a CDO session", e);
    }
  }

  private IConnector getConnector()
  {
    LOGGER.info(String.format("Get a CDO Connector for host=%s and port=%s", host, port));
    if ((host == null) || (port == null))
    {
      throw new IllegalArgumentException("unable to get CDO connector because host and port can't be null");
    }

    return TCPUtil.getConnector(IPluginContainer.INSTANCE, host + ":" + port);
  }

  private void closeSession(final CDOSession pCDOSession)
  {
    LOGGER.info("close the CDO session and connector...");

    if (pCDOSession != null)
    {
      pCDOSession.close();
    }

    final IConnector connector = cdoConfiguration.getConnector();
    if (connector != null)
    {
      final Collection<IChannel> listeChannel = connector.getChannels();
      for (final IChannel channel : listeChannel)
      {
        channel.close();
      }

      connector.close();
    }
    // now that the session is no more shared, i think it can be dagerous to do that...
    IPluginContainer.INSTANCE.clearElements();
  }

  public void setRepository(final String pRepository)
  {
    repository = pRepository;
    LOGGER.info(String.format("new value for repository is : %s", pRepository));
    cdoConfiguration.setRepositoryName(repository);
  }

  public void setHost(final String pHost)
  {
    host = pHost;
    LOGGER.info(String.format("new value for host is : %s", pHost));
  }

  public void setPort(final String pPort)
  {
    port = pPort;
    LOGGER.info(String.format("new value for port is : %s", pPort));
  }

  public void setRequirementFactory(final RequirementFactory pRequirementFactory)
  {
    requirementFactory = pRequirementFactory;
  }

  public void setRequirementManagerService(final RequirementManagerService pRequirementManagerService)
  {
    requirementManagerService = pRequirementManagerService;
  }

}
