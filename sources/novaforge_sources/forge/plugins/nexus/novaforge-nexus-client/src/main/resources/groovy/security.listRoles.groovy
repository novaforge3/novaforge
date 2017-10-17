import groovy.json.JsonSlurper;
def userId = new JsonSlurper().parseText(args);
def roles = security.asType(org.sonatype.nexus.security.internal.SecurityApiImpl.class).getSecuritySystem().listRoles();
return groovy.json.JsonOutput.toJson(roles)