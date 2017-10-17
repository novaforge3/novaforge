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
package org.novaforge.forge.tools.managementmodule.ui.client.view.pilotageexecution.commons;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.StylableTextCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationTaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskListEnum;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * @author BILET-JC
 * 
 */
public class TaskListViewImpl extends Composite implements TaskListView {

	public static final Comparator<IterationTaskDTO>           TASK_COMPARATOR = new Comparator<IterationTaskDTO>()
	{

		@Override
		public int compare(IterationTaskDTO o1, IterationTaskDTO o2)
		{
			return o1.getTask().getLabel().compareTo(o2.getTask().getLabel());
		}
	};
	private static      ListTaskViewImplUiBinder               uiBinder        = GWT.create(ListTaskViewImplUiBinder.class);
	private final       SingleSelectionModel<IterationTaskDTO> selectionModel  = new SingleSelectionModel<IterationTaskDTO>();
	@UiField                  Label                       taskListTitle;
	@UiField(provided = true) CellTable<IterationTaskDTO> iterationTaskCT;
	@UiField(provided = true) SimplePager                 iterationTaskPager;
	@UiField                  Label                       disciplineL;
	@UiField                  ListBox                     discipline;
	@UiField                  Label                       scopeUnitParentL;
	@UiField                  ListBox                     scopeUnitParent;
	@UiField                  Label                       scopeUnitL;
	@UiField                  ListBox                     scopeUnit;
	@UiField                  Label                       filterTitle;
	@UiField                  Label                       resultTitle;
	@UiField                  Label                       commentLabel;
	private Column<IterationTaskDTO, String>   labelC;
	private Column<IterationTaskDTO, String>   scopeUnitParentC;
	private Column<IterationTaskDTO, String>   scopeUnitC;
	private Column<IterationTaskDTO, String>   disciplineC;
	private Column<IterationTaskDTO, String>   responsibleC;
	private Column<IterationTaskDTO, String>   estimateTaskC;
	private Column<IterationTaskDTO, String>   statusC;
	private Column<IterationTaskDTO, String>   reestimateTaskC;
	private Column<IterationTaskDTO, String>   remainingTimeC;
	private Column<IterationTaskDTO, String>   consumedTimeC;
	private Column<IterationTaskDTO, String>   advancementC;
	private ListDataProvider<IterationTaskDTO> iterationTaskDataProvider;

	public TaskListViewImpl(TaskListEnum type)
	{
		initIterationTaskCT();
		initWidget(uiBinder.createAndBindUi(this));
		if (TaskListEnum.PREPARATION.equals(type))
		{
			taskListTitle.setText(Common.MESSAGES_BACKLOG.taskListPreparationTitle());
		}
		else if (TaskListEnum.BACKLOG.equals(type))
		{
			taskListTitle.setText(Common.MESSAGES_BACKLOG.taskListBacklogTitle());
		}
		filterTitle.setText(Common.GLOBAL.filterTitle());
		resultTitle.setText(Common.GLOBAL.resultTitle());
		disciplineL.setText(Common.MESSAGES_BACKLOG.disciplineL());
		scopeUnitParentL.setText(Common.MESSAGES_BACKLOG.scopeUnitParentL());
		scopeUnitL.setText(Common.MESSAGES_BACKLOG.scopeUnitL());
		commentLabel.setText(Common.MESSAGES_BACKLOG.commentConsumedAndRaf());
	}

