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
package org.novaforge.forge.ui.portal.data.container;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.model.enumerations.UserStatus;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;

import java.util.Date;
import java.util.List;

/**
 * @author caseryj
 */
public class UsersContainer extends IndexedContainer
{

  /**
   * 
   */
  private static final long serialVersionUID = 5182080120208850100L;

  /**
   * Default constructor. It will initialize user item property
   * 
   * @see UserItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public UsersContainer()
  {
    super();
    addContainerProperty(UserItemProperty.ID.getPropertyId(), String.class, null);
    addContainerProperty(UserItemProperty.LOGIN.getPropertyId(), String.class, null);
    addContainerProperty(UserItemProperty.FIRSTNAME.getPropertyId(), String.class, null);
    addContainerProperty(UserItemProperty.LASTNAME.getPropertyId(), String.class, null);
    addContainerProperty(UserItemProperty.EMAIL.getPropertyId(), String.class, null);
    addContainerProperty(UserItemProperty.LANGUAGE.getPropertyId(), Language.class, null);
    addContainerProperty(UserItemProperty.CREATED_DATE.getPropertyId(), Date.class, null);
    addContainerProperty(UserItemProperty.STATUS.getPropertyId(), UserStatus.class, null);
    addContainerProperty(UserItemProperty.PASSWORD.getPropertyId(), String.class, null);
    addContainerProperty(UserItemProperty.LAST_PASSWORD_UPDATED.getPropertyId(), Date.class, null);
    addContainerProperty(UserItemProperty.LAST_CONNECTED.getPropertyId(), Date.class, null);
    addContainerProperty(UserItemProperty.REALM_TYPE.getPropertyId(), RealmType.class, null);

    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  /**
   * Add users into container
   * 
   * @param pUsers
   *          Users to add
   */
  public void setUsers(final List<User> pUsers)
  {
    removeAllItems();
    for (final User user : pUsers)
    {
      final String itemID = user.getLogin();
      addItem(itemID);
      getContainerProperty(itemID, UserItemProperty.ID.getPropertyId()).setValue(itemID);
      getContainerProperty(itemID, UserItemProperty.LOGIN.getPropertyId()).setValue(user.getLogin());
      getContainerProperty(itemID, UserItemProperty.FIRSTNAME.getPropertyId()).setValue(user.getFirstName());
      getContainerProperty(itemID, UserItemProperty.LASTNAME.getPropertyId()).setValue(user.getName());
      getContainerProperty(itemID, UserItemProperty.EMAIL.getPropertyId()).setValue(user.getEmail());
      getContainerProperty(itemID, UserItemProperty.LANGUAGE.getPropertyId()).setValue(user.getLanguage());
      getContainerProperty(itemID, UserItemProperty.STATUS.getPropertyId()).setValue(user.getStatus());
      getContainerProperty(itemID, UserItemProperty.CREATED_DATE.getPropertyId()).setValue(user.getCreated());
      getContainerProperty(itemID, UserItemProperty.PASSWORD.getPropertyId()).setValue(user.getPassword());
      getContainerProperty(itemID, UserItemProperty.LAST_PASSWORD_UPDATED.getPropertyId()).setValue(
          user.getLastPasswordUpdated());
      getContainerProperty(itemID, UserItemProperty.LAST_CONNECTED.getPropertyId()).setValue(
          user.getLastConnected());
      getContainerProperty(itemID, UserItemProperty.REALM_TYPE.getPropertyId()).setValue(user.getRealmType());

    }

    sort(new Object[] { UserItemProperty.LOGIN.getPropertyId() }, new boolean[] { true });
  }
}
