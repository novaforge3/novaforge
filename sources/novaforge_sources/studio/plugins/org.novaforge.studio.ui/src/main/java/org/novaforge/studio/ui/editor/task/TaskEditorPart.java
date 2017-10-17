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


package org.novaforge.studio.ui.editor.task;


import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.novaforge.studio.core.StudioCorePlugin;
import org.novaforge.studio.core.StudioRepositoryConnector;
import org.novaforge.studio.core.task.StudioTaskAttributeMapper;
import org.novaforge.studio.ui.AbstractEditorPart;
import org.novaforge.studio.ui.editor.Messages;

/**
 * Management task editor.
 *
 */
public class TaskEditorPart extends AbstractEditorPart {

	private ScrolledForm form;
	
	public TaskEditorPart(String spaceName) {
		setPartName(spaceName);
	}
	
	@Override
	public void createControl(Composite parent, FormToolkit toolkit) {
		
		form = toolkit.createScrolledForm(parent);
		TableWrapLayout layout = new TableWrapLayout();
		layout.verticalSpacing = 10;
		form.getBody().setLayout(layout);
		
		String taskId = getTaskData().getTaskId();
		String taskTitle = getAttributeValue(getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_TITLE);
		Label titleLabel = createHeaderLabel(toolkit, form.getBody(), taskId + ": " + taskTitle); 
		TableWrapData td = new TableWrapData();
		td.maxWidth = 500;
		titleLabel.setLayoutData(td);
		
		String taskDescription = getAttributeValue(getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_DESCRIPTION);
		Label descriptionLabel = toolkit.createLabel(form.getBody(), taskDescription, SWT.TITLE | SWT.WRAP);
		td = new TableWrapData();
		td.maxWidth = 500;
		descriptionLabel.setLayoutData(td);

		createTaskSection(toolkit, form.getBody());
		
		TaskAttribute issueId = getTaskData().getRoot().getAttribute(StudioTaskAttributeMapper.ISSUE_TRACKER_ID);
		if (issueId != null && StringUtils.isNotEmpty(issueId.getValue()))
		{
			createIssueSection(toolkit, form.getBody());
		}
	}
	
