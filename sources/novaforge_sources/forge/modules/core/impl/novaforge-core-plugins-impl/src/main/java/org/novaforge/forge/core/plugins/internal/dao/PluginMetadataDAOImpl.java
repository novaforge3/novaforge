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
package org.novaforge.forge.core.plugins.internal.dao;

import org.novaforge.forge.core.plugins.dao.PluginMetadataDAO;
import org.novaforge.forge.core.plugins.domain.core.PluginPersistenceMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.entity.PluginMetadataEntity;
import org.novaforge.forge.core.plugins.entity.PluginMetadataEntity_;
import org.novaforge.forge.core.plugins.entity.PluginQueuesEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public class PluginMetadataDAOImpl implements PluginMetadataDAO
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
  public PluginPersistenceMetadata findByUUID(final UUID pUUID) throws NoResultException
  {
    final TypedQuery<PluginPersistenceMetadata> q = entityManager.createNamedQuery("PluginMetadataEntity.findByUUID",
                                                                                   PluginPersistenceMetadata.class);
    q.setParameter("uuid", pUUID.toString());
    return q.getSingleResult();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginPersistenceMetadata> findByCategory(final String... pCategoriesId)
  {
    final TypedQuery<PluginPersistenceMetadata> q = entityManager.createNamedQuery(
        "PluginMetadataEntity.findByCategory", PluginPersistenceMetadata.class);
    q.setParameter("categories", Arrays.asList(pCategoriesId));
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginPersistenceMetadata> findByCategoryAndStatusAndAvailability(final String pCategoryId,
      final PluginStatus pStatus, final boolean pAvailability)
  {
    final TypedQuery<PluginPersistenceMetadata> q = entityManager.createNamedQuery(
        "PluginMetadataEntity.findByCategoryAndStatusAndAvailability", PluginPersistenceMetadata.class);
    q.setParameter("category", pCategoryId);
    q.setParameter("available", pAvailability);
    q.setParameter("status", pStatus);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginPersistenceMetadata> findByType(final String pLabel)
  {
    final TypedQuery<PluginPersistenceMetadata> q = entityManager.createNamedQuery(
        "PluginMetadataEntity.findByType", PluginPersistenceMetadata.class);
    q.setParameter("label", pLabel);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginPersistenceMetadata> findByStatus(final PluginStatus pStatus)
  {
    final TypedQuery<PluginPersistenceMetadata> q = entityManager.createNamedQuery(
        "PluginMetadataEntity.findByStatus", PluginPersistenceMetadata.class);
    q.setParameter("status", pStatus);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginPersistenceMetadata> findByStatusAndAvailabitly(final PluginStatus pStatus,
      final boolean pAvailability)
  {
    final TypedQuery<PluginPersistenceMetadata> q = entityManager.createNamedQuery(
        "PluginMetadataEntity.findByStatusAndAvailability", PluginPersistenceMetadata.class);
    q.setParameter("available", pAvailability);
    q.setParameter("status", pStatus);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginViewEnum> findViewsByUUID(final UUID pUUID)
  {
    final TypedQuery<PluginPersistenceMetadata> q = entityManager.createNamedQuery(
        "PluginMetadataEntity.findByUUID", PluginPersistenceMetadata.class);
    q.setParameter("uuid", pUUID.toString());
    return q.getSingleResult().getViews();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginPersistenceMetadata> findByViewId(final PluginViewEnum pViewId)
  {
    final TypedQuery<PluginPersistenceMetadata> q = entityManager.createNamedQuery("PluginMetadataEntity.findByViewId",
                                                                                   PluginPersistenceMetadata.class);

    q.setParameter("view", pViewId);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean exist(final UUID pId)
  {

    final CriteriaBuilder            builder       = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Long>        countCriteria = builder.createQuery(Long.class);
    final Root<PluginMetadataEntity> entityRoot    = countCriteria.from(PluginMetadataEntity.class);
    countCriteria.select(builder.count(entityRoot));
    countCriteria.where(builder.equal(entityRoot.get(PluginMetadataEntity_.uuid), pId.toString()));
    final long count = entityManager.createQuery(countCriteria).getSingleResult();

    return count > 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginPersistenceMetadata> findAll()
  {
    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<PluginPersistenceMetadata> allCriteria = builder
        .createQuery(PluginPersistenceMetadata.class);
    final Root<PluginMetadataEntity> entityRoot = allCriteria.from(PluginMetadataEntity.class);
    allCriteria.select(entityRoot);
    return entityManager.createQuery(allCriteria).getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> findCategories()
  {
    final TypedQuery<String> q = entityManager.createNamedQuery("PluginMetadataEntity.findCategories",
        String.class);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginPersistenceMetadata create(final PluginPersistenceMetadata pPluginPersistenceMetadata)
  {
    entityManager.persist(pPluginPersistenceMetadata);
    entityManager.flush();
    return pPluginPersistenceMetadata;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginPersistenceMetadata update(final PluginPersistenceMetadata pPluginPersistenceMetadata)
  {
    entityManager.merge(pPluginPersistenceMetadata);
    entityManager.flush();
    return pPluginPersistenceMetadata;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginPersistenceMetadata newEntity()
  {
    final PluginQueuesEntity queues = new PluginQueuesEntity();
    final PluginMetadataEntity pluginMetadataEntity = new PluginMetadataEntity();
    pluginMetadataEntity.setJMSQueues(queues);
    return pluginMetadataEntity;
  }
}
