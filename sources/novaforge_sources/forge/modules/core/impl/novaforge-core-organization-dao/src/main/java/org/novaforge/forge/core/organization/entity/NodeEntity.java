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
import org.novaforge.forge.core.organization.model.Node;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Concret implementation of {@link Node}
 * 
 * @author Guillaume Lamirand
 * @see ApplicationEntity
 * @see SpaceEntity
 */
@Entity
@Table(name = "NODE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "NodeEntity.findByUri", query = "SELECT p FROM NodeEntity p WHERE p.uri = :uri")
public abstract class NodeEntity implements Node, Serializable
{
  /**
   * Serial version id
   */
  private static final long serialVersionUID = -231392111503862345L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "uri", nullable = false, unique = true)
  private String            uri;

  @Column(name = "name", nullable = false)
  private String            name;

  @Column(name = "description")
  private String            description;

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(id).append(uri).toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof NodeEntity))
    {
      return false;
    }
    final NodeEntity castOther = (NodeEntity) other;
    return new EqualsBuilder().append(id, castOther.getId()).append(uri, castOther.getUri()).isEquals();
  }

  /**
   * Return the entity id
   *
   * @see NodeEntity#id
   * @return entity id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * Set the entity id
   *
   * @param pId
   *          represents the id to set
   * @see NodeEntity#id
   */
  public void setId(final Long pId)
  {
    id = pId;
  }

  /**
   * {@inheritDoc}
   *
   * @see NodeEntity#uri
   */
  @Override
  @NotNull
  @Size(min = 1)
  public String getUri()
  {
    return uri;
  }

  /**
   * {@inheritDoc}
   *
   * @see NodeEntity#uri
   */
  @Override
  public void setUri(final String pUri)
  {
    uri = pUri;
  }

  /**
   * {@inheritDoc}
   *
   * @see NodeEntity#name
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
   * 
   * @see NodeEntity#name
   */
  @Override
  public void setName(final String name)
  {
    this.name = name;
  }

  /**
   * {@inheritDoc}
   *
   * @see NodeEntity#description
   */
  @Override
  public String getDescription()
  {
    return description;
  }

  /**
   * {@inheritDoc}
   * 
   * @see NodeEntity#description
   */
  @Override
  public void setDescription(final String pDescription)
  {
    description = pDescription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "NodeEntity [id=" + id + ", uri=" + uri + ", name=" + name + "]";
  }

}
