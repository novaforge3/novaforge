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
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

BeaverEngine beaverEngine = engine

def karafHome = beaverEngine.getResource("home")
Path homePath = Paths.get(karafHome)
if (Files.exists(homePath))
{
  beaverEngine.delete(karafHome)  
}

beaverEngine.unpackFile(dataFile, processTmpPath)
def karafDistrib = "karaf-distrib-3.0.1_2"
beaverEngine.move(processTmpPath + "/" + karafDistrib, karafHome)

// Create missing directory
def karafDatas = beaverEngine.getResource("datas")
def karafTmp = beaverEngine.getResource("tmp")
def karafLogs = beaverEngine.getResource("logs")

Path datasPath = Paths.get(karafDatas)
if (Files.exists(datasPath))
{
  beaverEngine.delete(karafDatas)  
}

Path tmpPath = Paths.get(karafTmp)
if (Files.exists(tmpPath))
{
  beaverEngine.delete(karafTmp)  
}

Path logPath = Paths.get(karafLogs)
if (Files.exists(logPath))
{
  beaverEngine.delete(karafLogs)  
}

beaverEngine.createDirectory(karafDatas);
beaverEngine.createDirectory(karafTmp);
beaverEngine.createDirectory(karafLogs);

def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
beaverEngine.setOwner(true, localGroup, localUser, karafHome)
beaverEngine.setOwner(true, localGroup, localUser, karafTmp)
beaverEngine.setOwner(true, localGroup, localUser, karafDatas)
beaverEngine.setOwner(true, localGroup, localUser, karafLogs)