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
package org.novaforge.forge.ui.portal.event;

import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalModuleId;

/**
 * This event is used to require a refresh on modules. The module will be added to a waiting queue until the
 * next request
 * 
 * @author Guillaume Lamirand
 */
public class ModuleRefreshRequestedEvent implements PortalEvent
{
  private final PortalComponent portalComponent;
  private final String          moduleId;

  /**
   * Default constructor
   * 
   * @param pPortalModuleId
   *          the {@link PortalModuleId} associated to the {@link PortalComponent} given
   * @param pPortalComponent
   *          the {@link PortalComponent}
   */
  public ModuleRefreshRequestedEvent(final String pPortalModuleId, final PortalComponent pPortalComponent)
  {
    super();
    moduleId = pPortalModuleId;
    portalComponent = pPortalComponent;
  }

  /**
   * @return the portalComponent
   */
  public PortalComponent getPortalComponent()
  {
    return portalComponent;
  }

  /**
   * @return the moduleId
   */
  public String getModuleId()
  {
    return moduleId;
  }
}
