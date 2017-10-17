import groovy.json.JsonSlurper;
def proxyParam = new JsonSlurper().parseText(args);core.httpsProxy(proxyParam.host,proxyParam.port)