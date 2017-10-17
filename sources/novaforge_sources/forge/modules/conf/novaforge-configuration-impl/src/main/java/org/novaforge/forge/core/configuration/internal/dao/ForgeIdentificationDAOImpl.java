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
package org.novaforge.forge.core.configuration.internal.dao;

import org.novaforge.forge.core.configuration.dao.ForgeIdentificationDAO;
import org.novaforge.forge.core.configuration.entity.ForgeIdentificationEntity;
import org.novaforge.forge.core.configuration.model.ForgeIdentification;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.UUID;

/**
 * Persistence implementation of {@link ForgeIdentificationDAO}
 * 
 * @author Guillaume Lamirand
 */
public class ForgeIdentificationDAOImpl implements ForgeIdentificationDAO
{

  /**
   * {@link EntityManagerFactory} injected by container
   */
  private EntityManagerFactory entityManagerFactory;

  /**
   * Use by container to inject {@link EntityManagerFactory}
   * 
   * @param pEntityManagerFactory
   *          the entityManagerFactory to set
   */
  public void setEntityManagerFactory(final EntityManagerFactory pEntityManagerFactory)
  {
    entityManagerFactory = pEntityManagerFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existIdentifiant()
  {
    final EntityManager                   entityManager = getEntityManager();
    final CriteriaBuilder                 builder       = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Long>             countCriteria = builder.createQuery(Long.class);
    final Root<ForgeIdentificationEntity> entityRoot    = countCriteria.from(ForgeIdentificationEntity.class);
    countCriteria.select(builder.count(entityRoot));
    final long count = entityManager.createQuery(countCriteria).getSingleResult();

    return count > 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ForgeIdentification findByUUID(final UUID pUUID)
  {
    final Query q = getEntityManager().createNamedQuery("ForgeIdentificationEntity.findByUUID");
    q.setParameter("uuid", pUUID.toString());
    return (ForgeIdentification) q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ForgeIdentification get()
  {
    final EntityManager entityManager = getEntityManager();
    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<ForgeIdentificationEntity> queryCriteria = builder
        .createQuery(ForgeIdentificationEntity.class);
    final Root<ForgeIdentificationEntity> entityRoot = queryCriteria.from(ForgeIdentificationEntity.class);
    queryCriteria.select(entityRoot);
    return entityManager.createQuery(queryCriteria).setMaxResults(1).getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ForgeIdentification create(final UUID pUUID)
  {
    final EntityManager entityManager = getEntityManager();
    if (!entityManager.getTransaction().isActive())
    {
      entityManager.getTransaction().begin();
    }
    final ForgeIdentification id = new ForgeIdentificationEntity(pUUID);
    entityManager.persist(id);
    entityManager.getTransaction().commit();
    return id;
  }

  private EntityManager getEntityManager()
  {
    return entityManagerFactory.createEntityManager();
  }

}
