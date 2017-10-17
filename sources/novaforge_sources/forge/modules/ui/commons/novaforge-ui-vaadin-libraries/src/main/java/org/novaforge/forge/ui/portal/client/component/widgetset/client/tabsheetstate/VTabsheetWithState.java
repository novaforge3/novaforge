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

import com.vaadin.client.ui.VTabsheet;

/**
 * This extends the default {@link VTabsheet} to manage more than one child.
 * 
 * @author Guillaume Lamirand
 */
public class VTabsheetWithState extends com.vaadin.client.ui.VTabsheet
{

  /**
   * {@inheritDoc}
   */
  @Override
  public void renderContent(final com.google.gwt.user.client.ui.Widget pNewWidget)
  {
    int index = tabPanel.getWidgetIndex(pNewWidget);
    if (index == -1)
    {
      index = tabPanel.getWidgetCount();
    }
    renderContent(pNewWidget, index);
  }

  /**
   * Renders the widget content for a tab sheet.
   * 
   * @param pWidget
   *          the widget to render
   * @param pNewIndex
   *          the new index of the widget
   */
  public void renderContent(final com.google.gwt.user.client.ui.Widget pWidget, final int pNewIndex)
  {
    com.google.gwt.user.client.ui.Widget widget = pWidget;
    if (null == pWidget)
    {
      widget = new com.google.gwt.user.client.ui.SimplePanel();
    }
    final int oldIndex = tabPanel.getWidgetIndex(widget);

    /*
     * The tab has previously been rendered in another position so
     * we must move the cached content to correct position
     * Or the tab doesn't exist into tabPanel so we insert it to the correct position
     */
    if (((oldIndex != -1) && (oldIndex != pNewIndex)) || (tabPanel.getWidgetIndex(widget) < 0))
    {
      tabPanel.insert(pWidget, pNewIndex);
    }

  }

  /**
   * Show the widget content on the index given
   * 
   * @param widgetIndex
   *          the index of the widget to show
   */
  public void showWidget(final int widgetIndex)
  {
    tabPanel.showWidget(widgetIndex);
  }
}
