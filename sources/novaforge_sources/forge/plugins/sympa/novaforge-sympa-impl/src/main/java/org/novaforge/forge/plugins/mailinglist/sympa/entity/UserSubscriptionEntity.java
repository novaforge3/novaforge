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
import org.novaforge.forge.plugins.mailinglist.sympa.model.UserSubscription;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "USER_SUBSCRIPTION")
@NamedQueries({
    @NamedQuery(name = "UserSubscriptionEntity.findByListName",
        query = "SELECT u FROM UserSubscriptionEntity u WHERE u.listName = :listname"),
    @NamedQuery(name = "UserSubscriptionEntity.findByListNameAndLogin",
        query = "SELECT u FROM UserSubscriptionEntity u WHERE u.listName = :listname AND u.login = :login") })
public class UserSubscriptionEntity implements UserSubscription
{
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long   id;

  @Column(name = "login", nullable = false)
  private String login;

  @Column(name = "listname", nullable = false)
  private String listName;

  /**
   * 
   */
  public UserSubscriptionEntity()
  {
    super();
  }

  /**
   * @param login
   * @param listName
   */
  public UserSubscriptionEntity(final String login, final String listName)
  {
    super();
    this.login = login;
    this.listName = listName;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(login).append(listName).toHashCode();
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof UserSubscriptionEntity))
    {
      return false;
    }
    final UserSubscriptionEntity castOther = (UserSubscriptionEntity) other;
    return new EqualsBuilder().append(login, castOther.getLogin()).append(listName, castOther.getListname()).isEquals();
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
  public String getLogin()
  {
    return login;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLogin(final String pLogin)
  {
    login = pLogin;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "UserSubscriptionEntity [id=" + id + ", login=" + login + ", listName=" + listName + "]";
  }

}
