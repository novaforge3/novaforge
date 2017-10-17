import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine

BeaverEngine beaverEngine = engine

def C1Home = beaverEngine.getResource("C1Home")

beaverEngine.unpackFile(dataFile,C1Home)