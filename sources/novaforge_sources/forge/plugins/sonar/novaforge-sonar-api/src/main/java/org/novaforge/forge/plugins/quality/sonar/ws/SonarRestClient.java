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
package org.novaforge.forge.plugins.quality.sonar.ws;



import java.util.Date;
import java.util.List;

import org.novaforge.forge.plugins.quality.sonar.ws.models.TimeMachine;
import org.novaforge.forge.plugins.quality.sonar.ws.models.User;
import org.sonarqube.ws.WsComponents.Component;




/**
 * @author tranxuan-c
 * @author BILET-JC
 * @author lequere-g sonar api refactoring for a sonar 5.6 compliance
 */
public interface SonarRestClient {

	public final static String COMPONENT_QUALIFIER_SUB_PROJECTS = "BRC";
	public final static String COMPONENT_QUALIFIER_CLA = "CLA";
	public final static String COMPONENT_QUALIFIER_DIRECTORIES = "DIR";
	public final static String COMPONENT_QUALIFIER_FILES = "FIL";
	public final static String COMPONENT_QUALIFIER_PACKAGES = "PAC";
	public final static String COMPONENT_QUALIFIER_PROJECTS = "TRK";
	public final static String COMPONENT_QUALIFIER_SUBPROJECTS = "PRJ";
	public final static String COMPONENT_QUALIFIER_UNIT_TEST_FILES = "UTS";

	public final static String COMPONENT_STRATEGY_TREE_ALL = "all";
	public final static String COMPONENT_STRATEGY_TREE_CHILDREN = "CHILDREN";
	public final static String COMPONENT_STRATEGY_TREE_LEAVES = "ALL";
	
	public final static String PROJECT_GROUP_USER = "groupuser";
	public final static String PROJECT_GROUP_ADMIN = "groupadmin";
	public final static String PROJECT_GROUP_CODEVIEWER = "groupcodeviewer";
	public final static String PROJECT_GROUP_ANALYSIS_EXECUTOR = "groupanalysisexecutor";
	public final static String PROJECT_GROUP_ISSUE_ADMIN = "groupissueadmin";
	
	public final static String GROUP_USER_SUFFIX = "_user";
	public final static String GROUP_ADMIN_SUFFIX = "_admin";
	public final static String GROUP_CODEVIEWER_SUFFIX = "_codeviewer";
	public final static String GROUP_ANALYSIS_SUFFIX = "_analysis_executor";
	public final static String GROUP_ISSUE_ADMIN_SUFFIX = "_issue_admin";
	
	public final static String GROUP_DESCRIPTION_PREFIX = "The group belongs to the projet ";
	
	/**
	 * Create a Sonar user based on a NovaForge user.
	 * @param sonarWSContext
	 * @param login
	 * @param name
	 * @param email
	 * @param password
	 */
	void createUser(final SonarWSContext sonarWSContext, final String login, final String name, final String email, final String password) throws SonarRestException;

	/**
	 * Update a Sonar user from a NovaForge user.
	 * @param sonarWSContext
	 * @param login
	 * @param name
	 * @param email
	 * @param password
	 */
	void updateUser(final SonarWSContext sonarWSContext, final String login, final String name, final String email, final String password) throws SonarRestException;

	/**
	 * Return the NovaForge user who matches the Sonar user login
	 * @param sonarWSContext
	 * @param userLogin
	 * @return
	 */
	User findUser(final SonarWSContext sonarWSContext, final String userLogin) throws SonarRestException;

	/**
	 * Delete the Sonar user with a given login
	 * @param sonarWSContext
	 * @param userLogin
	 */
	void deleteUser(final SonarWSContext sonarWSContext, final String userLogin) throws SonarRestException;

//	/**
//	 * Return the Sonar roles
//	 * @param sonarWSContext
//	 * @return
//	 */
//	List<Role> getRoles(final SonarWrapper sonarWSContext);
//
//	/**
//	 * Return the NovaForge role of a sonar role
//	 * @param sonarWSContext
//	 * @param roleName
//	 * @return
//	 */
//	Role getRole(final SonarWSContext sonarWSContext, final String roleName);
//
//
//	/**
//	 * Update a sonar membership
//	 * @param sonarWSContext
//	 * @param membership
//	 */
//	void updateMembership(final SonarWSContext sonarWSContext, final Membership membership);
//
//	/**
//	 * Remove a sonar membership
//	 * @param sonarWSContext
//	 * @param membership
//	 */
//	void removeMembership(final SonarWSContext sonarWSContext, final Membership membership);

	/**
	 * 
	 * @param sonarWSContext
	 * @param group
	 */
	void createGroup(final SonarWSContext sonarWSContext, final String groupName, final String description) throws SonarRestException;

//	/**
//	 * Associte a group to a permission
//	 * @param sonarWSContext
//	 * @param groupName
//	 * @param permission
//	 */
//	void addGroupToPermission(final SonarWSContext sonarWSContext, final String groupName, final String permission);
	/**
	 * 
	 * @param sonarWSContext
	 * @param forgeProjectId
	 */
	void createGroupsForProject(final SonarWSContext sonarWSContext, final String forgeProjectId) throws SonarRestException;
	
