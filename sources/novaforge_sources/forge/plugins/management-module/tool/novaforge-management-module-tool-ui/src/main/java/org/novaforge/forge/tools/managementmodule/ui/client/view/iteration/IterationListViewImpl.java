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
package org.novaforge.forge.tools.managementmodule.ui.client.view.iteration;

import java.util.Comparator;

import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.ValidateDialogBox;
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
 * @author fdemange
 * 
 */
public class IterationListViewImpl extends Composite implements IterationListView {

   private static IterationListViewImplUiBinder uiBinder = GWT.create(IterationListViewImplUiBinder.class);
   private final ValidateDialogBox validateDialogBox;
   private final InfoDialogBox     infoDialogBox;
   @UiField
   Label iterationCreationTitle;
   @UiField
   Label iterationsTitle;
   @UiField
   Button creationButton;
   @UiField
   Button modificationButton;
   @UiField
   Button deleteButton;
   @UiField(provided = true)
   CellTable<IterationDTO> iterationsCellTable;
   @UiField(provided = true)
   SimplePager iterationsPager;
   /** The selection model of the cell table*/
   private SingleSelectionModel<IterationDTO> selectionModel = new SingleSelectionModel<IterationDTO>();
   private ListDataProvider<IterationDTO> dataIterationProvider;
   private Column<IterationDTO, String> iterationNumberColumn;
   private Column<IterationDTO, String> iterationLabelColumn;
   private Column<IterationDTO, String> lotColumn;
   private Column<IterationDTO, String> startDateColumn;
   private Column<IterationDTO, String> endDateColumn;
   private Column<IterationDTO, String> phaseColumn;
   public IterationListViewImpl() {

      Common.RESOURCE.css().ensureInjected();

      initIterationsTable();

      initWidget(uiBinder.createAndBindUi(this));

      iterationCreationTitle.setText(Common.MESSAGES_CHARGE_PLAN.creationTitle());
      iterationsTitle.setText(Common.MESSAGES_CHARGE_PLAN.iterationsTitle());

      creationButton.setText(Common.MESSAGES_CHARGE_PLAN.iterationCreationLabel());
      modificationButton.setText(Common.MESSAGES_CHARGE_PLAN.iterationModificationLabel());
      deleteButton.setText(Common.MESSAGES_CHARGE_PLAN.iterationDeleteLabel());

      validateDialogBox = new ValidateDialogBox(Common.MESSAGES_CHARGE_PLAN.deleteValidationMessage());

      infoDialogBox = new InfoDialogBox(Common.MESSAGES_CHARGE_PLAN.cannotDeleteInfoMessage(),
            InfoTypeEnum.KO);
   }

   private void initIterationsTable() {
      iterationsCellTable = new CellTable<IterationDTO>(Common.PAGE_SIZE,
            (Resources) GWT.create(TableResources.class), CellKey.PROJECT_KEY_PROVIDER);
      iterationsCellTable.setWidth("100%", false);

      // Init empty widget
      Label emptyIterationsLabel = new Label(Common.MESSAGES_CHARGE_PLAN.emptyIterationFilterMessage());
      emptyIterationsLabel.setStyleName(Common.getResource().css().emptyLabel());
      iterationsCellTable.setEmptyTableWidget(emptyIterationsLabel);

      // Create a Pager to control the CellTable
      SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
      iterationsPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
      iterationsPager.setDisplay(iterationsCellTable);

      // Initialize the columns.
      initIterationsTableColumns();

      // Add the CellTable to the adapter
      dataIterationProvider = new ListDataProvider<IterationDTO>();
      dataIterationProvider.addDataDisplay(iterationsCellTable);

      // Add the CellTable to the adapter
      ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(iterationsCellTable);
      iterationsCellTable.addColumnSortHandler(columnSortHandler);

      //add selection model
      iterationsCellTable.setSelectionModel(selectionModel);
   }

