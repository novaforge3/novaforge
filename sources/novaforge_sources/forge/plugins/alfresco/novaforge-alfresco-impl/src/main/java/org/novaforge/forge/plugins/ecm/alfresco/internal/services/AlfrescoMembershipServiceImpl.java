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
package org.novaforge.forge.plugins.ecm.alfresco.internal.services;

import org.json.JSONObject;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;
import org.novaforge.forge.plugins.ecm.alfresco.datamapper.AlfrescoResourceBuilder;
import org.novaforge.forge.plugins.ecm.alfresco.datamapper.AlfrescoResourceBuilderException;
import org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestClient;
import org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestHelper;
import org.novaforge.forge.plugins.ecm.alfresco.rest.exceptions.AlfrescoRestException;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoConfigurationService;

import java.util.Map;

/**
 * This is an implementation of PluginMembershipService for Alfresco plugin.
 * 
 * @see org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService
 * @see org.novaforge.forge.core.plugins.services.PluginMembershipService
 * @author cadetr
 */
public class AlfrescoMembershipServiceImpl extends AbstractPluginMembershipService
{
	/**
	 * Reference to service implementation of {@link AlfrescoResourceBuilder}
	 */
	private AlfrescoResourceBuilder      alfrescoResourceBuilder;
	/**
	 * Reference to service implementation of {@link AlfrescoRestClient}
	 */
	private AlfrescoRestClient           alfrescoRestClient;
	/**
	 * Reference to service implementation of {@link AlfrescoConfigurationService}
	 */
	private AlfrescoConfigurationService pluginConfigurationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean addToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
	    final String pToolRole) throws PluginServiceException
	{

		final Map<String, String> user = alfrescoResourceBuilder.createUser(pUser);
		final String toolRoleId = pluginRoleMappingService.getToolRoleId(pToolRole);
		try
		{
			final JSONObject inviteUser = alfrescoResourceBuilder.inviteUser(pUser, pInstance.getToolProjectId(),
			    toolRoleId);

			final AlfrescoRestHelper connector = alfrescoRestClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			alfrescoRestClient.createUser(connector, user);
			alfrescoRestClient.inviteSiteUser(connector, inviteUser);

		}
		catch (final AlfrescoRestException e)
		{
			throw new PluginServiceException("Unable to build Alfresco client through AlfrescoClient.", e);
		}
		catch (final AlfrescoResourceBuilderException e)
		{
			throw new PluginServiceException(
			    String.format(
			        "Unable to build JSON object which represents an alfresco membership with [instance=%s, user=%s, role=%s]",
			        pInstance, pUser.toString(), pToolRole), e);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
	    final String pToolRole) throws PluginServiceException
	{
		try
		{
			final String toolRoleId = pluginRoleMappingService.getToolRoleId(pToolRole);
			final JSONObject inviteUser = alfrescoResourceBuilder.inviteUser(pUser, pInstance.getToolProjectId(),
			    toolRoleId);

			final AlfrescoRestHelper connector = alfrescoRestClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			alfrescoRestClient.inviteSiteUser(connector, inviteUser);

		}
		catch (final AlfrescoResourceBuilderException e)
		{
			throw new PluginServiceException(
			    String.format(
			        "Unable to build JSON object which represents an alfresco membership with [instance=%s, user=%s, role=%s]",
			        pInstance, pUser.toString(), pToolRole), e);
		}
		catch (final AlfrescoRestException e)
		{
			throw new PluginServiceException(
			    "Unable to connect to Alfresco service through AlfrescoClientService.", e);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removeToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
	    final String pToolRole) throws PluginServiceException
	{
		try
		{
			final String toolRoleId = pluginRoleMappingService.getToolRoleId(pToolRole);
			final JSONObject disinviteUser = alfrescoResourceBuilder.inviteUser(pUser,
			    pInstance.getToolProjectId(), toolRoleId);

			final AlfrescoRestHelper connector = alfrescoRestClient.getConnector(
			    pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
			    pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			alfrescoRestClient.disinviteSiteUser(connector, disinviteUser);

		}
		catch (final AlfrescoResourceBuilderException e)
		{
			throw new PluginServiceException(
			    String.format(
			        "Unable to build JSON object which represents an alfresco membership with [instance=%s, user=%s, role=%s]",
			        pInstance, pUser.toString(), pToolRole), e);
		}
		catch (final AlfrescoRestException e)
		{
			throw new PluginServiceException("Unable to build Alfresco client through AlfrescoClient.", e);
		}
		return true;
	}

	/**
	 * Use by container to inject {@link AlfrescoResourceBuilder}
	 * 
	 * @param pAlfrescoResourceBuilder
	 *          the alfrescoResourceBuilder to set
	 */
	public void setAlfrescoResourceBuilder(final AlfrescoResourceBuilder pAlfrescoResourceBuilder)
	{
		alfrescoResourceBuilder = pAlfrescoResourceBuilder;
	}

	/**
	 * Use by container to inject {@link AlfrescoRestClient}
	 * 
	 * @param pAlfrescoRestClient
	 *          the alfrescoRestClientCustom to set
	 */
	public void setAlfrescoRestClient(final AlfrescoRestClient pAlfrescoRestClient)
	{
		alfrescoRestClient = pAlfrescoRestClient;
	}

	/**
	 * Use by container to inject {@link AlfrescoConfigurationService}
	 * 
	 * @param pPluginConfigurationService
	 *          the configurationService to set
	 */
	public void setPluginConfigurationService(final AlfrescoConfigurationService pPluginConfigurationService)
	{
		pluginConfigurationService = pPluginConfigurationService;
	}

}
