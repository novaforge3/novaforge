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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;
import org.novaforge.forge.plugins.ecm.alfresco.datamapper.AlfrescoResourceBuilder;
import org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestClient;
import org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestHelper;
import org.novaforge.forge.plugins.ecm.alfresco.rest.exceptions.AlfrescoRestException;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoConfigurationService;

import java.util.Map;

/**
 * This is an implementation of PluginUserService for Alfresco plugin.
 * 
 * @see org.novaforge.forge.plugins.commons.services.AbstractPluginUserService
 * @see org.novaforge.forge.core.plugins.services.PluginUserService
 * @author cadetr
 */
public class AlfrescoUserServiceImpl extends AbstractPluginUserService
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
	public void createAdministratorUser(final ToolInstance pInstance, final PluginUser pUser)
	    throws PluginServiceException
	{
		final Map<String, String> user = alfrescoResourceBuilder.createUser(pUser);
		try
		{
			// Getting alfresco connector

			final AlfrescoRestHelper connector = alfrescoRestClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

			final String userLogin = pUser.getLogin();
			final JSONObject userAdmin = alfrescoRestClient.getUser(connector, userLogin);
			if (userAdmin == null)
			{
				alfrescoRestClient.createUser(connector, user);
			}
			final JSONArray jsonArray = alfrescoRestClient.getUserAdmin(connector);

			if (!isAdminUser(jsonArray, userLogin))
			{
				alfrescoRestClient.createUserAdmin(connector, user);
			}
		}
		catch (final AlfrescoRestException e)
		{
			throw new PluginServiceException("Unable to connect to Alfresco Rest Services.", e);
		}
		catch (final JSONException e)
		{
			throw new PluginServiceException("Unable to get to Alfresco Administrators.", e);
		}

	}

	private boolean isAdminUser(final JSONArray pJSONArray, final String pUserLogin) throws JSONException
	{
		boolean isAdmin = false;
		for (int i = 0; i < pJSONArray.length(); i++)
		{
			final JSONObject json = pJSONArray.getJSONObject(i);
			if (json.getString("shortName").equals(pUserLogin))
			{
				isAdmin = true;
			}
		}
		return isAdmin;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removeToolUser(final InstanceConfiguration pInstance, final PluginUser pUser)
			throws PluginServiceException
	{
		final Map<String, String> user = alfrescoResourceBuilder.createUser(pUser);
		try
		{
			// Getting alfresco connector

			final AlfrescoRestHelper connector = alfrescoRestClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			alfrescoRestClient.deleteUser(connector, user);
		}
		catch (final AlfrescoRestException e)
		{
			throw new PluginServiceException("Unable to connect to Alfresco Rest Services.", e);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolUser(final InstanceConfiguration pInstance, final String pUserName,
																	 final PluginUser pUser) throws PluginServiceException
	{
		final Map<String, String> user = alfrescoResourceBuilder.createUser(pUser);
		try
		{
			// Getting alfresco connector

			final AlfrescoRestHelper connector = alfrescoRestClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			alfrescoRestClient.updateUser(connector, user);
		}
		catch (final AlfrescoRestException e)
		{
			throw new PluginServiceException("Unable to connect to Alfresco Rest Services.", e);
		}
		return true;
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
