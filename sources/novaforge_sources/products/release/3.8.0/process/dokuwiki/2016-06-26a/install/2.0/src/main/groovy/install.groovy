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
def dokuwikiArchive = processTmpPath + "/" + "dokuwiki-2016-06-26a.tgz"
def splitBrainGalleryArchive = processTmpPath + "/" + "splitbrain-dokuwiki-plugin-gallery-06a0dc9.zip"
def splitBrainArchive = processTmpPath + "/" + "splitbrain-dokuwiki-plugin-dw2pdf-mpdf574-54-g47496d3.tar.gz"
def tablecalcArchive = processTmpPath + "/" + "tablecalc.zip"


// Install dokuwiki engine
Path homePath = Paths.get(home)
if (Files.exists(homePath))
{
  beaverEngine.delete(home)  
}
def localEngines = beaverEngine.getResource("local:engines")
beaverEngine.unpackFile(dokuwikiArchive,localEngines)

// Unzip AuthPlainCas plugin
beaverEngine.createDirectory(home + "/lib/nusoap")
beaverEngine.unpackFile(processTmpPath + "/resources/files/esn-org-authplaincas-c024204.zip", home + "/lib/plugins")

// Execute patches
def patchDir = processTmpPath + "/resources/patch/"

def keepBackup = false
def strip = 1
def patchFiles = [
    patchDir + "dokuwiki-001.patch",
	patchDir + "dokuwiki-002.patch",
]

// Delete unused files 
beaverEngine.delete(home + "/conf/acl.auth.php.dist") 
beaverEngine.delete(home + "/conf/local.php.dist") 
beaverEngine.delete(home + "/conf/mysql.conf.php.example") 
beaverEngine.delete(home + "/conf/users.auth.php.dist") 

// Copy files

beaverEngine.copy(processTmpPath + "/resources/files/acl.auth.php", home + "/conf")
beaverEngine.copy(processTmpPath + "/resources/files/local.php", home + "/conf")
beaverEngine.copy(processTmpPath + "/resources/files/local.protected.php", home + "/conf")
beaverEngine.copy(processTmpPath + "/resources/files/forgeutils.php", home + "/inc")
beaverEngine.copy(processTmpPath + "/resources/files/users.auth.plaincas.php", home + "/conf")
beaverEngine.copy(processTmpPath + "/resources/files/plaincas.settings.php", home + "/conf")
beaverEngine.copy(processTmpPath + "/resources/files/database.php", home + "/lib/plugins/authplaincas")

patchFiles.each() { 
	patchFile -> beaverEngine.patch(patchFile, 1, home, keepBackup)
}

// Unzip NuSOAP library for PHP
beaverEngine.createDirectory(home + "/lib/nusoap")
beaverEngine.unpackFile(processTmpPath + "/resources/files/nusoap-0.9.5-f12312.zip", home + "/lib/nusoap")



// Install dokuwiki plugins
def pluginsDir = beaverEngine.getResource("pluginsDir")
beaverEngine.unpackFile(splitBrainGalleryArchive,pluginsDir)
beaverEngine.unpackFile(splitBrainArchive,pluginsDir)
beaverEngine.unpackFile(tablecalcArchive,pluginsDir)

// Rename splitbrain plugin directory
beaverEngine.moveDir(pluginsDir + "/splitbrain-dokuwiki-plugin-dw2pdf-47496d3", pluginsDir + "/dw2pdf")

// Rename splitbrain gallery plugin directory
beaverEngine.moveDir(pluginsDir + "/splitbrain-dokuwiki-plugin-gallery-06a0dc9", pluginsDir + "/gallery")

// Move data directory
def datadir = beaverEngine.getResource("datas")
Path dataPath = Paths.get(datadir)
if (Files.exists(dataPath))
{
  beaverEngine.delete(datadir)  
}

beaverEngine.moveDir(home + '/data', datadir);

// Delete unused files 
beaverEngine.delete(home + "/lib/plugins/authad") 
beaverEngine.delete(home + "/lib/plugins/authldap") 
beaverEngine.delete(home + "/lib/plugins/authmysql")
beaverEngine.delete(home + "/lib/plugins/authpdo") 
beaverEngine.delete(home + "/lib/plugins/authpgsql") 
beaverEngine.delete(home + "/lib/plugins/authplain") 

// Set owner and rights
beaverEngine.setOwner(true, localGroup, localUser, home)

// Create database dokuwiki
def sqlFile = processTmpPath + "/resources/sql/create_database.sql"
def sqlUser = beaverEngine.getResource("sqlUser")
def sqlPwd = beaverEngine.getResource("sqlPwd")
def mariadbHost = beaverEngine.getResource("mariadb", "host")
def database = beaverEngine.getResource("database")
beaverEngine.replaceElement(sqlFile, "@DOKUWIKI_USER@", sqlUser)
beaverEngine.replaceElement(sqlFile, "@DOKUWIKI_PWD@", sqlPwd)
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
