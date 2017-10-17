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
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.client.MantisARDSoapClient;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.client.MantisARDSoapConnector;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.client.MantisARDSoapException;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.datamapper.MantisARDResourceBuilder;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.AccountData;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.ProjectData;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author BILET-JC
 */
public class MantisARDProjectServiceImpl extends AbstractPluginProjectService implements PluginProjectService
{

	/*
	 * Service injection declaration
	 */
	private MantisARDSoapClient				mantisARDSoapClient;

	private MantisARDResourceBuilder	 mantisARDResourceBuilder;

	private PluginConfigurationService pluginConfigurationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String createToolProject(final InstanceConfiguration pInstanceConfiguration,
			final PluginProject pPluginProject, final List<PluginMembership> pPluginMembership)
			throws PluginServiceException
	{

		BigInteger projectId = null;
		try
		{
			// Obtain mantis connector
			MantisARDSoapConnector connector = mantisARDSoapClient.getConnector(
					pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
					pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

			// Add if necessary the user to mantis instance
			final Map<BigInteger, String> memberships = addToolUser(connector, pPluginMembership);

			String name = pPluginProject.getName() + " (" + pInstanceConfiguration.getConfigurationId() + ")";
			if (mantisARDSoapClient.mc_configure_management(connector, name))
			{
				// Create the membership between user and project created
				projectId = mantisARDSoapClient.mc_project_get_id_from_name(connector, name);
				addUsersToProject(connector, pInstanceConfiguration.getInstanceId(), projectId, memberships);
			}
		}
		catch (MantisARDSoapException e)
		{
			throw new PluginServiceException(String.format(
					"Unable to create project with InstanceConfiguration=%s and PluginProject=%s",
					pInstanceConfiguration.toString(), pPluginProject.toString()), e);
		}

		return projectId.toString();
	}

	private Map<BigInteger, String> addToolUser(final MantisARDSoapConnector pConnector,
			final List<PluginMembership> pUsers) throws PluginServiceException, MantisARDSoapException
	{
		final Map<BigInteger, String> users = new HashMap<BigInteger, String>();
		for (PluginMembership membership : pUsers)
		{
			final PluginUser user = membership.getPluginUser();
			final String role = membership.getRole();

			final AccountData account = mantisARDResourceBuilder.buildAccountData(user);

			BigInteger userId = mantisARDSoapClient.mc_account_get(pConnector, account.getName());
			if (userId == null)
			{
				userId = mantisARDSoapClient.mc_account_add(pConnector, account, user.getPassword());
			}
			users.put(userId, role);
		}

		return users;
	}

	private void addUsersToProject(final MantisARDSoapConnector pConnector, final String pInstanceId,
			final BigInteger pProjectId, final Map<BigInteger, String> pUsers) throws MantisARDSoapException,
			PluginServiceException
	{
		final Set<Entry<BigInteger, String>> usersRole = pUsers.entrySet();
		for (Entry<BigInteger, String> entry : usersRole)
		{
			final BigInteger userId = entry.getKey();
			final String forgeRole = entry.getValue();
			if (pluginRoleMappingService.existToolRole(pInstanceId, forgeRole))
			{
				final String toolRole = pluginRoleMappingService.getToolRole(pInstanceId, forgeRole);
				final String roleId = pluginRoleMappingService.getToolRoleId(toolRole);
				final BigInteger roleBig = new BigInteger(roleId);

				mantisARDSoapClient.mc_management_add_user(pConnector, pProjectId, userId, roleBig);
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
		final BigInteger toolProjectId = new BigInteger(pInstance.getToolProjectId());
		try
		{
			// Obtain mantis connector
			MantisARDSoapConnector connector = mantisARDSoapClient.getConnector(pluginConfigurationService
																																							.getClientURL(pInstance.getToolInstance()
																																																		 .getBaseURL()),
					pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			success = mantisARDSoapClient.mc_management_delete(connector, toolProjectId);

		}
		catch (MantisARDSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to delete tool project user with toolProjectId=%s",
																										 toolProjectId), e);
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
	protected boolean updateToolProject(final InstanceConfiguration pInstanceConfiguration, final PluginProject pProject)
			throws PluginServiceException
	{
		boolean success = false;
		try
		{

			final ProjectData projectData = mantisARDResourceBuilder.buildProjectData(pProject, pInstanceConfiguration
																																															.getConfigurationId());
			final BigInteger toolProjectId = new BigInteger(getToolProjectId(pInstanceConfiguration.getInstanceId()));

			// Obtain mantis connector
			MantisARDSoapConnector connector = mantisARDSoapClient.getConnector(pluginConfigurationService
																																							.getClientURL(pInstanceConfiguration
																																																.getToolInstance()
																																																.getBaseURL()),
					pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			success = mantisARDSoapClient.mc_management_update(connector, toolProjectId, projectData);

		}
		catch (MantisARDSoapException e)
		{
			throw new PluginServiceException(String.format("Unable to update tool project user with InstanceConfiguration=%s and PluginProject=%s",
																										 pInstanceConfiguration.toString(), pProject.toString()), e);
		}
		return success;
	}

	private String getToolProjectId(final String pInstanceId) throws PluginServiceException
	{
		return instanceConfigurationDAO.findByInstanceId(pInstanceId).getToolProjectId();
	}

	public void setMantisARDSoapClient(final MantisARDSoapClient pMantisARDSoapClient)
	{
		mantisARDSoapClient = pMantisARDSoapClient;
	}

	public void setMantisARDResourceBuilder(final MantisARDResourceBuilder pMantisARDResourceBuilder)
	{
		mantisARDResourceBuilder = pMantisARDResourceBuilder;
	}

	public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
	{
		pluginConfigurationService = pPluginConfigurationService;
	}

}
