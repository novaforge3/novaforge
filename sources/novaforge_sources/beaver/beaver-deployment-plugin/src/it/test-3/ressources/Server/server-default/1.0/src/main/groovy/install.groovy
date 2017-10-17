import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine;

BeaverEngine beaverEngine = engine

def local = beaverEngine.getResource("local:home")
beaverEngine.createDirectory(local);

def server = beaverEngine.getResource("local:server")
beaverEngine.createDirectory(server);