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
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.groovy.GroovyFacade;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.groovy.GroovyLog;
import org.novaforge.beaver.exception.BeaverException;

BeaverEngine beaverEngine = engine

// == Resources
// - local
def localEngines = beaverEngine.getResource("local:engines")
def localhost = beaverEngine.getResource("local:host")
def localUser = beaverEngine.getResource("httpd", "user")
def localGroup = beaverEngine.getResource("httpd", "group")
def home = beaverEngine.getResource("home")
def clientHost = beaverEngine.getResource("main","novaforge-connector-limesurvey","clientHost")
// - limesurvey database
def database = beaverEngine.getResource("database")
def sqlUser = beaverEngine.getResource("sqlUser")
def sqlPwd = beaverEngine.getResource("sqlPwd")
// - MariaDB
def mariadbHost = beaverEngine.getResource("mariadb", "host")
def mariadbUser = beaverEngine.getResource("mariadb", "rootUser")
def mariadbPwd = beaverEngine.getResource("mariadb", "rootPwd")
def mariadbPort = beaverEngine.getResource("mariadb", "port")
def mariadbBin = beaverEngine.getResource("mariadb", "bin")
def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
// - httpd
def httpdService = beaverEngine.getResource("httpd", "systemdService")
// - CAS
def casPort = beaverEngine.getResource("cas", "externalPort")
def casURI = beaverEngine.getResource("cas", "uri")

// == Backup templates and upload folders
// - Create limesurvey tmp directory
def limesurveyTmp = beaverEngine.getResource("tmp")
Path limesurveyTmpPath = Paths.get(limesurveyTmp)
if (Files.exists(limesurveyTmpPath))
{
  beaverEngine.delete(limesurveyTmp)  
}
beaverEngine.createDirectory(limesurveyTmp)

// - Backup templates and upload folders
beaverEngine.executeCommand("tar","-zvcf", limesurveyTmp + "/backupLimesurvey1.91plus_templates.tar.gz", "-C" , home, "templates");
beaverEngine.executeCommand("tar","-zvcf", limesurveyTmp + "/backupLimesurvey1.91plus_upload.tar.gz", "-C" , home, "upload");

// == Limesurvey 2.00+
// = Install
def distFile200 = beaverEngine.getResource("distFile200")
Path dataFilePar = Paths.get(dataFile).getParent()
String dataFile200 = dataFilePar.toString() + "/../../" + distFile200
// Unpack the data archive
beaverEngine.unpackFile(dataFile200, processTmpPath)
def limesurveyArchive = processTmpPath + "/" + "limesurvey200plus-build131206.tar.gz"

// Install limesurvey engine
Path homePath = Paths.get(home)
if (Files.exists(homePath))
{
  beaverEngine.delete(home)  
}
beaverEngine.unpackFile(limesurveyArchive,localEngines)

// = Config
// Configure config.php from config.200.php
def configPhpSource = processTmpPath + "/resources/files/conf/config.200.php"
def configPhpTarget = home + "/application/config/config.php"
beaverEngine.copyToFile(configPhpSource, configPhpTarget)
beaverEngine.replaceElement(configPhpTarget, "@MARIADB_HOST@", mariadbHost)
beaverEngine.replaceElement(configPhpTarget, "@DATABASE@", database)
beaverEngine.replaceElement(configPhpTarget, "@LIMESURVEY_USER@", sqlUser)
beaverEngine.replaceElement(configPhpTarget, "@LIMESURVEY_PWD@", sqlPwd)

// HTTPD limesurvey.service local : same as for 3.7.0

// Set owner and rights
beaverEngine.setOwner(true, localGroup, localUser, home)
beaverEngine.setPermissionsOnFiles(true,"755", home)

// = Migration
// - Backup the Limesurvey database
def dumpLimesurveyDbFilename = "dumpLimesurvey1.91plus.sql"
SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.startService(mariadbService)

String mysqlDumpCommand = "mysqldump"
String[] mysqldumpArgs = new String[7]
mysqldumpArgs[0] = "--add-drop-database"
mysqldumpArgs[1] = "--skip-dump-date"
mysqldumpArgs[2] = "-u" + sqlUser
mysqldumpArgs[3] = "-p" + sqlPwd
mysqldumpArgs[4] = "--databases"
mysqldumpArgs[5] = database
mysqldumpArgs[6] = "--result-file=" + limesurveyTmp + "/" + dumpLimesurveyDbFilename
beaverEngine.executeCommand(mysqlDumpCommand, mysqldumpArgs)

// - Start Limesurvey (httpd)
systemdService.startService(httpdService)

// - Wait
String sleepCommand = "sleep"
String[] sleepArgs = new String[1]
sleepArgs[0] = "10s"
beaverEngine.executeCommand(sleepCommand, sleepArgs)

