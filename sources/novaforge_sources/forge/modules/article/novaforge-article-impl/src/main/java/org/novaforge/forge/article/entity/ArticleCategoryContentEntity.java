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

import org.novaforge.forge.article.model.ArticleCategoryContent;
import org.novaforge.forge.core.organization.entity.LanguageEntity;
import org.novaforge.forge.core.organization.model.Language;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Gauthier Cart
 */
@Entity
@Table(name = "ARTICLE_CATEGORY_CONTENT")
public class ArticleCategoryContentEntity implements ArticleCategoryContent, Serializable
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = -2045632337870050248L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "title", nullable = false)
  private String            title;

  @ManyToOne(targetEntity = LanguageEntity.class, fetch = FetchType.EAGER)
  private Language          language;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTitle()
  {
    return title;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTitle(String pTitle)
  {
    this.title = pTitle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Language getLanguage()
  {
    return language;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLanguage(Language pLanguage)
  {
    this.language = pLanguage;

  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((language == null) ? 0 : language.hashCode());
    result = prime * result + ((title == null) ? 0 : title.hashCode());
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
    ArticleCategoryContentEntity other = (ArticleCategoryContentEntity) obj;
    if (id == null)
    {
      if (other.id != null)
        return false;
    }
    else if (!id.equals(other.id))
      return false;
    if (language == null)
    {
      if (other.language != null)
        return false;
    }
    else if (!language.equals(other.language))
      return false;
    if (title == null)
    {
      if (other.title != null)
        return false;
    }
    else if (!title.equals(other.title))
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "ArticleCategoryEntity [title=" + title + ", language=" + language + "]";
  }
}
