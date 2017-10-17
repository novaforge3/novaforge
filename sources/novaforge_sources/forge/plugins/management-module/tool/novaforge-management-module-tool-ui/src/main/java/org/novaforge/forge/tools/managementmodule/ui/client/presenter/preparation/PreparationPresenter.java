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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.preparation;

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
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.OnSelectionTabEvent;
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
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.preparation.PreparationView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.preparation.PreparationViewImpl;
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
public class PreparationPresenter implements TabPresenter {

	private final SimpleEventBus globalEventBus;
	private final PreparationView display;
	/**
	 * A specific local event bus used to communicate with the components of
	 * this presenter (view, task list presenter ...)
	 */
	private final SimpleEventBus preparationEventBus = new SimpleEventBus();
	private IterationDTO      iteration;
	private TaskListPresenter taskListPresenter;
	AbstractManagementRPCCall<List<DisciplineDTO>> getAllDisciplines = new AbstractManagementRPCCall<List<DisciplineDTO>>()
	{

		@Override
		protected void callService(AsyncCallback<List<DisciplineDTO>> pCb)
		{
			Common.REFERENTIAL_SERVICE.getAllPhareDTOList(pCb);
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

		@Override
		public void onFailure(Throwable pCaught)
		{
			ErrorManagement.displayErrorMessage(pCaught);
		}
	};
	private IterationContextPresenter iterationContextPresenter;
	/**
	 * Get the next iteration to be prepared
	 */
	AbstractManagementRPCCall<IterationDTO> getNextComplexIteration     = new AbstractManagementRPCCall<IterationDTO>()
	{

		@Override
		protected void callService(AsyncCallback<IterationDTO> pCb)
		{
			Common.ITERATION_SERVICE.getNextIteration(SessionData.currentValidatedProjectPlanId, false, pCb);
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
				/* update and display the context */
				iterationContextPresenter.updateContext(iteration);
				iterationContextPresenter.go(display.getIterationContextPanel());
				/* get user's disciplines */

				fillDisciplinesListBox();
				//				getAllDisciplines.retry(0);
				/* update and display the tasks' list */
				List<IterationTaskDTO> taskList = new ArrayList<IterationTaskDTO>(iteration.getIterationTasks());
				taskListPresenter.initializeTaskList(taskList);
				taskListPresenter.go(display.getTasksPanel());
				/* display buttons */
				if (AccessRight.WRITE.equals(SessionData.getAccessRight(ApplicativeFunction.FUNCTION_PREPARATION_ITERATION)))
		    {
  				display.setActionButtonsVisible(true);
  				display.getCreateWorkTaskButton().setEnabled(true);
  				display.getCreateBugTaskButton().setEnabled(true);
		    }
			}
			// not allowed and first time the tab is displayed : error message
			else if (display.getCreateWorkTaskButton().isEnabled())
			{
				final InfoDialogBox dialogBox = new InfoDialogBox(Common.MESSAGES_BACKLOG.noMorePreparationMessage(),
																													InfoTypeEnum.WARNING);
				dialogBox.getDialogPanel().center();
				dialogBox.getDialogPanel().show();
				display.getCreateWorkTaskButton().setEnabled(false);
				display.getCreateBugTaskButton().setEnabled(false);
			}
		}

	};
	/**
	 * Check if it is allowed to preparate a new iteration. Only 2 not finished
	 * current iteration are allowed.
	 */
	AbstractManagementRPCCall<Boolean>      isAllowIterationPreparation = new AbstractManagementRPCCall<Boolean>()
	{

		@Override
		protected void callService(AsyncCallback<Boolean> pCb)
		{
			Common.TASK_SERVICE.isAllowIterationPreparation(SessionData.currentValidatedProjectPlanId, pCb);
		}

		@Override
		public void onFailure(Throwable pCaught)
		{
			ErrorManagement.displayErrorMessage(pCaught);
		}

		@Override
		public void onSuccess(Boolean pResult)
		{
			if (pResult != null)
			{
				// allowed
				if (pResult)
				{
					getNextComplexIteration.retry(0);
				}
				// not allowed and first time the tab is displayed : error
				// message
				else if (display.getCreateWorkTaskButton().isEnabled())
				{
					final InfoDialogBox dialogBox = new InfoDialogBox(Common.MESSAGES_BACKLOG.impossiblePreparationMessage(),
																														InfoTypeEnum.WARNING);
					dialogBox.getDialogPanel().center();
					dialogBox.getDialogPanel().show();
					display.getCreateWorkTaskButton().setEnabled(false);
					display.getCreateBugTaskButton().setEnabled(false);
				}
			}
		}

	};
	private ChooseBugPopUpPresenter           chooseBugPresenter;
	private ScopeUnitDisciplinePopupPresenter scopeUnitDisciplinePopupPresenter;
	/** The iteration task selected in the task list */
	private IterationTaskDTO                  selectedIterationTask;

