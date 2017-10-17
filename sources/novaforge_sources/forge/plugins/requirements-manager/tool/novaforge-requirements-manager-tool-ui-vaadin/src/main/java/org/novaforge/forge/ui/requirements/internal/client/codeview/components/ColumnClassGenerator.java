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
package org.novaforge.forge.ui.requirements.internal.client.codeview.components;

import com.vaadin.data.Item;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.ui.requirements.internal.client.containers.CodeViewItemProperty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Gauthier Cart
 */
public class ColumnClassGenerator implements ColumnGenerator
{
  /** Serial version id */
  private static final long serialVersionUID = 4124898342363622031L;

  /** {@inheritDoc} */
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    VerticalLayout layout = new VerticalLayout();
    if (pSource.getParent() != null)
    {
      final Item item = pSource.getItem(pItemId);
      final List<String> resourcesName = (List<String>) item.getItemProperty(
          CodeViewItemProperty.CLASS.getPropertyId()).getValue();

      if (resourcesName != null && !resourcesName.isEmpty())
      {
        Set<String> resourcesNameSet = new HashSet<String>(resourcesName);
        for (String resourceName : resourcesNameSet)
        {
          layout.addComponent(new Label(resourceName));
        }
      }
    }
    return layout;
  }
}
