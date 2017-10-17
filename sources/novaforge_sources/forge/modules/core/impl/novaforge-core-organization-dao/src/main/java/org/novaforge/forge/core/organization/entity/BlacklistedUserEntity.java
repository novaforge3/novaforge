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

import org.novaforge.forge.core.organization.model.BlacklistedUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * This describes a persisted {@link BlacklistedUser}
 */
@Entity
@Table(name = "USER_BLACKLIST")
@NamedQuery(name = "BlacklistedUserEntity.findByLogin",
    query = "SELECT u FROM BlacklistedUserEntity u WHERE u.login = :login")
public class BlacklistedUserEntity implements BlacklistedUser
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 1369340928614930431L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "login", nullable = false, unique = true)
  private String            login;

  @Column(name = "created", nullable = false, insertable = true, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date              creationDate;

  @Column(name = "email", nullable = false)
  private String            email;

  /**
   * This will be called before a persist and flush event
   */
  @PrePersist
  public void onPersist()
  {
    setCreationDate(new Date());
  }

  /**
   * Retrieve technical id
   * 
   * @return technical id
   */
  public Long getId()
  {
    return id;
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
  public void setLogin(final String login)
  {
    this.login = login;
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
  public Date getCreationDate()
  {
    return creationDate;
  }

  /**
   * Set the date when user has been blacklisted
   * 
   * @param pDate
   *          the date to set
   */
  public void setCreationDate(final Date pDate)
  {
    creationDate = pDate;
  }
}
