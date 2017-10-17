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
package org.novaforge.forge.dashboard.service;

import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.dashboard.model.DashBoard;
import org.novaforge.forge.dashboard.model.DashBoard.Type;
import org.novaforge.forge.dashboard.model.Tab;
import org.novaforge.forge.dashboard.model.Widget;

import javax.persistence.NoResultException;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public interface DashBoardService
{
  /**
   * This method will return the {@link DashBoard} for the {@link Type} and id given. The id should be either
   * a user login or a project id.
   * <p>
   * All {@link Tab} and {@link Widget} will be accessible from here.
   * 
   * @param pType
   *          the type of {@link DashBoard}
   * @param pTypeId
   *          the type id, should be either a user login or a project id
   * @return {@link DashBoard} found
   * @throws NoResultException
   *           thrown if no entity are found
   */
  DashBoard getDashBoard(final Type pType, final String pTypeId) throws NoResultException;

  /**
   * This method will build a {@link Tab} entity
   * 
   * @return new {@link Tab}
   */
  Tab newTab();

  /**
   * This method will return a {@link Tab}
   * 
   * @param pUUID
   *          tab uuid
   * @return {@link Tab} found
   * @throws NoResultException
   *           thrown if no entity are found
   */
  Tab getTab(final UUID pUUID) throws NoResultException;

  /**
   * This method will build a {@link Widget} entity
   * 
   * @param pWidgetKey
   *          the widget to add
   * @return new {@link Widget}
   */
  Widget newWidget(final String pWidgetKey);

  /**
   * This method will add a {@link TAb} to its {@link DashBoard}
   * 
   * @param pType
   *          represents the dashboard type
   * @param pTypeId
   *          represents the type id
   * @param pTab
   *          represents the tab to add
   * @return {@link Tab} persisted
   */
  Tab addTab(final Type pType, final String pTypeId, final Tab pTab);

  /**
   * This method will update a {@link Tab}
   * 
   * @param pTab
   *          represents the tab to update
   * @return {@link Tab} persisted
   */
  Tab updateTab(Tab pTab);

  /**
   * This method will remove the given {@link Tab} and update tabs indexes
   * 
   * @param pType
   *          represents the dashboard type
   * @param pTypeId
   *          represents the type id
   * @param pTabUUID
   *          represents the tab uuid to remove
   */
  void removeTab(final Type pType, final String pTypeId, final UUID pTabUUID);

  /**
   * This method will add a {@link Widget} to its {@link Tab}
   * 
   * @param pTabUUID
   *          represents the parent tab uuid to attach the widget
   * @param pWidget
   *          represents the widget to add
   * @return {@link Widget} persisted
   */
  Widget addWidget(final UUID pTabUUID, Widget pWidget);

  /**
   * This method will update a {@link Widget}
   * 
   * @param pWidget
   *          represents the widget to update
   * @return {@link Widget} persisted
   */
  Widget updateWidget(Widget pWidget);

  /**
   * This method will remove a {@link Widget} from its {@link Tab}
   * 
   * @param pTabUUID
   *          represents the parent tab uuid to attach the widget
   * @param pWidgetUUID
   *          represents the widget uuid to update
   */
  void removeWidget(final UUID pTabUUID, final UUID pWidgetUUID);

  /**
   * This method will return a {@link Widget}
   * 
   * @param pUUID
   *          widget uuid
   * @return {@link Widget} found
   * @throws NoResultException
   *           thrown if no entity are found
   */
  Widget getWidget(final UUID pUUID) throws NoResultException;

  /**
   * Returns new {@link BinaryFile} entity
   * 
   * @return new {@link BinaryFile} entity
   */
  BinaryFile newIcon();

  /**
   * Returns true if current user logged has admin right on the given dashboard
   * 
   * @param pType
   *          type of dashboard to check
   * @param pTypeId
   *          the type id ie project id or user login
   * @return true if current user logged has admin right on the given dashboard
   */
  boolean hasAdminRight(final Type pType, final String pTypeId);

  /**
   * @param pWidget
   * @param pAreaId
   * @param pIndex
   */
  void moveWidget(Widget pWidget, int pAreaId, int pIndex);

}
