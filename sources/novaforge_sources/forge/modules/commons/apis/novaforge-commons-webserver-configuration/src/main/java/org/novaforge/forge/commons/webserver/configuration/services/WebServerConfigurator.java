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
package org.novaforge.forge.commons.webserver.configuration.services;

import org.novaforge.forge.commons.webserver.configuration.exceptions.WebServerConfigurationException;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerProxyConfiguration;

import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 */
public interface WebServerConfigurator
{

  /**
   * WebService name.
   */
  String WEBSERVERCONFIGURATOR_ENDPOINT = "WebServerConfiguratorService";

  /**
   * This method desactive the given Webserver Configuration, Original configuration is backuped
   * 
   * @param pConfName
   *          The WebServer configuration name
   * @param pPublicAlias
   *          The WebServer Public aliases declared
   * @param pSuperAdmins
   *          The SuperAdministrators list : <login,email>
   * @throws WebServerConfigurationException
   */
  void desactivate(final String pConfName, final List<String> pPublicAlias,
      final Map<String, String> pSuperAdmins) throws WebServerConfigurationException;

  /**
   * This method reactivate a previously saved Webserver configuration
   * 
   * @param pConfName
   *          The WebServer configuration name
   * @throws WebServerConfigurationException
   */
  void reactivate(final String pConfName) throws WebServerConfigurationException;

  /**
   * This method reload the WebServer, updated configuration will be reloaded
   * 
   * @throws WebServerConfigurationException
   */
  void reloadWebServer() throws WebServerConfigurationException;

  /**
   * This method stop the given Webserver Configuration, Original configuration is backuped
   * 
   * @param pConfName
   *          The WebServer configuration name
   * @param pPublicAlias
   *          The WebServer Public aliases declared
   * @param pSuperAdmins
   *          The SuperAdministrators list : <login,email>
   * @throws WebServerConfigurationException
   */
  void stop(final String pConfName, final List<String> pPublicAlias) throws WebServerConfigurationException;

  /**
   * This method will add a proxy settings for the given parameters
   * 
   * @param pConfName
   *          The WebServer configuration name
   * @param pProxyId
   *          a value to identify proxy configuration
   * @param pPublicAlias
   *          The WebServer Public alias
   * @param pRedirectUrl
   *          The WebServer redirect alias
   * @param pProxyTemplate
   *          the proxy configuration to used
   * @throws WebServerConfigurationException
   */
  void addProxySettings(final WebServerProxyConfiguration pWebServerProxyConfiguration)
      throws WebServerConfigurationException;

  /**
   * @param pConfName
   * @param pProxyIdentifier
   * @throws WebServerConfigurationException
   */
  void removeProxySettings(final WebServerProxyConfiguration pWebServerProxyConfiguration)
      throws WebServerConfigurationException;

}
