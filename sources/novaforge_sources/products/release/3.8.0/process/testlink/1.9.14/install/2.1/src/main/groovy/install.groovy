/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */

import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine
import org.novaforge.beaver.deployment.plugin.deploy.engine.RepositoryService
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

BeaverEngine beaverEngine = engine

def home = beaverEngine.getResource("home")
def datas = beaverEngine.getResource("datas")
def logs = beaverEngine.getResource("logs")
def localGroup = beaverEngine.getResource("httpd", "group")
def localUser = beaverEngine.getResource("httpd", "user")

// Testlink directories : home, logs and datas
Path homePath = Paths.get(home)
if (Files.exists(homePath))
{
  beaverEngine.delete(home)  
}
Path logsPath = Paths.get(logs)
if (Files.exists(logsPath))
{
  beaverEngine.delete(logs)  
}
beaverEngine.createDirectory(logs)
Path datasPath = Paths.get(datas)
if (Files.exists(datasPath))
{
  beaverEngine.delete(datas)  
}
beaverEngine.createDirectory(datas)

// Unpack the data archive
beaverEngine.unpackFile(dataFile, processTmpPath)
def testlinkArchive = processTmpPath + "/testlink-1.9.14.tar.gz"

// Install testlink engine
def localEngines = beaverEngine.getResource("local:engines")
beaverEngine.unpackFile(testlinkArchive, localEngines)
beaverEngine.moveDir(localEngines + "/testlink-1.9.14", home)

// Moving upload_area directory
Path upareaPath = Paths.get(home + "/upload_area")
if (Files.exists(upareaPath))
{
  beaverEngine.move(home + "/upload_area", datas)
}
else 
{
  beaverEngine.createDirectory(datas + "/upload_area")
}

// Delete unused files
beaverEngine.delete(home + "/custom_config.inc.php.example") 
// install : All deleted except img and info
def tmpInstallNew = home + "/install.NEW"
beaverEngine.createDirectory(tmpInstallNew)
beaverEngine.move(home + "/install/img", tmpInstallNew)
beaverEngine.move(home + "/install/info", tmpInstallNew)
beaverEngine.delete(home + "/install")
beaverEngine.move(tmpInstallNew,home + "/install") 
// locale samples
beaverEngine.delete(home + "/locale/en_GB/custom_strings.txt.example")
beaverEngine.delete(home + "/locale/en_US/custom_strings.txt.example")
beaverEngine.delete(home + "/locale/fr_FR/custom_strings.txt.example")
// default logs directory
beaverEngine.delete(home + "/logs")

// Execute patch
beaverEngine.patch(processTmpPath + "/resources/patch/testlink-00.patch", 1, home, false)

// Copy files
// cfg
beaverEngine.copy(processTmpPath + "/resources/files/cfg/novaforge.cfg.php", home + "/cfg")
// gui
beaverEngine.copy(processTmpPath + "/resources/files/gui/forge_notification.js", home + "/gui/javascript")
beaverEngine.copy(processTmpPath + "/resources/files/gui/forge_project_context.js", home + "/gui/javascript")
beaverEngine.copy(processTmpPath + "/resources/files/gui/novaforge_gray.png", home + "/gui/themes/default/images")
beaverEngine.copy(processTmpPath + "/resources/files/gui/novaforge.png", home + "/gui/themes/default/images")
beaverEngine.copyToFile(home + "/gui/themes/default/css/custom.css.example", home + "/gui/themes/default/css/custom.css")
// home
beaverEngine.copy(processTmpPath + "/resources/files/home/custom_config.inc.php", home)
beaverEngine.copy(processTmpPath + "/resources/files/home/forge_api.php", home)
// lib
beaverEngine.copy(processTmpPath + "/resources/files/lib/sendNotification.php", home + "/lib/execute")
beaverEngine.createDirectory(home + "/lib/functions/novaforge")
beaverEngine.copy(processTmpPath + "/resources/files/lib/functions.php", home + "/lib/functions/novaforge")
// locale
beaverEngine.copyToFile(processTmpPath + "/resources/files/locale/en_GB_custom_strings.txt", home + "/locale/en_GB/custom_strings.txt")
beaverEngine.copyToFile(processTmpPath + "/resources/files/locale/en_US_custom_strings.txt", home + "/locale/en_US/custom_strings.txt")
beaverEngine.copyToFile(processTmpPath + "/resources/files/locale/fr_FR_custom_strings.txt", home + "/locale/fr_FR/custom_strings.txt")

// Unzip NuSOAP library for PHP
beaverEngine.createDirectory(home + "/lib/nusoap")
beaverEngine.unpackFile(processTmpPath + "/resources/files/nusoap-0.9.5-f12312.zip", home + "/lib/nusoap")

// Create symbolic link for xmlrpc.class.php and xmlrpc.php files
// because moved to lib/api/xmlrpc/v1 from 1.9.3 to 1.9.14 (-> avoid forge plugin update)
beaverEngine.createSymlink(home + "/lib/api/xmlrpc/v1/xmlrpc.class.php", home + "/lib/api/xmlrpc.class.php")
beaverEngine.createSymlink(home + "/lib/api/xmlrpc/v1/xmlrpc.php", home + "/lib/api/xmlrpc.php")

// Set owner and rights
beaverEngine.setOwner(true, localGroup, localUser, home)
beaverEngine.setOwner(true, localGroup, localUser, logs)
beaverEngine.setOwner(true, localGroup, localUser, datas)

// Create database testlink
def sqlFile = processTmpPath + "/resources/sql/create_database.sql"
def dumpFile = processTmpPath + "/resources/sql/dump_data.sql"
def sqlUser = beaverEngine.getResource("sqlUser")
def sqlPwd = beaverEngine.getResource("sqlPwd")
def mariadbHost = beaverEngine.getResource("mariadb", "host")
def database = beaverEngine.getResource("database")
beaverEngine.replaceElement(sqlFile, "@TESTLINK_USER@", sqlUser)
beaverEngine.replaceElement(sqlFile, "@TESTLINK_PWD@", sqlPwd)
beaverEngine.replaceElement(sqlFile, "@MARIADB_HOST@", mariadbHost)
beaverEngine.replaceElement(sqlFile, "@DATABASE@", database)

def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def mariadbUser = beaverEngine.getResource("mariadb", "rootUser")
def mariadbPwd = beaverEngine.getResource("mariadb", "rootPwd")
def mariadbPort = beaverEngine.getResource("mariadb", "port")
def mariadbBin = beaverEngine.getResource("mariadb", "bin")
SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.startService(mariadbService)
beaverEngine.executeMysqlScript(mariadbBin,mariadbPort, mariadbUser, mariadbPwd, sqlFile)
beaverEngine.executeMysqlScript(mariadbBin,mariadbPort, mariadbUser, mariadbPwd, dumpFile)
systemdService.stopService(mariadbService)
