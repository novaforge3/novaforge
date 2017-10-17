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

import org.novaforge.forge.commons.technical.normalize.NormalizeService;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.dao.ToolInstanceDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginInstance;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProjectInformation;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.core.plugins.services.PluginRoleMappingService;
import org.novaforge.forge.plugins.commons.persistence.entity.InstanceConfigurationEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This abstract class define a generic way to implement {@link PluginProjectService} Interface.
 * 
 * @see org.novaforge.forge.core.plugins.services.PluginProjectService
 * @author Guillaume Lamirand
 */
public abstract class AbstractPluginProjectService implements PluginProjectService
{

	/**
	 * Reference to service implementation of {@link InstanceConfigurationDAO}
	 */
	protected InstanceConfigurationDAO instanceConfigurationDAO;
	/**
	 * Reference to service implementation of {@link ToolInstanceDAO}
	 */
	protected ToolInstanceDAO          toolInstanceDAO;
	/**
	 * Reference to service implementation of {@link PluginRoleMappingService}
	 */
	protected PluginRoleMappingService pluginRoleMappingService;
	/**
	 * Reference to service implementation of {@link NormalizeService}
	 */
	protected NormalizeService         normalizeService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createProject(final PluginInstance pPluginInstance,
	    final PluginProjectInformation pPluginProjectInformation) throws PluginServiceException
	{

		// Initialize instance information
		final InstanceConfiguration initializeConfiguration = initializeConfiguration(pPluginInstance);

		// Create roles mapping
		pluginRoleMappingService.createRolesMapping(pPluginInstance.getInstanceId(),
		    pPluginProjectInformation.getRolesMapping());

		// Delete the membership who doesn't have role mapping
		final List<PluginMembership> filteredList = doFilterMembership(initializeConfiguration.getInstanceId(),
		    pPluginProjectInformation.getMemberships());

		// Create project, user and membership on tool
		final String toolProjectId = createToolProject(initializeConfiguration,
		    pPluginProjectInformation.getProject(), filteredList);

		// Update instance information with project tool id
		initializeConfiguration.setToolProjectId(toolProjectId);
		updateConfiguration(initializeConfiguration);

	}

	/**
	 * This method is used in order to intialize instance configuration data from plugin instance
	 *
	 * @param pPluginInstance
	 *          represents the plugin instance
	 * @return instanceconfiguration object
	 * @throws PluginServiceException
	 *           occured if errors come up from DAO acces
	 */
	protected InstanceConfiguration initializeConfiguration(final PluginInstance pPluginInstance)
			throws PluginServiceException
	{
		InstanceConfigurationEntity instanceEntity;
		instanceEntity = newInstanceEntity(pPluginInstance);
		instanceConfigurationDAO.persist(instanceEntity);
		return instanceEntity;
	}

	/**
	 * This method filter membership who doesn't have any role mapping
	 *
	 * @param pInstanceId
	 *          represents the instance id
	 * @param pMemberships
	 *          represents the list of original membership
	 * @return filtered list
	 * @throws PluginServiceException
	 *           if an error to acces to database occured
	 */
	private List<PluginMembership> doFilterMembership(final String pInstanceId,
	    final List<PluginMembership> pMemberships) throws PluginServiceException
	{
		final List<PluginMembership> returnList = new ArrayList<PluginMembership>();
		if (pMemberships != null)
		{
			for (final PluginMembership pluginMembership : pMemberships)
			{
				final String forgeRole = pluginMembership.getRole();
				if (pluginRoleMappingService.existToolRole(pInstanceId, forgeRole))
				{
					returnList.add(pluginMembership);
				}
			}
		}
		return returnList;
	}

	/**
	 * That allows to create a project into the tool behind the plugin
	 *
	 * @param pInstanceConfiguration
	 *          represents instance configuration
	 * @param pPluginProject
	 *          represents project object
	 * @param pMemberships
	 * @return tool project id
	 * @throws PluginServiceException
	 *           used if errors occured
	 */
	protected abstract String createToolProject(final InstanceConfiguration pInstanceConfiguration,
																							final PluginProject pPluginProject,
																							final List<PluginMembership> pMemberships) throws PluginServiceException;

	/**
	 * This method is used in order to update instance configuration data
	 *
	 * @param pIinitializeConfiguration
	 *          represents the instance configuration updated
	 * @throws PluginServiceException
	 *           occured if errors come up from DAO acces
	 */
	protected void updateConfiguration(final InstanceConfiguration pIinitializeConfiguration)
	    throws PluginServiceException
	{
		instanceConfigurationDAO.update(pIinitializeConfiguration);
	}

	/**
	 * This allows to create new instance entity from PluginInstance object
	 *
	 * @param pPluginInstance
	 *          represents to plugin instance object used to create entity
	 * @return InstanceConfigurationEntity created
	 */
	protected InstanceConfigurationEntity newInstanceEntity(final PluginInstance pPluginInstance)
	{
		final InstanceConfigurationEntity instanceEntity = new InstanceConfigurationEntity();
		instanceEntity.setConfigurationId(normalizeService.normalize(pPluginInstance.getInstanceLabel()));
		instanceEntity.setForgeProjectId(pPluginInstance.getForgeProjectId());
		instanceEntity.setForgeId(pPluginInstance.getForgeId());
		instanceEntity.setInstanceId(pPluginInstance.getInstanceId());
		instanceEntity.setToolInstance(getToolInstance(pPluginInstance.getToolInstanceId()));
		return instanceEntity;
	}

	protected ToolInstance getToolInstance(final String pUUID)
	{
		return toolInstanceDAO.findInstanceByUUID(UUID.fromString(pUUID));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateProject(final String pInstanceId, final PluginProject pProject)
	    throws PluginServiceException
	{
		final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);
		updateToolProject(instance, pProject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteProject(final String pInstanceId) throws PluginServiceException
	{
		final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);
		deleteToolProject(instance);
		pluginRoleMappingService.deleteByInstance(pInstanceId);
		instanceConfigurationDAO.delete(instance);
	}

	/**
	 * That allows to delete project from the tool behind the plugin
	 *
	 * @param pInstanceConfigurationEntity
	 *          represents instance configuration
	 * @return true if succeed otherwise false
	 * @throws PluginServiceException
	 *           used if errors occured
	 */
	protected abstract boolean deleteToolProject(final InstanceConfiguration pInstanceConfigurationEntity) throws PluginServiceException;

	/**
	 * {@inheritDoc}
	 *
	 * @throws PluginServiceException
	 */
	@Override
	public abstract void archiveProject(final String pInstanceId) throws PluginServiceException;

	/**
	 * That allows to update project into the tool behind the plugin
	 *
	 * @param pInstanceConfigurationEntity
	 *          represents instance configuration
	 * @param pProject
	 *          represents project information
	 * @return true if successfull
	 * @throws PluginServiceException
	 *           used if errors occured
	 */
	protected abstract boolean updateToolProject(final InstanceConfiguration pInstanceConfigurationEntity,
	    final PluginProject pProject) throws PluginServiceException;

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
	 * Use by container to inject {@link ToolInstanceDAO}
	 * 
	 * @param pToolInstanceDAO
	 *          the toolInstanceDAO to set
	 */
	public void setToolInstanceDAO(final ToolInstanceDAO pToolInstanceDAO)
	{
		toolInstanceDAO = pToolInstanceDAO;
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

	/**
	 * Use by container to inject {@link NormalizeService}
	 * 
	 * @param pNormalizeService
	 *          the normalizeService to set
	 */
	public void setNormalizeService(final NormalizeService pNormalizeService)
	{
		normalizeService = pNormalizeService;
	}

}
