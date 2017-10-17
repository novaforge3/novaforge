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

BeaverEngine beaverEngine = engine
def localHome = beaverEngine.getResource("local:home")
def home = beaverEngine.getResource("home")
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
def alfrescoLogs = beaverEngine.getResource("logs")
def alfrescoTmp = beaverEngine.getResource("tmp")
def solrHome = beaverEngine.getResource("solrHome")

// Create tmp directory
def tmpDir = processTmpPath + "/tmp"

///////////////////// Configure Alfresco Webapp ///////////////////////////
def alfrescoWebapp = home + "/" + "webapps/alfresco.war"
beaverEngine.createDirectory(tmpDir)
beaverEngine.unpackFile(alfrescoWebapp,tmpDir)

// web.xml
def alfrescoWebXml = tmpDir + "/WEB-INF/web.xml"
beaverEngine.copyToFile(processTmpPath + "/resources/webapps/alfresco/WEB-INF/web.xml", alfrescoWebXml)

// Configuration CAS
def casBaseUrl = beaverEngine.getResource("main", "cas", "baseUrl")
def casUri = beaverEngine.getResource("main", "cas", "uri")
def casLogoutUri = beaverEngine.getResource("main", "cas", "logoutUri")
def casLoginUri = beaverEngine.getResource("main", "cas", "loginUri")
def mainBaseUrl = beaverEngine.getResource("main","httpd","baseUrl")
beaverEngine.replaceElement(alfrescoWebXml, "@EXTERNAL_URL@", mainBaseUrl)
beaverEngine.replaceElement(alfrescoWebXml, "@CAS_URL@", casBaseUrl)
beaverEngine.replaceElement(alfrescoWebXml, "@CAS_LOGIN_URI@", casLoginUri)

// log4j.properties
beaverEngine.replaceElement(tmpDir + "/WEB-INF/classes/log4j.properties","log4j.appender.File.File=alfresco.log","log4j.appender.File.File=" + alfrescoLogs + "/alfresco.log")

beaverEngine.pack(tmpDir, alfrescoWebapp)
beaverEngine.delete(tmpDir)


///////////////////// Configure Share Webapp ///////////////////////////
def shareWebapp = home + "/" + "webapps/share.war"
beaverEngine.createDirectory(tmpDir)
beaverEngine.unpackFile(shareWebapp,tmpDir)

// web.xml
def shareWebXml = tmpDir + "/WEB-INF/web.xml"
beaverEngine.copyToFile(processTmpPath + "/resources/webapps/share/WEB-INF/web.xml", shareWebXml)

beaverEngine.replaceElement(shareWebXml, "@EXTERNAL_URL@", mainBaseUrl)
beaverEngine.replaceElement(shareWebXml, "@CAS_URL@", casBaseUrl)
beaverEngine.replaceElement(shareWebXml, "@CAS_LOGIN_URI@", casLoginUri)

// log4j.properties
beaverEngine.replaceElement(tmpDir+"/WEB-INF/classes/log4j.properties","log4j.appender.File.File=share.log","log4j.appender.File.File=" + beaverEngine.getResource("logs") + "/share.log")

beaverEngine.pack(tmpDir, shareWebapp)
beaverEngine.delete(tmpDir)

//////////////////////////// Configure Shared directory ///////////////////////////
def baseUrl = beaverEngine.getResource("main", "httpd", "baseUrl")
def clienthost = beaverEngine.getResource("main", "novaforge-connector-alfresco", "clientHost")
def mariadbPort = beaverEngine.getResource("mariadb", "port")
def alfrescoDefaultAlias = beaverEngine.getResource("main", "novaforge-connector-alfresco", "defaultAlias")
def alfrescoPort = beaverEngine.getResource("main", "novaforge-connector-alfresco", "clientPort")
def alfrescoExternalPort = beaverEngine.getResource("main", "novaforge-connector-alfresco", "externalPort")

				
def alfrescoDatas = beaverEngine.getResource("datas")
def alfrescoProtocol = beaverEngine.getResource("protocol")

beaverEngine.replaceElement(home + "/shared/classes/alfresco/web-extension/share-config-custom.xml", "http://localhost:8081/alfresco/wcs", "http://localhost:" + alfrescoPort + "/" + alfrescoDefaultAlias + "/alfresco/wcs")
beaverEngine.replaceExpression(home + "/shared/classes/alfresco/web-extension/share-config-custom.xml", "<share-base-url>(.*)</share-base-url>", "<share-base-url>" + baseUrl + "</share-base-url>")
beaverEngine.replaceExpression(home + "/shared/classes/alfresco/web-extension/share-config-custom.xml", "<repository-url>(.*)</repository-url>", "<repository-url>" + baseUrl + "/" + alfrescoDefaultAlias + "/alfresco</repository-url>")
beaverEngine.replaceElement(home + "/shared/classes/alfresco/web-extension/share-config-custom.xml", "http://localhost:8081/alfresco/s", "http://localhost:" + alfrescoPort + "/" + alfrescoDefaultAlias + "/alfresco/s")

