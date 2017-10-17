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
import org.novaforge.forge.core.organization.services.UserService;
import org.novaforge.forge.core.plugins.data.ActionType;
import org.novaforge.forge.core.plugins.data.AlfrescoFolderDTO;
import org.novaforge.forge.core.plugins.data.DataDTO;
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
@Command(scope = "distribution", name = "add-folder-ged-ref",
    description = "Add a folder to the Alfresco from the referential project")
public class AddFolderAlfrescoReferentielCommand extends OsgiCommandSupport
{

  private static final String        ALFRESCO_TEST_FOLDER = "alfresco_test_folder";

  private ForgeIdentificationService forgeIdentificationService;

  private AuthentificationService    authentificationService;

  private UserService                userService;

  private ForgeConfigurationService  forgeConfigurationService;

  private PluginsManager             pluginsManager;

  private ApplicationService         applicationService;

  private PluginDataClient           pluginDataClient;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object doExecute() throws Exception
  {
    String referenceProjectId;
    String forgeId;
    String pluginId = null;
    String pluginInstanceId = null;

    try
    {
      final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
      final User user = userService.getUser(superAdministratorLogin);
      authentificationService.login(superAdministratorLogin, user.getPassword());

      // get current forge
      forgeId = forgeIdentificationService.getForgeId().toString();

      // get referentiel project
      referenceProjectId = forgeConfigurationService.getReferentielProjectId();

      // get pluginId for Alfresco form referentiel project
      final List<PluginMetadata> plugins = pluginsManager
          .getPluginsMetadataByCategory(ReferentielProjectConstants.GED_CATEGORY);

      for (final PluginMetadata plugin : plugins)
      {
        if (ReferentielProjectConstants.GED_TYPE.equals(plugin.getType()))
        {
          pluginId = plugin.getUUID();
        }
      }

      // get the pluginInstanceId for Alfresco
      final List<ProjectApplication> applications = applicationService
          .getAllProjectApplications(referenceProjectId);
      for (final ProjectApplication application : applications)
      {
        final String currentPluginId = application.getPluginUUID().toString();
        if (currentPluginId.equalsIgnoreCase(pluginId))
        {
          pluginInstanceId = application.getPluginInstanceUUID().toString();
        }
      }
      final PluginDataService pluginDataService = pluginDataClient.getPluginDataService(pluginId);

      // Get data references

      final List<ItemReferenceDTO> references = pluginDataService
          .getDataReferences(forgeId, pluginInstanceId);
      final String parentReferenceId = references.get(0).getReferenceId();

      // Create a new folder
      final AlfrescoFolderDTO newFolder = new AlfrescoFolderDTO();

      // newFolder.setParentPath(referenceToolProjectId);
      newFolder.setParentPath(parentReferenceId);
      newFolder.setName(ALFRESCO_TEST_FOLDER);
      final ItemReferenceDTO newFolderReference = new ItemReferenceDTO(ALFRESCO_TEST_FOLDER, "",
          "cmis:folder");
      final ItemDTO newFolderItem = new ItemDTO(newFolderReference, ActionType.CREATE);
      final DataDTO newFolderData = new DataDTO(newFolderItem, newFolder);
      pluginDataService.putData(forgeId, pluginInstanceId, newFolderData);
      System.out.println("Alfresco Folder created: " + ALFRESCO_TEST_FOLDER);
    }
    finally
    {
      authentificationService.logout();
    }

    return null;
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
