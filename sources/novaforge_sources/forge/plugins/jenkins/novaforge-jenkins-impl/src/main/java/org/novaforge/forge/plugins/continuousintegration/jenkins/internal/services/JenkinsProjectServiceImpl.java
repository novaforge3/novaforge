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
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;
import org.novaforge.forge.plugins.continuousintegration.jenkins.client.JenkinsXMLClient;
import org.novaforge.forge.plugins.continuousintegration.jenkins.client.JenkinsXMLException;
import org.novaforge.forge.plugins.continuousintegration.jenkins.internal.utils.Utils;
import org.novaforge.forge.plugins.continuousintegration.jenkins.model.JenkinsClientConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 */
public class JenkinsProjectServiceImpl extends AbstractPluginProjectService implements PluginProjectService
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
	protected String createToolProject(final InstanceConfiguration pInstanceConfiguration,
	    final PluginProject pPluginProject, final List<PluginMembership> pPluginMembership)
	    throws PluginServiceException
	{
		String jobName = null;
		try
		{
			// build memberships
			final Map<String, String> memberships = buildMemberships(pInstanceConfiguration.getInstanceId(),
			    pPluginMembership);

			// Create default job
			jobName = Utils.buildDefaultJobName(pInstanceConfiguration);

			final JenkinsClientConnector connector = jenkinsXMLClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			jenkinsXMLClient.createJob(connector, jobName, pPluginProject.getDescription(), memberships);
		}
		catch (final JenkinsXMLException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to create default job for project=%s and application=%s on jenkins",
			    pInstanceConfiguration.getForgeProjectId(), pInstanceConfiguration.getConfigurationId()), e);
		}

		return jobName;
	}

	private Map<String, String> buildMemberships(final String pInstanceId,
	    final List<PluginMembership> pPluginMembership) throws PluginServiceException
	{
		final Map<String, String> memberships = new HashMap<String, String>();

		String forgeRole = null;
		String roleId = null;
		String toolRole = null;

		for (final PluginMembership pluginMembership : pPluginMembership)
		{
			forgeRole = pluginMembership.getRole();
			if (pluginRoleMappingService.existToolRole(pInstanceId, forgeRole))
			{
				toolRole = pluginRoleMappingService.getToolRole(pInstanceId, forgeRole);
				roleId = pluginRoleMappingService.getToolRoleId(toolRole);
				memberships.put(pluginMembership.getPluginUser().getLogin(), roleId);
			}
		}

		return memberships;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean deleteToolProject(final InstanceConfiguration pInstanceConfiguration)
	    throws PluginServiceException
	{
		final String token = pInstanceConfiguration.getToolProjectId().split("_default")[0];

		try
		{
			final JenkinsClientConnector connector = jenkinsXMLClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			final List<String> jobs = jenkinsXMLClient.getAllProjectJobs(connector, token);
			for (final String job : jobs)
			{
				jenkinsXMLClient.deleteJob(connector, job);
			}
		}
		catch (final JenkinsXMLException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to delete all the jobs on jenkins for project=%s",
			    pInstanceConfiguration.getToolProjectId()), e);
		}
		return true;
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
		// not implemented
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
