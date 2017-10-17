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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.CustomListBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.DateBoxValidation;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.TextBoxValidation;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskCategoryDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskStatusDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.UserDTO;

public class TaskEditViewImpl extends Composite implements TaskEditView {

   private static final String TWO_BY_LINE_GRID_FIELD_WIDTH = "350px";
   private static final String TWO_BY_LINE_GRID_LABEL_WIDTH = "160px";
   private static final String TWO_BY_LINE_IN_FIELDSET_GRID_LABEL_WIDTH = "160px";
   private static final String TWO_BY_LINE_IN_FIELDSET_GRID_FIELD_WIDTH = "340px";
   private static final String THIRD_BY_LINE_GRID_TEXTBOX_WIDTH = "240px";
   private static final String THIRD_BY_LINE_GRID_LABEL_WIDTH = "120px";
   private static final String DEFAULT_TEXTBOX_WIDTH = "200px";
   private static TaskEditViewImplUiBinder uiBinder = GWT.create(TaskEditViewImplUiBinder.class);
   //the top
   @UiField
   Button buttonCancel;
   @UiField
   Button buttonSave;
   @UiField
   Button buttonReOpen;
   @UiField
   Label labelTitle;
   //the top grid
   @UiField
   Grid topGrid;
   @UiField
   Label labelIteration;
   @UiField
   TextBox iterationNameTB;
   @UiField
   Label labelLot;
   @UiField
   TextBox lotNameTB;
   @UiField
   Label labelParentLot;
   @UiField
   TextBox parentLotNameTB;
   @UiField
   Label labelType;
   @UiField
   TextBox typeNameTB;
   @UiField
   Label labelUP;
   @UiField
   TextBox UPNameTB;
   @UiField
   Label labelParentUP;
   @UiField
   TextBox parentUpNameTB;
   //the form grid
   @UiField
   Grid formGrid;
   @UiField
   Grid topLeftFormGrid;
   @UiField
   Grid topRightFormGrid;
   @UiField
   Label labelName;
   @UiField
   TextBoxValidation nameTBV;
   @UiField
   Label labelStartDate;
   @UiField
   DateBoxValidation startDateDBV;
   @UiField
   Label labelEndDate;
   @UiField
   DateBoxValidation endDateDBV;
   @UiField
   Label labelState;
   @UiField
   CustomListBox<TaskStatusDTO> stateLB;
   @UiField
   Label labelDiscipline;
   @UiField
   CustomListBox<DisciplineDTO> disciplineLB;
   @UiField
   Label labelUser;
   @UiField
   CustomListBox<UserDTO> userLB;
   @UiField
   Label labelCategory;
   @UiField
   CustomListBox<TaskCategoryDTO> categoryLB;
   @UiField
   Label labelInitialEstimation;
   @UiField
   TextBoxValidation initialEstimationTBV;
   @UiField
   Grid suiviGrid;
   @UiField
   Label suiviFieldSetTitle;
   @UiField
   Label labelRemainingTime;
   @UiField
   TextBoxValidation remainingTimeTBV;
   @UiField
   Label labelConsumedTime;
   @UiField
   TextBoxValidation consumedTimeTBV;
   @UiField
   Label labelReEstimated;
   @UiField
   TextBoxValidation reEstimatedTBV;
   @UiField
   Grid textAreaGrid;
   @UiField
   Label labelDescription;
   @UiField
   TextArea descriptionTA;
   @UiField
   Label labelComment;
   @UiField
   TextArea commentTA;
   @UiField
   Panel bugPanel;
   @UiField
   Label bugFieldSetTitle;
   @UiField
   TextBox bugTitleTB;
   @UiField
   Button buttonInfos;
   @UiField
   Button buttonChooseBug;
   /**
    * Constructor
    */
   public TaskEditViewImpl() {
      initWidget(uiBinder.createAndBindUi(this));
      //buttons
      buttonSave.setText(Common.getGlobal().buttonSave());
      buttonCancel.setText(Common.getGlobal().buttonCancel());
      buttonReOpen.setText(Common.MESSAGES_TASK.buttonReOpenTask());
      buttonReOpen.setVisible(false);
      //top grid
      topGrid.setCellPadding(3);
      labelIteration.setText(Common.getGlobal().iteration());
      iterationNameTB.setWidth(DEFAULT_TEXTBOX_WIDTH);
      topGrid.getCellFormatter().setWidth(0, 0, THIRD_BY_LINE_GRID_LABEL_WIDTH);
      topGrid.getCellFormatter().setWidth(0, 1, THIRD_BY_LINE_GRID_TEXTBOX_WIDTH);
      labelLot.setText(Common.getGlobal().lot());
      lotNameTB.setWidth(DEFAULT_TEXTBOX_WIDTH);
      topGrid.getCellFormatter().setWidth(0, 2, THIRD_BY_LINE_GRID_LABEL_WIDTH);
      topGrid.getCellFormatter().setWidth(0, 3, THIRD_BY_LINE_GRID_TEXTBOX_WIDTH);
      labelParentLot.setText(Common.getGlobal().parentLot());
      parentLotNameTB.setWidth(DEFAULT_TEXTBOX_WIDTH);
      topGrid.getCellFormatter().setWidth(0, 4, THIRD_BY_LINE_GRID_LABEL_WIDTH);
      topGrid.getCellFormatter().setWidth(0, 5, THIRD_BY_LINE_GRID_TEXTBOX_WIDTH);
      labelType.setText(Common.MESSAGES_TASK.type());
      typeNameTB.setWidth(DEFAULT_TEXTBOX_WIDTH);
      labelUP.setText(Common.MESSAGES_TASK.UP());
      UPNameTB.setWidth(DEFAULT_TEXTBOX_WIDTH);
      labelParentUP.setText(Common.MESSAGES_TASK.parentUP());
      parentUpNameTB.setWidth(DEFAULT_TEXTBOX_WIDTH);
      //form grid
      formGrid.setCellPadding(3);
      formGrid.setWidth("100%");
      formGrid.getCellFormatter().setWidth(0, 0, "525px");
      formGrid.getCellFormatter().setWidth(0, 2, "525px");
      //top left
      topLeftFormGrid.getCellFormatter().setWidth(0, 0, TWO_BY_LINE_GRID_LABEL_WIDTH);
      topLeftFormGrid.getCellFormatter().setWidth(0, 1, TWO_BY_LINE_GRID_FIELD_WIDTH);
      labelName.setText(Common.MESSAGES_TASK.taskName());
      nameTBV.setWidth("280px");
      labelStartDate.setText(Common.MESSAGES_TASK.startDate());
      startDateDBV.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd/MM/yyyy")));
      labelState.setText(Common.MESSAGES_TASK.state());
      labelInitialEstimation.setText(Common.MESSAGES_TASK.initialEstimation());
      initialEstimationTBV.setWidth(DEFAULT_TEXTBOX_WIDTH);
      //top right
      topRightFormGrid.getCellFormatter().setWidth(0, 0, TWO_BY_LINE_GRID_LABEL_WIDTH);
      topRightFormGrid.getCellFormatter().setWidth(0, 1, TWO_BY_LINE_GRID_FIELD_WIDTH);
      labelEndDate.setText(Common.MESSAGES_TASK.endDate());
      endDateDBV.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd/MM/yyyy")));
      labelDiscipline.setText(Common.MESSAGES_TASK.discipline());
      labelUser.setText(Common.MESSAGES_TASK.user());
      labelCategory.setText(Common.MESSAGES_TASK.category());
      // time grid
      suiviFieldSetTitle.setText(Common.MESSAGES_TASK.suiviFieldSetTitle());
      suiviGrid.getCellFormatter().setWidth(0, 0, TWO_BY_LINE_IN_FIELDSET_GRID_LABEL_WIDTH);
      suiviGrid.getCellFormatter().setWidth(0, 1, TWO_BY_LINE_IN_FIELDSET_GRID_FIELD_WIDTH);
      labelRemainingTime.setText(Common.MESSAGES_TASK.remainingTime());
      remainingTimeTBV.setWidth(DEFAULT_TEXTBOX_WIDTH);
      labelConsumedTime.setText(Common.MESSAGES_TASK.consumedTime());
      consumedTimeTBV.setWidth(DEFAULT_TEXTBOX_WIDTH);
      labelReEstimated.setText(Common.MESSAGES_TASK.reEstimated());
      reEstimatedTBV.setWidth(DEFAULT_TEXTBOX_WIDTH);
      //text area grid
      textAreaGrid.getCellFormatter().setWidth(0, 0, TWO_BY_LINE_GRID_LABEL_WIDTH);
      textAreaGrid.getCellFormatter().setWidth(0, 1, TWO_BY_LINE_GRID_FIELD_WIDTH);
      descriptionTA.setWidth("100%");
      descriptionTA.setHeight("65px");
      commentTA.setWidth("100%");
      commentTA.setHeight("50px");
      labelDescription.setText(Common.GLOBAL.description());
      labelComment.setText(Common.MESSAGES_TASK.comment());
      //bug grid
      bugFieldSetTitle.setText(Common.MESSAGES_TASK.bug());
      buttonInfos.setText(Common.MESSAGES_TASK.buttonInfos());
      buttonChooseBug.setText(Common.MESSAGES_TASK.buttonChooseBug());
      bugTitleTB.setWidth("500px");
   }
   
