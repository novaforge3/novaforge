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
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;

import java.util.List;
import java.util.UUID;

/**
 * @author Gauthier Cart
 */
public interface ArticleCategoryService
{
  /**
   * This method allows to instanciate an article category
   * 
   * @return ArticleCategory
   * @throws LanguageServiceException
   */
  ArticleCategory newArticleCategory() throws LanguageServiceException;

  /**
   * This method allows to create an article category
   * 
   * @param pArticleCategory
   * @throws ArticleCategoryServiceException
   */
  void createArticleCategory(final ArticleCategory pArticleCategory) throws ArticleCategoryServiceException,
      LanguageServiceException;

  /**
   * This method allows to get an article category from its uuid
   * 
   * @param pUuid
   * @throws ArticleCategoryServiceException
   * @return ArticleCategory
   */
  ArticleCategory getArticleCategory(final UUID pUuid) throws ArticleCategoryServiceException;

  /**
   * This method allows to update an article category
   * 
   * @param pArticleCategoryUuid
   * @throws ArticleCategoryServiceException
   * @return ArticleCategory
   */
  ArticleCategory updateArticleCategory(final ArticleCategory pArticleCategory)
      throws ArticleCategoryServiceException;

  /**
   * This method allows to delete an article category from it uuid
   * 
   * @param pUuid
   * @throws ArticleCategoryServiceException
   */
  void deleteArticleCategory(final UUID pUuid) throws ArticleCategoryServiceException;

  /**
   * This method allows to get all article category for a given language
   * 
   * @throws ArticleCategoryServiceException
   */
  List<ArticleCategory> getAllArticleCategory() throws ArticleCategoryServiceException;
}
