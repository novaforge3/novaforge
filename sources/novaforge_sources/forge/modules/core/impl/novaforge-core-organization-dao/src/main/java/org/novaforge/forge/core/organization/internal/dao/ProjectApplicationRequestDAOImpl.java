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

import org.novaforge.forge.core.organization.dao.ProjectApplicationRequestDAO;
import org.novaforge.forge.core.organization.entity.ApplicationEntity_;
import org.novaforge.forge.core.organization.entity.ProjectApplicationRequestEntity;
import org.novaforge.forge.core.organization.entity.ProjectApplicationRequestEntity_;
import org.novaforge.forge.core.organization.entity.ProjectElementEntity_;
import org.novaforge.forge.core.organization.model.ProjectApplicationRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

/**
 * JPA2 implementation of {@link ProjectApplicationRequestDAO}
 * 
 * @author Guillaume Lamirand
 */
public class ProjectApplicationRequestDAOImpl implements ProjectApplicationRequestDAO
{
  /**
   * {@link EntityManager} injected by container
   */
  private EntityManager entityManager;

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
  public ProjectApplicationRequest newProjectApplicationRequest()
  {
    return new ProjectApplicationRequestEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectApplicationRequest findByProjectAndApp(final String pProjectId, final UUID pInstanceUUID)
  {
    final TypedQuery<ProjectApplicationRequest> q = entityManager.createNamedQuery(
        "ProjectApplicationRequestEntity.findByProjectAndApp", ProjectApplicationRequest.class);
    q.setParameter("projectId", pProjectId);
    q.setParameter("instanceUUID", pInstanceUUID.toString());

    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectApplicationRequest> findByProject(final String pProjectId)
  {
    final TypedQuery<ProjectApplicationRequest> q = entityManager.createNamedQuery("ProjectApplicationRequestEntity.findByProject",
                                                                                   ProjectApplicationRequest.class);
    q.setParameter("projectId", pProjectId);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectApplicationRequest> findByPlugin(final UUID pUUID)
  {
    final TypedQuery<ProjectApplicationRequest> q = entityManager.createNamedQuery(
        "ProjectApplicationRequestEntity.findByPlugin", ProjectApplicationRequest.class);
    q.setParameter("pluginUUID", pUUID.toString());
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectApplicationRequest findOldestByPlugin(final UUID pPluginUUID)
  {
    final TypedQuery<ProjectApplicationRequest> q = entityManager.createNamedQuery(
        "ProjectApplicationRequestEntity.findOldest", ProjectApplicationRequest.class);
    q.setParameter("pluginUUID", pPluginUUID.toString());
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectApplicationRequest> findByProjectAndPlugin(final String pProjectId, final UUID pUUID)
  {
    final TypedQuery<ProjectApplicationRequest> q = entityManager.createNamedQuery("ProjectApplicationRequestEntity.findByProjectAndPlugin",
                                                                                   ProjectApplicationRequest.class);
    q.setParameter("projectId", pProjectId);
    q.setParameter("pluginUUID", pUUID.toString());

    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectApplicationRequest persist(final ProjectApplicationRequest pProjectApplicationRequest)
  {
    entityManager.merge(pProjectApplicationRequest);
    entityManager.flush();
    return pProjectApplicationRequest;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final ProjectApplicationRequest pProjectApplicationRequest)
  {
    entityManager.remove(pProjectApplicationRequest);
    entityManager.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectApplicationRequest newRequest()
  {
    return new ProjectApplicationRequestEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existRequest(final String pProjectId, final UUID pPluginUUID)
  {

    final CriteriaBuilder     builder       = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    final Root<ProjectApplicationRequestEntity> entityRoot = countCriteria.from(ProjectApplicationRequestEntity.class);
    countCriteria.select(builder.count(entityRoot));

    final Predicate pluginPredicate = builder.equal(entityRoot.get(ProjectApplicationRequestEntity_.app)
                                                              .get(ApplicationEntity_.pluginUUID),
                                                    pPluginUUID.toString());
    final Predicate projectPredicate = builder.equal(entityRoot.get(ProjectApplicationRequestEntity_.project)
                                                               .get(ProjectElementEntity_.elementId), pProjectId);
    countCriteria.where(builder.and(pluginPredicate, projectPredicate));
    final long count = entityManager.createQuery(countCriteria).getSingleResult();

    return (count > 0);
  }
}
