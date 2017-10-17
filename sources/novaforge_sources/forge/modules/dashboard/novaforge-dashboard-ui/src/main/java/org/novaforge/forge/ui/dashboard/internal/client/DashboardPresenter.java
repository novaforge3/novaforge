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
package org.novaforge.forge.ui.dashboard.internal.client;

import com.google.common.base.Strings;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import net.engio.mbassy.listener.Handler;
import org.novaforge.forge.dashboard.model.DashBoard;
import org.novaforge.forge.dashboard.model.Tab;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.dashboard.internal.client.component.TabDropHandler;
import org.novaforge.forge.ui.dashboard.internal.client.event.CloseTabHeaderEvent;
import org.novaforge.forge.ui.dashboard.internal.client.event.DeleteTabEvent;
import org.novaforge.forge.ui.dashboard.internal.client.event.UpdateTabInfoEvent;
import org.novaforge.forge.ui.dashboard.internal.client.tab.TabPresenter;
import org.novaforge.forge.ui.dashboard.internal.client.tab.TabViewImpl;
import org.novaforge.forge.ui.dashboard.internal.client.tab.settings.apps.ApplicationsSourcePresenter;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;
import org.novaforge.forge.ui.portal.client.component.DDTabsheetImproved;
import org.novaforge.forge.ui.portal.client.component.TabsheetImproved;
import org.novaforge.forge.ui.portal.client.component.TabsheetImproved.SelectedTabChangeEvent;
import org.novaforge.forge.ui.portal.client.component.TabsheetImproved.SelectedTabChangeListener;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

/**
 * Presenter used to manage dashboard component.
 * 
 * @author Guillaume Lamirand
 */
public class DashboardPresenter extends AbstractDashBoardPresenter implements Serializable, PortalComponent
{
  /**
   * Serial version id used for serialization
   */
  private static final long                     serialVersionUID = 1158368564534063818L;

  /**
   * View associated to this presenter
   */
  private final DashboardView                   view;

  /**
   * This map contains a reference to a {@link Tab#getUUID()} associated to its
   * {@link org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab} element.
   */
  private final Map<UUID, DDTabsheetImproved.Tab> tabMapping;
  /**
   * This map contains a reference to a {@link Tab#getUUID()} associated to its
   * {@link ApplicationsSourcePresenter} element.
   */
  private final Map<UUID, TabPresenter>           tabContentMapping;

