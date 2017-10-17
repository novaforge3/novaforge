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
package org.novaforge.forge.tools.deliverymanager.ui.client.view;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.shared.DeliveryDTO;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.commons.client.celltable.TableResources;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;

import java.util.Comparator;

/**
 * @author HUANG-V
 */
public class DeliveryListViewImpl extends Composite implements DeliveryListView
{
  private static DeliveryManagementViewImplUiBinder uiBinder = GWT
                                                                 .create(DeliveryManagementViewImplUiBinder.class);
  @UiField
  HorizontalPanel                       panelActionsDelivery;
  @UiField
  Button                                buttonCreateDelivery;
  @UiField
  Button                                buttonManageDeliveryNote;
  @UiField
  Label                                 deliveryManagementTitle;
  @UiField
  Label                                 deliverySearchTitle;
  @UiField
  Label                                 deliveryResultTitle;
  @UiField
  Label                                 statusSearchLabel;
  @UiField
  ListBox                               statusSearchLB;
  @UiField
  Label                                 typeSearchLabel;
  @UiField
  ListBox                               typeSearchLB;
  @UiField(provided = true)
  CellTable<DeliveryDTO>                deliveryCellTable;
  @UiField(provided = true)
  SimplePager                           deliveryPager;
  ValidateDialogBox                     lockDeliveryPopup;
  ValidateDialogBox                     deleteDeliveryPopup;
  InfoDialogBox                         successGeneratedDialogBox;
  InfoDialogBox                         failedGeneratedDialogBox;
  private ListDataProvider<DeliveryDTO> dataDeliveryProvider;
  private Column<DeliveryDTO, String>   typeColumn;
  private Column<DeliveryDTO, String>   nameColumn;
  private Column<DeliveryDTO, String>   versionColumn;
  private Column<DeliveryDTO, String>   statusColumn;
  private Column<DeliveryDTO, String>   deliveryColumn;
  public DeliveryListViewImpl()
  {
    DeliveryCommon.getResources().css().ensureInjected();
    this.initDeliveryTable();
    this.deleteDeliveryPopup = new ValidateDialogBox(DeliveryCommon.getMessages().deleteConfirm());
    this.lockDeliveryPopup = new ValidateDialogBox(DeliveryCommon.getMessages().deliveredConfirm());
    this.successGeneratedDialogBox = new InfoDialogBox(DeliveryCommon.getMessages().generationSuccess(),
        InfoTypeEnum.SUCCESS, 2500);
    this.failedGeneratedDialogBox = new InfoDialogBox(DeliveryCommon.getMessages().generationFailed(),
        InfoTypeEnum.ERROR, 2500);
    this.initWidget(uiBinder.createAndBindUi(this));

    this.buttonCreateDelivery.setText(DeliveryCommon.getMessages().buttonCreateDelivery());
    this.buttonManageDeliveryNote.setText(DeliveryCommon.getMessages().buttonManageDeliveryNote());
    this.deliveryManagementTitle.setText(DeliveryCommon.getMessages().deliveryManagementTitle());
    this.deliverySearchTitle.setText(DeliveryCommon.getMessages().filterTitle());
    this.deliveryResultTitle.setText(DeliveryCommon.getMessages().resultTitle());
    this.statusSearchLabel.setText(this.formStyle(DeliveryCommon.getMessages().status()));
    this.typeSearchLabel.setText(this.formStyle(DeliveryCommon.getMessages().type()));
    this.statusSearchLB.ensureDebugId("statusSearch");
    this.typeSearchLB.ensureDebugId("typeSearch");
  }

  private void initDeliveryTable()
  {
    this.deliveryCellTable = new CellTable<DeliveryDTO>(8, (Resources) GWT.create(TableResources.class));
    this.deliveryCellTable.setWidth("100%", false);

    // Init empty widget
    final Label emptyDeliveryLabel = new Label(DeliveryCommon.getMessages().emptyDeliveryFilterMessage());
    emptyDeliveryLabel.setStyleName(Common.getResources().css().emptyLabel());
    this.deliveryCellTable.setEmptyTableWidget(emptyDeliveryLabel);

    // Create a Pager to control the CellTable
    final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
    this.deliveryPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
    this.deliveryPager.setDisplay(this.deliveryCellTable);

    // Initialize the columns.
    this.initDeliveryTableColumns();

    // Add the CellTable to the adapter
    this.dataDeliveryProvider = new ListDataProvider<DeliveryDTO>();
    this.dataDeliveryProvider.addDataDisplay(this.deliveryCellTable);

    // Add the CellTable to the adapter
    final ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(
        this.deliveryCellTable);
    this.deliveryCellTable.addColumnSortHandler(columnSortHandler);

  }

  private String formStyle(final String pText)
  {
    return pText + " : ";
  }

