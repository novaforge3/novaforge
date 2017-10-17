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
package org.novaforge.forge.ui.historization.client.presenter.purge;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.historization.client.HistorizationEntryPoint;
import org.novaforge.forge.ui.historization.client.helper.AbstractHistorizationRPCCall;
import org.novaforge.forge.ui.historization.client.presenter.Presenter;
import org.novaforge.forge.ui.historization.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.historization.client.properties.LoggingMessage;
import org.novaforge.forge.ui.historization.client.view.purge.LoggingPurgeView;

import java.util.Date;

/**
 * @author qsivan
 */
public class LoggingPurgePresenter implements Presenter
{

  private final LoggingMessage   loggingMessages = (LoggingMessage) GWT.create(LoggingMessage.class);

  private final LoggingPurgeView display;

  public LoggingPurgePresenter(final LoggingPurgeView display)
  {
    super();
    this.display = display;

    bind();
  }

  public void bind()
  {
    display.getButtonValidatePurge().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(final ClickEvent event)
      {
        display.getValidatePurgeDialogBox().getDialogPanel().center();
        display.getValidatePurgeDialogBox().getDialogPanel().show();
      }
    });
    display.getValidatePurgeDialogBox().getValidate().addClickHandler(new ClickHandler()
    {

      @Override
      public void onClick(final ClickEvent event)
      {
        new AbstractHistorizationRPCCall<Void>()
        {
          @Override
          protected void callService(final AsyncCallback<Void> pCb)
          {
            final Date limitDate = display.getLimitDateBox().getValue();
            HistorizationEntryPoint.getServiceAsync().purgeFunctionnalLogs(limitDate, pCb);
          }

          @Override
          public void onFailure(final Throwable caught)
          {
            ErrorManagement.displayErrorMessage(caught);
          }

          @Override
          public void onSuccess(final Void result)
          {
            // TODO : afficher confirmation ?
            display.getValidatePurgeDialogBox().getDialogPanel().hide();
          }

        }.retry(0);
      }
    });
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
