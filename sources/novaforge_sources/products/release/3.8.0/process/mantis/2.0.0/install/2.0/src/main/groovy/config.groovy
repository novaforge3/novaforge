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

def mantisHome = beaverEngine.getResource("home")
def logs = beaverEngine.getResource("logs")
def httpdGroup = beaverEngine.getResource("httpd", "group")
def httpdUser = beaverEngine.getResource("httpd", "user")
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
def hostname = beaverEngine.getResource("local:host")

def mantisHost = beaverEngine.getResource("mariadb", "host")
def mantisDatabase = beaverEngine.getResource("database")
def sqlUser = beaverEngine.getResource("sqlUser")
def sqlPwd = beaverEngine.getResource("sqlPwd")
def casHost = beaverEngine.getResource("certificat", "commonName")
def casPort = beaverEngine.getResource("main", "cas","externalPort")
def casURI = beaverEngine.getResource("main", "cas","uri")
def casLogout = beaverEngine.getResource("main", "cas","logoutUri")
def casValidateUri = beaverEngine.getResource("main", "cas","validateUri")
def smtpHost = beaverEngine.getResource("smtp","host")
def smtpPort = beaverEngine.getResource("smtp","port")
def smtpUsername = beaverEngine.getResource("smtp","username")
def smtpPassword = beaverEngine.getResource("smtp","password")
def smtpNoReply = beaverEngine.getResource("smtp","noReply")
def dateTimezone = beaverEngine.getResource("dateTimezone")
def karafPort = beaverEngine.getResource("karaf","port")
def adminEmail = beaverEngine.getResource("main", "novaforge-connector-forge","adminEmail")
def httpdBaseURL = beaverEngine.getResource("main","httpd","baseUrl") 
def mantisDefaultAlias = beaverEngine.getResource("main", "novaforge-connector-mantis","defaultAlias") 
def mantisToolAlias = beaverEngine.getResource("main", "novaforge-connector-mantis","toolAlias") 
def localhost = beaverEngine.getResource("main", "novaforge-connector-mantis","clientHost") 

// Fix WSDL
def wsdlMantisFile = mantisHome + "/api/soap/mantisconnect.wsdl"
beaverEngine.replaceElement(wsdlMantisFile, "http://localhost", "https://" + hostname)

// Configure logs
// Add log into log directory
def mantisLogFile = logs + "/mantis.log"
Path mantisLogFilePath = Paths.get(mantisLogFile)
if (!Files.exists(mantisLogFilePath))
{
	beaverEngine.copyToFile(processTmpPath + "/resources/log/mantis.log", mantisLogFile)
}

def mantisMigrationLogFile = logs + "/mantis_Migration.log"
Path mantisMigrationLogFilePath = Paths.get(mantisMigrationLogFile)
if (Files.exists(mantisMigrationLogFilePath))
{
	beaverEngine.delete(mantisMigrationLogFile)
}
beaverEngine.copyToFile(processTmpPath + "/resources/log/mantis.log", mantisMigrationLogFile)

//customize the file config_inc.php mantis
def configIncFile = mantisHome + "/config/config_inc.php"
beaverEngine.copyToFile( processTmpPath + "/resources/config/config_inc.php",configIncFile)
// Set Mantis base URL 
beaverEngine.replaceElement(configIncFile, "@HTTPD_BASE_URL@",httpdBaseURL) 
beaverEngine.replaceElement(configIncFile, "@MANTIS_DEFAULT_ALIAS@",mantisDefaultAlias) 
beaverEngine.replaceElement(configIncFile, "@MANTIS_TOOL_ALIAS@",mantisToolAlias) 
beaverEngine.replaceElement(configIncFile, "@HOSTMANTIS@", mantisHost)
beaverEngine.replaceElement(configIncFile, "%DATABASE%", mantisDatabase)
beaverEngine.replaceElement(configIncFile, "@MANTISUSER@", sqlUser)
beaverEngine.replaceElement(configIncFile, "@PASSMANTIS@", sqlPwd)
beaverEngine.replaceElement(configIncFile, "@CAS_HOST@",casHost)
beaverEngine.replaceElement(configIncFile, "@CAS_PORT@",casPort)
beaverEngine.replaceElement(configIncFile, "@CAS_URI@",casURI)
beaverEngine.replaceElement(configIncFile, "@CAS_SERVICE_URI@",casValidateUri)
beaverEngine.replaceElement(configIncFile, "@MANTIS_HOME@",mantisHome)
beaverEngine.replaceElement(configIncFile, "@PORTKARAF@",karafPort)
beaverEngine.replaceElement(configIncFile, "@RELAISMTP@",smtpHost)
beaverEngine.replaceElement(configIncFile, "@LOGINSMTP@",smtpUsername)
beaverEngine.replaceElement(configIncFile, "@PASSSMTP@",smtpPassword)
beaverEngine.replaceElement(configIncFile, "@RELAISMTPPORT@",smtpPort)
beaverEngine.replaceElement(configIncFile, "@DATETIMEZONE@",dateTimezone)

