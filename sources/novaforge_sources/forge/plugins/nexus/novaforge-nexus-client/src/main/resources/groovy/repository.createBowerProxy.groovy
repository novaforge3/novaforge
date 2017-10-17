import groovy.json.JsonSlurper;
def bowerProxyRepositoryParam = new JsonSlurper().parseText(args);
def repo=repository.createBowerProxy(bowerProxyRepositoryParam.name,bowerProxyRepositoryParam.remoteUrl,bowerProxyRepositoryParam.blobStoreName,bowerProxyRepositoryParam.strictContentTypeValidation,bowerProxyRepositoryParam.rewritePackageUrls);
def configuration=repo.getConfiguration();
def type=repo.getType().getValue();
def format=repo.getFormat().getValue();
def proxyUrl=configuration.attributes.proxy.remoteUrl;
def jsonResult = '{' + "\"type\":\"" + type + "\",\"format\":\"" +  format + "\",\"name\":\"" + repo.getName() + "\",\"url\":\"" + repo.getUrl() + "\",\"remoteUrl\":\"" + proxyUrl + "\"}";
return jsonResult
  