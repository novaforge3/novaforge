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
package org.novaforge.forge.distribution.reference.internal.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.common.util.StringUtils;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.commons.technical.mail.MailService;
import org.novaforge.forge.commons.technical.mail.MailServiceException;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectRolePresenter;
import org.novaforge.forge.core.organization.presenters.SpacePresenter;
import org.novaforge.forge.core.organization.presenters.TemplateNodePresenter;
import org.novaforge.forge.core.organization.presenters.TemplatePresenter;
import org.novaforge.forge.core.organization.presenters.TemplateRolePresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginDataService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.distribution.reference.exception.ReferenceServiceException;
import org.novaforge.forge.distribution.reference.model.ApplicationInError;
import org.novaforge.forge.distribution.reference.model.ApplicationItem;
import org.novaforge.forge.distribution.reference.model.ApplicationNode;
import org.novaforge.forge.distribution.reference.model.ApplicationRoleMapping;
import org.novaforge.forge.distribution.reference.model.ReferenceProject;
import org.novaforge.forge.distribution.reference.model.ReferenceTemplate;
import org.novaforge.forge.distribution.reference.model.ReferenceTemplates;
import org.novaforge.forge.distribution.reference.model.RootNode;
import org.novaforge.forge.distribution.reference.model.SpaceNode;
import org.novaforge.forge.distribution.reference.model.SynchronizationResult;
import org.novaforge.forge.distribution.reference.model.TemplateInError;
import org.novaforge.forge.distribution.reference.model.TemplateRole;
import org.novaforge.forge.distribution.reference.model.TemplateSynchroResult;
import org.novaforge.forge.distribution.reference.model.ToolsSynchronizationReport;
import org.novaforge.forge.distribution.reference.service.SynchonizationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author rols-p
 * @author salvat-a
 */
public class SynchronizationServiceImpl extends AbstractReferenceServiceImpl implements SynchonizationService
{

  private static final Log          LOGGER                  = LogFactory
                                                                .getLog(SynchronizationServiceImpl.class);
  /**
   * Time to wait (in millisecond).
   */
  private final static int    TIME_TO_WAIT_ADD_APP_MS = 2 * 1000;
  // TODO migrate to distribution configuration file
  private final static String REPORT_SUBJECT          = "Reference tools synchronization report";
  private ProjectPresenter          projectPresenter;
  private ProjectRolePresenter      projectRolePresenter;
  private MembershipPresenter       membershipPresenter;
  private UserPresenter             userPresenter;
  private AuthentificationService   authentificationService;
  private ForgeConfigurationService forgeConfigurationService;
  private TemplatePresenter         templatePresenter;
  private TemplateNodePresenter     templateNodePresenter;
  private TemplateRolePresenter     templateRolePresenter;
  private MailService               mailService;
  private SpacePresenter            spacePresenter;

