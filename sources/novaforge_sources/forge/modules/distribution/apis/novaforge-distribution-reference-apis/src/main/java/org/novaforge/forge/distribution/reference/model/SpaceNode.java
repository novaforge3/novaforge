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

import java.util.ArrayList;
import java.util.List;

/**
 * @author rols-p
 */
public class SpaceNode
{
  private String                uri;
  private String                name;
  private List<SpaceNode>       children     = new ArrayList<SpaceNode>();
  private List<ApplicationNode> applications = new ArrayList<ApplicationNode>();

  public SpaceNode()
  {
  }

  /**
   * @param pName
   * @param pUri
   */
  public SpaceNode(final String pUri, final String pName)
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
   * @param uri
   *          the uri to set
   */
  public void setUri(final String uri)
  {
    this.uri = uri;
  }

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(final String name)
  {
    this.name = name;
  }

  /**
   * @return the children
   */
  public List<SpaceNode> getChildren()
  {
    return children;
  }

  /**
   * @param children
   *          the children to set
   */
  public void setChildren(final List<SpaceNode> children)
  {
    this.children = children;
  }

  /**
   * @return the applications
   */
  public List<ApplicationNode> getApplications()
  {
    return applications;
  }

  /**
   * @param applications
   *          the applications to set
   */
  public void setApplications(final List<ApplicationNode> applications)
  {
    this.applications = applications;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((name == null) ? 0 : name.hashCode());
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
    final SpaceNode other = (SpaceNode) obj;
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
    return "SpaceNodeDTO [uri=" + uri + ", name=" + name + ", children=" + children + ", applications="
        + applications + "]";
  }

}
