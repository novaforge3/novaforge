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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import org.novaforge.forge.article.entity.ArticleCategoryContentEntity;
import org.novaforge.forge.article.entity.ArticleCategoryEntity;
import org.novaforge.forge.article.entity.ArticleCategoryEntity_;
import org.novaforge.forge.article.entity.ArticleContentEntity;
import org.novaforge.forge.article.entity.ArticleContentEntity_;
import org.novaforge.forge.article.entity.ArticleEntity;
import org.novaforge.forge.article.entity.ArticleEntity_;
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

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author IO
 */

@RunWith(Parameterized.class)
public class ArticleDAOMockImplTest
{

  private String title;
  private String shText;
  private String text;
  private String color;
  private String language;
  private String expected; 
  
  private ArticleDAOImpl articleDAOImpl;
  private EntityManager emm;  
  
  private Expression <Boolean> expr;
  private Expression<List<ArticleContentEntity>> exprList;
  private Path <String> path;
  private Path <ArticleCategory> pathCateg;
  private Path <Language> pathLanguage;
  private TypedQuery<Article> typeQuery;
  private CriteriaQuery<Article> articleCriteria;
  private CriteriaBuilder cbMock;
  private Root<ArticleEntity> root;
  //private Root<ArticleContentEntity> rootContent;
  private Subquery<ArticleCategoryEntity> subquery;
  private Subquery<ArticleContentEntity> subqueryContent;
  private Root<ArticleCategoryEntity> fromCategory;
  private Root<ArticleContentEntity> fromContent;
 
 
  
  
  public ArticleDAOMockImplTest(final String input1, final String input2, final String input3, 
      final String input4, final String input5, final String expected) 
  {
    this.title = input1;
    this.shText = input2;
    this.text = input3;
    this.color = input4;
    this.language = input5;
    this.expected = expected;

  }
  
 
  /**
   * {@inheritDoc}
   */
 
  @SuppressWarnings("unchecked")
  @Before
  public void setUp()
  {
    emm = Mockito.mock(EntityManager.class);
    typeQuery = Mockito.mock(TypedQuery.class);
    articleCriteria =  Mockito.mock(CriteriaQuery.class);
    cbMock = Mockito.mock(CriteriaBuilder.class);
    root = Mockito.mock(Root.class);
    //rootContent = Mockito.mock(Root.class);
    subquery = Mockito.mock(Subquery.class);
    subqueryContent = Mockito.mock(Subquery.class);
    fromCategory =  Mockito.mock(Root.class);
    fromContent =  Mockito.mock(Root.class);
    expr = Mockito.mock(Expression.class);
    exprList = Mockito.mock(Expression.class);
    path = Mockito.mock(Path.class);
    pathCateg = Mockito.mock(Path.class);
    pathLanguage = Mockito.mock(Path.class);
   
    
    articleDAOImpl = new ArticleDAOImpl();
  
    articleDAOImpl.setEntityManager(emm);    
   
  
    
    
  }

  /**
   * {@inheritDoc}
   */
 
  @After
  public void tearDown()
  {
    
    articleDAOImpl = null;
    emm = null;
  }

