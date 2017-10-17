import groovy.json.JsonSlurper;
def userParam = new JsonSlurper().parseText(args);
def user=security.addUser(userParam.id,userParam.firstName,userParam.lastName,userParam.email,userParam.active,userParam.password,userParam.roleIds);
return groovy.json.JsonOutput.toJson(user)  