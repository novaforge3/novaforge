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
package org.novaforge.forge.article.internal.dao;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.novaforge.forge.article.dao.ArticleCategoryDAO;
import org.novaforge.forge.article.entity.ArticleCategoryContentEntity;
import org.novaforge.forge.article.entity.ArticleCategoryEntity;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.article.model.ArticleCategoryContent;
import org.novaforge.forge.core.organization.model.Language;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

/**
 * JPA2 implementation of {@link ArticleCategoryDAO}
 * 
 * @author Gauthier Cart
 */
public class ArticleCategoryDAOImpl implements ArticleCategoryDAO
{

  /**
   * {@link EntityManager} injected by container
   */
  private EntityManager entityManager;

  /**
   * Use by container to inject {@link EntityManager}
   * 
   * @param pEntityManager
   *          the entityManager to set
   */
  public void setEntityManager(final EntityManager pEntityManager)
  {
    entityManager = pEntityManager;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ArticleCategory newArticleCategory()
  {
    return new ArticleCategoryEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ArticleCategory findByUUID(final UUID pArticleCategoryUUID)
  {
    if (pArticleCategoryUUID != null)
    {
      final TypedQuery<ArticleCategory> q = entityManager.createNamedQuery(
          "ArticleCategoryEntity.findByUUID", ArticleCategory.class);
      q.setParameter("uuid", pArticleCategoryUUID.toString());
      return q.getSingleResult();
    }
    else
    {
      throw new IllegalArgumentException("The given uuid shouldn't be null.");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ArticleCategory> findAll()
  {

    final TypedQuery<ArticleCategory> q = entityManager.createNamedQuery("ArticleCategoryEntity.findAll",
        ArticleCategory.class);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ArticleCategory persist(ArticleCategory pArticleCategory, List<Language> pLanguage)
  {

    // Remove from language list all the language already in the article
    for (ArticleCategoryContent articleCategoryContent : pArticleCategory.getArticleCategoryContents())
    {
      if (pLanguage.contains(articleCategoryContent.getLanguage()))
      {
        pLanguage.remove(articleCategoryContent.getLanguage());
      }
    }

    // Create new article content entity for language not initialized
    for (Language language : pLanguage)
    {
      ArticleCategoryContentEntity articleCategoryContentEntity = new ArticleCategoryContentEntity();
      articleCategoryContentEntity.setLanguage(language);
      pArticleCategory.addArticleCategoryContent(articleCategoryContentEntity);
    }

    // Persist the article
    entityManager.persist(pArticleCategory);
    entityManager.flush();
    return pArticleCategory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ArticleCategory update(ArticleCategory pArticleCategory)
  {
    entityManager.merge(pArticleCategory);
    entityManager.flush();
    final OpenJPAEntityManager oem = OpenJPAPersistence.cast(entityManager);
    oem.evict(pArticleCategory); // will evict from data cache also
    return pArticleCategory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(UUID pUuid)
  {
    final TypedQuery<ArticleCategory> q = entityManager.createNamedQuery("ArticleCategoryEntity.findByUUID",
        ArticleCategory.class);
    q.setParameter("uuid", pUuid.toString());
    final ArticleCategory merged = entityManager.merge(q.getSingleResult());
    entityManager.remove(merged);
    entityManager.flush();
  }

}