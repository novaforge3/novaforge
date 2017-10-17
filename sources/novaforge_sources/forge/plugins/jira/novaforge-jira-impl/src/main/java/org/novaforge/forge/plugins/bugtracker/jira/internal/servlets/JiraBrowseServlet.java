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
package org.novaforge.forge.plugins.bugtracker.jira.internal.servlets;

import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.plugins.bugtracker.jira.services.JiraConfigurationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Default servlet exposed, which will be redirect the user to the correct location on Jira
 * 
 * @author Gauthier Cart
 */
public class JiraBrowseServlet extends HttpServlet
{

  /**
   * Serial version id
   */
  private static final long              serialVersionUID      = 8148851139764501744L;

  /**
   * Instance id parameter
   */
  private static final String            INSTANCE_ID_PARAMETER = "instance_id";
  /**
   * Slash constants
   */
  private static final String            SUFFIX                = "/";
  private final JiraConfigurationService jiraConfigurationService;
  private final InstanceConfigurationDAO instanceConfigurationDAO;

  /**
   * @param pSvnConfigurationService
   * @param pInstanceConfigurationDAO
   */
  public JiraBrowseServlet(final JiraConfigurationService pJiraConfigurationService,
      final InstanceConfigurationDAO pInstanceConfigurationDAO)
  {
    jiraConfigurationService = pJiraConfigurationService;
    instanceConfigurationDAO = pInstanceConfigurationDAO;
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
    if (instanceId == null)
    {
      throw new ServletException(String.format("no parameter \"%s\" found", INSTANCE_ID_PARAMETER));
    }

    final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(instanceId);
    final URL url = getRedirectUrl(instance);
    final String urlWithSessionID = pResp.encodeRedirectURL(url.toExternalForm());
    pResp.sendRedirect(urlWithSessionID);

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
   * @throws MalformedURLException
   *           thrown if the URL built is malformated
   * @throws UnsupportedEncodingException
   *           thrown if the URL built is malformated
   * @throws DataAccessException
   */
  protected URL getRedirectUrl(final InstanceConfiguration pInstanceConfiguration) throws ServletException
  {
    try
    {
      final String instanceAlias = pInstanceConfiguration.getToolInstance().getAlias();
      final StringBuilder redirectAlias = new StringBuilder(instanceAlias);
      final String jiraBrowseAlias = jiraConfigurationService.getJiraBrowseAlias();

      // Remove one "/" if two are present after alias concat
      if ((instanceAlias.endsWith(SUFFIX)) && (jiraBrowseAlias.startsWith(SUFFIX)))
      {
        redirectAlias.append(jiraBrowseAlias.substring(1));
      }
      // Add one "/" if no one is present after alias concat
      else if ((!instanceAlias.endsWith(SUFFIX)) && (!jiraBrowseAlias.startsWith(SUFFIX)))
      {
        redirectAlias.append(SUFFIX);
        redirectAlias.append(jiraBrowseAlias);
      }
      // Just concat
      else
      {
        redirectAlias.append(jiraBrowseAlias);
      }

      // Substitute the Key token and return the Jira URL alias
      return new URL(new URL(jiraConfigurationService.getJiraBaseUrl()), redirectAlias.toString().replace(
          jiraConfigurationService.getJiraBrowseToken(), pInstanceConfiguration.getToolProjectId()));
    }
    catch (final MalformedURLException e)
    {
      throw new ServletException(String.format(
          "A problem occured during process to build redirect url with instance=%s", pInstanceConfiguration),
          e);
    }
  }

}
