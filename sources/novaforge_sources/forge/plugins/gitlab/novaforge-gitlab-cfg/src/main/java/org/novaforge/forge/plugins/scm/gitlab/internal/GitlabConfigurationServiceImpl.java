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
package org.novaforge.forge.plugins.scm.gitlab.internal;

import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.scm.gitlab.services.GitlabConfigurationService;

import java.net.URL;

/**
 * @author Guillaume Lamirand
 */
public class GitlabConfigurationServiceImpl implements GitlabConfigurationService
{

  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;

  /**
   * Admin URL of gitlab instance
   */
  private String                     adminAccess         = "/gitlabBrowse?admin=@toolUUID";
  /**
   * Cas Auth URL
   */
  private String                     gitLabCasAuthAccess = "/gitlab/users/auth/cas";
  /**
   * Gitlab URL
   */
  private String                     gitLabProjectAccess = "/gitlab";
  /**
   * Gitlab Admin URL
   */
  private String                     gitLabAdminAccess   = "/gitlab/admin";
  /**
   * Gitlab Alias
   */
  private String                     gitLabAlias         = "/gitlab";

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
  public String getAdminAccess()
  {
    return adminAccess;
  }

  public void setAdminAccess(final String pAdminAccess)
  {
    adminAccess = pAdminAccess;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getGitLabProjectAccess()
  {
    return gitLabProjectAccess;
  }

  /**
   * @param pGitLabProjectAccess
   *          the gitLabProjectAccess to set
   */
  public void setGitLabProjectAccess(final String pGitLabProjectAccess)
  {
    gitLabProjectAccess = pGitLabProjectAccess;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getGitLabCasAuthAccess()
  {
    return gitLabCasAuthAccess;
  }

  /**
   * @param pGitLabCasAuthAccess
   *          the gitLabCasAuthAccess to set
   */
  public void setGitLabCasAuthAccess(final String pGitLabCasAuthAccess)
  {
    gitLabCasAuthAccess = pGitLabCasAuthAccess;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getGitLabAdminAccess()
  {
    return gitLabAdminAccess;
  }

  /**
   * @param pGitLabAdminAccess
   *          the gitLabAdminAccess to set
   */
  public void setGitLabAdminAccess(final String pGitLabAdminAccess)
  {
    gitLabAdminAccess = pGitLabAdminAccess;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getGitLabAlias()
  {
    return gitLabAlias;
  }

  /**
   * @param gitLabAlias
   *          the gitLabAlias to set
   */
  public void setGitLabAlias(final String gitLabAlias)
  {
    this.gitLabAlias = gitLabAlias;
  }

}
