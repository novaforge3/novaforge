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
package org.novaforge.forge.core.plugins.domain.plugin;

import java.net.URL;
import java.util.UUID;

/**
 * @author sbenoist
 */
public interface ToolInstance
{
  /**
   * @return the uuid of the plugin tool
   */
  UUID getUUID();

  /**
   * @return the name of the plugin tool
   */
  String getName();

  /**
   * @param pName
   *          the name of the plugin tool
   */
  void setName(String pName);

  /**
   * @return the description of the plugin tool
   */
  String getDescription();

  /**
   * @param pDescription
   *          the description of the plugin tool
   */
  void setDescription(String pDescription);

  /**
   * @return the tool instance status
   */
  ToolInstanceStatus getToolInstanceStatus();

  /**
   * Return <code>true</code> if the instance is internal, ie doesn't need base url and default alias
   * 
   * @return <code>true</code> if the instance is internal,
   */
  boolean isInternal();

  /**
   * Sets the internal type
   * 
   * @param pInternal
   *          type to set
   */
  void setInternal(boolean pInternal);

  /**
   * @return true if the tool instance is shareable
   */
  boolean isShareable();

  /**
   * @param pShareable
   *          true by default
   */
  void setShareable(boolean pShareable);

  /**
   * @return the alias associated to the tool instance
   */
  String getAlias();

  /**
   * @param pAlias
   */
  void setAlias(String pAlias);

  /**
   * @return the base URL of the plugin tool
   */
  URL getBaseURL();

  /**
   * @param pBaseURL
   *          the base URL of the plugin tool
   */
  void setBaseURL(URL pBaseURL);

}
