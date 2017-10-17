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
package org.novaforge.forge.plugins.cms.spip.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.cms.spip.client.SpipSoapClient;
import org.novaforge.forge.plugins.cms.spip.client.SpipSoapConnector;
import org.novaforge.forge.plugins.cms.spip.client.SpipSoapException;
import org.novaforge.forge.plugins.cms.spip.datamapper.SpipResourceBuilder;
import org.novaforge.forge.plugins.cms.spip.services.SpipConfigurationService;
import org.novaforge.forge.plugins.cms.spip.soap.SiteInput;
import org.novaforge.forge.plugins.cms.spip.soap.UserData;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Guillaume Lamirand
 */
public class SpipProjectServiceImpl extends AbstractPluginProjectService implements PluginProjectService
{

  /**
   * Reference to service implementation of {@link SpipSoapClient}
   */
  private SpipSoapClient           spipSoapClient;
  /**
   * Reference to service implementation of {@link SpipResourceBuilder}
   */
  private SpipResourceBuilder      spipResourceBuilder;
  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private SpipConfigurationService pluginConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected String createToolProject(final InstanceConfiguration pInstanceConfiguration,
      final PluginProject pPluginProject, final List<PluginMembership> pPluginMembership)
      throws PluginServiceException
  {

    final String toolProjectId =
        pInstanceConfiguration.getForgeProjectId() + "_" + pInstanceConfiguration.getConfigurationId();
    try
    {
      // Obtain spip connector

      final SpipSoapConnector connector = spipSoapClient.getConnector(pluginConfigurationService
          .getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL(), toolProjectId),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      // Create the spip site: consequently the projectToolId will be initialized.
      final SiteInput siteInput = spipResourceBuilder.buildSiteInput(pPluginProject, toolProjectId);
      spipSoapClient.create_site(connector, siteInput);

      // Add if necessary the user to spip instance
      final Map<String, String> memberships = addToolUser(connector, pPluginMembership);

      // Create the memberships between users and project created
      // membership : user id must retrieved from Spip :may be user does not exist!
      addUsersToProject(connector, pInstanceConfiguration.getInstanceId(), toolProjectId, memberships);

    }
    catch (final SpipSoapException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to create project with InstanceConfiguration=%s and PluginProject=%s",
          pInstanceConfiguration.toString(), pPluginProject.toString()), e);
    }

    return toolProjectId;
  }

  private Map<String, String> addToolUser(final SpipSoapConnector pConnector,
      final List<PluginMembership> pUsers) throws PluginServiceException, SpipSoapException
  {
    final Map<String, String> users = new HashMap<String, String>();
    for (final PluginMembership membership : pUsers)
    {
      final PluginUser user = membership.getPluginUser();
      final String role = membership.getRole();

      final UserData userData = spipResourceBuilder.buildUserData(user);

      BigInteger userId = spipSoapClient.get_user_id(pConnector, userData.getLogin());
      if (userId == null)
      {
        userId = spipSoapClient.add_user(pConnector, userData);
      }
      users.put(userData.getLogin(), role);
    }

    return users;
  }

  private void addUsersToProject(final SpipSoapConnector pConnector, final String pInstanceId,
      final String pToolProjectId, final Map<String, String> pUsers) throws SpipSoapException,
      PluginServiceException
  {
    final Set<Entry<String, String>> usersRole = pUsers.entrySet();
    for (final Entry<String, String> entry : usersRole)
    {
      final String login = entry.getKey();
      final String forgeRole = entry.getValue();

      if (pluginRoleMappingService.existToolRole(pInstanceId, forgeRole))
      {
        final String toolRole = pluginRoleMappingService.getToolRole(pInstanceId, forgeRole);
        final String roleId = pluginRoleMappingService.getToolRoleId(toolRole);

        spipSoapClient.add_user_site(pConnector, pToolProjectId, login, roleId);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean deleteToolProject(final InstanceConfiguration pInstance) throws PluginServiceException
  {
    boolean success = false;
    try
    {
      // Obtain spip connector
      final SpipSoapConnector connector = spipSoapClient.getConnector(pluginConfigurationService.getClientURL(pInstance
                                                                                                                  .getToolInstance()
                                                                                                                  .getBaseURL(),
                                                                                                              pInstance
                                                                                                                  .getToolProjectId()),
                                                                      pluginConfigurationService.getClientAdmin(),
          pluginConfigurationService.getClientPwd());
      success = spipSoapClient.delete_site(connector, pInstance.getToolProjectId());

    }
    catch (final SpipSoapException e)
    {
      throw new PluginServiceException(String.format("Unable to delete tool project user with toolProjectId=%s",
                                                     pInstance.getToolProjectId()), e);
    }
    return success;
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
  protected boolean updateToolProject(final InstanceConfiguration pInstanceConfiguration, final PluginProject pProject)
      throws PluginServiceException
  {
    boolean success = false;
    try
    {
      final SiteInput siteInput = spipResourceBuilder.buildSiteInput(pProject,
                                                                     pInstanceConfiguration.getToolProjectId());

      // Obtain spip connector
      final SpipSoapConnector connector = spipSoapClient.getConnector(pluginConfigurationService
                                                                          .getClientURL(pInstanceConfiguration
                                                                                            .getToolInstance()
                                                                                            .getBaseURL(),
                                                                                        pInstanceConfiguration
                                                                                            .getToolProjectId()),
                                                                      pluginConfigurationService.getClientAdmin(),
          pluginConfigurationService.getClientPwd());
      success = spipSoapClient.update_site(connector, siteInput);
    }
    catch (final SpipSoapException e)
    {
      throw new PluginServiceException(String.format("Unable to update tool project user with InstanceConfiguration=%s and PluginProject=%s",
                                                     pInstanceConfiguration.toString(), pProject.toString()), e);
    }
    return success;
  }

  private String getToolProjectId(final String pInstanceId) throws PluginServiceException
  {
    return instanceConfigurationDAO.findByInstanceId(pInstanceId).getToolProjectId();
  }

  /**
   * Use by container to inject {@link SpipSoapClient}
   * 
   * @param pSpipSoapClient
   *          the spipSoapClient to set
   */
  public void setSpipSoapClient(final SpipSoapClient pSpipSoapClient)
  {
    spipSoapClient = pSpipSoapClient;
  }

  /**
   * Use by container to inject {@link SpipResourceBuilder}
   * 
   * @param pSpipResourceBuilder
   *          the spipResourceBuilder to set
   */
  public void setSpipResourceBuilder(final SpipResourceBuilder pSpipResourceBuilder)
  {
    spipResourceBuilder = pSpipResourceBuilder;
  }

  /**
   * Use by container to inject {@link SpipConfigurationService}
   * 
   * @param pPluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final SpipConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }
}
