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
package org.novaforge.forge.distribution.register.server.internal.dao;

import org.novaforge.forge.distribution.register.dao.ForgeRequestDAO;
import org.novaforge.forge.distribution.register.domain.ForgeRequest;
import org.novaforge.forge.distribution.register.domain.RequestStatus;
import org.novaforge.forge.distribution.register.server.entity.ForgeEntity_;
import org.novaforge.forge.distribution.register.server.entity.ForgeRequestEntity;
import org.novaforge.forge.distribution.register.server.entity.ForgeRequestEntity_;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 3 janv. 2012
 */
public class ForgeRequestDAOImpl implements ForgeRequestDAO
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
  public ForgeRequest findById(final UUID forgeRequestId) throws NoResultException
  {
    if (forgeRequestId == null)
    {
      throw new IllegalArgumentException("the given forge request id should not be null");
    }

    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<ForgeRequestEntity> query = builder.createQuery(ForgeRequestEntity.class);
    final Root<ForgeRequestEntity> from = query.from(ForgeRequestEntity.class);
    query.select(from);

    final Predicate uuidPredicate = builder.equal(from.get(ForgeRequestEntity_.forgeRequestId),
        forgeRequestId.toString());

    query.where(uuidPredicate);
    try
    {
      return entityManager.createQuery(query).getSingleResult();
    }
    catch (final NoResultException e)
    {
      return null;
    }

  }

  @Override
  public void delete(final UUID forgeRequestId) throws NoResultException
  {
    if (forgeRequestId == null)
    {
      throw new IllegalArgumentException("the given forge request id should not be null");
    }
    final ForgeRequest forgeRequest = findById(forgeRequestId);
    if (forgeRequest == null)
    {
      throw new NoResultException("Not found forge request id: " + forgeRequestId.toString());
    }
    entityManager.remove(forgeRequest);
    entityManager.flush();

  }

  @Override
  public List<ForgeRequest> findByForgeIdAndStatus(final UUID forgeId, final RequestStatus requestStatus,
      final String type)
  {
    if (forgeId == null)
    {
      throw new IllegalArgumentException("the given forge id should not be null");
    }

    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<ForgeRequest> query = builder.createQuery(ForgeRequest.class);
    final Root<ForgeRequestEntity> from = query.from(ForgeRequestEntity.class);
    query.select(from);

    // String type = "destinationForge" or "sourgeForge"
    Predicate destinationForgeUUIDPred;
    if ("sourceForge".equals(type))
    {
      destinationForgeUUIDPred = builder.equal(
          from.get(ForgeRequestEntity_.sourceForge).get(ForgeEntity_.forgeId), forgeId.toString());
    }
    else
    {
      destinationForgeUUIDPred = builder.equal(
          from.get(ForgeRequestEntity_.destinationForge).get(ForgeEntity_.forgeId), forgeId.toString());
    }
    final Predicate statusPredicate = builder.equal(from.get(ForgeRequestEntity_.requestStatus),
        requestStatus);

    query.where(destinationForgeUUIDPred, statusPredicate);

    return entityManager.createQuery(query).getResultList();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ForgeRequest newForgeRequest()
  {
    return new ForgeRequestEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ForgeRequest save(final ForgeRequest pForgeRequest)
  {
    entityManager.persist(pForgeRequest);
    entityManager.flush();
    return pForgeRequest;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ForgeRequest update(final ForgeRequest pForgeRequest)
  {
    entityManager.merge(pForgeRequest);
    entityManager.flush();
    return pForgeRequest;
  }

}
