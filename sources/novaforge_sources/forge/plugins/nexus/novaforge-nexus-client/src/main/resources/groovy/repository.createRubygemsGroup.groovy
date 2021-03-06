import groovy.json.JsonSlurper;
groupRepositoryParam = new JsonSlurper().parseText(args);
def repo=repository.createRubygemsGroup(groupRepositoryParam.name,groupRepositoryParam.members,groupRepositoryParam.blobStoreName);
def configuration=repo.getConfiguration();
def type=repo.getType().getValue();
def format=repo.getFormat().getValue();
def members=groovy.json.JsonOutput.toJson(configuration.attributes.group.memberNames);
def jsonResult = '{' + "\"type\":\"" + type + "\",\"format\":\"" +  format + "\",\"name\":\"" + repo.getName() + "\",\"url\":\"" + repo.getUrl() + "\",\"members\":" + members + "\"}";
return jsonResult