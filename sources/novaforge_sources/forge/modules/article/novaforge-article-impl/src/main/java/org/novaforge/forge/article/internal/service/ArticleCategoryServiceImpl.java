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
package org.novaforge.forge.article.internal.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.article.dao.ArticleCategoryDAO;
import org.novaforge.forge.article.dao.ArticleDAO;
import org.novaforge.forge.article.entity.ArticleCategoryContentEntity;
import org.novaforge.forge.article.entity.ArticleCategoryEntity;
import org.novaforge.forge.article.exception.ArticleCategoryServiceException;
import org.novaforge.forge.article.internal.model.ArticleFilterImpl;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.article.model.ArticleFilter;
import org.novaforge.forge.article.service.ArticleCategoryService;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.services.LanguageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This is an implementation of {@link ArticleCategoryService}
 * 
 * @author Gauthier Cart
 * @see ArticleCategoryService
 */
public class ArticleCategoryServiceImpl implements ArticleCategoryService
{

  private static final Log LOG = LogFactory.getLog(ArticleCategoryServiceImpl.class);
  /**
   * Reference to {@link ArticleCategoryDAO} service injected by the container
   */
  private ArticleCategoryDAO articleCategoryDAO;

  /**
   * Reference to {@link ArticleDAO} service injected by the container
   */
  private ArticleDAO articleDAO;

  /**
   * Reference to {@link LanguageService} service injected by the container
   */
  private LanguageService    languageService;

  /**
   * {@inheritDoc}
   */
  @Override
  public ArticleCategory newArticleCategory() throws LanguageServiceException
  {
    ArticleCategory articleCategory = new ArticleCategoryEntity();
    for (Language language : languageService.getAllLanguages())
    {
      ArticleCategoryContentEntity articleCategoryContentEntity = new ArticleCategoryContentEntity();
      articleCategoryContentEntity.setLanguage(language);
      articleCategory.addArticleCategoryContent(articleCategoryContentEntity);
    }
    return articleCategory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createArticleCategory(ArticleCategory pArticleCategory) throws ArticleCategoryServiceException,
      LanguageServiceException
  {
    articleCategoryDAO.persist(pArticleCategory, languageService.getAllLanguages());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ArticleCategory getArticleCategory(UUID pUuid) throws ArticleCategoryServiceException
  {
    return articleCategoryDAO.findByUUID(pUuid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ArticleCategory updateArticleCategory(ArticleCategory pArticleCategory)
      throws ArticleCategoryServiceException
  {
    return articleCategoryDAO.update(pArticleCategory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteArticleCategory(UUID pUuid) throws ArticleCategoryServiceException
  {

    // Create a new filter with the category uuid
    ArticleFilter filter = new ArticleFilterImpl();
    List<UUID> uuidList= new ArrayList();
    uuidList.add(pUuid);
    filter.setArticleCategoryUuids(uuidList);

    // Get all the article linked to the category
    List<Article> articleList = articleDAO.findFromFilter(filter, languageService.getDefault());

    // Remove the category for each article
    for (Article article : articleList) {
      article.setArticleCategory(null);
    }

    articleCategoryDAO.delete(pUuid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ArticleCategory> getAllArticleCategory() throws ArticleCategoryServiceException
  {

    return articleCategoryDAO.findAll();
  }

  /**
   * Use by container to inject {@link ArticleCategoryDAO}
   * 
   * @param articleCategoryDAO
   *          the articleCategoryDAO to set
   */
  public void setArticleCategoryDAO(final ArticleCategoryDAO articleCategoryDAO)
  {
    this.articleCategoryDAO = articleCategoryDAO;
  }

  /**
   * Use by container to inject {@link ArticleDAO}
   *
   * @param articleDAO
   *          the articleDAO to set
   */
  public void setArticleDAO(final ArticleDAO articleDAO)
  {
    this.articleDAO = articleDAO;
  }

  /**
   * Use by container to inject {@link LanguageService}
   * 
   * @param languageService
   *          the LanguageService to set
   */
  public void setLanguageService(final LanguageService languageService)
  {
    this.languageService = languageService;
  }

}