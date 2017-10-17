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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.instances;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public interface InstancesListView extends ComponentContainer
{

  /**
   * Get the return button
   * 
   * @return {@link Button} the return button
   */
  Button getReturnToPluginsList();

  /**
   * Get the instances table
   * 
   * @return the instances table
   */
  Table getInstancesTable();

  /**
   * Get the plugin icon embedded
   * 
   * @return the plugin icon
   */
  Embedded getPluginIcon();

  /**
   * Get the plugin name label
   * 
   * @return the plugin name label
   */
  Label getPluginNameLabel();

  /**
   * Will display subwindow and edit its caption
   * 
   * @param pDisplay
   *          true to show sub windows false to close it
   */
  void showCreateInstanceWindow(boolean pDisplay);

  /**
   * Get the create instance button
   * 
   * @return the create instance button
   */
  Button getCreateInstanceButton();

  /**
   * Get the create instance form
   * 
   * @return the create instance form
   */
  Form getCreateInstanceForm();

  /**
   * Get the create instance form button submit
   * 
   * @return the button submit
   */
  Button getCreateInstanceButtonSubmit();

  /**
   * Get the edit instance form button submit
   * 
   * @return the button submit
   */
  Button getEditInstanceButtonSubmit();

  /**
   * Get the edit instance form
   * 
   * @return the edit instance form
   */
  Form getEditInstanceForm();

  /**
   * Will display subwindow and edit its caption
   * 
   * @param pDisplay
   *          true to show sub windows false to close it
   */
  void showEditInstanceWindow(boolean pDisplay);

  /**
   * Get the delete instance confirm window
   * 
   * @return the {@link DeleteConfirmWindow} window
   */
  DeleteConfirmWindow getInstanceDeleteWindow();

  // /**
  // * Will display subwindow and edit its caption
  // *
  // * @param pDisplay
  // * true to show sub windows false to close it
  // */
  // void showDeleteInstanceWindow(boolean pDisplay);

  /**
   * Get the Gotorequestslist button
   * 
   * @return the button
   */
  Button getGoToRequestsListButton();

  /**
   * Get the requests combobox
   * 
   * @return the requests combobox
   */
  ComboBox getRequestsComboBox();

  /**
   * Remove and detach the instance table from the view
   */
  void detachInstancesTable();

  /**
   * Add and attach the instance table to the view
   */
  void attachInstancesTable();

  /**
   * @param pLocale
   */
  void refreshLocale(Locale pLocale);

  /**
   * @param pDisplay
   */
  void showProjectsWindow(boolean pDisplay);

  /**
   * @return
   */
  Table getProjectsTable();

  /**
   * @return
   */
  DeleteConfirmWindow getProjectDeleteWindow();

}
