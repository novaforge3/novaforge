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
package org.novaforge.forge.plugins.scm.gitlab.internal.servlets;

import org.apache.commons.lang.StringUtils;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.dao.ToolInstanceDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.plugins.scm.gitlab.services.GitlabConfigurationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

/**
 * Default servlet exposed, which will be redirect the user to the correct location on Gitlab
 * 
 * @author Gauthier Cart
 */
public class GitlabBrowseServlet extends HttpServlet
{

  /**
   * Serial version id
   */
  private static final long                serialVersionUID      = -2473885045375635053L;
  /**
   * Instance id parameter
   */
  private static final String              INSTANCE_ID_PARAMETER = "instance_id";
  /**
   * Admin parameter
   */
  private static final String              ADMIN_PARAMETER       = "admin";
  /**
   * Slash constants
   */
  private static final String              SUFFIX                = "/";

  private static final String              DASH                  = "-";

  /**
   * Reference to service implementation of {@link GitlabConfigurationService}
   */
  private final GitlabConfigurationService gitlabConfigurationService;
  /**
   * Reference to service implementation of {@link InstanceConfigurationDAO}
   */
  private final InstanceConfigurationDAO   instanceConfigurationDAO;
  /**
   * Reference to service implementation of {@link ToolInstanceDAO}
   */
  private final ToolInstanceDAO            toolInstanceDAO;

  /**
   * @param pGitlabConfigurationService
   * @param pInstanceConfigurationDAO
   * @param pToolInstanceDAO
   */
  public GitlabBrowseServlet(final GitlabConfigurationService pGitlabConfigurationService,
      final InstanceConfigurationDAO pInstanceConfigurationDAO, final ToolInstanceDAO pToolInstanceDAO)
  {
    gitlabConfigurationService = pGitlabConfigurationService;
    instanceConfigurationDAO = pInstanceConfigurationDAO;
    toolInstanceDAO = pToolInstanceDAO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doGet(final HttpServletRequest pReq, final HttpServletResponse pResp)
      throws ServletException, IOException
  {
    // Get the instanceId
    final String instanceId = pReq.getParameter(INSTANCE_ID_PARAMETER);
    final String admin = pReq.getParameter(ADMIN_PARAMETER);
    if (StringUtils.isNotBlank(instanceId))
    {
      redirectToInstance(pResp, instanceId);
    }
    else if (StringUtils.isNotBlank(admin))
    {
      redirectToAdmin(pResp, admin);
    }
    else
    {
      throw new ServletException(String.format("No parameter '%s' or '%s' found", INSTANCE_ID_PARAMETER,
          ADMIN_PARAMETER));
    }

  }

  /**
   * @param pResp
   * @throws ServletException
   * @throws IOException
   */
  private void redirectToAdmin(final HttpServletResponse pResp, final String pToolUUID)
      throws ServletException, IOException
  {
    final ToolInstance instance = toolInstanceDAO.findInstanceByUUID(UUID.fromString(pToolUUID));
    // Get the uri
    final String adminUri = getGitToolUri(instance);

    // Get the uri
    final URI uri = buildRedirectURI(instance, adminUri);
    pResp.sendRedirect(pResp.encodeRedirectURL(uri.toString()));

  }

  /**
   * @param pInstance
   * @return
   */
  private String getGitToolUri(final ToolInstance pInstance)
  {
    final String instanceAlias = pInstance.getAlias();
    final StringBuilder redirectAlias = new StringBuilder(instanceAlias);
    final String gitLabAlias = gitlabConfigurationService.getGitLabAlias();
    redirectAlias.append(gitLabAlias);
    final String gitLabProjectAccess = gitlabConfigurationService.getGitLabAdminAccess();
    redirectAlias.append(gitLabProjectAccess);
    return redirectAlias.toString();
  }

  private void redirectToInstance(final HttpServletResponse pResp, final String instanceId)
      throws ServletException, IOException
  {
    final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(instanceId);

    // Get the uri
    final String projectUri = getGitProjectUri(instance);

    // Get the uri
    final URI uri = buildRedirectURI(instance.getToolInstance(), projectUri);
    pResp.sendRedirect(pResp.encodeRedirectURL(uri.toString()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doPost(final HttpServletRequest pReq, final HttpServletResponse pResp)
      throws ServletException, IOException
  {
    doGet(pReq, pResp);
  }

  /**
   * This method will construct the redirect url
   * 
   * @param pInstanceConfiguration
   *          the instance concerned
   * @return {@link URL} to redirect user request
   * @throws ServletException
   * @throws ServletException
   *           thrown if the URI built is malformated
   */
  private String getGitProjectUri(final InstanceConfiguration pInstanceConfiguration) throws ServletException
  {
    final String instanceAlias = pInstanceConfiguration.getToolInstance().getAlias();
    final StringBuilder redirectAlias = new StringBuilder(instanceAlias);
    final String gitLabAlias = gitlabConfigurationService.getGitLabAlias();
    redirectAlias.append(gitLabAlias);
    final String gitLabProjectAccess = gitlabConfigurationService.getGitLabProjectAccess();
    redirectAlias.append(gitLabProjectAccess);
    // If instance alias dont end with "/", add one
    if (!gitLabProjectAccess.endsWith(SUFFIX))
    {
      redirectAlias.append(SUFFIX);
    }

    // Build new URI
    redirectAlias.append(pInstanceConfiguration.getForgeProjectId()).append(DASH)
        .append(pInstanceConfiguration.getConfigurationId()).append(SUFFIX)
        .append(pInstanceConfiguration.getConfigurationId());

    return redirectAlias.toString();

  }

  /**
   * @param pProjectUri
   * @return
   * @throws ServletException
   */
  private URI buildRedirectURI(final ToolInstance pToolInstance, final String pProjectUri)
      throws ServletException
  {
    try
    {
      final String instanceAlias = pToolInstance.getAlias();
      final StringBuilder redirectAlias = new StringBuilder(instanceAlias);

      final String gitLabAlias = gitlabConfigurationService.getGitLabAlias();
      redirectAlias.append(gitLabAlias);
      final String gitLabCasAuthAccess = gitlabConfigurationService.getGitLabCasAuthAccess();

      redirectAlias.append(gitLabCasAuthAccess).append(String.format("?url=%s", pProjectUri));

      return new URI(redirectAlias.toString());
    }
    catch (final URISyntaxException e)
    {
      throw new ServletException(String.format(
          "A problem occured during process to build redirect uri with project_uri=%s", pProjectUri), e);
    }
  }

}
