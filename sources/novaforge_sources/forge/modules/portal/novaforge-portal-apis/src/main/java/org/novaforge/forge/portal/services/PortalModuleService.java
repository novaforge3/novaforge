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
package org.novaforge.forge.portal.services;

import net.engio.mbassy.bus.MBassador;
import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This service will handle
 * 
 * @author Guillaume Lamirand
 */
public interface PortalModuleService
{

  /**
   * Return {@link PortalModule} found for the id given, or <code>null</code> if none are found
   * 
   * @param pModuleId
   *          the module id
   * @return {@link PortalModule} associated to the id given, or <code>null</code> if none are found
   */
  PortalModule getModule(final String pModuleId);

  /**
   * Returns all {@link PortalModule} available
   * 
   * @return {@link List} of {@link PortalModule} available
   */
  List<PortalModule> getModules();

  /**
   * Add a {@link PortalModuleListener}. After this method the listener given will be notified when a module
   * appear or disapear.
   * 
   * @param pListener
   *          the listener to add
   */
  void addListener(final PortalModuleListener pListener);

  /**
   * Check if the given {@link PortalModuleListener} is already registered as a listener.
   * 
   * @param pListener
   *          the listener to check
   * @return <code>true</code> if the {@link PortalModuleListener} is already registered
   */
  boolean existListener(PortalModuleListener pListener);

  /**
   * Remove a {@link PortalModuleListener}. After this method the listener given won't be notified again.
   * 
   * @param pListener
   *          the listener to remove
   */
  void removeListener(final PortalModuleListener pListener);

  /**
   * Will build a {@link PortalContext} from parameters. This context can be given to {@link PortalModule}
   * 
   * @param pEventBus
   *          the event bus
   * @param pInitialLocale
   *          the initial Locale
   * @param pAttributes
   *          the attributes
   * @return {@link PortalContext}
   */
  PortalContext buildContext(final MBassador<PortalEvent> pEventBus, final Locale pInitialLocale,
      final Map<PortalContext.KEY, String> pAttributes);

}
