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
package org.novaforge.forge.portal.models;

import net.engio.mbassy.bus.MBassador;
import org.novaforge.forge.portal.events.PortalEvent;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * This interface describes a internal application which can be added to portal
 * 
 * @author Guillaume Lamirand
 */
public interface PortalContext
{

  /**
   * Return a global {@link MBassador} event bus
   *
   * @return the eventBus
   */
  MBassador<PortalEvent> getEventBus();

  /**
   * Return the initial locale
   *
   * @return the initial locale
   */
  Locale getInitialLocale();

  /**
   * Return attributes available when the associated {@link PortalModule} is instantiated. The key is from
   * {@link PortalContext.KEY} but just some can be present.
   *
   * @return attributes available
   */
  Map<PortalContext.KEY, String> getAttributes();

  /**
   * Return the unique identifier of the module
   *
   * @return {@link UUID} as unique identifier
   */
  UUID getUuid();

  /**
   * Describes key attributes used for {@link PortalContext#getAttributes()}
   *
   * @author Guillaume Lamirand
   */
  enum KEY
  {
    PROJECTID, APP_ID, TICKET, SMALL, IN_SUBWINDOW, LOGIN, PWD, DESCRIPTION, AUTH_STATUS
  }
}
