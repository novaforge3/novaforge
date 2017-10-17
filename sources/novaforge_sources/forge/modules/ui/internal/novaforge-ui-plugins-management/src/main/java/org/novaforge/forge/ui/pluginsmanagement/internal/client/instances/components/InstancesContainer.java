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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components;

import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstanceStatus;

import java.net.URL;
import java.util.Set;
import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class InstancesContainer extends IndexedContainer
{

  /**
   * SerialUID
   */
  private static final long serialVersionUID = 8936712840151111292L;

  /**
   * The default constructor, define the container properties
   */
  public InstancesContainer()
  {
    super();
    addContainerProperty(InstancesItemProperty.NAME.getPropertyId(), String.class, null);
    addContainerProperty(InstancesItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(InstancesItemProperty.ALIAS.getPropertyId(), String.class, null);
    addContainerProperty(InstancesItemProperty.BASEURL.getPropertyId(), URL.class, null);
    addContainerProperty(InstancesItemProperty.STATUS.getPropertyId(), ToolInstanceStatus.class, null);
    addContainerProperty(InstancesItemProperty.SHAREABLE.getPropertyId(), Boolean.class, null);
  }

  /**
   * Add instances into container
   * 
   * @param pInstances
   *          instances to add
   */
  public void setInstances(final Set<ToolInstance> pInstances)
  {
    removeAllItems();
    for (final ToolInstance instance : pInstances)
    {
      final UUID uuid = instance.getUUID();
      addItem(uuid);
      getContainerProperty(uuid, InstancesItemProperty.NAME.getPropertyId()).setValue(instance.getName());
      getContainerProperty(uuid, InstancesItemProperty.DESCRIPTION.getPropertyId()).setValue(
          instance.getDescription());
      getContainerProperty(uuid, InstancesItemProperty.ALIAS.getPropertyId()).setValue(instance.getAlias());
      getContainerProperty(uuid, InstancesItemProperty.BASEURL.getPropertyId()).setValue(
          instance.getBaseURL());
      getContainerProperty(uuid, InstancesItemProperty.STATUS.getPropertyId()).setValue(
          instance.getToolInstanceStatus());
      getContainerProperty(uuid, InstancesItemProperty.SHAREABLE.getPropertyId()).setValue(
          instance.isShareable());
    }
    sort(new Object[] { InstancesItemProperty.NAME.getPropertyId() }, new boolean[] { true });
  }
}
