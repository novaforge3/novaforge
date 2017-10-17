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

import com.google.common.base.Strings;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.dashboard.xml.Layout;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Guillaume Lamirand
 */
public class LayoutsHeaderComponent extends VerticalLayout
{
  /**
   * 
   */
  private static final long        serialVersionUID = 4105600163784494516L;
  private final List<LayoutMapper> layoutsMapping;
  private HorizontalLayout         areasButtonLayout;
  private Label                    areaLabel;
  private Panel                    areasGridComponent;

  public LayoutsHeaderComponent()
  {
    // Init mapping map
    layoutsMapping = new ArrayList<LayoutMapper>();

    // Init boxes layout
    final Component areas = initAreas();
    addComponent(areas);
    setComponentAlignment(areas, Alignment.MIDDLE_LEFT);

    // Init grid layout
    final Component layout = initLayout();
    addComponent(layout);
    setComponentAlignment(layout, Alignment.MIDDLE_LEFT);

    // Init global layout
    setSpacing(true);
  }

  /**
   * Initialize a {@link Component} containing buttons to choose area number
   * 
   * @return {@link Component} built
   */
  private Component initAreas()
  {
    final HorizontalLayout layout = new HorizontalLayout();
    layout.setSpacing(true);
    layout.addStyleName(NovaForge.DASHBOARD_BOXES);
    areasButtonLayout = new HorizontalLayout();
    areasButtonLayout.setSpacing(true);
    areaLabel = new Label();

    layout.addComponent(areasButtonLayout);
    layout.setComponentAlignment(areasButtonLayout, Alignment.MIDDLE_LEFT);
    layout.addComponent(areaLabel);
    layout.setComponentAlignment(areaLabel, Alignment.MIDDLE_LEFT);
    return layout;
  }

  /**
   * Initialize a {@link Component} containing dashboard layout icon
   * 
   * @return {@link Component} built
   */
  private Component initLayout()
  {
    areasGridComponent = new Panel();
    areasGridComponent.setStyleName(Reindeer.PANEL_LIGHT);
    return areasGridComponent;

  }

  /**
   * Refresh this component with the given list. It will clear and redraw full component
   * 
   * @param pLayouts
   *          the list of {@link Layout}
   */
  public void refresh(final List<Layout> pLayouts)
  {
    layoutsMapping.clear();
    areasButtonLayout.removeAllComponents();

    for (final Layout layout : pLayouts)
    {
      final int currentBoxes = layout.getArea().size();
      LayoutMapper layoutMapping = getLayoutMapper(currentBoxes);
      if (layoutMapping == null)
      {
        layoutMapping = new LayoutMapper(currentBoxes);
        layoutsMapping.add(layoutMapping);
      }
      areasButtonLayout.addComponent(layoutMapping.getButton());
      layoutMapping.addLayout(layout);
    }

  }

  /**
   * Returns the {@link LayoutMapper} associated to the boxes number given
   *
   * @param pBoxesNumber
   *          boxes number
   * @return {@link LayoutMapper} found or <code>null</code>
   */
  public LayoutMapper getLayoutMapper(final int pBoxesNumber)
  {
    LayoutMapper returnLayout = null;
    for (final LayoutMapper layoutMapping : layoutsMapping)
    {
      if (layoutMapping.getBoxes() == pBoxesNumber)
      {
        returnLayout = layoutMapping;
        break;
      }
    }
    return returnLayout;
  }

  /**
   * Returns the {@link List} of {@link LayoutMapper} registered
   *
   * @return {@link List} of {@link LayoutMapper}
   */
  public List<LayoutMapper> getLayoutsMapper()
  {
    return layoutsMapping;
  }

  /**
   * Select the given boxe and layout
   * 
   * @param pBoxesNumber
   *          the boxes number choosen
   * @param pLayoutKey
   *          the layout key to select
   */
  public void setSelectLayout(final int pBoxesNumber, final String pLayoutKey)
  {
    final LayoutMapper layoutMapper = getLayoutMapper(pBoxesNumber);
    if (layoutMapper != null)
    {
      for (final LayoutMapper layoutMapping : layoutsMapping)
      {
        layoutMapping.getButton().removeStyleName(NovaForge.BUTTON_PRIMARY);
        final Set<Entry<String, GridLayout>> entrySet = layoutMapping.getKeyGridMapping().entrySet();
        for (final Entry<String, GridLayout> gridLayout : entrySet)
        {
          gridLayout.getValue().removeStyleName(NovaForge.SELECTED);
        }
      }

      layoutMapper.getButton().addStyleName(NovaForge.BUTTON_PRIMARY);
      areasGridComponent.setContent(layoutMapper.getComponent());
      if (Strings.isNullOrEmpty(pLayoutKey))
      {
        GridLayout firstGrid = layoutMapper.getKeyGridMapping().values().iterator().next();
        firstGrid.addStyleName(NovaForge.SELECTED);
      }
      else
      {
        layoutMapper.getKeyGridMapping().get(pLayoutKey).addStyleName(NovaForge.SELECTED);
      }
    }

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
   * Refresh the labels which new locale given
   * 
   * @param pLocale
   *          the new locale
   */
  public void refreshLocale(final Locale pLocale)
  {
    final PortalMessages portalMessages = DashboardModule.getPortalMessages();
    areaLabel.setValue(portalMessages.getMessage(pLocale, Messages.DASHBOARD_TAB_LAYOUT_BOXES));
  }

}
