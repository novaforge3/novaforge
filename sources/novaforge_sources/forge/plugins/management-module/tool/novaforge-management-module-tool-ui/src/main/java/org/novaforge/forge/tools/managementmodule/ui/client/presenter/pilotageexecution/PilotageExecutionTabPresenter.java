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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.pilotageexecution;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.FinishTaskEditionEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.FinishTaskEditionEventHandler;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowBacklogEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowIterationListEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowIterationMonitoringEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowTaskEditViewEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowTaskEditViewEventHandler;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.backlog.BacklogPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.burndowniteration.BurnDownPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.globalmonitoring.GlobalMonitoringPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.iterationmonitoring.IterationMonitoringPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.preparation.PreparationPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.projectmonitoringgraph.ProjectMonitoringGraphPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.scopeunitdiscipline.ScopeUnitDisciplinePresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.task.TaskEditPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.view.pilotageexecution.PilotageExecutionTabView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.pilotageexecution.PilotageExecutionTabViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.AnchorsEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author BILET-JC
 * 
 */
public class PilotageExecutionTabPresenter implements Presenter {
  private Logger logger = Logger.getLogger(PilotageExecutionTabPresenter.class.getName());
  
	private final SimpleEventBus eventBus;
	private final PilotageExecutionTabView display;
	// the indexes of the tab
	int iterationListTab;
	int preparationTab;
	int globalMonitoringTab;
	int scopeUnitDisciplineTab;
	int iterationMonitoringTab;
	int backlogTab;
	int burndownTab;
	int projectMonitoringGraphTab;
	private BacklogPresenter backlogPresenter;

	// private IterationDTO currentIteration;
	private PreparationPresenter            preparationPresenter;
	private GlobalMonitoringPresenter       globalMonitoringPresenter;
	private IterationListPresenter          iterationListPresenter;
	private TaskEditPresenter               taskEditPresenter;
	private ScopeUnitDisciplinePresenter    scopeUnitDisciplinePresenter;
	private BurnDownPresenter               burnDownPresenter;
	private ProjectMonitoringGraphPresenter projectMonitoringGraphPresenter;
	private IterationMonitoringPresenter    iterationMonitoringPresenter;
	/** This map stocks the current presenter for a tab index */
	private Map<Integer, TabPresenter> presentersMap = new HashMap<Integer, TabPresenter>();
	/** This map stocks the default presenter for a tab index */
	private Map<Integer, TabPresenter> defaultPresentersByTabId = new HashMap<Integer, TabPresenter>();
	/** This map stocks the tab label for a tab index */
	private Map<Integer, String> titlesByTabId = new HashMap<Integer, String>();

	/**
	 * @param eventBus
	 * @param display
	 */
	public PilotageExecutionTabPresenter(final SimpleEventBus eventBus) {
		super();
		this.eventBus = eventBus;
		this.display = new PilotageExecutionTabViewImpl();
		bind();
		insertPanels();
	}

