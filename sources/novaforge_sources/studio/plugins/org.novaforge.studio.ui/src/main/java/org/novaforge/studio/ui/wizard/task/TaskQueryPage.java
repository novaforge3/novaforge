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


package org.novaforge.studio.ui.wizard.task;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.novaforge.studio.ui.editor.Messages;

/**
 * Query page implementation used by the new query wizard if the task repository
 * type is task.
 *
 */
public class TaskQueryPage extends AbstractRepositoryQueryPage
{
	public TaskQueryPage(String pageName, TaskRepository taskRepository, IRepositoryQuery query) 
	{
		super(pageName, taskRepository, query);
		super.setTitle(Messages.TaskQueryPage_Title);
		super.setDescription(Messages.TaskQueryPage_Description);
	}

	public TaskQueryPage(String pageName, TaskRepository taskRepository) 
	{
		super(pageName, taskRepository);
	}

	public TaskQueryPage(TaskRepository taskRepository) 
	{
		super(null, taskRepository);
	}
	
	@Override
	public void createControl(Composite parent) 
	{
		Composite control = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		control.setLayoutData(gd);
		GridLayout layout = new GridLayout(4, false);
		control.setLayout(layout);
		
		Label titleLabel = new Label(control, SWT.NONE);
		titleLabel.setText(Messages.TaskQueryPage_Content);
		
		Dialog.applyDialogFont(control);
		setControl(control);
	}

	@Override
	public boolean isPageComplete() 
	{
		return false;
	}
	
	@Override
	public void applyTo(IRepositoryQuery repositoryQuery) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getQueryTitle() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IRepositoryQuery createQuery() {
		// TODO Auto-generated method stub
		return super.createQuery();
	}
}
