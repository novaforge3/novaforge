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
def logs = beaverEngine.getResource("logs")
def datas = beaverEngine.getResource("datas")
def sites = beaverEngine.getResource("sites")
def localGroup = beaverEngine.getResource("httpd", "group")
def localUser = beaverEngine.getResource("httpd", "user")

// Unpack the data archive
beaverEngine.unpackFile(dataFile, processTmpPath)

def spipArchive = processTmpPath + "/" + "spip-3.1.1.zip"

// Install spip engine
Path homePath = Paths.get(home)
if (Files.exists(homePath))
{
  beaverEngine.delete(home)  
}
def localEngines = beaverEngine.getResource("local:engines")

beaverEngine.unpackFile(spipArchive, localEngines)

// Install spip sites logs directory
Path logsPath = Paths.get(logs)
if (Files.exists(logsPath))
{
  beaverEngine.delete(logs)  
}
beaverEngine.createDirectory(logs)

// Install spip sites datas directory
Path datasPath = Paths.get(datas)
if (Files.exists(datasPath))
{
  beaverEngine.delete(datas)  
}
beaverEngine.createDirectory(datas)

def spipSitesEcrire = beaverEngine.getResource("ecrire")
beaverEngine.createDirectory(spipSitesEcrire)
def spipSitesEcrireImg = beaverEngine.getResource("ecrireImg")
beaverEngine.createDirectory(spipSitesEcrireImg)
def spipSitesEcrireTmp = beaverEngine.getResource("ecrireTmp")
beaverEngine.createDirectory(spipSitesEcrireTmp)
def spipSitesEcrireLocal = beaverEngine.getResource("ecrireLocal")
beaverEngine.createDirectory(spipSitesEcrireLocal)
def spipSitesEcrireConfig = beaverEngine.getResource("ecrireConfig")
beaverEngine.createDirectory(spipSitesEcrireConfig)

// Create symlink on engines directory
def link = home + "/sites"
beaverEngine.createSymlink(sites, link)
beaverEngine.setOwner(false, localGroup, localUser, link)

// Delete unused files
beaverEngine.delete(home + "/tmp/remove.txt") 
beaverEngine.delete(home + "/INSTALL.txt") 
beaverEngine.delete(home + "/htaccess.txt") 

// Execute patch
beaverEngine.patch(processTmpPath + "/resources/patch/spip-3.1.1-001.patch", 1, home, false)

// Copy files
// config
beaverEngine.copy(processTmpPath + "/resources/files/chmod.php", home + "/config")
beaverEngine.copy(processTmpPath + "/resources/files/mes_options.php", home + "/config")
// ecrire
beaverEngine.copy(processTmpPath + "/resources/files/nf_core.php", home + "/ecrire")
beaverEngine.copy(processTmpPath + "/resources/files/nf_forge_api.php", home + "/ecrire")
beaverEngine.copy(processTmpPath + "/resources/files/nf_spip_account_api.php", home + "/ecrire")
beaverEngine.copy(processTmpPath + "/resources/files/nf_spip_commons_api.php", home + "/ecrire")
beaverEngine.copy(processTmpPath + "/resources/files/nf_spipconnect.php", home + "/ecrire")
beaverEngine.copy(processTmpPath + "/resources/files/nf_spip_site_api.php", home + "/ecrire")

// Unzip NuSOAP library for PHP
beaverEngine.createDirectory(home + "/ecrire/nusoap")
beaverEngine.unpackFile(processTmpPath + "/resources/files/nusoap-0.9.5-f12312.zip", home + "/ecrire/nusoap")

// Create database spip
def sqlFile = processTmpPath + "/resources/sql/create_database.sql"
def sqlUser = beaverEngine.getResource("sqlUser")
def sqlPwd = beaverEngine.getResource("sqlPwd")
def mariadbHost = beaverEngine.getResource("mariadb", "host")
def database = beaverEngine.getResource("database")
beaverEngine.replaceElement(sqlFile, "@SPIP_USER@", sqlUser)
beaverEngine.replaceElement(sqlFile, "@SPIP_PWD@", sqlPwd)
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
systemdService.stopService(mariadbService)