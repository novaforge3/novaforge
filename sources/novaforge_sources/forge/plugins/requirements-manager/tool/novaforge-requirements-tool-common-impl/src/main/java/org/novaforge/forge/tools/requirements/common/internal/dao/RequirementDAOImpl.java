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
package org.novaforge.forge.tools.requirements.common.internal.dao;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.novaforge.forge.tools.requirements.common.dao.RequirementDAO;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import serp.bytecode.Project;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Guillaume Morin
 */

public class RequirementDAOImpl implements RequirementDAO
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

  @Override
  public IRequirement loadRequirementTree(final IRequirement pRequirement)
  {
    final TypedQuery<IRequirement> q = entityManager.createNamedQuery(
        "RequirementEntity.loadRequirementTree", IRequirement.class);
    final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(q);
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("versions_all");
    q.setParameter("ID", pRequirement.getId());

    return q.getSingleResult();
  }

  @Override
  public IRequirement loadRequirementTree(final String pFunctionalReference)
  {
    final TypedQuery<IRequirement> q = entityManager.createNamedQuery(
        "RequirementEntity.loadRequirementTreeByReference", IRequirement.class);
    final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(q);
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("versions_all");
    q.setParameter("REF", pFunctionalReference);
    return q.getSingleResult();

  }

  @Override
  public Set<IRequirement> loadRequirementsByRepository(final IRepository pRepository)
  {
    final TypedQuery<IRequirement> q = entityManager.createNamedQuery(
        "RequirementEntity.loadRequirementsByRepository", IRequirement.class);
    final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(q);
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("versions_all");
    q.setParameter("repositoryURI", pRepository.getURI());
    q.setParameter("projectID", pRepository.getProject().getProjectId());
    return new HashSet<IRequirement>(q.getResultList());
  }

  @Override
  public void deleteRequirementByRef(final String pReference)
  {
    final Query query = entityManager.createNamedQuery("RequirementEntity.deleteByReference");
    query.setParameter("REF", pReference);
    query.executeUpdate();
  }

  @Override
  public IRequirement loadRequirementTreeByName(final String pProjectId, final String pName)
  {
    final TypedQuery<IRequirement> q = entityManager.createNamedQuery(
        "RequirementEntity.loadRequirementTreeByName", IRequirement.class);
    final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(q);
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("versions_all");
    q.setParameter("name", pName);
    q.setParameter("projectID", pProjectId);
    return q.getSingleResult();
  }

  @Override
  public IRequirement loadRequirementTreeById(final String pRequirementId)
  {
    final TypedQuery<IRequirement> q = entityManager.createNamedQuery(
        "RequirementEntity.loadRequirementTreeById", IRequirement.class);
    final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(q);
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("versions_all");
    q.setParameter("ID", new Integer(pRequirementId).longValue());
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRequirement persist(final IRequirement pRequirement)
  {
    entityManager.persist(pRequirement);
    entityManager.flush();
    return pRequirement;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRequirement update(final IRequirement pRequirement)
  {
    entityManager.merge(pRequirement);
    entityManager.flush();
    final OpenJPAEntityManager oem = OpenJPAPersistence.cast(entityManager);
    oem.evict(pRequirement.findLastRequirementVersion()); // will evict from data cache also
    return pRequirement;
  }

}
