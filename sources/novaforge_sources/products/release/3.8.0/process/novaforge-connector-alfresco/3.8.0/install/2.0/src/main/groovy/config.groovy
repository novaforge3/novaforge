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

// CFG diretories
def pluginConf = beaverEngine.getResource("novaforge-runtime","pluginsConf")
def datasourceConf = beaverEngine.getResource("novaforge-runtime","datasourceConf")

// Repository information
def karafRepository = beaverEngine.getResource("karaf","repository")
def dataVersion = beaverEngine.getDataVersion()
def pluginsRepository = karafRepository + "/org/novaforge/forge/plugins/"

// Plugin configuration
def alfrescoCfg = pluginsRepository + "novaforge-alfresco-cfg/" + dataVersion + "/novaforge-alfresco-cfg-" + dataVersion + "-plugins.cfg"

def description = beaverEngine.getResource("description")

def clientPort = beaverEngine.getResource("clientPort")
def clientAdmin = beaverEngine.getResource("clientAdmin")
def clientPwd = beaverEngine.getResource("clientPwd")
def defaultAlias = beaverEngine.getResource("defaultAlias")
def toolAlias = beaverEngine.getResource("toolAlias")
def shareAlias = beaverEngine.getResource("shareAlias")
def serverConf = beaverEngine.getResource("serverConf")

def clientHost = beaverEngine.getResource("clientHost")
beaverEngine.replaceElement(alfrescoCfg, "@DESCRIPTION", description)
beaverEngine.replaceElement(alfrescoCfg, "@CLIENT_HOST", clientHost)
beaverEngine.replaceElement(alfrescoCfg, "@CLIENT_PORT", clientPort)
beaverEngine.replaceElement(alfrescoCfg, "@CLIENT_ADMIN", clientAdmin)
beaverEngine.replaceElement(alfrescoCfg, "@CLIENT_PWD", clientPwd)
beaverEngine.replaceElement(alfrescoCfg, "@DEFAULT_ALIAS", defaultAlias)
beaverEngine.replaceElement(alfrescoCfg, "@TOOL_ALIAS", toolAlias)
beaverEngine.replaceElement(alfrescoCfg, "@SHARE_ALIAS", shareAlias)
beaverEngine.replaceElement(alfrescoCfg, "@SERVER_CONF", serverConf)

// Copy alfresco cfg to novaforge runtime
beaverEngine.copyToFile(alfrescoCfg, pluginConf + "/plugins.alfresco.cfg")

// Configure Alfresco datasource
def mariadbHost = beaverEngine.getResource("mariadb","host")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def datasourceUser = beaverEngine.getResource("novaforge-runtime","datasourceUser")
def datasourcePwd = beaverEngine.getResource("novaforge-runtime","datasourcePwd")
def alfrescoDatasource = pluginsRepository + "novaforge-alfresco-impl/" + dataVersion + "/novaforge-alfresco-impl-" + dataVersion + "-datasource.cfg"
beaverEngine.replaceElement(alfrescoDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(alfrescoDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(alfrescoDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(alfrescoDatasource, "password=root", "password=" + datasourcePwd)

// Copy alfresco datasource to novaforge runtime
beaverEngine.copyToFile(alfrescoDatasource, datasourceConf + "/datasource.alfresco.cfg")
