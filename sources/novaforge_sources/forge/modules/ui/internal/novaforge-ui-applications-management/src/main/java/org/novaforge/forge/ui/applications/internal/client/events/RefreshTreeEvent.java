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

import org.novaforge.forge.core.organization.model.Node;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.portal.events.PortalEvent;
import java.util.UUID;

/**
 * Used to refresh the project left tree
 * 
 * @author Guillaume Lamirand
 */
public class RefreshTreeEvent implements PortalEvent
{

  /**
   * {@link Node} to select
   */
  private final Node node;
  
  /**
   * Unique identifier of module instance
   */
  private final UUID  moduleUUID;

  /**
   * Default contructor
   * 
   * @param pNode
   *          the node to select
   * @param pModuleUUID
   *          the module unique identifier
   */
  public RefreshTreeEvent(final Node pNode, final UUID pModuleUUID)
  {
    node = pNode;
    moduleUUID = pModuleUUID;
  }

  /**
   * @return the {@link Node} can be {@link Space} or {@link ProjectApplication}
   */
  public Node getNode()
  {
    return node;
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
