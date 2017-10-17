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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components;

import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;

import java.util.List;
import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class CategoryContainer extends IndexedContainer
{

  /**
    * 
    */
  private static final long serialVersionUID = -9093186772100588462L;

  /**
   * Default constructor. It will initialize Category item property
   * 
   * @see CategoryItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public CategoryContainer()
  {
    super();
    addContainerProperty(CategoryItemProperty.ID.getPropertyId(), String.class, null);
    addContainerProperty(CategoryItemProperty.LABEL.getPropertyId(), String.class, null);
  }

  /**
   * Add categories into container
   * 
   * @param pCategories
   *          categories to add
   * @param pLocale
   *          user's locale
   */
  public void setCategories(final List<CategoryDefinitionService> pCategories, final Locale pLocale)
  {
    removeAllItems();
    for (final CategoryDefinitionService category : pCategories)
    {
      final String categoryID = category.getCategory().name();
      addItem(categoryID);
      getContainerProperty(categoryID, CategoryItemProperty.ID.getPropertyId()).setValue(categoryID);
      getContainerProperty(categoryID, CategoryItemProperty.LABEL.getPropertyId()).setValue(
          category.getName(pLocale));
    }
    sort(new Object[] { CategoryItemProperty.LABEL.getPropertyId() }, new boolean[] { true });
  }

}
