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
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.article.model.ArticleType;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class ArticleContainer extends IndexedContainer
{

  /**
   * 
   */
  private static final long serialVersionUID = -4731667970639131105L;

  /**
   * Default constructor. It will initialize article item property
   * 
   * @see ArticleItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public ArticleContainer()
  {
    super();
    addContainerProperty(ArticleItemProperty.ID.getPropertyId(), UUID.class, null);
    addContainerProperty(ArticleItemProperty.CONTENT.getPropertyId(), List.class, null);
    addContainerProperty(ArticleItemProperty.DATE.getPropertyId(), Date.class, null);
    addContainerProperty(ArticleItemProperty.CATEGORY.getPropertyId(), ArticleCategory.class, null);
    addContainerProperty(ArticleItemProperty.TYPE.getPropertyId(), ArticleType.class, null);
    addContainerProperty(ArticleItemProperty.PUBLISHED.getPropertyId(), Boolean.class, null);

    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  /**
   * Add article into container
   * 
   * @param pArticles
   *          Articles to add
   */
  public void setArticle(final List<Article> pArticles)
  {
    removeAllItems();
    for (final Article article : pArticles)
    {
      final UUID itemID = article.getUuid();
      addItem(itemID);
      getContainerProperty(itemID, ArticleItemProperty.ID.getPropertyId()).setValue(itemID);
      getContainerProperty(itemID, ArticleItemProperty.CONTENT.getPropertyId()).setValue(
          article.getArticleContents());
      getContainerProperty(itemID, ArticleItemProperty.DATE.getPropertyId()).setValue(article.getDate());
      getContainerProperty(itemID, ArticleItemProperty.CATEGORY.getPropertyId()).setValue(
          article.getArticleCategory());
      getContainerProperty(itemID, ArticleItemProperty.TYPE.getPropertyId()).setValue(article.getType());
      getContainerProperty(itemID, ArticleItemProperty.PUBLISHED.getPropertyId()).setValue(
          article.isPublished());
    }

    sort(new Object[] { ArticleItemProperty.DATE.getPropertyId() }, new boolean[] { false });
  }

}
