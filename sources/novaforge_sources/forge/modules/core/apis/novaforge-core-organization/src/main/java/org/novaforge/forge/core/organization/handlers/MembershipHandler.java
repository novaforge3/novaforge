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
package org.novaforge.forge.core.organization.handlers;

import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.MailDelegateEnum;

import java.util.List;
import java.util.Map;

public interface MembershipHandler
{

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
   * This method return the membership used for tool roles mapping for all users (in accordance with roles
   * mapping map).
   * 
   * @param pProjectId
   * @param pToolRoleMapping
   * @return List<Membership>
   * @throws ProjectServiceException
   */
  List<Membership> getAllToolUserMemberships(String pProjectId, Map<String, String> pToolRoleMapping)
      throws ProjectServiceException;

  /**
   * This method will send a message to propage new memberships for a user to each project's application
   * 
   * @param pInitialToolsMemberships
   *          can be null
   * @param pUserApplications
   * @param pProjectId
   * @param pLogin
   * @param pUsername
   * @throws ProjectServiceException
   */
  void sendUserMembershipsPropagation(Map<String, Membership> pInitialToolsMemberships,
      List<ProjectApplication> pUserApplications, String pProjectId, String pLogin, String pUsername)
      throws ProjectServiceException;

  /**
   * This method returns a map with for key the uri of the application and for value the membership mapped
   * with the tool
   * 
   * @param pProjectId
   * @param userApplications
   * @param pLogin
   * @return
   * @throws ProjectServiceException
   * @throws ApplicationServiceException
   */
  Map<String, Membership> getToolsMemberships(final String pProjectId,
      final List<ProjectApplication> userApplications, final String pLogin) throws ProjectServiceException,
      ApplicationServiceException;

  /**
   * This method allows to notify a user for changes on memberships
   * 
   * @param pProjectId
   * @param pLogin
   * @param pSendMail
   * @param pMailDelegateEnum
   * @throws ProjectServiceException
   */
  void notifyUserForMembershipsChange(String pProjectId, User pUser, boolean pSendMail,
      MailDelegateEnum pMailDelegateEnum) throws ProjectServiceException;

  /**
   * This method allows to notify a group of users for changes on memberships
   * 
   * @param pProjectId
   * @param group
   * @param pSendMail
   * @param pMailDelegateEnum
   * @throws ProjectServiceException
   */
  void notifyGroupForMembershipsChange(String pProjectId, List<User> pUsers, boolean pSendMail,
      MailDelegateEnum pMailDelegateEnum) throws ProjectServiceException;

}