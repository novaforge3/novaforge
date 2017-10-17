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
package org.novaforge.forge.tools.managementmodule.ui.client.view.lotsettings;

import java.util.Comparator;

import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;

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

public class LotSettingsListViewImpl extends Composite implements LotSettingsListView {

	   private static LotSettingsListViewImplUiBinder uiBinder        = GWT.create(LotSettingsListViewImplUiBinder.class);
	   @UiField
	   Label                                lotListTitle;
	   @UiField(provided = true)
	   CellTable<LotDTO>    	            lotCellTable;
	   @UiField(provided = true)
	   SimplePager     		                lotListPager;
	   @UiField
	   Label								lotResultTitle;
	   @UiField
	   Button								buttonCreationLot;
	   @UiField
	   Button								buttonModifyLot;
	   @UiField
	   Button								buttonDeleteLot;
	   @UiField
      Button                        buttonShowDiagram;
	   private ListDataProvider<LotDTO>    dataLotProvider;
	   private SingleSelectionModel<LotDTO> singleSelectionModel;
	   private Column<LotDTO, String>      nameColumn;
	   private Column<LotDTO, String>      lotParentNameColumn;
	   private Column<LotDTO, String>      descriptionColumn;
	   private Column<LotDTO, String>      startDateColumn;
	   private Column<LotDTO, String>      endDateColumn;
	   public LotSettingsListViewImpl() {
		   Common.getResource().css().ensureInjected();
		   initProjectsTable();
		   initWidget(uiBinder.createAndBindUi(this));

		   lotListTitle.setText(Common.getProjectPlanMessages().lotListTitle());
	       lotResultTitle.setText(Common.getProjectPlanMessages().lotResultTitle());
	       buttonCreationLot.setText(Common.getGlobal().buttonCreate());
	       buttonModifyLot.setText(Common.getGlobal().buttonModify());
	       buttonModifyLot.setEnabled(false);
	       buttonDeleteLot.setText(Common.getGlobal().buttonDelete());
	       buttonDeleteLot.setEnabled(false);
	       buttonShowDiagram.setText(Common.getGlobal().buttonShowDiagram());
	       buttonShowDiagram.setEnabled(true);

	   }

	public void initProjectsTable() {
		   lotCellTable = new CellTable<LotDTO>(Common.PAGE_SIZE,
		            (Resources) GWT.create(TableResources.class),
		            CellKey.LOT_KEY_PROVIDER);
		   lotCellTable.setWidth("100%", false);

		      // Init empty widget
		      Label emptyProjectsLabel = new Label(Common.getProjectPlanMessages().emptyLotFilterMessage());
		      emptyProjectsLabel.setStyleName(Common.getResource().css().emptyLabel());
		      lotCellTable.setEmptyTableWidget(emptyProjectsLabel);

		      // Create a Pager to control the CellTable
		      SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		      lotListPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		      lotListPager.setDisplay(lotCellTable);

		      // Initialize the columns.
		      initProjectsTableColumns();

		      // Add the CellTable to the adapter
		      dataLotProvider = new ListDataProvider<LotDTO>();
		      dataLotProvider.addDataDisplay(lotCellTable);

		      // Add the CellTable to the adapter
		      ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(lotCellTable);
		      lotCellTable.addColumnSortHandler(columnSortHandler);

		      //Add SingleSelectionModel
		      singleSelectionModel = new SingleSelectionModel<LotDTO>();
		      lotCellTable.setSelectionModel(singleSelectionModel);
	   }
	   
	   public void initProjectsTableColumns() {
			  nameColumn = new Column<LotDTO, String>(new TextCell())
		      {
		         @Override
		         public String getValue(LotDTO object)
		         {
		            return object.getName();
		         }
		      };
		      nameColumn.setSortable(true);
		      lotCellTable.addColumn(nameColumn, Common.getGlobal().name());

			 lotParentNameColumn = new Column<LotDTO, String>(new TextCell())
		      {
			         @Override
			         public String getValue(LotDTO object)
			         {
			            return object.getParentLotName();
			         }
			      };
			      lotParentNameColumn.setSortable(true);
			      lotCellTable.addColumn(lotParentNameColumn, Common.getProjectPlanMessages().lotParentList());

		      descriptionColumn = new Column<LotDTO, String>(new TextCell())
		      {
		         @Override
		         public String getValue(LotDTO object)
		         {
							 String val = object.getDesc();
							 if (object.getDesc() != null && object.getDesc().length() > 100){
		               val = val.substring(0, 100) + "[...]";
		            }
		            return val;
		         }
		      };

//		      descriptionColumn.getCell().
		      descriptionColumn.setSortable(true);
		      lotCellTable.addColumn(descriptionColumn, Common.getGlobal().description());

		      startDateColumn = new Column<LotDTO, String>(new TextCell())
		      {
		         @Override
		         public String getValue(LotDTO object)
		         {
		        	 if(object.getStartDate() != null){
		        		 return Common.FR_DATE_FORMAT_ONLY_DAY.format(object.getStartDate());
		        	 }else {
		        		 return null;
		        	 }
		         }
		      };
		      startDateColumn.setSortable(true);
			  lotCellTable.addColumn(startDateColumn, Common.getProjectPlanMessages().startDate());

			  endDateColumn = new Column<LotDTO, String>(new TextCell())
			  {
			     @Override
			     public String getValue(LotDTO object)
			     {
			    	 if(object.getEndDate() != null){
		        		 return Common.FR_DATE_FORMAT_ONLY_DAY.format(object.getEndDate());
		        	 }else {
		        		 return null;
		        	 }
			     }
			  };
			  endDateColumn.setSortable(true);
			  lotCellTable.addColumn(endDateColumn, Common.getProjectPlanMessages().endDate());
	   }
	   
