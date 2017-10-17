import groovy.json.JsonSlurper;
def userId = new JsonSlurper().parseText(args);
def user = security.asType(org.sonatype.nexus.security.internal.SecurityApiImpl.class).getSecuritySystem().getUser(userId);
return groovy.json.JsonOutput.toJson(user)