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
package org.novaforge.forge.ui.portal.client.modules;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;
import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.portal.event.PortalModuleUnRegisteredEvent;
import org.novaforge.forge.ui.portal.event.PortalPresenterClosedEvent;
import org.novaforge.forge.ui.portal.event.UIClosedEvent;

import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public abstract class AbstractPortalEventHandler
{
  /**
   * The {@link PortalContext}
   */
  private final PortalContext portalContext;

  /**
   * Default constructor.
   *
   * @param pPortalContext
   *          the initial context
   */
  public AbstractPortalEventHandler(final PortalContext pPortalContext)
  {
    portalContext = pPortalContext;
    if (portalContext.getEventBus() != null)
    {
      portalContext.getEventBus().subscribe(this);
    }

  }

  /**
   * Method call when a {@link UIClosedEvent} is received
   *
   * @param pEvent
   *          the event received
   */
  @Handler
  public void onUIClosedEvent(final UIClosedEvent pEvent)
  {
    unregisterReferences();
  }

  /**
   * This method will clear and unregister all references to this presenter and its children
   */
  public void unregisterReferences()
  {
    if (portalContext.getEventBus() != null)
    {
      portalContext.getEventBus().unsubscribe(this);
    }
  }

  /**
   * Method call when a {@link PortalModuleUnRegisteredEvent} is received
   *
   * @param pEvent
   *          the event received
   */
  @Handler
  public void onPortalModuleUnRegisteredEvent(final PortalModuleUnRegisteredEvent pEvent)
  {
    final PortalModuleId eventModuleId = PortalModuleId.getFromId(pEvent.getPortalModuleId());
    if ((getModuleId() != null) && (getModuleId().equals(eventModuleId)))
    {
      unregisterReferences();
    }
  }

  /**
   * Return the {@link PortalModuleId} attached to this component
   *
   * @return {@link PortalModuleId}
   */
  protected abstract PortalModuleId getModuleId();

  /**
   * Method call when a {@link PortalPresenterClosedEvent} is received
   *
   * @param pEvent
   *          the event received
   */
  @Handler
  public void onPortalPresenterClosed(final PortalPresenterClosedEvent pEvent)
  {
    if ((portalContext.getEventBus() != null) && (getUuid().equals(pEvent.getUuid())))
    {
      unregisterReferences();
    }
  }

  /**
   * Return {@link UUID} currently used to defined the unique identifier of this presenter
   *
   * @return uuid
   */
  protected UUID getUuid()
  {
    return portalContext.getUuid();
  }

  /**
   * Return {@link MBassador} event bus currently used by this presenter
   * 
   * @return eventbus
   */
  protected MBassador<PortalEvent> getEventBus()
  {
    return getPortalContext().getEventBus();
  }

  /**
   * Return {@link PortalContext} currently d by this presenter
   *
   * @return portalContext
   */
  public PortalContext getPortalContext()
  {
    return portalContext;
  }

}
