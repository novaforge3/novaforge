/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, version 3 of the
 * License.
 *
 * This file is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have
 * received a copy of the GNU Affero General Public License along with
 * this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with libraries listed in COPYRIGHT file at the
 * top-level directof of this distribution (or a modified version of
 * that libraries), containing parts covered by the terms of licenses
 * cited in the COPYRIGHT file, the licensors of this Program grant
 * you additional permission to convey the resulting work.
 */
package org.novaforge.forge.ui.novadeploy.internal.module;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.organization.services.UserService;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployRestClient;

/**
 * @author B-Martinelli
 */
public class NovaDeployModule implements PortalModule
{
	/** Acces to Portal Strings. */
	private static PortalMessages PORTAL_MESSAGES;

	/** Plugins management. */
	private static PluginsManager PLUGINS_MANAGER;

	/** Authentication service. */
	private static AuthentificationService AUTHENTIFICATION_SERVICE;

	/** Forge configuration Service. */
	private static ForgeConfigurationService FORGE_CONFIGURATION_SERVICE;
	
	private static UserService userService;

	private static NovadeployRestClient novadeployRestClient;

	/**
	 * Return {@link PortalModuleId} defined for this muserServiceodule
	 * 
	 * @return {@link PortalModuleId}
	 */
	public static PortalModuleId getPortalModuleId()
	{
		return PortalModuleId.DEVOPS_MANAGEMENT;
	}

	/** {@inheritDoc} */
	@Override
	public String getId()
	{
		
		return getPortalModuleId().getId();
	}

	/** {@inheritDoc} */
	@Override
	public PortalComponent createComponent(final PortalContext pPortalContext)
	{
		return new MainComponent(pPortalContext);
	}

	/**
	 * @return the {@link PortalMessages}
	 */
	public static PortalMessages getPortalMessages()
	{
		return PORTAL_MESSAGES;
	}

	/**
	 * Use by container to inject {@link PortalMessages}
	 * 
	 * @param pPortalMessages
	 *            the portalMessages to set
	 */
	public void setPortalMessages(final PortalMessages pPortalMessages)
	{
		PORTAL_MESSAGES = pPortalMessages;
	}

	/**
	 * @return the {@link PluginsManager}
	 */
	public static PluginsManager getPluginsManager()
	{
		return PLUGINS_MANAGER;
	}

	/**
	 * Use by container to inject {@link PluginsManager}
	 * 
	 * @param pPluginsManager
	 *            the pluginsManager to set
	 */
	public void setPluginsManager(final PluginsManager pPluginsManager)
	{
		PLUGINS_MANAGER = pPluginsManager;
	}

	/**
	 * @return the {@link AuthentificationService}
	 */
	public static AuthentificationService getAuthentificationService()
	{
		return AUTHENTIFICATION_SERVICE;
	}

	/**
	 * Use by container to inject {@link AuthentificationService}
	 * 
	 * @param pAuthentificationService
	 *            the authentificationService to set
	 */
	public void setAuthentificationService(final AuthentificationService pAuthentificationService)
	{
		AUTHENTIFICATION_SERVICE = pAuthentificationService;
	}

	/**
	 * @return the {@link ForgeCfgService}
	 */
	public static ForgeConfigurationService getForgeConfigurationService()
	{
		return FORGE_CONFIGURATION_SERVICE;
	}

	/**
	 * Use by container to inject {@link ForgeCfgService}
	 * 
	 * @param pForgeConfigurationService
	 *            the forgeConfigurationService to set
	 */
	public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
	{
		FORGE_CONFIGURATION_SERVICE = pForgeConfigurationService;
	}
	

	public static UserService getUserService() {
		return userService;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}


	public static NovadeployRestClient getNovadeployRestClient()
	{
		return novadeployRestClient;
	}

	public void setNovadeployRestClient(NovadeployRestClient novadeployRestClient)
	{
		this.novadeployRestClient = novadeployRestClient;
	}
}
