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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.BugAssociatedToTaskEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.BugAssociatedToTaskEventHandler;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.FinishTaskEditionEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.Validator;
import org.novaforge.forge.tools.managementmodule.ui.client.view.task.TaskEditView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.task.TaskEditViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskCategoryDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskStatusDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskStatusEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.UserDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * The presenter for task edition
 */
public class TaskEditPresenter implements TabPresenter {
  
   /** The comparator to sort user by name */
   private static final Comparator<UserDTO> COMPARATOR_USER = new Comparator<UserDTO>() {
      @Override
      public int compare(UserDTO o1, UserDTO o2) {
        return o1.getLastName().compareTo(o2.getLastName());
      }
   };
   /** The comparator to sort category by name */
   private static final Comparator<TaskCategoryDTO> COMPARATOR_TASK_CATEGORY = new Comparator<TaskCategoryDTO>() {
      @Override
      public int compare(TaskCategoryDTO o1, TaskCategoryDTO o2) {
        return o1.getName().compareTo(o2.getName());
      }
   };
   /** The comparator to sort disciplines by name */
   private static final Comparator<ProjectDisciplineDTO> COMPARATOR_DISCIPLINE = new Comparator<ProjectDisciplineDTO>() {
      @Override
      public int compare(ProjectDisciplineDTO o1, ProjectDisciplineDTO o2) {
        return o1.getDisciplineDTO().getLibelle().compareTo(o2.getDisciplineDTO().getLibelle());
      }
   };
   //validator for required non empty fields
   private static final Validator NON_EMPTY_VALIDATOR = new Validator() {

      @Override
      public boolean isValid(String pValue) {
         return !(pValue == null || pValue.trim().isEmpty());
      }

      @Override
      public String getErrorMessage()
      {
         return Common.MESSAGES_TASK.requiredField();
      }
   };
   // validator for the float format of text boxes
   private static final Validator FLOAT_VALIDATOR     = new Validator()
   {

      @Override
      public boolean isValid(String pValue)
      {
         return pValue.matches("[0-9\\s]*[\\.,]{0,1}[0-9\\s]*");
      }

      @Override
      public String getErrorMessage()
      {
         return Common.MESSAGES_TASK.incorrectFormat();
      }
   };
   /** The logger */
   private static final Logger    LOGGER              = Logger.getLogger(TaskEditPresenter.class.getName());
   
   /**
    * A specific local event bus used to communicate with the components of this presenter (
    */
   private final        SimpleEventBus localEventBus  = new SimpleEventBus();
   /**
    * The task we are editing
    */
   private TaskDTO        task;
   /**
    * The eventBus to share events with other presenters
    */
   private SimpleEventBus eventBus;
   /**
    * The view managed by this presenter
    */
   private TaskEditView view = new TaskEditViewImpl();
   /**
    * The users in the combo list
    */
   private List<UserDTO>              userList;
   /**
    * The categories to put in the combo list
    */
   private List<TaskCategoryDTO>      categoryList;
   /**
    * The disciplines to put in the combo list
    */
   private List<ProjectDisciplineDTO> disciplinesList;
   /**
    * The status list
    */
   private List<TaskStatusDTO>        taskStatusList;
   /**
    * The presenter of the bug detail pop up
    */
   private BugDetailPopUpPresenter    bugDetailPopUpPresenter;
   /**
    * The presenter of the choose bug pop up
    */
   private ChooseBugPopUpPresenter    chooseBugPresenter;
   /**
    * The mode use for the task edition (creation/modification/suprevision)
    */
   private Mode                       mode;

   /**
    * Constructor
    *
    * @param eventBus the bus for event
    */
   public TaskEditPresenter(SimpleEventBus eventBus)
   {
      this.eventBus = eventBus;
      bind();
   }

