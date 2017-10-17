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
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.cms.spip.client.SpipSoapClient;
import org.novaforge.forge.plugins.cms.spip.client.SpipSoapConnector;
import org.novaforge.forge.plugins.cms.spip.client.SpipSoapException;
import org.novaforge.forge.plugins.cms.spip.datamapper.SpipResourceBuilder;
import org.novaforge.forge.plugins.cms.spip.services.SpipConfigurationService;
import org.novaforge.forge.plugins.cms.spip.soap.UserData;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;

import java.math.BigInteger;

/**
 * @author Guillaume Lamirand, blachonm
 */
public class SpipMembershipServiceImpl extends AbstractPluginMembershipService implements
    PluginMembershipService
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
	 * {@inheritDoc}
	 */
	@Override
	protected boolean addToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
	    final String pToolRole) throws PluginServiceException
	{

		try
		{
			final String roleId = pluginRoleMappingService.getToolRoleId(pToolRole);
			final SpipSoapConnector connector = spipSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL(),
			        pInstance.getToolProjectId()), pluginConfigurationService.getClientAdmin(),
			    pluginConfigurationService.getClientPwd());
			final UserData userData = spipResourceBuilder.buildUserData(pUser);
			BigInteger userId = spipSoapClient.get_user_id(connector, userData.getLogin());
			if (userId == null)
			{
				userId = spipSoapClient.add_user(connector, userData);
			}
			spipSoapClient.add_user_site(connector, pInstance.getToolProjectId(), userData.getLogin(), roleId);
		}
		catch (final SpipSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to delete user with instance=%s and User=%s",
			    pInstance.toString(), pUser.toString()), e);
		}
		return true;

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
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removeToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
	    final String pToolRole) throws PluginServiceException
	{
		try
		{

			final SpipSoapConnector connector = spipSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL(),
			        pInstance.getToolProjectId()), pluginConfigurationService.getClientAdmin(),
			    pluginConfigurationService.getClientPwd());
			final UserData userData = spipResourceBuilder.buildUserData(pUser);
			final BigInteger userId = spipSoapClient.get_user_id(connector, userData.getLogin());
			if (userId != null)
			{
				spipSoapClient.delete_user(connector, userData.getLogin());
			}
		}
		catch (final SpipSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to delete user with instance=%s and User=%s",
			    pInstance.toString(), pUser.toString()), e);
		}
		return true;
	}

}
