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

def limesurveyCfg = pluginsRepository + "novaforge-limesurvey-cfg/" + dataVersion + "/novaforge-limesurvey-cfg-" + dataVersion + "-plugins.cfg"

def description = beaverEngine.getResource("description")
def clientHost = beaverEngine.getResource("clientHost")
def clientAdmin = beaverEngine.getResource("clientAdmin")
def clientPwd = beaverEngine.getResource("clientPwd")
def defaultAlias = beaverEngine.getResource("defaultAlias")
def toolAlias = beaverEngine.getResource("toolAlias")
def serverConf = beaverEngine.getResource("serverConf")

beaverEngine.replaceElement(limesurveyCfg, "@DESCRIPTION", description)
beaverEngine.replaceElement(limesurveyCfg, "@CLIENT_HOST", clientHost)
beaverEngine.replaceElement(limesurveyCfg, "@CLIENT_ADMIN", clientAdmin)
beaverEngine.replaceElement(limesurveyCfg, "@CLIENT_PWD", clientPwd)
beaverEngine.replaceElement(limesurveyCfg, "@DEFAULT_ALIAS", defaultAlias)
beaverEngine.replaceElement(limesurveyCfg, "@TOOL_ALIAS", toolAlias)
beaverEngine.replaceElement(limesurveyCfg, "@SERVER_CONF", serverConf)

// Copy Limesurvey cfg to novaforge runtime
beaverEngine.copyToFile(limesurveyCfg, pluginConf + "/plugins.limesurvey.cfg")

// Configure Limesurvey datasource
def limesurveyDatasource = pluginsRepository + "novaforge-limesurvey-impl/" + dataVersion + "/novaforge-limesurvey-impl-" + dataVersion + "-datasource.cfg"
beaverEngine.replaceElement(limesurveyDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(limesurveyDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(limesurveyDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(limesurveyDatasource, "password=root", "password=" + datasourcePwd)

// Copy Limesurvey datasource to novaforge runtime
beaverEngine.copyToFile(limesurveyDatasource, datasourceConf + "/datasource.limesurvey.cfg")