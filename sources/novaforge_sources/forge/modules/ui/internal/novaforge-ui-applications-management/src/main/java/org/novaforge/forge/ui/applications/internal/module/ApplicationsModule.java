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
package org.novaforge.forge.ui.applications.internal.module;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.organization.presenters.CompositionPresenter;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectRolePresenter;
import org.novaforge.forge.core.organization.presenters.SpacePresenter;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginsCategoryManager;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalMessages;

import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public class ApplicationsModule implements PortalModule
{

  private static PortalMessages            PORTAL_MESSAGES;
  private static SpacePresenter            SPACE_PRESENTER;
  private static CompositionPresenter      COMPOSITION_PRESENTER;
  private static ApplicationPresenter      APPLICATION_PRESENTER;
  private static ProjectRolePresenter      PROJECT_ROLE_PRESENTER;
  private static MembershipPresenter       MEMBERSHIP_PRESENTER;
  private static PluginsManager            PLUGINS_MANAGER;
  private static PluginsCategoryManager    PLUGINS_CATEGORY_MANAGER;
  private static ForgeConfigurationService FORGE_CONFIGURATION_SERVICE;

  /**
   * Get implementation of {@link PortalMessages}
   *
   * @return the {@link PortalMessages}
   */
  public static PortalMessages getPortalMessages()
  {
    return PORTAL_MESSAGES;
  }

  /**
   * Use by container to inject {@link PortalMessages}
   *
   * @param pPortalMessages
   *          the portalMessages to set
   */
  public void setPortalMessages(final PortalMessages pPortalMessages)
  {
    PORTAL_MESSAGES = pPortalMessages;
  }

  /**
   * Get implementation of {@link SpacePresenter}
   *
   * @return the {@link SpacePresenter}
   */
  public static SpacePresenter getSpacePresenter()
  {
    return SPACE_PRESENTER;
  }

  /**
   * Use by container to inject {@link SpacePresenter}
   *
   * @param pSpacePresenter
   *          the spacePresenter to set
   */
  public void setSpacePresenter(final SpacePresenter pSpacePresenter)
  {
    SPACE_PRESENTER = pSpacePresenter;
  }

  /**
   * Get implementation of {@link MembershipPresenter}
   *
   * @return the {@link MembershipPresenter}
   */
  public static MembershipPresenter getMembershipPresenter()
  {
    return MEMBERSHIP_PRESENTER;
  }

  /**
   * Use by container to inject {@link MembershipPresenter}
   *
   * @param pMembershipPresenter
   *          the membershipPresenter to set
   */
  public void setMembershipPresenter(final MembershipPresenter pMembershipPresenter)
  {
    MEMBERSHIP_PRESENTER = pMembershipPresenter;
  }

  /**
   * Get implementation of {@link ApplicationPresenter}
   *
   * @return the {@link ApplicationPresenter}
   */
  public static ApplicationPresenter getApplicationPresenter()
  {
    return APPLICATION_PRESENTER;
  }

  /**
   * Use by container to inject {@link ApplicationPresenter}
   *
   * @param pApplicationPresenter
   *          the applicationPresenter to set
   */
  public void setApplicationPresenter(final ApplicationPresenter pApplicationPresenter)
  {
    APPLICATION_PRESENTER = pApplicationPresenter;
  }

  /**
   * Get implementation of {@link PluginsManager}
   *
   * @return the {@link PluginsManager}
   */
  public static PluginsManager getPluginsManager()
  {
    return PLUGINS_MANAGER;
  }

  /**
   * Use by container to inject {@link PluginsManager}
   *
   * @param pPluginsManager
   *          the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    PLUGINS_MANAGER = pPluginsManager;
  }

  /**
   * Get implementation of {@link ProjectRolePresenter}
   *
   * @return the {@link ProjectRolePresenter}
   */
  public static ProjectRolePresenter getProjectRolePresenter()
  {
    return PROJECT_ROLE_PRESENTER;
  }

  /**
   * Use by container to inject {@link ProjectRolePresenter}
   *
   * @param pProjectRolePresenter
   *          the projectRolePresenter to set
   */
  public void setProjectRolePresenter(final ProjectRolePresenter pProjectRolePresenter)
  {
    PROJECT_ROLE_PRESENTER = pProjectRolePresenter;
  }

  /**
   * Get implementation of {@link CompositionPresenter}
   *
   * @return the {@link CompositionPresenter}
   */
  public static CompositionPresenter getCompositionPresenter()
  {
    return COMPOSITION_PRESENTER;
  }

  /**
   * Use by container to inject {@link CompositionPresenter}
   *
   * @param pCompositionPresenter
   *          the compositionPresenter to set
   */
  public void setCompositionPresenter(final CompositionPresenter pCompositionPresenter)
  {
    COMPOSITION_PRESENTER = pCompositionPresenter;
  }

  /**
   * Get implementation of {@link PluginsCategoryManager}
   *
   * @return the {@link PluginsCategoryManager}
   */
  public static PluginsCategoryManager getPluginsCategoryManager()
  {
    return PLUGINS_CATEGORY_MANAGER;
  }

  /**
   * Use by container to inject {@link PluginsCategoryManager}
   *
   * @param pPluginsCategoryManager
   *          the pluginsCategoryManager to set
   */
  public void setPluginsCategoryManager(final PluginsCategoryManager pPluginsCategoryManager)
  {
    PLUGINS_CATEGORY_MANAGER = pPluginsCategoryManager;
  }

  /**
   * Retrieve {@link CategoryDefinitionService} for plugin uuid
   *
   * @param pPluginUUID
   *          the plugin uuid
   * @return the {@link CategoryDefinitionService}
   * @throws PluginManagerException
   */
  public static CategoryDefinitionService getCategoryDefinitionService(final UUID pPluginUUID)
      throws PluginManagerException
  {

    final PluginMetadata plugin = PLUGINS_MANAGER.getPluginMetadataByUUID(pPluginUUID.toString());
    return PLUGINS_CATEGORY_MANAGER.getCategoryService(plugin.getCategory());
  }

  /**
   * @return the {@link ForgeCfgService}
   */
  public static ForgeConfigurationService getForgeConfigurationService()
  {
    return FORGE_CONFIGURATION_SERVICE;
  }

  /**
   * Use by container to inject {@link ForgeCfgService}
   *
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    FORGE_CONFIGURATION_SERVICE = pForgeConfigurationService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId()
  {
    return getPortalModuleId().getId();
  }

  /**
   * Return the {@link PortalModuleId} associated to the current module
   *
   * @return {@link PortalModuleId}
   */
  public static PortalModuleId getPortalModuleId()
  {
    return PortalModuleId.APPLICATIONS;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalComponent createComponent(final PortalContext pPortalContext)
  {
    return new MainComponent(pPortalContext);
  }

}
