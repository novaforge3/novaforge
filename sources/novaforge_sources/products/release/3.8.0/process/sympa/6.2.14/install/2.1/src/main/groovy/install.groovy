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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

BeaverEngine beaverEngine = engine
RepositoryService repositoryService = engine.getRepositoryService()
def systemdService = engine.getSystemdService()

def user = beaverEngine.getResource("user")
def group = beaverEngine.getResource("group")
def root = beaverEngine.getResource("root")

def home = beaverEngine.getResource("home")
def datas = beaverEngine.getResource("datas")
def logs = beaverEngine.getResource("logs")
def bin = beaverEngine.getResource("bin")

def list_data = beaverEngine.getResource("list_data")
def bounce = beaverEngine.getResource("bounce")
def arc = beaverEngine.getResource("arc")

def queuelink = beaverEngine.getResource("queueLink")
def familyqueuelink = beaverEngine.getResource("familyqueueLink")
def bouncequeuelink = beaverEngine.getResource("bouncequeueLink")
def queue = beaverEngine.getResource("queue")
def familyqueue = beaverEngine.getResource("familyqueue")
def bouncequeue = beaverEngine.getResource("bouncequeue")
def newaliaseswrapper = beaverEngine.getResource("newaliaseswrapper")

// Install RPMs
repositoryService.removeRPMs("sympa")
repositoryService.installRPMs(dataFile, "perl-Sys-Syslog", "sympa", "sympa-httpd")

// Patch installation
def patchFile = processTmpPath + "/resources/patch/mail_tt2.patch"
def keepBackup = false;
def template = beaverEngine.getResource("template")
def strip = 0
beaverEngine.patch(patchFile, strip, template, keepBackup)

// Copy sympa.mo file to /usr/share/locale/fr/LC_MESSAGES
def moFile = processTmpPath + "/resources/mo/sympa.mo"
def messages = beaverEngine.getResource("messages")
beaverEngine.copy(moFile,messages)

//scenari : inhiber la creation des listes depuis l'interface sympa
def scenari = beaverEngine.getResource("scenari")
def scenariCreateListNovaforgeFile = scenari + "/create_list.novaforge"
Path scenariCreateListNovaforgeFilePath = Paths.get(scenariCreateListNovaforgeFile)
if (Files.exists(scenariCreateListNovaforgeFilePath))
{
	beaverEngine.delete(scenariCreateListNovaforgeFile)
}
beaverEngine.copyToFile(processTmpPath + "/resources/scenari/create_list.novaforge", scenariCreateListNovaforgeFile)
beaverEngine.setOwner(false,root, root, scenariCreateListNovaforgeFile)
beaverEngine.setPermissionsOnFiles(false,"644",  scenariCreateListNovaforgeFile)

//Delete existing css at /var/lib/sympa/static_content/css
def css = beaverEngine.getResource("css")
def stylecss = css + "/style.css"
def printcss = css + "/print.css"
def printpreviewcss = css + "/print-preview.css"
def fullPagecss = css + "/fullPage.css"

Path stylecssPath = Paths.get(stylecss)
if (Files.exists(stylecssPath))
{
   beaverEngine.delete(stylecss)  
}
Path printcssPath = Paths.get(printcss)
if (Files.exists(printcssPath))
{
   beaverEngine.delete(printcss)  
}
Path printpreviewcssPath = Paths.get(printpreviewcss)
if (Files.exists(printpreviewcssPath))
{
   beaverEngine.delete(printpreviewcss)  
}
Path fullPagecssPath = Paths.get(fullPagecss)
if (Files.exists(fullPagecssPath))
{
   beaverEngine.delete(fullPagecss)  
}

def sympaWSDL = beaverEngine.getResource("sympaWSDL")
Path sympaWSDLPath = Paths.get(sympaWSDL)
if (Files.exists(sympaWSDLPath))
{
  beaverEngine.move(sympaWSDL, sympaWSDL+".old")
}
beaverEngine.copyToFile(processTmpPath + "/resources/wsdl/sympa.wsdl", sympaWSDL)

def SoapFile = beaverEngine.getResource("SOAPFile")
Path SoapFilePath = Paths.get(SoapFile)
if (Files.exists(SoapFilePath))
{
  beaverEngine.move(SoapFile, SoapFile+".old")
}
beaverEngine.copyToFile(processTmpPath + "/resources/lib/SOAP.pm", SoapFile)

