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
package org.novaforge.forge.plugins.bugtracker.jira.internal.client;

import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapClient;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapConnector;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapException;
import org.novaforge.forge.plugins.bugtracker.jira.soap.JiraSoapServiceServiceLocator;
import org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteAuthenticationException;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This class is used in order to instantiate new connector to jira web-service.
 * 
 * @author Gauthier Cart
 */
public class JiraSoapClientImpl implements JiraSoapClient
{

  private static final int SOAP_TIMEOUT = 30000;

  /**
   * {@inheritDoc}
   */
  @Override
  public JiraSoapConnector getConnector(final String pBaseUrl, final String pUsername, final String pPassword)
      throws JiraSoapException
  {
    JiraSoapConnector connector = null;
    URL url = null;
    try
    {
      url = new URL(pBaseUrl);
      final JirasoapserviceV2SoapBindingStub connectBindingStub = (JirasoapserviceV2SoapBindingStub) new JiraSoapServiceServiceLocator()
          .getJirasoapserviceV2(url);
      connectBindingStub.setTimeout(SOAP_TIMEOUT);

      final String authentificationToken = connectBindingStub.login(pUsername, pPassword);
      connector = new JiraSoapConnectorImpl(authentificationToken, connectBindingStub);
    }
    catch (final RemoteAuthenticationException e)
    {
      throw new JiraSoapException(String.format("Unable to logon Jira with [baseUrl=%s, login=%s]", pBaseUrl,
          pUsername), e);
    }
    catch (final MalformedURLException e)
    {
      throw new JiraSoapException(String.format("Unable to build URL object with [value=%s]", pBaseUrl), e);
    }
    catch (final Exception e)
    {
      throw new JiraSoapException(String.format("Unable to get the Jira Connector Binding with [URL=%s]",
          url.toString()), e);
    }
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteUser createUser(final JiraSoapConnector pConnector, final RemoteUser pRemoteUser,
      final String pPassword) throws JiraSoapException
  {

    RemoteUser remoteUser = null;

    try
    {
      remoteUser = pConnector.getConnectBindingStub().createUser(pConnector.getAuthenticationToken(),
          pRemoteUser.getName(), pPassword, pRemoteUser.getFullname(), pRemoteUser.getEmail());
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format(
          "Unable to create user with [username=%s, fullname=%s, e-mail=%s]", pRemoteUser.getName(),
          pRemoteUser.getFullname(), pRemoteUser.getEmail()), e);
    }

    return remoteUser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteUser getUser(final JiraSoapConnector pConnector, final String pUsername)
      throws JiraSoapException
  {
    RemoteUser remoteUser = null;

    try
    {
      remoteUser = pConnector.getConnectBindingStub().getUser(pConnector.getAuthenticationToken(), pUsername);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format("Unable to get the user with [username=%s]", pUsername), e);

    }

    return remoteUser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteUser updateUser(final JiraSoapConnector pConnector, final RemoteUser pRemoteUser)
      throws JiraSoapException
  {
    RemoteUser remoteUser = null;

    try
    {
      remoteUser = pConnector.getConnectBindingStub().updateUser(pConnector.getAuthenticationToken(),
          pRemoteUser);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format(
          "Unable to update user with [username=%s, fullname=%s, e-mail=%s]", pRemoteUser.getName(),
          pRemoteUser.getFullname(), pRemoteUser.getEmail()), e);
    }

    return remoteUser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUserPassword(final JiraSoapConnector pConnector, final RemoteUser pRemoteUser,
      final String pNewPassword) throws JiraSoapException
  {

    try
    {
      pConnector.getConnectBindingStub().setUserPassword(pConnector.getAuthenticationToken(), pRemoteUser,
          pNewPassword);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format("Unable to update the password of user with [username=%s]",
          pRemoteUser.getName()), e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteUser(final JiraSoapConnector pConnector, final String pUsername) throws JiraSoapException
  {
    try
    {
      pConnector.getConnectBindingStub().deleteUser(pConnector.getAuthenticationToken(), pUsername);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format("Unable to delete the user with [username=%s]", pUsername), e);

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addUserToGroup(final JiraSoapConnector pConnector, final RemoteGroup pGroup,
      final RemoteUser pUser) throws JiraSoapException
  {
    try
    {
      pConnector.getConnectBindingStub().addUserToGroup(pConnector.getAuthenticationToken(), pGroup, pUser);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format(
          "Unable to add the user to the group with [username=%s, group=%s]", pUser.getName(),
          pGroup.getName()), e);

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeUserFromGroup(final JiraSoapConnector pConnector, final RemoteGroup pGroup,
      final RemoteUser pUser) throws JiraSoapException
  {
    try
    {
      pConnector.getConnectBindingStub().removeUserFromGroup(pConnector.getAuthenticationToken(), pGroup,
          pUser);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format(
          "Unable to remove the user from the group with [username=%s, group=%s]", pUser.getName(),
          pGroup.getName()), e);

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteGroup getGroup(final JiraSoapConnector pConnector, final String pGroupName)
      throws JiraSoapException
  {
    RemoteGroup remoteGroup = null;

    try
    {
      remoteGroup = pConnector.getConnectBindingStub().getGroup(pConnector.getAuthenticationToken(),
          pGroupName);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format("Unable to get the group with [group name=%s]", pGroupName),
          e);

    }
    return remoteGroup;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addActorsToProjectRole(final JiraSoapConnector pConnector, final List<String> pActors,
      final RemoteProjectRole pProjectRole, final RemoteProject pRemoteProject, final String pActorType)
      throws JiraSoapException
  {
    try
    {
      pConnector.getConnectBindingStub().addActorsToProjectRole(pConnector.getAuthenticationToken(),
          pActors.toArray(new String[pActors.size()]), pProjectRole, pRemoteProject, pActorType);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(
          String.format(
              "Unable to add the user(s) to the project with [username(s)=%s, role=%s, project=%s, actor type=%s]",
              pActors, pProjectRole.getName(), pRemoteProject.getName(), pActorType), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeActorsFromProjectRole(final JiraSoapConnector pConnector, final List<String> pActors,
      final RemoteProjectRole pProjectRole, final RemoteProject pRemoteProject, final String pActorType)
      throws JiraSoapException
  {
    try
    {
      pConnector.getConnectBindingStub().removeActorsFromProjectRole(pConnector.getAuthenticationToken(),
          pActors.toArray(new String[pActors.size()]), pProjectRole, pRemoteProject, pActorType);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(
          String.format(
              "Unable to remove the user(s) from the project with [username(s)=%s, role=%s, project=%s, actor type=%s]",
              pActors, pProjectRole.getName(), pRemoteProject.getName(), pActorType), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteProjectRole getProjectRole(final JiraSoapConnector pConnector, final long pRoleId)
      throws JiraSoapException
  {
    RemoteProjectRole remoteProjectRole = null;

    try
    {
      remoteProjectRole = pConnector.getConnectBindingStub().getProjectRole(
          pConnector.getAuthenticationToken(), pRoleId);

    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format("Unable to get project role with [id=%s]", pRoleId), e);
    }

    return remoteProjectRole;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteProject createProject(final JiraSoapConnector pConnector, final RemoteProject pRemoteProject)
      throws JiraSoapException
  {
    RemoteProject remoteProject = null;

    try
    {
      remoteProject = pConnector.getConnectBindingStub().createProject(pConnector.getAuthenticationToken(),
          pRemoteProject.getKey(), pRemoteProject.getName(), pRemoteProject.getDescription(),
          pRemoteProject.getProjectUrl(), pRemoteProject.getLead(), pRemoteProject.getPermissionScheme(),
          pRemoteProject.getNotificationScheme(), pRemoteProject.getIssueSecurityScheme());
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format(
          "Unable to create project with [key=%s, name=%s, descritpion=%s, lead=%s]",
          pRemoteProject.getKey(), pRemoteProject.getName(), pRemoteProject.getDescription(),
          pRemoteProject.getLead()), e);
    }

    return remoteProject;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteProject getProjectById(final JiraSoapConnector pConnector, final long pProjectId)
      throws JiraSoapException
  {
    RemoteProject remoteProject = null;

    try
    {
      remoteProject = pConnector.getConnectBindingStub().getProjectById(pConnector.getAuthenticationToken(),
          pProjectId);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format("Unable to get the project with [id=%s]", pProjectId), e);
    }

    return remoteProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteProject getProjectByKey(final JiraSoapConnector pConnector, final String pProjectKey)
      throws JiraSoapException
  {
    RemoteProject remoteProject = null;

    try
    {
      remoteProject = pConnector.getConnectBindingStub().getProjectByKey(pConnector.getAuthenticationToken(),
          pProjectKey);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format("Unable to get the project with [key=%s]", pProjectKey), e);
    }

    return remoteProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteProject updateProject(final JiraSoapConnector pConnector, final RemoteProject pRemoteProject)
      throws JiraSoapException
  {
    RemoteProject remoteProject = null;

    try
    {
      remoteProject = pConnector.getConnectBindingStub().updateProject(pConnector.getAuthenticationToken(),
          pRemoteProject);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format(
          "Unable to update project with [key=%s, name=%s, descritpion=%s, lead=%s]",
          pRemoteProject.getKey(), pRemoteProject.getName(), pRemoteProject.getDescription(),
          pRemoteProject.getLead()), e);
    }

    return remoteProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteProject(final JiraSoapConnector pConnector, final String pProjectKey)
      throws JiraSoapException
  {
    try
    {
      pConnector.getConnectBindingStub().deleteProject(pConnector.getAuthenticationToken(), pProjectKey);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format("Unable to delete the project with [project key=%s]",
          pProjectKey), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteScheme[] getNotificationSchemes(final JiraSoapConnector pConnector) throws JiraSoapException
  {
    RemoteScheme[] remoteSchemes = null;

    try
    {
      remoteSchemes = pConnector.getConnectBindingStub().getNotificationSchemes(
          pConnector.getAuthenticationToken());
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException("Unable to delete the list of notification schemes", e);
    }

    return remoteSchemes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteScheme getNotificationSchemeById(final JiraSoapConnector pConnector,
      final long pNotificationSchemeId) throws JiraSoapException
  {
    RemoteScheme notificationScheme = new RemoteScheme();

    final RemoteScheme[] remoteSchemes = getNotificationSchemes(pConnector);

    for (final RemoteScheme remoteScheme : remoteSchemes)
    {
      if (remoteScheme.getId().equals(pNotificationSchemeId))
      {
        notificationScheme = remoteScheme;
        break;
      }
    }
    return notificationScheme;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteScheme[] getSecuritySchemes(final JiraSoapConnector pConnector) throws JiraSoapException
  {
    RemoteScheme[] remoteSchemes = null;

    try
    {
      remoteSchemes = pConnector.getConnectBindingStub().getSecuritySchemes(
          pConnector.getAuthenticationToken());
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException("Unable to delete the list of security schemes", e);
    }

    return remoteSchemes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteScheme getSecuritySchemeById(final JiraSoapConnector pConnector, final long pSecuritySchemeId)
      throws JiraSoapException
  {
    RemoteScheme securityScheme = new RemoteScheme();

    final RemoteScheme[] remoteSchemes = getSecuritySchemes(pConnector);
    if (remoteSchemes != null)
    {

      for (final RemoteScheme remoteScheme : remoteSchemes)
      {
        if (remoteScheme.getId().equals(pSecuritySchemeId))
        {
          securityScheme = remoteScheme;
          break;
        }
      }
    }
    return securityScheme;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemotePermissionScheme[] getPermissionSchemes(final JiraSoapConnector pConnector)
      throws JiraSoapException
  {
    RemotePermissionScheme[] remotePermissionSchemes = null;

    try
    {
      remotePermissionSchemes = pConnector.getConnectBindingStub().getPermissionSchemes(
          pConnector.getAuthenticationToken());
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException("Unable to delete the list of permissions schemes", e);
    }

    return remotePermissionSchemes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemotePermissionScheme getPermissionSchemeById(final JiraSoapConnector pConnector,
      final long pPermissionSchemesId) throws JiraSoapException
  {
    RemotePermissionScheme permissionScheme = new RemotePermissionScheme();

    final RemotePermissionScheme[] remotePermissionSchemes = getPermissionSchemes(pConnector);

    for (final RemotePermissionScheme remotePermissionScheme : remotePermissionSchemes)
    {
      if (remotePermissionScheme.getId().equals(pPermissionSchemesId))
      {
        permissionScheme = remotePermissionScheme;
        break;
      }
    }

    return permissionScheme;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteProjectRoleActors getProjectRoleActors(final JiraSoapConnector pConnector,
      final RemoteProjectRole pRemoteProjectRole, final RemoteProject pRemoteProject)
      throws JiraSoapException
  {

    RemoteProjectRoleActors remoteProjectRoleActors = new RemoteProjectRoleActors();

    try
    {
      remoteProjectRoleActors = pConnector.getConnectBindingStub().getProjectRoleActors(
          pConnector.getAuthenticationToken(), pRemoteProjectRole, pRemoteProject);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format(
          "Unable to get the actors of the project with [project key=%s, project role=%s]",
          pRemoteProject.getKey(), pRemoteProjectRole.getName()), e);
    }

    return remoteProjectRoleActors;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteIssue createIssue(final JiraSoapConnector pConnector, final RemoteIssue pRemoteIssue)
      throws JiraSoapException
  {

    RemoteIssue remoteIssue = null;

    try
    {
      remoteIssue = pConnector.getConnectBindingStub().createIssue(pConnector.getAuthenticationToken(),
          pRemoteIssue);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(
          String.format(
              "Unable to create the issue  with [project name=%s, summary=%s, description=%s, assignee=%s, reporter=%s, issue type=%s ]",
              pRemoteIssue.getProject(), pRemoteIssue.getSummary(), pRemoteIssue.getDescription(),
              pRemoteIssue.getAssignee(), pRemoteIssue.getReporter(), pRemoteIssue.getType()), e);
    }

    return remoteIssue;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteIssue getIssue(final JiraSoapConnector pConnector, final String pIssueKey)
      throws JiraSoapException
  {

    RemoteIssue remoteIssue = null;

    try
    {
      remoteIssue = pConnector.getConnectBindingStub().getIssue(pConnector.getAuthenticationToken(),
          pIssueKey);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format("Unable to get the issue with [ issue id=%s ]", pIssueKey), e);
    }

    return remoteIssue;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteIssue updateIssue(final JiraSoapConnector pConnector, final String pIssueKey,
      final RemoteFieldValue[] pActionParams) throws JiraSoapException
  {

    RemoteIssue remoteIssue = null;

    try
    {
      remoteIssue = pConnector.getConnectBindingStub().updateIssue(pConnector.getAuthenticationToken(),
          pIssueKey, pActionParams);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(
          String.format("Unable to update the issue with [ issue id=%s ]", pIssueKey), e);
    }

    return remoteIssue;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteIssue(final JiraSoapConnector pConnector, final String pIssueKey)
      throws JiraSoapException
  {
    try
    {
      pConnector.getConnectBindingStub().deleteIssue(pConnector.getAuthenticationToken(), pIssueKey);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format("Unable to get the issue with [ issue id=%s ]", pIssueKey), e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteIssue[] getIssuesFromTextSearchWithProject(final JiraSoapConnector pConnector,
      final String[] pProjectKeys, final String pSearchTerms, final int pMaxNumResults)
      throws JiraSoapException
  {

    RemoteIssue[] remoteIssue = null;

    try
    {
      remoteIssue = pConnector.getConnectBindingStub().getIssuesFromTextSearchWithProject(
          pConnector.getAuthenticationToken(), pProjectKeys, pSearchTerms, pMaxNumResults);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format("Unable to search issues with [ search terms =%s ]",
          pSearchTerms), e);
    }

    return remoteIssue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteIssue[] getIssuesFromJqlSearch(final JiraSoapConnector pConnector, final String pJqlSearch,
      final int pMaxNumResults) throws JiraSoapException
  {
    RemoteIssue[] remoteIssue = null;

    try
    {
      remoteIssue = pConnector.getConnectBindingStub().getIssuesFromJqlSearch(
          pConnector.getAuthenticationToken(), pJqlSearch, pMaxNumResults);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format("Unable to search issues with [ jql search query =%s ]",
          pJqlSearch), e);
    }

    return remoteIssue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteVersion[] getVersions(final JiraSoapConnector pConnector, final String pProjectKey)
      throws JiraSoapException
  {
    RemoteVersion[] fixVersions = null;

    try
    {
      fixVersions = pConnector.getConnectBindingStub().getVersions(pConnector.getAuthenticationToken(),
          pProjectKey);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String.format("Unable to get project versions with [ project key =%s ]",
          pProjectKey), e);
    }
    return fixVersions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteVersion addVersion(final JiraSoapConnector pConnector, final String pProjectKey,
                                  final RemoteVersion pRemoteVersion) throws JiraSoapException
  {

    RemoteVersion version = null;

    try
    {
      version = pConnector.getConnectBindingStub().addVersion(pConnector.getAuthenticationToken(), pProjectKey,
                                                              pRemoteVersion);
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException(String
                                      .format("Unable to add project versions with [ project key =%s, version name =%s ]",
                                              pProjectKey, pRemoteVersion.getName()), e);
    }
    return version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteStatus[] getStatuses(final JiraSoapConnector pConnector) throws JiraSoapException
  {
    RemoteStatus[] status = null;

    try
    {
      status = pConnector.getConnectBindingStub().getStatuses(pConnector.getAuthenticationToken());
    }
    catch (final RemoteException e)
    {
      throw new JiraSoapException("Unable to get statuses", e);
    }
    return status;
  }
}
