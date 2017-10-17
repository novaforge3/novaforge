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
package org.novaforge.forge.ui.dashboard.internal.client.tab.header;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.novaforge.forge.ui.dashboard.internal.client.tab.header.component.LayoutsHeaderComponent;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;
import org.novaforge.forge.ui.portal.client.component.UploadFieldCustom;
import org.vaadin.addon.itemlayout.horizontal.ItemHorizontal;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public interface HeaderView extends ComponentContainer
{
  /**
   * Refresh text view
   * 
   * @param pLocale
   *          the locale used to refresh labels
   */
  void refreshLocale(final Locale pLocale);

  /**
   * Returns {@link TextField} used to define tab name
   * 
   * @return {@link TextField} used to define tab name
   */
  TextField getTabName();

  /**
   * Returns {@link Image} used to add widget
   * 
   * @return {@link Image} used to add widget
   */
  Image getAddButton();

  /**
   * Get the upload picture field
   * 
   * @return the {@linkplain UploadFieldCustom} field
   */
  UploadFieldCustom getImageUpload();

  /**
   * Get the tab icon
   * 
   * @return {@link Image} tab icon
   */
  Image getTabIcon();

  /**
   * Get the update tab icon window
   * 
   * @return the {@link Window}
   */
  Window getUpdatePictureWindow();

  /**
   * Get the delete tab icon button
   * 
   * @return the {@link Button}
   */
  Button getIconDeleteButton();

  /**
   * Get the confirm update tab icon button
   * 
   * @return the {@link Button}
   */
  Button getIconConfirmButton();

  /**
   * Get window to confirm deleting tab
   * 
   * @return {@link DeleteConfirmWindow} to confirm deleting tab
   */
  DeleteConfirmWindow getTabDeleteWindow();

  /**
   * Get window to confirm selecting lower boxse number
   * 
   * @return {@link Window} to confirm selecting lower boxse number
   */
  Window getLayoutSelectWindow();

  /**
   * Get the button used to cancel selecting lower boxse number
   * 
   * @return the {@link Button}
   */
  Button getLayoutSelectCancelButton();

  /**
   * Get the button used to confirm selecting lower boxse number
   * 
   * @return the {@link Button}
   */
  Button getLayoutSelectConfirmButton();

  /**
   * Get the component used to display layout selection
   * 
   * @return {@link LayoutsHeaderComponent}
   */
  LayoutsHeaderComponent getLayoutHeader();

  /**
   * Get the widgets filter category combobox
   * 
   * @return The {@link ComboBox}
   */
  ComboBox getAddWidgetFilterCategoryComboBox();

  /**
   * Get the widgets filter textfield
   * 
   * @return the {@link TextField}
   */
  TextField getAddWidgetFilterTextField();

  /**
   * Get the add widget back label
   * 
   * @return the {@link Label}
   */
  Label getAddWidgetBackLabel();

  /**
   * Get the AddWidget layout componenet
   * 
   * @return the {@link Component}
   */
  Component getAddWidgetLayoutComponent();

  /**
   * Get the headerGeneral layout
   * 
   * @return the {@link HorizontalLayout}
   */
  HorizontalLayout getHeaderGeneralLayout();

  /**
   * Show the header general layout
   */
  void showHeaderGeneralLayout();

  /**
   * Show the add widget layout
   */
  void showAddWidgetLayout();

  /**
   * @return
   */
  VerticalLayout getBackButtonLayout();

  /**
   * @return
   */
  VerticalLayout getAddWidgetButtonLayout();

  /**
   * @return
   */
  VerticalLayout getDeleteButtonLayout();

  /**
   * @return
   */
  ItemHorizontal getWidgetList();

  /**
   * @return
   */
  HorizontalLayout getWidgetDetailsCategoriesLayout();

  /**
   * @param widgetDetailsCategoriesLayout
   */
  void setWidgetDetailsCategoriesLayout(HorizontalLayout widgetDetailsCategoriesLayout);

  /**
   * @return
   */
  Button getWidgetDetailsAddWidgetButton();

  /**
   * @param widgetDetailsAddWidgetButton
   */
  void setWidgetDetailsAddWidgetButton(Button widgetDetailsAddWidgetButton);

  /**
   * @return
   */
  Button getWidgetDetailsCloseButton();

  /**
   * @param widgetDetailsCloseButton
   */
  void setWidgetDetailsCloseButton(Button widgetDetailsCloseButton);

  /**
   * @return
   */
  Label getWidgetDetailsDescriptionLabel();

  /**
   * @param widgetDetailsDescriptionLabel
   */
  void setWidgetDetailsDescriptionLabel(Label widgetDetailsDescriptionLabel);

  /**
   * @return
   */
  Image getWidgetDetailsThumbImage();

  /**
   * @param widgetDetailsThumbImage
   */
  void setWidgetDetailsThumbImage(Image widgetDetailsThumbImage);

  /**
   * @return
   */
  Label getWidgetDetailsName();

  /**
   * @param widgetDetailsName
   */
  void setWidgetDetailsName(Label widgetDetailsName);

  /**
   * @return
   */
  Image getWidgetDetailsIcon();

  /**
   * @param widgetDetailsIcon
   */
  void setWidgetDetailsIcon(Image widgetDetailsIcon);

  /**
   * @return
   */
  Window getWidgetDetailsWindow();

  /**
   * @param widgetDetailsWindow
   */
  void setWidgetDetailsWindow(Window widgetDetailsWindow);
}