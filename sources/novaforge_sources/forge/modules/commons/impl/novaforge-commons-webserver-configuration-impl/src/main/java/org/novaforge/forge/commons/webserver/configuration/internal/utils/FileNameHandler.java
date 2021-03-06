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

import org.novaforge.forge.commons.webserver.configuration.model.WebServerConfiguration;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerConfiguratorConstants;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Guillaume Lamirand
 */
public class FileNameHandler
{

  public static Path getBackupConfigFileName(final String pWebserverConfFolder,
      final WebServerConfiguration pConf)
  {
    final StringBuilder userPathBuilder = new StringBuilder(pWebserverConfFolder);
    userPathBuilder.append(pConf.getName())
        .append(WebServerConfiguratorConstants.WEBSERVER_TOOL_CONFIG_SERVICE_EXT)
        .append(WebServerConfiguratorConstants.WEBSERVER_TOOL_CONFIG_BACKUP_EXT).toString();
    return Paths.get(userPathBuilder.toString());
  }

  public static Path getSavedConfigFileName(final String pWebserverConfFolder,
      final WebServerConfiguration pConf)
  {
    final StringBuilder userPathBuilder = new StringBuilder(pWebserverConfFolder);
    userPathBuilder.append(pConf.getName())
        .append(WebServerConfiguratorConstants.WEBSERVER_TOOL_CONFIG_SERVICE_EXT)
        .append(WebServerConfiguratorConstants.WEBSERVER_TOOL_CONFIG_SAVED_EXT).toString();
    return Paths.get(userPathBuilder.toString());
  }

  public static Path getConfigFileName(final String pWebserverConfFolder, final WebServerConfiguration pConf)
  {
    final StringBuilder userPathBuilder = new StringBuilder(pWebserverConfFolder);

    userPathBuilder.append(pConf.getName())
        .append(WebServerConfiguratorConstants.WEBSERVER_TOOL_CONFIG_SERVICE_EXT).toString();
    return Paths.get(userPathBuilder.toString());
  }

  public static Path getValidUserFileName(final String pWebserverConfFolder,
      final WebServerConfiguration pConf)
  {
    final StringBuilder userPathBuilder = new StringBuilder(pWebserverConfFolder);
    userPathBuilder.append(pConf.getName()).append(WebServerConfiguratorConstants.WEBSERVER_ACCESSFILE_EXT)
        .toString();
    return Paths.get(userPathBuilder.toString());
  }
}
