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
package org.novaforge.forge.tools.managementmodule.ui.client.view.iterationmonitoring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.CustomListBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.StylableTextCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltree.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitIterationMonitoringDTO;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Iteration Monitoring view implementation
 */
public class IterationMonitoringViewImpl extends Composite implements IterationMonitoringView {

   //comparators
   static final         Comparator<ScopeUnitIterationMonitoringDTO> SCOPE_UNIT_COMPARATOR            = new Comparator<ScopeUnitIterationMonitoringDTO>()
   {
      @Override
      public int compare(ScopeUnitIterationMonitoringDTO o1, ScopeUnitIterationMonitoringDTO o2)
      {
         return o1.getScopeUnitName().compareTo(o2.getScopeUnitName());
      }
   };
   static final         Comparator<ScopeUnitIterationMonitoringDTO> SCOPE_UNIT_PARENT_COMPARATOR     = new Comparator<ScopeUnitIterationMonitoringDTO>()
   {
      @Override
      public int compare(ScopeUnitIterationMonitoringDTO o1, ScopeUnitIterationMonitoringDTO o2)
      {
         return o1.getParentScopeUnitName().compareTo(o2.getParentScopeUnitName());
      }
   };
   static final         Comparator<ScopeUnitIterationMonitoringDTO> ESTIMATE_COMPARATOR              = new Comparator<ScopeUnitIterationMonitoringDTO>()
   {
      @Override
      public int compare(ScopeUnitIterationMonitoringDTO o1, ScopeUnitIterationMonitoringDTO o2)
      {
         return ((Float) o1.getEstimation()).compareTo(o2.getEstimation());
      }
   };
   static final         Comparator<ScopeUnitIterationMonitoringDTO> CONSUMED_COMPARATOR              = new Comparator<ScopeUnitIterationMonitoringDTO>()
   {
      @Override
      public int compare(ScopeUnitIterationMonitoringDTO o1, ScopeUnitIterationMonitoringDTO o2)
      {
         return (o1.getConsumed()).compareTo(o2.getConsumed());
      }
   };
   static final         Comparator<ScopeUnitIterationMonitoringDTO> REMAINING_TASK_COMPARATOR        = new Comparator<ScopeUnitIterationMonitoringDTO>()
   {
      @Override
      public int compare(ScopeUnitIterationMonitoringDTO o1, ScopeUnitIterationMonitoringDTO o2)
      {
         return (o1.getRemainingTasks()).compareTo(o2.getRemainingTasks());
      }
   };
   static final         Comparator<ScopeUnitIterationMonitoringDTO> REESTIMATE_COMPARATOR            = new Comparator<ScopeUnitIterationMonitoringDTO>()
   {
      @Override
      public int compare(ScopeUnitIterationMonitoringDTO o1, ScopeUnitIterationMonitoringDTO o2)
      {
         return ((Float) o1.getReestimate()).compareTo(o2.getReestimate());
      }
   };
   static final         Comparator<ScopeUnitIterationMonitoringDTO> ADVANCEMENT_COMPARATOR           = new Comparator<ScopeUnitIterationMonitoringDTO>()
   {
      @Override
      public int compare(ScopeUnitIterationMonitoringDTO o1, ScopeUnitIterationMonitoringDTO o2)
      {
         return ((Float) o1.getAdvancement()).compareTo(o2.getAdvancement());
      }
   };
   private static final String                                      THIRD_BY_LINE_GRID_TEXTBOX_WIDTH = "90px";
   private static final String                                      THIRD_BY_LINE_GRID_LABEL_WIDTH   = "240px";
   private static final String                                      DEFAULT_LABEL_WIDTH              = "120px";
   private static IterationMonitoringViewImplUiBinder uiBinder = GWT
         .create(IterationMonitoringViewImplUiBinder.class);
   //buttons
   @UiField
   Button homeReturnButton;
   @UiField
   Button iterationDetailButton;
   @UiField
   Button csvExportButton;
   //context
   @UiField
   Label iterationLabel;
   @UiField
   Label iterationValueLabel;
   @UiField
   Label lotLabel;
   @UiField
   Label lotValueLabel;
   @UiField
   Label parentLotLabel;
   @UiField
   Label parentLotValueLabel;
   @UiField
   Label startDateLabel;
   @UiField
   Label startDatevalueLabel;
   @UiField
   Label endDateLabel;
   @UiField
   Label endDatevalueLabel;
   @UiField
   Label disciplinesLabel;
   @UiField
   CustomListBox<DisciplineDTO> disciplinesListBox;
   //indicators
   @UiField
   Grid indicatorsGrid;
   @UiField
   Label nbActorsLabel;
   @UiField
   TextBox nbActorsTB;
   @UiField
   Label consumedLabel;
   @UiField
   TextBox consumedTB;
   @UiField
   Label focalisationLabel;
   @UiField
   TextBox focalisationTB;
   @UiField
   Label velocityLabel;
   @UiField
   TextBox velocityTB;
   @UiField
   Label errorLabel;
   @UiField
   TextBox errorTB;
   @UiField
   Label advancementLabel;
   @UiField
   TextBox advancementTB;
   //Bottom table
   @UiField
   Label iterationMonitoringTableTitle;
   @UiField
   Panel cellPanel;
   //cell table
   CellTable<ScopeUnitIterationMonitoringDTO> iterationMonitoringCellTable;
   SimplePager iterationMonitoringPager;
   List<ScopeUnitIterationMonitoringDTO> fullDataList = new ArrayList<ScopeUnitIterationMonitoringDTO>();
   private AsyncDataProvider<ScopeUnitIterationMonitoringDTO> dataProvider;
   private SingleSelectionModel<ScopeUnitIterationMonitoringDTO> selectionModel = new SingleSelectionModel<ScopeUnitIterationMonitoringDTO>();
   //columns
   private Column<ScopeUnitIterationMonitoringDTO, String> scopeUnitColumn;
   private Column<ScopeUnitIterationMonitoringDTO, String> scopeUnitParentColumn;
   private Column<ScopeUnitIterationMonitoringDTO, String> estimateColumn;
   private Column<ScopeUnitIterationMonitoringDTO, String> consumedColumn;
   private Column<ScopeUnitIterationMonitoringDTO, String> remainingTaskColumn;
   private Column<ScopeUnitIterationMonitoringDTO, String> reestimateColumn;
   private Column<ScopeUnitIterationMonitoringDTO, String> advancementColumn;
   public IterationMonitoringViewImpl() {
      initWidget(uiBinder.createAndBindUi(this));
      initIterationMonitoringCellTable();
      cellPanel.add(iterationMonitoringCellTable);
      cellPanel.add(iterationMonitoringPager);
      // action
      homeReturnButton.setText(Common.GLOBAL.homeReturn());
      iterationDetailButton.setText(Common.MESSAGES_MONITORING.iterationDetailButton());
      csvExportButton.setText(Common.GLOBAL.buttonExportCSV());
      // context filter lot
      lotLabel.setText(Common.MESSAGES_MONITORING.lotL());
      lotValueLabel.setWidth(DEFAULT_LABEL_WIDTH);
      parentLotLabel.setText(Common.MESSAGES_MONITORING.parentLotL());
      parentLotValueLabel.setWidth(DEFAULT_LABEL_WIDTH);
      iterationLabel.setText(Common.MESSAGES_MONITORING.iterationLabel());
      iterationValueLabel.setWidth(DEFAULT_LABEL_WIDTH);
      startDateLabel.setText(Common.MESSAGES_MONITORING.startDateLabel());
      endDateLabel.setText(Common.MESSAGES_MONITORING.endDateLabel());
      disciplinesLabel.setText(Common.MESSAGES_MONITORING.disciplineLabel());
      // indicators
      indicatorsGrid.setCellPadding(3);
      indicatorsGrid.getCellFormatter().setWidth(0, 0, THIRD_BY_LINE_GRID_LABEL_WIDTH);
      indicatorsGrid.getCellFormatter().setWidth(0, 1, THIRD_BY_LINE_GRID_TEXTBOX_WIDTH);
      indicatorsGrid.getCellFormatter().setWidth(0, 2, THIRD_BY_LINE_GRID_LABEL_WIDTH);
      indicatorsGrid.getCellFormatter().setWidth(0, 3, THIRD_BY_LINE_GRID_TEXTBOX_WIDTH);
      indicatorsGrid.getCellFormatter().setWidth(0, 4, THIRD_BY_LINE_GRID_LABEL_WIDTH);
      indicatorsGrid.getCellFormatter().setWidth(0, 5, THIRD_BY_LINE_GRID_TEXTBOX_WIDTH);
      indicatorsGrid.getCellFormatter().setWidth(0, 6, THIRD_BY_LINE_GRID_TEXTBOX_WIDTH);
      focalisationLabel.setText(Common.MESSAGES_MONITORING.focalisationFactorLabel());
      focalisationTB.setWidth(THIRD_BY_LINE_GRID_TEXTBOX_WIDTH);
      errorLabel.setText(Common.MESSAGES_MONITORING.averageErrorEstimationL());
      errorTB.setWidth(THIRD_BY_LINE_GRID_TEXTBOX_WIDTH);
      velocityLabel.setText(Common.MESSAGES_MONITORING.velocityLabel());
      velocityTB.setWidth(THIRD_BY_LINE_GRID_TEXTBOX_WIDTH);
      advancementLabel.setText(Common.MESSAGES_MONITORING.advancementLabel());
      advancementTB.setWidth(THIRD_BY_LINE_GRID_TEXTBOX_WIDTH);
      nbActorsLabel.setText(Common.MESSAGES_MONITORING.nbActorsLabel());
      nbActorsTB.setWidth(THIRD_BY_LINE_GRID_TEXTBOX_WIDTH);
      consumedLabel.setText(Common.MESSAGES_MONITORING.consumedLabel());
      consumedTB.setWidth(THIRD_BY_LINE_GRID_TEXTBOX_WIDTH);
   }
   
