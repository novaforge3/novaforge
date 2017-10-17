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
package org.novaforge.forge.core.plugins.services;

import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;

import java.util.Map;

/**
 * @author lamirang
 */
public interface PluginRoleMappingService
{

  /**
   * This method allows to create a roles mapping for a instance
   * 
   * @param pInstanceId
   *          represents the project instance id
   * @param pRoles
   *          represents a map between forge role and tool role
   * @throws PluginServiceException
   *           used if errors occured
   */
  void createRolesMapping(String pInstanceId, Map<String, String> pRoles) throws PluginServiceException;

  /**
   * This method allows to update a roles mapping for a instance
   * 
   * @param pInstanceId
   *          represents the project instance id
   * @param pRoles
   *          represents the complete roles mapping which will be used
   * @throws PluginServiceException
   *           used if errors occured
   */
  void updateRolesMapping(String pInstanceId, Map<String, String> pRoles) throws PluginServiceException;

  /**
   * This method allows to delete roles
   * 
   * @param pInstanceId
   *          represents the project instance id
   * @param pRoles
   *          represents a map of roles mapping which will be removed from existing one
   * @throws PluginServiceException
   *           used if errors occured
   */
  void removeRolesMapping(String pInstanceId, Map<String, String> pRoles) throws PluginServiceException;

  /**
   * Return a map between forge role and tool role
   * 
   * @param pInstanceId
   *          represents instance id
   * @return a map between forge role and tool role
   * @throws PluginServiceException
   *           used if errors occured
   */
  Map<String, String> getRolesMapping(String pInstanceId) throws PluginServiceException;

  /**
   * @param pInstanceId
   * @param pForgeRole
   * @return
   * @throws PluginServiceException
   */
  String getToolRole(String pInstanceId, String pForgeRole) throws PluginServiceException;

  /**
   * @param pToolRole
   * @return
   * @throws PluginServiceException
   */
  String getToolRoleId(String pToolRole) throws PluginServiceException;

  /**
   * @param pInstanceId
   */
  void deleteByInstance(String pInstanceId);

  /**
   * @param pInstanceId
   * @param pForgeRole
   * @return
   * @throws DataAccessException
   */
  boolean existToolRole(String pInstanceId, String pForgeRole);
}
