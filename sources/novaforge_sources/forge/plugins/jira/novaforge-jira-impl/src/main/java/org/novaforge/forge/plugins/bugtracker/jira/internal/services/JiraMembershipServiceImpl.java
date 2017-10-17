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
package org.novaforge.forge.plugins.bugtracker.jira.internal.services;

import com.atlassian.jira.rest.client.api.domain.BasicComponent;
import com.atlassian.jira.rest.client.api.domain.Component;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.input.ComponentInput;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.bugtracker.constants.JiraConstant;
import org.novaforge.forge.plugins.bugtracker.constants.JiraRolePrivilege;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestClient;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestConnector;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestException;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapClient;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapConnector;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapException;
import org.novaforge.forge.plugins.bugtracker.jira.datamapper.JiraResourceBuilder;
import org.novaforge.forge.plugins.bugtracker.jira.services.JiraConfigurationService;
import org.novaforge.forge.plugins.bugtracker.jira.services.JiraMembershipService;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteFieldValue;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteGroup;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteIssue;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProject;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProjectRole;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProjectRoleActors;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteUser;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Jira implementation of {@link PluginMembershipService}
 * 
 * @author Guillaume Lamirand
 */
public class JiraMembershipServiceImpl extends AbstractPluginMembershipService implements
    PluginMembershipService, JiraMembershipService
{

  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;

  /**
   * Reference to service implementation of {@link JiraResourceBuilder}
   */
  private JiraResourceBuilder        jiraResourceBuilder;

  /**
   * Reference to service implementation of {@link JiraSoapClient}
   */
  private JiraSoapClient             jiraSoapClient;

  /**
   * Reference to service implementation of {@link JiraRestClient}
   */
  private JiraRestClient             jiraRestClient;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser)
      throws PluginServiceException
  {

    return removeToolUserMemberships(pInstance, pUser, null);
  }

  private boolean removeUserFromProject(final InstanceConfiguration pInstance, final PluginUser pUser)
      throws PluginServiceException, JiraSoapException
  {
    final JiraSoapConnector connector = jiraSoapClient.getConnector(pluginConfigurationService.getClientURL(pInstance
                                                                                                                .getToolInstance()
                                                                                                                .getBaseURL()),
                                                                    pluginConfigurationService.getClientAdmin(),
                                                                    pluginConfigurationService.getClientPwd());

    // Get the remote user to update
    final RemoteUser remoteUser = jiraSoapClient.getUser(connector, pUser.getLogin());

    // Get the remote project to update
    final RemoteProject remoteProject = jiraSoapClient.getProjectByKey(connector, pInstance.getToolProjectId());

    // Get an array of jiraRolePrivileges
    final JiraRolePrivilege[] jiraRolePrivileges = org.novaforge.forge.plugins.bugtracker.constants.JiraRolePrivilege
                                                       .values();

    // Browse the jiraRolePrivileges array and get all the Jira RemoteProjectRole
    for (final JiraRolePrivilege jiraRolePrivilege : jiraRolePrivileges)
    {

      // Load the current RemoteProjectRole
      final RemoteProjectRole remoteProjectRole = jiraSoapClient.getProjectRole(connector, jiraRolePrivilege.getId());

      // Load all the actors linked to this RemoteProjectRole into a list
      RemoteProjectRoleActors remoteProjectRoleActors = new RemoteProjectRoleActors();
      remoteProjectRoleActors = jiraSoapClient.getProjectRoleActors(connector, remoteProjectRole, remoteProject);
      final RemoteUser[] users = remoteProjectRoleActors.getUsers();
      final List<RemoteUser> list = Arrays.asList(users);

      // Test if the user to update is into this list, if he is, remove it
      if (list.contains(jiraSoapClient.getUser(connector, pUser.getLogin())))
      {
        final List<String> remoteUserList = new ArrayList<String>();
        remoteUserList.add(remoteUser.getName());

        jiraSoapClient.removeActorsFromProjectRole(connector, remoteUserList, remoteProjectRole, remoteProject,
                                                   JiraConstant.ACTOR_TYPE_USER);
        break;
      }
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean addToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
      final String pToolRole) throws PluginServiceException
  {

    try
    {
      final long roleId = new Long(pluginRoleMappingService.getToolRoleId(pToolRole));

      final JiraSoapConnector connector = jiraSoapClient.getConnector(
          pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      final RemoteUser existRemoteUser = jiraSoapClient.getUser(connector, pUser.getLogin());

      if (existRemoteUser == null)
      {
        RemoteUser remoteUser = jiraResourceBuilder.buildRemoteUser(pUser);
        remoteUser = jiraSoapClient.createUser(connector, remoteUser, pUser.getPassword());
      }

      final RemoteProjectRole remoteProjectRole = jiraSoapClient.getProjectRole(connector, roleId);
      final RemoteProject remoteProject = jiraSoapClient.getProjectByKey(connector,
          pInstance.getToolProjectId());
      final RemoteUser remoteUser = jiraSoapClient.getUser(connector, pUser.getLogin());

      final List<String> remoteUserList = new ArrayList<String>();
      remoteUserList.add(remoteUser.getName());

      jiraSoapClient.addActorsToProjectRole(connector, remoteUserList, remoteProjectRole, remoteProject,
          JiraConstant.ACTOR_TYPE_USER);

    }
    catch (final JiraSoapException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to add user membership with instance=%s and User=%s", pInstance.toString(),
          pUser.toString()), e);
    }
    return true;

  }

  private boolean updateProjectLeader(final InstanceConfiguration pInstance, final PluginUser pUser)
      throws PluginServiceException, JiraSoapException
  {
    // Get jira connector
    final JiraSoapConnector connector = jiraSoapClient.getConnector(pluginConfigurationService.getClientURL(pInstance
                                                                                                                .getToolInstance()
                                                                                                                .getBaseURL()),
                                                                    pluginConfigurationService.getClientAdmin(),
                                                                    pluginConfigurationService.getClientPwd());

    // Get the remote project to update
    RemoteProject remoteProject = jiraSoapClient.getProjectByKey(connector, pInstance.getToolProjectId());

    // Test if the user to update is into this list, if he is, remove it
    if (pUser.getLogin().equals(remoteProject.getLead()))
    {
      // Get the Administrator role id
      final JiraRolePrivilege jiraRolePrivilege = JiraRolePrivilege.ADMINISTRATOR;

      // Get the remote project
      final RemoteProjectRole remoteProjectRole = jiraSoapClient.getProjectRole(connector, jiraRolePrivilege.getId());

      // Load all the actors linked to the administrator role into a list
      RemoteProjectRoleActors remoteProjectRoleActors = new RemoteProjectRoleActors();
      remoteProjectRoleActors = jiraSoapClient.getProjectRoleActors(connector, remoteProjectRole, remoteProject);
      final RemoteUser[] users = remoteProjectRoleActors.getUsers();
      final List<RemoteUser> remoteUserList = Arrays.asList(users);

      // Initialize a boolean to know if a new project lead has been found
      boolean projectLeadFound = false;

      final RemoteGroup jiraAdministratorsGroup = jiraSoapClient.getGroup(connector, JiraConstant.ADMIN_GROUP);
      final RemoteUser[] jiraAdministratorsUsers = jiraAdministratorsGroup.getUsers();
      final List<RemoteUser> jiraAdministratorsUserList = Arrays.asList(jiraAdministratorsUsers);

      // Browse all the administrator, one of them may be the future new project lead
      for (final RemoteUser remoteUser : remoteUserList)
      {

        // If the current browsed user is not the current project lead or in the jira-administrators group,
        // grant him project lead, congratulation !
        if ((!remoteUser.getName().equals(pUser.getLogin())) && jiraAdministratorsUserList.contains(remoteUser))
        {
          // Update the project lead
          remoteProject.setLead(remoteUser.getName());
          remoteProject = jiraSoapClient.updateProject(connector, remoteProject);
          // Change the boolean to true cause we found a new project lead
          projectLeadFound = true;
          break;
        }
      }

      // If any project lead is found, choose the first super administrator found as default project lead
      if (!projectLeadFound)
      {
        remoteProject.setLead(jiraAdministratorsUsers[0].getName());
        remoteProject = jiraSoapClient.updateProject(connector, remoteProject);
      }
    }
    return true;
  }

  /**
   * Text
   *
   * @param pInstance
   *          instance of jira application
   * @param pUser
   *          forge user
   * @return boolean true by default
   * @throws
   */
  private boolean updateComponentLeader(final InstanceConfiguration pInstance, final PluginUser pUser)
      throws PluginServiceException, JiraRestException, URISyntaxException
  {

    // Get jira connector
    final JiraRestConnector connector = jiraRestClient
                                            .getConnector(((JiraConfigurationService) pluginConfigurationService)
                                                              .getRestClientURL(pInstance.getToolInstance()
                                                                                         .getBaseURL()),
                                                          pluginConfigurationService.getClientAdmin(),
                                                          pluginConfigurationService.getClientPwd());

    // Get the project to get the project lead and the project components.
    final Project project = jiraRestClient.getProjectByKey(connector, pInstance.getToolProjectId());

    // Browse every project component
    for (final BasicComponent basicComponent : project.getComponents())
    {

      // Get the full component
      final Component component = jiraRestClient.getComponent(connector, basicComponent.getId().toString());

      // Test if the user is leader of the current component, if yes, update the component and set the project
      // leader as component leader.
      if (component.getLead().getName().equals(pUser.getLogin()))
      {
        final ComponentInput componentInput = new ComponentInput(component.getName(), component.getDescription(),
                                                                 project.getLead().getName(),
                                                                 component.getAssigneeInfo().getAssigneeType());
        jiraRestClient.updateComponent(connector, basicComponent.getId().toString(), componentInput);
      }
    }

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
                                              final String pToolRole) throws PluginServiceException
  {

    try
    {
      // Remove the user from the project
      removeUserFromProject(pInstance, pUser);

      // Add the new membership
      addToolUserMemberships(pInstance, pUser, pToolRole);
    }
    catch (final JiraSoapException e)
    {
      throw new PluginServiceException(String.format("Unable to update user membership with instance=%s and User=%s",
                                                     pInstance.toString(), pUser.toString()), e);
    }

    return true;
  }

  private boolean assignUserAssignedIssueToProjectLead(final InstanceConfiguration pInstance,
      final PluginUser pUser) throws PluginServiceException, JiraSoapException
  {
    // Get the jira connector
    final JiraSoapConnector connector = jiraSoapClient.getConnector(
        pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
        pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

    // Get the remote project to update
    final RemoteProject remoteProject = jiraSoapClient.getProjectByKey(connector,
        pInstance.getToolProjectId());

    // Build the jql query to get the issues assigned to the user
    final StringBuilder jqlSearch = new StringBuilder();
    jqlSearch.append("project = ").append(remoteProject.getKey()).append(" AND assignee in (")
        .append(pUser.getLogin()).append(")");

    // Execute the jql search query
    RemoteIssue[] remoteIssues = jiraSoapClient.getIssuesFromJqlSearch(connector, jqlSearch.toString(), 100);

    // Change the array of result into a list.
    List<RemoteIssue> remoteIssuesList = Arrays.asList(remoteIssues);

    // Initialize the objects needed to update a issue field value
    final RemoteFieldValue remoteFieldValueDescription = new RemoteFieldValue();
    remoteFieldValueDescription.setId("assignee");
    final String[] remoteFieldValueDescriptionValues = new String[1];
    final RemoteFieldValue[] actionParams = new RemoteFieldValue[1];

    // Until we have issue linked to the user, change their assignee
    while (!remoteIssuesList.isEmpty())
    {
      // Update each issue and change it assignee
      for (final RemoteIssue remoteIssue : remoteIssuesList)
      {
        // Update the issue
        remoteFieldValueDescriptionValues[0] = remoteProject.getLead();
        remoteFieldValueDescription.setValues(remoteFieldValueDescriptionValues);
        actionParams[0] = remoteFieldValueDescription;
        jiraSoapClient.updateIssue(connector, remoteIssue.getKey(), actionParams);
      }

      // Execute the jql search query
      remoteIssues = jiraSoapClient.getIssuesFromJqlSearch(connector, jqlSearch.toString(), 100);

      // Change the array of result into a list.
      remoteIssuesList = Arrays.asList(remoteIssues);
    }

    return true;
  }

  private boolean assignUserReportedIssueToProjectLead(final InstanceConfiguration pInstance,
      final PluginUser pUser) throws PluginServiceException, JiraSoapException
  {

    // Get jira connector
    final JiraSoapConnector connector = jiraSoapClient.getConnector(
        pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
        pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

    // Get the remote project to update
    final RemoteProject remoteProject = jiraSoapClient.getProjectByKey(connector,
        pInstance.getToolProjectId());

    // Build the jql query to get the issues assigned to the user
    final StringBuilder jqlSearch = new StringBuilder();
    jqlSearch.append("project = ").append(remoteProject.getKey()).append(" AND reporter in (")
        .append(pUser.getLogin()).append(")");

    // Execute the jql search query
    RemoteIssue[] remoteIssues = jiraSoapClient.getIssuesFromJqlSearch(connector, jqlSearch.toString(), 100);

    // Change the array of result into a list.
    List<RemoteIssue> remoteIssuesList = Arrays.asList(remoteIssues);

    // Initialize the objects needed to update a issue field value
    final RemoteFieldValue remoteFieldValueDescription = new RemoteFieldValue();
    remoteFieldValueDescription.setId("reporter");
    final String[] remoteFieldValueDescriptionValues = new String[1];
    final RemoteFieldValue[] actionParams = new RemoteFieldValue[1];

    // Until we have issue linked to the user, change their reporter
    while (!remoteIssuesList.isEmpty())
    {
      // Update each issue and change it reporter
      for (final RemoteIssue remoteIssue : remoteIssuesList)
      {
        // Update the issue
        remoteFieldValueDescriptionValues[0] = remoteProject.getLead();
        remoteFieldValueDescription.setValues(remoteFieldValueDescriptionValues);
        actionParams[0] = remoteFieldValueDescription;
        jiraSoapClient.updateIssue(connector, remoteIssue.getKey(), actionParams);
      }

      // Execute the jql search query tho check if there is more issue to update
      remoteIssues = jiraSoapClient.getIssuesFromJqlSearch(connector, jqlSearch.toString(), 100);

      // Change the array of result into a list.
      remoteIssuesList = Arrays.asList(remoteIssues);
    }

    return true;
  }

  /**
   * Use by container to inject {@link PluginConfigurationService}
   *
   * @param pPluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean removeToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
                                              final String pToolRole) throws PluginServiceException
  {
    try
    {

      // Remove the user from the project
      removeUserFromProject(pInstance, pUser);

      // Test if the user is project leader, if he is, update it
      updateProjectLeader(pInstance, pUser);

      // Test if the user is component leader, if he is, update it
      updateComponentLeader(pInstance, pUser);

      // Assign user assigned issue to project lead
      assignUserAssignedIssueToProjectLead(pInstance, pUser);

      // Assign user reported issue to project lead
      assignUserReportedIssueToProjectLead(pInstance, pUser);
    }
    catch (final Exception e)
    {
      throw new PluginServiceException(String.format("Unable to delete user membership with instance=%s and User=%s",
                                                     pInstance.toString(), pUser.toString()), e);
    }
    return true;
  }

  /**
   * Use by container to inject {@link JiraSoapClient}
   *
   * @param pJiraSoapClient
   *          the jiraSoapClient to set
   */
  public void setJiraSoapClient(final JiraSoapClient pJiraSoapClient)
  {
    jiraSoapClient = pJiraSoapClient;
  }

  /**
   * Use by container to inject {@link JiraRestClient}
   *
   * @param pJiraRestClient
   *          the jiraRestCustomClient to set
   */
  public void setJiraRestClient(final JiraRestClient pJiraRestClient)
  {
    jiraRestClient = pJiraRestClient;
  }

  /**
   * Use by container to inject {@link JiraResourceBuilder}
   *
   * @param pJiraResourceBuilder
   *          the jiraResourceBuilder to set
   */
  public void setJiraResourceBuilder(final JiraResourceBuilder pJiraResourceBuilder)
  {
    jiraResourceBuilder = pJiraResourceBuilder;
  }

}