   /**
    * Initialize the scope unit discipline cell table
    */
   private void initIterationMonitoringCellTable() {
      // init cell table
      final Label loadingLabel = new Label(Common.GLOBAL.loadingMessage());
      loadingLabel.setStyleName(Common.RESOURCE.css().emptyLabel());
      iterationMonitoringCellTable = new CellTable<ScopeUnitIterationMonitoringDTO>(Common.PAGE_SIZE,
            (Resources) GWT.create(TableResources.class), CellKey.ITERATION_MONITORING_KEY_PROVIDER, loadingLabel);
      iterationMonitoringCellTable.setWidth("100%", false);
      iterationMonitoringCellTable.setVisible(true);

      // Init empty widget
      final Label emptyLabel = new Label(Common.GLOBAL.noData());
      emptyLabel.setStyleName(Common.RESOURCE.css().emptyLabel());
      iterationMonitoringCellTable.setEmptyTableWidget(emptyLabel);
      // Create a Pager to control the CellTable
      SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
      iterationMonitoringPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
      iterationMonitoringPager.setDisplay(iterationMonitoringCellTable);

      // Initialize the columns.
      initTableColumns();

      // Add the CellTable to the adapter
      dataProvider = new AsyncDataProvider<ScopeUnitIterationMonitoringDTO>(){
         @Override
         protected void onRangeChanged(HasData<ScopeUnitIterationMonitoringDTO> display) {
            int start = display.getVisibleRange().getStart();
            int end = Math.min(start + display.getVisibleRange().getLength(), fullDataList.size());
            final List<ScopeUnitIterationMonitoringDTO> subList = fullDataList.subList(start, end);
            updateRowData(start, subList);
         }
      };
      dataProvider.addDataDisplay(iterationMonitoringCellTable);

      selectionModel = new SingleSelectionModel<ScopeUnitIterationMonitoringDTO>();
      iterationMonitoringCellTable.setSelectionModel(selectionModel);
   }
   
