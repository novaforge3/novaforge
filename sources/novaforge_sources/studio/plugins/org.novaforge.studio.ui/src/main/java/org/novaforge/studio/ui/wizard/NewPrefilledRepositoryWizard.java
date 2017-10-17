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


package org.novaforge.studio.ui.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.internal.tasks.ui.actions.AddRepositoryAction;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.TasksUiImages;
import org.eclipse.mylyn.tasks.ui.wizards.ITaskRepositoryPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Specific implementation of the new repository wizard that can take a task
 * repository in parameter to pre-fill some fields in the wizard such as the 
 * repository url, the user ID, the user password etc... 
 * It is used when contacting the mylyn-mantis connector to create a new mantis
 * task repository.
 *
 */
@SuppressWarnings("restriction")
public class NewPrefilledRepositoryWizard extends Wizard implements INewWizard 
{
	private AbstractRepositoryConnector connector;

	private final String connectorKind;

	private final TaskRepository modelRepository;
	
	private TaskRepository taskRepository;

	private ITaskRepositoryPage settingsPage;

	private String lastConnectorKind;

	public NewPrefilledRepositoryWizard(String connectorKind, TaskRepository modelRepository) {
		this.connectorKind = connectorKind;
		this.modelRepository = modelRepository;
		setDefaultPageImageDescriptor(TasksUiImages.BANNER_REPOSITORY);
		setForcePreviousAndNextButtons(connectorKind == null);
		setNeedsProgressMonitor(true);
		setWindowTitle(AddRepositoryAction.TITLE);
	}

	@Override
	public void addPages() 
	{
		connector = TasksUi.getRepositoryManager().getRepositoryConnector(connectorKind);
		updateSettingsPage();
		addPage(settingsPage);
	}

	@Override
	public boolean canFinish() 
	{
		return settingsPage != null && settingsPage.isPageComplete();
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) 
	{
		return super.getNextPage(page);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) 
	{
	}

	@Override
	public boolean performFinish() 
	{
		if (canFinish()) 
		{
			taskRepository = new TaskRepository(connector.getConnectorKind(), settingsPage.getRepositoryUrl());
			settingsPage.applyTo(taskRepository);
			TasksUi.getRepositoryManager().addRepository(taskRepository);
			return true;
		}
		return false;
	}

	public TaskRepository getTaskRepository() 
	{
		return taskRepository;
	}

	private void updateSettingsPage() 
	{
		assert connector != null;
		if (!connector.getConnectorKind().equals(lastConnectorKind)) 
		{
			AbstractRepositoryConnectorUi connectorUi = TasksUiPlugin.getConnectorUi(connector.getConnectorKind());
			settingsPage = connectorUi.getSettingsPage(modelRepository);
			settingsPage.setWizard(this);
			lastConnectorKind = connector.getConnectorKind();
		}
	}
}
