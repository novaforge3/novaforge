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
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService
import org.codehaus.plexus.util.StringUtils

BeaverEngine beaverEngine = engine

// Copy new jar
def karafRepository = beaverEngine.getResource("karaf","repository")
beaverEngine.unpackFile(dataFile,karafRepository)

// Configure novaforge-runtime deploy file
def dataVersion = beaverEngine.getDataVersion()
def deployFile = beaverEngine.getResource("novaforge-runtime", "deployFile")

// Update feature version or add feature if it is missing
def newFeature = "<feature version=\"" + dataVersion + "\">novaforge-plugins-jenkins</feature>"
def jenkinsFeature = beaverEngine.getValueFromRegex(deployFile, "<feature version=\"(.*)\">novaforge-plugins-jenkins</feature>")
if (StringUtils.isNotBlank(jenkinsFeature)) {
  beaverEngine.replaceElement(deployFile, jenkinsFeature, newFeature)
} else {
  def pluginsToken = beaverEngine.getResource("novaforge-runtime", "pluginsToken")
  def lineAdded = beaverEngine.addLineToFile(deployFile,pluginsToken,newFeature)
  if(lineAdded == 0) {
    throw new org.novaforge.beaver.exception.BeaverException("Can't find token \"" + pluginsToken + "\" in the file " + deployFile);
  }
}
