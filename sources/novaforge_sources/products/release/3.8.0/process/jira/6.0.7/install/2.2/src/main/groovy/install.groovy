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
SystemdService systemdService = beaverEngine.getSystemdService()

// Define configurable resources.
def home = beaverEngine.getResource("home")
def datas = beaverEngine.getResource("datas")
def logs = beaverEngine.getResource("logs")
def mainHost = beaverEngine.getResource("main:host")
def database = beaverEngine.getResource("database")
def mysqlUser = beaverEngine.getResource("mysqlUser")
def mysqlPwd = beaverEngine.getResource("mysqlPwd")
def defaultAlias = beaverEngine.getResource("main", "novaforge-connector-jira", "defaultAlias")
def toolAlias = beaverEngine.getResource("main","novaforge-connector-jira", "toolAlias")
def licence = beaverEngine.getResource("licence")
def mailSubject = beaverEngine.getResource("mailSubject")
def smtpHost = beaverEngine.getResource("main", "smtp","host")
def smtpPort = beaverEngine.getResource("main", "smtp","port")
def smtpNoReply = beaverEngine.getResource("main", "smtp","noReply")

// Define technicables configuration.
def apacheTomcat = "apache-tomcat-7.0.42"

// Get database services and resources.
def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def mariadbAdminUser = beaverEngine.getResource("mariadb","rootUser")
def mariadbAdminPwd= beaverEngine.getResource("mariadb","rootPwd")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def mariadbSocket = beaverEngine.getResource("mariadb","socket")
def mariadbBin = beaverEngine.getResource("mariadb","bin")

// Create directories
beaverEngine.createDirectory(home)
beaverEngine.createDirectory(datas)
beaverEngine.createDirectory(logs)

// Unpack the archive rpms.tar.gz which contains all rpms mandatory to JIRA (perl libraries)
// Unpack the archive binaries.tar.gz which contains tomcat and jira's binaries
beaverEngine.unpackFile(dataFile, processTmpPath)
def rpmsArchiveFile = processTmpPath + "/" + "rpms.tar.gz"
def binariesArchiveFile = processTmpPath + "/" + "binaries.tar.gz"
beaverEngine.unpackFile(binariesArchiveFile, processTmpPath)

// Install RPMs
RepositoryService repositoryService = engine.getRepositoryService()
repositoryService.installRPMs(rpmsArchiveFile, "perl-libwww-perl")

// Install Tomcat for Jira into Jira Engine

// Untar Tomcat War
def tomcatArchive = processTmpPath + "/" + apacheTomcat + ".tar.gz"
beaverEngine.unpackFile(dataFile, processTmpPath)
beaverEngine.unpackFile(tomcatArchive, processTmpPath)

beaverEngine.copy(processTmpPath + "/" + apacheTomcat , home)

// Add Libraries for Tomcat
beaverEngine.copy(processTmpPath + "/resources/lib", home + "/lib")

// Delete tomcat default webapps from webbapps folder
def webappsToDeletes = [
  "docs",
  "examples",
  "host-manager",
  "manager",
  "ROOT"
]
webappsToDeletes.each() {
  webappsToDelete -> beaverEngine.delete(home + "/webapps/" + webappsToDelete)
}

// Create database and user for jira if needed
systemdService.startService(mariadbService)
def jiraDbCreate = processTmpPath + "/resources/sql/jiradb_create.sql"
beaverEngine.replaceElement(jiraDbCreate, "@userJiraMysql@", mysqlUser)
beaverEngine.replaceElement(jiraDbCreate, "@passJiraMysql@", mysqlPwd)
beaverEngine.replaceElement(jiraDbCreate, "@databaseJiraMysql@", database)
beaverEngine.executeMysqlScript(mariadbBin, mariadbPort, mariadbAdminUser, mariadbAdminPwd, jiraDbCreate)

// Import default database
def jiraDb = processTmpPath + "/resources/sql/jiradb.sql"
beaverEngine.replaceElement(jiraDb, "@databaseJiraMysql@", database)
beaverEngine.executeMysqlScript(mariadbBin, mariadbPort, mariadbAdminUser, mariadbAdminPwd, jiraDb)

// Configure JIRA options
def jiraDbConfigureOptions = processTmpPath + "/resources/sql/jiradb_configure_options.sql"
beaverEngine.replaceElement(jiraDbConfigureOptions, "@jiraLicence@", licence)
beaverEngine.replaceElement(jiraDbConfigureOptions, "@forgeHostname@", mainHost)
beaverEngine.replaceElement(jiraDbConfigureOptions, "@aliasJira@", "/"+defaultAlias+"/"+toolAlias)
beaverEngine.replaceElement(jiraDbConfigureOptions, "@mailServerName@", smtpHost)
beaverEngine.replaceElement(jiraDbConfigureOptions, "@mailServerDescription@", smtpHost)
beaverEngine.replaceElement(jiraDbConfigureOptions, "@mailExpeditor@", smtpNoReply)
beaverEngine.replaceElement(jiraDbConfigureOptions, "@mailSubject@", mailSubject)
beaverEngine.replaceElement(jiraDbConfigureOptions, "@mailServerPort@", smtpPort)
beaverEngine.replaceElement(jiraDbConfigureOptions, "@mailServerHost@", smtpHost)
beaverEngine.replaceElement(jiraDbConfigureOptions, "@databaseJiraMysql@", database)
beaverEngine.executeMysqlScript(mariadbBin, mariadbPort, mariadbAdminUser, mariadbAdminPwd, jiraDbConfigureOptions)
systemdService.stopService(mariadbService)

// Install Tempo, Jira Agile, Service Desk and Script Runner
def installedPluginsDir = datas + "/plugins/installed-plugins"
beaverEngine.createDirectory(installedPluginsDir)
def jiraPluginsFiles = [
  "atlassian-chaperone-1.0.20.jar",
  "groovyrunner-2.1.15.jar",
  "jira-greenhopper-plugin-6.3.0.2.jar",
  "jira-issue-nav-components-6.2.23.jar",
  "jira-servicedesk-1.1.6.jar",
  "jira-workinghours-plugin-1.2.jar",
  "tempo-core-1.1.2.jar",
  "tempo-plugin-7.6.3.jar",
  "tempo-teams-1.0.6.jar"
]
jiraPluginsFiles.each() {
  jiraPluginsFile -> beaverEngine.copy(processTmpPath + "/resources/jira-plugins/" +jiraPluginsFile, installedPluginsDir)
}

// Configure Jira Home
beaverEngine.copy(processTmpPath + "/resources/conf/jira-config.properties", datas)