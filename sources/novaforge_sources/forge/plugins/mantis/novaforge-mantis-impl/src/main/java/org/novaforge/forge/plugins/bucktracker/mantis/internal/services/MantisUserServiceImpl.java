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
package org.novaforge.forge.plugins.bucktracker.mantis.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapClient;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapConnector;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapException;
import org.novaforge.forge.plugins.bucktracker.mantis.datamapper.MantisResourceBuilder;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;

import java.math.BigInteger;

/**
 * @author lamirang
 */
public class MantisUserServiceImpl extends AbstractPluginUserService implements PluginUserService
{

	/**
	 * This is a constant representing administrator access level on Mantis.
	 */
	private static final String ADMIN_ACCESS_LEVEL = "90";
	/**
	 * Reference to service implementation of {@link MantisSoapClient}
	 */
	private MantisSoapClient           mantisSoapClient;
	/**
	 * Reference to service implementation of {@link MantisResourceBuilder}
	 */
	private MantisResourceBuilder      mantisResourceBuilder;
	/**
	 * Reference to service implementation of {@link PluginConfigurationService}
	 */
	private PluginConfigurationService pluginConfigurationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createAdministratorUser(final ToolInstance pInstance, final PluginUser pUser)
			throws PluginServiceException
	{
		final AccountData account = mantisResourceBuilder.buildAccountData(pUser);
		account.setAccess(new BigInteger(ADMIN_ACCESS_LEVEL));

		try
		{

			// Obtain mantis connector
			final MantisSoapConnector connector = mantisSoapClient.getConnector(pluginConfigurationService
																																							.getClientURL(pInstance.getBaseURL()),
																																					pluginConfigurationService.getClientAdmin(),
																																					pluginConfigurationService.getClientPwd());
			BigInteger userId = mantisSoapClient.mc_account_get(connector, account.getName());
			if (userId == null)
			{
				userId = mantisSoapClient.mc_account_add(connector, account, pUser.getPassword());
			}
			else
			{
				mantisSoapClient.mc_account_update(connector, userId, account, pUser.getPassword());
			}
		}
		catch (final MantisSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to create administrator user with user=%s",
																										 pUser.toString()), e);
		}
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
			// Obtain mantis connector
			final MantisSoapConnector connector = mantisSoapClient.getConnector(pluginConfigurationService
																																							.getClientURL(pInstance.getToolInstance()
																																																		 .getBaseURL()),
																																					pluginConfigurationService.getClientAdmin(),
																																					pluginConfigurationService.getClientPwd());
			final BigInteger userId = mantisSoapClient.mc_account_get(connector, pUser.getLogin());
			success = mantisSoapClient.mc_account_delete(connector, userId);
		}
		catch (final MantisSoapException e)
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
			// Obtain mantis connector
			final MantisSoapConnector connector = mantisSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			final BigInteger userId = mantisSoapClient.mc_account_get(connector, pUserName);
			final AccountData accountData = mantisResourceBuilder.buildAccountData(pPluginUser);
			success = mantisSoapClient.mc_account_update(connector, userId, accountData, pPluginUser.getPassword());
		}
		catch (final MantisSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to update user with instance=%s, username=%s and User=%s",
																										 pInstance.toString(), pUserName, pPluginUser.toString()), e);

		}
		return success;
	}

	/**
	 * Use by container to inject {@link MantisSoapClient}
	 * 
	 * @param pMantisSoapClient
	 *          the mantisSoapClient to set
	 */
	public void setMantisSoapClient(final MantisSoapClient pMantisSoapClient)
	{
		mantisSoapClient = pMantisSoapClient;
	}

	/**
	 * Use by container to inject {@link MantisResourceBuilder}
	 * 
	 * @param pMantisResourceBuilder
	 *          the mantisResourceBuilder to set
	 */
	public void setMantisResourceBuilder(final MantisResourceBuilder pMantisResourceBuilder)
	{
		mantisResourceBuilder = pMantisResourceBuilder;
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
