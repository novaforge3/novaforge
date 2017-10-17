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
package org.novaforge.forge.ui.historization.client.view.functional;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.ui.historization.client.presenter.commons.CellKey;
import org.novaforge.forge.ui.historization.client.properties.LoggingMessage;
import org.novaforge.forge.ui.historization.client.resources.LoggingResources;
import org.novaforge.forge.ui.historization.client.view.commons.celltable.TableResources;
import org.novaforge.forge.ui.historization.shared.FunctionalLogDTO;

/**
 * @author qsivan
 */
public class FunctionalLoggingViewImpl extends Composite implements FunctionalLoggingView
{

  private static GlobalTabViewImplUiBinder uiBinder        = GWT.create(GlobalTabViewImplUiBinder.class);
  private static LoggingResources          ressources      = GWT.create(LoggingResources.class);
  private static int                       pageSize        = 10;
  private static String                    pathSeparator   = "/";
  private final LoggingMessage loggingMessages = (LoggingMessage) GWT.create(LoggingMessage.class);
  @UiField
  Label                                      functionnalLoggingTitle;
  @UiField
  HorizontalPanel                            panelSelectVisualization;
  @UiField
  HorizontalPanel                            panelFunctionalLogsList;
  @UiField
  Button                                     buttonValidateVisualization;
  @UiField
  Button                                     buttonValidateFiltering;
  @UiField
  Button                                     buttonResetFiltering;
  @UiField
  Button                                     buttonExtractCSV;
  @UiField(provided = true)
  CellTable<FunctionalLogDTO>                functionalLogsList;
  @UiField
  RadioButton                                realTimeRadioButton;
  @UiField
  RadioButton                                rangeTimeRadioButton;
  @UiField
  Label                                      realTimeLabel;
  @UiField
  Label                                      rangeTimeLabel;
  @UiField
  DateBox                                    startRangeDateBox;
  @UiField
  DateBox                                    endRangeDateBox;
  @UiField
  ListBox                                    levelListBox;
  @UiField
  ListBox                                    typeListBox;
  @UiField
  ListBox                                    userListBox;
  @UiField
  TextBox                                    keywordTextBox;
  @UiField(provided = true)
  SimplePager                                pager;
  @UiField
  Label                                      levelListLabel;
  @UiField
  Label                                      typeListLabel;
  @UiField
  Label                                      userListLabel;
  @UiField
  Label                                      keywordTextBoxLabel;
  private ListDataProvider<FunctionalLogDTO> dataFunctionalLogProvider;
  private Column<FunctionalLogDTO, String>   dateColumn;
  private Column<FunctionalLogDTO, String>   levelColumn;
  private Column<FunctionalLogDTO, String>   typeColumn;
  private Column<FunctionalLogDTO, String>   userColumn;
  private Column<FunctionalLogDTO, String>   messageColumn;
  public FunctionalLoggingViewImpl()
  {
    ressources.css().ensureInjected();
    initFunctionalLogTable();
    initWidget(uiBinder.createAndBindUi(this));
    functionnalLoggingTitle.setText(loggingMessages.functionnalLoggingTitle());
    buttonValidateVisualization.setText(loggingMessages.buttonValidateVisualizationLabel());
    buttonValidateFiltering.setText(loggingMessages.buttonValidateFilteringLabel());
    buttonResetFiltering.setText(loggingMessages.buttonResetFilteringLabel());
    buttonExtractCSV.setText(loggingMessages.buttonExtractCSVLabel());
    realTimeLabel.setText(loggingMessages.realTimeLabel());
    rangeTimeLabel.setText(loggingMessages.rangeTimeLabel());
    levelListLabel.setText(loggingMessages.levelListLabel());
    typeListLabel.setText(loggingMessages.typeListLabel());
    userListLabel.setText(loggingMessages.userListLabel());
    keywordTextBoxLabel.setText(loggingMessages.keywordTextBoxLabel());
  }

