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
package org.novaforge.forge.distribution.reference.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author rols-p
 */
public class ApplicationNode
{

  private String                      uri;
  private String                      name;
  private String                      type;
  private String                      category;

  private Set<ApplicationRoleMapping> roleMappings = new HashSet<ApplicationRoleMapping>();

  public ApplicationNode()
  {
  }

  /**
   * @param pName
   * @param pUri
   */
  public ApplicationNode(final String pUri, final String pName)
  {
    uri = pUri;
    name = pName;
  }

  /**
   * @return the uri
   */
  public String getUri()
  {
    return uri;
  }

  /**
   * @param pUri
   *          the uri to set
   */
  public void setUri(final String pUri)
  {
    uri = pUri;
  }

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param pName
   *          the name to set
   */
  public void setName(final String pName)
  {
    name = pName;
  }

  /**
   * @return the roleMappings
   */
  public Set<ApplicationRoleMapping> getRoleMappings()
  {
    return roleMappings;
  }

  /**
   * @param roleMappings
   *          the roleMappings to set
   */
  public void setRoleMappings(final Set<ApplicationRoleMapping> roleMappings)
  {
    this.roleMappings = roleMappings;
  }

  /**
   * @return the type
   */
  public String getType()
  {
    return type;
  }

  /**
   * @param type
   *          the type to set
   */
  public void setType(final String type)
  {
    this.type = type;
  }

  /**
   * @return the category
   */
  public String getCategory()
  {
    return category;
  }

  /**
   * @param category
   *          the category to set
   */
  public void setCategory(final String category)
  {
    this.category = category;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((uri == null) ? 0 : uri.hashCode());
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
    if (getClass() != obj.getClass())
    {
      return false;
    }
    final ApplicationNode other = (ApplicationNode) obj;
    if (uri == null)
    {
      if (other.uri != null)
      {
        return false;
      }
    }
    else if (!uri.equals(other.uri))
    {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "ApplicationNode [uri=" + uri + ", name=" + name + ", type=" + type + ", category=" + category
        + ", roleMappings=" + roleMappings + "]";
  }

}
