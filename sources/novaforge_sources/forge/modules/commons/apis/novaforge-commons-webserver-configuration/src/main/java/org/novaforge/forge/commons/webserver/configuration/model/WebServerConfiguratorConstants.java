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
package org.novaforge.forge.commons.webserver.configuration.model;

public final class WebServerConfiguratorConstants
{
  public final static String WEBSERVER_TOOL_CONFIG_BACKUP_EXT   = ".WebServerBackup";

  public final static String WEBSERVER_TOOL_CONFIG_SAVED_EXT    = ".WebServerSav";

  public final static String WEBSERVER_TOOL_CONFIG_SERVICE_EXT  = ".service";

  public final static String WEBSERVER_DIRECTIVE_INCLUDE        = "Include";

  public final static String WEBSERVER_DESACTIVATED_AUTHTYPE    = "AuthType Basic";

  public final static String WEBSERVER_DESACTIVATED_AUTHNAME    = "AuthName \"Restricted to Administrator\"";

  public final static String WEBSERVER_DESACTIVATED_ACCESSFILE  = "AuthUserFile";

  public final static String WEBSERVER_DESACTIVATED_REQUIRE     = "Require valid-user";

  public final static String WEBSERVER_STOPPED_ORDER            = "Order allow,deny";

  public final static String WEBSERVER_STOPPED_DENY             = "Deny from all";

  public final static String WEBSERVER_DIRECTIVE_LOCATION       = "Location";

  public final static String WEBSERVER_DIRECTIVE_ERRORDOCUMENT  = "ErrorDocument 401";

  public final static String WEBSERVER_ACCESSFILE_SEP           = ":";

  public final static String WEBSERVER_ACCESSFILE_EXT           = "-valid-users";

  public final static String FILE_EOL                           = System.getProperty("line.separator");

  public final static String WEBSERVER_REDIRECTMSG_DESACTIVATED = "This tool is currently desactivated, ask Administrators for more informations.";

  public final static String WEBSERVER_REDIRECTMSG_STOPPED      = "This tool has been stopped by Administrators.";

}