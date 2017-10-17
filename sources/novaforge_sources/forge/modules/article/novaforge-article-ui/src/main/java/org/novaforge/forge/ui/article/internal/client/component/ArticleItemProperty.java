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

/**
 * This enum defines the property id available for each Article item.
 * 
 * @author Jeremy Casery
 */
public enum ArticleItemProperty
{
  /**
   * Refere to {@link Article#getId()}
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
   * Generated Column
   * 
   * @see Article#getContent#getTitle()
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
   * Refere to {@link Article#getContent()}
   * 
   * @see Article#getContent()
   */
  CONTENT
  {
    @Override
    public String getPropertyId()
    {
      return "content";
    }
  },
  /**
   * Refere to {@link Article#getDate()}
   * 
   * @see Article#getDate()
   */
  DATE
  {
    @Override
    public String getPropertyId()
    {
      return "date";
    }
  },
  /**
   * Refere to {@link Article#getPublished}
   * 
   * @see Article#getPublished
   */
  PUBLISHED
  {
    @Override
    public String getPropertyId()
    {
      return "published";
    }
  },
  /**
   * Refere to {@link Article#getType()}
   * 
   * @see Article#getType()
   */
  TYPE
  {
    @Override
    public String getPropertyId()
    {
      return "type";
    }
  },
  /**
   * Refere to {@link Article#getCategory()}
   * 
   * @see Article#getCategory()
   */
  CATEGORY
  {
    @Override
    public String getPropertyId()
    {
      return "category";
    }
  },
  /**
   * Generated Item Property
   */
  ACTIONS
  {
    @Override
    public String getPropertyId()
    {
      return "actions";
    }
  };
  /**
   * Get ItemPropertyId used by {@link ComboBox}
   * 
   * @return property id
   */
  public abstract String getPropertyId();

}
