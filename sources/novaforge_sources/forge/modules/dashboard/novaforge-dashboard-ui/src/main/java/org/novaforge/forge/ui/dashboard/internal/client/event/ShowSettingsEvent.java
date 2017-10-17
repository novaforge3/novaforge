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
package org.novaforge.forge.ui.dashboard.internal.client.event;

import com.vaadin.server.Resource;
import org.novaforge.forge.portal.events.PortalEvent;

import java.util.UUID;

/**
 * This event is trigged when user wants to close settings view of a widget
 * 
 * @author Guillaume Lamirand
 */
public class ShowSettingsEvent implements PortalEvent
{

  /**
   * The widget uuid to set up
   */
  private final UUID     widgetUUID;
  /**
   * The tab uuid containing widget
   */
  private final UUID     tabUUID;
  private final String   widgetName;
  private final Resource icon;

  /**
   * Default constructor
   * 
   * @param pTabUUID
   *          the tab uuid
   * @param pWidgetUUID
   *          the widget uuid to set up
   * @param pWidgetName
   *          the widget name
   * @param pIcon
   *          the icon resource
   */
  public ShowSettingsEvent(final UUID pTabUUID, final UUID pWidgetUUID, final String pWidgetName,
      final Resource pIcon)
  {
    tabUUID = pTabUUID;
    widgetUUID = pWidgetUUID;
    widgetName = pWidgetName;
    icon = pIcon;

  }

  /**
   * Returns the tab uuid attached to this event
   * 
   * @return the tabUUID
   */
  public UUID getTabUUID()
  {
    return tabUUID;
  }

  /**
   * Returns the widget uuid attached to this event
   * 
   * @return the widgetUUID
   */
  public UUID getWidgetUUID()
  {
    return widgetUUID;
  }

  /**
   * Returns the widget name
   * 
   * @return the widgetName
   */
  public String getWidgetName()
  {
    return widgetName;
  }

  /**
   * Returns the widget icon
   * 
   * @return the resource
   */
  public Resource getIconResource()
  {
    return icon;
  }

}
