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
package org.novaforge.forge.plugins.management.managementmodule.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;
import org.novaforge.forge.tools.managementmodule.business.ManagementModuleManager;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.Role;
import org.novaforge.forge.tools.managementmodule.domain.User;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;

import java.text.MessageFormat;

/**
 * @author efalsquelle
 */
public class ManagementModuleMembershipServiceImpl extends AbstractPluginMembershipService implements
		PluginMembershipService
{
	/*
	 * Service injection declaration
	 */
	private ManagementModuleManager managementModuleManager;

	@Override
	protected boolean addToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
			final String pToolRole) throws PluginServiceException
	{
		try
		{
			if (!managementModuleManager.existUserLogin(pUser.getLogin()))
			{
				// create user
				managementModuleManager.creeteUser(pUser.getFirstName(), pUser.getName(), pUser.getLogin(),
						pUser.getLanguage());
			}
			// add user to project
			managementModuleManager.addUserMembership(pInstance.getToolProjectId(), pUser.getLogin(), pToolRole);
			return true;
		}
		catch (ManagementModuleException e)
		{
			throw new PluginServiceException(MessageFormat.format(
					"Unable to add users to project with projectID = {0}, login = {1} and role = {2}",
					pInstance.getToolProjectId(), pUser.getLogin(), pToolRole), e);
		}

	}

	@Override
	protected boolean updateToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
			final String pToolRole) throws PluginServiceException
	{

		try
		{

			User currentUser = managementModuleManager.getUser(pUser.getLogin());
			if (currentUser == null)
			{
				throw new PluginServiceException(MessageFormat.format("User doesn't exist with login = {0}",
						pUser.getLogin()));
			}

			Project currentProject = managementModuleManager.getFullProject(pInstance.getToolProjectId());
			if (currentProject == null)
			{
				throw new PluginServiceException("Project doesn't exist with id = " + pInstance.getToolProjectId());
			}
			Role currentRole = managementModuleManager.getRoleByName(pToolRole);
			if (currentRole == null)
			{
				throw new PluginServiceException(
						MessageFormat.format("Role doesn't exist with role = {0}", pToolRole));
			}
			return managementModuleManager.updateMembership(currentUser, currentProject, currentRole);

		}
		catch (ManagementModuleException e)
		{
			throw new PluginServiceException(MessageFormat.format(
					"Unable to update user to project with projectID =%s, login = %s and role = %s",
					pInstance.getToolProjectId(), pUser.getLogin(), pToolRole), e);
		}

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
			User currentUser = managementModuleManager.getUser(pUser.getLogin());
			if (currentUser == null)
			{
				throw new PluginServiceException(MessageFormat.format("User doesn't exist with login = %s",
						pUser.getLogin()));
			}
			Project currentProject = managementModuleManager.getFullProject(pInstance.getToolProjectId());
			if (currentProject == null)
			{
				throw new PluginServiceException(MessageFormat.format("Project doesn't exist with id = %s",
						pInstance.getToolProjectId()));
			}

			return managementModuleManager.deleteMembership(currentUser, currentProject);

		}
		catch (ManagementModuleException e)
		{
			throw new PluginServiceException(MessageFormat.format(
					"Unable to remove user from project with projectID =%s, login = %s", pInstance.getToolProjectId(),
					pUser.getLogin()), e);
		}
	}

	public void setManagementModuleManager(final ManagementModuleManager pManagementModuleManager)
	{
		managementModuleManager = pManagementModuleManager;
	}

}