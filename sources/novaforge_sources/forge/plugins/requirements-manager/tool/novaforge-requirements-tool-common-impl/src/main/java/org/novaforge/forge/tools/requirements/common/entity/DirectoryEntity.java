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
package org.novaforge.forge.tools.requirements.common.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.apache.openjpa.persistence.FetchGroups;
import org.novaforge.forge.tools.requirements.common.model.IDirectory;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Guillaume Morin
 */

@Entity
@Table(name = "DIRECTORY")
@NamedQueries({
    @NamedQuery(
        name = "DirectoryEntity.findAllRootDirectoriesByRepository",
        query = "SELECT e FROM DirectoryEntity e  WHERE e.repository.URI = :repositoryURI AND e.repository.project.projectId = :projectID AND  e.parentDirectory IS NULL"),
    @NamedQuery(name = "DirectoryEntity.findDirectoryByReference",
        query = "SELECT e FROM DirectoryEntity e WHERE e.reference = :reference") })
@FetchGroups({ @FetchGroup(name = "directories_all", attributes = { @FetchAttribute(
    name = "childrenDirectories", recursionDepth = -1) }) })
public class DirectoryEntity implements IDirectory, Serializable
{
  private static final long  serialVersionUID    = 7444842259505478402L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long               id;

  @Column(name = "description", nullable = true)
  private String             description;

  @Column(name = "name", nullable = false)
  private String             name;

  // Linked to one repository
  @ManyToOne(targetEntity = RepositoryEntity.class)
  @JoinColumn(name = "repository_id", nullable = false, insertable = true, updatable = true)
  private IRepository        repository;

  @Column(name = "reference", nullable = false, updatable = false)
  private String             reference;

  /***********************************
   * ------- Tables Relations ------
   **********************************/

  // Linked to many Requirements (stored in one composite directory)
  @OneToMany(mappedBy = "directory", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE,
      CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH },
      targetEntity = RequirementEntity.class, orphanRemoval = true)
  private List<IRequirement> requirements        = new ArrayList<IRequirement>();

  // Linked to one parent directory
  @ManyToOne(targetEntity = DirectoryEntity.class)
  @JoinColumn(name = "parent_id", nullable = true, insertable = true, updatable = true)
  private IDirectory         parentDirectory;

  // Linked to many sub directory (children directory)
  @OneToMany(mappedBy = "parentDirectory", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE,
      CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH },
      targetEntity = DirectoryEntity.class, orphanRemoval = true)
  private Set<IDirectory>    childrenDirectories = new HashSet<IDirectory>();

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(getReference()).toHashCode();
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof DirectoryEntity))
    {
      return false;
    }
    final DirectoryEntity castOther = (DirectoryEntity) other;
    return new EqualsBuilder().append(getReference(), castOther.getReference()).isEquals();
  }

  @Override
  public String toString()
  {
    return "DirectoryEntity [id=" + id + ", description=" + description + ", name=" + name + ", repositoryURI="
               + repository.getURI() + ", reference=" + reference + ", parentDirectoryName=" + parentDirectory.getName()
               + ", childrenDirectory=" + getChildrenDirectories() + "]";
  }

  @Override
  public String getDescription()
  {
    return description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDescription(final String pDescription)
  {
    description = pDescription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void setName(final String pName)
  {
    name = pName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<IDirectory> getChildrenDirectories()
  {
    return Collections.unmodifiableSet(childrenDirectories);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addDirectory(final IDirectory pDirectory)
  {
    pDirectory.setParent(this);
    pDirectory.setRepository(repository);
    childrenDirectories.add(pDirectory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteDirectory(final IDirectory pDirectory)
  {
    childrenDirectories.remove(pDirectory);
    pDirectory.setParent(null);
    pDirectory.setRepository(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteRequirement(final IRequirement pRequirement)
  {
    requirements.remove(pRequirement);
    pRequirement.setDirectory(null);
  }

  @Override
  public IRepository getRepository()
  {
    return repository;
  }

  @Override
  public void setRepository(final IRepository pRepository)
  {
    repository = pRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addRequirement(final IRequirement pRequirement)
  {
    pRequirement.setDirectory(this);
    requirements.add(pRequirement);
  }

  @Override
  public Set<IRequirement> getRequirements()
  {
    return Collections.unmodifiableSet(new HashSet<IRequirement>(requirements));
  }

  @Override
  public IDirectory getParent()
  {
    return parentDirectory;
  }

  @Override
  public void setParent(final IDirectory pParentDirectory)
  {
    parentDirectory = pParentDirectory;
    repository = parentDirectory.getRepository();
  }

  @Override
  public IDirectory findDirectoryByName(final String pName)
  {
    IDirectory directory = null;

    for (final IDirectory dir : getChildrenDirectories())
    {
      if (pName.equalsIgnoreCase(dir.getName()))
      {
        directory = dir;
        break;
      }
    }
    return directory;
  }

  @Override
  public IDirectory findDirectoryByReference(final String pReference)
  {
    IDirectory ret = null;

    for (final IDirectory directory : getChildrenDirectories())
    {
      final String reference = directory.getReference();
      if (pReference.equals(reference))
      {
        ret = directory;
        break;
      }
    }
    return ret;
  }

  @Override
  public String getPath()
  {
    IDirectory parent = getParent();
    String path = name;
    while (parent != null)
    {
      path = parent.getName() + "/" + path;
      parent = parent.getParent();
    }
    return "/" + path;
  }

  @Override
  public IRequirement findRequirementByReference(final String pFunctionalKey)
  {
    IRequirement returnedRequiremeent = null;

    for (final IRequirement requirement : requirements)
    {
      final String reference = requirement.getReference();
      if (pFunctionalKey.equals(reference))
      {
        returnedRequiremeent = requirement;
        break;
      }
    }
    return returnedRequiremeent;
  }

  @Override
  public String getReference()
  {
    return reference;
  }

  @Override
  public void setReference(final String pReference)
  {
    reference = pReference;
  }

  /**
   * @param pRequirements
   *          the requirements to set
   */
  public void setRequirements(final List<IRequirement> pRequirements)
  {
    requirements = pRequirements;
  }

  /**
   * @param pChildrenDirectories
   *     the childrenDirectories to set
   */
  public void setChildrenDirectories(final Set<IDirectory> pChildrenDirectories)
  {
    childrenDirectories = pChildrenDirectories;
  }

}
