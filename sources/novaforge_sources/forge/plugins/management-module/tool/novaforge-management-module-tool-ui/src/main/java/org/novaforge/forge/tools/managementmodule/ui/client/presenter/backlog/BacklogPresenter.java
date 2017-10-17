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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.backlog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.novaforge.forge.tools.managementmodule.ui.client.ManagementModuleEntryPoint;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.BugAssociatedToTaskEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.BugAssociatedToTaskEventHandler;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowIterationMonitoringEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowTaskEditViewEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.TaskSelectionModelChangeEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.pilotageexecution.commons.IterationContextPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.pilotageexecution.commons.TaskListPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.scopeunitdiscipline.ScopeUnitDisciplinePopupPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.task.ChooseBugPopUpPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.task.TaskEditPresenter.Mode;
import org.novaforge.forge.tools.managementmodule.ui.client.view.backlog.BacklogView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.backlog.BacklogViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationTaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskListEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskStatusEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BILET-JC
 *
 */
public class BacklogPresenter implements TabPresenter {

	private final BacklogView display; 
	/** The global event bus used to communicate with all the others presenters */
	private final SimpleEventBus globalEventBus;
	/** A specific local event bus used to communicate with the components of this presenter (view, task list presenter ...) */
	private final SimpleEventBus backlogEventBus = new SimpleEventBus();
	private IterationDTO iteration;
	private TaskListPresenter taskListPresenter;
  AbstractManagementRPCCall<List<DisciplineDTO>> getAllDisciplines = new AbstractManagementRPCCall<List<DisciplineDTO>>()
   {

    @Override
    protected void callService(AsyncCallback<List<DisciplineDTO>> pCb)
    {
      Common.REFERENTIAL_SERVICE.getAllPhareDTOList(pCb);
    }

    @Override
    public void onFailure(Throwable pCaught)
    {
      ErrorManagement.displayErrorMessage(pCaught);
    }

    @Override
    public void onSuccess(List<DisciplineDTO> pResult)
    {
      if (pResult != null)
      {

        for (final DisciplineDTO discipline : pResult)
        {

          if (SessionData.disciplinesOfConnectedUser.contains(discipline))
          {
            taskListPresenter.getDisciplines().addItem(discipline.getLibelle());
          }
        }
      }
    }

  };
  private ChooseBugPopUpPresenter           chooseBugPresenter;
  private ScopeUnitDisciplinePopupPresenter scopeUnitDisciplinePopupPresenter;
  private IterationContextPresenter         iterationContextPresenter;
  AbstractManagementRPCCall<IterationDTO> completeSelectedIterationAndDoDisplay = new AbstractManagementRPCCall<IterationDTO>()
  {

    @Override
    protected void callService(AsyncCallback<IterationDTO> pCb)
    {
      Common.ITERATION_SERVICE.getIterationById(iteration.getIterationId(), false, pCb);
    }

    @Override
    public void onSuccess(IterationDTO pResult)
    {
      if (pResult != null)
      {
        iteration = pResult;
        displaySelectedIteration();
      }
    }

    @Override
    public void onFailure(Throwable pCaught)
    {
      ErrorManagement.displayErrorMessage(pCaught);
    }
  };
  /**
   * The iteration task selected in the task list
   */
  private IterationTaskDTO selectedIterationTask;
  AbstractManagementRPCCall<IterationDTO> getDefaultIterationAndDoDisplay = new AbstractManagementRPCCall<IterationDTO>()
  {

    @Override
    protected void callService(AsyncCallback<IterationDTO> pCb)
    {
      Common.ITERATION_SERVICE.getCurrentOrLastFinishedIteration(SessionData.currentValidatedProjectPlanId, false, pCb);
    }

    @Override
    public void onFailure(Throwable pCaught)
    {
      ErrorManagement.displayErrorMessage(pCaught);
    }

    @Override
    public void onSuccess(IterationDTO pResult)
    {
      if (pResult != null)
      {
        iteration = pResult;
        manageButtonState();
        displaySelectedIteration();
      }
      else
      {
        new InfoDialogBox(Common.MESSAGES_BACKLOG.noIterationAvailable(), InfoTypeEnum.KO).getDialogPanel().center();
        manageButtonState();
      }
    }

  };
  //RPCCALL pour fermer une itération
  AbstractManagementRPCCall<IterationDTO> closeIteration                  = new AbstractManagementRPCCall<IterationDTO>()
  {

    @Override
    protected void callService(AsyncCallback<IterationDTO> callBack)
    {
      Common.TASK_SERVICE.closeIteration(iteration.getIterationId(), callBack);
    }

    @Override
    public void onSuccess(IterationDTO pResult)
    {
      iteration = pResult;
      manageButtonState();
      final InfoDialogBox infoBox = new InfoDialogBox(Common.MESSAGES_BACKLOG.closeIterationSuccessMessage(),
                                                      InfoTypeEnum.OK);
      infoBox.getDialogPanel().center();
    }

    @Override
    public void onFailure(Throwable pCaught)
    {
      ErrorManagement.displayErrorMessage(pCaught);
    }
  };

