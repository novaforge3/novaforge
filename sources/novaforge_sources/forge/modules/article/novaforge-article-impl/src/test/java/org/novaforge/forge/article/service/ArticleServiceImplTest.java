/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */

package org.novaforge.forge.article.service;

//import static org.hamcrest.Matchers.is;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


//import javax.persistence.TypedQuery;












import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.novaforge.forge.article.dao.ArticleDAO;
import org.novaforge.forge.article.entity.ArticleCategoryContentEntity;
import org.novaforge.forge.article.entity.ArticleCategoryEntity;
import org.novaforge.forge.article.entity.ArticleContentEntity;
import org.novaforge.forge.article.entity.ArticleEntity;
import org.novaforge.forge.article.exception.ArticleCategoryServiceException;
import org.novaforge.forge.article.exception.ArticleServiceException;
import org.novaforge.forge.article.internal.dao.ArticleDAOImpl;
import org.novaforge.forge.article.internal.model.ArticleFilterImpl;
import org.novaforge.forge.article.internal.service.ArticleServiceImpl;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.article.model.ArticleCategoryContent;
import org.novaforge.forge.article.model.ArticleContent;
import org.novaforge.forge.article.model.ArticleFilter;
import org.novaforge.forge.article.model.ArticleType;
import org.novaforge.forge.core.organization.entity.LanguageEntity;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.services.LanguageService;


/**
 * @author IO test
 */
 
public class ArticleServiceImplTest 
{

 
  private Article articleMock; 
  private Locale lang;  
  private ArticleDAO articleDAOMock;
  private LanguageEntity langEntityMock;
  private LanguageService langServiceMock;
  private ArticleFilter articleFilterMock;
  private ArticleCategoryEntity articleCategoryMock;
  private ArticleServiceImpl articleService; 
 
  /**
   * {@inheritDoc}
   */
 
    @Before
  public void setUp() throws Exception 
  {
    
    MockitoAnnotations.initMocks(this);
    
    lang = new Locale("FR");
    
    articleDAOMock = Mockito.mock(ArticleDAOImpl.class);
    articleCategoryMock =  Mockito.spy(new ArticleCategoryEntity());
    articleMock = Mockito.spy(new ArticleEntity());
    langEntityMock = Mockito.spy(new LanguageEntity());
    articleFilterMock =  Mockito.mock(ArticleFilter.class);
       
    articleService =  new ArticleServiceImpl();
    langServiceMock =  Mockito.mock(LanguageService.class);
   
    Mockito.when(langEntityMock.getName()).thenCallRealMethod();
    Mockito.doCallRealMethod().when(langEntityMock).setName("FR");;
    
    Mockito.when(articleMock.getType()).thenCallRealMethod();
    Mockito.doCallRealMethod().when(articleMock).setType(ArticleType.ANNOUCEMENT);
    
    Mockito.when(articleCategoryMock.getColor()).thenCallRealMethod();
    Mockito.doCallRealMethod().when(articleCategoryMock).setColor("RED");    
  }

  /**
   * {@inheritDoc}
   */

  @After
  public void tearDown() throws Exception {
      articleDAOMock = null;
      articleMock = null;
      langEntityMock = null;
      articleCategoryMock = null;
  }
  

  private ArticleContentEntity buildArticleContentEntity(final String pTitle, final String pShortText,
      final String pText, final LanguageEntity pLanguageEntity)
  {
    ArticleContentEntity articleContentEntity = new ArticleContentEntity();
    articleContentEntity.setLanguage(pLanguageEntity);
    articleContentEntity.setTitle(pTitle);
    articleContentEntity.setShortText(pShortText);
    articleContentEntity.setText(pText);   
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
  
    return article;
  }
  
