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


package org.novaforge.studio.ui.editor.project;

import org.eclipse.mylyn.internal.tasks.core.RepositoryQuery;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.internal.tasks.ui.util.TasksUiInternal;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.novaforge.studio.core.StudioCorePlugin;
import org.novaforge.studio.core.StudioQueryAttribute;
import org.novaforge.studio.core.StudioQueryType;
import org.novaforge.studio.core.StudioRepositoryConnector;
import org.novaforge.studio.core.StudioRepositoryType;
import org.novaforge.studio.core.project.ProjectAttributeMapper;
import org.novaforge.studio.core.repository.RepositoryUtil;
import org.novaforge.studio.ui.editor.Messages;

/**
 * Project tasks fetching button listener. 
 *
 */
@SuppressWarnings("restriction")
public class ProjectTasksButtonListener extends SelectionAdapter 
{
	private final TaskAttribute applicationAttribute;
	
	private final String projectName;
	
	public ProjectTasksButtonListener(TaskAttribute applicationAttribute, String projectName)
	{
		this.applicationAttribute = applicationAttribute;
		this.projectName = projectName;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) 
	{
		TaskAttribute pluginUUIDAttribute = applicationAttribute.getAttribute(ProjectAttributeMapper.APPLICATION_PLUGIN_UUID);
		TaskAttribute instanceIdAttribute = applicationAttribute.getAttribute(ProjectAttributeMapper.APPLICATION_PLUGIN_INSTANCE_ID);
		
		String pluginUUID = null;
		String instanceId = null;

		if (pluginUUIDAttribute != null)
		{
			pluginUUID = pluginUUIDAttribute.getValue();
		}
		
		if (instanceIdAttribute != null)
		{
			instanceId = instanceIdAttribute.getValue();
		}
		
		TaskRepository projectRepository = TasksUiUtil.getSelectedRepository();
		TaskRepository taskRepository = RepositoryUtil.duplicateRepository(projectRepository, Messages.ReporitoryTasks_Name, StudioRepositoryType.TASK);
		TasksUi.getRepositoryManager().addRepository(taskRepository);
		
		IRepositoryQuery query = TasksUi.getRepositoryModel().createRepositoryQuery(taskRepository);
		query.setSummary(projectName + " tasks");
		query.setAttribute(StudioQueryAttribute.QUERY_ATTR_PLUGIN_UUID, pluginUUID);
		query.setAttribute(StudioQueryAttribute.QUERY_ATTR_PLUGIN_INSTANCE_ID, instanceId);
		query.setAttribute(StudioQueryAttribute.QUERY_ATTR_TYPE, StudioQueryType.NONE.toString());
		
		StudioRepositoryConnector connector = (StudioRepositoryConnector) TasksUi.getRepositoryConnector(StudioCorePlugin.CONNECTOR_KIND);
		TasksUiPlugin.getTaskList().notifyElementChanged((RepositoryQuery) query);
		TasksUiInternal.getTaskList().addQuery((RepositoryQuery) query);
		TasksUiInternal.synchronizeQuery(connector, (RepositoryQuery) query, null, true);
	}
}
