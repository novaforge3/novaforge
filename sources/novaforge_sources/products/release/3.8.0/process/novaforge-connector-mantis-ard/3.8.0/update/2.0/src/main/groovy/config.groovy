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

def mantisArdCfg = pluginsRepository + "novaforge-mantis-ard-cfg/" + dataVersion + "/novaforge-mantis-ard-cfg-" + dataVersion + "-plugins.cfg"

def description = beaverEngine.getResource("description")
def clientHost = beaverEngine.getResource("clientHost")
def clientAdmin = beaverEngine.getResource("clientAdmin")
def clientPwd = beaverEngine.getResource("clientPwd")
def defaultAlias = beaverEngine.getResource("defaultAlias")
def toolAlias = beaverEngine.getResource("toolAlias")
def serverConf = beaverEngine.getResource("serverConf")

beaverEngine.replaceElement(mantisArdCfg, "@DESCRIPTION", description)
beaverEngine.replaceElement(mantisArdCfg, "@CLIENT_HOST", clientHost)
beaverEngine.replaceElement(mantisArdCfg, "@CLIENT_ADMIN", clientAdmin)
beaverEngine.replaceElement(mantisArdCfg, "@CLIENT_PWD", clientPwd)
beaverEngine.replaceElement(mantisArdCfg, "@DEFAULT_ALIAS", defaultAlias)
beaverEngine.replaceElement(mantisArdCfg, "@TOOL_ALIAS", toolAlias)
beaverEngine.replaceElement(mantisArdCfg, "@SERVER_CONF", serverConf)

// Copy mantis-ard cfg to novaforge runtime
beaverEngine.copyToFile(mantisArdCfg, pluginConf + "/plugins.mantis.ard.cfg")

// Configure mantis-ard datasource
def mantisArdDatasource = pluginsRepository + "novaforge-mantis-ard-impl/" + dataVersion + "/novaforge-mantis-ard-impl-" + dataVersion + "-datasource.cfg"
beaverEngine.replaceElement(mantisArdDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(mantisArdDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(mantisArdDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(mantisArdDatasource, "password=root", "password=" + datasourcePwd)

// Copy mantis-ard datasource to novaforge runtime
beaverEngine.copyToFile(mantisArdDatasource, datasourceConf + "/datasource.mantis.ard.cfg")