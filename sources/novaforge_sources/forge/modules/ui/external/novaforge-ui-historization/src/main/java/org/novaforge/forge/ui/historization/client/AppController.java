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
package org.novaforge.forge.ui.historization.client;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.novaforge.forge.ui.historization.client.presenter.GlobalTabPresenter;
import org.novaforge.forge.ui.historization.client.presenter.functional.LoggingPresenter;
import org.novaforge.forge.ui.historization.client.presenter.purge.LoggingPurgePresenter;
import org.novaforge.forge.ui.historization.client.view.GlobalTabViewImpl;
import org.novaforge.forge.ui.historization.client.view.functional.FunctionalLoggingViewImpl;
import org.novaforge.forge.ui.historization.client.view.purge.LoggingPurgeViewImpl;

/**
 * Application controller
 * 
 * @author qsivan
 */
public class AppController
{

  private static final String   PARAMETER_NAME_ACTION = "actionId";

  /**
   * Call to display the main view
   */
  public void go()
  {

    final String parameter = Window.Location.getParameter(PARAMETER_NAME_ACTION);
    if (parameter != null)
    {
      final ParameterAction action = ParameterAction.fromLabel(parameter);
      switch (action)
      {
        case GESTION:
          pluginManagementAction();
          break;
        case ACTIVATION:
          break;
      }
    }

    if ("".equals(History.getToken()))
    {
      pluginManagementAction();
    }
    else
    {
      History.fireCurrentHistoryState();
    }
  }

  /**
    * 
    */
  protected void pluginManagementAction()
  {
    // TODO :
    final String filtrageEtat = "Tous";

    final LoggingPresenter loggingPresenter = new LoggingPresenter(filtrageEtat, new FunctionalLoggingViewImpl());
    final LoggingPurgePresenter loggingPurgePresenter = new LoggingPurgePresenter(new LoggingPurgeViewImpl());
    final GlobalTabPresenter globalTabPresenter = new GlobalTabPresenter(new GlobalTabViewImpl(), loggingPresenter,
                                                                         loggingPurgePresenter);
    globalTabPresenter.go(RootLayoutPanel.get());
  }

}