	/**
	 * 
	 * @param sonarWSContext
	 * @param groupName
	 * @param forgeProjectId
	 * @param permission
	 */
	void addGroupToProjectPermission(final SonarWSContext sonarWSContext, final String groupName, final String forgeProjectId, final String permission)  throws SonarRestException;
	
	/**
	 * 
	 * @param sonarWSContext
	 * @param groupName
	 * @param forgeProjectId
	 * @param permission
	 */
	void removeGroupFromProjectPermission(final SonarWSContext sonarWSContext, final String groupName, final String forgeProjectId, final String permission)  throws SonarRestException;
	
	/**
	 * assign the permission to a group in a given template
	 * @param sonarWSContext
	 * @param groupName
	 * @param templateName
	 * @param permission
	 */
	void addGroupToTemplateWithPermission(final SonarWSContext sonarWSContext, final String groupName, final String templateName, final String permission) throws SonarRestException;
	
	/**
	 * remove a group from a template
	 * @param sonarWSContext
	 * @param groupName
	 * @param templateName
	 */
	void removeGroupfromTemplate(final SonarWSContext sonarWSContext, final String groupName, final String templateName) throws SonarRestException;
	
	/**
	 * 
	 * @param sonarWSContext
	 * @param sonarWSContext
	 * @param groupName
	 */
	void deleteGroup(final SonarWSContext sonarWSContext, final String groupName) throws SonarRestException;

	/**
	 * Add the user to the group
	 * @param sonarWSContext
	 * @param userLogin
	 * @param groupName
	 */
	void addUserToGroup(final SonarWSContext sonarWSContext, String userLogin, String groupName) throws SonarRestException;
	
	/**
	 * Remove the user from the group
	 * @param sonarWSContext
	 * @param userLogin
	 * @param groupName
	 */
	void removeUserFromGroup(final SonarWSContext sonarWSContext, String userLogin, String groupName) throws SonarRestException;
	
	/**
	 * Return the Sonar permission associate to a group
	 * @param sonarWSContext
	 * @param groupName
	 * @return
	 */
	// String getPermission(final SonarWSContext sonarWSContext, String groupName) throws SonarRestException;
	
	/**
	 * add a membership to the project. Associate a user, identified by his login, to groups.
	 * A project group exist for each role (permission) 
	 * @param sonarWSContext
	 * @param sonarRoleId
	 * @param forgeProjectId
	 * @param userLogin
	 * @param userName
	 * @param userEmail
	 * @param userPassword
	 */
	void addMembership(final SonarWSContext sonarWSContext, final String sonarRoleId, final String forgeProjectId,
			final String userLogin, final String userName, final String userEmail, final String userPassword) throws SonarRestException;
	
	/**
	 * remove a membership 
	 * @param sonarWSContext
	 * @param sonarRoleId
	 * @param forgeProjectId
	 * @param userLogin
	 */
	void removeMembership(final SonarWSContext sonarWSContext, final String sonarRoleId, final String forgeProjectId,
			final String userLogin) throws SonarRestException;
	/**
	 * This method returns all the resources included into the Sonar instance
	 * 
	 * @param sonarWSContext
	 * @return
	 */
	//List<Component> getAllComponents(final SonarWSContext sonarWSContext) throws SonarRestException;

	/**
	 * Return the components tree of the project for an allowed user
	 * @param sonarWSContext
	 * @param forgeProjectId
	 * @param userLogin
	 * @return
	 */
	List<Component> getProjectComponents(final SonarWSContext sonarWSContext, String forgeProjectId, String userLogin) throws SonarRestException;
	/**
	 * This method returns TimeMachine Measures History Object
	 * 
	 * @param sonarWSContext
	 * @param resourceId
	 * @param metrics
	 * @param fromDate
	 * @param toDate
	 * @return {@link TimeMachine}
	 */

	TimeMachine getTimeMachine(final SonarWSContext sonarWSContext, final String resourceId,
			final List<String> metrics, final Date fromDate, final Date toDate) throws SonarRestException;

	/**
	 * 
	 * @param sonarWSContext
	 * @param groupName
	 * @param resourceId
	 * @return a list of {@link String}.
	 */
	//List<String> getGroupPermissionsOnResource(final SonarWSContext sonarWSContext, String groupName, String resourceId);

	/**
	 * 
	 * @param sonarWSContext
	 * @param groupName
	 * @param resourceId
	 * @return true if the group has any permission on the resource
	 */
	//boolean hasGroupPermissionOnResource(final SonarWSContext sonarWSContext, String groupName, String resourceId);
}
