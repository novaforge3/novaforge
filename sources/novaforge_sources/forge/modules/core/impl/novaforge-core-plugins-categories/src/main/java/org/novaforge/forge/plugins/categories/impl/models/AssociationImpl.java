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
package org.novaforge.forge.plugins.categories.impl.models;

import org.novaforge.forge.core.plugins.categories.Association;
import org.novaforge.forge.core.plugins.categories.AssociationType;
import org.novaforge.forge.core.plugins.categories.Category;

/**
 * It is a default implementation of {@link Association}
 * 
 * @author Guillaume Lamirand
 */
public class AssociationImpl implements Association
{
  private final AssociationType type;
  private final String          name;
  private final Category        category;

  /**
   * Default constructor
   * 
   * @param pType
   * @param pName
   * @param pCategory
   */
  public AssociationImpl(final AssociationType pType, final String pName, final Category pCategory)
  {
    super();
    type = pType;
    name = pName;
    category = pCategory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AssociationType getType()
  {
    return type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Category getCategory()
  {
    return category;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((category == null) ? 0 : category.hashCode());
    result = (prime * result) + ((name == null) ? 0 : name.hashCode());
    result = (prime * result) + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (!(obj instanceof AssociationImpl))
    {
      return false;
    }
    final AssociationImpl other = (AssociationImpl) obj;
    if (category != other.category)
    {
      return false;
    }
    if (name == null)
    {
      if (other.name != null)
      {
        return false;
      }
    }
    else if (!name.equals(other.name))
    {
      return false;
    }
    return type == other.type;
  }

}
