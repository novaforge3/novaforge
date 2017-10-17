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

import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.article.model.ArticleCategoryContent;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Jeremy Casery
 * @author Gauthier Cart
 */
@Entity
@Table(name = "ARTICLE_CATEGORY")
@NamedQueries({
    @NamedQuery(name = "ArticleCategoryEntity.findByUUID",
        query = "SELECT ac FROM ArticleCategoryEntity ac WHERE ac.uuid = :uuid"),
    @NamedQuery(name = "ArticleCategoryEntity.findAll", query = "SELECT ac FROM ArticleCategoryEntity ac"), })
public class ArticleCategoryEntity implements ArticleCategory, Serializable
{

  /**
   * Serial version id
   */
  private static final long            serialVersionUID = 929484268297934751L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long                         id;

  @Column(name = "uuid", unique = true, nullable = false, insertable = true, updatable = false)
  private String                       uuid;

  @Column(name = "color", nullable = false)
  private String                       color;

  @OneToMany(targetEntity = ArticleCategoryContentEntity.class, fetch = FetchType.EAGER, cascade = {
      CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, orphanRemoval = true)
  @JoinTable(joinColumns = @JoinColumn(name = "article_category_id"), inverseJoinColumns = @JoinColumn(
      name = "article_category_content_id"))
  private List<ArticleCategoryContent> content          = new ArrayList<>();

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
  @Override
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
  public List<ArticleCategoryContent> getArticleCategoryContents()
  {
    return content;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addArticleCategoryContent(ArticleCategoryContent pArticleCategoryContent)
  {
    content.add(pArticleCategoryContent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeContent(ArticleCategoryContent pArticleCategoryContent)
  {
    content.remove(pArticleCategoryContent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ArticleCategoryContent getCategoryContent(final Locale pLocale)
  {
    ArticleCategoryContent returnArticleCategoryContent = null;
    if (pLocale != null)
    {
      for (ArticleCategoryContent articleCategoryContent : content)
      {
        if ((articleCategoryContent.getLanguage() != null)
            && (pLocale.equals(articleCategoryContent.getLanguage().getLocale())))
        {
          returnArticleCategoryContent = articleCategoryContent;
          break;
        }
      }
    }
    return returnArticleCategoryContent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getColor()
  {
    return color;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setColor(String pColor)
  {
    this.color = pColor;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((color == null) ? 0 : color.hashCode());
    result = prime * result + ((content == null) ? 0 : content.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ArticleCategoryEntity other = (ArticleCategoryEntity) obj;
    if (color == null)
    {
      if (other.color != null)
        return false;
    }
    else if (!color.equals(other.color))
      return false;
    if (content == null)
    {
      if (other.content != null)
        return false;
    }
    else if (!content.equals(other.content))
      return false;
    if (id == null)
    {
      if (other.id != null)
        return false;
    }
    else if (!id.equals(other.id))
      return false;
    if (uuid == null)
    {
      if (other.uuid != null)
        return false;
    }
    else if (!uuid.equals(other.uuid))
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "ArticleCategoryEntity [id=" + id + ", uuid=" + uuid + ", color=" + color + ", content=" + content + "]";
  }

}