// - Migration process (curl)
def descMig = "1.91+ -> 2.00+"
println "[MIGRATION "+descMig+"] - BEGIN"
String curlCommand = "curl"
String[] curlArgs = new String[7]
curlArgs[0] = "--noproxy"
curlArgs[1] = clientHost
curlArgs[2] = "-X"
curlArgs[3] = "POST"
curlArgs[4] = "http://" + clientHost + "/limesurvey-default/limesurvey/index.php?r=admin/update/sa/db/continue/yes"
curlArgs[5] = "--output"
curlArgs[6] = "/dev/null"
beaverEngine.executeCommand(curlCommand, curlArgs)

// - Migration Monitor
String[] paramMig = [home, database, mariadbBin, mariadbPort, mariadbUser, mariadbPwd]
boolean retMonitorMig=Boolean.FALSE
def monitorMigration(descMig,paramMig) {
  boolean retMigOK=Boolean.FALSE
  String dbVersionTarget = ""
  int valSleep = 10000   // in ms
  int nbLoops  = 30      // Total duration = valSleep*nbLoops  (5mn)
  String versionFile = paramMig[0] + "/application/config/version.php"
  new File(versionFile).eachLine { String line ->
    if (line.contains('dbversionnumber')) {
      dbVersionTarget=line.split('=')[1].replaceAll(/[^0-9]/, "")
    }
  }
  println "[MIGRATION "+descMig+"] - Target DB Version : "+dbVersionTarget      
  def mysqlCmd = "select CONCAT('@@',stg_value,'@@') DBVersion from "+paramMig[1]+".lime_settings_global where stg_name='DBVersion';"
  GroovyLog mysqlReturn
  StringBuilder errorBuilder
  String dbVersionCurr = ""
  for (i=0;i<nbLoops;i++) {
    mysqlReturn = GroovyFacade.mysqlExecute(paramMig[2], paramMig[3], paramMig[4], paramMig[5], mysqlCmd);
    if (mysqlReturn.getCodeReturn() == 0) {
      dbVersionCurr=mysqlReturn.getOut().split('@@')[1]
    }
    else {
      errorBuilder = new StringBuilder()
      errorBuilder.append("\n Cannot execute mysql command '")
      errorBuilder.append(mysqlCmd)
      errorBuilder.append("' ")
      errorBuilder.append(mysqlReturn.getOut())
      errorBuilder.append("\n Error: ")
      errorBuilder.append(mysqlReturn.getErr())
      throw new BeaverException(errorBuilder.toString())
    }
    println "[MIGRATION "+descMig+"] - Current DB Version : "+dbVersionCurr      
    if (dbVersionCurr==dbVersionTarget) {
      retMigOK=Boolean.TRUE
      break
    }
    sleep(valSleep)
  }
  return retMigOK
}
retMonitorMig=monitorMigration(descMig,paramMig)
if (retMonitorMig) {
  println "[MIGRATION "+descMig+"] - END : OK"     
}
else {
  println "[MIGRATION "+descMig+"] - END : KO"     
  throw new BeaverException("Migration not performed within expected timeframe")
}

// - Stop httpd
systemdService.stopService(httpdService)

// == Limesurvey 2.06+
// = Install
def distFile206 = beaverEngine.getResource("distFile206")
String dataFile206 = dataFilePar.toString() + "/../../" + distFile206
// Unpack the data archive
beaverEngine.unpackFile(dataFile206, processTmpPath)
limesurveyArchive = processTmpPath + "/" + "limesurvey206plus_build160129.zip"

// Install limesurvey engine
beaverEngine.delete(home)  
beaverEngine.unpackFile(limesurveyArchive,localEngines)

// = Config
// Configure config.php from config.update.php
configPhpSource = processTmpPath + "/resources/files/conf/config.update.php"
beaverEngine.copyToFile(configPhpSource, configPhpTarget)
beaverEngine.replaceElement(configPhpTarget, "@MARIADB_HOST@", mariadbHost)
beaverEngine.replaceElement(configPhpTarget, "@DATABASE@", database)
beaverEngine.replaceElement(configPhpTarget, "@LIMESURVEY_USER@", sqlUser)
beaverEngine.replaceElement(configPhpTarget, "@LIMESURVEY_PWD@", sqlPwd)

// HTTPD limesurvey.service local : same as for 3.7.0

// Disable CSRF Protection
def internalPhpFile = home + "/application/config/internal.php"
beaverEngine.replaceElement(internalPhpFile, "'enableCsrfValidation'=>true", "'enableCsrfValidation'=>false")

// Set owner and rights
beaverEngine.setOwner(true, localGroup, localUser, home)
beaverEngine.setPermissionsOnFiles(true,"755", home)

