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
import org.novaforge.forge.tools.deliverymanager.model.Project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "PROJECT", uniqueConstraints = @UniqueConstraint(columnNames = { "projectId" }))
@NamedQuery(name = "ProjectEntity.findProjectById",
    query = "SELECT e FROM ProjectEntity e  WHERE e.projectId = :projectId")
public class ProjectEntity implements Project
{
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long   id;

  @Column(name = "projectId", nullable = false, updatable = false)
  private String projectId;

  @Column(name = "description", nullable = true)
  private String description;

  @Column(name = "name", nullable = false)
  private String name;

  public ProjectEntity()
  {
    super();
  }

  public ProjectEntity(final String pProjectId, final String pName, final String pDescription)
  {
    super();
    projectId = pProjectId;
    description = pDescription;
    name = pName;
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(getProjectId()).toHashCode();
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof ProjectEntity))
    {
      return false;
    }
    final ProjectEntity castOther = (ProjectEntity) other;
    return new EqualsBuilder().append(getProjectId(), castOther.getProjectId()).isEquals();
  }

  @Override
  public String toString()
  {
    return "ProjectEntity [id=" + id + ", projectId=" + projectId + ", description=" + description + ", name=" + name
               + "]";
  }

  @Override
  public String getProjectId()
  {
    return projectId;
  }

  @Override
  public void setProjectId(final String pProjectId)
  {
    projectId = pProjectId;
  }

  @Override
  public String getDescription()
  {
    return description;
  }

  @Override
  public void setDescription(final String pDescription)
  {
    description = pDescription;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void setName(final String pName)
  {
    name = pName;
  }

  public Long getId()
  {
    return id;
  }
}
