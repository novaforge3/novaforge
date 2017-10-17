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
package org.novaforge.forge.ui.applications.internal.client.associations.models;

import com.vaadin.ui.TreeTable;
import org.novaforge.forge.core.organization.model.CompositionType;
import org.novaforge.forge.core.plugins.categories.Association;

import java.util.UUID;

/**
 * Object which is used to map two compatible association in {@link TreeTable} container
 * 
 * @author Guillaume Lamirand
 */
public class CompatibleComposition
{

  private final UUID            instanceUUID;
  private final CompositionType compositionType;
  private final Association     source;
  private final Association     target;

  /**
   * Default constructor.
   * 
   * @param pInstanceUUId
   *          the composition id
   * @param pCompositionType
   *          the composition type
   * @param pSource
   *          the source {@link Association}
   * @param pTarget
   *          the target {@link Association}
   */
  public CompatibleComposition(final UUID pInstanceUUId, final CompositionType pCompositionType,
      final Association pSource, final Association pTarget)
  {
    instanceUUID = pInstanceUUId;
    compositionType = pCompositionType;
    source = pSource;
    target = pTarget;
  }

  /**
   * @return composition type
   */
  public CompositionType getCompositionType()
  {
    return compositionType;
  }

  /**
   * @return source {@link Association}
   */
  public Association getSource()
  {
    return source;
  }

  /**
   * @return target {@link Association}
   */
  public Association getTarget()
  {
    return target;
  }

  /**
   * @return the instanceUUID
   */
  public UUID getInstanceUUID()
  {
    return instanceUUID;
  }
}
