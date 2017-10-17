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
package org.novaforge.forge.configuration.initialization.internal.creator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.configuration.initialization.exceptions.ForgeInitializationException;
import org.novaforge.forge.configuration.initialization.internal.datas.ReferentielProjectConstants;
import org.novaforge.forge.configuration.initialization.internal.properties.InitializationProperties;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectRolePresenter;
import org.novaforge.forge.core.organization.presenters.SpacePresenter;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.reference.tool.ReferenceToolService;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.novaforge.forge.configuration.initialization.internal.datas.ReferentielProjectConstants.REFERENTIEL_SPACE_NAME;

/**
 * @author Guillaume Lamirand
 */
public class ReferentielCreator
{
  /**
   * Default project reference id
   */
  public static final String       REFERENCE_PROJECT_ID = "reference";
  private static final Log LOGGER = LogFactory.getLog(ReferentielCreator.class);
  private AuthentificationService  authentificationService;
  private InitializationProperties initializationProperties;
  private ProjectRolePresenter     rolePresenter;
  private ReferenceToolService     referenceToolService;
  private SpacePresenter           spacePresenter;
  private ProjectPresenter         projectPresenter;

  private PluginService            alfrescoService;
  private PluginService            dokuwikiService;
  private ApplicationCreator       applicationCreator;

  public void createReferentiel() throws ForgeInitializationException
  {
    final boolean create = initializationProperties.isReferentielCreated();
    if (create)
    {
      // This is needed in order to authorize role creating
      authentificationService.login(initializationProperties.getSuperAdministratorLogin(),
          initializationProperties.getSuperAdministratorPassword());
      try
      {
        final String refentielProjectId = REFERENCE_PROJECT_ID;
        createReferentielProject(refentielProjectId);
        createReferentielRole(refentielProjectId);
        createReferentielSpace(refentielProjectId);
        createReferentielApplications(refentielProjectId);
        createReferentielToolsRepositories();
      }
      catch (ProjectServiceException | SpaceServiceException e)
      {
        throw new ForgeInitializationException("Unable to create referentiel project", e);
      }
      authentificationService.logout();
    }
    else
    {
      LOGGER.info("Creation of referentiel project isn't required, so skip it.");
    }
  }

  private void createReferentielProject(final String pReferentielProjectId) throws ProjectServiceException
  {
    final Project referentiel = projectPresenter.newProject();
    referentiel.setProjectId(pReferentielProjectId);
    referentiel.setName(initializationProperties.getReferentielProjectName());
    referentiel.setDescription(initializationProperties.getReferentielProjectDescription());
    referentiel.setLicenceType(initializationProperties.getReferentielProjectLicence());
    projectPresenter.createSystemProject(referentiel);

    LOGGER.info("Referentiel project created.");

  }

  private void createReferentielRole(final String pReferentielProjectId) throws ProjectServiceException
  {
    final ProjectRole member = rolePresenter.newRole();
    member.setName(initializationProperties.getReferentielMemberRoleName());
    rolePresenter.createSystemRole(member, pReferentielProjectId);

    LOGGER.info("Roles of referentiel project created.");

  }

  private void createReferentielSpace(final String pReferentielProjectId) throws SpaceServiceException
  {
    final Space space = spacePresenter.newSpace();
    space.setName(REFERENTIEL_SPACE_NAME);
    spacePresenter.addSpace(pReferentielProjectId, space);
    LOGGER.info("Space of referentiel project created.");

  }

  /**
   * Creates all the Referentiel Applications.
   * 
   * @throws ProjectServiceException
   * @throws SpaceServiceException
   */
  private void createReferentielApplications(final String pReferentielProjectId)
      throws ProjectServiceException, SpaceServiceException
  {
    final List<Space> refSpaces = spacePresenter.getAllSpaces(pReferentielProjectId);

    if ((refSpaces != null) && !refSpaces.isEmpty())
    {
      // get first space (the only one!)
      final Space refSpace = refSpaces.get(0);
      final String spaceUri = refSpace.getUri();

      createReferentielWikiApp(pReferentielProjectId, spaceUri);
      createReferentielGedApp(pReferentielProjectId, spaceUri);
    }
  }

