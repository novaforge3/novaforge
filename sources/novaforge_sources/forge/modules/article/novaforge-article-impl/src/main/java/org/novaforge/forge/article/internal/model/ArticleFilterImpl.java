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
package org.novaforge.forge.article.internal.model;

import org.novaforge.forge.article.exception.ArticleCategoryServiceException;
import org.novaforge.forge.article.model.ArticleFilter;
import org.novaforge.forge.article.model.ArticleType;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Gauthier Cart
 */
public class ArticleFilterImpl implements ArticleFilter
{

  private String      textSearch;

  private List<UUID>  listArticleCategoryUuid;

  private Date        beginDate;

  private Date        endDate;

  private Boolean     isPublished;

  private ArticleType articleType;

  private Locale      locale;

  /**
   * @throws ArticleCategoryServiceException
   */
  public ArticleFilterImpl() throws ArticleCategoryServiceException
  {
    super();
    this.textSearch = "";
    this.articleType = ArticleType.NEWS;
    this.beginDate = new Date(0);
    this.endDate = new Date();
    this.locale = Locale.ENGLISH;
    isPublished = null;
  }

  /**
   * @param textSearch
   * @param listArticleCategoryUuid
   * @param beginDate
   * @param endDate
   */
  public ArticleFilterImpl(final String pTextSearch, final List<UUID> pListArticleCategoryUuid,
      final Date pBeginDate, final Date pEndDate, final ArticleType pArticleType, final Locale locale)
  {
    super();
    this.textSearch = pTextSearch;
    this.listArticleCategoryUuid = pListArticleCategoryUuid;
    this.beginDate = pBeginDate;
    this.endDate = pEndDate;
    this.articleType = pArticleType;
    this.locale = locale;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTextSearch()
  {
    return textSearch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTextSearch(String pTextSearch)
  {

    this.textSearch = "%" + pTextSearch + "%";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<UUID> getArticleCategoryUuids()
  {
    return listArticleCategoryUuid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setArticleCategoryUuids(List<UUID> pArticleCategories)
  {
    this.listArticleCategoryUuid = pArticleCategories;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getBeginDate()
  {
    return beginDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBeginDate(Date pBeginDate)
  {
    this.beginDate = pBeginDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getEndDate()
  {
    return endDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEndDate(Date pEndDate)
  {
    this.endDate = pEndDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Locale getLocale()
  {
    return locale;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLocale(Locale pLocale)
  {
    this.locale = pLocale;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean getIsPublished()
  {
    return isPublished;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setIsPublished(Boolean pIsPublished)
  {
    this.isPublished = pIsPublished;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ArticleType getArticleType()
  {
    return articleType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setArticleType(ArticleType pArticleType)
  {
    this.articleType = pArticleType;
  }
}