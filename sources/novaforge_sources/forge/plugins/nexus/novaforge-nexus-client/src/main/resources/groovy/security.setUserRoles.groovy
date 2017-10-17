import groovy.json.JsonSlurper;
def userRoleParam = new JsonSlurper().parseText(args);
security.setUserRoles(userRoleParam.userId,userRoleParam.roleIds)