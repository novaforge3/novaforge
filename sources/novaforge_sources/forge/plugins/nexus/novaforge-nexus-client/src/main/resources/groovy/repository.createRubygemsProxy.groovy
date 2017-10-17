import groovy.json.JsonSlurper;
def proxyRepositoryParam = new JsonSlurper().parseText(args);
def repo=repository.createRubygemsProxy(proxyRepositoryParam.name,proxyRepositoryParam.remoteUrl,proxyRepositoryParam.blobStoreName,proxyRepositoryParam.strictContentTypeValidation);
def configuration=repo.getConfiguration();
def type=repo.getType().getValue();
def format=repo.getFormat().getValue();
def proxyUrl=configuration.attributes.proxy.remoteUrl;
def jsonResult = '{' + "\"type\":\"" + type + "\",\"format\":\"" +  format + "\",\"name\":\"" + repo.getName() + "\",\"url\":\"" + repo.getUrl() + "\",\"remoteUrl\":\"" + proxyUrl + "\"}";
return jsonResult