import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine;

BeaverEngine beaverEngine = engine

def previousBase = beaverEngine.getPreviousResource("Base")
def Base = beaverEngine.getResource("Base")
beaverEngine.moveDir(previousBase,Base);