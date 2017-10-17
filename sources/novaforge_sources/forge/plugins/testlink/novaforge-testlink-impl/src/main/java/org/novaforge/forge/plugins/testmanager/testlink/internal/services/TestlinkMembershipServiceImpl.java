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
package org.novaforge.forge.plugins.testmanager.testlink.internal.services;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;
import org.novaforge.forge.plugins.testmanager.testlink.client.TestlinkXmlRpcClient;
import org.novaforge.forge.plugins.testmanager.testlink.client.TestlinkXmlRpcException;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestLinkRPCStatus;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestlinkResourceBuilder;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 25 juil. 2011
 */
public class TestlinkMembershipServiceImpl extends AbstractPluginMembershipService implements
    PluginMembershipService
{
	/**
	 * Reference to service implementation of {@link TestlinkXmlRpcClient}
	 */
	private TestlinkXmlRpcClient       testlinkXmlRpcClient;
	/**
	 * Reference to service implementation of {@link TestlinkResourceBuilder}
	 */
	private TestlinkResourceBuilder    testlinkResourceBuilder;
	/**
	 * Reference to service implementation of {@link PluginConfigurationService}
	 */
	private PluginConfigurationService pluginConfigurationService;

	/**
	 * Use by container to inject {@link TestlinkXmlRpcClient}
	 *
	 * @param pTestlinkXmlRpcClient
	 *          the testlinkXmlRpcClient to set
	 */
	public void setTestlinkXmlRpcClient(final TestlinkXmlRpcClient pTestlinkXmlRpcClient)
	{
		testlinkXmlRpcClient = pTestlinkXmlRpcClient;
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
			final XmlRpcClient connector = testlinkXmlRpcClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			// create user if need be // nothing else
			testlinkXmlRpcClient.createUser(connector, testlinkResourceBuilder.buildAccountData(pUser));
			// Get role id
			final String toolRoleId = pluginRoleMappingService.getToolRoleId(pToolRole);
			// if create user is ko addUserToProject will failed
			final TestLinkRPCStatus<?> result = testlinkXmlRpcClient.addUserToProject(connector,
			    pInstance.getToolProjectId(), pUser.getLogin(), toolRoleId);
			return result.isSucces();
		}
		catch (final TestlinkXmlRpcException e)
		{
			throw new PluginServiceException(String.format("unable to add user %s to project %s", pUser.getLogin(),
			    pInstance.getToolProjectId()), e);
		}
	}

	/**
	 * Use by container to inject {@link TestlinkResourceBuilder}
	 *
	 * @param pTestlinkResourceBuilder
	 *          the testlinkResourceBuilder to set
	 */
	public void setTestlinkResourceBuilder(final TestlinkResourceBuilder pTestlinkResourceBuilder)
	{
		testlinkResourceBuilder = pTestlinkResourceBuilder;
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
			final XmlRpcClient connector = testlinkXmlRpcClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			final TestLinkRPCStatus<?> result = testlinkXmlRpcClient.removeUserFromProject(connector,
			    pInstance.getToolProjectId(), pUser.getLogin());
			return result.isSucces();
		}
		catch (final TestlinkXmlRpcException e)
		{
			throw new PluginServiceException(String.format("unable to remove user %s from project %s",
			    pUser.getLogin(), pInstance.getToolProjectId()), e);
		}
	}

}
