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
package org.novaforge.forge.ui.dashboard.internal.client.tab.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.security.authorization.PermissionAction;
import org.novaforge.forge.dashboard.model.Tab;
import org.novaforge.forge.dashboard.model.Widget;
import org.novaforge.forge.dashboard.model.WidgetDataComponent;
import org.novaforge.forge.dashboard.model.WidgetModule;
import org.novaforge.forge.dashboard.service.WidgetModuleListener;
import org.novaforge.forge.dashboard.xml.Layout;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.dashboard.event.WidgetClosedEvent;
import org.novaforge.forge.ui.dashboard.internal.client.AbstractDashBoardPresenter;
import org.novaforge.forge.ui.dashboard.internal.client.event.ShowSettingsEvent;
import org.novaforge.forge.ui.dashboard.internal.client.event.UpdateTabContentEvent;
import org.novaforge.forge.ui.dashboard.internal.client.event.UpdateTabLayoutEvent;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.component.PortalLayoutArea;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.component.WidgetHeader;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.component.WidgetPortletMapping;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.component.WidgetPortletMapping.WidgetPortlet;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.component.WidgetRefresher;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.event.PortletMoveEvent;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.event.PortletMoveEvent.Listener;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.task.WidgetComponentFactory;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.task.WidgetRefreshCallable;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.task.WidgetRefreshCallable.Result;
import org.novaforge.forge.ui.dashboard.internal.client.tab.content.task.WidgetRefreshExecutor;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.event.UIRefreshedEvent;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.vaadin.addons.portallayout.event.PortletCloseEvent;
import org.vaadin.addons.portallayout.portlet.Portlet;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.google.common.base.Strings;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import net.engio.mbassy.listener.Handler;

/**
 * @author Guillaume Lamirand
 */
public class ContentPresenter extends AbstractDashBoardPresenter implements Serializable, WidgetModuleListener
{
  /**
   * Serial version id
   */
  private static final long               serialVersionUID     = -3067819403919493755L;
  private static final Log                LOGGER               = LogFactory.getLog(ContentPresenter.class);
  private final ContentView               view;
  private final UUID                      currentTabUUID;
  private final List<WidgetDataComponent> refreshWidgetQueue   = new ArrayList<>();
  private final WidgetPortletMapping      widgetPortletMapping = new WidgetPortletMapping();
  private Tab                             currentTab;
  private Layout                          currentLayout;
  private Portlet                         currentDeletingPortlet;
  private PortalLayoutArea                currentDeletingArea;
  private Refresher                       widgetRefresher;
  private boolean                         hasAdminRights;

  /**
   * Default constructor
   *
   * @param pPortalContext
   *          the portalContext used to initialize this module
   * @param pTabUUID
   *          the associated tab uuid
   * @param pView
   *          the view associated to this presenter
   */
  public ContentPresenter(final PortalContext pPortalContext, final UUID pTabUUID, final ContentView pView)
  {
    super(pPortalContext);
    currentTabUUID = pTabUUID;

    // Init the view
    view = pView;

    // Register listeners
    DashboardModule.getWidgetModuleService().addListener(this);

    addListener();
  }

