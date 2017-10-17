import groovy.json.JsonSlurper;
def proxyBasicAutParam = new JsonSlurper().parseText(args);
core.httpProxyWithBasicAuth(proxyBasicAuthParam.host,proxyBasicAutParam.port,proxyBasicAuthParam.username,proxyBasicAutParam.password)