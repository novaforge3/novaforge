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
package org.novaforge.forge.plugins.mailinglist.sympa.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.plugins.mailinglist.sympa.model.GroupSubscription;
import org.novaforge.forge.plugins.mailinglist.sympa.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "GROUP_SUBSCRIPTION")
@NamedQueries({
    @NamedQuery(name = "GroupSubscriptionEntity.findByListName",
        query = "SELECT g FROM GroupSubscriptionEntity g WHERE g.listName = :listname"),
    @NamedQuery(
        name = "GroupSubscriptionEntity.findByListNameAndGroupUUID",
        query = "SELECT g FROM GroupSubscriptionEntity g WHERE g.listName = :listname AND g.groupUUID = :groupUUID"),
    @NamedQuery(name = "GroupSubscriptionEntity.findByGroupUUID",
        query = "SELECT g FROM GroupSubscriptionEntity g WHERE g.groupUUID = :groupUUID"),
    @NamedQuery(
        name = "GroupSubscriptionEntity.findByListNameAndUser",
        query = "SELECT g FROM GroupSubscriptionEntity g WHERE g.listName = :listname AND :user MEMBER OF g.members") })
public class GroupSubscriptionEntity implements GroupSubscription
{
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long      id;

  @Column(name = "group_uuid", nullable = false, updatable = false)
  private String    groupUUID;

  @Column(name = "group_name", nullable = false)
  private String    groupName;

  @Column(name = "listname", nullable = false)
  private String    listName;

  @ManyToMany(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
  @JoinTable(name = "GROUP_SUBSCRIPTION_USER", joinColumns = @JoinColumn(name = "group_subscription_id"),
      inverseJoinColumns = @JoinColumn(name = "user_login"))
  private Set<User> members = new HashSet<User>();

  public GroupSubscriptionEntity()
  {
    super();
  }

  public GroupSubscriptionEntity(final UUID pGroupUUID, final String pGroupName, final String pListName)
  {
    super();
    groupUUID = pGroupUUID.toString();
    groupName = pGroupName;
    listName = pListName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getGroupUUID()
  {
    return UUID.fromString(groupUUID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setGroupUUID(final UUID pUUID)
  {
    groupUUID = pUUID.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getGroupName()
  {
    return groupName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setGroupName(final String pName)
  {
    groupName = pName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getListname()
  {
    return listName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setListname(final String pName)
  {
    listName = pName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<User> getMembers()
  {
    return members;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMembers(final Set<User> pMembers)
  {
    members = pMembers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addMember(final User pMember)
  {
    members.add(pMember);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeMember(final User pMember)
  {
    members.remove(pMember);
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(groupUUID).append(listName).toHashCode();
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof GroupSubscriptionEntity))
    {
      return false;
    }
    final GroupSubscriptionEntity castOther = (GroupSubscriptionEntity) other;
    return new EqualsBuilder().append(groupUUID, castOther.groupUUID)
        .append(listName, castOther.getListname()).isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "GroupSubscriptionEntity [id=" + id + ", groupUUID=" + groupUUID + ", groupName=" + groupName + ", listName="
               + listName + ", members=" + members + "]";
  }

}