  private void initDeliveryTableColumns()
  {
    // Type Column
    this.typeColumn = new Column<DeliveryDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final DeliveryDTO object)
      {
        return object.getType();
      }
    };
    this.typeColumn.setSortable(true);
    this.deliveryCellTable.addColumn(this.typeColumn, DeliveryCommon.getMessages().type());
    this.deliveryCellTable.setColumnWidth(this.typeColumn, 150, Unit.PX);

    // Title Column
    this.nameColumn = new Column<DeliveryDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final DeliveryDTO object)
      {
        return object.getName();
      }
    };
    this.nameColumn.setSortable(true);
    this.deliveryCellTable.setColumnWidth(this.nameColumn, 30, Unit.PC);
    this.deliveryCellTable.addColumn(this.nameColumn, DeliveryCommon.getMessages().name());

    // Version column
    this.versionColumn = new Column<DeliveryDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final DeliveryDTO object)
      {
        return object.getVersion();
      }
    };
    this.versionColumn.setSortable(true);
    this.deliveryCellTable.addColumn(this.versionColumn, DeliveryCommon.getMessages().version());
    this.deliveryCellTable.setColumnWidth(this.versionColumn, 150, Unit.PX);

    // status column
    this.statusColumn = new Column<DeliveryDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final DeliveryDTO object)
      {
        return object.getStatus().getLabel();
      }
    };
    this.statusColumn.setSortable(true);
    this.deliveryCellTable.addColumn(this.statusColumn, DeliveryCommon.getMessages().status());
    this.deliveryCellTable.setColumnWidth(this.statusColumn, 150, Unit.PX);

    // Delivered date Column
    this.deliveryColumn = new Column<DeliveryDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final DeliveryDTO object)
      {
        if (object.getDeliveryDate() != null)
        {
          return Common.getDateFormat().format(object.getDeliveryDate());
        }
        else
        {
          return null;
        }
      }
    };
    this.deliveryColumn.setSortable(true);
    this.deliveryCellTable.addColumn(this.deliveryColumn, DeliveryCommon.getMessages().deliveryDate());
    this.deliveryCellTable.setColumnWidth(this.deliveryColumn, 150, Unit.PX);

  }

  @Override
  public CellTable<DeliveryDTO> getDeliveryTable()
  {
    return this.deliveryCellTable;
  }

  @Override
  public ListDataProvider<DeliveryDTO> getDeliveryDataProvider()
  {
    return this.dataDeliveryProvider;
  }

  @Override
  public ListBox getTypeSearch()
  {
    return this.typeSearchLB;
  }

  @Override
  public ListBox getStatusSearch()
  {
    return this.statusSearchLB;
  }

  @Override
  public void deliveryListSortHandler()
  {
    final ListHandler<DeliveryDTO> sortHandler = new ListHandler<DeliveryDTO>(
        this.dataDeliveryProvider.getList());
    this.deliveryCellTable.addColumnSortHandler(sortHandler);

    sortHandler.setComparator(this.nameColumn, new Comparator<DeliveryDTO>()
    {
      @Override
      public int compare(final DeliveryDTO o1, final DeliveryDTO o2)
      {
        return o1.getName().compareTo(o2.getName());
      }
    });

    sortHandler.setComparator(this.typeColumn, new Comparator<DeliveryDTO>()
    {
      @Override
      public int compare(final DeliveryDTO o1, final DeliveryDTO o2)
      {
        return o1.getType().compareTo(o2.getType());
      }
    });

    sortHandler.setComparator(this.statusColumn, new Comparator<DeliveryDTO>()
    {
      @Override
      public int compare(final DeliveryDTO o1, final DeliveryDTO o2)
      {
        return o1.getStatus().compareTo(o2.getStatus());
      }
    });

    sortHandler.setComparator(this.versionColumn, new Comparator<DeliveryDTO>()
    {
      @Override
      public int compare(final DeliveryDTO o1, final DeliveryDTO o2)
      {
        return o1.getVersion().compareTo(o2.getVersion());
      }
    });

    sortHandler.setComparator(this.deliveryColumn, new Comparator<DeliveryDTO>()
    {
      @Override
      public int compare(final DeliveryDTO o1, final DeliveryDTO o2)
      {
        return o1.getDeliveryDate().compareTo(o2.getDeliveryDate());
      }
    });
  }

  @Override
  public HasClickHandlers getButtonCreateDelivery()
  {
    return this.buttonCreateDelivery;
  }

  @Override
  public HasClickHandlers getButtonManageDeliveryNote()
  {
    return this.buttonManageDeliveryNote;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ValidateDialogBox getLockDeliveryPopup()
  {
    return this.lockDeliveryPopup;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InfoDialogBox getSuccessGeneratedDialogBox()
  {
    return this.successGeneratedDialogBox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InfoDialogBox getFailedGeneratedDialogBox()
  {
    return this.failedGeneratedDialogBox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ValidateDialogBox getDeleteDeliveryPopup()
  {
    return this.deleteDeliveryPopup;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HorizontalPanel getActionsPanel()
  {
    return panelActionsDelivery;
  }

  interface DeliveryManagementViewImplUiBinder extends UiBinder<Widget, DeliveryListViewImpl>
  {
  }

}
