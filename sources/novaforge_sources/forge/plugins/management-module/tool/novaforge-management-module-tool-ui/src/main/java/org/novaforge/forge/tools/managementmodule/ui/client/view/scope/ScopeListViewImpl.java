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

package org.novaforge.forge.tools.managementmodule.ui.client.view.scope;

import java.util.Comparator;

import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitStatusEnum;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.DomEvent;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * @author vvigo
 */
public class ScopeListViewImpl extends Composite implements ScopeListView {
   static final  Comparator<ScopeUnitDTO>           SCOPE_UNIT_COMPARATOR = new Comparator<ScopeUnitDTO>()
   {
      @Override
      public int compare(ScopeUnitDTO o1, ScopeUnitDTO o2)
      {
         return o1.getName().compareTo(o2.getName());
      }
   };
   private static DeliveryManagementViewImplUiBinder uiBinder = GWT
			.create(DeliveryManagementViewImplUiBinder.class);
   private final SingleSelectionModel<ScopeUnitDTO> selectionModel        = new SingleSelectionModel<ScopeUnitDTO>();
   @UiField
   Label                                            lotScopeSearchTitle;
   @UiField
   Label                                            scopeNameSearchLabel;
   @UiField
   Label                                            scopeTypeSearchLabel;
   @UiField
   Label                                            scopeManualSearchLabel;
   @UiField
   Label                                            scopeLotSearchLabel;
   @UiField
   Label                                            lotScopeResultTitle;
   @UiField
   TextBox                                          scopeNameSearchTB;
   @UiField
   ListBox                                          scopeTypeSearchTB;
   @UiField
   ListBox                                          scopeManualSearchTB;
   @UiField
   TextBox                                          scopeLotSearchTB;
   @UiField(provided = true)
   CellTable<ScopeUnitDTO>                          scopeCellTable;
   @UiField(provided = true)
   SimplePager                                      lotScopePager;
   private ListDataProvider<ScopeUnitDTO>           dataLotScopeProvider;
   private Column<ScopeUnitDTO, String>             lotColumn;
   private Column<ScopeUnitDTO, String>             parentLotColumn;
   private Column<ScopeUnitDTO, String>             upNameColumn;
   private Column<ScopeUnitDTO, String>             upParentNameColumn;
   private Column<ScopeUnitDTO, String>             versionColumn;
   private Column<ScopeUnitDTO, String>             typeColumn;
   private Column<ScopeUnitDTO, String>             manualCreationColumn;
   private Column<ScopeUnitDTO, String>             statusColumn;

	public ScopeListViewImpl() {

      Common.getResource().css().ensureInjected();
      initLotScopeTable();
      initWidget(uiBinder.createAndBindUi(this));

		lotScopeSearchTitle.setText(Common.MESSAGES_SCOPE.lotScopeSearchTitle());
		scopeNameSearchLabel.setText(Common.MESSAGES_SCOPE.scopeNameSearchLabel());
		scopeTypeSearchLabel.setText(Common.MESSAGES_SCOPE.scopeTypeSearchLabel());

		scopeManualSearchLabel.setText(Common.MESSAGES_SCOPE.scopeManualSearchLabel());
		scopeLotSearchLabel.setText(Common.MESSAGES_SCOPE.scopeLotSearchLabel());
		lotScopeResultTitle.setText(Common.MESSAGES_SCOPE.lotScopeResultTitle());
   }

	private void initLotScopeTable() {
      scopeCellTable = new CellTable<ScopeUnitDTO>(Common.PAGE_SIZE,
            (Resources) GWT.create(TableResources.class));
      scopeCellTable.setWidth("100%", false);

      // Init empty widget
		Label emptyLotScopeLabel = new Label(Common.MESSAGES_SCOPE.emptyLotScopeFilterMessage());
      emptyLotScopeLabel.setStyleName(Common.getResource().css().emptyLabel());
      scopeCellTable.setEmptyTableWidget(emptyLotScopeLabel);

      // Create a Pager to control the CellTable
      SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
      lotScopePager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
      lotScopePager.setDisplay(scopeCellTable);

      // Initialize the columns.
      initLotScopeTableColumns();

      // Add the CellTable to the adapter
      dataLotScopeProvider = new ListDataProvider<ScopeUnitDTO>();
      dataLotScopeProvider.addDataDisplay(scopeCellTable);

      // Add the CellTable to the adapter
      ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(scopeCellTable);
      scopeCellTable.addColumnSortHandler(columnSortHandler);

      scopeCellTable.setSelectionModel(selectionModel);
   }

