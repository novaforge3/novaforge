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
package org.novaforge.forge.ui.dashboard.internal.client.tab.content.task;

import net.engio.mbassy.bus.MBassador;
import org.novaforge.forge.dashboard.model.Widget;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.dashboard.model.WidgetDataComponent;
import org.novaforge.forge.dashboard.model.WidgetModule;
import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;

import java.util.Locale;

/**
 * Factory used to create specific {@link WidgetDataComponent} according the {@link Widget} and its settings
 * 
 * @author Guillaume Lamirand
 */
public class WidgetComponentFactory
{

  public static WidgetDataComponent createWidgetComponent(final MBassador<PortalEvent> pEventBus,
      final Locale pLocale, final Widget widget)
  {
    return createWidgetComponent(pEventBus, pLocale, widget, true);

  }

  private static WidgetDataComponent createWidgetComponent(final MBassador<PortalEvent> pEventBus,
      final Locale pLocale, final Widget widget, final boolean pIsLoaded)
  {
    WidgetDataComponent widgetComponent = null;
    final WidgetModule module = DashboardModule.getWidgetModuleService().getModule(widget.getKey());
    final WidgetContext context = DashboardModule.getWidgetModuleService().buildContext(pEventBus, pLocale,
        widget.getUUID());

    // If widget is not loaded yet
    if (!pIsLoaded)
    {
      final WidgetModule noSettingsModule = DashboardModule.getWidgetModuleService().getModule(
          WidgetModule.LOADING_KEY);
      widgetComponent = noSettingsModule.createDataComponent(context);
    }
    // If widget has been loaded and settings is valid
    else if ((pIsLoaded) && (module.isValidDataSource(context.getApplicationsByProject()))
        && (module.isValidProperties(context.getProperties())))
    {
      widgetComponent = module.createDataComponent(context);
    }
    // If widget is not loaded yet and setting is invalid
    else
    {
      final WidgetModule noSettingsModule = DashboardModule.getWidgetModuleService().getModule(
          WidgetModule.NO_SETTINGS_KEY);
      widgetComponent = noSettingsModule.createDataComponent(context);
    }

    return widgetComponent;
  }

  public static WidgetDataComponent createLoadingWidgetComponent(final MBassador<PortalEvent> pEventBus,
                                                                 final Locale pLocale, final Widget widget)
  {
    return createWidgetComponent(pEventBus, pLocale, widget, false);
  }

}
