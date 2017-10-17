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

package org.novaforge.forge.ui.portal.client.component;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.KeyMapper;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.shared.TabImprovedState;
import org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.shared.TabsheetImprovedServerRpc;
import org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.shared.TabsheetImprovedState;

/**
 * This custom tabsheet allows to enable a menu button on a {@link Tab} component. Also it allows to add a
 * first button displayed before the first tab element.
 */
public class TabsheetImproved extends com.vaadin.ui.AbstractComponentContainer
    implements com.vaadin.ui.Component.Focusable, com.vaadin.event.FieldEvents.FocusNotifier,
               com.vaadin.event.FieldEvents.BlurNotifier, com.vaadin.ui.SelectiveRenderer
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = -51942874887786311L;
  private static final java.lang.reflect.Method SELECTED_TAB_CHANGE_METHOD;

  static
  {
    try
    {
      SELECTED_TAB_CHANGE_METHOD = org.novaforge.forge.ui.portal.client.component.TabsheetImproved.SelectedTabChangeListener.class
                                       .getDeclaredMethod("selectedTabChange", SelectedTabChangeEvent.class);
    }
    catch (final NoSuchMethodException e)
    {
      // This should never happen
      throw new RuntimeException("Internal error finding methods in TabSheet");
    }
  }

  /**
   * List of component tabs (tab contents). In addition to being on this list,
   * there is a {@link Tab} object in tabs for each tab with meta-data about
   * the tab.
   */
  private final java.util.List<com.vaadin.ui.Component>                                                                     components   = new java.util.ArrayList<com.vaadin.ui.Component>();
  /**
   * Map containing information related to the tabs (caption, icon etc).
   */
  private final java.util.Map<Component, TabsheetImproved.Tab> tabs         = new java.util.HashMap<com.vaadin.ui.Component, org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab>();
  /**
   * Mapper between server-side component instances (tab contents) and keys
   * given to the client that identify tabs.
   */
  private final KeyMapper<Component>                           keyMapper    = new KeyMapper<com.vaadin.ui.Component>();
  private final TabsheetServerRpcImpl                          rpc          = new TabsheetImproved.TabsheetServerRpcImpl();
  private final FieldEvents.FocusAndBlurServerRpcImpl          focusBlurRpc = new FieldEvents.FocusAndBlurServerRpcImpl(this)
  {

    @Override
    protected void fireEvent(final com.vaadin.ui.Component.Event event)
    {
      TabsheetImproved.this.fireEvent(event);
    }
  };
  /**
   * Selected tab content component.
   */
  private       Component                                      selected     = null;
  /**
   * Handler to be called when a tab is closed.
   */
  private TabsheetImproved.CloseHandler     closeHandler;
  /**
   * Handler to be called when the user click on menu button
   */
  private TabsheetImproved.MenuHandler      menuHandler;
  /**
   * Handler to be called when the user click on first button
   */
  private TabsheetImproved.PlusClickHandler plusClickHandler;

  /**
   * Constructs a new TabsheetWithState containing the given components.
   *
   * @param components
   *     The components to add to the tab sheet. Each component will be
   *     added to a separate tab.
   */
  public TabsheetImproved(final com.vaadin.ui.Component... components)
  {
    this();
    addComponents(components);
  }

  /**
   * Constructs a new {@link TabsheetImproved}. A TabsheetWithState is immediate by default, and the
   * default close handler removes the tab being closed.
   */
  public TabsheetImproved()
  {
    super();

    registerRpc(rpc);
    registerRpc(focusBlurRpc);

    // expand horizontally by default
    setWidth(100, UNITS_PERCENTAGE);
    setImmediate(true);
    setCloseHandler(new org.novaforge.forge.ui.portal.client.component.TabsheetImproved.CloseHandler()
    {

      @Override
      public void onTabClose(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved tabsheet,
                             final com.vaadin.ui.Component c)
      {
        tabsheet.removeComponent(c);
      }
    });
  }

  /**
   * Provide a custom {@link CloseHandler} for this TabsheetImproved if you wish to
   * perform some additional tasks when a user clicks on a tabs close button,
   * e.g. show a confirmation dialogue before removing the tab.
   * To remove the tab, if you provide your own close handler, you must call
   * {@link #removeComponent(Component)} yourself.
   * The default CloseHandler for TabsheetImproved will only remove the tab.
   *
   * @param handler
   */
  public void setCloseHandler(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.CloseHandler handler)
  {
    closeHandler = handler;
  }

  /**
   * Sets the selected tab in the TabsheetImproved. Ensures that the selected tab is
   * repainted if needed.
   *
   * @param component
   *     The new selection or null for no selection
   */
  private void setSelected(final com.vaadin.ui.Component component)
  {
    org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab tab = tabs.get(selected);

    selected = component;
    // Repaint of the selected component is needed as only the selected
    // component is communicated to the client. Otherwise this will be a
    // "cached" update even though the client knows nothing about the
    // connector
    if (selected != null)
    {
      tab = getTab(component);

      if ((tab != null) && (tab.getDefaultFocusComponent() != null))
      {
        tab.getDefaultFocusComponent().focus();
      }

      getState().selected = keyMapper.key(selected);

      selected.markAsDirtyRecursive();
    }
    else
    {
      getState().selected = null;
    }
  }

  /**
   * Checks if the current selection is valid, and updates the selection if
   * the previously selected component is not visible and enabled. The first
   * visible and enabled tab is selected if the current selection is empty or
   * invalid.
   * This method does not fire tab change events, but the caller should do so
   * if appropriate.
   *
   * @return true if selection was changed, false otherwise
   */
  private boolean updateSelection()
  {
    final com.vaadin.ui.Component originalSelection = selected;
    for (final java.util.Iterator<com.vaadin.ui.Component> i = getComponentIterator(); i.hasNext(); )
    {
      final com.vaadin.ui.Component component = i.next();

      final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab tab = tabs.get(component);

      /*
       * If we have no selection, if the current selection is invisible or
       * if the current selection is disabled (but the whole component is
       * not) we select this tab instead
       */
      org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab selectedTabInfo = null;
      if (selected != null)
      {
        selectedTabInfo = tabs.get(selected);
      }
      if ((selected == null) || (selectedTabInfo == null) || !selectedTabInfo.isVisible() || !selectedTabInfo
                                                                                                  .isEnabled())
      {

        // The current selection is not valid so we need to change
        // it
        if (tab.isEnabled() && tab.isVisible())
        {
          setSelected(component);
          break;
        }
        else
        {
          /*
           * The current selection is not valid but this tab cannot be
           * selected either.
           */
          setSelected(null);
        }
      }
    }
    return originalSelection != selected;
  }

  /**
   * Sends an event that the currently selected tab has changed.
   */
  protected void fireSelectedTabChange()
  {
    fireEvent(new org.novaforge.forge.ui.portal.client.component.TabsheetImproved.SelectedTabChangeEvent(this));
  }

  /**
   * Returns the {@link Tab} (metadata) for a component. The {@link Tab} object can be used for setting
   * caption,icon, etc for the tab.
   *
   * @param c
   *     the component
   *
   * @return The tab instance associated with the given component, or null if
   * the tabsheet does not contain the component.
   */
  public org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab getTab(final com.vaadin.ui.Component c)
  {
    return tabs.get(c);
  }

  /**
   * Copies properties from one Tab to another.
   *
   * @param from
   *     The tab whose data to copy.
   * @param to
   *     The tab to which copy the data.
   */
  private static void copyTabMetadata(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab from,
                                      final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab to)
  {
    to.setCaption(from.getCaption());
    to.setIcon(from.getIcon(), from.getIconAlternateText());
    to.setDescription(from.getDescription());
    to.setVisible(from.isVisible());
    to.setEnabled(from.isEnabled());
    to.setClosable(from.isClosable());
    to.setStyleName(from.getStyleName());
    to.setComponentError(from.getComponentError());
  }

  /**
   * Gets the component container iterator for going through all the
   * components (tab contents).
   *
   * @return the unmodifiable Iterator of the tab content components
   */

  @Override
  public java.util.Iterator<com.vaadin.ui.Component> iterator()
  {
    return java.util.Collections.unmodifiableList(components).iterator();
  }

  /**
   * Removes a {@link Tab} and the component associated with it, as previously
   * added with {@link #addTab(Component)}, {@link #addTab(Component, String, Resource)} or
   * {@link #addComponent(Component)}.
   * <p>
   * If the tab was selected, the first eligible (visible and enabled) remaining tab is selected.
   * </p>
   *
   * @param tab
   *     the Tab to remove
   *
   * @see #addTab(Component)
   * @see #addTab(Component, String, Resource)
   * @see #addComponent(Component)
   * @see #removeComponent(Component)
   */
  public void removeTab(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab tab)
  {
    removeComponent(tab.getComponent());
  }

  /**
   * Adds a new tab into TabsheetWithState.
   * The first tab added to a tab sheet is automatically selected and a tab
   * selection event is fired.
   * If the component is already present in the tab sheet, changes its caption
   * and returns the corresponding (old) tab, preserving other tab metadata.
   *
   * @param c
   *     the component to be added onto tab - should not be null.
   * @param caption
   *     the caption to be set for the component and used rendered in
   *     tab bar
   *
   * @return the created {@link Tab}
   */
  public org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab addTab(final com.vaadin.ui.Component c,
                                                                                    final String caption)
  {
    return addTab(c, caption, null);
  }

  /**
   * Adds a new tab into TabsheetWithState.
   * The first tab added to a tab sheet is automatically selected and a tab
   * selection event is fired.
   * If the component is already present in the tab sheet, changes its caption
   * and icon and returns the corresponding (old) tab, preserving other tab
   * metadata.
   *
   * @param c
   *     the component to be added onto tab - should not be null.
   * @param caption
   *     the caption to be set for the component and used rendered in
   *     tab bar
   * @param icon
   *     the icon to be set for the component and used rendered in tab
   *     bar
   *
   * @return the created {@link Tab}
   */
  public org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab addTab(final com.vaadin.ui.Component c,
                                                                                    final String caption,
                                                                                    final com.vaadin.server.Resource icon)
  {
    return addTab(c, caption, icon, components.size());
  }

  /**
   * Adds a new tab into TabsheetWithState.
   * The first tab added to a tab sheet is automatically selected and a tab
   * selection event is fired.
   * If the component is already present in the tab sheet, changes its caption
   * and icon and returns the corresponding (old) tab, preserving other tab
   * metadata like the position.
   *
   * @param tabComponent
   *     the component to be added onto tab - should not be null.
   * @param caption
   *     the caption to be set for the component and used rendered in
   *     tab bar
   * @param icon
   *     the icon to be set for the component and used rendered in tab
   *     bar
   * @param position
   *     the position at where the the tab should be added.
   *
   * @return the created {@link Tab}
   */
  public org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab addTab(final com.vaadin.ui.Component tabComponent,
                                                                                    final String caption,
                                                                                    final com.vaadin.server.Resource icon,
                                                                                    final int position)
  {
    if (tabComponent == null)
    {
      return null;
    }
    else if (tabs.containsKey(tabComponent))
    {
      final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab tab = tabs.get(tabComponent);
      tab.setCaption(caption);
      tab.setIcon(icon);
      return tab;
    }
    else
    {
      components.add(position, tabComponent);

      final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.TabSheetTabImpl tab = new org.novaforge.forge.ui.portal.client.component.TabsheetImproved.TabSheetTabImpl(keyMapper
                                                                                                                                                                                          .key(tabComponent),
                                                                                                                                                                                      caption,
                                                                                                                                                                                      icon);

      getState().tabs.add(position, tab.getTabState());
      tabs.put(tabComponent, tab);

      if (selected == null)
      {
        setSelected(tabComponent);
        fireSelectedTabChange();
      }
      super.addComponent(tabComponent);
      markAsDirty();
      return tab;
    }
  }

  /**
   * Adds a new tab into TabsheetWithState. Component caption and icon are copied to
   * the tab metadata at creation time.
   * If the tab sheet already contains the component, its tab is returned.
   *
   * @param c
   *     the component to be added onto tab - should not be null.
   *
   * @return the created {@link Tab}
   */
  public org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab addTab(final com.vaadin.ui.Component c)
  {
    return addTab(c, components.size());
  }

  /**
   * Adds a new tab into TabsheetWithState. Component caption and icon are copied to
   * the tab metadata at creation time.
   * If the tab sheet already contains the component, its tab is returned.
   *
   * @param component
   *     the component to be added onto tab - should not be null.
   * @param position
   *     The position where the tab should be added
   *
   * @return the created {@link Tab}
   */
  public org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab addTab(final com.vaadin.ui.Component component,
                                                                                    final int position)
  {
    org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab result = tabs.get(component);

    if (result == null)
    {
      result = addTab(component, component.getCaption(), component.getIcon(), position);
    }

    return result;
  }

  /**
   * Moves all components from another container to this container. The
   * components are removed from the other container.
   * If the source container is a {@link TabSheet}, component captions and
   * icons are copied from it.
   *
   * @param source
   *     the container components are removed from.
   */

  @Override
  public void moveComponentsFrom(final com.vaadin.ui.ComponentContainer source)
  {
    for (final java.util.Iterator<com.vaadin.ui.Component> i = source.getComponentIterator(); i.hasNext(); )
    {
      final com.vaadin.ui.Component c = i.next();
      String caption = null;
      com.vaadin.server.Resource icon = null;
      String iconAltText = "";
      if (org.novaforge.forge.ui.portal.client.component.TabsheetImproved.class.isAssignableFrom(source.getClass()))
      {
        final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab tab = ((org.novaforge.forge.ui.portal.client.component.TabsheetImproved) source)
                                                                                            .getTab(c);
        caption = tab.getCaption();
        icon = tab.getIcon();
        iconAltText = tab.getIconAlternateText();
      }
      source.removeComponent(c);
      final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab tab = addTab(c, caption, icon);
      tab.setIconAlternateText(iconAltText);
    }
  }

  /**
   * Adds a new tab into TabsheetWithState. Component caption and icon are copied to
   * the tab metadata at creation time.
   *
   * @param c
   *     the component to be added.
   *
   * @see #addTab(Component)
   */

  @Override
  public void addComponent(final com.vaadin.ui.Component c)
  {
    addTab(c);
  }

  /**
   * Removes a component and its corresponding tab.
   * If the tab was selected, the first eligible (visible and enabled)
   * remaining tab is selected.
   *
   * @param component
   *     the component to be removed.
   */

  @Override
  public void removeComponent(final com.vaadin.ui.Component component)
  {
    if ((component != null) && components.contains(component))
    {
      super.removeComponent(component);
      keyMapper.remove(component);
      components.remove(component);

      final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab removedTab = tabs.remove(component);

      getState().tabs
          .remove(((org.novaforge.forge.ui.portal.client.component.TabsheetImproved.TabSheetTabImpl) removedTab)
                      .getTabState());

      if (component.equals(selected))
      {
        if (components.isEmpty())
        {
          setSelected(null);
        }
        else
        {
          // select the first enabled and visible tab, if any
          updateSelection();
          fireSelectedTabChange();
        }
      }
      markAsDirty();
    }
  }

  /**
   * Are the tab selection parts ("tabs") hidden.
   *
   * @return true if the tabs are hidden in the UI
   */
  public boolean areTabsHidden()
  {
    return !getState(false).tabsVisible;
  }

  /**
   * Hides or shows the tab selection parts ("tabs").
   *
   * @param tabsHidden
   *     true if the tabs should be hidden
   */
  public void hideTabs(final boolean tabsHidden)
  {
    getState().tabsVisible = !tabsHidden;
  }

  /**
   * Replaces a component (tab content) with another. This can be used to
   * change tab contents or to rearrange tabs. The tab position and some
   * metadata are preserved when moving components within the same {@link TabSheet}.
   * If the oldComponent is not present in the tab sheet, the new one is added
   * at the end.
   * If the oldComponent is already in the tab sheet but the newComponent
   * isn't, the old tab is replaced with a new one, and the caption and icon
   * of the old one are copied to the new tab.
   * If both old and new components are present, their positions are swapped. {@inheritDoc}
   */

  @Override
  public void replaceComponent(final com.vaadin.ui.Component oldComponent, final com.vaadin.ui.Component newComponent)
  {
    boolean selectAfterInserting = false;

    if (selected == oldComponent)
    {
      selectAfterInserting = true;
    }

    org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab       newTab = tabs.get(newComponent);
    final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab oldTab = tabs.get(oldComponent);

    // Gets the locations
    int oldLocation = -1;
    int newLocation = -1;
    int location    = 0;

    for (final com.vaadin.ui.Component component : components)
    {
      if (component == oldComponent)
      {
        oldLocation = location;
      }
      if (component == newComponent)
      {
        newLocation = location;
      }

      location++;
    }

    if (oldLocation == -1)
    {
      addComponent(newComponent);
    }
    else if (newLocation == -1)
    {
      removeComponent(oldComponent);
      newTab = addTab(newComponent, oldLocation);

      if (selectAfterInserting)
      {
        setSelected(newComponent);
      }

      // Copy all relevant metadata to the new tab (#8793)
      // TODO Should reuse the old tab instance instead?
      copyTabMetadata(oldTab, newTab);
    }
    else
    {
      components.set(oldLocation, newComponent);
      components.set(newLocation, oldComponent);

      if (selectAfterInserting)
      {
        setSelected(newComponent);
      }

      // Tab associations are not changed, but metadata is swapped between
      // the instances
      // TODO Should reassociate the instances instead?
      final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab tmp = new org.novaforge.forge.ui.portal.client.component.TabsheetImproved.TabSheetTabImpl(null,
                                                                                                                                                                          null,
                                                                                                                                                                          null);
      copyTabMetadata(newTab, tmp);
      copyTabMetadata(oldTab, newTab);
      copyTabMetadata(tmp, oldTab);

      markAsDirty();
    }
  }

  /**
   * Gets the number of contained components (tabs). Consistent with the
   * iterator returned by {@link #getComponentIterator()}.
   *
   * @return the number of contained components
   */

  @Override
  public int getComponentCount()
  {
    return components.size();
  }

  /**
   * @deprecated As of 7.0, replaced by {@link #addSelectedTabChangeListener(SelectedTabChangeListener)}
   */
  @Deprecated
  public void addListener(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.SelectedTabChangeListener listener)
  {
    addSelectedTabChangeListener(listener);
  }

  /* Click event */

  /**
   * Adds a tab selection listener
   *
   * @param listener
   *     the Listener to be added.
   */
  public void addSelectedTabChangeListener(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.SelectedTabChangeListener listener)
  {
    addListener(org.novaforge.forge.ui.portal.client.component.TabsheetImproved.SelectedTabChangeEvent.class, listener,
                SELECTED_TAB_CHANGE_METHOD);
  }

  /**
   * @deprecated As of 7.0, replaced by {@link #removeSelectedTabChangeListener(SelectedTabChangeListener)}
   */
  @Deprecated
  public void removeListener(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.SelectedTabChangeListener listener)
  {
    removeSelectedTabChangeListener(listener);
  }

  /**
   * Removes a tab selection listener
   *
   * @param listener
   *     the Listener to be removed.
   */
  public void removeSelectedTabChangeListener(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.SelectedTabChangeListener listener)
  {
    removeListener(org.novaforge.forge.ui.portal.client.component.TabsheetImproved.SelectedTabChangeEvent.class,
                   listener, SELECTED_TAB_CHANGE_METHOD);
  }

  /**
   * Provide a custom {@link MenuHandler} for this TabsheetImproved to
   * perform some tasks when a user clicks on a tabs menu button.
   *
   * @param handler
   */
  public void setMenuHandler(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.MenuHandler handler)
  {
    menuHandler = handler;
  }

  /**
   * Provide a custom {@link PlusClickHandler} for this TabSheetImproved to
   * perform tasks when a user clicks on a plus button.
   * <p>
   * This method will make plus element visible by default.
   *
   * @param pPlusClickHandler
   */
  public void setPlusClickHandler(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.PlusClickHandler pPlusClickHandler)
  {
    plusClickHandler = pPlusClickHandler;
    getState().plusButtonVisible = true;
    markAsDirty();
  }

  /**
   * This method will change the visibility state of the first element
   *
   * @param pState
   *     true to show first element, false to hid it
   */
  public void setPlusButtonVisible(final boolean pState)
  {
    if (getState().plusButtonVisible != pState)
    {
      getState().plusButtonVisible = pState;
      markAsDirty();
    }
  }

  /**
   * Sets the position of the tab.
   *
   * @param tab
   *     The tab
   * @param position
   *     The new position of the tab
   */
  public void setTabPosition(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab tab,
                             final int position)
  {
    final int oldPosition = getTabPosition(tab);
    components.remove(oldPosition);
    components.add(position, tab.getComponent());

    getState().tabs.remove(oldPosition);
    getState().tabs.add(position,
                        ((org.novaforge.forge.ui.portal.client.component.TabsheetImproved.TabSheetTabImpl) tab)
                            .getTabState());
  }

  /**
   * Gets the position of the tab
   *
   * @param tab
   *     The tab
   *
   * @return
   */
  public int getTabPosition(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab tab)
  {
    return components.indexOf(tab.getComponent());
  }

  @Override
  public void focus()
  {
    super.focus();
  }

  @Override
  protected TabsheetImprovedState getState()
  {
    return (TabsheetImprovedState) super.getState();
  }

  @Override
  protected TabsheetImprovedState getState(final boolean markAsDirty)
  {
    return (TabsheetImprovedState) super.getState(markAsDirty);
  }

  @Override
  public int getTabIndex()
  {
    return getState(false).tabIndex;
  }

  @Override
  public void setTabIndex(final int tabIndex)
  {
    getState().tabIndex = tabIndex;
  }

  @Override
  public boolean isRendered(final com.vaadin.ui.Component childComponent)
  {
    return childComponent == getSelectedTab();
  }

  /**
   * Gets the selected tab content component.
   *
   * @return the selected tab contents
   */
  public com.vaadin.ui.Component getSelectedTab()
  {
    return selected;
  }

  /**
   * Sets the selected tab. The tab is identified by the corresponding {@link Tab Tab} instance. Does nothing
   * if the tabsheet doesn't contain
   * the given tab.
   *
   * @param tab
   */
  public void setSelectedTab(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab tab)
  {
    if (tab != null)
    {
      setSelectedTab(tab.getComponent());
    }
  }

  /**
   * Sets the selected tab, identified by its position. Does nothing if the
   * position is out of bounds.
   *
   * @param position
   */
  public void setSelectedTab(final int position)
  {
    setSelectedTab(getTab(position));
  }

  /**
   * Returns the {@link Tab} (metadata) for a component. The {@link Tab} object can be used for setting
   * caption,icon, etc for the tab.
   *
   * @param position
   *     the position of the tab
   *
   * @return The tab in the given position, or null if the position is out of
   * bounds.
   */
  public org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab getTab(final int position)
  {
    if ((position >= 0) && (position < getComponentCount()))
    {
      return getTab(components.get(position));
    }
    else
    {
      return null;
    }
  }

  /**
   * Sets the selected tab. The tab is identified by the tab content
   * component. Does nothing if the tabsheet doesn't contain the component.
   *
   * @param c
   */
  public void setSelectedTab(final com.vaadin.ui.Component c)
  {
    if ((c != null) && components.contains(c) && !c.equals(selected))
    {
      setSelected(c);
      updateSelection();
      fireSelectedTabChange();
      markAsDirty();
      getRpcProxy(com.vaadin.shared.ui.tabsheet.TabsheetClientRpc.class).revertToSharedStateSelection();
    }
  }

  /**
   * Selected tab change event listener. The listener is called whenever
   * another tab is selected, including when adding the first tab to a
   * tabsheet.
   *
   * @author Vaadin Ltd.
   * @since 3.0
   */
  public interface SelectedTabChangeListener extends java.io.Serializable
  {

    /**
     * Selected (shown) tab in tab sheet has has been changed.
     *
     * @param event
     *     the selected tab change event.
     */
    void selectedTabChange(org.novaforge.forge.ui.portal.client.component.TabsheetImproved.SelectedTabChangeEvent event);
  }

  /**
   * Tab meta-data for a component in a {@link TabSheet}.
   * The meta-data includes the tab caption, icon, visibility and enabledness,
   * closability, description (tooltip) and an optional component error shown
   * in the tab.
   * Tabs are identified by the component contained on them in most cases, and
   * the meta-data can be obtained with {@link TabSheet#getTab(Component)}.
   */
  public interface Tab extends java.io.Serializable
  {
    /**
     * Returns the visible status for the tab. An invisible tab is not shown
     * in the tab bar and cannot be selected.
     *
     * @return true for visible, false for hidden
     */
    boolean isVisible();

    /**
     * Sets the visible status for the tab. An invisible tab is not shown in
     * the tab bar and cannot be selected, selection is changed
     * automatically when there is an attempt to select an invisible tab.
     *
     * @param visible
     *     true for visible, false for hidden
     */
    void setVisible(boolean visible);

    /**
     * Returns the closability status for the tab.
     *
     * @return true if the tab is allowed to be closed by the end user,
     * false for not allowing closing
     */
    boolean isClosable();

    /**
     * Sets the closability status for the tab. A closable tab can be closed
     * by the user through the user interface. This also controls if a close
     * button is shown to the user or not.
     * <p>
     * Note! Currently only supported by TabSheet, not Accordion.
     * </p>
     *
     * @param closable
     *     true if the end user is allowed to close the tab, false
     *     for not allowing to close. Should default to false.
     */
    void setClosable(boolean closable);

    /**
     * Returns the menuability status for the tab.
     *
     * @return true if the end user is allowed to click the menu button
     * false for not allowing to click
     */
    boolean hasMenu();

    /**
     * Sets if a menu button should be displayed.
     *
     * @param pHasMenu
     *     true if the end user is allowed to click on menu button on the tab, false
     *     for not allowing to open the menu. Should default to false.
     */
    void setMenu(boolean pHasMenu);

    /**
     * Get the component that should be automatically focused when the tab
     * is selected.
     *
     * @return the focusable component
     */
    com.vaadin.ui.Component.Focusable getDefaultFocusComponent();

    /**
     * Set the component that should automatically focused when the tab is
     * selected.
     *
     * @param component
     *     the component to focus
     */
    void setDefaultFocusComponent(com.vaadin.ui.Component.Focusable component);

    /**
     * Returns the enabled status for the tab. A disabled tab is shown as
     * such in the tab bar and cannot be selected.
     *
     * @return true for enabled, false for disabled
     */
    boolean isEnabled();

    /**
     * Sets the enabled status for the tab. A disabled tab is shown as such
     * in the tab bar and cannot be selected.
     *
     * @param enabled
     *     true for enabled, false for disabled
     */
    void setEnabled(boolean enabled);

    /**
     * Gets the caption for the tab.
     */
    String getCaption();

    /**
     * Sets the caption for the tab.
     *
     * @param caption
     *     the caption to set
     */
    void setCaption(String caption);

    /**
     * Gets the icon for the tab.
     */
    com.vaadin.server.Resource getIcon();

    /**
     * Sets the icon for the tab.
     *
     * @param icon
     *     the icon to set
     */
    void setIcon(com.vaadin.server.Resource icon);

    /**
     * Sets the icon and alt text for the tab.
     *
     * @param icon
     *     the icon to set
     */
    void setIcon(com.vaadin.server.Resource icon, String iconAltText);

    /**
     * Gets the icon alt text for the tab.
     *
     * @since 7.2
     */
    String getIconAlternateText();

    /**
     * Sets the icon alt text for the tab.
     *
     * @param iconAltText
     *     the icon to set
     *
     * @since 7.2
     */
    void setIconAlternateText(String iconAltText);

    /**
     * Gets the description for the tab. The description can be used to
     * briefly describe the state of the tab to the user, and is typically
     * shown as a tooltip when hovering over the tab.
     *
     * @return the description for the tab
     */
    String getDescription();

    /**
     * Sets the description for the tab. The description can be used to
     * briefly describe the state of the tab to the user, and is typically
     * shown as a tooltip when hovering over the tab.
     *
     * @param description
     *     the new description string for the tab.
     */
    void setDescription(String description);

    /**
     * Gets the current error message shown for the tab.
     * TODO currently not sent to the client
     *
     * @see AbstractComponent#setComponentError(ErrorMessage)
     */
    com.vaadin.server.ErrorMessage getComponentError();

    /**
     * Sets an error indicator to be shown in the tab. This can be used e.g.
     * to communicate to the user that there is a problem in the contents of
     * the tab.
     *
     * @param componentError
     *     error message or null for none
     *
     * @see AbstractComponent#setComponentError(ErrorMessage)
     */
    void setComponentError(com.vaadin.server.ErrorMessage componentError);

    /**
     * Get the component related to the Tab
     */
    com.vaadin.ui.Component getComponent();

    /**
     * Gets the user-defined CSS style name of the tab. Built-in style names
     * defined in Vaadin or GWT are not returned.
     *
     * @return the style name or of the tab
     *
     * @see #setStyleName(String)
     */
    String getStyleName();

    /**
     * Sets a style name for the tab. The style name will be rendered as a
     * HTML class name, which can be used in a CSS definition.
     *
     * <pre>
     * Tab tab = tabsheet.addTab(tabContent, &quot;Tab text&quot;);
     * tab.setStyleName(&quot;mystyle&quot;);
     * </pre>
     * <p>
     * The used style name will be prefixed with " {@code v-tabsheet-tabitemcell-}". For example, if you give
     * a tab the style "{@code mystyle}", the tab will get a " {@code v-tabsheet-tabitemcell-mystyle}" style.
     * You could then style the component with:
     * </p>
     *
     * <pre>
     * .v-tabsheet-tabitemcell-mystyle {font-style: italic;}
     * </pre>
     * <p>
     * This method will trigger a {@link RepaintRequestEvent} on the TabSheet to which the Tab belongs.
     * </p>
     *
     * @param styleName
     *     the new style to be set for tab
     *
     * @see #getStyleName()
     */
    void setStyleName(String styleName);

    /**
     * Gets currently set debug identifier
     *
     * @return current id, null if not set
     */
    String getId();

    /**
     * Adds an unique id for component that is used in the client-side for
     * testing purposes. Keeping identifiers unique is the responsibility of
     * the programmer.
     *
     * @param id
     *     An alphanumeric id
     */
    void setId(String id);
  }

  /**
   * CloseHandler is used to process tab closing events. Default behavior is
   * to remove the tab from the TabsheetImproved.
   *
   * @author Jouni Koivuviita / Vaadin Ltd.
   * @since 6.2.0
   */
  public interface CloseHandler extends java.io.Serializable
  {

    /**
     * Called when a user has pressed the close icon of a tab in the client
     * side widget.
     *
     * @param tabsheet
     *     the TabsheetImproved to which the tab belongs to
     * @param tabContent
     *     the component that corresponds to the tab whose close
     *     button was clicked
     */
    void onTabClose(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved tabsheet,
                    final com.vaadin.ui.Component tabContent);
  }

  /**
   * MenuHandler is used to process menu click events.
   */
  public interface MenuHandler extends java.io.Serializable
  {

    /**
     * Called when a user has pressed the menu icon of a tab in the client
     * side widget.
     *
     * @param tabsheet
     *     the TabSheetState to which the tab belongs to
     * @param tabContent
     *     the component that corresponds to the tab whose close
     *     button was clicked
     */
    void onTabMenu(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved tabsheet,
                   final com.vaadin.ui.Component tabContent);
  }

  /**
   * FirstClickHandler is used to process first click events.
   */
  public interface PlusClickHandler extends java.io.Serializable
  {

    /**
     * Called when a user has pressed the plus button in the client
     * side widget.
     *
     * @param tabsheet
     *     the TabSheetState to which the tab belongs to
     */
    void onPlusClick(final org.novaforge.forge.ui.portal.client.component.TabsheetImproved tabsheet);
  }

  /**
   * Selected tab change event. This event is sent when the selected (shown)
   * tab in the tab sheet is changed.
   *
   * @author Vaadin Ltd.
   * @since 3.0
   */
  public static class SelectedTabChangeEvent extends com.vaadin.ui.Component.Event
  {

    /**
     * New instance of selected tab change event
     *
     * @param source
     *     the Source of the event.
     */
    public SelectedTabChangeEvent(final com.vaadin.ui.Component source)
    {
      super(source);
    }

    /**
     * TabSheet where the event occurred.
     *
     * @return the Source of the event.
     */
    public org.novaforge.forge.ui.portal.client.component.TabsheetImproved getTabSheet()
    {
      return (org.novaforge.forge.ui.portal.client.component.TabsheetImproved) getSource();
    }
  }

  /**
   * Client to server RPC implementation for TabsheetImproved.
   *
   * @since 7.2
   */
  protected class TabsheetServerRpcImpl implements TabsheetImprovedServerRpc
  {

    /**
     * Serial version id
     */
    private static final long serialVersionUID = 5035823154251591394L;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelected(final String pKey)
    {
      setSelectedTab(keyMapper.get(pKey));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeTab(final String pKey)
    {
      final com.vaadin.ui.Component tab = keyMapper.get(pKey);
      if (tab != null)
      {
        closeHandler.onTabClose(org.novaforge.forge.ui.portal.client.component.TabsheetImproved.this, tab);
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void menuTab(final String pKey)
    {
      final com.vaadin.ui.Component tab = keyMapper.get(pKey);
      if ((tab != null) && (menuHandler != null))
      {
        menuHandler.onTabMenu(org.novaforge.forge.ui.portal.client.component.TabsheetImproved.this, tab);
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void plusClick()
    {
      if (plusClickHandler != null)
      {
        plusClickHandler.onPlusClick(org.novaforge.forge.ui.portal.client.component.TabsheetImproved.this);
      }
    }
  }

  @Override
  public void addBlurListener(final com.vaadin.event.FieldEvents.BlurListener listener)
  {
    addListener(com.vaadin.event.FieldEvents.BlurEvent.EVENT_ID, com.vaadin.event.FieldEvents.BlurEvent.class, listener,
                com.vaadin.event.FieldEvents.BlurListener.blurMethod);
  }

  /**
   * TabSheet's implementation of {@link Tab} - tab metadata.
   */
  public class TabSheetTabImpl implements org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab
  {

    private final TabImprovedState tabState;

    private com.vaadin.ui.Component.Focusable defaultFocus;

    private com.vaadin.server.ErrorMessage componentError;

    public TabSheetTabImpl(final String key, String caption, final com.vaadin.server.Resource icon)
    {
      tabState = new TabImprovedState();

      if (caption == null)
      {
        caption = "";
      }

      tabState.key = key;
      tabState.caption = caption;

      setIcon(icon);
    }

    @Override
    public boolean isVisible()
    {
      return tabState.visible;
    }

    @Override
    public void setVisible(final boolean visible)
    {
      tabState.visible = visible;

      if (updateSelection())
      {
        fireSelectedTabChange();
      }
      markAsDirty();
    }

    @Override
    public boolean isClosable()
    {
      return tabState.closable;
    }

    @Override
    public void setClosable(final boolean closable)
    {
      tabState.closable = closable;
      markAsDirty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasMenu()
    {
      return tabState.hasMenu;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMenu(final boolean pHasMenu)
    {
      tabState.hasMenu = pHasMenu;
      markAsDirty();
    }

    @Override
    public com.vaadin.ui.Component.Focusable getDefaultFocusComponent()
    {
      return defaultFocus;
    }

    @Override
    public void setDefaultFocusComponent(final com.vaadin.ui.Component.Focusable defaultFocus)
    {
      this.defaultFocus = defaultFocus;
    }

    @Override
    public boolean isEnabled()
    {
      return tabState.enabled;
    }

    @Override
    public void setEnabled(final boolean enabled)
    {
      tabState.enabled = enabled;

      if (updateSelection())
      {
        fireSelectedTabChange();
      }
      markAsDirty();
    }

    /**
     * Returns the tab caption. Can never be null.
     */

    @Override
    public String getCaption()
    {
      return tabState.caption;
    }

    @Override
    public void setCaption(final String caption)
    {
      tabState.caption = caption;
      markAsDirty();
    }

    @Override
    public com.vaadin.server.Resource getIcon()
    {
      return getResource(com.vaadin.shared.ComponentConstants.ICON_RESOURCE + tabState.key);
    }

    @Override
    public void setIcon(final com.vaadin.server.Resource icon)
    {
      // this might not be ideal (resetting icon altText), but matches
      // previous semantics
      setIcon(icon, "");
    }

    @Override
    public void setIcon(final com.vaadin.server.Resource icon, final String iconAltText)
    {
      setResource(com.vaadin.shared.ComponentConstants.ICON_RESOURCE + tabState.key, icon);
      tabState.iconAltText = iconAltText;
    }

    @Override
    public String getIconAlternateText()
    {
      return tabState.iconAltText;
    }

    @Override
    public void setIconAlternateText(final String iconAltText)
    {
      tabState.iconAltText = iconAltText;
      markAsDirty();
    }

    @Override
    public String getDescription()
    {
      return tabState.description;
    }

    @Override
    public void setDescription(final String description)
    {
      tabState.description = description;
      markAsDirty();
    }

    @Override
    public com.vaadin.server.ErrorMessage getComponentError()
    {
      return componentError;
    }

    @Override
    public void setComponentError(final com.vaadin.server.ErrorMessage componentError)
    {
      this.componentError = componentError;

      tabState.componentError = componentError != null ? componentError.getFormattedHtmlMessage() : null;

      markAsDirty();
    }

    @Override
    public com.vaadin.ui.Component getComponent()
    {
      for (final java.util.Map.Entry<com.vaadin.ui.Component, org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab> entry : tabs.entrySet())
      {
        if (entry.getValue() == this)
        {
          return entry.getKey();
        }
      }
      return null;
    }

    @Override
    public String getStyleName()
    {
      return tabState.styleName;
    }

    @Override
    public void setStyleName(final String styleName)
    {
      tabState.styleName = styleName;

      markAsDirty();
    }

    @Override
    public String getId()
    {
      return tabState.id;
    }

    @Override
    public void setId(final String id)
    {
      tabState.id = id;
      markAsDirty();

    }

    protected TabImprovedState getTabState()
    {
      return tabState;
    }
  }

  /**
   * @deprecated As of 7.0, replaced by {@link #addBlurListener(BlurListener)}
   */
  @Override
  @Deprecated
  public void addListener(final com.vaadin.event.FieldEvents.BlurListener listener)
  {
    addBlurListener(listener);
  }

  @Override
  public void removeBlurListener(final com.vaadin.event.FieldEvents.BlurListener listener)
  {
    removeListener(com.vaadin.event.FieldEvents.BlurEvent.EVENT_ID, com.vaadin.event.FieldEvents.BlurEvent.class,
                   listener);
  }

  /**
   * @deprecated As of 7.0, replaced by {@link #removeBlurListener(BlurListener)}
   */
  @Override
  @Deprecated
  public void removeListener(final com.vaadin.event.FieldEvents.BlurListener listener)
  {
    removeBlurListener(listener);
  }

  @Override
  public void addFocusListener(final com.vaadin.event.FieldEvents.FocusListener listener)
  {
    addListener(com.vaadin.event.FieldEvents.FocusEvent.EVENT_ID, com.vaadin.event.FieldEvents.FocusEvent.class,
                listener, com.vaadin.event.FieldEvents.FocusListener.focusMethod);
  }

  /**
   * @deprecated As of 7.0, replaced by {@link #addFocusListener(FocusListener)}
   */
  @Override
  @Deprecated
  public void addListener(final com.vaadin.event.FieldEvents.FocusListener listener)
  {
    addFocusListener(listener);
  }

  @Override
  public void removeFocusListener(final com.vaadin.event.FieldEvents.FocusListener listener)
  {
    removeListener(com.vaadin.event.FieldEvents.FocusEvent.EVENT_ID, com.vaadin.event.FieldEvents.FocusEvent.class,
                   listener);
  }

  /**
   * @deprecated As of 7.0, replaced by {@link #removeFocusListener(FocusListener)}
   */
  @Override
  @Deprecated
  public void removeListener(final com.vaadin.event.FieldEvents.FocusListener listener)
  {
    removeFocusListener(listener);
  }

}