  /**
   * @param rpcService
   * @param display
   */
  public BacklogPresenter(final SimpleEventBus eventBus)
  {
    super();
    this.globalEventBus = eventBus;
    this.display = new BacklogViewImpl();
    iterationContextPresenter = new IterationContextPresenter(backlogEventBus);
    taskListPresenter = new TaskListPresenter(backlogEventBus, TaskListEnum.BACKLOG);
    bind();
    enableUI();
  }

  private void bind()
  {
    display.getButtonHomeReturn().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(ClickEvent event)
      {
        ManagementModuleEntryPoint.getHomePresenter().go(RootLayoutPanel.get());
      }
    });
    /* selection model */
    backlogEventBus.addHandler(TaskSelectionModelChangeEvent.TYPE, new TaskSelectionModelChangeEvent.Handler()
    {
      @Override
      public void taskSelectionModelChange(TaskSelectionModelChangeEvent event)
      {
        selectedIterationTask = event.getSelectedObject();
        manageButtonState();
      }
    });

    //handler to listen action to choose a bug
    backlogEventBus.addHandler(BugAssociatedToTaskEvent.TYPE, new BugAssociatedToTaskEventHandler()
    {
      @Override
      public void onLinkBugToTask(BugAssociatedToTaskEvent bugAssociatedToTaskEvent)
      {
        final ShowTaskEditViewEvent showTaskEditViewEvent = new ShowTaskEditViewEvent();
        showTaskEditViewEvent.setMode(Mode.CREATION);
        showTaskEditViewEvent.setTask(bugAssociatedToTaskEvent.getTask());
        globalEventBus.fireEvent(showTaskEditViewEvent);
      }
    });

		/* buttons action */
    display.getCreateBugTaskButton().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(ClickEvent event)
      {
        final TaskDTO taskDTO = new TaskDTO();
        taskDTO.setIteration(iteration);
        taskDTO.setType(TaskTypeEnum.BUG);
        getChooseBugPresenter().setTask(taskDTO);
        getChooseBugPresenter().go(display.getContentPanel());
      }
    });
    display.getCreateWorkTaskButton().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(ClickEvent event)
      {
        final TaskDTO taskDTO = new TaskDTO();
        taskDTO.setIteration(iteration);
        taskDTO.setType(TaskTypeEnum.WORK);
        getScopeUnitDisciplinePopupPresenter().setTask(taskDTO);
        getScopeUnitDisciplinePopupPresenter().go(display.getContentPanel());
      }
    });
    display.getUpdateTaskButton().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(ClickEvent event)
      {
        final ShowTaskEditViewEvent showTaskEditViewEvent = new ShowTaskEditViewEvent();
        showTaskEditViewEvent.setMode(Mode.MODIFICATION);
        showTaskEditViewEvent.setTask(selectedIterationTask.getTask());
        globalEventBus.fireEvent(showTaskEditViewEvent);
      }
    });
    display.getReportTaskButton().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(ClickEvent event)
      {
        final ShowTaskEditViewEvent showTaskEditViewEvent = new ShowTaskEditViewEvent();
        showTaskEditViewEvent.setMode(Mode.SUPERVISION);
        showTaskEditViewEvent.setTask(selectedIterationTask.getTask());
        globalEventBus.fireEvent(showTaskEditViewEvent);
      }
    });
    display.getDeleteButton().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(ClickEvent event)
      {
        deleteTask(selectedIterationTask.getTask().getId());
      }
    });
    display.getButtonExportCSV().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(ClickEvent event)
      {
        taskListPresenter.createCSVFormat();
      }
    });
    display.getIterationMonitoringButton().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(ClickEvent event)
      {
        Common.GLOBAL_EVENT_BUS.fireEvent(new ShowIterationMonitoringEvent(iteration));
      }
    });
    display.getEndIterationButton().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(ClickEvent event)
      {
        display.getValidateFinishIteration().getDialogPanel().center();
      }
    });
    display.getValidateFinishIteration().getValidate().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(ClickEvent event)
      {
        closeIteration.retry(0);
        display.getValidateFinishIteration().getDialogPanel().hide();
      }
    });
  }

  /**
   * This method disable all actions if mode if READ
   */
  private void enableUI()
  {
    boolean bool = true;
    if (AccessRight.READ.equals(SessionData.getAccessRight(ApplicativeFunction.FUNCTION_MANAGING_TASK)))
    {
      bool = false;
    }
    display.getCreateBugTaskButton().setEnabled(bool);
    display.getCreateWorkTaskButton().setEnabled(bool);
    display.getUpdateTaskButton().setEnabled(bool);
    display.getDeleteButton().setEnabled(bool);
    display.getEndIterationButton().setEnabled(bool);
  }

  /**
   * This methods put the button in correct state (enable/disable) using context
   */
  private void manageButtonState()
  {
    final boolean hasTaskSelected     = selectedIterationTask != null;
    final boolean hasIteration        = iteration != null;
    boolean       isIterationFinished = false;
    if (hasIteration)
    {
      isIterationFinished = iteration.isFinished();
    }
    if (!AccessRight.READ.equals(SessionData.getAccessRight(ApplicativeFunction.FUNCTION_MANAGING_TASK)))
    {
      display.getUpdateTaskButton().setEnabled(hasIteration && hasTaskSelected && !isIterationFinished);
      display.getDeleteButton().setEnabled(hasIteration && hasTaskSelected && !isIterationFinished);
      display.getCreateBugTaskButton().setEnabled(hasIteration && !isIterationFinished);
      display.getCreateWorkTaskButton().setEnabled(hasIteration && !isIterationFinished);
      display.getEndIterationButton().setEnabled(hasIteration && !isIterationFinished);
      display.getReportTaskButton().setEnabled(hasIteration && hasTaskSelected);
  
      boolean canTaskBeDeleted =
          hasIteration && hasTaskSelected && selectedIterationTask.getTask().getConsumedTime() == 0F
              && (selectedIterationTask.getTask().getStatus().getEnumValue().equals(TaskStatusEnum.NOT_STARTED)
                      || selectedIterationTask.getTask().getStatus().getEnumValue().equals(TaskStatusEnum.NOT_AFFECTED));
      display.getDeleteButton().setEnabled(canTaskBeDeleted);
    }
  }

  /**
   * Get the ChooseBugPopUpPresenter (create oen if needed)
   */
  private ChooseBugPopUpPresenter getChooseBugPresenter()
  {
    if (this.chooseBugPresenter == null)
    {
      this.chooseBugPresenter = new ChooseBugPopUpPresenter(backlogEventBus);
    }
    return chooseBugPresenter;
  }

  /**
   * Get the ScopeUnitDisciplinePopupPresenter (create oen if needed)
   */
  private ScopeUnitDisciplinePopupPresenter getScopeUnitDisciplinePopupPresenter()
  {
    if (this.scopeUnitDisciplinePopupPresenter == null)
    {
      this.scopeUnitDisciplinePopupPresenter = new ScopeUnitDisciplinePopupPresenter(globalEventBus);
    }
    return scopeUnitDisciplinePopupPresenter;
  }

  /**
   * Get user's disciplines by his access level
   */
  private void deleteTask(final Long taskId)
  {
    new AbstractManagementRPCCall<Boolean>()
    {
      @Override
      protected void callService(AsyncCallback<Boolean> pCb)
      {
        Common.TASK_SERVICE.deleteTask(taskId, pCb);
      }

      @Override
      public void onSuccess(Boolean pResult)
      {
        InfoDialogBox box;
        if (pResult)
        {
          box = new InfoDialogBox(Common.MESSAGES_BACKLOG.deleteTaskSuccessMessage(), InfoTypeEnum.OK);
        }
        else
        {
          box = new InfoDialogBox(Common.MESSAGES_BACKLOG.deleteTaskFailMessage(), InfoTypeEnum.KO);
        }
        box.getDialogPanel().center();
        loadDataOnSelectionTab();
      }

      @Override
      public void onFailure(Throwable pCaught) {
            ErrorManagement.displayErrorMessage(pCaught);
         }
      }.retry(0);
   }

   @Override
   public void loadDataOnSelectionTab()
   {
      //direct click without choosing iteration -> we display default one
      if (iteration == null)
      {
         getDefaultIterationAndDoDisplay.retry(0);
      }
      else
      {
         completeSelectedIterationAndDoDisplay.retry(0);
      }
   }

   @Override
   public IsWidget getDisplay()
   {
      return display.asWidget();
   }

   public void setIteration(IterationDTO pIteration)
   {
      //if the iteration is changing
      if (iteration == null || iteration.getIterationId() != pIteration.getIterationId())
      {
         iteration = pIteration;
         selectedIterationTask = null;
         manageButtonState();
      }
   }

  /**
	 * Common instructions to display context and task list
	 */
	private void displaySelectedIteration() {
		iterationContextPresenter.updateContext(iteration);
		iterationContextPresenter.go(display.getIterationContextPanel());
		fillDisciplinesListBox();
		List<IterationTaskDTO> taskList = new ArrayList<IterationTaskDTO>(iteration.getIterationTasks());
		taskListPresenter.initializeTaskList(taskList);
		taskListPresenter.go(display.getTasksPanel());
  }

   /**
    * Fill the disciplines list box
    */
   private void fillDisciplinesListBox() {
      taskListPresenter.getDisciplines().clear();
      taskListPresenter.getDisciplines().addItem(Common.EMPTY_TEXT);
      getAllDisciplines.retry(0);
   }
   
	@Override
  public void go(HasWidgets container)
  {
    container.clear();
		container.add(display.asWidget());
	}
}
