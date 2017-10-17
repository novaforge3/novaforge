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
package org.novaforge.forge.ui.dashboard.client.modules;

import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.dashboard.model.DataSourceOptions;
import org.novaforge.forge.dashboard.model.WidgetModule;
import org.novaforge.forge.dashboard.service.DataSourceFactory;
import org.novaforge.forge.portal.services.PortalMessages;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 * @param <T>
 */
public abstract class AbstractWidgetModule<T> implements WidgetModule
{

  /**
   * Reference to {@link DataSourceFactory} service injected by the container
   */
  private static DataSourceFactory          DATASOURCE_FACTORY;
  /**
   * Contains reference to {@link PortalMessages} implementation
   */
  private static PortalMessages             PORTAL_MESSAGES;
  /**
   * Contains reference to {@link ForgeIdentificationService} implementation
   */
  private static ForgeIdentificationService FORGE_IDENTIFICATION_SERVICE;
  /**
   * Contains reference to {@link AuthentificationService} implementation
   */
  private static AuthentificationService    AUTHENTIFICATION_SERVICE;
  /**
   * Contains reference to {@link PluginsManager} implementation
   */
  private static PluginsManager             PLUGINS_MANAGER;
  /**
   * Contains reference to {@link ApplicationPresenter} implementation
   */
  private static ApplicationPresenter       APPLICATION_PRESENTER;

  /**
   * Returns the {@link UUID} which identify the current forge
   *
   * @return {@link UUID} identifing the forge
   */
  public static UUID getForgeId()
  {
    return FORGE_IDENTIFICATION_SERVICE.getForgeId();
  }

  /**
   * Returns the login of the current authentificated user
   *
   * @return login of the current authentificated user
   */
  public static String getCurrentUser()
  {
    return AUTHENTIFICATION_SERVICE.getCurrentUser();
  }

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
   * Get implementation of {@link DataSourceFactory}
   *
   * @return the {@link DataSourceFactory}
   */
  public static DataSourceFactory getDataSourceFactory()
  {
    return DATASOURCE_FACTORY;
  }

  /**
   * Use by container to inject {@link DataSourceFactory}
   *
   * @param pDataSourceFactory
   *          the dataSourceFactory to set
   */
  public void setDataSourceFactory(final DataSourceFactory pDataSourceFactory)
  {
    DATASOURCE_FACTORY = pDataSourceFactory;
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
   * {@inheritDoc}
   */
  @Override
  public boolean isValidDataSource(final Map<String, List<String>> pDataSource)
  {
    boolean                 isValid           = false;
    final DataSourceOptions dataSourceOptions = getDataSourceOptions();
    if (dataSourceOptions != null)
    {
      if ((dataSourceOptions.needProjects()) && ((pDataSource != null) && (!pDataSource.isEmpty())))
      {
        final Set<String> keySet = pDataSource.keySet();
        if (((dataSourceOptions.isMultiProject()) && (keySet.size() >= 1)) || ((!dataSourceOptions.isMultiProject())
                                                                                   && (keySet.size() == 1)))
        {
          isValid = validApplications(pDataSource, dataSourceOptions);
        }
      }
      else if ((!dataSourceOptions.needProjects()) && (((pDataSource == null) || (pDataSource.isEmpty()))))
      {
        isValid = true;
      }
    }
    else
    {
      isValid = true;
    }
    return isValid;

  }

  private boolean validApplications(final Map<String, List<String>> pDataSource,
                                    final DataSourceOptions dataSourceOptions)
  {
    boolean                                isValid  = false;
    final Set<Entry<String, List<String>>> entrySet = pDataSource.entrySet();
    for (final Entry<String, List<String>> entry : entrySet)
    {
      final List<String> apps = entry.getValue();
      if ((dataSourceOptions.needApplications()) && ((apps != null) && (!apps.isEmpty())))
      {
        if (((dataSourceOptions.isMultiApplications()) && (apps.size() >= 1)) || ((!dataSourceOptions
                                                                                        .isMultiApplications())
                                                                                      && (apps.size() == 1)))
        {
          isValid = true;
        }
      }
      else if ((!dataSourceOptions.needApplications()) && (((apps == null) || (apps.isEmpty()))))
      {
        isValid = true;
      }
      else
      {
        isValid = false;
        break;
      }
    }
    return isValid;
  }

  /**
   * Use by container to inject {@link ForgeIdentificationService}
   *
   * @param pForgeIdentificationService
   *     the forgeIdentificationService to set
   */
  public void setForgeIdentificationService(final ForgeIdentificationService pForgeIdentificationService)
  {
    FORGE_IDENTIFICATION_SERVICE = pForgeIdentificationService;
  }

  /**
   * Use by container to inject {@link AuthentificationService}
   *
   * @param pAuthentificationService
   *     the authentificationService to set
   */
  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    AUTHENTIFICATION_SERVICE = pAuthentificationService;
  }
}
