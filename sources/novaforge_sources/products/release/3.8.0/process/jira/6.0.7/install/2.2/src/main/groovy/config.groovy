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
import org.novaforge.beaver.deployment.plugin.deploy.engine.UserService
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService
import org.codehaus.plexus.util.StringUtils
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

BeaverEngine beaverEngine = engine
SystemdService systemdService = beaverEngine.getSystemdService()

// Get local resources.
def localHome = beaverEngine.getResource("local:home")
def localBin = beaverEngine.getResource("local:bin")
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
def localHost = beaverEngine.getResource("local:host")

// Define configurable resources.
def home = beaverEngine.getResource("home")
def datas = beaverEngine.getResource("datas")
def logs = beaverEngine.getResource("logs")
def mainHost = beaverEngine.getResource("main:host")
def defaultAlias = beaverEngine.getResource("main", "novaforge-connector-jira", "defaultAlias")
def toolAlias = beaverEngine.getResource("main","novaforge-connector-jira", "toolAlias")
def casPort = beaverEngine.getResource("main", "cas","externalPort")
def portApacheProxy = beaverEngine.getResource("main","novaforge-connector-jira", "portApacheProxy")
def portStandardHttp = beaverEngine.getResource("main","novaforge-connector-jira", "portStandardHttp")
def portApacheServer = beaverEngine.getResource("portApacheServer")
def jksPwd = beaverEngine.getResource("certificat","jksPwd")
def jks = beaverEngine.getResource("certificat","jks")
def javaHome = beaverEngine.getResource("jre","home")
def clientAdmin = beaverEngine.getResource("main","novaforge-connector-jira", "clientAdmin")
def clientPwd = beaverEngine.getResource("main","novaforge-connector-jira", "clientPwd")
def mysqlUser = beaverEngine.getResource("mysqlUser")
def mysqlPwd = beaverEngine.getResource("mysqlPwd")

// Get database services and resources.
def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def mariadbAdminUser = beaverEngine.getResource("mariadb","rootUser")
def mariadbAdminPwd= beaverEngine.getResource("mariadb","rootPwd")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def mariadbSocket = beaverEngine.getResource("mariadb","socket")
def mariadbBin = beaverEngine.getResource("mariadb","bin")
def database = beaverEngine.getResource("database")

// Define technicables configuration.
def jiraWarDir = "atlassian-jira-6.0.7-war"
def jiraWarFile = "atlassian-jira-6.0.7.war"

// Untar Jira War
def jiraArchive = processTmpPath + "/" + jiraWarDir + ".tar.gz"
beaverEngine.unpackFile(dataFile, processTmpPath)
beaverEngine.unpackFile(jiraArchive, processTmpPath)

// Delete libraries already embeded in apache tomcat
def librariesFiles = [
  "jcl-over-slf4j-1.6.4.jar",
  "jul-to-slf4j-1.6.4.jar",
  "log4j-1.2.16.jar",
  "slf4j-api-1.6.4.jar",
  "slf4j-log4j12-1.6.4.jar"
]

librariesFiles.each() {
  librariesFile -> beaverEngine.delete(processTmpPath + "/" + jiraWarDir + "/webapp/WEB-INF/lib/" + librariesFile)
}

// Contextualize CAS
def confDir = processTmpPath+"/resources/conf"
beaverEngine.replaceElement(confDir + '/edit-webapp/WEB-INF/web.xml', "@cashostnameLong@", mainHost)
beaverEngine.replaceElement(confDir + '/edit-webapp/WEB-INF/web.xml', "@casPort@", casPort)
beaverEngine.replaceElement(confDir + '/edit-webapp/WEB-INF/classes/seraph-config.xml', "@cashostnameLong@", mainHost)
beaverEngine.replaceElement(confDir + '/edit-webapp/WEB-INF/classes/jira-application.properties', "@jiraDataHome@", datas)

// Contextualize tomcat server.xml file
beaverEngine.copy(confDir + "/server.xml", home + "/conf")
def apacheServerXml = home + "/conf/server.xml"
beaverEngine.replaceElement(apacheServerXml, "@aliasJira@", "/"+defaultAlias+"/"+toolAlias)
beaverEngine.replaceElement(apacheServerXml, "@jiraWarFile@", jiraWarFile)
beaverEngine.replaceElement(apacheServerXml, "@forgeHostname@", mainHost)
beaverEngine.replaceElement(apacheServerXml, "@jiraTomcatServerPort@", portApacheServer)
beaverEngine.replaceElement(apacheServerXml, "@jiraTomcatApacheProxyPort@", portApacheProxy)
beaverEngine.replaceElement(apacheServerXml, "@jiraTomcatStandardHTTPPort@", portStandardHttp)

// Copy configuration to the data archive
beaverEngine.copy(confDir + '/edit-webapp/WEB-INF', processTmpPath + "/" + jiraWarDir + "/edit-webapp/WEB-INF" )

