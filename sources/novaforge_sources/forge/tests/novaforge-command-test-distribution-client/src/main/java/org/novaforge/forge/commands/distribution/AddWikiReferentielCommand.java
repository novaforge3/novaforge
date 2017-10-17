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
package org.novaforge.forge.commands.distribution;

import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.configuration.initialization.internal.datas.ReferentielProjectConstants;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.organization.services.ProjectService;
import org.novaforge.forge.core.organization.services.UserService;
import org.novaforge.forge.core.plugins.data.ActionType;
import org.novaforge.forge.core.plugins.data.DataDTO;
import org.novaforge.forge.core.plugins.data.DokuwikiPageDTO;
import org.novaforge.forge.core.plugins.data.ItemDTO;
import org.novaforge.forge.core.plugins.data.ItemReferenceDTO;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.services.PluginDataService;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.distribution.reference.client.PluginDataClient;

import java.util.List;

/**
 * This command (launched onto LOCAL or ZONAL forge) will will send a request to subscribe to a CENTRAL forge
 * 
 * @author Marc Blachon
 */
@Command(scope = "distribution", name = "add-wiki-page-ref",
    description = "Add a page to the Dokiwiki from the referential project")
public class AddWikiReferentielCommand extends OsgiCommandSupport
{

  private static final String DOKUWIKI_PAGE_NAME        = "distribution-test-page";
  private static final String DOKUWIKI_PAGE_DESCRIPTION = "distribution test page";
  private static final String DOKUWIKI_PAGE_CONTENT     = "distribution-test-page contents: new page for test";
  private ForgeIdentificationService forgeIdentificationService;
  private AuthentificationService    authentificationService;
  private UserService                userService;
  private ProjectService             projectService;
  private ForgeConfigurationService  forgeConfigurationService;
  private PluginsManager             pluginsManager;
  private ApplicationService         applicationService;
  private PluginDataClient           pluginDataClient;
  private String                     referenceProjectName;
  private String                     referenceToolProjectId;
  private String                     pluginInstanceName;
  private String                     referenceProjectId;
  private String                     pluginId                  = null;
  private String                     pluginInstanceId          = null;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object doExecute() throws Exception
  {
    String forgeId;
    try
    {
      // get current forge
      forgeId = forgeIdentificationService.getForgeId().toString();

      // get pluginId for Dokuwiki form ref project
      initPluginId();

      // get referentiel project
      initReferenceProject();

      // get the pluginInstanceId for Wiki
      initPluginInstance();

      // get referentiel tool project
      initToolProjectId();

      // authentication
      final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
      final User user = userService.getUser(superAdministratorLogin);
      authentificationService.login(superAdministratorLogin, user.getPassword());

      final PluginDataService pluginDataService = pluginDataClient.getPluginDataService(pluginId);

      // Get data references
      List<ItemReferenceDTO> references = pluginDataService.getDataReferences(forgeId, pluginInstanceId);

      if (!references.isEmpty())
      {
        // Create a new wiki Page
        DokuwikiPageDTO newPage = new DokuwikiPageDTO(referenceToolProjectId + DOKUWIKI_PAGE_NAME,
            DOKUWIKI_PAGE_DESCRIPTION, DOKUWIKI_PAGE_CONTENT);
        ItemReferenceDTO newPageReference = new ItemReferenceDTO(referenceToolProjectId + DOKUWIKI_PAGE_NAME,
            "1--49", "PAGE");
        ItemDTO newPageItem = new ItemDTO(newPageReference, ActionType.CREATE);
        DataDTO newPageData = new DataDTO(newPageItem, newPage);
        pluginDataService.putData(forgeId, pluginInstanceId, newPageData);
        System.out.println("Dokuwiki page: " + DOKUWIKI_PAGE_NAME + "created.");
      }
    }
    finally
    {
      authentificationService.logout();
    }

    return null;
  }

  private void initPluginId() throws Exception
  {
    final List<PluginMetadata> plugins = pluginsManager
                                             .getPluginsMetadataByCategory(ReferentielProjectConstants.WIKI_CATEGORY);

    for (final PluginMetadata plugin : plugins)
    {
      if (ReferentielProjectConstants.WIKI_TYPE.equals(plugin.getType()))
      {
        pluginId = plugin.getUUID();
      }
    }
  }

  private void initReferenceProject() throws Exception
  {
    referenceProjectId = forgeConfigurationService.getReferentielProjectId();
    referenceProjectName = projectService.getProjectInfo(referenceProjectId).getName();
  }

  private void initPluginInstance() throws Exception
  {

    final List<ProjectApplication> applications = applicationService
        .getAllProjectApplications(referenceProjectId);
    for (final ProjectApplication application : applications)
    {
      final String currentPluginId = application.getPluginUUID().toString();
      if (currentPluginId.equalsIgnoreCase(pluginId))
      {
        pluginInstanceId = application.getPluginInstanceUUID().toString();
        pluginInstanceName = application.getName();
      }
    }
  }

  private void initToolProjectId() throws Exception
  {
    referenceToolProjectId = referenceProjectName.replaceAll(" ", "_").toLowerCase() + "_" + pluginInstanceName
                                                                                                 .replace(" ", "")
                                                                                                 .replaceAll("-", "")
                                                                                                 .replaceAll("_", "")
                                                                                                 .toLowerCase();
  }

  /**
   * Use by container to inject {@link ForgeIdentificationService}
   * 
   * @param pForgeIdentificationService
   *          the forgeIdentificationService to set
   */
  public void setForgeIdentificationService(final ForgeIdentificationService pForgeIdentificationService)
  {

    forgeIdentificationService = pForgeIdentificationService;
  }

  /**
   * @param authentificationService
   */
  public void setAuthentificationService(final AuthentificationService authentificationService)
  {
    this.authentificationService = authentificationService;
  }

  public void setUserService(UserService userService)
  {
    this.userService = userService;
  }    

  public void setProjectService(ProjectService projectService)
  {
    this.projectService = projectService;
  }

  /**
   * @param forgeConfigurationService
   */
  public void setForgeConfigurationService(final ForgeConfigurationService forgeConfigurationService)
  {
    this.forgeConfigurationService = forgeConfigurationService;
  }

  /**
   * @param pluginManager
   */
  public void setPluginsManager(final PluginsManager pluginsManager)
  {
    this.pluginsManager = pluginsManager;
  }

  /**
   * @param applicationService
   */
  public void setApplicationService(final ApplicationService applicationService)
  {
    this.applicationService = applicationService;
  }

  /**
   * @param pluginDataClient
   */
  public void setPluginDataClient(final PluginDataClient pluginDataClient)
  {
    this.pluginDataClient = pluginDataClient;
  }

}
