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
package org.novaforge.forge.ui.dashboard.internal.client.tab.settings.apps;

import com.vaadin.event.LayoutEvents.LayoutClickNotifier;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.dashboard.model.DashBoard.Type;
import org.vaadin.addon.itemlayout.vertical.ItemVertical;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public interface ApplicationsSourceView extends ComponentContainer, LayoutClickNotifier
{

  /**
   * Refresh text view
   * 
   * @param pLocale
   *          the locale used to refresh labels
   */
  void refreshLocale(final Locale pLocale);

  /**
   * Returns {@link VerticalLayout} containing the content part
   * 
   * @return {@link VerticalLayout} containing the content part
   */
  VerticalLayout getContentLayout();

  /**
   * Returns {@link TextField} used to filter application by name
   * 
   * @return {@link TextField} used to filter application by name
   */
  TextField getNameFilter();

  /**
   * Returns {@link ComboBox} used to filter application by project
   * 
   * @return {@link ComboBox} used to filter application by project
   */
  ComboBox getProjectsBox();

  /**
   * @return
   */
  ItemVertical getSourcesSelected();

  /**
   * @return
   */
  Label getSourcesSelectedTitle();

  /**
   * Set the dashboard type. It will make visible or not some part of the view
   * 
   * @param pType
   *          dashboard type
   */
  void setDashBoardType(Type pType);

}