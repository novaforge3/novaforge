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

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.vaadin.client.Focusable;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.client.ui.SubPartAware;
import com.vaadin.client.ui.VTabsheet;
import com.vaadin.client.ui.VTabsheetBase;
import com.vaadin.client.ui.VTabsheetPanel;
import com.vaadin.shared.ui.ComponentStateUtil;
import com.vaadin.shared.ui.tabsheet.TabState;
import org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.shared.TabImprovedState;
import org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.shared.TabsheetImprovedState;


/**
 * This improved {@link VTabsheet} used to display first button and a menu icon on tab
 *
 * @author Guillaume Lamirand
 */
public class VTabsheetImproved extends VTabsheetBase
    implements Focusable, SubPartAware
{

  // TODO using the CLASSNAME directly makes primaryStyleName for TabSheet of
  // very limited use - all use of style names should be refactored in the
  // future
  public static final  String                                       CLASSNAME          = TabsheetImprovedState.PRIMARY_STYLE_NAME;
  public static final  String                                       TABS_CLASSNAME     = CLASSNAME + "-tabcontainer";
  public static final  String                                       SCROLLER_CLASSNAME = CLASSNAME + "-scroller";
  public static final  String                                       PLUS_CLASSNAME     = CLASSNAME + "-plus";
  private static final FocusImpl                                    focusImpl          = FocusImpl
                                                                                             .getFocusImplForPanel();
  /**
   * Matches tab[ix] - used for extracting the index of the targeted tab
   */
  private static final RegExp                                       SUBPART_TAB_REGEXP = RegExp
                                                                                             .compile("tab\\[(\\d+)](.*)");
  /**
   * For internal use only. May be removed or replaced in the future.
   */
  // tabbar and 'scroller' container
  public final Element tabs;
  /**
   * For internal use only. May be removed or replaced in the future.
   */
  public final Element contentNode;
  /**
   * For internal use only. May be removed or replaced in the future.
   */
  protected final VTabsheetPanel           tabPanel = new VTabsheetPanel();
  final           VTabsheetImproved.TabBar tb       = new VTabsheetImproved.TabBar(this);
  // tab-scroller element
  private final Element scroller;
  // tab-scroller next button element
  private final Element scrollerNext;
  // tab-scroller prev button element
  private final Element scrollerPrev;
  private final Element deco;
  /*
   * The focus and blur manager instance.
   */
  private final VTabsheetImproved.FocusBlurManager    focusBlurManager = new VTabsheetImproved.FocusBlurManager();
  /*
   * The tabs selection handler instance.
   */
  private final VTabsheetImproved.TabSelectionHandler selectionHandler = new VTabsheetImproved.TabSelectionHandler();
  /**
   * For internal use only. May be removed or replaced in the future.
   */
  public boolean waitingForResponse;
  /**
   * The tabindex property (position in the browser's focus cycle.) Named like
   * this to avoid confusion with activeTabIndex.
   */
  int tabulatorIndex = 0;
  // Container for plus element
  private Element plus;
  // Element for plus button
  private Element plusButton;
  private boolean hasPlus;
  /**
   * The index of the first visible tab (when scrolled)
   */
  private int scrollerIndex = 0;
  private String currentStyle;

  public VTabsheetImproved()
  {
    super(CLASSNAME);

    // Tab scrolling
    getElement().getStyle().setOverflow(com.google.gwt.dom.client.Style.Overflow.HIDDEN);
    tabs = DOM.createDiv();
    DOM.setElementProperty(tabs, "className", TABS_CLASSNAME);
    com.google.gwt.aria.client.Roles.getTablistRole().set(tabs);
    com.google.gwt.aria.client.Roles.getTablistRole().setAriaLiveProperty(tabs,
                                                                          com.google.gwt.aria.client.LiveValue.OFF);
    scroller = DOM.createDiv();
    com.google.gwt.aria.client.Roles.getTablistRole().setAriaHiddenState(scroller, true);

    DOM.setElementProperty(scroller, "className", SCROLLER_CLASSNAME);

    scrollerPrev = DOM.createButton();
    scrollerPrev.setTabIndex(-1);
    DOM.setElementProperty(scrollerPrev, "className", SCROLLER_CLASSNAME + "Prev");
    com.google.gwt.aria.client.Roles.getTablistRole().setAriaHiddenState(scrollerPrev, true);
    DOM.sinkEvents(scrollerPrev, Event.ONCLICK | Event.ONMOUSEDOWN);

    scrollerNext = DOM.createButton();
    scrollerNext.setTabIndex(-1);
    DOM.setElementProperty(scrollerNext, "className", SCROLLER_CLASSNAME + "Next");
    com.google.gwt.aria.client.Roles.getTablistRole().setAriaHiddenState(scrollerNext, true);
    DOM.sinkEvents(scrollerNext, Event.ONCLICK | Event.ONMOUSEDOWN);

    DOM.appendChild(getElement(), tabs);

    // Tabs
    tabPanel.setStyleName(CLASSNAME + "-tabsheetpanel");
    contentNode = DOM.createDiv();
    com.google.gwt.aria.client.Roles.getTabpanelRole().set(contentNode);

    deco = DOM.createDiv();

    addStyleDependentName("loading"); // Indicate initial progress
    tb.setStyleName(CLASSNAME + "-tabs");
    DOM.setElementProperty(contentNode, "className", CLASSNAME + "-content");
    DOM.setElementProperty(deco, "className", CLASSNAME + "-deco");

    add(tb, tabs);
    DOM.appendChild(scroller, scrollerPrev);
    DOM.appendChild(scroller, scrollerNext);

    DOM.appendChild(getElement(), contentNode);
    add(tabPanel, contentNode);
    DOM.appendChild(getElement(), deco);

    DOM.appendChild(tabs, scroller);

    // TODO Use for Safari only. Fix annoying 1px first cell in TabBar.
    // DOM.setStyleAttribute(DOM.getFirstChild(DOM.getFirstChild(DOM
    // .getFirstChild(tb.getElement()))), "display", "none");

  }

  /**
   * Load the content of a tab of the provided index.
   *
   * @param pTabIndex
   *     of the tab to load
   *
   * @return true if the specified sheet gets loaded, otherwise false.
   */
  public boolean loadTabSheet(final int pTabIndex)
  {
    if ((activeTabIndex != pTabIndex) && canSelectTab(pTabIndex))
    {
      tb.selectTab(pTabIndex);

      activeTabIndex = pTabIndex;

      addStyleDependentName("loading");
      // Hide the current contents so a loading indicator can be shown
      // instead
      getCurrentlyDisplayedWidget().getElement().getParentElement().getStyle()
                                   .setVisibility(com.google.gwt.dom.client.Style.Visibility.HIDDEN);

      getRpcProxy().setSelected(tabKeys.get(pTabIndex));

      waitingForResponse = true;

      tb.getTab(pTabIndex).focus(); // move keyboard focus to active tab

      return true;
    }

    return false;
  }

  /**
   * @return Whether the tab could be selected or not.
   */
  private boolean canSelectTab(final int tabIndex)
  {
    final VTabsheetImproved.Tab tab = tb.getTab(tabIndex);
    if ((getApplicationConnection() == null) || disabled || waitingForResponse)
    {
      return false;
    }
    if (!tab.isEnabledOnServer() || tab.isHiddenOnServer())
    {
      return false;
    }

    // Note that we return true when tabIndex == activeTabIndex; the active
    // tab could be selected, it's just a no-op.
    return true;
  }

  /**
   * Returns the currently displayed widget in the tab panel.
   *
   * @return currently displayed content widget
   *
   * @since 7.2
   */
  public Widget getCurrentlyDisplayedWidget()
  {
    return tabPanel.getWidget(tabPanel.getVisibleWidget());
  }

  /**
   * Returns the client to server RPC proxy for the tabsheet.
   *
   * @return RPC proxy
   *
   * @since 7.2
   */
  protected org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.shared.TabsheetImprovedServerRpc getRpcProxy()
  {
    return getConnector()
               .getRpcProxy(org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.shared.TabsheetImprovedServerRpc.class);
  }

  /**
   * For internal use only.
   * Avoid using this method directly and use appropriate superclass methods
   * where applicable.
   *
   * @return ApplicationConnection
   *
   * @deprecated since 7.2 - use more specific methods instead (getRpcProxy(),
   * getConnectorForWidget(Widget) etc.)
   */
  @Deprecated
  public com.vaadin.client.ApplicationConnection getApplicationConnection()
  {
    return client;
  }

  protected TabsheetImprovedConnector getConnector()
  {
    return (TabsheetImprovedConnector) connector;
  }

  @Override
  public void setConnector(final AbstractComponentConnector connector)
  {
    super.setConnector(connector);

    focusBlurManager.connector = connector;
  }

  @Override
  public void selectTab(final int index)
  {
    tb.selectTab(index);
  }

  private com.vaadin.client.VTooltip getVTooltip()
  {
    return getApplicationConnection().getVTooltip();
  }

  public void tabSizeMightHaveChanged(final VTabsheetImproved.Tab tab)
  {
    // icon onloads may change total width of tabsheet
    if (isDynamicWidth())
    {
      updateDynamicWidth();
    }
    updateTabScroller();

  }

  /**
   * For internal use only. May be removed or replaced in the future.
   */
  public void updateDynamicWidth()
  {
    // Find width consumed by tabs
    final TableCellElement spacerCell = ((com.google.gwt.dom.client.TableElement) tb.getElement()
                                                                                                              .cast())
                                                                      .getRows().getItem(0).getCells()
                                                                      .getItem(tb.getTabCount());

    final int spacerWidth = spacerCell.getOffsetWidth();
    final DivElement div = (com.google.gwt.dom.client.DivElement) spacerCell
                                                                                                .getFirstChildElement();

    final int spacerMinWidth = spacerCell.getOffsetWidth() - div.getOffsetWidth();

    int tabsWidth = (tb.getOffsetWidth() - spacerWidth) + spacerMinWidth;

    // Find content width
    final Style style    = tabPanel.getElement().getStyle();
    final String                          overflow = style.getProperty("overflow");
    style.setProperty("overflow", "hidden");
    style.setPropertyPx("width", tabsWidth);

    final boolean hasTabs = tabPanel.getWidgetCount() > 0;

    com.google.gwt.dom.client.Style wrapperstyle = null;
    if (hasTabs)
    {
      wrapperstyle = getCurrentlyDisplayedWidget().getElement().getParentElement().getStyle();
      wrapperstyle.setPropertyPx("width", tabsWidth);
    }
    // Get content width from actual widget

    int contentWidth = 0;
    if (hasTabs)
    {
      contentWidth = getCurrentlyDisplayedWidget().getOffsetWidth();
    }
    style.setProperty("overflow", overflow);

    // Set widths to max(tabs,content)
    if (tabsWidth < contentWidth)
    {
      tabsWidth = contentWidth;
    }

    final int outerWidth = tabsWidth + getContentAreaBorderWidth();

    tabs.getStyle().setPropertyPx("width", outerWidth);
    style.setPropertyPx("width", tabsWidth);
    if (hasTabs)
    {
      wrapperstyle.setPropertyPx("width", tabsWidth);
    }

    contentNode.getStyle().setPropertyPx("width", tabsWidth);
    super.setWidth(outerWidth + "px");
    updateOpenTabSize();
  }

  /**
   * Layouts the tab-scroller elements, and applies styles.
   */
  private void updateTabScroller()
  {
    if (!isDynamicWidth())
    {
      tabs.getStyle().setWidth(100, com.google.gwt.dom.client.Style.Unit.PCT);
    }

    // Make sure scrollerIndex is valid
    if ((scrollerIndex < 0) || (scrollerIndex > tb.getTabCount()))
    {
      scrollerIndex = tb.getFirstVisibleTab();
    }
    else if ((tb.getTabCount() > 0) && tb.getTab(scrollerIndex).isHiddenOnServer())
    {
      scrollerIndex = tb.getNextVisibleTab(scrollerIndex);
    }

    final boolean scrolled = isScrolledTabs();
    final boolean clipped  = isClippedTabs();
    if ((tb.getTabCount() > 0) && tb.isVisible() && (scrolled || clipped))
    {
      scroller.getStyle().clearDisplay();
      DOM.setElementProperty(scrollerPrev, "className",
                                                        SCROLLER_CLASSNAME + (scrolled ? "Prev" : "Prev-disabled"));
      DOM.setElementProperty(scrollerNext, "className",
                                                        SCROLLER_CLASSNAME + (clipped ? "Next" : "Next-disabled"));

      // the active tab should be focusable if and only if it is visible
      final boolean isActiveTabVisible = (scrollerIndex <= activeTabIndex) && !isClipped(tb.selected);
      tb.selected.setTabulatorIndex(isActiveTabVisible ? tabulatorIndex : -1);

    }
    else
    {
      scroller.getStyle().setDisplay(com.google.gwt.dom.client.Style.Display.NONE);
    }

    if (com.vaadin.client.BrowserInfo.get().isSafari())
    {
      /*
       * another hack for webkits. tabscroller sometimes drops without
       * "shaking it" reproducable in
       * com.vaadin.tests.components.tabsheet.TabSheetIcons
       */
      final Style style = scroller.getStyle();
      style.setProperty("whiteSpace", "normal");
      com.google.gwt.core.client.Scheduler.get().scheduleDeferred(new Command()
      {

        @Override
        public void execute()
        {
          style.setProperty("whiteSpace", "");
        }
      });
    }

  }

  /**
   * For internal use only. May be removed or replaced in the future.
   */
  public int getContentAreaBorderWidth()
  {
    return com.vaadin.client.Util.measureHorizontalBorder(contentNode);
  }

  /**
   * Sets the size of the visible tab (component). As the tab is set to
   * position: absolute (to work around a firefox flickering bug) we must keep
   * this up-to-date by hand.
   * <p>
   * For internal use only. May be removed or replaced in the future.
   */
  public void updateOpenTabSize()
  {
    /*
     * The overflow=auto element must have a height specified, otherwise it
     * will be just as high as the contents and no scrollbars will appear
     */
    int height   = -1;
    int width    = -1;
    int minWidth = 0;

    if (!isDynamicHeight())
    {
      height = contentNode.getOffsetHeight();
    }
    if (!isDynamicWidth())
    {
      width = contentNode.getOffsetWidth() - getContentAreaBorderWidth();
    }
    else
    {
      /*
       * If the tabbar is wider than the content we need to use the tabbar
       * width as minimum width so scrollbars get placed correctly (at the
       * right edge).
       */
      minWidth = tb.getOffsetWidth() - getContentAreaBorderWidth();
    }
    tabPanel.fixVisibleTabSize(width, height, minWidth);

  }

  private boolean isScrolledTabs()
  {
    return scrollerIndex > tb.getFirstVisibleTab();
  }

  private boolean isClippedTabs()
  {
    return (tb.getOffsetWidth() - DOM
                                      .getElementPropertyInt((com.google.gwt.dom.client.Element) tb.getContainerElement()
                                                                                                   .getLastChild()
                                                                                                   .cast(),
                                                             "offsetWidth")) > (getOffsetWidth() - (isScrolledTabs() ?
                                                                                                        scroller
                                                                                                            .getOffsetWidth() :
                                                                                                        0));
  }

  private boolean isClipped(final VTabsheetImproved.Tab tab)
  {
    return (tab.getAbsoluteLeft() + tab.getOffsetWidth()) > ((getAbsoluteLeft() + getOffsetWidth()) - scroller
                                                                                                          .getOffsetWidth());
  }

  void sendTabClosedEvent(final int tabIndex)
  {
    getRpcProxy().closeTab(tabKeys.get(tabIndex));
  }

  void sendTabMenuEvent(final int tabIndex)
  {
    getRpcProxy().menuTab(tabKeys.get(tabIndex));
  }

  @Override
  public void onBrowserEvent(final Event event)
  {
    final Element eventTarget = DOM.eventGetTarget(event);

    if (event.getTypeInt() == Event.ONCLICK)
    {

      // Tab scrolling
      if ((eventTarget == scrollerPrev) || (eventTarget == scrollerNext))
      {
        scrollAccordingToScrollTarget(eventTarget);

        event.stopPropagation();
      }
      else if ((eventTarget == plusButton) && (hasPlus))
      {
        sendPlusClickEvent();
        event.stopPropagation();
        return;
      }

    }
    else if (event.getTypeInt() == Event.ONMOUSEDOWN)
    {

      if ((eventTarget == scrollerPrev) || (eventTarget == scrollerNext))
      {
        // In case the focus was previously on a Tab, we need to cancel
        // the upcoming blur on the Tab which will follow this mouse
        // down event.
        focusBlurManager.cancelNextBlurSchedule();

        return;
      }
    }

    super.onBrowserEvent(event);
  }

  /*
   * Scroll the tab bar according to the last scrollTarget (the scroll button
   * pressed).
   */
  private void scrollAccordingToScrollTarget(final Element scrollTarget)
  {
    if (scrollTarget == null)
    {
      return;
    }

    int newFirstIndex = -1;

    // Scroll left.
    if (isScrolledTabs() && (scrollTarget == scrollerPrev))
    {
      newFirstIndex = tb.scrollLeft(scrollerIndex);

      // Scroll right.
    }
    else if (isClippedTabs() && (scrollTarget == scrollerNext))
    {
      newFirstIndex = tb.scrollRight(scrollerIndex);
    }

    if (newFirstIndex != -1)
    {
      scrollerIndex = newFirstIndex;
      updateTabScroller();
    }

    // For this to work well, make sure the method gets called only from
    // user events.
    selectionHandler.focusTabAtIndex(scrollerIndex);
  }

  void sendPlusClickEvent()
  {
    getRpcProxy().plusClick();
  }

  /**
   * For internal use only. May be removed or replaced in the future.
   */
  public void handleStyleNames(final com.vaadin.shared.AbstractComponentState state)
  {
    // Add proper stylenames for all elements (easier to prevent unwanted
    // style inheritance)
    if (ComponentStateUtil.hasStyles(state))
    {
      final java.util.List<String> styles = state.styles;
      if ((currentStyle == null) || !currentStyle.equals(styles.toString()))
      {
        currentStyle = styles.toString();
        final String tabsBaseClass = TABS_CLASSNAME;
        String tabsClass = tabsBaseClass;
        final String contentBaseClass = CLASSNAME + "-content";
        String contentClass = contentBaseClass;
        final String decoBaseClass = CLASSNAME + "-deco";
        String decoClass = decoBaseClass;
        for (final String style : styles)
        {
          tb.addStyleDependentName(style);
          tabsClass += " " + tabsBaseClass + "-" + style;
          contentClass += " " + contentBaseClass + "-" + style;
          decoClass += " " + decoBaseClass + "-" + style;
        }
        DOM.setElementProperty(tabs, "className", tabsClass);
        DOM.setElementProperty(contentNode, "className", contentClass);
        DOM.setElementProperty(deco, "className", decoClass);
      }
    }
    else
    {
      tb.setStyleName(CLASSNAME + "-tabs");
      DOM.setElementProperty(tabs, "className", TABS_CLASSNAME);
      DOM.setElementProperty(contentNode, "className", CLASSNAME + "-content");
      DOM.setElementProperty(deco, "className", CLASSNAME + "-deco");
    }
  }

  /**
   * @param pHasPlus
   */
  public void renderPlus(final boolean pHasPlus)
  {
    hasPlus = pHasPlus;
    if ((hasPlus) && (plus == null))
    {
      plus = DOM.createDiv();
      setStyleName(plus, PLUS_CLASSNAME);
      plusButton = DOM.createButton();
      setStyleName(plusButton, PLUS_CLASSNAME + "-button");
      DOM.sinkEvents(plusButton, Event.ONCLICK);
      DOM.appendChild(plus, plusButton);
      DOM.insertChild(tabs, plus, 0);
    }
    else if (!hasPlus && (plus != null))
    {
      tabs.removeChild(plus);
      plus = null;
      plusButton = null;
    }
    if (hasPlus)
    {
      tabs.addClassName(CLASSNAME + "-tabcontainer-plus");
    }
    else
    {
      tabs.removeClassName(CLASSNAME + "-tabcontainer-plus");
    }

  }

  /**
   * Renders the widget content for a tab sheet.
   *
   * @param newWidget
   */
  public void renderContent(Widget newWidget)
  {
    assert tabPanel.getWidgetCount() <= 1;

    if (null == newWidget)
    {
      newWidget = new SimplePanel();
    }

    if (tabPanel.getWidgetCount() == 0)
    {
      tabPanel.add(newWidget);
    }
    else if (tabPanel.getWidget(0) != newWidget)
    {
      tabPanel.remove(0);
      tabPanel.add(newWidget);
    }

    assert tabPanel.getWidgetCount() <= 1;

    // There's never any other index than 0, but maintaining API for now
    tabPanel.showWidget(0);

    VTabsheetImproved.this.iLayout();
    updateOpenTabSize();
    VTabsheetImproved.this.removeStyleDependentName("loading");
  }

  /**
   * Run internal layouting.
   */
  public void iLayout()
  {
    updateTabScroller();
    updateTabCaptionSizes();
  }

  /**
   * Recalculates the sizes of tab captions, causing the tabs to be rendered
   * the correct size.
   */
  private void updateTabCaptionSizes()
  {
    for (int tabIx = 0; tabIx < tb.getTabCount(); tabIx++)
    {
      tb.getTab(tabIx).recalculateCaptionWidth();
    }
  }

  /**
   * For internal use only. May be removed or replaced in the future.
   */
  public void updateContentNodeHeight()
  {
    if (!isDynamicHeight())
    {
      int contentHeight = getOffsetHeight();
      contentHeight -= DOM.getElementPropertyInt(deco, "offsetHeight");
      contentHeight -= tb.getOffsetHeight();
      if (contentHeight < 0)
      {
        contentHeight = 0;
      }

      // Set proper values for content element
      contentNode.getStyle().setHeight(contentHeight, com.google.gwt.dom.client.Style.Unit.PX);
    }
    else
    {
      contentNode.getStyle().clearHeight();
    }
  }

  /**
   * For internal use only. May be removed or replaced in the future.
   */
  public void showAllTabs()
  {
    scrollerIndex = tb.getFirstVisibleTab();
    for (int i = 0; i < tb.getTabCount(); i++)
    {
      final VTabsheetImproved.Tab t = tb.getTab(i);
      if (!t.isHiddenOnServer())
      {
        t.setVisible(true);
      }
    }
  }

  @Override
  public java.util.Iterator<Widget> getWidgetIterator()
  {
    return tabPanel.iterator();
  }

  @Override
  protected void clearPaintables()
  {

    int i = tb.getTabCount();
    while (i > 0)
    {
      tb.removeTab(--i);
    }
    tabPanel.clear();

  }

  /**
   * This method shouldn't be used because {@link TabsheetImprovedConnector} used {@link TabImprovedState}
   * instead of {@link TabState}
   */
  @Override
  public void renderTab(final TabState tabState, final int index)
  {
    renderTab((TabImprovedState) tabState,
              index);
  }

  /**
   * This should be used instead of {@link #renderTab(TabState, int)} because
   * {@link TabsheetImprovedConnector} used {@link TabImprovedState} instead of {@link TabState}
   *
   * @param pTabImprovedState
   *     the tab state
   * @param pIndex
   *     the tab pIndex
   */
  public void renderTab(final TabImprovedState pTabImprovedState,
                        final int pIndex)
  {
    VTabsheetImproved.Tab tab = tb.getTab(pIndex);
    if (tab == null)
    {
      tab = tb.addTab();
    }

    tab.updateFromState(pTabImprovedState);
    tab.setEnabledOnServer((!disabledTabKeys.contains(tabKeys.get(pIndex))));
    tab.setHiddenOnServer(!pTabImprovedState.visible);

    if (scrolledOutOfView(pIndex))
    {
      // Should not set tabs visible if they are scrolled out of view
      tab.setVisible(false);
    }
    else
    {
      tab.setVisible(pTabImprovedState.visible);
    }

    /*
     * Force the width of the caption container so the content will not wrap
     * and tabs won't be too narrow in certain browsers
     */
    tab.recalculateCaptionWidth();
  }

  /**
   * Checks if the tab with the selected index has been scrolled out of the
   * view (on the left side).
   *
   * @param index
   *
   * @return
   */
  private boolean scrolledOutOfView(final int index)
  {
    return scrollerIndex > index;
  }

  @Override
  public int getTabCount()
  {
    return tb.getTabCount();
  }

  @Override
  public com.vaadin.client.ComponentConnector getTab(final int index)
  {
    if (tabPanel.getWidgetCount() > index)
    {
      final Widget widget = tabPanel.getWidget(index);
      return getConnectorForWidget(widget);
    }
    return null;
  }

  @Override
  public void removeTab(final int index)
  {
    tb.removeTab(index);

    // Removing content from tp is handled by the connector
  }

  @Override
  public void focus()
  {
    getActiveTab().focus();
  }

  /*
   * Gets the active tab.
   */
  private VTabsheetImproved.Tab getActiveTab()
  {
    return tb.getTab(activeTabIndex);
  }

  public void blur()
  {
    getActiveTab().blur();
  }

  /**
   * @return The key code of the keyboard shortcut that selects the previous
   * tab in a focused tabsheet.
   */
  protected int getPreviousTabKey()
  {
    return com.google.gwt.event.dom.client.KeyCodes.KEY_LEFT;
  }

  /**
   * Gets the key to activate the selected tab when navigating using
   * previous/next (left/right) keys.
   *
   * @return the key to activate the selected tab.
   *
   * @see #getNextTabKey()
   * @see #getPreviousTabKey()
   */
  protected int getSelectTabKey()
  {
    return com.google.gwt.event.dom.client.KeyCodes.KEY_SPACE;
  }

  /**
   * @return The key code of the keyboard shortcut that selects the next tab
   * in a focused tabsheet.
   */
  protected int getNextTabKey()
  {
    return com.google.gwt.event.dom.client.KeyCodes.KEY_RIGHT;
  }

  /**
   * @return The key code of the keyboard shortcut that closes the currently
   * selected tab in a focused tabsheet.
   */
  protected int getCloseTabKey()
  {
    return com.google.gwt.event.dom.client.KeyCodes.KEY_DELETE;
  }

  private void scrollIntoView(final VTabsheetImproved.Tab tab)
  {

    if (!tab.isHiddenOnServer())
    {

      // Check for visibility first as clipped tabs to the right are
      // always visible.
      // On IE8 a tab with false visibility would have the bounds of the
      // full TabBar.
      if (!tab.isVisible())
      {
        while (!tab.isVisible())
        {
          scrollerIndex = tb.scrollLeft(scrollerIndex);
        }
        updateTabScroller();

      }
      else if (isClipped(tab))
      {
        while (isClipped(tab) && (scrollerIndex != -1))
        {
          scrollerIndex = tb.scrollRight(scrollerIndex);
        }
        updateTabScroller();
      }
    }
  }

  /**
   * Makes tab bar visible.
   *
   * @since 7.2
   */
  public void showTabs()
  {
    tb.setVisible(true);
    removeStyleName(CLASSNAME + "-hidetabs");
    tb.recalculateCaptionWidths();
  }

  /**
   * Makes tab bar invisible.
   *
   * @since 7.2
   */
  public void hideTabs()
  {
    tb.setVisible(false);
    addStyleName(CLASSNAME + "-hidetabs");
  }

  @Override
  public Element getSubPartElement(final String subPart)
  {
    if ("tabpanel".equals(subPart))
    {
      return DOM.asOld(tabPanel.getElement().getFirstChildElement());
    }
    else if (SUBPART_TAB_REGEXP.test(subPart))
    {
      final com.google.gwt.regexp.shared.MatchResult result = SUBPART_TAB_REGEXP.exec(subPart);
      final int tabIx = Integer.valueOf(result.getGroup(1));
      final VTabsheetImproved.Tab tab = tb.getTab(tabIx);
      if (tab != null)
      {
        if ("/close".equals(result.getGroup(2)))
        {
          if (tab.isClosable())
          {
            return tab.tabCaption.getCloseButton();
          }
        }
        else if ("/menu".equals(result.getGroup(2)))
        {
          if (tab.hasMenu())
          {
            return tab.tabCaption.getMenuButton();
          }
        }
        else
        {
          return tab.tabCaption.getElement();
        }
      }
    }
    return null;
  }

  @Override
  public String getSubPartName(final Element subElement)
  {
    if (tabPanel.getElement().equals(subElement.getParentElement()) || tabPanel.getElement().equals(subElement))
    {
      return "tabpanel";
    }
    else
    {
      for (int i = 0; i < tb.getTabCount(); ++i)
      {
        final VTabsheetImproved.Tab tab = tb.getTab(i);
        if (tab.isClosable() && tab.tabCaption.getCloseButton().isOrHasChild(subElement))
        {
          return "tab[" + i + "]/close";
        }
        else if (tab.hasMenu() && tab.tabCaption.getMenuButton().isOrHasChild(subElement))
        {
          return "tab[" + i + "]/menu";
        }
        else if (tab.getElement().isOrHasChild(subElement))
        {
          return "tab[" + i + "]";
        }
      }
    }
    return null;
  }

  private interface VCloseHandler
  {
    void onClose(VTabsheetImproved.VCloseEvent event);
  }

  private interface VMenuHandler
  {
    void onMenu(VTabsheetImproved.VMenuEvent event);
  }

  private static class VCloseEvent
  {
    private final VTabsheetImproved.Tab tab;

    VCloseEvent(final VTabsheetImproved.Tab tab)
    {
      this.tab = tab;
    }

    public VTabsheetImproved.Tab getTab()
    {
      return tab;
    }

  }

  private static class VMenuEvent
  {
    private final VTabsheetImproved.Tab tab;

    VMenuEvent(final VTabsheetImproved.Tab tab)
    {
      this.tab = tab;
    }

    public VTabsheetImproved.Tab getTab()
    {
      return tab;
    }

  }

  /**
   * Representation of a single "tab" shown in the TabBar
   */
  public static class Tab extends SimplePanel
      implements HasFocusHandlers, HasBlurHandlers, HasMouseDownHandlers, HasKeyDownHandlers
  {
    private static final String TD_CLASSNAME                = CLASSNAME + "-tabitemcell";
    private static final String TD_FIRST_CLASSNAME          = TD_CLASSNAME + "-first";
    private static final String TD_SELECTED_CLASSNAME       = TD_CLASSNAME + "-selected";
    private static final String TD_SELECTED_FIRST_CLASSNAME = TD_SELECTED_CLASSNAME + "-first";
    private static final String TD_FOCUS_CLASSNAME          = TD_CLASSNAME + "-focus";
    private static final String TD_FOCUS_FIRST_CLASSNAME    = TD_FOCUS_CLASSNAME + "-first";
    private static final String TD_DISABLED_CLASSNAME       = TD_CLASSNAME + "-disabled";

    private static final String DIV_CLASSNAME          = CLASSNAME + "-tabitem";
    private static final String DIV_SELECTED_CLASSNAME = DIV_CLASSNAME + "-selected";
    private static final String DIV_FOCUS_CLASSNAME    = DIV_CLASSNAME + "-focus";

    private final VTabsheetImproved.TabCaption      tabCaption;
    private final Element div;
    private final VTabsheetImproved.TabBar          tabBar;
    com.google.gwt.dom.client.Element td = getElement();
    private VTabsheetImproved.VCloseHandler closeHandler;
    private VTabsheetImproved.VMenuHandler  menuHandler;
    private boolean enabledOnServer = true;
    private boolean hiddenOnServer  = false;

    private String styleName;

    private String id;

    private Tab(final VTabsheetImproved.TabBar tabBar)
    {
      super(DOM.createTD());
      this.tabBar = tabBar;
      setStyleName(td, TD_CLASSNAME);

      com.google.gwt.aria.client.Roles.getTabRole().set(getElement());
      com.google.gwt.aria.client.Roles.getTabRole().setAriaSelectedState(getElement(),
                                                                         com.google.gwt.aria.client.SelectedValue.FALSE);

      div = DOM.createDiv();
      setTabulatorIndex(-1);
      setStyleName(div, DIV_CLASSNAME);

      DOM.appendChild(td, div);

      tabCaption = new VTabsheetImproved.TabCaption(this);
      add(tabCaption);

      com.google.gwt.aria.client.Roles.getTabRole().setAriaLabelledbyProperty(getElement(),
                                                                              com.google.gwt.aria.client.Id
                                                                                  .of(tabCaption.getElement()));
    }

    public void setTabulatorIndex(final int tabIndex)
    {
      getElement().setTabIndex(tabIndex);
    }

    public boolean isHiddenOnServer()
    {
      return hiddenOnServer;
    }

    public void setHiddenOnServer(final boolean hiddenOnServer)
    {
      this.hiddenOnServer = hiddenOnServer;
      com.google.gwt.aria.client.Roles.getTabRole().setAriaHiddenState(getElement(), hiddenOnServer);
    }

    @Override
    protected Element getContainerElement()
    {
      // Attach caption element to div, not td
      return DOM.asOld(div);
    }

    public boolean isEnabledOnServer()
    {
      return enabledOnServer;
    }

    public void setEnabledOnServer(final boolean enabled)
    {
      enabledOnServer = enabled;
      com.google.gwt.aria.client.Roles.getTabRole().setAriaDisabledState(getElement(), !enabled);

      setStyleName(td, TD_DISABLED_CLASSNAME, !enabled);
      if (!enabled)
      {
        focusImpl.setTabIndex(td, -1);
      }
    }

    public void addClickHandler(final ClickHandler handler)
    {
      tabCaption.addClickHandler(handler);
    }

    public void setCloseHandler(final VTabsheetImproved.VCloseHandler closeHandler)
    {
      this.closeHandler = closeHandler;
    }

    public void setMenuHandler(final VTabsheetImproved.VMenuHandler pMenuHandler)
    {
      menuHandler = pMenuHandler;
    }

    /**
     * Toggles the style names for the Tab
     *
     * @param selected
     *     true if the Tab is selected
     * @param first
     *     true if the Tab is the first visible Tab
     */
    public void setStyleNames(final boolean selected, final boolean first)
    {
      setStyleNames(selected, first, false);
    }

    public void setStyleNames(final boolean selected, final boolean first, final boolean keyboardFocus)
    {
      setStyleName(td, TD_FIRST_CLASSNAME, first);
      setStyleName(td, TD_SELECTED_CLASSNAME, selected);
      setStyleName(td, TD_SELECTED_FIRST_CLASSNAME, selected && first);
      setStyleName(div, DIV_SELECTED_CLASSNAME, selected);
      setStyleName(td, TD_FOCUS_CLASSNAME, keyboardFocus);
      setStyleName(td, TD_FOCUS_FIRST_CLASSNAME, keyboardFocus && first);
      setStyleName(div, DIV_FOCUS_CLASSNAME, keyboardFocus);
    }

    public boolean isClosable()
    {
      return tabCaption.isClosable();
    }

    public void onClose()
    {
      closeHandler.onClose(new VTabsheetImproved.VCloseEvent(this));
    }

    public boolean hasMenu()
    {
      return tabCaption.hasMenu();
    }

    public void onMenu()
    {
      menuHandler.onMenu(new VTabsheetImproved.VMenuEvent(this));
    }

    private void updateFromState(final org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.shared.TabImprovedState tabState)
    {
      tabCaption.update(tabState);
      // Apply the styleName set for the tab
      final String newStyleName = tabState.styleName;
      // Find the nth td element
      if ((newStyleName != null) && !newStyleName.isEmpty())
      {
        if (!newStyleName.equals(styleName))
        {
          // If we have a new style name
          if ((styleName != null) && !styleName.isEmpty())
          {
            // Remove old style name if present
            td.removeClassName(TD_CLASSNAME + "-" + styleName);
          }
          // Set new style name
          td.addClassName(TD_CLASSNAME + "-" + newStyleName);
          styleName = newStyleName;
        }
      }
      else if (styleName != null)
      {
        // Remove the set stylename if no stylename is present in the
        // uidl
        td.removeClassName(TD_CLASSNAME + "-" + styleName);
        styleName = null;
      }

      final String newId = tabState.id;
      if ((newId != null) && !newId.isEmpty())
      {
        td.setId(newId);
        id = newId;
      }
      else if (id != null)
      {
        td.removeAttribute("id");
        id = null;
      }
    }

    public void recalculateCaptionWidth()
    {
      tabCaption.setWidth(tabCaption.getRequiredWidth() + "px");
    }

    @Override
    public HandlerRegistration addFocusHandler(final FocusHandler handler)
    {
      return addDomHandler(handler, FocusEvent.getType());
    }

    @Override
    public HandlerRegistration addBlurHandler(final BlurHandler handler)
    {
      return addDomHandler(handler, BlurEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseDownHandler(final MouseDownHandler handler)
    {
      return addDomHandler(handler, MouseDownEvent.getType());
    }

    @Override
    public HandlerRegistration addKeyDownHandler(final KeyDownHandler handler)
    {
      return addDomHandler(handler, KeyDownEvent.getType());
    }

    public void focus()
    {
      getTabsheet().scrollIntoView(this);
      focusImpl.focus(td);
    }

    public VTabsheetImproved getTabsheet()
    {
      return tabBar.getTabsheet();
    }

    public void blur()
    {
      focusImpl.blur(td);
    }

    public boolean hasTooltip()
    {
      return tabCaption.getTooltipInfo() != null;
    }

    public com.vaadin.client.TooltipInfo getTooltipInfo()
    {
      return tabCaption.getTooltipInfo();
    }

    public void setAssistiveDescription(final String descriptionId)
    {
      com.google.gwt.aria.client.Roles.getTablistRole().setAriaDescribedbyProperty(getElement(),
                                                                                   com.google.gwt.aria.client.Id
                                                                                       .of(descriptionId));
    }

    public void removeAssistiveDescription()
    {
      com.google.gwt.aria.client.Roles.getTablistRole().removeAriaDescribedbyProperty(getElement());
    }
  }

  public static class TabCaption extends com.vaadin.client.VCaption
  {

    private final Element button;
    private final VTabsheetImproved.Tab             tab;
    private boolean closable = false;
    private Element closeButton;
    private boolean hasMenu = false;
    private Element menuButton;

    TabCaption(final VTabsheetImproved.Tab tab)
    {
      super(tab.getTabsheet().connector.getConnection());
      this.tab = tab;

      button = DOM.createSpan();
      button.setClassName(VTabsheetImproved.CLASSNAME + "-caption-button");
      getElement().appendChild(button);

      com.vaadin.client.ui.aria.AriaHelper.ensureHasId(getElement());
    }

    private boolean update(final org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.shared.TabImprovedState tabState)
    {
      if ((tabState.description != null) || (tabState.componentError != null))
      {
        setTooltipInfo(new com.vaadin.client.TooltipInfo(tabState.description, tabState.componentError, this));
      }
      else
      {
        setTooltipInfo(null);
      }

      // TODO need to call this instead of super because the caption does
      // not have an owner
      final String captionString = tabState.caption.isEmpty() ? null : tabState.caption;
      final boolean ret = updateCaptionWithoutOwner(captionString, !tabState.enabled,
                                                    hasAttribute(tabState.description),
                                                    hasAttribute(tabState.componentError), tab.getTabsheet().connector
                                                                                               .getResourceUrl(com.vaadin.shared.ComponentConstants.ICON_RESOURCE
                                                                                                                   + tabState.key),
                                                    tabState.iconAltText);

      setClosable(tabState.closable);
      setMenu(tabState.hasMenu);

      return ret;
    }

    private boolean hasAttribute(final String string)
    {
      return (string != null) && !string.trim().isEmpty();
    }

    public void setMenu(final boolean pMenu)
    {
      hasMenu = pMenu;
      if (hasMenu && (menuButton == null))
      {
        menuButton = DOM.createSpan();
        menuButton.setInnerHTML("&#9662;");
        menuButton.setClassName(VTabsheetImproved.CLASSNAME + "-caption-menu");

        com.google.gwt.aria.client.Roles.getTabRole().setAriaHiddenState(menuButton, true);
        com.google.gwt.aria.client.Roles.getTabRole().setAriaDisabledState(menuButton, true);

        button.appendChild(menuButton);
      }
      else if (!hasMenu && (menuButton != null))
      {
        button.removeChild(menuButton);
        menuButton = null;
      }
      if (hasMenu)
      {
        addStyleDependentName("hasmenu");
      }
      else
      {
        removeStyleDependentName("hasmenu");
      }
      setButtonStyle();
    }

    private void setButtonStyle()
    {
      if ((hasMenu) && (closable))
      {
        button.addClassName(CLASSNAME + "-button-both");
        addStyleDependentName("both");
      }
      else
      {

        button.removeClassName(CLASSNAME + "-button-both");
        removeStyleDependentName("both");
      }
    }

    @Override
    public void onBrowserEvent(final Event event)
    {
      if (closable && (event.getTypeInt() == Event.ONCLICK) && (event.getEventTarget().cast()
                                                                                               == closeButton))
      {
        tab.onClose();
        event.stopPropagation();
        event.preventDefault();
      }

      if (hasMenu && (event.getTypeInt() == Event.ONCLICK) && (event.getEventTarget().cast()
                                                                                              == menuButton))
      {
        tab.onMenu();
        event.stopPropagation();
        event.preventDefault();
      }

      super.onBrowserEvent(event);

      if (event.getTypeInt() == Event.ONLOAD)
      {
        getTabsheet().tabSizeMightHaveChanged(getTab());
      }
    }

    private VTabsheetImproved getTabsheet()
    {
      return tab.getTabsheet();
    }

    public VTabsheetImproved.Tab getTab()
    {
      return tab;
    }

    @Override
    public int getRequiredWidth()
    {
      int width = super.getRequiredWidth();
      if ((closeButton != null) || (menuButton != null))
      {
        width += com.vaadin.client.Util.getRequiredWidth(button);
      }
      return width;
    }

    public boolean isClosable()
    {
      return closable;
    }

    public void setClosable(final boolean closable)
    {
      this.closable = closable;
      if (closable && (closeButton == null))
      {
        closeButton = DOM.createSpan();
        closeButton.setInnerHTML("&times;");
        closeButton.setClassName(VTabsheetImproved.CLASSNAME + "-caption-close");

        com.google.gwt.aria.client.Roles.getTabRole().setAriaHiddenState(closeButton, true);
        com.google.gwt.aria.client.Roles.getTabRole().setAriaDisabledState(closeButton, true);

        button.appendChild(closeButton);
      }
      else if (!closable && (closeButton != null))
      {
        button.removeChild(closeButton);
        closeButton = null;
      }
      if (closable)
      {
        addStyleDependentName("closable");
      }
      else
      {
        removeStyleDependentName("closable");
      }
      setButtonStyle();
    }

    public boolean hasMenu()
    {
      return hasMenu;
    }

    public Element getCloseButton()
    {
      return DOM.asOld(closeButton);
    }

    public Element getMenuButton()
    {
      return DOM.asOld(menuButton);
    }
  }

  static class TabBar extends ComplexPanel
      implements VTabsheetImproved.VCloseHandler, VTabsheetImproved.VMenuHandler
  {

    private final Element tr = DOM.createTR();

    private final Element spacerTd = DOM.createTD();
    private final VTabsheetImproved     tabsheet;
    private       VTabsheetImproved.Tab selected;

    TabBar(final VTabsheetImproved tabsheet)
    {
      this.tabsheet = tabsheet;

      final Element el = DOM.createTable();
      com.google.gwt.aria.client.Roles.getPresentationRole().set(el);

      final Element tbody = DOM.createTBody();
      DOM.appendChild(el, tbody);
      DOM.appendChild(tbody, tr);
      setStyleName(spacerTd, CLASSNAME + "-spacertd");
      DOM.appendChild(tr, spacerTd);
      DOM.appendChild(spacerTd, DOM.createDiv());

      setElement(el);
    }

    @Override
    public void onClose(final VTabsheetImproved.VCloseEvent event)
    {
      final VTabsheetImproved.Tab tab = event.getTab();
      if (!tab.isEnabledOnServer())
      {
        return;
      }
      final int tabIndex = getWidgetIndex(tab);
      getTabsheet().sendTabClosedEvent(tabIndex);
    }

    /**
     * Gets the tab sheet instance where the tab bar is attached to.
     *
     * @return the tab sheet instance where the tab bar is attached to.
     */
    public VTabsheetImproved getTabsheet()
    {
      return tabsheet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMenu(final VTabsheetImproved.VMenuEvent pEvent)
    {
      final VTabsheetImproved.Tab tab = pEvent.getTab();
      if (!tab.isEnabledOnServer())
      {
        return;
      }
      final int tabIndex = getWidgetIndex(tab);
      getTabsheet().sendTabMenuEvent(tabIndex);

    }

    protected Element getContainerElement()
    {
      return DOM.asOld(tr);
    }

    /**
     * Adds a tab to the tab bar.
     *
     * @return the added tab.
     */
    public VTabsheetImproved.Tab addTab()
    {
      final VTabsheetImproved.Tab t        = new VTabsheetImproved.Tab(this);
      final int                   tabIndex = getTabCount();

      // Logical attach
      insert(t, tr, tabIndex, true);

      if (tabIndex == 0)
      {
        // Set the "first" style
        t.setStyleNames(false, true);
      }

      getTabsheet().selectionHandler.registerTab(t);

      t.setCloseHandler(this);
      t.setMenuHandler(this);

      return t;
    }

    /**
     * Gets the number of tabs from the tab bar.
     *
     * @return the number of tabs from the tab bar.
     */
    public int getTabCount()
    {
      return getWidgetCount();
    }

    public void selectTab(final int index)
    {
      final VTabsheetImproved.Tab newSelected = getTab(index);
      final VTabsheetImproved.Tab oldSelected = selected;

      newSelected.setStyleNames(true, isFirstVisibleTab(index), true);
      newSelected.setTabulatorIndex(getTabsheet().tabulatorIndex);
      com.google.gwt.aria.client.Roles.getTabRole().setAriaSelectedState(newSelected.getElement(),
                                                                         com.google.gwt.aria.client.SelectedValue.TRUE);

      if ((oldSelected != null) && (oldSelected != newSelected))
      {
        oldSelected.setStyleNames(false, isFirstVisibleTab(getWidgetIndex(oldSelected)));
        oldSelected.setTabulatorIndex(-1);

        com.google.gwt.aria.client.Roles.getTabRole().setAriaSelectedState(oldSelected.getElement(),
                                                                           com.google.gwt.aria.client.SelectedValue.FALSE);
      }

      // Update the field holding the currently selected tab
      selected = newSelected;

      // The selected tab might need more (or less) space
      newSelected.recalculateCaptionWidth();
      getTab(tabsheet.activeTabIndex).recalculateCaptionWidth();
    }

    public VTabsheetImproved.Tab getTab(final int index)
    {
      if ((index < 0) || (index >= getTabCount()))
      {
        return null;
      }
      return (VTabsheetImproved.Tab) super.getWidget(index);
    }

    private boolean isFirstVisibleTab(final int index)
    {
      return getFirstVisibleTab() == index;
    }

    /**
     * Returns the index of the first visible tab
     *
     * @return
     */
    private int getFirstVisibleTab()
    {
      return getNextVisibleTab(-1);
    }

    /**
     * Find the next visible tab. Returns -1 if none is found.
     *
     * @param i
     *
     * @return
     */
    private int getNextVisibleTab(int i)
    {
      final int tabs = getTabCount();
      do
      {
        i++;
      }
      while ((i < tabs) && getTab(i).isHiddenOnServer());

      if (i == tabs)
      {
        return -1;
      }
      else
      {
        return i;
      }
    }

    public VTabsheetImproved.Tab navigateTab(final int fromIndex, final int toIndex)
    {
      final VTabsheetImproved.Tab newNavigated = getTab(toIndex);
      if (newNavigated == null)
      {
        throw new IllegalArgumentException("Tab at provided index toIndex was not found");
      }

      final VTabsheetImproved.Tab oldNavigated = getTab(fromIndex);
      newNavigated.setStyleNames(newNavigated.equals(selected), isFirstVisibleTab(toIndex), true);

      if ((oldNavigated != null) && (fromIndex != toIndex))
      {
        oldNavigated.setStyleNames(oldNavigated.equals(selected), isFirstVisibleTab(fromIndex), false);
      }

      return newNavigated;
    }

    public void removeTab(final int i)
    {
      final VTabsheetImproved.Tab tab = getTab(i);
      if (tab == null)
      {
        return;
      }

      remove(tab);

      /*
       * If this widget was selected we need to unmark it as the last
       * selected
       */
      if (tab == selected)
      {
        selected = null;
      }

      // FIXME: Shouldn't something be selected instead?
    }

    /**
     * Find the previous visible tab. Returns -1 if none is found.
     *
     * @param i
     *
     * @return
     */
    private int getPreviousVisibleTab(int i)
    {
      do
      {
        i--;
      }
      while ((i >= 0) && getTab(i).isHiddenOnServer());

      return i;

    }

    public int scrollLeft(final int currentFirstVisible)
    {
      final int prevVisible = getPreviousVisibleTab(currentFirstVisible);
      if (prevVisible == -1)
      {
        return -1;
      }

      final VTabsheetImproved.Tab newFirst = getTab(prevVisible);
      newFirst.setVisible(true);
      newFirst.recalculateCaptionWidth();

      return prevVisible;
    }

    public int scrollRight(final int currentFirstVisible)
    {
      final int nextVisible = getNextVisibleTab(currentFirstVisible);
      if (nextVisible == -1)
      {
        return -1;
      }
      final VTabsheetImproved.Tab currentFirst = getTab(currentFirstVisible);
      currentFirst.setVisible(false);
      currentFirst.recalculateCaptionWidth();
      return nextVisible;
    }

    private void recalculateCaptionWidths()
    {
      for (int i = 0; i < getTabCount(); ++i)
      {
        getTab(i).recalculateCaptionWidth();
      }
    }

  }

  /**
   * @deprecated as of 7.1, VTabsheet only keeps the active tab in the DOM
   * without any place holders.
   */
  @Deprecated
  public class PlaceHolder extends com.vaadin.client.ui.VLabel
  {
    public PlaceHolder()
    {
      super("");
    }
  }

  /*
   * Generate the correct focus/blur events for the main TabSheet component
   * (#14304).
   * The TabSheet must fire one focus event when the user clicks on the tab
   * bar (i.e. inner TabBar class) containing the Tabs or when the focus is
   * provided to the TabSheet by any means. Also one blur event should be
   * fired only when the user leaves the tab bar. After the user focus on the
   * tab bar and before leaving it, no matter how many times he's pressing the
   * Tabs or the scroll buttons, the TabSheet component should not fire any of
   * those blur/focus events.
   * The only focusable elements contained in the tab bar are the Tabs (see
   * inner class Tab). The reason is the accessibility support.
   * Having this in mind, the chosen solution path for our problem is to match
   * a sequence of focus/blur events on the tabs, choose only the first focus
   * and last blur events and pass only those further to the main component.
   * Any consecutive blur/focus events on 2 Tabs must be ignored.
   * Because in a blur event we don't know whether or not a focus will follow,
   * we just defer a command initiated on the blur event to wait and see if
   * any focus will appear. The command will be executed after the next focus,
   * so if no focus was triggered in the mean while it'll submit the blur
   * event to the main component, otherwise it'll do nothing, so the main
   * component will not generate the blur..
   */
  private class FocusBlurManager
  {

    // The real tab with focus on it. If the focus goes to another element
    // in the page this will be null.
    private VTabsheetImproved.Tab                           focusedTab;
    /*
     * The ultimate focus/blur event dispatcher.
     */
    private com.vaadin.client.ui.AbstractComponentConnector connector;
    /*
     * The last blur command to be executed.
     */
    private VTabsheetImproved.FocusBlurManager.BlurCommand  blurCommand;
    /*
     * Flag that the next deferred command won't get executed. This is
     * useful in case of IE where the user focus event don't fire and we're
     * using the mouse down event to track the focus. But the mouse down
     * event triggers before the blur, so we need to cancel the deferred
     * execution in advance.
     */
    private boolean nextBlurScheduleCancelled = false;

    /*
     * Gets the focused tab.
     */
    private VTabsheetImproved.Tab getFocusedTab()
    {
      return focusedTab;
    }

    /*
     * Sets the local field tracking the focused tab.
     */
    private void setFocusedTab(final VTabsheetImproved.Tab focusedTab)
    {
      this.focusedTab = focusedTab;
    }

    /**
     * Delegate method for the onFocus event occurring on Tab.
     *
     * @param newFocusTab
     *     the new focused tab.
     *
     * @see #onBlur(Tab)
     * @since 7.2.6
     */
    public void onFocus(final VTabsheetImproved.Tab newFocusTab)
    {

      if (connector.hasEventListener(com.vaadin.shared.EventId.FOCUS))
      {

        // Send the focus event only first time when we focus on any
        // tab. The focused tab will be reseted on the last blur.
        if (focusedTab == null)
        {
          getConnector().getRpcProxy(com.vaadin.shared.communication.FieldRpc.FocusAndBlurServerRpc.class).focus();
        }
      }

      cancelLastBlurSchedule();

      setFocusedTab(newFocusTab);
    }

    /**
     * Remove the last blur deferred command from execution.
     */
    public void cancelLastBlurSchedule()
    {
      if (blurCommand != null)
      {
        blurCommand.stopSchedule();
        blurCommand = null;
      }

      // We really want to make sure this flag gets reseted at any time
      // when something interact with the blur manager and ther's no blur
      // command scheduled (as we just canceled it).
      nextBlurScheduleCancelled = false;
    }

    /**
     * Delegate method for the onBlur event occurring on Tab.
     *
     * @param blurSource
     *     the source of the blur.
     *
     * @see #onFocus(Tab)
     */
    public void onBlur(final VTabsheetImproved.Tab blurSource)
    {
      if ((focusedTab != null) && (focusedTab == blurSource))
      {

        if (connector.hasEventListener(com.vaadin.shared.EventId.BLUR))
        {
          scheduleBlur(focusedTab);
        }
      }
    }

    /*
     * Schedule a new blur event for a deferred execution.
     */
    private void scheduleBlur(final VTabsheetImproved.Tab blurSource)
    {

      if (nextBlurScheduleCancelled)
      {

        // This will set the stopNextBlurCommand back to false as well.
        cancelLastBlurSchedule();

        // Reset the status.
        nextBlurScheduleCancelled = false;
        return;
      }

      cancelLastBlurSchedule();

      blurCommand = new VTabsheetImproved.FocusBlurManager.BlurCommand(blurSource);
      blurCommand.scheduleDeferred();
    }

    /**
     * Cancel the next scheduled execution. This method must be called only
     * from an event occurring before the onBlur event. It's the case of IE
     * which doesn't trigger the focus event, so we're using this approach
     * to cancel the next blur event prior it's execution, calling the
     * method from mouse down event.
     */
    public void cancelNextBlurSchedule()
    {

      // Make sure there's still no other command to be executed.
      cancelLastBlurSchedule();

      nextBlurScheduleCancelled = true;
    }

    /*
     * Execute the final blur command.
     */
    private class BlurCommand implements Command
    {

      /*
       * The blur source.
       */
      private VTabsheetImproved.Tab blurSource;

      /**
       * Create the blur command using the blur source.
       *
       * @param pBlurSource
       *     the source.
       */
      public BlurCommand(final VTabsheetImproved.Tab pBlurSource)
      {
        this.blurSource = pBlurSource;
      }

      /**
       * Stop the command from being executed.
       */
      public void stopSchedule()
      {
        blurSource = null;
      }

      /**
       * Schedule the command for a deferred execution.
       */
      public void scheduleDeferred()
      {
        com.google.gwt.core.client.Scheduler.get().scheduleDeferred(this);
      }

      @Override
      public void execute()
      {

        final VTabsheetImproved.Tab focusedTab = getFocusedTab();

        if (blurSource == null)
        {
          return;
        }

        // The focus didn't change since this blur triggered, so
        // the new focused element is not a tab.
        if (focusedTab == blurSource)
        {

          // We're certain there's no focus anymore.
          focusedTab.removeAssistiveDescription();
          setFocusedTab(null);

          getConnector().getRpcProxy(com.vaadin.shared.communication.FieldRpc.FocusAndBlurServerRpc.class).blur();
        }

        // Call this to set it to null and be consistent.
        cancelLastBlurSchedule();
      }
    }

  }

  /*
   * Handle the events for selecting the tabs.
   */
  private class TabSelectionHandler
      implements com.google.gwt.event.dom.client.FocusHandler, com.google.gwt.event.dom.client.BlurHandler,
                 com.google.gwt.event.dom.client.KeyDownHandler, com.google.gwt.event.dom.client.ClickHandler,
                 com.google.gwt.event.dom.client.MouseDownHandler
  {

    /**
     * For internal use only. May be removed or replaced in the future.
     */
    // The current visible focused index.
    private int focusedTabIndex = 0;

    /**
     * Register the tab to the selection handler.
     *
     * @param tab
     *     the tab to register.
     */
    public void registerTab(final VTabsheetImproved.Tab tab)
    {

      tab.addBlurHandler(this);
      tab.addFocusHandler(this);
      tab.addKeyDownHandler(this);
      tab.addClickHandler(this);
      tab.addMouseDownHandler(this);
    }

    @Override
    public void onBlur(final BlurEvent event)
    {

      getVTooltip().hideTooltip();

      final Object blurSource = event.getSource();

      if (blurSource instanceof VTabsheetImproved.Tab)
      {
        focusBlurManager.onBlur((VTabsheetImproved.Tab) blurSource);
      }
    }

    @Override
    public void onFocus(final FocusEvent event)
    {

      if (event.getSource() instanceof VTabsheetImproved.Tab)
      {
        final VTabsheetImproved.Tab focusSource = (VTabsheetImproved.Tab) event.getSource();
        focusBlurManager.onFocus(focusSource);

        if (focusSource.hasTooltip())
        {
          focusSource.setAssistiveDescription(getVTooltip().getUniqueId());
          getVTooltip().showAssistive(focusSource.getTooltipInfo());
        }

      }
    }

    @Override
    public void onClick(final ClickEvent event)
    {

      // IE doesn't trigger focus when click, so we need to make sure
      // the previous blur deferred command will get killed.
      focusBlurManager.cancelLastBlurSchedule();

      final VTabsheetImproved.TabCaption      caption       = (VTabsheetImproved.TabCaption) event.getSource();
      final Element targetElement = event.getNativeEvent().getEventTarget().cast();
      // the tab should not be focused if the close button was clicked
      if (targetElement == caption.getCloseButton())
      {
        return;
      }

      final int index = tb.getWidgetIndex(caption.getParent());

      tb.navigateTab(focusedTabIndex, index);

      focusedTabIndex = index;

      if (!loadTabSheet(index))
      {

        // This needs to be called at the end, as the activeTabIndex
        // is set in the loadTabSheet.
        focus();
      }
    }

    @Override
    public void onMouseDown(final MouseDownEvent event)
    {

      if (event.getSource() instanceof VTabsheetImproved.Tab)
      {

        // IE doesn't trigger focus when click, so we need to make sure
        // the
        // next blur deferred command will get killed.
        focusBlurManager.cancelNextBlurSchedule();
      }
    }

    @Override
    public void onKeyDown(final KeyDownEvent event)
    {
      if (event.getSource() instanceof VTabsheetImproved.Tab)
      {
        final int keycode = event.getNativeEvent().getKeyCode();

        if (!event.isAnyModifierKeyDown())
        {
          if (keycode == getPreviousTabKey())
          {
            selectPreviousTab();
            event.stopPropagation();

          }
          else if (keycode == getNextTabKey())
          {
            selectNextTab();
            event.stopPropagation();

          }
          else if (keycode == getCloseTabKey())
          {
            final VTabsheetImproved.Tab tab = tb.getTab(activeTabIndex);
            if (tab.isClosable())
            {
              tab.onClose();
            }

          }
          else if (keycode == getSelectTabKey())
          {
            loadTabSheet(focusedTabIndex);

            // Prevent the page from scrolling when hitting space
            // (select key) to select the current tab.
            event.preventDefault();
          }
        }
      }
    }

    /*
     * Left arrow key selection.
     */
    private void selectPreviousTab()
    {
      int newTabIndex = focusedTabIndex;
      // Find the previous visible and enabled tab if any.
      do
      {
        newTabIndex--;
      }
      while ((newTabIndex >= 0) && !canSelectTab(newTabIndex));

      if (newTabIndex >= 0)
      {
        keySelectTab(newTabIndex);
      }
    }

    /*
     * Right arrow key selection.
     */
    private void selectNextTab()
    {
      int newTabIndex = focusedTabIndex;
      // Find the next visible and enabled tab if any.
      do
      {
        newTabIndex++;
      }
      while ((newTabIndex < getTabCount()) && !canSelectTab(newTabIndex));

      if (newTabIndex < getTabCount())
      {
        keySelectTab(newTabIndex);
      }
    }

    /*
     * Select the specified tab using left/right key.
     */
    private void keySelectTab(final int newTabIndex)
    {
      final VTabsheetImproved.Tab tab = tb.getTab(newTabIndex);
      if (tab == null)
      {
        return;
      }

      // Focus the tab, otherwise the selected one will loose focus and
      // TabSheet will get blurred.
      focusTabAtIndex(newTabIndex);

      tb.navigateTab(focusedTabIndex, newTabIndex);

      focusedTabIndex = newTabIndex;
    }

    /**
     * Focus the specified tab. Make sure to call this only from user
     * events, otherwise will break things.
     *
     * @param tabIndex
     *     the index of the tab to set.
     */
    void focusTabAtIndex(final int tabIndex)
    {
      final VTabsheetImproved.Tab tabToFocus = tb.getTab(tabIndex);
      if (tabToFocus != null)
      {
        tabToFocus.focus();
      }
    }

  }
}