def ListFile = beaverEngine.getResource("ListFile")
Path ListFilePath = Paths.get(ListFile)
if (Files.exists(ListFilePath))
{
  beaverEngine.move(ListFile, ListFile+".old")
}
beaverEngine.copyToFile(processTmpPath + "/resources/lib/List.pm", ListFile)

def AdminFile = beaverEngine.getResource("AdminFile")
Path AdminFilePath = Paths.get(AdminFile)
if (Files.exists(AdminFilePath))
{
  beaverEngine.move(AdminFile, AdminFile+".old")
}
beaverEngine.copyToFile(processTmpPath + "/resources/lib/Admin.pm", AdminFile)

// Create directories
beaverEngine.createDirectory(home)
beaverEngine.createDirectory(datas)
beaverEngine.createDirectory(logs)
beaverEngine.createDirectory(bin)

beaverEngine.createDirectory(list_data)
beaverEngine.createDirectory(bounce)
beaverEngine.createDirectory(arc)

// create sympa_aliases and sympa_aliases.db files
def sympa_aliases = beaverEngine.getResource("aliases")
Path sympaAliasesPath = Paths.get(sympa_aliases)
if (Files.exists(sympaAliasesPath) == false)
{
  File sympaAliasesFile = new File(sympa_aliases)
  beaverEngine.createNewFile(sympaAliasesFile.getPath());
}

def sympa_aliases_db = sympa_aliases + ".db"
Path sympaAliasesDBPath = Paths.get(sympa_aliases_db)
if (Files.exists(sympaAliasesDBPath) == false)
{
  File sympaAliasesDBFile = new File(sympa_aliases_db)
  beaverEngine.createNewFile(sympaAliasesDBFile.getPath());
}

beaverEngine.setOwner(true, user, group, home)
beaverEngine.setOwner(true, user, group, datas)
beaverEngine.setOwner(true, user, group, list_data)
beaverEngine.setOwner(true, user, group, bounce)
beaverEngine.setOwner(true, user, group, arc)

// Set specific owner and group for sympa_aliases.db file
def aliasDBGroup = beaverEngine.getResource("groupAliasesDB")
def aliasDBOwner = beaverEngine.getResource("ownerAliasesDB")
beaverEngine.setOwner(false, aliasDBGroup, aliasDBOwner, sympa_aliases_db)


// Suppression des anciens liens symboliques
Path queuelinkPath = Paths.get(queuelink);
Path familyqueuelinkPath = Paths.get(familyqueuelink);
Path bouncequeuelinkPath = Paths.get(bouncequeuelink);
if (Files.exists(queuelinkPath))
{
  beaverEngine.delete(queuelink)  
}
if (Files.exists(familyqueuelinkPath))
{
  beaverEngine.delete(familyqueuelink)  
}
if (Files.exists(bouncequeuelinkPath))
{
  beaverEngine.delete(bouncequeuelink)  
}

//Copy files from /usr/libexec/sympa to /datas/safran/system/sympa/bin
Path queuePath = Paths.get(queue)
Path familyqueuePath = Paths.get(familyqueue)
Path bouncequeuePath = Paths.get(bouncequeue)
Path newaliaseswrapperPath = Paths.get(newaliaseswrapper)

if (Files.exists(queuePath))
{
  beaverEngine.move(queue,bin + "/queue")
}
if (Files.exists(familyqueuePath))
{
  beaverEngine.move(familyqueue,bin + "/familyqueue")
}
if (Files.exists(bouncequeuePath))
{
  beaverEngine.move(bouncequeue,bin + "/bouncequeue")
}
// creation des nouveaux liens symboliques
beaverEngine.createSymlink(bin+"/queue",queuelink)
beaverEngine.createSymlink(bin+"/familyqueue",familyqueuelink)
beaverEngine.createSymlink(bin+"/bouncequeue",bouncequeuelink)

if (Files.exists(newaliaseswrapperPath))
{
  beaverEngine.move(newaliaseswrapper,bin + "/sympa_newaliases-wrapper")
}
	
beaverEngine.setOwner(true, user, group, bin)
beaverEngine.setOwner(false,  group, root, bin + "/sympa_newaliases-wrapper")

beaverEngine.setPermissionsOnFiles(false,"4750", bin + "/sympa_newaliases-wrapper")
beaverEngine.setPermissionsOnFiles(false,"4755", bin + "/queue")
beaverEngine.setPermissionsOnFiles(false,"4755", bin + "/familyqueue")
beaverEngine.setPermissionsOnFiles(false,"4755", bin + "/bouncequeue")
