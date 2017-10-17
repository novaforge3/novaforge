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


package org.novaforge.studio.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi;
import org.eclipse.mylyn.tasks.ui.LegendElement;
import org.eclipse.mylyn.tasks.ui.wizards.ITaskRepositoryPage;
import org.eclipse.mylyn.tasks.ui.wizards.RepositoryQueryWizard;
import org.novaforge.studio.core.StudioCorePlugin;
import org.novaforge.studio.core.StudioRepositoryType;
import org.novaforge.studio.core.StudioTaskType;
import org.novaforge.studio.ui.editor.Messages;
import org.novaforge.studio.ui.wizard.StudioRepositorySettingsPage;
import org.novaforge.studio.ui.wizard.project.ProjectQueryPage;
import org.novaforge.studio.ui.wizard.task.TaskQueryPage;

/**
 * Novastudio UI connector.
 *
 */
public class StudioConnectorUi extends AbstractRepositoryConnectorUi 
{

	public StudioConnectorUi() 
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getConnectorKind() 
	{
		return StudioCorePlugin.CONNECTOR_KIND;
	}

	@Override
	public ITaskRepositoryPage getSettingsPage(TaskRepository taskRepository) 
	{
		return new StudioRepositorySettingsPage(taskRepository);
	}
	
	@Override
	public IWizard getQueryWizard(TaskRepository taskRepository, IRepositoryQuery queryToEdit) 
	{
		RepositoryQueryWizard wizard = new RepositoryQueryWizard(taskRepository);
		
		String repositoryTypeProperty = taskRepository.getProperty(StudioCorePlugin.REPOSITORY_PROP_TYPE);
        StudioRepositoryType repositoryType = StudioRepositoryType.valueOf(repositoryTypeProperty);

        switch (repositoryType)
        {
        	case PROJECT:
        		wizard.addPage(new ProjectQueryPage(Messages.ProjectQueryPage_Name, taskRepository, queryToEdit));
        		break;
        	case TASK:
        		wizard.addPage(new TaskQueryPage(Messages.TaskQueryPage_Name, taskRepository, queryToEdit));
        		break;
        }
		
		return wizard;
	}
	
	@Override
	public IWizard getNewTaskWizard(TaskRepository taskRepository, ITaskMapping selection) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasSearchPage() 
	{
		return false;
	}
	
	@Override
	public String getTaskKindLabel(ITask repositoryTask) 
	{
		return repositoryTask.getTaskKind();
	}

	@Override
	public List<LegendElement> getLegendElements() 
	{
		List<LegendElement> legendItems = new ArrayList<LegendElement>();
		legendItems.add(LegendElement.createTask(StudioTaskType.PROJECT.value(), null));
		legendItems.add(LegendElement.createTask(StudioTaskType.TASK.value(), null));
		legendItems.add(LegendElement.createTask(StudioTaskType.BUG.value(), StudioImages.OVERLAY_BUG));
		return legendItems;
	}

	@Override
	public ImageDescriptor getTaskKindOverlay(ITask task) 
	{
		StudioTaskType type = StudioTaskType.fromValue(task.getTaskKind());
		if (type != null && type == StudioTaskType.BUG)
		{
			return StudioImages.OVERLAY_BUG;
		}
		return super.getTaskKindOverlay(task);
	}
	
	
}
