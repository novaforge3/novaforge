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

BeaverEngine beaverEngine = engine
// Server variables
def localUser = beaverEngine.getResource("local:user")
def localGroup = beaverEngine.getResource("local:group")

// CFG diretories
def pluginConf = beaverEngine.getResource("novaforge-runtime","pluginsConf")
def datasourceConf = beaverEngine.getResource("novaforge-runtime","datasourceConf")

// Repository information
def karafRepository = beaverEngine.getResource("karaf","repository")
def dataVersion = beaverEngine.getDataVersion()
def pluginsRepository = karafRepository + "/org/novaforge/forge/plugins/"

// ************** Create tmp directory
def tmpDirectory = processTmpPath + "/tmp"
beaverEngine.createDirectory(tmpDirectory)

// Commons
def localHost = beaverEngine.getResource("local:host")
def karafPort = beaverEngine.getResource("karaf","port")
def deliveryDatas = beaverEngine.getResource("datas")
def deliveryToolCfg = pluginsRepository + "novaforge-delivery-manager-tool-impl/" + dataVersion + "/novaforge-delivery-manager-tool-impl-" + dataVersion + "-tools.cfg"
def deliveryPluginCfg = pluginsRepository + "novaforge-delivery-manager-cfg/" + dataVersion + "/novaforge-delivery-manager-cfg-" + dataVersion + "-plugins.cfg"

def description = beaverEngine.getResource("description")
def toolAlias = beaverEngine.getResource("toolAlias")
def serverConf = beaverEngine.getResource("serverConf")

beaverEngine.replaceElement(deliveryPluginCfg, "@DESCRIPTION@", description)
beaverEngine.replaceElement(deliveryPluginCfg, "@TOOL_ALIAS@", toolAlias)
beaverEngine.replaceElement(deliveryPluginCfg, "@SERVER_CONF@", serverConf)
beaverEngine.replaceElement(deliveryPluginCfg, "@KARAF_PORT@", karafPort)
beaverEngine.replaceElement(deliveryPluginCfg, "@KARAF_HOST@", localHost)

beaverEngine.replaceElement(deliveryToolCfg, "@DELIVERYDATA", deliveryDatas)
beaverEngine.replaceElement(deliveryPluginCfg, "@DELIVERYDATA", deliveryDatas)

// Copy delivery cfg to novaforge runtime
beaverEngine.copyToFile(deliveryToolCfg, pluginConf + "/tools.delivery.cfg")
beaverEngine.copyToFile(deliveryPluginCfg, pluginConf + "/plugins.delivery.cfg")

// Configure Delivery datasource
def mariadbHost = beaverEngine.getResource("mariadb","host")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def datasourceUser = beaverEngine.getResource("novaforge-runtime","datasourceUser")
def datasourcePwd = beaverEngine.getResource("novaforge-runtime","datasourcePwd")
def deliveryToolDatasource = pluginsRepository + "novaforge-delivery-manager-tool-impl/" + dataVersion + "/novaforge-delivery-manager-tool-impl-" + dataVersion + "-datasource.cfg"
def deliveryPluginDatasource = pluginsRepository + "novaforge-delivery-manager-impl/" + dataVersion + "/novaforge-delivery-manager-impl-" + dataVersion + "-datasource.cfg"
beaverEngine.replaceElement(deliveryToolDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(deliveryToolDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(deliveryToolDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(deliveryToolDatasource, "password=root", "password=" + datasourcePwd)
beaverEngine.replaceElement(deliveryPluginDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(deliveryPluginDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(deliveryPluginDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(deliveryPluginDatasource, "password=root", "password=" + datasourcePwd)

// Copy delivery datasource to novaforge runtime
beaverEngine.copyToFile(deliveryToolDatasource, datasourceConf + "/datasource.tool.delivery.cfg")
beaverEngine.copyToFile(deliveryPluginDatasource, datasourceConf + "/datasource.delivery.cfg")

// Extract Delivery WAR
def deliveryWar = pluginsRepository + "novaforge-delivery-manager-tool-ui/" + dataVersion + "/novaforge-delivery-manager-tool-ui-" + dataVersion + ".war"
def deliveryWarTmp = tmpDirectory + "/delivery"
beaverEngine.unpackFile(deliveryWar, deliveryWarTmp)

// Configuration Delivery web.xml
def deliveryWebXml = deliveryWarTmp + "/WEB-INF/web.xml"

// Configuration CAS
def baseUrl = beaverEngine.getResource("httpd","baseUrl")
def casUri = beaverEngine.getResource("main", "cas","uri")
def casLoginUri = beaverEngine.getResource("main", "cas", "loginUri")
def casExternalPort = beaverEngine.getResource("main", "cas","externalPort")
beaverEngine.replaceElement(deliveryWebXml, "https://localhost:8443/cas/login", baseUrl + ":" + casExternalPort  + casUri + casLoginUri )
beaverEngine.replaceElement(deliveryWebXml, "https://localhost:8443/cas", baseUrl + ":" + casExternalPort + casUri )
beaverEngine.replaceElement(deliveryWebXml, "http://localhost:8181", baseUrl + ":" + casExternalPort)

// Pack Delivery
beaverEngine.pack(deliveryWarTmp, deliveryWar)