  /**
   * Test method for {@link org.novaforge.forge.article.internal.dao.ArticleDAOImpl#findByUUID()}.
   */
  @Test
  public void testFindByUUID()
  {

    final LanguageEntity lang       = buildLanguageEntity(language); 
    List<Language> languageList = new ArrayList<Language>();
    languageList.add(lang);

    final ArticleCategoryContent newsContent = buildArticleCategoryContentEntity(title, lang);
      
    List<ArticleCategoryContent> newsContentList = new ArrayList<ArticleCategoryContent>();
    newsContentList.add(newsContent);
  
    final ArticleCategory news = buildArticleCategoryEntity(color, newsContentList);
    ArticleContentEntity articleContentEntity = buildArticleContentEntity(title,
                                                                            shText,
                                                                            text, lang);
  
    List<ArticleContent> articleContentList = new ArrayList<ArticleContent>();
    articleContentList.add(articleContentEntity);
   
    Date dateToday = new Date();

    Article articlePersistent = buildArticleEntity(true, news, articleContentList, ArticleType.NEWS, dateToday);
    //q.getSingleResult();

    Mockito.when(emm.createNamedQuery("ArticleEntity.findByUUID", Article.class)).thenReturn(typeQuery);
    Mockito.when(typeQuery.setParameter("uuid", articlePersistent.toString())).thenReturn(typeQuery);
    Mockito.when(typeQuery.getSingleResult()).thenReturn(articlePersistent);
    
    articlePersistent = articleDAOImpl.findByUUID(UUID.randomUUID());
  
    assertNotNull(articlePersistent);
    assertThat(articlePersistent.getArticleCategory().getColor(), is(color));
    
    Mockito.verify(emm, Mockito.times(1)).persist(articlePersistent);  
    
  }
  
  
  void mockQuery(String name, List<LanguageEntity> results) {

    Query mockedQuery = Mockito.mock(Query.class);
    Mockito.when(mockedQuery.getResultList()).thenReturn(results);
  }
  
  void mockQuery1(String name, List<Article> results) {
    
    Query mockedQuery = Mockito.mock(Query.class);
    Mockito.when(mockedQuery.getResultList()).thenReturn(results);
    Mockito.when(this.emm.createNamedQuery(name)).thenReturn(mockedQuery);
}
  
  void mockTransaction(ArticleContentEntity articleContentEntity)
  {
    EntityTransaction transaction = Mockito.mock(EntityTransaction.class);
    Mockito.when(emm.getTransaction()).thenReturn(transaction);
    
    emm.getTransaction().begin();
    emm.persist(articleContentEntity);
    emm.getTransaction().commit();

  }

 

  private LanguageEntity buildLanguageEntity(final String pLanguageName)
  {
    LanguageEntity language1 = new LanguageEntity();
    language1.setName("EN");
    LanguageEntity language2 = new LanguageEntity();
    language2.setName("FR");
    List<LanguageEntity> results = new ArrayList<LanguageEntity>();
    results.add(language1);
    results.add(language2);
    
    
    //partie mock

//    final TypedQuery<LanguageEntity> query = emm.createQuery(
//        "SELECT l FROM LanguageEntity l WHERE l.name = :name", LanguageEntity.class);
//    query.setParameter("name", pLanguageName);
//    final List<LanguageEntity> resultList = query.getResultList();
//
//    if ((resultList == null) || (resultList.isEmpty()))
//    {
//      if (!emm.getTransaction().isActive())
//      {
//        emm.getTransaction().begin();
//      }
//      language = new LanguageEntity();
//      language.setName(pLanguageName);
//      emm.persist(language);
//      emm.getTransaction().commit();
//    }
//    else
//    {
//      language = resultList.get(0);
//    }
    
    mockQuery(pLanguageName, results);
    LanguageEntity language = results.get(0);    
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
    
    mockTransaction(articleContentEntity);
   
    
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
    emm.getTransaction().begin();
    emm.persist(article);
    emm.getTransaction().commit();

    return article;
  }

