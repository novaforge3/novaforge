package org.novaforge.forge.plugins.devops.novadeploy.internal.client;

/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 * 
 * This file is free software: you may redistribute and/or modify it
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, version 3 of the
 * License.
 * 
 * This file is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have
 * received a copy of the GNU Affero General Public License along with
 * this program. If not, see http://www.gnu.org/licenses.
 * 
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section
 * 7.
 * 
 * If you modify this Program, or any covered work, by linking or
 * combining it with libraries listed in COPYRIGHT file at the
 * top-level directory of this distribution (or a modified version of
 * that libraries), containing parts covered by the terms of licenses
 * cited in the COPYRIGHT file, the licensors of this Program grant
 * you additional permission to convey the resulting work.
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.novaforge.forge.plugins.devops.novadeploy.client.NovadeployClient;
import org.novaforge.forge.plugins.devops.novadeploy.client.NovadeployClientException;
import org.novaforge.forge.plugins.devops.novadeploy.client.NovadeployRestClient;
import org.novaforge.forge.plugins.devops.novadeploy.client.data.AccountState;
import org.novaforge.forge.plugins.devops.novadeploy.client.data.CustomerDescriptor;
import org.novaforge.forge.plugins.devops.novadeploy.client.data.CustomerEnvironment;
import org.novaforge.forge.plugins.devops.novadeploy.client.data.Membership;
import org.novaforge.forge.plugins.devops.novadeploy.client.response.AccountOrganizationResponse;
import org.novaforge.forge.plugins.devops.novadeploy.client.response.CloudConfigResponse;
import org.novaforge.forge.plugins.devops.novadeploy.client.response.DeployResponse;
import org.novaforge.forge.plugins.devops.novadeploy.client.response.DescriptorResponse;
import org.novaforge.forge.plugins.devops.novadeploy.client.response.DescriptorsArborescenceResponse;
import org.novaforge.forge.plugins.devops.novadeploy.client.response.DescriptorsResponse;
import org.novaforge.forge.plugins.devops.novadeploy.client.response.EnvironmentResponse;
import org.novaforge.forge.plugins.devops.novadeploy.client.response.EnvironmentsResponse;
import org.novaforge.forge.plugins.devops.novadeploy.client.response.LogsResponse;
import org.novaforge.forge.plugins.devops.novadeploy.client.response.ServiceResponse;
import org.novaforge.forge.plugins.devops.novadeploy.client.response.SessionResponse;
import org.novaforge.forge.plugins.devops.novadeploy.client.response.StatusResponse;
import org.novaforge.forge.plugins.devops.novadeploy.datamapper.NovadeployResourceBuilder;
import org.novaforge.forge.plugins.devops.novadeploy.internal.NovadeployRestConstant;
import org.novaforge.forge.plugins.devops.novadeploy.model.NovadeployUser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * This class is used in order to instantiate new connector to
 * Novadeploy rest server.
 * 
 * @author dekimpea
 */
public class NovadeployRestClientImpl implements NovadeployRestClient
{
	/**
	 * Reference to implemention of {@link NovadeployResourceBuilder}
	 */
	private NovadeployResourceBuilder NovadeployResourceBuilder;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NovadeployClient getConnector(final String pToolUrl, String pUsername, String pPassword)
			throws NovadeployClientException
	{

		NovadeployClient restClient = new NovadeployClient(pToolUrl);
		SessionResponse response;
		boolean success = false;
		try
		{
			final List<String> user = new ArrayList<String>();
			user.add(pUsername);
			user.add(pPassword);

			WebResource resource = restClient.getClient().resource(
					String.format("%s%s", pToolUrl, NovadeployRestConstant.Novadeploy_METHOD_LOGIN));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("user", pUsername);
			map.putSingle("password", pPassword);

			// login to novadeploy
			ClientResponse clientResponse = resource.accept(MediaType.APPLICATION_JSON_TYPE)
					.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, map);
			// unmarshalling
			String json = clientResponse.getEntity(String.class);
			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(json, JsonObject.class);
			response = gson.fromJson(jo.get("response"), SessionResponse.class);

			if (response.getSession() != null && response.getSession().length() > 0)
			{
				success = true;
				restClient.setSessionToken(response.getSession());
			}

		} catch (final Exception e)
		{
			throw new NovadeployClientException(String.format(
					"Unable to authentificate on server with [toolurl=%s, user=%s]", pToolUrl, pUsername), e);
		}
		if (!success)
		{
			throw new NovadeployClientException(String.format(
					"Authentification has failed on server with [toolurl=%s, user=%s]", pToolUrl, pUsername),response);
		}

