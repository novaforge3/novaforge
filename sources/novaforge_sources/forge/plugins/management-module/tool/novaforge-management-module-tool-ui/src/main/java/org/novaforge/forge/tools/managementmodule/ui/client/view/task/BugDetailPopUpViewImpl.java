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
package org.novaforge.forge.tools.managementmodule.ui.client.view.task;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;

/**
 * Implementation of the pop up which display the info of a bug
 */
public class BugDetailPopUpViewImpl extends PopupPanel implements BugDetailPopUpView {

   private static BugDetailPopUpViewImplUiBinder uiBinder = GWT.create(BugDetailPopUpViewImplUiBinder.class);
   @UiField
   Grid mainGrid;
   @UiField
   Label titleLabel;
   @UiField
   Label labelBugTrackerId;
   @UiField
   TextBox bugTrackerIdTB;
   @UiField
   Label labelTitle;
   @UiField
   TextBox titleTB;
   @UiField
   Label labelAssignedTo;
   @UiField
   TextBox assignedToTB;
   @UiField
   Label labelStatus;
   @UiField
   TextBox statusTB;
   @UiField
   Label labelSeverity;
   @UiField
   TextBox severityTB;
   @UiField
   Label labelReporter;
   @UiField
   TextBox reporterTB;
   @UiField
   Label labelPriority;
   @UiField
   TextBox priorityTB;
   @UiField
   Label labelCategory;
   @UiField
   TextBox categoryTB;
   @UiField
   Button closeButton;
   /**
    * Constructor
    */
   public BugDetailPopUpViewImpl() {
      //this.setWidth("600px");
      add(uiBinder.createAndBindUi(this));
      mainGrid.setCellPadding(5);
      mainGrid.getCellFormatter().setWidth(0, 0, "140px");
      mainGrid.getCellFormatter().setWidth(0, 1, "450px");
      titleLabel.setText(Common.MESSAGES_TASK.buttonInfos());
      labelAssignedTo.setText(Common.MESSAGES_TASK.bugAffectedUser());
      assignedToTB.setWidth("450px");
      labelBugTrackerId.setText(Common.MESSAGES_TASK.bugTrackerId());
      bugTrackerIdTB.setWidth("450px");
      labelCategory.setText(Common.MESSAGES_TASK.category());
      categoryTB.setWidth("450px");
      labelPriority.setText(Common.MESSAGES_TASK.bugPriority());
      priorityTB.setWidth("450px");
      labelReporter.setText(Common.MESSAGES_TASK.bugReporter());
      reporterTB.setWidth("450px");
      labelSeverity.setText(Common.MESSAGES_TASK.bugSeverity());
      severityTB.setWidth("450px");
      labelStatus.setText(Common.MESSAGES_TASK.bugStatus());
      statusTB.setWidth("450px");
      labelTitle.setText(Common.MESSAGES_TASK.bugTitle());
      titleTB.setWidth("450px");
      closeButton.setText(Common.GLOBAL.buttonClose());
      setGlassEnabled(true);
   }
   
   /**
    * Get the titleTB
    * @return the titleTB
    */
   @Override
   public TextBox getTitleTB() {
      return titleTB;
   }

   /**
    * Get the assignedToTB
    * @return the assignedToTB
    */
   @Override
   public TextBox getAssignedToTB() {
      return assignedToTB;
   }

   /**
    * Get the statusTB
    * @return the statusTB
    */
   @Override
   public TextBox getStatusTB() {
      return statusTB;
   }

   /**
    * Get the severityTB
    * @return the severityTB
    */
   @Override
   public TextBox getSeverityTB() {
      return severityTB;
   }

   /**
    * Get the reporterTB
    * @return the reporterTB
    */
   @Override
   public TextBox getReporterTB() {
      return reporterTB;
   }

   /**
    * Get the priorityTB
    * @return the priorityTB
    */
   @Override
   public TextBox getPriorityTB() {
      return priorityTB;
   }

   /**
    * Get the categoryTB
    * @return the categoryTB
    */
   @Override
   public TextBox getCategoryTB() {
      return categoryTB;
   }

   /**
    * Get the closeButton
    * @return the closeButton
    */
   @Override
   public Button getCloseButton() {
      return closeButton;
   }

   /**
    * Get the bugTrackerIdTB
    *
    * @return the bugTrackerIdTB
    */
   @Override
   public TextBox getBugTrackerIdTB()
   {
      return bugTrackerIdTB;
   }

   interface BugDetailPopUpViewImplUiBinder extends UiBinder<Widget, BugDetailPopUpViewImpl>
   {
   }
   
   
}
