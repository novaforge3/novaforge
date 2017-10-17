import groovy.json.JsonSlurper;
def proxyNTLMAutParam = new JsonSlurper().parseText(args);
core.httpsProxyWithNTLMAuth(proxyNTLMAuthParam.host,proxyNTLMAutParam.port,proxyNTLMAuthParam.username,proxyNTLMAutParam.password)