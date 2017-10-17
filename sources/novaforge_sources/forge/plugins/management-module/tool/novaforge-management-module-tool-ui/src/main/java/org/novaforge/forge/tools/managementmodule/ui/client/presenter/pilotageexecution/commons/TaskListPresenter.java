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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.pilotageexecution.commons;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.OnSelectionTabEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.TaskSelectionModelChangeEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.view.pilotageexecution.commons.TaskListView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.pilotageexecution.commons.TaskListViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationTaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskListEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author BILET-JC
 * 
 *         This class represents the list of task and its filters When selection
 *         tab change, do not forget to use the OnSelectionTabEvent event.
 */
public class TaskListPresenter implements Presenter {

	public static final String PREPARATION_CSV_NAME = "CSVPreparation";
	public static final String BACKLOG_CSV_NAME     = "CSVBacklog";
	private final SimpleEventBus localEventBus;
	private final TaskListView display;
	private final TaskListEnum type;
	private Set<IterationTaskDTO> iterationTasks;
	
	public TaskListPresenter(SimpleEventBus localEventBus, TaskListEnum type) {
		this.localEventBus = localEventBus;
		this.type = type;
		display = new TaskListViewImpl(type);
		bind();
	}
	
	private void bind() {
		localEventBus.addHandler(OnSelectionTabEvent.TYPE, new OnSelectionTabEvent.Handler() {

			@Override
			public void onSelectionTab(OnSelectionTabEvent event) {
				display.getDisciplines().clear();
				display.getDataProvider().setList(new ArrayList<IterationTaskDTO>());
				if (display.getSelectionModel().getSelectedObject() != null) {
					display.getSelectionModel().setSelected(display.getSelectionModel().getSelectedObject(), false);
				}
			}
		});
		display.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(final SelectionChangeEvent pEvent) {
				final TaskSelectionModelChangeEvent event = new TaskSelectionModelChangeEvent(display
						.getSelectionModel().getSelectedObject());
				localEventBus.fireEvent(event);
			}
		});

		display.getDisciplines().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				filteredList();
			}
		});
		display.getDisciplines().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				filteredList();
			}
		});
		display.getScopeUnitParents().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				filteredList();
			}
		});
		display.getScopeUnitParents().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				filteredList();
			}
		});
		display.getScopeUnits().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				filteredList();
			}
		});
		display.getScopeUnits().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				filteredList();
			}
		});
	}

	private void filteredList() {
		final String discipline = display.getDisciplines().getItemText(display.getDisciplines().getSelectedIndex());
		final String scopeUnitParent;
		if (display.getScopeUnitParents().isVisible()) {
			scopeUnitParent = display.getScopeUnitParents().getItemText(
					display.getScopeUnitParents().getSelectedIndex());
		} else {
			scopeUnitParent = Common.EMPTY_TEXT;
		}
		final String scopeUnit;
		if (display.getScopeUnits().isVisible()) {
			scopeUnit = display.getScopeUnits().getItemText(display.getScopeUnits().getSelectedIndex());
		} else {
			scopeUnit = Common.EMPTY_TEXT;
		}

		Collection<IterationTaskDTO> filter = filteredOnProject(new ArrayList<IterationTaskDTO>(iterationTasks),
				discipline, scopeUnitParent, scopeUnit);
		refreshList(new ArrayList<IterationTaskDTO>(filter));
	}

	private Collection<IterationTaskDTO> filteredOnProject(final List<IterationTaskDTO> pIterationTask,
			final String discipline, final String scopeUnitParent, final String scopeUnit) {
		return Collections2.filter(pIterationTask, new Predicate<IterationTaskDTO>()
		{

			@Override
			public boolean apply(IterationTaskDTO input) {
				boolean ret;
				if (!discipline.equals(Common.EMPTY_TEXT)) {
					if (!scopeUnit.equals(Common.EMPTY_TEXT)) {
						if (input.getTask().getScopeUnit() != null) {
							if (!scopeUnitParent.equals(Common.EMPTY_TEXT)) {
								if (input.getTask().getDiscipline().getLibelle().equals(discipline)
										&& input.getTask().getScopeUnit().getName().equals(scopeUnit)
										&& input.getTask().getParentScopeUnitName().equals(scopeUnitParent)) {
									ret = true;
								} else {
									ret = false;
								}
							} else {
								if (input.getTask().getDiscipline().getLibelle().equals(discipline)
										&& input.getTask().getScopeUnit().getName().equals(scopeUnit)) {
									ret = true;
								} else {
									ret = false;
								}
							}
						} else {
							ret = false;
						}
					} else {
						if (!scopeUnitParent.equals(Common.EMPTY_TEXT)) {
							if (input.getTask().getDiscipline().getLibelle().equals(discipline)
									&& input.getTask().getParentScopeUnitName().equals(scopeUnitParent)) {
								ret = true;
							} else {
								ret = false;
							}
						} else {
							if (input.getTask().getDiscipline().getLibelle().equals(discipline)) {
								ret = true;
							} else {
								ret = false;
							}
						}
					}
				} else {
					if (!scopeUnit.equals(Common.EMPTY_TEXT)) {
						if (input.getTask().getScopeUnit() != null) {
							if (!scopeUnitParent.equals(Common.EMPTY_TEXT)) {
								if (input.getTask().getScopeUnit().getName().equals(scopeUnit)
										&& input.getTask().getParentScopeUnitName().equals(scopeUnitParent)) {
									ret = true;
								} else {
									ret = false;
								}
							} else {
								if (input.getTask().getScopeUnit().getName().equals(scopeUnit)) {
									ret = true;
								} else {
									ret = false;
								}
							}
						} else {
							ret = false;
						}
					} else {
						if (!scopeUnitParent.equals(Common.EMPTY_TEXT)) {
							if (input.getTask().getParentScopeUnitName().equals(scopeUnitParent)) {
								ret = true;
							} else {
								ret = false;
							}
						} else {
							ret = true;
						}
					}
				}
				return ret;
			}
		});
	}

	/**
	 * Create the scopeUnits' list filter and display given list in the table
	 *
	 * @param pList
	 */
	private void refreshList(final List<IterationTaskDTO> pList) {
	   resetSelectionModel();
	   Collections.sort(pList, display.getTaskComparator());
		display.getDataProvider().setList(pList);
		display.updateSortHandler();
	}

	/**
    * Reset the selectionModel
    */
   private void resetSelectionModel() {
      if (display.getSelectionModel().getSelectedObject() != null) {
         display.getSelectionModel().setSelected(display.getSelectionModel().getSelectedObject(), false);
      }
   }

	/**
	 * Create a CSV format of given data which is put in session.
	 * On success is called a servlet which create a file which session put in session
	 *
	 * @param displayedList
	 */
	public void createCSVFormat()
	{
		new AbstractManagementRPCCall<Boolean>()
		{

			@Override
			protected void callService(AsyncCallback<Boolean> cb)
			{
				Common.TASK_SERVICE.createCSVFormatWithIterationTaskDTOList(new ArrayList<IterationTaskDTO>(display
																																																				.getDataProvider()
																																																				.getList()),
																																		cb);
			}

			@Override
			public void onFailure(Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught.getMessage());

			}

			@Override
			public void onSuccess(Boolean result)
			{
				Map<String, String> parameters = new HashMap<String, String>();
				if (TaskListEnum.PREPARATION.equals(type))
				{
					parameters.put(Constants.EXPORT_CSV_NAME_PARAMETER, PREPARATION_CSV_NAME);
				}
				else if (TaskListEnum.BACKLOG.equals(type))
				{
					parameters.put(Constants.EXPORT_CSV_NAME_PARAMETER, BACKLOG_CSV_NAME);
				}
				Common.exportCSV(GWT.getModuleBaseURL() + Constants.EXPORT_CSV_SERVLET_NAME, parameters);
			}
		}.retry(0);
	}

	/**
	 * Called by the container presenter to initialize task list
	 *
	 * @param pList
	 */
	public void initializeTaskList(final List<IterationTaskDTO> pList)
	{
		iterationTasks = new HashSet<IterationTaskDTO>(pList);
		createScopeUnitLists();
		refreshList(pList);
	}

	/**
	 * Add scope unit and parent's scope unit name to the correct list box
	 * filter
	 */
	private void createScopeUnitLists()
	{
		Set<String> scopeUnits       = new HashSet<String>();
		Set<String> parentScopeUnits = new HashSet<String>();
		//basic string comparator to display display filter's data in alphanumerical order
		final Comparator<String> scopeUnitListsComparator = new Comparator<String>()
		{
			@Override
			public int compare(String o1, String o2)
			{
				return o1.compareTo(o2);
			}
		};
		display.getScopeUnits().clear();
		display.getScopeUnitParents().clear();
		for (IterationTaskDTO iterationTask : iterationTasks)
		{
			if (iterationTask.getTask().getScopeUnit() != null)
			{
				scopeUnits.add(iterationTask.getTask().getScopeUnit().getName());
				if (!iterationTask.getTask().getParentScopeUnitName().equals(Common.EMPTY_TEXT))
				{
					parentScopeUnits.add(iterationTask.getTask().getParentScopeUnitName());
				}
			}
		}
		if (scopeUnits.size() > 0)
		{
			Collections.sort(new ArrayList<String>(scopeUnits), scopeUnitListsComparator);
			display.enableScopeUnitFilter(true);
			display.getScopeUnits().addItem(Common.EMPTY_TEXT);
			for (String name : scopeUnits)
			{
				display.getScopeUnits().addItem(name);
			}
		}
		else
		{
			display.enableScopeUnitFilter(false);
		}
		if (parentScopeUnits.size() > 0)
		{
			Collections.sort(new ArrayList<String>(parentScopeUnits), scopeUnitListsComparator);
			display.enableScopeUnitParentFilter(true);
			display.getScopeUnitParents().addItem(Common.EMPTY_TEXT);
			for (String name : parentScopeUnits)
			{
				display.getScopeUnitParents().addItem(name);
			}
		}
		else
		{
			display.enableScopeUnitParentFilter(false);
		}
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	@Override
	public IsWidget getDisplay() {
		return display.asWidget();
	}

	public ListBox getDisciplines() {
		return display.getDisciplines();
	}

}
