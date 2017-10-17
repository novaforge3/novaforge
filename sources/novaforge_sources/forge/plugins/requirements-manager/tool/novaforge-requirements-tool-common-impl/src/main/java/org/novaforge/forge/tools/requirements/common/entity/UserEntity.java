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
package org.novaforge.forge.tools.requirements.common.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.tools.requirements.common.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "RUSER")
@NamedQuery(name = "UserEntity.findByLogin", query = "SELECT u FROM UserEntity u WHERE u.login = :login")
public class UserEntity implements User, Serializable
{
  private static final long serialVersionUID = 1286619053002665183L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "login", unique = true, nullable = false)
  private String            login;

  @Column(name = "firstname")
  private String            firstname;

  @Column(name = "lastname")
  private String            lastname;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFirstname()
  {
    return firstname;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFirstname(final String pFirstName)
  {
    firstname = pFirstName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLastname()
  {
    return lastname;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLastname(final String pLastname)
  {
    lastname = pLastname;
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

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(getLogin()).toHashCode();
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof UserEntity))
    {
      return false;
    }
    final UserEntity castOther = (UserEntity) other;
    return new EqualsBuilder().append(getLogin(), castOther.getLogin()).isEquals();
  }

  @Override
  public String toString()
  {
    return "UserEntity [login=" + login + ", firstname=" + firstname + ", lastname=" + lastname + "]";
  }

  /**
   * @return the id
   */
  public Long getId()
  {
    return id;
  }
}
