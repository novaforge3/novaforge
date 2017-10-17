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
package org.novaforge.forge.core.organization.dao;

import org.novaforge.forge.core.organization.model.Permission;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * This class defines methods to access to {@link Role} and {@link Permission} data from persistence
 * 
 * @author Guillaume Lamirand
 * @see Role
 * @see Permission
 */
public interface RoleDAO
{

  /****************************************
   * The following methods will manage Role
   ****************************************/
  /**
   * This method will return a new entity detach of persiste context
   * 
   * @return new {@link Role}
   */
  Role newRole();

  /**
   * This method will return a new entity detach of persiste context
   * 
   * @return new {@link ProjectRole}
   */
  ProjectRole newProjectRole();

  /**
   * Find all {@link Role} from its project element
   * 
   * @param pElementId
   *          represents the project element id
   * @return a {@link List} of {@link Role} according to parameter
   */
  List<Role> findAllRole(final String pElementId);

  /**
   * Find a {@link Role} from its project element and name
   * 
   * @param pElementId
   *          represents the project element id
   * @param pRoleName
   *          represents the role name
   * @return {@link Role} according to parameters
   * @throws NoResultException
   *           thrown if no {@link Role} are existing for the parameters given
   */
  Role findByNameAndElement(final String pElementId, final String pRoleName) throws NoResultException;

  /**
   * This method allows to check if a role name is existing on the project element
   * 
   * @param pElementId
   *          element id
   * @param pRoleName
   *          the role name to check
   * @return true if existing, false otherwise
   */
  boolean existRole(final String pElementId, final String pRoleName);

  /**
   * Return for a project element its max order
   * 
   * @param pElementId
   *          represents the project element id
   * @return max order value as {@link Long}
   */
  Integer getMaxOrder(final String pElementId);

  /**
   * This method will increase or decrease roles's order. The role is found from its name and the project
   * element id given.
   * 
   * @param pElementId
   *          represents the project element id
   * @param pRoleName
   *          represents the role name
   * @param pIncrease
   *          <code>true</code> to increase, <code>false</code> to decrease value
   * @return {@link Role} updated and attach to persistence context
   */
  Role changeOrder(final String pElementId, final String pRoleName, final boolean pIncrease);

  /**
   * Update the roles' order bigger then parameter given
   * 
   * @param pElementId
   *          represents the project element id
   * @param pOrder
   *          role's order bigger than this order will be updated
   * @return number of {@link Role} updated
   */
  int updateOrdersBiggerThan(final String pElementId, final Integer pOrder);

  /**
   * Find a {@link Role} from its project element and order
   * 
   * @param pElementId
   *          represents the project element id
   * @param pOrder
   *          represents the role order
   * @return {@link Role} according to parameters
   * @throws NoResultException
   *           thrown if no {@link Role} are existing for the parameters given
   */
  Role findByElementAndOrder(final String pElementId, final Integer pOrder) throws NoResultException;

  /**
   * This will update {@link Role} into persistence context
   * 
   * @param pRole
   *          the role to update
   * @return {@link Role} updated and attach to persistence context
   */
  Role update(final Role pRole);

  /**
   * This will delete {@link Role} from persistence context
   * 
   * @param pRole
   *          the role to delete
   */
  void delete(final Role pRole);

  /****************************************
   * The following methods will manage Permission
   ****************************************/
  /**
   * Find a {@link Permission} from its name
   * 
   * @param pName
   *          represents the permission name
   * @return {@link Permission} according to parameter
   * @throws NoResultException
   *           thrown if no {@link Permission} are existing for the name given
   */
  Permission findByName(final String pName) throws NoResultException;

  /**
   * This method will return a new entity detach of persistence context
   * 
   * @param pName
   *          the name
   * @return new {@link Permission}
   */
  Permission newPermission(final String pName);

  /**
   * This method allows to check if a permission name is existing
   * 
   * @param pName
   * @return Boolean
   */
  boolean existPermission(final String pName);

  /**
   * This will persist {@link Permission} into persistence context
   * 
   * @param pPermission
   *          the permission to persist
   * @return {@link Permission} persisted and attach to persistence context
   */
  Permission persist(final Permission pPermission);

  /**
   * This will update {@link Permission} into persistence context
   * 
   * @param pPermission
   *          the permission to update
   * @return {@link Permission} updated and attach to persistence context
   */
  Permission update(final Permission pPermission);

  /**
   * This will delete {@link Permission} from persistence context
   * 
   * @param pPermission
   *          the permission to delete
   */
  void delete(final Permission pPermission);

}