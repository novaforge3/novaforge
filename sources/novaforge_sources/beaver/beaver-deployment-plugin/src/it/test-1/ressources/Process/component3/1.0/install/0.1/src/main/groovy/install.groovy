import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine

BeaverEngine beaverEngine = engine

def C3Home = beaverEngine.getResource("C3Home")

def C3data = C3Home + "/data_31"

def escapePropertieValue(value) {
    def escp = value
    // Escape simple backslash (not double backslash)
    escp = escp.replaceAll("([^\\\\]|^)\\\\([^\\\\]|\$)", "\$1\\\\\\\\\\\\\\\\\$2")
    // Escape double backslash
    escp = escp.replaceAll("\\\\", "\\\\\\\\\\\\\\\\")
    // Escape comma and double quote
    escp = escp.replaceAll("([\",])", "\\\\\\\\\\\\\\\\\$1")
}
/*
 */

		beaverEngine.copyToFile(processTmpPath + "/resources/data_31", C3data)
def valueFull = beaverEngine.getValueFromRegex(C3data, "value=(.*)")
def value = valueFull.substring(valueFull.indexOf('=') + 1, valueFull.length())
def escp = escapePropertieValue(value)
beaverEngine.replaceExpression(C3data, "token=(.*)", "token=" + escp)
def valueFullNew = beaverEngine.getValueFromRegex(C3data, "value=(.*)")
assert(valueFull.equals(valueFullNew))

beaverEngine.unpackFile(dataFile,C3Home)
beaverEngine.patch(processTmpPath + "/resources/patch", 0, C3Home, true)

//exclusion key list
def exclusionList = []

beaverEngine.propertiesFilesMerger(C3Home + "/messagesNew.properties", C3Home + "/messagesOld.properties", exclusionList);