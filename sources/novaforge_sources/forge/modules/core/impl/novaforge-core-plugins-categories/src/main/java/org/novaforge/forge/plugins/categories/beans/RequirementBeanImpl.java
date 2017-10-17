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
package org.novaforge.forge.plugins.categories.beans;

import org.novaforge.forge.core.plugins.categories.requirementsmanagement.DirectoryBean;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementBean;

/**
 * @author Franck Petretto
 */

public class RequirementBeanImpl implements RequirementBean
{
  private static final long serialVersionUID = 6595487535101237535L;

  private String            fName;
  private String            fFunctionalReference;
  private String            fDescription;
  private DirectoryBean     fDirectory;
  private int               fLastRequirementVersionId;

  public RequirementBeanImpl()
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return fName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setName(final String pName)
  {
    fName = pName;
  }

  @Override
  public String getReference()
  {
    return fFunctionalReference;
  }

  @Override
  public void setReference(final String pFunctionalId)
  {
    fFunctionalReference = pFunctionalId;
  }

  @Override
  public String getDescription()
  {
    return fDescription;
  }

  @Override
  public void setDescription(final String pDescription)
  {
    fDescription = pDescription;
  }

  @Override
  public int getLastRequirementVersionId()
  {
    return fLastRequirementVersionId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLastRequirementVersionId(final int pLastRequirementVersionId)
  {
    fLastRequirementVersionId = pLastRequirementVersionId;
  }

  public DirectoryBean getDirectory()
  {
    return fDirectory;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((fDirectory == null) ? 0 : fDirectory.hashCode());
    result = (prime * result) + ((fName == null) ? 0 : fName.hashCode());
    return result;
  }

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
    final RequirementBeanImpl other = (RequirementBeanImpl) obj;
    if (fDirectory == null)
    {
      if (other.fDirectory != null)
      {
        return false;
      }
    }
    else if (!fDirectory.equals(other.fDirectory))
    {
      return false;
    }
    if (fName == null)
    {
      if (other.fName != null)
      {
        return false;
      }
    }
    else if (!fName.equals(other.fName))
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "[RQ:" + ":NAME" + fName + ":REF" + fFunctionalReference + "]";
  }







}
