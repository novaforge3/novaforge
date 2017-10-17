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

import org.novaforge.forge.core.organization.model.BinaryFile;

import java.util.List;
import java.util.UUID;

/**
 * This interface describes a tabulation used to separate widget in a {@link DashBoard}
 * 
 * @author Guillaume Lamirand
 */
public interface Tab
{
  /**
   * Return the {@link Tab} identifiant which is unique
   * 
   * @return the {@link Tab} identifiant
   */
  UUID getUUID();

  /**
   * Return the {@link Tab} name
   * 
   * @return the {@link Tab} name
   */
  String getName();

  /**
   * Set the {@link Tab} name
   * 
   * @param pName
   *          represents the new name
   */
  void setName(final String pName);

  /**
   * Get the image content
   *
   * @return the image content
   */
  BinaryFile getImage();

  /**
   * Set the image content.
   *
   * @param pImage
   *          image to set
   */
  void setImage(BinaryFile pImage);

  /**
   * Return the {@link Tab} position in {@link DashBoard}
   * 
   * @return the {@link Tab} index
   */
  int getIndex();

  /**
   * Set the {@link Tab} position in {@link DashBoard}
   * 
   * @param pIndex
   *          represents the new index
   */
  void setIndex(final int pIndex);

  /**
   * Return the layout key sets for this {@link Tab}
   * 
   * @return the layout key
   */
  String getLayoutKey();

  /**
   * Set the layout key sets for this {@link Tab}
   * 
   * @param pLayoutKey
   *          represents the layout key
   */
  void setLayoutKey(final String pLayoutKey);

  /**
   * Returns {@link List} of {@link Widget}
   *
   * @return {@link List} of {@link Widget}
   */
  List<Widget> getWidgets();

  /**
   * Sets widgets list
   *
   * @param pWidgets
   *          the list to set
   */
  void setWidgets(List<Widget> pWidgets);

  /**
   * Add a widget to tab
   * 
   * @param pWidget
   *          the widget to add
   */
  void addWidget(Widget pWidget);

  /**
   * Remove a widget from tab
   * 
   * @param pWidget
   *          the widget to remove
   */
  void removeWidget(Widget pWidget);

}
