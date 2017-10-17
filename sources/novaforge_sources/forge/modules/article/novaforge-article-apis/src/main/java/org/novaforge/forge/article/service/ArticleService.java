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
package org.novaforge.forge.article.service;

import org.novaforge.forge.article.exception.ArticleCategoryServiceException;
import org.novaforge.forge.article.exception.ArticleServiceException;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleFilter;
import org.novaforge.forge.article.model.ArticleType;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;

import java.util.List;
import java.util.UUID;

/**
 * @author Gauthier Cart
 */
public interface ArticleService
{
  /**
   * This method allows to instantiate an article
   * 
   * @return Article
   * @throws LanguageServiceException
   */
  Article newArticle(ArticleType pType) throws LanguageServiceException;

  /**
   * This method allows to create an article
   * 
   * @param pArticle
   * @throws ArticleServiceException
   * @return Article
   */
  Article createArticle(final Article pArticle) throws ArticleServiceException, LanguageServiceException;

  /**
   * This method allows to update an article
   * 
   * @param pArticle
   * @throws ArticleServiceException
   * @return Article
   */
  Article updateArticle(final Article pArticle) throws ArticleServiceException;

  /**
   * This method allows to get an article from a given uuid
   * 
   * @param pUuid
   * @throws ArticleServiceException
   * @return Article
   */
  Article getArticle(final UUID pUuid) throws ArticleServiceException;

  /**
   * This method allows to delete an article by its uuid
   * 
   * @param pUuid
   * @throws ArticleServiceException
   */
  void deleteArticle(final UUID pUuid) throws ArticleServiceException;

  /**
   * This method returns all the published articles matching a filter
   * 
   * @param pArticleFilter
   *          the options used to retrieve article objects
   * @return {@link List} of {@link Article}
   * @throws ArticleServiceException
   * @throws LanguageServiceException
   */
  List<Article> getArticlesFromFilter(final ArticleFilter pArticleFilter) throws ArticleServiceException,
      LanguageServiceException;

  /**
   * This method returns the information article
   * 
   * @return an {@link Article}
   */
  Article getInformation() throws ArticleCategoryServiceException, LanguageServiceException;

  /**
   * This method returns a new newArticleFilter
   * 
   * @return a new {@link ArticleFilter}
   */
  ArticleFilter newArticleFilter() throws ArticleCategoryServiceException;

}