	   @Override
	   public Label getLotListTitle() {
			return lotListTitle;
		}

	@Override
		public SimplePager getLotListPager() {
			return lotListPager;
		}

	@Override
		public Label getLotResultTitle() {
			return lotResultTitle;
		}

	@Override
		public Button getButtonCreationLot() {
			return buttonCreationLot;
		}

	@Override
		public ListDataProvider<LotDTO> getDataLotProvider() {
			return dataLotProvider;
		}

	@Override
		public Column<LotDTO, String> getNameColumn() {
			return nameColumn;
		}

	@Override
		public Column<LotDTO, String> getLotParentNameColumn() {
			return lotParentNameColumn;
		}

	@Override
		public Column<LotDTO, String> getDescriptionColumn() {
			return descriptionColumn;
		}

	@Override
		public Column<LotDTO, String> getCreationDateColumn() {
			return startDateColumn;
		}

	@Override
		public Column<LotDTO, String> getEndDateColumn() {
			return endDateColumn;
		}

	@Override
	public Button getButtonModifyLot()
	{
		return buttonModifyLot;
	}

	@Override
	public Button getButtonDeleteLot()
	{
		return buttonDeleteLot;
	}

	@Override
	public Button getButtonShowDiagram()
	{
		return buttonShowDiagram;
	}

		@Override
		public CellTable<LotDTO> getLotTable() {
			return lotCellTable;
		}

	@Override
		public ListDataProvider<LotDTO> getLotDataProvider() {
			return dataLotProvider;
		}

		@Override
		public SingleSelectionModel<LotDTO> getSingleSelectionModel() {
			return singleSelectionModel;
		}
		
	@Override
	public void updateCellTableSortHandler()
	{
		// Add the Sort Handler to the CellTable
		ListHandler<LotDTO> sortHandler = new ListHandler<LotDTO>(dataLotProvider.getList());
		lotCellTable.addColumnSortHandler(sortHandler);

		// nameColumn sort
		sortHandler.setComparator(nameColumn, new Comparator<LotDTO>()
		{
			@Override
			public int compare(LotDTO o1, LotDTO o2)
			{
				return o1.getName().compareTo(o2.getName());
			}
		});
		// lotParentNameColumn sort
		sortHandler.setComparator(lotParentNameColumn, new Comparator<LotDTO>()
		{
			@Override
			public int compare(LotDTO o1, LotDTO o2)
			{
				return o1.getParentLotName().compareTo(o2.getParentLotName());
			}
		});
		// descriptionColumn sort
		sortHandler.setComparator(descriptionColumn, new Comparator<LotDTO>()
		{
			@Override
			public int compare(LotDTO o1, LotDTO o2)
			{
				return o1.getDesc().compareTo(o2.getDesc());
			}
		});

		// startDateColumn sort
		sortHandler.setComparator(startDateColumn, new Comparator<LotDTO>()
		{
			@Override
			public int compare(LotDTO o1, LotDTO o2)
			{
				return o1.getStartDate().compareTo(o2.getStartDate());
			}
		});
		// endDateColumn sort
		sortHandler.setComparator(endDateColumn, new Comparator<LotDTO>()
		{
			@Override
			public int compare(LotDTO o1, LotDTO o2)
			{
				return o1.getEndDate().compareTo(o2.getEndDate());
			}
		});
		
    if ( lotCellTable.getColumnSortList().size() > 0)
    {
      ColumnSortEvent.fire( lotCellTable, lotCellTable.getColumnSortList());
    }
	}

	interface LotSettingsListViewImplUiBinder extends UiBinder<Widget, LotSettingsListViewImpl>
	{
	}
}