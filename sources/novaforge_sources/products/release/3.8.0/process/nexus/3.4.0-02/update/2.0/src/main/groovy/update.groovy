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
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.codehaus.plexus.util.StringUtils

BeaverEngine beaverEngine = engine

// Extract nexus
def version = "2.14.4-03"
def versionData = version + "_0"
def nexusHome = beaverEngine.getResource("home")
Path nexusHomePath = Paths.get(nexusHome)

// - nexus service
def nexusSystemd = beaverEngine.getResource("systemdService")
SystemdService systemdService = beaverEngine.getSystemdService()
def nexusService = beaverEngine.getResource("nexus", "systemdService")
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
def productLog = beaverEngine.getResource("product.logs")
def nexusLogFile = productLog + "/wrapper.log"

/******************************************************************************************************************
* BEGIN install intermediate version
******************************************************************************************************************/

def nexus214Home = beaverEngine.getResource("product.214home")
def nexus214Log = beaverEngine.getResource("product.214logs")
def nexus214Datas = beaverEngine.getResource("product.214datas")
def nexus214LogFile = nexus214Log + "/wrapper.log"

beaverEngine.createDirectory(nexus214Home)
beaverEngine.createDirectory(nexus214Log)

def distFile214  = beaverEngine.getResource("distFile214")
Path dataFilePar = Paths.get(dataFile).getParent()
String dataFile214 = dataFilePar.toString() + "/../../" + distFile214
def processTmpPath214 = processTmpPath + "/distFile214";

//save previous wrapper.conf and nexus.properties config file (used later for intermediate version)
def nexusTmp = beaverEngine.getResource("tmp")
Path nexusTmpPath = Paths.get(nexusTmp)
if (!Files.exists(nexusTmpPath))
{
  beaverEngine.createDirectory(nexusTmp)  
}
beaverEngine.copy(nexusHome + "/bin/jsw/conf/wrapper.conf", nexusTmp)
beaverEngine.copy(nexusHome + "/conf/nexus.properties", nexusTmp)

//install intermediate version
beaverEngine.unpackFile(dataFile214, processTmpPath214)
String processTmpPath214Resources = processTmpPath214+"/nexus-distrib-mig-2_14-2.14.4-03_0/nexus-bundle-template-2.14.4-03"
String oobNexusDistrib=processTmpPath214Resources + "/nexus-2.14.4-03-bundle.tar.gz"
beaverEngine.unpackFile(oobNexusDistrib, processTmpPath214+"/nexus")

//retrieve previous configuration files

beaverEngine.copy(processTmpPath214+"/nexus/nexus-2.14.4-03", nexus214Home)
//copy web.xml
beaverEngine.copy(processTmpPath214Resources +"/nexus/WEB-INF/web.xml", nexus214Home+"/nexus/WEB-INF/")

//restore previous version config files
def wrapperConf = nexus214Home + "/bin/jsw/conf/wrapper.conf"
beaverEngine.copyToFile(nexusTmp+"/wrapper.conf", wrapperConf)
beaverEngine.replaceExpression(wrapperConf, "wrapper.logfile=(.*)", "wrapper.logfile=" + nexus214LogFile)
// Upgrade Perm Size to 192 Mo (needed to create upgrade agent capability)
def javaAdd6 = beaverEngine.getValueFromRegex(wrapperConf, "wrapper.java.additional.6=-Djavax.net.ssl.trustStorePassword(.*)")
if (StringUtils.isNotBlank(javaAdd6)) {
  beaverEngine.addLineToFile(wrapperConf,javaAdd6,"wrapper.java.additional.7=-XX:MaxPermSize=192m")
} else {
  throw new org.novaforge.beaver.exception.BeaverException("Can't find pattern \"wrapper.java.additional.6=-Djavax.net.ssl.trustStorePassword\" in the file " + wrapperConf);
}

//change port & data directory in nexus.properties
beaverEngine.copyToFile(nexusTmp+"/nexus.properties", nexus214Home + "/conf/nexus.properties")
beaverEngine.replaceExpression(nexus214Home + "/conf/nexus.properties", "application-port=(.*)", "application-port=28081")
beaverEngine.replaceExpression(nexus214Home + "/conf/nexus.properties", "nexus-work=(.*)", "nexus-work=" + nexus214Datas + "/sonatype-work/nexus")

def nexusDatas = beaverEngine.getResource("datas")
beaverEngine.moveDir(nexusDatas,nexus214Datas)
def securityConfigurationConfFile = nexus214Datas + "/conf/security-configuration.xml"
beaverEngine.copyToFile(processTmpPath214Resources + "/working-dir/nexus/conf/security-configuration.xml", securityConfigurationConfFile )

//update admin password to default (admin123)
def xmlFile = nexus214Datas +"/conf/security.xml"
def security = new groovy.util.XmlParser().parse(xmlFile)
def users = security.users[0].user.findAll{ u ->
	u.id[0].text() == "admin"
}.each { u ->
	u.password[0].value = '$shiro1$SHA-512$1024$k85QfCFJkn2V3Aliz45Nkw==$iVYD12IgyIHrLAP0SgGFN7aGWpIUhRpsUmG/k6UD/tO1hNv/isI99SyARpuiiqRl6kpazfRzaCrxZvR5zYhWzg=='
}	
new  groovy.util.XmlNodePrinter(new PrintWriter(new FileWriter(xmlFile))).print(security)

// Set owner and rights to 
beaverEngine.setOwner(true, localGroup, localUser, nexus214Home)
beaverEngine.setOwner(true, localGroup, localUser, nexus214Log)
beaverEngine.setOwner(true, localGroup, localUser, nexus214Datas)


/******************************************************************************************************************
* END OF install intermediate version
******************************************************************************************************************/
 
 
/******************************************************************************************************************
* install new version
******************************************************************************************************************/
def nexusDistrib=  "nexus-distrib-3.4.0-02_0" 

beaverEngine.unpackFile(dataFile, processTmpPath)

// Create nexus datas
Path nexusDatasPath = Paths.get(nexusDatas)
if (Files.exists(nexusDatasPath))
{
  beaverEngine.delete(nexusDatas)  
}
beaverEngine.createDirectory(nexusDatas)
//move sonatype dir to datas
beaverEngine.moveDir(processTmpPath + "/" + nexusDistrib + "/sonatype-work/nexus3/", nexusDatas)

//remove previous version
if (Files.exists(nexusHomePath))
{
  beaverEngine.delete(nexusHome) 
}
beaverEngine.createDirectory(nexusHome)
beaverEngine.moveDir(processTmpPath + "/" + nexusDistrib + "/nexus-3.4.0-02/", nexusHome)

// Create nexus tmp
nexusTmpPath = Paths.get(nexusTmp)
if (Files.exists(nexusTmpPath))
{
  beaverEngine.delete(nexusTmp)  
}
beaverEngine.createDirectory(nexusTmp)

// Create nexus logs
def nexusLogs = beaverEngine.getResource("logs")
Path nexusLogsPath = Paths.get(nexusLogs)
if (Files.exists(nexusLogsPath))
{
  beaverEngine.delete(nexusLogs)  
}
beaverEngine.createDirectory(nexusLogs)