   /**
    * Event management
    */
   private void bind()
   {
      //cancel button management
      view.getButtonCancel().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            eventBus.fireEvent(new FinishTaskEditionEvent());
         }
      });

      // save button management
      view.getButtonSave().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            try
            {
               saveTask();
            }
            catch (final Exception ex)
            {
               ErrorManagement.displayErrorMessage(ex);
               LOGGER.log(Level.SEVERE, "Unexpected task save problem", ex);
            }
         }
      });

      //re open task button
      view.getButtonReOpen().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            for (final TaskStatusDTO taskStatusDTO : taskStatusList)
            {
               if (taskStatusDTO.getEnumValue().equals(TaskStatusEnum.IN_PROGRESS))
               {
                  task.setStatus(taskStatusDTO);
                  break;
               }
            }
            loadDatas();
         }
      });
      //link to another bug button
      view.getButtonChooseBug().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            getChooseBugPresenter().setTask(task);
            getChooseBugPresenter().go(null);
         }
      });

      //handler to listen action to choose a bug
      localEventBus.addHandler(BugAssociatedToTaskEvent.TYPE, new BugAssociatedToTaskEventHandler()
      {
         @Override
         public void onLinkBugToTask(BugAssociatedToTaskEvent bugAssociatedToTaskEvent)
         {
            task.setBug(bugAssociatedToTaskEvent.getBug());
            view.getBugTitleTB().setText(bugAssociatedToTaskEvent.getBug().getTitle());
         }
      });
      view.getButtonInfos().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            BugDetailPopUpPresenter bugDetailPopUpPresenter = getBugDetailPopUpPresenter();
            bugDetailPopUpPresenter.setBug(task.getBug());
            bugDetailPopUpPresenter.go(null);
         }
      });
      //validator on required Name field
      view.getNameTBV().setValidator(NON_EMPTY_VALIDATOR);
      view.getInitialEstimationTBV().setValidator(FLOAT_VALIDATOR);
      view.getConsumedTimeTBV().setValidator(FLOAT_VALIDATOR);
      view.getRemainingTimeTBV().setValidator(FLOAT_VALIDATOR);
      //listeners on consumed and remaining to update the re-estimated field
      view.getConsumedTimeTBV().addValueChangeHandler(new ValueChangeHandler<String>()
      {
         @Override
         public void onValueChange(ValueChangeEvent<String> event)
         {
            try
            {
               float floatValue = getFloatValue(event.getValue());
               float remainingValue = getFloatValue(view.getRemainingTimeTBV().getValue());
               view.getReEstimatedTBV().setValue(String.valueOf(Common.floatFormat(floatValue + remainingValue, 2)));
            }
            catch (Exception ex)
            {
               //nothing, the validator will display an error if format is incorrect
            }
         }
      });
      view.getRemainingTimeTBV().addValueChangeHandler(new ValueChangeHandler<String>()
      {
         @Override
         public void onValueChange(ValueChangeEvent<String> event)
         {
            try
            {
               float floatValue = getFloatValue(event.getValue());
               float consumedValue = getFloatValue(view.getConsumedTimeTBV().getValue());
               view.getReEstimatedTBV().setValue(String.valueOf(Common.floatFormat(floatValue + consumedValue, 2)));
            }
            catch (Exception ex)
            {
               //nothing the validator will display an error if format is incorrect
            }
         }
      });
      
      view.getStartDateDBV().addValueChangeHandler(new ValueChangeHandler<Date>() {
        public void onValueChange(ValueChangeEvent<Date> event) {
          if ( event.getValue() != null )
          {
            view.getEndDateDBV().setCurrentMonth( view.getStartDateDBV().getValue() );
          }
        }
      });

   }

   /**
    * This methods save the task in persistence layer
    */
   private void saveTask()
   {
      //we validate the fields and stop the save if problem
      if (!validateFields())
      {
         return;
      }
      final TaskDTO taskToSave = new TaskDTO();
      // we set ton invariable thing from iteration
      taskToSave.setId(this.task.getId());
      taskToSave.setIteration(this.task.getIteration());
      taskToSave.setBug(task.getBug());
      taskToSave.setScopeUnit(task.getScopeUnit());
      taskToSave.setType(task.getType());
      //we set the value from text inputs
      taskToSave.setLabel(view.getNameTBV().getValue());
      taskToSave.setInitialEstimation(getFloatValue(view.getInitialEstimationTBV().getValue()));
      taskToSave.setConsumedTime(getFloatValue(view.getConsumedTimeTBV().getValue()));
      taskToSave.setRemainingTime(getFloatValue(view.getRemainingTimeTBV().getValue()));
      taskToSave.setDescription(view.getDescriptionTA().getText());
      taskToSave.setCommentary(view.getCommentTA().getText());
      taskToSave.setStartDate(view.getStartDateDBV().getValue());
      taskToSave.setEndDate(view.getEndDateDBV().getValue());
      //we set the value from combos
      taskToSave.setCategory(view.getCategoryLB().getSelectedAssociatedObject());
      taskToSave.setDiscipline(view.getDisciplineLB().getSelectedAssociatedObject());
      taskToSave.setUser(view.getUserLB().getSelectedAssociatedObject());
      taskToSave.setStatus(view.getStatusLB().getSelectedAssociatedObject());
      //if user have just set the initial estimation and than remaining time = 0 we set initial estimation to remaining time
      if (task.getInitialEstimation() == 0F && taskToSave.getInitialEstimation() != 0F
              && taskToSave.getRemainingTime() == 0F)
      {
         taskToSave.setRemainingTime(taskToSave.getInitialEstimation());
      }
      //we launch server save
      if (taskToSave.getId() == null)
      {
         createTask(taskToSave);
      }
      else
      {
         modifyTask(taskToSave);
      }
   }

   /**
    * This method loads the datas in the view
    */
   private void loadDatas()
   {
      try
      {
         controlDatas();
         manageRights();
         applyTaskStatusParticularities();
         manageModeDifferences();
         managedTaskTypeParticularities();
         fillTextBox();
         getProjectDatasAndFillAssociatedListBox();
         getTaskStatusListAndFillStatusListBox();
      }
      //something required to display the screen is incorrect
      catch (IllegalStateException isex)
      {
         ErrorManagement.displayErrorMessage(isex.getMessage());
         eventBus.fireEvent(new FinishTaskEditionEvent());
      }
      //unexcepted technical problem
      catch (RuntimeException ex)
      {
         ErrorManagement.displayErrorMessage(Common.MESSAGES_TASK.errorDuringDataLoading());
         LOGGER.log(Level.SEVERE, "Unexpected data loading problem", ex);
         eventBus.fireEvent(new FinishTaskEditionEvent());
      }
   }

   /**
    * Get the ChooseBugPopUpPresenter (create oen if needed)
    */
   private ChooseBugPopUpPresenter getChooseBugPresenter()
   {
      if (this.chooseBugPresenter == null)
      {
         this.chooseBugPresenter = new ChooseBugPopUpPresenter(localEventBus);
      }
      return chooseBugPresenter;
   }

   /**
    * Get the bug detail pop up presenter
    */
   public BugDetailPopUpPresenter getBugDetailPopUpPresenter()
   {
      if (bugDetailPopUpPresenter == null)
      {
         this.bugDetailPopUpPresenter = new BugDetailPopUpPresenter();
      }
      return this.bugDetailPopUpPresenter;
   }

   /**
    * Get the server float value using the value in the text box field
    *
    * @param value
    *     the value to convert
    *
    * @return the corresponding float
    *
    * @throws NumberFormatException
    *     if string after modification cant be parsed
    */
   private static float getFloatValue(final String value)
   {
      if (value == null || value.trim().equals(""))
      {
         return 0F;
      }
      String correctedValue = value.trim().replace(",", ".");
      return Float.parseFloat(correctedValue);
   }

   /**
    * Validation of the fields
    *
    * @return
    */
   private boolean validateFields()
   {
      boolean validatorsOK =
          view.getNameTBV().isValid() && view.getInitialEstimationTBV().isValid() && view.getConsumedTimeTBV().isValid()
              && view.getRemainingTimeTBV().isValid();
      if (!validatorsOK)
      {
         ErrorManagement.displayErrorMessage(Common.MESSAGES_TASK.fieldsIncorrect());
         return false;
      }
      //if task is marked as finished and if there is remaining time we put a error message
      if (view.getStatusLB().getSelectedAssociatedObject().getEnumValue().equals(TaskStatusEnum.DONE)
              && getFloatValue(view.getRemainingTimeTBV().getValue()) != 0F)
      {
         ErrorManagement.displayErrorMessage(Common.MESSAGES_TASK.closeWithRemainingTime());
         return false;
      }
      return true;
   }

   /**
    * This method persist a task in data layer
    *
    * @param taskDTOToCreate
    *     the task to create/persist
    */
   private void createTask(final TaskDTO taskDTOToCreate)
   {
      /** The RPC call to create a task */
      new AbstractManagementRPCCall<TaskDTO>()
      {

         @Override
         protected void callService(AsyncCallback<TaskDTO> callBack)
         {
            Common.TASK_SERVICE.createTask(taskDTOToCreate, callBack);
         }

         @Override
         public void onFailure(Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(TaskDTO persistedTask)
         {
            manageSuccessSave(persistedTask, taskDTOToCreate);
         }

      }.retry(0);
   }

   /**
    * This method modify a task in data layer
    *
    * @param taskDTOToCreate
    *     the task to update
    */
   private void modifyTask(final TaskDTO taskDTOToModify)
   {
      /** The RPC call to create a task */
      new AbstractManagementRPCCall<TaskDTO>()
      {

         @Override
         protected void callService(AsyncCallback<TaskDTO> callBack)
         {
            Common.TASK_SERVICE.modifyTask(taskDTOToModify, callBack);
         }

         @Override
         public void onSuccess(TaskDTO modifiedTask)
         {
            manageSuccessSave(modifiedTask, taskDTOToModify);
         }

         @Override
         public void onFailure(Throwable caught) {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
   }

   /**
    * This method controls that the datas passed to the presenter are OK to do the display
    */
   private void controlDatas() {
      if (task == null || task.getIteration() == null || task.getType() == null) {
         throw new IllegalStateException(Common.MESSAGES_TASK.illegalTaskDatas());
      }
   }

   /**
    * This method manages the access right on the task edition functionality
    */
   private void manageRights() {
      final AccessRight accessRight = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_MANAGING_TASK);
      switch(accessRight) {
         case READ:
            manageReadOnlyMode(true);
            break;
         case WRITE:
            manageReadOnlyMode(false);
            break;
         case NONE:
         default:
            throw new IllegalStateException(Common.GLOBAL.insufficientRights());
      }
   }
   
   /**
    * This method apply the particularities base on the task status
    */
   private void applyTaskStatusParticularities() {
      //change bug only if not consumed time and status = not started or not affected (or null -> creation)
      if ((task.getStatus() == null || task.getStatus().getEnumValue().equals(TaskStatusEnum.NOT_STARTED) || task
            .getStatus().getEnumValue().equals(TaskStatusEnum.NOT_AFFECTED))
            && task.getConsumedTime() == 0f) {
         view.getButtonChooseBug().setEnabled(true);
      }
      else
      {
         view.getButtonChooseBug().setEnabled(false);
      }
      if (task.getStatus() != null
            && (task.getStatus().getEnumValue().equals(TaskStatusEnum.DONE)
               || task.getStatus().getEnumValue().equals(TaskStatusEnum.CANCELED))) {
         manageReadOnlyMode(true);
         view.getButtonSave().setEnabled(false);
         if(SessionData.getAccessRight(ApplicativeFunction.FUNCTION_MANAGING_TASK).equals(AccessRight.WRITE)){
            view.getButtonReOpen().setVisible(true);
         }
         else {
            view.getButtonReOpen().setVisible(false);
         }
      }
      else {
         view.getButtonReOpen().setVisible(false);
      }
   }
   
   /**
    * This methods manage the differences between creation, modification  and follow modes
    */
   private void manageModeDifferences() {
      //creation
      switch(mode){
         case CREATION:
            view.getLabelTitle().setText(Common.MESSAGES_TASK.creationTitle());
            view.getRemainingTimeTBV().setEnabled(false);
            view.getConsumedTimeTBV().setEnabled(false);
            break;
         case MODIFICATION:
            view.getLabelTitle().setText(Common.MESSAGES_TASK.modificationTitle());
            break;
         case SUPERVISION:
            view.getLabelTitle().setText(Common.MESSAGES_TASK.supervisionTitle());
            manageReadOnlyMode(true);
            view.getButtonSave().setEnabled(false);
            view.getButtonReOpen().setVisible(false);
            view.getButtonChooseBug().setEnabled(false);
            break;
         default:
            throw new IllegalStateException(Common.MESSAGES_TASK.illegalTaskDatas());
      }
   }

   /**
    * Manage the difference between task of TYPE bug and task of TYPE work
    */
   private void managedTaskTypeParticularities()
   {
      if (task.getType().equals(TaskTypeEnum.WORK))
      {
         view.getTypeNameTB().setText(Common.MESSAGES_TASK.workTask());
         view.getBugPanel().setVisible(false);
         view.getDisciplineLB().setEnabled(false);
      }
      else
      {
         view.getTypeNameTB().setText(Common.MESSAGES_TASK.bugTask());
         view.getBugPanel().setVisible(true);
         view.getDisciplineLB().setEnabled(true);
      }
   }

   /**
    * This method fill the text boxes with task data
    */
   private void fillTextBox() {
      view.getIterationNameTB().setText(task.getIteration().getLabel());
      view.getLotNameTB().setText(task.getLotName());
      view.getParentLotNameTB().setText(task.getParentLotName());
      if(task.getScopeUnit() != null){
         view.getUPNameTB().setText(task.getScopeUnit().getName());
      }
      else {
         view.getUPNameTB().setText(Common.EMPTY_TEXT);
      }
      view.getParentUpNameTB().setText(task.getParentScopeUnitName());
      view.getNameTBV().clear();
      view.getNameTBV().setValue(task.getLabel());
      view.getInitialEstimationTBV().clear();
      view.getInitialEstimationTBV().setValue(String.valueOf(Common.floatFormat(task.getInitialEstimation(),2)));
      view.getConsumedTimeTBV().clear();
      view.getConsumedTimeTBV().setValue(String.valueOf(Common.floatFormat(task.getConsumedTime(),2)));
      view.getRemainingTimeTBV().clear();
      view.getRemainingTimeTBV().setValue(String.valueOf(Common.floatFormat(task.getRemainingTime(), 2)));
      view.getReEstimatedTBV().setValue(String.valueOf(Common.floatFormat(task.getRemainingTime() + task.getConsumedTime(),2)));
      view.getDescriptionTA().setText(task.getDescription());
      view.getCommentTA().setText(task.getCommentary());
      view.getStartDateDBV().setValue(task.getStartDate());
      view.getEndDateDBV().setValue(task.getEndDate());
      if (task.getBug() != null)
      {
         view.getBugTitleTB().setText(task.getBug().getTitle());
      }

   }

   /**
    * Get the project datas to fill user, categories and disciplines sets
    */
   private void getProjectDatasAndFillAssociatedListBox()
   {

      new AbstractManagementRPCCall<ProjectDTO>()
      {

         @Override
         protected void callService(AsyncCallback<ProjectDTO> callBack)
         {
            Common.COMMON_SERVICE.getProject(SessionData.projectId, callBack);
         }

         @Override
         public void onSuccess(ProjectDTO project)
         {
            disciplinesList = new ArrayList<ProjectDisciplineDTO>(project.getProjectDisciplines());
            categoryList = new ArrayList<TaskCategoryDTO>(project.getTaskCategories());
            userList = new ArrayList<UserDTO>(project.getUserList());
            Collections.sort(disciplinesList, COMPARATOR_DISCIPLINE);
            Collections.sort(categoryList, COMPARATOR_TASK_CATEGORY);
            Collections.sort(userList, COMPARATOR_USER);
            fillUserListBox();
            fillCategoryListBox();
            fillDisciplineListBox();
         }

         @Override
         public void onFailure(Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
   }

   /**
    * Get the task status
    */
   private void getTaskStatusListAndFillStatusListBox()
   {

      new AbstractManagementRPCCall<Set<TaskStatusDTO>>()
      {

         @Override
         protected void callService(AsyncCallback<Set<TaskStatusDTO>> callBack)
         {
            Common.TASK_SERVICE.getAllTaskStatus(callBack);
         }

         @Override
         public void onSuccess(Set<TaskStatusDTO> taskStatusSet)
         {
            taskStatusList = new ArrayList<TaskStatusDTO>(taskStatusSet);
            fillStatusListBox();
         }

         @Override
         public void onFailure(Throwable caught) {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
   }
   
   /**
    * This method contains the logic to apply after a successful save of a task
    * @param modifiedTask the task after modification
    * @param taskDTOToModify the task before modification
    */
   private void manageSuccessSave(TaskDTO modifiedTask, TaskDTO taskDTOToModify) {
      final StringBuilder sb = new StringBuilder();
      sb.append(Common.MESSAGES_TASK.taskSaved());
      if(Common.floatFormat(modifiedTask.getConsumedTime(), 2) > Common.floatFormat(taskDTOToModify.getConsumedTime(), 2)){
         sb.append("<br/>");
         sb.append(Common.MESSAGES_TASK.consumedNotUpdated());   
      }
      InfoDialogBox info = new InfoDialogBox(sb.toString(), InfoTypeEnum.OK);
      info.getDialogPanel().center();
      eventBus.fireEvent(new FinishTaskEditionEvent());
   }
   
   /**
    * Set the view in readonly mode or normal mode depending of the boolean isReadOnly value
    */
   private void manageReadOnlyMode(boolean isReadOnly) {
      view.getNameTBV().setEnabled(!isReadOnly);
      view.getInitialEstimationTBV().setEnabled(!isReadOnly);
      view.getConsumedTimeTBV().setEnabled(!isReadOnly);
      view.getRemainingTimeTBV().setEnabled(!isReadOnly);
      view.getDescriptionTA().setEnabled(!isReadOnly);
      view.getCommentTA().setEnabled(!isReadOnly);
      view.getStartDateDBV().setEnabled(!isReadOnly);
      view.getEndDateDBV().setEnabled(!isReadOnly);
      view.getCategoryLB().setEnabled(!isReadOnly);
      view.getDisciplineLB().setEnabled(!isReadOnly);
      view.getUserLB().setEnabled(!isReadOnly);
      view.getStatusLB().setEnabled(!isReadOnly);
      view.getButtonSave().setEnabled(!isReadOnly);
   }

   /**
    * Fill the User list box
    */
   private void fillUserListBox()
   {
      view.getUserLB().clear();
      int selectedIndex = 0;
      view.getUserLB().addItem("", "none", null);
      for (final UserDTO userDTO : userList)
      {
         view.getUserLB().addItem(userDTO.getLastName() + " " + userDTO.getFirstName(), userDTO.getLogin(), userDTO);
         // if there is an user we select it
         if (task.getUser() != null && task.getUser().equals(userDTO)) {
            selectedIndex = view.getUserLB().getItemCount() - 1;
         }
      }
      view.getUserLB().setSelectedIndex(selectedIndex);
   }

   /**
    * Fill the category list box
    */
   private void fillCategoryListBox()
   {
      view.getCategoryLB().clear();
      int selectedIndex = 0;
      view.getCategoryLB().addItem("", "none", null);
      for (final TaskCategoryDTO categoryDTO : categoryList)
      {
         view.getCategoryLB().addItem(categoryDTO.getName(), String.valueOf(categoryDTO.getId()), categoryDTO);
         // if there is an category we select it
         if (task.getCategory() != null && task.getCategory().equals(categoryDTO))
         {
            selectedIndex = view.getCategoryLB().getItemCount() - 1;
         }
      }
      view.getCategoryLB().setSelectedIndex(selectedIndex);
   }

   /**
    * Fill the discipline list box
    */
   private void fillDisciplineListBox()
   {
      view.getDisciplineLB().clear();
      int selectedIndex = 0;
      for (final ProjectDisciplineDTO projectDisciplineDTO : disciplinesList)
      {
         final DisciplineDTO discipline = projectDisciplineDTO.getDisciplineDTO();
         //we only display the disciplines the connected user has to see
         if (!SessionData.disciplinesOfConnectedUser.contains(discipline))
         {
            continue;
         }
         view.getDisciplineLB().addItem(discipline.getLibelle(), discipline.getFunctionalId(), discipline);
         // if there is a discipline set to the task we select it
         if (task.getDiscipline() != null && task.getDiscipline().equals(discipline))
         {
            selectedIndex = view.getDisciplineLB().getItemCount() - 1;
         }
      }
      view.getDisciplineLB().setSelectedIndex(selectedIndex);
   }

   /**
    * Fill the status list box
    */
   private void fillStatusListBox()
   {
      view.getStatusLB().clear();
      int selectedIndex = 0;
      for (final TaskStatusDTO statusDTO : taskStatusList)
      {
         view.getStatusLB().addItem(statusDTO.getLabel(), statusDTO.getFunctionalId(), statusDTO);
         // if there is a status we select it
         if (task.getStatus() != null && task.getStatus().equals(statusDTO))
         {
            selectedIndex = view.getStatusLB().getItemCount() - 1;
         }
         //if the task has no status (only possible on creation) we take the default one
         if (task.getStatus() == null && statusDTO.isDefaultStatus())
         {
            selectedIndex = view.getStatusLB().getItemCount() - 1;
         }
      }
      view.getStatusLB().setSelectedIndex(selectedIndex);
   }

   @Override
   public void go(HasWidgets container)
   {
      container.clear();
      container.add(view.asWidget());
      loadDatas();
   }

   @Override
   public void loadDataOnSelectionTab() {
      //nothing, data loading is done on setting a new task
   }
   
   @Override
   public IsWidget getDisplay() {
      return this.view.asWidget();
   }

   /**
    * Get the task
    *
    * @return the task
    */
   public TaskDTO getTask()
   {
      return task;
   }

   /**
    * Set le taskDTO à gérer dans le presenter et la vue
    * @param newTaskDTO
    */
   public void setTask(final TaskDTO newTaskDTO) {
      this.task = newTaskDTO;
      loadDatas();
   }

   /**
    * Get the mode
    * @return the mode
    */
   public Mode getMode() {
      return mode;
   }
   
   /**
    * Set the mode
    * @param mode the mode to set
    */
   public void setMode(Mode mode)
   {
      this.mode = mode;
   }
   
   /**
    * The different modes availables
    */
   public enum Mode
   {
      CREATION, MODIFICATION, SUPERVISION
   }
   
}
