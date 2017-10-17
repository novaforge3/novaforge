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

import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.portal.events.PortalEvent;

/**
 * This event is used when a {@link Project} is validated. It is used to refresh
 * portal project information.
 * 
 * @author Guillaume Lamirand
 */
public class ProjectValidatedEvent implements PortalEvent
{
  /**
   * Contains the project
   */
  private final String projectId;

  /**
   * Default constructor.
   * 
   * @param pProjectId
   *          the project which has been updated
   */
  public ProjectValidatedEvent(final String pProjectId)
  {
    projectId = pProjectId;
  }

  /**
   * Return the project associated to this Event
   * 
   * @return the {@link Project}
   */
  public String getProjectId()
  {
    return projectId;
  }

}
