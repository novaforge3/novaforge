package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.ChangePasswordParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.HostedRepositoryParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.JSONResponse;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.RoleParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.UserParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.UserRoleParam;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.User;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * To activate Junit tests inside Eclipse : 
 * <ol>
 * <li>menu Run > the Run Configurations..</li>
 * <li>Select the Run configuration corresponding to the test case</li>
 * <li>add the environment variable -Dnexus.profile=true into the VM arguments section</li>
 * <li>click Apply button>/li>
 * </ol>
 * 
 * @author s241664
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SecurityManagerTest extends LocalNexusTest {
	
	private final static boolean ANONYMOUS_ACCESS_FALSE = false;
	private final static boolean ANONYMOUS_ACCESS_TRUE = true;
	private final static boolean USER_ACTIVE = true;
	
	
	
	private final static String ROLE_ID = "role1-test";
	private final static String CONTAINED_ROLE_ID = "nx-naonymous";
	private final static String ROLE_NAME = "rolename1-test";
	private final static String ROLE_DESCRIPTION1 = "roledescription1";
	private final static String ROLE_PRIVILEGE1 = "nx-search-read";
	private final static String ROLE_PRIVILEGE2 = "nx-selectors-all";
	
	private final static String USER_ID1 = "user-test1";
	private final static String USER_FIRSTNAME1 = "User1-firstname";
	private final static String USER_LASTNAME1 = "User1-lastname";
	private final static String USER_EMAIL1 = "user1@example.com";
	private final static String USER_NEW_EMAIL1 = "user1@example2.com";
	private final static String USER_PASSWORD1 = "user1pwd";
	private final static String USER_NEW_PASSWORD1 = "user1newpwd";
	private final static String USER_ID2 = "user-test2";
	private final static String USER_FIRSTNAME2 = "User2-firstname";
	private final static String USER_LASTNAME2 = "User2-lastname";
	private final static String USER_EMAIL2 = "user2@example.com";
	private final static String USER_PASSWORD2 = "user2pwd";
	
	private SecurityManager getSecurityManager() {
		
		return SecurityManager.getInstance(
				NEXUS_SCRIPT_URL_BASE, 
				ADMIN_USER, 
				ADMIN_USER_PASSWORD);
	}
	
	@Test
	public void test01GetInstance() {

		assertNotNull(getSecurityManager());
	}

	@Test
	public void test02AddRole() {

		if(nexusProfileActivated) {
			
			SecurityManager securityManager = getSecurityManager();
			
			try {
				
				RoleParam roleParam = new RoleParam(ROLE_ID, ROLE_NAME, ROLE_DESCRIPTION1);
				
				roleParam.addRole(CONTAINED_ROLE_ID);
				
				roleParam.addPrivilege(ROLE_PRIVILEGE1);
				roleParam.addPrivilege(ROLE_PRIVILEGE2);
				
				JSONResponse jsonResponse = securityManager.addRole(roleParam);
				
				assertTrue(jsonResponse.getStatus() == 200);
				
			} catch (Exception e) {

				fail(e.getMessage());
			}

		}
	}

	@Test
	public void test03AddUser() {

		if(nexusProfileActivated) {
			
			SecurityManager securityManager = getSecurityManager();
			
			try {
				
				UserParam userParam = new UserParam(USER_ID1, USER_FIRSTNAME1, USER_LASTNAME1, USER_EMAIL1, USER_ACTIVE, USER_PASSWORD1);
				
				userParam.addRoleId(ROLE_ID);
				
				
				JSONResponse jsonResponse = securityManager.addUser(userParam);
				
				assertTrue(jsonResponse.getStatus() == 200);
				
			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test04SetAnonymousAccess() {

		if(nexusProfileActivated) {
			
			SecurityManager securityManager = getSecurityManager();
			
			try {
								
				JSONResponse jsonResponse = securityManager.setAnonymousAccess(ANONYMOUS_ACCESS_TRUE);
				
				if(jsonResponse.getStatus() != 200){
					
					jsonResponse = securityManager.setAnonymousAccess(ANONYMOUS_ACCESS_FALSE);
										
				} else {
					
					assertTrue(true);
				
					jsonResponse = securityManager.setAnonymousAccess(ANONYMOUS_ACCESS_FALSE);
				}
				assertTrue(jsonResponse.getStatus() == 200);
				
			} catch (Exception e) {

					fail(e.getMessage());
			}
		}
	}
	

	@Test
	public void test05SetUserRoles() {

		if(nexusProfileActivated) {
			
			SecurityManager securityManager = getSecurityManager();
			
			try {
				
				// user2 creation
				UserParam userParam = new UserParam(USER_ID2, USER_FIRSTNAME2, USER_LASTNAME2, USER_EMAIL2, USER_ACTIVE, USER_PASSWORD2);
				JSONResponse jsonResponse = securityManager.addUser(userParam);
				
				assertTrue(jsonResponse.getStatus() == 200);
				
				UserRoleParam userRoleParam = new UserRoleParam(USER_ID2);
				
				userRoleParam.addRoleId(ROLE_ID);
				
				jsonResponse = securityManager.setUserRoles(userRoleParam);
				
				assertTrue(jsonResponse.getStatus() == 200);
				
			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test06GetUser() {
		
		if(nexusProfileActivated) {
			
			SecurityManager securityManager = getSecurityManager();
			
			try {
				ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE).delete(ADMIN_USER, ADMIN_USER_PASSWORD, ScriptOperation.GET_USER);	
				ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE).createScriptIfDoesNotExist(ADMIN_USER, ADMIN_USER_PASSWORD, ScriptOperation.GET_USER);	
				
				JSONResponse jsonResponse = securityManager.getUser(USER_ID2);
				
				assertNotNull(jsonResponse);
				assertTrue(jsonResponse.getStatus() == 200);
				
			} catch (Exception e) {

				fail(e.getMessage());
			}

		}
	}
	
	@Test
	public void test07existstUser() {
		
		if(nexusProfileActivated) {
			
			SecurityManager securityManager = getSecurityManager();
			
			try {
				ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE).delete(ADMIN_USER, ADMIN_USER_PASSWORD, ScriptOperation.EXIST_USER);	
				ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE).createScriptIfDoesNotExist(ADMIN_USER, ADMIN_USER_PASSWORD, ScriptOperation.EXIST_USER);	
				
				JSONResponse jsonResponse = securityManager.existsUser(USER_ID2);
				
				assertNotNull(jsonResponse);
				assertTrue(jsonResponse.getStatus() == 200);
				assertTrue(jsonResponse.getResult().contains("true"));
				
				jsonResponse = securityManager.existsUser("xyz");
				
				assertNotNull(jsonResponse);
				assertTrue(jsonResponse.getStatus() == 200);
				assertTrue(jsonResponse.getResult().contains("false"));
			} catch (Exception e) {

				fail(e.getMessage());
			}

		}
	}
	@Test
	public void test08UpdatetUser() {
		
		if(nexusProfileActivated) {
			
			SecurityManager securityManager = getSecurityManager();
			
			try {
				ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE).delete(ADMIN_USER, ADMIN_USER_PASSWORD, ScriptOperation.GET_USER);	
				ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE).createScriptIfDoesNotExist(ADMIN_USER, ADMIN_USER_PASSWORD, ScriptOperation.GET_USER);	
				
				JSONResponse jsonResponse = securityManager.getUser(USER_ID1);
				
				SimpleModule module = new SimpleModule();
				
				module.addDeserializer(
						RoleIdentifier.class, 
						new RoleIdentifierDeserializer(RoleIdentifier.class));
				
				User user = RestClientHelper.convertJsonToJava(
						jsonResponse.getResult(), 
						User.class,
						module);
				
				assertNotNull(user);
			
				// modify the user
				user.setEmailAddress(USER_NEW_EMAIL1);
				jsonResponse = securityManager.updateUser(user);
				
				assertNotNull(jsonResponse);
				assertTrue(jsonResponse.getStatus() == 200);
				
				user = RestClientHelper.convertJsonToJava(
						jsonResponse.getResult(), 
						User.class, module);
				
				assertNotNull(jsonResponse);
				assertTrue(jsonResponse.getStatus() == 200);
				assertTrue(user.getEmailAddress().equals(USER_NEW_EMAIL1));
				
			} catch (Exception e) {

				fail(e.getMessage());
			}

		}
	}
	@Test
	public void test09DeleteUser() {
		
		if(nexusProfileActivated) {
			
			SecurityManager securityManager = getSecurityManager();
			
			try {
						
				JSONResponse jsonResponse = securityManager.deleteUser(USER_ID2);
				
				assertNotNull(jsonResponse);
				assertTrue(jsonResponse.getStatus() == 200);

				
			} catch (Exception e) {

				fail(e.getMessage());
			}

		}
	}

	@Test
	public void test10GetRoles() {
		
		if(nexusProfileActivated) {
			
			SecurityManager securityManager = getSecurityManager();
			
			try {
				
				
				JSONResponse jsonResponse = securityManager.getRoles();
				
				assertNotNull(jsonResponse);
				assertTrue(jsonResponse.getStatus() == 200);

				
			} catch (Exception e) {

				fail(e.getMessage());
			}

		}
	}
	
	@Test
	public void test11DeleteRole() {
		
		if(nexusProfileActivated) {
			
			SecurityManager securityManager = getSecurityManager();
			
			try {
				
				
				JSONResponse jsonResponse = securityManager.deleteRole(ROLE_ID);
				
				assertNotNull(jsonResponse);
				assertTrue(jsonResponse.getStatus() == 200);

				
			} catch (Exception e) {

				fail(e.getMessage());
			}

		}
	}
	
	@Test
	public void test12ChangePassword() {
		
		if(nexusProfileActivated) {
			
			SecurityManager securityManager = getSecurityManager();
			
			try {
				
				ChangePasswordParam changePasswordParam = new ChangePasswordParam(USER_ID1, USER_NEW_PASSWORD1);
				
				
				JSONResponse jsonResponse = securityManager.changePassword(changePasswordParam);
				
				assertNotNull(jsonResponse);
				assertTrue(jsonResponse.getStatus() == 200);

				// return to the old password
				changePasswordParam = new ChangePasswordParam(USER_ID1, USER_PASSWORD1);
				
				securityManager.changePassword(changePasswordParam);
				
			} catch (Exception e) {

				fail(e.getMessage());
			}

		}
	}
	
  @Test
  public void test99Purge()
  {

    if(nexusProfileActivated) {
      
      SecurityManager securityManager = getSecurityManager();
      
      try {
            
        JSONResponse jsonResponse = securityManager.deleteUser(USER_ID1);
        
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.getStatus() == 200);

        
      } catch (Exception e) {

        fail(e.getMessage());
      }

    }
  }
}
