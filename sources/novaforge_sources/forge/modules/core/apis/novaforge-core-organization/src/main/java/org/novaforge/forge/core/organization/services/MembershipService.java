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

import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.ProjectRole;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author sbenoist
 */
public interface MembershipService
{
  /**
   * Adds a membership for an User
   * 
   * @param pProjectId
   * @param pUserUUId
   * @param pRolesName
   * @param pDefaultRoleName
   * @param pUsername
   * @param pSendMail
   * @return user's login
   * @throws ProjectServiceException
   */
  String addUserMembership(final String pProjectId, final UUID pUserUUId, final Set<String> pRolesName,
      final String pDefaultRoleName, final String pUsername, final boolean pSendMail)
      throws ProjectServiceException;

  /**
   * Add a membership for a Group
   * 
   * @param pProjectId
   * @param pGroupUUID
   * @param pRolesName
   * @param pDefaultRoleName
   * @param pUsername
   * @throws ProjectServiceException
   */
  Group addGroupMembership(final String pProjectId, final UUID pGroupUUID, final Set<String> pRolesName,
      final String pDefaultRoleName, final String pUsername) throws ProjectServiceException;

  /**
   * This method returns all the memberships of the users in a project
   * 
   * @param pProjectId
   *          represents the projectid
   * @param pWithSystem
   *          if true, method will return all membership including for system user
   * @return List<Membership>
   * @throws ProjectServiceException
   */
  List<MembershipInfo> getAllUserMemberships(final String pProjectId, final boolean pWithSystem)
      throws ProjectServiceException;

  /**
   * This method returns all the memberships of all the groups of a project
   * 
   * @param pProjectId
   * @return List<Membership>
   * @throws ProjectServiceException
   */
  List<MembershipInfo> getAllGroupMemberships(String pProjectId) throws ProjectServiceException;

  /**
   * Updates a membership for an User
   * 
   * @param pProjectId
   * @param pUserUUID
   * @param pNewRolesName
   * @param pDefaultRoleName
   * @param pSendMail
   * @param pUserName
   * @return user's login updated
   * @throws ProjectServiceException
   */
  String updateUserMembership(final String pProjectId, final UUID pUserUUID, final Set<String> pNewRolesName,
      final String pDefaultRoleName, final boolean pSendMail, final String pUserName)
      throws ProjectServiceException;

  /**
   * Updates a membership for a Group
   * 
   * @param pProjectId
   * @param pGroupUUID
   * @param pNewRolesName
   * @param pDefaultRoleName
   * @param pUserName
   * @param pSendMail
   * @return {@link Group} updated
   * @throws ProjectServiceException
   */
  Group updateGroupMembership(final String pProjectId, final UUID pGroupUUID,
      final Set<String> pNewRolesName, final String pDefaultRoleName, final String pUserName,
      final boolean pSendMail) throws ProjectServiceException;

  /**
   * Removes a membership for an User for all its Roles
   * 
   * @param pProjectId
   * @param pUserUUID
   * @param pLogin
   * @param pUserName
   * @param pSendMail
   * @param pUsername
   * @return user's login removed
   * @throws ProjectServiceException
   */
  String removeUserMembership(final String pProjectId, final UUID pUserUUID, final boolean pSendMail,
      final String pUsername) throws ProjectServiceException;

  /**
   * Removes a membership for a Group for all its Roles
   * 
   * @param pProjectId
   * @param pGroupUUID
   * @param pAuthor
   * @param pSendMail
   * @param pUsername
   * @return {@link Group} removed
   * @throws ProjectServiceException
   */
  Group removeGroupMembership(final String pProjectId, final UUID pGroupUUID, final boolean pSendMail,
      final String pUsername) throws ProjectServiceException;

  /**
   * This method returns all the memberships of a project
   * 
   * @param pProjectId
   * @return List<Membership>
   * @throws ProjectServiceException
   */
  List<Membership> getAllMemberships(final String pProjectId) throws ProjectServiceException;

  /**
   * This method returns all the effective memberships of a project for all users (ie all the memberships for
   * a user in the project + all the memberships he obtains thanks to the groups he owns )
   * 
   * @param pProjectId
   * @return List<Membership>
   * @throws ProjectServiceException
   */
  List<Membership> getAllEffectiveUserMembershipsForProject(String pProjectId) throws ProjectServiceException;

  /**
   * This method returns all the effective memberships of a project for a user (ie all the memberships for a
   * user in the project + all the memberships he obtains thanks to the groups he owns )
   * 
   * @param pProjectId
   * @param pLogin
   * @return List<Membership>
   * @throws ProjectServiceException
   */
  List<Membership> getAllEffectiveUserMembershipsForUserAndProject(String pProjectId, String pLogin)
      throws ProjectServiceException;

  /**
   * This method returns all the actor having the specific role for a project.
   * 
   * @param pProjectId
   * @param pRoleName
   * @return List<Actor>
   * @throws ProjectServiceException
   */
  List<Actor> getAllActorsForRole(String pProjectId, String pRoleName) throws ProjectServiceException;

  /**
   * @param pUserLogin
   *          the user login
   * @return
   * @throws ProjectServiceException
   */
  Map<String, List<ProjectRole>> getAllEffectiveRolesForUser(final String pUserLogin)
      throws ProjectServiceException;

  /**
   * Return all membershipinfo for user on the project
   * 
   * @param pProjectId
   *          the project
   * @param pUserUUID
   *          the user unique identifier
   * @return List<MembershipInfo> all membershipinfo
   * @throws ProjectServiceException
   */
  List<MembershipInfo> getAllEffectiveMembershipsForUserAndProject(String pProjectId, final UUID pUserUUID)
      throws ProjectServiceException;

}
