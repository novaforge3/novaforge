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
package org.novaforge.forge.tools.managementmodule.ui.client.view.marker;

import java.util.Comparator;

import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.MarkerDTO;

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

public class MarkerListViewImpl extends Composite implements MarkerListView {

   private static MarkerListViewImplUiBinder uiBinder = GWT
         .create(MarkerListViewImplUiBinder.class);
   @UiField
   Label markerListTitle;
   @UiField(provided = true)
   CellTable<MarkerDTO> markerCellTable;
   @UiField(provided = true)
   SimplePager markerListPager;
   @UiField
   Label markerResultTitle;
   @UiField
   Button buttonCreationMarker;
   @UiField
   Button buttonModifyMarker;
   @UiField
   Button buttonDeleteMarker;
   private ListDataProvider<MarkerDTO> markerDataProvider;
   private SingleSelectionModel<MarkerDTO> singleSelectionModel;
   private Column<MarkerDTO, String> nameColumn;
   private Column<MarkerDTO, String> descriptionColumn;
   private Column<MarkerDTO, String> dateColumn;
   private Column<MarkerDTO, String> markerTypeColumn;
   public MarkerListViewImpl() {
      Common.getResource().css().ensureInjected();
      initProjectsTable();
      initWidget(uiBinder.createAndBindUi(this));

      markerListTitle.setText(Common.getProjectPlanMessages().markerListTitle());
      markerResultTitle.setText(Common.getProjectPlanMessages().markerResultTitle());
      buttonCreationMarker.setText(Common.getGlobal().buttonCreate());
      buttonModifyMarker.setText(Common.getGlobal().buttonModify());
      buttonModifyMarker.setEnabled(false);
      buttonDeleteMarker.setText(Common.getGlobal().buttonDelete());
      buttonDeleteMarker.setEnabled(false);
   }

   public void initProjectsTable() {
      markerCellTable = new CellTable<MarkerDTO>(Common.PAGE_SIZE,
            (Resources) GWT.create(TableResources.class), CellKey.MARKER_KEY_PROVIDER);
      markerCellTable.setWidth("100%", false);

      // Init empty widget
      Label emptyProjectsLabel = new Label(Common.getGlobal().messageNoElements());
      emptyProjectsLabel.setStyleName(Common.getResource().css().emptyLabel());
      markerCellTable.setEmptyTableWidget(emptyProjectsLabel);

      // Create a Pager to control the CellTable
      SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
      markerListPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
      markerListPager.setDisplay(markerCellTable);

      // Initialize the columns.
      initProjectsTableColumns();

      // Add the CellTable to the adapter
      markerDataProvider = new ListDataProvider<MarkerDTO>();
      markerDataProvider.addDataDisplay(markerCellTable);

      // Add the CellTable to the adapter
      ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(
            markerCellTable);
      markerCellTable.addColumnSortHandler(columnSortHandler);

      // Add SingleSelectionModel
      singleSelectionModel = new SingleSelectionModel<MarkerDTO>();
      markerCellTable.setSelectionModel(singleSelectionModel);
   }

   public void initProjectsTableColumns() {

      nameColumn = new Column<MarkerDTO, String>(new TextCell()) {
         @Override
         public String getValue(MarkerDTO object) {
            return object.getName();
         }
      };
      nameColumn.setSortable(true);
      markerCellTable.addColumn(nameColumn, Common.getGlobal().name());

      descriptionColumn = new Column<MarkerDTO, String>(new TextCell()) {
         @Override
         public String getValue(MarkerDTO object) {
            String val = object.getDesc();
            if (object.getDesc() != null && object.getDesc().length() > 100){
               val = val.substring(0, 100) + "[...]";
            }
            return val;
         }
      };
      descriptionColumn.setSortable(true);
      markerCellTable.addColumn(descriptionColumn, Common.getGlobal().description());

      dateColumn = new Column<MarkerDTO, String>(new TextCell()) {
         @Override
         public String getValue(MarkerDTO object) {
            if (object.getDate() != null) {
               return Common.FR_DATE_FORMAT_ONLY_DAY.format(object.getDate());
            } else {
               return null;
            }
         }
      };
      dateColumn.setSortable(true);
      markerCellTable.addColumn(dateColumn, Common.getGlobal().date());

      markerTypeColumn = new Column<MarkerDTO, String>(new TextCell()) {
         @Override
         public String getValue(MarkerDTO object) {
            return object.getMarkerTypeName();
         }
      };
      markerTypeColumn.setSortable(true);
      markerCellTable.addColumn(markerTypeColumn, Common.getGlobal().type());

   }

