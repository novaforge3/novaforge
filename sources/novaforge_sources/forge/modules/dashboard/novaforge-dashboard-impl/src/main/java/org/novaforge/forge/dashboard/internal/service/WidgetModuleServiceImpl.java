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
package org.novaforge.forge.dashboard.internal.service;

import net.engio.mbassy.bus.MBassador;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.dashboard.internal.model.WidgetContextImpl;
import org.novaforge.forge.dashboard.model.Widget;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.dashboard.model.WidgetModule;
import org.novaforge.forge.dashboard.service.DashBoardService;
import org.novaforge.forge.dashboard.service.DataSourceFactory;
import org.novaforge.forge.dashboard.service.WidgetModuleListener;
import org.novaforge.forge.dashboard.service.WidgetModuleService;
import org.novaforge.forge.portal.events.PortalEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of {@link WidgetModuleService}
 * 
 * @author Guillaume Lamirand
 */
public class WidgetModuleServiceImpl implements WidgetModuleService
{

  /**
   * Logger component
   */
  private static final Log           LOGGER = LogFactory.getLog(WidgetModuleServiceImpl.class);
  /**
   * Contains all {@link WidgetModule} registered
   */
  private Map<String, WidgetModule>  modules;
  /**
   * Contains all {@link WidgetModuleListener} registered
   */
  private List<WidgetModuleListener> listeners;
  /**
   * Implementation of {@link DashBoardService}
   */
  private DashBoardService           dashBoardService;
  /**
   * Implementation of {@link DataSourceFactory}
   */
  private DataSourceFactory          dataSourceFactory;

  /**
   * Call on when the container initialize this component
   */
  public void init()
  {
    modules = new HashMap<String, WidgetModule>();
    listeners = new ArrayList<WidgetModuleListener>();
  }

  /**
   * This will method will be called by container when a {@link WidgetModule} appeared
   * 
   * @param pModule
   *          the module which appeared
   * @param pProperties
   *          service properties
   */
  public synchronized void registerModule(final WidgetModule pModule, final Map<String, Object> pProperties)
  {
    if ((pModule != null) && (pProperties.containsKey(WidgetModule.WIDGET_KEY)))
    {
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("A new module has been registered with [key=%s]", pModule.getKey()));
      }
      final String moduleId = pProperties.get(WidgetModule.WIDGET_KEY).toString();
      modules.put(moduleId, pModule);
      for (final WidgetModuleListener listener : listeners)
      {
        listener.moduleRegistered(pModule);
      }
    }
  }

  /**
   * This will method will be called by container when a {@link WidgetModule} disapeared
   * 
   * @param pModule
   *          the module which disapeared
   * @param pProperties
   *          service properties
   */
  public synchronized void unregisterModule(final WidgetModule pModule, final Map<String, Object> pProperties)
  {
    if ((pModule != null) && (pProperties.containsKey(WidgetModule.WIDGET_KEY)))
    {
      final String widgetKey = pProperties.get(WidgetModule.WIDGET_KEY).toString();
      if (modules.containsKey(widgetKey))
      {
        modules.remove(widgetKey);
        for (final WidgetModuleListener listener : listeners)
        {
          listener.moduleUnregistered(widgetKey);
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized WidgetModule getModule(final String pModuleKey)
  {
    WidgetModule returned = null;
    if ((pModuleKey != null) && (!"".equals(pModuleKey)) && (modules.containsKey(pModuleKey)))
    {
      returned = modules.get(pModuleKey);
    }
    if (returned == null)
    {
      LOGGER.error(String.format(
          "Cannot find any module with the id given [id=%s], the default one will be returned.", pModuleKey));

      if (modules.containsKey(WidgetModule.UNAVAILABLE_KEY))
      {
        returned = modules.get(WidgetModule.UNAVAILABLE_KEY);
      }
    }
    return returned;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized List<WidgetModule> getModules()
  {
    return new ArrayList<WidgetModule>(modules.values());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void addListener(final WidgetModuleListener pListener)
  {
    if (pListener != null)
    {
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Adding listener [%s]", pListener));
      }
      listeners.add(pListener);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized boolean existListener(final WidgetModuleListener pListener)
  {
    boolean isExist = false;
    if (pListener != null)
    {
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Checking listener [%s]", pListener));
      }
      isExist = listeners.contains(pListener);
    }
    return isExist;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void removeListener(final WidgetModuleListener pListener)
  {
    if (pListener != null)
    {
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Removing listener [%s]", pListener));
      }
      listeners.remove(pListener);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WidgetContext buildContext(final MBassador<PortalEvent> pEventBus, final Locale pLocale,
      final UUID pUUID)
  {
    final Widget widget = dashBoardService.getWidget(pUUID);
    final Map<String, List<String>> dataSource = dataSourceFactory.readDataSource(widget.getDataSource());
    return new WidgetContextImpl(pEventBus, pLocale, pUUID, dataSource, widget.getProperties());
  }

  /**
   * Used by container to inject service implementation of {@link DashBoardService}
   * 
   * @param pDashBoardService
   *          the dashBoardService to set
   */
  public void setDashBoardService(final DashBoardService pDashBoardService)
  {
    dashBoardService = pDashBoardService;
  }

  /**
   * Used by container to inject service implementation of {@link DataSourceFactory}
   * 
   * @param pDataSourceFactory
   *          the dataSourceFactory to set
   */
  public void setDataSourceFactory(final DataSourceFactory pDataSourceFactory)
  {
    dataSourceFactory = pDataSourceFactory;
  }

}