	private void initLotScopeTableColumns() {
      // unite de permietre Column
		upNameColumn = new Column<ScopeUnitDTO, String>(new TextCell()) {
         @Override
			public String getValue(ScopeUnitDTO object) {
            return object.getName();
         }
      };
      upNameColumn.setSortable(true);
      scopeCellTable.setColumnWidth(upNameColumn, 200, Unit.PX);
		scopeCellTable.addColumn(upNameColumn, Common.GLOBAL.SU());

      // unite de perimetre Parent Column
		upParentNameColumn = new Column<ScopeUnitDTO, String>(new TextCell()) {
         @Override
			public String getValue(ScopeUnitDTO object) {
				if (object.getParentScopeUnit() != null) {
               return object.getParentScopeUnit().getName();
				} else {
               return "";
            }
         }
      };
      upParentNameColumn.setSortable(true);
      scopeCellTable.setColumnWidth(upParentNameColumn, 200, Unit.PX);
		scopeCellTable.addColumn(upParentNameColumn, Common.GLOBAL.SUParent());

      // Lot Column
		lotColumn = new Column<ScopeUnitDTO, String>(new TextCell()) {
         @Override
			public String getValue(ScopeUnitDTO object) {
            return object.getLotName();
         }
      };
      lotColumn.setSortable(true);
      scopeCellTable.setColumnWidth(lotColumn, 100, Unit.PX);
		scopeCellTable.addColumn(lotColumn, Common.MESSAGES_SCOPE.lotColumn());

      // parent Column
		parentLotColumn = new Column<ScopeUnitDTO, String>(new TextCell()) {
         @Override
			public String getValue(ScopeUnitDTO object) {
				if (object.getParentLotName() != null && !object.getParentLotName().equals("")) {
               return object.getParentLotName();
				} else {
               return "";
            }

         }
      };
      parentLotColumn.setSortable(true);
      scopeCellTable.setColumnWidth(parentLotColumn, 100, Unit.PX);
		scopeCellTable.addColumn(parentLotColumn, Common.GLOBAL.parentLot());

      // Version Column
		versionColumn = new Column<ScopeUnitDTO, String>(new TextCell()) {
         @Override
			public String getValue(ScopeUnitDTO object) {
            return object.getVersion();
         }
      };
      versionColumn.setSortable(true);
      scopeCellTable.addColumn(versionColumn, Common.getGlobal().version());
      scopeCellTable.setColumnWidth(versionColumn, 30, Unit.PX);

      // Type Column
		typeColumn = new Column<ScopeUnitDTO, String>(new TextCell()) {
         @Override
			public String getValue(ScopeUnitDTO object) {
            return object.getType();
         }
      };
      typeColumn.setSortable(true);
		scopeCellTable.addColumn(typeColumn, Common.MESSAGES_SCOPE.typeColumn());
      scopeCellTable.setColumnWidth(typeColumn, 30, Unit.PX);

      // Manual creation column
		manualCreationColumn = new Column<ScopeUnitDTO, String>(new TextCell()) {
         @Override
			public String getValue(ScopeUnitDTO object) {
        	 String ret = Common.MESSAGES_SCOPE.isManualFalse();
				if (object.isManual()) {
               return Common.MESSAGES_SCOPE.isManualTrue();
            }
            return ret;

         }
      };
      manualCreationColumn.setSortable(true);
		scopeCellTable.addColumn(manualCreationColumn, Common.MESSAGES_SCOPE.manualCreationColumn());
      scopeCellTable.setColumnWidth(manualCreationColumn, 20, Unit.PX);

      // status column
		statusColumn = new Column<ScopeUnitDTO, String>(new TextCell()) {

         @Override
			public String getValue(ScopeUnitDTO object) {
				if (object.getStatus().equals(ScopeUnitStatusEnum.obsolete.name())) {
					return Common.MESSAGES_SCOPE.statusObsolete();
				} else if (object.getStatus().equals(ScopeUnitStatusEnum.modified.name())) {
					return Common.MESSAGES_SCOPE.statusModified();
				} else {
               return "";
            }

         }
      };
      statusColumn.setSortable(true);
		scopeCellTable.addColumn(statusColumn, Common.MESSAGES_SCOPE.scopeStatus());
      scopeCellTable.setColumnWidth(statusColumn, 30, Unit.PX);

   }

   @Override
   public CellTable<ScopeUnitDTO> getScopeTable()
   {
      return scopeCellTable;
   }

   @Override
   public ListDataProvider<ScopeUnitDTO> getLotScopeDataProvider()
   {
      return dataLotScopeProvider;
   }

