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
package org.novaforge.forge.ui.dashboard.internal.client.tab.settings.apps.components;

import com.google.common.base.Strings;
import com.vaadin.data.Item;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.dashboard.internal.client.container.ApplicationItemProperty;
import org.vaadin.addon.itemlayout.layout.AbstractItemLayout;
import org.vaadin.addon.itemlayout.layout.model.ItemGenerator;

/**
 * @author Guillaume Lamirand
 */
public class ApplicationItemGenerator implements ItemGenerator
{
  /**
   * Serial version id
   */
  private static final long serialVersionUID = -7517770053197854993L;

  private static final int  WIDGET_ICON_SIZE = 30;

  /**
   * {@inheritDoc}
   */
  @Override
  public Component generateItem(final AbstractItemLayout pSource, final Object pItemId)
  {
    final Item item = pSource.getContainerDataSource().getItem(pItemId);
    final String name = (String) item.getItemProperty(ApplicationItemProperty.NAME.getPropertyId())
        .getValue();
    final String desc = (String) item.getItemProperty(ApplicationItemProperty.DESCRIPTION.getPropertyId())
        .getValue();
    final Resource icon = (Resource) item.getItemProperty(ApplicationItemProperty.ICON.getPropertyId())
        .getValue();
    final VerticalLayout boxLayout = new VerticalLayout();
    boxLayout.setSizeUndefined();
    boxLayout.addStyleName(NovaForge.CURSOR_BUTTON);

    if ((!Strings.isNullOrEmpty(name)) && (icon != null))
    {
      boxLayout.setDescription(desc);

      // Application name
      final Label appName = initName();
      appName.setValue(name);

      // Icon Layout
      final Image widgetImage = initIcon();
      widgetImage.setSource(icon);

      boxLayout.addComponent(widgetImage);
      boxLayout.setComponentAlignment(widgetImage, Alignment.MIDDLE_CENTER);
      boxLayout.setExpandRatio(widgetImage, 1f);
      boxLayout.addComponent(appName);
      boxLayout.setComponentAlignment(appName, Alignment.BOTTOM_CENTER);
    }
    return boxLayout;
  }

  private Label initName()
  {
    final Label appName = new Label();
    appName.setStyleName(NovaForge.LABEL_BOLD);
    appName.addStyleName(NovaForge.CURSOR_BUTTON);
    appName.addStyleName(NovaForge.TEXT_ELLIPSIS);
    return appName;
  }

  private Image initIcon()
  {
    final Image widgetImage = new Image();
    widgetImage.setWidth(WIDGET_ICON_SIZE, Unit.PIXELS);
    widgetImage.setHeight(WIDGET_ICON_SIZE, Unit.PIXELS);
    widgetImage.addStyleName(NovaForge.CURSOR_BUTTON);
    return widgetImage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeGenerated(final AbstractItemLayout pSource, final Object pItemId,
      final Object pPropertyId)
  {
    boolean isNeeded = false;

    if ((ApplicationItemProperty.NAME.getPropertyId().equals(pPropertyId))
        || (ApplicationItemProperty.DESCRIPTION.getPropertyId().equals(pPropertyId))
        || (ApplicationItemProperty.ICON.getPropertyId().equals(pPropertyId)))
    {
      final Item item = pSource.getContainerDataSource().getItem(pItemId);
      final Object name = item.getItemProperty(ApplicationItemProperty.NAME.getPropertyId()).getValue();
      final Object icon = item.getItemProperty(ApplicationItemProperty.ICON.getPropertyId()).getValue();
      isNeeded = (name != null) && (icon != null);
    }
    return isNeeded;
  }
}
