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
package org.novaforge.forge.portal.internal.services;

import net.engio.mbassy.bus.MBassador;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.portal.internal.models.PortalContextImpl;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalModuleListener;
import org.novaforge.forge.portal.services.PortalModuleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Implementation of {@link PortalModuleService}
 *
 * @author Guillaume Lamirand
 */
public class PortalModuleServiceImpl implements PortalModuleService
{

  /**
   * Logger component
   */
  private static final Log LOGGER = LogFactory.getLog(PortalModuleServiceImpl.class);
  /**
   * Contains all {@link PortalModule} registered
   */
  private Map<String, PortalModule>  modules;
  /**
   * Contains all {@link PortalModuleListener} registered
   */
  private List<PortalModuleListener> listeners;

  /**
   * Call on when the container initialize this component
   */
  public synchronized void init()
  {
    modules = new HashMap<>();
    listeners = new ArrayList<>();
  }

  /**
   * Call on when the container destroy this component
   */
  public synchronized void destroy()
  {
    modules.clear();
    listeners.clear();
  }

  /**
   * This will method will be called by container when a {@link PortalModule} appeared
   *
   * @param pModule
   *     the module which appeared
   * @param pProperties
   *     service properties
   */
  public synchronized void registerModule(final PortalModule pModule, final Map<String, Object> pProperties)
  {
    if ((modules != null) && (pModule != null) && (pProperties.containsKey(PortalModule.MODULE_ID)))
    {
      final String moduleId = pProperties.get(PortalModule.MODULE_ID).toString();
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("A new module has been registered with [id=%s]", moduleId));
      }
      modules.put(moduleId, pModule);
      for (final PortalModuleListener listener : listeners)
      {
        listener.moduleRegistered(pModule);
      }
    }
  }

  /**
   * This will method will be called by container when a {@link PortalModule} disapeared
   *
   * @param pModule
   *     the module which disapeared
   * @param pProperties
   *     service properties
   */
  public synchronized void unregisterModule(final PortalModule pModule, final Map<String, Object> pProperties)
  {
    if ((modules != null) && (pModule != null) && (pProperties.containsKey(PortalModule.MODULE_ID)))
    {
      final String moduleId = pProperties.get(PortalModule.MODULE_ID).toString();
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("A module has been unregistered with [id=%s]", moduleId));
      }
      if (modules.containsKey(moduleId))
      {
        modules.remove(moduleId);
        for (final PortalModuleListener listener : listeners)
        {
          listener.moduleUnregistered(moduleId);
        }
      }
    }
  }

  @Override
  public PortalModule getModule(final String pModuleId)
  {
    PortalModule returned = null;
    if (modules != null)
    {
      if ((pModuleId != null) && (!"".equals(pModuleId)) && (modules.containsKey(pModuleId)))
      {
        returned = modules.get(pModuleId);
      }
      if (returned == null)
      {
        LOGGER.error(String
                         .format("Cannot find any module with the id given [id=%s], the default one will be returned.",
                                 pModuleId));

        if (modules.containsKey(PortalModuleId.UNAVAILABLE.getId()))
        {
          returned = modules.get(PortalModuleId.UNAVAILABLE.getId());
        }
      }
    }
    return returned;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PortalModule> getModules()
  {
    return new ArrayList<>(modules.values());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void addListener(final PortalModuleListener pListener)
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
  public synchronized boolean existListener(final PortalModuleListener pListener)
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
  public synchronized void removeListener(final PortalModuleListener pListener)
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
  public PortalContext buildContext(final MBassador<PortalEvent> pEventBus, final Locale pLocale,
                                    final Map<PortalContext.KEY, String> pAttributes)
  {
    return new PortalContextImpl(pEventBus, pLocale, pAttributes);
  }

}