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

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Franck Petretto
 */

public class DirectoryBeanImpl implements DirectoryBean
{
  private static final long serialVersionUID = 4970614567314261341L;
  private final Set<RequirementBean> fDRequirements;
  private final Set<DirectoryBean>   fChildrenDirectory;
  private String        fDescription;
  private String        fName;
  private String        fRepositoryName;
  private DirectoryBean fParentDirectory;

  public DirectoryBeanImpl()
  {
    fChildrenDirectory = new HashSet<DirectoryBean>();
    fDRequirements = new HashSet<RequirementBean>();
  }

  @Override
  public String getDescription()
  {
    return fDescription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDescription(final String pDescription)
  {
    fDescription = pDescription;
  }

  @Override
  public String getName()
  {
    return fName;
  }

  @Override
  public void setName(final String pName)
  {
    fName = pName;
  }

  @Override
  public Set<DirectoryBean> findSubDirectories()
  {
    return Collections.unmodifiableSet(fChildrenDirectory);
  }

  @Override
  public boolean addDirectory(final DirectoryBean pDirectory)
  {
    return fChildrenDirectory.add(pDirectory);
  }

  @Override
  public boolean addRequirement(final RequirementBean pRequirement)
  {
    return fDRequirements.add(pRequirement);
  }

  @Override
  public Set<RequirementBean> findRequirements()
  {
    return Collections.unmodifiableSet(fDRequirements);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(fDRequirements, fChildrenDirectory, fDescription, fName, fRepositoryName, fParentDirectory);
  }

  @Override
  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    final DirectoryBeanImpl that = (DirectoryBeanImpl) o;
    return Objects.equals(fDRequirements, that.fDRequirements) &&
               Objects.equals(fChildrenDirectory, that.fChildrenDirectory) &&
               Objects.equals(fDescription, that.fDescription) &&
               Objects.equals(fName, that.fName) &&
               Objects.equals(fRepositoryName, that.fRepositoryName) &&
               Objects.equals(fParentDirectory, that.fParentDirectory);
  }

}
