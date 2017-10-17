import groovy.json.JsonSlurper;
import groovy.json.internal.LazyMap;
import org.sonatype.nexus.security.internal.SecurityApiImpl;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserStatus;
def userParam = new JsonSlurper().parseText(args);
def user = new User();
user.setUserId(userParam.userId);
user.setFirstName(userParam.firstName);
user.setLastName(userParam.lastName);
user.setEmailAddress(userParam.emailAddress);
user.setSource(userParam.source);
user.setStatus(UserStatus.valueOf(userParam.status));
user.setReadOnly(userParam.readOnly);
user.setVersion(userParam.version);
for(LazyMap role:userParam.roles){
	user.addRole(new RoleIdentifier(role.get("source"), role.get("roleId")));
};
User updatedUser=security.asType(SecurityApiImpl.class).getSecuritySystem().updateUser(user);
return groovy.json.JsonOutput.toJson(updatedUser)
  
