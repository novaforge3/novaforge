import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine
BeaverEngine beaverEngine = engine

def C2Base = beaverEngine.getResource("C2Base")
def C2Home = beaverEngine.getResource("C2Home")
def C2Link = beaverEngine.getResource("C2Link")

beaverEngine.unpackFile(dataFile,C2Home)   
beaverEngine.createSymlink(C2Home, C2Link)   