  /**
   * Test method for {@link org.novaforge.forge.article.dao.ArticleDAOImpl#findFromFilter()}.
   * 
   * @throws ArticleServiceException
   * @throws ArticleCategoryServiceException
   */
  //@Test
  public void testFindFromFilterOnText() throws ArticleServiceException, ArticleCategoryServiceException
  {

    final LanguageEntity lang = buildLanguageEntity(language);
    List<Language> languageList = new ArrayList<Language>();
    languageList.add(lang);
   
    final ArticleCategoryContent newsContentLang = buildArticleCategoryContentEntity(title, lang);
    final ArticleCategoryContent releaseContentLang = buildArticleCategoryContentEntity(title, lang);
    
    List<ArticleCategoryContent> newsContentList = new ArrayList<ArticleCategoryContent>();
    newsContentList.add(newsContentLang);
 

    List<ArticleCategoryContent> releaseContentList = new ArrayList<ArticleCategoryContent>();
    newsContentList.add(releaseContentLang);

    
    final ArticleCategoryEntity aceNews = buildArticleCategoryEntity(color, newsContentList);
    aceNews.setUuid(UUID.randomUUID());
    final ArticleCategoryEntity aceRelease = buildArticleCategoryEntity(color, releaseContentList);
    aceRelease.setUuid(UUID.randomUUID());
    
    ArticleContentEntity articleContentEntityNewsLang = buildArticleContentEntity(
       title, shText, text, lang);
   
    List<ArticleContent> articleContentNewsList = new ArrayList<ArticleContent>();
    articleContentNewsList.add(articleContentEntityNewsLang);


    ArticleContentEntity articleContentEntityNewsAltLang = buildArticleContentEntity(
        title, shText, text, lang);
 
    List<ArticleContent> articleContentNewsAltList = new ArrayList<ArticleContent>();
    articleContentNewsAltList.add(articleContentEntityNewsAltLang);

    ArticleContentEntity articleContentEntityReleaseLang = buildArticleContentEntity(
        title, shText, text,
        lang);
   
    List<ArticleContent> articleContentReleaseList = new ArrayList<ArticleContent>();
    articleContentReleaseList.add(articleContentEntityReleaseLang);
  

    ArticleContentEntity articleContentEntityReleaseAltLang = buildArticleContentEntity(
        title, shText, text, lang);
  
    List<ArticleContent> articleContentReleaseAltList = new ArrayList<ArticleContent>();
    articleContentReleaseAltList.add(articleContentEntityReleaseAltLang);
  

    Date dateToday = new Date();

    Article myArticleNews = buildArticleEntity(true, aceNews, articleContentNewsList, ArticleType.NEWS, dateToday);
    Article myArticleRelease = buildArticleEntity(true, aceRelease, articleContentReleaseList, ArticleType.NEWS, dateToday);
    //Article myArticleNewsAltList = buildArticleEntity(true, aceNews, articleContentNewsAltList, ArticleType.NEWS, dateToday);
    //Article myArticleReleaseAltList = buildArticleEntity(true, aceRelease, articleContentReleaseAltList, ArticleType.NEWS, dateToday);

    List<UUID> articleCategories = new ArrayList<UUID>();
    articleCategories.add(aceNews.getUuid());
    articleCategories.add(aceRelease.getUuid());

    ArticleFilter articleFilter = new ArticleFilterImpl();
    articleFilter.setArticleCategoryUuids(articleCategories);
    articleFilter.setArticleType(ArticleType.NEWS);
   // articleFilter.setTextSearch("lorem ipsum"); //textEmpty

    List<Article> listEntities = new ArrayList<Article>();
   
    Mockito.when(emm.getCriteriaBuilder()).thenReturn(cbMock);
    Mockito.when(emm.createQuery(articleCriteria)).thenReturn(typeQuery);
    Mockito.when(cbMock.createQuery(Article.class)).thenReturn(articleCriteria);
    Mockito.when(typeQuery.getResultList()).thenReturn(listEntities);
    
    Mockito.when(articleCriteria.from(ArticleEntity.class)).thenReturn(root);
    Mockito.when(articleCriteria.subquery(ArticleCategoryEntity.class)).thenReturn(subquery);
    
    Mockito.when(subquery.from(ArticleCategoryEntity.class)).thenReturn(fromCategory);
    Mockito.when(subquery.where(expr)).thenReturn(subquery);
    
    //set uuid
    Mockito.when(fromCategory.get(ArticleCategoryEntity_.uuid)).thenReturn(path);
    Mockito.when(root.get(ArticleEntity_.category)).thenReturn(pathCateg);
    
    //text empty
    Mockito.when(articleCriteria.subquery(ArticleContentEntity.class)).thenReturn(subqueryContent);
    Mockito.when(subqueryContent.from(ArticleContentEntity.class)).thenReturn(fromContent);
    Mockito.when(fromContent.get(ArticleContentEntity_.language)).thenReturn(pathLanguage);
    Mockito.when(root.get(ArticleEntity_.content)).thenReturn(exprList);
    
    
    listEntities = articleDAOImpl.findFromFilter(articleFilter, lang);

    listEntities.add(myArticleNews);
       
    assertNotNull(listEntities);
    Mockito.verify(emm, Mockito.times(2)).persist(myArticleNews);
    Mockito.verify(emm, Mockito.times(2)).persist(myArticleRelease);
    Mockito.verify(emm, Mockito.times(4)).persist(articleContentEntityNewsLang);
    
    assertEquals(myArticleNews.getArticleCategory().getColor(), expected);

  }
  
