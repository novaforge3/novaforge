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
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

BeaverEngine beaverEngine = engine

// Extract sonar
def sonarPackage = "sonar-distrib-5.6.3" 
def sonarHome = beaverEngine.getResource("home")
Path sonarHomePath = Paths.get(sonarHome)
if (Files.exists(sonarHomePath))
{
  beaverEngine.delete(sonarHome)  
}
beaverEngine.unpackFile(dataFile, processTmpPath)
beaverEngine.moveDir(processTmpPath + "/" + sonarPackage, sonarHome)

// Create sonar datas
def sonarDatas = beaverEngine.getResource("datas")
Path sonarDatasPath = Paths.get(sonarDatas)
if (Files.exists(sonarDatasPath))
{
  beaverEngine.delete(sonarDatas)  
}
beaverEngine.createDirectory(sonarDatas)

// Create sonar tmp
def sonarTmp = beaverEngine.getResource("tmp")
Path sonarTmpPath = Paths.get(sonarTmp)
if (Files.exists(sonarTmpPath))
{
  beaverEngine.delete(sonarTmp)  
}
beaverEngine.createDirectory(sonarTmp)

// Create sonar logs
def sonarLogs = beaverEngine.getResource("logs")
Path sonarLogsPath = Paths.get(sonarLogs)
if (Files.exists(sonarLogsPath))
{
  beaverEngine.delete(sonarLogs)  
}
beaverEngine.createDirectory(sonarLogs)

// Define rights
beaverEngine.setPermissionsOnFiles(true, "755", sonarHome + "/bin")

// Configure sql
def sqlFile = processTmpPath + "/resources/sql/sonar.sql"
def sonarSQLUser = beaverEngine.getResource("sqlUser")
def sonarSQLPwd = beaverEngine.getResource("sqlPwd")
def mariadbHost = beaverEngine.getResource("mariadb", "host")
beaverEngine.replaceElement(sqlFile, "@SONAR_USER@", sonarSQLUser)
beaverEngine.replaceElement(sqlFile, "@SONAR_PWD@", sonarSQLPwd)
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
