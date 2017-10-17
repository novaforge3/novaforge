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
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;
import org.novaforge.forge.plugins.scm.svn.agent.dto.UserDTO;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNAgentException;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNClientFacadeService;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNFacadeService;


/**
 * @author benoists
 */
public class SVNUserServiceImpl extends AbstractPluginUserService implements PluginUserService
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
	public void createAdministratorUser(final ToolInstance pToolInstance, final PluginUser pUser)
			throws PluginServiceException
	{
		// FIXME Should be implemeted
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolUser(final InstanceConfiguration pInstanceConfiguration, final String pUserName,
																	 final PluginUser pPluginUser) throws PluginServiceException
	{
		boolean returnValue = false;
		try
		{
			String clientURL = pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance()
			    .getBaseURL());
			final SVNFacadeService svnFacadeService = svnClientFacadeService.getSVNFacadeService(clientURL);
			final UserDTO user = svnClientFacadeService.findUserByName(svnFacadeService, pPluginUser.getLogin());
			user.setPassword(pPluginUser.getPassword());
			returnValue = svnClientFacadeService.updateUser(svnFacadeService, user);
		}
		catch (final SVNAgentException e)
		{
			throw new PluginServiceException(String.format("Unable to update user's information with [user=%s]", pPluginUser),
																			 e);
		}
		return returnValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removeToolUser(final InstanceConfiguration pInstanceConfiguration, final PluginUser pPluginUser)
			throws PluginServiceException
	{
		boolean returnValue = false;
		try
		{
			String clientURL = pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance()
			    .getBaseURL());
			final SVNFacadeService svnFacadeService = svnClientFacadeService.getSVNFacadeService(clientURL);
			final UserDTO user = svnClientFacadeService.findUserByName(svnFacadeService, pPluginUser.getLogin());
			returnValue = svnClientFacadeService.deleteUser(svnFacadeService, user);
		}
		catch (final SVNAgentException e)
		{
			throw new PluginServiceException(String.format("Unable to delete User on svn with [user_login=%s]",
																										 pPluginUser.getLogin()), e);
		}
		return returnValue;
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
