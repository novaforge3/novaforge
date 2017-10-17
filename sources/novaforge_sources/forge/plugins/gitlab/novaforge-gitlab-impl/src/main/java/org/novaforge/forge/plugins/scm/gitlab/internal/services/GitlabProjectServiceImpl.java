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
package org.novaforge.forge.plugins.scm.gitlab.internal.services;

import org.gitlab.api.models.GitlabAccessLevel;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabNamespace;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;
import org.novaforge.forge.plugins.scm.constants.GitlabConstant;
import org.novaforge.forge.plugins.scm.constants.GitlabRole;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestClient;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestConnector;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestException;
import org.novaforge.forge.plugins.scm.gitlab.datamapper.GitlabResourceBuilder;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Cart Gauthier
 */
public class GitlabProjectServiceImpl extends AbstractPluginProjectService implements PluginProjectService
{

  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;

  /**
   * Reference to service implementation of {@link GitlabRestClient}
   */
  private GitlabRestClient           gitlabRestClient;

  /**
   * Reference to service implementation of {@link GitlabResourceBuilder}
   */
  private GitlabResourceBuilder      gitlabResourceBuilder;

  /**
   * {@inheritDoc}
   */
  @Override
  protected String createToolProject(final InstanceConfiguration pInstanceConfiguration,
      final PluginProject pPluginProject, final List<PluginMembership> pPluginMembership)
      throws PluginServiceException
  {
    GitlabProject gitlabProject = new GitlabProject();

    try
    {
      // Obtain Gitlab connector
      final GitlabRestConnector connector;
      connector = gitlabRestClient.getConnector(
          pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientPwd());

      // Add if necessary the user to gitlab instance
      final Map<String, String> memberships = addToolUser(connector, pPluginMembership);

      // Get project author
      final GitlabUser gitlabAuthor = getProjectAuthor(connector, pPluginProject.getAuthor());

      // Add the group to gitlab instance
      final GitlabGroup gitlabGroup = addToolGroup(connector, pInstanceConfiguration.getForgeProjectId(),
          pInstanceConfiguration.getConfigurationId());

      final GitlabNamespace gitlabNamespace = new GitlabNamespace();
      gitlabNamespace.setId(gitlabGroup.getId());

      // Build the RemoteProject object
      gitlabProject = gitlabResourceBuilder.buildGitlabProject(pPluginProject,
          pInstanceConfiguration.getConfigurationId(), gitlabAuthor, gitlabNamespace);

      // Create the membership between user and the group
      addUsersToGroup(connector, pInstanceConfiguration.getInstanceId(), gitlabGroup, memberships);

      // Create the project
      gitlabProject = gitlabRestClient.createProject(connector, gitlabProject);
    }
    catch (final GitlabRestException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to create project with InstanceConfiguration=%s and PluginProject=%s",
          pInstanceConfiguration.toString(), pPluginProject.toString()), e);
    }

    return String.valueOf(gitlabProject.getId());
  }

  /**
   * @param pConnector
   * @param pMemberships
   * @return
   * @throws PluginServiceException
   * @throws GitlabRestException
   */
  private Map<String, String> addToolUser(final GitlabRestConnector pConnector,
      final List<PluginMembership> pMemberships) throws PluginServiceException, GitlabRestException
  {
    final Map<String, String> users = new HashMap<String, String>();
    for (final PluginMembership membership : pMemberships)
    {

      // Get the User
      final PluginUser user = membership.getPluginUser();
      final String role = membership.getRole();

      // If the User do not exist create it
      try
      {
        gitlabRestClient.getUserViaSudo(pConnector, user.getLogin());
      }
      catch (final GitlabRestException e)
      {
        // If the User does not exist, FileNotFoundException will be throw by the REST API
        if ((e.getCause() instanceof FileNotFoundException))
        {
          // Build Gitlab User
          final GitlabUser gitlabUser = gitlabResourceBuilder.buildGitlabUser(user);

          // Create the user
          gitlabRestClient.createUser(pConnector, gitlabUser, user.getPassword(),
              GitlabConstant.GITLAB_PERSONAL_PROJECTS_LIMIT);
        }
      }

      users.put(user.getLogin(), role);
    }

    return users;
  }

  /**
   * @param pConnector
   * @param pAuthor
   * @return
   * @throws GitlabRestException
   */
  private GitlabUser getProjectAuthor(final GitlabRestConnector pConnector, final String pAuthor)
      throws GitlabRestException
  {
    GitlabUser gitlabUser = new GitlabUser();

    gitlabUser = gitlabRestClient.getUserViaSudo(pConnector, pAuthor);

    return gitlabUser;
  }

  /**
   * @param pConnector
   * @param pInstanceName
   * @return
   * @throws PluginServiceException
   * @throws GitlabRestException
   */
  private GitlabGroup addToolGroup(final GitlabRestConnector pConnector, final String pForgeProjectId,
      final String pInstanceId) throws PluginServiceException, GitlabRestException
  {

    // Initialize Gitlab Group to return
    GitlabGroup gitlabGroup = gitlabResourceBuilder.buildGitlabGroup(pForgeProjectId, pInstanceId);

    gitlabGroup = gitlabRestClient.createGroup(pConnector, gitlabGroup);

    return gitlabGroup;
  }

  private void addUsersToGroup(final GitlabRestConnector pConnector, final String pInstanceId,
      final GitlabGroup pGitlabGroup, final Map<String, String> pUsers) throws PluginServiceException,
      GitlabRestException
  {
    final Set<Entry<String, String>> usersRole = pUsers.entrySet();

    for (final Entry<String, String> entry : usersRole)
    {
      final String userName = entry.getKey();
      final String forgeRole = entry.getValue();

      if (pluginRoleMappingService.existToolRole(pInstanceId, forgeRole))
      {
        // Get the label of the role in Gitlab
        final String toolRole = pluginRoleMappingService.getToolRole(pInstanceId, forgeRole);

        final GitlabUser gitlabUser = gitlabRestClient.getUserViaSudo(pConnector, userName);

        final GitlabAccessLevel gitlabAccessLevel = GitlabAccessLevel.fromAccessValue(GitlabRole.fromLabel(
            toolRole).getId());

        // Get the RemoteProject associated with the project key
        gitlabRestClient.addGroupMember(pConnector, pGitlabGroup.getId(), gitlabUser.getId(),
            gitlabAccessLevel);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean deleteToolProject(final InstanceConfiguration pInstanceConfiguration)
      throws PluginServiceException
  {
    try
    {
      // Obtain Gitlab connector
      final GitlabRestConnector connector = gitlabRestClient.getConnector(
          pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientPwd());

      final String toolProjectId = pInstanceConfiguration.getToolProjectId();
      if (!"".equals(toolProjectId))
      {
        final GitlabProject project = gitlabRestClient.getProject(connector, toolProjectId);

        // Delete gitlab group
        gitlabRestClient.deleteGroup(connector, project.getNamespace().getId());

        gitlabRestClient.deleteProject(connector, toolProjectId);
      }

    }
    catch (final GitlabRestException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to delete tool project user with toolProjectId=%s",
          pInstanceConfiguration.getToolProjectId()), e);
    }

    return true;
  }

  @Override
  public void archiveProject(final String pInstanceId) throws PluginServiceException
  {
    // NOT IMPLEMENTED
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolProject(final InstanceConfiguration pInstanceConfiguration, final PluginProject pProject)
      throws PluginServiceException
  {
    // The gitlab project cannot be updated using the rest API and the description are not set in it
    return true;
  }

  /**
   * Use by container to inject {@link GitlabRestClient}
   * 
   * @param pGitlabRestClient
   *          the gitlabRestClient to set
   */
  public void setGitlabRestClient(final GitlabRestClient pGitlabRestClient)
  {
    gitlabRestClient = pGitlabRestClient;
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
   * Use by container to inject {@link GitlabResourceBuilder}
   * 
   * @param pGitlabResourceBuilder
   *          the gitlabResourceBuilder to set
   */
  public void setGitlabResourceBuilder(final GitlabResourceBuilder pGitlabResourceBuilder)
  {
    gitlabResourceBuilder = pGitlabResourceBuilder;
  }
}
