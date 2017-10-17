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

// Connector informations
def defaultAlias = beaverEngine.getResource("defaultAlias")

// CFG diretories
def pluginConf = beaverEngine.getResource("novaforge-runtime","pluginsConf")
def datasourceConf = beaverEngine.getResource("novaforge-runtime","datasourceConf")

// Repository information
def karafRepository = beaverEngine.getResource("karaf","repository")
def dataVersion = beaverEngine.getDataVersion()
def pluginsRepository = karafRepository + "/org/novaforge/forge/plugins/"

def svnCfg = pluginsRepository + "novaforge-svn-cfg/" + dataVersion + "/novaforge-svn-cfg-" + dataVersion + "-plugins.cfg"

def description = beaverEngine.getResource("description")
def clientPort = beaverEngine.getResource("clientPort")
def clientEndPoint = beaverEngine.getResource("clientEndPoint")
def servletAlias = beaverEngine.getResource("servletAlias")
def webAlias = beaverEngine.getResource("webAlias")
def serverConf = beaverEngine.getResource("serverConf")
def baseUrl = beaverEngine.getResource("main","httpd","baseUrl")
def karafPort = beaverEngine.getResource("karaf","port")

// Workaround to avoid infinite loops for SVN in all-in-one mode
def clientHost = beaverEngine.getResource("clientHost")
def serverId = beaverEngine.getServerId()
if ("portal".equals(serverId) == false) {
  clientHost = "127.0.0.1"
}

beaverEngine.replaceElement(svnCfg, "@DESCRIPTION", description)
beaverEngine.replaceElement(svnCfg, "@CLIENT_HOST", clientHost)
beaverEngine.replaceElement(svnCfg, "@CLIENT_PORT", clientPort)
beaverEngine.replaceElement(svnCfg, "@CLIENT_END_POINT", clientEndPoint)
beaverEngine.replaceElement(svnCfg, "@SERVLET_ALIAS", servletAlias)
beaverEngine.replaceElement(svnCfg, "@WEB_ALIAS", webAlias)
beaverEngine.replaceElement(svnCfg, "@SERVER_CONF", serverConf)
beaverEngine.replaceElement(svnCfg, "@BASE_URL", baseUrl)
beaverEngine.replaceElement(svnCfg, "@DEFAULT_ALIAS@", defaultAlias)

// Copy svn cfg to novaforge runtime
beaverEngine.copyToFile(svnCfg, pluginConf + "/plugins.svn.cfg")

// Configure svn datasource
def mariadbHost = beaverEngine.getResource("mariadb","host")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def datasourceUser = beaverEngine.getResource("novaforge-runtime","datasourceUser")
def datasourcePwd = beaverEngine.getResource("novaforge-runtime","datasourcePwd")
def svnDatasource = pluginsRepository + "novaforge-svn-impl/" + dataVersion + "/novaforge-svn-impl-" + dataVersion + "-datasource.cfg"
beaverEngine.replaceElement(svnDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(svnDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(svnDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(svnDatasource, "password=root", "password=" + datasourcePwd)

// Copy svn datasource to novaforge runtime
beaverEngine.copyToFile(svnDatasource, datasourceConf + "/datasource.svn.cfg")