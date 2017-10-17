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
package org.novaforge.forge.core.plugins.categories.management;

import org.novaforge.forge.core.plugins.categories.PluginCategoryService;

import java.util.Set;

/**
 * Interface containing all the functional services specific to this category.
 * 
 * @author rols-p
 */
public interface ManagementCategoryService extends PluginCategoryService
{

  /**
   * Returns the task for a user and a management application identified by the couple forgeId + instanceId
   * and for a given task id.
   * 
   * @param login
   *          the login of the user
   * @param instanceId
   *          the instance Id of the application
   * @param forgeId
   *          the Id of the forge
   * @param taskId
   *          the unique id of the task.
   * @return the appropriates task in function of it's id.
   * @throws ManagementServiceException
   *           in case of error of if the Task is not found.
   */
  TaskBean getTask(String login, String instanceId, String forgeId, String taskId)
      throws ManagementServiceException;

  /**
   * Returns the task info list for a user and a management application identified by the couple forgeId +
   * instanceId.
   * 
   * @param login
   *          the login of the user
   * @param instanceId
   *          the instance Id of the application
   * @param forgeId
   *          the Id of the forge
   * @return the appropriates tasks info
   * @throws ManagementServiceException
   *           in case of error.
   */
  Set<TaskInfoBean> getTaskList(String login, String instanceId, String forgeId)
      throws ManagementServiceException;

  /**
   * Returns the tasks info which are in current iteration of a user and a management application identified
   * by the couple forgeId + instanceId.
   * 
   * @param login
   *          the login of the user
   * @param instanceId
   *          the instance Id of the application
   * @param forgeId
   *          the Id of the forge
   * @return the appropriates tasks info
   * @throws ManagementServiceException
   *           in case of error.
   */
  Set<TaskInfoBean> getTaskListOfCurrentIteration(String login, String instanceId, String forgeId)
      throws ManagementServiceException;

  /**
   * Returns the iterations for a user and a management application identified by the couple forgeId +
   * instanceId which contains at least one task.
   * 
   * @param login
   *          the login of the user
   * @param instanceId
   *          the instance Id of the application
   * @param forgeId
   *          the Id of the forge
   * @return the appropriates iterations
   * @throws ManagementServiceException
   *           in case of error.
   */
  Set<IterationBean> getIterationList(String login, String instanceId, String forgeId)
      throws ManagementServiceException;

  /**
   * Returns the task info list for a user, and a management application identified by the couple forgeId +
   * instanceId and an iteration.
   * 
   * @param login
   *          the login of the user
   * @param instanceId
   *          the instance Id of the application
   * @param forgeId
   *          the Id of the forge
   * @param iterationId
   *          the iteration Id
   * @return the appropriates tasks info
   * @throws ManagementServiceException
   *           in case of error.
   */
  Set<TaskInfoBean> getTaskListByIteration(String login, String instanceId, String forgeId, String iterationId)
      throws ManagementServiceException;

  /**
   * Returns the task info list for a user, and a management application identified by the couple forgeId +
   * instanceId and a task type.
   * 
   * @param login
   *          the login of the user
   * @param instanceId
   *          the instance Id of the application
   * @param forgeId
   *          the Id of the forge
   * @param taskType
   *          the task type to use
   * @return the appropriates tasks info
   * @throws ManagementServiceException
   *           in case of error.
   */
  Set<TaskInfoBean> getTaskListByType(String login, String instanceId, String forgeId, TaskTypeEnum taskType)
      throws ManagementServiceException;

  /**
   * Returns the task info list for a user, and a management application identified by the couple forgeId +
   * instanceId and a task status.
   * 
   * @param login
   *          the login of the user
   * @param instanceId
   *          the instance Id of the application
   * @param forgeId
   *          the Id of the forge
   * @param taskStatus
   *          the task status to use
   * @return the appropriate tasks info
   * @throws ManagementServiceException
   *           in case of error.
   */
  Set<TaskInfoBean> getTaskListByStatus(String login, String instanceId, String forgeId,
      TaskStatusEnum taskStatus) throws ManagementServiceException;

  /**
   * Modify the task passed in argument.
   * 
   * @param login
   *          the login of the user
   * @param instanceId
   *          the instance Id of the application
   * @param forgeId
   *          the Id of the forge
   * @param taskToModify
   *          the task to modify
   * @throws ManagementServiceException
   *           in case of error.
   */
  void modifyTask(String login, String instanceId, String forgeId, TaskBean taskToModify)
      throws ManagementServiceException;

  /**
   * Close the task identified by the argument.
   * 
   * @param login
   *          the login of the user
   * @param instanceId
   *          the instance Id of the application
   * @param forgeId
   *          the Id of the forge
   * @param taskId
   *          the identifier of the task to close
   * @throws ManagementServiceException
   *           in case of error.
   */
  void closeTask(String login, String instanceId, String forgeId, String taskId)
      throws ManagementServiceException;
}