  @Override
  @Historization(type = EventType.GET_REFERENCE_PROJECT, returnLabel = "Result")
  public ReferenceProject getReferenceProject() throws ReferenceServiceException
  {
    try
    {
      handleLogin();

      final String referenceProjectId = forgeConfigurationService.getReferentielProjectId();
      if (StringUtils.isEmpty(referenceProjectId))
      {
        throw new ReferenceServiceException(
            "No reference project found in the forge, cannot get reference project information");
      }

      Project referentiel;
      try
      {
        referentiel = projectPresenter.getProject(referenceProjectId, false);
      }
      catch (final ProjectServiceException e)
      {
        throw new ReferenceServiceException(String.format("No reference project found with id=%s",
            referenceProjectId));
      }

      final ReferenceProject referenceProjectDTO = new ReferenceProject();
      referenceProjectDTO.setId(referentiel.getProjectId());
      referenceProjectDTO.setDescription(referentiel.getDescription());
      referenceProjectDTO.setName(referentiel.getName());
      referenceProjectDTO.setLicence(referentiel.getLicenceType());
      referenceProjectDTO.setMemberRoleName(forgeConfigurationService.getReferentielMemberRoleName());

      // Get all plugin instances that implements the PluginDataService interface
      final List<ProjectApplication> applications = getAllSynchronizableApplications(referenceProjectId);

      // For each plugin instance, get its item references list
      for (final ProjectApplication application : applications)
      {
        referenceProjectDTO.getPluginItemReferences().add(getPluginItemReferences(application));
      }
      return referenceProjectDTO;
    }
    finally
    {
      handleLogout();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.UPDATE_REFERENCE_PROJECT, returnLabel = "Result")
  public SynchronizationResult updateReferenceProject(final ReferenceProject refProjectDTO)
      throws ReferenceServiceException
  {
    SynchronizationResult result = null;
    try
    {
      handleLogin();

      final boolean projectCreated = createOrUpdateReferentielProject(refProjectDTO);
      final boolean isRefRoleCreated = createOrUpdateReferentielRole(refProjectDTO, projectCreated);
      cleanUpReferentielApplications(refProjectDTO);
      result = createOrUpdateReferentielApplications(refProjectDTO);
      if (isRefRoleCreated)
      {
        propagateReferentielRolesToAllUsers(refProjectDTO);
      }
      // maj config
      forgeConfigurationService.setReferentielCreated(true);
    }
    catch (final Exception e)
    {
      LOGGER.error(String.format("An error occurs when trying to synchronize the project %s: error=%s",
          refProjectDTO.getName(), e.getLocalizedMessage()));
      throw new ReferenceServiceException(e);
    }
    finally
    {
      handleLogout();
    }

    LOGGER.info("updateReferenceProject: sucess. " + refProjectDTO);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.UPDATE_TEMPLATES, returnLabel = "Result")
  public TemplateSynchroResult updateTemplates(final ReferenceTemplates refTemplates) throws ReferenceServiceException
  {
    final TemplateSynchroResult result = new TemplateSynchroResult(forgeConfigurationService.getPublicUrl().getHost());
    final List<ReferenceTemplate> templates = refTemplates == null ? null : refTemplates.getTemplates();
    if ((templates != null) && !templates.isEmpty())
    {
      deleteAllTemplates();
      final List<String> ok = new ArrayList<String>();
      final List<TemplateInError> errors = new ArrayList<TemplateInError>();
      for (final ReferenceTemplate template : templates)
      {
        try
        {
          createTemplate(template);
        }
        catch (final Exception e)
        {
          final TemplateInError error = new TemplateInError(template.getName(), e.getMessage());
          errors.add(error);
        }
        ok.add(template.getName());
      }
      result.setTemplatesInError(errors);
      result.setTemplatesOK(ok);
    }
    return result;
  }

  @Override
  @Historization(type = EventType.UPDATE_APPLICATION_DATA, returnLabel = "Result")
  public boolean updateApplicationItem(final ApplicationItem applicationItem) throws ReferenceServiceException
  {
    boolean updateDone = false;
    try
    {
      handleLogin();

      final String forgeId = forgeIdentificationService.getForgeId().toString();
      final String referenceProjectId = forgeConfigurationService.getReferentielProjectId();
      if (StringUtils.isEmpty(referenceProjectId))
      {
        throw new ReferenceServiceException("No reference project found in the forge, cannot update application data");
      }

      final String uri = applicationItem.getUri();
      final ProjectApplication application = applicationPresenter.getApplication(referenceProjectId, uri);
      final PluginDataService pluginDataService = pluginDataClient.getPluginDataService(application.getPluginUUID()
                                                                                                   .toString());
      pluginDataService.putData(forgeId, application.getPluginInstanceUUID().toString(), applicationItem.getData());
      updateDone = true;
    }
    catch (final ApplicationServiceException e)
    {
      LOGGER.error("Could not get the application, application updated failed.");
      throw new ReferenceServiceException(e);
    }
    catch (final PluginServiceException e)
    {
      LOGGER.warn("An error happened during the data update on the plugin, no application update done.");
      updateDone = false;
    }
    finally
    {
      handleLogout();
    }

    return updateDone;
  }

  @Override
  @Historization(type = EventType.SEND_REFERENCE_TOOLS_REPORT, returnLabel = "Result")
  public void sendReferenceToolsUpdateReport(final ToolsSynchronizationReport report) throws ReferenceServiceException
  {
    if (mailService != null)
    {
      try
      {
        final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
        final User user = userPresenter.getUser(superAdministratorLogin);
        final String forgeAdminEmail = user.getEmail();
        LOGGER.info(String.format("Sending the following email to %s: %s", forgeAdminEmail, report.getReport()));
        mailService.sendMail(forgeAdminEmail, REPORT_SUBJECT, report.getReport());
      }
      catch (final MailServiceException e)
      {
        throw new ReferenceServiceException("Unable to send the email report to the forge administrator", e);
      }
      catch (final UserServiceException e)
      {
        throw new ReferenceServiceException("Unable to retrieve the forge administrator", e);
      }
    }
    else
    {
      LOGGER.info("No mail service available, cannot send the report.");
    }
  }

  private void handleLogin() throws ReferenceServiceException
  {
    if (authentificationService.checkLogin())
    {
      authentificationService.logout();
    }
    try
    {
      final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
      final User user = userPresenter.getUser(superAdministratorLogin);
      authentificationService.login(superAdministratorLogin, user.getPassword());
    }
    catch (final UserServiceException e)
    {
      throw new ReferenceServiceException("Unable to authenticate super administrator", e);
    }
  }

  private void handleLogout()
  {
    if (authentificationService.checkLogin())
    {
      authentificationService.logout();
    }
  }

  /**
   * @param pReferenceProject
   * @return true if the referentiel project is created.
   * @throws ProjectServiceException
   * @throws UserServiceException
   */
  private boolean createOrUpdateReferentielProject(final ReferenceProject pReferenceProject)
      throws ProjectServiceException, UserServiceException
  {
    Project referentiel = null;
    try
    {
      referentiel = projectPresenter.getProject(pReferenceProject.getId(), false);
    }
    catch (final ProjectServiceException e)
    {
      LOGGER.info(String.format("No Reference Project found with id=%s", pReferenceProject.getId()));
    }
    boolean create = false;
    if (referentiel == null)
    {
      create = true;
      referentiel = projectPresenter.newProject();
      referentiel.setProjectId(pReferenceProject.getId());
    }
    final String oldName = referentiel.getName();
    referentiel.setName(pReferenceProject.getName());
    referentiel.setDescription(pReferenceProject.getDescription());
    referentiel.setLicenceType(pReferenceProject.getLicence());

    if (create)
    {
      projectPresenter.createSystemProject(referentiel);
      LOGGER.info("Referentiel project created.");
    }
    else
    {
      projectPresenter.updateProject(oldName, referentiel);
      LOGGER.info("Referentiel project updated.");
    }

    return create;
  }

  private void cleanUpReferentielApplications(final ReferenceProject refProjectDTO)
      throws ProjectServiceException, ApplicationServiceException, SpaceServiceException
  {
    // Get all uris to delete
    final List<String> urisToDelete = getAllUrisToDelete(refProjectDTO);

    // Delete the spaces or applications
    // We assume that we have only one level of spaces at the moment (as specified in the UI).
    final String projectId = refProjectDTO.getId();
    final List<Space> spaces = spacePresenter.getAllSpaces(projectId);
    for (final Space space : spaces)
    {
      final String spaceUri = space.getUri();

      if (urisToDelete.contains(spaceUri))
      {
        spacePresenter.removeSpace(projectId, spaceUri);
      }
      else
      {
        for (final ProjectApplication application : applicationPresenter.getAllSpaceApplications(spaceUri,
            projectId))
        {
          final String applicationUri = application.getUri();
          if (urisToDelete.contains(applicationUri))
          {
            applicationPresenter.removeApplication(projectId, applicationUri);
          }
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private List<String> getAllUrisToDelete(final ReferenceProject refProjectDTO)
      throws ProjectServiceException, ApplicationServiceException, SpaceServiceException
  {
    final String projectId = refProjectDTO.getId();

    // Get the list of all the application uris of the reference
    final List<SpaceNode> spaces = refProjectDTO.getRootNode().getSpaces();
    final List<String> referenceUris = new ArrayList<String>();
    if ((spaces != null) && !spaces.isEmpty())
    {
      for (final SpaceNode spaceNodeDTO : spaces)
      {
        getAllUris(referenceUris, spaceNodeDTO);
      }
    }

    // Get the list of uris to update
    final List<Space> oldSpaces = spacePresenter.getAllSpaces(projectId);
    final List<String> oldUris = new ArrayList<String>();
    for (final Space space : oldSpaces)
    {
      final String spaceUri = space.getUri();
      oldUris.add(spaceUri);
      final List<ProjectApplication> oldApplications = applicationPresenter.getAllSpaceApplications(spaceUri,
          projectId);
      for (final ProjectApplication oldApplication : oldApplications)
      {
        oldUris.add(oldApplication.getUri());
      }
    }

    // Return the list of application uris to remove
    final Collection<String> intersection = CollectionUtils.intersection(referenceUris, oldUris);
    oldUris.removeAll(intersection);
    return oldUris;
  }

  private void getAllUris(final List<String> result, final SpaceNode node)
  {
    result.add(node.getUri());

    final List<ApplicationNode> applications = node.getApplications();
    for (final ApplicationNode application : applications)
    {
      result.add(application.getUri());
    }

    final List<SpaceNode> children = node.getChildren();
    for (final SpaceNode child : children)
    {
      getAllUris(result, child);
    }
  }

  /**
   * @param refProjectDTO
   * @return the result of the synchronization.
   * @throws ProjectServiceException
   * @throws SpaceServiceException
   */
  private SynchronizationResult createOrUpdateReferentielApplications(final ReferenceProject refProjectDTO)
      throws ProjectServiceException, SpaceServiceException
  {

    final String refProjectId = refProjectDTO.getId();
    final SynchronizationResult result = createOrUpdateAllSpaces(refProjectDTO.getRootNode(), refProjectId);

    LOGGER.info("Referentiel space updated.");
    return result;
  }

  /**
   * @param parentNodeDTO
   * @param refProjectId
   * @return the list of applications which fail to be created/updated.
   * @throws SpaceServiceException
   */
  private SynchronizationResult createOrUpdateAllSpaces(final RootNode parentNodeDTO,
      final String refProjectId) throws SpaceServiceException
  {
    final SynchronizationResult result = new SynchronizationResult(forgeConfigurationService.getPublicUrl()
        .getHost());
    final List<SpaceNode> spaces = parentNodeDTO.getSpaces();
    final String parentNodeUri = parentNodeDTO.getUri();
    if ((spaces != null) && !spaces.isEmpty())
    {
      for (final SpaceNode spaceNodeDTO : spaces)
      {
        final SynchronizationResult spaceResult = createOrUpdateSpace(refProjectId, parentNodeUri,
            spaceNodeDTO);
        result.getApplicationsInError().addAll(spaceResult.getApplicationsInError());
        result.getApplicationsSynchronized().addAll(spaceResult.getApplicationsSynchronized());
      }
    }

    return result;
  }

  /**
   * @param refProjectId
   * @param parentNodeUri
   * @param spaceNodeDTO
   * @return the list of applications which fail to be created/updated.
   * @throws SpaceServiceException
   */
  private SynchronizationResult createOrUpdateSpace(final String refProjectId, final String parentNodeUri,
      final SpaceNode spaceNodeDTO) throws SpaceServiceException
  {
    final List<ApplicationInError> failedApplis = new ArrayList<ApplicationInError>();
    final List<String> applisOK = new ArrayList<String>();

    final String spaceNodeUri = spaceNodeDTO.getUri();
    Space space = null;
    try
    {
      space = spacePresenter.getSpace(refProjectId, spaceNodeUri);
    }
    catch (final SpaceServiceException e)
    {
      LOGGER.info(String
          .format("No Space found with [projectId=%s, spaceUri=%s]", refProjectId, spaceNodeUri));
    }

    // 1- creates space if does not exist.
    if (space == null)
    {
      space = spacePresenter.newSpace();
      space.setName(spaceNodeDTO.getName());
      spacePresenter.addSpace(refProjectId, space);
    }
    else
    {
      // update name
      final String oldName = space.getName();
      if (spaceNodeDTO.getName().compareToIgnoreCase(oldName) != 0)
      {
        space.setName(spaceNodeDTO.getName());
        spacePresenter.updateSpace(refProjectId, oldName, space);
      }
    }
    // 2- manage applications for this space
    final List<ApplicationNode> applications = spaceNodeDTO.getApplications();
    if ((applications != null) && !applications.isEmpty())
    {
      for (final ApplicationNode appliNodeDTO : applications)
      {
        try
        {
          createOrUpdateApplication(refProjectId, spaceNodeUri, appliNodeDTO);
          applisOK.add(appliNodeDTO.getName());
        }
        catch (final Exception e)
        {
          final String msg = String.format("An error occured when updating the application with uri=%s.",
              appliNodeDTO.getUri());
          LOGGER.error(msg);
          final ApplicationInError error = new ApplicationInError(appliNodeDTO.getName(), String.format(
              "%s, exception=%s", msg, e.getMessage()));
          failedApplis.add(error);
        }
      }
    }

    // 3- Call this method recursively for each child space of this space.
    final List<SpaceNode> childrenSpaces = spaceNodeDTO.getChildren();
    if ((childrenSpaces != null) && !childrenSpaces.isEmpty())
    {
      for (final SpaceNode childSpaceNodeDTO : childrenSpaces)
      {
        final SynchronizationResult spaceResult = createOrUpdateSpace(refProjectId, spaceNodeUri,
            childSpaceNodeDTO);
        failedApplis.addAll(spaceResult.getApplicationsInError());
        applisOK.addAll(spaceResult.getApplicationsSynchronized());
      }
    }

    final SynchronizationResult result = new SynchronizationResult(forgeConfigurationService.getPublicUrl()
        .getHost());
    result.setApplicationsInError(failedApplis);
    result.setApplicationsSynchronized(applisOK);

    return result;
  }

  /**
   * @param refProjectId
   * @param spaceNodeUri
   * @param appliNodeDTO
   * @throws PluginManagerException
   * @throws ApplicationServiceException
   */
  private void createOrUpdateApplication(final String refProjectId, final String spaceNodeUri,
      final ApplicationNode appliNodeDTO) throws PluginManagerException, ApplicationServiceException
  {
    Application appli = null;
    try
    {
      appli = applicationPresenter.getApplication(refProjectId, appliNodeDTO.getUri());
    }
    catch (final ApplicationServiceException e)
    {
      LOGGER.info(String.format("No Application found with [projectId=%s, appliUri=%s]", refProjectId,
          appliNodeDTO.getUri()));
    }
    final Map<String, String> roleMappings = buildRoleMap(appliNodeDTO.getRoleMappings());
    if (appli == null)
    {
      // 0- get Plugin UUID
      final String pluginUUID = getPluginUUID(appliNodeDTO.getType(), appliNodeDTO.getCategory());

      // 1- create application if does not exist
      applicationPresenter.addApplication(refProjectId, spaceNodeUri, appliNodeDTO.getName(),
          appliNodeDTO.getName(), UUID.fromString(pluginUUID), roleMappings);
      LOGGER.info(String.format("Application added [project=%s, space=%s, application=%s]", refProjectId,
          spaceNodeUri, appliNodeDTO.getName()));
      waitApplicationPropagated();
    }
    else
    {
      // 2- update role mapping if application already exist
      applicationPresenter.updateRoleMapping(refProjectId, appli.getUri(), roleMappings);
      LOGGER.info(String.format("Application updated [project=%s, space=%s, application=%s]", refProjectId,
          appli.getUri(), appli.getName()));
    }
  }

  private void waitApplicationPropagated()
  {
    try
    {
      Thread.sleep(TIME_TO_WAIT_ADD_APP_MS);
    }
    catch (final InterruptedException e)
    {
      LOGGER.warn(e);
    }
  }

  /**
   * @param roleMappings
   * @return
   */
  private Map<String, String> buildRoleMap(final Set<ApplicationRoleMapping> roleMappings)
  {
    final Map<String, String> roleMap = new HashMap<String, String>();
    if ((roleMappings != null) && !roleMappings.isEmpty())
    {
      for (final ApplicationRoleMapping dto : roleMappings)
      {
        roleMap.put(dto.getProjectRole(), dto.getApplicationRole());
      }
    }
    return roleMap;
  }

  private boolean createOrUpdateReferentielRole(final ReferenceProject refProjectDTO,
      final boolean projectCreated) throws ProjectServiceException
  {

    boolean isRoleCreated = false;
    if (projectCreated)
    {
      createReferentielRole(refProjectDTO);
      isRoleCreated = true;
    }
    else
    {
      // Here we are in the case the project already exist
      final String oldRole = forgeConfigurationService.getReferentielMemberRoleName();
      final boolean isMemberRoleChanged = !oldRole.equals(refProjectDTO.getMemberRoleName());
      // if role has changed, try to get the old one. If exist update it.
      if (isMemberRoleChanged)
      {
        ProjectRole oldMemberRole = null;
        try
        {
          oldMemberRole = projectRolePresenter.getRole(oldRole, refProjectDTO.getId());
        }
        catch (final ProjectServiceException e)
        {
          LOGGER.info(String.format("No role exist for the project id=%s with the name %s",
              refProjectDTO.getId(), forgeConfigurationService.getForgeMemberRoleName()));
        }
        if (oldMemberRole != null)
        {
          oldMemberRole.setName(refProjectDTO.getMemberRoleName());
          projectRolePresenter.updateRole(forgeConfigurationService.getForgeMemberRoleName(), oldMemberRole,
              refProjectDTO.getId());
        }
      }

    }

    LOGGER.info("Referentiel project roles updated.");
    return isRoleCreated;
  }

  private void createReferentielRole(final ReferenceProject refProjectDTO) throws ProjectServiceException
  {
    final ProjectRole member = projectRolePresenter.newRole();
    member.setName(refProjectDTO.getMemberRoleName());
    projectRolePresenter.createSystemRole(member, refProjectDTO.getId());
    LOGGER.info("Referentiel project role created.");
  }

  private void propagateReferentielRolesToAllUsers(final ReferenceProject refProjectDTO)
      throws UserServiceException, ProjectServiceException, ApplicationServiceException
  {

    // Add all the users to this role.
    final List<User> users = userPresenter.getAllUsers(true);
    for (final User user : users)
    {
      addReferentielProjectMembership(user, refProjectDTO);
    }

    LOGGER.info("Referentiel project roles propagated to all the forge users.");
  }

  private void addReferentielProjectMembership(final User pUser, final ReferenceProject refProjectDTO)
      throws ProjectServiceException, ApplicationServiceException
  {
    final String refProjectId = refProjectDTO.getId();

    final Set<String> roles = new HashSet<String>();
    final RealmType userRealmType = pUser.getRealmType();
    String roleToBeAdded;
    if (RealmType.SYSTEM.equals(userRealmType))
    {
      roleToBeAdded = forgeConfigurationService.getForgeAdministratorRoleName();
    }
    else
    {
      roleToBeAdded = refProjectDTO.getMemberRoleName();
    }
    roles.add(roleToBeAdded);
    pUser.getLogin();

    applicationPresenter.getAllProjectApplications(refProjectId);

    // addd the memberships
    final List<Membership> memberships = membershipPresenter.getAllEffectiveUserMembershipsForUserAndProject(
        refProjectId, pUser.getLogin());
    boolean membershipExist = false;
    for (final Membership membership : memberships)
    {
      final String roleName = membership.getRole().getName();
      if ((roleName != null) && roleName.equals(roleToBeAdded))
      {
        membershipExist = true;
        break;
      }
    }
    if (!membershipExist)
    {
      membershipPresenter.addUserMembership(refProjectId, pUser.getUuid(), roles, null, false);
    }
    else
    {
      membershipPresenter.updateUserMembership(refProjectId, pUser.getUuid(), roles, null, false);
    }
  }

  /**
   * Return a not-null
   *
   * @param pluginType
   * @param pluginCategory
   * @return
   * @throws PluginManagerException
   */
  private String getPluginUUID(final String pluginType, final String pluginCategory)
      throws PluginManagerException
  {
    String pluginUUID = null;
    final List<PluginMetadata> plugins = pluginsManager.getPluginsMetadataByCategory(pluginCategory);
    if ((plugins != null) && !plugins.isEmpty())
    {
      for (final PluginMetadata plugin : plugins)
      {
        if (pluginType.equals(plugin.getType()))
        {
          pluginUUID = plugin.getUUID();
        }
      }
    }
    if (pluginUUID == null)
    {
      throw new PluginManagerException(String.format("No pluginUUID found for [type=%s, category=%s]",
          pluginType, pluginCategory));
    }
    return pluginUUID;
  }

  /**
   * @param refTemplate
   * @throws TemplateServiceException
   * @throws PluginManagerException
   * @throws ProjectServiceException
   */
  private void createTemplate(final ReferenceTemplate refTemplate) throws TemplateServiceException,
      ProjectServiceException, PluginManagerException
  {
    // 1- creates the template
    final Template template = templatePresenter.newTemplate();
    template.setTemplateId(refTemplate.getId());
    template.setName(refTemplate.getName());
    template.setDescription(refTemplate.getDescription());
    templatePresenter.createTemplate(template);

    // 2- creates the roles
    final List<TemplateRole> roles = refTemplate.getRoles();
    for (final TemplateRole templateRole : roles)
    {
      final Role role = templateRolePresenter.newRole();
      role.setName(templateRole.getName());
      role.setDescription(templateRole.getDescription());
      if (templateRole.isSystem())
      {
        templateRolePresenter.createSystemRole(role, refTemplate.getId());
      }
      else
      {
        templateRolePresenter.createRole(role, refTemplate.getId());
      }
    }

    // 3- creates the applications
    createTemplateSpaces(refTemplate.getRootNode(), refTemplate.getId());

    // 4 - Activate the template if enabled.
    if ((refTemplate.getStatus() != null)
        && refTemplate.getStatus().equals(ReferenceTemplate.RefTemplateStatus.ENABLE))
    {
      templatePresenter.enableTemplate(refTemplate.getId());
    }
  }

  /**
   *
    */
  private void deleteAllTemplates() throws ReferenceServiceException
  {
    List<Template> templates;
    try
    {
      templates = templatePresenter.getTemplates();
    }
    catch (final TemplateServiceException e)
    {
      final String msg = "An error occurs when retreiving the templates to be removed";
      LOGGER.error(msg);
      throw new ReferenceServiceException(msg, e);
    }
    if ((templates != null) && !templates.isEmpty())
    {
      for (final Template template : templates)
      {
        try
        {
          templatePresenter.deleteTemplate(template.getTemplateId());
        }
        catch (final TemplateServiceException e)
        {
          final String msg = String.format("An error occurs when deleting the template id=%s",
              template.getName());
          LOGGER.error(msg);
          throw new ReferenceServiceException(msg, e);
        }
      }
    }

  }

  /**
   * @param parentNodeDTO
   * @param templateId
   * @throws TemplateServiceException
   * @throws ProjectServiceException
   * @throws PluginManagerException
   */
  private void createTemplateSpaces(final RootNode parentNodeDTO, final String templateId)
      throws TemplateServiceException, PluginManagerException
  {
    final List<SpaceNode> spaces = parentNodeDTO.getSpaces();
    final String parentNodeUri = parentNodeDTO.getUri();
    if ((spaces != null) && !spaces.isEmpty())
    {
      for (final SpaceNode spaceNodeDTO : spaces)
      {
        createTemplateSpace(templateId, parentNodeUri, spaceNodeDTO);
      }
    }
  }

  /**
   * @param templateId
   * @param parentNodeUri
   * @param spaceNodeDTO
   * @throws TemplateServiceException
   * @throws PluginManagerException
   */
  private void createTemplateSpace(final String templateId, final String parentNodeUri,
      final SpaceNode spaceNodeDTO) throws TemplateServiceException, PluginManagerException
  {
    final String spaceNodeUri = spaceNodeDTO.getUri();
    final Space space = templateNodePresenter.newSpace();
    space.setName(spaceNodeDTO.getName());
    templateNodePresenter.addSpace(templateId, space);

    // 2- manage applications for this space
    final List<ApplicationNode> applications = spaceNodeDTO.getApplications();
    if ((applications != null) && !applications.isEmpty())
    {
      for (final ApplicationNode appliNodeDTO : applications)
      {
        createTemplateApplication(templateId, spaceNodeUri, appliNodeDTO);
      }
    }
    // 3- Call this method recursively for each child space of this space.
    final List<SpaceNode> childrenSpaces = spaceNodeDTO.getChildren();
    if ((childrenSpaces != null) && !childrenSpaces.isEmpty())
    {
      for (final SpaceNode childSpaceNodeDTO : childrenSpaces)
      {
        createTemplateSpace(templateId, spaceNodeUri, childSpaceNodeDTO);
      }
    }

  }

  /**
   * @param templateId
   * @param spaceNodeUri
   * @param appliNodeDTO
   * @throws TemplateServiceException
   * @throws PluginManagerException
   */
  private void createTemplateApplication(final String templateId, final String spaceNodeUri,
      final ApplicationNode appliNodeDTO) throws TemplateServiceException, PluginManagerException
  {
    final Map<String, String> roleMappings = buildRoleMap(appliNodeDTO.getRoleMappings());
    // 0- get Plugin UUID
    final String pluginUUID = getPluginUUID(appliNodeDTO.getType(), appliNodeDTO.getCategory());

    // 1- create application if does not exist
    templateNodePresenter.addApplication(templateId, spaceNodeUri, appliNodeDTO.getName(),
        UUID.fromString(pluginUUID), roleMappings);
    LOGGER.info(String.format("Template Application added [template=%s, space=%s, application=%s]",
        templateId, spaceNodeUri, appliNodeDTO.getName()));

  }

}
