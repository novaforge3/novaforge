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
import org.novaforge.forge.core.organization.model.ProjectElement;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author lamirang
 */
@Entity
@Table(name = "ROLE", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "element_id" }))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
    @NamedQuery(name = "RoleEntity.findByNameAndProjectElement",
        query = "SELECT p FROM RoleEntity p WHERE p.element.elementId = :elementId AND p.name = :name"),
    @NamedQuery(name = "RoleEntity.getMaxOrder",
        query = "SELECT COALESCE(MAX(p.order), 0) FROM RoleEntity p WHERE p.element.elementId = :elementId"),
    @NamedQuery(name = "RoleEntity.findByOrderAndProjectElement",
        query = "SELECT p FROM RoleEntity p WHERE p.element.elementId = :elementId AND p.order = :order"),
    @NamedQuery(
        name = "RoleEntity.updateOrder",
        query = "UPDATE RoleEntity r SET r.order = r.order - 1 WHERE r.order > :order AND r.element.elementId = :elementId") })
public class RoleEntity implements Role, Serializable
{

  /**
    * 
    */
  private static final long    serialVersionUID = 1885487647852049037L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long                 id;

  @Column(name = "name", nullable = false, length = 250)
  private String               name;

  @Lob
  @Column(name = "description", nullable = true)
  private String               description;

  // Has to refer explicitly ProjectElementEntity in order to generate correct metamodel
  @ManyToOne(fetch = FetchType.LAZY, targetEntity = ProjectElementEntity.class)
  @JoinColumn(name = "element_id", nullable = false)
  private ProjectElementEntity element;

  @Column(name = "role_order", nullable = false)
  private Integer              order;

  @Column(name = "realm_type", nullable = true)
  @Enumerated
  private RealmType            realmType        = RealmType.USER;

  public Long getId()
  {
    return id;
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
  @Size(max = 1024)
  public String getDescription()
  {
    return description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDescription(final String description)
  {
    this.description = description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getOrder()
  {
    return order;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setOrder(final Integer pOrder)
  {
    order = pOrder;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RealmType getRealmType()
  {
    return realmType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRealmType(final RealmType pRealmType)
  {
    realmType = pRealmType;
  }

  /**
   * @return the element
   */
  public ProjectElement getElement()
  {
    return element;
  }

  /**
   * @param element
   *          the element to set
   */
  public void setElement(final ProjectElement element)
  {
    this.element = (ProjectElementEntity) element;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(id).append(name).toHashCode();
  }

  /**
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(Object)
   */
  @Override
  public boolean equals(final Object pOther)
  {
    if (this == pOther)
    {
      return true;
    }
    if (!(pOther instanceof RoleEntity))
    {
      return false;
    }
    final RoleEntity castOther = (RoleEntity) pOther;
    return new EqualsBuilder().append(id, castOther.id).append(name, castOther.name).isEquals();
  }

  @Override
  public String toString()
  {
    return "RoleEntity [" + "id=" + id + "," + "name=" + name + "," + "description=" + description + "," + "order="
               + order + "]";
  }

  /**
   * Take care that order=1 > order=10 (inverse to the integers order)
   */
  @Override
  public int compareTo(final Role pRole)
  {
    return -(order.compareTo(pRole.getOrder()));
  }
}
