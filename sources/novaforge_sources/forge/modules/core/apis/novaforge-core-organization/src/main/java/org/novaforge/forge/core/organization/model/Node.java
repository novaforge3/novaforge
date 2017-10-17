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
package org.novaforge.forge.core.organization.model;

import java.io.Serializable;

/**
 * Define default fields for a Node Element such as {@link Application} or {@link Space}
 * 
 * @author sbenoist
 */
public interface Node extends Serializable
{
  /**
   * This method returns the uri of the node
   *
   * @return URI as {@link String}
   */
  String getUri();

  /**
   * This method allows to set the uri of the node
   *
   * @param pUri
   *          the uri to set
   */
  void setUri(final String pUri);

  /**
   * This method returns the name of the node
   *
   * @return name as {@link String}
   */
  String getName();

  /**
   * This method allows to set the name of the node
   *
   * @param pName
   *          the name to set
   */
  void setName(final String pName);

  /**
   * This method returns the description of the node
   * 
   * @return description as {@link String}
   */
  String getDescription();

  /**
   * This method allows to set the description of the node
   * 
   * @param pDescription
   *          the description to set
   */
  void setDescription(String pDescription);

}
