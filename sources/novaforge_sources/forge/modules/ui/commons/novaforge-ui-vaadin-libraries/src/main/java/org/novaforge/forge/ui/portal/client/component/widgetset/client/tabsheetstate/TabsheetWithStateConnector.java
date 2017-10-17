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
package org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetstate;

import com.vaadin.client.ui.tabsheet.TabsheetConnector;
import org.novaforge.forge.ui.portal.client.component.TabsheetWithState;

/**
 * This connector is the client side of {@link TabsheetWithState}. It extends default
 * {@link TabsheetConnector} in order to render all children component instead of just the new selected one.
 * 
 * @author Guillaume Lamirand
 */
@com.vaadin.shared.ui.Connect(TabsheetWithState.class)
public class TabsheetWithStateConnector extends com.vaadin.client.ui.tabsheet.TabsheetConnector
{
  /**
   * Serial version id
   */
  private static final long serialVersionUID = 2166647691837021011L;

  /**
   * {@inheritDoc}
   */
  @Override
  public VTabsheetWithState getWidget()
  {
    return (VTabsheetWithState) super.getWidget();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void renderContent()
  {

    for (int indexConnector = 0; indexConnector < getChildComponents().size(); indexConnector++)
    {

      final com.vaadin.client.ComponentConnector contentConnector = getChildComponents().get(indexConnector);
      final com.google.gwt.user.client.ui.Widget widget = contentConnector.getWidget();

      getWidget().renderContent(widget, indexConnector);

      final String key = getState().tabs.get(indexConnector).key;
      final boolean selected = key.equals(getState().selected);
      if (selected)
      {
        getWidget().showWidget(indexConnector);
      }
    }
    getWidget().iLayout();
    getWidget().updateOpenTabSize();
    getWidget().removeStyleDependentName("loading");

  }
}
