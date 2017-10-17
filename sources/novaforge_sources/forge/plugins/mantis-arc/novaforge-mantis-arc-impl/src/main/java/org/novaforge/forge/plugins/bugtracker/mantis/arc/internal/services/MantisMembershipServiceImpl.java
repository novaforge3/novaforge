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
package org.novaforge.forge.plugins.bugtracker.mantis.arc.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapClient;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapConnector;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapException;
import org.novaforge.forge.plugins.bucktracker.mantis.datamapper.MantisResourceBuilder;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;

import java.math.BigInteger;

/**
 * Mantis implementation of {@link PluginMembershipService}
 * 
 * @author Guillaume Lamirand Aimen Merkich
 */
public class MantisMembershipServiceImpl extends AbstractPluginMembershipService implements
    PluginMembershipService
{

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
	 * {@inheritDoc}
	 */
	@Override
	protected boolean addToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
	    final String pToolRole) throws PluginServiceException
	{

		try
		{
			final BigInteger roleId = new BigInteger(pluginRoleMappingService.getToolRoleId(pToolRole));

			final MantisSoapConnector connector = mantisSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			final AccountData account = mantisResourceBuilder.buildAccountData(pUser);
			BigInteger userId = mantisSoapClient.mc_account_get(connector, account.getName());
			if (userId == null)
			{
				userId = mantisSoapClient.mc_account_add(connector, account, pUser.getPassword());
			}
			final BigInteger projectId = new BigInteger(pInstance.getToolProjectId());
			mantisSoapClient.mc_project_add_user(connector, projectId, userId, roleId);
		}
		catch (final MantisSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to delete user with instance=%s and User=%s",
			    pInstance.toString(), pUser.toString()), e);
		}
		return true;

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
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
	    final String pToolRole) throws PluginServiceException
	{
		return addToolUserMemberships(pInstance, pUser, pToolRole);

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removeToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
	    final String pToolRole) throws PluginServiceException
	{
		try
		{

			final MantisSoapConnector connector = mantisSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			final AccountData account = mantisResourceBuilder.buildAccountData(pUser);
			final BigInteger userId = mantisSoapClient.mc_account_get(connector, account.getName());
			if (userId != null)
			{
				final BigInteger projectId = new BigInteger(pInstance.getToolProjectId());
				mantisSoapClient.mc_project_remove_user(connector, projectId, userId);
			}
		}
		catch (final MantisSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to delete user with instance=%s and User=%s",
			    pInstance.toString(), pUser.toString()), e);
		}
		return true;
	}

}
