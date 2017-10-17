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
package org.novaforge.forge.tools.deliverymanager.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.tools.deliverymanager.model.Permission;
import org.novaforge.forge.tools.deliverymanager.model.Role;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "ROLE")
@NamedQuery(name = "RoleEntity.findByName", query = "SELECT r FROM RoleEntity r WHERE r.name = :name")
public class RoleEntity implements Role, Serializable
{
  private static final long serialVersionUID = -7651309286258128735L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "name", unique = true, nullable = false)
  private String            name;

  @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH },
      targetEntity = PermissionEntity.class, fetch = FetchType.EAGER)
  @JoinTable(name = "ROLE_PERMISSION", joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"))
  private Set<Permission>   permissions      = new HashSet<Permission>();

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(name).toHashCode();
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof RoleEntity))
    {
      return false;
    }
    final RoleEntity castOther = (RoleEntity) other;
    return new EqualsBuilder().append(name, castOther.getName()).isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setName(final String pName)
  {
    name = pName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Permission> getPermissions()
  {
    return Collections.unmodifiableSet(permissions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPermission(final Permission pPermission)
  {
    permissions.add(pPermission);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePermission(final Permission pPermission)
  {
    permissions.remove(pPermission);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearPermissions()
  {
    permissions.clear();
  }

  @Override
  public String toString()
  {
    return "RoleEntity [name=" + name + ", permissions=" + permissions + "]";
  }

  /**
   * @return the id
   */
  public Long getId()
  {
    return id;
  }

}
