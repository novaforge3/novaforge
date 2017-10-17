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

// Global variable
def localUser = beaverEngine.getResource("local:user")

// Configure karaf
def karafHome = beaverEngine.getResource("home")
def karafEtc = beaverEngine.getResource("etc")
def karafBin = beaverEngine.getResource("bin")

beaverEngine.copy(processTmpPath + "/resources/bin", karafBin)
beaverEngine.copy(processTmpPath + "/resources/etc", karafEtc)

def karafDatas = beaverEngine.getResource("datas")
def karafTmp = beaverEngine.getResource("tmp")
def javaHome = beaverEngine.getResource("jre", "home")
def karafSetEnv = karafBin + "/setenv"
beaverEngine.replaceElement(karafSetEnv,"@KARAF_DATA@", karafDatas)
beaverEngine.replaceElement(karafSetEnv,"@KARAF_TMP@", karafTmp)
beaverEngine.replaceElement(karafSetEnv,"@JAVA_HOME@", javaHome)

def karafLogs = beaverEngine.getResource("logs")
def karafEtcLog = karafEtc + "/org.ops4j.pax.logging.cfg"
beaverEngine.replaceElement(karafEtcLog,"@KARAF_LOG@", karafLogs)

def karafPort = beaverEngine.getResource("port")
def karafEtcWeb = karafEtc + "/org.ops4j.pax.web.cfg"
beaverEngine.replaceElement(karafEtcWeb,"@KARAF_PORT@", karafPort)
def karafEtcJetty = karafEtc + "/jetty.xml"
beaverEngine.replaceElement(karafEtcJetty,"@KARAF_PORT@", karafPort)

// Karaf console admin
def karafEtcUser = karafEtc + "/users.properties"
def adminUser = beaverEngine.getResource("adminUser")
def adminPwd = beaverEngine.getResource("adminPwd")
beaverEngine.copyToFile(processTmpPath + "/resources/users.properties", karafEtcUser)
beaverEngine.replaceElement(karafEtcUser, "@ADMIN_USER@", adminUser)
beaverEngine.replaceElement(karafEtcUser, "@ADMIN_PWD@", adminPwd)

// Configure karaf exec
def localBin = beaverEngine.getResource("local:bin")
def karafBinFile = localBin + "/karaf"
beaverEngine.copy(processTmpPath + "/resources/karaf", localBin)
beaverEngine.replaceElement(karafBinFile,"@NOVAFORGE_USER@", localUser)
beaverEngine.replaceElement(karafBinFile,"@KARAF_HOME@", karafHome)


// Set up permissions
def localGroup = beaverEngine.getResource("local:group")
beaverEngine.setOwner(true,localGroup,localUser,karafBinFile)
beaverEngine.setOwner(true,localGroup,localUser,karafHome)
beaverEngine.setPermissionsOnFiles(false,"755", karafBin)
beaverEngine.setPermissionsOnFiles(false,"755", karafBinFile)