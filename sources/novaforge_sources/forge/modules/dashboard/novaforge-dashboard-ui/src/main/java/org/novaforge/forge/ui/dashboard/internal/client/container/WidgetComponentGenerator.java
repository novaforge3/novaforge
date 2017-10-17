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

import com.vaadin.data.Item;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.dashboard.model.WidgetModule;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.dashboard.internal.client.tab.header.HeaderPresenter;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.vaadin.addon.itemlayout.layout.AbstractItemLayout;
import org.vaadin.addon.itemlayout.layout.model.ItemGenerator;

import java.util.Locale;

/**
 * @author caseryj
 */
public class WidgetComponentGenerator implements ItemGenerator
{
  /**
   * 
   */
  private static final long     serialVersionUID = -2998921555948545299L;

  private static final int      WIDGET_ICON_SIZE = 45;

  private final HeaderPresenter presenter;

  public WidgetComponentGenerator(final HeaderPresenter pHeaderPresenter)
  {
    presenter = pHeaderPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component generateItem(final AbstractItemLayout pSource, final Object pItemId)
  {
    final Item item = pSource.getContainerDataSource().getItem(pItemId);
    final PortalMessages portalMessages = DashboardModule.getPortalMessages();
    final Locale locale = presenter.getCurrentLocale();
    final WidgetModule widgetModule = DashboardModule.getWidgetModuleService().getModule((String) pItemId);

    final VerticalLayout widgetLayout = new VerticalLayout();
    widgetLayout.setSizeUndefined();
    widgetLayout.setStyleName(NovaForge.DASHBOARD_HEADER_WIDGET_ITEM);
    widgetLayout.addStyleName(NovaForge.CURSOR_BUTTON);
    // Name Layout
    final Label widgetName = new Label();
    widgetName.setStyleName(NovaForge.DASHBOARD_HEADER_WIDGET_ITEM_TITLE);
    widgetName.addStyleName(NovaForge.CURSOR_BUTTON);
    widgetName.setValue((String) item.getItemProperty(WidgetModuleItemProperty.NAME.getPropertyId())
        .getValue());
    widgetLayout.addComponent(widgetName);
    widgetLayout.setComponentAlignment(widgetName, Alignment.TOP_CENTER);
    // Icon Layout
    final CssLayout iconLayout = new CssLayout();
    iconLayout.setStyleName(NovaForge.DASHBOARD_HEADER_WIDGET_ITEM_ICON);
    final Image widgetImage = new Image();
    widgetImage.setWidth(WIDGET_ICON_SIZE, Unit.PIXELS);
    widgetImage.setHeight(WIDGET_ICON_SIZE, Unit.PIXELS);
    widgetImage.setSource(ResourceUtils.buildImageResource(
        (byte[]) item.getItemProperty(WidgetModuleItemProperty.ICON.getPropertyId()).getValue(),
        (String) pItemId));
    widgetImage.addStyleName(NovaForge.CURSOR_BUTTON);
    iconLayout.addComponent(widgetImage);
    widgetLayout.addComponent(iconLayout);
    widgetLayout.setComponentAlignment(iconLayout, Alignment.TOP_CENTER);

    // / Add button
    final CssLayout addButton = new CssLayout();
    addButton.setWidth(26, Unit.PIXELS);
    addButton.setHeight(26, Unit.PIXELS);
    addButton.setStyleName(NovaForge.DASHBOARD_HEADER_WIDGET_ITEM_ADD_CORNER);
    addButton.setDescription(portalMessages.getMessage(locale,
        Messages.DASHBOARD_ADDWIDGET_BUTTON_DESCRIPTION,
        (String) item.getItemProperty(WidgetModuleItemProperty.NAME.getPropertyId()).getValue()));

    // Add listeners
    widgetLayout.addLayoutClickListener(new LayoutClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = -4137352758738094035L;

      @Override
      public void layoutClick(final LayoutClickEvent event)
      {
        if (!addButton.equals(event.getClickedComponent()))
        {
          presenter.showWidgetDetails(widgetModule);
        }
      }
    });
    addButton.addLayoutClickListener(new LayoutClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = 463103439584240597L;

      @Override
      public void layoutClick(final LayoutClickEvent event)
      {
        presenter.addWidgetToDashboard(widgetModule.getKey());
      }
    });
    widgetLayout.addComponent(addButton);
    widgetLayout.setComponentAlignment(addButton, Alignment.BOTTOM_RIGHT);
    widgetLayout.setDescription(getWidgetToolTip(widgetModule));

    // TODO Define WidgetItem as dragable item
    // Put the component in a D&D wrapper and allow dragging it
    // final DragAndDropWrapper widgetWrap = new DragAndDropWrapper(widgetLayout);
    // widgetWrap.setDragStartMode(DragStartMode.COMPONENT);
    //
    // Set the wrapper to wrap tightly around the component
    // widgetWrap.setSizeUndefined();

    return widgetLayout;
  }

  private String getWidgetToolTip(final WidgetModule pWidget)
  {
    final PortalMessages portalMessages = DashboardModule.getPortalMessages();
    final Locale locale = presenter.getComponent().getLocale();
    final String widgetName = portalMessages.getMessage(locale, getI18NNameKey(pWidget.getKey()));

    return "<h1>" + widgetName + "</h1>" + "<p>" + portalMessages.getMessage(locale, getI18NDescKey(pWidget.getKey()))
               + "</p>" + "<p>" + portalMessages.getMessage(locale, Messages.DASHBOARD_ADDWIDGET_LAYOUT_DESCRIPTION,
                                                            widgetName) + "</p>";
  }

  /**
   * Get the I18N name for this widgetModule key
   * 
   * @param pWidgetModuleKey
   *          the WidgetModule key
   * @return the I18N name associated
   */
  public String getI18NNameKey(final String pWidgetModuleKey)
  {
    return Messages.WIDGET_I18N_PREFIX + pWidgetModuleKey + Messages.WIDGET_I18N_NAME_SUFFIX;

  }

  /**
   * Get the I18N description for this widgetModule key
   * 
   * @param pWidgetModuleKey
   *          the WidgetModule key
   * @return the I18N description associated
   */
  public String getI18NDescKey(final String pWidgetModuleKey)
  {
    return Messages.WIDGET_I18N_PREFIX + pWidgetModuleKey + Messages.WIDGET_I18N_DESC_SUFFIX;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeGenerated(final AbstractItemLayout pSource, final Object pItemId,
      final Object pPropertyId)
  {
    boolean canBe = false;

    if ((WidgetModuleItemProperty.NAME.getPropertyId().equals(pPropertyId))
        || (WidgetModuleItemProperty.ICON.getPropertyId().equals(pPropertyId)))
    {
      final Item item = pSource.getContainerDataSource().getItem(pItemId);
      final Object itemLogin = item.getItemProperty(WidgetModuleItemProperty.NAME.getPropertyId()).getValue();
      final Object itemIcon = item.getItemProperty(WidgetModuleItemProperty.ICON.getPropertyId()).getValue();
      canBe = (itemLogin != null) && (itemIcon != null);
    }
    return canBe;
  }
}
