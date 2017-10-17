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
package org.novaforge.forge.plugins.bugtracker.jira.client;

import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteFieldValue;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteGroup;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteIssue;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemotePermissionScheme;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProject;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProjectRole;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProjectRoleActors;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteScheme;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteStatus;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteUser;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteVersion;

import java.util.List;

/**
 * @author Gauthier Cart
 */
public interface JiraSoapClient
{
  /**
   * @return Use to connect to jira instance with specific url, username and password.
   * @param baseUrl
   *          represents the url of jira instance
   * @param username
   *          represents username used to log in
   * @param password
   *          represents password used to log in
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built can occured if connection failed
   *           or client can be built
   * @throws
   */
  JiraSoapConnector getConnector(final String pBaseUrl, final String pUsername, final String pPassword)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @param pRemoteUser
   * @return RemoteUser
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#createUser)
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteUser createUser(final JiraSoapConnector pConnector, final RemoteUser pRemoteUser,
      final String pPassword) throws JiraSoapException;

  /**
   * @param pConnector
   * @param pUsername
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#getUser
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteUser getUser(final JiraSoapConnector pConnector, final String pUsername) throws JiraSoapException;

  /**
   * @param pConnector
   * @param pRemoteUser
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#updateUser
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteUser updateUser(final JiraSoapConnector pConnector, final RemoteUser pRemoteUser)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @param pRemoteUser
   * @param pNewPassword
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#setUserPassword
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  void setUserPassword(final JiraSoapConnector pConnector, final RemoteUser pRemoteUser,
      final String pNewPassword) throws JiraSoapException;

  /**
   * @param pConnector
   * @param pUsername
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#deleteUser
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  void deleteUser(final JiraSoapConnector pConnector, final String pUsername) throws JiraSoapException;

  /**
   * @param pConnector
   * @param pGroup
   * @param pUser
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#addUserToGroup
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  void addUserToGroup(final JiraSoapConnector pConnector, final RemoteGroup pGroup, final RemoteUser pUser)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @param pGroup
   * @param pUser
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#removeUserFromGroup
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  void removeUserFromGroup(final JiraSoapConnector pConnector, final RemoteGroup pGroup,
      final RemoteUser pUser) throws JiraSoapException;

  /**
   * @param pConnector
   * @param pGroupName
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#getGroup
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteGroup getGroup(final JiraSoapConnector pConnector, final String pGroupName) throws JiraSoapException;

  /**
   * @param pConnector
   * @param pActors
   * @param pProjectRole
   * @param pRemoteProject
   * @param pActorType
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#addActorsToProjectRole
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  void addActorsToProjectRole(final JiraSoapConnector pConnector, final List<String> pActors,
      final RemoteProjectRole pProjectRole, RemoteProject pRemoteProject, final String pActorType)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @param pActors
   * @param pProjectRole
   * @param pRemoteProject
   * @param pActorType
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#removeActorsToProjectRole
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  void removeActorsFromProjectRole(final JiraSoapConnector pConnector, final List<String> pActors,
      final RemoteProjectRole pProjectRole, RemoteProject pRemoteProject, final String pActorType)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @param pRoleId
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#getProjectRole
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteProjectRole getProjectRole(final JiraSoapConnector pConnector, final long pRoleId)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @param pRemoteProject
   * @return RemoteProject
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#createProject
   *      (org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapConnector,
   *      org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProject)
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteProject createProject(final JiraSoapConnector pConnector, final RemoteProject pRemoteProject)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @param pProjectId
   * @return RemoteProject
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#getProjectById()
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteProject getProjectById(final JiraSoapConnector pConnector, final long pProjectId)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @param pProjectKey
   * @return RemoteProject
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#getProjectByKey()
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteProject getProjectByKey(final JiraSoapConnector pConnector, final String pProjectKey)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @param pRemoteProject
   * @return RemoteProject
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#updateProject()
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteProject updateProject(final JiraSoapConnector pConnector, final RemoteProject pRemoteProject)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @param pprojectKey
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#deleteProject
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  void deleteProject(final JiraSoapConnector pConnector, final String pProjectKey) throws JiraSoapException;

  /**
   * @param pConnector
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#getNotificationSchemes
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteScheme[] getNotificationSchemes(final JiraSoapConnector pConnector) throws JiraSoapException;

  /**
   * @param pConnector
   * @param pNotificationSchemeId
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteScheme getNotificationSchemeById(final JiraSoapConnector pConnector, final long pNotificationSchemeId)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#getSecuritySchemes
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteScheme[] getSecuritySchemes(final JiraSoapConnector pConnector) throws JiraSoapException;

  /**
   * @param pConnector
   * @param pSecuritySchemeId
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteScheme getSecuritySchemeById(final JiraSoapConnector pConnector, final long pSecuritySchemeId)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @see org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub.java#getSecuritySchemes
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemotePermissionScheme[] getPermissionSchemes(final JiraSoapConnector pConnector) throws JiraSoapException;

  /**
   * @param pConnector
   * @param pSecuritySchemeId
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemotePermissionScheme getPermissionSchemeById(final JiraSoapConnector pConnector,
      final long pPermissionSchemesId) throws JiraSoapException;

  /**
   * @param pConnector
   * @param pRemoteProjectRole
   * @param pRemoteProject
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteProjectRoleActors getProjectRoleActors(final JiraSoapConnector pConnector,
      final RemoteProjectRole pRemoteProjectRole, final RemoteProject pRemoteProject)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @param pRemoteIssue
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteIssue createIssue(final JiraSoapConnector pConnector, final RemoteIssue pRemoteIssue)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @param pIssueKey
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteIssue getIssue(final JiraSoapConnector pConnector, final String pIssueKey) throws JiraSoapException;

  /**
   * @param pConnector
   * @param pIssueKey
   * @param pActionParams
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteIssue updateIssue(final JiraSoapConnector pConnector, final String pIssueKey,
      final RemoteFieldValue[] pActionParams) throws JiraSoapException;

  /**
   * @param pConnector
   * @param pIssueKey
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  void deleteIssue(final JiraSoapConnector pConnector, final String pIssueKey) throws JiraSoapException;

  /**
   * @param pConnector
   * @param pProjectKeys
   * @param pSearchTerms
   * @param pMaxNumResults
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteIssue[] getIssuesFromTextSearchWithProject(final JiraSoapConnector pConnector,
      final String[] pProjectKeys, final String pSearchTerms, final int pMaxNumResults)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @param pJqlSearch
   * @param pMaxNumResults
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteIssue[] getIssuesFromJqlSearch(final JiraSoapConnector pConnector, final String pJqlSearch,
      final int pMaxNumResults) throws JiraSoapException;

  /**
   * @param pConnector
   * @param projectKey
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteVersion[] getVersions(final JiraSoapConnector pConnector, final String pProjectKey)
      throws JiraSoapException;

  /**
   * @param pConnector
   * @param projectKey
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteVersion addVersion(final JiraSoapConnector pConnector, final String pProjectKey,
      final RemoteVersion pRemoteVersion) throws JiraSoapException;

  /**
   * @param pConnector
   * @return
   * @throws JiraSoapException
   *           can occured if jira action failed or client can be built
   */
  RemoteStatus[] getStatuses(JiraSoapConnector pConnector) throws JiraSoapException;

}
