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
package org.novaforge.forge.plugins.forum.phpbb.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;
import org.novaforge.forge.plugins.forum.phpbb.client.PhpBBSoapClient;
import org.novaforge.forge.plugins.forum.phpbb.client.PhpBBSoapConnector;
import org.novaforge.forge.plugins.forum.phpbb.client.PhpBBSoapException;
import org.novaforge.forge.plugins.forum.phpbb.datamapper.PhpBBResourceBuilder;
import org.novaforge.forge.plugins.forum.phpbb.model.PhpbbUser;

import java.math.BigInteger;

/**
 * @author caseryj
 */
public class PhpBBUserServiceImpl extends AbstractPluginUserService implements PluginUserService
{

	/**
	 * Reference to service implementation of {@link PhpBBSoapClient}
	 */
	private PhpBBSoapClient            phpbbSoapClient;
	/**
	 * Reference to service implementation of {@link PhpBBResourceBuilder}
	 */
	private PhpBBResourceBuilder       phpbbResourceBuilder;
	/**
	 * Reference to service implementation of {@link PluginConfigurationService}
	 */
	private PluginConfigurationService pluginConfigurationService;

	/**
	 * {{@inheritDoc}
	 */
	@Override
	protected boolean updateToolUser(final InstanceConfiguration pInstance, final String pUserName,
	    final PluginUser pPluginUser) throws PluginServiceException
	{
		final boolean success = false;
		try
		{
			// Obtain phpbb connector
			final PhpBBSoapConnector connector = phpbbSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

			final PhpbbUser account = phpbbResourceBuilder.buildAccountData(pPluginUser);
			phpbbSoapClient.pc_account_update(connector, account);

		}
		catch (final PhpBBSoapException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to update user with instance=%s, username=%s and User=%s", pInstance.toString(), pUserName,
			    pPluginUser.toString()), e);

		}
		return success;
	}

	/**
	 * {{@inheritDoc}
	 */
	@Override
	protected boolean removeToolUser(final InstanceConfiguration pInstance, final PluginUser pUser)
	    throws PluginServiceException
	{

		final boolean success = false;
		try
		{
			// Obtain phpbb connector
			final PhpBBSoapConnector connector = phpbbSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			final PhpbbUser account = phpbbResourceBuilder.buildAccountData(pUser);
			phpbbSoapClient.pc_account_delete(connector, account);
		}
		catch (final PhpBBSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to delete user with instance=%s and User=%s",
			    pInstance.toString(), pUser.toString()), e);
		}
		return success;
	}

	@Override
	public void createAdministratorUser(final ToolInstance pInstance, final PluginUser pUser)
	    throws PluginServiceException
	{
		try
		{
			// Obtain phpbb connector
			final PhpBBSoapConnector connector = phpbbSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

			final PhpbbUser account = phpbbResourceBuilder.buildAccountData(pUser);

			final BigInteger userId = phpbbSoapClient.pc_account_get(connector, account);

			if (userId.longValue() < 0)
			{
				phpbbSoapClient.pc_create_superadmin(connector, account);
				phpbbSoapClient.pc_purge_cache(connector);
			}

		}
		catch (final PhpBBSoapException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to get user with instance=%s, username=%s and User=%s", phpbbSoapClient.toString(),
			    pUser.getName(), pUser.toString()), e);

		}
	}

	/**
	 * Use by container to inject {@link PhpBBSoapClient}
	 * 
	 * @param pPhpbbSoapClient
	 *          the phpbbSoapClient to set
	 */
	public void setPhpbbSoapClient(final PhpBBSoapClient pPhpbbSoapClient)
	{
		phpbbSoapClient = pPhpbbSoapClient;
	}

	/**
	 * Use by container to inject {@link PhpBBResourceBuilder}
	 * 
	 * @param pPhpbbResourceBuilder
	 *          the phpbbResourceBuilder to set
	 */
	public void setPhpbbResourceBuilder(final PhpBBResourceBuilder pPhpbbResourceBuilder)
	{
		phpbbResourceBuilder = pPhpbbResourceBuilder;
	}

	/**
	 * Use by container to inject {@link PluginConfigurationService}
	 * 
	 * @param pPluginConfigurationService
	 *          the pluginConfigurationService to set
	 */
	public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
	{
		pluginConfigurationService = pPluginConfigurationService;
	}
}
