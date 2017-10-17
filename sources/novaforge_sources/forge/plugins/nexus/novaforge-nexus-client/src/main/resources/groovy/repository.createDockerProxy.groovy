import groovy.json.JsonSlurper;
def dockerProxyRepositoryParam = new JsonSlurper().parseText(args);
def repo=repository.createDockerProxy(dockerProxyRepositoryParam.name,dockerProxyRepositoryParam.remoteUrl,dockerProxyRepositoryParam.indexType,dockerProxyRepositoryParam.indexUrl,dockerProxyRepositoryParam.httpPort,dockerProxyRepositoryParam.httpsPort,dockerProxyRepositoryParam.blobStoreName,dockerProxyRepositoryParam.strictContentTypeValidation,dockerProxyRepositoryParam.v1Enabled);
def configuration=repo.getConfiguration();
def type=repo.getType().getValue();
def format=repo.getFormat().getValue();
def proxyUrl=configuration.attributes.proxy.remoteUrl;
def jsonResult = '{' + "\"type\":\"" + type + "\",\"format\":\"" +  format + "\",\"name\":\"" + repo.getName() + "\",\"url\":\"" + repo.getUrl() + "\",\"remoteUrl\":\"" + proxyUrl + "\"}";
return jsonResult