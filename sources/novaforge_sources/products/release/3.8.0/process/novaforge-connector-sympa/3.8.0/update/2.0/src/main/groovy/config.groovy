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

// CFG diretories
def pluginConf = beaverEngine.getResource("novaforge-runtime","pluginsConf")
def datasourceConf = beaverEngine.getResource("novaforge-runtime","datasourceConf")

// Repository information
def karafRepository = beaverEngine.getResource("karaf","repository")
def dataVersion = beaverEngine.getDataVersion()
def pluginsRepository = karafRepository + "/org/novaforge/forge/plugins/"

def sympaCfg = pluginsRepository + "novaforge-sympa-cfg/" + dataVersion + "/novaforge-sympa-cfg-" + dataVersion + "-plugins.cfg"

def description = beaverEngine.getResource("description")
def clientHost = beaverEngine.getResource("clientHost")
def clientEndPoint = beaverEngine.getResource("clientEndPoint")
def clientAdmin = beaverEngine.getResource("clientAdmin")
def clientPwd = beaverEngine.getResource("clientPwd")
def listmaster = beaverEngine.getResource("listmaster")
def defaultAlias = beaverEngine.getResource("defaultAlias")
def toolAlias = beaverEngine.getResource("toolAlias")
def serverConf = beaverEngine.getResource("serverConf")

beaverEngine.replaceElement(sympaCfg, "@DESCRIPTION", description)
beaverEngine.replaceElement(sympaCfg, "@CLIENT_HOST", clientHost)
beaverEngine.replaceElement(sympaCfg, "@CLIENT_END_POINT", clientEndPoint)
beaverEngine.replaceElement(sympaCfg, "@CLIENT_ADMIN", clientAdmin)
beaverEngine.replaceElement(sympaCfg, "@CLIENT_PWD", clientPwd)
beaverEngine.replaceElement(sympaCfg, "@LISTMASTER", listmaster)
beaverEngine.replaceElement(sympaCfg, "@DEFAULT_ALIAS", defaultAlias)
beaverEngine.replaceElement(sympaCfg, "@TOOL_ALIAS", toolAlias)
beaverEngine.replaceElement(sympaCfg, "@SERVER_CONF", serverConf)


def defaultListnameSuffix = beaverEngine.getResource("defaultListnameSuffix")
def defaultListnameTemplate = beaverEngine.getResource("defaultListnameTemplate")
def defaultListnameDescription = beaverEngine.getResource("defaultListnameDescription")
def defaultListnameSubject = beaverEngine.getResource("defaultListnameSubject")
beaverEngine.replaceExpression(sympaCfg, "defaultListnameSuffix=(.*)", "defaultListnameSuffix=" + defaultListnameSuffix)
beaverEngine.replaceExpression(sympaCfg, "defaultListnameTemplate=(.*)", "defaultListnameTemplate=" + defaultListnameTemplate)
beaverEngine.replaceExpression(sympaCfg, "defaultListnameDescription=(.*)", "defaultListnameDescription=" + defaultListnameDescription)
beaverEngine.replaceExpression(sympaCfg, "defaultListnameSubject=(.*)", "defaultListnameSubject=" + defaultListnameSubject)

// Copy Sympa cfg to novaforge runtime
beaverEngine.copyToFile(sympaCfg, pluginConf + "/plugins.sympa.cfg")

// Configure Sympa datasource
def mariadbHost = beaverEngine.getResource("mariadb","host")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def datasourceUser = beaverEngine.getResource("novaforge-runtime","datasourceUser")
def datasourcePwd = beaverEngine.getResource("novaforge-runtime","datasourcePwd")
def sympaDatasource = pluginsRepository + "novaforge-sympa-impl/" + dataVersion + "/novaforge-sympa-impl-" + dataVersion + "-datasource.cfg"
beaverEngine.replaceElement(sympaDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(sympaDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(sympaDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(sympaDatasource, "password=root", "password=" + datasourcePwd)

// Copy Sympa datasource to novaforge runtime
beaverEngine.copyToFile(sympaDatasource, datasourceConf + "/datasource.sympa.cfg")