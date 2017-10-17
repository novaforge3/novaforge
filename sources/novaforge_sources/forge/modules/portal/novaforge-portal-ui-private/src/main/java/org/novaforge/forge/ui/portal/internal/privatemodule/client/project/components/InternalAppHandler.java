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
package org.novaforge.forge.ui.portal.internal.privatemodule.client.project.components;

import net.engio.mbassy.bus.MBassador;
import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.portal.event.ApplicationCloseRequestedEvent;
import org.novaforge.forge.ui.portal.event.OpenInternalAppEvent;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.WorkspacePresenter;

import java.io.Serializable;

/**
 * This class handle a {@link OpenInternalAppEvent}.
 * 
 * @author Guillaume Lamirand
 */
public class InternalAppHandler implements Serializable
{

  /**
   * Serial version id for serialization
   */
  private static final long            serialVersionUID = -4433579733692778206L;

  private final MBassador<PortalEvent> eventBus;

  /**
   * Default constructor
   * 
   * @param pEventBus
   *          the event bus
   */
  public InternalAppHandler(final MBassador<PortalEvent> pEventBus)
  {
    eventBus = pEventBus;
  }

  /**
   * This method should be used when a {@link OpenInternalAppEvent} is received from
   * {@link WorkspacePresenter}
   * 
   * @param pEvent
   *          the source event
   */
  public void handleEvent(final OpenInternalAppEvent pEvent)
  {
    if ((pEvent != null) && (pEvent.getApplication() != null))
    {
      final PortalModuleId fromId = PortalModuleId.getFromId(pEvent.getApplication().getId());
      switch (fromId)
      {
        case LOGOUT:
          eventBus.publish(new ApplicationCloseRequestedEvent());
          break;
        default:
          break;
      }
    }
  }

}
