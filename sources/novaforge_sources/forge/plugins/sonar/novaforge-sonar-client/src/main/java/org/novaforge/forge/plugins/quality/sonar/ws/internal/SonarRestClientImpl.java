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
package org.novaforge.forge.plugins.quality.sonar.ws.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.novaforge.forge.plugins.quality.sonar.utils.SonarUtils;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarRestClient;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarRestException;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarWSContext;
import org.novaforge.forge.plugins.quality.sonar.ws.marshallers.ProjectUnmarshaller;
import org.novaforge.forge.plugins.quality.sonar.ws.marshallers.TimeMachineUnmarshaller;
import org.novaforge.forge.plugins.quality.sonar.ws.marshallers.UserUnmarshaller;
import org.novaforge.forge.plugins.quality.sonar.ws.models.Project;
import org.novaforge.forge.plugins.quality.sonar.ws.models.Projects;
import org.novaforge.forge.plugins.quality.sonar.ws.models.SonarGroup;
import org.novaforge.forge.plugins.quality.sonar.ws.models.SonarRole;
import org.novaforge.forge.plugins.quality.sonar.ws.models.TimeMachine;
import org.novaforge.forge.plugins.quality.sonar.ws.models.User;
import org.novaforge.forge.plugins.quality.sonar.ws.models.Users;
import org.sonarqube.ws.WsComponents.Component;
import org.sonarqube.ws.WsComponents.TreeWsResponse;
import org.sonarqube.ws.client.GetRequest;
import org.sonarqube.ws.client.HttpConnector;
import org.sonarqube.ws.client.PostRequest;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import org.sonarqube.ws.client.WsResponse;
import org.sonarqube.ws.client.component.TreeWsRequest;
import org.sonarqube.ws.client.permission.AddGroupToTemplateWsRequest;
import org.sonarqube.ws.client.permission.AddGroupWsRequest;
import org.sonarqube.ws.client.permission.RemoveGroupFromTemplateWsRequest;
import org.sonarqube.ws.client.permission.RemoveGroupWsRequest;

/**
 * Refactoring based on the new Webservice API : https://sonarqube.com/web_api/
 * @author tranxuan-c
 * @author BILET-JC
 * @author Guillaume Lamirand
 * @author lequere-g mise en compatibilité Sonar 5.6
 */
public class SonarRestClientImpl implements SonarRestClient {


	public WsClient getWsAdminClient(final SonarWSContext sonarWSContext) throws SonarRestException {

	
		 return WsClientFactories.getDefault().newClient(HttpConnector.newBuilder()
			      .url(sonarWSContext.getBaseURL())
			      .credentials(
			    		  sonarWSContext.getSonarAdminLogin(), 
			    		  sonarWSContext.getSonarAdminPassword())
			      .build());
	}
	
	/**
	 * POST api/users/create  
	 * @param sonarWSContext
	 * @param user
	 */
	@Override
	public void createUser(SonarWSContext sonarWSContext, String login, String name, String email, String password) throws SonarRestException {
			
		WsClient wsClient = getWsAdminClient(sonarWSContext);
	
		
		wsClient.wsConnector().call(
	    	      new PostRequest("api/users/create")
	    	        .setParam("login", login)
	    	        .setParam("name", name)
	    	        .setParam("email", email)
	    	        .setParam("password", password));
	}

	/**
	 * POST api/users/update
	 * POST api/user_groups/add_user
	 * @param sonarWSContext
	 * @param user
	 */
	@Override
	public void updateUser(SonarWSContext sonarWSContext, String login, String name, String email, String password) throws SonarRestException {

		WsClient wsClient = getWsAdminClient(sonarWSContext);
		
		// update the user
		wsClient.wsConnector().call(
	    	      new PostRequest("api/users/update")
	    	        .setParam("login", login)
	    	        .setParam("name", name)
	    	        .setParam("email", email)
	    	        .setParam("password", password));	
	}


