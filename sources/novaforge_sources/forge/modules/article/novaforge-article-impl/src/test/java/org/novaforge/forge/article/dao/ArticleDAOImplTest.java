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
import org.novaforge.forge.article.entity.ArticleContentEntity;
import org.novaforge.forge.article.entity.ArticleEntity;
import org.novaforge.forge.article.exception.ArticleCategoryServiceException;
import org.novaforge.forge.article.exception.ArticleServiceException;
import org.novaforge.forge.article.internal.dao.ArticleDAOImpl;
import org.novaforge.forge.article.internal.model.ArticleFilterImpl;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.article.model.ArticleCategoryContent;
import org.novaforge.forge.article.model.ArticleContent;
import org.novaforge.forge.article.model.ArticleFilter;
import org.novaforge.forge.article.model.ArticleType;
import org.novaforge.forge.core.organization.entity.LanguageEntity;
import org.novaforge.forge.core.organization.model.Language;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Gauthier Cart
 */
public class ArticleDAOImplTest extends ArticleJPATestCase
{

  private static final String LANGUAGE_NAME_EN                     = "en";
  private static final String LANGUAGE_NAME_FR                     = "fr";
  private static final String NEWS_TITLE_FR                        = "Actualité";
  private static final String NEWS_TITLE_EN                        = "News";
  private static final String NEWS_COLOR                           = "#0066A1";
  private static final String RELEASE_TITLE_FR                     = "Release Note";
  private static final String RELEASE_TITLE_EN                     = "Release Note";
  private static final String RELEASE_COLOR                        = "#00FF00";
  private static final String ARTICLE_CONTENT_NEWS_TITLE_EN        = "News lorem en";
  private static final String ARTICLE_CONTENT_NEWS_TITLE_FR        = "News lorem fr";
  private static final String ARTICLE_CONTENT_NEWS_SHORT_EN        = "News lorem ipsum en.";
  private static final String ARTICLE_CONTENT_NEWS_SHORT_FR        = "News lorem ipsum fr.";
  private static final String ARTICLE_CONTENT_NEWS_LONG_EN         = "News lorem ipsum dolor sit amet en.";
  private static final String ARTICLE_CONTENT_NEWS_LONG_FR         = "News lorem ipsum dolor sit amet fr.";
  private static final String ARTICLE_CONTENT_NEWS_ALT_TITLE_EN    = "News morbi en";
  private static final String ARTICLE_CONTENT_NEWS_ALT_TITLE_FR    = "News morbi fr";
  private static final String ARTICLE_CONTENT_NEWS_ALT_SHORT_EN    = "News morbi rutrum en.";
  private static final String ARTICLE_CONTENT_NEWS_ALT_SHORT_FR    = "News morbi rutrum fr.";
  private static final String ARTICLE_CONTENT_NEWS_ALT_LONG_EN     = "News morbi rutrum leo lectus en.";
  private static final String ARTICLE_CONTENT_NEWS_ALT_LONG_FR     = "News morbi rutrum leo lectus fr.";
  private static final String ARTICLE_CONTENT_RELEASE_TITLE_EN     = "Release morbi en";
  private static final String ARTICLE_CONTENT_RELEASE_TITLE_FR     = "Release morbi fr";
  private static final String ARTICLE_CONTENT_RELEASE_SHORT_EN     = "Release morbi rutrum en.";
  private static final String ARTICLE_CONTENT_RELEASE_SHORT_FR     = "Release morbi rutrum fr.";
  private static final String ARTICLE_CONTENT_RELEASE_LONG_EN      = "Release morbi rutrum leo lectus en.";
  private static final String ARTICLE_CONTENT_RELEASE_LONG_FR      = "Release morbi rutrum leo lectus fr.";
  private static final String ARTICLE_CONTENT_RELEASE_ALT_TITLE_EN = "Release lorem en";
  private static final String ARTICLE_CONTENT_RELEASE_ALT_TITLE_FR = "Release lorem fr";
  private static final String ARTICLE_CONTENT_RELEASE_ALT_SHORT_EN = "Release lorem ipsum en.";
  private static final String ARTICLE_CONTENT_RELEASE_ALT_SHORT_FR = "Release lorem ipsum fr.";
  private static final String ARTICLE_CONTENT_RELEASE_ALT_LONG_EN  = "Release lorem ipsum dolor sit amet en.";
  private static final String ARTICLE_CONTENT_RELEASE_ALT_LONG_FR  = "Release lorem ipsum dolor sit amet fr.";
  /*
   * Constants declaration
   */
  private ArticleDAOImpl articleDAOImpl;

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    articleDAOImpl = new ArticleDAOImpl();
    articleDAOImpl.setEntityManager(em);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    articleDAOImpl = null;
  }

  /**
   * Test method for {@link org.novaforge.forge.article.internal.dao.ArticleDAOImpl#findByUUID()}.
   */
  @Test
  public void testFindByUUID()
  {

    final LanguageEntity french       = buildLanguageEntity(LANGUAGE_NAME_FR);
    final LanguageEntity english      = buildLanguageEntity(LANGUAGE_NAME_EN);
    List<Language>       languageList = new ArrayList<Language>();
    languageList.add(french);
    languageList.add(english);

    final ArticleCategoryContent newsContentEN = buildArticleCategoryContentEntity(NEWS_TITLE_EN, english);
    final ArticleCategoryContent newsContentFR = buildArticleCategoryContentEntity(NEWS_TITLE_FR, french);

    List<ArticleCategoryContent> newsContentList = new ArrayList<ArticleCategoryContent>();
    newsContentList.add(newsContentEN);
    newsContentList.add(newsContentFR);

    final ArticleCategory news = buildArticleCategoryEntity(NEWS_COLOR, newsContentList);

    ArticleContentEntity articleContentEntityEn = buildArticleContentEntity(ARTICLE_CONTENT_NEWS_TITLE_EN,
                                                                            ARTICLE_CONTENT_NEWS_SHORT_EN,
                                                                            ARTICLE_CONTENT_NEWS_LONG_EN, english);
    ArticleContentEntity articleContentEntityFr = buildArticleContentEntity(ARTICLE_CONTENT_NEWS_TITLE_FR,
                                                                            ARTICLE_CONTENT_NEWS_SHORT_FR,
                                                                            ARTICLE_CONTENT_NEWS_LONG_FR, french);
    List<ArticleContent> articleContentList = new ArrayList<ArticleContent>();
    articleContentList.add(articleContentEntityEn);
    articleContentList.add(articleContentEntityFr);

    Date dateToday = new Date();

    Article articlePersistent = buildArticleEntity(true, news, articleContentList, ArticleType.NEWS, dateToday);

    final Article entity = articleDAOImpl.findByUUID(articlePersistent.getUuid());
    assertNotNull(entity);
    assertThat(entity.getArticleCategory().getColor(), is(NEWS_COLOR));
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

  private ArticleContentEntity buildArticleContentEntity(final String pTitle, final String pShortText,
      final String pText, final LanguageEntity pLanguageEntity)
  {
    ArticleContentEntity articleContentEntity = new ArticleContentEntity();
    articleContentEntity.setLanguage(pLanguageEntity);
    articleContentEntity.setTitle(pTitle);
    articleContentEntity.setShortText(pShortText);
    articleContentEntity.setText(pText);
    em.getTransaction().begin();
    em.persist(articleContentEntity);
    em.getTransaction().commit();
    return articleContentEntity;
  }

  private ArticleEntity buildArticleEntity(final boolean pPublished, final ArticleCategory pArticleCategory,
      final List<ArticleContent> pContent, final ArticleType pType, final Date pDate)
  {
    ArticleEntity article = new ArticleEntity();
    article.setPublished(pPublished);
    article.setArticleCategory(pArticleCategory);
    for (ArticleContent articleContent : pContent)
    {
      article.addArticleContent(articleContent);
    }
    article.setType(pType);
    article.setDate(pDate);
    em.getTransaction().begin();
    em.persist(article);
    em.getTransaction().commit();

    return article;
  }

  /**
   * Test method for {@link org.novaforge.forge.article.internal.dao.ArticleDAOImpl#findFromFilter()}.
   * 
   * @throws ArticleServiceException
   * @throws ArticleCategoryServiceException
   */
  @Test
  public void testfindFromFilterEmptyFilter() throws ArticleServiceException, ArticleCategoryServiceException
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

    ArticleContentEntity articleContentEntityNewsEn = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_TITLE_EN, ARTICLE_CONTENT_NEWS_SHORT_EN, ARTICLE_CONTENT_NEWS_LONG_EN, english);
    ArticleContentEntity articleContentEntityNewsFr = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_TITLE_FR, ARTICLE_CONTENT_NEWS_SHORT_FR, ARTICLE_CONTENT_NEWS_LONG_FR, french);
    List<ArticleContent> articleContentNewsList = new ArrayList<ArticleContent>();
    articleContentNewsList.add(articleContentEntityNewsEn);
    articleContentNewsList.add(articleContentEntityNewsFr);

    ArticleContentEntity articleContentEntityNewsAltEn = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_ALT_TITLE_EN, ARTICLE_CONTENT_NEWS_ALT_SHORT_EN,
        ARTICLE_CONTENT_NEWS_ALT_LONG_EN, english);
    ArticleContentEntity articleContentEntityNewsAltFr = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_ALT_TITLE_FR, ARTICLE_CONTENT_NEWS_ALT_SHORT_FR,
        ARTICLE_CONTENT_NEWS_ALT_LONG_FR, french);
    List<ArticleContent> articleContentNewsAltList = new ArrayList<ArticleContent>();
    articleContentNewsAltList.add(articleContentEntityNewsAltEn);
    articleContentNewsAltList.add(articleContentEntityNewsAltFr);

    ArticleContentEntity articleContentEntityReleaseEn = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_TITLE_EN, ARTICLE_CONTENT_RELEASE_SHORT_EN, ARTICLE_CONTENT_RELEASE_LONG_EN,
        english);
    ArticleContentEntity articleContentEntityReleaseFr = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_TITLE_FR, ARTICLE_CONTENT_RELEASE_SHORT_FR, ARTICLE_CONTENT_RELEASE_LONG_FR,
        french);
    List<ArticleContent> articleContentReleaseList = new ArrayList<ArticleContent>();
    articleContentReleaseList.add(articleContentEntityReleaseEn);
    articleContentReleaseList.add(articleContentEntityReleaseFr);

    ArticleContentEntity articleContentEntityReleaseAltEn = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_ALT_TITLE_EN, ARTICLE_CONTENT_RELEASE_ALT_SHORT_EN,
        ARTICLE_CONTENT_RELEASE_ALT_LONG_EN, english);
    ArticleContentEntity articleContentEntityReleaseAltFr = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_ALT_TITLE_FR, ARTICLE_CONTENT_RELEASE_ALT_SHORT_FR,
        ARTICLE_CONTENT_RELEASE_ALT_LONG_FR, french);
    List<ArticleContent> articleContentReleaseAltList = new ArrayList<ArticleContent>();
    articleContentReleaseAltList.add(articleContentEntityReleaseAltEn);
    articleContentReleaseAltList.add(articleContentEntityReleaseAltFr);

    Date dateToday = new Date();

    buildArticleEntity(true, news, articleContentNewsList, ArticleType.NEWS, dateToday);
    buildArticleEntity(true, release, articleContentReleaseList, ArticleType.NEWS, dateToday);
    buildArticleEntity(true, news, articleContentNewsAltList, ArticleType.NEWS, dateToday);
    buildArticleEntity(true, release, articleContentReleaseAltList, ArticleType.NEWS, dateToday);

    List<UUID> articleCategories = new ArrayList<UUID>();
    articleCategories.add(release.getUuid());

    ArticleFilter articleFilter = new ArticleFilterImpl();
    final List<Article> listEntities = articleDAOImpl.findFromFilter(articleFilter, english);

    assertNotNull(listEntities);
    assertFalse(listEntities.isEmpty());
    assertTrue(listEntities.size() == 4);
  }

  /**
   * Test method for {@link org.novaforge.forge.article.dao.ArticleDAOImpl#findFromFilter()}.
   * 
   * @throws ArticleServiceException
   * @throws ArticleCategoryServiceException
   */
  @Test
  public void testFindPublishedFromFilterOnDate() throws ArticleServiceException,
      ArticleCategoryServiceException
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

    ArticleContentEntity articleContentEntityNewsEn = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_TITLE_EN, ARTICLE_CONTENT_NEWS_SHORT_EN, ARTICLE_CONTENT_NEWS_LONG_EN, english);
    ArticleContentEntity articleContentEntityNewsFr = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_TITLE_FR, ARTICLE_CONTENT_NEWS_SHORT_FR, ARTICLE_CONTENT_NEWS_LONG_FR, french);
    List<ArticleContent> articleContentNewsList = new ArrayList<ArticleContent>();
    articleContentNewsList.add(articleContentEntityNewsEn);
    articleContentNewsList.add(articleContentEntityNewsFr);

    ArticleContentEntity articleContentEntityNewsAltEn = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_ALT_TITLE_EN, ARTICLE_CONTENT_NEWS_ALT_SHORT_EN,
        ARTICLE_CONTENT_NEWS_ALT_LONG_EN, english);
    ArticleContentEntity articleContentEntityNewsAltFr = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_ALT_TITLE_FR, ARTICLE_CONTENT_NEWS_ALT_SHORT_FR,
        ARTICLE_CONTENT_NEWS_ALT_LONG_FR, french);
    List<ArticleContent> articleContentNewsAltList = new ArrayList<ArticleContent>();
    articleContentNewsAltList.add(articleContentEntityNewsAltEn);
    articleContentNewsAltList.add(articleContentEntityNewsAltFr);

    ArticleContentEntity articleContentEntityReleaseEn = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_TITLE_EN, ARTICLE_CONTENT_RELEASE_SHORT_EN, ARTICLE_CONTENT_RELEASE_LONG_EN,
        english);
    ArticleContentEntity articleContentEntityReleaseFr = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_TITLE_FR, ARTICLE_CONTENT_RELEASE_SHORT_FR, ARTICLE_CONTENT_RELEASE_LONG_FR,
        french);
    List<ArticleContent> articleContentReleaseList = new ArrayList<ArticleContent>();
    articleContentReleaseList.add(articleContentEntityReleaseEn);
    articleContentReleaseList.add(articleContentEntityReleaseFr);

    ArticleContentEntity articleContentEntityReleaseAltEn = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_ALT_TITLE_EN, ARTICLE_CONTENT_RELEASE_ALT_SHORT_EN,
        ARTICLE_CONTENT_RELEASE_ALT_LONG_EN, english);
    ArticleContentEntity articleContentEntityReleaseAltFr = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_ALT_TITLE_FR, ARTICLE_CONTENT_RELEASE_ALT_SHORT_FR,
        ARTICLE_CONTENT_RELEASE_ALT_LONG_FR, french);
    List<ArticleContent> articleContentReleaseAltList = new ArrayList<ArticleContent>();
    articleContentReleaseAltList.add(articleContentEntityReleaseAltEn);
    articleContentReleaseAltList.add(articleContentEntityReleaseAltFr);

    Date dateToday = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, -1);
    Date dateYesterday = calendar.getTime();
    calendar.add(Calendar.DATE, +2);
    Date dateTomorrow = calendar.getTime();
    calendar.add(Calendar.DATE, -3);
    Date dateBeforeYesterday = calendar.getTime();

    buildArticleEntity(true, news, articleContentNewsList, ArticleType.NEWS, dateToday);
    buildArticleEntity(true, release, articleContentReleaseList, ArticleType.NEWS, dateTomorrow);
    buildArticleEntity(true, news, articleContentNewsAltList, ArticleType.NEWS, dateYesterday);
    buildArticleEntity(true, release, articleContentReleaseAltList, ArticleType.NEWS, dateBeforeYesterday);

    List<UUID> articleCategories = new ArrayList<UUID>();
    articleCategories.add(news.getUuid());

    ArticleFilter articleFilter = new ArticleFilterImpl();
    articleFilter.setBeginDate(dateYesterday);
    articleFilter.setEndDate(dateToday);
    articleFilter.setArticleCategoryUuids(articleCategories);

    List<Article> listEntities = articleDAOImpl.findFromFilter(articleFilter, english);

    assertNotNull(listEntities);
    assertFalse(listEntities.isEmpty());
    assertTrue(listEntities.size() == 2);
  }

  /**
   * Test method for {@link org.novaforge.forge.article.dao.ArticleDAOImpl#getLastPublishedArticleFromType()}.
   * 
   * @throws ArticleServiceException
   * @throws ArticleCategoryServiceException
   */
  @Test
  public void testFindPublishedFromFilterOnType() throws ArticleServiceException,
      ArticleCategoryServiceException
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

    ArticleContentEntity articleContentEntityNewsEn = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_TITLE_EN, ARTICLE_CONTENT_NEWS_SHORT_EN, ARTICLE_CONTENT_NEWS_LONG_EN, english);
    ArticleContentEntity articleContentEntityNewsFr = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_TITLE_FR, ARTICLE_CONTENT_NEWS_SHORT_FR, ARTICLE_CONTENT_NEWS_LONG_FR, french);
    List<ArticleContent> articleContentNewsList = new ArrayList<ArticleContent>();
    articleContentNewsList.add(articleContentEntityNewsEn);
    articleContentNewsList.add(articleContentEntityNewsFr);

    ArticleContentEntity articleContentEntityNewsAltEn = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_ALT_TITLE_EN, ARTICLE_CONTENT_NEWS_ALT_SHORT_EN,
        ARTICLE_CONTENT_NEWS_ALT_LONG_EN, english);
    ArticleContentEntity articleContentEntityNewsAltFr = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_ALT_TITLE_FR, ARTICLE_CONTENT_NEWS_ALT_SHORT_FR,
        ARTICLE_CONTENT_NEWS_ALT_LONG_FR, french);
    List<ArticleContent> articleContentNewsAltList = new ArrayList<ArticleContent>();
    articleContentNewsAltList.add(articleContentEntityNewsAltEn);
    articleContentNewsAltList.add(articleContentEntityNewsAltFr);

    ArticleContentEntity articleContentEntityReleaseEn = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_TITLE_EN, ARTICLE_CONTENT_RELEASE_SHORT_EN, ARTICLE_CONTENT_RELEASE_LONG_EN,
        english);
    ArticleContentEntity articleContentEntityReleaseFr = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_TITLE_FR, ARTICLE_CONTENT_RELEASE_SHORT_FR, ARTICLE_CONTENT_RELEASE_LONG_FR,
        french);
    List<ArticleContent> articleContentReleaseList = new ArrayList<ArticleContent>();
    articleContentReleaseList.add(articleContentEntityReleaseEn);
    articleContentReleaseList.add(articleContentEntityReleaseFr);

    ArticleContentEntity articleContentEntityReleaseAltEn = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_ALT_TITLE_EN, ARTICLE_CONTENT_RELEASE_ALT_SHORT_EN,
        ARTICLE_CONTENT_RELEASE_ALT_LONG_EN, english);
    ArticleContentEntity articleContentEntityReleaseAltFr = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_ALT_TITLE_FR, ARTICLE_CONTENT_RELEASE_ALT_SHORT_FR,
        ARTICLE_CONTENT_RELEASE_ALT_LONG_FR, french);
    List<ArticleContent> articleContentReleaseAltList = new ArrayList<ArticleContent>();
    articleContentReleaseAltList.add(articleContentEntityReleaseAltEn);
    articleContentReleaseAltList.add(articleContentEntityReleaseAltFr);

    Date dateToday = new Date();

    buildArticleEntity(true, news, articleContentNewsList, ArticleType.NEWS, dateToday);
    buildArticleEntity(true, release, articleContentReleaseList, ArticleType.ANNOUCEMENT, dateToday);
    buildArticleEntity(true, news, articleContentNewsAltList, ArticleType.INFORMATION, dateToday);
    buildArticleEntity(true, release, articleContentReleaseAltList, ArticleType.NEWS, dateToday);

    List<UUID> articleCategories = new ArrayList<UUID>();
    articleCategories.add(news.getUuid());
    articleCategories.add(release.getUuid());

    ArticleFilter articleFilter = new ArticleFilterImpl();
    articleFilter.setArticleCategoryUuids(articleCategories);
    articleFilter.setArticleType(ArticleType.NEWS);

    List<Article> listEntities = articleDAOImpl.findFromFilter(articleFilter, english);

    assertNotNull(listEntities);
    assertFalse(listEntities.isEmpty());

    assertTrue(listEntities.size() == 2);
  }

  /**
   * Test method for {@link org.novaforge.forge.article.dao.ArticleDAOImpl#getLastPublishedArticleFromType()}.
   * 
   * @throws ArticleServiceException
   * @throws ArticleCategoryServiceException
   */
  @Test
  public void testFindFromFilterOnLanguage() throws ArticleServiceException, ArticleCategoryServiceException
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

    ArticleContentEntity articleContentEntityNewsEn = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_TITLE_EN, ARTICLE_CONTENT_NEWS_SHORT_EN, ARTICLE_CONTENT_NEWS_LONG_EN, english);
    ArticleContentEntity articleContentEntityNewsFr = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_TITLE_FR, ARTICLE_CONTENT_NEWS_SHORT_FR, ARTICLE_CONTENT_NEWS_LONG_FR, french);
    List<ArticleContent> articleContentNewsList = new ArrayList<ArticleContent>();
    articleContentNewsList.add(articleContentEntityNewsEn);
    articleContentNewsList.add(articleContentEntityNewsFr);

    ArticleContentEntity articleContentEntityNewsAltEn = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_ALT_TITLE_EN, ARTICLE_CONTENT_NEWS_ALT_SHORT_EN,
        ARTICLE_CONTENT_NEWS_ALT_LONG_EN, english);
    ArticleContentEntity articleContentEntityNewsAltFr = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_ALT_TITLE_FR, ARTICLE_CONTENT_NEWS_ALT_SHORT_FR,
        ARTICLE_CONTENT_NEWS_ALT_LONG_FR, french);
    List<ArticleContent> articleContentNewsAltList = new ArrayList<ArticleContent>();
    articleContentNewsAltList.add(articleContentEntityNewsAltEn);
    articleContentNewsAltList.add(articleContentEntityNewsAltFr);

    ArticleContentEntity articleContentEntityReleaseEn = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_TITLE_EN, ARTICLE_CONTENT_RELEASE_SHORT_EN, ARTICLE_CONTENT_RELEASE_LONG_EN,
        english);
    ArticleContentEntity articleContentEntityReleaseFr = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_TITLE_FR, ARTICLE_CONTENT_RELEASE_SHORT_FR, ARTICLE_CONTENT_RELEASE_LONG_FR,
        french);
    List<ArticleContent> articleContentReleaseList = new ArrayList<ArticleContent>();
    articleContentReleaseList.add(articleContentEntityReleaseEn);
    articleContentReleaseList.add(articleContentEntityReleaseFr);

    // ArticleContentEntity articleContentEntityReleaseAltEn = buildArticleContentEntity(
    // ARTICLE_CONTENT_RELEASE_ALT_TITLE_EN, ARTICLE_CONTENT_RELEASE_ALT_SHORT_EN,
    // ARTICLE_CONTENT_RELEASE_ALT_LONG_EN, english);
    ArticleContentEntity articleContentEntityReleaseAltFr = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_ALT_TITLE_FR, ARTICLE_CONTENT_RELEASE_ALT_SHORT_FR,
        ARTICLE_CONTENT_RELEASE_ALT_LONG_FR, french);
    List<ArticleContent> articleContentReleaseAltList = new ArrayList<ArticleContent>();
    // articleContentReleaseAltList.add(articleContentEntityReleaseAltEn);
    articleContentReleaseAltList.add(articleContentEntityReleaseAltFr);

    Date dateToday = new Date();

    buildArticleEntity(true, news, articleContentNewsList, ArticleType.NEWS, dateToday);
    buildArticleEntity(true, release, articleContentReleaseList, ArticleType.NEWS, dateToday);
    buildArticleEntity(true, news, articleContentNewsAltList, ArticleType.NEWS, dateToday);
    buildArticleEntity(true, release, articleContentReleaseAltList, ArticleType.NEWS, dateToday);

    List<UUID> articleCategories = new ArrayList<UUID>();
    articleCategories.add(news.getUuid());
    articleCategories.add(release.getUuid());

    ArticleFilter articleFilter = new ArticleFilterImpl();
    articleFilter.setArticleCategoryUuids(articleCategories);
    articleFilter.setArticleType(ArticleType.NEWS);

    List<Article> listEntities = articleDAOImpl.findFromFilter(articleFilter, english);

    assertNotNull(listEntities);
    assertFalse(listEntities.isEmpty());
    assertTrue(listEntities.size() == 3);
  }

  /**
   * Test method for {@link org.novaforge.forge.article.dao.ArticleDAOImpl#getLastPublishedArticleFromType()}.
   * 
   * @throws ArticleServiceException
   * @throws ArticleCategoryServiceException
   */
  @Test
  public void testFindFromFilterOnText() throws ArticleServiceException, ArticleCategoryServiceException
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

    ArticleContentEntity articleContentEntityNewsEn = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_TITLE_EN, ARTICLE_CONTENT_NEWS_SHORT_EN, ARTICLE_CONTENT_NEWS_LONG_EN, english);
    ArticleContentEntity articleContentEntityNewsFr = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_TITLE_FR, ARTICLE_CONTENT_NEWS_SHORT_FR, ARTICLE_CONTENT_NEWS_LONG_FR, french);
    List<ArticleContent> articleContentNewsList = new ArrayList<ArticleContent>();
    articleContentNewsList.add(articleContentEntityNewsEn);
    articleContentNewsList.add(articleContentEntityNewsFr);

    ArticleContentEntity articleContentEntityNewsAltEn = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_ALT_TITLE_EN, ARTICLE_CONTENT_NEWS_ALT_SHORT_EN,
        ARTICLE_CONTENT_NEWS_ALT_LONG_EN, english);
    ArticleContentEntity articleContentEntityNewsAltFr = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_ALT_TITLE_FR, ARTICLE_CONTENT_NEWS_ALT_SHORT_FR,
        ARTICLE_CONTENT_NEWS_ALT_LONG_FR, french);
    List<ArticleContent> articleContentNewsAltList = new ArrayList<ArticleContent>();
    articleContentNewsAltList.add(articleContentEntityNewsAltEn);
    articleContentNewsAltList.add(articleContentEntityNewsAltFr);

    ArticleContentEntity articleContentEntityReleaseEn = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_TITLE_EN, ARTICLE_CONTENT_RELEASE_SHORT_EN, ARTICLE_CONTENT_RELEASE_LONG_EN,
        english);
    ArticleContentEntity articleContentEntityReleaseFr = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_TITLE_FR, ARTICLE_CONTENT_RELEASE_SHORT_FR, ARTICLE_CONTENT_RELEASE_LONG_FR,
        french);
    List<ArticleContent> articleContentReleaseList = new ArrayList<ArticleContent>();
    articleContentReleaseList.add(articleContentEntityReleaseEn);
    articleContentReleaseList.add(articleContentEntityReleaseFr);

    ArticleContentEntity articleContentEntityReleaseAltEn = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_ALT_TITLE_EN, ARTICLE_CONTENT_RELEASE_ALT_SHORT_EN,
        ARTICLE_CONTENT_RELEASE_ALT_LONG_EN, english);
    ArticleContentEntity articleContentEntityReleaseAltFr = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_ALT_TITLE_FR, ARTICLE_CONTENT_RELEASE_ALT_SHORT_FR,
        ARTICLE_CONTENT_RELEASE_ALT_LONG_FR, french);
    List<ArticleContent> articleContentReleaseAltList = new ArrayList<ArticleContent>();
    articleContentReleaseAltList.add(articleContentEntityReleaseAltEn);
    articleContentReleaseAltList.add(articleContentEntityReleaseAltFr);

    Date dateToday = new Date();

    buildArticleEntity(true, news, articleContentNewsList, ArticleType.NEWS, dateToday);
    buildArticleEntity(true, release, articleContentReleaseList, ArticleType.NEWS, dateToday);
    buildArticleEntity(true, news, articleContentNewsAltList, ArticleType.NEWS, dateToday);
    buildArticleEntity(true, release, articleContentReleaseAltList, ArticleType.NEWS, dateToday);

    List<UUID> articleCategories = new ArrayList<UUID>();
    articleCategories.add(news.getUuid());
    articleCategories.add(release.getUuid());

    ArticleFilter articleFilter = new ArticleFilterImpl();
    articleFilter.setArticleCategoryUuids(articleCategories);
    articleFilter.setArticleType(ArticleType.NEWS);
    articleFilter.setTextSearch("lorem ipsum");

    List<Article> listEntities = articleDAOImpl.findFromFilter(articleFilter, english);

    assertNotNull(listEntities);
    assertFalse(listEntities.isEmpty());
    assertTrue(listEntities.size() == 2);
  }

  /**
   * Test method for {@link org.novaforge.forge.article.dao.ArticleDAOImpl#getLastPublishedArticleFromType()}.
   * 
   * @throws ArticleServiceException
   * @throws ArticleCategoryServiceException
   */
  @Test
  public void testFindFromFilterOnPublished() throws ArticleServiceException, ArticleCategoryServiceException
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

    ArticleContentEntity articleContentEntityNewsEn = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_TITLE_EN, ARTICLE_CONTENT_NEWS_SHORT_EN, ARTICLE_CONTENT_NEWS_LONG_EN, english);
    ArticleContentEntity articleContentEntityNewsFr = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_TITLE_FR, ARTICLE_CONTENT_NEWS_SHORT_FR, ARTICLE_CONTENT_NEWS_LONG_FR, french);
    List<ArticleContent> articleContentNewsList = new ArrayList<ArticleContent>();
    articleContentNewsList.add(articleContentEntityNewsEn);
    articleContentNewsList.add(articleContentEntityNewsFr);

    ArticleContentEntity articleContentEntityNewsAltEn = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_ALT_TITLE_EN, ARTICLE_CONTENT_NEWS_ALT_SHORT_EN,
        ARTICLE_CONTENT_NEWS_ALT_LONG_EN, english);
    ArticleContentEntity articleContentEntityNewsAltFr = buildArticleContentEntity(
        ARTICLE_CONTENT_NEWS_ALT_TITLE_FR, ARTICLE_CONTENT_NEWS_ALT_SHORT_FR,
        ARTICLE_CONTENT_NEWS_ALT_LONG_FR, french);
    List<ArticleContent> articleContentNewsAltList = new ArrayList<ArticleContent>();
    articleContentNewsAltList.add(articleContentEntityNewsAltEn);
    articleContentNewsAltList.add(articleContentEntityNewsAltFr);

    ArticleContentEntity articleContentEntityReleaseEn = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_TITLE_EN, ARTICLE_CONTENT_RELEASE_SHORT_EN, ARTICLE_CONTENT_RELEASE_LONG_EN,
        english);
    ArticleContentEntity articleContentEntityReleaseFr = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_TITLE_FR, ARTICLE_CONTENT_RELEASE_SHORT_FR, ARTICLE_CONTENT_RELEASE_LONG_FR,
        french);
    List<ArticleContent> articleContentReleaseList = new ArrayList<ArticleContent>();
    articleContentReleaseList.add(articleContentEntityReleaseEn);
    articleContentReleaseList.add(articleContentEntityReleaseFr);

    ArticleContentEntity articleContentEntityReleaseAltEn = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_ALT_TITLE_EN, ARTICLE_CONTENT_RELEASE_ALT_SHORT_EN,
        ARTICLE_CONTENT_RELEASE_ALT_LONG_EN, english);
    ArticleContentEntity articleContentEntityReleaseAltFr = buildArticleContentEntity(
        ARTICLE_CONTENT_RELEASE_ALT_TITLE_FR, ARTICLE_CONTENT_RELEASE_ALT_SHORT_FR,
        ARTICLE_CONTENT_RELEASE_ALT_LONG_FR, french);
    List<ArticleContent> articleContentReleaseAltList = new ArrayList<ArticleContent>();
    articleContentReleaseAltList.add(articleContentEntityReleaseAltEn);
    articleContentReleaseAltList.add(articleContentEntityReleaseAltFr);

    Date dateToday = new Date();

    buildArticleEntity(false, news, articleContentNewsList, ArticleType.NEWS, dateToday);
    buildArticleEntity(false, release, articleContentReleaseList, ArticleType.NEWS, dateToday);
    buildArticleEntity(true, news, articleContentNewsAltList, ArticleType.NEWS, dateToday);
    buildArticleEntity(true, release, articleContentReleaseAltList, ArticleType.NEWS, dateToday);

    List<UUID> articleCategories = new ArrayList<UUID>();
    articleCategories.add(news.getUuid());
    articleCategories.add(release.getUuid());

    ArticleFilter articleFilter = new ArticleFilterImpl();
    articleFilter.setArticleCategoryUuids(articleCategories);
    articleFilter.setIsPublished(true);

    List<Article> listEntities = articleDAOImpl.findFromFilter(articleFilter, english);

    assertNotNull(listEntities);
    assertFalse(listEntities.isEmpty());
    assertTrue(listEntities.size() == 2);
  }
}