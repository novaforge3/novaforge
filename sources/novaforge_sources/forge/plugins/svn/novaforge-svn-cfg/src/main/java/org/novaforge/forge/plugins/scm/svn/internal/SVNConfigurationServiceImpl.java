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
package org.novaforge.forge.plugins.scm.svn.internal;

import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.scm.svn.services.SVNConfigurationService;

import java.net.URL;

/**
 * @author Guillaume Lamirand
 */
public class SVNConfigurationServiceImpl implements SVNConfigurationService
{

  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;

  /**
   * Define the base url where to redirect the user
   */
  private String                     svnWebBrowserBaseUrl = "http://localhost:9000";
  /**
   * Define the external alias of svn web client webapp
   */
  private String                     svnWebBrowserAlias   = "/svnwebclient";

  /**
   * {@inheritDoc}
   * 
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getClientAdmin()
   */
  @Override
  public String getClientAdmin()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getClientPwd()
   */
  @Override
  public String getClientPwd()
  {
    throw new UnsupportedOperationException();
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
  public String getSvnWebBrowserAlias()
  {
    return svnWebBrowserAlias;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSvnWebBrowserBaseUrl()
  {
    return svnWebBrowserBaseUrl;

  }

  /**
   * @param pSvnWebBrowserBaseUrl
   *          the svnWebBrowserBaseUrl to set
   */
  public void setSvnWebBrowserBaseUrl(final String pSvnWebBrowserBaseUrl)
  {
    svnWebBrowserBaseUrl = pSvnWebBrowserBaseUrl;

  }

  /**
   * @param pSvnWebBrowserAlias
   *          the svnWebBrowserAlias to set
   */
  public void setSvnWebBrowserAlias(final String pSvnWebBrowserAlias)
  {
    svnWebBrowserAlias = pSvnWebBrowserAlias;

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
