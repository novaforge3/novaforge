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

import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.core.organization.model.Language;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.UUID;

/**
 * This class defines methods to access to {@link ArticleCategory} data from persistence
 * 
 * @author Gauthier Cart
 * @see ArticleCategory
 */
public interface ArticleCategoryDAO
{

  /**
   * This method will return a new entity detach of persistence context
   * 
   * @return new {@link ArticleCategory}
   */
  ArticleCategory newArticleCategory();

  /**
   * Find a {@link ArticleCategory} by its uuid
   * 
   * @param pArticleCategoryUUID
   *          the uuid used to seek article category
   * @return {@link ArticleCategory} found
   * @throws NoResultException
   *           thrown if no {@link ArticleCategory} are existing for the uuid given
   */
  ArticleCategory findByUUID(final UUID pArticleCategoryUUID);

  /**
   * This method will find all {@link ArticleCategory}
   */
  List<ArticleCategory> findAll();

  /**
   * Will persist the {@link ArticleCategory} given from persistence context
   * 
   * @param pArticleCategory
   *          the category to persist
   * @param pLanguage
   *          the list of language
   * @return {@link ArticleCategory} persist and attach to persistence context
   */
  ArticleCategory persist(ArticleCategory pArticleCategory, List<Language> pLanguage);

  /**
   * Will update the {@link ArticleCategory} given into persistence context
   * 
   * @param pArticleCategory
   *          the category to persist
   * @return {@link ArticleCategory} updated and attached to persistence context
   */
  ArticleCategory update(final ArticleCategory pArticleCategory);

  /**
   * Will delete the {@link ArticleCategory} given from persistence context
   * 
   * @param pUuid
   *          the UUID of the category to delete
   */
  void delete(final UUID pUuid);

}