// Configure tomcat logs
beaverEngine.replaceElement(home+"/conf/logging.properties", "\${catalina.base}/logs", logs)

// Contextualize setenv.sh and add it in Tomcat
beaverEngine.replaceElement(processTmpPath+"/resources/sh/setenv.sh", "@storepass@", jksPwd)
beaverEngine.replaceElement(processTmpPath+"/resources/sh/setenv.sh", "@keystore@", jks)
beaverEngine.move(processTmpPath + "/resources/sh/setenv.sh", home + "/bin")

// Build Jira WAR
def buildWarScript = processTmpPath + "/resources/sh/build_war.sh"
beaverEngine.replaceElement(buildWarScript, "@jiraHome@", datas);
beaverEngine.replaceElement(buildWarScript, "@javaHome@", javaHome);
beaverEngine.replaceElement(buildWarScript, "@jiraWarPath@", processTmpPath + "/" + jiraWarDir );
beaverEngine.executeScript(buildWarScript)

// Move Jira WAR to Tomcat webbapps folder
beaverEngine.copy(processTmpPath + "/" + jiraWarDir  + "/dist-tomcat/" + jiraWarFile, home + "/webapps")

// Configure Jira Database Connexion File
def databaseConnexionFile = confDir + "/dbconfig.xml"
beaverEngine.replaceElement(databaseConnexionFile, "@mysqlPort@", mariadbPort )
beaverEngine.replaceElement(databaseConnexionFile, "@databaseJiraMysql@", database )
beaverEngine.replaceElement(databaseConnexionFile, "@jiradbusername@", mysqlUser )
beaverEngine.replaceElement(databaseConnexionFile, "@jiradbpassword@", mysqlPwd )
beaverEngine.move(databaseConnexionFile, datas)

// Configure Jira bin
def jiraForgeBin = localBin + "/jira"
beaverEngine.copyToFile(processTmpPath+"/resources/bin/jira", jiraForgeBin)
beaverEngine.replaceElement(jiraForgeBin, "@JAVA_HOME@", javaHome)
beaverEngine.replaceElement(jiraForgeBin, "@JIRA_HOME@", datas);
beaverEngine.replaceElement(jiraForgeBin, "@JIRA_ENGINE@", home)
beaverEngine.replaceElement(jiraForgeBin, "@PORT_TOMCAT_JIRA@", portStandardHttp)
beaverEngine.replaceElement(jiraForgeBin, "@HOST_NAME_LONG@", localHost)
beaverEngine.replaceElement(jiraForgeBin, "@ALIAS_JIRA@", "/"+defaultAlias+"/"+toolAlias)
beaverEngine.replaceElement(jiraForgeBin, "@JIRA_LOGS@", logs)
beaverEngine.setOwner(false, localGroup, localUser, jiraForgeBin)
beaverEngine.setPermissionsOnFiles(false,"755",jiraForgeBin)

// Configure Jira Systemd service
def jiraSystemdService = processTmpPath + "/resources/bin/jira.service"
beaverEngine.replaceElement(jiraSystemdService, "@MARIADB_SERVICE@", mariadbService)
beaverEngine.replaceElement(jiraSystemdService, "@JIRA_BIN@", jiraForgeBin)
beaverEngine.replaceElement(jiraSystemdService, "@NOVAFORGE_USER@", localUser)
beaverEngine.replaceElement(jiraSystemdService, "@NOVAFORGE_GROUP@", localGroup)
def jiraSystemd = beaverEngine.getResource("systemdService")
systemdService.addService("multi-user.target",jiraSystemdService, jiraSystemd, true)
beaverEngine.setOwner(true, localGroup, localUser, datas)
beaverEngine.setPermissionsOnFiles(true,"775",datas)

// Configure Jira indexation script
beaverEngine.createDirectory(home + "/scripts")
def reIndexScript = home+ "/scripts/reIndex.pl"
beaverEngine.copyToFile(processTmpPath+"/resources/perl/reIndex.pl", reIndexScript)
beaverEngine.replaceElement(reIndexScript, "8080", portStandardHttp)
beaverEngine.replaceElement(reIndexScript, "technicalAdminUsername", clientAdmin)
beaverEngine.replaceElement(reIndexScript, "technicalAdminPassword", clientPwd)
beaverEngine.replaceElement(reIndexScript, "localhost", localHost)
beaverEngine.replaceElement(reIndexScript, "aliasJira", "/"+defaultAlias+"/"+toolAlias)
beaverEngine.setPermissionsOnFiles(false,"755", reIndexScript)

// Set owner and rights
beaverEngine.setOwner(true, localGroup, localUser, datas)
beaverEngine.setPermissionsOnFiles(true,"775",datas)
beaverEngine.setOwner(true, localGroup, localUser, home)
beaverEngine.setPermissionsOnFiles(true,"775",home)
beaverEngine.setOwner(true, localGroup, localUser, logs)
beaverEngine.setPermissionsOnFiles(true,"775",logs)