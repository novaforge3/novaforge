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

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.bugtracker.constants.JiraConstant;
import org.novaforge.forge.plugins.bugtracker.constants.JiraRolePrivilege;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapClient;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapConnector;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapException;
import org.novaforge.forge.plugins.bugtracker.jira.datamapper.JiraResourceBuilder;
import org.novaforge.forge.plugins.bugtracker.jira.services.JiraMembershipService;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteGroup;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProject;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProjectRole;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProjectRoleActors;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteUser;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;

import java.util.Arrays;
import java.util.List;

/**
 * @author Gauthier CART
 */
public class JiraUserServiceImpl extends AbstractPluginUserService implements PluginUserService
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
   * Reference to service implementation of {@link PluginMembershipService}
   */
  private JiraMembershipService      jiraMembershipService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void createAdministratorUser(final ToolInstance pInstance, final PluginUser pUser)
      throws PluginServiceException
  {
    try
    {

      // Get jjra connector
      final JiraSoapConnector connector = jiraSoapClient.getConnector(
          pluginConfigurationService.getClientURL(pInstance.getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      // Check if the user already exist
      final RemoteUser existRemoteUser = jiraSoapClient.getUser(connector, pUser.getLogin());

      // Get the jira administrator group
      final RemoteGroup remoteGroup = jiraSoapClient.getGroup(connector, JiraConstant.ADMIN_GROUP);

      // If the admin do no exist create it and add him to the administrator group
      if (existRemoteUser == null)
      {
        RemoteUser remoteUser = jiraResourceBuilder.buildRemoteUser(pUser);
        remoteUser = jiraSoapClient.createUser(connector, remoteUser, pUser.getPassword());
        jiraSoapClient.addUserToGroup(connector, remoteGroup, remoteUser);
      }
      // Check that the user is already into administrator group, add him if not
      else
      {
        // Get the group jira-administrators
        final RemoteGroup jiraAdministratorsGroup = jiraSoapClient.getGroup(connector,
            JiraConstant.ADMIN_GROUP);

        // Construct a list of the remote user of the jira-administrators group
        final List<RemoteUser> usersInJiraAdministratorsGroup = Arrays.asList(jiraAdministratorsGroup
            .getUsers());

        // If the administrator is not inside, add it
        if (!usersInJiraAdministratorsGroup.contains(existRemoteUser))
        {
          jiraSoapClient.addUserToGroup(connector, remoteGroup, existRemoteUser);
        }

      }
    }
    catch (final JiraSoapException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to create administrator user with instance=%s and user=%s", pInstance.toString(),
          pUser.toString()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolUser(final InstanceConfiguration pInstance, final String pUserName,
      final PluginUser pPluginUser) throws PluginServiceException
  {
    try
    {
      // Obtain jira connector
      final JiraSoapConnector connector = jiraSoapClient.getConnector(
          pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      // Build the new user
      final RemoteUser remoteUser = jiraResourceBuilder.buildRemoteUser(pPluginUser);

      // Impossible to change the user name in jira, so set the old one.
      remoteUser.setName(pUserName);

      jiraSoapClient.updateUser(connector, remoteUser);
    }
    catch (final JiraSoapException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to update user with instance=%s, username=%s and User=%s", pInstance.toString(), pUserName,
          pPluginUser.toString()), e);

    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean removeToolUser(final InstanceConfiguration pInstance, final PluginUser pUser)
      throws PluginServiceException
  {

    try
    {
      // Get jira connector
      final JiraSoapConnector connector = jiraSoapClient.getConnector(
          pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      // Get an array of roles available in jira
      final JiraRolePrivilege[] jiraRolePrivileges = org.novaforge.forge.plugins.bugtracker.constants.JiraRolePrivilege
          .values();

      // Get all the instances configuration of jira to get all the jira project keys
      final List<InstanceConfiguration> instanceConfigurationList = instanceConfigurationDAO
          .findByForgeId(pInstance.getForgeId());

      // For each project found on the forge, load it and take a look if the user have a role inside.
      for (InstanceConfiguration instanceConfiguration : instanceConfigurationList)
      {

        // Get the current remote project
        final RemoteProject remoteProject = jiraSoapClient.getProjectByKey(connector,
            instanceConfiguration.getToolProjectId());

        // Browse the roles array and get all the jira roles
        for (final JiraRolePrivilege jiraRolePrivilege : jiraRolePrivileges)
        {

          // Load the current role
          final RemoteProjectRole remoteProjectRole = jiraSoapClient.getProjectRole(connector,
              jiraRolePrivilege.getId());

          // Load all the actors linked to this role into a list
          RemoteProjectRoleActors remoteProjectRoleActors = new RemoteProjectRoleActors();
          remoteProjectRoleActors = jiraSoapClient.getProjectRoleActors(connector, remoteProjectRole,
              remoteProject);
          final RemoteUser[] users = remoteProjectRoleActors.getUsers();
          final List<RemoteUser> list = Arrays.asList(users);

          // Test if the user to remove is into this list, if he is remove the membership
          if (list.contains(jiraSoapClient.getUser(connector, pUser.getLogin())))
          {
            // Remove the membership of the user to delete of the project
            jiraMembershipService.removeToolUserMemberships(instanceConfiguration, pUser);
            break;
          }
        }
      }

      jiraSoapClient.deleteUser(connector, pUser.getLogin());
    }
    catch (final JiraSoapException e)
    {
      throw new PluginServiceException(String.format("Unable to delete user with instance=%s, and User=%s",
          pInstance.toString(), pUser.getLogin()), e);

    }
    return true;
  }

  /**
   * Used by container to inject {@link PluginConfigurationService}
   * 
   * @param pPluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }

  /**
   * Used by container to inject {@link JiraSoapClient}
   * 
   * @param pJiraSoapClient
   *          the jiraSoapClient to set
   */
  public void setJiraSoapClient(final JiraSoapClient pJiraSoapClient)
  {
    jiraSoapClient = pJiraSoapClient;
  }

  /**
   * Used by container to inject {@link JiraResourceBuilder}
   * 
   * @param pJiraResourceBuilder
   *          the jiraResourceBuilder to set
   */
  public void setJiraResourceBuilder(final JiraResourceBuilder pJiraResourceBuilder)
  {
    jiraResourceBuilder = pJiraResourceBuilder;
  }

  /**
   * Used by container to inject {@link JiraMembershipService}
   * 
   * @param pJiraMembershipService
   *          the jiraMembershipService to set
   */
  public void setJiraMembershipService(final JiraMembershipService pJiraMembershipService)
  {
    jiraMembershipService = pJiraMembershipService;
  }
}
