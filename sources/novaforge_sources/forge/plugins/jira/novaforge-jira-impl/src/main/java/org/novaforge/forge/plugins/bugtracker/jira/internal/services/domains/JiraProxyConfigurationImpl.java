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
package org.novaforge.forge.plugins.bugtracker.jira.internal.services.domains;

import org.apache.commons.lang.StringUtils;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerConfiguratorConstants;
import org.novaforge.forge.plugins.bugtracker.jira.internal.servlets.JiraBrowse;
import org.novaforge.forge.plugins.commons.services.domains.PluginProxyConfigurationImpl;

public class JiraProxyConfigurationImpl extends PluginProxyConfigurationImpl
{

  private static final String JIRA = "/jira";
  private final String        servletUrl;
  private final int           publicPort;

  /**
   * @param pForgePublicUrl
   * @param pName
   * @param pProxyId
   * @param pPublicAliases
   * @param pRedirectUrl
   * @param pPublicPort
   * @param pServletUrl
   */
  public JiraProxyConfigurationImpl(final String pForgePublicUrl, final String pName, final String pProxyId,
      final String pPublicAliases, final String pRedirectUrl, final int pPublicPort, final String pServletUrl)
  {
    super(pForgePublicUrl, pName, pProxyId, pPublicAliases, pRedirectUrl);

    // Remove / at the end
    if ((pServletUrl != null) && (pServletUrl.endsWith(SUFFIX)))
    {
      servletUrl = pServletUrl.substring(0, pServletUrl.length() - 1);
    }
    else
    {
      servletUrl = pServletUrl;
    }

    // Set public port
    publicPort = pPublicPort;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProxySettings()
  {
    final StringBuilder proxyRule = new StringBuilder();
    proxyRule.append(getStartToken()).append(WebServerConfiguratorConstants.FILE_EOL);
    if ((StringUtils.isNotBlank(getIdentifier())) && (StringUtils.isNotBlank(getPublicAlias()))
        && (StringUtils.isNotBlank(getRedirectURL())))
    {
      // Servlet alias
      proxyRule.append("<Location ").append(getPublicAlias()).append(JiraBrowse.JIRA_ALIAS_SERVLET)
          .append(">").append(WebServerConfiguratorConstants.FILE_EOL);
      proxyRule.append("\t").append("ProxyPass ").append(servletUrl).append(JiraBrowse.JIRA_ALIAS_SERVLET)
          .append(WebServerConfiguratorConstants.FILE_EOL);
      proxyRule.append("\t").append("ProxyPassReverse ").append(servletUrl)
          .append(JiraBrowse.JIRA_ALIAS_SERVLET).append(WebServerConfiguratorConstants.FILE_EOL);
      proxyRule.append("</Location>").append(WebServerConfiguratorConstants.FILE_EOL);

      // Tool alias
      proxyRule.append("<Location ").append(getPublicAlias()).append(JIRA).append(">")
          .append(WebServerConfiguratorConstants.FILE_EOL);

      final String redirectUrlOnPublicPort = getRedirectURL().replaceAll("(.*)://(.*):[\\d]*/(.*)",
          "$1://$2:" + publicPort + "/$3");
      proxyRule.append("\t").append("ProxyPass ").append(redirectUrlOnPublicPort).append(JIRA)
          .append(WebServerConfiguratorConstants.FILE_EOL);
      proxyRule.append("\t").append("ProxyPassReverse ").append(redirectUrlOnPublicPort).append(JIRA)
          .append(WebServerConfiguratorConstants.FILE_EOL);

      final String redirectUrlWithoutPort = getRedirectURL().replaceAll("(.*)://(.*):[\\d]*/(.*)",
          "$1://$2/$3");
      if (!getRedirectURL().equals(redirectUrlWithoutPort))
      {
        proxyRule.append("\t").append("ProxyPassReverse ").append(redirectUrlWithoutPort).append(JIRA)
            .append(WebServerConfiguratorConstants.FILE_EOL);
      }
      proxyRule.append("</Location>").append(WebServerConfiguratorConstants.FILE_EOL);
    }
    proxyRule.append(getEndToken()).append(WebServerConfiguratorConstants.FILE_EOL);
    return proxyRule.toString();
  }
}