	/**
	 * This method is the interface between the presenter and the view
	 */
	private void bind() {
		display.getTabPanel().addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				onSelectionTab(event);
			}
		});

		// EVENTBUS
		eventBus.addHandler(ShowBacklogEvent.TYPE, new ShowBacklogEvent.Handler() {

			@Override
			public void showBacklog(ShowBacklogEvent event) {
				getBacklogPresenter().setIteration(event.getIteration());
				display.getTabPanel().selectTab(backlogTab);
				replaceTabDatas(backlogTab, getBacklogPresenter());
			}
		});
		eventBus.addHandler(ShowIterationMonitoringEvent.TYPE, new ShowIterationMonitoringEvent.Handler() {

			@Override
			public void showIterationMonitoring(ShowIterationMonitoringEvent event) {
				getIterationMonitoringPresenter().setIteration(event.getSelectedObject());
				display.getTabPanel().selectTab(iterationMonitoringTab);
			}
		});

		eventBus.addHandler(ShowIterationListEvent.TYPE, new ShowIterationListEvent.Handler() {

			@Override
			public void showBacklog(ShowIterationListEvent event) {
				display.getTabPanel().selectTab(iterationListTab);
			}
		});
		eventBus.addHandler(ShowTaskEditViewEvent.TYPE, new ShowTaskEditViewEventHandler() {
			@Override
			public void onShowTaskEditView(ShowTaskEditViewEvent event) {
				replaceTabDatas(display.getTabPanel().getSelectedIndex(), getTaskEditPresenter());
				getTaskEditPresenter().setMode(event.getMode());
				getTaskEditPresenter().setTask(event.getTask());
			}
		});
		eventBus.addHandler(FinishTaskEditionEvent.TYPE, new FinishTaskEditionEventHandler() {
			@Override
			public void onFinishTaskEdition(final FinishTaskEditionEvent taskEditionEvent) {
        if (display.getTabPanel().getSelectedIndex() == preparationTab)
        {
          logger.log(Level.FINEST,"Redirection vers l'onglet preparation numero=" + display.getTabPanel().getSelectedIndex());
          setDefaultDatasIntoTab(preparationTab);
        }
        else
          if (display.getTabPanel().getSelectedIndex() == backlogTab)
        {
            logger.log(Level.FINEST,"Redirection vers l'onglet backlog numero=" + display.getTabPanel().getSelectedIndex());
          setDefaultDatasIntoTab(backlogTab);
        } 
          else
        {
          logger.log(Level.WARNING,"Redirection inattendue vers l'onglet numero=" + display.getTabPanel().getSelectedIndex());
          setDefaultDatasIntoTab(display.getTabPanel().getSelectedIndex());
        }
			}
		});
	}

	private void insertPanels() {
		this.display.getTabPanel().clear();
		final AccessRight accessRightPreparation = SessionData
				.getAccessRight(ApplicativeFunction.FUNCTION_PREPARATION_ITERATION);
		final AccessRight accessRightManageTasks = SessionData
				.getAccessRight(ApplicativeFunction.FUNCTION_MANAGING_TASK);
		final AccessRight accessRightSuiviIteration = SessionData
				.getAccessRight(ApplicativeFunction.FUNCTION_ITERATION_MONITORING);
		final AccessRight accessRightSuiviGlobal = SessionData
				.getAccessRight(ApplicativeFunction.FUNCTION_GLOBAL_MONITORING);
   final AccessRight accessRightBurndown = SessionData
	        .getAccessRight(ApplicativeFunction.FUNCTION_BURNDOWN);
	
		this.display.getTabPanel().add(getIterationListPresenter().getDisplay(),
				Common.MESSAGES_BACKLOG.headerIterationListTab());
		iterationListTab = this.display.getTabPanel()
				.getWidgetIndex(getIterationListPresenter().getDisplay());
		presentersMap.put(iterationListTab, getIterationListPresenter());
		titlesByTabId.put(iterationListTab, Common.MESSAGES_BACKLOG.headerIterationListTab());

		// scopeUnit discipline
		if (!accessRightPreparation.equals(AccessRight.NONE)) {
			this.display.getTabPanel().add(getScopeUnitDisciplinePresenter().getDisplay(),
					Common.MESSAGES_BACKLOG.headerScopeUnitDisciplineTab());
			scopeUnitDisciplineTab = this.display.getTabPanel().getWidgetIndex(
					getScopeUnitDisciplinePresenter().getDisplay());
			presentersMap.put(scopeUnitDisciplineTab, getScopeUnitDisciplinePresenter());
			titlesByTabId.put(scopeUnitDisciplineTab, Common.MESSAGES_BACKLOG.headerScopeUnitDisciplineTab());
		}

		// preparation
		if (!accessRightPreparation.equals(AccessRight.NONE)) {
			this.display.getTabPanel().add(getPreparationPresenter().getDisplay(),
					Common.MESSAGES_BACKLOG.headerPreparationTab());
			preparationTab = this.display.getTabPanel()
					.getWidgetIndex(getPreparationPresenter().getDisplay());
			presentersMap.put(preparationTab, getPreparationPresenter());
			titlesByTabId.put(preparationTab, Common.MESSAGES_BACKLOG.headerPreparationTab());
		}

		// backlog
		if (!accessRightManageTasks.equals(AccessRight.NONE)) {
			this.display.getTabPanel().add(getBacklogPresenter().getDisplay(),
					Common.MESSAGES_BACKLOG.headerBacklogTab());
			backlogTab = this.display.getTabPanel().getWidgetIndex(getBacklogPresenter().getDisplay());
			presentersMap.put(backlogTab, getBacklogPresenter());
			titlesByTabId.put(backlogTab, Common.MESSAGES_BACKLOG.headerBacklogTab());
		}

		// iteration monitoring
		if (!accessRightSuiviIteration.equals(AccessRight.NONE)) {
			this.display.getTabPanel().add(getIterationMonitoringPresenter().getDisplay(),
					Common.MESSAGES_BACKLOG.headerIterationMonitoringTab());
			iterationMonitoringTab = this.display.getTabPanel().getWidgetIndex(
					getIterationMonitoringPresenter().getDisplay());
			presentersMap.put(iterationMonitoringTab, getIterationMonitoringPresenter());
			titlesByTabId.put(iterationMonitoringTab, Common.MESSAGES_BACKLOG.headerIterationMonitoringTab());
		}

		// global monitoring
		if (!accessRightSuiviGlobal.equals(AccessRight.NONE)) {
			this.display.getTabPanel().add(getGlobalMonitoringPresenter().getDisplay(),
					Common.MESSAGES_BACKLOG.headerGlobalMonitoringTab());
			globalMonitoringTab = this.display.getTabPanel().getWidgetIndex(
					getGlobalMonitoringPresenter().getDisplay());
			presentersMap.put(globalMonitoringTab, getGlobalMonitoringPresenter());
			titlesByTabId.put(globalMonitoringTab, Common.MESSAGES_BACKLOG.headerGlobalMonitoringTab());
		}

		if (!accessRightBurndown.equals(AccessRight.NONE)) {
			this.display.getTabPanel().add(getBurnDownPresenter().getDisplay(),
					Common.MESSAGES_BACKLOG.headerBurnDownTab());
			burndownTab = this.display.getTabPanel().getWidgetIndex(
					getBurnDownPresenter().getDisplay());
			presentersMap.put(burndownTab, getBurnDownPresenter());
			titlesByTabId.put(burndownTab, Common.MESSAGES_BACKLOG.headerBurnDownTab());
		}

		if (!accessRightSuiviGlobal.equals(AccessRight.NONE)) {

			this.display.getTabPanel().add(getProjectMonitoringGraphPresenter().getDisplay(),
					Common.MESSAGES_BACKLOG.headerProjectMonitoringGraphTab());
			projectMonitoringGraphTab = this.display.getTabPanel().getWidgetIndex(
					getProjectMonitoringGraphPresenter().getDisplay());
			presentersMap.put(projectMonitoringGraphTab, getProjectMonitoringGraphPresenter());
			titlesByTabId.put(projectMonitoringGraphTab,
					Common.MESSAGES_BACKLOG.headerProjectMonitoringGraphTab());
		}

		// the presenters we put in presentersMap at initialization are the
		// default map, so we do initialization of default map with it
		defaultPresentersByTabId.putAll(presentersMap);

		// a selectTab on the base selected panel will not produce event and the
		// loadDataOnSelectionTab() call so we manually call it now
		presentersMap.get(this.display.getTabPanel().getSelectedIndex()).loadDataOnSelectionTab();
	}

	private void onSelectionTab(SelectionEvent<Integer> event)
	{
		// null at insertPanels
		if (presentersMap.get(event.getSelectedItem()) != null)
		{
			presentersMap.get(event.getSelectedItem()).loadDataOnSelectionTab();
		}
	}

	private BacklogPresenter getBacklogPresenter() {
		if (null == backlogPresenter) {
			backlogPresenter = new BacklogPresenter(eventBus);
		}
		return backlogPresenter;
	}

	/**
	 * This methods replace the datas of a tab (changing presenter)
	 *
	 * @param tabNumber
	 *            the tab index to change
	 * @param tabPresenter
	 *            the presenter to set
	 */
	private void replaceTabDatas(int tabNumber, TabPresenter tabPresenter) {
		final int initialWidgetCount = display.getTabPanel().getWidgetCount();
		presentersMap.put(tabNumber, tabPresenter);
		final String message = titlesByTabId.get(tabNumber);
		display.getTabPanel().insert(tabPresenter.getDisplay(), message, tabNumber);
		try {
			display.getTabPanel().selectTab(tabNumber);
		}
		// in a finally block to avoid tab duplication on uncaught exception
		finally {
			// if duplication (ie not same display inserted)
			if (display.getTabPanel().getWidgetCount() != initialWidgetCount) {
				// remove after the insert to avoid first tab selection and
				// loading
				display.getTabPanel().remove(tabNumber + 1);
			}
		}
	}

	/**
	 * Get the iterationMonitoringPresenter
	 *
	 * @return the iterationMonitoringPresenter
	 */
	private IterationMonitoringPresenter getIterationMonitoringPresenter()
	{
		if (iterationMonitoringPresenter == null)
		{
			iterationMonitoringPresenter = new IterationMonitoringPresenter();
		}
		return iterationMonitoringPresenter;
	}

	private TaskEditPresenter getTaskEditPresenter()
	{
		if (taskEditPresenter == null)
		{
			taskEditPresenter = new TaskEditPresenter(eventBus);
		}
		return taskEditPresenter;
	}

	/**
	 * Reset the tab to its default presenter
	 *
	 * @param tabIndex
	 *            the index of the tab to modify
	 */
	private void setDefaultDatasIntoTab(int tabIndex)
	{
		replaceTabDatas(tabIndex, defaultPresentersByTabId.get(tabIndex));
	}

	private IterationListPresenter getIterationListPresenter()
	{
		if (iterationListPresenter == null)
		{
			iterationListPresenter = new IterationListPresenter(eventBus);
		}
		return iterationListPresenter;
	}

	private ScopeUnitDisciplinePresenter getScopeUnitDisciplinePresenter()
	{
		if (null == scopeUnitDisciplinePresenter)
		{
			scopeUnitDisciplinePresenter = new ScopeUnitDisciplinePresenter();
		}
		return scopeUnitDisciplinePresenter;
	}

	private PreparationPresenter getPreparationPresenter()
	{
		if (null == preparationPresenter)
		{
			preparationPresenter = new PreparationPresenter(eventBus);
		}
		return preparationPresenter;
	}

	private GlobalMonitoringPresenter getGlobalMonitoringPresenter()
	{
		if (null == globalMonitoringPresenter)
		{
			globalMonitoringPresenter = new GlobalMonitoringPresenter();
		}
		return globalMonitoringPresenter;
	}

	private BurnDownPresenter getBurnDownPresenter()
	{
		if (burnDownPresenter == null)
		{
			burnDownPresenter = new BurnDownPresenter(eventBus);
		}
		return burnDownPresenter;
	}

	private ProjectMonitoringGraphPresenter getProjectMonitoringGraphPresenter()
	{
		if (projectMonitoringGraphPresenter == null)
		{
			projectMonitoringGraphPresenter = new ProjectMonitoringGraphPresenter();
		}
		return projectMonitoringGraphPresenter;
	}

	@Override
	public void go(HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());
	}

	@Override
	public IsWidget getDisplay()
	{
		return this.display.asWidget();
	}

	public void setTab(AnchorsEnum anchor) {
		switch (anchor) {
		case SCOPE_UNIT_DISCIPLINE_TAB:
			selectTab(scopeUnitDisciplineTab);
			break;
		case PREPARATION_TAB:
			selectTab(preparationTab);
			break;
		case GLOBAL_MONITORING_TAB:
			selectTab(globalMonitoringTab);
			break;
		case REF_ITERATION_TAB:
			selectTab(backlogTab);
			break;
		case ITERATION_MONITORING_TAB:
			selectTab(iterationMonitoringTab);
			break;
		case BURNDOWN_IT_TAB:
			selectTab(burndownTab);
			break;
		case PROJECT_MONITORING_GRAPH_TAB:
			selectTab(projectMonitoringGraphTab);
			break;
	   case BACKLOG_LIST:
		default:
			selectTab(iterationListTab);
			break;
		}

	}

	/**
	 * Select a tab in the tab panel
	 *
	 * @param tabIndex
	 *     the index to select
	 */
	private void selectTab(int tabIndex)
	{
		// if its the same index, the onSelection is not call so we manually
		// call loadDataOnSelectionChange
		if (tabIndex == display.getTabPanel().getSelectedIndex())
		{
			presentersMap.get(tabIndex).loadDataOnSelectionTab();
		}
		display.getTabPanel().selectTab(tabIndex);
	}

}
