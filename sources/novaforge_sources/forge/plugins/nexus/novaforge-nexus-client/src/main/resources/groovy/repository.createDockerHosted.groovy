import groovy.json.JsonSlurper;
def dockerHostedRepositoryParam = new JsonSlurper().parseText(args);
def repo=repository.createDockerHosted(dockerHostedRepositoryParam.name,null,null,dockerHostedRepositoryParam.blobStoreName,dockerHostedRepositoryParam.strictContentTypeValidation,dockerHostedRepositoryParam.v1Enabled,org.sonatype.nexus.repository.storage.WritePolicy.valueOf(dockerHostedRepositoryParam.writePolicy));
def type=repo.getType().getValue();
def format=repo.getFormat().getValue();
def name = repo.getName();
def repoUrl=repo.getUrl();
def jsonResult = '{' + "\"type\":\"" + type + "\",\"format\":\"" +  format + "\",\"name\":\"" + name + "\",\"url\":\"" + repoUrl + "\"}";
return jsonResult
