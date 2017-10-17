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
package org.novaforge.forge.core.plugins.dao;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author sbenoist
 */
public interface ToolInstanceDAO
{
  /**
   * This method returns all the tool instances
   * 
   * @return List<ToolInstance>
   */
  List<ToolInstance> findAllInstances();

  /**
   * This method returns the tool instance found by name
   * 
   * @param pName
   * @return ToolInstance
   */
  ToolInstance findInstanceByName(String pName);

  /**
   * This method returns the tool instance found by UUID
   * 
   * @param pUUID
   * @return ToolInstance
   */
  ToolInstance findInstanceByUUID(UUID pUUID);

  /**
   * This method returns the tool instance found by host or null if no tool instance is found
   * 
   * @param pHost
   * @return ToolInstance
   */
  ToolInstance findInstanceByHost(String pHost);

  /**
   * @param pName
   * @return the applications hosted by tool instance
   */
  Set<InstanceConfiguration> getApplicationsByName(String pName);

  /**
   * @param pUUID
   * @return
   */
  Set<InstanceConfiguration> getApplicationsByUUID(UUID pUUID);

  /**
   * @param pUUID
   * @return
   */
  long countApplicationsByInstance(UUID pUUID);

  /**
   * This method will persist the object given in parameter
   * 
   * @param pToolInstance
   *          the tool instance to persist
   * @return {@link pToolInstance} attached to persistence context
   */
  ToolInstance persist(ToolInstance pToolInstance);

  /**
   * This method will update the object given in parameter
   * 
   * @param pToolInstance
   *          the tool instance to persist
   * @return {@link pToolInstance} attached to persistence context
   */
  ToolInstance update(ToolInstance pToolInstance);

  /**
   * Will delete the {@link ToolInstance} given from persistence context
   * 
   * @param pToolInstance
   *          the tool instance to delete
   */
  void delete(final ToolInstance pToolInstance);

}
