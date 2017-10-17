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
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.bugtracker.constants.JiraConstant;
import org.novaforge.forge.plugins.bugtracker.constants.JiraRolePrivilege;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapClient;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapConnector;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapException;
import org.novaforge.forge.plugins.bugtracker.jira.datamapper.JiraResourceBuilder;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProject;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProjectRole;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteUser;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Jira implementation of {@link PluginProjectService}
 * 
 * @author Guillaume Lamirand
 */
public class JiraProjectServiceImpl extends AbstractPluginProjectService implements PluginProjectService
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
   * {@inheritDoc}
   */
  @Override
  protected String createToolProject(final InstanceConfiguration pInstance,
      final PluginProject pPluginProject, final List<PluginMembership> pPluginMembership)
      throws PluginServiceException
  {

    String projectToolId;
    try
    {
      // Obtain Jira connector
      final JiraSoapConnector connector = jiraSoapClient.getConnector(
          pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      // Add if necessary the user to jira instance
      final Map<String, String> memberships = addToolUser(connector, pPluginMembership);

      final List<InstanceConfiguration> usedKey = instanceConfigurationDAO.findByForgeId(pInstance
          .getForgeId());
      for (final InstanceConfiguration instanceConfiguration : usedKey)
      {
        instanceConfiguration.getToolProjectId();
      }

      // Generate the project Key
      final String key = generateProjectKey(pPluginProject.getProjectId(), pInstance.getConfigurationId(),
          pInstance.getForgeId());

      // Build the RemoteProject object
      RemoteProject remoteProject = jiraResourceBuilder.buildRemoteProject(pPluginProject,
          pInstance.getConfigurationId(), key, connector);

      // Create the project into Jira
      remoteProject = jiraSoapClient.createProject(connector, remoteProject);

      projectToolId = remoteProject.getKey();

      // Create the membership between user and created project
      addUsersToProject(connector, pInstance.getInstanceId(), projectToolId, memberships);

      // Remove the developers group from the created project
      JiraRolePrivilege jiraRolePrivilege = JiraRolePrivilege.DEVELOPER;
      RemoteProjectRole remoteProjectRole = jiraSoapClient.getProjectRole(connector,
          jiraRolePrivilege.getId());

      List<String> actorsList = new ArrayList<String>();
      actorsList.add(JiraConstant.DEVELOPERS_GROUP);

      jiraSoapClient.removeActorsFromProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
          JiraConstant.ACTOR_TYPE_GROUP);

      // Remove the user group from the created project
      jiraRolePrivilege = JiraRolePrivilege.USER;
      remoteProjectRole = jiraSoapClient.getProjectRole(connector, jiraRolePrivilege.getId());

      actorsList = new ArrayList<String>();
      actorsList.add(JiraConstant.USERS_GROUP);

      jiraSoapClient.removeActorsFromProjectRole(connector, actorsList, remoteProjectRole, remoteProject,
          JiraConstant.ACTOR_TYPE_GROUP);
    }
    catch (final JiraSoapException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to create project with InstanceConfiguration=%s and PluginProject=%s",
          pInstance.toString(), pPluginProject.toString()), e);
    }

    return projectToolId;
  }

  private Map<String, String> addToolUser(final JiraSoapConnector pConnector, final List<PluginMembership> pUsers)
      throws PluginServiceException, JiraSoapException
  {
    final Map<String, String> users = new HashMap<String, String>();
    for (final PluginMembership membership : pUsers)
    {
      final PluginUser user = membership.getPluginUser();
      final String role = membership.getRole();

      // Test is user already exist in the tool instance
      final RemoteUser existRemoteUser = jiraSoapClient.getUser(pConnector, user.getLogin());
      if (existRemoteUser == null)
      {
        // Create the user
        RemoteUser remoteUser = jiraResourceBuilder.buildRemoteUser(user);
        remoteUser = jiraSoapClient.createUser(pConnector, remoteUser, user.getPassword());
      }

      users.put(user.getLogin(), role);
    }

    return users;
  }

  /**
   * Generate a Jira compatible Key :instanceConfigurationDAO
   * * The Key should not already exist into Jira
   * * The key should not have more than 10 characters, only letter
   * * The Key should be explicit as possible
   * * The key should be in upper case
   * Pattern : 5 first characters of projectId + 5 first characters of configurationId
   */
  private String generateProjectKey(final String pProjectId, final String pConfigurationId,
      final String pForgeId)
  {

    // Get the list of all the instance
    final List<InstanceConfiguration> instanceConfigurations = instanceConfigurationDAO
        .findByForgeId(pForgeId);
    final List<String> existingKeys = new ArrayList<String>();
    for (final InstanceConfiguration instanceConfiguration : instanceConfigurations)
    {
      existingKeys.add(instanceConfiguration.getToolProjectId());
    }

    // Initialize both projectIdIndice and configurationIdIndice to five to create a ten character key.
    int projectIdIndice = pProjectId.replaceAll("[^a-zA-Z]", "").length();

    if (projectIdIndice > JiraConstant.PROJECT_ID_KEY)
    {
      projectIdIndice = JiraConstant.PROJECT_ID_KEY;
    }

    int configurationIdIndice = pConfigurationId.replaceAll("[^a-zA-Z]", "").length();

    if (configurationIdIndice > JiraConstant.CONFIGURATION_ID_KEY)
    {
      configurationIdIndice = JiraConstant.CONFIGURATION_ID_KEY;
    }

    // Generate the first key
    String key = generateKeyString(pProjectId, pConfigurationId, projectIdIndice, configurationIdIndice);

    while (existingKeys.contains(key))
    {

      // Decrement projectIdIndice or configurationIdIndice.
      if (projectIdIndice == configurationIdIndice)
      {
        configurationIdIndice--;
      }
      else
      {
        projectIdIndice--;
      }

      // Generate the compatible Jira Key.
      key = generateKeyString(pProjectId, pConfigurationId, projectIdIndice, configurationIdIndice);

      // To avoid infinite loop, this case should never happen.
      if ((projectIdIndice == 0) && (configurationIdIndice == 0))
      {
        key = null;
      }

    }
    // Return the compatible Jira Key
    return key;

  }

  private void addUsersToProject(final JiraSoapConnector pConnector, final String pInstanceId,
      final String pProjectKey, final Map<String, String> pUsers) throws JiraSoapException,
      PluginServiceException
  {
    final Set<Entry<String, String>> usersRole = pUsers.entrySet();
    for (final Entry<String, String> entry : usersRole)
    {
      final String userName = entry.getKey();
      final String forgeRole = entry.getValue();

      if (pluginRoleMappingService.existToolRole(pInstanceId, forgeRole))
      {
        final String toolRole = pluginRoleMappingService.getToolRole(pInstanceId, forgeRole);
        final String roleId = pluginRoleMappingService.getToolRoleId(toolRole);

        // Create a list with the Jira username to add
        final List<String> remoteUserList = new ArrayList<String>();
        remoteUserList.add(userName);

        // Get the RemoteProjectRole associated with the forgeRole
        final RemoteProjectRole remoteProjectRole = jiraSoapClient.getProjectRole(pConnector,
            Long.valueOf(roleId).longValue());

        // Get the RemoteProject associated with the project key
        final RemoteProject remoteProject = jiraSoapClient.getProjectByKey(pConnector, pProjectKey);

        jiraSoapClient.addActorsToProjectRole(pConnector, remoteUserList, remoteProjectRole, remoteProject,
            JiraConstant.ACTOR_TYPE_USER);
      }
    }
  }

  /**
   * Strip numbers, trunk and concatenates configuration Id and project id to generate a compatible Jira Key
   */
  protected String generateKeyString(final String pProjectId, final String pConfigurationId, final int pProjectIdIndice,
                                     final int pConfigurationIdIndice)
  {
    return (pProjectId.replaceAll("[^a-zA-Z]", "").substring(0, pProjectIdIndice) + pConfigurationId
                                                                                        .replaceAll("[^a-zA-Z]", "")
                                                                                        .substring(0,
                                                                                                   pConfigurationIdIndice))
               .toUpperCase();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean deleteToolProject(final InstanceConfiguration pInstance) throws PluginServiceException
  {

    try
    {
      // Obtain Jira connector
      final JiraSoapConnector connector = jiraSoapClient.getConnector(
          pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      jiraSoapClient.deleteProject(connector, pInstance.getToolProjectId());

    }
    catch (final JiraSoapException e)
    {
      throw new PluginServiceException(String.format("Unable to delete tool project user with toolProjectId=%s",
                                                     pInstance.getToolProjectId()), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void archiveProject(final String pInstanceId) throws PluginServiceException
  {
    // TODO Not implemented yet
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolProject(final InstanceConfiguration pInstance, final PluginProject pProject)
      throws PluginServiceException
  {
    try
    {

      // Obtain Jira connector
      final JiraSoapConnector connector = jiraSoapClient.getConnector(
          pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      RemoteProject remoteProject = jiraResourceBuilder.buildRemoteProject(pProject, pInstance.getConfigurationId(),
                                                                           pInstance.getToolProjectId(), connector);
      remoteProject = jiraSoapClient.updateProject(connector, remoteProject);

    }
    catch (final JiraSoapException e)
    {
      throw new PluginServiceException(String.format("Unable to update tool project user with InstanceConfiguration=%s and PluginProject=%s",
                                                     pInstance.toString(), pProject.toString()), e);
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