	/**
	 * Init the iterationTask table
	 */
	private void initIterationTaskCT()
	{

		iterationTaskCT = new CellTable<IterationTaskDTO>(Common.PAGE_SIZE, (Resources) GWT.create(TableResources.class),
																											CellKey.ITERATION_TASK_KEY_PROVIDER);
		iterationTaskCT.setWidth("100%", false);

		// Init empty widget
		Label emptyProjectPlanListLabel = new Label(Common.MESSAGES_BACKLOG.emptyTaskList());
		emptyProjectPlanListLabel.setStyleName(Common.RESOURCE.css().emptyLabel());
		iterationTaskCT.setEmptyTableWidget(emptyProjectPlanListLabel);

		// Create a Pager to control the CellTable
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		iterationTaskPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		iterationTaskPager.setDisplay(iterationTaskCT);

		// Initialize the columns.
		initIterationTaskCTColumns();

		// Add the CellTable to the adapter
		iterationTaskDataProvider = new ListDataProvider<IterationTaskDTO>();
		iterationTaskDataProvider.addDataDisplay(iterationTaskCT);

		iterationTaskCT.setSelectionModel(selectionModel);

	}

	/**
	 * Init the columns of the iterationTask table
	 */
	private void initIterationTaskCTColumns()
	{
		final Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("align", "center");
		// Task label Column
		labelC = new Column<IterationTaskDTO, String>(new TextCell())
		{
			@Override
			public String getValue(IterationTaskDTO object)
			{
				return object.getTask().getLabel();
			}
		};
		labelC.setSortable(true);
		iterationTaskCT.addColumn(labelC, Common.MESSAGES_BACKLOG.label());
		iterationTaskCT.setColumnWidth(labelC, 200, Unit.PX);

		// scope unit Column
		scopeUnitC = new Column<IterationTaskDTO, String>(new TextCell())
		{
			@Override
			public String getValue(IterationTaskDTO object)
			{
				String ret = Common.EMPTY_TEXT;
				if (object.getTask().getScopeUnit() != null)
				{
					ret = object.getTask().getScopeUnit().getName();
				}
				return ret;

			}
		};
		scopeUnitC.setSortable(true);
		iterationTaskCT.addColumn(scopeUnitC, Common.getLinesHeader(Common.GLOBAL.SU()));

		// scope unit parent Column
		scopeUnitParentC = new Column<IterationTaskDTO, String>(new TextCell()) {
			@Override
			public String getValue(IterationTaskDTO object) {
				return object.getTask().getParentScopeUnitName();
			}
		};
		scopeUnitParentC.setSortable(true);
		iterationTaskCT.addColumn(scopeUnitParentC, Common.getLinesHeader(Common.GLOBAL.SUParent()));

		// discipline Column
		disciplineC = new Column<IterationTaskDTO, String>(new TextCell()) {
			@Override
			public String getValue(IterationTaskDTO object) {
				return object.getTask().getDiscipline().getLibelle();
			}
		};
		disciplineC.setSortable(true);
		iterationTaskCT.addColumn(disciplineC, Common.MESSAGES_BACKLOG.discipline());

		// responsible Column
		responsibleC = new Column<IterationTaskDTO, String>(new TextCell()) {
			@Override
			public String getValue(IterationTaskDTO object) {
				String ret = Common.EMPTY_TEXT;
				if (object.getTask().getUser() != null) {
					ret = getUserName(object);
				}
				return ret;
			}
		};
		responsibleC.setSortable(true);
		iterationTaskCT.addColumn(responsibleC, Common.MESSAGES_BACKLOG.responsible());

		// estimate task Column
		estimateTaskC = new Column<IterationTaskDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(IterationTaskDTO object) {
				return String.valueOf(Common.floatFormat(object.getTask().getInitialEstimation(),2));
			}
		};
		estimateTaskC.setSortable(true);
		iterationTaskCT.addColumn(estimateTaskC,
				Common.getLinesHeader(Common.MESSAGES_BACKLOG.taskEstimation()));

		// status Column
		statusC = new Column<IterationTaskDTO, String>(new TextCell()) {
			@Override
			public String getValue(IterationTaskDTO object) {

				return object.getTask().getStatus().getLabel();
			}
		};
		statusC.setSortable(true);
		iterationTaskCT.addColumn(statusC, Common.MESSAGES_BACKLOG.status());
		iterationTaskCT.setColumnWidth(statusC, 120, Unit.PX);

