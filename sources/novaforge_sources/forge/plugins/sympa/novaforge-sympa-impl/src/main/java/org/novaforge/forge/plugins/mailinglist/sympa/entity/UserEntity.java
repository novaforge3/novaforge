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
import org.novaforge.forge.plugins.mailinglist.sympa.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "USER_T")
@NamedQueries({
    @NamedQuery(name = "UserEntity.findByLogin", query = "SELECT u FROM UserEntity u WHERE u.login = :login"),
    @NamedQuery(name = "UserEntity.findByEmail", query = "SELECT u FROM UserEntity u WHERE u.email = :email") })
public class UserEntity implements User, Serializable
{
  private static final long serialVersionUID = -5390816066724137494L;

  @Id
  @Column(name = "login", nullable = false)
  private String            login;

  @Column(name = "email", nullable = false, unique = true)
  private String            email;

  public UserEntity()
  {
    super();
  }

  public UserEntity(final String pLogin, final String pEmail)
  {
    super();
    login = pLogin;
    email = pEmail;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(login).toHashCode();
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
    return new EqualsBuilder().append(login, castOther.getLogin()).isEquals();
  }

  /**
   * @inheritDoc
   */
  @Override
  public String getLogin()
  {
    return login;
  }

  /**
   * @inheritDoc
   */
  @Override
  public void setLogin(final String pLogin)
  {
    login = pLogin;
  }

  /**
   * @inheritDoc
   */
  @Override
  public String getEmail()
  {
    return email;
  }

  /**
   * @inheritDoc
   */
  @Override
  public void setEmail(final String pEmail)
  {
    email = pEmail;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "UserEntity [login=" + login + ", email=" + email + "]";
  }

}
