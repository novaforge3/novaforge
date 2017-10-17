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
import org.apache.commons.codec.digest.DigestUtils

BeaverEngine beaverEngine = engine

def karafHome = beaverEngine.getResource("karaf","home")

beaverEngine.unpackFile(dataFile,karafHome)

// Add Cas binaries
def casHome = beaverEngine.getResource("cas","home")
beaverEngine.copy(processTmpPath + "/resources/cas/bin/", casHome+"/bin")

// Add Karaf  binaries 
def karafBin = beaverEngine.getResource("karaf","bin")
beaverEngine.copy(processTmpPath + "/resources/karaf/bin/", karafBin)
//beaverEngine.replaceExpression(karafBin + "/karaf", "KARAF_DATA=`cygpath --path --windows", "KARAF_DATA=`cygpath --path --unix")

// Karaf conf
def karafRepository = beaverEngine.getResource("karaf","repository")
def karafDatas = beaverEngine.getResource("karaf", "datas")
def karafTmp = beaverEngine.getResource("karaf", "tmp")
def karafEtc = beaverEngine.getResource("karaf", "etc")
def javaHome = beaverEngine.getResource("jre", "home")
def javaKeystore = beaverEngine.getResource("certificat", "jks")
def javaKeystorePass = beaverEngine.getResource("certificat", "jksPwd")
def karafSetEnv = karafHome + "/bin/setenv.bat"
beaverEngine.replaceElement(karafSetEnv,"@KARAF_DATA@", karafDatas)
beaverEngine.replaceElement(karafSetEnv,"@KARAF_TMP@", karafTmp)
beaverEngine.replaceElement(karafSetEnv,"@JAVA_HOME@", javaHome)
beaverEngine.replaceElement(karafSetEnv,"@KEYSTORE@", javaKeystore)
beaverEngine.replaceElement(karafSetEnv,"@STORE_PASS@", javaKeystorePass)
beaverEngine.copy(processTmpPath + "/resources/", karafEtc)
beaverEngine.replaceExpression(karafEtc + "/org.ops4j.pax.url.mvn.cfg", "#org.ops4j.pax.url.mvn.localRepository=(.*)", "org.ops4j.pax.url.mvn.localRepository=" + karafRepository)

// CFG diretories
def forgeConf = beaverEngine.getResource("novaforge-runtime","forgeConf")
def pluginConf = beaverEngine.getResource("novaforge-runtime","pluginsConf")
def datasourceConf = beaverEngine.getResource("novaforge-runtime","datasourceConf")

beaverEngine.createDirectory(pluginConf);
beaverEngine.createDirectory(datasourceConf);

// Repository information
def dataVersion = beaverEngine.getDataVersion()
def modulesRepository = karafRepository + "/org/novaforge/forge/modules/"

def localHost = beaverEngine.getResource("local:host")
def karafPort = beaverEngine.getResource("karaf","port")
def cxfAlias = beaverEngine.getResource("karaf","cxfAlias")
def baseUrl = "http://"+ localHost + ":" + karafPort
def casBaseUrl = beaverEngine.getResource("main", "cas","baseUrl")
def smtpHost = beaverEngine.getResource("smtp","host")
def smtpPort = beaverEngine.getResource("smtp","port")
def smtpUsername = beaverEngine.getResource("smtp","username")
def smtpPassword = beaverEngine.getResource("smtp","password")
def smtpNoReply = beaverEngine.getResource("smtp","noReply")
def adminEmail = beaverEngine.getResource("adminEmail")
def adminLogin = beaverEngine.getResource("adminLogin")

// NovaForge Core Configuration
def forgeProperties = modulesRepository + "novaforge-configuration-impl/" + dataVersion + "/novaforge-configuration-impl-" + dataVersion + ".properties"

def novaforgeProperties = forgeConf + "/novaforge.properties"
beaverEngine.copyToFile(forgeProperties, novaforgeProperties)
beaverEngine.replaceElement(novaforgeProperties, "@PUBLIC_URL@", baseUrl)
beaverEngine.replaceElement(novaforgeProperties, "@KARAF_PORT@", karafPort)
beaverEngine.replaceElement(novaforgeProperties, "@CXF_ALIAS@", cxfAlias)
beaverEngine.replaceElement(novaforgeProperties, "@SERVER_HOST@", localHost)
beaverEngine.replaceElement(novaforgeProperties, "@CAS_URL@", casBaseUrl)
beaverEngine.replaceElement(novaforgeProperties, "@SMTP_HOST@", smtpHost)
beaverEngine.replaceElement(novaforgeProperties, "@SMTP_PORT@",  smtpPort)
beaverEngine.replaceElement(novaforgeProperties, "@SMTP_USERNAME@", smtpUsername)
beaverEngine.replaceElement(novaforgeProperties, "@SMTP_PASSWORD@", smtpPassword)
beaverEngine.replaceElement(novaforgeProperties, "@SMTP_NOREPLY@", smtpNoReply)
beaverEngine.replaceElement(novaforgeProperties, "@MAIL_SYSTEM_FORGE@", adminEmail)
beaverEngine.replaceElement(forgeProperties, "@LOGIN_ADMIN_FORGE@", adminLogin)

// NovaForge Initialization
def iniProperties = modulesRepository + "novaforge-initialization/" + dataVersion + "/novaforge-initialization-" + dataVersion + ".properties"
def novaforgeInitProperties = forgeConf + "/novaforge-init.properties"
beaverEngine.copyToFile(iniProperties, novaforgeInitProperties)

def adminPwd = beaverEngine.getResource("adminPwd")
String sha1password = DigestUtils.sha1Hex(adminPwd);

beaverEngine.replaceElement(novaforgeInitProperties, "@LOGIN_ADMIN_FORGE@", adminLogin)
beaverEngine.replaceElement(novaforgeInitProperties, "@MAIL_ADMIN_FORGE@", adminEmail)
beaverEngine.replaceElement(novaforgeInitProperties, "@PASSWORD_ADMIN@", sha1password)

// NovaForge Datasource
def repoDatasourceXml = modulesRepository + "novaforge-core-organization-dao/" + dataVersion + "/novaforge-core-organization-dao-" + dataVersion + "-datasource.xml"
def karafDeploy = beaverEngine.getResource("karaf","deploy")
def datasourceXml = karafDeploy + "/datasource.novaforge.xml"
beaverEngine.copyToFile(repoDatasourceXml, datasourceXml)

// Set up permissions
def localUser = beaverEngine.getResource("local:user")
def localGroup = beaverEngine.getResource("local:group")
beaverEngine.setOwner(true,localGroup,localUser,karafHome)