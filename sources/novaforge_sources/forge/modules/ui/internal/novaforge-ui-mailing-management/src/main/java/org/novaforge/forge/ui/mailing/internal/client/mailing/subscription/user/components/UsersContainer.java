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
package org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.user.components;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.ui.portal.data.util.StringComparator;

import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class UsersContainer extends IndexedContainer
{

  /**
	 * 
	 */
  private static final long serialVersionUID = 7245265409387407802L;

  /**
   * Default constructor. It will initialize user item property
   * 
   * @see UserItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public UsersContainer()
  {
    super();
    addContainerProperty(UserItemProperty.LOGIN.getPropertyId(), String.class, null);
    addContainerProperty(UserItemProperty.FIRSTNAME.getPropertyId(), String.class, null);
    addContainerProperty(UserItemProperty.LASTNAME.getPropertyId(), String.class, null);
    addContainerProperty(UserItemProperty.EMAIL.getPropertyId(), String.class, null);

    setItemSorter(new DefaultItemSorter(new StringComparator()));
  }

  /**
   * Add memberships into container
   * 
   * @param pUsers
   *          memberships to add
   */
  public void setUsers(final List<User> pUsers)
  {
    removeAllContainerFilters();
    removeAllItems();
    for (final User user : pUsers)
    {
      final String login = user.getLogin();
      addItem(login);
      getContainerProperty(login, UserItemProperty.LOGIN.getPropertyId()).setValue(login);
      getContainerProperty(login, UserItemProperty.FIRSTNAME.getPropertyId()).setValue(user.getFirstName());
      getContainerProperty(login, UserItemProperty.LASTNAME.getPropertyId()).setValue(user.getName());
      getContainerProperty(login, UserItemProperty.EMAIL.getPropertyId()).setValue(user.getEmail());

    }

    sort(new Object[] { UserItemProperty.LOGIN.getPropertyId() }, new boolean[] { true });
  }

}
