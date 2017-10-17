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
package org.novaforge.forge.ui.user.management.internal.client.admin;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Form;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;
import org.novaforge.forge.ui.user.management.internal.client.admin.components.UserFieldFactory;
import org.vaadin.haijian.ExcelExporter;

import java.util.Locale;

/**
 * Interface of the UserManagementView
 * 
 * @author Jeremy Casery
 */
public interface UserManagementView extends ComponentContainer
{

  /**
   * Initialize all labels
   * 
   * @param pLocale
   *          The {@link Locale} used for labels
   */
  void refreshLocale(Locale pLocale);

  /**
   * Get the users Table
   * 
   * @return the users {@link Table}
   */
  Table getUsersTable();

  /**
   * Get the users filter textfield
   * 
   * @return the textfield
   */
  TextField getFilterTextField();

  /**
   * Define which columns are show in the users table
   */
  void setUserTableVisibleColumns();

  /**
   * Get the delete user confirmation window
   * 
   * @return the window
   */
  DeleteConfirmWindow getDeleteUserWindow();

  /**
   * Get the create user button
   * 
   * @return the {@link Button}
   */
  Button getCreateUserButton();

  /**
   * Get the create user window
   * 
   * @return the {@link Window}
   */
  Window getCreateUserWindow();

  /**
   * Get the create user window button
   * 
   * @return the {@link Button}
   */
  Button getCreateUserWindowButton();

  /**
   * Get the create user form
   * 
   * @return the {@link Form}
   */
  Form getCreateUserForm();

  /**
   * Get the user field factory
   * 
   * @return the {@link UserFieldFactory}
   */
  UserFieldFactory getUserFieldFactory();

  /**
   * Get the button used to export table to excel
   * 
   * @return the {@link ExcelExporter}
   */
  ExcelExporter getExcelExporter();

}
