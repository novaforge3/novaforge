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

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.dashboard.event.WidgetClosedEvent;
import org.novaforge.forge.ui.portal.event.PortalModuleUnRegisteredEvent;
import org.novaforge.forge.ui.portal.event.UIClosedEvent;

import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public abstract class AbstractWidgetEventHandler
{
  /**
   * The {@link WidgetContext}
   */
  private WidgetContext widgetContext;

  /**
   * Default constructor.
   * 
   * @param pWidgetContext
   *          the initial context
   */
  public AbstractWidgetEventHandler(final WidgetContext pWidgetContext)
  {
    widgetContext = pWidgetContext;
    if (widgetContext.getEventBus() != null)
    {
      widgetContext.getEventBus().subscribe(this);
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
    if ((widgetContext != null) && (widgetContext.getEventBus() != null))
    {
      widgetContext.getEventBus().unsubscribe(this);
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
    final PortalModuleId eventId = PortalModuleId.getFromId(pEvent.getPortalModuleId());
    if (PortalModuleId.DASHBOARD.equals(eventId))
    {
      unregisterReferences();
    }
  }

  /**
   * Method call when a {@link WidgetClosedEvent} is received
   * 
   * @param pEvent
   *          the event received
   */
  @Handler
  public void onWidgetClosedEvent(final WidgetClosedEvent pEvent)
  {
    if ((getWidgetUUID() != null) && (getWidgetUUID().equals(pEvent.getWidgetUUID())))
    {
      unregisterReferences();
    }
  }

  /**
   * Return the widget uuid attached to this component
   *
   * @return widget uuid as {@link UUID}
   */
  protected UUID getWidgetUUID()
  {
    return widgetContext.getId();
  }

  /**
   * Return {@link WidgetContext} currently used by this presenter
   *
   * @return widgetContext
   */
  public WidgetContext getWidgetContext()
  {
    return widgetContext;
  }

  /**
   * Sets {@link WidgetContext} currently used by this presenter
   *
   * @param pWidgetContext
   *          the new context
   */
  protected void setWidgetContext(final WidgetContext pWidgetContext)
  {
    widgetContext = pWidgetContext;
  }

  /**
   * Return {@link MBassador} event bus currently used by this presenter
   *
   * @return eventbus
   */
  protected MBassador<PortalEvent> getEventBus()
  {
    return widgetContext.getEventBus();
  }

}
