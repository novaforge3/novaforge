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
package org.novaforge.forge.dashboard.dao;

import org.novaforge.forge.dashboard.model.DashBoard;
import org.novaforge.forge.dashboard.model.DashBoard.Type;
import org.novaforge.forge.dashboard.model.Tab;
import org.novaforge.forge.dashboard.model.Widget;

import javax.persistence.NoResultException;
import java.util.UUID;

/**
 * This interface describes an accesseur to persistence object
 * 
 * @author Guillaume Lamirand
 */
public interface DashBoardDAO
{

  /**
   * This method will build a dashboard entity from the given parameter
   * 
   * @param pType
   *          the dashboard type
   * @param pTypeId
   *          the type id related
   * @return new {@link DashBoard}
   */
  DashBoard newDashboard(final Type pType, final String pTypeId);

  /**
   * This method persists a dashboard entity
   * 
   * @param pDashboard
   *          the entity to persist
   * @return {@link DashBoard} merged
   */
  DashBoard persist(DashBoard pDashboard);

  /**
   * This method update a dashboard entity
   * 
   * @param pDashboard
   *          the entity to update
   * @return {@link DashBoard} merged
   */
  DashBoard update(DashBoard pDashboard);

  /**
   * This method removes a dashboard entity
   * 
   * @param pDashboard
   *          the entity to remove
   */
  void remove(DashBoard pDashboard);

  /**
   * This method will return the dashboard found for the type and type id given
   * 
   * @param pType
   *          the type of the dashboard
   * @param pTypeId
   *          the id
   * @return {@link DashBoard} found
   * @throws NoResultException
   *           thrown if no entity are found
   */
  DashBoard findDashboardByType(final Type pType, final String pTypeId) throws NoResultException;

  /**
   * This method will build a {@link Tab} entity
   * 
   * @return new {@link Tab}
   */
  Tab newTab();

  /**
   * This method update a tab entity
   * 
   * @param pTab
   *          the entity to update
   * @return {@link Tab} merged
   */
  Tab update(final Tab pTab);

  /**
   * This method remove a tab entity
   * 
   * @param pTab
   *          the entity to remove
   */
  void remove(final Tab pTab);

  /**
   * This method will return the tab found for the uuid given
   * 
   * @param pUUID
   *          the tab uuid
   * @return {@link Tab} found
   * @throws NoResultException
   *           thrown if no entity are found
   */
  Tab findTabByUUID(final UUID pUUID) throws NoResultException;

  /**
   * This method will return the tab found for the index given
   * 
   * @param pIndex
   *          the tab index
   * @return {@link Tab} found
   * @throws NoResultException
   *           thrown if no entity are found
   */
  Tab findTabByIndex(final int pIndex) throws NoResultException;

  /**
   * This method will build a {@link Widget} entity
   * 
   * @return new {@link Widget}
   */
  Widget newWidget();

  /**
   * This method remove a Widget entity
   * 
   * @param pWidget
   *          the entity to remove
   */
  void remove(final Widget pWidget);

  /**
   * This method update a widget entity
   * 
   * @param pWidget
   *          the entity to update
   * @return {@link Widget} merged
   */
  Widget update(final Widget pWidget);

  /**
   * This method will return the widget found for the uuid given
   * 
   * @param pUUID
   *          the widget uuid
   * @return {@link Widget} found
   * @throws NoResultException
   *           thrown if no entity are found
   */
  Widget findWidgetByUUID(final UUID pUUID) throws NoResultException;

  /**
   * This method increase index lower or equals than given index
   * 
   * @param pAreaId
   *          the area id to update
   * @param pIndex
   *          index used to find lower or equals
   * @return number of {@link Widget} updated
   */
  int increaseIndex(int pAreaId, int pIndex);

  /**
   * This method decrease index lower than given index
   * 
   * @param pAreaId
   *          the area id to update
   * @param pIndex
   *          index used to find lower
   * @return number of {@link Widget} updated
   */
  int decreaseIndex(final int pAreaId, final int pIndex);

  /**
   * This method decrease index between the two given
   * 
   * @param pAreaId
   *          the area id to update
   * @param pPreviousIndex
   *          widget previous index
   * @param pNewIndex
   *          the new index
   * @return number of {@link Widget}t updated
   */
  int decreaseIndexBetween(int pAreaId, int pPreviousIndex, int pNewIndex);

  /**
   * This method increase index between the two given
   * 
   * @param pAreaId
   *          the area id to update
   * @param pPreviousIndex
   *          widget previous index
   * @param pNewIndex
   *          the new index
   * @return number of {@link Widget} updated
   */
  int increaseIndexBetween(int pAreaId, int pPreviousIndex, int pNewIndex);

}
