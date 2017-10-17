import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine

BeaverEngine beaverEngine = engine

def C4Home = beaverEngine.getResource("C4Home")

beaverEngine.unpackFile(dataFile,C4Home)	
