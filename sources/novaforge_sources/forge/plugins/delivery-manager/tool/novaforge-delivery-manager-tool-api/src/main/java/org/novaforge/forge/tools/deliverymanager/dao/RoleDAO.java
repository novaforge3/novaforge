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
package org.novaforge.forge.tools.deliverymanager.dao;

import org.novaforge.forge.tools.deliverymanager.model.Permission;
import org.novaforge.forge.tools.deliverymanager.model.Role;

import java.util.Set;

/**
 * @author sbenoist
 */
public interface RoleDAO
{
  /**
   * This method will return a new entity detach of persiste context
   * 
   * @return new {@link Role}
   */
  Role newRole();

  /**
   * This method will return a new entity detach of persiste context
   * 
   * @return new {@link Permission}
   */
  Permission newPermission();

  /**
   * This method will return the role found by its name
   * 
   * @param pName
   * @return Role found
   */
  Role findRoleByName(String pName);

  /**
   * This method returns all the roles of the application
   * 
   * @return a Set of Role
   */
  Set<Role> findAllRoles();

  /**
   * This method will return the permission found by its name
   * 
   * @param pName
   * @return Permission found
   */
  Permission findPermissionByName(String pName);

  /**
   * Check if the role with the given name already exists
   * 
   * @param pName
   *          Role's pName
   * @return <code>true</code> if exists otherwise <code>false</code>
   */
  boolean existRole(String pName);

  /**
   * This will persist {@link Role} into persistence context
   * 
   * @param pRole
   *          the Role to persist
   * @return {@link Role} persisted and attach to persistence context
   */
  Role persist(final Role pRole);

  /**
   * This will update {@link Role} into persistence context
   * 
   * @param pRole
   *          the Role to update
   * @return {@link Role} updated and attach to persistence context
   */
  Role update(final Role pRole);

  /**
   * This will update {@link Permission} into persistence context
   * 
   * @param pPermission
   *          the Permission to update
   * @return {@link Permission} updated and attach to persistence context
   */
  Permission update(Permission pPermission);

  /**
   * This will delete {@link Role} from persistence context
   * 
   * @param pRole
   *          the Role to delete
   */
  void delete(final Role pRole);

  /**
   * Check if the permission with the given name already exists
   * 
   * @param pName
   *          Permission's pName
   * @return <code>true</code> if exists otherwise <code>false</code>
   */
  boolean existPermission(String pName);
}
