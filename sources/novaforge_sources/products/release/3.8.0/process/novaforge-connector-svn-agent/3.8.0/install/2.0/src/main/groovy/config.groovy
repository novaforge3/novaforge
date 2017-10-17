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
import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine;
BeaverEngine beaverEngine = engine

def dataVersion = beaverEngine.getDataVersion()
def karafRepository = beaverEngine.getResource("karaf","repository")

// Configure svn agent cfg

def svnAgentCfg = karafRepository + "/org/novaforge/thirdparty/plugins/novaforge-svn-agent-impl/" + dataVersion + "/novaforge-svn-agent-impl-" + dataVersion + "-agent.cfg"
def datas = beaverEngine.getResource("polarion", "datas")
def defaultAlias = beaverEngine.getResource("main", "novaforge-connector-svn","defaultAlias")
def toolAlias = beaverEngine.getResource("main", "novaforge-connector-svn","toolAlias")
beaverEngine.replaceElement(svnAgentCfg, "@SVN_DATAS", datas)
beaverEngine.replaceElement(svnAgentCfg, "@DEFAULT_ALIAS", defaultAlias)
beaverEngine.replaceElement(svnAgentCfg, "@TOOL_ALIAS", toolAlias)

// Copy svn agent cfg to novaforge runtime
def pluginConf = beaverEngine.getResource("novaforge-runtime","pluginsConf")
beaverEngine.copyToFile(svnAgentCfg, pluginConf + "/plugins.svn.agent.cfg")

// Configure svn agent datasource
def mariadbHost = beaverEngine.getResource("mariadb","host")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def datasourceUser = beaverEngine.getResource("novaforge-runtime","datasourceUser")
def datasourcePwd = beaverEngine.getResource("novaforge-runtime","datasourcePwd")
def svnAgentDatasource = karafRepository + "/org/novaforge/thirdparty/plugins/novaforge-svn-agent-impl/" + dataVersion + "/novaforge-svn-agent-impl-" + dataVersion + "-datasource.cfg"
beaverEngine.replaceElement(svnAgentDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(svnAgentDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(svnAgentDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(svnAgentDatasource, "password=root", "password=" + datasourcePwd)

// Copy svn agent cfg to novaforge runtime
def datasourceConf = beaverEngine.getResource("novaforge-runtime","datasourceConf")
beaverEngine.copyToFile(svnAgentDatasource, datasourceConf + "/datasource.svn.agent.cfg")