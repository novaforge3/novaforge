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
package org.novaforge.forge.tools.managementmodule.ui.server;

import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.domain.StatusTask;
import org.novaforge.forge.tools.managementmodule.domain.TaskType;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanStatus;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskStatusEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.UnitTimeEnum;

/**
 * This class permits to get a client value form a server value on different
 * object types
 */
public class ServerToClientMapper
{
  /**
   * private constructor
   */
  private ServerToClientMapper()
  {
  }

	/**
	 * Return an ApplicativeFunction client enum, from a server string value
	 * 
	 * @param applicativeFunctionName
	 *          the applicative function name
	 * @return the appropriate ApplicativeFunction
	 */
	public static ApplicativeFunction getApplicativeFunctionFromServerStringValue(
			final String applicativeFunctionName)
	{
		if (applicativeFunctionName.equals(ManagementModuleConstants.FUNCTION_ESTIMATION))
		{
			return ApplicativeFunction.FUNCTION_ESTIMATION;
		}
		else if (applicativeFunctionName.equals(ManagementModuleConstants.FUNCTION_TASK_MANAGEMENT))
		{
			return ApplicativeFunction.FUNCTION_MANAGING_TASK;
		}
		else if (applicativeFunctionName.equals(ManagementModuleConstants.FUNCTION_PERIMETER_MANAGEMENT))
		{
			return ApplicativeFunction.FUNCTION_PERIMETER;
		}
		else if (applicativeFunctionName.equals(ManagementModuleConstants.FUNCTION_CHARGE_PLAN))
		{
			return ApplicativeFunction.FUNCTION_CHARGE_PLAN;
		}
		else if (applicativeFunctionName.equals(ManagementModuleConstants.FUNCTION_ITERATION_PREPARATION))
		{
			return ApplicativeFunction.FUNCTION_PREPARATION_ITERATION;
		}
		else if (applicativeFunctionName.equals(ManagementModuleConstants.FUNCTION_GLOBAL_SUPERVISION))
		{
			return ApplicativeFunction.FUNCTION_GLOBAL_MONITORING;
		}
		else if (applicativeFunctionName.equals(ManagementModuleConstants.FUNCTION_ITERATION_SUPERVISION))
		{
			return ApplicativeFunction.FUNCTION_ITERATION_MONITORING;
		} else if (applicativeFunctionName.equals(ManagementModuleConstants.FUNCTION_ADMINISTRATION))
    {
      return ApplicativeFunction.FUNCTION_ADMINISTRATION;
    } else if (applicativeFunctionName.equals(ManagementModuleConstants.FUNCTION_PROJECT_PLAN))
    {
      return ApplicativeFunction.FUNCTION_PROJECT_PLAN;
    }
    else if (applicativeFunctionName.equals(ManagementModuleConstants.FUNCTION_BURNDOWN))
    {
      return ApplicativeFunction.FUNCTION_BURNDOWN;
    }
		else
		{
			return null;
		}
	}

	/**
	 * Return an UnitTimeEnum client enum, from a server string value
	 * 
	 * @param unitTimeName
	 *          the unit time name
	 * @return the appropriate UnitTimeEnum
	 */
	public static UnitTimeEnum getUnitTimeFromServerStringValue(final String unitTimeName)
	{
		if (unitTimeName.equals(ManagementModuleConstants.UNIT_TIME_MONTH))
		{
			return UnitTimeEnum.UNIT_TIME_MONTH;
		}
		else if (unitTimeName.equals(ManagementModuleConstants.UNIT_TIME_WEEK))
		{
			return UnitTimeEnum.UNIT_TIME_WEEK;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Return a client AccessRight from the server value
	 * 
	 * @param accessRightName
	 *          the server value
	 * @return the AccessRight enum value
	 */
	public static AccessRight getAccessRightFromServerValue(final String accessRightName)
	{
		if (accessRightName.equals(ManagementModuleConstants.ACCESS_RIGHTS_READ))
		{
			return AccessRight.READ;
		}
		else if (accessRightName.equals(ManagementModuleConstants.ACCESS_RIGHTS_WRITE))
		{
			return AccessRight.WRITE;
		}
		return AccessRight.NONE;
	}

	/**
	 * Return a client ProjectPlanStatus from the server value
	 * 
	 * @param functionalId
	 *          the functionalId to use to get client value
	 * @return the client value
	 */
	public static ProjectPlanStatus getProjectPlanStatusFromServerStringValue(final String functionalId)
	{
		if (ManagementModuleConstants.PROJECT_PLAN_DRAFT.equals(functionalId))
		{
			return ProjectPlanStatus.PROJECTPLAN_STATUS_DRAFT;
		}
		else if (ManagementModuleConstants.PROJECT_PLAN_VALIDATE.equals(functionalId))
		{
			return ProjectPlanStatus.PROJECTPLAN_STATUS_VALIDATE;
		}
		throw new IllegalStateException("Project Plan status incorrect");
	}

	/**
	 * Return a client TaskTypeEnum from the server value
	 * 
	 * @param taskType
	 *          the server value
	 * @return the TaskType enum value
	 */
	public static TaskTypeEnum getTaskTypeEnumFromServerValue(final TaskType taskType)
	{
		if (taskType.getFunctionalId().equals(ManagementModuleConstants.TASK_TYPE_BUG))
		{
			return TaskTypeEnum.BUG;
		}
		else if (taskType.getFunctionalId().equals(ManagementModuleConstants.TASK_TYPE_WORK))
		{
			return TaskTypeEnum.WORK;
		}
		else
		{
			throw new IllegalStateException("Unknwown taskType with functionalId : " + taskType.getFunctionalId());
		}
	}

	/**
	 * Return a client TaskStatusEnum from the server value
	 * 
	 * @param taskType
	 *          the server value
	 * @return the TaskStatusEnum enum value
	 */
	public static TaskStatusEnum getTaskStatusEnumFromServerValue(final StatusTask taskStatus)
	{
		if (taskStatus.getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_NOT_AFFECTED))
		{
			return TaskStatusEnum.NOT_AFFECTED;
		}
		else if (taskStatus.getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_NOT_STARTED))
		{
			return TaskStatusEnum.NOT_STARTED;
		}
		else if (taskStatus.getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_IN_PROGRESS))
		{
			return TaskStatusEnum.IN_PROGRESS;
		}
		else if (taskStatus.getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_DONE))
		{
			return TaskStatusEnum.DONE;
		}
		else if (taskStatus.getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_CANCELED))
		{
			return TaskStatusEnum.CANCELED;
		}
		else
		{
			throw new IllegalStateException("Unknwown taskStatus functionalId : " + taskStatus.getFunctionalId());
		}
	}

}