  private void createReferentielToolsRepositories()
  {
    createReferentielToolsDirectory(referenceToolService.getUserStorageDirectory());
    createReferentielToolsDirectory(referenceToolService.getAdminStorageDirectory());
  }

  /**
   * Creates Ref Wiki Application.
   *
   * @param projectId
   * @param pSpaceUri
   */
  private void createReferentielWikiApp(final String pReferentielProjectId, final String pSpaceUri)
  {
    try
    {
      final Map<String, String> roleMappings = getRolesMapping(ReferentielProjectConstants.WIKI_READ_ROLE,
                                                               ReferentielProjectConstants.WIKI_WRITE_ROLE);

      final UUID serviceUUID = dokuwikiService.getServiceUUID();
      if (serviceUUID != null)
      {

        applicationCreator.sheduleTask(pReferentielProjectId, pSpaceUri, roleMappings,
                                       ReferentielProjectConstants.WIKI_TYPE,
                                       ReferentielProjectConstants.WIKI_APPLICATION_NAME);

        LOGGER.info(String.format("Application %s creation sheduled.", ReferentielProjectConstants.WIKI_TYPE));
      }
    }
    catch (final Exception e)
    {
      LOGGER.warn(String.format("Referentiel project: unable to create the %s application",
                                ReferentielProjectConstants.WIKI_TYPE));
    }
  }

  /**
   * Creates Ref ALFRESCO Application.
   *
   * @param projectId
   * @param spaceUri
   */
  private void createReferentielGedApp(final String pReferentielProjectId, final String pSpaceUri)
  {
    try
    {
      final Map<String, String> roleMappings = getRolesMapping(ReferentielProjectConstants.GED_READ_ROLE,
                                                               ReferentielProjectConstants.GED_WRITE_ROLE);

      final UUID serviceUUID = alfrescoService.getServiceUUID();
      if (serviceUUID != null)
      {
        applicationCreator.sheduleTask(pReferentielProjectId, pSpaceUri, roleMappings,
                                       ReferentielProjectConstants.GED_TYPE,
                                       ReferentielProjectConstants.GED_APPLICATION_NAME);

        LOGGER.info(String.format("Application %s created sheduled.", ReferentielProjectConstants.GED_TYPE));
      }
    }
    catch (final Exception e)
    {
      LOGGER.warn(String.format("Referentiel project: unable to create the %s application",
                                ReferentielProjectConstants.GED_TYPE));
    }
  }

  private void createReferentielToolsDirectory(final String directory)
  {
    final File storage = new File(directory);

    if (!storage.exists())
    {
      final boolean created = storage.mkdirs();
      if (!created)
      {
        LOGGER
            .warn(String
                .format(
                    "The reference tools repository [%s] has not been created, the tools import may not work correctly.",
                    directory));
      }
    }
  }

  /**
   * @return a Map of <ToolRole, ProjectRole>
   */
  private Map<String, String> getRolesMapping(final String toolReadRole, final String toolWriteRole)
  {
    final Map<String, String> roleMappings = new HashMap<String, String>();
    roleMappings.put(initializationProperties.getForgeAdministratorRoleName(), toolWriteRole);
    roleMappings.put(initializationProperties.getReferentielMemberRoleName(), toolReadRole);
    return roleMappings;
  }

  public void setInitializationProperties(final InitializationProperties pInitializationProperties)
  {
    initializationProperties = pInitializationProperties;
  }

  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  public void setRolePresenter(final ProjectRolePresenter pRolePresenter)
  {
    rolePresenter = pRolePresenter;
  }

  public void setReferenceToolService(final ReferenceToolService pReferenceToolService)
  {
    referenceToolService = pReferenceToolService;
  }

  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    projectPresenter = pProjectPresenter;
  }

  public void setSpacePresenter(final SpacePresenter pSpacePresenter)
  {
    spacePresenter = pSpacePresenter;
  }

  public void setAlfrescoService(final PluginService pAlfrescoService)
  {
    alfrescoService = pAlfrescoService;
  }

  public void setDokuwikiService(final PluginService pDokuwikiService)
  {
    dokuwikiService = pDokuwikiService;
  }

  /**
   * @param applicationCreator
   *          the applicationCreator to set
   */
  public void setApplicationCreator(final ApplicationCreator applicationCreator)
  {
    this.applicationCreator = applicationCreator;
  }

}
