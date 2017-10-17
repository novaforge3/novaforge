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
package org.novaforge.forge.core.plugins.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.initialization.services.ForgeInitializationListener;
import org.novaforge.forge.core.initialization.services.ForgeInitializationService;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.plugins.services.PluginsManager;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public class PluginServiceListener implements ForgeInitializationListener
{

  /**
   * Logger component
   */
  private static final Log LOGGER = LogFactory.getLog(PluginServiceListener.class);
  /**
   * {@link PluginsManager} injected by container
   */
  private PluginsManager             pluginsManager;
  /**
   * {@link ForgeInitializationService} injected by container
   */
  private ForgeInitializationService forgeInitializationService;
  /**
   * Contains all {@link PluginService} registered
   */
  private List<PluginService>        pluginServices;

  /**
   * Call on when the container initialize this component
   */
  public void init()
  {
    pluginServices = new ArrayList<PluginService>();
  }

  /**
   * Call on when the container destroy this component
   */
  public void destroy()
  {
    pluginServices = null;
  }

  /**
   * This will method will be called by container when a {@link PluginService} appeared
   *
   * @param pPluginService
   *          the pluginService which appeared
   */
  public synchronized void addService(final PluginService pPluginService)
  {

    try
    {
      if (pPluginService != null)
      {
        final UUID uuid = pPluginService.getServiceUUID();
        if (uuid != null)
        {
          LOGGER.info(String.format("PluginServiceListener has detected a new plugin with [id=%s]", uuid));
          if (forgeInitializationService.isInitialized())
          {
            addPluginService(pPluginService);
          }
          else
          {
            pluginServices.add(pPluginService);
          }
        }
        else
        {
          LOGGER.error("Plugin UUID shouldn't be null or empty");
        }

      }
    }
    catch (final Exception e)
    {
      // This catch is necessary to avoid crashing WBP handler
      LOGGER.error(String.format("A problem appeared while detecting the arrival of a plugin [service=%s]",
                                 pPluginService), e);
    }
  }

  private void addPluginService(final PluginService pPluginService)
  {
    final UUID uuid = pPluginService.getServiceUUID();

    if (uuid != null)
    {
      try
      {
        pluginsManager.registerPlugin(pPluginService);
        pluginsManager.configureToolInstance(pPluginService, null);
      }
      catch (final PluginManagerException e)
      {
        LOGGER.error(String.format("Problem while register plugin with [uuid=%s]", uuid), e);
      }
    }
    else
    {
      LOGGER.error("Plugin UUID shouldn't be null or empty");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void onInitializationSuccessfull()
  {
    for (final PluginService pluginService : pluginServices)
    {
      addPluginService(pluginService);
    }
    pluginServices.clear();
  }

  /**
   * This will method will be called by container when a {@link PluginService} disapeared
   * 
   * @param pPluginService
   *          the pluginService which disapeared
   */
  public synchronized void removeService(final PluginService pPluginService)
  {
    try
    {
      if (pPluginService != null)
      {
        final UUID uuid = pPluginService.getServiceUUID();
        if (uuid != null)
        {
          LOGGER.info(String.format(
              "PluginServiceListener has detected the departure of a plugin with [id=%s]", uuid));
          try
          {
            pluginsManager.updatePluginAvailability(uuid, false);
          }
          catch (final PluginManagerException e)
          {
            if (e.getCause() instanceof NoResultException)
            {
              LOGGER
                  .warn(String
                      .format(
                          "The departure of a plugin has been detected but is not existing in the database [uuid=%s]",
                          uuid.toString()));
            }
            else
            {
              LOGGER.error(String.format(
                  "Problem while setting the unavailability of a plugin with [uuid=%s], the plugin wi",
                  uuid.toString()), e);
            }
          }
        }
      }
    }
    catch (final Exception e)
    {
      // This catch is necessary to avoid crashing WBP handler
      LOGGER.error(String.format("A problem appeared while detecting the departure of a plugin [service=%s]",
          pPluginService), e);
    }
    finally
    {
      if (pluginServices.contains(pPluginService))
      {
        pluginServices.remove(pPluginService);
      }
    }

  }

  /**
   * Bind method used by the container to inject {@link PluginsManager} service.
   * 
   * @param pPluginsManager
   *          the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  /**
   * Bind method used by the container to inject {@link ForgeInitializationService} service.
   * 
   * @param pForgeInitializationService
   *          the forgeInitializationService to set
   */
  public void setForgeInitializationService(final ForgeInitializationService pForgeInitializationService)
  {
    forgeInitializationService = pForgeInitializationService;
  }

}