  //@Test
  public void testArticleType()
  {    
    Mockito.when(articleMock.getType()).thenReturn(ArticleType.ANNOUCEMENT);
    
    Assert.assertEquals(ArticleType.ANNOUCEMENT, articleMock.getType()); //OK
    Mockito.verify(articleMock, Mockito.times(1)).getType();
        
    Mockito.doCallRealMethod().when(articleMock).setType(ArticleType.INFORMATION);
    articleMock.setType(ArticleType.INFORMATION);
    
    Assert.assertEquals(ArticleType.INFORMATION, articleMock.getType()); //FAUX , reste ANNOUCEMENT    
    Mockito.verify(articleMock, Mockito.times(1)).setType(ArticleType.INFORMATION);
  }
    
//  @Test
  public void testGetArticleFromFilter() throws LanguageServiceException, ArticleCategoryServiceException, ArticleServiceException
  {

    Date dateToday = new Date();

    articleFilterMock.setArticleType(ArticleType.ANNOUCEMENT);
    articleFilterMock.setTextSearch("mockito testing");
    articleFilterMock.setLocale(lang);
  
    ArticleContentEntity ace = buildArticleContentEntity("pTitle", "pShortText",
  	      "pText", langEntityMock);
    List<ArticleContent> pContent = new ArrayList<ArticleContent>();
    pContent.add(ace);
    
    Mockito.when(articleCategoryMock.getColor()).thenReturn("RED");
    
    Article myArticle = buildArticleEntity(true, articleCategoryMock,
  	     pContent, ArticleType.ANNOUCEMENT, dateToday);
    
    List<Article> listArticles = new ArrayList<Article>();
    listArticles.add(myArticle);
       
    //called in getArticlesFromFilter
    Mockito.when(articleDAOMock.findFromFilter(articleFilterMock, langEntityMock)).thenReturn(listArticles);
    
    //called in getArticlesFromFilter 
    //languageService.getLanguageFromLocale(pArticleFilter.getLocale());
    Mockito.when(articleFilterMock.getLocale()).thenReturn(lang);
    Mockito.when(langServiceMock.getLanguageFromLocale(lang)).thenReturn(langEntityMock);
    Mockito.when(langServiceMock.getDefault()).thenReturn(langEntityMock);
        
    Assert.assertEquals(ArticleType.ANNOUCEMENT, myArticle.getType());
    
    articleService.setArticleDAO(articleDAOMock);
    articleService.setLanguageService(langServiceMock);
    
    List<Article> articlesFilter = articleService.getArticlesFromFilter(articleFilterMock);   
    
    Assert.assertEquals(myArticle.getType(), ((Article) articlesFilter.get(0)).getType());    
    Mockito.verify(articleDAOMock, Mockito.times(1)).findFromFilter(articleFilterMock, langEntityMock);
 
	
  } 
  
  //@Test
  public void testGetArticle() throws ArticleServiceException, LanguageServiceException
  {
    UUID pUuid = UUID.randomUUID();
    Date dateToday = new Date();
    
    ArticleContentEntity ace = buildArticleContentEntity("pTitle", "pShortText",
        "pText", langEntityMock);
    
    List<ArticleContent> pContent = new ArrayList<ArticleContent>();
    pContent.add(ace);
    
    Mockito.when(articleCategoryMock.getColor()).thenReturn("RED");
    
    Article myArticle = buildArticleEntity(true, articleCategoryMock,
         pContent, ArticleType.ANNOUCEMENT, dateToday);
      
    langEntityMock.setName("EN");

    articleService.setArticleDAO(articleDAOMock);
    articleService.setLanguageService(langServiceMock);
    
    Mockito.when(articleDAOMock.findByUUID(pUuid)).thenReturn(myArticle);
    
    Article as = articleService.getArticle(pUuid);    
    Mockito.verify(articleDAOMock, Mockito.times(1)).findByUUID(pUuid); 
  }  
  