   /**
    * Init the column
    */
   private void initTableColumns() {
      final Map<String, String> attributes = new HashMap<String, String>();
      attributes.put("align", "center");

      scopeUnitColumn = new Column<ScopeUnitIterationMonitoringDTO, String>(new TextCell()) {
         @Override
         public String getValue(ScopeUnitIterationMonitoringDTO object) {
            return object.getScopeUnitName();
         }
      };
      scopeUnitColumn.setSortable(true);
      iterationMonitoringCellTable.addColumn(scopeUnitColumn, Common.MESSAGES_MONITORING.scopeUnit());

      scopeUnitParentColumn = new Column<ScopeUnitIterationMonitoringDTO, String>(new TextCell()) {
         @Override
         public String getValue(ScopeUnitIterationMonitoringDTO object) {
            return object.getParentScopeUnitName();
         }
      };
      scopeUnitParentColumn.setSortable(true);
      iterationMonitoringCellTable.addColumn(scopeUnitParentColumn, Common.MESSAGES_MONITORING.scopeUnitParent());

      estimateColumn = new Column<ScopeUnitIterationMonitoringDTO, String>(new StylableTextCell(attributes)) {
         @Override
         public String getValue(ScopeUnitIterationMonitoringDTO object) {
            return Common.floatFormat(object.getEstimation(), 2).toString();
         }
      };
      estimateColumn.setSortable(true);
      iterationMonitoringCellTable.addColumn(estimateColumn, Common.getLinesHeader(Common.MESSAGES_MONITORING.workToDo()));

      consumedColumn = new Column<ScopeUnitIterationMonitoringDTO, String>(new StylableTextCell(attributes)) {
         @Override
         public String getValue(ScopeUnitIterationMonitoringDTO object) {
            return Common.floatFormat(object.getConsumed(), 2).toString();
         }
      };
      consumedColumn.setSortable(true);
      iterationMonitoringCellTable.addColumn(consumedColumn, Common.getLinesHeader(Common.MESSAGES_MONITORING.consumed()));

      remainingTaskColumn = new Column<ScopeUnitIterationMonitoringDTO, String>(new StylableTextCell(attributes)) {
         @Override
         public String getValue(ScopeUnitIterationMonitoringDTO object) {
            return Common.floatFormat(object.getRemainingTasks(), 2).toString();
         }
      };
      remainingTaskColumn.setSortable(true);
      iterationMonitoringCellTable.addColumn(remainingTaskColumn, Common.getLinesHeader(Common.MESSAGES_MONITORING.remainingTask()));

      reestimateColumn = new Column<ScopeUnitIterationMonitoringDTO, String>(new StylableTextCell(attributes)) {
         @Override
         public String getValue(ScopeUnitIterationMonitoringDTO object) {
            return Common.floatFormat(object.getReestimate(), 2).toString();
         }
      };
      reestimateColumn.setSortable(true);
      iterationMonitoringCellTable.addColumn(reestimateColumn, Common.getLinesHeader(Common.MESSAGES_MONITORING.reestimate()));

      advancementColumn = new Column<ScopeUnitIterationMonitoringDTO, String>(new StylableTextCell(attributes)) {
         @Override
         public String getValue(ScopeUnitIterationMonitoringDTO object) {
            return Common.floatFormat(object.getAdvancement(), 2).toString();
         }
      };
      advancementColumn.setSortable(true);
      iterationMonitoringCellTable.addColumn(advancementColumn, Common.getLinesHeader(Common.MESSAGES_MONITORING.advancement()));
   }

