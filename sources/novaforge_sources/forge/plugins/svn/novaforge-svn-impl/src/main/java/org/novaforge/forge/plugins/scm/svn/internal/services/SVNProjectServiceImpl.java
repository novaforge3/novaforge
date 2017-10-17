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
package org.novaforge.forge.plugins.scm.svn.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;
import org.novaforge.forge.plugins.scm.svn.agent.dto.MembershipDTO;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNAgentException;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNClientFacadeService;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNFacadeService;
import org.novaforge.forge.plugins.scm.svn.domain.Role;
import org.novaforge.forge.plugins.scm.svn.internal.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author benoists
 */
public class SVNProjectServiceImpl extends AbstractPluginProjectService implements PluginProjectService
{

	/**
	 * Reference to service implementation of {@link SVNClientFacadeService}
	 */
	private SVNClientFacadeService     svnClientFacadeService;
	/**
	 * Reference to service implementation of {@link PluginConfigurationService}
	 */
	private PluginConfigurationService pluginConfigurationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String createToolProject(final InstanceConfiguration pInstanceConfiguration,
	    final PluginProject pPluginProject, final List<PluginMembership> pMemberships)
	    throws PluginServiceException
	{
		final String repositoryName = Utils.getRepositoryName(pInstanceConfiguration);

		// build the memberships map
		final List<MembershipDTO> memberships = new ArrayList<MembershipDTO>();
		for (final PluginMembership membership : pMemberships)
		{
			final String toolRole = pluginRoleMappingService.getToolRole(pInstanceConfiguration.getInstanceId(),
			    membership.getRole());
			final Role role = Role.fromLabel(toolRole);
			if (role == null)
			{
				throw new PluginServiceException("unable to get the svn role from the label:" + membership.getRole());
			}

			final MembershipDTO member = new MembershipDTO(membership.getPluginUser().getLogin(), membership
			    .getPluginUser().getPassword(), role);
			memberships.add(member);
		}

		try
		{
			String clientURL = pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance()
			    .getBaseURL());
			final SVNFacadeService svnFacadeService = svnClientFacadeService.getSVNFacadeService(clientURL);
			svnClientFacadeService.createProject(svnFacadeService, memberships, repositoryName);
		}
		catch (final SVNAgentException e)
		{
			throw new PluginServiceException(String.format(
			    "Unable to create svn project with [memberships=%s, repository_name=%s]", memberships,
			    repositoryName), e);
		}

		return repositoryName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean deleteToolProject(final InstanceConfiguration pInstanceConfiguration)
	    throws PluginServiceException
	{
		try
		{
			String clientURL = pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance()
			    .getBaseURL());
			final SVNFacadeService svnFacadeService = svnClientFacadeService.getSVNFacadeService(clientURL);
			return svnClientFacadeService
			    .deleteProject(svnFacadeService, pInstanceConfiguration.getToolProjectId());
		}
		catch (final SVNAgentException e)
		{
			throw new PluginServiceException(String.format("Unable to delete svn project with [repository_id=%s]",
			    pInstanceConfiguration.getToolProjectId()), e);
		}
	}

	@Override
	public void archiveProject(final String pInstanceId) throws PluginServiceException
	{
		// NOT IMPLEMENTED
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolProject(final InstanceConfiguration pInstanceConfigurationEntity,
																			final PluginProject pProject) throws PluginServiceException
	{
		// NOT IMPLEMENTED
		return false;
	}

	/**
	 * Use by container to inject {@link SVNClientFacadeService}
	 * 
	 * @param pSvnClientFacadeService
	 *          the svnClientFacadeService to set
	 */
	public void setSvnClientFacadeService(final SVNClientFacadeService pSvnClientFacadeService)
	{
		svnClientFacadeService = pSvnClientFacadeService;
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
