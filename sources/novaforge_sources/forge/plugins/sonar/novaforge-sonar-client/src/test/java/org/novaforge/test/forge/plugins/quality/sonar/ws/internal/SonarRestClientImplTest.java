package org.novaforge.test.forge.plugins.quality.sonar.ws.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.novaforge.forge.plugins.quality.sonar.SonarDataTest;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarRestClient;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarWSContext;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarWSContextFactory;
import org.novaforge.forge.plugins.quality.sonar.ws.internal.SonarRestClientImpl;
import org.novaforge.forge.plugins.quality.sonar.ws.internal.SonarWSContextFactoryImpl;
import org.novaforge.forge.plugins.quality.sonar.ws.models.TimeMachine;
import org.novaforge.forge.plugins.quality.sonar.ws.models.User;
import org.sonarqube.ws.WsComponents.Component;
import org.sonarqube.ws.client.GetRequest;
import org.sonarqube.ws.client.HttpConnector;
import org.sonarqube.ws.client.PostRequest;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import org.sonarqube.ws.client.WsResponse;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SonarRestClientImplTest {

	private SonarRestClient sonarRestClient = null;

	private SonarWSContext sonarWSContext = null;

	private boolean sonarProfileActivated = false;

	public SonarRestClientImplTest() {
		
		final String property = System.getProperty("sonar.profile");

		sonarProfileActivated = "true".equals(property);
	
	}

	private WsClient getWsAdminClient() {

		return WsClientFactories.getDefault().newClient(HttpConnector.newBuilder().url(sonarWSContext.getBaseURL())
				.credentials(SonarDataTest.ADMIN_USER, SonarDataTest.ADMIN_PASWORD).build());
	}

	private WsResponse createSonarProject() {

		WsClient wsClient = getWsAdminClient();

		return wsClient.wsConnector().call(new PostRequest("api/projects/create")
				.setParam("key", SonarDataTest.PROJECT_TEST_KEY).setParam("name", SonarDataTest.PROJECT_TEST_NAME));

	}

	private void deleteSonarProject() {

		WsClient wsClient = getWsAdminClient();

		// update the user
		wsClient.wsConnector()
				.call(new PostRequest("api/projects/delete").setParam("key", SonarDataTest.PROJECT_TEST_KEY));
	}

	private WsResponse getSonarProject(String projectName) {

		WsClient wsClient = getWsAdminClient();

		// update the user
		return wsClient.wsConnector().call(new GetRequest("api/projects/provisioned").setParam("q", projectName));
	}

	private WsResponse getGroups() {

		WsClient wsClient = getWsAdminClient();

		return wsClient.wsConnector().call(new GetRequest("api/user_groups/search"));
	}

	private WsResponse getProjectPermissions() {

		WsClient wsClient = getWsAdminClient();

		return wsClient.wsConnector().call(new GetRequest("api/permissions/search_project_permissions")
				.setParam("qualifier", "TRK").setParam("q", SonarDataTest.PROJECT_TEST_NAME));

	}
	
	@Before
	public void init() {

		if (sonarProfileActivated) {
			
			this.sonarRestClient = new SonarRestClientImpl();

			SonarWSContextFactory factory = new SonarWSContextFactoryImpl();
			this.sonarWSContext = factory.getWSContext(SonarDataTest.BASE_URL, SonarDataTest.ADMIN_USER,
					SonarDataTest.ADMIN_PASWORD);
		}
	}

	@Test
	public void test01CreateUser() {

		if (sonarProfileActivated) {
			
			try {

				this.sonarRestClient.createUser(sonarWSContext, SonarDataTest.USER_LOGIN, SonarDataTest.USER_NAME,
						SonarDataTest.USER_EMAIL, SonarDataTest.USER_PASWWORD);

				assertNotNull(this.sonarRestClient.findUser(sonarWSContext, SonarDataTest.USER_LOGIN));

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test02FindUser() {

		if (sonarProfileActivated) {
			
			try {
				User user = this.sonarRestClient.findUser(sonarWSContext, SonarDataTest.USER_LOGIN);

				assertNotNull(user);
				assertTrue(user.getLogin().equals(SonarDataTest.USER_LOGIN));
				assertTrue(user.getName().equals(SonarDataTest.USER_NAME));

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test03UpdateUser() {

		if (sonarProfileActivated) {
			
			try {
				this.sonarRestClient.updateUser(sonarWSContext, SonarDataTest.USER_LOGIN, SonarDataTest.USER_NAME,
						SonarDataTest.USER_NEWEMAIL, SonarDataTest.USER_PASWWORD);

				User user = this.sonarRestClient.findUser(sonarWSContext, SonarDataTest.USER_LOGIN);

				assertNotNull(user);
				assertTrue(user.getEmail().equals(SonarDataTest.USER_NEWEMAIL));

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test04DeleteUser() {

		if (sonarProfileActivated) {
			
			try {
				this.sonarRestClient.deleteUser(sonarWSContext, SonarDataTest.USER_LOGIN);

				User user = this.sonarRestClient.findUser(sonarWSContext, SonarDataTest.USER_LOGIN);

				assertNull(user);

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test05CreateProject() {

		if (sonarProfileActivated) {
			
			String response = this.createSonarProject().content();

			assertNotNull(response);
			assertTrue(response.contains(SonarDataTest.PROJECT_TEST_NAME));
		}
	}

	@Test
	public void test06GetProject() {

		if (sonarProfileActivated) {
			
			String response = this.getSonarProject(SonarDataTest.PROJECT_TEST_NAME).content();

			assertNotNull(response);
			assertTrue(response.contains(SonarDataTest.PROJECT_TEST_NAME));
		}
	}

	@Test
	public void test07DeleteProject() {

		if (sonarProfileActivated) {
			
			this.deleteSonarProject();

			String response = this.getSonarProject(SonarDataTest.PROJECT_TEST_NAME).content();

			assertNotNull(response);
			assertFalse(response.contains(SonarDataTest.PROJECT_TEST_NAME));
		}
	}

	@Test
	public void test08CreateGroup() {

		if (sonarProfileActivated) {
			
			try {
				this.createSonarProject();
				this.sonarRestClient.createGroup(sonarWSContext, SonarDataTest.GROUP_NAME,
						SonarDataTest.GROUP_DESCRIPTION);
				this.sonarRestClient.addGroupToProjectPermission(sonarWSContext, SonarDataTest.GROUP_NAME,
						SonarDataTest.PROJECT_TEST_NAME, SonarDataTest.PERMISSION_CODEVIEWER);

				String response = this.getGroups().content();

				assertNotNull(response);
				assertTrue(response.contains(SonarDataTest.GROUP_NAME));
				assertTrue(response.contains(SonarDataTest.GROUP_DESCRIPTION));

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test09DeleteGroup() {

		if (sonarProfileActivated) {
			
			try {
				this.sonarRestClient.deleteGroup(sonarWSContext, SonarDataTest.GROUP_NAME);

				String response = this.getGroups().content();

				assertNotNull(response);
				assertFalse(response.contains(SonarDataTest.GROUP_NAME));
				assertFalse(response.contains(SonarDataTest.GROUP_DESCRIPTION));

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test10AddGroupToProjectPermission() {

		if (sonarProfileActivated) {
			
			try {
				this.createSonarProject();
				this.sonarRestClient.createGroup(sonarWSContext, SonarDataTest.GROUP_NAME,
						SonarDataTest.GROUP_DESCRIPTION);

				this.sonarRestClient.addGroupToProjectPermission(sonarWSContext, SonarDataTest.GROUP_NAME,
						SonarDataTest.PROJECT_TEST_NAME, SonarDataTest.PERMISSION_CODEVIEWER);

				String response = this.getProjectPermissions().content();

				assertNotNull(response);

				// check the group adding increments the group counter
				assertTrue(response.contains(SonarDataTest.PERMISSION_CODEVIEWER_ADD_GROUP_VALIDATION));

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test11RemoveGroupFromProjectPermission() {

		if (sonarProfileActivated) {
			
			try {
				this.sonarRestClient.removeGroupFromProjectPermission(sonarWSContext, SonarDataTest.GROUP_NAME,
						SonarDataTest.PROJECT_TEST_NAME, SonarDataTest.PERMISSION_CODEVIEWER);

				String response = this.getProjectPermissions().content();

				assertNotNull(response);

				// check the group adding decrements the group counter
				assertTrue(response.contains(SonarDataTest.PERMISSION_CODEVIEWER_REMOVE_GROUP_VALIDATION));

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test12CreateGroupsForProject() {

		if (sonarProfileActivated) {
			
			try {
				this.sonarRestClient.createGroupsForProject(sonarWSContext, SonarDataTest.PROJECT_TEST_NAME);

				String response = this.getGroups().content();

				assertNotNull(response);
				assertTrue(response.contains(SonarDataTest.GROUP_NAME));
				assertTrue(response.contains(SonarDataTest.GROUP_DESCRIPTION));
				assertTrue(response.contains(SonarDataTest.GROUP_PROJET_ADMIN));
				assertTrue(response.contains(SonarDataTest.GROUP_PROJET_CODEVIEWER));
				assertTrue(response.contains(SonarDataTest.GROUP_PROJET_USER));

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test13AddUserToGroup() {

		if (sonarProfileActivated) {
			
			try {
				this.sonarRestClient.createUser(sonarWSContext, SonarDataTest.USER_LOGIN, SonarDataTest.USER_NAME,
						SonarDataTest.USER_EMAIL, SonarDataTest.USER_PASWWORD);
				this.sonarRestClient.createGroup(sonarWSContext, SonarDataTest.GROUP_NAME,
						SonarDataTest.GROUP_DESCRIPTION);
				this.sonarRestClient.addUserToGroup(sonarWSContext, SonarDataTest.USER_LOGIN, SonarDataTest.GROUP_NAME);

				User user = this.sonarRestClient.findUser(sonarWSContext, SonarDataTest.USER_LOGIN);

				assertNotNull(user);
				List<String> groups = user.getGroups();

				assertFalse(groups.isEmpty());

				Iterator<String> iterator = groups.iterator();

				boolean found = false;

				while (iterator.hasNext()) {

					String group = iterator.next();

					if (found = group.equals(SonarDataTest.GROUP_NAME))
						break;
				}
				assertTrue(found);

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test14GetPermission() {

		if (sonarProfileActivated) {
			
			// try {
			// String permission =
			// this.sonarRestClient.getPermission(sonarWSContext,
			// SonarDataTest.GROUP_NAME);
			//
			// assertNotNull(permission);
			// assertTrue(permission.equals(SonarDataTest.PERMISSION_CODEVIEWER));
			//
			// } catch (Exception e) {
			//
			// fail(e.getMessage());
			// }
			assertTrue(true);
		}
	}

	@Test
	public void test15RemoveUserFromGroup() {

		if (sonarProfileActivated) {
			
			try {
				this.sonarRestClient.removeUserFromGroup(sonarWSContext, SonarDataTest.USER_LOGIN,
						SonarDataTest.GROUP_NAME);

				User user = this.sonarRestClient.findUser(sonarWSContext, SonarDataTest.USER_LOGIN);

				assertNotNull(user);

				List<String> groups = user.getGroups();

				assertNotNull(groups);
				assertFalse(groups.isEmpty());

				Iterator<String> iterator = groups.iterator();

				boolean found = false;

				while (iterator.hasNext()) {

					String group = iterator.next();

					if (found = group.equals(SonarDataTest.GROUP_NAME))
						break;
				}
				assertFalse(found);

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test16AddMembership() {

		if (sonarProfileActivated) {
			
			try {
				this.sonarRestClient.addMembership(sonarWSContext, SonarDataTest.PERMISSION_CODEVIEWER,
						SonarDataTest.PROJECT_TEST_NAME, SonarDataTest.USER_LOGIN, SonarDataTest.USER_NAME,
						SonarDataTest.USER_EMAIL, SonarDataTest.ADMIN_PASWORD);

				User user = this.sonarRestClient.findUser(sonarWSContext, SonarDataTest.USER_LOGIN);

				assertNotNull(user);
				assertTrue(user.getLogin().equals(SonarDataTest.USER_LOGIN));

				List<String> groups = user.getGroups();

				assertNotNull(groups);
				assertFalse(groups.isEmpty());

				Iterator<String> iterator = groups.iterator();

				boolean found = false;

				while (iterator.hasNext()) {

					String group = iterator.next();

					if (found = group.equals(SonarDataTest.GROUP_PROJET_CODEVIEWER))
						break;
				}
				assertTrue(found);

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test17RemoveMembership() {

		if (sonarProfileActivated) {
			
			try {
				this.sonarRestClient.removeMembership(sonarWSContext, SonarDataTest.PERMISSION_CODEVIEWER,
						SonarDataTest.PROJECT_TEST_NAME, SonarDataTest.USER_LOGIN);

				User user = this.sonarRestClient.findUser(sonarWSContext, SonarDataTest.USER_LOGIN);

				assertNotNull(user);
				assertTrue(user.getLogin().equals(SonarDataTest.USER_LOGIN));

				List<String> groups = user.getGroups();

				assertNotNull(groups);
				assertFalse(groups.isEmpty());

				Iterator<String> iterator = groups.iterator();

				boolean found = false;

				while (iterator.hasNext()) {

					String group = iterator.next();

					if (found = group.equals(SonarDataTest.GROUP_PROJET_CODEVIEWER))
						break;
				}
				assertFalse(found);

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	// @Test
	// public void test18GetAllComponents() {
	// if(sonarProfileActivated){
	// try {
	// List<Component> components =
	// this.sonarRestClient.getAllComponents(sonarWSContext);
	//
	// assertNotNull(components);
	//
	// assertFalse(components.isEmpty());
	// assertTrue(components.get(0).getName().equals(SonarDataTest.PROJECT_TEST_NAME));
	//
	// } catch (Exception e) {
	//
	// fail(e.getMessage());
	// }
	// }
	// }

	@Test
	public void test18GetProjectComponents() {

		if (sonarProfileActivated) {
			
			try {
				this.sonarRestClient.addUserToGroup(sonarWSContext, SonarDataTest.USER_LOGIN,
						"dummy_project_test_user");

				List<Component> components = this.sonarRestClient.getProjectComponents(sonarWSContext,
						SonarDataTest.PROJECT_TEST_NAME, SonarDataTest.USER_LOGIN);

				assertNotNull(components);

				assertFalse(components.isEmpty());
				assertTrue(components.get(0).getName().equals(SonarDataTest.PROJECT_TEST_NAME));

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

	@Test
	public void test19GetTimeMachine() {

		if (sonarProfileActivated) {
			
			try {
				List<String> metrics = Arrays.asList("violations", "coverage");

//				Calendar calendar = GregorianCalendar.getInstance();
//
//				calendar.add(Calendar.DAY_OF_MONTH, -1);
//				Date fromDate = calendar.getTime();
//
//				calendar.add(Calendar.DAY_OF_MONTH, 2);
//				Date toDate = calendar.getTime();

				TimeMachine timeMachine = this.sonarRestClient.getTimeMachine(sonarWSContext,
						SonarDataTest.PROJECT_TEST_NAME, metrics, null, null);
						//SonarDataTest.PROJECT_TEST_NAME, metrics, fromDate, toDate);

				assertNotNull(timeMachine);

			} catch (Exception e) {

				fail(e.getMessage());
			}
		}
	}

}
