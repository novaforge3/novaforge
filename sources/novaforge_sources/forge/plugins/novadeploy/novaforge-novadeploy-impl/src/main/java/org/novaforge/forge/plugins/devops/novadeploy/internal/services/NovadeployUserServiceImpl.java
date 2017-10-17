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
package org.novaforge.forge.plugins.devops.novadeploy.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;
import org.novaforge.forge.plugins.devops.novadeploy.client.NovadeployClient;
import org.novaforge.forge.plugins.devops.novadeploy.client.NovadeployClientException;
import org.novaforge.forge.plugins.devops.novadeploy.client.NovadeployRestClient;
import org.novaforge.forge.plugins.devops.novadeploy.datamapper.NovadeployResourceBuilder;
import org.novaforge.forge.plugins.devops.novadeploy.model.NovadeployUser;
import org.novaforge.forge.plugins.devops.novadeploy.services.NovadeployConfigurationService;

/**
 * @author dekimpea
 */
public class NovadeployUserServiceImpl extends AbstractPluginUserService implements PluginUserService
{

  /**
   * Reference to service implementation of {@link NovadeployRestClient}
   */
  private NovadeployRestClient         novadeployRestClient;

  /**
   * Reference to service implementation of {@link NovadeployResourceBuilder}
   */
  private NovadeployResourceBuilder      novadeployResourceBuilder;

  /**
   * Reference to service implementation of {@link NovadeployConfigurationService}
   */
  private NovadeployConfigurationService novadeployConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void createAdministratorUser(final ToolInstance pInstance, final PluginUser pUser)
      throws PluginServiceException
  {
	  // Impossible on Novadeploy
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean removeToolUser(final InstanceConfiguration pInstance, final PluginUser pUser)
      throws PluginServiceException
  {

    boolean success = false;
    try
    {

      NovadeployClient connector = novadeployRestClient.getConnector(
          novadeployConfigurationService.getClientURL(novadeployConfigurationService.getDefaultToolURL()),
          novadeployConfigurationService.getClientAdmin(), novadeployConfigurationService.getClientPwd());
      final NovadeployUser user = novadeployResourceBuilder.buildNovadeployUser(pUser);
      success = novadeployRestClient.deleteUser(connector, user);
    }
    catch (final NovadeployClientException e)
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

    boolean success = false;
    try
    {

      NovadeployClient connector = novadeployRestClient.getConnector(
          novadeployConfigurationService.getClientURL(novadeployConfigurationService.getDefaultToolURL()),
          novadeployConfigurationService.getClientAdmin(), novadeployConfigurationService.getClientPwd());
      
      
      final NovadeployUser user = novadeployResourceBuilder.buildNovadeployUser(pPluginUser);
      success = novadeployRestClient.updateUser(connector, pUserName, user);
    }
    catch (final NovadeployClientException e)
    {
      throw new PluginServiceException(String
                                           .format("Unable to update user with [instance=%s, username=%s and User=%s]",
                                                   pInstance.toString(), pUserName, pPluginUser.toString()), e);

    }
    return success;
  }

  /**
   * Use by container to inject {@link NovadeployRestClient}
   * 
   * @param pNovadeployRestClient
   *          the NovadeployRestClient to set
   */
  public void setNovadeployRestClient(final NovadeployRestClient pNovadeployRestClient)
  {
    novadeployRestClient = pNovadeployRestClient;
  }

  /**
   * Use by container to inject {@link NovadeployResourceBuilder}
   * 
   * @param pNovadeployResourceBuilder
   *          the NovadeployResourceBuilder to set
   */
  public void setNovadeployResourceBuilder(final NovadeployResourceBuilder pNovadeployResourceBuilder)
  {
    novadeployResourceBuilder = pNovadeployResourceBuilder;
  }

  /**
   * Use by container to inject {@link NovadeployConfigurationService}
   * 
   * @param pNovadeployConfigurationService
   *          the NovadeployConfigurationService to set
   */
  public void setNovadeployConfigurationService(final NovadeployConfigurationService pNovadeployConfigurationService)
  {
    novadeployConfigurationService = pNovadeployConfigurationService;
  }
}
