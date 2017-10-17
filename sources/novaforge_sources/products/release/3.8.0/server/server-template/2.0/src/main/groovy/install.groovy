import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine
import org.novaforge.beaver.deployment.plugin.deploy.engine.UserService
import org.novaforge.beaver.deployment.plugin.deploy.engine.RepositoryService
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.codehaus.plexus.util.StringUtils

BeaverEngine beaverEngine = engine
UserService userService = beaverEngine.getUserService();

// Check mandatory value
def localIp = beaverEngine.getResource("local:ip")
def localHost = beaverEngine.getResource("local:host")
if ((StringUtils.isBlank(localIp)) || (StringUtils.isBlank(localHost))) {
  throw new Exception("Some of the mandatory properties are empty; Check ip and host defined for current server.")
}
// Create server user
def localUser = beaverEngine.getResource("local:user")
def localGroup = beaverEngine.getResource("local:group")
// Default convention for user creation is to use its login as default password
def localUserPwd = localUser

userService.createLinuxUser(localUser,localUserPwd,localGroup)

// Create Home ssh user directory and files
def sshDirectory = "/home/" + localUser + "/.ssh"
Path sshDirectoryPath = Paths.get(sshDirectory);
if (Files.exists(sshDirectoryPath))
{
	beaverEngine.delete(sshDirectory)
}
beaverEngine.createDirectory(sshDirectory)
beaverEngine.setOwner(true,localGroup,localUser,sshDirectory)

def configBash =  processTmpPath + "/config_ssh.sh"
beaverEngine.replaceElement(configBash, "@NOVAFORGE_USER@", localUser)
beaverEngine.setPermissionsOnFiles(false,"755", configBash)
beaverEngine.executeScript(configBash)

// Installing sshpass
RepositoryService repositoryService = engine.getRepositoryService()
//TODO replace with dataFile
def serverTemplateDistribPath = processTmpPath + '/server-template-distrib-2.0-SNAPSHOT.tar.gz'
repositoryService.installRPMs(serverTemplateDistribPath, "sshpass")

// Copying SSH pub key
def serverId = beaverEngine.getServerId()
if ("portal".equals(serverId) == false && "aio".equals(serverId) == false) {
  def sshKeyScriptPath = processTmpPath + "/sshKey.sh"
  def srcServerHost = beaverEngine.getResource("main:ip")
  beaverEngine.replaceElement(sshKeyScriptPath, "@HOST_SRC@", srcServerHost)
  beaverEngine.replaceElement(sshKeyScriptPath, "@NOVAFORGE_USER@", localUser)
  beaverEngine.replaceElement(sshKeyScriptPath, "@NOVAFORGE_PASSWORD@", localUserPwd)
  beaverEngine.executeScript(sshKeyScriptPath)
}

// Setting ulimit for open files and max user processes
def limitsConf =  processTmpPath + "/novaforge-limits.conf"
beaverEngine.replaceElement(limitsConf, "@NOVAFORGE_USER@", localUser)
beaverEngine.setPermissionsOnFiles(false,"644", limitsConf)
beaverEngine.copy(limitsConf, '/etc/security/limits.d/')

// Create installation directory
def home = beaverEngine.getResource("local:home")
def bin = beaverEngine.getResource("local:bin")
def datas = beaverEngine.getResource("local:datas")
def engine = beaverEngine.getResource("local:engines")
def logs = beaverEngine.getResource("local:logs")
def system = beaverEngine.getResource("local:system")
def tmp = beaverEngine.getResource("local:tmp")

beaverEngine.createDirectory(home)
beaverEngine.setOwner(true,localGroup,localUser,home)
beaverEngine.createDirectory(bin)
beaverEngine.setOwner(true,localGroup,localUser,bin)
beaverEngine.createDirectory(datas)
beaverEngine.setOwner(true,localGroup,localUser,datas)
beaverEngine.createDirectory(engine)
beaverEngine.setOwner(true,localGroup,localUser,engine)
beaverEngine.createDirectory(logs)
beaverEngine.setOwner(true,localGroup,localUser,logs)
beaverEngine.createDirectory(system)
beaverEngine.setOwner(true,localGroup,localUser,system)
beaverEngine.createDirectory(tmp)
beaverEngine.setOwner(true,localGroup,localUser,tmp)