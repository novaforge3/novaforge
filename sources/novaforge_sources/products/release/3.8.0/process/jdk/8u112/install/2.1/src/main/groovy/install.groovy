import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine

BeaverEngine beaverEngine = engine

def JDKHome = beaverEngine.getResource("home")

def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")

def localEngine = beaverEngine.getResource("local:engines")
def javaEngine = localEngine + "/java";

beaverEngine.unpackFile(dataFile,javaEngine)
beaverEngine.moveDir(javaEngine+"/jdk1.8.0_112", JDKHome)

beaverEngine.setOwner(true,localGroup,localUser,JDKHome)
beaverEngine.setPermissionsOnFiles(false,"755",JDKHome +"/bin")
beaverEngine.setPermissionsOnFiles(false,"755",JDKHome +"/jre/bin")