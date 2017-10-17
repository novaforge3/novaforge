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
package org.novaforge.forge.ui.dashboard.internal.client.tab.content.event;

import com.vaadin.ui.Component;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.component.PortalLayoutArea;
import org.vaadin.addons.portallayout.portlet.Portlet;

/**
 * @author Guillaume Lamirand
 */
public class PortletMoveEvent extends Component.Event
{

  public static final java.lang.reflect.Method PORTLET_MOVED;
  /**
   *
   */
  private static final long                    serialVersionUID = 7498409113955664165L;

  static
  {
    try
    {
      PORTLET_MOVED = Listener.class.getDeclaredMethod("portletMoved", PortletMoveEvent.class);
    }
    catch (final java.lang.NoSuchMethodException e)
    {
      throw new java.lang.RuntimeException(e);
    }
  }

  private final Portlet portlet;
  private final int     index;
  public PortletMoveEvent(final PortalLayoutArea pSource, final Portlet pPortlet, final int pIndex)
  {
    super(pSource);
    portlet = pPortlet;
    index = pIndex;
  }

  public PortalLayoutArea getPortalLayoutArea()
  {
    return (PortalLayoutArea) getSource();
  }

  public Portlet getPortlet()
  {
    return portlet;
  }

  public int getIndex()
  {
    return index;
  }

  public interface Listener
  {
    void portletMoved(PortletMoveEvent event);
  }

}
