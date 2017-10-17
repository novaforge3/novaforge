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
package org.novaforge.forge.ui.article.internal.client.component;

import com.vaadin.ui.ComboBox;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleContent;

/**
 * This enum defines the property id available for each ArticleContent item.
 * 
 * @author Jeremy Casery
 */
public enum ArticleContentItemProperty
{
  /**
   * Refere to {@link ArticleContent#getId()}
   * 
   * @see Article#getId()
   */
  ID
  {
    @Override
    public String getPropertyId()
    {
      return "id";
    }
  },
  /**
   * Refere to {@link ArticleContent()#getTitle()}
   * 
   * @see ArticleContent()#getTitle()
   */
  TITLE
  {
    @Override
    public String getPropertyId()
    {
      return "title";
    }
  },
  /**
   * Refere to {@link ArticleContent#getShortText()}
   * 
   * @see ArticleContent#getShortText()
   */
  SHORTTEXT
  {
    @Override
    public String getPropertyId()
    {
      return "shortText";
    }
  },
  /**
   * Refere to {@link ArticleContent#getText()}
   * 
   * @see ArticleContent#getText()
   */
  TEXT
  {
    @Override
    public String getPropertyId()
    {
      return "text";
    }
  },
  /**
   * Refere to {@link ArticleContent#getLanguage()}
   * 
   * @see ArticleContent#getLanguage()
   */
  LANGUAGE
  {
    @Override
    public String getPropertyId()
    {
      return "language";
    }
  };
  /**
   * Get ItemPropertyId used by {@link ComboBox}
   * 
   * @return property id
   */
  public abstract String getPropertyId();

}
