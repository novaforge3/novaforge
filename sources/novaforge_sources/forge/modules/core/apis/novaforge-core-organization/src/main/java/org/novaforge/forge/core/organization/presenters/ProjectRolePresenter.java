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
package org.novaforge.forge.core.organization.presenters;

import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.security.authorization.PermissionAction;

import java.util.List;

/**
 * @author sbenoist
 */
public interface ProjectRolePresenter
{
  String ROLE_NAME_SEARCH_CRITERIA_KEY = "name";

  /**
   * This method allows to instanciate a role
   * 
   * @return Role
   */
  ProjectRole newRole();

  /**
   * This method allows to create a role
   * 
   * @param role
   * @param projectId
   * @param author
   *          the username of the user which make the action
   * @throws ProjectServiceException
   */
  void createRole(Role role, String projectId) throws ProjectServiceException;

  /**
   * This method allows to create a system role
   * 
   * @param role
   * @param projectId
   * @param author
   *          the username of the user which make the action
   * @throws ProjectServiceException
   */
  void createSystemRole(Role role, String projectId) throws ProjectServiceException;

  /**
   * This method allows to delete a role
   * 
   * @param roleName
   * @param projectId
   * @param author
   *          the username of the user which make the action
   * @throws ProjectServiceException
   */
  void deleteRole(String roleName, String projectId) throws ProjectServiceException;

  /**
   * This method returns all the roles for a project
   * 
   * @param projectId
   * @return
   * @throws ProjectServiceException
   */
  List<ProjectRole> getAllRoles(String projectId) throws ProjectServiceException;

  /**
   * This method returns a role by name and project
   * 
   * @param roleName
   * @param projectId
   * @return
   * @throws ProjectServiceException
   */
  ProjectRole getRole(String roleName, String projectId) throws ProjectServiceException;

  /**
   * This method allows to update a role
   * 
   * @param pRole
   * @param pProjectId
   * @param pAuthor
   *          he username of the user which make the action
   * @throws ProjectServiceException
   */
  void updateRole(final String pOldName, final Role pRole, final String pProjectId)
      throws ProjectServiceException;

  /**
   * @param pProjectId
   * @param pRoleName
   * @param pResource
   * @param pActions
   * @throws ProjectServiceException
   */
  void addPermissionToRole(final String pProjectId, String pRoleName, final Class<?> pResource,
      final PermissionAction... pActions) throws ProjectServiceException;

  /**
   * This method allows to increase (pIncrease=true) or decrease role order
   * 
   * @param pProjectId
   * @param pRoleName
   * @param pIncrease
   * @return role object containing new order
   * @throws ProjectServiceException
   */
  ProjectRole changeOrder(final String pProjectId, final String pRoleName, final boolean pIncrease)
      throws ProjectServiceException;
}
