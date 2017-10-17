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
package org.novaforge.forge.article.entity;

import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.article.model.ArticleContent;
import org.novaforge.forge.article.model.ArticleType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Jeremy Casery
 * @author Gauthier Cart
 */
@Entity
@Table(name = "ARTICLE")
@NamedQuery(name = "ArticleEntity.findByUUID", query = "SELECT a FROM ArticleEntity a WHERE a.uuid = :uuid")
public class ArticleEntity implements Article, Serializable
{

  /**
   * Serial version id
   */
  private static final long    serialVersionUID = 6970279568939139509L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long                 id;

  @Column(name = "uuid", unique = true, nullable = false, insertable = true, updatable = false)
  private String               uuid;

  @Column(name = "date", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date                 date;

  @Column(name = "published", nullable = false)
  private boolean              published;

  @Column(name = "type", nullable = false)
  @Enumerated
  private ArticleType          type;

  @ManyToOne(targetEntity = ArticleCategoryEntity.class, fetch = FetchType.EAGER, cascade = {
      CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
  private ArticleCategory      category;

  @OneToMany(targetEntity = ArticleContentEntity.class, fetch = FetchType.EAGER, cascade = {
      CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, orphanRemoval = true)
  @JoinTable(joinColumns = @JoinColumn(name = "article_id"), inverseJoinColumns = @JoinColumn(
      name = "article_content_id"))
  private List<ArticleContent> content          = new ArrayList<>();

  /**
   * This will be called before a persist and flush event
   */
  @PrePersist
  public void onPersist()
  {
    setUuid(UUID.randomUUID());
  }

  /**
   * {@inheritDoc}
   */
  public UUID getUuid()
  {
    return UUID.fromString(uuid);
  }

  public void setUuid(UUID uuid)
  {
    this.uuid = uuid.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ArticleContent> getArticleContents()
  {
    return Collections.unmodifiableList(content);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addArticleContent(ArticleContent pArticleContent)
  {
    content.add(pArticleContent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeArticleContent(ArticleContent pArticleContent)
  {
    content.remove(pArticleContent);
  }

  /**
   * @return the content
   */
  @Override
  public ArticleContent getArticleContent(final Locale pLocale)
  {
    ArticleContent returnArticleContent = null;
    if (pLocale != null)
    {
      for (ArticleContent articleContent : content)
      {
        if ((articleContent.getLanguage() != null) && (pLocale.equals(articleContent.getLanguage().getLocale())))
        {
          returnArticleContent = articleContent;
          break;
        }
      }
    }
    return returnArticleContent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getDate()
  {
    return date;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDate(Date date)
  {
    this.date = date;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPublished()
  {
    return published;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPublished(boolean published)
  {
    this.published = published;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ArticleCategory getArticleCategory()
  {
    return category;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setArticleCategory(ArticleCategory pArticleCategory)
  {
    this.category = pArticleCategory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ArticleType getType()
  {
    return type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setType(ArticleType pType)
  {
    this.type = pType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
    result = prime * result + ((content == null) ? 0 : content.hashCode());
    result = prime * result + ((category == null) ? 0 : category.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + (published ? 1231 : 1237);
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ArticleEntity other = (ArticleEntity) obj;
    if (uuid == null)
    {
      if (other.uuid != null)
        return false;
    }
    else if (!uuid.equals(other.uuid))
      return false;
    if (category == null)
    {
      if (other.category != null)
        return false;
    }
    else if (!category.equals(other.category))
      return false;
    if (category == null)
    {
      if (other.category != null)
        return false;
    }
    else if (!category.equals(other.category))
      return false;
    if (content == null)
    {
      if (other.content != null)
        return false;
    }
    else if (!content.equals(other.content))
      return false;
    if (date == null)
    {
      if (other.date != null)
        return false;
    }
    else if (!date.equals(other.date))
      return false;
    if (published != other.published)
      return false;
    if (type == null)
    {
      if (other.type != null)
        return false;
    }
    else if (!type.equals(other.type))
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "ArticleEntity [uuid=" + uuid + ", date=" + date + ", published=" + published + ", type=" + type
               + ", category=" + category + ", content=" + content + "]";
  }

}
