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
package org.novaforge.forge.plugins.continuousintegration.jenkins.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;
import org.novaforge.forge.plugins.continuousintegration.jenkins.client.JenkinsXMLClient;
import org.novaforge.forge.plugins.continuousintegration.jenkins.client.JenkinsXMLException;
import org.novaforge.forge.plugins.continuousintegration.jenkins.model.JenkinsClientConnector;

import java.util.List;

/**
 * @author sbenoist
 */
public class JenkinsMembershipServiceImpl extends AbstractPluginMembershipService implements
    PluginMembershipService
{

	/**
	 * Reference to service implementation of {@link PluginConfigurationService}
	 */
	private JenkinsXMLClient           jenkinsXMLClient;
	/**
	 * Reference to service implementation of {@link PluginConfigurationService}
	 */
	private PluginConfigurationService pluginConfigurationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean addToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
	    final String pToolRole) throws PluginServiceException
	{
		try
		{
			final String token = pInstance.getToolProjectId().split("_default")[0];
			final JenkinsClientConnector connector = jenkinsXMLClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

			final List<String> jobs = jenkinsXMLClient.getAllProjectJobs(connector, token);
			final String roleId = pluginRoleMappingService.getToolRoleId(pToolRole);
			for (final String job : jobs)
			{
				jenkinsXMLClient.addJobPermissions(connector, job, pUser.getLogin(), roleId);
			}
		}
		catch (final JenkinsXMLException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to add user memberships on Jenkins for project=%s and application=%s on jenkins",
			    pInstance.getForgeProjectId(), pInstance.getConfigurationId()), e);
		}

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
	    final String pToolRole) throws PluginServiceException
	{
		try
		{
			final String token = pInstance.getToolProjectId().split("_default")[0];
			final JenkinsClientConnector connector = jenkinsXMLClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			final List<String> jobs = jenkinsXMLClient.getAllProjectJobs(connector, token);
			final String roleId = pluginRoleMappingService.getToolRoleId(pToolRole);
			for (final String job : jobs)
			{
				jenkinsXMLClient.updateJobPermissions(connector, job, pUser.getLogin(), roleId);
			}
		}
		catch (final JenkinsXMLException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to update user memberships on Jenkins for project=%s and application=%s on jenkins",
			    pInstance.getForgeProjectId(), pInstance.getConfigurationId()), e);
		}

		return true;
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
			final String token = pInstance.getToolProjectId().split("_default")[0];
			final JenkinsClientConnector connector = jenkinsXMLClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			final List<String> jobs = jenkinsXMLClient.getAllProjectJobs(connector, token);
			for (final String job : jobs)
			{
				jenkinsXMLClient.removeJobPermissions(connector, job, pUser.getLogin());
			}
		}
		catch (final JenkinsXMLException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to remove user memberships on Jenkins for project=%s and application=%s on jenkins",
			    pInstance.getForgeProjectId(), pInstance.getConfigurationId()), e);
		}

		return true;
	}

	/**
	 * Use by container to inject {@link JenkinsXMLClient}
	 * 
	 * @param pJenkinsXMLClient
	 *          the jenkinsXMLClient to set
	 */
	public void setJenkinsXMLClient(final JenkinsXMLClient pJenkinsXMLClient)
	{
		jenkinsXMLClient = pJenkinsXMLClient;
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
