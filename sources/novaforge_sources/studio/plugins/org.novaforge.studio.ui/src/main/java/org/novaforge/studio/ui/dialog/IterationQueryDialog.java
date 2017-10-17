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


package org.novaforge.studio.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.novaforge.studio.core.StudioCorePlugin;
import org.novaforge.studio.core.StudioRepositoryConnector;
import org.novaforge.studio.core.StudioRepositoryType;
import org.novaforge.studio.core.repository.RepositoryUtil;
import org.novaforge.studio.ui.editor.Messages;

/**
 * Iteration query building dialog.
 *
 */
public class IterationQueryDialog extends Dialog 
{
	public static final String CURRENT_ITERATION = "Tasks for current iteration";
	
	private IterationQueryData iterationQueryData;
	
	private final String pluginUUID;
	
	private final String instanceId;
	
	public IterationQueryDialog(Shell parent, String pluginUUID, String instanceId)
	{
		super(parent);
		this.pluginUUID = pluginUUID;
		this.instanceId = instanceId;
	}
	
	public IterationQueryDialog(Shell parent, int style, String pluginUUID, String instanceId)
	{
		super(parent, style);
		this.pluginUUID = pluginUUID;
		this.instanceId = instanceId;
	}
	
	public IterationQueryData open()
	{
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.TITLE | SWT.BORDER | SWT.DIALOG_TRIM |  SWT.APPLICATION_MODAL);
		shell.setText(Messages.ProjectEditorPart_IterationDialog_Title);
		GridLayout shellLayout = new GridLayout(2, false);
		shellLayout.verticalSpacing = 10;
		shellLayout.horizontalSpacing = 15;
		shellLayout.marginWidth = 10;
		shell.setLayout(shellLayout);
		
		Label descriptionLabel = new Label(shell, SWT.NONE);
		descriptionLabel.setText(Messages.ProjectEditorPart_IterationDialog_Description);
		GridData descriptionData = new GridData();
		descriptionData.horizontalSpan = 2;
		descriptionLabel.setLayoutData(descriptionData);
		
		Label queryTitleLabel = new Label(shell, SWT.NONE);
		queryTitleLabel.setText(Messages.ProjectEditorPart_IterationDialog_QueryLabel);
		queryTitleLabel.setLayoutData(new GridData());
		
		final Text queryTitleText = new Text(shell, SWT.SINGLE | SWT.BORDER);
		GridData titleData = new GridData();
		titleData.horizontalAlignment = GridData.FILL;
		queryTitleText.setLayoutData(titleData);
		queryTitleText.setText(CURRENT_ITERATION);
		
		Label filterLabel = new Label(shell, SWT.NULL);
		filterLabel.setText(Messages.ProjectEditorPart_IterationDialog_FilterLabel);
		filterLabel.setLayoutData(new GridData());
		
		final Combo filterCombo = new Combo(shell, SWT.READ_ONLY);
		String items[] = getIterationList(shell).toArray(new String[0]);
		filterCombo.setItems(items);
		filterCombo.select(0);
		GridData comboData = new GridData();
		comboData.horizontalAlignment = GridData.FILL;
		filterCombo.setLayoutData(comboData);
		
		Composite buttonsComposite = new Composite(shell, SWT.NONE);
		GridData buttonsData = new GridData();
		buttonsData.horizontalSpan = 2;
		buttonsData.horizontalAlignment = GridData.END;
		buttonsComposite.setLayoutData(buttonsData);
		
		RowLayout buttonsLayout = new RowLayout();
		buttonsLayout.marginRight = 0;
		buttonsLayout.spacing = 5;
		buttonsComposite.setLayout(buttonsLayout);
		
	    Button buttonCancel = new Button(buttonsComposite, SWT.PUSH);
	    buttonCancel.setText(Messages.ProjectEditorPart_IterationDialog_CancelButton);
		Button buttonOK = new Button(buttonsComposite, SWT.PUSH);
	    buttonOK.setText(Messages.ProjectEditorPart_IterationDialog_FinishButton);

	    buttonOK.addListener(SWT.Selection, new Listener() {
	    	public void handleEvent(Event event) {
	    		iterationQueryData = new IterationQueryData(queryTitleText.getText(), filterCombo.getText());
	    		if (StringUtils.equalsIgnoreCase(filterCombo.getText(), CURRENT_ITERATION))
	    		{
	    			iterationQueryData.setFilter(null);
	    		}
	    		shell.dispose();
	    	}
	    });

	    buttonCancel.addListener(SWT.Selection, new Listener() {
	    	public void handleEvent(Event event) {
	    		iterationQueryData = null;
	    		shell.dispose();
	    	}
	    });
	    
		shell.pack();
	    shell.open();

	    Display display = parent.getDisplay();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    
		return iterationQueryData;
	}
	
	private List<String> getIterationList(Shell shell)
	{
		List<String> iterationList = new ArrayList<String>();
		iterationList.add(CURRENT_ITERATION);

		TaskRepository projectRepository = TasksUiUtil.getSelectedRepository();
		TaskRepository taskRepository = RepositoryUtil.duplicateRepository(projectRepository, Messages.ReporitoryTasks_Name, 
				StudioRepositoryType.TASK);
		StudioRepositoryConnector connector = (StudioRepositoryConnector) TasksUi.getRepositoryConnector(StudioCorePlugin.CONNECTOR_KIND);

		try
		{
			List<String> iterations = connector.getIterationList(taskRepository, pluginUUID, instanceId);
			if (iterations!=null && !iterations.isEmpty()){
				iterationList.addAll(iterations);
			}
		}
		catch (CoreException e)
		{
			iterationList.clear();
			MessageDialog.openError(shell, Messages.ProjectEditorPart_IterationDialog_ErrorTitle,
					Messages.ProjectEditorPart_IterationDialog_ErrorBody + e.getMessage());
		}

		return iterationList;
	}
	
}
