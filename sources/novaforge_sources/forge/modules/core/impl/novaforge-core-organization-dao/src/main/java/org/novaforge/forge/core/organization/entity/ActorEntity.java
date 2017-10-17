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
/**
 * 
 */
package org.novaforge.forge.core.organization.entity;

import org.novaforge.forge.core.organization.model.Actor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

/**
 * This entity describes an concret {@link Actor}
 * 
 * @author BILET-JC
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "ACTOR")
@Inheritance(strategy = InheritanceType.JOINED)
public class ActorEntity implements Actor
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = -8963415586143099240L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "uuid", unique = true, nullable = false, insertable = true, updatable = false)
  private String            uuid;

  @Column(name = "created", nullable = false, insertable = true, updatable = false)
  private Date              created;

  /**
   * This will be called before a persist and flush event
   */
  @PrePersist
  public void onPersist()
  {
    setCreated(new Date());
    setUuid(UUID.randomUUID());
  }

  /**
   * @return {@link ActorEntity} technical id
   * @see ActorEntity#id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getUuid()
  {
    return UUID.fromString(uuid);
  }

  /**
   * @param pUUID
   *          the uuid to set
   */
  public void setUuid(final UUID pUUID)
  {
    uuid = pUUID.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getCreated()
  {
    return created;
  }

  /**
   * Allows to set the creation date
   * 
   * @param pCreated
   *          the date to set
   */
  public void setCreated(final Date pCreated)
  {
    created = pCreated;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((id == null) ? 0 : id.hashCode());
    result = (prime * result) + ((uuid == null) ? 0 : uuid.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (getClass() != obj.getClass())
    {
      return false;
    }
    final ActorEntity other = (ActorEntity) obj;
    if (id == null)
    {
      if (other.id != null)
      {
        return false;
      }
    }
    else if (!id.equals(other.id))
    {
      return false;
    }
    if (uuid == null)
    {
      if (other.uuid != null)
      {
        return false;
      }
    }
    else if (!uuid.equals(other.uuid))
    {
      return false;
    }
    return true;
  }

}