beaverEngine.setOwner(false,httpdGroup,httpdUser,configIncFile)

//customize the file config_default_inc.php mantis
def configDefaultIncFile = mantisHome + "/config_defaults_inc.php"
beaverEngine.copyToFile( processTmpPath + "/resources/config/config_defaults_inc.php",configDefaultIncFile);
beaverEngine.replaceElement(configDefaultIncFile, "%DATABASE%", mantisDatabase)
beaverEngine.replaceElement(configDefaultIncFile, "@HOSTMANTIS@", mantisHost)
beaverEngine.replaceElement(configDefaultIncFile, "@MANTISUSER@", sqlUser)
beaverEngine.replaceElement(configDefaultIncFile, "@PASSMANTIS@", sqlPwd)
beaverEngine.replaceElement(configDefaultIncFile, "@MAIL_ADMIN_FORGE@",adminEmail)
beaverEngine.replaceElement(configDefaultIncFile, "@MAILNOREPLY@",smtpNoReply)
beaverEngine.replaceElement(configDefaultIncFile, "@RELAISMTP@",smtpHost)
beaverEngine.replaceElement(configDefaultIncFile, "@AUTHSMTPMANTIS@","")
beaverEngine.replaceElement(configDefaultIncFile, "@LOGINSMTP@",smtpUsername)
beaverEngine.replaceElement(configDefaultIncFile, "@PASSSMTP@",smtpPassword)
beaverEngine.replaceElement(configDefaultIncFile, "@RELAISMTPPORT@",smtpPort)
beaverEngine.replaceElement(configDefaultIncFile, "@CAS_HOST@",casHost)
beaverEngine.replaceElement(configDefaultIncFile, "@CAS_PORT@",casPort)
beaverEngine.replaceElement(configDefaultIncFile, "@CAS_URI@",casURI)
beaverEngine.replaceElement(configDefaultIncFile, "@CAS_SERVICE_URI@",casValidateUri)
beaverEngine.replaceElement(configDefaultIncFile, "@MANTIS_HOME@",mantisHome)
beaverEngine.replaceElement(configDefaultIncFile, "@MANTIS_LOG@",logs)
beaverEngine.setOwner(false,httpdGroup,httpdUser,configDefaultIncFile)

def mantisAlias = beaverEngine.getResource("mantisAlias")
def mantisName = beaverEngine.getResource("mantisName")

//createRulesHttp
//local
def localMantisServ = processTmpPath + "/resources/apache/local/mantis.service"
def apacheLocalServices = beaverEngine.getResource("httpServiceLocal")
beaverEngine.replaceElement(localMantisServ, "%ALIAS%", mantisAlias)
beaverEngine.replaceElement(localMantisServ,"%MANTIS_HOME%",mantisHome)
beaverEngine.replaceElement(localMantisServ, "%MANTIS_NAME%", mantisName)
beaverEngine.copyToFile(localMantisServ ,apacheLocalServices)
beaverEngine.setOwner(false, localGroup, localUser, apacheLocalServices)
beaverEngine.setPermissionsOnFiles(false,"664",apacheLocalServices)

// Set owner and rights
beaverEngine.setOwner(true, httpdGroup, httpdUser, mantisHome)
beaverEngine.setPermissionsOnFiles(true,"775", mantisHome)
beaverEngine.setOwner(true, httpdGroup, httpdUser, logs)

//Create database
def sqlCreateTableFile = processTmpPath + "/resources/sql/createTablesMantis.sql"
def sqlFile = processTmpPath + "/resources/sql/pluginsMantisCvsImport.sql"
beaverEngine.replaceElement(sqlCreateTableFile, "%DATABASE%", mantisDatabase)
beaverEngine.replaceElement(sqlCreateTableFile, "%USERMANTIS%", sqlUser)
beaverEngine.replaceElement(sqlCreateTableFile, "%PASSMANTIS%", sqlPwd)
beaverEngine.replaceElement(sqlCreateTableFile, "%HOSTNAME%",  mantisHost)

