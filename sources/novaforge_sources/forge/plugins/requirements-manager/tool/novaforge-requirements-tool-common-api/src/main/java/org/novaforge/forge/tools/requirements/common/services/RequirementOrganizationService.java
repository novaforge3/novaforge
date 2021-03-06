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
package org.novaforge.forge.tools.requirements.common.services;

import org.novaforge.forge.tools.requirements.common.exceptions.RequirementOrganizationServiceException;
import org.novaforge.forge.tools.requirements.common.model.Role;
import org.novaforge.forge.tools.requirements.common.model.User;

import java.util.Set;

/**
 * @author sbenoist
 */
public interface RequirementOrganizationService
{
  /**
   * This method return an instance of User Object non persisted
   * 
   * @param pLogin
   * @param pFirstname
   * @param pLastname
   * @return an instance of User Object
   */
  User buildUser(String pLogin, String pFirstname, String pLastname);

  /**
   * This method adds a membership for the user on the given project
   * if the user doesn't exists, it creates him,
   * if the user has already any membership on the given project, it replaces it
   * if the user hasn't already any membership on the given project, it creates it
   * 
   * @param pUser
   * @param pRoleName
   * @param pProjectId
   * @throws RequirementOrganizationServiceException
   */
  void addMembership(User pUser, String pRoleName, String pProjectId)
      throws RequirementOrganizationServiceException;

  /**
   * This method deletes a membership for the user on the given project
   * 
   * @param pLogin
   * @param pProjectId
   * @throws RequirementOrganizationServiceException
   */
  void deleteMembership(String pLogin, String pProjectId) throws RequirementOrganizationServiceException;

  /**
   * This method deletes all memberships of a project
   * 
   * @param pProjectId
   * @throws RequirementOrganizationServiceException
   */
  void deleteAllProjectMemberships(String pProjectId) throws RequirementOrganizationServiceException;

  /**
   * This method deletes a user and its memberships
   * 
   * @param pLogin
   * @throws RequirementOrganizationServiceException
   */
  void deleteUser(String pLogin) throws RequirementOrganizationServiceException;

  /**
   * This method updates an user properties
   * 
   * @param pUser
   * @throws RequirementOrganizationServiceException
   */
  void updateUser(User pUser) throws RequirementOrganizationServiceException;

  /**
   * This method returns all the roles of the application
   * 
   * @return a Set of Role
   * @throws RequirementOrganizationServiceException
   */
  Set<Role> findAllRoles() throws RequirementOrganizationServiceException;

}
