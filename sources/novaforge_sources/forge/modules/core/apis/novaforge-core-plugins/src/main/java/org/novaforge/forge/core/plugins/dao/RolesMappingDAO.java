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

import org.novaforge.forge.core.plugins.domain.plugin.RolesMapping;

import java.util.Map;

/**
 * This class defines methods to access to role mapping from database
 * 
 * @author lamirang
 */
public interface RolesMappingDAO
{

  /**
   * This method allows to find the list of roles mapping regarding an instance id
   * 
   * @param pInstanceID
   *          represents instance id
   * @return list of roles mapping regarding instance id
   */
  Map<String, String> findByInstance(final String pInstanceID);

  /**
   * @param pInstanceID
   *          represents instance id
   * @param pForgeRole
   *          represents forge role
   */
  void removeByInstanceAndForgeRole(final String pInstanceID, final String pForgeRole);

  /**
   * This method allows to delete all roles mapping regarding an instance id
   * 
   * @param pInstanceID
   *          represents instance id
   */
  void deleteByInstance(final String pInstanceID);

  /**
   * @param pInstanceID
   * @param pForgeRole
   * @return
   */
  String findByInstanceAndForgeRole(String pInstanceID, String pForgeRole);

  /**
   * This method will persist the object given in parameter
   * 
   * @param pRolesMapping
   *          the roles mapping to persist
   * @return {@link RolesMapping} attached to persistence context
   */
  RolesMapping persist(RolesMapping pRolesMapping);

}