   @Override
   public AsyncDataProvider<ScopeUnitIterationMonitoringDTO> getDataProvider() {
      return dataProvider;
   }

   @Override
   public void updateSortHandler() {
      //default sort
      Collections.sort(fullDataList, SCOPE_UNIT_COMPARATOR);
      // Add the Sort Handler to the CellTable
      iterationMonitoringCellTable.addColumnSortHandler(new ColumnSortEvent.Handler() {
         @Override
         public void onColumnSort(ColumnSortEvent event)
         {
            // scope unit
            if (event.getColumn().equals(scopeUnitColumn)) {
               Comparator<ScopeUnitIterationMonitoringDTO> comparator = SCOPE_UNIT_COMPARATOR;
               if (!event.isSortAscending()) {
                  comparator = Collections.reverseOrder(comparator);
               }
               Collections.sort(fullDataList, Collections.reverseOrder(comparator));
            }
            // parent scope unit
            else if (event.getColumn().equals(scopeUnitParentColumn)) {
               Comparator<ScopeUnitIterationMonitoringDTO> comparator = SCOPE_UNIT_PARENT_COMPARATOR;
               if (!event.isSortAscending()) {
                  comparator = Collections.reverseOrder(comparator);
               }
               Collections.sort(fullDataList, Collections.reverseOrder(comparator));
            }
            // estimateColumn
            else if (event.getColumn().equals(estimateColumn)) {
               Comparator<ScopeUnitIterationMonitoringDTO> comparator = ESTIMATE_COMPARATOR;
               if (!event.isSortAscending()) {
                  comparator = Collections.reverseOrder(comparator);
               }
               Collections.sort(fullDataList, Collections.reverseOrder(comparator));
            }
            // consumedColumn
            else if (event.getColumn().equals(consumedColumn)) {
               Comparator<ScopeUnitIterationMonitoringDTO> comparator = CONSUMED_COMPARATOR;
               if (!event.isSortAscending()) {
                  comparator = Collections.reverseOrder(comparator);
               }
               Collections.sort(fullDataList, Collections.reverseOrder(comparator));
            }
            // remainingTaskColumn
            else if (event.getColumn().equals(remainingTaskColumn)) {
               Comparator<ScopeUnitIterationMonitoringDTO> comparator = REMAINING_TASK_COMPARATOR;
               if (!event.isSortAscending()) {
                  comparator = Collections.reverseOrder(comparator);
               }
               Collections.sort(fullDataList, Collections.reverseOrder(comparator));
            }
            // reestimateColumn
            else if (event.getColumn().equals(reestimateColumn)) {
               Comparator<ScopeUnitIterationMonitoringDTO> comparator = REESTIMATE_COMPARATOR;
               if (!event.isSortAscending()) {
                  comparator = Collections.reverseOrder(comparator);
               }
               Collections.sort(fullDataList, Collections.reverseOrder(comparator));
            }
            // advancementColumn
            else if (event.getColumn().equals(advancementColumn)) {
               Comparator<ScopeUnitIterationMonitoringDTO> comparator = ADVANCEMENT_COMPARATOR;
               if (!event.isSortAscending()) {
                  comparator = Collections.reverseOrder(comparator);
               }
               Collections.sort(fullDataList, Collections.reverseOrder(comparator));
            }
            int start = iterationMonitoringCellTable.getVisibleRange().getStart();
            int end = Math.min(start + iterationMonitoringCellTable.getVisibleRange().getLength(), fullDataList.size());
            final List<ScopeUnitIterationMonitoringDTO> subList = fullDataList.subList(start, end);
            dataProvider.updateRowData(start, subList);
         }
      });
      
      if ( iterationMonitoringCellTable.getColumnSortList().size() > 0)
      {
        ColumnSortEvent.fire( iterationMonitoringCellTable, iterationMonitoringCellTable.getColumnSortList());
      }

   }

