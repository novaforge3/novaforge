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
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;
import org.novaforge.forge.plugins.testmanager.testlink.client.TestlinkXmlRpcClient;
import org.novaforge.forge.plugins.testmanager.testlink.client.TestlinkXmlRpcException;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestLinkParameter;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestlinkResourceBuilder;

import java.util.HashMap;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 25 juil. 2011
 */
public class TestlinkUserServiceImpl extends AbstractPluginUserService implements PluginUserService
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
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolUser(final InstanceConfiguration pInstance, final String pUserName,
	    final PluginUser pPluginUser) throws PluginServiceException
	{
		try
		{

			final HashMap<String, Object> accountData = testlinkResourceBuilder.buildAccountData(pPluginUser);

			final XmlRpcClient connector = testlinkXmlRpcClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

			return testlinkXmlRpcClient.updateUser(connector, accountData).isSucces();
		}
		catch (final TestlinkXmlRpcException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to update user with instance=%s, username=%s and User=%s", pInstance, pUserName,
			    pPluginUser), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removeToolUser(final InstanceConfiguration pInstance, final PluginUser pPluginUser)
	    throws PluginServiceException
	{
		try
		{
			final XmlRpcClient connector = testlinkXmlRpcClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			final HashMap<String, Object> accountData = new HashMap<String, Object>();
			accountData.put(TestLinkParameter.USER_NAME.toString(), pPluginUser.getLogin());
			return testlinkXmlRpcClient.deleteUser(connector, accountData).isSucces();
		}
		catch (final TestlinkXmlRpcException e)
		{
			throw new PluginServiceException(String.format("Unable to delete user with instance=%s and User=%s",
			    pInstance, pPluginUser), e);
		}
	}

	@Override
	public void createAdministratorUser(final ToolInstance pInstance, final PluginUser pUser)
	    throws PluginServiceException
	{
		try
		{

			final XmlRpcClient connector = testlinkXmlRpcClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			final HashMap<String, Object> account = testlinkResourceBuilder.buildAccountData(pUser);
			testlinkXmlRpcClient.createAdminUser(connector, account);

		}
		catch (final TestlinkXmlRpcException e)
		{
			throw new PluginServiceException(String.format("Unable to create administrator user with user=%s",
			    pUser.toString()), e);
		}
	}

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
