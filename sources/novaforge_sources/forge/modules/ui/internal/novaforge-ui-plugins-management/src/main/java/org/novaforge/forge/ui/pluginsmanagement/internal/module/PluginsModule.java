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
package org.novaforge.forge.ui.pluginsmanagement.internal.module;

import org.novaforge.forge.core.organization.handlers.ApplicationHandler;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectApplicationRequestPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.plugins.services.PluginsCategoryManager;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.portal.services.PortalNavigation;
import org.novaforge.forge.portal.services.PortalService;

/**
 * @author Guillaume Lamirand
 */
public class PluginsModule implements PortalModule
{

  private static PortalNavigation                   PORTAL_NAVIGATION;
  private static PortalService                      PORTAL_SERVICE;
  private static PortalMessages                     PORTAL_MESSAGES;
  private static PluginsManager                     PLUGINS_MANAGER;
  private static PluginsCategoryManager             PLUGINS_CATEGORY_MANAGER;
  private static ApplicationHandler                 APPLICATION_HANDLER;
  private static ProjectApplicationRequestPresenter PROJECT_APPLICATIONREQUEST_PRESENTER;
  private static UserPresenter                      USER_PRESENTER;
  private static ProjectPresenter                   PROJECT_PRESENTER;
  private static ApplicationPresenter               APPLICATION_PRESENTER;

  /**
   * @return the {@link PortalService}
   */
  public static PortalService getPortalService()
  {
    return PORTAL_SERVICE;
  }

  /**
   * Use by container to inject {@link PortalService}
   *
   * @param pPortalService
   *          the portalService to set
   */
  public void setPortalService(final PortalService pPortalService)
  {
    PORTAL_SERVICE = pPortalService;
  }

  /**
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
   * @return the {@link PortalNavigation}
   */
  public static PortalNavigation getPortalNavigation()
  {
    return PORTAL_NAVIGATION;
  }

  /**
   * Use by container to inject {@link PortalNavigation}
   *
   * @param pPortalNavigation
   *          the portalNavigation to set
   */
  public void setPortalNavigation(final PortalNavigation pPortalNavigation)
  {
    PORTAL_NAVIGATION = pPortalNavigation;
  }

  /**
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
   * @return the {@link ApplicationHandler}
   */
  public static ApplicationHandler getApplicationHandler()
  {
    return APPLICATION_HANDLER;
  }

  /**
   * Use by container to inject {@link ApplicationHandler}
   *
   * @param pApplicationHandler
   *          the applicationHandler to set
   */
  public void setApplicationHandler(final ApplicationHandler pApplicationHandler)
  {
    APPLICATION_HANDLER = pApplicationHandler;
  }

  /**
   * @return the {@link ProjectApplicationRequestPresenter}
   */
  public static ProjectApplicationRequestPresenter getProjectApplicationRequestPresenter()
  {
    return PROJECT_APPLICATIONREQUEST_PRESENTER;
  }

  /**
   * Use by container to inject {@link ProjectApplicationRequestPresenter}
   *
   * @param pProjectApplicationRequestPresenter
   *          the projectApplicationRequestPresenter to set
   */
  public void setProjectApplicationRequestPresenter(
      final ProjectApplicationRequestPresenter pProjectApplicationRequestPresenter)
  {
    PROJECT_APPLICATIONREQUEST_PRESENTER = pProjectApplicationRequestPresenter;
  }

  /**
   * @return the {@link UserPresenter}
   */
  public static UserPresenter getUserPresenter()
  {
    return USER_PRESENTER;
  }

  /**
   * Use by container to inject {@link UserPresenter}
   *
   * @param pUserPresenter
   *          the userPresenter to set
   */
  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    USER_PRESENTER = pUserPresenter;
  }

  /**
   * @return the {@link ProjectPresenter}
   */
  public static ProjectPresenter getProjectPresenter()
  {
    return PROJECT_PRESENTER;
  }

  /**
   * Use by container to inject {@link ProjectPresenter}
   *
   * @param pProjectPresenter
   *          the projectPresenter to set
   */
  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    PROJECT_PRESENTER = pProjectPresenter;
  }

  /**
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
    return PortalModuleId.PLUGINS_MANAGEMENT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalComponent createComponent(final PortalContext pPortalContext)
  {
    return new PluginsComponent(pPortalContext);
  }
}
