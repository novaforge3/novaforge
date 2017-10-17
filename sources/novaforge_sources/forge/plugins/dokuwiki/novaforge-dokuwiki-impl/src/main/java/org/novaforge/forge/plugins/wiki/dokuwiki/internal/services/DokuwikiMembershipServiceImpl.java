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
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;
import org.novaforge.forge.plugins.wiki.dokuwiki.client.DokuwikiClientException;
import org.novaforge.forge.plugins.wiki.dokuwiki.client.DokuwikiXmlRpcClient;
import org.novaforge.forge.plugins.wiki.dokuwiki.datamapper.DokuwikiResourceBuilder;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuWikiUser;
import org.novaforge.forge.plugins.wiki.dokuwiki.services.DokuwikiConfigurationService;

/**
 * @author lamirang
 */
public class DokuwikiMembershipServiceImpl extends AbstractPluginMembershipService
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
   * {@inheritDoc}
   */
  @Override
  protected boolean addToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
      final String pToolRole) throws PluginServiceException
  {

    final DokuWikiUser user = dokuwikiResourceBuilder.buildDokuWikiUser(pUser);
    final String roleId = pluginRoleMappingService.getToolRoleId(pToolRole);

    try
    {
      final XmlRpcClient connector = dokuwikiXmlRpcClient.getConnector(
          dokuwikiConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          dokuwikiConfigurationService.getClientAdmin(), dokuwikiConfigurationService.getClientPwd());

      if (!dokuwikiXmlRpcClient.hasUser(connector, user.getUserName()))
      {
        dokuwikiXmlRpcClient.createUser(connector, user, false);
      }
      dokuwikiXmlRpcClient.setUserPermissionToNameSpace(connector, pInstance.getToolProjectId(),
          user.getUserName(), Integer.parseInt(roleId));
    }
    catch (final DokuwikiClientException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to add or update user membership with [instance=%s, user=%s] ", pInstance.toString(),
          pUser.toString()), e);
    }
    return true;

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

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
      final String pToolRole) throws PluginServiceException
  {
    return addToolUserMemberships(pInstance, pUser, pToolRole);

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

      final XmlRpcClient connector = dokuwikiXmlRpcClient.getConnector(
          dokuwikiConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          dokuwikiConfigurationService.getClientAdmin(), dokuwikiConfigurationService.getClientPwd());

      if (dokuwikiXmlRpcClient.hasUser(connector, pUser.getLogin()))
      {
        dokuwikiXmlRpcClient.deleteUserPermission(connector, pInstance.getToolProjectId(), pUser.getLogin());
      }
    }
    catch (final DokuwikiClientException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to add or update user membership with [instance=%s, user=%s] ", pInstance.toString(),
          pUser.toString()), e);
    }
    return true;
  }

}
