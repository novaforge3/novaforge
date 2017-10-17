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


package org.novaforge.studio.ui.editor;

import org.eclipse.osgi.util.NLS;

/**
 * Messages bundle used by the UI (the data are fetched from the message.properties file)
 *
 */
public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.novaforge.studio.ui.editor.messages";

	static {
		// load message values from bundle file
		reloadMessages();
	}

	public static void reloadMessages() {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String ReporitoryTasks_Name;
	
	public static String ProjectEditorPart_Name;
	public static String ProjectEditorPart_ProjectTasksButton;
	public static String ProjectEditorPart_IterationTasksButton;
	public static String ProjectEditorPart_MantisButton;
	public static String ProjectEditorPart_IterationDialog_Title;
	public static String ProjectEditorPart_IterationDialog_Description;
	public static String ProjectEditorPart_IterationDialog_QueryLabel;
	public static String ProjectEditorPart_IterationDialog_FilterLabel;
	public static String ProjectEditorPart_IterationDialog_CancelButton;
	public static String ProjectEditorPart_IterationDialog_FinishButton;
	public static String ProjectEditorPart_IterationDialog_ErrorTitle;
	public static String ProjectEditorPart_IterationDialog_ErrorBody; 

	public static String TaskEditorPart_Name;
	public static String TaskEditorPart_TaskSection;
	public static String TaskEditorPart_TaskTypeLabel;
	public static String TaskEditorPart_TaskCategoryLabel;
	public static String TaskEditorPart_TaskIterationLabel;
	public static String TaskEditorPart_TaskDisciplineLabel;
	public static String TaskEditorPart_TaskDueDateLabel;
	public static String TaskEditorPart_TaskLastUpdateDateLabel;
	public static String TaskEditorPart_TaskEstimateLabel;
	public static String TaskEditorPart_TaskStartDateLabel;
	public static String TaskEditorPart_TaskTimeSpentLabel;
	public static String TaskEditorPart_TaskScopeUnitLabel;
	public static String TaskEditorPart_TaskToDoLabel;
	public static String TaskEditorPart_TaskStatusLabel;
	public static String TaskEditorPart_TaskCommentLabel;
	public static String TaskEditorPart_TaskUpdateButton;
	public static String TaskEditorPart_TaskCloseButton;
	public static String TaskEditorPart_UpdateTaskMessageDialogSuccessTitle;
	public static String TaskEditorPart_UpdateTaskMessageDialogSuccessBody;
	public static String TaskEditorPart_UpdateTaskMessageDialogErrorTitle;
	public static String TaskEditorPart_UpdateTaskMessageDialogErrorBody;
	public static String TaskEditorPart_CloseTaskMessageDialogSuccessTitle;
	public static String TaskEditorPart_CloseTaskMessageDialogSuccessBody;
	public static String TaskEditorPart_CloseTaskMessageDialogErrorTitle;
	public static String TaskEditorPart_CloseTaskMessageDialogErrorBody; 
	public static String TaskEditorPart_CloseTaskMessageDialogCancelledTitle;
	public static String TaskEditorPart_CloseTaskMessageDialogCancelledBody;

	public static String TaskEditorPart_IssueSection;
	public static String TaskEditorPart_IssueTrackerIdLabel;
	public static String TaskEditorPart_IssueCategoryLabel;
	public static String TaskEditorPart_IssueReporterLabel;
	public static String TaskEditorPart_IssueAssignedToLabel;
	public static String TaskEditorPart_IssuePriorityLabel;
	public static String TaskEditorPart_IssueSeverityLabel;
	public static String TaskEditorPart_IssueReproducibilityLabel;
	public static String TaskEditorPart_IssueStatusLabel;
	public static String TaskEditorPart_IssueResolutionLabel;
	public static String TaskEditorPart_IssueProductVersionLabel;
	public static String TaskEditorPart_IssueTitleLabel;
	public static String TaskEditorPart_IssueDescriptionLabel;
	public static String TaskEditorPart_IssueAdditionalInfoLabel;

	
	public static String ProjectQueryPage_Name;
	public static String ProjectQueryPage_Title;
	public static String ProjectQueryPage_Description;
	public static String ProjectQueryPage_Content;
	
	public static String TaskQueryPage_Name;
	public static String TaskQueryPage_Title;
	public static String TaskQueryPage_Description;
	public static String TaskQueryPage_Content;
			
	public static String ProjectsQuery_Summary;

	public static String ProjectTask_KindLabel;
	
	public static String ProjectRepositorySettingsPage_Title;
	public static String ProjectRepositorySettingsPage_Description;
	public static String ProjectRepositorySettingsPage_UserLabel;
	public static String ProjectRepositorySettingsPage_PasswordLabel;
	
	public static String TaskEditorPart_TaskCloseConfirmTitle;
	public static String TaskEditorPart_TaskCloseConfirmBody;
	public static String TaskEditorPart_TaskCloseCancelled;
}
