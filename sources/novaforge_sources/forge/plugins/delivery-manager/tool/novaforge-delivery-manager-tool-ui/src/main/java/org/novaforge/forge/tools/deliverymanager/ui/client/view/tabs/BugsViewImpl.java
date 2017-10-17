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

package org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs;

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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.deliverymanager.ui.shared.BugTrackerIssueDTO;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.commons.client.celltable.TableResources;

import java.util.Comparator;

/**
 * @author Guillaume Lamirand
 */
public class BugsViewImpl extends Composite implements BugsView
{

  private static BugsViewImplUiBinder uiBinder = GWT.create(BugsViewImplUiBinder.class);
  @UiField
  Label                                        versionTitle;
  @UiField
  Label                                        versionInfo;
  @UiField
  Label                                        versionLabel;
  @UiField
  ListBox                                      versionListBox;
  @UiField
  VerticalPanel                                bugsListPanel;
  @UiField
  Label                                        bugsTitle;
  @UiField
  Label                                        bugsInfo;
  @UiField(provided = true)
  CellTable<BugTrackerIssueDTO>                bugsTable;
  @UiField(provided = true)
  SimplePager                                  bugsPager;
  @UiField
  Button                                       buttonDisplay;
  @UiField
  Button                                       buttonSave;
  private ListDataProvider<BugTrackerIssueDTO> dataDeliveryProvider;
  private Column<BugTrackerIssueDTO, String>   titleColumn;
  private Column<BugTrackerIssueDTO, String>   descriptionColumn;
  private Column<BugTrackerIssueDTO, String>   severityColumn;
  private Column<BugTrackerIssueDTO, String>   categoryColumn;
  private Column<BugTrackerIssueDTO, String>   reporterColumn;
  public BugsViewImpl()
  {
    this.initBugsTable();
    DeliveryCommon.getResources().css().ensureInjected();

    this.initWidget(uiBinder.createAndBindUi(this));

    // Set txt
    this.versionTitle.setText(DeliveryCommon.getMessages().versionTitle());
    this.versionInfo.setText(DeliveryCommon.getMessages().versionInfo());
    this.versionLabel.setText(DeliveryCommon.getMessages().versionLabel());
    this.bugsTitle.setText(DeliveryCommon.getMessages().bugsTitle());
    this.bugsInfo.setText(DeliveryCommon.getMessages().bugsInfo());
    this.buttonSave.setText(Common.getMessages().save());
    this.buttonDisplay.setText(DeliveryCommon.getMessages().displayBugs());

    //
    this.bugsListPanel.setVisible(false);

  }

  private void initBugsTable()
  {
    this.bugsTable = new CellTable<BugTrackerIssueDTO>(15, (Resources) GWT.create(TableResources.class),
        CellKey.BUG_KEY_PROVIDER);
    this.bugsTable.setWidth("100%", false);

    // Init empty widget
    final Label emptyDeliveryLabel = new Label(DeliveryCommon.getMessages().emptyBugsMessage());
    emptyDeliveryLabel.setStyleName(Common.getResources().css().emptyLabel());
    this.bugsTable.setEmptyTableWidget(emptyDeliveryLabel);

    // Create a Pager to control the CellTable
    final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
    this.bugsPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
    this.bugsPager.setDisplay(this.bugsTable);

    // Initialize the columns.
    this.initBugsTableColumns();

    // Add the CellTable to the adapter
    this.dataDeliveryProvider = new ListDataProvider<BugTrackerIssueDTO>(CellKey.BUG_KEY_PROVIDER);
    this.dataDeliveryProvider.addDataDisplay(this.bugsTable);

    // Add the CellTable to the adapter
    final ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(this.bugsTable);
    this.bugsTable.addColumnSortHandler(columnSortHandler);

  }

