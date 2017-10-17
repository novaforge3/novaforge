import groovy.json.JsonSlurper;
def mavenHostedRepositoryParam = new JsonSlurper().parseText(args);
def repo=repository.createMavenHosted(mavenHostedRepositoryParam.name,mavenHostedRepositoryParam.blobStoreName,mavenHostedRepositoryParam.strictContentTypeValidation,org.sonatype.nexus.repository.maven.VersionPolicy.valueOf(mavenHostedRepositoryParam.versionPolicy),org.sonatype.nexus.repository.storage.WritePolicy.valueOf(mavenHostedRepositoryParam.writePolicy),org.sonatype.nexus.repository.maven.LayoutPolicy.valueOf(mavenHostedRepositoryParam.layoutPolicy));
def configuration=repo.getConfiguration();
def type=repo.getType().getValue();
def format=repo.getFormat().getValue();
def name = repo.getName(); 
def repoUrl=repo.getUrl();
def versionPolicy=configuration.attributes.maven.versionPolicy;
def jsonResult = '{' + "\"type\":\"" + type + "\",\"format\":\"" +  format + "\",\"name\":\"" + name + "\",\"url\":\"" + repoUrl + "\",\"versionPolicy\":\"" + versionPolicy + "\"}";
return jsonResult