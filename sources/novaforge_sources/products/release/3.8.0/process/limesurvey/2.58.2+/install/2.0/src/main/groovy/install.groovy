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
def localGroup = beaverEngine.getResource("httpd", "group")
def localUser = beaverEngine.getResource("httpd", "user")

// Unpack the data archive
beaverEngine.unpackFile(dataFile, processTmpPath)
def limesurveyArchive = processTmpPath + "/" + "limesurvey2.58.2plus-170114.tar.gz"

// Install limesurvey engine
Path homePath = Paths.get(home)
if (Files.exists(homePath))
{
  beaverEngine.delete(home)  
}
def localEngines = beaverEngine.getResource("local:engines")
beaverEngine.unpackFile(limesurveyArchive,localEngines)

// Execute Limesurvey patch
beaverEngine.patch(processTmpPath + "/resources/patch/limesurvey-00.patch", 1, home, false)

// Delete unused files
// - Install programs
beaverEngine.delete(home + "/installer/sql")
beaverEngine.delete(home + "/application/commands/InstallCommand.php")
beaverEngine.delete(home + "/application/commands/ResetPasswordCommand.php")
beaverEngine.delete(home + "/application/views/installer")
beaverEngine.delete(home + "/application/controllers/InstallerController.php")

// Copy files
// config
beaverEngine.copy(processTmpPath + "/resources/files/conf/forge.conf.admin.php", home + "/application/config")
// extensions
beaverEngine.createDirectory(home + "/application/extensions/admin/novaforge")
beaverEngine.copy(processTmpPath + "/resources/files/novaforge_functions.php", home + "/application/extensions/admin/novaforge")
// Unzip NuSOAP library for PHP
beaverEngine.createDirectory(home + "/application/libraries/nusoap")
beaverEngine.unpackFile(processTmpPath + "/resources/files/lib/nusoap-0.9.5-f12312.zip", home + "/application/libraries/nusoap")
// Deploy plugin CAS
beaverEngine.createDirectory(home + "/plugins/AuthCAS")
beaverEngine.unpackFile(processTmpPath + "/resources/files/plugin/AuthCAS.zip", home + "/plugins/AuthCAS")

// Execute AuthCAS patch
beaverEngine.patch(processTmpPath + "/resources/patch/AuthCAS-00.patch", 1, home + "/plugins/AuthCAS", false)

// Set owner and rights
beaverEngine.setOwner(true, localGroup, localUser, home)

// Create database limesurvey
def sqlFile = processTmpPath + "/resources/sql/create_database.sql"
def sqlUser = beaverEngine.getResource("sqlUser")
def sqlPwd = beaverEngine.getResource("sqlPwd")
def mariadbHost = beaverEngine.getResource("mariadb", "host")
def database = beaverEngine.getResource("database")
def localhost = beaverEngine.getResource("local:host")
def casPort = beaverEngine.getResource("cas", "externalPort")
def casURI = beaverEngine.getResource("cas", "uri")
beaverEngine.replaceElement(sqlFile, "@LIMESURVEY_USER@", sqlUser)
beaverEngine.replaceElement(sqlFile, "@LIMESURVEY_PWD@", sqlPwd)
beaverEngine.replaceElement(sqlFile, "@MARIADB_HOST@", mariadbHost)
beaverEngine.replaceElement(sqlFile, "@DATABASE@", database)
beaverEngine.replaceElement(sqlFile, "@CAS_HOST@", localhost)
beaverEngine.replaceElement(sqlFile, "@CAS_PORT@", casPort)
beaverEngine.replaceElement(sqlFile, "@CAS_URI@", casURI)

def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def mariadbUser = beaverEngine.getResource("mariadb", "rootUser")
def mariadbPwd = beaverEngine.getResource("mariadb", "rootPwd")
def mariadbPort = beaverEngine.getResource("mariadb", "port")
def mariadbBin = beaverEngine.getResource("mariadb", "bin")
SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.startService(mariadbService)
beaverEngine.executeMysqlScript(mariadbBin,mariadbPort, mariadbUser, mariadbPwd, sqlFile)
systemdService.stopService(mariadbService)