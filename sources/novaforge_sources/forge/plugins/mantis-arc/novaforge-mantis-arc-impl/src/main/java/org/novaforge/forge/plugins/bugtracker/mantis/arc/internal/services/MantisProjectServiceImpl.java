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
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapClient;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapConnector;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapException;
import org.novaforge.forge.plugins.bucktracker.mantis.datamapper.MantisResourceBuilder;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectData;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Mantis implementation of {@link PluginProjectService}
 * 
 * @author Guillaume Lamirand Aimen Merkich
 */
public class MantisProjectServiceImpl extends AbstractPluginProjectService implements PluginProjectService
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
			// Obtain mantis connector

			final MantisSoapConnector connector = mantisSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

			// Add if necessary the user to mantis instance
			final Map<BigInteger, String> memberships = addToolUser(connector, pPluginMembership);

			// Create the project
			final ProjectData projectData = mantisResourceBuilder.buildProjectData(pPluginProject,
			    pInstance.getConfigurationId());
			projectToolId = mantisSoapClient.mc_project_add(connector, projectData);

			// Create the membership between user and project created
			addUsersToProject(connector, pInstance.getInstanceId(), projectToolId, memberships);

		}
		catch (final MantisSoapException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to create project with InstanceConfiguration=%s and PluginProject=%s",
			    pInstance.toString(), pPluginProject.toString()), e);
		}

		return projectToolId.toString();
	}

	private Map<BigInteger, String> addToolUser(final MantisSoapConnector pConnector,
	    final List<PluginMembership> pUsers) throws PluginServiceException, MantisSoapException
	{
		final Map<BigInteger, String> users = new HashMap<BigInteger, String>();
		for (final PluginMembership membership : pUsers)
		{
			final PluginUser user = membership.getPluginUser();
			final String role = membership.getRole();

			final AccountData account = mantisResourceBuilder.buildAccountData(user);

			BigInteger userId = mantisSoapClient.mc_account_get(pConnector, account.getName());
			if (userId == null)
			{
				userId = mantisSoapClient.mc_account_add(pConnector, account, user.getPassword());
			}
			users.put(userId, role);
		}

		return users;
	}

	private void addUsersToProject(final MantisSoapConnector pConnector, final String pInstanceId,
	    final BigInteger pProjectId, final Map<BigInteger, String> pUsers) throws MantisSoapException,
	    PluginServiceException
	{
		final Set<Entry<BigInteger, String>> usersRole = pUsers.entrySet();
		for (final Entry<BigInteger, String> entry : usersRole)
		{
			final BigInteger userId = entry.getKey();
			final String forgeRole = entry.getValue();

			if (pluginRoleMappingService.existToolRole(pInstanceId, forgeRole))
			{
				final String toolRole = pluginRoleMappingService.getToolRole(pInstanceId, forgeRole);
				final String roleId = pluginRoleMappingService.getToolRoleId(toolRole);
				final BigInteger roleBig = new BigInteger(roleId);

				mantisSoapClient.mc_project_add_user(pConnector, pProjectId, userId, roleBig);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean deleteToolProject(final InstanceConfiguration pInstance) throws PluginServiceException
	{
		boolean success = false;
		try
		{
			final BigInteger toolProjectId = new BigInteger(pInstance.getToolProjectId());

			// Obtain mantis connector

			final MantisSoapConnector connector = mantisSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			success = mantisSoapClient.mc_project_delete(connector, toolProjectId);

		}
		catch (final MantisSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to delete tool project user with toolProjectId=%s",
																										 pInstance.getToolProjectId()), e);
		}
		return success;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void archiveProject(final String pInstanceId) throws PluginServiceException
	{
		// TODO Not implemented yet
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolProject(final InstanceConfiguration pInstance, final PluginProject pProject)
			throws PluginServiceException
	{
		boolean success = false;
		try
		{

			final ProjectData projectData = mantisResourceBuilder.buildProjectData(pProject, pInstance.getConfigurationId());
			final BigInteger toolProjectId = new BigInteger(getToolProjectId(pInstance.getInstanceId()));

			// Obtain mantis connector

			final MantisSoapConnector connector = mantisSoapClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			success = mantisSoapClient.mc_project_update(connector, toolProjectId, projectData);

		}
		catch (final MantisSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to update tool project user with InstanceConfiguration=%s and PluginProject=%s",
																										 pInstance.toString(), pProject.toString()), e);
		}
		return success;
	}

	private String getToolProjectId(final String pInstanceId) throws PluginServiceException
	{
		return instanceConfigurationDAO.findByInstanceId(pInstanceId).getToolProjectId();
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
