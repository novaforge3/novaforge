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
package org.novaforge.forge.ui.dashboard.internal.client.tab.content;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import org.novaforge.forge.dashboard.xml.Area;
import org.novaforge.forge.dashboard.xml.Layout;
import org.novaforge.forge.dashboard.xml.Position;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.component.PortalLayoutArea;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public class ContentViewImpl extends CssLayout implements ContentView
{

  /**
   * Serial version id
   */
  private static final long                    serialVersionUID  = 3529814710722435066L;
  private final List<PortalLayoutArea>         portalLayoutAreas = new ArrayList<PortalLayoutArea>();
  private final Map<Integer, PortalLayoutArea> areasIdMapping    = new HashMap<Integer, PortalLayoutArea>();
  private DeleteConfirmWindow widgetDeleteWindow;

  public ContentViewImpl()
  {
    // Init sub window
    initConfirmDeleteWindow();
  }

  /**
   * Initialize the window displayed when deleting a tab
   */
  private void initConfirmDeleteWindow()
  {
    widgetDeleteWindow = new DeleteConfirmWindow(Messages.DASHBOARD_WIDGET_DELETE_CONFIRMLABEL);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    final PortalMessages portalMessages = DashboardModule.getPortalMessages();
    // Delete window
    widgetDeleteWindow.setCaption(portalMessages.getMessage(pLocale, Messages.DASHBOARD_WIDGET_DELETE_TITLE));

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshGrid(final Layout pLayout)
  {
    portalLayoutAreas.clear();
    areasIdMapping.clear();
    removeAllComponents();
    final GridLayout grid = new GridLayout(pLayout.getColumn(), pLayout.getRow());
    grid.setWidth(100, Unit.PERCENTAGE);
    grid.setMargin(true);
    grid.setSpacing(true);
    for (final Area area : pLayout.getArea())
    {
      final Position top = area.getTopLeft();
      final Position bottom = area.getBottomRight();
      final PortalLayoutArea portalLayoutArea = new PortalLayoutArea(area.getId());
      grid.addComponent(portalLayoutArea, top.getColumn() - 1, top.getRow() - 1, bottom.getColumn() - 1,
          bottom.getRow() - 1);
      portalLayoutAreas.add(portalLayoutArea);
      areasIdMapping.put(area.getId(), portalLayoutArea);
    }
    addComponent(grid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalLayoutArea getPortalLayoutArea(final int pBoxId)
  {
    return areasIdMapping.get(pBoxId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PortalLayoutArea> getPortalLayoutAreas()
  {
    return portalLayoutAreas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeleteConfirmWindow getWidgetDeleteWindow()
  {
    return widgetDeleteWindow;
  }

}
