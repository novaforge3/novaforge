import groovy.json.JsonSlurper;
def mavenProxyRepositoryParam = new JsonSlurper().parseText(args);
def repo=repository.createMavenProxy(mavenProxyRepositoryParam.name,mavenProxyRepositoryParam.remoteUrl,mavenProxyRepositoryParam.blobStoreName,mavenProxyRepositoryParam.strictContentTypeValidation,org.sonatype.nexus.repository.maven.VersionPolicy.valueOf(mavenProxyRepositoryParam.versionPolicy),org.sonatype.nexus.repository.maven.LayoutPolicy.valueOf(mavenProxyRepositoryParam.layoutPolicy));
def configuration=repo.getConfiguration();
def type=repo.getType().getValue();
def format=repo.getFormat().getValue();
def versionPolicy=configuration.attributes.maven.versionPolicy;
def proxyUrl=configuration.attributes.proxy.remoteUrl;
def jsonResult = '{' + "\"type\":\"" + type + "\",\"format\":\"" +  format + "\",\"name\":\"" + repo.getName() + "\",\"url\":\"" + repo.getUrl() + "\",\"versionPolicy\":\"" + versionPolicy + "\",\"remoteUrl\":\"" + proxyUrl + "\"}";
return jsonResult

  