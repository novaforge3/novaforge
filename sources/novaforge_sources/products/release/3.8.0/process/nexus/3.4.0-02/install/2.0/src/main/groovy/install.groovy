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
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

// NB: "dataFile", "processTmpPath" and "engine" are variables set by Beaver
BeaverEngine beaverEngine = engine

// Extract nexus
def nexusDistrib=  "nexus-distrib-3.4.0-02_0" 
def nexusHome = beaverEngine.getResource("home")
Path nexusHomePath = Paths.get(nexusHome)

//install new version : 
// 1 - unpack the tar.gz file in a temp directory
// 2 - move the nexus installation from the temp directory to the target directory

beaverEngine.unpackFile(dataFile, processTmpPath)

//remove previous version
if (Files.exists(nexusHomePath))
{
  beaverEngine.delete(nexusHome)  
}
beaverEngine.createDirectory(nexusHome)
beaverEngine.moveDir(processTmpPath + "/" + nexusDistrib  + "/nexus-3.4.0-02/", nexusHome)

// Create nexus datas
def nexusDatas = beaverEngine.getResource("datas")
Path nexusDatasPath = Paths.get(nexusDatas)
if (Files.exists(nexusDatasPath))
{
  beaverEngine.delete(nexusDatas)  
}
beaverEngine.createDirectory(nexusDatas)
//move sonatype dir to datas
beaverEngine.moveDir(processTmpPath + "/" + nexusDistrib + "/sonatype-work/nexus3/", nexusDatas)

// Create nexus tmp
def nexusTmp = beaverEngine.getResource("tmp")
Path nexusTmpPath = Paths.get(nexusTmp)
if (Files.exists(nexusTmpPath))
{
  beaverEngine.delete(nexusTmp)  
}
beaverEngine.createDirectory(nexusTmp)

def nexusLogs = beaverEngine.getResource("logs")

// Create nexus logs
Path nexusLogsPath = Paths.get(nexusLogs)
if (Files.exists(nexusLogsPath))
{
  beaverEngine.delete(nexusLogs)  
}
beaverEngine.createDirectory(nexusLogs)