  //parameters
  //date, type, language, text, shortText, published, category, content, title
  
  @Parameters
  public static Collection <Object[]> articleContent() 
  {
     Object[][] data = new Object [][]{ 
         {"pTitle_FR", "pShortText_FR","pText_FR", "#0066A1", "FR", "#0066A1"}, 
         {"pTitle_EN", "pShortText_EN","pText_EN", "#0066A1", "EN", "#0066A1"},  
         {"pTitle_FR", "pShortText_FR","pText_FR", "#0066A1", "EN", "#0066A1"}};
     return Arrays.asList(data);
  }
  
  //TODO: filter type, published, date, ...
  //@Test
  public void testFindFromFilterData() throws ArticleServiceException, ArticleCategoryServiceException
  {
    //ArticleFilter pArticleFilter, Language pLanguage
  
    final LanguageEntity english = buildLanguageEntity(language);
    List<Language> languageList = new ArrayList<Language>();   
    languageList.add(english);
    
    final ArticleCategoryContent newsContentEN = buildArticleCategoryContentEntity(title, english);
    
    List<ArticleCategoryContent> newsContentList = new ArrayList<ArticleCategoryContent>();
    newsContentList.add(newsContentEN);
    
    final ArticleCategoryEntity aceNews = buildArticleCategoryEntity(color, newsContentList);
    aceNews.setUuid(UUID.randomUUID());
  
    ArticleContentEntity articleContentEntityNewsEn = buildArticleContentEntity(
        title, shText, text, english);
   
    List<ArticleContent> articleContentNewsList = new ArrayList<ArticleContent>();
    articleContentNewsList.add(articleContentEntityNewsEn);
    
    List<UUID> articleCategories = new ArrayList<UUID>();
    articleCategories.add(aceNews.getUuid());
  
    ArticleFilter articleFilter = new ArticleFilterImpl();
    articleFilter.setArticleCategoryUuids(articleCategories);
    articleFilter.setArticleType(ArticleType.NEWS);
    articleFilter.setTextSearch("lorem ipsum"); 
    
     //published filter
//    ArticleFilter articleFilter = new ArticleFilterImpl();
//    articleFilter.setArticleCategoryUuids(articleCategories);
//    articleFilter.setIsPublished(true);
    
    //type filter
    //articleFilter.setArticleType(ArticleType.NEWS);
    
    //empty filter
    //ArticleFilter articleFilter = new ArticleFilterImpl();
    //final List<Article> listEntities = articleDAOImpl.findFromFilter(articleFilter, english);

    
    
    Date dateToday = new Date();
        
    Article myArticleNews = buildArticleEntity(true, aceNews, articleContentNewsList, ArticleType.NEWS, dateToday);
    
    List<Article> listEntities = new ArrayList<Article>();
    listEntities.add(myArticleNews);
   
    Mockito.when(emm.getCriteriaBuilder()).thenReturn(cbMock);
    Mockito.when(cbMock.createQuery(Article.class)).thenReturn(articleCriteria);
   
    mockQuery1(Mockito.anyString(), listEntities); 
   
    articleDAOImpl.findFromFilter(articleFilter, english);
    
    assertNotNull(listEntities);
    assertEquals(myArticleNews.getArticleCategory().getColor(), expected);
    Mockito.verify(emm, Mockito.times(1)).persist(myArticleNews);
    Mockito.verify(emm, Mockito.times(1)).persist(articleContentEntityNewsEn);
    
  }
  

  
 
}