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
package org.novaforge.forge.ui.applications.internal.client.associations.components;

import com.vaadin.data.util.HierarchicalContainer;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.ui.applications.internal.client.global.components.ItemProperty;

import java.util.List;
import java.util.UUID;

/**
 * This component describes a specific {@link HierarchicalContainer} used to build association tree table.
 * 
 * @author Guillaume Lamirand
 */
public class NotificationsContainer extends HierarchicalContainer
{

  /**
   * Serial version id for Serialization
   */
  private static final long serialVersionUID = 3439109343778568310L;

  /**
   * Default constructor. It will initialize table item property
   * 
   * @see ItemProperty
   * @see HierarchicalContainer#HierarchicalContainer()
   */
  public NotificationsContainer()
  {
    super();
    addContainerProperty(ItemProperty.SOURCE_NAME.getPropertyId(), String.class, null);
    addContainerProperty(ItemProperty.TARGET_NAME.getPropertyId(), String.class, null);
    addContainerProperty(ItemProperty.COMPOSITION.getPropertyId(), Composition.class, null);

  }

  /**
   * Will initialize table with the given {@link Composition}
   * 
   * @param pCompositions
   *          the compostions
   */
  public void setDatas(final List<Composition> pCompositions)
  {
    if (pCompositions != null)
    {
      for (final Composition composition : pCompositions)
      {
        final UUID uuid = composition.getUUID();
        addItem(uuid);
        getContainerProperty(uuid, ItemProperty.SOURCE_NAME.getPropertyId()).setValue(
            composition.getSourceName());
        getContainerProperty(uuid, ItemProperty.TARGET_NAME.getPropertyId()).setValue(
            composition.getTargetName());
        getContainerProperty(uuid, ItemProperty.COMPOSITION.getPropertyId()).setValue(composition);

      }
    }
  }
}
