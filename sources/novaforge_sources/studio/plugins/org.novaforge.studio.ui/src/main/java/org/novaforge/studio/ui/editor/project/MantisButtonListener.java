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

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.mylyn.commons.core.StatusHandler;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.internal.tasks.ui.views.TaskRepositoriesView;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.eclipse.mylyn.tasks.ui.wizards.TaskRepositoryWizardDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.novaforge.studio.core.repository.RepositoryUtil;
import org.novaforge.studio.ui.StudioUiPlugin;
import org.novaforge.studio.ui.wizard.NewPrefilledRepositoryWizard;

/**
 * Issues fetching button listener (integrates with mylyn-mantis plugin).
 *
 */
@SuppressWarnings("restriction")
public class MantisButtonListener extends SelectionAdapter 
{
	private final String applicationName;
		
	private final String applicationUrl;
	
	public MantisButtonListener(final String pApplicationName, final String pApplicationUrl)
	{
		applicationName = pApplicationName;
		applicationUrl = pApplicationUrl;
	} 
	
	@SuppressWarnings("deprecation")
	@Override
	public void widgetSelected(SelectionEvent event) 
	{
		TaskRepository mantisRepository = null;
		List<TaskRepository> repositories = TasksUi.getRepositoryManager().getAllRepositories();
		for (TaskRepository repository : repositories)
		{
			if (repository.getConnectorKind().equals(StudioUiPlugin.MANTIS_CONNECTOR_KIND) && repository.getUrl().equals(applicationUrl))
			{
				mantisRepository = repository;
			}
		}
		
		if (mantisRepository == null)
		{
			// launch add task repository wizard (preconfigured url, label, user ID and password)
			TaskRepository projectRepository = TasksUiUtil.getSelectedRepository();
			
			mantisRepository = new TaskRepository(StudioUiPlugin.MANTIS_CONNECTOR_KIND, projectRepository.getRepositoryUrl());
			mantisRepository.setRepositoryLabel(applicationName + " bugs");
			String mantisUrl = RepositoryUtil.getBaseToolUrl(applicationUrl);
			mantisRepository.setRepositoryUrl(mantisUrl);
			mantisRepository.setBugRepository(true);
			mantisRepository.setCredentials(AuthenticationType.REPOSITORY, 
					projectRepository.getCredentials(AuthenticationType.REPOSITORY), 
					projectRepository.getSavePassword(AuthenticationType.REPOSITORY));
			
			
			try 
			{
				NewPrefilledRepositoryWizard wizard = new NewPrefilledRepositoryWizard(mantisRepository.getConnectorKind(), mantisRepository);
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				if (shell != null && !shell.isDisposed()) 
				{
					WizardDialog dialog = new TaskRepositoryWizardDialog(shell, wizard);
					dialog.create();
					dialog.setBlockOnOpen(true);
					dialog.open();
				}

				if (TaskRepositoriesView.getFromActivePerspective() != null) 
				{
					TaskRepositoriesView.getFromActivePerspective().getViewer().refresh();
				}
			}
			catch (Exception e) 
			{
				StatusHandler.fail(new Status(IStatus.ERROR, TasksUiPlugin.ID_PLUGIN, e.getMessage(), e));
			}
		}
		
		// launch create mantis query wizard
		AbstractRepositoryConnectorUi connectorUi = TasksUiPlugin.getConnectorUi(mantisRepository.getConnectorKind());
		IWizard wizard = connectorUi.getQueryWizard(mantisRepository, null);
		((Wizard) wizard).setForcePreviousAndNextButtons(true);
		try 
		{
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			if (shell != null && !shell.isDisposed()) 
			{
				WizardDialog dialog = new WizardDialog(shell, wizard);
				dialog.create();
				dialog.setBlockOnOpen(true);
				dialog.open();
			}
		} 
		catch (Exception e) 
		{
			StatusHandler.fail(new Status(IStatus.ERROR, TasksUiPlugin.ID_PLUGIN, e.getMessage(), e));
		}
	}
}
