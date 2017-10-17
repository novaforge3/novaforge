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
package org.novaforge.forge.ui.memberships.internal.client.users.update;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.novaforge.forge.ui.memberships.internal.client.roles.components.RolesHandlerComponent;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public interface UserView extends ComponentContainer
{

  /**
   * Should be called to refresh view according to the {@link Locale} given
   * 
   * @param pLocale
   *          the new locale
   */
  void refreshLocale(Locale pLocale);

  /**
   * Return {@link Button} used to add a new user as member
   * 
   * @return {@link Button} used to add a new user as member
   */
  Button getAddButton();

  /**
   * Return {@link TextField} used to filter user's table
   * 
   * @return {@link TextField} used to filter user's table
   */
  TextField getFilterTextField();

  /**
   * Return {@link Table} containing users
   * 
   * @return {@link Table} containing users
   */
  Table getUsersTable();

  /**
   * Return sub window used to update user's role
   * 
   * @return {@link Window}
   */
  Window getEditRolesWindow();

  /**
   * Return component which manage roles selection
   * 
   * @return {@link RolesHandlerComponent}
   */
  RolesHandlerComponent getEditRolesComponent();

  /**
   * Return sub window used to delete user
   * 
   * @return {@link Window}
   */
  Window getDeleteUserWindow();

  /**
   * Return button used to confirm user deletion
   * 
   * @return {@link Button}
   */
  Button getDeleteUserConfirmButton();

  /**
   * Return button used to cancel user deletion
   * 
   * @return {@link Button}
   */
  Button getDeleteUserCancelButton();

  /**
   * Return button used to cancel user deletion
   * 
   * @return {@link Button}
   */
  CheckBox getDeleteUserNotifCheck();

}