   /**
    * Add the columns to the table.
    */
   private void initIterationsTableColumns() {
      // Iteration Number Column
      iterationNumberColumn = new Column<IterationDTO, String>(new TextCell()) {
         @Override
         public String getValue(IterationDTO object) {
            return String.valueOf(object.getNumIteration());
         }
      };
      iterationNumberColumn.setSortable(true);
      iterationsCellTable.addColumn(iterationNumberColumn, Common.MESSAGES_CHARGE_PLAN.iterationNumber());

      // Iteration Label Column
      iterationLabelColumn = new Column<IterationDTO, String>(new TextCell()) {
         @Override
         public String getValue(IterationDTO object) {
            return object.getLabel();
         }
      };
      iterationLabelColumn.setSortable(true);
      iterationsCellTable.addColumn(iterationLabelColumn, Common.MESSAGES_CHARGE_PLAN.iterationLabel());

      // lot Column
      lotColumn = new Column<IterationDTO, String>(new TextCell()) {
         @Override
         public String getValue(IterationDTO object) {
            return object.getLot().getName();
         }
      };
      lotColumn.setSortable(true);
      iterationsCellTable.addColumn(lotColumn, Common.GLOBAL.lot());

      // Start Date Column

      startDateColumn = new Column<IterationDTO, String>(new TextCell()) {
         @Override
         public String getValue(IterationDTO object) {
            return Common.FR_DATE_FORMAT_ONLY_DAY.format(object.getStartDate());
         }
      };
      startDateColumn.setSortable(true);
      iterationsCellTable.addColumn(startDateColumn, Common.MESSAGES_CHARGE_PLAN.startDate());

      // End Date Column

      endDateColumn = new Column<IterationDTO, String>(new TextCell()) {
         @Override
         public String getValue(IterationDTO object) {
            return Common.FR_DATE_FORMAT_ONLY_DAY.format(object.getEndDate());
         }
      };
      endDateColumn.setSortable(true);
      iterationsCellTable.addColumn(endDateColumn, Common.MESSAGES_CHARGE_PLAN.endDate());

      // Phase Column

      phaseColumn = new Column<IterationDTO, String>(new TextCell()) {
         @Override
         public String getValue(IterationDTO object) {
            return String.valueOf(object.getPhaseType().getName());
         }
      };
      phaseColumn.setSortable(true);
      iterationsCellTable.addColumn(phaseColumn, Common.MESSAGES_CHARGE_PLAN.phase());
   }

   @Override
   public CellTable<IterationDTO> getIterationsTable() {
      return iterationsCellTable;
   }

   @Override
   public ListDataProvider<IterationDTO> getIterationsDataProvider() {
      return dataIterationProvider;
   }
   
   @Override
   public void updateIterationsSortHandler() {
      // Add the Sort Handler to the CellTable
      ListHandler<IterationDTO> sortHandler = new ListHandler<IterationDTO>(dataIterationProvider.getList());
      iterationsCellTable.addColumnSortHandler(sortHandler);

      // iteration number sort
      sortHandler.setComparator(iterationNumberColumn, new Comparator<IterationDTO>() {

         @Override
         public int compare(IterationDTO o1, IterationDTO o2) {

            return Integer.valueOf(o1.getNumIteration()).compareTo(Integer.valueOf(o2.getNumIteration()));
         }
      });

      // iteration label sort
      sortHandler.setComparator(iterationLabelColumn, new Comparator<IterationDTO>() {
         @Override
         public int compare(IterationDTO o1, IterationDTO o2) {
            return o1.getLabel().compareTo(o2.getLabel());
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
      sortHandler.setComparator(lotColumn, new Comparator<IterationDTO>() {
         @Override
         public int compare(IterationDTO o1, IterationDTO o2) {
            return o1.getLot().getName().compareTo(o2.getLot().getName());
         }
      });
      sortHandler.setComparator(phaseColumn, new Comparator<IterationDTO>() {
         @Override
         public int compare(IterationDTO o1, IterationDTO o2) {
            return o1.getPhaseType().getName().compareTo(o2.getPhaseType().getName());
         }
      });
      
      if ( iterationsCellTable.getColumnSortList().size() > 0)
      {
        ColumnSortEvent.fire( iterationsCellTable, iterationsCellTable.getColumnSortList());
      }
   }

   @Override
   public Button getCreationButton()
   {
      return creationButton;
   }

   @Override
   public ValidateDialogBox getValidateDialogBox() {
      return validateDialogBox;
   }

   @Override
   public InfoDialogBox getInfoDialogBox() {
      return infoDialogBox;
   }

   /**
    * Get the modificationButton
    * @return the modificationButton
    */
   @Override
   public Button getModificationButton() {
      return modificationButton;
   }

   /**
    * Get the deleteButton
    * @return the deleteButton
    */
   @Override
   public Button getDeleteButton() {
      return deleteButton;
   }

   /**
    * Get the selectionModel
    * @return the selectionModel
    */
   @Override
   public SingleSelectionModel<IterationDTO> getSelectionModel() {
      return selectionModel;
   }

   interface IterationListViewImplUiBinder extends UiBinder<Widget, IterationListViewImpl>
   {
   }

   
   
}
