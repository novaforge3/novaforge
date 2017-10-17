/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 * Copyright (c) 2016, ATOS, NovaForge Version 3 and above.
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

import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine;
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

BeaverEngine beaverEngine = engine

def mantisHome = beaverEngine.getResource("home")
def tmp = beaverEngine.getResource("tmp")

def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")

def mantisOriDir = "mantisbt-2.0.0"
def csvimportDir = "csv-import"
def jpgraphDir = "jpgraph-3.5.0b1"


// Unpack the archive mantisbt-2.0.0.tar.gz which contains all tar.gz
beaverEngine.unpackFile(dataFile, processTmpPath)

//mantis archive
def mantisArchive = processTmpPath + "/" + "mantisbt-2.0.0.tar.gz"

//Csv import plugin archive
def csvImportArchive = processTmpPath + "/" + "csv-import-1.4.0.tar.gz"

//jpgraph lib archive
def jpgraphArchive = processTmpPath + "/" + "jpgraph-3.5.0b1.tar.gz"

Path tmpPath = Paths.get(tmp)
if (Files.exists(tmpPath))
{
  beaverEngine.delete(tmp)
}
beaverEngine.createDirectory(tmp)
// Set owner and rights
beaverEngine.setOwner(true, localGroup, localUser, tmp)


// Back up existing bugtracker database
def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def mantisSQLUser = beaverEngine.getResource("sqlUser")
def mantisSQLPwd = beaverEngine.getResource("sqlPwd")
def database = beaverEngine.getResource("database")
def dumpMantisDbFilename = "dumpMantis-1.2.17.sql"

String mysqlDumpCommand = "mysqldump"
String[] mysqldumpArgs = new String[7]
mysqldumpArgs[0] = "--add-drop-database"
mysqldumpArgs[1] = "--skip-dump-date"
mysqldumpArgs[2] = "-u" + mantisSQLUser
mysqldumpArgs[3] = "-p" + mantisSQLPwd
mysqldumpArgs[4] = "--databases"
mysqldumpArgs[5] = database
mysqldumpArgs[6] = "--result-file=" + tmp + "/" + dumpMantisDbFilename

SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.startService(mariadbService)
beaverEngine.executeCommand(mysqlDumpCommand, mysqldumpArgs)
systemdService.stopService(mariadbService)


// Back up conf-instance directory
def confInstanceTar = tmp + "/MantisConfIntance.tar.gz"

def confInstance = beaverEngine.getResource("confInstance")
Path confInstancePath = Paths.get(confInstance)
if (Files.exists(confInstancePath))
{
  beaverEngine.move(confInstance, tmp)
  beaverEngine.pack(tmp + "/conf-instance", confInstanceTar) 
}

// Install mantis engine
Path homePath = Paths.get(mantisHome)
if (Files.exists(homePath))
{
  beaverEngine.delete(mantisHome)
}

beaverEngine.unpackFile(mantisArchive,processTmpPath)
beaverEngine.move(processTmpPath + "/" + mantisOriDir, mantisHome)

//Copy of lib JPgraph
def jpgraphLib= mantisHome+"/library/jpgraph"
Path jpgraphLibHome = Paths.get(jpgraphLib)
if (Files.exists(jpgraphLibHome))
{
  beaverEngine.delete(jpgraphLib)
}

beaverEngine.unpackFile(jpgraphArchive, processTmpPath)
beaverEngine.move(processTmpPath + "/" +jpgraphDir +"/src", jpgraphLib)


// Copy of the plugin import csv
def pluginCsvMantis= mantisHome+"/plugins/Csv_import"
Path pluginCsvMantisPath = Paths.get(pluginCsvMantis)
if (Files.exists(pluginCsvMantisPath))
{
  beaverEngine.delete(pluginCsvMantis)
}

beaverEngine.unpackFile(csvImportArchive,processTmpPath)
beaverEngine.move(processTmpPath + "/" + csvimportDir, pluginCsvMantis)


// Copy Files
// home
beaverEngine.copy(processTmpPath + "/resources/files/home/manage_config_status_create.php", mantisHome)
beaverEngine.copy(processTmpPath + "/resources/files/home/manage_config_status_update.php", mantisHome)
beaverEngine.copy(processTmpPath + "/resources/files/home/manage_config_status_page.php", mantisHome)
beaverEngine.copy(processTmpPath + "/resources/files/home/manage_config_status_view.php", mantisHome)
// api
beaverEngine.copy(processTmpPath + "/resources/files/api/mc_history_api.php", mantisHome + "/api/soap")
// core
beaverEngine.copy(processTmpPath + "/resources/files/core/forge_api.php", mantisHome + "/core")
beaverEngine.copy(processTmpPath + "/resources/files/core/status_api.php", mantisHome + "/core")
beaverEngine.copy(processTmpPath + "/resources/files/core/conf_instance_file_api.php", mantisHome + "/core")
//js
beaverEngine.copy(processTmpPath + "/resources/files/js/novaforge.js", mantisHome + "/js")
// cas
beaverEngine.copy(processTmpPath + "/resources/cas/CAS.php",mantisHome);
// admin
beaverEngine.copy(processTmpPath + "/resources/files/admin/migration_etat.php", mantisHome + "/admin")

//patch Mantis for Novaforge
def patchFile_00 = processTmpPath+"/resources/patch/mantisbt-2.0.0.patch"
//Patch Gestion des états
def patchFile_01 = processTmpPath+"/resources/patch/mantisbt-2.0.0_01.patch"
//Patch Inhibition user et project management
def patchFile_03 = processTmpPath+"/resources/patch/mantisbt-2.0.0_03.patch"
def strip = 1
def keepBackup = false
beaverEngine.patch(patchFile_00, strip, mantisHome, keepBackup)
beaverEngine.patch(patchFile_01, strip, mantisHome, keepBackup)
beaverEngine.patch(patchFile_03, strip, mantisHome, keepBackup)

// Restore conf-instance mantisArd repository
if (Files.exists(Paths.get(tmp+"/conf-instance")))
{
  beaverEngine.move(tmp+"/conf-instance", mantisHome)
}

// Mantis log file creation
def logs = beaverEngine.getResource("logs")
Path logsPath = Paths.get(logs)
if (!Files.exists(logsPath))
{
  beaverEngine.createDirectory(logs)
}