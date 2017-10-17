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

import org.novaforge.forge.core.organization.dao.ProjectElementDAO;
import org.novaforge.forge.core.organization.entity.ProjectElementEntity;
import org.novaforge.forge.core.organization.entity.ProjectElementEntity_;
import org.novaforge.forge.core.organization.model.ProjectElement;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

/**
 * JPA2 implementation of {@link ProjectElementDAO}
 * 
 * @author Guillaume Lamirand
 * @see ProjectElementEntity
 */
public class ProjectElementDAOImpl implements ProjectElementDAO
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
  public ProjectElement findByElementId(final String pElementId)
  {
    final TypedQuery<ProjectElement> q = entityManager.createNamedQuery(
        "ProjectElementEntity.findByElementId", ProjectElement.class);
    q.setParameter("elementId", pElementId);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existId(final String pElementId)
  {

    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    final Root<ProjectElementEntity> entityRoot = countCriteria.from(ProjectElementEntity.class);
    countCriteria.select(builder.count(entityRoot));

    final Predicate namePredicate = builder
        .equal(entityRoot.get(ProjectElementEntity_.elementId), pElementId);
    countCriteria.where(namePredicate);
    final long count = entityManager.createQuery(countCriteria).getSingleResult();

    return (count > 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existName(final String pElementName)
  {
    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    final Root<ProjectElementEntity> entityRoot = countCriteria.from(ProjectElementEntity.class);
    countCriteria.select(builder.count(entityRoot));

    final Predicate namePredicate = builder.equal(entityRoot.get(ProjectElementEntity_.name), pElementName);
    countCriteria.where(namePredicate);
    final long count = entityManager.createQuery(countCriteria).getSingleResult();

    return (count > 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int updateLastModified(final String pElementId)
  {
    final Query q = entityManager.createNamedQuery("ProjectElementEntity.updateLastModified");
    q.setParameter("date", new Date());
    q.setParameter("elementId", pElementId);
    return q.executeUpdate();

  }
}
