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
package org.novaforge.forge.ui.historization.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import org.novaforge.forge.ui.historization.client.presenter.functional.LoggingPresenter;
import org.novaforge.forge.ui.historization.client.presenter.purge.LoggingPurgePresenter;
import org.novaforge.forge.ui.historization.client.properties.LoggingMessage;
import org.novaforge.forge.ui.historization.client.view.GlobalTabView;
import org.novaforge.forge.ui.historization.client.view.functional.FunctionalLoggingView;

public class GlobalTabPresenter implements Presenter
{

  public static final Integer         TAB_VIEW_FUNCTIONAL_LOGGING = 0;
  public static final Integer         TAB_REQUIREMENTS_CODED      = 1;
  private final LoggingMessage        messages                    = (LoggingMessage) GWT
                                                                      .create(LoggingMessage.class);
  private final GlobalTabView         display;

  private final LoggingPresenter      loggingPresenter;
  private final LoggingPurgePresenter loggingPurgePresenter;

  public GlobalTabPresenter(final GlobalTabView display, final LoggingPresenter pLoggingPresenter,
      final LoggingPurgePresenter pLoggingPurgePresenter)
  {
    super();
    this.display = display;
    loggingPresenter = pLoggingPresenter;
    loggingPurgePresenter = pLoggingPurgePresenter;
    bind();
  }

  public void bind()
  {
    display.getTabPanel().addSelectionHandler(new SelectionHandler<Integer>()
    {

      @Override
      public void onSelection(final SelectionEvent<Integer> pEvent)
      {
        switch (pEvent.getSelectedItem())
        {
          case 0:
            ((FunctionalLoggingView) loggingPresenter.getDisplay()).getPanelFunctionalLogsList().setVisible(
                false);
            ((FunctionalLoggingView) loggingPresenter.getDisplay()).getRealTimeRadioButton().setValue(true);
            ((FunctionalLoggingView) loggingPresenter.getDisplay()).getStartRangeDateBox().setEnabled(false);
            ((FunctionalLoggingView) loggingPresenter.getDisplay()).getEndRangeDateBox().setEnabled(false);
            break;
          case 1:
            break;
        }

      }
    });
  }

  @Override
  public void go(final HasWidgets container)
  {
    container.clear();
    container.add(display.asWidget());
    display.getTabPanel().insert(loggingPresenter.getDisplay(), messages.headerFunctionalLoggingTab(), 0);
    display.getTabPanel().insert(loggingPurgePresenter.getDisplay(), messages.headerLoggingPurgeTab(), 1);
  }
}