  private void initBugsTableColumns()
  {
    // Title Column
    this.titleColumn = new Column<BugTrackerIssueDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final BugTrackerIssueDTO object)
      {
        return object.getTitle();
      }
    };
    this.titleColumn.setSortable(true);
    this.bugsTable.setColumnWidth(this.titleColumn, 30, Unit.PC);
    this.bugsTable.addColumn(this.titleColumn, DeliveryCommon.getMessages().title());

    // Description Column
    this.descriptionColumn = new Column<BugTrackerIssueDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final BugTrackerIssueDTO object)
      {
        return object.getDescription();
      }
    };
    this.descriptionColumn.setSortable(true);
    this.bugsTable.setColumnWidth(this.descriptionColumn, 30, Unit.PC);
    this.bugsTable.addColumn(this.descriptionColumn, DeliveryCommon.getMessages().description());
    // Severity Column
    this.severityColumn = new Column<BugTrackerIssueDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final BugTrackerIssueDTO object)
      {
        return object.getSeverity();
      }
    };
    this.severityColumn.setSortable(true);
    this.bugsTable.setColumnWidth(this.severityColumn, 30, Unit.PC);
    this.bugsTable.addColumn(this.severityColumn, DeliveryCommon.getMessages().severity());

    // Category Column
    this.categoryColumn = new Column<BugTrackerIssueDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final BugTrackerIssueDTO object)
      {
        return object.getCategory();
      }
    };
    this.categoryColumn.setSortable(true);
    this.bugsTable.setColumnWidth(this.categoryColumn, 30, Unit.PC);
    this.bugsTable.addColumn(this.categoryColumn, DeliveryCommon.getMessages().category());
    // Reporter Column
    this.reporterColumn = new Column<BugTrackerIssueDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final BugTrackerIssueDTO object)
      {
        return object.getReporter();
      }
    };
    this.reporterColumn.setSortable(true);
    this.bugsTable.setColumnWidth(this.reporterColumn, 30, Unit.PC);
    this.bugsTable.addColumn(this.reporterColumn, DeliveryCommon.getMessages().reporter());

  }

  @Override
  public CellTable<BugTrackerIssueDTO> getBugsTable()
  {
    return this.bugsTable;
  }

  @Override
  public ListDataProvider<BugTrackerIssueDTO> getListDataProvider()
  {
    return this.dataDeliveryProvider;
  }

  @Override
  public Button getSaveButton()
  {
    return this.buttonSave;
  }

  @Override
  public void bugsListSortHandler()
  {
    final ListHandler<BugTrackerIssueDTO> sortHandler = new ListHandler<BugTrackerIssueDTO>(
        this.dataDeliveryProvider.getList());
    this.bugsTable.addColumnSortHandler(sortHandler);

    sortHandler.setComparator(this.titleColumn, new Comparator<BugTrackerIssueDTO>()
    {
      @Override
      public int compare(final BugTrackerIssueDTO o1, final BugTrackerIssueDTO o2)
      {
        return o1.getTitle().compareTo(o2.getTitle());
      }
    });
    sortHandler.setComparator(this.descriptionColumn, new Comparator<BugTrackerIssueDTO>()
    {
      @Override
      public int compare(final BugTrackerIssueDTO o1, final BugTrackerIssueDTO o2)
      {
        return o1.getDescription().compareTo(o2.getDescription());
      }
    });
    sortHandler.setComparator(this.categoryColumn, new Comparator<BugTrackerIssueDTO>()
    {
      @Override
      public int compare(final BugTrackerIssueDTO o1, final BugTrackerIssueDTO o2)
      {
        return o1.getCategory().compareTo(o2.getCategory());
      }
    });
    sortHandler.setComparator(this.reporterColumn, new Comparator<BugTrackerIssueDTO>()
    {
      @Override
      public int compare(final BugTrackerIssueDTO o1, final BugTrackerIssueDTO o2)
      {
        return o1.getReporter().compareTo(o2.getReporter());
      }
    });
  }

  @Override
  public Button getDisplayButton()
  {
    return this.buttonDisplay;
  }

  @Override
  public ListBox getVersionList()
  {
    return this.versionListBox;
  }

  @Override
  public Panel getBugsListPanel()
  {
    return this.bugsListPanel;
  }

  interface BugsViewImplUiBinder extends UiBinder<Widget, BugsViewImpl>
  {
  }

}
