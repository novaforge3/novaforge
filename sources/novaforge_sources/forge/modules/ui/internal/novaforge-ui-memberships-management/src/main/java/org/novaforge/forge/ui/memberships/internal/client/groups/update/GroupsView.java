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
package org.novaforge.forge.ui.memberships.internal.client.groups.update;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Form;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.novaforge.forge.ui.memberships.internal.client.roles.components.RolesHandlerComponent;
import org.novaforge.forge.ui.memberships.internal.client.users.components.UserTableSelectable;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public interface GroupsView extends ComponentContainer
{

  /**
   * Should be called to refresh view according to the {@link Locale} given
   * 
   * @param pLocale
   *          the new locale
   */
  void refreshLocale(Locale pLocale);

  /**
   * Return {@link Button} used to add a new group as member
   * 
   * @return {@link Button} used to add a new group as member
   */
  Button getCreateButton();

  /**
   * Return {@link TextField} used to filter group's table
   * 
   * @return {@link TextField} used to filter group's table
   */
  TextField getFilterTextField();

  /**
   * Return {@link Table} containing groups
   * 
   * @return {@link Table} containing groups
   */
  Table getGroupsTable();

  /**
   * Return sub window used to update group's role
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
   * Return sub window used to delete group
   * 
   * @return {@link Window}
   */
  Window getDeleteGroupWindow();

  /**
   * Return button used to confirm group deletion
   * 
   * @return {@link Button}
   */
  Button getDeleteGroupConfirmButton();

  /**
   * Return button used to cancel group deletion
   * 
   * @return {@link Button}
   */
  Button getDeleteGroupCancelButton();

  /**
   * Return button used to cancel group deletion
   * 
   * @return {@link CheckBox}
   */
  CheckBox getDeleteGroupNotifCheck();

  /**
   * This will attach or detach visuel composant from its parent
   * 
   * @param pAttach
   *          if true so table will be attached otherwise detached
   */
  void attachGroupsTable(boolean pAttach);

  /**
   * Return button used to cancel group edition
   * 
   * @return {@link Button}
   */
  Button getEditGroupCancelButton();

  /**
   * Return button used to confirm group edition
   * 
   * @return {@link Button}
   */
  Button getEditGroupConfirmButton();

  /**
   * Return {@link Form} used to edit group
   * 
   * @return {@link Form}
   */
  Form getEditGroupForm();

  /**
   * Return sub window used to edit group
   * 
   * @return {@link Window}
   */
  Window getEditGroupWindow();

  /**
   * Return button used to cancel users edition
   * 
   * @return {@link Button}
   */
  Button getEditUsersCancelButton();

  /**
   * Return button used to confirm users edition
   * 
   * @return {@link Button}
   */
  Button getEditUsersConfirmButton();

  /**
   * Return sub window used to edit group's user
   * 
   * @return {@link Window}
   */
  Window getEditUsersWindow();

  /**
   * Return {@link UserTableSelectable} used to manage selecting users
   * 
   * @return {@link UserTableSelectable}
   */
  UserTableSelectable getEditUsersTable();

  /**
   * Return sub window used to view group users
   * 
   * @return {@link Window}
   */
  Window getViewUsersWindow();

  /**
   * Return button used to close view users window
   * 
   * @return {@link Button}
   */
  Button getViewUserCloseButton();

  /**
   * Return the view user table
   * 
   * @return {@link Table}
   */
  Table getViewUsersTable();

  /**
   * Return the textfield for filter the view users list
   * 
   * @return {@link Textfield}
   */
  TextField getViewUsersFiltersText();

}
