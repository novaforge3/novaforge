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
import org.novaforge.beaver.deployment.plugin.deploy.engine.RepositoryService
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

BeaverEngine beaverEngine = engine

def home = beaverEngine.getResource("home")
// Unpack the archive rpms.tar.gz which contains all rpms
beaverEngine.unpackFile(dataFile, processTmpPath)
def rpmsArchiveFile = processTmpPath + "/" + "rpms.tar.gz"
def alfrescoArchiveFile = processTmpPath + "/alfresco.tar.gz"

// Install RPMs
RepositoryService repositoryService = engine.getRepositoryService()
repositoryService.installRPMs(rpmsArchiveFile, "giflib", "swftools", "ImageMagick")

// Install Alfresco engine
Path homePath = Paths.get(home)
if (Files.exists(homePath))
{
  beaverEngine.delete(home)  
}
def localEngines = beaverEngine.getResource("local:engines")
beaverEngine.unpackFile(alfrescoArchiveFile,localEngines)

// Create directories
def datas = beaverEngine.getResource("datas")
Path datasPath = Paths.get(datas)
if (Files.exists(datasPath))
{
  beaverEngine.delete(datas)  
}
beaverEngine.createDirectory(datas)

// Create Solr data directory
def solrDatas = beaverEngine.getResource("solrDatas")
Path solrDatasPath = Paths.get(solrDatas)
if (Files.exists(solrDatasPath))
{
  beaverEngine.delete(solrDatas)  
}
beaverEngine.createDirectory(solrDatas)

def logs = beaverEngine.getResource("logs")
Path logsPath = Paths.get(logs)
if (Files.exists(logsPath))
{
  beaverEngine.delete(logs)  
}
beaverEngine.createDirectory(logs)

def tmp = beaverEngine.getResource("tmp")
Path tmpPath = Paths.get(tmp)
if (Files.exists(tmpPath))
{
  beaverEngine.delete(tmp)  
}
beaverEngine.createDirectory(tmp)

// Set owner and rights
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
beaverEngine.setOwner(true, localGroup, localUser, home)
beaverEngine.setOwner(true, localGroup, localUser, datas)
beaverEngine.setOwner(true, localGroup, localUser, logs)
beaverEngine.setOwner(true, localGroup, localUser, tmp)

// Create database alfresco
def sqlFile = processTmpPath + "/resources/sql/alfresco.sql"
def alfrescoSQLUser = beaverEngine.getResource("sqlUser")
def alfrescoSQLPwd = beaverEngine.getResource("sqlPwd")
def mariadbHost = beaverEngine.getResource("mariadb", "host")
def database = beaverEngine.getResource("database")
beaverEngine.replaceElement(sqlFile, "@ALFRESCO_USER@", alfrescoSQLUser)
beaverEngine.replaceElement(sqlFile, "@ALFRESCO_PWD@", alfrescoSQLPwd)
beaverEngine.replaceElement(sqlFile, "@MARIADB_HOST@", mariadbHost)
beaverEngine.replaceElement(sqlFile, "@DATABASE@", database)

def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def mariadbUser = beaverEngine.getResource("mariadb", "rootUser")
def mariadbPwd = beaverEngine.getResource("mariadb", "rootPwd")
def mariadbPort = beaverEngine.getResource("mariadb", "port")
def mariadbBin = beaverEngine.getResource("mariadb", "bin")
SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.startService(mariadbService)
beaverEngine.executeMysqlScript(mariadbBin,mariadbPort, mariadbUser, mariadbPwd, sqlFile)
systemdService.stopService(mariadbService)