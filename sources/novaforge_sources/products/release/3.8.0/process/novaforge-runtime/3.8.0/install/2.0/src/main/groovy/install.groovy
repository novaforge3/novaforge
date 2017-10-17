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
 * grant you additional permission to convey the resulting work
 */
import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService

BeaverEngine beaverEngine = engine


// Copy correct deployment file
def deployFile = beaverEngine.getResource("deployFile")
def serverId = beaverEngine.getServerId()

if (("portal".equals(serverId)) || ("aio".equals(serverId)) || ("dev".equals(serverId)) ) {
  beaverEngine.copyToFile(processTmpPath + "/resources/deploy/portal.xml", deployFile)
}
else if ("svn".equals(serverId)) {
  beaverEngine.copyToFile(processTmpPath + "/resources/deploy/svn.xml", deployFile)
}
else {
  beaverEngine.copyToFile(processTmpPath + "/resources/deploy/log.xml", deployFile)
}

def novaforgeVersion = beaverEngine.getResource("novaforge-version")
beaverEngine.replaceElement(deployFile, "@NOVAFORGE_VERSION@", novaforgeVersion)

// Copy etc files
def karafEtc = beaverEngine.getResource("karaf","etc")
beaverEngine.copy(processTmpPath + "/resources/etc", karafEtc)

// Configure feature cfg
def featureCfg = karafEtc + "/org.apache.karaf.features.cfg"
def openjpaVersion = beaverEngine.getResource("openjpa-version")
def cxfVersion = beaverEngine.getResource("cxf-version")
def karafVersion = beaverEngine.getResource("karaf-version")
def paxwebVersion = beaverEngine.getResource("paxweb-version")
beaverEngine.replaceElement(featureCfg, "@NOVAFORGE_VERSION@", novaforgeVersion)
beaverEngine.replaceElement(featureCfg, "@OPENJPA_VERSION@", openjpaVersion)
beaverEngine.replaceElement(featureCfg, "@CXF_VERSION@", cxfVersion)
beaverEngine.replaceElement(featureCfg, "@KARAF_VERSION@", karafVersion)
beaverEngine.replaceElement(featureCfg, "@PAXWEB_VERSION@", paxwebVersion)

// Suppression de novaforge-security
if ("svn".equals(serverId)) {
  beaverEngine.replaceExpression(featureCfg, "featuresBoot=.*","featuresBoot=config,standard,region,package,ssh,management,wrapper,jndi,jpa,transaction,eventadmin,war,http-whiteboard,blueprint-web,webconsole,openjpa;version="+openjpaVersion+",cxf;version="+cxfVersion+",novaforge-jms;version="+novaforgeVersion)
}
// Suppression de openjpa,cxf,novaforge-jms,novaforge-security
else if (("ged".equals(serverId)) || ("pic".equals(serverId)) ) {
  beaverEngine.replaceExpression(featureCfg, "featuresBoot=.*","featuresBoot=config,standard,region,package,ssh,management,wrapper,jndi,jpa,transaction,eventadmin,war,http-whiteboard,blueprint-web,webconsole")
}

// Configure file install cfg
def datasourceCfg = karafEtc + "/org.apache.felix.fileinstall-datasource.cfg"
def datasourceConf = beaverEngine.getResource("datasourceConf")
beaverEngine.replaceElement(datasourceCfg, "@DATASOURCE_CONF@", datasourceConf)
def forgeCfg = karafEtc + "/org.apache.felix.fileinstall-forge.cfg"
def forgeConf = beaverEngine.getResource("forgeConf")
beaverEngine.replaceElement(forgeCfg, "@FORGE_CONF@", forgeConf)
def pluginsCfg = karafEtc + "/org.apache.felix.fileinstall-plugins.cfg"
def pluginsConf = beaverEngine.getResource("pluginsConf")
beaverEngine.replaceElement(pluginsCfg, "@PLUGINS_CONF@", pluginsConf)

// Configure jks
def karafBin = beaverEngine.getResource("karaf","bin")
def jks = beaverEngine.getResource("certificat","jks")
def jksPwd = beaverEngine.getResource("certificat","jksPwd")
def setEnv = karafBin + "/setenv"
def keyStore="-Djavax.net.ssl.keyStore=" + jks
def keyStorePassword="-Djavax.net.ssl.keyStorePassword=" + jksPwd
def trustStore="-Djavax.net.ssl.trustStore=" + jks
def trustStorePassword="-Djavax.net.ssl.trustStorePassword=" + jksPwd
beaverEngine.replaceElement(setEnv, "KARAF_OPTS=\"", "KARAF_OPTS=\"" + keyStore + " "+keyStorePassword+" "+trustStore+" "+trustStorePassword+" ")

// Configure sql
def sqlFile = processTmpPath + "/resources/sql/user_create.sql"
def datasourceUser = beaverEngine.getResource("datasourceUser")
def datasourcePwd = beaverEngine.getResource("datasourcePwd")
def mariadbHost = beaverEngine.getResource("mariadb", "host")
beaverEngine.replaceElement(sqlFile, "@NOVAFORGE_USER@", datasourceUser)
beaverEngine.replaceElement(sqlFile, "@NOVAFORGE_PWD@", datasourcePwd)
beaverEngine.replaceElement(sqlFile, "@MARIADB_HOST@", mariadbHost)

def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def mariadbUser = beaverEngine.getResource("mariadb", "rootUser")
def mariadbPwd = beaverEngine.getResource("mariadb", "rootPwd")
def mariadbPort = beaverEngine.getResource("mariadb", "port")
def mariadbBin = beaverEngine.getResource("mariadb", "bin")
SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.startService(mariadbService)
beaverEngine.executeMysqlScript(mariadbBin,mariadbPort, mariadbUser, mariadbPwd, sqlFile)
systemdService.stopService(mariadbService)

//Owner
def karafHome = beaverEngine.getResource("karaf","home")
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
beaverEngine.setOwner(true,localGroup,localUser,karafHome)
