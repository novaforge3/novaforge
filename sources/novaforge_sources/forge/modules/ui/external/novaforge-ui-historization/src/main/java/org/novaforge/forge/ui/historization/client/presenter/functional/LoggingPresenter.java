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
package org.novaforge.forge.ui.historization.client.presenter.functional;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import org.novaforge.forge.ui.historization.client.HistorizationEntryPoint;
import org.novaforge.forge.ui.historization.client.helper.AbstractHistorizationRPCCall;
import org.novaforge.forge.ui.historization.client.presenter.Presenter;
import org.novaforge.forge.ui.historization.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.historization.client.properties.ErrorMessage;
import org.novaforge.forge.ui.historization.client.properties.LoggingMessage;
import org.novaforge.forge.ui.historization.client.view.functional.FunctionalLoggingView;
import org.novaforge.forge.ui.historization.shared.FunctionalLogDTO;
import org.novaforge.forge.ui.historization.shared.export.CSVProperties;

import java.util.Date;
import java.util.List;

/**
 * @author qsivan
 */
public class LoggingPresenter implements Presenter
{
  private final LoggingMessage                outilMessages = (LoggingMessage) GWT
                                                                .create(LoggingMessage.class);

  private final ErrorMessage                  errorMessages = (ErrorMessage) GWT.create(ErrorMessage.class);

  private final FunctionalLoggingView         display;
  private int                                 start;
  private int                                 length;
  private int                                 end;
  private AsyncDataProvider<FunctionalLogDTO> dataProvider;
  private String                              fixedDate;

  public LoggingPresenter(final String pFiltrageEtat, final FunctionalLoggingView display)
  {
    super();
    this.display = display;

    bind();
  }

