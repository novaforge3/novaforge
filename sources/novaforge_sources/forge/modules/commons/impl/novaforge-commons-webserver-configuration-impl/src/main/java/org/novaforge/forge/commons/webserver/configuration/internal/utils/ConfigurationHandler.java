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
package org.novaforge.forge.commons.webserver.configuration.internal.utils;

/**
 * @author Guillaume Lamirand
 */
public class ConfigurationHandler
{

  private static String SERVICES_FOLDER;

  private static String RELOAD_CMD;

  /**
   * @return the webserverConfFolder
   */
  public static String getServicesFolder()
  {
    return SERVICES_FOLDER;
  }

  /**
   * @param pServicesFolder
   *          the webserverConfFolder to set
   */
  public static void setConfFolder(final String pServicesFolder)
  {
    SERVICES_FOLDER = pServicesFolder;
  }

  /**
   * @return the webserverReloadCmd
   */
  public static String getReloadCmd()
  {
    return RELOAD_CMD;
  }

  /**
   * @param pReloadCmd
   *          the webserverReloadCmd to set
   */
  public static void setReloadCmd(final String pReloadCmd)
  {
    RELOAD_CMD = pReloadCmd;
  }
}
