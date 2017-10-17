import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine

BeaverEngine beaverEngine = engine

def C3Home = beaverEngine.getResource("C3Home")

beaverEngine.unpackFile(dataFile,C3Home)   