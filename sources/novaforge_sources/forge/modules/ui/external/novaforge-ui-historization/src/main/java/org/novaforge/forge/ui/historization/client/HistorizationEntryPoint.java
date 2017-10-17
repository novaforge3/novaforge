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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.historization.client.service.HistorizationRemote;
import org.novaforge.forge.ui.historization.client.service.HistorizationRemoteAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author qsivan
 */
public class HistorizationEntryPoint implements EntryPoint
{

  /**
   * Create a remote service proxy to talk to the server-side Greeting service.
   */
  private static final HistorizationRemoteAsync pluginmanagementservice = GWT
                                                                            .create(HistorizationRemote.class);
  private static SimpleEventBus eventBus;
  private static String         uuid;
  private        AppController  appViewer;

  /**
   * @return the RootPanel
   */
  public static RootLayoutPanel getRootLayoutPanel()
  {
    return RootLayoutPanel.get();
  }

  public static SimpleEventBus getEventBus()
  {
    return eventBus;
  }

  public static HistorizationRemoteAsync getServiceAsync()
  {
    return pluginmanagementservice;
  }

  /**
   * @return the uuid
   */
  public static String getUuid()
  {
    return uuid;
  }

  /**
   * This is the entry point method.
   */
  @Override
  public void onModuleLoad()
  {
    // Get application uuid
    final String parameter = Window.Location.getParameter(Common.UUID_PARAMETER);
    if (parameter != null)
    {
      uuid = parameter;
    }
    eventBus = new SimpleEventBus();
    appViewer = new AppController();
    appViewer.go();
  }

}
