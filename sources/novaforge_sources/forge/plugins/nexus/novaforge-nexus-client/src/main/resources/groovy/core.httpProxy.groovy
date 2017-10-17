import groovy.json.JsonSlurper;
def proxyParam = new JsonSlurper().parseText(args);core.httpProxy(proxyParam.host,proxyParam.port)