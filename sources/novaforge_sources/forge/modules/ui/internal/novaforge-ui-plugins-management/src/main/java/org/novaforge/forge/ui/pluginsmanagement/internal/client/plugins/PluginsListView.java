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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;

import java.util.List;
import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public interface PluginsListView extends ComponentContainer
{

  /**
   * Get the plugins table
   * 
   * @return {@link Table} the plugins list table
   */
  Table getPluginsTable();

  /**
   * Get the status filter combobox
   * 
   * @return {@link Combobox} the status combobox
   */
  ComboBox getStatusCombobox();

  /**
   * Get the reset filters button
   * 
   * @return {@link Button} the reset button
   */
  Button getResetFiltersButton();

  /**
   * Get the category filter combobox
   * 
   * @return {@link Combobox} the category combobox
   */
  ComboBox getCategoryCombobox();

  /**
   * Get the change status popup
   * 
   * @return {@link Window} the popup window
   */
  Window getChangeStatusWindow();

  /**
   * Show the manage status for the given plugin
   * 
   * @param pPluginId
   *          The id of the plugin
   * @param pStatus
   *          The available status for current plugin's status
   */
  void showManageStatusPopUp(Object pPluginId, List<PluginStatus> pStatus);

  /**
   * Get the description of the new status
   * 
   * @return The label
   */
  Label getNewStatusDescriptionLabel();

  /**
   * Get the new status combobox
   * 
   * @return the new status combobox
   */
  ComboBox getNewStatusComboBox();

  /**
   * Get the change status submit button
   * 
   * @return the submit button
   */
  Button getChangeStatusSubmit();

  /**
   * Get the new status description title
   * 
   * @return The new status description title label
   */
  Label getNewStatusDescriptionTitle();

  /**
   * Add and attach the plugins table to the view
   */
  void attachPluginsTable();

  /**
   * Detach and remove the plugins table from the view
   */
  void detachPluginsTable();

  /**
   * @param pLocale
   */
  void refreshLocale(Locale pLocale);
}
