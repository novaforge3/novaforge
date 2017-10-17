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
package org.novaforge.forge.dashboard.service;

import net.engio.mbassy.bus.MBassador;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.dashboard.model.WidgetModule;
import org.novaforge.forge.portal.events.PortalEvent;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * This service will handle {@link WidgetModule}
 * 
 * @author Guillaume Lamirand
 */
public interface WidgetModuleService
{

  /**
   * Return {@link WidgetModule} found for the key given, or <code>null</code> if none are found
   * 
   * @param pKey
   *          the module key
   * @return {@link WidgetModule} associated to the key given, or <code>null</code> if none are found
   */
  WidgetModule getModule(final String pKey);

  /**
   * Returns all {@link WidgetModule} available
   * 
   * @return {@link List} of {@link WidgetModule} available
   */
  List<WidgetModule> getModules();

  /**
   * Add a {@link WidgetModuleListener}. After this method the listener given will be notified when a module
   * appear or disapear.
   * 
   * @param pListener
   *          the listener to add
   */
  void addListener(final WidgetModuleListener pListener);

  /**
   * Check if the given {@link WidgetModuleListener} is already registered as a listener.
   * 
   * @param pListener
   *          the listener to check
   * @return <code>true</code> if the {@link WidgetModuleListener} is already registered
   */
  boolean existListener(WidgetModuleListener pListener);

  /**
   * Remove a {@link WidgetModuleListener}. After this method the listener given won't be notified again.
   * 
   * @param pListener
   *          the listener to remove
   */
  void removeListener(final WidgetModuleListener pListener);

  /**
   * Will build a {@link WidgetContext} from parameters. This context can be given to {@link WidgetModule}
   * 
   * @param pEventBus
   *          the evnt bus
   * @param pInitialLocale
   *          the initial Locale
   * @param pUUID
   *          the widget id
   * @return {@link WidgetContext}
   */
  WidgetContext buildContext(final MBassador<PortalEvent> pEventBus, final Locale pInitialLocale,
      final UUID pUUID);

}
