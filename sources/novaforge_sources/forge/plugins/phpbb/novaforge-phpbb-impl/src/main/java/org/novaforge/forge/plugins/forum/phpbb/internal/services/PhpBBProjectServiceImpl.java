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
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;
import org.novaforge.forge.plugins.forum.phpbb.client.PhpBBSoapClient;
import org.novaforge.forge.plugins.forum.phpbb.client.PhpBBSoapConnector;
import org.novaforge.forge.plugins.forum.phpbb.client.PhpBBSoapException;
import org.novaforge.forge.plugins.forum.phpbb.datamapper.PhpBBResourceBuilder;
import org.novaforge.forge.plugins.forum.phpbb.model.PhpbbForum;
import org.novaforge.forge.plugins.forum.phpbb.model.PhpbbUser;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public class PhpBBProjectServiceImpl extends AbstractPluginProjectService implements PluginProjectService
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
	 * {@inheritDoc}
	 */
	@Override
	protected String createToolProject(final InstanceConfiguration pInstance,
	    final PluginProject pPluginProject, final List<PluginMembership> pPluginMembership)
	    throws PluginServiceException
	{

		BigInteger projectToolId;
		try
		{

			// Obtain phpbb connector
			final PhpBBSoapConnector connector = phpbbSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

			addToolUser(connector, pPluginMembership);
			final PhpbbForum projectData = phpbbResourceBuilder.buildProjectData(pPluginProject,
			    pInstance.getConfigurationId());

			projectToolId = phpbbSoapClient.pc_project_add(connector, projectData);

			addUsersToProject(connector, pInstance, pPluginMembership, projectToolId);

			phpbbSoapClient.pc_purge_cache(connector);
		}
		catch (final PhpBBSoapException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to create project with InstanceConfiguration=%s and PluginProject=%s",
			    pInstance.toString(), pPluginProject.toString()), e);
		}

		return projectToolId.toString();
	}

	private Map<BigInteger, String> addToolUser(final PhpBBSoapConnector pConnector,
	    final List<PluginMembership> pUsers) throws PluginServiceException, PhpBBSoapException
	{
		final Map<BigInteger, String> users = new HashMap<BigInteger, String>();
		for (final PluginMembership membership : pUsers)
		{
			final PluginUser user = membership.getPluginUser();
			final String role = membership.getRole();

			final PhpbbUser account = phpbbResourceBuilder.buildAccountData(user);

			BigInteger userId = phpbbSoapClient.pc_account_get(pConnector, account);
			if ((userId == null) || (userId.longValue() < 0))
			{
				userId = phpbbSoapClient.pc_account_add(pConnector, account);
			}
			users.put(userId, role);
		}

		return users;
	}

	private void addUsersToProject(final PhpBBSoapConnector pConnector,
	    final InstanceConfiguration pInstanceConfiguration, final List<PluginMembership> pUsers,
	    final BigInteger projectToolId) throws PhpBBSoapException, PluginServiceException
	{

		for (final PluginMembership membership : pUsers)
		{
			final PluginUser user = membership.getPluginUser();
			final PhpbbUser account = phpbbResourceBuilder.buildAccountData(user);
			final String toolRole = pluginRoleMappingService.getToolRole(pInstanceConfiguration.getInstanceId(),
			    membership.getRole());

			phpbbSoapClient.pc_project_add_user(pConnector, projectToolId, account, toolRole);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean deleteToolProject(final InstanceConfiguration pInstanceConfiguration) throws PluginServiceException
	{
		boolean success = false;
		try
		{
			final BigInteger toolProjectId = new BigInteger(pInstanceConfiguration.getToolProjectId());

			// Obtain phpbb connector

			final PhpBBSoapConnector connector = phpbbSoapClient.getConnector(pluginConfigurationService
																																						.getClientURL(pInstanceConfiguration
																																															.getToolInstance()
																																															.getBaseURL()),
																																				pluginConfigurationService.getClientAdmin(),
																																				pluginConfigurationService.getClientPwd());
			success = phpbbSoapClient.pc_project_delete(connector, toolProjectId);

		}
		catch (final PhpBBSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to delete tool project user with toolProjectId=%s",
																										 pInstanceConfiguration.getToolProjectId()), e);
		}
		return success;
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
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolProject(final InstanceConfiguration pInstanceConfiguration,
	    final PluginProject pProject) throws PluginServiceException
	{
		boolean success = false;
		try
		{
			// Obtain phpbb connector
			final PhpBBSoapConnector connector = phpbbSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

			final PhpbbForum forumData = phpbbResourceBuilder.buildProjectData(pProject,
			    pInstanceConfiguration.getConfigurationId());
			final BigInteger toolProjectId = new BigInteger(
			    getToolProjectId(pInstanceConfiguration.getInstanceId()));
			success = phpbbSoapClient.pc_project_update(connector, toolProjectId, forumData);

		}
		catch (final PhpBBSoapException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to update tool project user with InstanceConfiguration=%s and PluginProject=%s",
			    pInstanceConfiguration.toString(), pProject.toString()), e);
		}
		return success;
	}

	private String getToolProjectId(final String pInstanceId) throws PluginServiceException
	{
		return instanceConfigurationDAO.findByInstanceId(pInstanceId).getToolProjectId();
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
