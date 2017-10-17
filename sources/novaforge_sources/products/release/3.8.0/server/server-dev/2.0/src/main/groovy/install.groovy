import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine
import org.novaforge.beaver.deployment.plugin.deploy.engine.UserService
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