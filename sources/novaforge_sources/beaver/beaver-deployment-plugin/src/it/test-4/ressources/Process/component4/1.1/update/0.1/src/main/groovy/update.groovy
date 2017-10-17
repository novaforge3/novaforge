import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine

BeaverEngine beaverEngine = engine
String environment = beaverEngine.getEnvironment()

def C4Home = beaverEngine.getResource("C4Home")

beaverEngine.unpackFile(dataFile,C4Home)	
