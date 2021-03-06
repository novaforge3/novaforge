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
import org.codehaus.plexus.util.StringUtils;

BeaverEngine beaverEngine = engine
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")

// Copy new jar
def karafRepository = beaverEngine.getResource("karaf","repository")
beaverEngine.unpackFile(dataFile,karafRepository)

// Configure novaforge-runtime deploy file
def deployFile = beaverEngine.getResource("novaforge-runtime", "deployFile")

def updateFeatureVersion(deployFile, beaverEngine, feature)
{
  def dataVersion = beaverEngine.getDataVersion()
  def newFeature = "<feature version=\"" + dataVersion + "\">" + feature + "</feature>"
  def oldFeature = beaverEngine.getValueFromRegex(deployFile, "<feature version=\"(.*)\">" + feature + "</feature>")
  if (StringUtils.isBlank(oldFeature)) {
    def pluginsToken = beaverEngine.getResource("novaforge-runtime", "pluginsToken")
    def lineAdded = beaverEngine.addLineToFile(deployFile,pluginsToken,newFeature)
    if(lineAdded == 0) {
      throw new org.novaforge.beaver.exception.BeaverException("Can't find token \"" + pluginsToken + "\" in the file " + deployFile);
    }
  } else {
    throw new org.novaforge.beaver.exception.BeaverException("The feature \"" + oldFeature + "\" is already into the file " + deployFile);
  }
}

// Update feature version or add feature if it is missing
updateFeatureVersion(deployFile, beaverEngine, "novaforge-itests")
updateFeatureVersion(deployFile, beaverEngine, "novaforge-import-export")

////////////////////////////////////////////////////////////////////
//////////////// add Jacoco options within karaf_opts
////////////////////////////////////////////////////////////////////

def karafBin = beaverEngine.getResource("karaf","bin")
def setEnv = karafBin + "/setenv"

def jacocoKey = "-javaagent:"

def jacocoAgentJar = processTmpPath + "/resources/jacocoagent.jar"

def destfileKey="=destfile="

// /datas/novaforge3/datas/jacoco
def jacocoDatas = beaverEngine.getResource("jacocoDatas")
def jacocoDatasJar = jacocoDatas + "/jar"
def jacocoDatasJarFile = jacocoDatasJar + "/jacocoagent.jar"

def jacocoReports = jacocoDatas + "/reports"
def jacocoExec = jacocoReports + "/jacoco.exec"
def jacocoClasses = jacocoReports + "/jacococlasses"

def appendOption = ",append=true"
def includesOption = ",includes=org.novaforge.*"
def dumpOption = ",dumponexit=true"
def outputOption = ",output=file"
def classDumpDirOption = ",classdumpdir=" + jacocoClasses

jacocoValue = jacocoDatasJarFile + destfileKey + jacocoExec + appendOption + includesOption + dumpOption + outputOption + classDumpDirOption

beaverEngine.replaceElement(setEnv, "KARAF_OPTS=\"", "KARAF_OPTS=\"" + jacocoKey + jacocoValue + " ")


////////////////////////////////////////////////////////////////////
//////////////// create folders into jacoco datas
////////////////////////////////////////////////////////////////////
//create jar
beaverEngine.createDirectory(jacocoDatasJar)

//create reports 
beaverEngine.createDirectory(jacocoReports)

//create jacococlasses under reports
beaverEngine.createDirectory(jacocoClasses)

////////////////////////////////////////////////////////////////////
//////////////// copy jacoco agent jar
////////////////////////////////////////////////////////////////////
beaverEngine.copyToFile(jacocoAgentJar, jacocoDatasJarFile)

////////////////////////////////////////////////////////////////////
/////////////////  Owner on jacoco data  ///////////////////////////////
////////////////////////////////////////////////////////////////////
beaverEngine.setOwner(true, localGroup, localUser, jacocoDatas)

