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
public class ReferenceTemplate
{
  private String             id;
  private String             name;
  private String             description;
  private RootNode           rootNode;
  private List<TemplateRole> roles = new ArrayList<TemplateRole>();
  private RefTemplateStatus  status;

  public ReferenceTemplate()
  {
    super();
  }

  /**
   * @return
   */
  public String getId()
  {
    return id;
  }

  /**
   * @param idProject
   */
  public void setId(final String id)
  {
    this.id = id;
  }

  /**
   * @return
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param projectName
   */
  public void setName(final String pName)
  {
    name = pName;
  }

  /**
   * @return
   */
  public String getDescription()
  {

    return description;
  }

  /**
   * @param projectDescription
   */
  public void setDescription(final String pDescription)
  {
    description = pDescription;
  }

  /**
   * @return
   */
  public RootNode getRootNode()
  {
    return rootNode;
  }

  /**
   * @param rootNode
   */
  public void setRootNode(final RootNode rootNode)
  {
    this.rootNode = rootNode;
  }

  /**
   * @return the roles
   */
  public List<TemplateRole> getRoles()
  {
    return roles;
  }

  /**
   * @param roles
   *          the roles to set
   */
  public void setRoles(final List<TemplateRole> roles)
  {
    this.roles = roles;
  }

  /**
   * @return the status
   */
  public RefTemplateStatus getStatus()
  {
    return status;
  }

  /**
   * @param status
   *          the status to set
   */
  public void setStatus(final RefTemplateStatus status)
  {
    this.status = status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((id == null) ? 0 : id.hashCode());
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
    final ReferenceTemplate other = (ReferenceTemplate) obj;
    if (id == null)
    {
      if (other.id != null)
      {
        return false;
      }
    }
    else if (!id.equals(other.id))
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
    return "ReferenceTemplate [id=" + id + ", name=" + name + ", description=" + description + ", rootNode=" + rootNode
               + ", roles=" + roles + ", status=" + status + "]";
  }

  public enum RefTemplateStatus
  {
    IN_PROGRESS, ENABLE
  }

}