  public void bind()
  {
    display.getRealTimeRadioButton().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(final ClickEvent event)
      {
        display.getStartRangeDateBox().setEnabled(false);
        display.getEndRangeDateBox().setEnabled(false);
      }
    });

    display.getRangeTimeRadioButton().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(final ClickEvent event)
      {
        display.getStartRangeDateBox().setEnabled(true);
        display.getEndRangeDateBox().setEnabled(true);
      }
    });

    display.getButtonValidateVisualization().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(final ClickEvent event)
      {
        if (LoggingPresenter.this.display.getRangeTimeRadioButton().getValue())
        {
          if (LoggingPresenter.this.display.getStartRangeDateBox().getValue()
              .after(LoggingPresenter.this.display.getEndRangeDateBox().getValue()))
          {
            ErrorManagement.displayErrorMessage(errorMessages.INTERVAL_DATES_CONTROL());
            return;
          }
        }

        display.getLevelListBox().clear();
        display.getTypeListBox().clear();
        display.getUserListBox().clear();
        display.getKeywordTextBox().setText(null);
        display.getLevelListBox().addItem(outilMessages.allLabel());
        display.getTypeListBox().addItem(outilMessages.allLabel());
        display.getUserListBox().addItem(outilMessages.allLabel());

        // Create a data provider.
        start = 0;
        display.getFunctionalLogsList().setPageStart(0);
        setEndValueForAsyncProvider();
        dataProvider = new AsyncDataProvider<FunctionalLogDTO>()
        {
          @Override
          protected void onRangeChanged(final HasData<FunctionalLogDTO> display)
          {
            length = display.getVisibleRange().getLength();
            start = LoggingPresenter.this.display.getFunctionalLogsList().getPageStart();
            new AbstractHistorizationRPCCall<List<FunctionalLogDTO>>()
            {
              @Override
              protected void callService(final AsyncCallback<List<FunctionalLogDTO>> pCb)
              {
                final String level = getLevel();
                final String type = getType();
                final String user = getUser();
                final String keyword = getKeyword();

                if (LoggingPresenter.this.display.getRangeTimeRadioButton().getValue())
                {
                  final Date startDate = LoggingPresenter.this.display.getStartRangeDateBox().getValue();
                  final Date endDate = LoggingPresenter.this.display.getEndRangeDateBox().getValue();

                  HistorizationEntryPoint.getServiceAsync().getListFunctionalLogs(level, type, user, keyword,
                      startDate, endDate, start, length, null, pCb);
                }
                else
                {
                  HistorizationEntryPoint.getServiceAsync().getListFunctionalLogs(level, type, user, keyword,
                      null, null, start, length, null, pCb);
                }
              }

              @Override
              public void onFailure(final Throwable caught)
              {
                ErrorManagement.displayErrorMessage(caught);
              }

              @Override
              public void onSuccess(final List<FunctionalLogDTO> pResult)
              {
                if ((pResult != null) && (pResult.size() > 0))
                {
                  updateRowData(start, pResult);
                  updateRowCount(end, true);
                  LoggingPresenter.this.display.getButtonExtractCSV().setEnabled(true);
                  fixedDate = pResult.get(0).getLogDate();
                }
                else
                {
                  updateRowCount(0, true);
                  LoggingPresenter.this.display.getButtonExtractCSV().setEnabled(false);
                }
              }



            }.retry(0);
          }
        };

        initFilteringZone();
        dataProvider.addDataDisplay(display.getFunctionalLogsList());

        display.getPanelFunctionalLogsList().setVisible(true);
      }
    });

    display.getButtonValidateFiltering().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(final ClickEvent event)
      {
        setEndValueForAsyncProvider();
        new AbstractHistorizationRPCCall<List<FunctionalLogDTO>>()
        {
          @Override
          protected void callService(final AsyncCallback<List<FunctionalLogDTO>> pCb)
          {
            start = 0;

            final String level = getLevel();
            final String type = getType();
            final String user = getUser();
            final String keyword = getKeyword();

            if (display.getRangeTimeRadioButton().getValue())
            {
              final Date startDate = display.getStartRangeDateBox().getValue();
              final Date endDate = display.getEndRangeDateBox().getValue();

              HistorizationEntryPoint.getServiceAsync().getListFunctionalLogs(level, type, user, keyword,
                  startDate, endDate, start, length, null, pCb);
            }
            else
            {
              HistorizationEntryPoint.getServiceAsync().getListFunctionalLogs(level, type, user, keyword,
                  null, null, start, length, fixedDate, pCb);
            }
          }

          @Override
          public void onSuccess(final List<FunctionalLogDTO> pResult)
          {
            if ((pResult != null) && (pResult.size() > 0))
            {
              dataProvider.updateRowData(0, pResult);
              dataProvider.updateRowCount(end, true);
              display.getFunctionalLogsList().setPageStart(0);
              display.getButtonExtractCSV().setEnabled(true);
            }
            else
            {
              dataProvider.updateRowCount(0, true);
              display.getButtonExtractCSV().setEnabled(false);
            }
          }

          @Override
          public void onFailure(final Throwable caught)
          {
            ErrorManagement.displayErrorMessage(caught);
          }

        }.retry(0);

      }

    });

    display.getButtonResetFiltering().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(final ClickEvent event)
      {
        // We reset the filtering params and reload initial datas
        display.getLevelListBox().setSelectedIndex(0);
        display.getTypeListBox().setSelectedIndex(0);
        display.getUserListBox().setSelectedIndex(0);
        display.getKeywordTextBox().setText(null);

        setEndValueForAsyncProvider();

        new AbstractHistorizationRPCCall<List<FunctionalLogDTO>>()
        {
          @Override
          protected void callService(final AsyncCallback<List<FunctionalLogDTO>> pCb)
          {
            start = 0;
            if (display.getRangeTimeRadioButton().getValue())
            {
              final Date startDate = display.getStartRangeDateBox().getValue();
              final Date endDate = display.getEndRangeDateBox().getValue();

              HistorizationEntryPoint.getServiceAsync().getListFunctionalLogs(null, null, null, null,
                  startDate, endDate, start, length, null, pCb);
            }
            else
            {
              HistorizationEntryPoint.getServiceAsync().getListFunctionalLogs(null, null, null, null, null,
                  null, start, length, fixedDate, pCb);
            }
          }

          @Override
          public void onSuccess(final List<FunctionalLogDTO> pResult)
          {
            if ((pResult != null) && (pResult.size() > 0))
            {
              dataProvider.updateRowData(0, pResult);
              dataProvider.updateRowCount(end, true);
              display.getFunctionalLogsList().setPageStart(0);
            }
            else
            {
              dataProvider.updateRowCount(0, true);
            }
          }

          @Override
          public void onFailure(final Throwable caught)
          {
            ErrorManagement.displayErrorMessage(caught);
          }

        }.retry(0);
      }

    });

    display.getButtonExtractCSV().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(final ClickEvent event)
      {
        new AbstractHistorizationRPCCall<String>()
        {
          @Override
          protected void callService(final AsyncCallback<String> pCb)
          {
            final String level = getLevel();
            final String type = getType();
            final String user = getUser();
            final String keyword = getKeyword();

            final String locale = LocaleInfo.getCurrentLocale().getLocaleName();

            if (display.getRangeTimeRadioButton().getValue())
            {
              final Date startDate = display.getStartRangeDateBox().getValue();
              final Date endDate = display.getEndRangeDateBox().getValue();

              HistorizationEntryPoint.getServiceAsync().exportCSVFromCriterias(level, type, user, keyword,
                  startDate, endDate, null, locale, pCb);
            }
            else
            {
              HistorizationEntryPoint.getServiceAsync().exportCSVFromCriterias(level, type, user, keyword,
                  null, null, fixedDate, locale, pCb);
            }
          }

          @Override
          public void onSuccess(final String result)
          {
            // TODO Auto-generated method stub

            Window.open(GWT.getModuleBaseURL() + CSVProperties.EXPORT_SERVLET_NAME + "?"
                            + CSVProperties.FILENAME_PARAMETER + "=" + result, "Attachment", "");
          }

          @Override
          public void onFailure(final Throwable caught)
          {
            ErrorManagement.displayErrorMessage(caught);
          }

        }.retry(0);

      }

    });
  }

  public void setEndValueForAsyncProvider()
  {
    new AbstractHistorizationRPCCall<Integer>()
    {
      @Override
      protected void callService(final AsyncCallback<Integer> pCb)
      {
        String level = display.getLevelListBox().getValue(display.getLevelListBox().getSelectedIndex());
        if (level.equals(outilMessages.allLabel()))
        {
          level = null;
        }
        String type = display.getTypeListBox().getValue(display.getTypeListBox().getSelectedIndex());
        if (type.equals(outilMessages.allLabel()))
        {
          type = null;
        }
        String user = display.getUserListBox().getValue(display.getUserListBox().getSelectedIndex());
        if (user.equals(outilMessages.allLabel()))
        {
          user = null;
        }
        String keyword = display.getKeywordTextBox().getText();
        if ((keyword.trim()).equals(""))
        {
          keyword = null;
        }

        if (display.getRangeTimeRadioButton().getValue())
        {
          final Date startDate = display.getStartRangeDateBox().getValue();
          final Date endDate = display.getEndRangeDateBox().getValue();

          HistorizationEntryPoint.getServiceAsync().countEventsByCriterias(level, type, user, keyword, startDate,
                                                                           endDate, null, pCb);
        }
        else
        {
          HistorizationEntryPoint.getServiceAsync().countEventsByCriterias(level, type, user, keyword, null, null,
                                                                           fixedDate, pCb);
        }

      }

      @Override
      public void onSuccess(final Integer pResult)
      {
        end = pResult.intValue();
      }

      @Override
      public void onFailure(final Throwable caught)
      {
        ErrorManagement.displayErrorMessage(caught);
      }

    }.retry(0);
  }

  /**
   * @return
   */
  private String getLevel()
  {
    String level = display.getLevelListBox().getValue(display.getLevelListBox().getSelectedIndex());
    if (level.equals(outilMessages.allLabel()))
    {
      level = null;
    }
    return level;
  }

  /**
   * @return
   */
  private String getType()
  {
    String type = LoggingPresenter.this.display.getTypeListBox().getValue(
        LoggingPresenter.this.display.getTypeListBox().getSelectedIndex());
    if (type.equals(outilMessages.allLabel()))
    {
      type = null;
    }
    return type;
  }

  /**
   * @return
   */
  private String getUser()
  {
    String user = display.getUserListBox().getValue(display.getUserListBox().getSelectedIndex());
    if (user.equals(outilMessages.allLabel()))
    {
      user = null;
    }
    return user;
  }

  /**
   * @return
   */
  private String getKeyword()
  {
    String keyword = display.getKeywordTextBox().getText();
    if ((keyword.trim()).equals(""))
    {
      keyword = null;
    }
    return keyword;
  }

  private void initFilteringZone()
  {
    // We load the levels list
    new AbstractHistorizationRPCCall<List<String>>()
    {
      @Override
      protected void callService(final AsyncCallback<List<String>> pCb)
      {
        HistorizationEntryPoint.getServiceAsync().getListLevels(pCb);
      }

      @Override
      public void onSuccess(final List<String> pResult)
      {
        for (final String level : pResult)
        {
          display.getLevelListBox().addItem(level);
        }
      }

      @Override
      public void onFailure(final Throwable caught)
      {
        ErrorManagement.displayErrorMessage(caught);
      }

    }.retry(0);

    // We load the types list
    new AbstractHistorizationRPCCall<List<String>>()
    {
      @Override
      protected void callService(final AsyncCallback<List<String>> pCb)
      {
        HistorizationEntryPoint.getServiceAsync().getListUsers(pCb);
      }

      @Override
      public void onSuccess(final List<String> pResult)
      {
        for (final String type : pResult)
        {
          display.getUserListBox().addItem(type);
        }
      }

      @Override
      public void onFailure(final Throwable caught)
      {
        ErrorManagement.displayErrorMessage(caught);
      }

    }.retry(0);

    // We load the users list
    new AbstractHistorizationRPCCall<List<String>>()
    {
      @Override
      protected void callService(final AsyncCallback<List<String>> pCb)
      {
        HistorizationEntryPoint.getServiceAsync().getListTypes(pCb);
      }

      @Override
      public void onSuccess(final List<String> pResult)
      {
        for (final String user : pResult)
        {
          display.getTypeListBox().addItem(user);
        }
      }

      @Override
      public void onFailure(final Throwable caught)
      {
        ErrorManagement.displayErrorMessage(caught);
      }

    }.retry(0);
  }

  @Override
  public void go(final HasWidgets container)
  {
    container.clear();
    container.add(display.asWidget());
  }

  public IsWidget getDisplay()
  {
    return display.asWidget();
  }
}
