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

// Repository information
def karafRepository = beaverEngine.getResource("karaf","repository")
def dataVersion = beaverEngine.getDataVersion()
def modulesRepository = karafRepository + "/org/novaforge/forge/modules/"

// Distribution Configuration
def reportingCfg = modulesRepository + "novaforge-distribution-reporting-client/" + dataVersion + "/novaforge-distribution-reporting-client-" + dataVersion + "-client.cfg"
def registerCfg = modulesRepository + "novaforge-distribution-register-client/" + dataVersion + "/novaforge-distribution-register-client-" + dataVersion + ".cfg"
def urlDirectory = beaverEngine.getResource("urlDirectory")
def label = beaverEngine.getResource("label")
def description = beaverEngine.getResource("description")
def level = beaverEngine.getResource("level")
beaverEngine.replaceElement(reportingCfg, "@URLDIRECTORY", urlDirectory)
beaverEngine.replaceElement(registerCfg, "@URLDIRECTORY", urlDirectory)
beaverEngine.replaceElement(registerCfg, "@FORGELABEL", label)
beaverEngine.replaceElement(registerCfg, "@FORGEDESCRIPTION", description)
beaverEngine.replaceElement(registerCfg, "@FORGELEVEL", level)