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
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author sbenoist
 */
public interface MembershipPresenter
{
  /**
   * Adds a membership for an User
   * 
   * @param pProjectId
   * @param pUserUUID
   * @param pRolesName
   * @param pDefaultRoleName
   * @param pSendMail
   * @throws ProjectServiceException
   */
  void addUserMembership(final String pProjectId, final UUID pUserUUID, final Set<String> pRolesName,
      final String pDefaultRoleName, final boolean pSendMail) throws ProjectServiceException;

  /**
   * Add a membership for a Group
   * 
   * @param pProjectId
   * @param pGroupUUID
   * @param pRolesName
   * @param pDefaultRoleName
   * @param pAuthor
   * @throws ProjectServiceException
   */
  void addGroupMembership(String pProjectId, final UUID pGroupUUID, Set<String> pRolesName,
      String pDefaultRoleName) throws ProjectServiceException;

  /**
   * This method returns all the memberships of the users in a project
   * 
   * @param pProjectId
   *          represents the project id
   * @param pWithSystem
   *          if true, method will return memberships including for system user
   * @return List<Membership>
   * @throws ProjectServiceException
   */
  List<MembershipInfo> getAllUserMemberships(final String pProjectId, final boolean pWithSystem)
      throws ProjectServiceException;

  /**
   * @param pProjectId
   * @param pUserUUID
   * @return
   * @throws ProjectServiceException
   */
  List<MembershipInfo> getAllEffectiveMembershipsForUserAndProject(final String pProjectId,
      final UUID pUserUUID) throws ProjectServiceException;

  /**
   * This method returns all the memberships of all the groups of a project
   * 
   * @param pProjectId
   * @return List<Membership>
   * @throws ProjectServiceException
   */
  List<MembershipInfo> getAllGroupMemberships(final String pProjectId) throws ProjectServiceException;

  /**
   * Updates a membership for an User
   * 
   * @param pProjectId
   * @param pLogin
   * @param pNewRolesName
   * @param pDefaultRoleName
   * @param pSendMail
   * @throws ProjectServiceException
   */
  void updateUserMembership(final String pProjectId, final UUID pUserUUID, final Set<String> pNewRolesName,
      final String pDefaultRoleName, final boolean pSendMail) throws ProjectServiceException;

  /**
   * Updates a membership for a Group
   * 
   * @param pProjectId
   * @param pGroupUUID
   * @param pNewRolesName
   * @param pDefaultRoleName
   * @param pSendMail
   * @throws ProjectServiceException
   */
  void updateGroupMembership(final String pProjectId, final UUID pGroupUUID, final Set<String> pNewRolesName,
      final String pDefaultRoleName, boolean pSendMail) throws ProjectServiceException;

  /**
   * Removes a membership for an User for all its Roles
   * 
   * @param pProjectId
   * @param pUserUUID
   * @param pUserName
   * @param pSendMail
   * @throws ProjectServiceException
   */
  void removeUserMembership(String pProjectId, UUID pUserUUID, boolean pSendMail)
      throws ProjectServiceException;

  /**
   * Removes a membership for a Group for all its Roles
   * 
   * @param pProjectId
   * @param pGoupeUUID
   * @param pAuthor
   * @param pSendMail
   * @throws ProjectServiceException
   */
  void removeGroupMembership(String pProjectId, final UUID pGoupeUUID, boolean pSendMail)
      throws ProjectServiceException;

  /**
   * This method returns all the memberships of a project
   * 
   * @param pProjectId
   * @return List<Membership>
   * @throws ProjectServiceException
   */
  List<Membership> getAllMemberships(String pProjectId) throws ProjectServiceException;

  /**
   * This method check if an {@link User} has super-adminitrator right on forge.
   * 
   * @param pUser
   *          represents the user to check
   * @return true if user is super-admin otherwise false
   * @throws ProjectServiceException
   */
  boolean isSuperAdmin(User pUser);

  /**
   * This method check if current authentificated {@link User} has super-adminitrator right on forge.
   * 
   * @return true if current authentificated user is super-admin otherwise false
   * @throws ProjectServiceException
   */
  boolean isCurrentSuperAdmin();

  /**
   * @return
   * @throws ProjectServiceException
   */
  List<User> getAllSuperAdmin() throws ProjectServiceException;

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
  List<Membership> getAllEffectiveUserMembershipsForUserAndProject(final String pProjectId,
      final String pLogin) throws ProjectServiceException;

  /**
   * @param pLogin
   * @return
   * @throws ProjectServiceException
   */
  Map<String, List<ProjectRole>> getAllEffectiveRolesForUser(String pLogin) throws ProjectServiceException;

  /**
   * Return {@link List} of {@link Actor} means {@link Group} or {@link User} which have membership with the
   * request {@link Role}
   * 
   * @param pProjectId
   * @param pRoleName
   * @return {@link List} of {@link Actor} means {@link Group} or {@link User}
   * @throws ProjectServiceException
   */
  List<Actor> getAllActorsForRole(String pProjectId, String pRoleName) throws ProjectServiceException;

}
