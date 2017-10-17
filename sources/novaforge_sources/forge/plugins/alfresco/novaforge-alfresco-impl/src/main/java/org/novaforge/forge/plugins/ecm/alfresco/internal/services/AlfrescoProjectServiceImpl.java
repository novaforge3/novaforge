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
package org.novaforge.forge.plugins.ecm.alfresco.internal.services;

import org.json.JSONException;
import org.json.JSONObject;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;
import org.novaforge.forge.plugins.ecm.alfresco.datamapper.AlfrescoResourceBuilder;
import org.novaforge.forge.plugins.ecm.alfresco.datamapper.AlfrescoResourceBuilderException;
import org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestClient;
import org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestHelper;
import org.novaforge.forge.plugins.ecm.alfresco.rest.exceptions.AlfrescoRestException;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoConfigurationService;

import java.util.List;
import java.util.Map;

/**
 * This is an implementation of PluginProjectService for Alfresco plugin.
 * 
 * @see org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService
 * @see org.novaforge.forge.core.plugins.services.PluginProjectService
 * @author cadetr
 */
public class AlfrescoProjectServiceImpl extends AbstractPluginProjectService
{

	/**
	 * Reference to service implementation of {@link AlfrescoResourceBuilder}
	 */
	private AlfrescoResourceBuilder      alfrescoResourceBuilder;
	/**
	 * Reference to service implementation of {@link AlfrescoRestClient}
	 */
	private AlfrescoRestClient           alfrescoRestClient;
	/**
	 * Reference to service implementation of {@link AlfrescoConfigurationService}
	 */
	private AlfrescoConfigurationService pluginConfigurationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String createToolProject(final InstanceConfiguration pInstanceConfiguration,
	    final PluginProject pPluginProject, final List<PluginMembership> pMemberships)
	    throws PluginServiceException
	{
		String projectId = "";
		JSONObject project = null;
		try
		{
			project = alfrescoResourceBuilder.createProject(pPluginProject,
			    pInstanceConfiguration.getConfigurationId());

			// Getting alfresco connector
			final AlfrescoRestHelper connector = alfrescoRestClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

			// Create project to Alfresco
			alfrescoRestClient.createSiteProject(connector, project);

			// Get project Id
			projectId = project.getString("shortName");

			// Add user to alfresco with correct roles
			addUsers(connector, pInstanceConfiguration.getInstanceId(), projectId, pMemberships);

		}
		catch (final AlfrescoResourceBuilderException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to build JSON object reprensenting forge project with [instance=%s, project=%s]",
			    pInstanceConfiguration.toString(), pPluginProject.toString()), e);
		}
		catch (final AlfrescoRestException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to create project to Alfresco Instance with [instance=%s, project=%s, memberships=%s]",
			    pInstanceConfiguration.toString(), pPluginProject.toString(), pMemberships.toString()), e);
		}
		catch (final JSONException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to get project id from JSON Object created with [JSON=%s]", project.toString()), e);
		}
		return projectId;
	}

	/**
	 * This method will add to users specific roles which will allow them to use repository. If the user is
	 * already existing on alfresco instance, this one will be just updated.
	 * 
	 * @param pInstanceId
	 *          represents instance's id
	 * @param pProjectId
	 *          represents repository's id
	 * @param pMemberships
	 *          represents mapping between users and roles
	 * @throws AlfrescoRestException
	 *           occured if the communication with alfresco instance failed, or if any http errors occured.
	 * @throws PluginServiceException
	 *           occured if tool role cannot be found.
	 */
	private void addUsers(final AlfrescoRestHelper pConnector, final String pInstanceId,
	    final String pProjectId, final List<PluginMembership> pMemberships) throws AlfrescoRestException,
	    PluginServiceException
	{
		for (final PluginMembership pluginMembership : pMemberships)
		{
			final String toolRole = pluginRoleMappingService.getToolRole(pInstanceId, pluginMembership.getRole());
			final String toolRoleId = pluginRoleMappingService.getToolRoleId(toolRole);

			final PluginUser pluginUser = pluginMembership.getPluginUser();
			final JSONObject user = alfrescoRestClient.getUser(pConnector, pluginUser.getLogin());

			try
			{
				if (user == null)
				{
					// Create the user
					final Map<String, String> pUser = alfrescoResourceBuilder.createUser(pluginUser);
					alfrescoRestClient.createUser(pConnector, pUser);

					// Add the user to the project with the specific role
					final JSONObject pUserProject = alfrescoResourceBuilder.inviteUser(pluginUser, pProjectId,
					    toolRoleId);
					alfrescoRestClient.inviteSiteUser(pConnector, pUserProject);

				}
				else
				{
					// Add the user to the project with the specific role
					final JSONObject pUserProject = alfrescoResourceBuilder.inviteUser(pluginUser, pProjectId,
					    toolRoleId);
					alfrescoRestClient.inviteSiteUser(pConnector, pUserProject);
				}
			}
			catch (final AlfrescoResourceBuilderException e)
			{
				throw new PluginServiceException(
				    String.format(
				        "Unable to build JSON object which represents an alfresco membership with [project_id=%s, user=%s, role=%s]",
				        pProjectId, pluginUser.toString(), toolRoleId), e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolProject(final InstanceConfiguration pInstanceConfiguration,
	    final PluginProject pProject) throws PluginServiceException
	{
		final JSONObject project = new JSONObject();
		try
		{
			project.put("shortName", pInstanceConfiguration.getToolProjectId());
			project.put("title", pProject.getName());
			project.put("description", pProject.getDescription());
		}
		catch (final JSONException e1)
		{
			throw new PluginServiceException(String.format(
			    "Unable to update project to Alfresco Instance with [instance=%s, toolprojectid=%s, project=%s]",
			    pInstanceConfiguration.toString(), pInstanceConfiguration.getToolProjectId(), pProject.toString()),
			    e1);
		}

		try
		{
			// Getting alfresco connector
			final AlfrescoRestHelper connector = alfrescoRestClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

			// Update project to Alfresco
			alfrescoRestClient.updateSiteProject(connector, project);

		}
		catch (final AlfrescoRestException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to update project to Alfresco Instance with [instance=%s, toolprojectid=%s, project=%s]",
			    pInstanceConfiguration.toString(), pInstanceConfiguration.getToolProjectId(), pProject.toString()),
			    e);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean deleteToolProject(final InstanceConfiguration pInstanceConfiguration)
	    throws PluginServiceException
	{
		final JSONObject project = new JSONObject();
		try
		{
			project.put("shortName", pInstanceConfiguration.getToolProjectId());
		}
		catch (final JSONException e1)
		{
			throw new PluginServiceException(String.format(
			    "Unable to delete project to Alfresco Instance with [projectName=%s]",
			    pInstanceConfiguration.getToolProjectId()), e1);
		}
		try
		{
			// Getting alfresco connector
			final AlfrescoRestHelper connector = alfrescoRestClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

			// Delete an alfresco's project
			alfrescoRestClient.deleteSiteProject(connector, project);
		}
		catch (final AlfrescoRestException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to delete project to Alfresco Instance with [projectName=%s]",
			    pInstanceConfiguration.getToolProjectId()), e);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void archiveProject(final String pInstanceId) throws PluginServiceException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Use by container to inject {@link AlfrescoResourceBuilder}
	 * 
	 * @param pAlfrescoResourceBuilder
	 *          the alfrescoResourceBuilder to set
	 */
	public void setAlfrescoResourceBuilder(final AlfrescoResourceBuilder pAlfrescoResourceBuilder)
	{
		alfrescoResourceBuilder = pAlfrescoResourceBuilder;
	}

	/**
	 * Use by container to inject {@link AlfrescoRestClient}
	 * 
	 * @param pAlfrescoRestClient
	 *          the alfrescoRestClientCustom to set
	 */
	public void setAlfrescoRestClient(final AlfrescoRestClient pAlfrescoRestClient)
	{
		alfrescoRestClient = pAlfrescoRestClient;
	}

	/**
	 * Use by container to inject {@link AlfrescoConfigurationService}
	 * 
	 * @param pPluginConfigurationService
	 *          the configurationService to set
	 */
	public void setPluginConfigurationService(final AlfrescoConfigurationService pPluginConfigurationService)
	{
		pluginConfigurationService = pPluginConfigurationService;
	}
}
