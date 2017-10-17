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
package org.novaforge.forge.ui.dashboard.internal.client.tab.content.component;

import org.novaforge.forge.dashboard.model.Widget;
import org.novaforge.forge.dashboard.model.WidgetDataComponent;
import org.vaadin.addons.portallayout.portlet.Portlet;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public class WidgetPortletMapping
{
  private final List<WidgetPortlet> portletMapping = new ArrayList<WidgetPortlet>();

  /**
   * This method will try to retrieve an existing {@link WidgetPortlet} from {@link Widget#getUUID()} and
   * update its {@link WidgetDataComponent}, if there are none, a new one is created.
   *
   * @param pWidget
   *          the {@link Widget}
   * @param pWidgetDataComponent
   *          the {@link WidgetDataComponent}
   * @return {@link WidgetPortlet} found or newly created
   */
  public WidgetPortlet addWidgetPortlet(final Widget pWidget, final WidgetDataComponent pWidgetDataComponent)
  {
    WidgetPortlet widgetPortlet = get(pWidget.getUUID());
    if (widgetPortlet != null)
    {
      widgetPortlet.setWidgetDataComponent(pWidgetDataComponent);
    }
    else
    {
      widgetPortlet = new WidgetPortlet(pWidget, pWidgetDataComponent);
      portletMapping.add(widgetPortlet);
    }
    return widgetPortlet;
  }

  public WidgetPortlet get(final UUID pUUID)
  {
    WidgetPortlet widgetPortletFound = null;
    if (pUUID != null)
    {
      for (final WidgetPortlet widgetPortlet : portletMapping)
      {
        if (pUUID.equals(widgetPortlet.getUUID()))
        {
          widgetPortletFound = widgetPortlet;
          break;
        }

      }
    }
    return widgetPortletFound;
  }

  public void clear()
  {
    portletMapping.clear();
  }

  public boolean exist(final UUID pUUID)
  {
    boolean isExisting = false;
    if (pUUID != null)
    {
      for (final WidgetPortlet widgetPortlet : portletMapping)
      {
        if (pUUID.equals(widgetPortlet.getUUID()))
        {
          isExisting = true;
          break;
        }

      }
    }
    return isExisting;
  }

  public WidgetPortlet get(final Portlet pPortlet)
  {
    WidgetPortlet widgetPortletFound = null;
    if (pPortlet != null)
    {
      for (final WidgetPortlet widgetPortlet : portletMapping)
      {
        if (pPortlet.equals(widgetPortlet.getPortlet()))
        {
          widgetPortletFound = widgetPortlet;
          break;
        }

      }
    }
    return widgetPortletFound;
  }

  public List<WidgetPortlet> gets(final String pWidgetKey)
  {
    final List<WidgetPortlet> widgetPortletFound = new ArrayList<WidgetPortlet>();
    if ((pWidgetKey != null) && (!"".equals(pWidgetKey)))
    {
      UUID widgetKey = UUID.fromString(pWidgetKey);
      for (final WidgetPortlet widgetPortlet : portletMapping)
      {
        if ((widgetPortlet.getWidget() != null) && (widgetKey.equals(widgetPortlet.getWidget().getUUID())))
        {
          widgetPortletFound.add(widgetPortlet);
        }
      }
    }
    return widgetPortletFound;
  }

  public List<WidgetPortlet> gets()
  {
    return portletMapping;
  }

  public class WidgetPortlet
  {
    private final Widget              widget;
    private       Portlet             portlet;
    private       WidgetDataComponent widgetDataComponent;

    private WidgetPortlet(final Widget pWidget, final WidgetDataComponent pWidgetDataComponent)
    {
      widget = pWidget;
      widgetDataComponent = pWidgetDataComponent;

    }

    /**
     * @return the widgetDataComponent
     */
    public WidgetDataComponent getWidgetDataComponent()
    {
      return widgetDataComponent;
    }

    /**
     * @param widgetDataComponent
     *     the widgetDataComponent to set
     */
    public void setWidgetDataComponent(final WidgetDataComponent widgetDataComponent)
    {
      this.widgetDataComponent = widgetDataComponent;
    }

    /**
     * @return the portlet
     */
    public Portlet getPortlet()
    {
      return portlet;
    }

    /**
     * @param portlet
     *     the portlet to set
     */
    public void setPortlet(final Portlet portlet)
    {
      this.portlet = portlet;
    }

    /**
     * @return the widgetUUID
     */
    public UUID getUUID()
    {
      return getWidget().getUUID();
    }

    /**
     * @return the widget
     */
    public Widget getWidget()
    {
      return widget;
    }

  }

}
