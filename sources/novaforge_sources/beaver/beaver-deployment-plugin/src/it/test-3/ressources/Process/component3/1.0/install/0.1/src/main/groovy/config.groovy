import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine

BeaverEngine beaverEngine = engine

def C3Home = beaverEngine.getResource("C3Home")

beaverEngine.copy(processTmpPath+"/resources/replace.test",C3Home+"/copy")
beaverEngine.replaceElement(C3Home+"/copy/replace.test","@replace@","OK")

beaverEngine.copy(processTmpPath+"/resources/replaceExpression.test",C3Home+"/copy")
beaverEngine.replaceExpression(C3Home+"/copy/replaceExpression.test","(.*)Listen(.*)80(.*)","OK")