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

import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;

/**
 * @author lamirang
 */
public interface PluginUserService
{

  /**
   * This method allows to create user as administrator without any relationship with a project
   * 
   * @param pToolInstanceByName
   *          the target instance where to create new administrator
   * @param pUser
   *          represents the user to create
   * @throws PluginServiceException
   *           occured if errors occured
   */
  void createAdministratorUser(final ToolInstance pToolInstanceByName, PluginUser pUser)
      throws PluginServiceException;

  /**
   * This method allows to update user
   * 
   * @param pInstanceId
   *          represents the project instance id
   * @param pUserName
   *          represents the initial username
   * @param pUser
   *          represents the user to update
   * @throws PluginServiceException
   *           occured if errors occured
   */
  void updateUser(String pInstanceId, String pUserName, PluginUser pUser) throws PluginServiceException;

  /**
   * This method allows to delete user
   * 
   * @param pInstanceId
   *          represents the project instance id
   * @param pPluginUser
   *          represents the user to delete
   * @throws PluginServiceException
   *           occured if errors occured
   */
  void removeUser(String pInstanceId, PluginUser pPluginUser) throws PluginServiceException;
}
