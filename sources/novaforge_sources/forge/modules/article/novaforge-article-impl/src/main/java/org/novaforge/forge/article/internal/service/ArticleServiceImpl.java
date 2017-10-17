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
import org.novaforge.forge.article.dao.ArticleDAO;
import org.novaforge.forge.article.entity.ArticleContentEntity;
import org.novaforge.forge.article.exception.ArticleCategoryServiceException;
import org.novaforge.forge.article.exception.ArticleServiceException;
import org.novaforge.forge.article.internal.model.ArticleFilterImpl;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleFilter;
import org.novaforge.forge.article.model.ArticleType;
import org.novaforge.forge.article.service.ArticleService;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.services.LanguageService;

import java.util.List;
import java.util.UUID;

/**
 * This is an implementation of {@link ArticleService}
 * 
 * @author Gauthier Cart
 * @see ArticleService
 */
public class ArticleServiceImpl implements ArticleService
{

  private static final Log LOG = LogFactory.getLog(ArticleServiceImpl.class);
  /**
   * Reference to {@link ArticleDAO} service injected by the container
   */
  private ArticleDAO       articleDAO;
  /**
   * Reference to {@link LanguageService} service injected by the container
   */
  private LanguageService  languageService;

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

  /**
   * {@inheritDoc}
   */
  @Override
  public Article newArticle(ArticleType pType) throws LanguageServiceException
  {
    Article article = articleDAO.newArticle();
    article.setType(pType);
    // Create new article content entity for language not initialized
    for (Language language : languageService.getAllLanguages())
    {
      ArticleContentEntity articleContentEntity = new ArticleContentEntity();
      articleContentEntity.setLanguage(language);
      article.addArticleContent(articleContentEntity);
    }

    return article;
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public Article updateArticle(Article pArticle) throws ArticleServiceException
  {
    return articleDAO.update(pArticle);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Article getArticle(UUID pUuid) throws ArticleServiceException
  {
    return articleDAO.findByUUID(pUuid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteArticle(UUID pUuid) throws ArticleServiceException
  {
    articleDAO.delete(pUuid);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws LanguageServiceException
   */
  @Override
  public List<Article> getArticlesFromFilter(ArticleFilter pArticleFilter) throws ArticleServiceException,
      LanguageServiceException
  {

    Language language = languageService.getLanguageFromLocale(pArticleFilter.getLocale());

    List<Article> publishedArticlePersistent = articleDAO.findFromFilter(pArticleFilter, language);

    return publishedArticlePersistent;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws LanguageServiceException
   */
  @Override
  public Article createArticle(Article pArticle) throws ArticleServiceException, LanguageServiceException
  {
    return articleDAO.persist(pArticle, languageService.getAllLanguages());
  }

  /**
   * This method returns the information article
   * 
   * @return an {@link Article}
   * @throws ArticleCategoryServiceException
   * @throws LanguageServiceException
   */
  public Article getInformation() throws ArticleCategoryServiceException, LanguageServiceException
  {
    Language language = languageService.getDefault();

    ArticleFilter pArticleFilter = new ArticleFilterImpl();
    pArticleFilter.setArticleType(ArticleType.INFORMATION);
    List<Article> articlePersistent = articleDAO.findFromFilter(pArticleFilter, language);

    Article article;

    if (articlePersistent.isEmpty())
    {
      article = newArticle(ArticleType.INFORMATION);
      article.setPublished(true);
    }
    else
    {
      article = articlePersistent.get(0);
    }

    return article;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws ArticleCategoryServiceException
   */
  @Override
  public ArticleFilter newArticleFilter() throws ArticleCategoryServiceException
  {
    return new ArticleFilterImpl();
  }

}
