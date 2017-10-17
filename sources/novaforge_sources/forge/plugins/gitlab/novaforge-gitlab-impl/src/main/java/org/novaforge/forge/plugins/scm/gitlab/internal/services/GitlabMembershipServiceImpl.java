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
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;
import org.novaforge.forge.plugins.scm.constants.GitlabConstant;
import org.novaforge.forge.plugins.scm.constants.GitlabRole;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestClient;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestConnector;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestException;
import org.novaforge.forge.plugins.scm.gitlab.datamapper.GitlabResourceBuilder;

import java.io.FileNotFoundException;

/**
 * @author Cart Gauthier
 */
public class GitlabMembershipServiceImpl extends AbstractPluginMembershipService implements PluginMembershipService
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
   *     the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean addToolUserMemberships(final InstanceConfiguration pInstanceConfiguration,
      final PluginUser pUser, final String pToolRole) throws PluginServiceException
  {

    try
    {
      // Obtain Gitlab connector
      final GitlabRestConnector connector;
      connector = gitlabRestClient.getConnector(pluginConfigurationService.getClientURL(pInstanceConfiguration
                                                                                            .getToolInstance()
                                                                                            .getBaseURL()),
                                                pluginConfigurationService.getClientPwd());

      // Get Gitlab User, if user does not exist, create it
      GitlabUser gitlabUser;
      try
      {
        gitlabUser = gitlabRestClient.getUserViaSudo(connector, pUser.getLogin());
      }
      catch (final GitlabRestException e)
      {
        // If the User does not exist, FileNotFoundException will be throw by the REST API
        if ((e.getCause() instanceof FileNotFoundException))
        {
          // Build Gitlab User
          gitlabUser = gitlabResourceBuilder.buildGitlabUser(pUser);

          // Create the user
          gitlabUser = gitlabRestClient.createUser(connector, gitlabUser, pUser.getPassword(),
                                                   GitlabConstant.GITLAB_PERSONAL_PROJECTS_LIMIT);
        }
        else
        {
          throw e;
        }
      }

      // Get the project to get the group ID
      final GitlabProject project = gitlabRestClient.getProject(connector, pInstanceConfiguration.getToolProjectId());

      // Get the access level
      final GitlabAccessLevel gitlabAccessLevel = GitlabAccessLevel.fromAccessValue(GitlabRole.fromLabel(pToolRole)
                                                                                              .getId());

      // Add the group member
      gitlabRestClient.addGroupMember(connector, project.getNamespace().getId(), gitlabUser.getId(), gitlabAccessLevel);

    }
    catch (final GitlabRestException e)
    {
      throw new PluginServiceException(String.format("Unable to add user membership with instance=%s and User=%s",
                                                     pInstanceConfiguration.toString(), pUser.toString()), e);
    }

    return true;
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

  @Override
  protected boolean updateToolUserMemberships(final InstanceConfiguration pInstanceConfiguration,
                                              final PluginUser pUser, final String pToolRole)
      throws PluginServiceException
  {
    try
    {
      removeToolUserMemberships(pInstanceConfiguration, pUser, pToolRole);
    }
    catch (PluginServiceException e)
    {
      final Throwable cause = e.getCause();
      // If the User does not exist, FileNotFoundException will be throw by the REST API; we ignore this
      if (!(cause.getCause() instanceof FileNotFoundException))
      {
        throw e;
      }

    }

    addToolUserMemberships(pInstanceConfiguration, pUser, pToolRole);

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean removeToolUserMemberships(final InstanceConfiguration pInstanceConfiguration,
                                              final PluginUser pUser, final String pToolRole)
      throws PluginServiceException
  {
    try
    {
      // Obtain Gitlab connector
      final GitlabRestConnector connector;
      connector = gitlabRestClient.getConnector(pluginConfigurationService.getClientURL(pInstanceConfiguration
                                                                                            .getToolInstance()
                                                                                            .getBaseURL()),
                                                pluginConfigurationService.getClientPwd());

      // Get Gitlab User
      final GitlabUser gitlabUser = gitlabRestClient.getUserViaSudo(connector, pUser.getLogin());

      // Get the project to get the group ID
      final GitlabProject project = gitlabRestClient.getProject(connector, pInstanceConfiguration.getToolProjectId());

      // Delete the group member
      gitlabRestClient.deleteGroupMember(connector, project.getNamespace().getId(), gitlabUser.getId());

    }
    catch (final GitlabRestException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to add user membership with instance=%s and User=%s", pInstanceConfiguration.toString(),
          pUser.toString()), e);
    }

    return true;
  }






}
