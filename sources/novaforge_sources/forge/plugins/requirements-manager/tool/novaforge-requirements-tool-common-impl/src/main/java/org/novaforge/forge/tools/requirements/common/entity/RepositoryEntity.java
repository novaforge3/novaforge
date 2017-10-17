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
package org.novaforge.forge.tools.requirements.common.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.tools.requirements.common.model.ERepositoryType;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.IRepository;

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
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "REPOSITORY", uniqueConstraints = @UniqueConstraint(columnNames = { "uri", "project_id" }))
@NamedQueries({
    @NamedQuery(name = "RepositoryEntity.findRepositoriesByType",
        query = "SELECT r FROM RepositoryEntity r  WHERE r.project.projectId = :projectID AND r.type = :type"),
    @NamedQuery(name = "RepositoryEntity.findRepositoryByURI",
        query = "SELECT r FROM RepositoryEntity r  WHERE r.project.projectId = :projectID AND r.URI = :uri") })
public class RepositoryEntity implements IRepository, Serializable
{

  private static final long serialVersionUID = 8238725488634657173L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "description", nullable = true)
  private String            description;

  @Column(name = "uri", nullable = false)
  private String            URI;

  @Column(name = "type", nullable = false)
  private String            type;

  /***********************************
   * ------- Tables Relations ------
   **********************************/

  // Linked to one project
  @ManyToOne(targetEntity = ProjectEntity.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "project_id", nullable = false, insertable = true, updatable = true)
  private IProject          project;

  /**
   * {@inheritDoc}
   */
  @Override
  public Long getId()
  {
    return id;
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
  public String getURI()
  {
    return URI;
  }

  @Override
  public void setURI(final String pURI)
  {
    URI = pURI;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ERepositoryType getType()
  {
    ERepositoryType returned = null;
    if (type != null)
    {
      returned = ERepositoryType.valueOf(type);
    }
    return returned;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setType(final ERepositoryType pType)
  {
    if (pType != null)
    {
      type = pType.name();
    }
  }

  @Override
  public IProject getProject()
  {
    return project;
  }

  @Override
  public void setProject(final IProject pProject)
  {
    project = pProject;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(URI).append(project).toHashCode();
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof RepositoryEntity))
    {
      return false;
    }
    final RepositoryEntity castOther = (RepositoryEntity) other;
    return new EqualsBuilder().append(URI, castOther.URI).append(project, castOther.getProject()).isEquals();
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return "RepositoryEntity [id=" + id + ", description=" + description + ", URI=" + URI + ", projectId=" + project
                                                                                                                 .getProjectId()
               + ", type=" + type + "]";
  }

}