   @Override
   public Button getButtonSave() {
      return buttonSave;
   }

   @Override
   public Button getButtonCancel()
   {
      return buttonCancel;
   }
   
   /**
    * Get the labelTitle
    * @return the labelTitle
    */
   @Override
   public Label getLabelTitle() {
      return labelTitle;
   }

   /**
    * Get the iterationNameTB
    * @return the iterationNameTB
    */
   @Override
   public TextBox getIterationNameTB() {
      return iterationNameTB;
   }

   /**
    * Get the parentLotNameTB
    * @return the parentLotNameTB
    */
   @Override
   public TextBox getParentLotNameTB() {
      return parentLotNameTB;
   }

   /**
    * Get the typeNameTB
    * @return the typeNameTB
    */
   @Override
   public TextBox getTypeNameTB() {
      return typeNameTB;
   }

   /**
    * Get the uPNameTB
    * @return the uPNameTB
    */
   @Override
   public TextBox getUPNameTB() {
      return UPNameTB;
   }

   /**
    * Get the parentUpNameTB
    * @return the parentUpNameTB
    */
   @Override
   public TextBox getParentUpNameTB() {
      return parentUpNameTB;
   }

   /**
    * Get the startDateDBV
    * @return the startDateDBV
    */
   @Override
   public DateBoxValidation getStartDateDBV() {
      return startDateDBV;
   }

