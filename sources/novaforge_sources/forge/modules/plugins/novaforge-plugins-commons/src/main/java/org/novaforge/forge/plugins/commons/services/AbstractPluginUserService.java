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
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginUserService;

/**
 * This abstract class define a generic way to implement PluginService Interface. The real plugin service
 * should declare an starting callback method to automaticaly set uuid as service property.
 * 
 * @see org.novaforge.forge.core.plugins.services.PluginService
 * @author lamirang
 */
public abstract class AbstractPluginUserService implements PluginUserService
{

	/**
	 * Reference to service implementation of {@link InstanceConfigurationDAO}
	 */
	protected InstanceConfigurationDAO instanceConfigurationDAO;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateUser(final String pInstanceId, final String pUserName, final PluginUser pUser)
	    throws PluginServiceException
	{
		final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);
		updateToolUser(instance, pUserName, pUser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeUser(final String pInstanceId, final PluginUser pUser) throws PluginServiceException
	{
		final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);
		removeToolUser(instance, pUser);
	}

	/**
	 * This method is used in order to delete user in tool behind a plugin
	 *
	 * @param pInstance
	 *          represents instance configuration
	 * @param pUser
	 *          represents user to delete
	 * @throws PluginServiceException
	 *           used if errors occured
	 */
	protected abstract boolean removeToolUser(final InstanceConfiguration pInstance, final PluginUser pUser)
			throws PluginServiceException;

	/**
	 * This method is used in order to update user in tool behind a plugin
	 *
	 * @param pInstance
	 *          represents instance configuration object
	 * @param pUser
	 *          represents user to update
	 * @return true if successfull
	 * @throws PluginServiceException
	 *           used if errors occured
	 */
	protected abstract boolean updateToolUser(final InstanceConfiguration pInstance, final String pUserName,
																						final PluginUser pUser) throws PluginServiceException;

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
}
