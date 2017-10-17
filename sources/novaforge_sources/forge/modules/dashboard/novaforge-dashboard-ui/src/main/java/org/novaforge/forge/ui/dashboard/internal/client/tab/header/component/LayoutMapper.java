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
package org.novaforge.forge.ui.dashboard.internal.client.tab.header.component;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import org.novaforge.forge.dashboard.xml.Area;
import org.novaforge.forge.dashboard.xml.Layout;
import org.novaforge.forge.dashboard.xml.Position;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public class LayoutMapper
{
  /**
   * The {@link Button} used to display the full layouts
   */
  private final Button                  button;
  /**
   * {@link HorizontalLayout} containing all layout for this areas number
   */
  private final HorizontalLayout        component      = new HorizontalLayout();
  /**
   * This map contains mapping between {@link Layout#getKey()} and its {@link GridLayout}. Its has to keep the
   * order determined by the insertion order.
   */
  private final Map<String, GridLayout> keyGridMapping = new LinkedHashMap<>();
  /**
   * Number of areas
   */
  private final int areaCount;

  /**
   * Default constructor
   *
   * @param pAreaNumber
   *          represents the number of area
   */
  public LayoutMapper(final int pAreaNumber)
  {
    areaCount = pAreaNumber;
    button = new Button(String.valueOf(areaCount));
    component.setSpacing(true);
  }

  /**
   * Add given layout to this mapper
   *
   * @param pLayout
   *          the {@link Layout} to add
   */
  public void addLayout(final Layout pLayout)
  {
    if ((pLayout != null) && (pLayout.getArea().size() == areaCount))
    {
      final GridLayout generateIcon = createGrid(pLayout);
      keyGridMapping.put(pLayout.getKey(), generateIcon);
      component.addComponent(generateIcon);
    }
  }

  /**
   * Generate a small grid layout representing the dashboard layout
   *
   * @param pLayout
   *          the dashboard layout to draw
   * @return {@link GridLayout} built
   */
  private GridLayout createGrid(final Layout pLayout)
  {
    final GridLayout grid = new GridLayout(pLayout.getColumn(), pLayout.getRow());
    grid.setWidth(60, Unit.PIXELS);
    grid.setHeight(60, Unit.PIXELS);
    grid.addStyleName(NovaForge.DASHBOARD_GRID_LAYOUT);
    for (final Area area : pLayout.getArea())
    {
      final Position top = area.getTopLeft();
      final Position bottom = area.getBottomRight();
      final CssLayout areaContainer = new CssLayout();
      areaContainer.setSizeFull();
      areaContainer.setStyleName(NovaForge.DASHBOARD_BOX_CONTAINER);
      final CssLayout areaLayout = new CssLayout();
      areaLayout.setSizeFull();
      areaContainer.addComponent(areaLayout);
      grid.addComponent(areaContainer, top.getColumn() - 1, top.getRow() - 1, bottom.getColumn() - 1,
                        bottom.getRow() - 1);

    }
    return grid;
  }

  /**
   * @return the button
   */
  public Button getButton()
  {
    return button;
  }

  /**
   * @return the boxes
   */
  public int getBoxes()
  {
    return areaCount;
  }

  /**
   * @return the component
   */
  public HorizontalLayout getComponent()
  {
    return component;
  }

  /**
   * @return the keyGridMapping
   */
  public Map<String, GridLayout> getKeyGridMapping()
  {
    return keyGridMapping;
  }
}
