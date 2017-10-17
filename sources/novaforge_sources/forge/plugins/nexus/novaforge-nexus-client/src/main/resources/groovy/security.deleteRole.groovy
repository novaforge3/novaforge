import groovy.json.JsonSlurper;
def roleParam = new JsonSlurper().parseText(args);
security.asType(org.sonatype.nexus.security.internal.SecurityApiImpl.class).getSecuritySystem().getAuthorizationManager(roleParam.sourceId).deleteRole(roleParam.id)