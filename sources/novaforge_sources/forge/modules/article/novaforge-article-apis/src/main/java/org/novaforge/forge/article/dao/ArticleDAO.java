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
package org.novaforge.forge.article.dao;

import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleFilter;
import org.novaforge.forge.core.organization.model.Language;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.UUID;

/**
 * This class defines methods to access to {@link Article} data from persistence
 * 
 * @author Gauthier Cart
 * @see Article
 */
public interface ArticleDAO
{
  /**
   * This method will return a new entity detach of persistence context
   * 
   * @return new {@link Article}
   */
  Article newArticle();

  /**
   * Find published {@link Article} list according to the filter
   * 
   * @param pType
   *          the type used to retrieve articles
   * @param pLanguage
   *          the language used to retrieve articles
   * @param pArticleFilter
   *          the filter used to retrieve articles
   * @return {@link List} of {@link Article}
   */
  List<Article> findFromFilter(final ArticleFilter pArticleFilter, Language pLanguage);

  /**
   * Find a {@link Article} by its uuid
   * 
   * @param pArticleUUID
   *          the uuid used to seek article
   * @return {@link Article} found
   * @throws NoResultException
   *           thrown if no {@link Article} are existing for the uuid given
   */
  Article findByUUID(final UUID pArticleUUID);

  /**
   * Will persist the {@link Article} given from persistence context
   * 
   * @param pArticle
   *          the article to persist
   * @param pLanguages
   *          the languages to persist the article in
   * @return {@link Article} persist and attach to persistence context
   */
  Article persist(final Article pArticle, final List<Language> pLanguages);

  /**
   * Will update the {@link Article} given into persistence context
   * 
   * @param pArticle
   *          the article to persist
   * @return {@link Article} updated and attached to persistence context
   */
  Article update(final Article pArticle);

  /**
   * Will delete the {@link Article} given from persistence context
   * 
   * @param pUuid
   *          the uuid of the article to delete
   */
  void delete(final UUID pUuid);

}
