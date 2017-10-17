import groovy.json.JsonSlurper;
def changePasswordParam = new JsonSlurper().parseText(args);
security.securitySystem.changePassword(changePasswordParam.userId, changePasswordParam.newPassword)