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
package org.novaforge.forge.ui.dashboard.internal.client.container;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

import java.util.List;

/**
 * @author caseryj
 */
public class WidgetCategoryFilter implements Container.Filter
{

  /**
   * 
   */
  private static final long serialVersionUID = 4898301088295619704L;
  protected String          value;

  public WidgetCategoryFilter(final String pValue)
  {
    value = pValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean passesFilter(final Object itemId, final Item item) throws UnsupportedOperationException
  {
    // Acquire the relevant property from the item object
    final Property p = item.getItemProperty(WidgetModuleItemProperty.CATEGORY.getPropertyId());

    // Should always check validity
    if ((p == null) || !p.getType().equals(List.class))
    {
      return false;
    }
    final List<String> categories = (List<String>) p.getValue();

    if (value == null)
    {
      return ((categories == null) || categories.isEmpty());
    }
    else
    {
      return categories.contains(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean appliesToProperty(final Object propertyId)
  {
    return (propertyId != null) && propertyId.equals(WidgetModuleItemProperty.CATEGORY.getPropertyId());
  }

}