  private void initFunctionalLogTable()
  {
    functionalLogsList = new CellTable<FunctionalLogDTO>(pageSize,
        (Resources) GWT.create(TableResources.class), CellKey.FUNCTIONAL_LOG_KEY_PROVIDER);
    functionalLogsList.setWidth("100%", false);

    // Init empty widget
    final Label emptyFunctionalLogLabel = new Label(loggingMessages.emptyFunctionalLogLabel());

    functionalLogsList.setEmptyTableWidget(emptyFunctionalLogLabel);

    // Create a Pager to control the CellTable
    final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
    pager.setDisplay(functionalLogsList);
    pager.setPage(pageSize);

    // Initialize the columns.
    initFunctionLogTableColumns();

    // Add the CellTable to the adapter
    // dataFunctionalLogProvider = new ListDataProvider<FunctionalLogDTO>();
    // dataFunctionalLogProvider.addDataDisplay(functionalLogsList);

    // Add the CellTable to the adapter
    final ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(
        functionalLogsList);
    functionalLogsList.addColumnSortHandler(columnSortHandler);

    // functionalLogsList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
  }

  private void initFunctionLogTableColumns()
  {
    // Date Column
    dateColumn = new Column<FunctionalLogDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final FunctionalLogDTO object)
      {
        return object.getLogDate();
      }
    };

    dateColumn.setSortable(true);

    // Level Column
    levelColumn = new Column<FunctionalLogDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final FunctionalLogDTO object)
      {
        return object.getLogLevel();
      }
    };

    levelColumn.setSortable(true);

    // Type Column
    typeColumn = new Column<FunctionalLogDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final FunctionalLogDTO object)
      {
        return object.getLogType();
      }
    };

    typeColumn.setSortable(true);

    // User Column
    userColumn = new Column<FunctionalLogDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final FunctionalLogDTO object)
      {
        return object.getLogUser();
      }
    };

    userColumn.setSortable(true);

    // Message Column
    messageColumn = new Column<FunctionalLogDTO, String>(new TextCell())
    {
      @Override
      public String getValue(final FunctionalLogDTO object)
      {
        return object.getLogMessage();
      }
    };

    messageColumn.setSortable(true);

    functionalLogsList.addColumn(dateColumn, loggingMessages.dateLabel());
    functionalLogsList.addColumn(levelColumn, loggingMessages.levelLabel());
    functionalLogsList.addColumn(typeColumn, loggingMessages.typeLabel());
    functionalLogsList.addColumn(userColumn, loggingMessages.userLabel());
    functionalLogsList.addColumn(messageColumn, loggingMessages.messageLabel());
  }

  @Override
  public HorizontalPanel getPanelSelectVisualization()
  {
    return panelSelectVisualization;
  }

  @Override
  public HasClickHandlers getButtonValidateVisualization()
  {
    return buttonValidateVisualization;
  }

  @Override
  public HorizontalPanel getPanelFunctionalLogsList()
  {
    return panelFunctionalLogsList;
  }

  @Override
  public ListDataProvider<FunctionalLogDTO> getDataFunctionalLogProvider()
  {
    return dataFunctionalLogProvider;
  }

  @Override
  public RadioButton getRealTimeRadioButton()
  {
    return realTimeRadioButton;
  }

  @Override
  public RadioButton getRangeTimeRadioButton()
  {
    return rangeTimeRadioButton;
  }

  @Override
  public DateBox getStartRangeDateBox()
  {
    return startRangeDateBox;
  }

  @Override
  public DateBox getEndRangeDateBox()
  {
    return endRangeDateBox;
  }

  @Override
  public ListBox getLevelListBox()
  {
    return levelListBox;
  }

  @Override
  public ListBox getTypeListBox()
  {
    return typeListBox;
  }

  @Override
  public ListBox getUserListBox()
  {
    return userListBox;
  }

  @Override
  public TextBox getKeywordTextBox()
  {
    return keywordTextBox;
  }

  @Override
  public Button getButtonValidateFiltering()
  {
    return buttonValidateFiltering;
  }

  @Override
  public Button getButtonResetFiltering()
  {
    return buttonResetFiltering;
  }

  @Override
  public Button getButtonExtractCSV()
  {
    return buttonExtractCSV;
  }

  @Override
  public CellTable<FunctionalLogDTO> getFunctionalLogsList()
  {
    return functionalLogsList;
  }

  interface GlobalTabViewImplUiBinder extends UiBinder<Widget, FunctionalLoggingViewImpl>
  {
  }
}
