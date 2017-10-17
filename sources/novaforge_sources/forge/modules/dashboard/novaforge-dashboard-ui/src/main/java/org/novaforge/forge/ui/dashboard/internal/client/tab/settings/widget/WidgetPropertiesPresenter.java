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
package org.novaforge.forge.ui.dashboard.internal.client.tab.settings.widget;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import org.novaforge.forge.dashboard.model.Widget;
import org.novaforge.forge.dashboard.model.WidgetAdminComponent;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.dashboard.model.WidgetModule;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.dashboard.internal.client.AbstractDashBoardPresenter;
import org.novaforge.forge.ui.dashboard.internal.client.event.UpdateWidgetNameEvent;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
@SuppressWarnings("deprecation")
public class WidgetPropertiesPresenter extends AbstractDashBoardPresenter implements Serializable
{
  /**
   * Serial version id
   */
  private static final long          serialVersionUID = -3067819403919493755L;
  private final WidgetPropertiesView view;
  private UUID                       widgetUUID;

  private WidgetAdminComponent       currentAdminComponent;

  /**
   * Default constructor
   * 
   * @param pPortalContext
   *          the portalContext used to initialize this module
   * @param pView
   *          the view associated to this presenter
   */
  public WidgetPropertiesPresenter(final WidgetPropertiesView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);

    // Init the view
    view = pView;

    addListener();
  }

  private void addListener()
  {
    view.getSaveButton().addClickListener(new ClickListener()
    {

      /**
       * serial version id
       */
      private static final long serialVersionUID = -2704625930271014680L;

      /**
       * @param pEvent
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {

        try
        {
          final boolean validName = view.getWidgetNameField().isValid();
          final boolean validProperties = currentAdminComponent.isValid();
          if (validName && validProperties)
          {
            final Widget widget = DashboardModule.getDashBoardService().getWidget(widgetUUID);
            final String widgetName = view.getWidgetNameField().getValue();
            if (!widgetName.equals(widget.getName()))
            {
              widget.setName(widgetName);
              getEventBus().publish(new UpdateWidgetNameEvent(widgetUUID, widgetName));
            }
            widget.setProperties(currentAdminComponent.getProperties());
            DashboardModule.getDashBoardService().updateWidget(widget);
          }
          else if (!validName)
          {
            TrayNotification.show(
                DashboardModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.DASHBOARD_SETTINGS_WIDGET_INFO_NAME_VALIDATOR), TrayNotificationType.WARNING);
          }
          else if (!validProperties)
          {
            TrayNotification.show(
                DashboardModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.DASHBOARD_SETTINGS_WIDGET_PROPERTIES_VALIDATOR), TrayNotificationType.WARNING);
          }
        }
        catch (final Exception e)
        {
          ExceptionCodeHandler.showNotificationError(DashboardModule.getPortalMessages(), e,
              getCurrentLocale());
        }

      }
    });
  }

  /**
   * Refresh the associated view according the parameter
   * 
   * @param pWidgetUUID
   *          the widget uuid
   * @param pWidgetName
   *          the widget name
   */
  public void refresh(final UUID pWidgetUUID, final String pWidgetName)
  {
    widgetUUID = pWidgetUUID;
    view.getWidgetNameField().setValue(pWidgetName);

    final Widget widget = DashboardModule.getDashBoardService().getWidget(pWidgetUUID);
    final WidgetModule module = DashboardModule.getWidgetModuleService().getModule(widget.getKey());
    final WidgetContext context = DashboardModule.getWidgetModuleService().buildContext(getEventBus(),
        getCurrentLocale(), pWidgetUUID);
    currentAdminComponent = module.createAdminComponent(context);
    view.getWidgetForm().getLayout().removeAllComponents();
    view.getWidgetForm().getLayout().addComponent(currentAdminComponent.getComponent());
    currentAdminComponent.refresh();
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
    // Do not handle refresh
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

}
