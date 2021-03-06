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
package org.novaforge.forge.plugins.bugtracker.jira.internal.services;

import org.novaforge.forge.commons.webserver.configuration.model.WebServerProxyConfiguration;
import org.novaforge.forge.plugins.bugtracker.jira.internal.services.domains.JiraProxyConfigurationImpl;
import org.novaforge.forge.plugins.bugtracker.jira.services.JiraConfigurationService;
import org.novaforge.forge.plugins.commons.services.DefaultToolInstanceProvider;

/**
 * @author Guillaume Lamirand
 */
public class JiraToolInstanceProvider extends DefaultToolInstanceProvider
{

  /**
   * @param pWebServerName
   * @param pProxyId
   * @param pPublicAliases
   * @param pRedirectUrl
   * @return
   */
  @Override
  protected WebServerProxyConfiguration getProxyConfiguration(final String pWebServerName,
      final String pProxyId, final String pPublicAliases, final String pRedirectUrl)
  {
    final int publicPort = ((JiraConfigurationService) getPluginConfigurationService()).getJiraPublicPort();
    return new JiraProxyConfigurationImpl(getForgeConfigurationService().getPublicUrl().toExternalForm(),
        pWebServerName, pProxyId, pPublicAliases, pRedirectUrl, publicPort, getServletUrl());

  }

  private String getServletUrl()
  {
    final String localHostName = getForgeConfigurationService().getLocalHostName();
    final String localPort = getForgeConfigurationService().getLocalPort();
    return String.format("http://%s:%s", localHostName, localPort);
  }

}
