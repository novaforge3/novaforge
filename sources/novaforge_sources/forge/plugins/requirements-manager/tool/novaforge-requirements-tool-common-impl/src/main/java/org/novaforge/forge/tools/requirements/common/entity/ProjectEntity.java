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
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.IRepository;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Guillaume Morin
 */
@Entity
@Table(name = "PROJECT", uniqueConstraints = @UniqueConstraint(columnNames = { "projectId" }))
@NamedQuery(name = "ProjectEntity.findProjectByID",
    query = "SELECT e FROM ProjectEntity e  WHERE e.projectId = :projectID")
public class ProjectEntity implements IProject
{
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long             id;

  @Column(name = "projectId", nullable = false, updatable = false)
  private String           projectId;

  @Column(name = "description", nullable = true)
  private String           description;

  @Column(name = "name", nullable = false)
  private String           name;

  @Column(name = "codeRepositoryPath", nullable = true)
  private String           codeRepositoryPath;

  @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE,
      CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH },
      targetEntity = RepositoryEntity.class, orphanRemoval = true)
  private Set<IRepository> repositories = new HashSet<IRepository>();

  public Long getId()
  {
    return id;
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
               + ", codeRespositoryPath=" + codeRepositoryPath + ", repositories=" + repositories + "]";
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

  @Override
  public Set<IRepository> getRepositories()
  {
    return Collections.unmodifiableSet(repositories);
  }

  @Override
  public void addRepository(final IRepository pRepository)
  {
    pRepository.setProject(this);
    repositories.add(pRepository);
  }

  @Override
  public void deleteRepository(final IRepository pRepository)
  {
    repositories.remove(pRepository);
    pRepository.setProject(null);
  }

  @Override
  public IRepository getRepository(final String pRepositoryURI)
  {
    IRepository found = null;
    for (final IRepository repository : repositories)
    {
      if (repository.getURI().equals(pRepositoryURI))
      {
        found = repository;
        break;
      }
    }

    return found;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCodeRepositoryPath()
  {
    return codeRepositoryPath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCodeRepositoryPath(final String pCodeRepositoryPath)
  {
    codeRepositoryPath = pCodeRepositoryPath;
  }

  /**
   * @param pRepositories
   *          the repositories to set
   */
  public void setRepositories(final Set<IRepository> pRepositories)
  {
    repositories = pRepositories;
  }

}
