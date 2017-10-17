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

package org.novaforge.forge.tools.managementmodule.ui.client.view.projectplan;

import java.util.Comparator;

import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

public class ProjectPlanListViewImpl extends Composite implements ProjectPlanListView {
   private static ProjectPlanListViewImplUiBinder uiBinder = GWT
         .create(ProjectPlanListViewImplUiBinder.class);
   private final SingleSelectionModel<ProjectPlanDTO> selectionModel = new SingleSelectionModel<ProjectPlanDTO>();
   @UiField
   Button buttonCreateProjectPlan;
   @UiField
   Button buttonEditProjectPlan;
   @UiField
   Button buttonHomeReturn;
   @UiField
   HasText projectPlanListTitle;
   @UiField(provided = true)
   CellTable<ProjectPlanDTO> projectPlanCellTable;
   @UiField(provided = true)
   SimplePager projectPlanPager;

   private ListDataProvider<ProjectPlanDTO> dataProjectPlanProvider;

   private Column<ProjectPlanDTO, Number> versionColumn;
   private Column<ProjectPlanDTO, String> dateColumn;
   private Column<ProjectPlanDTO, String> statusColumn;

   public ProjectPlanListViewImpl() {

      Common.getResource().css().ensureInjected();
      initProjectPlanTable();
      initWidget(uiBinder.createAndBindUi(this));

      buttonCreateProjectPlan.setText(Common.getGlobal().buttonNew());
      buttonCreateProjectPlan.setEnabled(true);
      buttonEditProjectPlan.setText(Common.getGlobal().buttonEdit());
      buttonEditProjectPlan.setEnabled(false);
      buttonHomeReturn.setText(Common.getGlobal().homeReturn());

      projectPlanListTitle.setText(Common.getProjectPlanMessages().projectPlanListTitle());
   }

   private void initProjectPlanTable() {
      projectPlanCellTable = new CellTable<ProjectPlanDTO>(Common.PAGE_SIZE
      // ,
      // (Resources) GWT.create(TableResources.class)
      );
      projectPlanCellTable.setWidth("100%", false);

      // Init empty widget
      Label emptyProjectPlanListLabel = new Label(Common.getProjectPlanMessages().emptyProjectPlanList());
      emptyProjectPlanListLabel.setStyleName(Common.getResource().css().emptyLabel());
      projectPlanCellTable.setEmptyTableWidget(emptyProjectPlanListLabel);

      // Create a Pager to control the CellTable
      SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
      projectPlanPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
      projectPlanPager.setDisplay(projectPlanCellTable);

      // Initialize the columns.
      initProjectPlanTableColumns();

      // Add the CellTable to the adapter
      dataProjectPlanProvider = new ListDataProvider<ProjectPlanDTO>();
      dataProjectPlanProvider.addDataDisplay(projectPlanCellTable);

      // Add the CellTable to the adapter
      ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(projectPlanCellTable);
      projectPlanCellTable.addColumnSortHandler(columnSortHandler);

      projectPlanCellTable.setSelectionModel(selectionModel);
   }

   private void initProjectPlanTableColumns() {

      // Version column
      versionColumn = new Column<ProjectPlanDTO, Number>(new NumberCell()) {

         @Override
         public Integer getValue(ProjectPlanDTO object) {
            if (object.getVersion() != null) {
               return object.getVersion();
            } else {
               return null;
            }
         }
      };

      versionColumn.setSortable(true);
      projectPlanCellTable.addColumn(versionColumn, Common.getGlobal().version());
      projectPlanCellTable.setColumnWidth(versionColumn, 150, Unit.PX);

      // status column
      statusColumn = new Column<ProjectPlanDTO, String>(new TextCell()) {
         @Override
         public String getValue(ProjectPlanDTO object) {
            if (object.getStatusLabel() != null) {
               return object.getStatusLabel();
            } else {
               return null;
            }
         }
      };
      statusColumn.setSortable(true);
      projectPlanCellTable.addColumn(statusColumn, Common.getGlobal().status());
      projectPlanCellTable.setColumnWidth(statusColumn, 150, Unit.PX);

      // date Column
      dateColumn = new Column<ProjectPlanDTO, String>(new TextCell()) {
         @Override
         public String getValue(ProjectPlanDTO object) {
            if (object.getDate() != null) {
               return Common.EN_DATE_FORMAT.format(object.getDate());
            } else {
               return null;
            }
         }
      };
      dateColumn.setSortable(true);
      projectPlanCellTable.addColumn(dateColumn, Common.getProjectPlanMessages().lastUpdateDate());
      projectPlanCellTable.setColumnWidth(dateColumn, 150, Unit.PX);
   }

   @Override
   public Button getButtonCreateProjectPlan() {
      return buttonCreateProjectPlan;
   }

   @Override
   public Button getButtonEditProjectPlan() {
      return buttonEditProjectPlan;
   }

   @Override
   public HasText getProjectPlanListTitle() {
      return projectPlanListTitle;
   }

   @Override
   public CellTable<ProjectPlanDTO> getProjectPlanCellTable() {
      return projectPlanCellTable;
   }

   @Override
   public SimplePager getProjectPlanPager() {
      return projectPlanPager;
   }

   @Override
   public ListDataProvider<ProjectPlanDTO> getDataProjectPlanProvider() {
      return dataProjectPlanProvider;
   }

   @Override
   public void projectPlanListSortHandler()
   {
      ListHandler<ProjectPlanDTO> sortHandler = new ListHandler<ProjectPlanDTO>(dataProjectPlanProvider.getList());
      projectPlanCellTable.addColumnSortHandler(sortHandler);

      sortHandler.setComparator(statusColumn, new Comparator<ProjectPlanDTO>()
      {
         @Override
         public int compare(ProjectPlanDTO o1, ProjectPlanDTO o2)
         {
            return o1.getStatusLabel().compareTo(o2.getStatusLabel());
         }
      });

      sortHandler.setComparator(versionColumn, new Comparator<ProjectPlanDTO>()
      {
         @Override
         public int compare(ProjectPlanDTO o1, ProjectPlanDTO o2)
         {
            return o1.getVersion().compareTo(o2.getVersion());
         }
      });

      sortHandler.setComparator(dateColumn, new Comparator<ProjectPlanDTO>()
      {
         @Override
         public int compare(ProjectPlanDTO o1, ProjectPlanDTO o2)
         {
            return o1.getDate().compareTo(o2.getDate());
         }
      });
      
      if ( projectPlanCellTable.getColumnSortList().size() > 0)
      {
        ColumnSortEvent.fire( projectPlanCellTable, projectPlanCellTable.getColumnSortList());
      }
      
   }

   @Override
   public SingleSelectionModel<ProjectPlanDTO> getSelectionModel() {
      return selectionModel;
   }

   @Override
   public Button getButtonHomeReturn() {
      return buttonHomeReturn;
   }

   interface ProjectPlanListViewImplUiBinder extends UiBinder<Widget, ProjectPlanListViewImpl>
   {
   }

}