   /**
    * Get the homeReturnButton
    * @return the homeReturnButton
    */
   @Override
   public Button getHomeReturnButton() {
      return homeReturnButton;
   }

   /**
    * Get the iterationDetailButton
    * @return the iterationDetailButton
    */
   @Override
   public Button getIterationDetailButton() {
      return iterationDetailButton;
   }

   /**
    * Get the iterationValueLabel
    * @return the iterationValueLabel
    */
   @Override
   public Label getIterationValueLabel() {
      return iterationValueLabel;
   }

   /**
    * Get the lotValueLabel
    * @return the lotValueLabel
    */
   @Override
   public Label getLotValueLabel() {
      return lotValueLabel;
   }

   /**
    * Get the subLotValueLabel
    * @return the subLotValueLabel
    */
   @Override
   public Label getParentLotValueLabel() {
      return parentLotValueLabel;
   }

   /**
    * Get the startDatevalueLabel
    * @return the startDatevalueLabel
    */
   @Override
   public Label getStartDateValueLabel() {
      return startDatevalueLabel;
   }

   /**
    * Get the endDatevalueLabel
    * @return the endDatevalueLabel
    */
   @Override
   public Label getEndDateValueLabel() {
      return endDatevalueLabel;
   }

   /**
    * Get the disciplinesListBox
    * @return the disciplinesListBox
    */
   @Override
   public CustomListBox<DisciplineDTO> getDisciplinesListBox() {
      return disciplinesListBox;
   }

   /**
    * Get the nbActorsTB
    * @return the nbActorsTB
    */
   @Override
   public TextBox getNbActorsTB() {
      return nbActorsTB;
   }

   /**
    * Get the consumedTB
    * @return the consumedTB
    */
   @Override
   public TextBox getConsumedTB() {
      return consumedTB;
   }

   /**
    * Get the focalisationTB
    * @return the focalisationTB
    */
   @Override
   public TextBox getFocalisationTB() {
      return focalisationTB;
   }

   /**
    * Get the velocityTB
    * @return the velocityTB
    */
   @Override
   public TextBox getVelocityTB() {
      return velocityTB;
   }

   /**
    * Get the errorTB
    * @return the errorTB
    */
   @Override
   public TextBox getErrorTB() {
      return errorTB;
   }

   /**
    * Get the advancementTB
    * @return the advancementTB
    */
   @Override
   public TextBox getAdvancementTB() {
      return advancementTB;
   }

   /**
    * Get the iterationMonitoringCT
    * @return the iterationMonitoringCT
    */
   @Override
   public CellTable<ScopeUnitIterationMonitoringDTO> getIterationMonitoringCellTable() {
      return iterationMonitoringCellTable;
   }

   /**
    * Get the selectionModel
    *
    * @return the selectionModel
    */
   @Override
   public SingleSelectionModel<ScopeUnitIterationMonitoringDTO> getSelectionModel()
   {
      return selectionModel;
   }

   interface IterationMonitoringViewImplUiBinder extends UiBinder<Widget, IterationMonitoringViewImpl>
   {
   }

   @Override
   public void setFullDataList(List<ScopeUnitIterationMonitoringDTO> list) {
      fullDataList = list;
   }

   
   /**
    * Get the fullDataList
    * @return the fullDataList
    */
   @Override
   public List<ScopeUnitIterationMonitoringDTO> getFullDataList() {
      return fullDataList;
   }

   /**
    * Get the csvExportButton
    * @return the csvExportButton
    */
   @Override
   public Button getCsvExportButton() {
      return csvExportButton;
   }
   
}
