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

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.event.HasPortletDropListeners;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.event.PortletMoveEvent;
import org.vaadin.addons.portallayout.event.PortletCloseEvent;
import org.vaadin.addons.portallayout.gwt.shared.portal.rpc.StackPortalRpc;
import org.vaadin.addons.portallayout.portal.StackPortalLayout;
import org.vaadin.addons.portallayout.portlet.Portlet;

import java.util.LinkedList;
import java.util.List;

/**
 * This class defined a box area
 * 
 * @author Guillaume Lamirand
 */
public class PortalLayoutArea extends StackPortalLayout implements HasPortletDropListeners
{

  /**
   * Serial version
   */
  private static final long serialVersionUID = -147946772845169180L;
  /**
   * Contains the area id of this {@link PortalLayoutArea}
   */
  private final int         areaId;

  /**
   * Default constructor
   * 
   * @param pAreaId
   *          the area id describing this layout
   */
  public PortalLayoutArea(final int pAreaId)
  {
    super();
    areaId = pAreaId;
    setSizeFull();
    setMargin(true);
    setSpacing(true);
    registerRpc(new StackPortalRpc()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -8293134038028648120L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void removePortlet(final Connector pPortletComponent)
      {
        PortalLayoutArea.this.removePortlet((Component) pPortletComponent);
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void insertPortletAt(final Connector pPortletComponent, final int pIndex)
      {
        if (pIndex >= 0)
        {
          final Portlet portlet = addPortletAt((Component) pPortletComponent, pIndex);
          fireEvent(new PortletMoveEvent(PortalLayoutArea.this, portlet, pIndex));
        }
      }

    });
  }

  /**
   * Wraps the provided component into a {@link Portlet} instance or returns the existing {@link Portlet}. The
   * {@link Portlet} is inserted or moved to the correct index.
   *
   * @param pComponent
   *          Component to be wrapped into a {@link Portlet}.
   * @param pIndex
   *          the index of the {@link Portlet}
   * @return created {@link Portlet}.
   */
  public Portlet addPortletAt(final Component pComponent, final int pIndex)
  {
    final Portlet portlet = getOrCreatePortletForComponent(pComponent);
    final List<Connector> portlets = getState().portlets();
    if ((getState().portlets().indexOf(portlet) != pIndex) && (getState().portlets().size() > pIndex))
    {
      if (portlets.contains(portlet))
      {
        portlets.remove(portlet);
      }
      portlets.add(pIndex, portlet);
    }
    return portlet;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void closePortlet(final Portlet portlet)
  {
    fireEvent(new PortletCloseEvent(this, portlet));
  }

  /**
   * Return the list of {@link Portlet} registered
   * 
   * @return the list of {@link Portlet} registered
   */
  public List<Portlet> getPortlets()
  {
    final List<Portlet> returnPortlets = new LinkedList<Portlet>();
    final List<Connector> portlets = getState().portlets();
    for (final Connector connector : portlets)
    {
      returnPortlets.add((Portlet) connector);
    }
    return returnPortlets;
  }

  /**
   * Return the index of {@link Portlet} given
   * 
   * @return the index of {@link Portlet} given
   */
  public int indexOf(final Portlet pPortlet)
  {
    return getState().portlets().indexOf(pPortlet);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPortletMoveListener(final PortletMoveEvent.Listener pListener)
  {
    addListener("portletMoveEvent", PortletMoveEvent.class, pListener, PortletMoveEvent.PORTLET_MOVED);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePortletMoveListener(final PortletMoveEvent.Listener pListener)
  {
    removeListener(PortletMoveEvent.class, pListener, PortletMoveEvent.PORTLET_MOVED);

  }

  /**
   * Returns the area id of this {@link PortalLayoutArea}
   * 
   * @return the areaId
   */
  public int getAreaId()
  {
    return areaId;
  }
}