beaverEngine.replaceExpression(home + "/shared/classes/alfresco-global.properties", "web.application.context.url=(.*)", "web.application.context.url=" + baseUrl + "/" + alfrescoDefaultAlias + "/alfresco")
beaverEngine.replaceExpression(home + "/shared/classes/alfresco-global.properties", "dir.root=(.*)", "dir.root=" + alfrescoDatas)
beaverEngine.replaceExpression(home + "/shared/classes/alfresco-global.properties", "localname=(.*)", "localname=" + clienthost)
beaverEngine.replaceExpression(home + "/shared/classes/alfresco-global.properties", "db.url=(.*)", "db.url=jdbc:mysql://localhost:" + mariadbPort + "/" + beaverEngine.getResource("database"))
beaverEngine.replaceExpression(home + "/shared/classes/alfresco-global.properties", "alfresco.port=(.*)", "alfresco.port=" + alfrescoExternalPort )
beaverEngine.replaceExpression(home + "/shared/classes/alfresco-global.properties", "alfresco.protocol=(.*)", "alfresco.protocol=" + alfrescoProtocol)
beaverEngine.replaceExpression(home + "/shared/classes/alfresco-global.properties", "alfresco.context=(.*)", "alfresco.context=" + alfrescoDefaultAlias + "/alfresco")
beaverEngine.replaceExpression(home + "/shared/classes/alfresco-global.properties", "share.port=(.*)", "share.port=" + alfrescoExternalPort)
beaverEngine.replaceExpression(home + "/shared/classes/alfresco-global.properties", "share.protocol=(.*)", "share.protocol=" + alfrescoProtocol)
beaverEngine.replaceExpression(home + "/shared/classes/alfresco-global.properties", "share.context=(.*)", "share.context=" + alfrescoDefaultAlias + "/share")
beaverEngine.replaceExpression(home + "/shared/classes/alfresco-global.properties", "solr.port=(.*)", "solr.port=" + alfrescoPort )
beaverEngine.replaceElement(home + "/shared/classes/alfresco-global.properties", "8080", alfrescoPort)

def addToken = "authentication.chain=alfrescoNtlm1:alfrescoNtlm,cas:external"
beaverEngine.addLineToFile(home + "/shared/classes/alfresco-global.properties", addToken, "mail.host=" + beaverEngine.getResource("main","smtp","host"))
beaverEngine.addLineToFile(home + "/shared/classes/alfresco-global.properties", addToken, "mail.port=" + beaverEngine.getResource("main","smtp","port"))
beaverEngine.addLineToFile(home + "/shared/classes/alfresco-global.properties", addToken, "mail.username=anonymous")
beaverEngine.addLineToFile(home + "/shared/classes/alfresco-global.properties", addToken, "mail.password=" + beaverEngine.getResource("main","smtp","password"))
beaverEngine.addLineToFile(home + "/shared/classes/alfresco-global.properties", addToken, "mail.encoding=UTF-8")
beaverEngine.addLineToFile(home + "/shared/classes/alfresco-global.properties", addToken, "mail.from.default=" + beaverEngine.getResource("main","smtp","noReply"))
beaverEngine.addLineToFile(home + "/shared/classes/alfresco-global.properties", addToken, "mail.smtp.auth=false")
beaverEngine.addLineToFile(home + "/shared/classes/alfresco-global.properties", addToken, "cifs.enabled=false")


// Configure conf directory (server.xml and logging.properties)
def serverXml = home + "/conf/server.xml"

beaverEngine.copyToFile(processTmpPath + "/resources/conf/server.xml", serverXml)
beaverEngine.replaceElement(serverXml, "@ALFRESCO_PORT@", alfrescoPort)
beaverEngine.replaceElement(serverXml, "@ALFRESCO_LOGS@", alfrescoLogs)
beaverEngine.replaceElement(serverXml, "@SOLR_HOME@", solrHome)

def loggingProps = home + "/conf/logging.properties"
beaverEngine.copyToFile(processTmpPath + "/resources/conf/logging.properties", loggingProps)
beaverEngine.replaceElement(loggingProps, "@ALFRESCO_LOGS@", alfrescoLogs)


// Configure bin directory (catalina.sh)
beaverEngine.replaceExpression(home + "/bin/catalina.sh", "CATALINA_OUT=(.*)/catalina.out", "CATALINA_OUT=" + alfrescoLogs + "/catalina.out")