  private void addListener()
  {
    view.getWidgetDeleteWindow().getYesButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 641183805053727835L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        UI.getCurrent().removeWindow(view.getWidgetDeleteWindow());
        if ((currentDeletingPortlet != null) && (currentDeletingArea != null))
        {
          currentDeletingArea.removePortlet(currentDeletingPortlet);
          final WidgetPortlet widgetPortlet = widgetPortletMapping.get(currentDeletingPortlet);
          if ((widgetPortlet != null) && (widgetPortlet.getWidget() != null))
          {
            final Widget widget = widgetPortlet.getWidget();
            DashboardModule.getDashBoardService().removeWidget(currentTabUUID, widget.getUUID());
            getEventBus().publishAsync(new WidgetClosedEvent(widget.getUUID()));
            for (final Portlet portlet : currentDeletingArea.getPortlets())
            {
              final WidgetPortlet otherWidgetPortlet = widgetPortletMapping.get(portlet);
              if ((otherWidgetPortlet != null) && (otherWidgetPortlet.getPortlet() != null))
              {
                final Widget otherWidget = otherWidgetPortlet.getWidget();
                final Portlet otherPortlet = otherWidgetPortlet.getPortlet();
                final int newIndex = currentDeletingArea.indexOf(otherPortlet);
                if (newIndex != widget.getAreaIndex())
                {
                  otherWidget.setAreaIndex(newIndex);
                  DashboardModule.getDashBoardService().updateWidget(otherWidget);
                }
              }
            }
          }
          currentDeletingPortlet = null;
          currentDeletingArea = null;
          refreshContent();
        }
      }
    });

  }

  private void refreshGrid()
  {
    // Clear previous data
    refreshWidgetQueue.clear();
    if ((widgetRefresher != null) && (widgetRefresher.isAttached()))
    {
      widgetRefresher.remove();
    }

    // Refresh grid with new layout
    view.refreshGrid(currentLayout);
    for (final PortalLayoutArea portalLayoutArea : view.getPortalLayoutAreas())
    {
      addListenerToArea(portalLayoutArea);
    }

    // Build executor to refresh widget content
    final WidgetRefreshExecutor widgetRefreshExecutor = new WidgetRefreshExecutor();

    // Browse widgets sorted by area index and build its associated callable
    final List<Widget> widgets = new LinkedList(currentTab.getWidgets());
    Collections.sort(widgets, new Comparator<Widget>()
    {

      /**
       * @param pWidget1
       *          the first widget
       * @param pWidget2
       *          the second widget
       * @return the value 0 if x == y; a value less than 0 if x < y; and a value greater than 0 if x > y
       */
      @Override
      public int compare(final Widget pWidget1, final Widget pWidget2)
      {
        return Integer.compare(pWidget1.getAreaIndex(), pWidget2.getAreaIndex());
      }
    });
    for (final Widget widget : widgets)
    {
      try
      {
        if (isPermittedToAccess(widget))
        {
          // Build a widget component for the widget found
          final WidgetDataComponent widgetComponent = WidgetComponentFactory
              .createLoadingWidgetComponent(getEventBus(), getCurrentLocale(), widget);
          final Portlet addPortlet = addPortlet(widget, widgetComponent);
          // Updating mapping
          final WidgetPortlet addedWidgetPortlet = widgetPortletMapping.addWidgetPortlet(widget,
              widgetComponent);
          addedWidgetPortlet.setPortlet(addPortlet);
          widgetRefreshExecutor
              .addCallable(new WidgetRefreshCallable(widget, getEventBus(), getCurrentLocale()));
        }
      }
      catch (final Exception e)
      {
        LOGGER.warn("Unable to add widget to the current dashboard", e);
        ExceptionCodeHandler.showNotificationError(DashboardModule.getPortalMessages(), e,
            getCurrentLocale());
      }
    }
    // Create new WidgetRefresher
    widgetRefresher = new WidgetRefresher(view.getUI());
    widgetRefresher.addListener(new RefreshListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 6991774853844859939L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void refresh(final Refresher pSource)
      {
        final boolean viewIsVisible = view.isAttached() || view.isConnectorEnabled();
        boolean removeExtension;

        if (viewIsVisible)
        {
          for (final Future<Result> future : widgetRefreshExecutor.getDoneFutures())
          {
            try
            {
              if (!future.isCancelled())
              {
                final WidgetRefreshCallable.Result result = future.get();
                final WidgetPortlet widgetPortlet = widgetPortletMapping.get(result.getUuid());
                if (widgetPortlet != null)
                {

                  final WidgetDataComponent widgetDataComponent = result.getWidgetDataComponent();
                  final Widget widget = widgetPortlet.getWidget();
                  final Portlet portlet = widgetPortlet.getPortlet();

                  // Clean existing portlet
                  final PortalLayoutArea boxArea = view.getPortalLayoutArea(widget.getAreaId());
                  boxArea.removePortlet(portlet);

                  // Add widget component as portlet
                  final Portlet newPortlet = addPortlet(widget, widgetDataComponent);

                  // Refreshing widget component
                  widgetDataComponent.refreshComponent();

                  // Updating mapping
                  widgetPortlet.setPortlet(newPortlet);
                  widgetPortlet.setWidgetDataComponent(widgetDataComponent);
                }
              }
            }
            catch (InterruptedException | CancellationException e)
            {
              LOGGER.warn("One of the widget callable task has been cancelled during refresh process", e);
            }
            catch (final ExecutionException e)
            {
              LOGGER.error("Unable to refresh widget content", e);
              ExceptionCodeHandler.showNotificationError(DashboardModule.getPortalMessages(), e,
                  getCurrentLocale());
            }
            finally
            {
              // Remove future for next process
              widgetRefreshExecutor.removeFuture(future);
            }
          }
          removeExtension = !widgetRefreshExecutor.hasFutures();
        }
        else
        {
          removeExtension = true;
        }

        if ((removeExtension) && (UI.getCurrent() != null))
        {
          widgetRefresher.remove();
        }

      }

    });

    // Execute all widget refresh callable
    widgetRefreshExecutor.submitAll();
  }

  private void addListenerToArea(final PortalLayoutArea portalLayoutArea)
  {
    portalLayoutArea.addPortletMoveListener(new Listener()
    {

      /**
       * {@inheritDoc}
       */
      @Override
      public void portletMoved(final PortletMoveEvent event)
      {
        final WidgetPortlet widgetPortlet = widgetPortletMapping.get(event.getPortlet());
        if ((widgetPortlet != null) && (widgetPortlet.getWidget() != null))
        {
          final Widget widget = widgetPortlet.getWidget();
          DashboardModule.getDashBoardService().moveWidget(widget, event.getPortalLayoutArea().getAreaId(),
              event.getIndex());
        }
      }
    });
    portalLayoutArea.addPortletCloseListener(new PortletCloseEvent.Listener()
    {

      /**
       * {@inheritDoc}
       */
      @Override
      public void portletClosed(final PortletCloseEvent pEvent)
      {
        UI.getCurrent().addWindow(view.getWidgetDeleteWindow());
        currentDeletingPortlet = pEvent.getPortlet();
        currentDeletingArea = (PortalLayoutArea) pEvent.getSource();
      }
    });
  }

  private boolean isPermittedToAccess(final Widget widget)
  {
    boolean isPermitted = true;
    if (!Strings.isNullOrEmpty(widget.getDataSource()))
    {
      final Map<String, List<String>> dataSource = DashboardModule.getDataSourceFactory()
          .readDataSource(widget.getDataSource());
      final Set<Entry<String, List<String>>> entrySet = dataSource.entrySet();
      for (final Entry<String, List<String>> entry : entrySet)
      {
        final String projectId = entry.getKey();
        if (entry.getValue() != null)
        {
          for (final String app : entry.getValue())
          {
            isPermitted = DashboardModule.getSecurityDelegate().isPermitted(projectId, app,
                PermissionAction.READ);
            if (!isPermitted)
            {
              break;
            }
          }
        }
        if (!isPermitted)
        {
          break;
        }
      }
    }
    return isPermitted;
  }

  private Portlet addPortlet(final Widget pWidget, final WidgetDataComponent pWidgetDataComponent)
  {
    // Getting box area target
    final PortalLayoutArea boxArea = view.getPortalLayoutArea(pWidget.getAreaId());

    // Creating portlet
    final Portlet widgetPortlet = boxArea.addPortletAt(pWidgetDataComponent.getComponent(),
        pWidget.getAreaIndex());
    widgetPortlet.getContent().setCaption(pWidget.getName());

    // Only make widget closable for admin
    widgetPortlet.setClosable(hasAdminRights);

    // Updating top icon
    final WidgetModule module = DashboardModule.getWidgetModuleService().getModule(pWidget.getKey());
    StreamResource widgetIcon = null;
    if (module.getIcon() != null)
    {
      widgetIcon = ResourceUtils.buildImageResource(module.getIcon(), pWidget.getUUID().toString());
      widgetPortlet.getContent().setIcon(widgetIcon);
    }

    // Create portlet header
    final WidgetHeader header = createHeaderComponent(pWidget.getUUID(), pWidget.getName(), widgetIcon);
    widgetPortlet.setHeaderComponent(header);

    return widgetPortlet;
  }

  private WidgetHeader createHeaderComponent(final UUID pWidgetUUID, final String pWidgetName,
      final Resource pIcon)
  {
    final WidgetHeader header = new WidgetHeader(hasAdminRights);
    header.getRefresh().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3724644361695095072L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        final WidgetPortlet widgetPortlet = widgetPortletMapping.get(pWidgetUUID);
        if (widgetPortlet != null)
        {
          final WidgetDataComponent widgetComponent = widgetPortlet.getWidgetDataComponent();
          widgetComponent.refresh();
          if (refreshWidgetQueue.contains(widgetComponent))
          {
            refreshWidgetQueue.remove(widgetComponent);
          }
        }
        // Handle refresh widgets queue
        handleRefreshWidgets();

      }
    });
    if (hasAdminRights)
    {
      header.getSettings().addClickListener(new ClickListener()
      {

        /**
         * Serial version id
         */
        private static final long serialVersionUID = 789305470658622810L;

        /**
         * {@inheritDoc}
         */
        @Override
        public void buttonClick(final ClickEvent pEvent)
        {
          getEventBus().publish(new ShowSettingsEvent(currentTabUUID, pWidgetUUID, pWidgetName, pIcon));

        }
      });
    }
    return header;
  }

  private void handleRefreshWidgets()
  {

    if (refreshWidgetQueue != null)
    {
      for (final WidgetDataComponent component : refreshWidgetQueue)
      {
        if (component.getComponent().getParent() != null)
        {
          component.refresh();
        }
        else
        {
          try
          {
            getEventBus().unsubscribe(component);
          }
          catch (final IllegalArgumentException e)
          {
            LOGGER.warn(
                "An widget module has been refreshed but it cannot be unregistered from current eventBus");
          }
        }
      }
      refreshWidgetQueue.clear();
    }
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
  public void refreshContent()
  {
    widgetPortletMapping.clear();
    currentDeletingPortlet = null;
    currentDeletingArea = null;
    currentTab = DashboardModule.getDashBoardService().getTab(currentTabUUID);
    currentLayout = DashboardModule.getLayoutService().getLayout(currentTab.getLayoutKey());
    hasAdminRights = hasAdminRights();

    refreshGrid();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void moduleRegistered(final WidgetModule pWidgetModule)
  {
    handleModuleListener(pWidgetModule.getKey());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void moduleUnregistered(final String pWidgetKey)
  {
    handleModuleListener(pWidgetKey);
  }

  private void handleModuleListener(final String pWidgetKey)
  {
    if ((pWidgetKey != null) && (!"".equals(pWidgetKey)))
    {
      final List<WidgetPortlet> gets = widgetPortletMapping.gets(pWidgetKey);
      for (final WidgetPortlet widgetPortlet : gets)
      {
        final Portlet portlet = widgetPortlet.getPortlet();
        final Widget widget = widgetPortlet.getWidget();
        final PortalLayoutArea boxArea = view.getPortalLayoutArea(widget.getAreaId());
        boxArea.removePortlet(portlet);

        // Build a widget component for the widget found
        final WidgetDataComponent newWidgetComponent = WidgetComponentFactory
            .createWidgetComponent(getEventBus(), getCurrentLocale(), widget);
        final Portlet addPortlet = addPortlet(widget, newWidgetComponent);

        // Updating mapping
        final WidgetPortlet addedWidgetPortlet = widgetPortletMapping.addWidgetPortlet(widget,
            newWidgetComponent);
        addedWidgetPortlet.setPortlet(addPortlet);
        refreshWidgetQueue.add(newWidgetComponent);
      }
    }
  }

  /**
   * Method call when a {@link UIRefreshedEvent} is received
   *
   * @param pEvent
   *          the event received
   */
  @Handler
  public void onUpdateTabLayoutEvent(final UpdateTabLayoutEvent pEvent)
  {
    final Tab tab = pEvent.getTab();
    if (currentTabUUID.equals(tab.getUUID()))
    {
      final Layout layout = DashboardModule.getLayoutService().getLayout(tab.getLayoutKey());
      final int areaIds = layout.getArea().size();
      if (currentLayout.getArea().size() > areaIds)
      {
        currentTab = DashboardModule.getDashBoardService().getTab(currentTabUUID);
        for (final Widget widget : currentTab.getWidgets())
        {
          if (widget.getAreaId() > areaIds)
          {
            DashboardModule.getDashBoardService().removeWidget(currentTabUUID, widget.getUUID());
          }
        }
      }
      // Updating the tab after the removing of widgets
      currentTab = DashboardModule.getDashBoardService().getTab(currentTabUUID);
      currentLayout = layout;
      refreshGrid();
    }
  }

  /**
   * Method call when a {@link UIRefreshedEvent} is received
   *
   * @param pEvent
   *          the event received
   */
  @Handler
  public void onUpdateTabContentEvent(final UpdateTabContentEvent pEvent)
  {
    final Tab tab = pEvent.getTab();
    if (currentTabUUID.equals(tab.getUUID()))
    {
      refreshContent();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return DashboardModule.getModuleId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregisterReferences()
  {
    if (widgetPortletMapping != null)
    {
      final List<WidgetPortlet> gets = widgetPortletMapping.gets();
      if (gets != null)
      {
        for (final WidgetPortlet widgetPortlet : gets)
        {
          getEventBus().publishAsync(new WidgetClosedEvent(widgetPortlet.getUUID()));
        }
      }
      widgetPortletMapping.clear();
    }
    if ((widgetRefresher != null) && (widgetRefresher.isAttached()))
    {
      widgetRefresher.remove();
    }
    refreshWidgetQueue.clear();
    currentDeletingPortlet = null;
    currentDeletingArea = null;
    widgetRefresher = null;
    super.unregisterReferences();
  }
}
