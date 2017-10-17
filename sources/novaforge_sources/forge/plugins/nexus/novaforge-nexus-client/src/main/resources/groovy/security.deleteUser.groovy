import groovy.json.JsonSlurper;
def userParam = new JsonSlurper().parseText(args);
security.asType(org.sonatype.nexus.security.internal.SecurityApiImpl.class).getSecuritySystem().deleteUser(userParam.id, userParam.sourceId)