import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine
BeaverEngine beaverEngine = engine

def C2Base = beaverEngine.getResource("C2Base")
def C2Home = beaverEngine.getResource("C2Home")

beaverEngine.unpackFile(dataFile,C2Home)   