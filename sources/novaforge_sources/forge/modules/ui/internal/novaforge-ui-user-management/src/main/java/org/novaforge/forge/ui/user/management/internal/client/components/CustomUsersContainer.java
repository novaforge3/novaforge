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
package org.novaforge.forge.ui.user.management.internal.client.components;

import org.novaforge.forge.core.organization.model.BlacklistedUser;
import org.novaforge.forge.ui.portal.data.container.UserItemProperty;
import org.novaforge.forge.ui.portal.data.container.UsersContainer;

import java.util.List;

/**
 * This component describes a specific {@link UsersContainer} used to build users table.
 * 
 * @author Jeremy Casery
 */
public class CustomUsersContainer extends UsersContainer
{

  /**
	 * 
   */
  private static final long serialVersionUID = 7245265409387407802L;

  //
  // public CustomUsersContainer()
  // {
  // super();
  // setItemSorter(new DefaultItemSorter(new StringComparator()));
  // }

  /**
   * Add users into container
   * 
   * @param {@link List}<{@link BlacklistedUser}> pUsers
   *        Users to add
   */
  public void setBlacklistedUsers(final List<BlacklistedUser> pUsers)
  {
    removeAllItems();
    for (final BlacklistedUser user : pUsers)
    {
      final String itemID = user.getLogin();
      addItem(itemID);
      getContainerProperty(itemID, UserItemProperty.ID.getPropertyId()).setValue(itemID);
      getContainerProperty(itemID, UserItemProperty.LOGIN.getPropertyId()).setValue(user.getLogin());
      getContainerProperty(itemID, UserItemProperty.EMAIL.getPropertyId()).setValue(user.getEmail());
      getContainerProperty(itemID, UserItemProperty.CREATED_DATE.getPropertyId()).setValue(
          user.getCreationDate());
    }

    sort(new Object[] { UserItemProperty.LOGIN.getPropertyId() }, new boolean[] { true });
  }

}
