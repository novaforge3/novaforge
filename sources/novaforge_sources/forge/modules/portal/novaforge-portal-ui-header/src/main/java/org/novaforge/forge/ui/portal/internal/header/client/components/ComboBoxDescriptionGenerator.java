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
package org.novaforge.forge.ui.portal.internal.header.client.components;

import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tree;

/**
 * This component will be used by navigation {@link Tree} to generate tool tip for each items.
 * 
 * @author Guillaume Lamirand
 * @see Tree#setItemDescriptionGenerator(ItemDescriptionGenerator)
 */
public class ComboBoxDescriptionGenerator implements ItemDescriptionGenerator
{

  /**
   * Serial version uid for serialization
   */
  private static final long serialVersionUID = 395553539309852145L;

  /**
   * {@inheritDoc}
   */
  @Override
  public String generateDescription(final Component source, final Object itemId, final Object propertyId)
  {
    String descriptionReturned = null;
    if (source instanceof Tree)
    {
      final Tree tree = (Tree) source;
      final Property<?> description = tree.getContainerProperty(itemId,
          ProjectItemProperty.DESCRIPTION.getPropertyId());
      if ((description.getValue() != null) && (!"".equals(description.getValue())))
      {
        descriptionReturned = (String) description.getValue();
      }
      else
      {
        final Property<?> caption = tree.getContainerProperty(itemId, ProjectItemProperty.NAME.getPropertyId());
        descriptionReturned = (String) caption.getValue();

      }
    }
    return descriptionReturned;
  }
}
