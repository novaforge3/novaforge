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
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.model.enumerations.UserStatus;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;

import java.util.Date;
import java.util.List;

/**
 * @author caseryj
 */
public class UserProfileContainer extends IndexedContainer
{

  /**
   * 
   */
  private static final long serialVersionUID = -5298502857295236278L;

  /**
   * Default constructor. It will initialize user item property
   * 
   * @see UserItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public UserProfileContainer()
  {
    super();
    addContainerProperty(UserProfileItemProperty.ID.getPropertyId(), String.class, null);
    addContainerProperty(UserProfileItemProperty.LOGIN.getPropertyId(), String.class, null);
    addContainerProperty(UserProfileItemProperty.FIRSTNAME.getPropertyId(), String.class, null);
    addContainerProperty(UserProfileItemProperty.LASTNAME.getPropertyId(), String.class, null);
    addContainerProperty(UserProfileItemProperty.EMAIL.getPropertyId(), String.class, null);
    addContainerProperty(UserProfileItemProperty.LANGUAGE.getPropertyId(), Language.class, null);
    addContainerProperty(UserProfileItemProperty.CREATED_DATE.getPropertyId(), Date.class, null);
    addContainerProperty(UserProfileItemProperty.STATUS.getPropertyId(), UserStatus.class, null);
    addContainerProperty(UserProfileItemProperty.PASSWORD.getPropertyId(), String.class, null);
    addContainerProperty(UserProfileItemProperty.LAST_PASSWORD_UPDATED.getPropertyId(), Date.class, null);
    addContainerProperty(UserProfileItemProperty.LAST_CONNECTED.getPropertyId(), Date.class, null);
    addContainerProperty(UserProfileItemProperty.REALM_TYPE.getPropertyId(), RealmType.class, null);
    addContainerProperty(UserProfileItemProperty.PICTURE.getPropertyId(), BinaryFile.class, null);

    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  /**
   * Add users into container
   * 
   * @param pUsers
   *          Users to add
   */
  public void setUserProfiles(final List<UserProfile> pUserProfiles)
  {
    removeAllItems();
    for (final UserProfile userProfile : pUserProfiles)
    {
      final String itemID = userProfile.getUser().getLogin();
      addItem(itemID);
      getContainerProperty(itemID, UserProfileItemProperty.ID.getPropertyId()).setValue(itemID);
      getContainerProperty(itemID, UserProfileItemProperty.LOGIN.getPropertyId()).setValue(
          userProfile.getUser().getLogin());
      getContainerProperty(itemID, UserProfileItemProperty.FIRSTNAME.getPropertyId()).setValue(
          userProfile.getUser().getFirstName());
      getContainerProperty(itemID, UserProfileItemProperty.LASTNAME.getPropertyId()).setValue(
          userProfile.getUser().getName());
      getContainerProperty(itemID, UserProfileItemProperty.EMAIL.getPropertyId()).setValue(
          userProfile.getUser().getEmail());
      getContainerProperty(itemID, UserProfileItemProperty.LANGUAGE.getPropertyId()).setValue(
          userProfile.getUser().getLanguage());
      getContainerProperty(itemID, UserProfileItemProperty.STATUS.getPropertyId()).setValue(
          userProfile.getUser().getStatus());
      getContainerProperty(itemID, UserProfileItemProperty.CREATED_DATE.getPropertyId()).setValue(
          userProfile.getUser().getCreated());
      getContainerProperty(itemID, UserProfileItemProperty.PASSWORD.getPropertyId()).setValue(
          userProfile.getUser().getPassword());
      getContainerProperty(itemID, UserProfileItemProperty.LAST_PASSWORD_UPDATED.getPropertyId()).setValue(
          userProfile.getUser().getLastPasswordUpdated());
      getContainerProperty(itemID, UserProfileItemProperty.LAST_CONNECTED.getPropertyId()).setValue(
          userProfile.getUser().getLastConnected());
      getContainerProperty(itemID, UserProfileItemProperty.REALM_TYPE.getPropertyId()).setValue(
          userProfile.getUser().getRealmType());
      getContainerProperty(itemID, UserProfileItemProperty.PICTURE.getPropertyId()).setValue(
          userProfile.getImage());

    }

    sort(new Object[] { UserProfileItemProperty.LOGIN.getPropertyId() }, new boolean[] { true });
  }
}
