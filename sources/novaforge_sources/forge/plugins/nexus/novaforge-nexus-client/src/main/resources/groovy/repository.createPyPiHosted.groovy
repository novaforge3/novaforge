import groovy.json.JsonSlurper;
def hostedRepositoryParam = new JsonSlurper().parseText(args);
def repo=repository.createPyPiHosted(hostedRepositoryParam.name,hostedRepositoryParam.blobStoreName,hostedRepositoryParam.strictContentTypeValidation,org.sonatype.nexus.repository.storage.WritePolicy.valueOf(hostedRepositoryParam.writePolicy));
def type=repo.getType().getValue();
def format=repo.getFormat().getValue();
def name = repo.getName();
def repoUrl=repo.getUrl();
def jsonResult = '{' + "\"type\":\"" + type + "\",\"format\":\"" +  format + "\",\"name\":\"" + name + "\",\"url\":\"" + repoUrl + "\"}";
return jsonResult