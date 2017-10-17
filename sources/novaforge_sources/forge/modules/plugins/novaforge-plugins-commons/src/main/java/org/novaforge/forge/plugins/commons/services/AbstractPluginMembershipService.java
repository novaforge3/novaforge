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
package org.novaforge.forge.plugins.commons.services;

import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.core.plugins.services.PluginRoleMappingService;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * This abstract class define a generic way to implement {@link PluginMembershipService} Interface.
 * 
 * @see org.novaforge.forge.core.plugins.services.PluginMembershipService
 * @author Guillaume Lamirand
 */
public abstract class AbstractPluginMembershipService implements PluginMembershipService
{

	/**
	 * Reference to service implementation of {@link InstanceConfigurationDAO}
	 */
	protected InstanceConfigurationDAO instanceConfigurationDAO;
	/**
	 * Reference to service implementation of {@link PluginRoleMappingService}
	 */
	protected PluginRoleMappingService pluginRoleMappingService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addUserMemberships(final String pInstanceId, final List<PluginMembership> pUsersRole)
	    throws PluginServiceException
	{
		try
		{
			final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);

			for (final PluginMembership membership : pUsersRole)
			{
				final PluginUser user = membership.getPluginUser();
				final String forgeRole = membership.getRole();
				final String toolRole = pluginRoleMappingService.getToolRole(pInstanceId, forgeRole);
				addToolUserMemberships(instance, user, toolRole);
			}

		}
		catch (final NoResultException e)
		{
			throw new PluginServiceException(String.format("Cannot find project instance with instanceId=%s",
			    pInstanceId), e);
		}
	}

	protected abstract boolean addToolUserMemberships(final InstanceConfiguration pInstance,
	    final PluginUser pUser, final String pToolRole) throws PluginServiceException;

	/**
	 * {@inheritDoc}
	 * 
	 * @throws PluginServiceException
	 */
	@Override
	public void updateUserMemberships(final String pInstanceId, final List<PluginMembership> pMemberships)
	    throws PluginServiceException
	{
		final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);

		for (final PluginMembership pluginMembership : pMemberships)
		{
			final PluginUser user = pluginMembership.getPluginUser();
			final String forgeRole = pluginMembership.getRole();
			final String toolRole = pluginRoleMappingService.getToolRole(pInstanceId, forgeRole);
			if (pluginRoleMappingService.existToolRole(pInstanceId, forgeRole))
			{
				updateToolUserMemberships(instance, user, toolRole);
			}
			else
			{
				removeToolUserMemberships(instance, user, toolRole);
			}
		}
	}

	protected abstract boolean updateToolUserMemberships(final InstanceConfiguration pInstance,
	    final PluginUser pUser, final String pToolRole) throws PluginServiceException;

	protected abstract boolean removeToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
																											 final String pToolRole) throws PluginServiceException;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeUserMemberships(final String pInstanceId, final List<PluginMembership> pMemberships)
	    throws PluginServiceException
	{
		final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);
		for (final PluginMembership pluginMembership : pMemberships)
		{
			final PluginUser user = pluginMembership.getPluginUser();
			final String forgeRole = pluginMembership.getRole();
			if (pluginRoleMappingService.existToolRole(pInstanceId, forgeRole))
			{
				final String toolRole = pluginRoleMappingService.getToolRole(pInstanceId, forgeRole);
				removeToolUserMemberships(instance, user, toolRole);
			}
		}
	}

	/**
	 * Use by container to inject {@link InstanceConfigurationDAO}
	 * 
	 * @param pInstanceConfigurationDAO
	 *          the instanceConfigurationDAO to set
	 */
	public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
	{
		instanceConfigurationDAO = pInstanceConfigurationDAO;
	}

	/**
	 * Use by container to inject {@link PluginRoleMappingService}
	 * 
	 * @param pPluginRoleMappingService
	 *          the pluginRoleMappingService to set
	 */
	public void setPluginRoleMappingService(final PluginRoleMappingService pPluginRoleMappingService)
	{
		pluginRoleMappingService = pPluginRoleMappingService;
	}

}
