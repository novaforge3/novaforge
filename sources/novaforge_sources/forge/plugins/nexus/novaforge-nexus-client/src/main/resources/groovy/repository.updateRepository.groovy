import groovy.json.JsonSlurper;
import groovy.json.internal.LazyMap;
org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.manager.internal.RepositoryImpl;

def repositoryParam = new JsonSlurper().parseText(args);
def repository = new RepositoryImpl();
repository.setUserId(userParam.userId);
repository.setFirstName(userParam.firstName);
repository.setLastName(userParam.lastName);
repository.setEmailAddress(userParam.emailAddress);
repository.setSource(userParam.source);
repository.setStatus(UserStatus.valueOf(userParam.status));
repository.setReadOnly(userParam.readOnly);
repository.setVersion(userParam.version);
for(LazyMap role:userParam.roles){
	user.addRole(new RoleIdentifier(role.get("source"), role.get("roleId")));
};
User updatedUser=security.asType(SecurityApiImpl.class).getSecuritySystem().updateUser(user);
return groovy.json.JsonOutput.toJson(updatedUser)
  