	private void createTaskSection(FormToolkit toolkit, Composite parent)
	{
		Section taskSection = toolkit.createSection(parent, SWT.NONE);
		taskSection.setText(Messages.TaskEditorPart_TaskSection);
		TableWrapData td = new TableWrapData();
		td.align = TableWrapData.FILL;
		taskSection.setLayoutData(td);
		
		Composite separator = toolkit.createCompositeSeparator(taskSection);
		td = new TableWrapData();
		td.align = TableWrapData.FILL;
		separator.setLayoutData(td);
		
		Composite sectionClient = toolkit.createComposite(taskSection);
		td = new TableWrapData();
		td.align = TableWrapData.FILL;
		sectionClient.setLayoutData(td);
		GridLayout clientLayout = new GridLayout(4, false);
		clientLayout.horizontalSpacing = 30;
		sectionClient.setLayout(clientLayout);
		
		boolean isTaskEditable = Boolean.parseBoolean(getAttributeValue(getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_EDITABLE));
		
		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_TaskTypeLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_TYPE);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_TaskCategoryLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_CATEGORY);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_TaskIterationLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_ITERATION);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_TaskDisciplineLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_DISCIPLINE);
		
		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_TaskDueDateLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_END_DATE);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_TaskLastUpdateDateLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_LAST_UPDATE_DATE);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_TaskEstimateLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_INITIAL_ESTIMATION);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_TaskStartDateLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_START_DATE);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_TaskTimeSpentLabel);
		Text consumedTimeText = createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_CONSUMED_TIME);
		consumedTimeText.setTextLimit(6);
		consumedTimeText.setEditable(isTaskEditable);
		consumedTimeText.setEnabled(isTaskEditable);
		
		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_TaskScopeUnitLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_SCOPE_UNIT);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_TaskToDoLabel);
		Text remainingTimeText = createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_REMAINING_TIME);
		remainingTimeText.setTextLimit(6);
		remainingTimeText.setEditable(isTaskEditable);
		remainingTimeText.setEnabled(isTaskEditable);
		
		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_TaskStatusLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_STATUS);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_TaskCommentLabel);
		String commentValue = getAttributeValue(getTaskData().getRoot(), StudioTaskAttributeMapper.TASK_COMMENT);
		Text commentText = toolkit.createText(sectionClient, commentValue, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		commentText.setEditable(false);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false);
		gridData.horizontalSpan = 3;
		gridData.widthHint = 100;
		gridData.heightHint = 100;
		commentText.setLayoutData(gridData);

		Composite buttonsComposite = toolkit.createComposite(sectionClient);
		GridData buttonsData = new GridData(GridData.END, GridData.BEGINNING, false, false);
		buttonsData.horizontalSpan = 4;
		buttonsComposite.setLayoutData(buttonsData);
		RowLayout buttonsLayout = new RowLayout();
		buttonsLayout.marginRight = 0;
		buttonsLayout.spacing = 5;
		buttonsComposite.setLayout(buttonsLayout);
		
		Button closeTaskButton = toolkit.createButton(buttonsComposite, Messages.TaskEditorPart_TaskCloseButton, SWT.PUSH);
		closeTaskButton.setVisible(true);
		closeTaskButton.addSelectionListener(new CloseTaskListener());
		closeTaskButton.setEnabled(isTaskEditable);
		
		Button updateTaskButton = toolkit.createButton(buttonsComposite, Messages.TaskEditorPart_TaskUpdateButton, SWT.PUSH);
		updateTaskButton.setVisible(true);
		updateTaskButton.addSelectionListener(new ApplyTaskListener(consumedTimeText, remainingTimeText));
		updateTaskButton.setEnabled(isTaskEditable);
		
		taskSection.setClient(sectionClient);
	}
	
	private void createIssueSection(FormToolkit toolkit, Composite parent)
	{
		Section issueSection = toolkit.createSection(parent, SWT.NONE);
		issueSection.setText(Messages.TaskEditorPart_IssueSection);
		toolkit.createCompositeSeparator(issueSection);
		
		Composite sectionClient = toolkit.createComposite(issueSection);
		GridLayout clientLayout = new GridLayout(4, false);
		clientLayout.horizontalSpacing = 30;
		sectionClient.setLayout(clientLayout);
		
		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_IssueTrackerIdLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.ISSUE_TRACKER_ID);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_IssueCategoryLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.ISSUE_CATEGORY);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_IssueReporterLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.ISSUE_REPORTER);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_IssueAssignedToLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.ISSUE_ASSIGNED_TO);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_IssuePriorityLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.ISSUE_PRIORITY);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_IssueSeverityLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.ISSUE_SEVERITY);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_IssueReproducibilityLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.ISSUE_REPRODUCIBILITY);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_IssueStatusLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.ISSUE_STATUS);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_IssueResolutionLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.ISSUE_RESOLUTION);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_IssueProductVersionLabel);
		createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.ISSUE_PRODUCT_VERSION);

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_IssueTitleLabel);
		Text titleText = createText(toolkit, sectionClient, getTaskData().getRoot(), StudioTaskAttributeMapper.ISSUE_TITLE);
		GridData titleData = (GridData) titleText.getLayoutData();
		titleData.horizontalSpan = 3;

		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_IssueDescriptionLabel);
		String descriptionValue = getAttributeValue(getTaskData().getRoot(), StudioTaskAttributeMapper.ISSUE_DESCRIPTION);
		Text descriptionText = toolkit.createText(sectionClient, descriptionValue, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		descriptionText.setEditable(false);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false);
		gridData.horizontalSpan = 3;
		gridData.widthHint = 100;
		gridData.heightHint = 100;
		descriptionText.setLayoutData(gridData);
		
		createLabel(toolkit, sectionClient, Messages.TaskEditorPart_IssueAdditionalInfoLabel);
		String additionalInfoValue = getAttributeValue(getTaskData().getRoot(), StudioTaskAttributeMapper.ISSUE_ADDITIONAL_INFO);
		Text additionalInfoText = toolkit.createText(sectionClient, additionalInfoValue, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		additionalInfoText.setEditable(false);
		gridData = new GridData(GridData.FILL, GridData.FILL, false, false);
		gridData.horizontalSpan = 3;
		gridData.widthHint = 100;
		gridData.heightHint = 100;
		additionalInfoText.setLayoutData(gridData);

		issueSection.setClient(sectionClient);
	}
	
	private Label createHeaderLabel(FormToolkit toolkit, Composite parent, String labelValue)
	{
		Label label = toolkit.createLabel(parent, labelValue);
		label.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		Display display = Display.getCurrent();
		FontData fontData = label.getFont().getFontData()[0];
		Font bold = new Font(display, new FontData(fontData.getName(), fontData.getHeight()+2, SWT.BOLD));
		label.setFont(bold);
		return label;
	}
	
	private Label createLabel(FormToolkit toolkit, Composite parent, String labelValue)
	{
		Label label = toolkit.createLabel(parent, labelValue);
		label.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, true, false));
		Display display = Display.getCurrent();
		FontData fontData = label.getFont().getFontData()[0];
		Font bold = new Font(display, new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		label.setFont(bold);
		return label;
	}
	
	private Text createText(FormToolkit toolkit, Composite parentComposite, TaskAttribute parentAttribute, String attributeId)
	{
		TaskAttribute attribute = parentAttribute.getAttribute(attributeId);
		String textValue = getAttributeValue(attribute);
		
		boolean editable = !attribute.getMetaData().isReadOnly();
		
		Text text = null;
		if (editable)
		{
			text = toolkit.createText(parentComposite, textValue, SWT.BORDER);
			text.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		}
		else 
		{
			text = toolkit.createText(parentComposite, textValue);
			text.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		}
		text.setEditable(!attribute.getMetaData().isReadOnly());
		
		return text;
	}
	
	private class ApplyTaskListener extends SelectionAdapter
	{
		private final Text consumedTimeText;
		
		private final Text remainingTimeText;
		
		public ApplyTaskListener(Text consumedTimeText, Text remainingTimeText)
		{
			this.consumedTimeText = consumedTimeText;
			this.remainingTimeText = remainingTimeText;
		}
		
		@Override
		public void widgetSelected(SelectionEvent event) 
		{
			TaskRepository taskRepository = getModel().getTaskRepository();
			StudioRepositoryConnector connector = (StudioRepositoryConnector) TasksUi.getRepositoryConnector(StudioCorePlugin.CONNECTOR_KIND);
			
			try
			{
				connector.modifyTask(taskRepository, getModel().getTaskData(), consumedTimeText.getText(), remainingTimeText.getText());
			}
			catch (CoreException e)
			{
				MessageDialog.openError(form.getShell(), Messages.TaskEditorPart_UpdateTaskMessageDialogErrorTitle,
						Messages.TaskEditorPart_UpdateTaskMessageDialogErrorBody + e.getMessage());
			}
			MessageDialog.openInformation(form.getShell(), Messages.TaskEditorPart_UpdateTaskMessageDialogSuccessTitle,
					Messages.TaskEditorPart_UpdateTaskMessageDialogSuccessBody);
		}
	}
	
	private class CloseTaskListener extends SelectionAdapter
	{
		@Override
		public void widgetSelected(SelectionEvent event) 
		{
			TaskRepository taskRepository = getModel().getTaskRepository();
			StudioRepositoryConnector connector = (StudioRepositoryConnector) TasksUi.getRepositoryConnector(StudioCorePlugin.CONNECTOR_KIND);
			
			try
			{
				boolean close = 
						MessageDialog.openConfirm(form.getShell(), Messages.TaskEditorPart_TaskCloseConfirmTitle, Messages.TaskEditorPart_TaskCloseConfirmBody);
				if (close){
					connector.closeTask(taskRepository, getModel().getTaskData());
					MessageDialog.openInformation(form.getShell(), Messages.TaskEditorPart_CloseTaskMessageDialogSuccessTitle,
							Messages.TaskEditorPart_CloseTaskMessageDialogSuccessBody);
				} else {
					MessageDialog.openInformation(form.getShell(), Messages.TaskEditorPart_CloseTaskMessageDialogCancelledTitle,
							Messages.TaskEditorPart_CloseTaskMessageDialogCancelledBody);
				}
			}
			catch (CoreException e)
			{
				MessageDialog.openError(form.getShell(), Messages.TaskEditorPart_CloseTaskMessageDialogErrorTitle,
						Messages.TaskEditorPart_CloseTaskMessageDialogErrorBody + e.getMessage());
			}
		}
	}
}