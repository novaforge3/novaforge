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
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;
import org.novaforge.forge.plugins.wiki.dokuwiki.client.DokuwikiClientException;
import org.novaforge.forge.plugins.wiki.dokuwiki.client.DokuwikiXmlRpcClient;
import org.novaforge.forge.plugins.wiki.dokuwiki.datamapper.DokuwikiResourceBuilder;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuWikiUser;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiPage;
import org.novaforge.forge.plugins.wiki.dokuwiki.services.DokuwikiConfigurationService;

import java.util.List;

/**
 * @author lamirang
 */
public class DokuwikiProjectServiceImpl extends AbstractPluginProjectService implements PluginProjectService
{

  /**
   * Reference to service implementation of {@link DokuwikiXmlRpcClient}
   */
  private DokuwikiXmlRpcClient         dokuwikiXmlRpcClient;
  /**
   * Reference to service implementation of {@link DokuwikiConfigurationService}
   */
  private DokuwikiConfigurationService dokuwikiConfigurationService;
  /**
   * Reference to service implementation of {@link DokuwikiResourceBuilder}
   */
  private DokuwikiResourceBuilder      dokuwikiResourceBuilder;

  /**
   * {@inheritDoc}
   */
  @Override
  protected String createToolProject(final InstanceConfiguration pInstanceConfiguration,
      final PluginProject pPluginProject, final List<PluginMembership> pPluginMembership)
      throws PluginServiceException
  {
    String toolId = "";
    try
    {
      final XmlRpcClient connector = dokuwikiXmlRpcClient.getConnector(
          dokuwikiConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
          dokuwikiConfigurationService.getClientAdmin(), dokuwikiConfigurationService.getClientPwd());

      // Create namespace and start page on dokuwiki
      toolId = createMainPage(connector, pInstanceConfiguration.getConfigurationId(), pPluginProject);

      // Add user to dokuwiki with correct permission
      addUsers(connector, pInstanceConfiguration.getInstanceId(), toolId, pPluginMembership);
    }
    catch (final DokuwikiClientException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to create project with InstanceConfiguration=%s and PluginProject=%s",
          pInstanceConfiguration.toString(), pPluginProject.toString()), e);
    }
    return toolId;
  }

  /**
   * @param pConnector
   * @param pConfigurationId
   * @param pPluginProject
   * @return
   * @throws PluginServiceException
   * @throws DokuwikiClientException
   */
  private String createMainPage(final XmlRpcClient pConnector, final String pConfigurationId,
                                final PluginProject pPluginProject)
      throws PluginServiceException, DokuwikiClientException
  {
    final DokuwikiPage mainPage = dokuwikiResourceBuilder.buildDokuwikiMainPage(pPluginProject, pConfigurationId);

    final String pageId = dokuwikiXmlRpcClient.createPage(pConnector, mainPage);

    return dokuwikiResourceBuilder.buildDokuwikiNameSpace(pageId);
  }

  /**
   * @param pInstanceId
   * @param pToolId
   * @param pPluginMembership
   * @throws PluginServiceException
   * @throws DokuwikiClientException
   */
  private void addUsers(final XmlRpcClient pConnector, final String pInstanceId, final String pProjectToolId,
      final List<PluginMembership> pPluginMembership) throws PluginServiceException, DokuwikiClientException
  {
    for (final PluginMembership pluginMembership : pPluginMembership)
    {
      final String toolRole = pluginRoleMappingService.getToolRole(pInstanceId, pluginMembership.getRole());
      final int toolRoleId = Integer.parseInt(pluginRoleMappingService.getToolRoleId(toolRole));

      final DokuWikiUser user = dokuwikiResourceBuilder.buildDokuWikiUser(pluginMembership.getPluginUser());

      if (!dokuwikiXmlRpcClient.hasUser(pConnector, user.getUserName()))
      {
        dokuwikiXmlRpcClient.createUser(pConnector, user, false);
      }
      dokuwikiXmlRpcClient.setUserPermissionToNameSpace(pConnector, pProjectToolId, user.getUserName(),
          toolRoleId);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean deleteToolProject(final InstanceConfiguration pInstance) throws PluginServiceException
  {
    try
    {

      final XmlRpcClient connector = dokuwikiXmlRpcClient.getConnector(dokuwikiConfigurationService
                                                                           .getClientURL(pInstance.getToolInstance()
                                                                                                  .getBaseURL()),
                                                                       dokuwikiConfigurationService.getClientAdmin(),
                                                                       dokuwikiConfigurationService.getClientPwd());
      final String nameSpace = pInstance.getToolProjectId();
      final String startingPage = dokuwikiResourceBuilder.buildStartingPage(nameSpace);
      dokuwikiXmlRpcClient.deletePage(connector, startingPage);
    }
    catch (final DokuwikiClientException e)
    {
      throw new PluginServiceException(String.format("Unable to delete project with [tool_project_id=%s]",
                                                     pInstance.getToolProjectId()), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void archiveProject(final String pInstanceId) throws PluginServiceException
  {

    // TODO Not implemented yet
    System.out.println("DokuwikiProjectServiceImpl.archiveProject() -> NOT IMPLEMETED YET");

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolProject(final InstanceConfiguration pInstance, final PluginProject pProject)
      throws PluginServiceException
  {

    try
    {
      final DokuwikiPage mainPage = dokuwikiResourceBuilder.buildDokuwikiMainPage(pProject,
          pInstance.getConfigurationId());

      final XmlRpcClient connector = dokuwikiXmlRpcClient.getConnector(
          dokuwikiConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
          dokuwikiConfigurationService.getClientAdmin(), dokuwikiConfigurationService.getClientPwd());
      dokuwikiXmlRpcClient.updatePage(connector, pInstance.getForgeProjectId(), mainPage.getName(),
          mainPage.getDescription());

      final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstance
          .getInstanceId());
      instance.setToolProjectId(mainPage.getName());
    }
    catch (final DokuwikiClientException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to update project with InstanceConfiguration=%s and PluginProject=%s",
          pInstance.toString(), pProject.toString()), e);
    }
    return true;
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
   * Use by container to inject {@link DokuwikiResourceBuilder}
   * 
   * @param pDokuwikiResourceBuilder
   *          the dokuwikiResourceBuilder to set
   */
  public void setDokuwikiResourceBuilder(final DokuwikiResourceBuilder pDokuwikiResourceBuilder)
  {
    dokuwikiResourceBuilder = pDokuwikiResourceBuilder;
  }
}
