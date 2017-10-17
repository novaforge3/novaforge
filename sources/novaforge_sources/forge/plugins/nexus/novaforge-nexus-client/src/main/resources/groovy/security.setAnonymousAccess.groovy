import groovy.json.JsonSlurper;
def anonymousAccessParam = new JsonSlurper().parseText(args);
security.setAnonymousAccess(anonymousAccessParam)