	/**
	 * 
	 * @param sonarWSContext
	 * @param userLogin
	 */
	@Override
	public void deleteUser(SonarWSContext sonarWSContext, String userLogin) throws SonarRestException {

		WsClient wsClient = getWsAdminClient(sonarWSContext);
		
		wsClient.wsConnector().call(
				new PostRequest("api/users/deactivate")
					.setParam("login", userLogin));
	}

	@Override
	public void createGroupsForProject(final SonarWSContext sonarWSContext, final String forgeProjectId) throws SonarRestException {
		
					
		String description = GROUP_DESCRIPTION_PREFIX + forgeProjectId;
		
		SonarRole[] roles = SonarRole.values();
		
		for(int i=0; i< roles.length; i++) {

			this.createGroup(
					sonarWSContext, 
					SonarUtils.getProjectGroupName(forgeProjectId, roles[i]), description);		
		}
	}
			
	@Override
	public void deleteGroup(SonarWSContext sonarWSContext, String groupName) throws SonarRestException {
		
		WsClient wsClient = getWsAdminClient(sonarWSContext);
		
		wsClient.wsConnector().call(
	    	      new PostRequest("api/user_groups/delete")
	    	        .setParam("name", groupName));
		
	}

//	@Override
//	public List<Component> getAllComponents(SonarWSContext sonarWSContext) throws SonarRestException {
//		
//		WsClient wsClient = getWsAdminClient(sonarWSContext);
//		
//		TreeWsRequest treeWsRequest = new TreeWsRequest();
//		
//		//treeWsRequest.setQualifiers(Collections.singletonList(COMPONENT_QUALIFIER_PROJECTS));
//		treeWsRequest.setStrategy(COMPONENT_STRATEGY_TREE_ALL);
//
//		TreeWsResponse response = wsClient.components().tree(treeWsRequest);		
//
//		return response.getComponentsList();
//	}
	
	@Override
	public List<Component> getProjectComponents(SonarWSContext sonarWSContext, String forgeProjectId, String userLogin) throws SonarRestException {
		
		List<Component> ret = new ArrayList<Component>();
		
		WsClient wsClient = getWsAdminClient(sonarWSContext);
		
		// get the user groups to check permission 
		User sonarUser = this.findUser(sonarWSContext, userLogin);
		

		Project project = this.searchProject(sonarWSContext, forgeProjectId);
		
		// check the user is member of the project
		if (project != null && sonarUser.isProjectUser(forgeProjectId)) {

			// get the component tree of the project
			TreeWsRequest treeWsRequest = new TreeWsRequest();
			
			treeWsRequest.setBaseComponentKey(project.getKey());
			treeWsRequest.setQualifiers(Collections.singletonList(COMPONENT_QUALIFIER_PROJECTS));
			treeWsRequest.setStrategy(COMPONENT_STRATEGY_TREE_ALL);
	
			TreeWsResponse response = wsClient.components().tree(treeWsRequest);		
	
			ret.add(response.getBaseComponent());
			ret.addAll(response.getComponentsList());
		}
		return ret;
	}



	@Override
	public User findUser(SonarWSContext sonarWSContext, String userLogin) throws SonarRestException {
		
		User ret = null;
		
		WsClient wsClient = getWsAdminClient(sonarWSContext);
		
		WsResponse response = wsClient.wsConnector().call(
	    	      new GetRequest("api/users/search")
	    	        .setParam("q", userLogin));
		
		Users users = UserUnmarshaller.parse(response.content());
		
		if(users != null) {
			
			ret = users.getFirst();
		}
				
		return ret;
	}


	@Override
	public void createGroup(SonarWSContext sonarWSContext, String groupName, String description) throws SonarRestException {

		WsClient wsClient = getWsAdminClient(sonarWSContext);
		
		wsClient.wsConnector().call(
	    	      new PostRequest("api/user_groups/create")
	    	        .setParam("name", groupName)
	    	        .setParam("description", description));
		
	}

//	@Override
//	public void addGroupToPermission(SonarWSContext sonarWSContext, String groupName, String permission) {
//
//		
//		WsClient wsClient = getWsAdminClient(sonarWSContext);
//		
//		wsClient.wsConnector().call(
//	    	      new PostRequest("api/permissions/add_group")
//	    	        .setParam("name", groupName)
//	    	        .setParam("description", description));
//		
//	}
	@Override
	public void addUserToGroup(SonarWSContext sonarWSContext, String userLogin, String groupName) throws SonarRestException {

		WsClient wsClient = getWsAdminClient(sonarWSContext);
		
		wsClient.wsConnector().call(
	    	      new PostRequest("api/user_groups/add_user")
	    	        .setParam("login", userLogin)
	    	        .setParam("name", groupName));
		
	}

