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

import java.util.UUID;

/**
 * This interface describes a widget
 * 
 * @author Guillaume Lamirand
 */
public interface Widget
{
  /**
   * Return the {@link Widget} identifiant which is unique
   * 
   * @return the {@link Widget} identifiant
   */
  UUID getUUID();

  /**
   * Return the {@link Widget} name
   * 
   * @return the {@link Widget} name
   */
  String getName();

  /**
   * Set the {@link Widget} name
   * 
   * @param pName
   *          represents the new name
   */
  void setName(final String pName);

  /**
   * Get the {@link WidgetModule} key
   *
   * @return the key associated to {@link WidgetModule#getKey()}
   */
  String getKey();

  /**
   * Set the {@link WidgetModule} key
   *
   * @param pKey
   *          key to set
   */
  void setKey(final String pKey);

  /**
   * Return the {@link Widget} position in {@link Tab}
   * 
   * @return the box id
   */
  int getAreaId();

  /**
   * Set the {@link Widget} position in {@link Tab}
   * 
   * @param pAreaId
   *          represents the new id
   */
  void setAreaId(final int pAreaId);

  /**
   * Return the {@link Widget} position in {@link Tab}
   * 
   * @return the {@link Widget} index
   */
  int getAreaIndex();

  /**
   * Set the {@link Widget} position in {@link Tab}
   * 
   * @param pIndex
   *          represents the new index
   */
  void setAreaIndex(final int pIndex);

  /**
   * Return the {@link Widget} datasource
   * 
   * @return the {@link Widget} datasource
   */
  String getDataSource();

  /**
   * Set the {@link Widget} datasource
   * 
   * @param pDataSource
   *          represents the new datasource
   */
  void setDataSource(final String pDataSource);

  /**
   * Return the {@link Widget} configuration
   * 
   * @return the {@link Widget} configuration
   */
  String getProperties();

  /**
   * Set the {@link Widget} configuration
   * 
   * @param pProperties
   *          represents the new configuration
   */
  void setProperties(final String pProperties);

}
