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
package org.novaforge.forge.ui.dashboard.internal.module;

import org.novaforge.forge.core.organization.delegates.SecurityDelegate;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.plugins.services.PluginsCategoryManager;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.dashboard.service.DashBoardService;
import org.novaforge.forge.dashboard.service.DataSourceFactory;
import org.novaforge.forge.dashboard.service.LayoutService;
import org.novaforge.forge.dashboard.service.WidgetModuleService;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.portal.services.PortalService;
import org.novaforge.forge.ui.dashboard.internal.client.DashboardPresenter;
import org.novaforge.forge.ui.dashboard.internal.client.DashboardView;
import org.novaforge.forge.ui.dashboard.internal.client.DashboardViewImpl;

/**
 * @author Guillaume Lamirand
 */
public class DashboardModule implements PortalModule
{
  /**
   * Contains reference to {@link PortalService} implementation
   */
  private static PortalService           PORTAL_SERVICE;
  /**
   * Contains reference to {@link PortalMessages} implementation
   */
  private static PortalMessages          PORTAL_MESSAGES;
  /**
   * Contains reference to {@link AuthentificationService} implementation
   */
  private static AuthentificationService AUTHENTIFICATION_SERVICE;
  /**
   * Contains reference to {@link ProjectPresenter} implementation
   */
  private static ProjectPresenter        PROJECT_PRESENTER;
  /**
   * Contains reference to {@link ApplicationPresenter} implementation
   */
  private static ApplicationPresenter    APPLICATION_PRESENTER;
  /**
   * Contains reference to {@link DashBoardService} implementation
   */
  private static DashBoardService        DASHBOARD_SERVICE;
  /**
   * Contains reference to {@link LayoutService} implementation
   */
  private static LayoutService           LAYOUT_SERVICE;
  /**
   * Contains reference to {@link WidgetModuleService} implementation
   */
  private static WidgetModuleService     WIDGET_MODULE_SERVICE;
  /**
   * Contains reference to {@link PluginsCategoryManager} implementation
   */
  private static PluginsCategoryManager  PLUGINS_CATEGORY_MANAGER;
  /**
   * Contains reference to {@link PluginsManager} implementation
   */
  private static PluginsManager          PLUGINS_MANAGER;
  /**
   * Contains reference to {@link DataSourceFactory} implementation
   */
  private static DataSourceFactory       DATA_SOURCE_FACTORY;
  /**
   * Contains reference to {@link SecurityDelegate} implementation
   */
  private static SecurityDelegate        SECURITY_DELEGATE;

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
   * @return the {@link DashBoardService}
   */
  public static DashBoardService getDashBoardService()
  {
    return DASHBOARD_SERVICE;
  }

  /**
   * Use by container to inject {@link DashBoardService}
   *
   * @param pDashBoardService
   *          the dashBoardService to set
   */
  public void setDashBoardService(final DashBoardService pDashBoardService)
  {
    DASHBOARD_SERVICE = pDashBoardService;
  }

  /**
   * @return the {@link LayoutService}
   */
  public static LayoutService getLayoutService()
  {
    return LAYOUT_SERVICE;
  }

  /**
   * Use by container to inject {@link LayoutService}
   *
   * @param pLayoutService
   *          the layoutService to set
   */
  public void setLayoutService(final LayoutService pLayoutService)
  {
    LAYOUT_SERVICE = pLayoutService;
  }

  /**
   * @return the {@link WidgetModuleService}
   */
  public static WidgetModuleService getWidgetModuleService()
  {
    return WIDGET_MODULE_SERVICE;
  }

  /**
   * Use by container to inject {@link WidgetModuleService}
   *
   * @param pWidgetModuleService
   *          the widgetModuleService to set
   */
  public void setWidgetModuleService(final WidgetModuleService pWidgetModuleService)
  {
    WIDGET_MODULE_SERVICE = pWidgetModuleService;
  }

  /**
   * @return the {@link AuthentificationService}
   */
  public static AuthentificationService getAuthentificationService()
  {
    return AUTHENTIFICATION_SERVICE;
  }

  /**
   * Use by container to inject {@link AuthentificationService}
   *
   * @param pAuthentificationService
   *          the authentificationService to set
   */
  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    AUTHENTIFICATION_SERVICE = pAuthentificationService;
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
   *          the pluginsManager to set
   */
  public void setPluginsCategoryManager(final PluginsCategoryManager pPluginsCategoryManager)
  {
    PLUGINS_CATEGORY_MANAGER = pPluginsCategoryManager;
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
   * @return the {@link DataSourceFactory}
   */
  public static DataSourceFactory getDataSourceFactory()
  {
    return DATA_SOURCE_FACTORY;
  }

  /**
   * Use by container to inject {@link DataSourceFactory}
   *
   * @param pDataSourceFactory
   *          the dataSourceFactory to set
   */
  public void setDataSourceFactory(final DataSourceFactory pDataSourceFactory)
  {
    DATA_SOURCE_FACTORY = pDataSourceFactory;
  }

  /**
   * @return the {@link SecurityDelegate}
   */
  public static SecurityDelegate getSecurityDelegate()
  {
    return SECURITY_DELEGATE;
  }

  /**
   * Use by container to inject {@link SecurityDelegate}
   *
   * @param pSecurityDelegate
   *          the securityDelegate to set
   */
  public void setSecurityDelegate(final SecurityDelegate pSecurityDelegate)
  {
    SECURITY_DELEGATE = pSecurityDelegate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId()
  {
    return getModuleId().getId();
  }

  /**
   * Returns {@link PortalModuleId} for this module
   *
   * @return {@link PortalModuleId} for this module
   */
  public static PortalModuleId getModuleId()
  {
    return PortalModuleId.DASHBOARD;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalComponent createComponent(final PortalContext pPortalContext)
  {
    final DashboardView      view      = new DashboardViewImpl();
    return new DashboardPresenter(pPortalContext, view);
  }

}
