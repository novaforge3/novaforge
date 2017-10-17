package org.novaforge.forge.plugins.gcl.nexus.internal.datamapper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class RoleMapperTest {
	
	private final static String EXPECTED_ROLE_ID = "monprojet_nexus_maven_snapshot_administrator";
	private final static String EXPECTED_ROLE_NAME = "monprojet_nexus_maven_snapshot (Administrator)";
	private final static String REPOSITORY_ID = "monprojet_nexus_maven_snapshot";
	private final static String ROLE_ID = "administrator";
  private final static String ROLE_LABEL = "Administrator";
	@Test
	public void testGetRoleId() {
		
		String roleId = RoleMapper.getRoleId(REPOSITORY_ID, ROLE_ID);
		
		assertNotNull(roleId);
		assertTrue(roleId.equals(EXPECTED_ROLE_ID));
	}

	@Test
	public void testGetRoleName() {

		String roleName = RoleMapper.getRoleName(REPOSITORY_ID, ROLE_LABEL);
		
		assertNotNull(roleName);
		assertTrue(roleName.equals(EXPECTED_ROLE_NAME));
	}

}
