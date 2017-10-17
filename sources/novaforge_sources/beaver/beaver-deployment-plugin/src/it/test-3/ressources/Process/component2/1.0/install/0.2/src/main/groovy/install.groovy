import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine
BeaverEngine beaverEngine = engine

def C2Home = beaverEngine.getResource("C2Home")

beaverEngine.unpackFile(dataFile,C2Home)   