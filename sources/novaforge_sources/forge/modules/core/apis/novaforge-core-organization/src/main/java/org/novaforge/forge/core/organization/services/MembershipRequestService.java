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
import org.novaforge.forge.core.organization.model.MembershipRequest;

import java.util.List;
import java.util.Set;

/**
 * @author lamirang
 */
public interface MembershipRequestService
{

  /**
   * This method should be used to create a new membership request to a projet. It will be created for the
   * current user.
   * 
   * @param pProjectId
   *          represents project id
   * @param pUserLogin
   *          represents the user's login
   * @param pMessage
   *          represents the message used to inform project
   *          administrator
   * @throws ProjectServiceException
   */
  void createRequest(final String pProjectId, final String pUser, final String pMessage,
      final boolean pSendMail) throws ProjectServiceException;

  /**
   * This method returns all request which have been treated by project manager.
   * 
   * @param pProjectId
   *          represents project id
   * @return {@link List} of {@link MembershipRequest} validate or invalidated
   * @throws ProjectServiceException
   */
  List<MembershipRequest> getHistoryRequests(final String pProjectId) throws ProjectServiceException;

  /**
   * This method returns all request which haven't been treated by project manager.
   * 
   * @param pProjectId
   *          represents project id
   * @return {@link List} of {@link MembershipRequest} in progress
   * @throws ProjectServiceException
   */
  List<MembershipRequest> getInProcessRequests(final String pProjectId) throws ProjectServiceException;

  /**
   * This method returns all request which haven't been treated by project manager for a user
   * 
   * @param pUserLogin
   *          user's login
   * @return {@link List} of {@link MembershipRequest} in progress
   * @throws ProjectServiceException
   */
  List<MembershipRequest> getInProcessRequestsForUser(String pUserLogin) throws ProjectServiceException;

  /**
   * This method will validate a request regarding an user and a project. It will also used the set of roles
   * to create the membership.
   * 
   * @param pProjectId
   * @param pUserLogin
   * @param pRoles
   * @param pCurrentLogin
   * @param pSendMail
   * @throws ProjectServiceException
   */
  void validateRequest(final String pProjectId, final String pUserLogin, final Set<String> pRoles,
      final String pDefaultRoleName, String pCurrentLogin, final boolean pSendMail)
      throws ProjectServiceException;

  /**
   * This method will invalidate a request regarding an user and a project.
   * 
   * @param pProjectId
   *          represents project if
   * @param pUserLogin
   *          represents the user's login which have requested a membership
   * @param pMesssage
   *          represents a message which will be send by email to user. It can be null or empty
   * @throws ProjectServiceException
   */
  void invalidateRequest(final String pProjectId, final String pUserLogin, final String pMesssage,
      final boolean pSendMail) throws ProjectServiceException;

}
