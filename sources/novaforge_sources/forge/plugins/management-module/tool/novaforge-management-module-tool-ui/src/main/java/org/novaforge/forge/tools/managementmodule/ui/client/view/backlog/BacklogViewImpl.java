/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.client.view.backlog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.ValidateDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;

/**
 * @author BILET-JC
 * 
 */
public class BacklogViewImpl extends ResizeComposite implements BacklogView {

	private static BacklogViewImplUiBinder uiBinder = GWT.create(BacklogViewImplUiBinder.class);
	//popup
	private final ValidateDialogBox validateFinishIteration = new ValidateDialogBox(Common.MESSAGES_BACKLOG
																																											.messageFinishIterationWarning());
	@UiField
	ScrollPanel scrollPanel;
	/* context */
	@UiField
	Panel iterationContextPanel;
	/* list */
	@UiField
	Panel taskListPanel;
	/* buttons action */
	@UiField
	Button createWorkTaskB;
	@UiField
	Button createBugTaskB;
	@UiField
	Button updateTaskB;
	@UiField
	Button followTaskB;
	@UiField
	Button deleteB;
	@UiField
	Button iterationMonitoringButton;
	@UiField
	Button endITB;
	@UiField
	Button exportCSVB;
	@UiField
	Button buttonHomeReturn;

	public BacklogViewImpl() {
		Common.RESOURCE.css().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));
		/* buttons action */
		createWorkTaskB.setText(Common.MESSAGES_BACKLOG.buttonCreateWorkTask());
		createBugTaskB.setText(Common.MESSAGES_BACKLOG.buttonCreateBugTask());
		updateTaskB.setText(Common.MESSAGES_BACKLOG.buttonUpdateTask());
		followTaskB.setText(Common.MESSAGES_BACKLOG.buttonFollowTask());
		deleteB.setText(Common.MESSAGES_BACKLOG.buttonDeleteTask());
		iterationMonitoringButton.setText(Common.MESSAGES_BACKLOG.buttonIterationMonitoring());
		endITB.setText(Common.MESSAGES_BACKLOG.buttonEndIT());
		exportCSVB.setText(Common.GLOBAL.buttonExportCSV());
		buttonHomeReturn.setText(Common.GLOBAL.homeReturn());
		updateTaskB.setEnabled(false);
		followTaskB.setEnabled(false);
		deleteB.setEnabled(false);
	}

	@Override
	public HasWidgets getTasksPanel()
	{
		return taskListPanel;
	}

	@Override
	public HasWidgets getIterationContextPanel()
	{
		return iterationContextPanel;
	}

	@Override
	public Button getCreateBugTaskButton() {
		return createBugTaskB;
	}

	@Override
	public Button getCreateWorkTaskButton() {
		return createWorkTaskB;
	}

	@Override
	public Button getUpdateTaskButton() {
		return updateTaskB;
	}

	@Override
	public Button getReportTaskButton() {
		return followTaskB;
	}

	@Override
	public Button getDeleteButton() {
		return deleteB;
	}

	@Override
	public Button getEndIterationButton() {
		return endITB;
	}

	@Override
	public HasWidgets getContentPanel()
	{
		return scrollPanel;
	}

	@Override
	public Button getButtonHomeReturn()
	{
		return buttonHomeReturn;
	}

	@Override
	public Button getButtonExportCSV() {
		return exportCSVB;
	}

   /**
    * Get the iterationMonitoringButton
    * @return the iterationMonitoringButton
    */
   @Override
   public Button getIterationMonitoringButton() {
      return iterationMonitoringButton;
   }

   /**
    * Get the validateFinishIteration
    * @return the validateFinishIteration
    */
   @Override
   public ValidateDialogBox getValidateFinishIteration() {
      return validateFinishIteration;
   }

	interface BacklogViewImplUiBinder extends UiBinder<Widget, BacklogViewImpl>
	{
	}

}