   /**
    * Get the endDateDBV
    * @return the endDateDBV
    */
   @Override
   public DateBoxValidation getEndDateDBV() {
      return endDateDBV;
   }

   /**
    * Get the stateLB
    * @return the stateLB
    */
   @Override
   public CustomListBox<TaskStatusDTO> getStatusLB() {
      return stateLB;
   }

   /**
    * Get the disciplineLB
    * @return the disciplineLB
    */
   @Override
   public CustomListBox<DisciplineDTO> getDisciplineLB() {
      return disciplineLB;
   }

   /**
    * Get the userLB
    * @return the userLB
    */
   @Override
   public CustomListBox<UserDTO> getUserLB() {
      return userLB;
   }

   /**
    * Get the categoryLB
    * @return the categoryLB
    */
   @Override
   public CustomListBox<TaskCategoryDTO> getCategoryLB() {
      return categoryLB;
   }

   /**
    * Get the initialEstimationTBV
    * @return the initialEstimationTBV
    */
   @Override
   public TextBoxValidation getInitialEstimationTBV() {
      return initialEstimationTBV;
   }

   /**
    * Get the suiviGrid
    * @return the suiviGrid
    */
   @Override
   public Grid getSuiviGrid() {
      return suiviGrid;
   }

   /**
    * Get the suiviFieldSetTitle
    * @return the suiviFieldSetTitle
    */
   @Override
   public Label getSuiviFieldSetTitle() {
      return suiviFieldSetTitle;
   }

   /**
    * Get the remainingTimeTBV
    * @return the remainingTimeTBV
    */
   @Override
   public TextBoxValidation getRemainingTimeTBV() {
      return remainingTimeTBV;
   }

   /**
    * Get the consumedTimeTBV
    * @return the consumedTimeTBV
    */
   @Override
   public TextBoxValidation getConsumedTimeTBV() {
      return consumedTimeTBV;
   }

   /**
    * Get the reEstimatedTBV
    * @return the reEstimatedTBV
    */
   @Override
   public TextBoxValidation getReEstimatedTBV() {
      return reEstimatedTBV;
   }

   /**
    * Get the descriptionTA
    * @return the descriptionTA
    */
   @Override
   public TextArea getDescriptionTA() {
      return descriptionTA;
   }

   /**
    * Get the commentTA
    * @return the commentTA
    */
   @Override
   public TextArea getCommentTA() {
      return commentTA;
   }

   /**
    * Get the lotNameTB
    * @return the lotNameTB
    */
   @Override
   public TextBox getLotNameTB() {
      return lotNameTB;
   }

   /**
    * Get the nameTBV
    * @return the nameTBV
    */
   @Override
   public TextBoxValidation getNameTBV() {
      return nameTBV;
   }

   /**
    * Get the buttonReOpen
    * @return the buttonReOpen
    */
   @Override
   public Button getButtonReOpen() {
      return buttonReOpen;
   }

   @Override
   public Button getButtonInfos() {
      return buttonInfos;
   }

   @Override
   public Button getButtonChooseBug() {
      return buttonChooseBug;
   }

   @Override
   public TextBox getBugTitleTB() {
      return bugTitleTB;
   }

   @Override
   public Panel getBugPanel() {
      return bugPanel;
   }

   interface TaskEditViewImplUiBinder extends UiBinder<Widget, TaskEditViewImpl>
   {
   }
   
}