// Configure solr properties
def solrDatas = beaverEngine.getResource("solrDatas")
def solrWorkspaceProperties = home + "/solr/workspace-SpacesStore/conf/solrcore.properties"
def solrArchiveProperties = home + "/solr/archive-SpacesStore/conf/solrcore.properties"
beaverEngine.replaceExpression(solrWorkspaceProperties, "data.dir.root=(.*)", "data.dir.root=" + solrDatas)
beaverEngine.replaceExpression(solrArchiveProperties, "data.dir.root=(.*)", "data.dir.root=" + solrDatas)
beaverEngine.replaceExpression(solrWorkspaceProperties, "alfresco.baseUrl=(.*)", "alfresco.baseUrl=/" + alfrescoDefaultAlias + "/alfresco")
beaverEngine.replaceExpression(solrArchiveProperties, "alfresco.baseUrl=(.*)", "alfresco.baseUrl=/" + alfrescoDefaultAlias + "/alfresco")
beaverEngine.replaceExpression(solrWorkspaceProperties, "alfresco.port=(.*)", "alfresco.port=" + alfrescoPort )
beaverEngine.replaceExpression(solrArchiveProperties, "alfresco.port=(.*)", "alfresco.port=" + alfrescoPort )

// Configure solr log4j properties
def solLog4j = home + "/solr/log4j-solr.properties"
beaverEngine.replaceExpression(solLog4j, "log4j.appender.File.File=(.*)", "log4j.appender.File.File=" + alfrescoLogs + "/solr.log" )


// Configure alfresco shell
def javaHome = beaverEngine.getResource("jre","home")
def gedHost = beaverEngine.getResource("main:host")

def karafPort = beaverEngine.getResource("main", "karaf", "port")
def jksPwd = beaverEngine.getResource("certificat","jksPwd")
def jks = beaverEngine.getResource("certificat","jks")
def casRestUri = beaverEngine.getResource("main","cas","restUri")
def alfrescoSh = beaverEngine.getResource("execFile")
beaverEngine.copyToFile(processTmpPath + "/resources/bin/alfresco", alfrescoSh)
beaverEngine.replaceElement(alfrescoSh,"@NOVA_HOME@",localHome)
beaverEngine.replaceElement(alfrescoSh,"@JAVA_HOME@",javaHome)
beaverEngine.replaceElement(alfrescoSh,"@ALFRESCO_HOME@",home)
beaverEngine.replaceElement(alfrescoSh,"@ALFRESCO_LOGS@",alfrescoLogs)
beaverEngine.replaceElement(alfrescoSh,"@NOVAFORGE_USER@",localUser)
beaverEngine.replaceElement(alfrescoSh,"@NOVAFORGE_GROUP@",localGroup)
beaverEngine.replaceElement(alfrescoSh,"@ALFRESCO_TMP_DIR@",alfrescoTmp)
beaverEngine.replaceElement(alfrescoSh,"@HOSTNAMEFORGE@",gedHost)
beaverEngine.replaceElement(alfrescoSh,"@PORT_KARAF@",karafPort)
beaverEngine.replaceElement(alfrescoSh,"@CASRESTURI@", mainBaseUrl + casRestUri)
beaverEngine.replaceElement(alfrescoSh,"@KEYSTORE@",jks)
beaverEngine.replaceElement(alfrescoSh,"@KEYSTOREPWD@",jksPwd)
beaverEngine.setOwner(false, localGroup, localUser, alfrescoSh)
beaverEngine.setPermissionsOnFiles(false,"755",alfrescoSh)

// Service configuration
def alfrescoService = processTmpPath + "/resources/bin/alfresco.service"
def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
beaverEngine.replaceElement(alfrescoService, "@MARIADB_SERVICE@", mariadbService)
beaverEngine.replaceElement(alfrescoService, "@ALFRESCO_BIN@", alfrescoSh)
beaverEngine.replaceElement(alfrescoService, "@NOVAFORGE_USER@", localUser)
beaverEngine.replaceElement(alfrescoService, "@NOVAFORGE_GROUP@", localGroup)
def alfrescoSystemd = beaverEngine.getResource("systemdService")
SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.addService("multi-user.target", alfrescoService, alfrescoSystemd, true);


// Set owner and rights
beaverEngine.setOwner(true, localGroup, localUser, home)
beaverEngine.setPermissionsOnFiles(true,"644",home)
beaverEngine.setPermissionsOnFiles(false,"755", home + "/bin/catalina.sh")
beaverEngine.setPermissionsOnFiles(false,"755", home + "/bin/startup.sh")
beaverEngine.setPermissionsOnFiles(false,"755", home + "/bin/shutdown.sh")

