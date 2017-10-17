package org.novaforge.forge.plugins.gcl.nexus.internal.datamapper;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;
import org.novaforge.forge.plugins.gcl.nexus.domain.HostedRepository;
import org.novaforge.forge.plugins.gcl.nexus.domain.NexusRolePrivilege;
import org.novaforge.forge.plugins.gcl.nexus.domain.RepositoryFormat;
import org.sonatype.nexus.repository.maven.VersionPolicy;
import org.sonatype.nexus.security.role.Role;

public class RoleFactoryTest {
	
	private final static String EXPECTED_ROLE_NAME_FOR_DEVELOPER = "myproject-maven-hosted-release (Developper Senior)";
	private final static String EXPECTED_ROLE_NAME_FOR_ADMINISTRATOR = "myproject-maven-hosted-release (Administrator)";
	private final static String EXPECTED_ADMIN_PRIVILEGE = "nx-repository-admin-maven2-myproject-maven-hosted-release-*";
	private final static String EXPECTED_VIEW_PRIVILEGE = "nx-repository-view-maven2-myproject-maven-hosted-release-*";
	private final static String TARGET_REPOSITORY = "myproject-maven-hosted-release";

	private final static NexusRolePrivilege NEXUS_ROLE_PRIVILEGE_DEVELOPER = NexusRolePrivilege.DEVELOPER_SENIOR;
	private final static NexusRolePrivilege NEXUS_ROLE_PRIVILEGE_ADMINISTRATOR = NexusRolePrivilege.ADMINISTRATOR;
	
	@Test
	public void test01GetRole() {
		
		HostedRepository repository = new HostedRepository(RepositoryFormat.MAVEN2, TARGET_REPOSITORY, "", VersionPolicy.RELEASE);

		Role role = RoleFactory.getRole(NEXUS_ROLE_PRIVILEGE_DEVELOPER, repository);
		
		assertNotNull(role);
		assertTrue(role.getName().equals(EXPECTED_ROLE_NAME_FOR_DEVELOPER));
	}
	
	@Test
	public void test02GetRoleAdministrator() {
		
		HostedRepository repository = new HostedRepository(RepositoryFormat.MAVEN2, TARGET_REPOSITORY, "", VersionPolicy.RELEASE);

		Role role = RoleFactory.getRole(NEXUS_ROLE_PRIVILEGE_ADMINISTRATOR, repository);
		
		assertNotNull(role);
		assertTrue(role.getName().equals(EXPECTED_ROLE_NAME_FOR_ADMINISTRATOR));
		
		Set<String> privileges = role.getPrivileges(); 
		
		
		assertTrue(privileges.contains(EXPECTED_ADMIN_PRIVILEGE));
		assertTrue(privileges.contains(EXPECTED_VIEW_PRIVILEGE));
		
	}

}
