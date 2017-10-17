/**
 * NovaForge(TM) is a web-based forge offering a Collaborative Development and 
 * Project Management Environment.
 *
 * Copyright (C) 2007-2012  BULL SAS
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */


package org.novaforge.studio.core;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;

public interface StudioDataHandler 
{

	/**
	 * Gets the task data for the given task ID.
	 * @param repository
	 * @param taskId
	 * @return
	 * @throws CoreException
	 */
	TaskData getTaskData(TaskRepository repository, String taskId) throws CoreException;

	/**
	 * Gets the tasks list for the given query.
	 * @param repository
	 * @param query
	 * @return
	 * @throws CoreException
	 */
	List<TaskData> getTasks(TaskRepository repository, IRepositoryQuery query) throws CoreException;

	/**
	 * Sets the task modification date from the corresponding task data. 
	 * @param taskData
	 * @param task
	 */
	void setTaskModificationDate(TaskData taskData, ITask task);

	/**
	 * Determines if the modification date of a task has changed by looking into the up to date task data.
	 * @param taskData
	 * @param task
	 * @return
	 */
	boolean hasTaskModificationDateChanged(TaskData taskData, ITask task);
}
