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

def dokuwikiCfg = pluginsRepository + "novaforge-dokuwiki-cfg/" + dataVersion + "/novaforge-dokuwiki-cfg-" + dataVersion + "-plugins.cfg"

def description = beaverEngine.getResource("description")
def clientHost = beaverEngine.getResource("clientHost")
def clientAdmin = beaverEngine.getResource("clientAdmin")
def clientPwd = beaverEngine.getResource("clientPwd")
def defaultAlias = beaverEngine.getResource("defaultAlias")
def toolAlias = beaverEngine.getResource("toolAlias")
def serverConf = beaverEngine.getResource("serverConf")

beaverEngine.replaceElement(dokuwikiCfg, "@DESCRIPTION", description)
beaverEngine.replaceElement(dokuwikiCfg, "@CLIENT_HOST", clientHost)
beaverEngine.replaceElement(dokuwikiCfg, "@CLIENT_ADMIN", clientAdmin)
beaverEngine.replaceElement(dokuwikiCfg, "@CLIENT_PWD", clientPwd)
beaverEngine.replaceElement(dokuwikiCfg, "@DEFAULT_ALIAS", defaultAlias)
beaverEngine.replaceElement(dokuwikiCfg, "@TOOL_ALIAS", toolAlias)
beaverEngine.replaceElement(dokuwikiCfg, "@SERVER_CONF", serverConf)

// Copy dokuwiki cfg to novaforge runtime
beaverEngine.copyToFile(dokuwikiCfg, pluginConf + "/plugins.dokuwiki.cfg")

// Configure dokuwiki datasource
def dokuwikiDatasource = pluginsRepository + "novaforge-dokuwiki-impl/" + dataVersion + "/novaforge-dokuwiki-impl-" + dataVersion + "-datasource.cfg"
beaverEngine.replaceElement(dokuwikiDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(dokuwikiDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(dokuwikiDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(dokuwikiDatasource, "password=root", "password=" + datasourcePwd)

// Copy dokuwiki datasource to novaforge runtime
beaverEngine.copyToFile(dokuwikiDatasource, datasourceConf + "/datasource.dokuwiki.cfg")