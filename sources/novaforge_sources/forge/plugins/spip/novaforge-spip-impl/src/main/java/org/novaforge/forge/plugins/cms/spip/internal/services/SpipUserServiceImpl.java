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
package org.novaforge.forge.plugins.cms.spip.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.cms.spip.client.SpipSoapClient;
import org.novaforge.forge.plugins.cms.spip.client.SpipSoapConnector;
import org.novaforge.forge.plugins.cms.spip.client.SpipSoapException;
import org.novaforge.forge.plugins.cms.spip.datamapper.SpipResourceBuilder;
import org.novaforge.forge.plugins.cms.spip.services.SpipConfigurationService;
import org.novaforge.forge.plugins.cms.spip.soap.UserData;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;

/**
 * @author Guillaume Lamirand
 */
public class SpipUserServiceImpl extends AbstractPluginUserService implements PluginUserService
{

	/**
	 * Reference to service implementation of {@link SpipSoapClient}
	 */
	private SpipSoapClient           spipSoapClient;
	/**
	 * Reference to service implementation of {@link SpipResourceBuilder}
	 */
	private SpipResourceBuilder      spipResourceBuilder;
	/**
	 * Reference to service implementation of {@link PluginConfigurationService}
	 */
	private SpipConfigurationService pluginConfigurationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createAdministratorUser(final ToolInstance pInstance, final PluginUser pUser)
	    throws PluginServiceException
	{
		// already done at site creation. Nothing to do.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removeToolUser(final InstanceConfiguration pInstance, final PluginUser pUser)
			throws PluginServiceException
	{

		boolean success = false;
		try
		{
			// Obtain spip connector

			final SpipSoapConnector connector = spipSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL(),
			        pInstance.getToolProjectId()), pluginConfigurationService.getClientAdmin(),
			    pluginConfigurationService.getClientPwd());
			success = spipSoapClient.delete_user(connector, pUser.getLogin());
		}
		catch (final SpipSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to delete user with instance=%s and User=%s",
																										 pInstance.toString(), pUser.toString()), e);
		}
		return success;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolUser(final InstanceConfiguration pInstance, final String pUserName,
																	 final PluginUser pPluginUser) throws PluginServiceException
	{
		boolean success = false;
		try
		{
			// Obtain spip connector

			final SpipSoapConnector connector = spipSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL(),
			        pInstance.getToolProjectId()), pluginConfigurationService.getClientAdmin(),
			    pluginConfigurationService.getClientPwd());
			final UserData userData = spipResourceBuilder.buildUserData(pPluginUser);
			success = spipSoapClient.update_user(connector, pUserName, userData);
		}
		catch (final SpipSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to update user with instance=%s, username=%s and User=%s",
																										 pInstance.toString(), pUserName, pPluginUser.toString()), e);

		}
		return success;
	}

	/**
	 * Use by container to inject {@link SpipSoapClient}
	 * 
	 * @param pSpipSoapClient
	 *          the spipSoapClient to set
	 */
	public void setSpipSoapClient(final SpipSoapClient pSpipSoapClient)
	{
		spipSoapClient = pSpipSoapClient;
	}

	/**
	 * Use by container to inject {@link SpipResourceBuilder}
	 * 
	 * @param pSpipResourceBuilder
	 *          the spipResourceBuilder to set
	 */
	public void setSpipResourceBuilder(final SpipResourceBuilder pSpipResourceBuilder)
	{
		spipResourceBuilder = pSpipResourceBuilder;
	}

	/**
	 * Use by container to inject {@link SpipConfigurationService}
	 * 
	 * @param pPluginConfigurationService
	 *          the pluginConfigurationService to set
	 */
	public void setPluginConfigurationService(final SpipConfigurationService pPluginConfigurationService)
	{
		pluginConfigurationService = pPluginConfigurationService;
	}
}
