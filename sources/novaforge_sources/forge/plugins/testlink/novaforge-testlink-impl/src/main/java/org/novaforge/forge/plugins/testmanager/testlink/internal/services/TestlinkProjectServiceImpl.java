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
package org.novaforge.forge.plugins.testmanager.testlink.internal.services;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;
import org.novaforge.forge.plugins.testmanager.testlink.client.TestlinkXmlRpcClient;
import org.novaforge.forge.plugins.testmanager.testlink.client.TestlinkXmlRpcException;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestLinkRPCStatus;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestlinkResourceBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 25 juil. 2011
 */
public class TestlinkProjectServiceImpl extends AbstractPluginProjectService implements PluginProjectService
{

  /**
   * Reference to service implementation of {@link TestlinkXmlRpcClient}
   */
  private TestlinkXmlRpcClient       testlinkXmlRpcClient;
  /**
   * Reference to service implementation of {@link TestlinkResourceBuilder}
   */
  private TestlinkResourceBuilder    testlinkResourceBuilder;
  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected String createToolProject(final InstanceConfiguration instanceConfiguration,
      final PluginProject pluginProject, final List<PluginMembership> pluginMemberships)
      throws PluginServiceException
  {
    try
    {
      final XmlRpcClient connector = testlinkXmlRpcClient.getConnector(
          pluginConfigurationService.getClientURL(instanceConfiguration.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      final Map<String, String> memberships = addToolUsers(connector, pluginMemberships);
      final HashMap<String, Object> projectData = testlinkResourceBuilder.buildProjectData(pluginProject,
          instanceConfiguration.getConfigurationId());

      final TestLinkRPCStatus<String> result = testlinkXmlRpcClient.createProject(connector, projectData);

      if (result.isSucces())
      {
        final String projectId = result.getReturnValue();
        addUsersToProject(connector, instanceConfiguration.getInstanceId(), projectId, memberships);
        return projectId;
      }
      else
      {
        throw new PluginServiceException(
            String
                .format(
                    "Unable to create project with InstanceConfiguration=%s and PluginProject=%s : tool call error message :\"%s\"",
                    instanceConfiguration.toString(), pluginProject.toString(), result.getMessage()));
      }

    }
    catch (final TestlinkXmlRpcException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to create project with InstanceConfiguration=%s and PluginProject=%s",
          instanceConfiguration.toString(), pluginProject.toString()), e);
    }
  }

  /**
   * @param pluginMemberships
   * @return
   * @throws TestlinkXmlRpcException
   */
  private Map<String, String> addToolUsers(final XmlRpcClient pXmlRpcClient,
      final List<PluginMembership> pluginMemberships) throws TestlinkXmlRpcException
  {
    final Map<String, String> users = new HashMap<String, String>();
    for (final PluginMembership membership : pluginMemberships)
    {
      final PluginUser user = membership.getPluginUser();
      final String role = membership.getRole();

      final TestLinkRPCStatus<?> isExistingLogin = testlinkXmlRpcClient.isExistingLogin(pXmlRpcClient,
          user.getLogin());
      if (!isExistingLogin.isSucces())
      {
        final HashMap<String, Object> accountData = testlinkResourceBuilder.buildAccountData(user);
        testlinkXmlRpcClient.createUser(pXmlRpcClient, accountData);
      }

      users.put(user.getLogin(), role);

    }

    return users;
  }

  /**
   * @param instanceId
   * @param projectToolId
   * @param memberships
   * @throws TestlinkXmlRpcException
   * @throws PluginServiceException
   */
  private void addUsersToProject(final XmlRpcClient pXmlRpcClient, final String instanceId,
      final String projectId, final Map<String, String> memberships) throws TestlinkXmlRpcException,
      PluginServiceException
  {
    for (final Map.Entry<String, String> entry : memberships.entrySet())
    {
      final String login = entry.getKey();
      final String forgeRole = entry.getValue();
      if (pluginRoleMappingService.existToolRole(instanceId, forgeRole))
      {
        final String toolRole = pluginRoleMappingService.getToolRole(instanceId, forgeRole);
        final String roleId = pluginRoleMappingService.getToolRoleId(toolRole);
        testlinkXmlRpcClient.addUserToProject(pXmlRpcClient, projectId, login, roleId);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean deleteToolProject(final InstanceConfiguration instanceConfiguration) throws PluginServiceException
  {

    final String toolProjectId = instanceConfiguration.getToolProjectId();
    try
    {
      final XmlRpcClient connector = testlinkXmlRpcClient.getConnector(
          pluginConfigurationService.getClientURL(instanceConfiguration.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
      return testlinkXmlRpcClient.deleteProject(connector, toolProjectId).isSucces();
    }
    catch (final TestlinkXmlRpcException e)
    {
      throw new PluginServiceException(String.format("Unable to delete project to Testlink Instance with [projectName=%s]",
                                                     toolProjectId), e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void archiveProject(final String pInstanceId) throws PluginServiceException
  {
    // TODO Auto-generated method stub

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolProject(final InstanceConfiguration instanceConfiguration,
                                      final PluginProject pluginProject) throws PluginServiceException
  {
    try
    {

      final HashMap<String, Object> projectData = testlinkResourceBuilder.buildProjectData(pluginProject,
                                                                                           instanceConfiguration
                                                                                               .getConfigurationId());

      final XmlRpcClient connector = testlinkXmlRpcClient.getConnector(
          pluginConfigurationService.getClientURL(instanceConfiguration.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
      return testlinkXmlRpcClient.updateProject(connector, projectData, instanceConfiguration.getToolProjectId())
                                 .isSucces();

    }
    catch (final TestlinkXmlRpcException e)
    {
      throw new PluginServiceException(String.format("Unable to update project to Testlink Instance with [instance=%s, toolprojectid=%s, project=%s]",
                                                     instanceConfiguration.toString(),
                                                     instanceConfiguration.getToolProjectId(),
                                                     pluginProject.toString()), e);
    }
  }

  /**
   * Use by container to inject {@link TestlinkXmlRpcClient}
   * 
   * @param pTestlinkXmlRpcClient
   *          the testlinkXmlRpcClient to set
   */
  public void setTestlinkXmlRpcClient(final TestlinkXmlRpcClient pTestlinkXmlRpcClient)
  {
    testlinkXmlRpcClient = pTestlinkXmlRpcClient;
  }

  /**
   * Use by container to inject {@link TestlinkResourceBuilder}
   * 
   * @param pTestlinkResourceBuilder
   *          the testlinkResourceBuilder to set
   */
  public void setTestlinkResourceBuilder(final TestlinkResourceBuilder pTestlinkResourceBuilder)
  {
    testlinkResourceBuilder = pTestlinkResourceBuilder;
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

}
