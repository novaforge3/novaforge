/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import java.util.List;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.novaforge.forge.plugins.gcl.nexus.rest.NexusRestClientCustom;
import org.novaforge.forge.plugins.gcl.nexus.rest.NexusRestException;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.user.User;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NexusRestClientCustomImplTest extends LocalNexusTest
{
	
	private final static String ROLE_ID = "test-role1-id";
	private final static String ROLE_NAME = "test-role1-name";
	private final static String PRIVILEGE = "nx-privileges-read";
	private final static String USER_ID = "test-user5-id";
	private final static String USER_FIRSTNAME = "test_user5-firstname";
	private final static String USER_LASTNAME = "test_user5-lastname";
	private final static String USER_EMAIL = "test_user5@example.com";
	private final static String USER_PASSWORD = "test_user5-pwd";
	
	private  NexusRestClientCustom nexusRestClientCustom;

	@Before
	public void setup() throws Exception {
		
		this.nexusRestClientCustom = new NexusRestClientCustomImpl();
		this.nexusRestClientCustom.initConnectionSettings(NEXUS_SCRIPT_URL_BASE, ADMIN_USER, ADMIN_USER_PASSWORD);
	}
	
	@Test
	public void test02CreateRole() throws NexusRestException
	{
		if (nexusProfileActivated)
		{
			Role role = new Role();
			
			role.setRoleId(ROLE_ID);
			role.setName(ROLE_NAME);
			role.addPrivilege(PRIVILEGE);
			
			Role result = nexusRestClientCustom.createRole(role);
			
			assertNotNull(result);
			assertTrue(result.getRoleId().equals(ROLE_ID));
		}
	}	
	
	@Test
	public void test03GetRoles() throws NexusRestException
	{
		if (nexusProfileActivated)
		{
			List<Role> roles = nexusRestClientCustom.getRoles();
			
			for (final Role role : roles)
      {
			  assertNotNull(role.getRoleId());
      }
			
			assertNotNull(roles);
			assertTrue(roles.size() >0);
			
		}
	}
	
	@Test
	public void test04CreateUser() throws NexusRestException
	{
		if (nexusProfileActivated)
		{
		
			User result = nexusRestClientCustom.createUser(USER_ID, USER_FIRSTNAME, USER_LASTNAME, USER_EMAIL, true, USER_PASSWORD, ROLE_ID);
			
			assertNotNull(result);
			assertTrue(result.getEmailAddress().equals(USER_EMAIL));
		}
	}
	
	@Test
	public void test05ExistsUser() throws NexusRestException
	{
		if (nexusProfileActivated)
		{
		
			boolean result = nexusRestClientCustom.existsUser(USER_ID);
			
			assertTrue(result);
			result = nexusRestClientCustom.existsUser("tartanpion");
			assertFalse(result);
		}
	}
	
  @Test
  public void test06DeleteUser() throws NexusRestException
  {
    if (nexusProfileActivated)
    {
      try
      {
        nexusRestClientCustom.deleteUser(USER_ID);
      }
      catch (Exception e)
      {
        fail(e.getMessage());
      }
    }
  }

  @Test
  public void test06DeleteRole() throws NexusRestException
  {
    if (nexusProfileActivated)
    {
      try
      {
        nexusRestClientCustom.deleteRole(ROLE_ID);
      }
      catch (Exception e)
      {
        fail(e.getMessage());
      }
    }
  }
}
