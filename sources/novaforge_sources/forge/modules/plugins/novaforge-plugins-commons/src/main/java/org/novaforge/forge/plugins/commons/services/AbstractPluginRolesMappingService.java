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
import org.novaforge.forge.core.plugins.dao.RolesMappingDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginRoleMappingService;
import org.novaforge.forge.plugins.commons.persistence.entity.RolesMappingEntity;

import javax.persistence.NoResultException;
import java.util.Map;

/**
 * This abstract class define a generic way to implement {@link PluginRoleMappingService} Interface.
 * 
 * @see org.novaforge.forge.core.plugins.services.PluginRoleMappingService
 * @author lamirang
 */
public abstract class AbstractPluginRolesMappingService implements PluginRoleMappingService
{
	/**
	 * Reference to service implementation of {@link InstanceConfigurationDAO}
	 */
	protected InstanceConfigurationDAO instanceConfigurationDAO;
	/**
	 * Reference to service implementation of {@link RolesMappingDAO}
	 */
	protected RolesMappingDAO          rolesMappingDAO;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createRolesMapping(final String pInstanceId, final Map<String, String> pRoles)
	    throws PluginServiceException
	{
		final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);
		for (final Map.Entry<String, String> roleEntry : pRoles.entrySet())
		{
			final RolesMappingEntity rolesMappingEntity = new RolesMappingEntity();
			rolesMappingEntity.setInstance(instance);
			rolesMappingEntity.setForgeRole(roleEntry.getKey());
			rolesMappingEntity.setToolRole(roleEntry.getValue());
			rolesMappingDAO.persist(rolesMappingEntity);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateRolesMapping(final String pInstanceId, final Map<String, String> pRoles)
	    throws PluginServiceException
	{
		final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);
		rolesMappingDAO.deleteByInstance(pInstanceId);
		for (final Map.Entry<String, String> roleEntry : pRoles.entrySet())
		{
			final RolesMappingEntity rolesMappingEntity = new RolesMappingEntity();
			rolesMappingEntity.setInstance(instance);
			rolesMappingEntity.setForgeRole(roleEntry.getKey());
			rolesMappingEntity.setToolRole(roleEntry.getValue());
			rolesMappingDAO.persist(rolesMappingEntity);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeRolesMapping(final String pInstanceId, final Map<String, String> pRoles)
	    throws PluginServiceException
	{
		final Map<String, String> existingMapping = rolesMappingDAO.findByInstance(pInstanceId);
		for (final String forgeRole : existingMapping.keySet())
		{
			rolesMappingDAO.removeByInstanceAndForgeRole(pInstanceId, forgeRole);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> getRolesMapping(final String pInstanceId) throws PluginServiceException
	{
		return rolesMappingDAO.findByInstance(pInstanceId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getToolRole(final String pInstanceId, final String pForgeRole) throws PluginServiceException
	{
		String toolRole = null;
		try
		{
			toolRole = rolesMappingDAO.findByInstanceAndForgeRole(pInstanceId, pForgeRole);
		}
		catch (final NoResultException e)
		{
			// if the tool role doesn't exists, we do not throw the exception and return null
		}
		return toolRole;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteByInstance(final String pInstanceId)
	{
		rolesMappingDAO.deleteByInstance(pInstanceId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existToolRole(final String pInstanceId, final String pForgeRole)
	{
		boolean returnValue;
		try
		{
			rolesMappingDAO.findByInstanceAndForgeRole(pInstanceId, pForgeRole);
			returnValue = true;
		}
		catch (final NoResultException e)
		{
			returnValue = false;
		}

		return returnValue;
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
	 * Use by container to inject {@link RolesMappingDAO}
	 * 
	 * @param pRolesMappingDAO
	 *          the pluginRoleMappingService to set
	 */
	public void setRolesMappingDAO(final RolesMappingDAO pRolesMappingDAO)
	{
		rolesMappingDAO = pRolesMappingDAO;
	}
}
