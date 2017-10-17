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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.requests;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public interface RequestsListView extends ComponentContainer
{

  /**
   * Get the return to plugins list button
   * 
   * @return the return button
   */
  Button getReturnToPluginsList();

  /**
   * Get the requests' plugin icon
   * 
   * @return {@link Enbedded} the plugin icon
   */
  Embedded getPluginIcon();

  /**
   * Get the requests' plugin name label
   * 
   * @return the plugin name label
   */
  Label getPluginNameLabel();

  /**
   * Get the gotoinstnaceslist button
   * 
   * @return the button
   */
  Button getGoToInstancesList();

  /**
   * Get the requests list table
   * 
   * @return the requests table
   */
  Table getRequestsTable();

  /**
   * Define which requests table column will be showed
   */
  void setRequestsTableVisibleColumns();

  /**
   * Get the delete request window
   * 
   * @return the delete window
   */
  DeleteConfirmWindow getDeleteRequestWindow();

  /**
   * Get the link request window
   * 
   * @return the link window
   */
  Window getLinkInstanceWindow();

  /**
   * Get the link request button
   * 
   * @return the link button
   */
  Button getLinkInstanceButtonSubmit();

  /**
   * Get the link instance combobox
   * 
   * @return the instnace combobox
   */
  ComboBox getLinkInstanceComboBox();

  /**
   * Get the link request name label
   * 
   * @return the name label
   */
  Label getLinkRequestNameLabel();

  /**
   * Add and attach the requests table to the view
   */
  void attachRequestsTable();

  /**
   * Remove and detach the requests table from the view
   */
  void detachRequestsTable();

  /**
   * @param pLocale
   */
  void refreshLocale(Locale pLocale);

}
