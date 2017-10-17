import groovy.json.JsonSlurper;
def roleParam = new JsonSlurper().parseText(args);
def role=security.addRole(roleParam.id,roleParam.name,roleParam.description,roleParam.privilegeIds,roleParam.roleIds);
return groovy.json.JsonOutput.toJson(role)