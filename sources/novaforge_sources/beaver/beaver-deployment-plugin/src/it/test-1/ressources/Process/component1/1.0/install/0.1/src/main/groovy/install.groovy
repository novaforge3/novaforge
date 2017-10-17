@Grab(group='net.sf.json-lib', module='json-lib', version='2.4', classifier='jdk15')
import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine;
import groovy.json.JsonSlurper;
import net.sf.json.JSONObject
import net.sf.json.JSONSerializer
import java.util.Map

BeaverEngine beaverEngine = engine

def C1Home = beaverEngine.getResource("C1Home")
def C1Base = beaverEngine.getResource("C1Base")

beaverEngine.unpackFile(dataFile,C1Home)

// Test JSON with JSON String
def C1JSON = beaverEngine.getResource("C1JSON")
def jsonMap = beaverEngine.parseJson( C1JSON )
assert jsonMap in Map == true
if (jsonMap in Map) {
  assert jsonMap.size() == 2
  assert jsonMap.get("server1") == "1" 
  assert jsonMap.get("server2") == "2" 
}

def json = (JSONObject) JSONSerializer.toJSON( C1JSON );
if ( ! json.isNullObject() )
{
assert json in Map == true
}

// Test JSON with non JSON String
def C1String = beaverEngine.getResource("C1String")
def nonJson = beaverEngine.parseJson( C1String )
assert nonJson.toString() == C1String

