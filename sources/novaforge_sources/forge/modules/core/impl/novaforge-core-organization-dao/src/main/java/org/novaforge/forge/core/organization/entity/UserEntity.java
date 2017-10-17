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
package org.novaforge.forge.core.organization.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.model.enumerations.UserStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author BILET-JC
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "ACTOR_USER")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
    @NamedQuery(name = "UserEntity.findAll", query = "SELECT u FROM UserEntity u"),
    @NamedQuery(name = "UserEntity.findByEmail",
        query = "SELECT u FROM UserEntity u left join fetch u.language WHERE u.email = :email"),
    @NamedQuery(name = "UserEntity.findByLogin",
        query = "SELECT u FROM UserEntity u left join fetch u.language WHERE u.login = :login"),
    @NamedQuery(name = "UserEntity.findByUUID",
        query = "SELECT u FROM UserEntity u left join fetch u.language WHERE u.uuid = :uuid"),
    @NamedQuery(name = "UserEntity.findCredential",
        query = "SELECT u.password FROM UserEntity u WHERE u.login = :login") })
public class UserEntity extends ActorEntity implements User
{
  private static final long serialVersionUID = 5763517453179436024L;

  @Column(name = "login", nullable = false, unique = true)
  private String            login;

  @Column(name = "password")
  private String            password;

  @Column(name = "firstname")
  private String            firstName;

  @Column(name = "name")
  private String            name;

  @Column(name = "email")
  private String            email;

  @Column(name = "status")
  @Enumerated
  private UserStatus        status;

  @ManyToOne(targetEntity = LanguageEntity.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "language_id")
  private Language          language;

  @Column(name = "last_connected", insertable = false, updatable = true)
  @Temporal(TemporalType.TIMESTAMP)
  private Date              lastConnected;

  @Column(name = "last_password_updated", insertable = false, updatable = true)
  @Temporal(TemporalType.TIMESTAMP)
  private Date              lastPasswordUpdated;

  @Column(name = "realm_type")
  @Enumerated
  private RealmType         realmType        = RealmType.USER;

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(getId()).append(getLogin()).toHashCode();
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
    return new EqualsBuilder().append(getId(), castOther.getId()).append(getLogin(), castOther.getLogin()).isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Size(min = 1)
  public String getLogin()
  {
    return login;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLogin(final String login)
  {
    this.login = login;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Size(min = 1)
  public String getPassword()
  {
    return password;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPassword(final String password)
  {
    this.password = password;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Size(min = 1)
  public String getFirstName()
  {
    return firstName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFirstName(final String firstName)
  {
    this.firstName = firstName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Size(min = 1)
  public String getName()
  {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setName(final String name)
  {
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public String getEmail()
  {
    return email;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEmail(final String email)
  {
    this.email = email;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserStatus getStatus()
  {
    return status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStatus(final UserStatus pStatus)
  {
    status = pStatus;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Language getLanguage()
  {
    return language;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLanguage(final Language language)
  {
    this.language = language;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getLastConnected()
  {
    return lastConnected;
  }

  /**
   * @param lastConnected
   */
  public void setLastConnected(final Date lastConnected)
  {
    this.lastConnected = lastConnected;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getLastPasswordUpdated()
  {
    return lastPasswordUpdated;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLastPasswordUpdated(final Date lastPasswordUpdated)
  {
    this.lastPasswordUpdated = lastPasswordUpdated;
  }

  @Override
  public RealmType getRealmType()
  {
    return realmType;
  }

  @Override
  public void setRealmType(final RealmType realmType)
  {
    this.realmType = realmType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "UserEntity [login=" + login + ", firstName=" + firstName + ", name=" + name + ", email=" + email
               + ", status=" + status + ", language=" + language + ", lastConnected=" + lastConnected
               + ", lastPasswordUpdated=" + lastPasswordUpdated + ", realmType=" + realmType + "]";
  }

}
