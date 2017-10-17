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
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService
import java.util.Map;

BeaverEngine beaverEngine = engine

// Server variables
def localUser = beaverEngine.getResource("local:user")
def localGroup = beaverEngine.getResource("local:group")

// Nexus variables
def sonarHome = beaverEngine.getResource("home")
def sonarConf = sonarHome + "/conf"
def sonarLogs = beaverEngine.getResource("logs")
def sonarDatas = beaverEngine.getResource("datas")
def sonarTmp = beaverEngine.getResource("tmp")
def clientHost = beaverEngine.getResource("main","novaforge-connector-sonar","clientHost")
def clientPort = beaverEngine.getResource("main", "novaforge-connector-sonar", "clientPort")
def defaultAlias = beaverEngine.getResource("main", "novaforge-connector-sonar", "defaultAlias")
def toolAlias = beaverEngine.getResource("main", "novaforge-connector-sonar", "toolAlias")


// Log settings
def logback = beaverEngine.getResource("logBack")
beaverEngine.copyToFile(processTmpPath + "/resources/conf/logback.xml", logback)
beaverEngine.replaceElement(logback, "@SONAR_LOGS@", sonarLogs)

// Sonar settings
def sonarProperties = sonarConf + "/sonar.properties"
beaverEngine.copyToFile(processTmpPath + "/resources/conf/sonar.properties", sonarProperties)

// SQL configuration
def mariadbHost = beaverEngine.getResource("mariadb","host")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def sonarSQLUser = beaverEngine.getResource("sqlUser")
def sonarSQLPwd = beaverEngine.getResource("sqlPwd")
beaverEngine.replaceElement(sonarProperties, "@MYSQL_HOST@", mariadbHost)
beaverEngine.replaceElement(sonarProperties, "@MYSQL_PORT@", mariadbPort)
beaverEngine.replaceElement(sonarProperties, "@SONAR_JDBC_USER@", sonarSQLUser)
beaverEngine.replaceElement(sonarProperties, "@SONAR_JDBC_PWD@", sonarSQLPwd)

// Server configuration
def baseUrl = beaverEngine.getResource("main", "httpd", "baseUrl")

def server = beaverEngine.getServerId()
def sonarHostJSON = beaverEngine.parseJson(clientHost)
if ( sonarHostJSON in Map )
{
  clientHost = sonarHostJSON.get(server);
}

def sonarPortJSON = beaverEngine.parseJson(clientPort)
if ( sonarPortJSON in Map )
{
  clientPort = sonarPortJSON.get(server);
}
def defaultAliasJSON = beaverEngine.parseJson(defaultAlias)
if ( defaultAliasJSON in Map )
{
  defaultAlias = defaultAliasJSON.get(server);
}

def toolAliasJSON = beaverEngine.parseJson(toolAlias)
if ( toolAliasJSON in Map )
{
  toolAlias = toolAliasJSON.get(server);
}

beaverEngine.replaceElement(sonarProperties, "@SONAR_HOST@", clientHost)
beaverEngine.replaceElement(sonarProperties, "@SONAR_PORT@", clientPort)
beaverEngine.replaceElement(sonarProperties, "@DEFAULT_ALIAS@", defaultAlias)
beaverEngine.replaceElement(sonarProperties, "@TOOL_ALIAS@", toolAlias)
beaverEngine.replaceElement(sonarProperties, "@BASE_URL@", baseUrl)

// Cas configuration
def casBaseUrl = beaverEngine.getResource("main", "cas", "baseUrl")
def casLogoutUri = beaverEngine.getResource("main", "cas", "logoutUri")
def casLoginUri = beaverEngine.getResource("main", "cas", "loginUri")
beaverEngine.replaceElement(sonarProperties, "@CAS_URL@", casBaseUrl)
beaverEngine.replaceElement(sonarProperties, "@CAS_LOGIN_URI@", casLoginUri)
beaverEngine.replaceElement(sonarProperties, "@CAS_LOGOUT_URI@", casLogoutUri)


