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

import org.novaforge.forge.core.organization.model.RecoveryPassword;
import org.novaforge.forge.core.organization.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "RECOVERY_PASSWORD", uniqueConstraints = @UniqueConstraint(columnNames = { "token" }))
@NamedQueries({
    @NamedQuery(name = "RecoveryPasswordEntity.findByToken",
        query = "SELECT u FROM RecoveryPasswordEntity u WHERE u.token = :token"),
    @NamedQuery(name = "RecoveryPasswordEntity.findByUser",
    query = "SELECT u FROM RecoveryPasswordEntity u WHERE u.user.email = :email")
})
public class RecoveryPasswordEntity implements Serializable, RecoveryPassword
{

  private static final long serialVersionUID = 1710043403368273700L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
  @JoinColumn(name = "user_id", nullable = false)
  private User              user;

  @Column(name = "token", nullable = false)
  private String            token;

  @Column(name = "expiration_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date              expirationDate;

  @Column(name = "enabled", nullable = false)
  private boolean           enabled          = true;

  public RecoveryPasswordEntity()
  {
    super();

    // provide the expiration date (24h)
    final Calendar calendar = new GregorianCalendar();
    calendar.add(Calendar.DATE, 1);
    expirationDate = calendar.getTime();

    // provide the UUID token
    token = UUID.randomUUID().toString();
  }

  public Long getId()
  {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User getUser()
  {
    return user;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUser(final User pUser)
  {
    user = pUser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getExpirationDate()
  {
    return expirationDate;
  }

  /**
   * @param expirationDate
   *          the expirationDate to set
   */
  public void setExpirationDate(final Date expirationDate)
  {
    this.expirationDate = expirationDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getToken()
  {
    return UUID.fromString(token);
  }

  /**
   * @param token
   *          the token to set
   */
  public void setToken(final UUID token)
  {
    this.token = token.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEnabled()
  {
    return enabled;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void enable(final boolean pEnable)
  {
    enabled = pEnable;
  }

}
