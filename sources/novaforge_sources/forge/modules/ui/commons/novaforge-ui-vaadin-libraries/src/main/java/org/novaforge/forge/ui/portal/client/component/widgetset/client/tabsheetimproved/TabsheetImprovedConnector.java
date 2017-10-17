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
package org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.AbstractComponentContainerConnector;
import com.vaadin.client.ui.SimpleManagedLayout;
import com.vaadin.client.ui.layout.MayScrollChildren;
import com.vaadin.client.ui.tabsheet.TabsheetBaseConnector;
import com.vaadin.client.ui.tabsheet.TabsheetConnector;
import com.vaadin.shared.ui.tabsheet.TabsheetClientRpc;
import org.novaforge.forge.ui.portal.client.component.TabsheetImproved;
import org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.shared.TabImprovedState;
import org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.shared.TabsheetImprovedState;

/**
 * This connector is the client side of {@link TabsheetImproved}. It extends default {@link TabsheetConnector}
 * in order to add menu button on tab element and a first button.
 *
 * @author Guillaume Lamirand
 * @see TabsheetConnector
 * @see TabsheetBaseConnector
 */
@com.vaadin.shared.ui.Connect(TabsheetImproved.class)
public class TabsheetImprovedConnector extends AbstractComponentContainerConnector
    implements SimpleManagedLayout, MayScrollChildren
{

  public TabsheetImprovedConnector()
  {
    registerRpc(TabsheetClientRpc.class,
                new com.vaadin.shared.ui.tabsheet.TabsheetClientRpc()
                {
                  @Override
                  public void revertToSharedStateSelection()
                  {
                    for (int i = 0; i < getState().tabs.size(); ++i)
                    {
                      final String key = getState().tabs.get(i).key;
                      final boolean selected = key.equals(getState().selected);
                      if (selected)
                      {
                        getWidget().selectTab(i);
                        break;
                      }
                    }
                    renderContent();
                  }
                });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VTabsheetImproved getWidget()
  {
    return (VTabsheetImproved) super.getWidget();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TabsheetImprovedState getState()
  {
    return (TabsheetImprovedState) super
                                                                                                                               .getState();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onStateChanged(final com.vaadin.client.communication.StateChangeEvent stateChangeEvent)
  {
    super.onStateChanged(stateChangeEvent);

    // The following code come from TabsheetBaseConnector, we have to duplicate it because of TabState used

    // Update member references
    getWidget().setEnabled(isEnabled());

    // Widgets in the TabSheet before update
    final java.util.ArrayList<Widget> oldWidgets = new java.util.ArrayList<com.google.gwt.user.client.ui.Widget>();
    for (final java.util.Iterator<Widget> iterator = getWidget()
                                                                                       .getWidgetIterator(); iterator
                                                                                                                 .hasNext(); )
    {
      oldWidgets.add(iterator.next());
    }

    // Clear previous values
    getWidget().clearTabKeys();

    int index = 0;
    for (final TabImprovedState tab : getState().tabs)
    {
      final String key = tab.key;
      final boolean selected = key.equals(getState().selected);

      getWidget().addTabKey(key, !tab.enabled && tab.visible);

      if (selected)
      {
        getWidget().setActiveTabIndex(index);
      }
      getWidget().renderTab(tab, index);
      if (selected)
      {
        getWidget().selectTab(index);
      }
      index++;
    }

    int tabCount = getWidget().getTabCount();
    while (tabCount-- > index)
    {
      getWidget().removeTab(index);
    }

    for (int i = 0; i < getWidget().getTabCount(); i++)
    {
      final com.vaadin.client.ComponentConnector p = getWidget().getTab(i);
      // null for PlaceHolder widgets
      if (p != null)
      {
        oldWidgets.remove(p.getWidget());
      }
    }

    // Detach any old tab widget, should be max 1
    for (final com.google.gwt.user.client.ui.Widget oldWidget : oldWidgets)
    {
      if (oldWidget.isAttached())
      {
        oldWidget.removeFromParent();
      }
    }

    // The following code come from TabsheetConnector, we have to duplicate it because of TabsheetState used
    getWidget().handleStyleNames(getState());

    if (getState().tabsVisible)
    {
      getWidget().showTabs();
    }
    else
    {
      getWidget().hideTabs();
    }
    getWidget().renderPlus(getState().plusButtonVisible);

    // tabs; push or not
    if (!isUndefinedWidth())
    {
      getWidget().tabs.getStyle().setOverflow(com.google.gwt.dom.client.Style.Overflow.HIDDEN);
    }
    else
    {
      getWidget().showAllTabs();
      getWidget().tabs.getStyle().clearWidth();
      getWidget().tabs.getStyle().setOverflow(com.google.gwt.dom.client.Style.Overflow.VISIBLE);
      getWidget().updateDynamicWidth();
    }

    if (!isUndefinedHeight())
    {
      // Must update height after the styles have been set
      getWidget().updateContentNodeHeight();
      getWidget().updateOpenTabSize();
    }

    getWidget().iLayout();

    getWidget().waitingForResponse = false;
  }

  @Override
  public com.vaadin.client.TooltipInfo getTooltipInfo(final com.google.gwt.dom.client.Element element)
  {

    com.vaadin.client.TooltipInfo info = null;

    // Find a tooltip for the tab, if the element is a tab
    if (element != getWidget().getElement())
    {
      final Object node = com.vaadin.client.Util.findWidget(element, VTabsheetImproved.TabCaption.class);

      if (node != null)
      {
        final VTabsheetImproved.TabCaption caption = (VTabsheetImproved.TabCaption) node;
        info = caption.getTooltipInfo();
      }
    }

    // If not tab tooltip was found, use the default
    if (info == null)
    {
      info = super.getTooltipInfo(element);
    }

    return info;
  }

  @Override
  public boolean hasTooltip()
  {
    /*
     * Tab tooltips are not processed until updateFromUIDL, so we can't be
     * sure that there are no tooltips during onStateChange when this method
     * is used.
     */
    return true;
  }

  /**
   * (Re-)render the content of the active tab.
   */
  protected void renderContent()
  {
    com.vaadin.client.ComponentConnector contentConnector = null;
    if (!getChildComponents().isEmpty())
    {
      contentConnector = getChildComponents().get(0);
    }

    if (null != contentConnector)
    {
      getWidget().renderContent(contentConnector.getWidget());
    }
    else
    {
      getWidget().renderContent(null);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void init()
  {
    super.init();
    getWidget().setClient(getConnection());
    getWidget().setConnector(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected <T extends com.vaadin.shared.communication.ServerRpc> T getRpcProxy(final Class<T> pRpcInterface)
  {
    return super.getRpcProxy(pRpcInterface);
  }

  @Override
  public void updateCaption(final com.vaadin.client.ComponentConnector component)
  {
    /* Tabsheet does not render its children's captions */
  }

  @Override
  public void layout()
  {
    final VTabsheetImproved tabsheet = getWidget();

    tabsheet.updateContentNodeHeight();

    if (isUndefinedWidth())
    {
      tabsheet.contentNode.getStyle().setProperty("width", "");
    }
    else
    {
      int contentWidth = tabsheet.getOffsetWidth() - tabsheet.getContentAreaBorderWidth();
      if (contentWidth < 0)
      {
        contentWidth = 0;
      }
      tabsheet.contentNode.getStyle().setProperty("width", contentWidth + "px");
    }

    tabsheet.updateOpenTabSize();
    if (isUndefinedWidth())
    {
      tabsheet.updateDynamicWidth();
    }

    tabsheet.iLayout();

  }

  @Override
  public void onConnectorHierarchyChange(final com.vaadin.client.ConnectorHierarchyChangeEvent connector)
  {
    renderContent();
  }

}
