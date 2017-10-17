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
package org.novaforge.forge.plugins.cms.spip.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.cms.spip.services.SpipConfigurationService;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This is a class which contains configuration used to connect to spip instance.
 * 
 * @author Guillaume Lamirand, blachonm
 */
public class SpipConfigurationImpl implements SpipConfigurationService
{

  /**
   * Logger instanciation
   */
  private static final Log LOG = LogFactory.getLog(SpipConfigurationImpl.class);
  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;
  /**
   * Site url
   */
  private String siteUrl      = "/spip/@spipProject@";
  /**
   * Used to replace project id in the endpoint URL
   */
  private String projectToken = "@spipProject@";

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
    throw new UnsupportedOperationException("You have to use getClientURL(URL,pProjectId) instead");
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
  public String getProjectToken()
  {
    return projectToken;
  }

  /**
   * @param pProjectToken
   */
  public void setProjectToken(final String pProjectToken)
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug(String.format("Update project token pwd configuration from '%s' to '%s'.", projectToken,
                              pProjectToken));
    }
    projectToken = pProjectToken;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSiteUrl(final URL pBaseUrl, final String pSpipProjectId) throws PluginServiceException
  {
    try
    {
      final String projectUrl = siteUrl.replace(projectToken, pSpipProjectId);
      return new URL(pBaseUrl, projectUrl).toString();
    }
    catch (final MalformedURLException e)
    {
      throw new PluginServiceException(String.format("Unable to get client URL with [base_url=%s, end_point=%s]",
                                                     siteUrl, siteUrl), e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getClientURL(final URL pBaseUrl, final String pSpipProjectId) throws PluginServiceException
  {
    final String clientURL       = pluginConfigurationService.getClientURL(pBaseUrl);
    return clientURL.replace(projectToken, pSpipProjectId);
  }

  /**
   * @param pSiteUrl
   */
  public void setSiteUrl(final String pSiteUrl)
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug(String.format("Update administrator pwd configuration from '%s' to '%s'.", siteUrl, pSiteUrl));
    }
    if (pSiteUrl.startsWith("/"))
    {
      siteUrl = pSiteUrl.substring(1);
    }
    else
    {
      siteUrl = pSiteUrl;
    }

  }
  
  /**
   * @param pPluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }

}
