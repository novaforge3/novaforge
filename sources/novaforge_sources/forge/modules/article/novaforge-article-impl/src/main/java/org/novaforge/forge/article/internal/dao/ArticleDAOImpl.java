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
import org.novaforge.forge.article.dao.ArticleDAO;
import org.novaforge.forge.article.entity.ArticleCategoryEntity;
import org.novaforge.forge.article.entity.ArticleCategoryEntity_;
import org.novaforge.forge.article.entity.ArticleContentEntity;
import org.novaforge.forge.article.entity.ArticleContentEntity_;
import org.novaforge.forge.article.entity.ArticleEntity;
import org.novaforge.forge.article.entity.ArticleEntity_;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleContent;
import org.novaforge.forge.article.model.ArticleFilter;
import org.novaforge.forge.core.organization.model.Language;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA2 implementation of {@link ArticleDAO}
 * 
 * @author Gauthier Cart
 */
public class ArticleDAOImpl implements ArticleDAO
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
  public Article newArticle()
  {
    return new ArticleEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Article> findFromFilter(ArticleFilter pArticleFilter, Language pLanguage)
  {
    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Article> articleCriteria = builder.createQuery(Article.class);
    final Root<ArticleEntity> article = articleCriteria.from(ArticleEntity.class);
    articleCriteria.select(article);

    List<Predicate> predicates = new ArrayList<>();

    // Add between clause on Dates
    predicates.add(builder.between(article.get(ArticleEntity_.date), pArticleFilter.getBeginDate(),
        pArticleFilter.getEndDate()));

    // Add clause on Category
    if ((pArticleFilter.getArticleCategoryUuids() != null) && (!pArticleFilter.getArticleCategoryUuids().isEmpty()))
    {
      List<String> articleCategoryUuidList = new ArrayList<>();
      for (UUID articleCatergoryUuid : pArticleFilter.getArticleCategoryUuids())
      {
        articleCategoryUuidList.add(articleCatergoryUuid.toString());
      }

      Subquery<ArticleCategoryEntity> subquery = articleCriteria.subquery(ArticleCategoryEntity.class);
      Root<ArticleCategoryEntity> fromCategory = subquery.from(ArticleCategoryEntity.class);
      subquery.select(fromCategory);
      subquery.where(fromCategory.get(ArticleCategoryEntity_.uuid).in(articleCategoryUuidList));

      predicates.add(article.get(ArticleEntity_.category).in(subquery));
    }

    // Add clause on Type
    if (pArticleFilter.getArticleType() != null)
    {
      predicates.add(builder.equal(article.get(ArticleEntity_.type), pArticleFilter.getArticleType()));
    }

    // Add clause on Published
    if (pArticleFilter.getIsPublished() != null)
    {
      predicates.add(builder.equal(article.get(ArticleEntity_.published), pArticleFilter.getIsPublished()));
    }

    // Add clause on Language
    if (pLanguage != null && pArticleFilter.getTextSearch().isEmpty())
    {
      Subquery<ArticleContentEntity> subquery = articleCriteria.subquery(ArticleContentEntity.class);
      Root<ArticleContentEntity> fromContent = subquery.from(ArticleContentEntity.class);
      subquery.select(fromContent);
      subquery.where(builder.equal(fromContent.get(ArticleContentEntity_.language), pLanguage));
      predicates.add(article.get(ArticleEntity_.content).in(subquery));
    }
    // Add clause on Text
    else if (pLanguage != null && !pArticleFilter.getTextSearch().isEmpty())
    {
      Subquery<ArticleContentEntity> subquery = articleCriteria.subquery(ArticleContentEntity.class);
      Root<ArticleContentEntity> fromContent = subquery.from(ArticleContentEntity.class);

      Predicate hasLanguage = builder.equal(fromContent.get(ArticleContentEntity_.language), pLanguage);
      Predicate hasTextInTitle = builder.like(fromContent.get(ArticleContentEntity_.title),
          pArticleFilter.getTextSearch());
      Predicate hasTextInShortText = builder.like(fromContent.get(ArticleContentEntity_.shortText),
          pArticleFilter.getTextSearch());
      Predicate hasTextInText = builder.like(fromContent.get(ArticleContentEntity_.text),
          pArticleFilter.getTextSearch());

      Predicate hasTextSomeWhere = builder.or(hasTextInTitle, hasTextInShortText, hasTextInText);

      subquery.select(fromContent);
      subquery.where(builder.and(hasLanguage, hasTextSomeWhere));
      predicates.add(article.get(ArticleEntity_.content).in(subquery));
    }

    articleCriteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));

    return entityManager.createQuery(articleCriteria).getResultList();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Article findByUUID(final UUID pArticleUUID)
  {
    if (pArticleUUID != null)
    {
      final TypedQuery<Article> q = entityManager.createNamedQuery("ArticleEntity.findByUUID", Article.class);
      // Needed to retrieve also article's contents on this request
      q.setParameter("uuid", pArticleUUID.toString());
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
  public Article persist(Article pArticle, List<Language> pLanguage)
  {

    // Remove from language list all the language already in the article
    for (ArticleContent articleContent : pArticle.getArticleContents())
    {
      if (pLanguage.contains(articleContent.getLanguage()))
      {
        pLanguage.remove(articleContent.getLanguage());
      }
    }

    // Create new article content entity for language not initialized
    for (Language language : pLanguage)
    {
      ArticleContentEntity articleContentEntity = new ArticleContentEntity();
      articleContentEntity.setLanguage(language);
      pArticle.addArticleContent(articleContentEntity);
    }

    // Persist the article
    entityManager.persist(pArticle);
    entityManager.flush();
    return pArticle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Article update(Article pArticle)
  {
    entityManager.merge(pArticle);
    entityManager.flush();
    final OpenJPAEntityManager oem = OpenJPAPersistence.cast(entityManager);
    oem.evict(pArticle); // will evict from data cache also
    return pArticle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final UUID pUuid)
  {
    final TypedQuery<Article> q = entityManager.createNamedQuery("ArticleEntity.findByUUID", Article.class);
    // Needed to retrieve also article's contents on this request
    q.setParameter("uuid", pUuid.toString());
    final Article merged = entityManager.merge(q.getSingleResult());
    entityManager.remove(merged);
    entityManager.flush();
  }
}