 // @Test
  public void testNewArticle() throws ArticleServiceException, LanguageServiceException
  {
    Date dateToday = new Date();
    
    ArticleContentEntity ace = buildArticleContentEntity("pTitle", "pShortText",
        "pText", langEntityMock);
    
    List<ArticleContent> pContent = new ArrayList<ArticleContent>();
    pContent.add(ace);
    
    Mockito.when(articleCategoryMock.getColor()).thenReturn("RED");
    
    Article myArticle = buildArticleEntity(true, articleCategoryMock,
         pContent, ArticleType.ANNOUCEMENT, dateToday);

   
    Mockito.when(articleDAOMock.newArticle()).thenReturn(myArticle);
        
    articleService.setArticleDAO(articleDAOMock);
    articleService.setLanguageService(langServiceMock);
    
    Article as = articleService.newArticle(ArticleType.INFORMATION);
    
    Assert.assertEquals(myArticle.getArticleCategory().getColor(),  as.getArticleCategory().getColor());    
    
    //set language for myArticle
    articleService.setLanguageService(langServiceMock);    
    langEntityMock.setName("FR");
    
    //lang FR
    Assert.assertEquals(myArticle.getArticleContent(lang).getLanguage().getName(), "FR");    
    Mockito.verify(articleDAOMock, Mockito.times(1)).newArticle();
    
  }

  @Test
  public void testCreateArticle() throws ArticleServiceException, LanguageServiceException
  {
    Date dateToday = new Date();
    List<Language> listLanguages = new ArrayList<Language>();
    
    ArticleContentEntity ace = buildArticleContentEntity("pTitle", "pShortText",
        "pText", langEntityMock);
    
    List<ArticleContent> pContent = new ArrayList<ArticleContent>();
    pContent.add(ace);
    
    Mockito.when(articleCategoryMock.getColor()).thenReturn("RED");
    
    Article myArticle = buildArticleEntity(true, articleCategoryMock,
         pContent, ArticleType.ANNOUCEMENT, dateToday);
    
    Mockito.when(langServiceMock.getAllLanguages()).thenReturn(listLanguages);
        
    langEntityMock.setName("FR");
    listLanguages.add(langEntityMock);
    
    articleService.setArticleDAO(articleDAOMock);
    articleService.setLanguageService(langServiceMock);
   
    Mockito.when(articleDAOMock.persist(myArticle, listLanguages)).thenReturn(myArticle);
    
    Article as = articleService.createArticle(myArticle);
    
    Assert.assertEquals(myArticle.getArticleCategory().getColor(),  as.getArticleCategory().getColor());    
    articleService.setLanguageService(langServiceMock);    
    Mockito.verify(articleDAOMock, Mockito.times(1)).persist(myArticle, listLanguages);
  }
  
  //@Test
  public void testUpdateArticle() throws ArticleServiceException, LanguageServiceException
  {
    Date dateToday = new Date();
    List<Language> listLanguages = new ArrayList<Language>();
    
    ArticleContentEntity ace = buildArticleContentEntity("pTitle", "pShortText",
        "pText", langEntityMock);
    
    List<ArticleContent> pContent = new ArrayList<ArticleContent>();
    pContent.add(ace);
    
    Mockito.when(articleCategoryMock.getColor()).thenReturn("RED");
    
    Article myArticle = buildArticleEntity(true, articleCategoryMock,
         pContent, ArticleType.ANNOUCEMENT, dateToday);
    
    Mockito.when(articleDAOMock.update(myArticle)).thenReturn(myArticle);
    
    
    articleService.setArticleDAO(articleDAOMock);
    articleService.setLanguageService(langServiceMock);
    
    Article articleS = articleService.updateArticle(myArticle);
    
    Assert.assertEquals(myArticle.getArticleCategory().getColor(),  articleS.getArticleCategory().getColor());    
    
    //set language for myArticle
    articleService.setLanguageService(langServiceMock);    
    langEntityMock.setName("FR");

//    
//    //lang FR
    Assert.assertEquals(myArticle.getArticleContent(lang).getLanguage().getName(), "FR");    
    Mockito.verify(articleDAOMock, Mockito.times(1)).update(myArticle);
    
  }
  

}