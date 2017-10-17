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
package org.novaforge.forge.plugins.management.managementmodule.internal;

import org.novaforge.forge.core.plugins.categories.management.IterationBean;
import org.novaforge.forge.core.plugins.categories.management.ManagementCategoryService;
import org.novaforge.forge.core.plugins.categories.management.ManagementServiceException;
import org.novaforge.forge.core.plugins.categories.management.TaskBean;
import org.novaforge.forge.core.plugins.categories.management.TaskInfoBean;
import org.novaforge.forge.core.plugins.categories.management.TaskStatusEnum;
import org.novaforge.forge.core.plugins.categories.management.TaskTypeEnum;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.commons.services.AbstractPluginCategoryService;
import org.novaforge.forge.plugins.management.managementmodule.services.ManagementModuleFunctionalService;

import java.util.Locale;
import java.util.Set;

/**
 * Management Module implementation of the ManagementCategoryService
 */
public class ManagementModuleCategoryServiceImpl extends AbstractPluginCategoryService implements
		ManagementCategoryService
{

	/** Property file name use */
	private static final String							 PROPERTY_FILE = "management_module";
	private ManagementModuleFunctionalService managementModuleFunctionalService;

	@Override
	protected String getPropertyFileName()
	{
		return PROPERTY_FILE;
	}

	@Override
	public String getApplicationAccessInfo(final String instanceId, final Locale locale)
			throws PluginServiceException
	{
		return getMessage(KEY_ACCESS_INFO, locale);
	}

	@Override
	public TaskBean getTask(final String login, final String instanceId, final String forgeId,
			final String taskId) throws ManagementServiceException
	{
		return managementModuleFunctionalService.getTask(login, instanceId, forgeId, taskId);
	}

	@Override
	public Set<TaskInfoBean> getTaskList(final String login, final String instanceId, final String forgeId)
			throws ManagementServiceException
	{
		return managementModuleFunctionalService.getTaskList(login, instanceId, forgeId);
	}

	@Override
	public Set<TaskInfoBean> getTaskListOfCurrentIteration(final String login, final String instanceId,
			final String forgeId) throws ManagementServiceException
	{
		return managementModuleFunctionalService.getTaskListOfCurrentIteration(login, instanceId, forgeId);
	}

	@Override
	public Set<IterationBean> getIterationList(final String login, final String instanceId, final String forgeId)
			throws ManagementServiceException
	{
		return managementModuleFunctionalService.getIterationList(login, instanceId, forgeId);
	}

	@Override
	public Set<TaskInfoBean> getTaskListByIteration(final String login, final String instanceId,
			final String forgeId, final String iterationId) throws ManagementServiceException
	{
		return managementModuleFunctionalService.getTaskListByIteration(login, instanceId, forgeId, iterationId);
	}

	@Override
	public Set<TaskInfoBean> getTaskListByType(final String login, final String instanceId,
			final String forgeId, final TaskTypeEnum taskType) throws ManagementServiceException
	{
		return managementModuleFunctionalService.getTaskListByType(login, instanceId, forgeId, taskType);
	}

	@Override
	public Set<TaskInfoBean> getTaskListByStatus(final String login, final String instanceId,
			final String forgeId, final TaskStatusEnum taskStatus) throws ManagementServiceException
	{
		return managementModuleFunctionalService.getTaskListByStatus(login, instanceId, forgeId, taskStatus);
	}

	@Override
	public void modifyTask(final String login, final String instanceId, final String forgeId,
			final TaskBean taskToModify) throws ManagementServiceException
	{
		managementModuleFunctionalService.modifyTask(login, instanceId, forgeId, taskToModify);
	}

	@Override
	public void closeTask(final String login, final String instanceId, final String forgeId, final String taskId)
			throws ManagementServiceException
	{
		managementModuleFunctionalService.closeTask(login, instanceId, forgeId, taskId);
	}

	public void setManagementModuleFunctionalService(
			final ManagementModuleFunctionalService pManagementModuleFunctionalService)
	{
		managementModuleFunctionalService = pManagementModuleFunctionalService;
	}

}
