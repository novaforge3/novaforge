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

/**
 * @author rols-p
 */
public class ApplicationRoleMapping
{

  private String projectRole;
  private String applicationRole;

  public ApplicationRoleMapping()
  {
  }

  /**
   * @param projectRole
   * @param applicationRole
   */
  public ApplicationRoleMapping(final String projectRole, final String applicationRole)
  {
    this.projectRole = projectRole;
    this.applicationRole = applicationRole;
  }

  /**
   * @return the projectRole
   */
  public String getProjectRole()
  {
    return projectRole;
  }

  /**
   * @param projectRole
   *          the projectRole to set
   */
  public void setProjectRole(final String projectRole)
  {
    this.projectRole = projectRole;
  }

  /**
   * @return the applicationRole
   */
  public String getApplicationRole()
  {
    return applicationRole;
  }

  /**
   * @param applicationRole
   *          the applicationRole to set
   */
  public void setApplicationRole(final String applicationRole)
  {
    this.applicationRole = applicationRole;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((applicationRole == null) ? 0 : applicationRole.hashCode());
    result = (prime * result) + ((projectRole == null) ? 0 : projectRole.hashCode());
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
    final ApplicationRoleMapping other = (ApplicationRoleMapping) obj;
    if (applicationRole == null)
    {
      if (other.applicationRole != null)
      {
        return false;
      }
    }
    else if (!applicationRole.equals(other.applicationRole))
    {
      return false;
    }
    if (projectRole == null)
    {
      if (other.projectRole != null)
      {
        return false;
      }
    }
    else if (!projectRole.equals(other.projectRole))
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
    return "ApplicationRoleMappingDTO [projectRole=" + projectRole + ", applicationRole=" + applicationRole
        + "]";
  }
}
