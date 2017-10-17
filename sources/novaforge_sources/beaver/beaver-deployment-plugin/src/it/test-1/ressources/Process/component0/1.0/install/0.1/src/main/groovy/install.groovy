import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine;

BeaverEngine beaverEngine = engine

def Base = beaverEngine.getResource("Base")
beaverEngine.createDirectory(Base);