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
package org.novaforge.forge.plugins.surveytool.limesurvey.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyClientException;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyXmlRpcClient;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyXmlRpcClientExt;
import org.novaforge.forge.plugins.surveytool.limesurvey.datamapper.LimesurveyResourceBuilder;
import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyGroupUser;
import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyRolePrivilege;
import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vvigo
 */
public class LimesurveyProjectServiceImpl extends AbstractPluginProjectService implements
    PluginProjectService
{

  /*
   * Service injection declaration
   */
  private LimesurveyXmlRpcClient     limesurveyXmlRpcClient;

  private LimesurveyResourceBuilder  limesurveyResourceBuilder;

  private PluginConfigurationService pluginConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected String createToolProject(final InstanceConfiguration pInstanceConfiguration,
      final PluginProject pPluginProject, final List<PluginMembership> pPluginMembership)
      throws PluginServiceException
  {
    Integer userGroupId = null;
    try
    {
      // Create user group on limesurvey
      userGroupId = createUserGroup(pInstanceConfiguration, pPluginProject);

      // Create project users in limesurvey
      addToolUser(pInstanceConfiguration, pPluginMembership);

      // add created users in project user group
      addUserToGroup(pInstanceConfiguration, pPluginMembership, pPluginProject);

    }
    catch (final LimesurveyClientException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to create user group with InstanceConfiguration=%s and PluginProject=%s",
          pInstanceConfiguration.toString(), pPluginProject.toString()), e);
    }
    return userGroupId.toString();
  }

  /**
   * @param pInstanceConfiguration
   * @param pPluginProject
   * @return
   * @throws PluginServiceException
   * @throws LimesurveyClientException
   */
  private Integer createUserGroup(final InstanceConfiguration pInstanceConfiguration,
      final PluginProject pPluginProject) throws PluginServiceException, LimesurveyClientException
  {

    LimesurveyXmlRpcClientExt connector = limesurveyXmlRpcClient.getConnector(
        pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
        pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

    final LimesurveyGroupUser surveyGroupUsers = limesurveyResourceBuilder.buildLimesurveyGroupUser(
        pPluginProject, pInstanceConfiguration.getConfigurationId());

    return limesurveyXmlRpcClient.createGroupUser(connector, surveyGroupUsers);
  }

  private Map<Integer, String> addToolUser(final InstanceConfiguration pInstanceConfiguration,
      final List<PluginMembership> pUsers) throws PluginServiceException, LimesurveyClientException
  {

    LimesurveyXmlRpcClientExt connector = limesurveyXmlRpcClient.getConnector(
        pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
        pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

    final Map<Integer, String> users = new HashMap<Integer, String>();
    for (final PluginMembership membership : pUsers)
    {
      final PluginUser user = membership.getPluginUser();
      final String role = membership.getRole();
      final String toolRole = pluginRoleMappingService.getToolRole(pInstanceConfiguration.getInstanceId(),
          role);

      final LimesurveyUser account = limesurveyResourceBuilder.buildLimesurveyUser(user);

      Integer userId = new Integer(limesurveyXmlRpcClient.isUserExist(connector, account));
      if (userId.intValue() < 0)
      {
        userId = limesurveyXmlRpcClient.createUser(connector, account);

      }

      if (LimesurveyRolePrivilege.ADMINISTRATOR.getLabel().equals(toolRole))
      {
        limesurveyXmlRpcClient.setCreateSurveyPermission(connector, account, true);
      }
      else
      {
        limesurveyXmlRpcClient.setCreateSurveyPermission(connector, account, false);
      }

      users.put(userId, role);
    }

    return users;
  }

  private void addUserToGroup(final InstanceConfiguration pInstanceConfiguration,
      final List<PluginMembership> pUsers, final PluginProject pPluginProject) throws PluginServiceException,
      LimesurveyClientException
  {

    LimesurveyXmlRpcClientExt connector = limesurveyXmlRpcClient.getConnector(
        pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
        pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

    final LimesurveyGroupUser surveyGroupUsers = limesurveyResourceBuilder.buildLimesurveyGroupUser(
        pPluginProject, pInstanceConfiguration.getConfigurationId());

    if (limesurveyXmlRpcClient.isGroupUserExist(connector, surveyGroupUsers).intValue()!=-1)
    {
      for (final PluginMembership membership : pUsers)
      {
        final PluginUser user = membership.getPluginUser();
        final LimesurveyUser account = limesurveyResourceBuilder.buildLimesurveyUser(user);

        limesurveyXmlRpcClient.addUserInGroup(connector, account, surveyGroupUsers);
      }
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean deleteToolProject(final InstanceConfiguration pInstanceConfiguration) throws PluginServiceException
  {

    boolean success = false;
    try
    {
      final Integer toolGroupProjectId = new Integer(pInstanceConfiguration.getToolProjectId());

      LimesurveyXmlRpcClientExt connector = limesurveyXmlRpcClient.getConnector(pluginConfigurationService
                                                                                      .getClientURL(pInstanceConfiguration
                                                                                                        .getToolInstance()
                                                                                                        .getBaseURL()),
                                                                                  pluginConfigurationService
                                                                                      .getClientAdmin(),
                                                                                  pluginConfigurationService
                                                                                      .getClientPwd());

      success = Boolean.valueOf(limesurveyXmlRpcClient.deleteGroupUser(connector, toolGroupProjectId));

    }
    catch (final LimesurveyClientException e)
    {
      throw new PluginServiceException(String.format("Unable to delete user group with group id=%s",
                                                     pInstanceConfiguration.getToolProjectId()), e);
    }

    return success;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void archiveProject(final String pInstanceId) throws PluginServiceException
  {

    // TODO Not implemented yet
    System.out.println("LimesurveyProjectServiceImpl.archiveProject() -> NOT IMPLEMETED YET");

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolProject(final InstanceConfiguration pInstanceConfiguration,
      final PluginProject pPluginProject) throws PluginServiceException
  {

    boolean success = false;
    try
    {
      // get group id
      final Integer groupId = new Integer(getToolGroupProjectId(pInstanceConfiguration.getInstanceId()));

      // update user group on limesurvey
      success = updateUserGroup(pInstanceConfiguration, pPluginProject, groupId);

    }
    catch (final LimesurveyClientException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to create user group with InstanceConfiguration=%s and PluginProject=%s",
          pInstanceConfiguration.toString(), pPluginProject.toString()), e);
    }

    return success;
  }

  private String getToolGroupProjectId(final String pInstanceId) throws PluginServiceException
  {
    return instanceConfigurationDAO.findByInstanceId(pInstanceId).getToolProjectId();
  }

  /**
   * @param pInstanceConfiguration
   * @param pPluginProject
   * @return
   * @throws PluginServiceException
   * @throws LimesurveyClientException
   */
  private boolean updateUserGroup(final InstanceConfiguration pInstanceConfiguration,
                                  final PluginProject pPluginProject, final Integer pGroupId)
      throws PluginServiceException, LimesurveyClientException
  {
    boolean success = false;
    LimesurveyXmlRpcClientExt connector = limesurveyXmlRpcClient.getConnector(pluginConfigurationService
                                                                                    .getClientURL(pInstanceConfiguration
                                                                                                      .getToolInstance()
                                                                                                      .getBaseURL()),
                                                                                pluginConfigurationService
                                                                                    .getClientAdmin(),
                                                                                pluginConfigurationService
                                                                                    .getClientPwd());

    final LimesurveyGroupUser surveyGroupUsers = limesurveyResourceBuilder.buildLimesurveyGroupUser(pPluginProject,
                                                                                                    pInstanceConfiguration
                                                                                                        .getConfigurationId());

    success = limesurveyXmlRpcClient.updateGroupUser(connector, surveyGroupUsers, pGroupId);

    return success;
  }

  public void setLimesurveyXmlRpcClient(final LimesurveyXmlRpcClient pLimesurveyXmlRpcClient)
  {
    limesurveyXmlRpcClient = pLimesurveyXmlRpcClient;
  }

  public void setLimesurveyResourceBuilder(final LimesurveyResourceBuilder pLimesurveyResourceBuilder)
  {
    limesurveyResourceBuilder = pLimesurveyResourceBuilder;
  }

  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }

}
