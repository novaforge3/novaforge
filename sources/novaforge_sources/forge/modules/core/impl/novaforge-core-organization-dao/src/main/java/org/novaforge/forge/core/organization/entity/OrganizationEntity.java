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

import org.novaforge.forge.core.organization.model.Organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author BILET-JC
 */
@Entity
@Table(name = "ORGANIZATION")
@NamedQuery(name = "OrganizationEntity.findByName",
    query = "SELECT o FROM OrganizationEntity o WHERE o.name = :name")
public class OrganizationEntity implements Organization
{

  /**
    * 
    */
  private static final long serialVersionUID = -2048846679566230360L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "name", length = 26, nullable = false, unique = true)
  private String            name;

  /**
    * 
    */
  public OrganizationEntity()
  {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Size(min = 2, max = 26)
  @Pattern(regexp = "[A-Z0-9_]*", message = "Must be composed by alphanumericals characters and underscore")
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
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((id == null) ? 0 : id.hashCode());
    result = (prime * result) + ((name == null) ? 0 : name.hashCode());
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
    final OrganizationEntity other = (OrganizationEntity) obj;
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
    if (name == null)
    {
      if (other.name != null)
      {
        return false;
      }
    }
    else if (!name.equals(other.name))
    {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "OrganizationEntity [id=" + id + ", name=" + name + "]";
  }

}
