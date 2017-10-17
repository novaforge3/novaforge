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

import org.hamcrest.CoreMatchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapClient;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapConnector;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapException;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteAuthenticationException;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteException;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteFieldValue;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteGroup;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteIssue;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemotePermissionException;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemotePermissionScheme;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProject;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProjectRole;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProjectRoleActors;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteScheme;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteStatus;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteUser;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteValidationException;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteVersion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Gauthier Cart
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JiraSoapClientImplTest
{
  /**
   * private static final String ENDPOINT = "http://vm-infra-8:8080/rpc/soap/jirasoapservice-v2";
   * private static final String USERNAME = "cart-g";
   * private static final String PASSWORD = "totopouet";
   **/
  private static final String            ENDPOINT                       = "http://gauthier-Precision-T1650:2990/jira/rpc/soap/jirasoapservice-v2";
  private static final String            USERNAME                       = "admin";
  private static final String            PASSWORD                       = "admin";
  private static final String            BASE_URL_REST                  = "http://gauthier-Precision-T1650:2990/jira";
  private static final String            ENDPOINT_REST                  = "http://gauthier-Precision-T1650:2990/jira/rest/api/latest";

  private static final String            USERNAME_ALT                   = "Toto";
  private static final String            EMAIL_ALT                      = ".net";
  private static final String            FULLNAME_ALT                   = "Toto Pouet";
  private static final String            PASSWORD_ALT                   = "totopouet";

  private static final String            EMAIL_ALT_UP                   = ".net";
  private static final String            FULLNAME_ALT_UP                = "Toto Pouet Up";
  private static final String            PASSWORD_ALT_UP                = "totopouet Up";

  private static final String            KEY                            = "MONPROJETG";
  private static final String            LEAD                           = "cart-g";
  private static final String            NAME                           = "TEST PROJET";
  private static final String            DESCRIPTION                    = "C'est le projet de test.";
  private static final String            URL_PROJECT                    = "http://www.toto.com/";

  private static final String            NAME_UP                        = "TEST PROJET UP";
  private static final String            DESCRIPTION_UP                 = "C'est le projet up de test.";
  private static final String            URL_PROJECT_UP                 = "http://www.toto-up.com/";

  private static final Long              SECURITY_SCHEME_ID             = (long) 10000;
  private static final Long              NOTIFICATION_SCHEME_ID         = (long) 10000;
  private static final Long              PERMISSION_SCHEME_ID           = (long) 0;

  private static final String            PROJECT_ROLE_DESCRIPTION       = "A project role that represents developers in a project";
  private static final Long              PROJECT_ROLE_ID                = (long) 10001;
  private static final String            PROJECT_ROLE_NAME              = "Developers";

  private static final Long              PROJECT_ROLE_ADMIN_ID          = (long) 10002;
  private static final String            PROJECT_ROLE_ADMIN_NAME        = "Administrators";
  private static final String            PROJECT_ROLE_ADMIN_DESCRIPTION = "A project role that represents administrators in a project";

  private static final String            ACTOR_TYPE                     = "atlassian-user-role-actor";

  private static final String            GROUP                          = "jira-developers";

  private static final RemoteProjectRole remoteProjectRole              = new RemoteProjectRole(
                                                                            PROJECT_ROLE_DESCRIPTION,
                                                                            PROJECT_ROLE_ID,
                                                                            PROJECT_ROLE_NAME);

  private static final RemoteProjectRole remoteProjectRoleAdmin         = new RemoteProjectRole(
                                                                            PROJECT_ROLE_ADMIN_DESCRIPTION,
                                                                            PROJECT_ROLE_ADMIN_ID,
                                                                            PROJECT_ROLE_ADMIN_NAME);

  private static final String            ISSUE_DESCRIPTION              = "DESCRIPTION ISSUE DE TEST A CREER";
  private static final String            ISSUE_DESCRIPTION_DELETE       = "DESCRIPTION ISSUE DE TEST A SUPPRIMER";
  private static final String            ISSUE_DESCRIPTION_UPDATE       = "DESCRIPTION ISSUE DE TEST A UPDATER";
  private static final String            ISSUE_SUMMARY                  = "SUMMARY ISSUE DE TEST";

  // The ID of the issue type BUG.
  private static final String            ISSUE_TYPE                     = "1";
  private static final RemoteIssue       remoteIssue                    = new RemoteIssue(null, null,
                                                                            USERNAME_ALT, null, null, null,
                                                                            null, ISSUE_DESCRIPTION, null,
                                                                            null, null, null, null, KEY,
                                                                            USERNAME_ALT, null, null,
                                                                            ISSUE_SUMMARY, ISSUE_TYPE, null,
                                                                            null);

  private static final String            VERSION_NAME                   = "TEST VERSION";
  private static final Calendar          VERSION_RELEASE_DATE           = null;
  private static final RemoteVersion     remoteVersion                  = new RemoteVersion(null,
                                                                            VERSION_NAME, false,
                                                                            VERSION_RELEASE_DATE, false, null);

  private boolean                        jiraProfileActivated           = false;

  public JiraSoapClientImplTest()
  {
    final String property = System.getProperty("jira.profile");
    if ("true".equals(property))
    {
      jiraProfileActivated = true;
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getConnector(java.lang.String, java.lang.String, java.lang.String)}
   * .
   * 
   * @throws JiraSoapException
   */
  @Test
  public void test00GetConnector() throws JiraSoapException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);
      assertThat(connector, notNullValue());
      assertThat(connector.getAuthenticationToken(), notNullValue());
      assertThat(connector.getConnectBindingStub(), notNullValue());
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#createProject(org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapConnector, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.novaforge.forge.plugins.bugtracker.jira.soap.RemotePermissionScheme, org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteScheme, org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteScheme)}
   * .
   * 
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   *           import org.mockito.Matchers; * @throws RemotePermissionException
   */
  @Test
  public void test01CreateProject() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final RemoteScheme issueSecurityScheme = getSecuritySchemeById(SECURITY_SCHEME_ID);
      final RemoteScheme notificationScheme = getNotificationSchemeById(NOTIFICATION_SCHEME_ID);
      final RemotePermissionScheme permissionScheme = getPermissionSchemeById(PERMISSION_SCHEME_ID);

      // Build the RemoteProject to create.
      RemoteProject remoteProject = new RemoteProject(null, NAME, DESCRIPTION, issueSecurityScheme, KEY,
          LEAD, notificationScheme, permissionScheme, URL_PROJECT, null);

      remoteProject = jiraSoapClient.createProject(connector, remoteProject);

      assertThat(remoteProject, notNullValue());
      assertThat(remoteProject.getName(), CoreMatchers.is(NAME));
      assertThat(remoteProject.getKey(), CoreMatchers.is(KEY));
      assertThat(remoteProject.getId(), notNullValue());
      assertThat(remoteProject.getUrl(), notNullValue());
      assertThat(remoteProject.getIssueSecurityScheme().getId(), CoreMatchers.is(SECURITY_SCHEME_ID));
      assertThat(remoteProject.getNotificationScheme().getId(), CoreMatchers.is(NOTIFICATION_SCHEME_ID));
      assertThat(remoteProject.getPermissionScheme().getId(), CoreMatchers.is(PERMISSION_SCHEME_ID));
    }
  }

  private RemoteScheme getSecuritySchemeById(final long pSecuritySchemeId)
      throws JiraSoapException, RemotePermissionException, RemoteAuthenticationException, RemoteException,
                 java.rmi.RemoteException
  {
    RemoteScheme securityScheme = new RemoteScheme();

    final JiraSoapClient    jiraSoapClient = new JiraSoapClientImpl();
    final JiraSoapConnector connector      = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

    final RemoteScheme[] remoteSchemes = connector.getConnectBindingStub().getSecuritySchemes(connector
                                                                                                  .getAuthenticationToken());

    for (final RemoteScheme remoteScheme : remoteSchemes)
    {
      if (remoteScheme.getId().equals(pSecuritySchemeId))
      {
        securityScheme = remoteScheme;
        break;
      }
    }
    return securityScheme;
  }

  private RemoteScheme getNotificationSchemeById(final long pNotificationSchemeId)
      throws JiraSoapException, RemotePermissionException, RemoteAuthenticationException, RemoteException,
                 java.rmi.RemoteException
  {
    RemoteScheme notificationScheme = new RemoteScheme();

    final JiraSoapClient    jiraSoapClient = new JiraSoapClientImpl();
    final JiraSoapConnector connector      = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

    final RemoteScheme[] remoteSchemes = connector.getConnectBindingStub().getNotificationSchemes(connector
                                                                                                      .getAuthenticationToken());

    for (final RemoteScheme remoteScheme : remoteSchemes)
    {
      if (remoteScheme.getId().equals(NOTIFICATION_SCHEME_ID))
      {
        notificationScheme = remoteScheme;
        break;
      }
    }

    return notificationScheme;
  }

  private RemotePermissionScheme getPermissionSchemeById(final long pPermissionSchemesId)
      throws JiraSoapException, RemotePermissionException, RemoteAuthenticationException, RemoteException,
                 java.rmi.RemoteException
  {
    RemotePermissionScheme permissionScheme = new RemotePermissionScheme();

    final JiraSoapClient    jiraSoapClient = new JiraSoapClientImpl();
    final JiraSoapConnector connector      = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

    final RemotePermissionScheme[] remotePermissionSchemes = connector.getConnectBindingStub()
                                                                      .getPermissionSchemes(connector
                                                                                                .getAuthenticationToken());

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
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getProjectById()} .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test02GetProjectById() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {

      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final String projectId = connector.getConnectBindingStub()
          .getProjectByKey(connector.getAuthenticationToken(), KEY).getId();

      final RemoteProject remoteProject = jiraSoapClient.getProjectById(connector, Long.valueOf(projectId)
          .longValue());

      assertThat(remoteProject, notNullValue());
      assertThat(remoteProject.getName(), CoreMatchers.is(NAME));
      assertThat(remoteProject.getKey(), CoreMatchers.is(KEY));
      assertThat(remoteProject.getId(), CoreMatchers.is(projectId));
      assertThat(remoteProject.getDescription(), CoreMatchers.is(DESCRIPTION));

    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getProjectByKey()}
   * .
   *
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test03GetProjectByKey() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {

      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final String projectId = connector.getConnectBindingStub()
          .getProjectByKey(connector.getAuthenticationToken(), KEY).getId();

      final RemoteProject remoteProject = jiraSoapClient.getProjectByKey(connector, KEY);

      assertThat(remoteProject, notNullValue());
      assertThat(remoteProject.getName(), CoreMatchers.is(NAME));
      assertThat(remoteProject.getKey(), CoreMatchers.is(KEY));
      assertThat(remoteProject.getId(), CoreMatchers.is(projectId));
      assertThat(remoteProject.getDescription(), CoreMatchers.is(DESCRIPTION));

    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#updateProject()} .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test04UpdateProject() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      RemoteProject remoteProject = connector.getConnectBindingStub().getProjectByKey(
          connector.getAuthenticationToken(), KEY);

      final RemoteScheme issueSecurityScheme = getSecuritySchemeById(SECURITY_SCHEME_ID);
      final RemoteScheme notificationScheme = getNotificationSchemeById(NOTIFICATION_SCHEME_ID);
      final RemotePermissionScheme permissionScheme = getPermissionSchemeById(PERMISSION_SCHEME_ID);

      remoteProject.setDescription(DESCRIPTION_UP);
      remoteProject.setName(NAME_UP);
      remoteProject.setProjectUrl(URL_PROJECT_UP);
      remoteProject.setIssueSecurityScheme(issueSecurityScheme);
      remoteProject.setNotificationScheme(notificationScheme);
      remoteProject.setPermissionScheme(permissionScheme);

      remoteProject = jiraSoapClient.updateProject(connector, remoteProject);

      assertThat(remoteProject, notNullValue());
      assertThat(remoteProject.getName(), CoreMatchers.is(NAME_UP));
      assertThat(remoteProject.getDescription(), CoreMatchers.is(DESCRIPTION_UP));
      assertThat(remoteProject.getProjectUrl(), CoreMatchers.is(URL_PROJECT_UP));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#createUser} .
   *
   * @throws JiraSoapException
   */
  @Test
  public void test05CreateUser() throws JiraSoapException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      RemoteUser remoteUser = new RemoteUser(EMAIL_ALT, FULLNAME_ALT, USERNAME_ALT);

      remoteUser = jiraSoapClient.createUser(connector, remoteUser, PASSWORD_ALT);

      assertThat(remoteUser, notNullValue());
      assertThat(remoteUser.getName(), CoreMatchers.is(USERNAME_ALT));
      assertThat(remoteUser.getEmail(), CoreMatchers.is(EMAIL_ALT));

    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getUser(org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapConnector, java.lang.String)}
   * .
   *
   * @throws JiraSoapException
   */
  @Test
  public void test06GetUser() throws JiraSoapException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final RemoteUser remoteUser = jiraSoapClient.getUser(connector, USERNAME_ALT);

      assertThat(remoteUser, notNullValue());
      assertThat(remoteUser.getName(), CoreMatchers.is(USERNAME_ALT));
      assertThat(remoteUser.getEmail(), notNullValue());

    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#updateUser()} .
   *
   * @throws JiraSoapException
   */
  @Test
  public void test07UpdateUser() throws JiraSoapException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      RemoteUser remoteUser = new RemoteUser(EMAIL_ALT_UP, FULLNAME_ALT_UP, USERNAME_ALT);

      remoteUser = jiraSoapClient.updateUser(connector, remoteUser);

      assertThat(remoteUser, notNullValue());
      assertThat(remoteUser.getName(), CoreMatchers.is(USERNAME_ALT));
      assertThat(remoteUser.getEmail(), CoreMatchers.is(EMAIL_ALT_UP));
      assertThat(remoteUser.getFullname(), CoreMatchers.is(FULLNAME_ALT_UP));

    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#setUserPassword()}
   * .
   *
   * @throws JiraSoapException
   */
  @Test
  public void test08SetUserPassword() throws JiraSoapException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final RemoteUser remoteUser = new RemoteUser(EMAIL_ALT_UP, FULLNAME_ALT_UP, USERNAME_ALT);

      jiraSoapClient.setUserPassword(connector, remoteUser, PASSWORD_ALT_UP);

    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#addUserToGroup()} .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemoteValidationException
   * @throws RemotePermissionException
   */
  @Test
  public void test09AddUserToGroup() throws JiraSoapException, RemotePermissionException,
      RemoteValidationException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final RemoteUser remoteUser = new RemoteUser(EMAIL_ALT, FULLNAME_ALT, USERNAME_ALT);
      RemoteGroup remoteGroup = connector.getConnectBindingStub().getGroup(
          connector.getAuthenticationToken(), GROUP);

      jiraSoapClient.addUserToGroup(connector, remoteGroup, remoteUser);

      boolean found = false;

      remoteGroup = connector.getConnectBindingStub().getGroup(connector.getAuthenticationToken(), GROUP);

      for (final RemoteUser remoteUserAlt : remoteGroup.getUsers())
      {
        if (remoteUserAlt.getName().equals(USERNAME_ALT))
        {
          found = true;
          break;
        }
      }
      assertTrue(found);

    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#removeUserFromGroup()}
   * .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemoteValidationException
   * @throws RemotePermissionException
   */
  @Test
  public void test10RemoveUserFromGroup() throws JiraSoapException, RemotePermissionException,
      RemoteValidationException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final RemoteUser remoteUser = new RemoteUser(EMAIL_ALT, FULLNAME_ALT, USERNAME_ALT);
      RemoteGroup remoteGroup = connector.getConnectBindingStub().getGroup(
          connector.getAuthenticationToken(), GROUP);

      jiraSoapClient.removeUserFromGroup(connector, remoteGroup, remoteUser);

      boolean found = true;

      remoteGroup = connector.getConnectBindingStub().getGroup(connector.getAuthenticationToken(), GROUP);

      for (final RemoteUser remoteUserAlt : remoteGroup.getUsers())
      {
        if (remoteUserAlt.getName().equals(USERNAME_ALT))
        {
          found = false;
        }
      }
      assertTrue(found);

    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getGroup()} .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemoteValidationException
   * @throws RemotePermissionException
   */
  @Test
  public void test11GetGroup() throws JiraSoapException, RemotePermissionException,
      RemoteValidationException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final RemoteGroup remoteGroup = jiraSoapClient.getGroup(connector, GROUP);

      assertThat(remoteGroup, notNullValue());
      assertThat(remoteGroup.getName(), CoreMatchers.is(GROUP));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#addActorsToProjectRole}
   * .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test12AddActorsToProjectRole() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final List<String> actorsList = new ArrayList<String>();
      actorsList.add(USERNAME_ALT);
      final String projectId = connector.getConnectBindingStub()
          .getProjectByKey(connector.getAuthenticationToken(), KEY).getId();
      final RemoteProject remoteProject = new RemoteProject(projectId, null, null, null, null, null, null,
          null, null, null);

      jiraSoapClient.addActorsToProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
          ACTOR_TYPE);

      // Get a list of RemoteUser associated to the project remoteProjectRole.
      final RemoteUser[] users = connector.getConnectBindingStub()
          .getProjectRoleActors(connector.getAuthenticationToken(), remoteProjectRole, remoteProject)
          .getUsers();
      final List<RemoteUser> list = Arrays.asList(users);
      assertTrue(list.contains(jiraSoapClient.getUser(connector, USERNAME_ALT)));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getProjectRoleActors}
   * .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test13GetProjectRoleActors() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final String projectId = connector.getConnectBindingStub()
          .getProjectByKey(connector.getAuthenticationToken(), KEY).getId();
      final RemoteProject remoteProject = new RemoteProject(projectId, null, null, null, null, null, null,
          null, null, null);

      RemoteProjectRoleActors remoteProjectRoleActors = new RemoteProjectRoleActors();

      remoteProjectRoleActors = jiraSoapClient.getProjectRoleActors(connector, remoteProjectRole,
          remoteProject);

      final RemoteUser[] users = remoteProjectRoleActors.getUsers();

      final List<RemoteUser> list = Arrays.asList(users);
      assertTrue(list.contains(jiraSoapClient.getUser(connector, USERNAME_ALT)));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#addVersion} .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   */
  @Test
  public void test14AddVersion() throws JiraSoapException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      RemoteVersion addedRemoteVersion = connector.getConnectBindingStub().addVersion(
          connector.getAuthenticationToken(), KEY, remoteVersion);

      assertThat(addedRemoteVersion, notNullValue());
      assertThat(addedRemoteVersion.getName(), CoreMatchers.is(VERSION_NAME));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#addVersion} .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   */
  @Test
  public void test15GetVersions() throws JiraSoapException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      RemoteVersion[] remoteVersions = connector.getConnectBindingStub().getVersions(
          connector.getAuthenticationToken(), KEY);

      assertThat(remoteVersions, notNullValue());
      assertThat(remoteVersions[0].getName(), CoreMatchers.is(VERSION_NAME));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getProjectRoleActors}
   * .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test16CreateIssue() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      // Add the user USERNAME to the project in order to get the permission to browse the project
      final List<String> actorsList = new ArrayList<String>();
      actorsList.add(USERNAME);
      final String projectId = connector.getConnectBindingStub()
          .getProjectByKey(connector.getAuthenticationToken(), KEY).getId();
      final RemoteProject remoteProject = new RemoteProject(projectId, null, null, null, null, null, null,
          null, null, null);

      jiraSoapClient.addActorsToProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
          ACTOR_TYPE);

      // Create the issue
      final RemoteIssue createdRemoteIssue = connector.getConnectBindingStub().createIssue(
          connector.getAuthenticationToken(), remoteIssue);

      // Remove the user USERNAME from the project
      jiraSoapClient.removeActorsFromProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
          ACTOR_TYPE);

      assertThat(createdRemoteIssue, notNullValue());
      assertThat(createdRemoteIssue.getId(), notNullValue());
      assertThat(createdRemoteIssue.getProject(), CoreMatchers.is(KEY));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getProjectRoleActors}
   * .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test17GetIssue() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      // Add the user USERNAME to the project in order to get the permission to browse the project
      final List<String> actorsList = new ArrayList<String>();
      actorsList.add(USERNAME);
      final String projectId = connector.getConnectBindingStub()
          .getProjectByKey(connector.getAuthenticationToken(), KEY).getId();
      final RemoteProject remoteProject = new RemoteProject(projectId, null, null, null, null, null, null,
          null, null, null);

      jiraSoapClient.addActorsToProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
          ACTOR_TYPE);

      // Create the issue
      final RemoteIssue createdRemoteIssue = connector.getConnectBindingStub().createIssue(
          connector.getAuthenticationToken(), remoteIssue);

      // Get the created issue
      final RemoteIssue gotRemoteIssue = connector.getConnectBindingStub().getIssue(
          connector.getAuthenticationToken(), createdRemoteIssue.getKey());

      // Remove the user USERNAME from the project
      jiraSoapClient.removeActorsFromProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
          ACTOR_TYPE);

      assertThat(gotRemoteIssue, notNullValue());
      assertThat(gotRemoteIssue.getKey(), CoreMatchers.is(createdRemoteIssue.getKey()));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getProjectRoleActors}
   * .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test18GetIssuesFromTextSearchWithProject() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final List<String> actorsList = new ArrayList<String>();
      actorsList.add(USERNAME);
      final String projectId = connector.getConnectBindingStub()
          .getProjectByKey(connector.getAuthenticationToken(), KEY).getId();
      final RemoteProject remoteProject = new RemoteProject(projectId, null, null, null, null, null, null,
          null, null, null);

      // Add the user USERNAME to the project in order to get the permission to browse the project
      jiraSoapClient.addActorsToProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
          ACTOR_TYPE);

      // Create the array of projet key
      final String[] projectList = new String[] { KEY };

      final RemoteIssue[] remoteIssues = jiraSoapClient.getIssuesFromTextSearchWithProject(connector,
          projectList, ISSUE_DESCRIPTION, 10);

      // Remove the user USERNAME from the project
      jiraSoapClient.removeActorsFromProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
          ACTOR_TYPE);

      assertThat(remoteIssues[0], notNullValue());
      assertThat(remoteIssues[0].getDescription(), CoreMatchers.is(ISSUE_DESCRIPTION));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getProjectRoleActors}
   * .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test19GetIssuesFromJqlSearch() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final List<String> actorsList = new ArrayList<String>();
      actorsList.add(USERNAME);
      final String projectId = connector.getConnectBindingStub()
          .getProjectByKey(connector.getAuthenticationToken(), KEY).getId();
      final RemoteProject remoteProject = new RemoteProject(projectId, null, null, null, null, null, null,
          null, null, null);

      // Add the user USERNAME to the project in order to get the permission to browse the project
      jiraSoapClient.addActorsToProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
          ACTOR_TYPE);

      // Construct the jql search query

      final RemoteIssue[] remoteIssues = jiraSoapClient.getIssuesFromJqlSearch(connector,
                                                                               "project = " + KEY + " AND text ~ \""
                                                                                   + ISSUE_DESCRIPTION + "\"", 10);

      // Remove the user USERNAME from the project
      jiraSoapClient.removeActorsFromProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
          ACTOR_TYPE);

      assertThat(remoteIssues[0], notNullValue());
      assertThat(remoteIssues[0].getDescription(), CoreMatchers.is(ISSUE_DESCRIPTION));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getProjectRoleActors}
   * .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test20UpdateIssue() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final List<String> actorsList = new ArrayList<String>();
      actorsList.add(USERNAME);
      final String projectId = connector.getConnectBindingStub()
          .getProjectByKey(connector.getAuthenticationToken(), KEY).getId();
      final RemoteProject remoteProject = new RemoteProject(projectId, null, null, null, null, null, null,
          null, null, null);

      // Create the array of project key
      final String[] projectList = new String[] { KEY };

      // Add the user USERNAME to the project in order to get the permission to browse the project
      jiraSoapClient.addActorsToProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
          ACTOR_TYPE);

      // Get the issue to update
      final RemoteIssue[] remoteIssues = jiraSoapClient.getIssuesFromTextSearchWithProject(connector,
          projectList, ISSUE_DESCRIPTION, 10);

      final RemoteFieldValue remoteFieldValueDescription = new RemoteFieldValue();
      remoteFieldValueDescription.setId("description");
      final String[] remoteFieldValueDescriptionValues = { ISSUE_DESCRIPTION_UPDATE };
      remoteFieldValueDescription.setValues(remoteFieldValueDescriptionValues);
      final RemoteFieldValue[] actionParams = { remoteFieldValueDescription };

      // Find the description of the updated issue
      final RemoteIssue remoteIssue = jiraSoapClient.updateIssue(connector, remoteIssues[0].getKey(),
          actionParams);

      // Remove the user USERNAME from the project
      jiraSoapClient.removeActorsFromProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
          ACTOR_TYPE);

      // Assert that the description have been update
      assertThat(remoteIssue, notNullValue());
      assertThat(remoteIssue.getDescription(), CoreMatchers.is(ISSUE_DESCRIPTION_UPDATE));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getProjectRoleActors}
   * .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test21DeleteIssue() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      // Add the user USERNAME to the project in order to get the permission to browse the project
      final List<String> actorsList = new ArrayList<String>();
      actorsList.add(USERNAME);
      final String projectId = connector.getConnectBindingStub()
          .getProjectByKey(connector.getAuthenticationToken(), KEY).getId();
      final RemoteProject remoteProject = new RemoteProject(projectId, null, null, null, null, null, null,
          null, null, null);

      // Add the user USERNAME to the project administrator
      jiraSoapClient.addActorsToProjectRole(connector, actorsList, remoteProjectRoleAdmin, remoteProject,
          ACTOR_TYPE);

      final RemoteIssue remoteIssueToDelete = new RemoteIssue(null, null, USERNAME_ALT, null, null, null,
          null, ISSUE_DESCRIPTION_DELETE, null, null, null, null, null, KEY, USERNAME_ALT, null, null,
          ISSUE_SUMMARY, ISSUE_TYPE, null, null);

      // Create the issue
      final RemoteIssue createdRemoteIssue = connector.getConnectBindingStub().createIssue(
          connector.getAuthenticationToken(), remoteIssueToDelete);

      // Delete the issue
      connector.getConnectBindingStub().deleteIssue(connector.getAuthenticationToken(),
          createdRemoteIssue.getKey());

      // Create the array of projet key
      final String[] projectList = new String[] { KEY };

      // Find the description of the deleted issue
      final RemoteIssue[] remoteIssues = jiraSoapClient.getIssuesFromTextSearchWithProject(connector,
          projectList, ISSUE_DESCRIPTION_DELETE, 10);

      // Remove the user USERNAME to the project administrator
      jiraSoapClient.removeActorsFromProjectRole(connector, actorsList, remoteProjectRoleAdmin,
          remoteProject, ACTOR_TYPE);

      // Assert that no issue as been found
      final List<RemoteIssue> remoteIssuesList = Arrays.asList(remoteIssues);
      assertTrue(remoteIssuesList.isEmpty());
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#removeActorsFromProjectRole}
   * .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test22RemoveActorsFromProjectRole() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final List<String> actorsList = new ArrayList<String>();
      actorsList.add(USERNAME_ALT);
      final String projectId = connector.getConnectBindingStub()
          .getProjectByKey(connector.getAuthenticationToken(), KEY).getId();
      final RemoteProject remoteProject = new RemoteProject(projectId, null, null, null, null, null, null,
          null, null, null);

      jiraSoapClient.removeActorsFromProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
          ACTOR_TYPE);

      // Get a list of RemoteUser associated to the project remoteProjectRole.
      final RemoteUser[] users = connector.getConnectBindingStub()
          .getProjectRoleActors(connector.getAuthenticationToken(), remoteProjectRole, remoteProject)
          .getUsers();
      final List<RemoteUser> list = Arrays.asList(users);

      assertTrue(!list.contains(jiraSoapClient.getUser(connector, USERNAME_ALT)));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getProjectRole}.
   */
  @Test
  public void test23GetProjectRole() throws JiraSoapException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final RemoteProjectRole remoteProjectRole = jiraSoapClient.getProjectRole(connector, PROJECT_ROLE_ID);

      assertThat(remoteProjectRole, notNullValue());
      assertThat(remoteProjectRole.getName(), CoreMatchers.is(PROJECT_ROLE_NAME));
      assertThat(remoteProjectRole.getId(), CoreMatchers.is(PROJECT_ROLE_ID));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#deleteProject} .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test24DeleteProject() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      jiraSoapClient.deleteProject(connector, KEY);

      try
      {
        connector.getConnectBindingStub().getProjectByKey(connector.getAuthenticationToken(), KEY);
        fail("The project still exist.");
      }
      catch (final RemoteException e)
      {
        // Nothing to do
      }
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#deleteUser} .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws java.rmi.RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test25DeleteUser() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      jiraSoapClient.deleteUser(connector, USERNAME_ALT);

      final RemoteUser remoteUser = connector.getConnectBindingStub().getUser(
          connector.getAuthenticationToken(), USERNAME_ALT);

      assertThat(remoteUser, CoreMatchers.nullValue());

    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getNotificationSchemes}
   * .
   *
   * @throws JiraSoapException
   */
  @Test
  public void test26GetNotificationSchemes() throws JiraSoapException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      RemoteScheme[] remoteSchemes = null;

      boolean found = false;

      remoteSchemes = jiraSoapClient.getNotificationSchemes(connector);

      for (final RemoteScheme remoteScheme : remoteSchemes)
      {
        if (remoteScheme.getId().equals(NOTIFICATION_SCHEME_ID))
        {
          found = true;
          break;
        }
      }
      assertTrue(found);
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getNotificationSchemeById}
   * .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test27GetNotificationSchemeById() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final RemoteScheme remoteScheme = jiraSoapClient.getNotificationSchemeById(connector,
          NOTIFICATION_SCHEME_ID);

      assertThat(remoteScheme, CoreMatchers.is(getNotificationSchemeById(NOTIFICATION_SCHEME_ID)));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getSecuritySchemes}
   * .
   *
   * @throws JiraSoapException
   */
  @Test
  public void test28GetSecuritySchemes() throws JiraSoapException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      RemoteScheme[] remoteSchemes = null;

      boolean found = false;

      remoteSchemes = jiraSoapClient.getSecuritySchemes(connector);

      for (final RemoteScheme remoteScheme : remoteSchemes)
      {
        if (remoteScheme.getId().equals(SECURITY_SCHEME_ID))
        {
          found = true;
          break;
        }
      }
      assertTrue(found);
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getSecuritySchemeById}
   * .
   *
   * @throws JiraSoapException
   * @throws java.rmi.RemoteException
   * @throws RemoteException
   * @throws RemoteAuthenticationException
   * @throws RemotePermissionException
   */
  @Test
  public void test29GetSecuritySchemeById() throws JiraSoapException, RemotePermissionException,
      RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      final RemoteScheme remoteScheme = jiraSoapClient.getSecuritySchemeById(connector, SECURITY_SCHEME_ID);

      assertThat(remoteScheme, CoreMatchers.is(getSecuritySchemeById(SECURITY_SCHEME_ID)));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapClientImpl#getStatuses} .
   *
   * @throws JiraSoapException
   */
  @Test
  public void test28GetStatuses() throws JiraSoapException
  {
    if (jiraProfileActivated)
    {
      final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
      final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);

      RemoteStatus[] remoteStatus = null;

      remoteStatus = jiraSoapClient.getStatuses(connector);

      assertThat(remoteStatus, CoreMatchers.nullValue());
    }
  }

  // @Test
  // public void test40GetIssuesFromJqlSearch() throws JiraSoapException, RemotePermissionException,
  // RemoteAuthenticationException, RemoteException, java.rmi.RemoteException, JiraRestException,
  // URISyntaxException
  // {
  //
  // final JiraSoapClient jiraSoapClient = new JiraSoapClientImpl();
  // final JiraSoapConnector connector = jiraSoapClient.getConnector(ENDPOINT, USERNAME, PASSWORD);
  //
  // final JiraRestCustomClient jiraRestClient = new JiraRestCustomClientImpl();
  // final JiraRestConnector connectorRest = jiraRestClient.getConnector(BASE_URL_REST, ENDPOINT_REST,
  // USERNAME, PASSWORD);
  //
  // final List<String> actorsList = new ArrayList<String>();
  // actorsList.add(USERNAME);
  // final String projectId = connector.getConnectBindingStub()
  // .getProjectByKey(connector.getAuthenticationToken(), KEY).getId();
  // final RemoteProject remoteProject = new RemoteProject(projectId, null, null, null, null, null, null,
  // null, null, null);
  //
  // // Add the user USERNAME to the project in order to get the permission to browse the project
  // // jiraSoapClient
  // // .addActorsToProjectRole(connector, actorsList, remoteProjectRole, remoteProject, ACTOR_TYPE);
  //
  // // Construct the jql search query
  // final StringBuilder jqlSearch = new StringBuilder();
  // jqlSearch.append("project = ").append(KEY)
  // .append(" AND created < \"2014/08/30\" AND affectedVersion =\"1.1\" ");
  //
  // System.out.println(jqlSearch.toString());
  //
  // final RemoteIssue[] remoteIssues = jiraSoapClient.getIssuesFromJqlSearch(connector, jqlSearch.toString(),
  // 10);
  //
  // // Remove the user USERNAME from the project
  // jiraSoapClient.removeActorsFromProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
  // ACTOR_TYPE);
  //
  // for (RemoteIssue remoteIssue : remoteIssues)
  // {
  // Issue issue = jiraRestClient.getIssue(connectorRest, remoteIssue.getKey());
  //
  // Iterable<ChangelogGroup> changelogs = issue.getChangelog();
  //
  // System.out.println("############");
  // System.out.println(issue.getSummary());
  // System.out.println("############");
  //
  // if (Iterables.isEmpty(changelogs))
  // {
  // System.out.println("No change log");
  // }
  //
  // for (ChangelogGroup changelog : changelogs)
  // {
  // System.out.println("############");
  // System.out.println(changelog.toString());
  // System.out.println("############");
  // }
  //
  // }
  //
  // assertThat(remoteIssues[0], notNullValue());
  // }
}