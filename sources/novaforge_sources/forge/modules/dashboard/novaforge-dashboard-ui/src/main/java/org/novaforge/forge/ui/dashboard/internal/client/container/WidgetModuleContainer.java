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
package org.novaforge.forge.ui.dashboard.internal.client.container;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.dashboard.model.Widget;
import org.novaforge.forge.dashboard.model.WidgetModule;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.dashboard.internal.client.tab.header.HeaderPresenter;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caseryj
 */
public class WidgetModuleContainer extends IndexedContainer
{

  /**
   * 
   */
  private static final long  serialVersionUID = 3712008135593969541L;
  private final List<String> widgetToIgnore   = new ArrayList<String>();

  private HeaderPresenter    presenter;

  /**
   * Default constructor. It will initialize widget item property
   * 
   * @see WidgetModuleItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public WidgetModuleContainer(final HeaderPresenter pPresenter)
  {
    super();
    presenter = pPresenter;
    initIgnoreList();
    addContainerProperty(WidgetModuleItemProperty.KEY.getPropertyId(), String.class, null);
    addContainerProperty(WidgetModuleItemProperty.NAME.getPropertyId(), String.class, null);
    addContainerProperty(WidgetModuleItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(WidgetModuleItemProperty.ICON.getPropertyId(), byte[].class, null);
    addContainerProperty(WidgetModuleItemProperty.CATEGORY.getPropertyId(), List.class, null);
    addContainerProperty(WidgetModuleItemProperty.PREVIEW.getPropertyId(), byte[].class, null);

    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  private void initIgnoreList()
  {
    widgetToIgnore.add(WidgetModule.NO_SETTINGS_KEY);
    widgetToIgnore.add(WidgetModule.UNAVAILABLE_KEY);
    widgetToIgnore.add(WidgetModule.LOADING_KEY);
  }

  /**
   * Add widget into container
   * 
   * @param {@link List}<{@link Widget}> pWidgets
   *        Widgets to add
   */
  public void setWidgetModules(final List<WidgetModule> pWidgetModules)
  {
    // removeAllItems();
    final PortalMessages portalMessages = DashboardModule.getPortalMessages();
    for (final WidgetModule widgetModule : pWidgetModules)
    {
      if (!widgetToIgnore.contains(widgetModule.getKey()))
      {
        final String itemID = widgetModule.getKey();
        addItem(itemID);
        getContainerProperty(itemID, WidgetModuleItemProperty.KEY.getPropertyId()).setValue(itemID);
        getContainerProperty(itemID, WidgetModuleItemProperty.NAME.getPropertyId()).setValue(
            portalMessages.getMessage(presenter.getCurrentLocale(), presenter.getI18NNameKey(itemID)));
        getContainerProperty(itemID, WidgetModuleItemProperty.DESCRIPTION.getPropertyId()).setValue(
            portalMessages.getMessage(presenter.getCurrentLocale(), presenter.getI18NDescKey(itemID)));
        getContainerProperty(itemID, WidgetModuleItemProperty.ICON.getPropertyId()).setValue(
            widgetModule.getIcon());
        getContainerProperty(itemID, WidgetModuleItemProperty.CATEGORY.getPropertyId()).setValue(
            widgetModule.getCategories());
        getContainerProperty(itemID, WidgetModuleItemProperty.PREVIEW.getPropertyId()).setValue(
            widgetModule.getPreview());
      }

    }
    sort(new Object[] { WidgetModuleItemProperty.KEY.getPropertyId() }, new boolean[] { true });
  }

}
