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
package org.novaforge.forge.ui.article.internal.client.component;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.UI;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;

import java.util.List;
import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class CategoryContainer extends IndexedContainer
{

  /**
   * 
   */
  private static final long serialVersionUID = -4731667970639131105L;

  /**
   * Default constructor. It will initialize ArticleCategory item property
   * 
   * @see ArticleCategoryItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public CategoryContainer()
  {
    super();
    addContainerProperty(CategoryItemProperty.ID.getPropertyId(), UUID.class, null);
    addContainerProperty(CategoryItemProperty.TITLE.getPropertyId(), String.class, null);
    addContainerProperty(CategoryItemProperty.CONTENT.getPropertyId(), List.class, null);
    addContainerProperty(CategoryItemProperty.COLOR.getPropertyId(), String.class, null);

    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  /**
   * Add article category into container
   * 
   * @param pArticleCategories
   *          pArticleCategories to add
   */
  public void setCategories(final List<ArticleCategory> pArticleCategories)
  {
    removeAllItems();
    for (final ArticleCategory category : pArticleCategories)
    {
      addItem(category);
      getContainerProperty(category, CategoryItemProperty.ID.getPropertyId()).setValue(category.getUuid());
      String title;
      if (category.getCategoryContent(UI.getCurrent().getLocale()) == null)
      {
        title = category.getCategoryContent(ArticleModule.getDefaultLocale()).getTitle();
      }
      else
      {
        title = category.getCategoryContent(UI.getCurrent().getLocale()).getTitle();
      }
      getContainerProperty(category, CategoryItemProperty.TITLE.getPropertyId()).setValue(title);
      getContainerProperty(category, CategoryItemProperty.CONTENT.getPropertyId()).setValue(
          category.getArticleCategoryContents());
      getContainerProperty(category, CategoryItemProperty.COLOR.getPropertyId())
          .setValue(category.getColor());

    }
    sort(new Object[] { CategoryItemProperty.ID.getPropertyId() }, new boolean[] { false });
  }

}
