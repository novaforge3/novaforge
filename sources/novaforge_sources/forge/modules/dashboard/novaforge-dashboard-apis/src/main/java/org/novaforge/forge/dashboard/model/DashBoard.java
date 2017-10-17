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
package org.novaforge.forge.dashboard.model;

import java.util.List;
import java.util.UUID;

/**
 * This interface describes a dashboard object
 * 
 * @author Guillaume Lamirand
 */
public interface DashBoard
{
  /**
   * Return the {@link DashBoard} identifiant which is unique
   *
   * @return the {@link DashBoard} identifiant
   */
  UUID getUUID();

  /**
   * This method returns the dashbaord type
   *
   * @return {@link Type}
   */
  Type getType();

  /**
   * This method returns the id associated to the {@link Type}
   *
   * @return the di
   */
  String getTypeId();

  /**
   * Returns {@link List} of {@link Tab}
   *
   * @return {@link List} of {@link Tab}
   */
  List<Tab> getTabs();

  /**
   * Sets tabs list
   * 
   * @param pTabs
   *          the list to set
   */
  void setTabs(final List<Tab> pTabs);

  /**
   * Add a tab to dashboard
   *
   * @param pTab
   *          the tab to add
   */
  void addTab(final Tab pTab);

  /**
   * Remove a tab from dashboard
   *
   * @param pTab
   *          the tab to remove
   */
  void removeTab(final Tab pTab);

  /**
   * This enum will define the type of the dashboard, if it is attached to a user or a project
   */
  enum Type
  {
    /**
     * User related
     */
    USER,
    /**
     * Project related
     */
    PROJECT
  }

}
