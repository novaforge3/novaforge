/**
 * Copyright (c) 2011-2016, BULL SAS, NovaForge Version 3 and above.
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
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

BeaverEngine beaverEngine = engine

// Extract jenkins
def jenkinsHome = beaverEngine.getResource("home")
Path jenkinsHomePath = Paths.get(jenkinsHome)
if (Files.exists(jenkinsHomePath))
{
  beaverEngine.delete(jenkinsHome)  
}
beaverEngine.createDirectory(jenkinsHome)
beaverEngine.unpackFile(dataFile,jenkinsHome)

// Create jenkins datas
def jenkinsDatas = beaverEngine.getResource("datas")
Path jenkinsDatasPath = Paths.get(jenkinsDatas)
if (Files.exists(jenkinsDatasPath))
{
  beaverEngine.delete(jenkinsDatas)  
}
beaverEngine.move(jenkinsHome + "/jenkins", jenkinsDatas)

// Create jenkins tmp
def jenkinsTmp = beaverEngine.getResource("tmp")
Path jenkinsTmpPath = Paths.get(jenkinsTmp)
if (Files.exists(jenkinsTmpPath))
{
  beaverEngine.delete(jenkinsTmp)  
}
beaverEngine.createDirectory(jenkinsTmp)

// Create jenkins logs
def jenkinsLogs = beaverEngine.getResource("logs")
Path jenkinsLogsPath = Paths.get(jenkinsLogs)
if (Files.exists(jenkinsLogsPath))
{
  beaverEngine.delete(jenkinsLogs)  
}
beaverEngine.createDirectory(jenkinsLogs)