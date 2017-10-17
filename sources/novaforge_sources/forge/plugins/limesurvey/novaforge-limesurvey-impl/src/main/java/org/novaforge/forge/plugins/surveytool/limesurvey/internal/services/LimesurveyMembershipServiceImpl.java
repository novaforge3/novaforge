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
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyClientException;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyXmlRpcClient;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyXmlRpcClientExt;
import org.novaforge.forge.plugins.surveytool.limesurvey.datamapper.LimesurveyResourceBuilder;
import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyRolePrivilege;
import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyUser;

/**
 * @author vvigo
 */
public class LimesurveyMembershipServiceImpl extends AbstractPluginMembershipService implements
    PluginMembershipService
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
  protected boolean addToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
      final String pToolRole) throws PluginServiceException
  {
    try
    {
      LimesurveyXmlRpcClientExt connector = limesurveyXmlRpcClient.getConnector(
          pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      // get group id
      final Integer groupId = new Integer(pInstance.getToolProjectId());

      final LimesurveyUser account = limesurveyResourceBuilder.buildLimesurveyUser(pUser);

      Integer userId = new Integer(limesurveyXmlRpcClient.isUserExist(connector, account));
      if (userId.intValue() < 0)
      {
        userId = limesurveyXmlRpcClient.createUser(connector, account);
      }
      boolean success = true;
      if (LimesurveyRolePrivilege.ADMINISTRATOR.getLabel().equals(pToolRole))
      {
        success = limesurveyXmlRpcClient.setCreateSurveyPermission(connector, account, Boolean.TRUE);
      }
      if (success)
      {
        success = false;
        if (limesurveyXmlRpcClient.isGroupUserExistById(connector, groupId).intValue()!=-1)
        {
          success = limesurveyXmlRpcClient.addUserInGroupById(connector, account, groupId);
        }
      }
      if (!success)
        throw new PluginServiceException(String.format(
            "Unable to add user to project with InstanceConfiguration=%s and User=%s", pInstance.toString(),
            pUser.getLogin()));      
    }
    catch (final LimesurveyClientException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to add user to project with InstanceConfiguration=%s and User=%s", pInstance.toString(),
          pUser.getLogin()), e);
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
      LimesurveyXmlRpcClientExt connector = limesurveyXmlRpcClient.getConnector(
          pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      final LimesurveyUser account = limesurveyResourceBuilder.buildLimesurveyUser(pUser);

      Integer userId = new Integer(limesurveyXmlRpcClient.isUserExist(connector, account));
      if (userId.intValue() < 0)
      {
        userId = limesurveyXmlRpcClient.createUser(connector, account);
      }
      boolean success = true;
      if (LimesurveyRolePrivilege.ADMINISTRATOR.getLabel().equals(pToolRole))
      {
        success = limesurveyXmlRpcClient.setCreateSurveyPermission(connector, account, true);
      }
      else
      {
        success = limesurveyXmlRpcClient.setCreateSurveyPermission(connector, account, false);
      }
      if (!success)
        throw new PluginServiceException(String.format(
            "Unable to update user membership to project with InstanceConfiguration=%s and User=%s", 
            pInstance.toString(), pUser.getLogin()));      
    }
    catch (final LimesurveyClientException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to update user membership to project with InstanceConfiguration=%s and User=%s",
          pInstance.toString(), pUser.getLogin()), e);
    }
    return true;
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

      LimesurveyXmlRpcClientExt connector = limesurveyXmlRpcClient.getConnector(
          pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      // get group id
      pInstance.getToolProjectId();

      final LimesurveyUser account = limesurveyResourceBuilder.buildLimesurveyUser(pUser);

      final Integer userId = new Integer(limesurveyXmlRpcClient.isUserExist(connector, account));
      boolean success = true;
      if (userId.intValue() > 0)
      {
        limesurveyXmlRpcClient.removeUserFromGroup(connector, account, pInstance.getToolProjectId());
      }
      if (!success)
        throw new PluginServiceException(String.format(
            "Unable to add user to project with InstanceConfiguration=%s and User=%s", 
            pInstance.toString(), pUser.getLogin()));      
    }
    catch (final LimesurveyClientException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to add user to project with InstanceConfiguration=%s and User=%s", pInstance.toString(),
          pUser.getLogin()), e);
    }
    return true;
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
