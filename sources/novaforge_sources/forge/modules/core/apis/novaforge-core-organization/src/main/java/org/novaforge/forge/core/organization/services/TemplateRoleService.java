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
package org.novaforge.forge.core.organization.services;

import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Role;

import java.util.List;

/**
 * @author lamirang
 */
public interface TemplateRoleService
{

  /**
   * This method allows to instanciate a role
   * 
   * @return Role
   */
  Role newRole();

  /**
   * This method allows to create a role
   * 
   * @param role
   * @param pTemplateId
   * @throws TemplateServiceException
   */
  void createRole(final Role pRole, final String pTemplateId) throws TemplateServiceException;

  /**
   * This method allows to create a role with a system type
   * 
   * @param pRole
   * @param pTemplateId
   * @throws TemplateServiceException
   */
  void createSystemRole(Role pRole, String pTemplateId) throws TemplateServiceException;

  /**
   * This method allows to delete a role
   * 
   * @param pRoleName
   * @param pTemplateId
   * @throws TemplateServiceException
   */
  void deleteRole(final String pRoleName, final String pTemplateId) throws TemplateServiceException;

  /**
   * This method returns all the roles for a template
   * 
   * @param pTemplateId
   * @return list of role
   * @throws TemplateServiceException
   */
  List<Role> getAllRoles(String pTemplateId) throws TemplateServiceException;

  /**
   * This method returns a role by name and template id
   * 
   * @param roleName
   * @param pTemplateId
   * @return role
   * @throws TemplateServiceException
   */
  Role getRole(String roleName, String pTemplateId) throws TemplateServiceException;

  /**
   * This method allows to update a role
   * 
   * @param pRole
   * @param pTemplateId
   * @param pAuthor
   *          he username of the user which make the action
   * @throws TemplateServiceException
   */
  void updateRole(final String pOldName, final Role pRole, final String pTemplateId)
      throws TemplateServiceException;

  /**
   * This method allows to increase (pIncrease=true) or decrease role order
   * 
   * @param pTemplateId
   * @param pRoleName
   * @param pIncrease
   * @return role object containing new order
   * @throws TemplateServiceException
   */
  Role changeOrder(final String pTemplateId, final String pRoleName, final boolean pIncrease)
      throws TemplateServiceException;

  /**
   * This method a list contains the roles which has to be created for a template
   * 
   * @return list of roles
   */
  List<Role> getDefaultRoles();

}
