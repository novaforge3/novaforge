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
package org.novaforge.forge.ui.applications.internal.client.events;

import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.portal.events.PortalEvent;
import java.util.UUID; 

/**
 * This event is used when a {@link Composition} has been created
 * 
 * @author Guillaume Lamirand
 */
public class LinkApplicationEvent implements PortalEvent
{

  /**
   * The application linked
   */
  private final ProjectApplication projectApplication;
  
  /**
   * Unique identifier of module instance
   */
  private final UUID  moduleUUID;

  /**
   * Default constructor
   * 
   * @param pProjectApplication
   *          the application to link
   * @param pModuleUUID
   *          the module unique identifier
   */
  public LinkApplicationEvent(final ProjectApplication pProjectApplication, final UUID pModuleUUID)
  {
    projectApplication = pProjectApplication;
    moduleUUID = pModuleUUID; 

  }

  /**
   * Return {@link ProjectApplication} linked
   * 
   * @return {@link ProjectApplication} linked
   */
  public ProjectApplication getProjectApplication()
  {
    return projectApplication;
  }

  /**
   * Return the unique identifier of module instance
   * 
   * @return the unique identifier of module instance
   */
  public UUID getUuid()
  {
    return moduleUUID;
  }
}
