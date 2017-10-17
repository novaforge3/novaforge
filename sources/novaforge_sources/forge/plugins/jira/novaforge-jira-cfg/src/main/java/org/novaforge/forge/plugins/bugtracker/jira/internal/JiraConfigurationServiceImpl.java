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
package org.novaforge.forge.plugins.bugtracker.jira.internal;

import org.apache.commons.lang.StringUtils;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestException;
import org.novaforge.forge.plugins.bugtracker.jira.services.JiraConfigurationService;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Guillaume Lamirand
 */
public class JiraConfigurationServiceImpl implements JiraConfigurationService
{

  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;

  /**
   * Define the external alias of jira webapp
   */
  private String                     jiraBrowseAlias       = "/browse/@KEY";

  /**
   * Define the base url where to redirect the user
   */
  private String                     jiraBaseUrl           = "http://127.0.0.1:8080";

  /**
   * Admin URL of jira instance
   */
  private String                     adminAccess           = "http://127.0.0.1:8080/jira/login.jsp";

  /**
   * Define the token to replace dynamically to generate the alias
   */
  private String                     jiraBrowseToken       = "@KEY";

  /**
   * Endpoint used to contact client
   */
  private String                     restClientEndPoint    = "jira/rest/api/latest";

  /**
   * Public port
   */
  private String                     jiraPublicPortNumeric = "8084";

  /**
   * {@inheritDoc}
   * 
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getClientAdmin()
   */
  @Override
  public String getClientAdmin()
  {
    return pluginConfigurationService.getClientAdmin();
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getClientPwd()
   */
  @Override
  public String getClientPwd()
  {
    return pluginConfigurationService.getClientPwd();
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getClientURL(java.net.URL)
   */
  @Override
  public String getClientURL(final URL pURL) throws PluginServiceException
  {
    return pluginConfigurationService.getClientURL(pURL);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getDescription()
   */
  @Override
  public String getDescription()
  {
    return pluginConfigurationService.getDescription();
  }

  /**
   * {@inheritDoc}
   *
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getWebServerConfName()
   */
  @Override
  public String getWebServerConfName()
  {
    return pluginConfigurationService.getWebServerConfName();
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getDefaultAccess()
   */
  @Override
  public String getDefaultAccess()
  {
    return pluginConfigurationService.getDefaultAccess();
  }

  /**
   * {@inheritDoc}
   *
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#isDefaultToolInternal()
   */
  @Override
  public boolean isDefaultToolInternal()
  {
    return pluginConfigurationService.isDefaultToolInternal();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL getDefaultToolURL()
  {
    return pluginConfigurationService.getDefaultToolURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxAllowedProjectInstances()
  {
    return pluginConfigurationService.getMaxAllowedProjectInstances();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getJiraBrowseAlias()
  {
    return jiraBrowseAlias;
  }

  /**
   * @param pJiraBaseUrl
   *          the jiraBaseUrl to set
   */
  public void setJiraBrowseAlias(final String pJiraBrowseAlias)
  {
    jiraBrowseAlias = pJiraBrowseAlias;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getJiraBaseUrl()
  {
    return jiraBaseUrl;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getJiraBrowseToken()
  {
    return jiraBrowseToken;
  }

  /**
   * @param pJiraBaseUrl
   *          the jiraBaseUrl to set
   */
  public void setJiraBrowseToken(final String pJiraBrowseToken)
  {
    jiraBrowseToken = pJiraBrowseToken;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAdminAccess()
  {
    return adminAccess;
  }

  /**
   * {@inheritDoc}
   *
   * @throws JiraRestException
   */
  @Override
  public String getRestClientURL(final URL pBaseUrl) throws JiraRestException
  {
    try
    {
      return new URL(pBaseUrl, restClientEndPoint).toString();
    }
    catch (final MalformedURLException e)
    {
      throw new JiraRestException(String.format("Unable to get rest client URL with [base_url=%s, end_point=%s]",
                                                pBaseUrl, restClientEndPoint), e);
    }
  }

  @Override
  public int getJiraPublicPort()
  {
    return Integer.valueOf(jiraPublicPortNumeric);
  }

  /**
   * @param pAdminAccess
   *          the default URI access to set
   */
  public void setAdminAccess(final String pAdminAccess)
  {
    adminAccess = pAdminAccess;
  }

  /**
   * @param pJiraBaseUrl
   *          the jiraBaseUrl to set
   */
  public void setJiraBaseUrl(final String pJiraBaseUrl)
  {
    jiraBaseUrl = pJiraBaseUrl;

  }

  /**
   * @param pPluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }

  /**
   * @return the restClientEndPoint
   */
  public String getRestClientEndPoint()
  {
    return restClientEndPoint;
  }

  /**
   * @param pPluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setRestClientEndPoint(final String pRestClientEndPoint)
  {
    restClientEndPoint = pRestClientEndPoint;
  }

  /**
   * @param publicPort
   *          the public port to set
   */
  public void setJiraPublicPortNumeric(final String pJiraPublicPortNumeric)
  {

    if (StringUtils.isNumeric(pJiraPublicPortNumeric))
    {
      jiraPublicPortNumeric = pJiraPublicPortNumeric;
    }
    else
    {
      throw new IllegalArgumentException(String.format(
          "Jira public port has to be a integer [public_port=%s]", pJiraPublicPortNumeric));
    }

  }
}
