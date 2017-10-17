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

def mainHost = beaverEngine.getResource("main:host")

def karafRepository = beaverEngine.getResource("karaf","repository")
def dataVersion = beaverEngine.getDataVersion()

// Configure log agent cfg
def logCfg = karafRepository + "/org/novaforge/forge/modules/novaforge-commons-log-impl/" + dataVersion + "/novaforge-commons-log-impl-" + dataVersion + ".cfg"

def serverLogs = beaverEngine.getResource("local:logs")
beaverEngine.replaceElement(logCfg, "@SERVER_LOGS", serverLogs)

def serverId = beaverEngine.getServerId()
def serverProfile = "[{'name': 'Local'}]"
if ("portal".equals(serverId)) {
  serverProfile = "[{'name': 'Portal'}"
  def karafPort = beaverEngine.getResource("karaf","port")
  def servers = beaverEngine.getResource("servers").split("[;,:|]")
  servers.each() {
    server -> addServer:{
      server = server.trim()
      if (serverId.equals(server) == false) {
        def serverHost = beaverEngine.getResource(server+":host")
        serverProfile += ", {'name': '" +server.toUpperCase() + "', 'url':'http://" + serverHost +":" + karafPort+ "/technical-logs/'}"
      }
    }
  }
  serverProfile += "]"
}
beaverEngine.replaceElement(logCfg, "@SERVER_PROFILE@", serverProfile)

// Copy log cfg to novaforge runtime
def forgeConf = beaverEngine.getResource("novaforge-runtime","forgeConf")
beaverEngine.copyToFile(logCfg, forgeConf + "/commons.logtechnical.cfg")