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
package org.novaforge.forge.plugins.quality.sonar.internal.services;

import java.util.List;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarRestClient;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarRestException;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarWSContext;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarWSContextFactory;

/**
 * @author BILET-JC
 * @author lequere-g update for sonar 5.2 compliance
 */
public class SonarProjectServiceImpl extends AbstractPluginProjectService implements PluginProjectService {

	/**
	 * SonarRestClient service injected by container.
	 */
	private SonarRestClient sonarRestClient;
	/**
	 * SonarWSContextFactory service injected by container
	 */
	private SonarWSContextFactory sonarWSContextFactory;
	/**
	 * PluginConfigurationService service injected by container.
	 */
	private PluginConfigurationService pluginConfigurationService;

	@Override
	protected String createToolProject(final InstanceConfiguration pInstanceConfiguration, final PluginProject pProject,
			final List<PluginMembership> pMemberships) throws PluginServiceException {

		final String instanceId = pInstanceConfiguration.getInstanceId();
		final SonarWSContext sonarWSContext = getSonarWSContext(pInstanceConfiguration);

		final String forgeProjectId = pProject.getProjectId();
		
		// Create a sonar groups 'projectId'_'roleId' where roleId = admin,
		// user, codeviewer, analysis executor, issue admin)
		try {
			
			sonarRestClient.createGroupsForProject(sonarWSContext, forgeProjectId);
		
		} catch (SonarRestException e) {
			
			throw new PluginServiceException("Fails to create groups for project " + forgeProjectId + " caused by " + e.getMessage());
		}
		
		// add memberships 
		for (final PluginMembership pluginMembership : pMemberships) {
			
			final String forgeRole = pluginMembership.getRole();
			
			if (pluginRoleMappingService.existToolRole(instanceId, forgeRole)) {
				
				final String toolRole = pluginRoleMappingService.getToolRole(instanceId, forgeRole);
				final String roleId = pluginRoleMappingService.getToolRoleId(toolRole);
				
				final PluginUser pluginUser = pluginMembership.getPluginUser();

				try {
					sonarRestClient.addMembership(
							sonarWSContext, 
							roleId, 
							forgeProjectId, 
							pluginUser.getLogin(), 
							pluginUser.getFirstName() + ' ' + pluginUser.getName(), 
							pluginUser.getEmail(), 
							pluginUser.getPassword());
				
				} catch (SonarRestException e) {
					
					throw new PluginServiceException("Fails to add membership " + pluginUser.getLogin() + " to project " + forgeProjectId + " caused by " + e.getMessage());
				}
			}
		}
		
		return pProject.getProjectId();
	}


	@Override
	protected boolean deleteToolProject(final InstanceConfiguration pInstanceConfiguration)
			throws PluginServiceException {
		
		boolean ret = true;
		final String forgeProjectId = pInstanceConfiguration.getForgeProjectId();
		
		try {	

			if (null != pluginRoleMappingService) {
	
				final SonarWSContext sonarWSContext = getSonarWSContext(pInstanceConfiguration);
	
				
				sonarRestClient.deleteGroup(sonarWSContext, forgeProjectId + "_admin");
				sonarRestClient.deleteGroup(sonarWSContext, forgeProjectId + "_user");
				sonarRestClient.deleteGroup(sonarWSContext, forgeProjectId + "_codeviewer");
			}
		} catch (SonarRestException e) {
			
			ret = false;
			
			throw new PluginServiceException("Fails to remove groups from project \"" + forgeProjectId + e.getMessage());
		}
		return ret;
	}

	private SonarWSContext getSonarWSContext(final InstanceConfiguration pInstanceConfiguration)
			throws PluginServiceException {
		
		final SonarWSContext sonarWSContext = sonarWSContextFactory.getWSContext(
				pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
				pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
		
		return sonarWSContext;
	}

	@Override
	public void archiveProject(final String arg0) throws PluginServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean updateToolProject(final InstanceConfiguration arg0, final PluginProject arg1)
			throws PluginServiceException {
		return true;
	}

	/**
	 * Used by the OSGi container (Karaf ) to inject the instance (see the
	 * blueprint.xml file of novaforge-sonar-impl module)
	 * 
	 * @param pSonarRestClient
	 */
	public void setSonarRestClient(final SonarRestClient pSonarRestClient) {
		sonarRestClient = pSonarRestClient;
	}

	/**
	 * Used by the OSGi container (Karaf ) to inject the instance (see the
	 * blueprint.xml file of novaforge-sonar-impl module)
	 * 
	 * @param pPluginConfigurationService
	 */
	public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService) {
		pluginConfigurationService = pPluginConfigurationService;
	}

	/**
	 * Used by the OSGi container (Karaf ) to inject the instance (see the
	 * blueprint.xml file of novaforge-sonar-impl module)
	 * 
	 * @param sonarWSContextFactory
	 */
	public void setSonarWSContextFactory(SonarWSContextFactory sonarWSContextFactory) {
		this.sonarWSContextFactory = sonarWSContextFactory;
	}

}
