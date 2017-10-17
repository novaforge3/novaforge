import groovy.json.JsonSlurper;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserNotFoundException;
def userId = new JsonSlurper().parseText(args);
def ret=false;
User user=null;
try{user=security.asType(org.sonatype.nexus.security.internal.SecurityApiImpl.class).getSecuritySystem().getUser(userId);
	ret= user!=null;
}catch (UserNotFoundException e) {};
return groovy.json.JsonOutput.toJson(ret)