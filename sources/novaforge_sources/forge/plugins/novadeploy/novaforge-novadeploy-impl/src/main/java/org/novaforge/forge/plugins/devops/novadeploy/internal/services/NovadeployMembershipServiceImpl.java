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
package org.novaforge.forge.plugins.devops.novadeploy.internal.services;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;
import org.novaforge.forge.plugins.devops.novadeploy.client.NovadeployClient;
import org.novaforge.forge.plugins.devops.novadeploy.client.NovadeployClientException;
import org.novaforge.forge.plugins.devops.novadeploy.client.NovadeployRestClient;
import org.novaforge.forge.plugins.devops.novadeploy.client.data.CustomerEnvironment;
import org.novaforge.forge.plugins.devops.novadeploy.client.data.Membership;
import org.novaforge.forge.plugins.devops.novadeploy.datamapper.NovadeployResourceBuilder;
import org.novaforge.forge.plugins.devops.novadeploy.model.NovadeployUser;
import org.novaforge.forge.plugins.devops.novadeploy.services.NovadeployConfigurationService;

/**
 * @author dekimpea
 */
public class NovadeployMembershipServiceImpl extends AbstractPluginMembershipService {

	/**
	 * Reference to service implementation of {@link NovadeployXmlRpcClient}
	 */
	private NovadeployRestClient NovadeployRestClient;

	/**
	 * Reference to service implementation of {@link NovadeployResourceBuilder}
	 */
	private NovadeployResourceBuilder NovadeployResourceBuilder;

	/**
	 * Reference to service implementation of
	 * {@link NovadeployConfigurationService}
	 */
	private NovadeployConfigurationService NovadeployConfigurationService;

	/**
	 * Use by container to inject {@link NovadeployRestClient}
	 *
	 * @param pNovadeployRestClient
	 *            the NovadeployRestClient to set
	 */
	public void setNovadeployRestClient(final NovadeployRestClient pNovadeployRestClient) {
		NovadeployRestClient = pNovadeployRestClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean addToolUserMemberships(final InstanceConfiguration pInstance,
			final PluginUser pUser, final String pToolRole) throws PluginServiceException {

		final NovadeployUser user = NovadeployResourceBuilder.buildNovadeployUser(pUser);
		final String roleId = pluginRoleMappingService.getToolRoleId(pToolRole);

		try {
			NovadeployClient connector = NovadeployRestClient.getConnector(
					NovadeployConfigurationService.getClientURL(NovadeployConfigurationService
							.getDefaultToolURL()), NovadeployConfigurationService.getClientAdmin(),
					NovadeployConfigurationService.getClientPwd());

			NovadeployRestClient.createUser(connector, user);
			NovadeployRestClient.setUserPermissionToNameSpace(connector,
					pInstance.getToolProjectId(), user.getUserName(), Integer.parseInt(roleId));
			
		} catch (final NovadeployClientException  e) {
			throw new PluginServiceException(String.format(
					"Unable to add or update user membership with [instance=%s, user=%s] ",
					pInstance.toString(), pUser.toString()), e);
		}
		return true;

	}

	/**
	 * Use by container to inject {@link NovadeployResourceBuilder}
	 *
	 * @param pNovadeployResourceBuilder
	 *            the NovadeployResourceBuilder to set
	 */
	public void setNovadeployResourceBuilder(
			final NovadeployResourceBuilder pNovadeployResourceBuilder) {
		NovadeployResourceBuilder = pNovadeployResourceBuilder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolUserMemberships(final InstanceConfiguration pInstance,
			final PluginUser pUser, final String pToolRole) throws PluginServiceException {
		return addToolUserMemberships(pInstance, pUser, pToolRole);

	}

	/**
	 * Use by container to inject {@link NovadeployConfigurationService}
	 *
	 * @param pNovadeployConfigurationService
	 *            the NovadeployConfigurationService to set
	 */
	public void setNovadeployConfigurationService(
			final NovadeployConfigurationService pNovadeployConfigurationService) {
		NovadeployConfigurationService = pNovadeployConfigurationService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removeToolUserMemberships(final InstanceConfiguration pInstance,
			final PluginUser pUser, final String pToolRole) throws PluginServiceException {
		try {

			NovadeployClient connector = NovadeployRestClient.getConnector(
					NovadeployConfigurationService.getClientURL(NovadeployConfigurationService
							.getDefaultToolURL()), NovadeployConfigurationService.getClientAdmin(),
					NovadeployConfigurationService.getClientPwd());

			NovadeployRestClient.deleteUserPermission(connector, pInstance.getToolProjectId(),
					pUser.getLogin());

		} catch (final NovadeployClientException e) {
			throw new PluginServiceException(String.format(
					"Unable to add or update user membership with [instance=%s, user=%s] ",
					pInstance.toString(), pUser.toString()), e);
		}
		return true;
	}

}
