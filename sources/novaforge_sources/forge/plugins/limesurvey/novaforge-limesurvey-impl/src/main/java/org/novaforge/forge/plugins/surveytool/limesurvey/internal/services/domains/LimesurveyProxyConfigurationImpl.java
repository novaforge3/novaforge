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
package org.novaforge.forge.plugins.surveytool.limesurvey.internal.services.domains;

import org.apache.commons.lang.StringUtils;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerConfiguratorConstants;
import org.novaforge.forge.plugins.commons.services.domains.PluginProxyConfigurationImpl;

public class LimesurveyProxyConfigurationImpl extends PluginProxyConfigurationImpl
{

  /**
   * @param pForgePublicUrl 
   * @param pName
   * @param pProxyId
   * @param pPublicAliases
   * @param pRedirectUrl
   */
  public LimesurveyProxyConfigurationImpl(final String pForgePublicUrl, final String pName, 
      final String pProxyId, final String pPublicAliases, final String pRedirectUrl)
  {
    super(pForgePublicUrl, pName, pProxyId, pPublicAliases, pRedirectUrl);
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
      proxyRule.append("<Location ").append(getPublicAlias()).append(">")
          .append(WebServerConfiguratorConstants.FILE_EOL);
      proxyRule.append("\t").append("ProxyPass ").append(getRedirectURL())
          .append(" connectiontimeout=300 timeout=300").append(WebServerConfiguratorConstants.FILE_EOL);
      proxyRule.append("\t").append("ProxyPassReverse ").append(getRedirectURL())
          .append(WebServerConfiguratorConstants.FILE_EOL);
      proxyRule.append("</Location>").append(WebServerConfiguratorConstants.FILE_EOL);
    }
    proxyRule.append(getEndToken()).append(WebServerConfiguratorConstants.FILE_EOL);
    return proxyRule.toString();
  }

}