	@Override
	public void removeUserFromGroup(SonarWSContext sonarWSContext, String userLogin, String groupName) throws SonarRestException {

		WsClient wsClient = getWsAdminClient(sonarWSContext);
		
		wsClient.wsConnector().call(
	    	      new PostRequest("api/user_groups/remove_user")
	    	        .setParam("login", userLogin)
	    	        .setParam("name", groupName));
		
	}

	@Override
	public void addGroupToProjectPermission(SonarWSContext sonarWSContext, String groupName, String forgeProjectId, String permission) throws SonarRestException {
		
		Project project = this.searchProject(sonarWSContext, forgeProjectId);
		
		if(project != null){
			
			WsClient wsClient = getWsAdminClient(sonarWSContext);
			
			AddGroupWsRequest addGroupWsRequest = new AddGroupWsRequest();
			
			addGroupWsRequest.setGroupName(groupName);
			addGroupWsRequest.setPermission(permission);
			addGroupWsRequest.setProjectKey(project.getKey());
			
			wsClient.permissions().addGroup(addGroupWsRequest);
			
		} else {
			
			throw new SonarRestException("Project " + forgeProjectId + " not found - no permission associated to the group " + groupName);
		}
		
	}

	@Override
	public void removeGroupFromProjectPermission(SonarWSContext sonarWSContext, String groupName, String forgeProjectId, String permission)  throws SonarRestException {

		Project project = this.searchProject(sonarWSContext, forgeProjectId);
		
		if(project != null){
			
			WsClient wsClient = getWsAdminClient(sonarWSContext);
			
			RemoveGroupWsRequest removeGroupWsRequest = new RemoveGroupWsRequest();
			
			removeGroupWsRequest.setGroupName(groupName);
			removeGroupWsRequest.setPermission(permission);
			removeGroupWsRequest.setProjectKey(project.getKey());
			
			wsClient.permissions().removeGroup(removeGroupWsRequest);
		
		} else {
			
			throw new SonarRestException("Project " + forgeProjectId + " not found - no permission associated to the group " + groupName);
		}
	}

	@Override
	public void addGroupToTemplateWithPermission(SonarWSContext sonarWSContext, String groupName, String templateName, String permission)  throws SonarRestException {

		WsClient wsClient = getWsAdminClient(sonarWSContext);
		
		AddGroupToTemplateWsRequest addGroupToTemplateWsRequest = new AddGroupToTemplateWsRequest();
		
		addGroupToTemplateWsRequest.setGroupName(groupName);
		addGroupToTemplateWsRequest.setTemplateName(templateName);
		addGroupToTemplateWsRequest.setPermission(permission);
		
		wsClient.permissions().addGroupToTemplate(addGroupToTemplateWsRequest);
	}

	@Override
	public void removeGroupfromTemplate(SonarWSContext sonarWSContext, String groupName, String templateName) throws SonarRestException {

		WsClient wsClient = getWsAdminClient(sonarWSContext);
		
		RemoveGroupFromTemplateWsRequest removeGroupFromTemplateWsRequest = new RemoveGroupFromTemplateWsRequest();
		
		removeGroupFromTemplateWsRequest.setGroupName(groupName);
		removeGroupFromTemplateWsRequest.setTemplateName(templateName);
		
		wsClient.permissions().removeGroupFromTemplate(removeGroupFromTemplateWsRequest);
		
	}


//	@Override
//	public String getPermission(SonarWSContext sonarWSContext, String groupName)  throws SonarRestException {
//
//		return null;
//	}

