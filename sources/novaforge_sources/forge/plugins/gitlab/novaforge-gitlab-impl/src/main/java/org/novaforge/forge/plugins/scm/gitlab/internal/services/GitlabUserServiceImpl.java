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

import org.gitlab.api.models.GitlabUser;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;
import org.novaforge.forge.plugins.scm.constants.GitlabConstant;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestClient;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestConnector;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestException;
import org.novaforge.forge.plugins.scm.gitlab.datamapper.GitlabResourceBuilder;

import java.io.FileNotFoundException;

/**
 * @author Cart Gauthier
 */
public class GitlabUserServiceImpl extends AbstractPluginUserService implements PluginUserService
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
  public void createAdministratorUser(final ToolInstance pToolInstance, final PluginUser pUser)
      throws PluginServiceException
  {
    try
    {
      // Get Gitlab Connector
      final GitlabRestConnector connector = gitlabRestClient.getConnector(
          pluginConfigurationService.getClientURL(pToolInstance.getBaseURL()),
          pluginConfigurationService.getClientPwd());

      // If the Admin User do not exist create it
      try
      {
        gitlabRestClient.getUserViaSudo(connector, pUser.getLogin());
      }
      catch (final GitlabRestException e)
      {
        // If the Admin User does not exist, FileNotFoundException will be throw by the REST API
        if ((e.getCause() instanceof FileNotFoundException))
        {
          // Build Gitlab User
          final GitlabUser gitlabUser = gitlabResourceBuilder.buildGitlabUser(pUser);

          // Set User as Admin
          gitlabUser.setAdmin(true);

          // Create the user
          gitlabRestClient.createUser(connector, gitlabUser, pUser.getPassword(),
              GitlabConstant.GITLAB_PERSONAL_PROJECTS_LIMIT);
        }
      }
    }
    catch (final GitlabRestException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to create administrator user with instance=%s and user=%s", pToolInstance.toString(),
          pUser.toString()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean removeToolUser(final InstanceConfiguration pInstanceConfiguration, final PluginUser pPluginUser)
      throws PluginServiceException
  {
    try
    {
      // Get Gitlab Connector
      final GitlabRestConnector connector = gitlabRestClient.getConnector(pluginConfigurationService
                                                                              .getClientURL(pInstanceConfiguration
                                                                                                .getToolInstance()
                                                                                                .getBaseURL()),
                                                                          pluginConfigurationService.getClientPwd());

      // Get Gitlab User
      final GitlabUser gitlabUser = gitlabRestClient.getUserViaSudo(connector, pPluginUser.getLogin());

      // Delete it
      gitlabRestClient.deleteUser(connector, gitlabUser.getId());
    }
    catch (final GitlabRestException e)
    {
      throw new PluginServiceException(String.format("Unable to delete user on Gitlab with instance=%s, and User=%s",
                                                     pInstanceConfiguration.toString(), pPluginUser.getLogin()), e);
    }

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolUser(final InstanceConfiguration pInstanceConfiguration, final String pUserName,
                                   final PluginUser pPluginUser) throws PluginServiceException
  {
    try
    {
      // Get Gitlab Connector
      final GitlabRestConnector connector = gitlabRestClient.getConnector(pluginConfigurationService
                                                                              .getClientURL(pInstanceConfiguration
                                                                                                .getToolInstance()
                                                                                                .getBaseURL()),
                                                                          pluginConfigurationService.getClientPwd());

      final GitlabUser gitlabUser = gitlabResourceBuilder.buildGitlabUser(pPluginUser);

      // Get Gitlab User
      final GitlabUser gitlabUserToUpdate = gitlabRestClient.getUserViaSudo(connector, pUserName);

      gitlabRestClient.updateUser(connector, gitlabUserToUpdate.getId(), gitlabUser, pPluginUser.getPassword(),
                                  GitlabConstant.GITLAB_PERSONAL_PROJECTS_LIMIT);
    }
    catch (final GitlabRestException e)
    {
      throw new PluginServiceException(String.format("Unable to update user with instance=%s, username=%s and User=%s",
                                                     pInstanceConfiguration.toString(), pUserName,
                                                     pPluginUser.toString()), e);
    }

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
