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
package org.novaforge.forge.ui.dashboard.internal.client.tab.settings;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import net.engio.mbassy.listener.Handler;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.dashboard.internal.client.AbstractDashBoardPresenter;
import org.novaforge.forge.ui.dashboard.internal.client.component.PortletWrapper;
import org.novaforge.forge.ui.dashboard.internal.client.event.CloseSettingsEvent;
import org.novaforge.forge.ui.dashboard.internal.client.event.ShowSettingsEvent;
import org.novaforge.forge.ui.dashboard.internal.client.event.UpdateWidgetNameEvent;
import org.novaforge.forge.ui.dashboard.internal.client.tab.settings.apps.ApplicationsSourcePresenter;
import org.novaforge.forge.ui.dashboard.internal.client.tab.settings.apps.ApplicationsSourceViewImpl;
import org.novaforge.forge.ui.dashboard.internal.client.tab.settings.widget.WidgetPropertiesPresenter;
import org.novaforge.forge.ui.dashboard.internal.client.tab.settings.widget.WidgetPropertiesViewImpl;

import java.util.Locale;
import java.util.UUID;

/**
 * This presenter handles settings main view.
 * 
 * @author Guillaume Lamirand
 */
public class SettingsPresenter extends AbstractDashBoardPresenter
{
  /**
   * Content of settings view
   */
  private final SettingsView                view;

  /**
   * Tab uuid attached to this presenter
   */
  private final UUID                        currentTabUUID;

  private final PortletWrapper              portletWrapper;
  private final WidgetPropertiesPresenter   widgetPropertiesPresenter;
  private final ApplicationsSourcePresenter applicationsSourcePresenter;
  private       UUID                        widgetUUID;
  private String                            widgetName;

  /**
   * Default constructor.
   * 
   * @param pPortalContext
   *          the portalContext used to initialize this module
   * @param pTabUUID
   *          the associated tab uuid
   * @param pView
   *          the view associated to this presenter
   */
  public SettingsPresenter(final PortalContext pPortalContext, final UUID pTabUUID, final SettingsView pView)
  {
    super(pPortalContext);
    currentTabUUID = pTabUUID;
    view = pView;
    portletWrapper = new PortletWrapper();
    portletWrapper.wrap(view);
    widgetPropertiesPresenter = new WidgetPropertiesPresenter(new WidgetPropertiesViewImpl(), pPortalContext);
    applicationsSourcePresenter = new ApplicationsSourcePresenter(new ApplicationsSourceViewImpl(),
        pPortalContext);

    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    portletWrapper.addBackClickListener(new ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        getEventBus().publish(new CloseSettingsEvent(currentTabUUID, widgetUUID));

      }
    });
    view.getDataSource().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7394860492677119821L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(applicationsSourcePresenter.getComponent());
        applicationsSourcePresenter.refresh(widgetUUID);
      }
    });
    view.getProperties().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 3758544191489241509L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(widgetPropertiesPresenter.getComponent());
        widgetPropertiesPresenter.refresh(widgetUUID, widgetName);
      }
    });
  }

  /**
   * Will refresh the widget settings
   * 
   * @param pWidgetUUID
   *          the widget uuid used to refresh this view
   * @param pWidgetName
   *          the widget name used to refresh this view
   * @param pIcon
   *          the widget icon
   */
  public void refresh(final UUID pWidgetUUID, final String pWidgetName, final Resource pIcon)
  {
    widgetUUID = pWidgetUUID;
    widgetName = pWidgetName;
    if (pIcon != null)
    {
      portletWrapper.setHeaderIcon(pIcon);
    }
    portletWrapper.setHeaderCaption(pWidgetName);
    view.getProperties().removeStyleName(NovaForge.SELECTED);
    view.getDataSource().addStyleName(NovaForge.SELECTED);
    view.setSecondComponent(applicationsSourcePresenter.getComponent());
    applicationsSourcePresenter.refresh(widgetUUID);
    refreshLocalized(getCurrentLocale());
  }

  /**
   * Method call when a {@link ShowSettingsEvent} is received
   *
   * @param pEvent
   *          the event received
   */
  @Handler
  public void onUpdateWidgetNameEvent(final UpdateWidgetNameEvent pEvent)
  {
    if ((view.getParent() != null) && (widgetUUID.equals(pEvent.getWidgetUUID())))
    {
      portletWrapper.setHeaderCaption(pEvent.getWidgetName());
      widgetName = pEvent.getWidgetName();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return portletWrapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshContent()
  {
    // Do not handle refresh
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

}
