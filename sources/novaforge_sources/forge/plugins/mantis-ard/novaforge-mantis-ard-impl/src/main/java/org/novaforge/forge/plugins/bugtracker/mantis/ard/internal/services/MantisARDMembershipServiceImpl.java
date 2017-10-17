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
package org.novaforge.forge.plugins.bugtracker.mantis.ard.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.client.MantisARDSoapClient;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.client.MantisARDSoapConnector;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.client.MantisARDSoapException;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.datamapper.MantisARDResourceBuilder;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.AccountData;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;

import java.math.BigInteger;

/**
 * @author BILET-JC
 */
public class MantisARDMembershipServiceImpl extends AbstractPluginMembershipService implements
		PluginMembershipService
{

	/*
	 * Service injection declaration
	 */
	private MantisARDSoapClient				mantisARDSoapClient;

	private MantisARDResourceBuilder	 mantisARDResourceBuilder;

	private PluginConfigurationService pluginConfigurationService;

	public void setMantisARDSoapClient(final MantisARDSoapClient pMantisARDSoapClient)
	{
		mantisARDSoapClient = pMantisARDSoapClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean addToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
			final String pToolRole) throws PluginServiceException
	{

		try
		{
			final BigInteger roleId = new BigInteger(pluginRoleMappingService.getToolRoleId(pToolRole));
			MantisARDSoapConnector connector = mantisARDSoapClient.getConnector(
					pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
					pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			final AccountData account = mantisARDResourceBuilder.buildAccountData(pUser);
			BigInteger userId = mantisARDSoapClient.mc_account_get(connector, account.getName());
			if (userId == null)
			{
				userId = mantisARDSoapClient.mc_account_add(connector, account, pUser.getPassword());
			}
			final BigInteger projectId = new BigInteger(pInstance.getToolProjectId());
			mantisARDSoapClient.mc_management_add_user(connector, projectId, userId, roleId);
		}
		catch (MantisARDSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to delete user with instance=%s and User=%s",
					pInstance.toString(), pUser.toString()), e);
		}
		return true;

	}

	public void setMantisARDResourceBuilder(final MantisARDResourceBuilder pMantisARDResourceBuilder)
	{
		mantisARDResourceBuilder = pMantisARDResourceBuilder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
			final String pToolRole) throws PluginServiceException
	{
		return addToolUserMemberships(pInstance, pUser, pToolRole);

	}

	public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
	{
		pluginConfigurationService = pPluginConfigurationService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removeToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
			final String pToolRole) throws PluginServiceException
	{
		try
		{
			MantisARDSoapConnector connector = mantisARDSoapClient.getConnector(
					pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
					pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			final AccountData account = mantisARDResourceBuilder.buildAccountData(pUser);
			BigInteger userId = mantisARDSoapClient.mc_account_get(connector, account.getName());
			if (userId != null)
			{
				final BigInteger projectId = new BigInteger(pInstance.getToolProjectId());
				mantisARDSoapClient.mc_management_remove_user(connector, projectId, userId);
			}
		}
		catch (MantisARDSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to delete user with instance=%s and User=%s",
					pInstance.toString(), pUser.toString()), e);
		}
		return true;
	}

}
