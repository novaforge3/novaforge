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
package org.novaforge.forge.remote.services.management;

import org.novaforge.forge.remote.services.core.RemoteService;
import org.novaforge.forge.remote.services.exception.RemoteServiceException;
import org.novaforge.forge.remote.services.model.management.Iteration;
import org.novaforge.forge.remote.services.model.management.Task;
import org.novaforge.forge.remote.services.model.management.TaskInfo;
import org.novaforge.forge.remote.services.model.management.TaskStatus;
import org.novaforge.forge.remote.services.model.management.TaskType;

import javax.jws.WebService;
import java.util.Set;

/**
 * Interface containing all the functional services specific to this category.
 * 
 * @author rols-p
 */
@WebService
public interface RemoteManagementService extends RemoteService
{

	/**
	 * ServiceName used to provide a web service uri.
	 */
	String REMOTE_MANAGEMENT_SERVICE_NAME = "RemoteManagementService";

	/**
	 * Returns the task assigned to the connected user for a management application identified by it's
	 * instanceId and for a given task id
	 * 
	 * @param pluginUUID
	 *          Unique ID of the Plugin
	 * @param instanceId
	 *          the appication instance Id
	 * @param taskId
	 *          the unique task Id
	 * @return the appropriates tasks info
	 * @throws RemoteServiceException
	 *           if the task can not be found for this id.
	 */
	Task getTask(String pluginUUID, String instanceId, String taskId) throws RemoteServiceException;

	/**
	 * Returns the task info list assigned to the connected user for a management application identified by
	 * it's instanceId.
	 * 
	 * @param pluginUUID
	 *          Unique ID of the Plugin
	 * @param instanceId
	 *          the appication instance Id
	 * @return the appropriates tasks info
	 * @throws RemoteServiceException
	 */
	Set<TaskInfo> getTaskList(String pluginUUID, String instanceId) throws RemoteServiceException;

	/**
	 * Returns the task info list assigned to the connected user for a management application identified by
	 * it's instanceId and for in current iteration
	 * 
	 * @param pluginUUID
	 *          Unique ID of the Plugin
	 * @param instanceId
	 *          the appication instance Id
	 * @return the appropriates tasks info
	 * @throws RemoteServiceException
	 */
	Set<TaskInfo> getTaskListOfCurrentIteration(String pluginUUID, String instanceId)
	    throws RemoteServiceException;

	/**
	 * Returns the iterations which contains at least one task available in a management application identified
	 * by it's instanceId.
	 * 
	 * @param pluginUUID
	 *          Unique ID of the Plugin
	 * @param instanceId
	 *          the appication instance Id
	 * @return the appropriates iterations
	 * @throws RemoteServiceException
	 */
	Set<Iteration> getIterationList(String pluginUUID, String instanceId) throws RemoteServiceException;

	/**
	 * Returns the task info list assigned to the connected user for a management application identified by
	 * it's instanceId and for a given iteration.
	 * 
	 * @param pluginUUID
	 *          Unique ID of the Plugin
	 * @param instanceId
	 *          the appication instance Id
	 * @param iterationId
	 *          the iteration Id
	 * @return the appropriates tasks info
	 * @throws RemoteServiceException
	 */
	Set<TaskInfo> getTaskListByIteration(String pluginUUID, String instanceId, String iterationId)
	    throws RemoteServiceException;

	/**
	 * Returns the task info list assigned to the connected user for a management application identified by
	 * it's instanceId and for a given TaskType.
	 * 
	 * @param pluginUUID
	 *          Unique ID of the Plugin
	 * @param instanceId
	 *          the appication instance Id
	 * @param taskType
	 *          the task type to use
	 * @return the appropriates tasks info
	 * @throws RemoteServiceException
	 */
	Set<TaskInfo> getTaskListByType(String pluginUUID, String instanceId, TaskType taskType)
	    throws RemoteServiceException;

	/**
	 * Returns the task info list assigned to the connected user for a management application identified by
	 * it's instanceId and for a given TaskStatus.
	 * 
	 * @param pluginUUID
	 *          Unique ID of the Plugin
	 * @param instanceId
	 *          the appication instance Id
	 * @param taskStatus
	 *          the task status to use
	 * @return the appropriate tasks info
	 * @throws RemoteServiceException
	 */
	Set<TaskInfo> getTaskListByStatus(String pluginUUID, String instanceId, TaskStatus taskStatus)
	    throws RemoteServiceException;

	/**
	 * Modify the task passed in argument.
	 * 
	 * @param pluginUUID
	 *          Unique ID of the Plugin
	 * @param instanceId
	 *          the appication instance Id
	 * @param taskToModify
	 *          the task to modify
	 * @throws RemoteServiceException
	 */
	void modifyTask(String pluginUUID, String instanceId, Task taskToModify) throws RemoteServiceException;

	/**
	 * Close the task identified by the argument.
	 * 
	 * @param pluginUUID
	 *          Unique ID of the Plugin
	 * @param instanceId
	 *          the appication instance Id
	 * @param taskId
	 *          the identifier of the task to close
	 * @throws RemoteServiceException
	 */
	void closeTask(String pluginUUID, String instanceId, String taskId) throws RemoteServiceException;
}
