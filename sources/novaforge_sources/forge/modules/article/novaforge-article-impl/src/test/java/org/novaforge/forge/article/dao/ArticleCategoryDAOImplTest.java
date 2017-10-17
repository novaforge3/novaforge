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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.article.entity.ArticleCategoryContentEntity;
import org.novaforge.forge.article.entity.ArticleCategoryEntity;
import org.novaforge.forge.article.internal.dao.ArticleCategoryDAOImpl;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.article.model.ArticleCategoryContent;
import org.novaforge.forge.core.organization.entity.LanguageEntity;
import org.novaforge.forge.core.organization.model.Language;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Gauthier Cart
 */
public class ArticleCategoryDAOImplTest extends ArticleJPATestCase
{

  private static final String    LANGUAGE_NAME_FR = "FR";
  private static final String    LANGUAGE_NAME_EN = "EN";
  private static final String    NEWS_TITLE_FR    = "Actualité";
  private static final String    NEWS_TITLE_EN    = "News";
  private static final String    NEWS_COLOR       = "#0066A1";
  private static final String    RELEASE_TITLE_FR = "Release Note";
  private static final String    RELEASE_TITLE_EN = "Release Note";
  private static final String    RELEASE_COLOR    = "#FFFFFF";
  /*
   * Constants declaration
   */
  private ArticleCategoryDAOImpl articleCategoryDAOImpl;

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    articleCategoryDAOImpl = new ArticleCategoryDAOImpl();
    articleCategoryDAOImpl.setEntityManager(em);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    articleCategoryDAOImpl = null;
  }

  /**
   * Test method for {@link org.novaforge.forge.article.internal.dao.ArticleCategoryDAOImpl#findAll()}.
   */
  @Test
  public void testFindAll()
  {

    final LanguageEntity french       = buildLanguageEntity(LANGUAGE_NAME_FR);
    final LanguageEntity english      = buildLanguageEntity(LANGUAGE_NAME_EN);
    List<Language>       languageList = new ArrayList<Language>();
    languageList.add(french);
    languageList.add(english);

    final ArticleCategoryContent newsContentEN = buildArticleCategoryContentEntity(NEWS_TITLE_EN, english);
    final ArticleCategoryContent newsContentFR = buildArticleCategoryContentEntity(NEWS_TITLE_FR, french);
    final ArticleCategoryContent releaseContentEN = buildArticleCategoryContentEntity(RELEASE_TITLE_EN, english);
    final ArticleCategoryContent releaseContentFR = buildArticleCategoryContentEntity(RELEASE_TITLE_FR, french);

    List<ArticleCategoryContent> newsContentList = new ArrayList<ArticleCategoryContent>();
    newsContentList.add(newsContentEN);
    newsContentList.add(newsContentFR);

    List<ArticleCategoryContent> releaseContentList = new ArrayList<ArticleCategoryContent>();
    newsContentList.add(releaseContentEN);
    newsContentList.add(releaseContentFR);

    final ArticleCategory news    = buildArticleCategoryEntity(NEWS_COLOR, newsContentList);
    final ArticleCategory release = buildArticleCategoryEntity(RELEASE_COLOR, releaseContentList);

    em.getTransaction().begin();
    articleCategoryDAOImpl.persist(news, languageList);
    articleCategoryDAOImpl.persist(release, languageList);
    em.getTransaction().commit();
    final List<ArticleCategory> list = articleCategoryDAOImpl.findAll();
    assertNotNull(list);
    assertThat(list.size(), is(2));
  }

  private LanguageEntity buildLanguageEntity(final String pLanguageName)
  {
    LanguageEntity language = new LanguageEntity();

    final TypedQuery<LanguageEntity> query = em.createQuery(
        "SELECT l FROM LanguageEntity l WHERE l.name = :name", LanguageEntity.class);
    query.setParameter("name", pLanguageName);
    final List<LanguageEntity> resultList = query.getResultList();

    if ((resultList == null) || (resultList.isEmpty()))
    {
      if (!em.getTransaction().isActive())
      {
        em.getTransaction().begin();
      }
      language = new LanguageEntity();
      language.setName(pLanguageName);
      em.persist(language);
      em.getTransaction().commit();
    }
    else
    {
      language = resultList.get(0);
    }

    return language;
  }

  private ArticleCategoryContentEntity buildArticleCategoryContentEntity(final String pTitle,
      final LanguageEntity pLanguage)
  {
    ArticleCategoryContentEntity articleCategoryContent = new ArticleCategoryContentEntity();
    articleCategoryContent.setLanguage(pLanguage);
    articleCategoryContent.setTitle(pTitle);
    return articleCategoryContent;
  }

  private ArticleCategoryEntity buildArticleCategoryEntity(final String pColor,
      final List<ArticleCategoryContent> pArticleCategoryContents)
  {
    ArticleCategoryEntity articleCategory = new ArticleCategoryEntity();
    articleCategory.setColor(pColor);
    for (ArticleCategoryContent articleCategoryContent : pArticleCategoryContents)
    {
      articleCategory.addArticleCategoryContent(articleCategoryContent);
    }
    return articleCategory;
  }

  /**
   * Test method for {@link org.novaforge.forge.article.internal.dao.ArticleCategoryDAOImpl#findByUUID()}.
   */
  @Test
  public void testFindByUUID()
  {
    final LanguageEntity french = buildLanguageEntity(LANGUAGE_NAME_FR);
    final LanguageEntity english = buildLanguageEntity(LANGUAGE_NAME_EN);
    List<Language> languageList = new ArrayList<Language>();
    languageList.add(french);
    languageList.add(english);

    final ArticleCategoryContent newsContentEN = buildArticleCategoryContentEntity(NEWS_TITLE_EN, english);
    final ArticleCategoryContent newsContentFR = buildArticleCategoryContentEntity(NEWS_TITLE_FR, french);
    final ArticleCategoryContent releaseContentEN = buildArticleCategoryContentEntity(RELEASE_TITLE_EN,
        english);
    final ArticleCategoryContent releaseContentFR = buildArticleCategoryContentEntity(RELEASE_TITLE_FR,
        french);

    List<ArticleCategoryContent> newsContentList = new ArrayList<ArticleCategoryContent>();
    newsContentList.add(newsContentEN);
    newsContentList.add(newsContentFR);

    List<ArticleCategoryContent> releaseContentList = new ArrayList<ArticleCategoryContent>();
    newsContentList.add(releaseContentEN);
    newsContentList.add(releaseContentFR);

    final ArticleCategory news = buildArticleCategoryEntity(NEWS_COLOR, newsContentList);
    final ArticleCategory release = buildArticleCategoryEntity(RELEASE_COLOR, releaseContentList);

    em.getTransaction().begin();
    articleCategoryDAOImpl.persist(news, languageList);
    articleCategoryDAOImpl.persist(release, languageList);
    em.getTransaction().commit();

    final ArticleCategory entity = articleCategoryDAOImpl.findByUUID(news.getUuid());
    assertNotNull(entity);
    assertThat(entity.getColor(), is(NEWS_COLOR));

  }
}
