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
package org.novaforge.forge.tools.managementmodule.internal.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.novaforge.forge.tools.managementmodule.dao.LotDAO;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.entity.LotEntity;

import serp.bytecode.Project;

/**
 * @author fdemange
 */
public class LotDAOImpl implements LotDAO
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
    this.entityManager = pEntityManager;
  }

  @Override
  public Lot completeFindByLotId(final Long id)
  {
    Query q = entityManager.createNamedQuery("LotEntity.completeGetLot");
    final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(q);
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("lots_all");
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("iterations_all");
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("scopes_all");
    q.setParameter("id", id);
    Lot result = null;
    List<?> resultList = new LinkedList(q.getResultList());
    if (resultList != null)
    {
      if (resultList.size() > 0)
      {
        result = (Lot) resultList.get(0);
      }
      else
      {
        throw new RuntimeException("too many matches found for this LotId :" + id);
      }
    }
    return result;
  }

  @Override
  public Lot findByLotName(final String lotName)
  {

    Query                       q            = entityManager.createNamedQuery("LotEntity.findByName");
    final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(q);
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("lots_all");
    q.setParameter("name", lotName);
    Lot     result     = null;
    List<?> resultList = new LinkedList(q.getResultList());
    if (resultList != null && resultList.size() == 0)
    {
      result = (Lot) resultList.get(0);
    }
    return result;
  }

  @Override
  public List<Lot> findByProjectPlanId(final Long projectPlanId)
  {

    Query q = entityManager.createNamedQuery("LotEntity.findByProjectPlanId");
    final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(q);
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("lots_all");
    q.setParameter("projectPlanId", projectPlanId);
    final List<Lot> result = new ArrayList<Lot>();
    final List<?> resultList = new LinkedList(q.getResultList());
    if (resultList != null)
    {
      for (Object o : new HashSet<Object>(resultList))
      {
        result.add((Lot) o);
      }
    }
    return result;
  }

  @Override
  public List<Lot> findParentLotsByProjectPlanId(final Long projectPlanId)
  {
    Query q = entityManager.createNamedQuery("LotEntity.findParentLotsByProjectPlanId");
    final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(q);
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("lots_all");
    q.setParameter("projectPlanId", projectPlanId);
    final List<Lot> result = new ArrayList<Lot>();
    final List<?> resultList = new LinkedList(q.getResultList());

    if (resultList != null)
    {
      for (Object o : new HashSet<Object>(resultList))
      {
        result.add((Lot) o);
      }
    }
    return result;
  }

  @Override
  public List<Lot> completeFindByProjectPlanId(final Long projectPlanId)
  {
    Query q = entityManager.createNamedQuery("LotEntity.completeFindByProjectPlanId");
    final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(q);
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("lots_all");
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("iterations_all");
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("scopes_all");
    q.setParameter("projectPlanId", projectPlanId);
    final List<Lot> result = new ArrayList<Lot>();
    final List<?> resultList = new LinkedList(q.getResultList());
    if (resultList != null)
    {
      for (Object o : new HashSet<Object>(resultList))
      {
        result.add((Lot) o);
      }
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Lot findById(final Long pLotId)
  {
    return entityManager.find(LotEntity.class, pLotId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final Lot pLot)
  {
    entityManager.remove(pLot);
    entityManager.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Lot merge(final Lot pLot)
  {
    entityManager.merge(pLot);
    entityManager.flush();
    return pLot;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Lot persist(final Lot pLot)
  {
    entityManager.persist(pLot);
    entityManager.flush();
    return pLot;
  }
}