// Setup keystore
def jksPwd = beaverEngine.getResource("certificat", "jksPwd")
def jks = beaverEngine.getResource("certificat", "jks")
def wrapperConf = sonarConf  + "/wrapper.conf"
beaverEngine.copyToFile(processTmpPath + "/resources/conf/wrapper.conf", wrapperConf)
beaverEngine.replaceElement(wrapperConf, "@SONAR_LOGS@",sonarLogs)
beaverEngine.replaceElement(wrapperConf,"@JKS_PATH@",jks)
beaverEngine.replaceElement(wrapperConf,"@JKS_PWD@",jksPwd)
beaverEngine.replaceElement(sonarProperties,"@JKS_PATH@",jks)
beaverEngine.replaceElement(sonarProperties,"@JKS_PWD@",jksPwd)

// Copye and set up new sonar executable
def jre8Home = beaverEngine.getResource("jre8", "home")
def sonarBin = beaverEngine.getResource("bin")
beaverEngine.copyToFile(processTmpPath + "/resources/bin/sonar", sonarBin)
beaverEngine.replaceElement(sonarBin, "@JAVA_HOME@", jre8Home)
beaverEngine.replaceElement(sonarBin, "@SONAR_HOME@", sonarHome)
beaverEngine.replaceElement(sonarBin, "@SONAR_TMP@", sonarTmp)

// Service configuration
def sonarService = processTmpPath + "/resources/bin/sonar.service"
def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
beaverEngine.replaceElement(sonarService, "@MARIADB_SERVICE@", mariadbService)
beaverEngine.replaceElement(sonarService, "@SONAR_BIN@", sonarBin)
beaverEngine.replaceElement(sonarService, "@NOVAFORGE_USER@", localUser)
beaverEngine.replaceElement(sonarService, "@NOVAFORGE_GROUP@", localGroup)
def sonarSystemd = beaverEngine.getResource("systemdService")
SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.addService("multi-user.target",sonarService, sonarSystemd, true);

// Define owner
beaverEngine.setOwner(false, localGroup, localUser, sonarBin)
beaverEngine.setPermissionsOnFiles(false, "755", sonarBin)
beaverEngine.setOwner(true,localGroup,localUser,sonarHome)
beaverEngine.setOwner(true,localGroup,localUser,sonarDatas)
beaverEngine.setOwner(true,localGroup,localUser,sonarLogs)
beaverEngine.setOwner(true,localGroup,localUser,sonarTmp)

// Start sonar to create tables
systemdService.startService(sonarSystemd)
// Wait 1m for sonar to be started
if (beaverEngine.isSimulateMode() == false)
{
  sleep 60000
} 

// Set up smtp
def sqlFile = processTmpPath + "/resources/sql/sonar_smtp.sql"
def smtpHost = beaverEngine.getResource("main", "smtp", "host")
def smtpPort = beaverEngine.getResource("main", "smtp", "port")
def smtpUsername = beaverEngine.getResource("main", "smtp", "username")
def smtpPassword = beaverEngine.getResource("main", "smtp", "password")
def smtpNoReply = beaverEngine.getResource("main", "smtp", "noReply")    
beaverEngine.replaceElement(sqlFile, "@SMTP_HOST@", smtpHost)
beaverEngine.replaceElement(sqlFile, "@SMTP_PORT@", smtpPort)
beaverEngine.replaceElement(sqlFile, "@SMTP_USERNAME@", smtpUsername)
beaverEngine.replaceElement(sqlFile, "@SMTP_PASSWORD@", smtpPassword)
beaverEngine.replaceElement(sqlFile, "@SMTP_NOREPLY@", smtpNoReply)


def mariadbUser = beaverEngine.getResource("mariadb", "rootUser")
def mariadbPwd = beaverEngine.getResource("mariadb", "rootPwd")
def mariadbBin = beaverEngine.getResource("mariadb", "bin")
beaverEngine.executeMysqlScript(mariadbBin,mariadbPort, mariadbUser, mariadbPwd, sqlFile)

// Stop sonar
systemdService.stopService(sonarSystemd)
systemdService.stopService(mariadbService)