   @Override
   public Label getMarkerListTitle() {
      return markerListTitle;
   }

   @Override
   public SimplePager getMarkerListPager() {
      return markerListPager;
   }

   @Override
   public Label getMarkerResultTitle() {
      return markerResultTitle;
   }

   @Override
   public Column<MarkerDTO, String> getNameColumn()
   {
      return nameColumn;
   }

   @Override
   public Column<MarkerDTO, String> getDescriptionColumn()
   {
      return descriptionColumn;
   }

   @Override
   public Column<MarkerDTO, String> getDateColumn()
   {
      return dateColumn;
   }

   @Override
   public Column<MarkerDTO, String> getMarkerTypeColumn()
   {
      return markerTypeColumn;
   }

   @Override
   public Button getButtonCreationMarker()
   {
      return buttonCreationMarker;
   }

   @Override
   public Button getButtonModifyMarker()
   {
      return buttonModifyMarker;
   }

   @Override
   public Button getButtonDeleteMarker()
   {
      return buttonDeleteMarker;
   }

   @Override
   public CellTable<MarkerDTO> getMarkerTable() {
      return markerCellTable;
   }

   @Override
   public ListDataProvider<MarkerDTO> getMarkerDataProvider() {
      return markerDataProvider;
   }

   @Override
   public SingleSelectionModel<MarkerDTO> getSingleSelectionModel() {
      return singleSelectionModel;
   }

   @Override
   public void updateCellTableSortHandler()
   {
      // Add the Sort Handler to the CellTable
      ListHandler<MarkerDTO> sortHandler = new ListHandler<MarkerDTO>(markerDataProvider.getList());
      markerCellTable.addColumnSortHandler(sortHandler);

      // nameColumn sort
      sortHandler.setComparator(nameColumn, new Comparator<MarkerDTO>()
      {
         @Override
         public int compare(MarkerDTO o1, MarkerDTO o2)
         {
            return o1.getName().compareTo(o2.getName());
         }
      });

      // descriptionColumn sort
      sortHandler.setComparator(descriptionColumn, new Comparator<MarkerDTO>()
      {
         @Override
         public int compare(MarkerDTO o1, MarkerDTO o2)
         {
            return o1.getDesc().compareTo(o2.getDesc());
         }
      });

      // dateColumn sort
      sortHandler.setComparator(dateColumn, new Comparator<MarkerDTO>()
      {
         @Override
         public int compare(MarkerDTO o1, MarkerDTO o2)
         {
            return o1.getDate().compareTo(o2.getDate());
         }
      });

      // markerParentNameColumn sort
      sortHandler.setComparator(markerTypeColumn, new Comparator<MarkerDTO>()
      {
         @Override
         public int compare(MarkerDTO o1, MarkerDTO o2)
         {
            return o1.getMarkerTypeName().compareTo(o2.getMarkerTypeName());
         }
      });
      if ( markerCellTable.getColumnSortList().size() > 0)
      {
        ColumnSortEvent.fire( markerCellTable, markerCellTable.getColumnSortList());
      }
   }

   interface MarkerListViewImplUiBinder extends UiBinder<Widget, MarkerListViewImpl>
   {
   }
}