	/**
	 * @param eventBus
	 */
	public PreparationPresenter(final SimpleEventBus eventBus)
	{
		super();
		this.globalEventBus = eventBus;
		iterationContextPresenter = new IterationContextPresenter(preparationEventBus);
		taskListPresenter = new TaskListPresenter(preparationEventBus, TaskListEnum.PREPARATION);
		display = new PreparationViewImpl();
		bind();
		enableUI();
	}

	//	/**
	//	 * Get user's disciplines by his access level
	//	 */
	//	AbstractManagementRPCCall<Set<DisciplineDTO>> getDisciplines = new AbstractManagementRPCCall<Set<DisciplineDTO>>() {
	//
	//		@Override
	//		protected void callService(AsyncCallback<Set<DisciplineDTO>> pCb) {
	//			Common.COMMON_SERVICE.getDisciplinesOfConnectedUser(SessionData.projectId, pCb);
	//		}
	//
	//		@Override
	//		public void onSuccess(Set<DisciplineDTO> pResult) {
	//			if (pResult != null) {
	//				taskListPresenter.getDisciplines().clear();
	//				taskListPresenter.getDisciplines().addItem(Common.EMPTY_TEXT);
	//				for (DisciplineDTO discipline : pResult) {
	//					taskListPresenter.getDisciplines().addItem(discipline.getLibelle());
	//				}
	//			}
	//		}
	//
	//		@Override
	//		public void onFailure(Throwable pCaught) {
	//			ErrorManagement.displayErrorMessage(pCaught);
	//		}
	//	};

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
		preparationEventBus.addHandler(TaskSelectionModelChangeEvent.TYPE, new TaskSelectionModelChangeEvent.Handler()
		{
			@Override
			public void taskSelectionModelChange(TaskSelectionModelChangeEvent event)
			{
				if (event.getSelectedObject() != null)
				{
	        if (AccessRight.WRITE.equals(SessionData.getAccessRight(ApplicativeFunction.FUNCTION_PREPARATION_ITERATION)))
	        {
	          display.getUpdateTaskButton().setEnabled(true);
	        }
					selectedIterationTask = event.getSelectedObject();
										 /*
                      * enable delete button only if task status is 'not started' or not affected and if consumed time = 0
                      */
					if (selectedIterationTask.getTask().getConsumedTime() == 0F && (selectedIterationTask.getTask().getStatus()
																																															 .getEnumValue()
																																															 .equals(TaskStatusEnum.NOT_STARTED)
																																							|| selectedIterationTask.getTask()
																																																			.getStatus()
																																																			.getEnumValue()
																																																			.equals(TaskStatusEnum.NOT_AFFECTED)))
					{
			       if (AccessRight.WRITE.equals(SessionData.getAccessRight(ApplicativeFunction.FUNCTION_PREPARATION_ITERATION)))
			        {
			         display.getDeleteButton().setEnabled(true);
			        }
					}
					else
					{
						display.getDeleteButton().setEnabled(false);
					}
				}
				else
				{
					display.getUpdateTaskButton().setEnabled(false);
					display.getDeleteButton().setEnabled(false);
				}
			}
		});
		// handler to listen action to choose a bug
		preparationEventBus.addHandler(BugAssociatedToTaskEvent.TYPE, new BugAssociatedToTaskEventHandler()
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
		display.getDeleteButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				// TODO JCB delete task not changing status
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
	}

	/**
	 * This method disable all actions if mode if READ
	 */
	private void enableUI()
	{
		boolean bool = true;
		if (!AccessRight.WRITE.equals(SessionData.getAccessRight(ApplicativeFunction.FUNCTION_PREPARATION_ITERATION)))
		{
			bool = false;
		}
		display.getCreateBugTaskButton().setEnabled(bool);
		display.getCreateWorkTaskButton().setEnabled(bool);
		display.getUpdateTaskButton().setEnabled(bool);
		display.getDeleteButton().setEnabled(bool);
	}

	/**
	 * Get the ChooseBugPopUpPresenter (create oen if needed)
	 */
	private ChooseBugPopUpPresenter getChooseBugPresenter()
	{
		if (this.chooseBugPresenter == null)
		{
			this.chooseBugPresenter = new ChooseBugPopUpPresenter(preparationEventBus);
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
				box.getDialogPanel().show();
				loadDataOnSelectionTab();
			}

			@Override
			public void onFailure(Throwable pCaught)
			{
				ErrorManagement.displayErrorMessage(pCaught);
			}
		}.retry(0);
	}

	@Override
	public void loadDataOnSelectionTab()
	{
		preparationEventBus.fireEvent(new OnSelectionTabEvent());
		isAllowIterationPreparation.retry(0);
	}

	@Override
	public IsWidget getDisplay()
	{
		return display.asWidget();
	}

	/**
	 * Fill the disciplines list box
	 */
	private void fillDisciplinesListBox()
	{
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
