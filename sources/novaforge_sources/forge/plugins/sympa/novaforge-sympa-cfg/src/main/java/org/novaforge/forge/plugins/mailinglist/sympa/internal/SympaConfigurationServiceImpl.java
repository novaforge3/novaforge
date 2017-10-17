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
package org.novaforge.forge.plugins.mailinglist.sympa.internal;

import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.mailinglist.sympa.services.SympaConfigurationService;

import java.net.URL;

/**
 * @author Guillaume Lamirand
 */
public class SympaConfigurationServiceImpl implements SympaConfigurationService
{

  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;

  /**
   * Admin URL of mantis instance
   */
  private String                     adminAccess = "http://127.0.0.1/mantis/login.php";

  /**
   * Listmaster
   */
  private String                     listmaster;

  /**
   * default listname suffix
   */
  private String                     defaultListnameSuffix;

  /**
   * default listname template
   */
  private String                     defaultListnameTemplate;

  /**
   * default listname description
   */
  private String                     defaultListnameDescription;

  /**
   * default listname subject
   */
  private String                     defaultListnameSubject;

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
   * @param pPluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getListmaster()
  {
    return listmaster;
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
   * @param pAdminAccess
   *          the default URI access to set
   */
  public void setAdminAccess(final String pAdminAccess)
  {
    adminAccess = pAdminAccess;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDefaultListnameSuffix()
  {
    return defaultListnameSuffix;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDefaultListnameSubject()
  {
    return defaultListnameSubject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDefaultListnameTemplate()
  {
    return defaultListnameTemplate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDefaultListnameDescription()
  {
    return defaultListnameDescription;
  }

  /**
   * @param defaultListnameDescription
   *          the defaultListnameDescription to set
   */
  public void setDefaultListnameDescription(final String defaultListnameDescription)
  {
    this.defaultListnameDescription = defaultListnameDescription;
  }

  /**
   * @param defaultListnameTemplate
   *          the defaultListnameTemplate to set
   */
  public void setDefaultListnameTemplate(final String defaultListnameTemplate)
  {
    this.defaultListnameTemplate = defaultListnameTemplate;
  }

  /**
   * @param defaultListnameSubject
   *          the defaultListnameSubject to set
   */
  public void setDefaultListnameSubject(final String defaultListnameSubject)
  {
    this.defaultListnameSubject = defaultListnameSubject;
  }

  /**
   * @param defaultListnameSuffix
   *          the defaultListnameSuffix to set
   */
  public void setDefaultListnameSuffix(final String defaultListnameSuffix)
  {
    this.defaultListnameSuffix = defaultListnameSuffix;
  }

  public void setListmaster(final String pListmaster)
  {
    listmaster = pListmaster;
  }

}
