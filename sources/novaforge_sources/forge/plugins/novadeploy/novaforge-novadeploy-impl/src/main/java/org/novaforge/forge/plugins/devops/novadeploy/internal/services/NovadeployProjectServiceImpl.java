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

import java.io.PrintWriter;
import java.util.List;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;
import org.novaforge.forge.plugins.devops.novadeploy.client.NovadeployClient;
import org.novaforge.forge.plugins.devops.novadeploy.client.NovadeployClientException;
import org.novaforge.forge.plugins.devops.novadeploy.client.NovadeployRestClient;
import org.novaforge.forge.plugins.devops.novadeploy.client.data.AccountState;
import org.novaforge.forge.plugins.devops.novadeploy.client.data.Membership;
import org.novaforge.forge.plugins.devops.novadeploy.datamapper.NovadeployResourceBuilder;
import org.novaforge.forge.plugins.devops.novadeploy.model.NovadeployUser;
import org.novaforge.forge.plugins.devops.novadeploy.services.NovadeployConfigurationService;

/**
 * @author dekimpea
 */
public class NovadeployProjectServiceImpl extends AbstractPluginProjectService
		implements PluginProjectService {

	/**
	 * Reference to service implementation of {@link NovadeployRestClient}
	 */
	private NovadeployRestClient novadeployRestClient;
	/**
	 * Reference to service implementation of
	 * {@link NovadeployConfigurationService}
	 */
	private NovadeployConfigurationService novadeployConfigurationService;
	/**
	 * Reference to service implementation of {@link NovadeployResourceBuilder}
	 */
	private NovadeployResourceBuilder novadeployResourceBuilder;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String createToolProject(
			final InstanceConfiguration pInstanceConfiguration,
			final PluginProject pPluginProject,
			final List<PluginMembership> pPluginMembership)
			throws PluginServiceException {
		String toolId = "";

		
		try {
			
			
			NovadeployClient connector = novadeployRestClient.getConnector(
					novadeployConfigurationService
							.getClientURL(novadeployConfigurationService.getDefaultToolURL()),
					novadeployConfigurationService.getClientAdmin(),
					novadeployConfigurationService.getClientPwd());
			
			toolId = novadeployResourceBuilder.buildNovadeployNameSpace(pPluginProject.getProjectId());
			AccountState state = null;
		
			state = novadeployRestClient.accountStatus(connector, toolId);
			switch(state){
			case LOCK:
					novadeployRestClient.unlockAccount(connector, toolId);
				break;
			case UNKNOWN:
					novadeployRestClient.createAccount(connector,toolId);
				
				break;
				default:
					throw new Exception("Account must be in LOCK state or be inexistant");
			}

			// Add user to Novadeploy with correct permission
			addUsers(connector, pInstanceConfiguration.getInstanceId(),toolId, pPluginMembership);

			
		} catch (final Exception e) {
			throw new PluginServiceException(
					String.format(
							"Unable to create project with InstanceConfiguration=%s and PluginProject=%s",
							pInstanceConfiguration.toString(),
							pPluginProject.toString()), e);
		}
		return toolId;
	}

	/**
	 * @param pInstanceId
	 * @param pToolId
	 * @param pPluginMembership
	 * @throws PluginServiceException
	 * @throws NovadeployClientException
	 */
	private void addUsers(final NovadeployClient pConnector, final String pInstanceId, final String pProjectToolId,final List<PluginMembership> pPluginMembership)
			throws PluginServiceException, NovadeployClientException {
		
		for (final PluginMembership pluginMembership : pPluginMembership) {
			
			final String toolRole = pluginRoleMappingService.getToolRole(
					pInstanceId, pluginMembership.getRole());
			
			final int toolRoleId = Integer.parseInt(pluginRoleMappingService
					.getToolRoleId(toolRole));

			final NovadeployUser user = novadeployResourceBuilder
					.buildNovadeployUser(pluginMembership.getPluginUser());

			
			novadeployRestClient.createUser(pConnector, user);
			
			novadeployRestClient.setUserPermissionToNameSpace(pConnector,
					pProjectToolId, user.getUserName(), toolRoleId);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean deleteToolProject(final InstanceConfiguration pInstance)
			throws PluginServiceException {
		try {

			NovadeployClient connector = novadeployRestClient.getConnector(
					novadeployConfigurationService.getClientURL(pInstance
							.getToolInstance().getBaseURL()),
					novadeployConfigurationService.getClientAdmin(),
					novadeployConfigurationService.getClientPwd());
			final String nameSpace = pInstance.getToolProjectId();

			List<Membership> memberships = novadeployRestClient.accountMemberships(connector, nameSpace);

			for(Membership m : memberships){
				novadeployRestClient.deleteUserPermission(connector, nameSpace, m.getUser());
			}

			novadeployRestClient.lockAccount(connector, nameSpace);

		} catch (final NovadeployClientException e) {
			throw new PluginServiceException(String.format(
					"Unable to delete project with [tool_project_id=%s]",
					pInstance.getToolProjectId()), e);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void archiveProject(final String pInstanceId) throws PluginServiceException
	{

		// TODO Not implemented yet
		throw new PluginServiceException("NovaDeploy archiveProject - not implemented yet");

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolProject(final InstanceConfiguration pInstance, final PluginProject pProject)
			throws PluginServiceException
	{
		// TODO ?

		return true;
	}

	/**
	 * Use by container to inject {@link NovadeployRestClient}
	 * 
	 * @param pNovadeployRestClient
	 *            the NovadeployRestClient to set
	 */
	public void setNovadeployRestClient(
			final NovadeployRestClient pNovadeployRestClient) {
		novadeployRestClient = pNovadeployRestClient;
	}

	/**
	 * Use by container to inject {@link NovadeployConfigurationService}
	 * 
	 * @param pNovadeployConfigurationService
	 *            the NovadeployConfigurationService to set
	 */
	public void setNovadeployConfigurationService(
			final NovadeployConfigurationService pNovadeployConfigurationService) {
		novadeployConfigurationService = pNovadeployConfigurationService;
	}

	/**
	 * Use by container to inject {@link NovadeployResourceBuilder}
	 * 
	 * @param pNovadeployResourceBuilder
	 *            the NovadeployResourceBuilder to set
	 */
	public void setNovadeployResourceBuilder(
			final NovadeployResourceBuilder pNovadeployResourceBuilder) {
		novadeployResourceBuilder = pNovadeployResourceBuilder;
	}
}