// = Migration
// - Start Limesurvey (httpd)
systemdService.startService(httpdService)

// - Wait
sleepArgs[0] = "5s"
beaverEngine.executeCommand(sleepCommand, sleepArgs)

// - Migration process (curl)
descMig = "2.00+ -> 2.06+"
println "[MIGRATION "+descMig+"] - BEGIN"
curlArgs[4] = "http://" + clientHost + "/limesurvey-default/limesurvey/index.php?r=admin/databaseupdate/sa/db/continue/yes"
beaverEngine.executeCommand(curlCommand, curlArgs)

// - Migration Monitor
retMonitorMig=monitorMigration(descMig,paramMig)
if (retMonitorMig) {
  println "[MIGRATION "+descMig+"] - END : OK"     
}
else {
  println "[MIGRATION "+descMig+"] - END : KO"     
  throw new BeaverException("Migration not performed within expected timeframe")
}

// - Stop httpd
systemdService.stopService(httpdService)

// == Limesurvey 2.58.2+
// = Install
// Unpack the data archive
beaverEngine.unpackFile(dataFile, processTmpPath)
limesurveyArchive = processTmpPath + "/" + "limesurvey2.58.2plus-170114.tar.gz"

// Install limesurvey engine
beaverEngine.delete(home)  
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
// Configure config.php from config.update.php
configPhpSource = processTmpPath + "/resources/files/conf/config.update.php"
beaverEngine.copyToFile(configPhpSource, configPhpTarget)
beaverEngine.replaceElement(configPhpTarget, "@MARIADB_HOST@", mariadbHost)
beaverEngine.replaceElement(configPhpTarget, "@DATABASE@", database)
beaverEngine.replaceElement(configPhpTarget, "@LIMESURVEY_USER@", sqlUser)
beaverEngine.replaceElement(configPhpTarget, "@LIMESURVEY_PWD@", sqlPwd)

// Execute AuthCAS patch
beaverEngine.patch(processTmpPath + "/resources/patch/AuthCAS-00.patch", 1, home + "/plugins/AuthCAS", false)

// HTTPD limesurvey.service local : same as for 3.7.0

// Disable CSRF Protection
beaverEngine.replaceElement(internalPhpFile, "'enableCsrfValidation'=>true", "'enableCsrfValidation'=>false")

// Set owner and rights
beaverEngine.setOwner(true, localGroup, localUser, home)
beaverEngine.setPermissionsOnFiles(true,"755", home)

// = Migration
// - Start Limesurvey (httpd)
systemdService.startService(httpdService)

// - Wait
sleepArgs[0] = "5s"
beaverEngine.executeCommand(sleepCommand, sleepArgs)

// - Migration process (curl)
descMig = "2.06+ -> 2.58.2+"
println "[MIGRATION "+descMig+"] - BEGIN"
beaverEngine.executeCommand(curlCommand, curlArgs)

// - Migration Monitor
retMonitorMig=monitorMigration(descMig,paramMig)
if (retMonitorMig) {
  println "[MIGRATION "+descMig+"] - END : OK"     
}
else {
  println "[MIGRATION "+descMig+"] - END : KO"     
  throw new BeaverException("Migration not performed within expected timeframe")
}

// - Stop httpd
systemdService.stopService(httpdService)

// - Re-enable CSRF Protection
beaverEngine.replaceElement(internalPhpFile, "'enableCsrfValidation'=>false", "'enableCsrfValidation'=>true")

// - Update database limesurvey
// . Table _sessions : ID field increased from 32 to 50
// . CAS plugin setting
def sqlFile = processTmpPath + "/resources/sql/update_database.sql"
beaverEngine.replaceElement(sqlFile, "@DATABASE@", database)
beaverEngine.replaceElement(sqlFile, "@CAS_HOST@", localhost)
beaverEngine.replaceElement(sqlFile, "@CAS_PORT@", casPort)
beaverEngine.replaceElement(sqlFile, "@CAS_URI@", casURI)
beaverEngine.executeMysqlScript(mariadbBin,mariadbPort, mariadbUser, mariadbPwd, sqlFile)
systemdService.stopService(mariadbService)

// - Stop MariaDB
systemdService.stopService(mariadbService)

// - Restore /upload backup
beaverEngine.executeCommand("tar","-zvxf", limesurveyTmp + "/backupLimesurvey1.91plus_upload.tar.gz", "-C" , home);

// - Restore /templates backup without replacing existing ones (only templates/default/favicon.ico & templates/default/preview.png)
beaverEngine.executeCommand("tar","--skip-old-files","-zvxf", limesurveyTmp + "/backupLimesurvey1.91plus_templates.tar.gz", "-C" , home);
