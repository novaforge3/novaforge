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
package org.novaforge.forge.article.model;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * This is the behavior of an article into the forge
 * 
 * @author Jeremy Casery
 * @author Gauthier Cart
 */
public interface Article
{

  /**
   * @return
   */
  UUID getUuid();

  /**
   * @return
   */
  List<ArticleContent> getArticleContents();

  /**
   * @param pArticleContent
   */
  void addArticleContent(ArticleContent pArticleContent);

  /**
   * @param pArticleContent
   */
  void removeArticleContent(ArticleContent pArticleContent);

  /**
   * @param pLocale
   * @return
   */
  ArticleContent getArticleContent(Locale pLocale);

  /**
   * @return
   */
  Date getDate();

  /**
   * @param pDate
   */
  void setDate(Date pDate);

  /**
   * @return
   */
  boolean isPublished();

  /**
   * @param published
   */
  void setPublished(boolean pPublished);

  /**
   * @return
   */
  ArticleCategory getArticleCategory();

  /**
   * @param category
   */
  void setArticleCategory(ArticleCategory pArticleCategory);

  /**
   * @return
   */
  ArticleType getType();

  /**
   * @param type
   */
  void setType(ArticleType pType);

}
