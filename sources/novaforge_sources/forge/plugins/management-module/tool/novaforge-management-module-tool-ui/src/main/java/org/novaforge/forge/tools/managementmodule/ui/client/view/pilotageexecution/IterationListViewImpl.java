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
package org.novaforge.forge.tools.managementmodule.ui.client.view.pilotageexecution;

import java.util.Comparator;

import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * @author BILET-JC
 *
 */
public class IterationListViewImpl extends Composite implements IterationListView {

	private static IterationListViewImplUiBinder uiBinder = GWT
			.create(IterationListViewImplUiBinder.class);
	private final SingleSelectionModel<IterationDTO> selectionModel = new SingleSelectionModel<IterationDTO>();
	@UiField
	Button reportB;
	@UiField
   Button updateB;
	@UiField
   Button buttonHomeReturn;
	@UiField
	Label refIterationTitle;
	@UiField
	Label resultTitle;
	@UiField(provided = true)
	CellTable<IterationDTO> backlogListCT;
	@UiField(provided = true)
	SimplePager backlogListPager;
	 
	private ListDataProvider<IterationDTO>   backlogDataProvider;
	   private Column<IterationDTO, String> iterationNumberColumn;
	   private Column<IterationDTO, String> lotColumn;
	   private Column<IterationDTO, String> startDateColumn;
	   private Column<IterationDTO, String> endDateColumn;
	   private Column<IterationDTO, String> statusColumn;
	public IterationListViewImpl() {
		 Common.RESOURCE.css().ensureInjected();
		 initBacklogCT();
		initWidget(uiBinder.createAndBindUi(this));

		refIterationTitle.setText(Common.MESSAGES_BACKLOG.refIterationList());
		resultTitle.setText(Common.GLOBAL.resultTitle());
		reportB.setText(Common.MESSAGES_BACKLOG.buttonReportIteration());
		updateB.setText(Common.MESSAGES_BACKLOG.buttonUpdateIteration());
		buttonHomeReturn.setText(Common.GLOBAL.homeReturn());
		reportB.setEnabled(false);
		updateB.setEnabled(false);
	}

	private void initBacklogCT()
	{
		backlogListCT = new CellTable<IterationDTO>(Common.PAGE_SIZE,
	            (Resources) GWT.create(TableResources.class), CellKey.PROJECT_KEY_PROVIDER);
		backlogListCT.setWidth("100%", false);

      // Init empty widget
      Label emptyProjectPlanListLabel = new Label(Common.MESSAGES_BACKLOG.emptyIterationList());
      emptyProjectPlanListLabel.setStyleName(Common.RESOURCE.css().emptyLabel());
      backlogListCT.setEmptyTableWidget(emptyProjectPlanListLabel);

      // Create a Pager to control the CellTable
      SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
      backlogListPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
      backlogListPager.setDisplay(backlogListCT);

      // Initialize the columns.
      initBacklogCTColumns();

      // Add the CellTable to the adapter
      backlogDataProvider = new ListDataProvider<IterationDTO>();
      backlogDataProvider.addDataDisplay(backlogListCT);

      // Add the CellTable to the adapter
      ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(backlogListCT);
      backlogListCT.addColumnSortHandler(columnSortHandler);

		backlogListCT.setSelectionModel(selectionModel);

	}

