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
package org.novaforge.forge.plugins.commons.services.domains;

import org.apache.commons.lang.StringUtils;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerConfiguratorConstants;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerProxyConfiguration;

public class PluginProxyConfigurationImpl implements WebServerProxyConfiguration
{

  protected static final String SUFFIX = "/";
  private final String          name;
  private final String          identifier;
  private String forgePublicUrl;
  private String                publicAlias;
  private String                redirectUrl;

  /**
   * @param pForgePublicUrl
   * @param pName
   * @param pProxyId
   * @param pPublicAliases
   * @param pRedirectUrl
   */
  public PluginProxyConfigurationImpl(final String pForgePublicUrl, final String pName,
      final String pProxyId, final String pPublicAliases, final String pRedirectUrl)
  {
    super();
    if (StringUtils.isNotBlank(pForgePublicUrl))
    {
      forgePublicUrl = pForgePublicUrl.replaceAll("(.*)://(.*)", "http://$2");
    }
    name = pName;
    identifier = pProxyId;

    // Remove / at the end
    if ((pPublicAliases != null) && (pPublicAliases.endsWith(SUFFIX)))
    {
      publicAlias = pPublicAliases.substring(0, pPublicAliases.length() - 1);
    }
    else
    {
      publicAlias = pPublicAliases;

    }
    // Remove / at the end
    if ((pRedirectUrl != null) && (pRedirectUrl.endsWith(SUFFIX)))
    {
      redirectUrl = pRedirectUrl.substring(0, pRedirectUrl.length() - 1);
    }
    else
    {
      redirectUrl = pRedirectUrl;
    }
  }

  /**
   * @return the name
   */
  @Override
  public String getName()
  {
    return name;
  }

  protected String getIdentifier()
  {
    return identifier;
  }

  protected String getPublicAlias()
  {
    return publicAlias;
  }

  protected String getRedirectURL()
  {
    return redirectUrl;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "PluginProxyConfigurationImpl [name=" + name + ", identifier=" + identifier + ", publicAlias=" + publicAlias
               + ", redirectUrl=" + redirectUrl + ", getProxySettings()=" + getProxySettings() + "]";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProxySettings()
  {

    final StringBuilder proxyRule = new StringBuilder();
    proxyRule.append(getStartToken()).append(WebServerConfiguratorConstants.FILE_EOL);
    if ((StringUtils.isNotBlank(identifier)) && (StringUtils.isNotBlank(redirectUrl))
        && (StringUtils.isNotBlank(publicAlias)))
    {
      proxyRule.append("<Location ").append(publicAlias).append(">")
          .append(WebServerConfiguratorConstants.FILE_EOL);
      proxyRule.append("\t").append("ProxyPass ").append(redirectUrl)
          .append(WebServerConfiguratorConstants.FILE_EOL);
      proxyRule.append("\t").append("ProxyPassReverse ").append(redirectUrl)
          .append(WebServerConfiguratorConstants.FILE_EOL);

      // Rewrite redirect url using public url
      String publicRedirect = redirectUrl;

      // Delete port from redirect url
      final String redirectUrlWithoutPort = redirectUrl.replaceAll("(.*)://(.*):[\\d]*/(.*)", "$1://$2/$3");
      if (!redirectUrl.equals(redirectUrlWithoutPort))
      {
        proxyRule.append("\t").append("ProxyPassReverse ").append(redirectUrlWithoutPort)
            .append(WebServerConfiguratorConstants.FILE_EOL);
      }
      if (StringUtils.isNotBlank(forgePublicUrl))
      {
        publicRedirect = redirectUrl.replaceAll("(.*)://(.*)/(.*)", String.format("%s/$3", forgePublicUrl));
        proxyRule.append("\t").append("ProxyPassReverse ").append(publicRedirect)
            .append(WebServerConfiguratorConstants.FILE_EOL);
      }
      proxyRule.append("</Location>").append(WebServerConfiguratorConstants.FILE_EOL);
    }
    proxyRule.append(getEndToken()).append(WebServerConfiguratorConstants.FILE_EOL);
    return proxyRule.toString();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getStartToken()
  {
    return String.format("## Start %s", identifier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getEndToken()
  {
    return String.format("## End %s", identifier);

  }

}
