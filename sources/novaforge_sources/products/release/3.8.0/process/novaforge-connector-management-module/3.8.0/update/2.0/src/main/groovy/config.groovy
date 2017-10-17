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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

BeaverEngine beaverEngine = engine
// Server variables
def localUser = beaverEngine.getResource("local:user")
def localGroup = beaverEngine.getResource("local:group")

// Datasource information
def mariadbHost = beaverEngine.getResource("mariadb","host")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def datasourceUser = beaverEngine.getResource("novaforge-runtime","datasourceUser")
def datasourcePwd = beaverEngine.getResource("novaforge-runtime","datasourcePwd")

// CFG diretories
def pluginConf = beaverEngine.getResource("novaforge-runtime","pluginsConf")
def datasourceConf = beaverEngine.getResource("novaforge-runtime","datasourceConf")

// Repository information
def karafRepository = beaverEngine.getResource("karaf","repository")
def dataVersion = beaverEngine.getDataVersion()
def pluginsRepository = karafRepository + "/org/novaforge/forge/plugins/"

def managementCfg = pluginsRepository + "novaforge-management-module-cfg/" + dataVersion + "/novaforge-management-module-cfg-" + dataVersion + "-plugins.cfg"

def description = beaverEngine.getResource("description")
def toolAlias = beaverEngine.getResource("toolAlias")
def serverConf = beaverEngine.getResource("serverConf")
def localHost = beaverEngine.getResource("local:host")
def karafPort = beaverEngine.getResource("karaf","port")

beaverEngine.replaceElement(managementCfg, "@DESCRIPTION@", description)
beaverEngine.replaceElement(managementCfg, "@TOOL_ALIAS@", toolAlias)
beaverEngine.replaceElement(managementCfg, "@SERVER_CONF@", serverConf)
beaverEngine.replaceElement(managementCfg, "@KARAF_PORT@", karafPort)
beaverEngine.replaceElement(managementCfg, "@KARAF_HOST@", localHost)

beaverEngine.copyToFile(managementCfg, pluginConf + "/plugins.management.module.cfg")

// Configure Management datasource
def managementToolDatasource = pluginsRepository + "novaforge-management-module-tool-impl/" + dataVersion + "/novaforge-management-module-tool-impl-" + dataVersion + "-datasource.cfg"
def managementPluginDatasource = pluginsRepository + "novaforge-management-module-impl/" + dataVersion + "/novaforge-management-module-impl-" + dataVersion + "-datasource.cfg"
beaverEngine.replaceElement(managementToolDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(managementToolDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(managementToolDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(managementToolDatasource, "password=root", "password=" + datasourcePwd)
beaverEngine.replaceElement(managementPluginDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(managementPluginDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(managementPluginDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(managementPluginDatasource, "password=root", "password=" + datasourcePwd)


// Copy Management datasource to novaforge runtime
beaverEngine.copyToFile(managementToolDatasource, datasourceConf + "/datasource.tool.management.module.cfg")
beaverEngine.copyToFile(managementPluginDatasource, datasourceConf + "/datasource.management.module.cfg")

// ************** Create tmp directory
def tmpDirectory = processTmpPath + "/tmp"
beaverEngine.createDirectory(tmpDirectory)

// Extract Management WAR
def managementWar = pluginsRepository + "novaforge-management-module-tool-ui/" + dataVersion + "/novaforge-management-module-tool-ui-" + dataVersion + ".war"
def managementWarTmp = tmpDirectory + "/management"
beaverEngine.unpackFile(managementWar, managementWarTmp)

// Configuration Management web.xml
def managementWebXml = managementWarTmp + "/WEB-INF/web.xml"

// Configuration CAS
def baseUrl = beaverEngine.getResource("httpd","baseUrl")
def casUri = beaverEngine.getResource("main", "cas","uri")
def casLoginUri = beaverEngine.getResource("main", "cas", "loginUri")
def casExternalPort = beaverEngine.getResource("main", "cas","externalPort")
beaverEngine.replaceElement(managementWebXml, "https://localhost:8443/cas/login", baseUrl + ":" + casExternalPort  + casUri + casLoginUri )
beaverEngine.replaceElement(managementWebXml, "https://localhost:8443/cas", baseUrl + ":" + casExternalPort + casUri )
beaverEngine.replaceElement(managementWebXml, "http://localhost:8181", baseUrl + ":" + casExternalPort)


// Pack Management
beaverEngine.pack(managementWarTmp, managementWar)