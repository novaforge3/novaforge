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
package org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.group.containers;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.ui.portal.data.util.StringComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public class GroupContainer extends IndexedContainer
{

  /**
	 * 
	 */
  private static final long serialVersionUID = 7245265409387407802L;

  /**
   * Default constructor. It will initialize user item property
   * 
   * @see GroupItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public GroupContainer()
  {
    super();
    addContainerProperty(GroupItemProperty.NAME.getPropertyId(), String.class, null);
    addContainerProperty(GroupItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(GroupItemProperty.GROUP.getPropertyId(), Group.class, null);
    addContainerProperty(GroupItemProperty.DEFAULT.getPropertyId(), MembershipInfo.class, null);
    addContainerProperty(GroupItemProperty.MEMBERSHIPS.getPropertyId(), List.class,
        new ArrayList<MembershipInfo>());
    addContainerProperty(GroupItemProperty.EDITABLE.getPropertyId(), Boolean.class, true);

    setItemSorter(new DefaultItemSorter(new StringComparator()));
  }

  /**
   * Add memberships into container
   * 
   * @param pGroup
   *          memberships to add
   * @param isForgeProject
   *          define if current project is forge one
   */
  public void setGroups(final List<Group> pGroup, final boolean isForgeProject)
  {
    removeAllContainerFilters();
    removeAllItems();
    for (final Group group : pGroup)
    {
      final UUID itemID = group.getUuid();
      if (!containsId(itemID))
      {
        addItem(itemID);
        addGroup(itemID, group, isForgeProject);
      }
    }
    sort(new Object[] { GroupItemProperty.NAME.getPropertyId() }, new boolean[] { true });
  }

  /**
   * Add the given group to container
   *
   * @param pGroup
   * @param login
   */
  private void addGroup(final UUID pItemId, final Group pGroup, final boolean pIsForgeProject)
  {
    getContainerProperty(pItemId, GroupItemProperty.NAME.getPropertyId()).setValue(pGroup.getName());
    getContainerProperty(pItemId, GroupItemProperty.DESCRIPTION.getPropertyId()).setValue(pGroup.getDescription());
    getContainerProperty(pItemId, GroupItemProperty.GROUP.getPropertyId()).setValue(pGroup);
    getContainerProperty(pItemId, GroupItemProperty.MEMBERSHIPS.getPropertyId())
        .setValue(new ArrayList<MembershipInfo>());
    boolean isEditable = true;
    if ((pGroup.isVisible()) && (!pIsForgeProject))
    {
      isEditable = false;
    }
    getContainerProperty(pItemId, GroupItemProperty.EDITABLE.getPropertyId()).setValue(isEditable);
  }

  /**
   * Add memberships into container
   *
   * @param pMemberships
   *          memberships to add
   */
  @SuppressWarnings("unchecked")
  public void setGroupsMember(final List<MembershipInfo> pMemberships)
  {
    for (final MembershipInfo member : pMemberships)
    {
      if (member.getActor() instanceof Group)
      {
        final Group group = (Group) member.getActor();
        final UUID itemID = group.getUuid();
        if (containsId(itemID))
        {
          final ProjectRole role = member.getRole();
          final MembershipInfo existingMember = (MembershipInfo) getContainerProperty(itemID,
              GroupItemProperty.DEFAULT.getPropertyId()).getValue();
          if ((existingMember == null) || ((existingMember.getRole().getOrder() > role.getOrder()) && (!existingMember
                                                                                                            .getPriority()))
              || (member.getPriority()))
          {
            getContainerProperty(itemID, GroupItemProperty.DEFAULT.getPropertyId()).setValue(member);
          }
          final List<MembershipInfo> memberships = (List<MembershipInfo>) getContainerProperty(itemID,
              GroupItemProperty.MEMBERSHIPS.getPropertyId()).getValue();
          memberships.add(member);
        }
      }
    }

  }

}
