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
import org.novaforge.forge.dashboard.model.WidgetDataComponent;
import org.novaforge.forge.portal.events.PortalEvent;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @author Guillaume Lamirand
 */
public class WidgetRefreshCallable implements Callable<WidgetRefreshCallable.Result>
{
  /**
   * The source {@link Widget} used for this task
   */
  private final Widget                 widget;
  /**
   * The {@link MBassador} event bus shared between dashboard and widget
   */
  private final MBassador<PortalEvent> eventBus;
  /**
   * The {@link Locale} defined by dashboad
   */
  private final Locale                 locale;
  /**
   * Default constructor
   *
   * @param pWidget
   *          the source {@link Widget} used for this task
   * @param pEventBus
   *          the event bus shared between dashboard and widget
   * @param pLocale
   *          the current user locale
   */
  public WidgetRefreshCallable(final Widget pWidget, final MBassador<PortalEvent> pEventBus,
      final Locale pLocale)
  {
    widget = pWidget;
    eventBus = pEventBus;
    locale = pLocale;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WidgetRefreshCallable.Result call() throws Exception
  {
    final WidgetDataComponent createWidgetComponent = WidgetComponentFactory.createWidgetComponent(eventBus,
        locale, widget);
    createWidgetComponent.refreshData();

    return new Result(widget.getUUID(), createWidgetComponent);
  }

  /**
   * This class defines the value returns by {@link WidgetRefreshCallable} when task is done
   *
   * @author Guillaume Lamirand
   */
  public class Result
  {
    /**
     * the Widget's uuid
     */
    private final UUID                uuid;
    /**
     * the {@link WidgetDataComponent}
     */
    private final WidgetDataComponent widgetDataComponent;

    public Result(final UUID uuid, final WidgetDataComponent widgetDataComponent)
    {
      this.uuid = uuid;
      this.widgetDataComponent = widgetDataComponent;
    }

    /**
     * Retrieve widget uuid
     *
     * @return {@link UUID} defining widget concerned
     */
    public UUID getUuid()
    {
      return uuid;
    }

    /**
     * Retrieve the {@link WidgetDataComponent} built during the task
     *
     * @return the {@link WidgetDataComponent} created during the task
     */
    public WidgetDataComponent getWidgetDataComponent()
    {
      return widgetDataComponent;
    }

  }
}