	@Override
	public void addMembership(SonarWSContext sonarWSContext, String sonarRoleId, String forgeProjectId,
			String userLogin, String userName, String userEmail, String userPassword) throws SonarRestException {

		final String projectGroupName = SonarUtils.getProjectGroupName(forgeProjectId, SonarRole.fromId(sonarRoleId));
		
		final User sonarUser = this.findUser(sonarWSContext, userLogin);
		
		if (sonarUser == null) {
			
			// create a user
			this.createUser(
					sonarWSContext, 
					userLogin, 
					userName,
					userEmail,
					userPassword);			
		}
		// warning, the role of an existing user is not overriden with a new
		// project creation
		else {
			
			// existing user
			this.updateUser(
					sonarWSContext, 
					userLogin, 
					userName,
					userEmail,
					userPassword);
			
			Iterator<String> iterator = sonarUser.getGroups(forgeProjectId).iterator();
			
			// remove the user of the current groups
			while (iterator.hasNext()){
					
				this.removeUserFromGroup(sonarWSContext, userLogin, iterator.next());
			}
		}
		
		// add the new user to relevant project group
		this.addUserToGroup(sonarWSContext, userLogin, projectGroupName);
		
		// any any user who is not codeviewer is added to the project codeviewer group
		if(!sonarRoleId.equals(SonarRole.CODE_VIEWERS.getId())){
			
			this.addUserToGroup(
					sonarWSContext, 
					userLogin, 
					SonarUtils.getProjectGroupName(forgeProjectId, SonarRole.CODE_VIEWERS));
		}
		
		// any sonar user is added to the "soner-users" group
		this.addUserToGroup(sonarWSContext, userLogin, SonarGroup.USERS.getId());
		
		if(sonarRoleId.equals(SonarRole.ADMINISTRATORS.getId())){
			
			// add an admin user to the "soner-administrators" group
			this.addUserToGroup(sonarWSContext, userLogin, SonarGroup.ADMINISTRATORS.getId());
		}
		
	}

	@Override
	public void removeMembership(SonarWSContext sonarWSContext, String sonarRoleId, String forgeProjectId,
			String userLogin) throws SonarRestException {
		
		final String projectGroupName = SonarUtils.getProjectGroupName(forgeProjectId, SonarRole.fromId(sonarRoleId));
		
		this.removeUserFromGroup(sonarWSContext, userLogin, projectGroupName);
	}

	@Override
	public TimeMachine getTimeMachine(SonarWSContext sonarWSContext, String resourceId, List<String> metrics,
			Date fromDate, Date toDate) throws SonarRestException {
		
		
		WsClient wsClient = getWsAdminClient(sonarWSContext);
		
		WsResponse response = wsClient.wsConnector().call(
	    	      new GetRequest("api/timemachine/index")
	    	        .setParam("fromDateTime", fromDate)
	    	        .setParam("metrics", metrics)
	    	        .setParam("resource", resourceId)
	    	        .setParam("toDateTime", toDate));
		
		TimeMachine timeMachine = TimeMachineUnmarshaller.parse(response.content());
		
		return timeMachine;
	}

	/**
	 * Return the project identifier
	 * @param forgeProjectId
	 * @return
	 */
	private Project searchProject(SonarWSContext sonarWSContext, String forgeProjectId) throws SonarRestException {
		
		Project ret = null;
		
		WsClient wsClient = getWsAdminClient(sonarWSContext);
		

		WsResponse response = wsClient.wsConnector().call(
	    	      new GetRequest("api/projects/provisioned")
	    	        .setParam("q", forgeProjectId));
		
		Projects projects = ProjectUnmarshaller.parse(response.content());
		
		if(projects != null){
			
			List<Project> projectList = projects.getProjects();
			
			if(!projectList.isEmpty()){
				
				if(projectList.size() == 1){
					
					ret = projectList.get(0);
					
				} else {
					
					throw new SonarRestException("forgeProjectId[" + forgeProjectId + " matches more one project");
				}
			}
		}
		
		return ret;
	}
}
