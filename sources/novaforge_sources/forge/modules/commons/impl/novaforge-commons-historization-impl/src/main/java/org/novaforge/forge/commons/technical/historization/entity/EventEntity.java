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
package org.novaforge.forge.commons.technical.historization.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.commons.technical.historization.model.Event;
import org.novaforge.forge.commons.technical.historization.model.EventLevel;
import org.novaforge.forge.commons.technical.historization.model.EventType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "EVENT")
@NamedQueries({
    @NamedQuery(name = "EventEntity.findAllEvents",
        query = "SELECT e FROM EventEntity e ORDER BY e.date DESC"),
    @NamedQuery(name = "EventEntity.findAllEventsBeforeDate",
        query = "SELECT e FROM EventEntity e WHERE e.date <= :date ORDER BY e.date DESC"),
    @NamedQuery(name = "EventEntity.findEventsByActor",
        query = "SELECT e FROM EventEntity e WHERE e.actor = :actor ORDER BY e.date DESC"),
    @NamedQuery(name = "EventEntity.findEventsByLevel",
        query = "SELECT e FROM EventEntity e WHERE e.level = :level ORDER BY e.date DESC"),
    @NamedQuery(name = "EventEntity.findEventsByType",
        query = "SELECT e FROM EventEntity e WHERE e.type = :type ORDER BY e.date DESC"),
    @NamedQuery(name = "EventEntity.findEventsByKeyword",
        query = "SELECT e FROM EventEntity e WHERE e.details LIKE :keyword ORDER BY e.date DESC"),
    @NamedQuery(name = "EventEntity.findEventsByDates",
        query = "SELECT e FROM EventEntity e WHERE e.date BETWEEN :begin AND :end ORDER BY e.date DESC"),
    @NamedQuery(name = "EventEntity.deleteEventsFromDate",
        query = "DELETE FROM EventEntity e WHERE e.date < :date") })
public class EventEntity implements Event, Serializable
{
  /**
    * 
    */
  private static final long serialVersionUID = -2527249173175761365L;

  @Id
  @Column(name = "uuid", nullable = false, updatable = false)
  private String            uuid;

  @Column(name = "date_event", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date              date;

  @Column(name = "type", nullable = false)
  @Enumerated
  private EventType         type;

  @Column(name = "level", nullable = false)
  @Enumerated
  private EventLevel        level;

  @Column(name = "actor", nullable = false)
  private String            actor;

  @Column(length = 2048, name = "details", nullable = true)
  private String            details;

  /**
   * Default constructor used by JPA2
   */
  public EventEntity()
  {
    super();
  }

  public EventEntity(final String pUuid)
  {
    super();
    uuid = pUuid;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(uuid).toHashCode();
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof EventEntity))
    {
      return false;
    }
    final EventEntity castOther = (EventEntity) other;
    return new EqualsBuilder().append(uuid, castOther.getUuid()).isEquals();
  }

  @Override
  public String toString()
  {
    return "EventEntity [actor=" + actor + ", date=" + date + ", details=" + details + ", id=" + uuid + ", level="
               + level.getLabel() + ", type=" + type.getLabel() + "]";
  }

  @Override
  public String getUuid()
  {
    return uuid;
  }

  @Override
  public Date getDate()
  {
    return date;
  }

  public void setDate(final Date pDate)
  {
    date = pDate;
  }

  @Override
  public String getActor()
  {
    return actor;
  }

  @Override
  public EventType getType()
  {
    return type;
  }

  public void setType(final EventType pType)
  {
    type = pType;
  }

  @Override
  public EventLevel getLevel()
  {
    return level;
  }

  @Override
  public String getDetails()
  {
    return details;
  }

  public void setDetails(final String pDetails)
  {
    details = pDetails;
  }

  public void setLevel(final EventLevel pLevel)
  {
    level = pLevel;
  }

  public void setActor(final String pActor)
  {
    actor = pActor;
  }

}
