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

import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;

import java.util.List;

/**
 * @author lamirang
 */
public interface PluginMembershipService
{

  /**
   * This method allows to add user memberships
   * 
   * @param pInstanceId
   *          represents the project instance id
   * @param pMemberships
   *          represents a list between forge role and tool role which has to be created
   * @throws PluginServiceException
   *           used if errors occured during the process
   */
  void addUserMemberships(String pInstanceId, List<PluginMembership> pMemberships)
      throws PluginServiceException;

  /**
   * This method allows to add user memberships
   * 
   * @param pInstanceId
   *          represents the project instance id
   * @param pMemberships
   *          represents a list which associated user and forge role which has to be updated
   * @throws PluginServiceException
   *           used if errors occured during the process
   */
  void updateUserMemberships(String pInstanceId, List<PluginMembership> pMemberships)
      throws PluginServiceException;

  /**
   * This method allows to remove user memberships
   * 
   * @param pInstanceId
   *          represents the project instance id
   * @param pMemberships
   *          represents a list which associated user and forge role which has to be removed
   * @throws PluginServiceException
   *           used if errors occured during the process
   */
  void removeUserMemberships(String pInstanceId, List<PluginMembership> pMemberships)
      throws PluginServiceException;
}
