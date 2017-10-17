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
package org.novaforge.forge.ui.memberships.internal.client.containers;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.ui.portal.data.util.StringComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public class UserMembersContainer extends IndexedContainer
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
  public UserMembersContainer()
  {
    super();
    addContainerProperty(UserItemProperty.LOGIN.getPropertyId(), String.class, null);
    addContainerProperty(UserItemProperty.FIRSTNAME.getPropertyId(), String.class, null);
    addContainerProperty(UserItemProperty.LASTNAME.getPropertyId(), String.class, null);
    addContainerProperty(UserItemProperty.EMAIL.getPropertyId(), String.class, null);
    addContainerProperty(UserItemProperty.DEFAULT.getPropertyId(), MembershipInfo.class, null);
    addContainerProperty(UserItemProperty.MEMBERSHIPS.getPropertyId(), List.class,
        new ArrayList<MembershipInfo>());

    setItemSorter(new DefaultItemSorter(new StringComparator()));
  }

  /**
   * Add memberships into container
   * 
   * @param pMemberships
   *          memberships to add
   */
  @SuppressWarnings("unchecked")
  public void setUsersMember(final List<MembershipInfo> pMemberships)
  {
    removeAllContainerFilters();
    removeAllItems();
    for (final MembershipInfo member : pMemberships)
    {
      if (member.getActor() instanceof User)
      {
        final User user = (User) member.getActor();
        final UUID uuid = user.getUuid();
        if (!containsId(uuid))
        {
          addItem(uuid);
          getContainerProperty(uuid, UserItemProperty.LOGIN.getPropertyId()).setValue(user.getLogin());
          getContainerProperty(uuid, UserItemProperty.FIRSTNAME.getPropertyId())
              .setValue(user.getFirstName());
          getContainerProperty(uuid, UserItemProperty.LASTNAME.getPropertyId()).setValue(user.getName());
          getContainerProperty(uuid, UserItemProperty.EMAIL.getPropertyId()).setValue(user.getEmail());
          getContainerProperty(uuid, UserItemProperty.MEMBERSHIPS.getPropertyId()).setValue(
              new ArrayList<MembershipInfo>());
        }
        final ProjectRole role = member.getRole();
        final MembershipInfo existingMember = (MembershipInfo) getContainerProperty(uuid,
            UserItemProperty.DEFAULT.getPropertyId()).getValue();
        if ((existingMember == null) || ((existingMember.getRole().getOrder() > role.getOrder()) && (!existingMember
                                                                                                          .getPriority()))
            || (member.getPriority()))
        {
          getContainerProperty(uuid, UserItemProperty.DEFAULT.getPropertyId()).setValue(member);
        }
        final List<MembershipInfo> memberships = (List<MembershipInfo>) getContainerProperty(uuid,
            UserItemProperty.MEMBERSHIPS.getPropertyId()).getValue();
        memberships.add(member);
      }
    }

    sort(
        new Object[] { UserItemProperty.FIRSTNAME.getPropertyId(), UserItemProperty.LASTNAME.getPropertyId() },
        new boolean[] { true });
  }

}
