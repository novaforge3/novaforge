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

import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine;
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

BeaverEngine beaverEngine = engine

def mantisArdHome = beaverEngine.getResource("home")
def tmp = beaverEngine.getResource("tmp")

def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")


beaverEngine.unpackFile(dataFile, processTmpPath)
def mantisArdArchive = processTmpPath + "/" + "mantisbt-2.0.0.tar.gz"

Path tmpPath = Paths.get(tmp)
if (Files.exists(tmpPath))
{
  beaverEngine.delete(tmp)
}
beaverEngine.createDirectory(tmp)
// Set owner and rights
beaverEngine.setOwner(true, localGroup, localUser, tmp)


// Back up existing MantisARD database
def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def mantisArdSQLUser = beaverEngine.getResource("sqlUser")
def mantisArdSQLPwd = beaverEngine.getResource("sqlPwd")
def database = beaverEngine.getResource("database")
def dumpMantisArdDbFilename = "dumpMantisArd-1.2.5.sql"

String mysqlDumpCommand = "mysqldump"
String[] mysqldumpArgs = new String[7]
mysqldumpArgs[0] = "--add-drop-database"
mysqldumpArgs[1] = "--skip-dump-date"
mysqldumpArgs[2] = "-u" + mantisArdSQLUser
mysqldumpArgs[3] = "-p" + mantisArdSQLPwd
mysqldumpArgs[4] = "--databases"
mysqldumpArgs[5] = database
mysqldumpArgs[6] = "--result-file=" + tmp + "/" + dumpMantisArdDbFilename

SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.startService(mariadbService)
beaverEngine.executeCommand(mysqlDumpCommand, mysqldumpArgs)
systemdService.stopService(mariadbService)


// Back up conf-instance directory
def confInstanceTar = tmp + "/MantisArdConfIntance.tar.gz"

def confInstance = beaverEngine.getResource("confInstance")
Path confInstancePath = Paths.get(confInstance)
if (Files.exists(confInstancePath))
{
  beaverEngine.move(confInstance, tmp)
  beaverEngine.pack(tmp + "/conf-instance", confInstanceTar) 
}

// Install mantis engine
Path homePath = Paths.get(mantisArdHome)
if (Files.exists(homePath))
{
  beaverEngine.delete(mantisArdHome)
}

// Unpack the archive
def localEngines = beaverEngine.getResource("local:engines")
def mantisArdOriDir = localEngines + "/" + "mantisbt-2.0.0"
beaverEngine.unpackFile(mantisArdArchive, localEngines)
beaverEngine.move(mantisArdOriDir, mantisArdHome)


// Copy Files
// home
beaverEngine.copy(processTmpPath + "/resources/files/home/bug_associate.php", mantisArdHome)
beaverEngine.copy(processTmpPath + "/resources/files/home/manage_config_status_create.php", mantisArdHome)
beaverEngine.copy(processTmpPath + "/resources/files/home/manage_config_status_update.php", mantisArdHome)
beaverEngine.copy(processTmpPath + "/resources/files/home/manage_config_status_page.php", mantisArdHome)
beaverEngine.copy(processTmpPath + "/resources/files/home/manage_config_status_view.php", mantisArdHome)
beaverEngine.copy(processTmpPath + "/resources/files/home/reporting_prism_page.php", mantisArdHome)
beaverEngine.copy(processTmpPath + "/resources/files/home/risk_appreciation_page.php", mantisArdHome)
beaverEngine.copy(processTmpPath + "/resources/files/home/risk_appreciation_update.php", mantisArdHome)
beaverEngine.copy(processTmpPath + "/resources/files/home/risk_criteria_create.php", mantisArdHome)
beaverEngine.copy(processTmpPath + "/resources/files/home/risk_criteria_update.php", mantisArdHome)
beaverEngine.copy(processTmpPath + "/resources/files/home/risk_criteria_page.php", mantisArdHome)
beaverEngine.copy(processTmpPath + "/resources/files/home/risk_criteria_view.php", mantisArdHome)
// api
beaverEngine.copy(processTmpPath + "/resources/files/api/mc_management_api.php", mantisArdHome + "/api/soap")
// core
beaverEngine.copy(processTmpPath + "/resources/files/core/conf_instance_file_api.php", mantisArdHome + "/core")
beaverEngine.copy(processTmpPath + "/resources/files/core/forge_api.php", mantisArdHome + "/core")
beaverEngine.copy(processTmpPath + "/resources/files/core/risk_criteria_api.php", mantisArdHome + "/core")
beaverEngine.copy(processTmpPath + "/resources/files/core/status_api.php", mantisArdHome + "/core")
// admin
beaverEngine.copy(processTmpPath + "/resources/files/admin/migration_etat.php", mantisArdHome + "/admin")
// Mantis ARD logo copy
beaverEngine.copyToFile(processTmpPath + "/resources/files/images/mantisard_logo.png",mantisArdHome + "/images/mantisard_logo.png");
// cas
beaverEngine.copy(processTmpPath + "/resources/cas/CAS.php",mantisArdHome);
// conf-instance 
beaverEngine.copy(processTmpPath + "/resources/files/confinstance/default_config_status_action.php", confInstance)
beaverEngine.copy(processTmpPath + "/resources/files/confinstance/default_config_status_risk.php", confInstance)
beaverEngine.copy(processTmpPath + "/resources/files/confinstance/default_config_status_decision.php", confInstance)
beaverEngine.copy(processTmpPath + "/resources/files/confinstance/default_config_status_evolution.php", confInstance)

// execute patch
//patch Mantis for Novaforge
def patchFile_00 = processTmpPath+"/resources/patch/mantisbt-2.0.0.patch"
//patch NOVAFHELP_318 : Mail
def patchFile_01 = processTmpPath+"/resources/patch/mantisbt-2.0.0_01.patch"
//patch Gestion des états 
def patchFile_02 = processTmpPath+"/resources/patch/mantisbt-2.0.0_02.patch"
//Patch Inhibition user et project management
def patchFile_03 = processTmpPath+"/resources/patch/mantisbt-2.0.0_03.patch"
def strip = 1
def keepBackup = false
beaverEngine.patch(patchFile_00, strip, mantisArdHome, keepBackup)
beaverEngine.patch(patchFile_01, strip, mantisArdHome, keepBackup)
beaverEngine.patch(patchFile_02, strip, mantisArdHome, keepBackup)
beaverEngine.patch(patchFile_03, strip, mantisArdHome, keepBackup)

// Restore conf-instance mantisArd repository
// MinDef never used mantis_ard, it is unecessary to restore conf-instance
//if (Files.exists(Paths.get(tmp+"/conf-instance")))
//{
//  beaverEngine.move(tmp+"/conf-instance", mantisArdHome)
//}

// MantisARD log file creation
def logs = beaverEngine.getResource("logs")
Path logsPath = Paths.get(logs)
if (!Files.exists(logsPath))
{
  beaverEngine.createDirectory(logs)
}
