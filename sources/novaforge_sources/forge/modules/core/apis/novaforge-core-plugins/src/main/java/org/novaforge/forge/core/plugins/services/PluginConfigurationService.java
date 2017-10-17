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
package org.novaforge.forge.core.plugins.services;

import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;

import java.net.URL;

/**
 * This interface describes a default service used to configure plugin
 * 
 * @author Guillaume Lamirand
 */
public interface PluginConfigurationService
{

  /**
   * This will return the administrator login used to contact client instance
   * 
   * @return the administrator login
   */
  String getClientAdmin();

  /**
   * This will return the administrator password used to contact client instance
   * 
   * @return the administrator password
   */
  String getClientPwd();

  /**
   * This will return a full url used to contact client instance
   * 
   * @param pBaseUrl
   *          instance base url
   * @return the full contact url
   * @throws PluginServiceException
   *           if any errors occured to build target URL
   */
  String getClientURL(final URL pBaseUrl) throws PluginServiceException;

  /**
   * This will return the plugin descrition
   * 
   * @return the plugin description
   */
  String getDescription();

  /**
   * This will set the web server name
   * 
   * @return the server name
   */
  String getWebServerConfName();

  /**
   * This will return the default plugin access
   * 
   * @return the default access
   */
  String getDefaultAccess();

  /**
   * Returns <code>true</code> if default tool instance is internal
   * 
   * @return the defaultToolInternal value
   */
  boolean isDefaultToolInternal();

  /**
   * Return URL defined for the default tool instance
   * 
   * @return {@link URL} has to end with /
   */
  URL getDefaultToolURL();

  /**
   * Return the numbe of max allowed project instances for the plugin (-1 for no limit)
   * 
   * @return int
   */
  int getMaxAllowedProjectInstances();
}
