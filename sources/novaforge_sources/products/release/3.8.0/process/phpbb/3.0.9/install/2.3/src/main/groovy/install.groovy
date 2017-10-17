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
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService;
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

BeaverEngine beaverEngine = engine

// Unpack the data archive
def phpbbHome = beaverEngine.getResource("home")

def phpbbOriDir = "phpBB3"


beaverEngine.unpackFile(dataFile, processTmpPath)
def phpbbArchive = processTmpPath + "/" + "phpbb-3.0.9.tar.gz"

// Install phpbb engine
Path homePath = Paths.get(phpbbHome)
if (Files.exists(homePath))
{
  beaverEngine.delete(phpbbHome)
}
def localEngines = beaverEngine.getResource("local:engines")
beaverEngine.unpackFile(phpbbArchive,processTmpPath)
beaverEngine.move(processTmpPath + "/" + phpbbOriDir, localEngines)



//create databe
def sqlCreateTableFile = processTmpPath + "/resources/sql/createBasePhpbb.sql"
def sqlUser = beaverEngine.getResource("sqlUser")
def sqlPwd = beaverEngine.getResource("sqlPwd")
def phpbbHost = beaverEngine.getResource("mariadb", "host")
def phpbbDatabase = beaverEngine.getResource("database")
beaverEngine.replaceElement(sqlCreateTableFile, "@DATABASE@", phpbbDatabase)
beaverEngine.replaceElement(sqlCreateTableFile, "@USERPHPBB@", sqlUser)
beaverEngine.replaceElement(sqlCreateTableFile, "@PASSPHPBB@", sqlPwd)
beaverEngine.replaceElement(sqlCreateTableFile, "@HOSTNAME@",  phpbbHost)

def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def mariadbAdminUser = beaverEngine.getResource("mariadb","rootUser")
def mariadbAdminPwd= beaverEngine.getResource("mariadb","rootPwd")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def mariadbSocket = beaverEngine.getResource("mariadb","socket")
def mariadbBin = beaverEngine.getResource("mariadb","bin")
SystemdService systemdService = beaverEngine.getSystemdService()



systemdService.startService(mariadbService)
beaverEngine.executeMysqlScript(mariadbBin, mariadbPort, mariadbAdminUser, mariadbAdminPwd, sqlCreateTableFile)
def sqlResteFile = processTmpPath + "/resources/sql/phpbb.sql"

def prefix = beaverEngine.getResource("prefix")
def adminPhpbb = beaverEngine.getResource("adminPhpbb")
def passAdminPhpbb = beaverEngine.getResource("passAdminPhpbb")
def adminEmail = beaverEngine.getResource("main","novaforge-connector-forge","adminEmail")
def smtpPort = beaverEngine.getResource("smtp","port")
def smtpPassword = beaverEngine.getResource("smtp","password")
def smtpUsername = beaverEngine.getResource("smtp","username")
def casPort = beaverEngine.getResource("main","cas","externalPort")
def casHost = beaverEngine.getResource("certificat", "commonName")
def casURI = beaverEngine.getResource("main","cas","uri")
def casValidateUri = beaverEngine.getResource("main","cas","validateUri")
def phpbbScriptPath = beaverEngine.getResource("phpbbScriptPath")
def phpbbServerProtocole = beaverEngine.getResource("phpbbServerProtocole")




beaverEngine.replaceElement(sqlResteFile, "@DATABASE@", phpbbDatabase)
beaverEngine.replaceElement(sqlResteFile, "@PHPBBPREFIX@", prefix)
beaverEngine.replaceElement(sqlResteFile, "@LOGIN_ADMIN_PHPBB@", adminPhpbb)
beaverEngine.replaceElement(sqlResteFile, "@PASSWORD_ADMIN_PHPBB@", passAdminPhpbb)
beaverEngine.replaceElement(sqlResteFile, "@MAIL_ADMIN_FORGE@", adminEmail)
beaverEngine.replaceElement(sqlResteFile, "@RELAISMTPPORT@", smtpPort)
beaverEngine.replaceElement(sqlResteFile, "@PASSSMTP@", smtpPassword)
beaverEngine.replaceElement(sqlResteFile, "@LOGINSMTP@", smtpUsername)
beaverEngine.replaceElement(sqlResteFile, "@CAS_PORT@", casPort)
beaverEngine.replaceElement(sqlResteFile, "@CAS_HOST@", casHost)
beaverEngine.replaceElement(sqlResteFile, "@CAS_URI@", casURI)
beaverEngine.replaceElement(sqlResteFile, "@CAS_VALIDATE@", casValidateUri)
beaverEngine.replaceElement(sqlResteFile, "@PHPBB_SCRIPT_PATH@", phpbbScriptPath)
beaverEngine.replaceElement(sqlResteFile, "@PHPBB_SERVER_NAME@", casHost)
beaverEngine.replaceElement(sqlResteFile, "@PHPBB_SERVER_PORT@", casPort)
beaverEngine.replaceElement(sqlResteFile, "@PHPBB_SERVER_PROTO@", phpbbServerProtocole)
beaverEngine.executeMysqlScript(mariadbBin, mariadbPort, mariadbAdminUser, mariadbAdminPwd, sqlResteFile)
systemdService.stopService(mariadbService)

//patch Mantis for Novaforge
def patchFile = processTmpPath+"/resources/patch/phpbb.patch"
def strip = 2
def keepBackup = false
beaverEngine.patch(patchFile, strip, phpbbHome, keepBackup)
