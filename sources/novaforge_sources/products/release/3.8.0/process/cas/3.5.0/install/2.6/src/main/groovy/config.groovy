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

def casHome = beaverEngine.getResource("home")
def casDatas = beaverEngine.getResource("datas")
def casTmp = beaverEngine.getResource("tmp")
def casLogs = beaverEngine.getResource("logs")
def casInternalPort = beaverEngine.getResource("internalPort")
def casAJPPort = beaverEngine.getResource("ajpPort")

def localHome = beaverEngine.getResource("local:home")
def localBin = beaverEngine.getResource("local:bin")
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")

def javaHome = beaverEngine.getResource("jre","home")
def jksPwd = beaverEngine.getResource("certificat","jksPwd")
def jks = beaverEngine.getResource("certificat","jks")

def sessionMaxTimeToLive = beaverEngine.getResource("sessionmaxtimetolive")
def sessionIdleTime = beaverEngine.getResource("sessionidletime")

// Server configuration file
def serverXml = casHome + "/conf/server.xml"
beaverEngine.copyToFile(processTmpPath+"/resources/conf/server.xml",serverXml)
beaverEngine.replaceElement(serverXml,"@CAS_INTERNAL_PORT@",casInternalPort)
beaverEngine.replaceElement(serverXml,"@JKS_PATH@",jks)
beaverEngine.replaceElement(serverXml,"@JKS_PWD@",jksPwd)
beaverEngine.replaceElement(serverXml,"@CAS_AJP_PORT@",casAJPPort)

// Logs configuration files
def loggingProperties = casHome + "/conf/logging.properties"
beaverEngine.copyToFile(processTmpPath+"/resources/conf/logging.properties",loggingProperties)
beaverEngine.replaceElement(loggingProperties,"@CAS_LOGS@",casLogs)

// Extract CAS WAR
def tmpDirectory = processTmpPath + "/tmp"
beaverEngine.createDirectory(tmpDirectory)
def casWar = casHome + "/webapps/cas.war"
def casWarTmp = tmpDirectory + "/cas"
beaverEngine.unpackFile(casWar, casWarTmp)

def log4jXml = casWarTmp + "/WEB-INF/classes/log4j.xml"
beaverEngine.copyToFile(processTmpPath+"/resources/conf/log4j.xml",log4jXml)
beaverEngine.replaceElement(log4jXml,"@CAS_LOGS@",casLogs)

def expirationPolicies = casWarTmp+"/WEB-INF/spring-configuration/ticketExpirationPolicies.xml"
beaverEngine.copyToFile(processTmpPath+"/resources/ticketExpirationPolicies.xml",expirationPolicies)
beaverEngine.replaceElement(expirationPolicies,"@CAS_SESSION_MAXTIMETOLIVE@",sessionMaxTimeToLive)
beaverEngine.replaceElement(expirationPolicies,"@CAS_SESSION_IDLETIME@",sessionIdleTime)

def baseUrl = beaverEngine.getResource("httpd","baseUrl")
def casLogoutView = casWarTmp+"/WEB-INF/view/jsp/default/ui/casLogoutView.jsp"
beaverEngine.replaceElement(casLogoutView,"http://localhost:8181",baseUrl)

// Pack Cas
beaverEngine.pack(casWarTmp, casWar)

// Script
def casForgeBin = localBin + "/cas"
beaverEngine.copyToFile(processTmpPath+"/resources/bin/cas",casForgeBin)
beaverEngine.replaceElement(casForgeBin,"@CAS_HOME@",casHome)
beaverEngine.replaceElement(casForgeBin,"@CAS_TMP_DIR@",casTmp)
beaverEngine.replaceElement(casForgeBin,"@CAS_LOGS@",casLogs)
beaverEngine.replaceElement(casForgeBin,"@JAVA_HOME@",javaHome)
beaverEngine.replaceElement(casForgeBin,"@JKS_PATH@",jks)
beaverEngine.replaceElement(casForgeBin,"@JKS_PWD@",jksPwd)
beaverEngine.replaceElement(casForgeBin,"@NOVAFORGE_GROUP@",localGroup)
beaverEngine.replaceElement(casForgeBin,"@NOVAFORGE_USER@",localUser)

// Service configuration
def casService = processTmpPath + "/resources/bin/cas.service"
def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
beaverEngine.replaceElement(casService, "@MARIADB_SERVICE@", mariadbService)
beaverEngine.replaceElement(casService, "@CAS_BIN@", casForgeBin)
beaverEngine.replaceElement(casService, "@NOVAFORGE_USER@", localUser)
beaverEngine.replaceElement(casService, "@NOVAFORGE_GROUP@", localGroup)
def casSystemd = beaverEngine.getResource("systemdService")
SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.addService("multi-user.target",casService, casSystemd, true);

//Apache
def httpService = beaverEngine.getResource("httpService")
def uri = beaverEngine.getResource("uri")
def internalPort = beaverEngine.getResource("internalPort")
beaverEngine.copyToFile(processTmpPath + "/resources/apache/cas.service",httpService)
beaverEngine.replaceElement(httpService,"@CAS_URI@",uri)
beaverEngine.replaceElement(httpService,"@CAS_HOST@","127.0.0.1")
beaverEngine.replaceElement(httpService,"@CAS_PORT@",internalPort)

// Owner
beaverEngine.setOwner(true, localGroup, localUser, casHome)
beaverEngine.setOwner(true, localGroup, localUser, casDatas)
beaverEngine.setOwner(true, localGroup, localUser, casTmp)
beaverEngine.setOwner(true, localGroup, localUser, casLogs)
beaverEngine.setOwner(false, localGroup, localUser, httpService)
beaverEngine.setOwner(false, localGroup, localUser, casForgeBin)
beaverEngine.setPermissionsOnFiles(false,"664",httpService)
beaverEngine.setPermissionsOnFiles(false,"755",casForgeBin)
beaverEngine.setPermissionsOnFiles(false,"755",casHome + "/bin")
