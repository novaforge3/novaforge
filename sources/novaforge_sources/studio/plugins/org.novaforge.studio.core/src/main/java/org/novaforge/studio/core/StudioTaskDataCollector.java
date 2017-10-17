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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;

/**
 * Novastudio task data collector
 *
 */
public class StudioTaskDataCollector extends TaskDataCollector {

	final Set<TaskData> taskDataCollected = new HashSet<TaskData>();

	String queryTimestamp = null;

	@Override
	public void accept(TaskData taskData) {
		taskDataCollected.add(taskData);
	}

	public Set<TaskData> getTaskData() {
		return taskDataCollected;
	}

	public String getQueryTimestamp() {
		return queryTimestamp;
	}

	public void setQueryTimestamp(String queryTimestamp) {
		this.queryTimestamp = queryTimestamp;
	}
}
