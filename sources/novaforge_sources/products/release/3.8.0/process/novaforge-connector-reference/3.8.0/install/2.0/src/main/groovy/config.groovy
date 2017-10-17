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

// Repository information
def karafRepository = beaverEngine.getResource("karaf","repository")
def dataVersion = beaverEngine.getDataVersion()
def modulesRepository = karafRepository + "/org/novaforge/forge/modules/"

// Reference Configuration
def localUser = beaverEngine.getResource("local:user")
def localDatas = beaverEngine.getResource("local:datas")
def referenceCfg = modulesRepository + "novaforge-reference-impl/" + dataVersion + "/novaforge-reference-impl-" + dataVersion + ".cfg"
beaverEngine.replaceElement(referenceCfg, "@NOVA_DATA", localDatas)
beaverEngine.replaceElement(referenceCfg, "@NOVAFORGE_USER", localUser)

// Initialization configuration
def iniProperties = modulesRepository + "novaforge-initialization/" + dataVersion + "/novaforge-initialization-" + dataVersion + ".properties"

// Reference project configuration
def referenceName = beaverEngine.getResource("referenceName")
def referenceDescription = beaverEngine.getResource("referenceDescription")
def referenceLicence = beaverEngine.getResource("referenceLicence")

beaverEngine.replaceExpression(iniProperties, "referentielCreated=(.*)", "referentielCreated=true")
beaverEngine.replaceExpression(iniProperties, "referentielProjectName=(.*)", "referentielProjectName=" + referenceName)
beaverEngine.replaceExpression(iniProperties, "referentielProjectDescription=(.*)", "referentielProjectDescription=" + referenceDescription)
beaverEngine.replaceExpression(iniProperties, "referentielProjectLicence=(.*)", "referentielProjectLicence=" + referenceLicence)

// Forge roles configuration
def referenceRoleMember = beaverEngine.getResource("referenceRoleMember")
beaverEngine.replaceExpression(iniProperties, "referentielMemberRoleName=(.*)", "referentielMemberRoleName=" + referenceRoleMember)

def forgeConf = beaverEngine.getResource("novaforge-runtime","forgeConf")
def novaforgeInitProperties = forgeConf + "/novaforge-init.properties"
beaverEngine.copyToFile(iniProperties, novaforgeInitProperties)