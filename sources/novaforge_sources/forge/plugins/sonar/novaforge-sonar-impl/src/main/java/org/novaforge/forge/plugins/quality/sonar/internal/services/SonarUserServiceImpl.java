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
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarRestClient;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarRestException;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarWSContext;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarWSContextFactory;
import org.novaforge.forge.plugins.quality.sonar.ws.models.User;


/**
 * This is an implementation of PluginUserService for Nexus plugin.
 * 
 * @see org.novaforge.forge.plugins.commons.services.AbstractPluginUserService
 * @see org.novaforge.forge.core.plugins.services.PluginUserService
 * @author BILET-JC
 */
public class SonarUserServiceImpl extends AbstractPluginUserService implements PluginUserService {

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
	public void createAdministratorUser(final ToolInstance toolInstance, final PluginUser pluginUser)
			throws PluginServiceException {

		final SonarWSContext sonarWSContext = sonarWSContextFactory.getWSContext(
				pluginConfigurationService.getClientURL(toolInstance.getBaseURL()),
				pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

		final String userLogin = pluginUser.getLogin();
		final String userName =	pluginUser.getFirstName() + ' ' + pluginUser.getName();
		
		User sonarUser = null;
		
		try {
			
			sonarUser = sonarRestClient.findUser(sonarWSContext, userLogin);
			
		} catch (SonarRestException e) {
	
			throw new PluginServiceException("Fails to search user [login="
					+ userLogin
					+ " - name="
					+ userName
					+ "] caused by:"
					+ e.getMessage());
		}
		
		
		if (sonarUser == null) {

			try {
				// create the sonar user administrator
				sonarRestClient.createUser(
						sonarWSContext, 
						userLogin, 
						userName, 
						pluginUser.getEmail(), 
						pluginUser.getPassword());
				
				// add to the group which has admin privilege
				sonarRestClient.addUserToGroup(sonarWSContext, userLogin, "sonar-administrators");
			
			} catch (SonarRestException e) {
				
				throw new PluginServiceException("Fails to create user [login="
						+ userLogin
						+ " - name="
						+ userName
						+ "] caused by:"
						+ e.getMessage());
			}
			
		} else {

			try {
				sonarRestClient.updateUser(
						sonarWSContext, 
						userLogin, 
						pluginUser.getName(), 
						pluginUser.getEmail(), 
						pluginUser.getPassword());
				
			} catch (SonarRestException e) {
				
				throw new PluginServiceException("Fails to update user [login="
						+ userLogin
						+ " - name="
						+ userName
						+ "] caused by:"
						+ e.getMessage());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removeToolUser(final InstanceConfiguration pInstanceConfiguration, final PluginUser pluginUser)
			throws PluginServiceException {

		boolean returnValue = true;
		
		final SonarWSContext sonarWSContext = getSonarWSContext(pInstanceConfiguration);
		
		final String userLogin = pluginUser.getLogin(); 
		final String userName =	pluginUser.getFirstName() + ' ' + pluginUser.getName();
		
		try {
			sonarRestClient.deleteUser(sonarWSContext, userLogin);
		
		} catch (SonarRestException e) {
			
			returnValue = false;
			
			throw new PluginServiceException("Fails to delete user [login="
					+ userLogin
					+ " - name="
					+ userName
					+ "] caused by:"
					+ e.getMessage());
		}
		return returnValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolUser(final InstanceConfiguration pInstanceConfiguration, final String pluginUserName,
			final PluginUser pluginUser) throws PluginServiceException {

		boolean returnValue = false;
		final SonarWSContext sonarWSContext = getSonarWSContext(pInstanceConfiguration);
		
		final String userLogin = pluginUser.getLogin(); 
		final String userName =	pluginUser.getFirstName() + ' ' + pluginUser.getName();
		
		User sonarUser = null;
		
		try {
			sonarUser = sonarRestClient.findUser(sonarWSContext, pluginUserName);
			
		} catch (SonarRestException e) {
			
			returnValue = false;
			
			throw new PluginServiceException("User not found [login="
					+ userLogin
					+ " - name="
					+ userName
					+ "] caused by:"
					+ e.getMessage());
		}
		
		if (sonarUser != null) {
				
			try {
				sonarRestClient.updateUser(
						sonarWSContext, 
						userLogin, 
						userName, 
						pluginUser.getEmail(), 
						pluginUser.getPassword());
			
			} catch (SonarRestException e) {
				
				returnValue = false;
				
				throw new PluginServiceException("Fails to update user [login="
						+ userLogin
						+ " - name="
						+ userName
						+ "] caused by:"
						+ e.getMessage());
			}
		}
		return returnValue;
	}

	/**
	 * Return a new instance of the Web service call context
	 * @param pInstanceConfiguration
	 * @return
	 * @throws PluginServiceException
	 */
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
