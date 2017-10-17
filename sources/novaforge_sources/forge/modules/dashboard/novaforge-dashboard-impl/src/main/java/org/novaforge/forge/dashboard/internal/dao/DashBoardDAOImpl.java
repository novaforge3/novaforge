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
package org.novaforge.forge.dashboard.internal.dao;

import org.novaforge.forge.dashboard.dao.DashBoardDAO;
import org.novaforge.forge.dashboard.entity.DashBoardEntity;
import org.novaforge.forge.dashboard.entity.TabEntity;
import org.novaforge.forge.dashboard.entity.WidgetEntity;
import org.novaforge.forge.dashboard.model.DashBoard;
import org.novaforge.forge.dashboard.model.DashBoard.Type;
import org.novaforge.forge.dashboard.model.Tab;
import org.novaforge.forge.dashboard.model.Widget;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * JPA2 implementation of {@link DashBoardDAO}
 * 
 * @author Guillaume Lamirand
 */
public class DashBoardDAOImpl implements DashBoardDAO
{

  /**
   * Constant for uuid paramater
   */
  private static final String UUID    = "uuid";
  /**
   * Constant for type id paramater
   */
  private static final String TYPE    = "type";
  /**
   * Constant for type id paramater
   */
  private static final String TYPE_ID = "type_id";
  /**
   * Constant for index paramater
   */
  private static final String INDEX   = "index";

  /**
   * {@link EntityManager} injected by container
   */
  private EntityManager       entityManager;

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
  public DashBoard newDashboard(final Type pType, final String pTypeId)
  {
    return new DashBoardEntity(pType, pTypeId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DashBoard persist(final DashBoard pDashboard)
  {
    entityManager.persist(pDashboard);
    entityManager.flush();
    return pDashboard;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DashBoard update(final DashBoard pDashboard)
  {
    entityManager.merge(pDashboard);
    entityManager.flush();
    return pDashboard;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove(final DashBoard pDashboard)
  {
    entityManager.remove(pDashboard);
    entityManager.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DashBoard findDashboardByType(final Type pType, final String pTypeId)
  {
    final TypedQuery<DashBoard> q = entityManager.createNamedQuery("DashBoardEntity.findByType",
        DashBoard.class);
    q.setParameter(TYPE, pType);
    q.setParameter(TYPE_ID, pTypeId);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Tab newTab()
  {
    return new TabEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Tab update(final Tab pTab)
  {
    entityManager.merge(pTab);
    entityManager.flush();
    return pTab;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove(final Tab pTab)
  {
    entityManager.remove(pTab);
    entityManager.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Tab findTabByUUID(final java.util.UUID pUUID)
  {
    final TypedQuery<Tab> q = entityManager.createNamedQuery("TabEntity.findByUUID", Tab.class);
    q.setParameter(UUID, pUUID.toString());
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Tab findTabByIndex(final int pIndex)
  {
    final TypedQuery<Tab> q = entityManager.createNamedQuery("TabEntity.findByIndex", Tab.class);
    q.setParameter(INDEX, pIndex);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Widget newWidget()
  {
    return new WidgetEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove(final Widget pWidget)
  {
    entityManager.remove(pWidget);
    entityManager.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Widget update(final Widget pWidget)
  {
    entityManager.merge(pWidget);
    entityManager.flush();
    return pWidget;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Widget findWidgetByUUID(final java.util.UUID pUUID)
  {
    final TypedQuery<Widget> q = entityManager.createNamedQuery("WidgetEntity.findByUUID", Widget.class);
    q.setParameter(UUID, pUUID.toString());
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int increaseIndex(final int pAreaId, final int pIndex)
  {

    final Query q = entityManager.createNamedQuery("WidgetEntity.increaseIndex");
    q.setParameter("areaId", pAreaId);
    q.setParameter("index", pIndex);
    return q.executeUpdate();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int decreaseIndex(final int pAreaId, final int pIndex)
  {

    final Query q = entityManager.createNamedQuery("WidgetEntity.decreaseIndex");
    q.setParameter("areaId", pAreaId);
    q.setParameter("index", pIndex);
    return q.executeUpdate();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int decreaseIndexBetween(final int pAreaId, final int pPreviousIndex, final int pNewIndex)
  {

    final Query q = entityManager.createNamedQuery("WidgetEntity.decreaseIndexBetween");
    q.setParameter("areaId", pAreaId);
    q.setParameter("previousIndex", pPreviousIndex);
    q.setParameter("newIndex", pNewIndex);
    return q.executeUpdate();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int increaseIndexBetween(final int pAreaId, final int pPreviousIndex, final int pNewIndex)
  {

    final Query q = entityManager.createNamedQuery("WidgetEntity.increaseIndexBetween");
    q.setParameter("areaId", pAreaId);
    q.setParameter("previousIndex", pPreviousIndex);
    q.setParameter("newIndex", pNewIndex);
    return q.executeUpdate();
  }

}
