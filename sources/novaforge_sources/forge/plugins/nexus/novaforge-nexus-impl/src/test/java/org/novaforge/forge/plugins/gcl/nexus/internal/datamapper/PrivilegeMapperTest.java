package org.novaforge.forge.plugins.gcl.nexus.internal.datamapper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.novaforge.forge.plugins.gcl.nexus.domain.GroupRepository;
import org.novaforge.forge.plugins.gcl.nexus.domain.PrivilegeActionCategoryType;
import org.novaforge.forge.plugins.gcl.nexus.domain.PrivilegeActionType;
import org.novaforge.forge.plugins.gcl.nexus.domain.RepositoryFormat;
import org.novaforge.forge.plugins.gcl.nexus.internal.datamapper.PrivilegeMapper;

public class PrivilegeMapperTest extends PrivilegeMapper {

	private final static String EXPECTED_PRIVILEGE_ID = "nx-repository-admin-maven2-repotest-maven-group-browse";
	private final static String REPOSITORY_NAME = "repotest-maven-group";
	
	@Test
	public void testGetPrivilegeId() {
		
		GroupRepository groupRepository = new GroupRepository(RepositoryFormat.MAVEN2, REPOSITORY_NAME);
		
		String result = PrivilegeMapper.getPrivilegeId(
				PrivilegeActionCategoryType.ADMIN, 
				PrivilegeActionType.ACTION_BROWSE, 
				groupRepository);
		
		assertNotNull(result);
		assertTrue(result.equals(EXPECTED_PRIVILEGE_ID));
	}

}
