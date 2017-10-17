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

// CFG directories
def pluginConf = beaverEngine.getResource("novaforge-runtime","pluginsConf")
def datasourceConf = beaverEngine.getResource("novaforge-runtime","datasourceConf")

// Repository information
def karafRepository = beaverEngine.getResource("karaf","repository")
def dataVersion = beaverEngine.getDataVersion()
def pluginsRepository = karafRepository + "/org/novaforge/forge/plugins/"


// **************** Requirements Configuration
def requirementsCfg = pluginsRepository + "novaforge-requirements-manager-cfg/" + dataVersion + "/novaforge-requirements-manager-cfg-" + dataVersion + "-plugins.cfg"

// Setting values for Parsing process
def description = beaverEngine.getResource("description")
def datas = beaverEngine.getResource("datas")
def parserValue = beaverEngine.getResource("parserValue")
def codeHandlerType = beaverEngine.getResource("codeHandlerType")
def fileExtensions = beaverEngine.getResource("fileExtensions")
def excelFileMaxSize = beaverEngine.getResource("excelFileMaxSize")
beaverEngine.replaceElement(requirementsCfg, "@DESCRIPTION", description)
beaverEngine.replaceElement(requirementsCfg, "@PATH_DATA", datas)
// Using replaceElement instead of replaceExpression cause concatenation in replaceExpression escape special chars like anti slash
beaverEngine.replaceElement(requirementsCfg, "@PARSER_VALUE", parserValue)
beaverEngine.replaceExpression(requirementsCfg, "codeHandlerType=(.*)", "codeHandlerType=" + codeHandlerType)
beaverEngine.replaceExpression(requirementsCfg, "fileExtensions=(.*)", "fileExtensions=" + fileExtensions)
beaverEngine.replaceExpression(requirementsCfg, "excelFileMaxSize=(.*)", "excelFileMaxSize=" + excelFileMaxSize)

// Copy Requirements cfg to novaforge runtime
def cdoConnectorCfg = pluginsRepository + "novaforge-requirements-tool-common-connectors/" + dataVersion + "/novaforge-requirements-tool-common-connectors-" + dataVersion + "-cdo.cfg"
def svnConnectorCfg = pluginsRepository + "novaforge-requirements-tool-common-connectors/" + dataVersion + "/novaforge-requirements-tool-common-connectors-" + dataVersion + "-svn.cfg"
def excelConnectorCfg = pluginsRepository + "novaforge-requirements-tool-common-connectors/" + dataVersion + "/novaforge-requirements-tool-common-connectors-" + dataVersion + "-excel.cfg"
beaverEngine.copyToFile(cdoConnectorCfg, pluginConf + "/requirements.cdo.connector.cfg")
beaverEngine.copyToFile(svnConnectorCfg, pluginConf + "/requirements.svn.connector.cfg")
beaverEngine.copyToFile(excelConnectorCfg, pluginConf + "/requirements.excel.connector.cfg")
beaverEngine.copyToFile(requirementsCfg, pluginConf + "/plugins.requirements.cfg")

// Configure requirements datasource
def mariadbHost = beaverEngine.getResource("mariadb","host")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def datasourceUser = beaverEngine.getResource("novaforge-runtime","datasourceUser")
def datasourcePwd = beaverEngine.getResource("novaforge-runtime","datasourcePwd")
def requirementsToolDatasource = pluginsRepository + "novaforge-requirements-tool-common-impl/" + dataVersion + "/novaforge-requirements-tool-common-impl-" + dataVersion + "-datasource.cfg"
def requirementsPluginDatasource = pluginsRepository + "novaforge-requirements-manager-impl/" + dataVersion + "/novaforge-requirements-manager-impl-" + dataVersion + "-datasource.cfg"
beaverEngine.replaceElement(requirementsToolDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(requirementsToolDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(requirementsToolDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(requirementsToolDatasource, "password=root", "password=" + datasourcePwd)
beaverEngine.replaceElement(requirementsPluginDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(requirementsPluginDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(requirementsPluginDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(requirementsPluginDatasource, "password=root", "password=" + datasourcePwd)

// Copy Requirements datasource to novaforge runtime
beaverEngine.copyToFile(requirementsToolDatasource, datasourceConf + "/datasource.tool.requirements.cfg")
beaverEngine.copyToFile(requirementsPluginDatasource, datasourceConf + "/datasource.requirements.cfg")