	private void initBacklogCTColumns() {

	      // Iteration Number Column
	      iterationNumberColumn = new Column<IterationDTO, String>(new TextCell()) {
	         @Override
	         public String getValue(IterationDTO object) {
	            return String.valueOf(object.getNumIteration());
	         }
	      };
	      iterationNumberColumn.setSortable(true);
	      backlogListCT.addColumn(iterationNumberColumn,
	            Common.MESSAGES_BACKLOG.iterationNumber());
	      // lot Column
	      lotColumn = new Column<IterationDTO, String>(new TextCell()) {
	         @Override
	         public String getValue(IterationDTO object) {
	            return object.getLot().getName();
	         }
	      };
	      lotColumn.setSortable(true);
	      backlogListCT.addColumn(lotColumn, Common.MESSAGES_BACKLOG.lot());

	      // Start Date Column

	      startDateColumn = new Column<IterationDTO, String>(new TextCell()) {
	         @Override
	         public String getValue(IterationDTO object) {
	            return Common.FR_DATE_FORMAT_ONLY_DAY.format(object.getStartDate());
	         }
	      };
	      startDateColumn.setSortable(true);
	      backlogListCT.addColumn(startDateColumn, Common.MESSAGES_BACKLOG.startDate());

	      // End Date Column

	      endDateColumn = new Column<IterationDTO, String>(new TextCell()) {
	         @Override
	         public String getValue(IterationDTO object) {
	            return Common.FR_DATE_FORMAT_ONLY_DAY.format(object.getEndDate());
	         }
	      };
	      endDateColumn.setSortable(true);
	      backlogListCT.addColumn(endDateColumn, Common.MESSAGES_BACKLOG.endDate());

	      // Status Column
	      statusColumn = new Column<IterationDTO, String>(new TextCell()) {
	         @Override
	         public String getValue(IterationDTO object) {
	        	 String ret;
	        	 if (object.isFinished()) {
					ret = Common.MESSAGES_BACKLOG.finished();
				}
	        	 else {
	        		 ret = Common.MESSAGES_BACKLOG.open();
	        	 }
	            return ret;
	         }
	      };
	      statusColumn.setSortable(true);
	      backlogListCT.addColumn(statusColumn, Common.MESSAGES_BACKLOG.status());


	}

	@Override
	public ListDataProvider<IterationDTO> getIterationDataProvider() {
		return backlogDataProvider;
	}

	@Override
	public CellTable<IterationDTO> getIterationCT() {
		return backlogListCT;
	}
  
	@Override
	public void iterationListSortHandler() {
	      // Add the Sort Handler to the CellTable
	      ListHandler<IterationDTO> sortHandler = new ListHandler<IterationDTO>(
	            backlogDataProvider.getList());
	      backlogListCT.addColumnSortHandler(sortHandler);

	      // iteration number sort
	      sortHandler.setComparator(iterationNumberColumn, new Comparator<IterationDTO>() {

	          @Override
	          public int compare(IterationDTO o1, IterationDTO o2) {

	             return Integer.valueOf(o1.getNumIteration()).compareTo(
	                   Integer.valueOf(o2.getNumIteration()));
	          }
	       });
	      // start date sort
	      sortHandler.setComparator(startDateColumn, new Comparator<IterationDTO>() {
	         @Override
	         public int compare(IterationDTO o1, IterationDTO o2) {
	            return o1.getStartDate().compareTo(o2.getStartDate());
	         }
	      });

	      // start date sort
	      sortHandler.setComparator(endDateColumn, new Comparator<IterationDTO>() {
	         @Override
	         public int compare(IterationDTO o1, IterationDTO o2) {
	            return o1.getEndDate().compareTo(o2.getEndDate());
	         }
	      });
	      //lot sort
	      sortHandler.setComparator(lotColumn, new Comparator<IterationDTO>() {
	         @Override
	         public int compare(IterationDTO o1, IterationDTO o2) {
	            return o1.getLot().getName().compareTo(o2.getLot().getName());
	         }
	      });
	      //status sort
	      sortHandler.setComparator(statusColumn, new Comparator<IterationDTO>() {
	         @Override
	         public int compare(IterationDTO o1, IterationDTO o2) {
	            return ((Boolean)o1.isFinished()).compareTo(o2.isFinished());
	         }
	      });
	      if ( backlogListCT.getColumnSortList().size() > 0)
	      {
	        ColumnSortEvent.fire( backlogListCT, backlogListCT.getColumnSortList());
	      }
	}

	@Override
	public Button getFollowB() {
		return reportB;
	}

	@Override
	public Button getUpdateB() {
		return updateB;
	}

	@Override
	public SingleSelectionModel<IterationDTO> getSelectionModel() {
		return selectionModel;
	}

   @Override
   public Button getButtonHomeReturn() {
      return buttonHomeReturn;
   }

	interface IterationListViewImplUiBinder extends UiBinder<Widget, IterationListViewImpl>
	{
	}

}
