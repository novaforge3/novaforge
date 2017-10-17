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

import org.novaforge.forge.tools.managementmodule.dao.ScopeUnitDAO;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author fdemange
 */
public class ScopeUnitDAOImpl implements ScopeUnitDAO
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
  public ScopeUnit findByUnitId(final String scopeUnitId)
  {

    Query q = entityManager.createNamedQuery("ScopeUnitEntity.findByUnitId");
    q.setParameter("unitId", scopeUnitId);
    List<?> resultList = q.getResultList();
    ScopeUnit result = null;
    if (resultList != null && resultList.size() == 1)
    {
      result = (ScopeUnit) resultList.get(0);
    }
    return result;
  }

  @Override
  public ScopeUnit findByRefVersion(final String refScopeUnitId, final String refVersion,
      final Long projectPlanId)
  {
    Query q = entityManager.createNamedQuery("ScopeUnitEntity.findByRefVersion");
    q.setParameter("unitId", refScopeUnitId);
    q.setParameter("version", refVersion);
    q.setParameter("projectPlanId", projectPlanId);
    List<?> resultList = q.getResultList();
    ScopeUnit result = null;
    if (resultList != null && resultList.size() == 1)
    {
      result = (ScopeUnit) resultList.get(0);
    }
    return result;
  }

  @Override
  public List<ScopeUnit> findByRefUnitId(final String refScopeUnitId)
  {
    Query q = entityManager.createNamedQuery("ScopeUnitEntity.findByRefUnitId");
    q.setParameter("unitId", refScopeUnitId);

    final List<ScopeUnit> result = new ArrayList<ScopeUnit>();
    final List<?> resultList = new LinkedList(q.getResultList());
    if (resultList != null)
    {
      for (Object o : resultList)
      {
        result.add((ScopeUnit) o);
      }
    }
    return result;
  }

  @Override
  public List<ScopeUnit> findByProjectPlanId(final Long pProjectPlanId)
  {
    Query q = entityManager.createNamedQuery("ScopeUnitEntity.findByProjectPlanId");
    q.setParameter("projectPlanId", pProjectPlanId);

    final List<ScopeUnit> result = new ArrayList<ScopeUnit>();
    final List<?> resultList = new LinkedList(q.getResultList());
    if (resultList != null)
    {
      for (Object o : resultList)
      {
        result.add((ScopeUnit) o);
      }
    }
    return result;
  }

  @Override
  public void deleteByProjectPlanId(final Long projectPlanId)
  {
    final List<ScopeUnit> listeScopeUnit = findByProjectPlanId(projectPlanId);
    deleteList(listeScopeUnit);
  }

  @Override
  public void deleteScopeUnit(final String unitId)
  {
    final List<ScopeUnit> listeScopeUnit = new ArrayList<ScopeUnit>();
    ScopeUnit su = findByUnitId(unitId);
    if (su != null)
    {
      listeScopeUnit.add(su);
      listeScopeUnit.addAll(su.getChildscopeunit());
    }
    deleteList(listeScopeUnit);
  }

  /**
   * @param listeScopeUnit
   *          @
   */
  private void deleteList(final List<ScopeUnit> listeScopeUnit)
  {
    if (!listeScopeUnit.isEmpty())
    {
      for (ScopeUnit scopeUnit : listeScopeUnit)
      {
        entityManager.remove(scopeUnit);
      }
      entityManager.flush();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ScopeUnit save(final ScopeUnit pScope)
  {
    entityManager.persist(pScope);
    entityManager.flush();
    return pScope;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ScopeUnit merge(final ScopeUnit pCurrentScope)
  {
    entityManager.merge(pCurrentScope);
    entityManager.flush();
    return pCurrentScope;
  }

}
