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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

BeaverEngine beaverEngine = engine

def deployFile = beaverEngine.getResource("deployFile")
def serverId = beaverEngine.getServerId()

def previousNovaforgeVersion = beaverEngine.getPreviousResource("novaforge-version")
def novaforgeVersion = beaverEngine.getResource("novaforge-version")
beaverEngine.replaceElement(deployFile, previousNovaforgeVersion, novaforgeVersion)

// Configure feature cfg
def karafEtc = beaverEngine.getResource("karaf","etc")
def featureCfg = karafEtc + "/org.apache.karaf.features.cfg"

beaverEngine.replaceElement(featureCfg, previousNovaforgeVersion, novaforgeVersion)

// Suppression de novaforeg-security
if ("svn".equals(serverId)) {
  beaverEngine.replaceExpression(featureCfg, "featuresBoot=.*","featuresBoot=config,standard,region,package,ssh,management,wrapper,jndi,jpa,transaction,eventadmin,war,http-whiteboard,blueprint-web,webconsole,openjpa;version=2.3.0,cxf;version=2.7.11,novaforge-jms;version=" + beaverEngine.getMyPreviousVersion())
}
// Suppression de openjpa,cxf,novaforge-jms,novaforge-security
else if (("ged".equals(serverId)) || ("pic".equals(serverId)) ) {
  beaverEngine.replaceExpression(featureCfg, "featuresBoot=.*","featuresBoot=config,standard,region,package,ssh,management,wrapper,jndi,jpa,transaction,eventadmin,war,http-whiteboard,blueprint-web,webconsole")
}

def karafDatas = beaverEngine.getResource("karaf","datas")
final Path karafDatasPath = Paths.get(karafDatas);
if (Files.exists(karafDatasPath))
{
  beaverEngine.delete(karafDatas)
}

beaverEngine.createDirectory(karafDatas);
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
beaverEngine.setOwner(true,localGroup,localUser,deployFile)
beaverEngine.setOwner(true,localGroup,localUser,featureCfg)
beaverEngine.setOwner(true,localGroup,localUser,karafDatas)

