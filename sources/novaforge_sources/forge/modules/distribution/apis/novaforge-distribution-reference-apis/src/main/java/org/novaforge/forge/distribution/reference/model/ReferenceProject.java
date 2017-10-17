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
 * @author salvat-a
 */
public class ReferenceProject
{
  private String                          idProject;
  private String                          name;
  private String                          description;
  private String                          licence;
  private RootNode                        rootNode;
  private String                          memberRoleName;

  private List<ApplicationItemReferences> pluginItemReferences;

  public ReferenceProject()
  {
    super();
  }

  /**
   * @return
   */
  public String getId()
  {
    return idProject;
  }

  /**
   * @param id
   */
  public void setId(final String id)
  {
    idProject = id;
  }

  /**
   * @return
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param pName
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
   * @param pDescription
   */
  public void setDescription(final String pDescription)
  {
    description = pDescription;
  }

  /**
   * @return
   */
  public String getLicence()
  {
    return licence;

  }

  /**
   * @param licence
   */
  public void setLicence(final String licence)
  {
    this.licence = licence;
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
   * @return the emberRoleName
   */
  public String getMemberRoleName()
  {
    return memberRoleName;
  }

  /**
   * @param emberRoleName
   *          the emberRoleName to set
   */
  public void setMemberRoleName(final String emberRoleName)
  {
    memberRoleName = emberRoleName;
  }

  public List<ApplicationItemReferences> getPluginItemReferences()
  {
    if (pluginItemReferences == null)
    {
      pluginItemReferences = new ArrayList<ApplicationItemReferences>();
    }
    return pluginItemReferences;
  }

  public void setPluginItemReferences(final List<ApplicationItemReferences> pluginItemReferences)
  {
    this.pluginItemReferences = pluginItemReferences;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((idProject == null) ? 0 : idProject.hashCode());
    result = (prime * result) + ((name == null) ? 0 : name.hashCode());
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
    final ReferenceProject other = (ReferenceProject) obj;
    if (idProject == null)
    {
      if (other.idProject != null)
      {
        return false;
      }
    }
    else if (!idProject.equals(other.idProject))
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
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "ProjectDTO [idProject=" + idProject + ", name=" + name + ", description=" + description
        + ", licence=" + licence + ", rootNode=" + rootNode + ", memberRoleName=" + memberRoleName + "]";
  }

}
