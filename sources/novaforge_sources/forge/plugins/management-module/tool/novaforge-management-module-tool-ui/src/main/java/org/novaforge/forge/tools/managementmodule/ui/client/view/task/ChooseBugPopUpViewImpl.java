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

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltree.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.shared.BugDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;

import java.util.Collections;
import java.util.Comparator;

/**
 * The view implementation of the pop-up which permits to choose a bug
 */
public class ChooseBugPopUpViewImpl extends PopupPanel implements ChooseBugPopUpView {

   private static ChooseBugPopUpViewImplUiBinder uiBinder = GWT.create(ChooseBugPopUpViewImplUiBinder.class);
   private final Comparator<BugDTO> COMPARATOR_BUG_TRACKER_ID = new Comparator<BugDTO>() {
      @Override
      public int compare(BugDTO bug1, BugDTO bug2) {
         //case of int id
         try{
            return Long.valueOf(bug1.getBugTrackerId()).compareTo(Long.valueOf(bug2.getBugTrackerId()));
         }
         catch(NumberFormatException nfex) {
            //id are not decimal (possible only if mantis is changed by something else)
            return bug1.getBugTrackerId().compareTo(bug2.getBugTrackerId());
         }
      }
   };
   private final Comparator<BugDTO> COMPARATOR_TITLE = new Comparator<BugDTO>() {
      @Override
      public int compare(BugDTO bug1, BugDTO bug2) {
         return bug1.getTitle().compareTo(bug2.getTitle());
      }
   };
   private final Comparator<BugDTO> COMPARATOR_ASSIGNED_TO = new Comparator<BugDTO>() {
      @Override
      public int compare(BugDTO bug1, BugDTO bug2) {
         return bug1.getAssignedTo().compareTo(bug2.getAssignedTo());
      }
   };
   private final Comparator<BugDTO> COMPARATOR_STATUS = new Comparator<BugDTO>() {
      @Override
      public int compare(BugDTO bug1, BugDTO bug2) {
         return bug1.getStatus().compareTo(bug2.getStatus());
      }
   };
   private final Comparator<BugDTO> COMPARATOR_SEVERITY = new Comparator<BugDTO>() {
      @Override
      public int compare(BugDTO bug1, BugDTO bug2) {
         return bug1.getSeverity().compareTo(bug2.getSeverity());
      }
   };
   @UiField Label         titleLabel;
   @UiField Button        chooseButton;
   @UiField Button        cancelButton;
   @UiField VerticalPanel cellPanel;
   CellTable<BugDTO> bugListCellTable;
   SimplePager       bugListPager;
   /**
    * The data provider of the bug list cell table
    */
   private ListDataProvider<BugDTO>     bugListDataProvider;
   /**
    * The selection model of the bug list cell table
    */
   private SingleSelectionModel<BugDTO> bugListSelectionModel;
   private Column<BugDTO, String>       bugTrackerIdColumn;
   private Column<BugDTO, String>       titleColumn;
   private Column<BugDTO, String>       affectedColumn;
   private Column<BugDTO, String>       statusColumn;
   private Column<BugDTO, String>       severityColumn;

   public ChooseBugPopUpViewImpl()
   {
      this.setWidth("100%");
      add(uiBinder.createAndBindUi(this));
      initBugCellTable();
      cellPanel.add(bugListCellTable);
      cellPanel.add(bugListPager);
      titleLabel.setText(Common.MESSAGES_TASK.chooseBugTitle());
      chooseButton.setText(Common.MESSAGES_TASK.chooseBug());
      chooseButton.setEnabled(false);
      cancelButton.setText(Common.GLOBAL.buttonCancel());
      bugListPager.addStyleName("res.css.zoneTable");
      setGlassEnabled(true);
   }

   /**
    * Initialize the scope unit discipline cell table
    */
   private void initBugCellTable()
   {
      // init cell table
      bugListCellTable = new CellTable<BugDTO>(Common.PAGE_SIZE, (Resources) GWT.create(TableResources.class),
                                               CellKey.BUG_KEY_PROVIDER);
      bugListCellTable.setWidth("100%", false);
      bugListCellTable.setVisible(true);

      // Init empty widget
      final Label emptyLabel = new Label(Common.MESSAGES_TASK.emptyBugList());
      emptyLabel.setStyleName(Common.RESOURCE.css().emptyLabel());
      bugListCellTable.setEmptyTableWidget(emptyLabel);

      // Create a Pager to control the CellTable
      SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
      bugListPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
      bugListPager.setDisplay(bugListCellTable);

      // Initialize the columns.
      initTableColumns();

      // Add the CellTable to the adapter
      bugListDataProvider = new ListDataProvider<BugDTO>();
      bugListDataProvider.addDataDisplay(bugListCellTable);

      bugListSelectionModel = new SingleSelectionModel<BugDTO>();
      bugListCellTable.setSelectionModel(bugListSelectionModel);
   }