def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def httpdService = beaverEngine.getResource("httpd", "systemdService")
def mariadbAdminUser = beaverEngine.getResource("mariadb","rootUser")
def mariadbAdminPwd= beaverEngine.getResource("mariadb","rootPwd")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def mariadbSocket = beaverEngine.getResource("mariadb","socket")
def mariadbBin = beaverEngine.getResource("mariadb","bin")
def dbTablePrefix = beaverEngine.getResource("dbTablePrefix")
def dbTableSuffix = beaverEngine.getResource("dbTableSuffix")
def dbTablePluginPrefix = beaverEngine.getResource("dbTablePluginPrefix")

// Wait
String sleepCommand = "sleep"
String[] sleepArgs = new String[1]
sleepArgs[0] = "10s"

// Database Installation	
String curlCommand = "curl" 
String[] curlArgs = new String[9] 
curlArgs[0] = "--noproxy"
curlArgs[1] = "-k" 
curlArgs[2] = "-X" 
curlArgs[3] = "POST" 
curlArgs[4] = "http://" +localhost+ "/" + mantisDefaultAlias + "/" + mantisToolAlias + "/admin/install.php"
curlArgs[5] = "--data"
curlArgs[6] = "install=2&db_type=mysql&hostname=" + mantisHost + "&db_username=" + sqlUser +"&db_password=" + sqlPwd +"&database_name=" + mantisDatabase + "&admin_username=" + mariadbAdminUser +"&admin_password=" + mariadbAdminPwd +"&db_table_prefix=" + dbTablePrefix +"&db_table_plugin_prefix=" + dbTablePluginPrefix +"&db_table_suffix=" + dbTableSuffix +"&timezone=" + dateTimezone + "&go=Install/Upgrade+Database"
curlArgs[7] = "--output" 
curlArgs[8] = mantisMigrationLogFile


// Migration Monitor
import groovy.time.*

public class monRunnable implements Runnable {
  
     private boolean keepOn = false;
     private File logFile;
     private String  msgStopFrench;
     private String  msgStopEnglish;
     
      @Override
      public void run() {
        def monitor
        def timeOut = 5; //minutes
        try {
          boolean stop = false
          Date maxDate
          monitor = logFile.newReader()
          use (groovy.time.TimeCategory) {
            maxDate=timeOut.minute.from.now
          }
          while (!stop) {
            def line = monitor.readLine()
            if (line) {
              if (line.contains(this.msgStopFrench) || line.contains(this.msgStopEnglish)) {
                stop = true
                this.keepOn = true
                println "Process stopped normally - Migration terminated successfully" 
              }
            }
            else if (new Date() > maxDate) {
              println "Process force stopped - Timeout reached"
              stop = true
            }
            else {
              Thread.currentThread().sleep(10) //milliseconds
            }
          }
        }
        finally {
          monitor?.close()
        }
    
    }
      
    public boolean getKeepOn (){
      return keepOn;
    }
    
    public void setLogfile (File logFile){
      this.logFile = logFile;
    }
    
    public void setMsgFrench (String msgStopFrench){
      this.msgStopFrench = msgStopFrench;
    } 
    
    public void setMsgEnglish (String msgStopEnglish){
      this.msgStopEnglish = msgStopEnglish;
    }
}

println "Database installation ..."

SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.startService(mariadbService)
systemdService.startService(httpdService)
beaverEngine.executeCommand(sleepCommand, sleepArgs)
beaverEngine.executeMysqlScript(mariadbBin, mariadbPort, mariadbAdminUser, mariadbAdminPwd, sqlCreateTableFile)
beaverEngine.executeCommand(curlCommand, curlArgs) 

def File logFile = new File(mantisMigrationLogFile)
def msgStopEnglish = "MantisBT was installed successfully."
def msgStopFrench = "MantisBT was installed successfully."

def databaseRunnable = new monRunnable();
databaseRunnable.setLogfile(logFile);
databaseRunnable.setMsgEnglish(msgStopEnglish);
databaseRunnable.setMsgFrench(msgStopFrench);
def databaseThread = new Thread (databaseRunnable);
databaseThread.start();
databaseThread.join();

beaverEngine.executeMysqlScript(mariadbBin, mariadbPort, mariadbAdminUser, mariadbAdminPwd, sqlFile)
systemdService.stopService(httpdService)
systemdService.stopService(mariadbService)
