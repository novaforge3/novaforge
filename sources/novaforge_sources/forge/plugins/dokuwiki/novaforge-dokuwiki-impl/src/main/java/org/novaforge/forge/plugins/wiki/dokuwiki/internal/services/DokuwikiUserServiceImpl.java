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
package org.novaforge.forge.plugins.wiki.dokuwiki.internal.services;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;
import org.novaforge.forge.plugins.wiki.dokuwiki.client.DokuwikiClientException;
import org.novaforge.forge.plugins.wiki.dokuwiki.client.DokuwikiXmlRpcClient;
import org.novaforge.forge.plugins.wiki.dokuwiki.datamapper.DokuwikiResourceBuilder;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuWikiUser;
import org.novaforge.forge.plugins.wiki.dokuwiki.services.DokuwikiConfigurationService;

/**
 * @author lamirang
 */
public class DokuwikiUserServiceImpl extends AbstractPluginUserService implements PluginUserService
{

  /**
   * Reference to service implementation of {@link DokuwikiXmlRpcClient}
   */
  private DokuwikiXmlRpcClient         dokuwikiXmlRpcClient;

  /**
   * Reference to service implementation of {@link DokuwikiResourceBuilder}
   */
  private DokuwikiResourceBuilder      dokuwikiResourceBuilder;

  /**
   * Reference to service implementation of {@link DokuwikiConfigurationService}
   */
  private DokuwikiConfigurationService dokuwikiConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void createAdministratorUser(final ToolInstance pInstance, final PluginUser pUser)
      throws PluginServiceException
  {
    final DokuWikiUser user = dokuwikiResourceBuilder.buildDokuWikiUser(pUser);
    user.addGroup(DokuWikiUser.ADMIN_GROUP);
    try
    {

      final XmlRpcClient connector = dokuwikiXmlRpcClient.getConnector(
          dokuwikiConfigurationService.getClientURL(pInstance.getBaseURL()),
          dokuwikiConfigurationService.getClientAdmin(), dokuwikiConfigurationService.getClientPwd());

      if (!dokuwikiXmlRpcClient.hasUser(connector, user.getUserName()))
      {
        dokuwikiXmlRpcClient.createUser(connector, user, false);
      }
      else
      {
        dokuwikiXmlRpcClient.updateUser(connector, user.getUserName(), user, false);
      }
    }
    catch (final DokuwikiClientException e)
    {
      throw new PluginServiceException(String.format("Unable to create administrator user with user=%s",
          pUser.toString()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean removeToolUser(final InstanceConfiguration pInstance, final PluginUser pUser)
      throws PluginServiceException
  {

    boolean success;
    try
    {

      final XmlRpcClient connector = dokuwikiXmlRpcClient.getConnector(
          dokuwikiConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          dokuwikiConfigurationService.getClientAdmin(), dokuwikiConfigurationService.getClientPwd());
      final DokuWikiUser user = dokuwikiResourceBuilder.buildDokuWikiUser(pUser);
      success = dokuwikiXmlRpcClient.deleteUser(connector, user);
    }
    catch (final DokuwikiClientException e)
    {
      throw new PluginServiceException(String.format("Unable to delete user with [instance=%s and User=%s]",
                                                     pInstance.toString(), pUser.toString()), e);

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

    boolean success;
    try
    {

      final XmlRpcClient connector = dokuwikiXmlRpcClient.getConnector(
          dokuwikiConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          dokuwikiConfigurationService.getClientAdmin(), dokuwikiConfigurationService.getClientPwd());
      final DokuWikiUser user = dokuwikiResourceBuilder.buildDokuWikiUser(pPluginUser);
      success = dokuwikiXmlRpcClient.updateUser(connector, pUserName, user, false);
    }
    catch (final DokuwikiClientException e)
    {
      throw new PluginServiceException(String
                                           .format("Unable to update user with [instance=%s, username=%s and User=%s]",
                                                   pInstance.toString(), pUserName, pPluginUser.toString()), e);

    }
    return success;
  }

  /**
   * Use by container to inject {@link DokuwikiXmlRpcClient}
   * 
   * @param pDokuwikiXmlRpcClient
   *          the dokuwikiXmlRpcClient to set
   */
  public void setDokuwikiXmlRpcClient(final DokuwikiXmlRpcClient pDokuwikiXmlRpcClient)
  {
    dokuwikiXmlRpcClient = pDokuwikiXmlRpcClient;
  }

  /**
   * Use by container to inject {@link DokuwikiResourceBuilder}
   * 
   * @param pDokuwikiResourceBuilder
   *          the dokuwikiResourceBuilder to set
   */
  public void setDokuwikiResourceBuilder(final DokuwikiResourceBuilder pDokuwikiResourceBuilder)
  {
    dokuwikiResourceBuilder = pDokuwikiResourceBuilder;
  }

  /**
   * Use by container to inject {@link DokuwikiConfigurationService}
   * 
   * @param pDokuwikiConfigurationService
   *          the dokuwikiConfigurationService to set
   */
  public void setDokuwikiConfigurationService(final DokuwikiConfigurationService pDokuwikiConfigurationService)
  {
    dokuwikiConfigurationService = pDokuwikiConfigurationService;
  }
}
