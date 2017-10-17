package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.ChangePasswordParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.JSONResponse;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.RoleParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.UserParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.UserRoleParam;
import org.sonatype.nexus.security.user.User;

import com.fasterxml.jackson.core.JsonProcessingException;

public class SecurityManager extends AbstractNexusManager {

	/**
	 * Logger component
	 */
	private static final Log LOGGER = LogFactory.getLog(SecurityManager.class);

	private static SecurityManager instance = new SecurityManager();

	
	private SecurityManager() {
		super();
	}
	
	/**
	 * Return the unique instance of the ScriptManager
	 * 
	 * @param urlbase
	 * @return
	 */
	public static SecurityManager getInstance(String urlbase, String adminUser, String adminPassword) {

		instance.urlBase = urlbase;
		instance.adminUser = adminUser;
		instance.adminPassword = adminPassword;

		return instance;
	}
	/**
	 * Add a role.
	 * @param roleParam
	 * @return
	 */
	public JSONResponse addRole(RoleParam roleParam) throws JsonProcessingException, IOException {

		createScriptIfDoesNotExist(ScriptOperation.ADD_ROLE);

		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.ADD_ROLE, roleParam);

		return jsonResponse;
	}

	/**
	 * Delete a given role
	 * @param roleId id of the role to delete
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public JSONResponse deleteRole(String roleId) throws JsonProcessingException, IOException {
		
		createScriptIfDoesNotExist(ScriptOperation.DELETE_ROLE);
		
		RoleParam roleParam = new RoleParam(roleId, DEFAULT_SOURCE);
				
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.DELETE_ROLE,  roleParam);
		
		return jsonResponse;
	}
	
	/**
	 * Return all roles stored on the Nexus server.
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public JSONResponse getRoles() throws JsonProcessingException, IOException {
		
		createScriptIfDoesNotExist(ScriptOperation.LIST_ROLES);
				
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.LIST_ROLES,  null);
		
		return jsonResponse;
	}
	
	/**
	 * Return true if the user exists.
	 * @param userId id of the user to check 
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public JSONResponse existsUser(String userId) throws JsonProcessingException, IOException {
		
		createScriptIfDoesNotExist(ScriptOperation.EXIST_USER);
		
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.EXIST_USER,  userId);
		
		return jsonResponse;
	}
	/**
	 * Return a given user.
	 * @param userId id of the user to return
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public JSONResponse getUser(String userId) throws JsonProcessingException, IOException {
		
		createScriptIfDoesNotExist(ScriptOperation.GET_USER);
		
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.GET_USER,  userId);
		
		return jsonResponse;
	}
	
	/**
	 * Delete a given user.
	 * @param userId id of the user to delete
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public JSONResponse deleteUser(String userId) throws JsonProcessingException, IOException {
		
		createScriptIfDoesNotExist(ScriptOperation.DELETE_USER);
		
		UserParam userParam = new UserParam(userId, DEFAULT_SOURCE);
		
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.DELETE_USER,  userParam);
		
		return jsonResponse;
	}
	/**
	 * Add a user.
	 * @param userParam
	 * @return
	 */
	public JSONResponse addUser(UserParam userParam) throws JsonProcessingException, IOException {

		createScriptIfDoesNotExist(ScriptOperation.ADD_USER);

		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.ADD_USER, userParam);

		return jsonResponse;
	}

	/**
	 * Update a user.
	 * @param userParam
	 * @return
	 */
	public JSONResponse updateUser(User user) throws JsonProcessingException, IOException {

		createScriptIfDoesNotExist(ScriptOperation.UPDATE_USER);

		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.UPDATE_USER, user);

		return jsonResponse;
	}
	/**
	 * Enable/disable the anomymous access.
	 * @param enabled
	 * @return
	 */
	public JSONResponse setAnonymousAccess(boolean enabled) throws JsonProcessingException, IOException {

		createScriptIfDoesNotExist(ScriptOperation.SET_ANONYMOUS_ACCESS);

		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.SET_ANONYMOUS_ACCESS, enabled);

		return jsonResponse;
	}

	/**
	 * set roles of a given user.
	 * @param userRoleParam
	 * @return
	 */
	public JSONResponse setUserRoles(UserRoleParam userRoleParam) throws JsonProcessingException, IOException {

		createScriptIfDoesNotExist(ScriptOperation.SET_USER_ROLES);

		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.SET_USER_ROLES, userRoleParam);

		return jsonResponse;
	}

	/**
	 * Change the password of a given user.
	 * @param changePasswordParam parameter grouping the userId, the old password and the new password
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public JSONResponse changePassword(ChangePasswordParam changePasswordParam) throws JsonProcessingException, IOException {

		createScriptIfDoesNotExist(ScriptOperation.CHANGE_PASSWORD);

		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.CHANGE_PASSWORD, changePasswordParam);

		return jsonResponse;
	}
}
