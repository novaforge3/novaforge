import groovy.json.JsonSlurper;
def dockerGroupRepositoryParam = new JsonSlurper().parseText(args);
def repo=repository.createDockerGroup(dockerGroupRepositoryParam.name,dockerGroupRepositoryParam.httpPort,dockerGroupRepositoryParam.httpsPort,dockerGroupRepositoryParam.members,dockerGroupRepositoryParam.v1Enabled,dockerGroupRepositoryParam.blobStoreName);
def configuration=repo.getConfiguration();
def type=repo.getType().getValue();
def format=repo.getFormat().getValue();
def members=groovy.json.JsonOutput.toJson(configuration.attributes.group.memberNames);
def jsonResult = '{' + "\"type\":\"" + type + "\",\"format\":\"" +  format + "\",\"name\":\"" + repo.getName() + "\",\"url\":\"" + repo.getUrl() + "\",\"members\":" + members + "\"}";
return jsonResult