   /**
    * Initialize the column of the cell table
    */
   private void initTableColumns()
   {
      bugTrackerIdColumn = new Column<BugDTO, String>(new TextCell())
      {
         @Override
         public String getValue(BugDTO bug)
         {
            return bug.getBugTrackerId();
         }
      };
      bugTrackerIdColumn.setSortable(true);
      bugListCellTable.addColumn(bugTrackerIdColumn, Common.MESSAGES_TASK.bugTrackerId());
      bugListCellTable.setColumnWidth(bugTrackerIdColumn, "75px");

      titleColumn = new Column<BugDTO, String>(new TextCell())
      {
         @Override
         public String getValue(BugDTO bug)
         {
            return bug.getTitle();
         }
      };
      titleColumn.setSortable(true);
      bugListCellTable.addColumn(titleColumn, Common.MESSAGES_TASK.bugTitle());
      bugListCellTable.setColumnWidth(titleColumn, "450px");

      affectedColumn = new Column<BugDTO, String>(new TextCell())
      {
         @Override
         public String getValue(BugDTO bug)
         {
            return bug.getAssignedTo();
         }
      };
      affectedColumn.setSortable(true);
      bugListCellTable.addColumn(affectedColumn, Common.MESSAGES_TASK.bugAffectedUser());
      bugListCellTable.setColumnWidth(affectedColumn, "150px");

      statusColumn = new Column<BugDTO, String>(new TextCell())
      {
         @Override
         public String getValue(BugDTO bug)
         {
            return bug.getStatus();
         }
      };
      statusColumn.setSortable(true);
      bugListCellTable.addColumn(statusColumn, Common.MESSAGES_TASK.bugStatus());
      bugListCellTable.setColumnWidth(statusColumn, "75px");

      severityColumn = new Column<BugDTO, String>(new TextCell())
      {
         @Override
         public String getValue(BugDTO bug)
         {
            return bug.getSeverity();
         }
      };
      severityColumn.setSortable(true);
      bugListCellTable.addColumn(severityColumn, Common.MESSAGES_TASK.bugSeverity());
      bugListCellTable.setColumnWidth(severityColumn, "75px");
   }

   @Override
   public ListDataProvider<BugDTO> getDataProvider()
   {
      return bugListDataProvider;
   }

   @Override
   public void updateSortHandler()
   {
      Collections.sort(bugListDataProvider.getList(), Collections.reverseOrder(COMPARATOR_BUG_TRACKER_ID));
      ListHandler<BugDTO> sortHandler = new ListHandler<BugDTO>(bugListDataProvider.getList());
      bugListCellTable.addColumnSortHandler(sortHandler);
      sortHandler.setComparator(bugTrackerIdColumn, COMPARATOR_BUG_TRACKER_ID);
      sortHandler.setComparator(titleColumn, COMPARATOR_TITLE);
      sortHandler.setComparator(affectedColumn, COMPARATOR_ASSIGNED_TO);
      sortHandler.setComparator(statusColumn, COMPARATOR_STATUS);
      sortHandler.setComparator(severityColumn, COMPARATOR_SEVERITY);

   }

   @Override
   public SingleSelectionModel<BugDTO> getSelectionModel()
   {
      return bugListSelectionModel;
   }

   @Override
   public Button getCancelButton() {
      return cancelButton;
   }

   @Override
   public Button getValidateButton() {
      return chooseButton;
   }

   @Override
   public void show() {
      int left = (Window.getClientWidth() - 850) >> 1;
      int top = (Window.getClientHeight() - 400) >> 1;
      setPopupPosition(Math.max(Window.getScrollLeft() + left, 0), Math.max(
          Window.getScrollTop() + top, 0));
      super.show();
   }

   interface ChooseBugPopUpViewImplUiBinder extends UiBinder<Widget, ChooseBugPopUpViewImpl> {
   }
   
}