  /**
   * Default constructor
   *
   * @param pPortalContext
   *          the portalContext used to initialize this module
   * @param pView
   *          the view associated to this presenter
   */
  public DashboardPresenter(final PortalContext pPortalContext, final DashboardView pView)
  {
    // Init context
    super(pPortalContext);

    // Init tab mapping
    tabMapping = new HashMap<UUID, DDTabsheetImproved.Tab>();
    tabContentMapping = new LinkedHashMap<UUID, TabPresenter>();

    // Init the view and sub-view
    view = pView;

    // Register listeners
    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getTabSheet().setDragMode(LayoutDragMode.CLONE);
    // Enable dropping
    view.getTabSheet().setDropHandler(new TabDropHandler(this));
    view.getTabSheet().setMenuHandler(new TabsheetImproved.MenuHandler()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -2877774191059737279L;

      /**
       * @param pTabsheet
       *     the TabSheetState to which the tab belongs to
       * @param pTabContent
       *     the component that corresponds to the tab whose close
       */
      @Override
      public void onTabMenu(final TabsheetImproved pTabsheet, final Component pTabContent)
      {

        final Set<Entry<UUID, org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab>> entrySet = tabMapping
                                                                                                                   .entrySet();
        for (final Entry<UUID, org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab> entry : entrySet)
        {
          if (entry.getValue().getComponent().equals(pTabContent))
          {
            if (tabContentMapping.containsKey(entry.getKey()))
            {
              final TabPresenter tabPresenter = tabContentMapping.get(entry.getKey());
              tabPresenter.showHeader(!tabPresenter.isHeaderShown());
              if (tabPresenter.isHeaderShown())
              {
                tabPresenter.refreshTabHeader();
              }
              break;
            }

          }
        }
      }

    });
    view.getTabSheet().setPlusClickHandler(new TabsheetImproved.PlusClickHandler()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 1564960115480405209L;

      /**
       * Called when a user has pressed the plus button in the client
       * side widget.
       *
       * @param pTabsheet
       *     the TabSheetState to which the tab belongs to
       */
      @Override
      public void onPlusClick(final TabsheetImproved pTabsheet)
      {

        final Tab newTab = DashboardModule.getDashBoardService().newTab();
        newTab.setName(DashboardModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
                                                                      Messages.DASHBOARD_TAB_DEFAULTNAME));
        final Tab addTab = DashboardModule.getDashBoardService().addTab(getType(), getTypeId(), newTab);
        refreshContent();
        view.getTabSheet().setSelectedTab(tabMapping.size() - 1);

        if (tabContentMapping.containsKey(addTab.getUUID()))
        {
          final TabPresenter tabPresenter = tabContentMapping.get(addTab.getUUID());
          tabPresenter.showHeader(true);
          tabPresenter.refreshTabHeader();
        }

      }
    });
    view.getTabSheet().addSelectedTabChangeListener(new SelectedTabChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 29701475196761748L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void selectedTabChange(final SelectedTabChangeEvent pEvent)
      {
        final Set<Entry<UUID, org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab>> entrySet = tabMapping
                                                                                                                   .entrySet();
        for (final Entry<UUID, org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab> entry : entrySet)
        {
          if ((pEvent.getTabSheet().getSelectedTab() != null) && (pEvent.getTabSheet().getSelectedTab().equals(entry
                                                                                                                   .getValue()
                                                                                                                   .getComponent())))
          {
            if (tabContentMapping.containsKey(entry.getKey()))
            {
              final TabPresenter tabPresenter = tabContentMapping.get(entry.getKey());
              tabPresenter.refreshTabContent();
            }
          }
        }
      }
    });
  }

  private Component buildTabContent(final UUID pTabUUID)
  {
    final TabPresenter tabPresenter = new TabPresenter(getPortalContext(), pTabUUID, new TabViewImpl());
    tabContentMapping.put(pTabUUID, tabPresenter);
    return tabPresenter.getComponent();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    if ((getType() != null) && (!Strings.isNullOrEmpty(getTypeId())))
    {
      final boolean hasAdminRights = hasAdminRights();

      // Clear the view and sub-component
      if ((!tabMapping.isEmpty()) || (!tabContentMapping.isEmpty()))
      {
        final Set<Entry<UUID, TabPresenter>> entrySet = tabContentMapping.entrySet();
        for (final Entry<UUID, TabPresenter> entry : entrySet)
        {
          entry.getValue().unregisterReferences();
        }
        tabMapping.clear();
        tabContentMapping.clear();
        view.getTabSheet().removeAllComponents();

      }
      // Show first element only if user log on has admin rights
      view.getTabSheet().setPlusButtonVisible(hasAdminRights);

      // Retrieve dashboard
      final DashBoard dashBoard = DashboardModule.getDashBoardService().getDashBoard(getType(), getTypeId());
      if (dashBoard != null)
      {
        final List<Tab> tabs = dashBoard.getTabs();
        for (final Tab tab : tabs)
        {
          StreamResource image = null;
          if (tab.getImage() != null)
          {
            image = ResourceUtils.buildImageResource(tab.getImage().getFile(), tab.getName());
          }
          final Component tabContent = buildTabContent(tab.getUUID());
          final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab addTab = view.getTabSheet()
                                                                                                 .addTab(tabContent,
                                                                                                         tab.getName(),
                                                                                                         image,
              tab.getIndex());
          // Show menu action button only if user log on has admin rights
          addTab.setMenu(hasAdminRights);
          tabContentMapping.get(tab.getUUID()).initHiddenHeader();
          tabMapping.put(tab.getUUID(), addTab);
        }

        // Get and refresh the first tab
        if (tabContentMapping.size() > 0)
        {
          final TabPresenter firstTab = tabContentMapping.values().iterator().next();
          firstTab.refreshTabContent();
          view.getTabSheet().setSelectedTab(0);
        }
      }

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init()
  {
    refreshContent();
    refreshLocalized(getCurrentLocale());

  }

  /**
   * This method is call when a tab has been drag and drop
   */
  public void handleTabReordering()
  {
    if (tabMapping != null)
    {
      final Set<Entry<UUID, org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab>> entrySet = tabMapping
                                                                                                                 .entrySet();
      for (final Entry<UUID, org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab> entry : entrySet)
      {
        final Tab tab = DashboardModule.getDashBoardService().getTab(entry.getKey());
        final int newTabPosition = view.getTabSheet().getTabPosition(entry.getValue());
        if (tab.getIndex() != newTabPosition)
        {
          tab.setIndex(newTabPosition);
          DashboardModule.getDashBoardService().updateTab(tab);
        }

      }
    }

  }

  /**
   * Method call when a {@link CloseTabHeaderEvent} is received
   * 
   * @param pEvent
   *          the event received
   */
  @Handler
  public void onCloseTabHeaderEvent(final CloseTabHeaderEvent pEvent)
  {
    final UUID uuid = pEvent.getTab().getUUID();
    if (tabContentMapping.containsKey(uuid))
    {
      tabContentMapping.get(uuid).showHeader(false);
    }
  }

  /**
   * Method call when a {@link UpdateTabInfoEvent} is received
   * 
   * @param pEvent
   *          the event received
   */
  @Handler
  public void onUpdateTabEvent(final UpdateTabInfoEvent pEvent)
  {
    if (tabMapping.containsKey(pEvent.getTab().getUUID()))
    {
      final org.novaforge.forge.ui.portal.client.component.TabsheetImproved.Tab tab = tabMapping.get(pEvent.getTab()
                                                                                                           .getUUID());
      tab.setCaption(pEvent.getTab().getName());
      StreamResource image = null;
      if (pEvent.getTab().getImage() != null)
      {
        image = ResourceUtils.buildImageResource(pEvent.getTab().getImage().getFile(), pEvent.getTab()
            .getName());
      }
      tab.setIcon(image);
    }
  }

  /**
   * Method call when a {@link DeleteTabEvent} is received
   * 
   * @param pEvent
   *          the event received
   */
  @Handler
  public void onDeleteTabEvent(final DeleteTabEvent pEvent)
  {
    if (tabMapping.containsKey(pEvent.getTab().getUUID()))
    {
      DashboardModule.getDashBoardService().removeTab(getType(), getTypeId(), pEvent.getTab().getUUID());
      refreshContent();
    }

  }

}
