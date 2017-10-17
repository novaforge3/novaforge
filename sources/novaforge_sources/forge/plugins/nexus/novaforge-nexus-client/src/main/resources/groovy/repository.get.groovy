import groovy.json.JsonSlurper;def repositoryNameParam = new JsonSlurper().parseText(args);
def repo=repository.asType( org.sonatype.nexus.script.plugin.internal.provisioning.RepositoryApiImpl ).getRepositoryManager().get(repositoryNameParam);
def configuration=repo.getConfiguration();
def type=repo.getType().getValue();
def format=repo.getFormat().getValue();
def members;
def proxyUrl; 
def versionPolicy; 
def jsonResult = '{' + "\"type\":\"" + type + "\",\"format\":\"" +  format + "\",\"name\":\"" + repo.getName() + "\",\"url\":\"" + repo.getUrl() + "\"";
if(format.startsWith("maven")){versionPolicy=configuration.attributes.maven.versionPolicy;jsonResult+= ",\"versionPolicy\":\"" + versionPolicy + "\""};
if(type.equals("group")){members=groovy.json.JsonOutput.toJson(configuration.attributes.group.memberNames);jsonResult+= ",\"members\":" + members + "}"};
if(type.equals("proxy")){proxyUrl=configuration.attributes.proxy.remoteUrl;jsonResult+= ",\"remoteUrl\":\"" + proxyUrl + "\"}"};
return jsonResult