		// reestimate Column
		reestimateTaskC = new Column<IterationTaskDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(IterationTaskDTO object) {
			   float reestimate = object.getTask().getConsumedTime() + object.getTask().getRemainingTime();
				return Common.floatFormat(reestimate, 2).toString();
			}
		};
		reestimateTaskC.setSortable(true);
		iterationTaskCT.addColumn(reestimateTaskC,
				Common.getLinesHeader(Common.MESSAGES_BACKLOG.reestimateTask()));

		// consumed time Column
		consumedTimeC = new Column<IterationTaskDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(IterationTaskDTO object) {
				return String.valueOf(Common.floatFormat(object.getConsumedTime(),2));
			}
		};
		consumedTimeC.setSortable(true);
		iterationTaskCT.addColumn(consumedTimeC,
				Common.getLinesHeader(Common.MESSAGES_BACKLOG.consumedTime()));

		// remaining time Column
		remainingTimeC = new Column<IterationTaskDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(IterationTaskDTO object) {
				return String.valueOf(Common.floatFormat(object.getRemainingTime(),2));
			}
		};
		remainingTimeC.setSortable(true);
		iterationTaskCT.addColumn(remainingTimeC,
				Common.getLinesHeader(Common.MESSAGES_BACKLOG.remainingTime()));

		// advancement task Column
		advancementC = new Column<IterationTaskDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(IterationTaskDTO object) {
				return String.valueOf(Common.floatFormat(object.getAdvancement(), 2));
			}
		};
		advancementC.setSortable(true);
		iterationTaskCT.addColumn(advancementC, Common.getLinesHeader(Common.MESSAGES_BACKLOG.advancement()));
	}

	/**
	 * Return the complete name of the responsible of the task
	 *
	 * @param iteration
	 * @return
	 */
	private String getUserName(IterationTaskDTO iteration) {
		return iteration.getTask().getUser().getFirstName() + " "
				+ iteration.getTask().getUser().getLastName();
	}

	@Override
	public SingleSelectionModel<IterationTaskDTO> getSelectionModel()
	{
		return selectionModel;
	}

	@Override
	public ListDataProvider<IterationTaskDTO> getDataProvider()
	{
		return iterationTaskDataProvider;
	}

	@Override
	public ListBox getDisciplines()
	{
		return discipline;
	}

	@Override
	public ListBox getScopeUnitParents()
	{
		return scopeUnitParent;
	}

	@Override
	public ListBox getScopeUnits()
	{
		return scopeUnit;
	}

	@Override
	public void updateSortHandler() {

		// Add the Sort Handler to the CellTable
		ListHandler<IterationTaskDTO> sortHandler = new ListHandler<IterationTaskDTO>(
				iterationTaskDataProvider.getList());
		iterationTaskCT.addColumnSortHandler(sortHandler);
		// task name sort
		sortHandler.setComparator(labelC, TASK_COMPARATOR);
		// scope unit parent
		sortHandler.setComparator(scopeUnitParentC, new Comparator<IterationTaskDTO>() {

			@Override
			public int compare(IterationTaskDTO o1, IterationTaskDTO o2) {
				return o1.getTask().getParentScopeUnitName().compareTo(o2.getTask().getParentScopeUnitName());
			}
		});
		// scope parent
		sortHandler.setComparator(scopeUnitC, new Comparator<IterationTaskDTO>() {

			@Override
			public int compare(IterationTaskDTO o1, IterationTaskDTO o2) {
				int ret = 0;
				if (o1.getTask().getScopeUnit() == null) {
					if (o2.getTask().getScopeUnit() != null) {
						ret = 1;
					}
				} else {
					if (o2.getTask().getScopeUnit() == null) {
						ret = -1;
					} else {
						ret = o1.getTask().getScopeUnit().getName()
								.compareTo(o2.getTask().getScopeUnit().getName());
					}
				}
				return ret;
			}
		});
		// discipline sort
		sortHandler.setComparator(disciplineC, new Comparator<IterationTaskDTO>() {

			@Override
			public int compare(IterationTaskDTO o1, IterationTaskDTO o2) {
				return o1.getTask().getDiscipline().getLibelle()
						.compareTo(o2.getTask().getDiscipline().getLibelle());
			}
		});
		// responsible sort
		sortHandler.setComparator(responsibleC, new Comparator<IterationTaskDTO>() {

			@Override
			public int compare(IterationTaskDTO o1, IterationTaskDTO o2) {
				int ret = 0;
				if (o1.getTask().getScopeUnit() == null) {
					if (o2.getTask().getUser() != null) {
						ret = 1;
					}
				} else {
					if (o2.getTask().getUser() == null) {
						ret = -1;
					} else {
						ret = getUserName(o1).compareTo(getUserName(o2));
					}
				}
				return ret;
			}
		});
		// estimate task sort
		sortHandler.setComparator(estimateTaskC, new Comparator<IterationTaskDTO>() {

			@Override
			public int compare(IterationTaskDTO o1, IterationTaskDTO o2) {
				return String.valueOf(o1.getTask().getInitialEstimation()).compareTo(
						String.valueOf(o2.getTask().getInitialEstimation()));
			}
		});
		// status sort
		sortHandler.setComparator(statusC, new Comparator<IterationTaskDTO>() {

			@Override
			public int compare(IterationTaskDTO o1, IterationTaskDTO o2) {
				return o1.getTask().getStatus().getLabel().compareTo(o2.getTask().getStatus().getLabel());
			}
		});
		// reestimate task sort
		sortHandler.setComparator(reestimateTaskC, new Comparator<IterationTaskDTO>() {

			@Override
			public int compare(IterationTaskDTO o1, IterationTaskDTO o2) {
				return String.valueOf(o1.getReestimate()).compareTo(String.valueOf(o2.getReestimate()));
			}
		});
		// consumed task sort
		sortHandler.setComparator(consumedTimeC, new Comparator<IterationTaskDTO>() {

			@Override
			public int compare(IterationTaskDTO o1, IterationTaskDTO o2) {
				return String.valueOf(o1.getTask().getConsumedTime()).compareTo(
						String.valueOf(o2.getTask().getConsumedTime()));
			}
		});
		// remaining task sort
		sortHandler.setComparator(remainingTimeC, new Comparator<IterationTaskDTO>() {

			@Override
			public int compare(IterationTaskDTO o1, IterationTaskDTO o2) {
				return String.valueOf(o1.getRemainingTime()).compareTo(String.valueOf(o2.getRemainingTime()));
			}
		});
		// advancement task sort
		sortHandler.setComparator(advancementC, new Comparator<IterationTaskDTO>() {

			@Override
			public int compare(IterationTaskDTO o1, IterationTaskDTO o2) {
				return String.valueOf(o1.getAdvancement()).compareTo(String.valueOf(o2.getAdvancement()));
			}
		});
		
    if ( iterationTaskCT.getColumnSortList().size() > 0)
    {
      ColumnSortEvent.fire( iterationTaskCT, iterationTaskCT.getColumnSortList());
    }
	}

	@Override
	public void enableScopeUnitFilter(boolean pEnable) {
		scopeUnit.setVisible(pEnable);
		scopeUnitL.setVisible(pEnable);
	}

	@Override
	public void enableScopeUnitParentFilter(boolean pEnable) {
		scopeUnitParent.setVisible(pEnable);
		scopeUnitParentL.setVisible(pEnable);
	}

	@Override
	public Comparator<IterationTaskDTO> getTaskComparator() {
		return TASK_COMPARATOR;
	}

	interface ListTaskViewImplUiBinder extends UiBinder<Widget, TaskListViewImpl>
	{
	}
}
