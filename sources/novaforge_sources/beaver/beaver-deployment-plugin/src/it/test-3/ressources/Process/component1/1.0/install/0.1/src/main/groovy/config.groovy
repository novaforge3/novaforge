import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine

BeaverEngine beaverEngine = engine

def C1Home = beaverEngine.getResource("C1Home")

beaverEngine.commentLines(C1Home+"/data_1","curarum","inquam","shell")
beaverEngine.deleteLines(C1Home+"/data_1","Tantum","quantum")
beaverEngine.deleteLines(C1Home+"/data_1","Vide","fallare")

beaverEngine.copy(processTmpPath+"/resources/xml.test",C1Home+"/C1Test")
beaverEngine.commentLines(C1Home+"/C1Test/xml.test","<Array type=\"java.lang.String\">","</Array>","xml")