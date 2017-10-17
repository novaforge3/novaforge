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

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarRestClient;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarRestException;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarWSContext;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarWSContextFactory;


/**
 * This is an implementation of PluginMembershipService for Sonar plugin.
 * 
 * @see org.novaforge.forge.plugins.commons.services.
 *      AbstractPluginMembershipService
 * @see org.novaforge.forge.core.plugins.services.PluginMembershipService
 * @author BILET-JC
 */
public class SonarMembershipServiceImpl extends AbstractPluginMembershipService implements PluginMembershipService {

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean addToolUserMemberships(final InstanceConfiguration pInstanceConfiguration, final PluginUser pluginUser,
			final String pToolRole) throws PluginServiceException {

		boolean ret = true;

		final SonarWSContext sonarWSContext = getSonarWSContext(pInstanceConfiguration);

		final String roleId = pluginRoleMappingService.getToolRoleId(pToolRole);
		final String userLogin = pluginUser.getLogin();
		final String userName = pluginUser.getFirstName() + ' ' + pluginUser.getName();
		final String forgeProjectId = pInstanceConfiguration.getForgeProjectId();
		
		try {
			
			sonarRestClient.addMembership(
					sonarWSContext, 
					roleId, 
					forgeProjectId, 
					userLogin, 
					userName, 
					pluginUser.getEmail(), 
					pluginUser.getPassword());
		
		} catch (SonarRestException e) {
			
			ret = false;
			throw new PluginServiceException("fail to remove membership [login=" 
					+ userLogin 
					+ " - name="
					+ userName
					+ " - Sonar role=" 
					+ roleId + " - project=" 
					+ forgeProjectId + " caused by: " 
					+ e.getMessage());
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolUserMemberships(final InstanceConfiguration pInstanceConfiguration,
			final PluginUser pluginUser, final String pToolRole) throws PluginServiceException {

		
		return this.addToolUserMemberships(pInstanceConfiguration, pluginUser, pToolRole);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removeToolUserMemberships(final InstanceConfiguration pInstanceConfiguration,
			final PluginUser pluginUser, final String pToolRole) throws PluginServiceException {
		
		boolean ret = true;
		
		final String roleId = pluginRoleMappingService.getToolRoleId(pToolRole);
		final String userLogin = pluginUser.getLogin();
		final String userName = pluginUser.getFirstName() + ' ' + pluginUser.getName();
		final String forgeProjectId = pInstanceConfiguration.getForgeProjectId();		
		final SonarWSContext sonarWSContext = getSonarWSContext(pInstanceConfiguration);

		try {
			
			sonarRestClient.removeMembership(
				sonarWSContext, 
				roleId, 
				forgeProjectId,
				userLogin);
				
		} catch (SonarRestException e) {
			
			ret = false;
			throw new PluginServiceException("fail to remove membership [login=" 
					+ userLogin 
					+ " - name="
					+ userName
					+ " - Sonar role=" 
					+ roleId 
					+ " - project=" 
					+ forgeProjectId 
					+ " caused by: " 
					+ e.getMessage());
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
