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
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyClientException;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyXmlRpcClient;
import org.novaforge.forge.plugins.surveytool.limesurvey.client.LimesurveyXmlRpcClientExt;
import org.novaforge.forge.plugins.surveytool.limesurvey.datamapper.LimesurveyResourceBuilder;
import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyUser;

/**
 * @author vvigo
 */
public class LimesurveyUserServiceImpl extends AbstractPluginUserService implements PluginUserService
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
  public void createAdministratorUser(final ToolInstance pInstance, final PluginUser pUser)
      throws PluginServiceException
  {
    try
    {
      LimesurveyXmlRpcClientExt connector = limesurveyXmlRpcClient.getConnector(
          pluginConfigurationService.getClientURL(pInstance.getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      final LimesurveyUser account = limesurveyResourceBuilder.buildLimesurveyUser(pUser);

      if (limesurveyXmlRpcClient.isUserExist(connector, account).intValue()==-1)
      {
        limesurveyXmlRpcClient.createSuperAdmin(connector, account);
      }

    }
    catch (final LimesurveyClientException e)
    {
      throw new PluginServiceException("Unable to add super-administrator", e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean removeToolUser(final InstanceConfiguration pInstance, final PluginUser pUser)
      throws PluginServiceException
  {

    final boolean success = false;
    try
    {
      LimesurveyXmlRpcClientExt connector = limesurveyXmlRpcClient.getConnector(
          pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      final LimesurveyUser account = limesurveyResourceBuilder.buildLimesurveyUser(pUser);

      final Integer userId = limesurveyXmlRpcClient.isUserExist(connector, account);
      if (userId.intValue() > 0)
      {
        limesurveyXmlRpcClient.deleteUser(connector, account);
      }
    }
    catch (final LimesurveyClientException e)
    {
      throw new PluginServiceException(String.format("Unable to delete user with username=%s", pUser.getLogin()), e);
    }

    return success;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolUser(final InstanceConfiguration pInstance, final String pUserName,
                                   final PluginUser pPluginUser) throws PluginServiceException
  {

    boolean success = false;
    try
    {
      LimesurveyXmlRpcClientExt connector = limesurveyXmlRpcClient.getConnector(
          pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      final LimesurveyUser account = limesurveyResourceBuilder.buildLimesurveyUser(pPluginUser);

      final Integer userId = limesurveyXmlRpcClient.isUserExist(connector, account);
      if (userId.intValue() > 0)
      {
        success = limesurveyXmlRpcClient.updateUser(connector, account);
      }
    }
    catch (final LimesurveyClientException e)
    {
      throw new PluginServiceException(String.format("Unable to update user with username=%s", pUserName), e);
    }

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