		return restClient;
	}

	// ///////////////////////////////////////////

	// Account Management

	// ///////////////////////////////////////////
	/**
	 * {@inheritDoc}
	 * 
	 * @return
	 */
	@Override
	public CloudConfigResponse validateCloud(NovadeployClient connector, final String project, final String login,
			final String cloudname, final String password, final String provider) throws NovadeployClientException
	{
		
		CloudConfigResponse response;
		try
		{
			WebResource resource = connector.getClient().resource(
					String.format("%s%s", connector.getToolUrl(),
							NovadeployRestConstant.Novadeploy_METHOD_VALIDATE_CLOUD));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", project);
			map.putSingle("login", login);
			map.putSingle("cloudname", cloudname);
			map.putSingle("password", password);
			map.putSingle("provider", provider);

			// create account on novadeploy
			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);
			// unmarshalling
			String json = clientResponse.getEntity(String.class);
			Gson gson = new Gson();
			
			JsonObject jo = gson.fromJson(json, JsonObject.class);
			
			jo = jo.get("response").getAsJsonObject();
			
			/*
			try
			{
				jo.getAsJsonArray("vdc");
				jo.getAsJsonArray("network");
			} catch (Exception e)
			{
				JsonArray vdc = new JsonArray();
				vdc.add(jo.get("vdc"));
				JsonArray network = new JsonArray();
				network.add(jo.get("network"));
				jo.add("vdc", vdc);
				jo.add("network", network);
			}
			*/

			response = gson.fromJson(jo, CloudConfigResponse.class);

		} catch (Exception e)
		{
			throw new NovadeployClientException(String.format("Unable to validate cloud [project=%s].", project), e);
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format("Error : %s.", response.getError()),response);
		}
		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configCloud(NovadeployClient connector, String project, String vCloudVDCName, String vCloudNetWork)
			throws NovadeployClientException
	{
		ServiceResponse response;
		try
		{

			WebResource resource = connector.getClient()
					.resource(
							String.format("%s%s", connector.getToolUrl(),
									NovadeployRestConstant.Novadeploy_METHOD_CONFIG_CLOUD));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", project);
			map.putSingle("vdc", vCloudVDCName);
			map.putSingle("network", vCloudNetWork);

			// create account on novadeploy
			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);
			// unmarshalling
			response = getBasicServiceResponse(clientResponse);

			
		} catch (Exception e)
		{
			throw new NovadeployClientException(String.format("Unable to config cloud [project=%s].", project), e);
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format("Error in NovaDeploy response", project));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteUser(NovadeployClient connector, final NovadeployUser pUser) throws NovadeployClientException
	{
		ServiceResponse response;
		try
		{
			WebResource resource = connector.getClient()
					.resource(
							String.format("%s%s", connector.getToolUrl(),
									NovadeployRestConstant.Novadeploy_METHOD_DELETE_USER));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("user", pUser.getUserName());

			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);
			// unmarshalling
			response = getBasicServiceResponse(clientResponse);

		} catch (final Exception e)
		{
			throw new NovadeployClientException(String.format("Unable to delete user on Novadeploy with [user=%s]",
					pUser.toString()), e);
		}
		// process response
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format("Failed to delete user on Novadeploy with [user=%s]",
					pUser.toString()),response);
		}

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean updateUser(NovadeployClient connector, final String pUserName, final NovadeployUser pUser)
			throws NovadeployClientException
	{
		ServiceResponse response;
		try
		{
			WebResource resource = connector.getClient()
					.resource(
							String.format("%s%s", connector.getToolUrl(),
									NovadeployRestConstant.Novadeploy_METHOD_UPDATE_USER));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("user", pUser.getUserName());
			map.putSingle("password", pUser.getPassword());
			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);
			// unmarshalling
			response = getBasicServiceResponse(clientResponse);

		} catch (final Exception e)
		{
			throw new NovadeployClientException(String.format("Unable to update user on Novadeploy with [user=%s]",
					pUser.toString()), e);
		}
		// process response
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format("Failed to update user on Novadeploy with [user=%s]",
					pUser.toString()),response);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createUser(NovadeployClient connector, final NovadeployUser pUser) throws NovadeployClientException
	{
		ServiceResponse response;
		try
		{

			WebResource resource = connector.getClient()
					.resource(
							String.format("%s%s", connector.getToolUrl(),
									NovadeployRestConstant.Novadeploy_METHOD_CREATE_USER));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("user", pUser.getUserName());
			map.putSingle("password", pUser.getPassword());
			// create user if not already existing on novadeploy
			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);
			// unmarshalling
			response = getBasicServiceResponse(clientResponse);

		} catch (final Exception e)
		{
			throw new NovadeployClientException(String.format("Unable to create user on Novadeploy with [user=%s]",
					pUser.toString()), e);
		}
		// ie error = 1 => user already existing
		if (response.getError() != null && response.getErrorNumber() != null
				&& !response.getErrorNumber().equals("1"))
		{
			throw new NovadeployClientException(String.format("Failed to create user on Novadeploy with [user=%s]",
					pUser.toString()),response);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createAccount(final NovadeployClient pClient, String pProjectId) throws NovadeployClientException
	{
		ServiceResponse response;
		try
		{
			WebResource resource = pClient.getClient().resource(
					String.format("%s%s", pClient.getToolUrl(), NovadeployRestConstant.Novadeploy_METHOD_INIT_CLOUD));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", pProjectId);

			// create account on novadeploy
			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, pClient, map);
			// unmarshalling
			response = getBasicServiceResponse(clientResponse);

		
		} catch (Exception e)
		{
			throw new NovadeployClientException(String.format("Unable to create project [project=%s].", pProjectId));
		}
		if (response.getError() != null && response.getError().length() > 0)
		{
			throw new NovadeployClientException(String.format("Failed to create project [project=%s].", pProjectId),response);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteUserPermission(NovadeployClient connector, final String pNameSpace, final String pUserName)
			throws NovadeployClientException
	{
		ServiceResponse response;
		boolean success = false;
		try
		{

			WebResource resource = connector.getClient().resource(
					String.format("%s%s", connector.getToolUrl(),
							NovadeployRestConstant.Novadeploy_METHOD_DELETE_PERMISSION));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("user", pUserName);
			map.putSingle("project", pNameSpace);

			// add membership
			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);
			// unmarshalling
			response = getBasicServiceResponse(clientResponse);

		} catch (final Exception e)
		{
			throw new NovadeployClientException(
					String.format("Unable to delete permission to namespace with [namespace=%s, username=%s]",
							pNameSpace, pUserName), e);
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(
					String.format("Failed to delete permission to namespace with [namespace=%s, username=%s]",
							pNameSpace, pUserName), response);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserPermissionToNameSpace(final NovadeployClient connector, final String pNameSpace,
			final String pUserName, final int pPermissionId) throws NovadeployClientException
	{
		ServiceResponse response;
		try
		{

			WebResource resource = connector.getClient().resource(
					String.format("%s%s", connector.getToolUrl(),
							NovadeployRestConstant.Novadeploy_METHOD_SET_PERMISSION));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("user", pUserName);
			map.putSingle("project", pNameSpace);
			map.putSingle("role", this.getRoleFromId(pPermissionId));

			// add membership
			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);
			// unmarshalling
			response = getBasicServiceResponse(clientResponse);

		
		} catch (final Exception e)
		{
			throw new NovadeployClientException(String.format(
					"Unable to set permission to namespace with [namespace=%s, username=%s, permission=%s]",
					pNameSpace, pUserName, pPermissionId), e);
		}
		// process response
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format(
					"Failed to set permission to namespace with [namespace=%s, username=%s, permission=%s]",
					pNameSpace, pUserName, pPermissionId), response);
		}
	}

	/*
	 * Method used to get the novadeploy role with a permission id
	 */
	private String getRoleFromId(final int pPermissionId) throws Exception
	{
		switch (pPermissionId)
		{
		case 1:
			return "MANAGER";
		case 4:
			return "TESTER";
		case 8:
			return "ARCHITECT";
		}
		throw new Exception(String.format("Failed to match novadeploy role with  [permissionid=%s]", pPermissionId));
	}

	/**
	 * Methods used to query novadeploy server
	 * 
	 * @param resource
	 * @param connector
	 * @param map
	 * @return
	 */
	private ClientResponse getAuthenticatedClientResponse(WebResource resource, NovadeployClient connector,
			MultivaluedMap<String, String> map)
	{
		return resource.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_FORM_URLENCODED)
				.header("auth_token", connector.getSessionToken()).post(ClientResponse.class, map);
	}

	private ClientResponse getAuthenticatedClientResponse(WebResource resource, NovadeployClient connector)
	{
		return resource.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_FORM_URLENCODED)
				.header("auth_token", connector.getSessionToken()).post(ClientResponse.class);
	}

	/**
	 * Use to get a service response from a client response
	 * 
	 * @param clientResponse
	 * @return
	 */
	private ServiceResponse getBasicServiceResponse(ClientResponse clientResponse)
	{
		String json = clientResponse.getEntity(String.class);
		Gson gson = new Gson();
		JsonObject jo = gson.fromJson(json, JsonObject.class);
		return gson.fromJson(jo.get("response"), ServiceResponse.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void lockAccount(NovadeployClient connector, String pProjectId) throws NovadeployClientException
	{
		ServiceResponse response;
		try
		{

			WebResource resource = connector.getClient().resource(
					String.format("%s%s", connector.getToolUrl(), NovadeployRestConstant.Novadeploy_METHOD_LOCK_CLOUD));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", pProjectId);

			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);

			// unmarshalling
			response = getBasicServiceResponse(clientResponse);


		} catch (Exception e)
		{
			throw new NovadeployClientException(String.format("Unable to lock project [project=%s].", pProjectId));
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format("Error detected on Novadeploy [project=%s].",
					pProjectId),response);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unlockAccount(NovadeployClient connector, String pProject) throws NovadeployClientException
	{
		ServiceResponse response;
		try
		{
			WebResource resource = connector.getClient()
					.resource(
							String.format("%s%s", connector.getToolUrl(),
									NovadeployRestConstant.Novadeploy_METHOD_UNLOCK_CLOUD));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", pProject);

			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);

			// unmarshalling
			response = getBasicServiceResponse(clientResponse);

			
		} catch (Exception e)
		{
			throw new NovadeployClientException(String.format("Unable to unlock project [project=%s].", pProject));
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format(
					"Failed to unlock project on Novadeploy [project=%s].", pProject),response);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountState accountStatus(NovadeployClient connector, String pProject) throws NovadeployClientException
	{
		StatusResponse response;
		try
		{
			WebResource resource = connector.getClient()
					.resource(
							String.format("%s%s", connector.getToolUrl(),
									NovadeployRestConstant.Novadeploy_METHOD_STATUS_CLOUD));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", pProject);

			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);

			// unmarshalling
			String json = clientResponse.getEntity(String.class);
			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(json, JsonObject.class);

			response = gson.fromJson(jo.get("response"), StatusResponse.class);
			
		} catch (Exception e)
		{
			throw new NovadeployClientException(
					String.format("Unable to get status of project [project=%s].", pProject));
		}
		if (response.getErrorNumber() != null && response.getErrorNumber().equals("1"))
		{
			return AccountState.UNKNOWN;
		} else if (response.hasError())
		{
			throw new NovadeployClientException(String.format("Failed to get status of project [error=%s].",
					response.getError()),response);
		}
		return AccountState.valueOf(response.state);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Membership> accountMemberships(NovadeployClient connector, String pProject)
			throws NovadeployClientException
	{
		AccountOrganizationResponse response;
		List<Membership> memberships = null;
		try
		{
			WebResource resource = connector.getClient().resource(
					String.format("%s%s", connector.getToolUrl(),
							NovadeployRestConstant.Novadeploy_METHOD_GET_ACCOUNT_MEMBERSHIPS));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", pProject);

			// create account on novadeploy
			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);
			// unmarshalling
			String json = clientResponse.getEntity(String.class);
			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(json, JsonObject.class);
			jo = jo.get("response").getAsJsonObject();

			// do trick to retrieve array of one element... see jira
			// story &
			// mantis bug => json array one element
			/*
			try
			{
				jo.getAsJsonArray("memberships");
			} catch (Exception e)
			{
				JsonArray membershipsJson = new JsonArray();
				membershipsJson.add(jo.get("memberships"));

				jo.add("memberships", membershipsJson);

			}
			*/

			response = gson.fromJson(jo, AccountOrganizationResponse.class);

		} catch (Exception e)
		{
			throw new NovadeployClientException(String.format("Unable to get organization of project [project=%s].",
					pProject), e);
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format("An error occured [error=%s].", response.getError()),response);
		}
		memberships = Arrays.asList(response.getMemberships());
		return memberships;
	}

	// ///////////////////////////////////////////
	//
	// Descriptor Management
	//
	// ///////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createDescriptor(NovadeployClient connector, String descriptorName, String descriptorContent, String project)

	throws NovadeployClientException
	{
		ServiceResponse response;
		try
		{
			WebResource resource = connector.getClient().resource(
					String.format("%s%s", connector.getToolUrl(),
							NovadeployRestConstant.Novadeploy_METHOD_ADD_DESCRIPTOR));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", project);
			map.putSingle("name", descriptorName);
			map.putSingle("content", descriptorContent);

			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);
			// unmarshalling
			response = getBasicServiceResponse(clientResponse);


		}catch (Exception e)
		{
			throw new NovadeployClientException(String.format("Failed to add a descriptor on project [project=%s].",
					project), e);
		}
		if (response.hasError() || response.hasParsingError())
		{
			throw new NovadeployClientException(String.format(
					"Error detected on NovaDeploy : unable to add descriptor on project  [project=%s]. %s",
					project, response.parserMessageError), response);
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forkDescriptor(NovadeployClient connector, String project, String descriptorName,
			int descriptorVersion, String forkName)
	throws NovadeployClientException
	{
		ServiceResponse response;
		try
		{
			WebResource resource = connector.getClient().resource(
					String.format("%s%s", connector.getToolUrl(),
							NovadeployRestConstant.Novadeploy_METHOD_FORK_DESCRIPTOR));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", project);
			map.putSingle("name", descriptorName);
			map.putSingle("version", Integer.toString(descriptorVersion));
			map.putSingle("forkName", forkName);

			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);
			// unmarshalling
			response = getBasicServiceResponse(clientResponse);

			
		}catch (Exception e)
		{
			throw new NovadeployClientException(String.format("Failed to fork a descriptor on project [project=%s].",
					project), e);
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format(
					"Error detected on NovaDeploy : unable to fork descriptor on project  [project=%s].", project),response);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDescriptor(NovadeployClient connector, String descriptorName, String descriptorContent, String project)

	throws NovadeployClientException
	{
		ServiceResponse response;
		try
		{
			WebResource resource = connector.getClient().resource(
					String.format("%s%s", connector.getToolUrl(),
							NovadeployRestConstant.Novadeploy_METHOD_UPDATE_DESCRIPTOR));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", project);
			map.putSingle("name", descriptorName);
			map.putSingle("content", descriptorContent);

			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);
			// unmarshalling
			response = getBasicServiceResponse(clientResponse);

		} catch (Exception e)
		{
			throw new NovadeployClientException(String.format("Failed to update a descriptor on project [project=%s].",
					project), e);
		}
		if (response.hasError() || response.hasParsingError())
		{
			throw new NovadeployClientException(
					String.format(
							"Error detected on NovaDeploy : unable to update descriptor on project  [error=%s][project=%s].",
							project),response);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateDescriptor(NovadeployClient connector, String project, String descriptorName)

	throws NovadeployClientException
	{
		ServiceResponse response;
		try
		{
			WebResource resource = connector.getClient().resource(
					String.format("%s%s", connector.getToolUrl(),
							NovadeployRestConstant.Novadeploy_METHOD_VALIDATE_DESCRIPTOR));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", project);
			map.putSingle("name", descriptorName);

			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);
			// unmarshalling
			response = getBasicServiceResponse(clientResponse);

		}  
		catch (Exception e)
		{
			throw new NovadeployClientException(String.format(
					"Failed to validate a descriptor on project [project=%s].", project), e);
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(
					String.format(
							"Error detected on NovaDeploy : unable to validate descriptor on project  [error=%s][project=%s].",
							response.getError(), project),response);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerDescriptor[] getDescriptors(NovadeployClient connector, String project)

	throws NovadeployClientException
	{
		DescriptorsResponse response;
		try
		{
			WebResource resource = connector.getClient().resource(
					String.format("%s%s", connector.getToolUrl(),
							NovadeployRestConstant.Novadeploy_METHOD_GET_DESCRIPTORS));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", project);

			// create account on novadeploy
			ClientResponse clientResponse = resource.accept(MediaType.APPLICATION_JSON_TYPE)
					.type(MediaType.APPLICATION_FORM_URLENCODED).header("auth_token", connector.getSessionToken())
					.post(ClientResponse.class, map);
			// unmarshalling
			String json = clientResponse.getEntity(String.class);
			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(json, JsonObject.class);
			jo = jo.get("response").getAsJsonObject();
			try
			{
				jo.getAsJsonArray("descriptors");
			} catch (Exception e)
			{
				JsonArray vdc = new JsonArray();
				vdc.add(jo.get("descriptors"));
				jo.add("descriptors", vdc);
			}

			response = gson.fromJson(jo, DescriptorsResponse.class);

		} catch (Exception e)
		{
			throw new NovadeployClientException(String.format(
					"Unable to retrieve descriptors of project [project=%s].", project));
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format("An error occured [error=%s].", response.getError()),response);
		}
		return response.getEnvironments();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerDescriptor[] getDescriptorsTree(NovadeployClient connector, String project)

	throws NovadeployClientException
	{
		DescriptorsArborescenceResponse response;
		try
		{
			WebResource resource = connector.getClient().resource(
					String.format("%s%s", connector.getToolUrl(),
							NovadeployRestConstant.Novadeploy_METHOD_GET_DESCRIPTORS_TREE));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", project);

			// create account on novadeploy
			ClientResponse clientResponse = resource.accept(MediaType.APPLICATION_JSON_TYPE)
					.type(MediaType.APPLICATION_FORM_URLENCODED).header("auth_token", connector.getSessionToken())
					.post(ClientResponse.class, map);
			// unmarshalling
			String json = clientResponse.getEntity(String.class);
			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(json, JsonObject.class);
			jo = jo.get("response").getAsJsonObject();
			try
			{
				jo.getAsJsonArray("descriptors");
			} catch (Exception e)
			{
				JsonArray vdc = new JsonArray();
				vdc.add(jo.get("descriptors"));
				jo.add("descriptors", vdc);
			}

			response = gson.fromJson(jo, DescriptorsArborescenceResponse.class);

		} catch (Exception e)
		{
			throw new NovadeployClientException(String.format(
					"Unable to retrieve descriptors of project [project=%s].", project));
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format("An error occured [error=%s].", response.getError()));
		}
		return response.getArborescence();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public DescriptorResponse getDescriptor(NovadeployClient connector, int descriptorVersion, String descriptorName, String project)

	throws NovadeployClientException
	{
		DescriptorResponse response;
		try
		{
			WebResource resource = connector.getClient().resource(
					String.format("%s%s", connector.getToolUrl(),
							NovadeployRestConstant.Novadeploy_METHOD_GET_DESCRIPTOR));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", project);
			map.putSingle("name", descriptorName);
			map.putSingle("version", Integer.toString(descriptorVersion));

			ClientResponse clientResponse = resource.accept(MediaType.APPLICATION_JSON_TYPE)
					.type(MediaType.APPLICATION_FORM_URLENCODED).header("auth_token", connector.getSessionToken())
					.post(ClientResponse.class, map);
			// unmarshalling
			String json = clientResponse.getEntity(String.class);
			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(json, JsonObject.class);
			jo = jo.get("response").getAsJsonObject();

			response = gson.fromJson(jo, DescriptorResponse.class);

			if (response.hasError())
			{
				throw new NovadeployClientException(String.format("An error occured [error=%s].", response.getError()));
			}
		} catch (Exception e)
		{
			throw new NovadeployClientException(String.format(
					"Unable to retrieve descriptors of project [project=%s].", project));
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format("An error occured [error=%s].", response.getError()));
		}
		return response;
	}

	// ///////////////////////////////////////////
	//
	// Environment Management
	//
	// ///////////////////////////////////////////
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long deployEnvironment(NovadeployClient connector, String descriptorName, int descriptorVersion, String project)
			throws NovadeployClientException
	{
		DeployResponse response;
		try
		{
			WebResource resource = connector.getClient().resource(
					String.format("%s%s", connector.getToolUrl(),
							NovadeployRestConstant.Novadeploy_METHOD_DEPLOY_ENVIRONMENT));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", project);
			map.putSingle("descriptor", descriptorName);
			map.putSingle("version", Integer.toString(descriptorVersion));

			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);
			// unmarshalling
			String json = clientResponse.getEntity(String.class);
			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(json, JsonObject.class);
			response = gson.fromJson(jo.get("response"), DeployResponse.class);


		}  
		catch (Exception e)
		{
			throw new NovadeployClientException(String.format("Failed to launch deployment on project [project=%s].",
					project), e);
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(
					String.format(
							"Error detected on NovaDeploy : unable to launch deployment on project  [error=%s][project=%s].",
							response.getError(), project),response);
		}
		return response.getTimeReferenceID();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] deploymentStatus(NovadeployClient connector, long timeRefId) throws NovadeployClientException
	{
		LogsResponse response;
		try
		{
			String s = String.format("%s%s%s", connector.getToolUrl(),
					NovadeployRestConstant.Novadeploy_METHOD_DEPLOY_ENVIRONMENT_STATUS, "/" + Long.toString(timeRefId));
			WebResource resource = connector.getClient().resource(s);

			ClientResponse clientResponse = resource.accept(MediaType.APPLICATION_JSON_TYPE)
					.type(MediaType.APPLICATION_FORM_URLENCODED).header("auth_token", connector.getSessionToken())
					.get(ClientResponse.class);

			// unmarshalling
			String json = clientResponse.getEntity(String.class);
			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(json, JsonObject.class);
			jo = jo.get("response").getAsJsonObject();
			/*
			try
			{
				jo.getAsJsonArray("logs");
			} catch (Exception e)
			{
				JsonArray logsJson = new JsonArray();
				logsJson.add(jo.get("logs"));
				jo.add("logs", logsJson);
			}
			*/
			response = gson.fromJson(jo, LogsResponse.class);


		}  
		catch (Exception e)
		{
			throw new NovadeployClientException(String.format("Failed to fetch logs about deployment [project=%s].",
					Long.toString(timeRefId)), e);
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format(
					"Error detected on NovaDeploy : unable to fetch logs about deployment [error=%s][project=%s].",
					response.getError(), Long.toString(timeRefId)),response);
		}
		return response.getLogs();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteEnvironment(NovadeployClient connector, String project, long timeRefId)

	throws NovadeployClientException
	{
		ServiceResponse response;
		try
		{
			WebResource resource = connector.getClient()
					.resource(
							String.format("%s%s%s", connector.getToolUrl(),
									NovadeployRestConstant.Novadeploy_METHOD_DELETE_ENVIRONMENT,
									"/" + Long.toString(timeRefId)));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", project);

			ClientResponse clientResponse = getAuthenticatedClientResponse(resource, connector, map);
			// unmarshalling
			response = getBasicServiceResponse(clientResponse);


		}  
		catch (Exception e)
		{
			throw new NovadeployClientException(String.format(
					"Failed to delete environment on project [envID=%s][project=%s].", Long.toString(timeRefId),
					project), e);
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(
					String.format(
							"Error detected on NovaDeploy : unable to delete environment on project [error=%s][envID=%s][project=%s].",
							response.getError(), Long.toString(timeRefId), project),response);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerEnvironment[] listEnvironments(NovadeployClient connector, String project)
			throws NovadeployClientException
	{
		EnvironmentsResponse response;
		try
		{
			WebResource resource = connector.getClient().resource(
					String.format("%s%s", connector.getToolUrl(),
							NovadeployRestConstant.Novadeploy_METHOD_LIST_ENVIRONMENTS));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", project);

			// create account on novadeploy
			ClientResponse clientResponse = resource.accept(MediaType.APPLICATION_JSON_TYPE)
					.type(MediaType.APPLICATION_FORM_URLENCODED).header("auth_token", connector.getSessionToken())
					.post(ClientResponse.class, map);
			// unmarshalling
			String json = clientResponse.getEntity(String.class);
			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(json, JsonObject.class);
			jo = jo.get("response").getAsJsonObject();
			try
			{
				jo.getAsJsonArray("environments");
			} catch (Exception e)
			{
				JsonArray environmentsJson = new JsonArray();
				environmentsJson.add(jo.get("environments"));
				jo.add("environments", environmentsJson);
			}

			response = gson.fromJson(jo, EnvironmentsResponse.class);
			
		}  
		catch (Exception e)
		{
			throw new NovadeployClientException(String.format(
					"Failed to get list of environments on project [project=%s].", project), e);
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format(
					"An error occured when requesting list of environment on project [project=%s] [error=%s].",
					project, response.getError()),response);
		}
		return response.getEnvironments();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerEnvironment getEnvironment(NovadeployClient connector, String project, long timeRefId)
			throws NovadeployClientException
	{
		EnvironmentResponse response;
		try
		{
			WebResource resource = connector.getClient()
					.resource(
							String.format("%s%s%s", connector.getToolUrl(),
									NovadeployRestConstant.Novadeploy_METHOD_GET_ENVIRONMENT,
									"/" + Long.toString(timeRefId)));

			MultivaluedMap<String, String> map = new MultivaluedMapImpl();
			map.putSingle("project", project);

			// create account on novadeploy
			ClientResponse clientResponse = resource.accept(MediaType.APPLICATION_JSON_TYPE)
					.type(MediaType.APPLICATION_FORM_URLENCODED).header("auth_token", connector.getSessionToken())
					.post(ClientResponse.class, map);
			/// unmarshalling
			String json = clientResponse.getEntity(String.class);
			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(json, JsonObject.class);
			jo = jo.get("response").getAsJsonObject();

			response = gson.fromJson(jo, EnvironmentResponse.class);


		} catch (Exception e)
		{
			throw new NovadeployClientException(String.format(
					"Failed to get list of environments on project [project=%s].", project), e);
		}
		if (response.hasError())
		{
			throw new NovadeployClientException(String.format("An error occured [error=%s].", response.getError()),response);
		}
		return response.getEnvironment();
	}


	/**
	 * Used by container to inject service implementation of
	 * {@link NovadeployResourceBuilder}
	 *
	 * @param pNovadeployResourceBuilder
	 *            the NovadeployResourceBuilder to set
	 */

	public void setNovadeployResourceBuilder(final NovadeployResourceBuilder pNovadeployResourceBuilder)
	{
		NovadeployResourceBuilder = pNovadeployResourceBuilder;
	}

}