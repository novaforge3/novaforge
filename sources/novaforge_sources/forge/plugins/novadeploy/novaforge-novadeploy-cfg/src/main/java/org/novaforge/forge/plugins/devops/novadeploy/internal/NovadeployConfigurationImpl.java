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
package org.novaforge.forge.plugins.devops.novadeploy.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.devops.novadeploy.services.NovadeployConfigurationService;

import java.net.URL;

/**
 * This is a class which contains configuration used to connect to Novadeploy instance.
 * 
 * @author 
 */
public class NovadeployConfigurationImpl implements NovadeployConfigurationService
{
  /**
   * Logger instanciation
   */
  private static final Log LOGGER = LogFactory.getLog(NovadeployConfigurationImpl.class);
  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;


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
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getClientURL()
   */
  @Override
  public String getClientURL(final URL pBaseUrl) throws PluginServiceException
  {
    return pluginConfigurationService.getClientURL(pBaseUrl);
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
   *
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getDefaultToolURL()
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
   * Use by container to inject {@link PluginConfigurationService}
   *
   * @param pPluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }



}