   @Override
   public SingleSelectionModel<ScopeUnitDTO> getSelectionModel()
   {
      return selectionModel;
   }
   
	@Override
   public void scopeLotListSortHandler() {
      ListHandler<ScopeUnitDTO> sortHandler = new ListHandler<ScopeUnitDTO>(dataLotScopeProvider.getList());
      scopeCellTable.addColumnSortHandler(sortHandler);

		sortHandler.setComparator(lotColumn, new Comparator<ScopeUnitDTO>() {
         @Override
			public int compare(ScopeUnitDTO o1, ScopeUnitDTO o2) {
            return o1.getLotName().compareTo(o2.getLotName());
         }
            });

		sortHandler.setComparator(parentLotColumn, new Comparator<ScopeUnitDTO>() {
         @Override
			public int compare(ScopeUnitDTO o1, ScopeUnitDTO o2) {
            return o1.getParentLotName().compareTo(o2.getParentLotName());
         }
            });

		sortHandler.setComparator(upNameColumn, SCOPE_UNIT_COMPARATOR);

		sortHandler.setComparator(upParentNameColumn, new Comparator<ScopeUnitDTO>() {
         @Override
			public int compare(ScopeUnitDTO o1, ScopeUnitDTO o2) {
				int ret = 0;
				// if both scopeUnit have parent
				if (o1.getParentScopeUnit() != null && o2.getParentScopeUnit() != null) {
					ret = o1.getParentScopeUnit().getName().compareTo(o2.getParentScopeUnit().getName());
				} else if (o1.getParentScopeUnit() == null) {
					ret = -1;
				} else if (o2.getParentScopeUnit() == null) {
					ret = 1;
            }
				return ret;

         }
            });

     sortHandler.setComparator(versionColumn, new Comparator<ScopeUnitDTO>() {
         @Override
			public int compare(ScopeUnitDTO o1, ScopeUnitDTO o2) {
            return o1.getVersion().compareTo(o2.getVersion());
         }
            });

		sortHandler.setComparator(typeColumn, new Comparator<ScopeUnitDTO>() {
         @Override
			public int compare(ScopeUnitDTO o1, ScopeUnitDTO o2) {
            return o1.getType().compareTo(o2.getType());
         }
            });

		sortHandler.setComparator(manualCreationColumn, new Comparator<ScopeUnitDTO>() {
         @Override
			public int compare(ScopeUnitDTO o1, ScopeUnitDTO o2) {
            return (Boolean.valueOf(o1.isManual()).compareTo(Boolean.valueOf(o2.isManual())));
         }
            });

		sortHandler.setComparator(statusColumn, new Comparator<ScopeUnitDTO>() {
         @Override
			public int compare(ScopeUnitDTO o1, ScopeUnitDTO o2) {
            return o1.getStatus().compareTo(o2.getStatus());
         }
            });
    if ( scopeCellTable.getColumnSortList().size() > 0)
    {
      ColumnSortEvent.fire( scopeCellTable, scopeCellTable.getColumnSortList());
    }
   }

	public void applyFiltering(){
    if (getScopeManualSearchTB().getSelectedIndex() > 0)
    {
      DomEvent.fireNativeEvent(Document.get().createChangeEvent(), getScopeManualSearchTB());
    }
    if (getScopeNameSearchTB().getValue() != null &&
        getScopeNameSearchTB().getValue().length() > 0)
    {
      DomEvent.fireNativeEvent(Document.get().createChangeEvent(), getScopeNameSearchTB());
    }

    if (getScopeLotSearchTB().getValue() != null &&
        getScopeLotSearchTB().getValue().length() > 0)
    {
      DomEvent.fireNativeEvent(Document.get().createChangeEvent(), getScopeLotSearchTB());
    }

    if (getScopeTypeSearchTB().getSelectedIndex() > 0)
    {
      DomEvent.fireNativeEvent(Document.get().createChangeEvent(), getScopeTypeSearchTB());
    }
	}
	
   @Override
	public TextBox getScopeNameSearchTB() {
      return scopeNameSearchTB;
   }

   @Override
	public ListBox getScopeTypeSearchTB() {
      return scopeTypeSearchTB;
   }

   @Override
	public ListBox getScopeManualSearchTB() {
      return scopeManualSearchTB;
   }

   @Override
	public TextBox getScopeLotSearchTB() {
      return scopeLotSearchTB;
   }

	@Override
	public Comparator<ScopeUnitDTO> getScopeUnitComparator() {
		return SCOPE_UNIT_COMPARATOR;
	}

   interface DeliveryManagementViewImplUiBinder extends UiBinder<Widget, ScopeListViewImpl>
   {
   }

}
