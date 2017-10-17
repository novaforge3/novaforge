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
package org.novaforge.forge.plugins.commons.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This default implementation define a generic way to implement {@link PluginConfigurationService} Interface.
 * 
 * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService
 * @author Guillaume Lamirand
 */
public class DefaultPluginConfigurationService implements PluginConfigurationService
{

  /**
   * Instantiate logger
   */
  private static final Log LOGGER = LogFactory.getLog(DefaultPluginConfigurationService.class);
  /**
   * Administrator login used to contact client
   */
  private String clientAdmin;
  /**
   * Administrator pwd used to contact client
   */
  private String clientPwd;
  /**
   * Endpoint used to contact client
   */
  private String clientEndPoint;
  /**
   * Description of plugin
   */
  private String description = "I am a plugin for everyone";
  /**
   * Tool URI of plugin instance
   */
  private String  defaultAccess;
  /**
   * WebServer Configuration name
   */
  private String  webServerConfName;
  /**
   * Define if the default instance is internal
   */
  private boolean defaultToolInternal;
  /**
   * This is the default tool URL
   */
  private URL     defaultToolURL;
  /**
   * This is the default tool URL as string value
   */
  private String  defaultToolURLString;
  /**
   * This is the max allowed project instances
   */
  private int     maxAllowedProjectInstances;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getClientAdmin()
  {
    return clientAdmin;
  }

  /**
   * @param pAdmin
   */
  public void setClientAdmin(final String pAdmin)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update administrator user configuration from '%s' to '%s'.", clientAdmin, pAdmin));
    }
    clientAdmin = pAdmin;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getClientPwd()
  {
    return clientPwd;
  }

  /**
   * @param pPassword
   */
  public void setClientPwd(final String pPassword)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update administrator pwd configuration from '%s' to '%s'.", clientPwd, pPassword));
    }
    clientPwd = pPassword;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getClientURL(final URL pBaseUrl) throws PluginServiceException
  {
    try
    {
      return new URL(pBaseUrl, clientEndPoint).toString();
    }
    catch (final MalformedURLException e)
    {
      throw new PluginServiceException(String.format("Unable to get client URL with [base_url=%s, end_point=%s]",
                                                     pBaseUrl, clientEndPoint), e);
    }
  }

  /**
   * @return the description
   */
  @Override
  public String getDescription()
  {
    return description;
  }

  /**
   * @param pDescription
   *     the description to set
   */
  public void setDescription(final String pDescription)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update description configuration from '%s' to '%s'.", description, pDescription));
    }
    description = pDescription;
  }

  /**
   * @return the webServerConfName
   */
  @Override
  public String getWebServerConfName()
  {
    return webServerConfName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDefaultAccess()
  {
    return defaultAccess;
  }

  /**
   * @param pDefaultAccess
   *          the default URI access to set
   */
  public void setDefaultAccess(final String pDefaultAccess)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update default access configuration from '%s' to '%s'.", defaultAccess,
          pDefaultAccess));
    }
    defaultAccess = pDefaultAccess;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDefaultToolInternal()
  {
    return defaultToolInternal;
  }

  /**
   * @param defaultToolInternal
   *          the defaultToolInternal to set
   */
  public void setDefaultToolInternal(final boolean defaultToolInternal)
  {
    this.defaultToolInternal = defaultToolInternal;
  }

  @Override
  public URL getDefaultToolURL()
  {
    return defaultToolURL;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxAllowedProjectInstances()
  {
    return maxAllowedProjectInstances;
  }

  /**
   * @param pWebServerConfName
   *          the webServerConfName to set
   */
  public void setMaxAllowedProjectInstances(final int pMaxAllowedProjectInstances)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update max allowed project instances from '%s' to '%s'.", maxAllowedProjectInstances,
                                 pMaxAllowedProjectInstances));
    }
    maxAllowedProjectInstances = pMaxAllowedProjectInstances;
  }

  /**
   * @param pWebServerConfName
   *          the webServerConfName to set
   */
  public void setWebServerConfName(final String pWebServerConfName)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update web server configuration from '%s' to '%s'.", webServerConfName,
          pWebServerConfName));
    }
    webServerConfName = pWebServerConfName;
  }

  /**
   * @param pEndpoint
   */
  public void setClientEndPoint(final String pEndpoint)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update client endpoint configuration from '%s' to '%s'.", clientEndPoint, pEndpoint));
    }
    if (pEndpoint.startsWith("/"))
    {
      clientEndPoint = pEndpoint.substring(1);
    }
    else
    {
      clientEndPoint = pEndpoint;
    }

  }

  /**
   * @param pDefaultToolURL
   *          the defaultURL to set
   */
  public void setDefaultToolURLString(final String pDefaultToolURL)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update default tool url configuration from '%s' to '%s'.", defaultToolURLString,
                                 pDefaultToolURL));
    }
    defaultToolURLString = pDefaultToolURL;
    if ((defaultToolURLString != null) && (!"".equals(defaultToolURLString)))
    {
      try
      {
        final StringBuilder url = new StringBuilder(defaultToolURLString);
        if (!defaultToolURLString.endsWith("/"))
        {
          url.append("/");
        }
        defaultToolURL = new URL(url.toString());
        LOGGER.debug(String.format("DefaultToolURL of plugin '%s' has been changed from '%s' to '%s'.",
                                   this.getClass().getName(), defaultToolURL, defaultToolURLString));
      }
      catch (final MalformedURLException e)
      {
        LOGGER.error(String.format("Unable to build an URL with '%s'.", defaultToolURLString), e);
      }
    }
    else
    {
      defaultToolURL = null;
    }
  }
}
