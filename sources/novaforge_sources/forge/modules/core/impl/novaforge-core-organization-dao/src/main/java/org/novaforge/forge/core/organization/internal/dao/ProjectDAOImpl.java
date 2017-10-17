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
package org.novaforge.forge.core.organization.internal.dao;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.novaforge.forge.core.organization.dao.ProjectDAO;
import org.novaforge.forge.core.organization.entity.BinaryFileEntity;
import org.novaforge.forge.core.organization.entity.ProjectEntity;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.List;

/**
 * JPA2 implementation of {@link ProjectDAO}
 * 
 * @author sbenoist
 * @author Guillaume Lamirand
 */
public class ProjectDAOImpl implements ProjectDAO
{

  /**
   * {@link EntityManager} injected by container
   */
  private EntityManager entityManager;
  private Predicate     whereClause;

  /**
   * Use by container to inject {@link EntityManager}
   * 
   * @param pEntityManager
   *          the entityManager to set
   */
  public void setEntityManager(final EntityManager pEntityManager)
  {
    entityManager = pEntityManager;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project newProject()
  {
    final ProjectEntity projectEntity = new ProjectEntity();
    projectEntity.setImage(new BinaryFileEntity());
    return projectEntity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project findByProjectId(final String pProjectId)
  {
    final TypedQuery<Project> q = entityManager.createNamedQuery("ProjectEntity.findByProjectId",
        Project.class);
    q.setParameter("projectId", pProjectId);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project findById(final String pProjectId, final ProjectOptions pProjectOptions)
  {
    final TypedQuery<Project> q = entityManager.createNamedQuery("ProjectEntity.findByProjectId",
        Project.class);
    if (pProjectOptions != null)
    {
      // Needed to retrieve also project's image on this request
      final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(q);
      if ((pProjectOptions.isRetrievedOrganization()) && (!pProjectOptions.isRetrievedImage()))
      {
        openJPAQuery.getFetchPlan().setMaxFetchDepth(1).addFetchGroup("project_organization");

      }
      else if ((pProjectOptions.isRetrievedImage()) && (!pProjectOptions.isRetrievedOrganization()))
      {
        openJPAQuery.getFetchPlan().setMaxFetchDepth(1).addFetchGroup("project_image");

      }
      else if ((pProjectOptions.isRetrievedOrganization()) && (pProjectOptions.isRetrievedImage()))
      {
        openJPAQuery.getFetchPlan().setMaxFetchDepth(1).addFetchGroup("project_all");
      }
    }
    q.setParameter("projectId", pProjectId);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Project> findAllByStatus(final ProjectStatus... pProjectStatus)
  {
    final TypedQuery<Project> q = entityManager.createNamedQuery("ProjectEntity.findByStatus", Project.class);
    q.setParameter("status", Arrays.asList(pProjectStatus));
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Project> findAllByStatus(final ProjectOptions pProjectOptions,
      final ProjectStatus... pProjectStatus)
  {
    String queryToUse = "ProjectEntity.findByStatus";
    TypedQuery<Project> q = null;
    if (pProjectOptions != null)
    {
      if (pProjectOptions.isRetrievedSystem())
      {
        queryToUse = "ProjectEntity.findByStatusWithSystem";
      }

      q = entityManager.createNamedQuery(queryToUse, Project.class);
      // Needed to retrieve also project's image on this request
      final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(q);
      if ((pProjectOptions.isRetrievedOrganization()) && (!pProjectOptions.isRetrievedImage()))
      {
        openJPAQuery.getFetchPlan().setMaxFetchDepth(1).addFetchGroup("project_organization");

      }
      else if ((pProjectOptions.isRetrievedImage()) && (!pProjectOptions.isRetrievedOrganization()))
      {
        openJPAQuery.getFetchPlan().setMaxFetchDepth(1).addFetchGroup("project_image");

      }
      else if ((pProjectOptions.isRetrievedOrganization()) && (pProjectOptions.isRetrievedImage()))
      {
        openJPAQuery.getFetchPlan().setMaxFetchDepth(1).addFetchGroup("project_all");
      }
    }
    else
    {
      q = entityManager.createNamedQuery(queryToUse, Project.class);
    }
    q.setParameter("status", Arrays.asList(pProjectStatus));
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project persist(final Project pProject)
  {

    entityManager.persist(pProject);
    entityManager.flush();
    return pProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project update(final Project pProject)
  {
    entityManager.merge(pProject);
    entityManager.flush();
    final OpenJPAEntityManager oem = OpenJPAPersistence.cast(entityManager);
    oem.evict(pProject); // will evict from data cache also
    return pProject;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final Project pProject)
  {
    final Project merged = entityManager.merge(pProject);
    entityManager.remove(merged);
    entityManager.flush();
  }

}
