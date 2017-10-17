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
import org.novaforge.forge.tools.requirements.common.model.ERequirementType;
import org.novaforge.forge.tools.requirements.common.model.IDirectory;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.tools.requirements.common.model.IRequirementVersion;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Guillaume Morin
 */

// TODO Put index on functional_reference to optimize (JPA do not have @index ? )
// TODO see to link requirement setter and low level setter inclusion

@Entity
@Table(name = "REQUIREMENT")
@FetchGroups({ @FetchGroup(name = "versions_all", attributes = { @FetchAttribute(
    name = "requirementVersions", recursionDepth = -1) }) })
@NamedQueries({
    @NamedQuery(name = "RequirementEntity.loadRequirementTree",
        query = "select s from RequirementEntity s WHERE s.id=:ID"),
    @NamedQuery(name = "RequirementEntity.loadRequirementTreeByReference",
        query = "select s from RequirementEntity s where s.functionalReference=:REF"),
    @NamedQuery(name = "RequirementEntity.loadRequirementTreeById",
        query = "select s from RequirementEntity s where s.id=:ID"),
    @NamedQuery(
        name = "RequirementEntity.loadRequirementsByRepository",
        query = "select s from RequirementEntity s join fetch s.directory WHERE s.directory.repository.URI=:repositoryURI AND s.projectId=:projectID"),
    @NamedQuery(name = "RequirementEntity.deleteByReference",
        query = "delete from RequirementEntity s  WHERE s.functionalReference=:REF"),
    @NamedQuery(name = "RequirementEntity.loadRequirementTreeByName",
        query = "select s from RequirementEntity s where s.name=:name AND s.projectId=:projectID") })
public class RequirementEntity implements IRequirement, Serializable
{
  private static final long        serialVersionUID    = 6595487535101237535L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long                     id;

  @Column(length = 500, name = "name", nullable = false)
  private String                   name;

  /** Size must be projet_id's size + name's size .*/
  @Column(length = 755,name = "reference", nullable = false, updatable = false)
  private String                   functionalReference;

  @Column(length = 20, name = "type", nullable = false, updatable = true)
  private String                   type                = ERequirementType.UNDEFINED.getLabel();

  @Column(length = 1024, name = "rationale", nullable = true, updatable = true)
  private String                   rationale;

  @Column(length = 1024, name = "acceptance_criteria", nullable = true, updatable = true)
  private String                   acceptanceCriteria;

  @Column(name = "subtype", nullable = true, updatable = true)
  private String                   subType;

  @Lob
  @Column(name = "description", nullable = true, updatable = true)
  private String                   description;

  @Column(name = "status", nullable = true, updatable = true)
  private String                   status;

  @Column(length = 255, name = "project_id", nullable = true, updatable = false)
  private String                   projectId;

  /***********************************
   * ------- Tables Relations ------
   **********************************/

  // Linked to one directory
  @ManyToOne(targetEntity = DirectoryEntity.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "directory_id", nullable = true, insertable = true, updatable = true)
  private IDirectory               directory;

  /**
   * By default we load with a LAZY FetchType to provide only a high level requirement tree
   * (name,statement,etc....)
   */
  // Linked to many version root.
  @OneToMany(mappedBy = "requirement", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE,
      CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH },
      targetEntity = RequirementVersionEntity.class, orphanRemoval = true)
  private Set<IRequirementVersion> requirementVersions = new HashSet<IRequirementVersion>();

  @ManyToMany(targetEntity = RequirementEntity.class, fetch = FetchType.EAGER, cascade = {
      CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
  @JoinTable(name = "REQUIREMENTS_RELATIONSHIP", joinColumns = @JoinColumn(name = "parent_id"),
      inverseJoinColumns = @JoinColumn(name = "child_id"))
  private Set<IRequirement>        children            = new HashSet<IRequirement>();

  @Override
  public Set<IRequirementVersion> getRequirementVersions()
  {
    return Collections.unmodifiableSet(requirementVersions);
  }

  @Override
  public IRequirementVersion findRequirementVersion(final int pVersion)
  {
    IRequirementVersion returnedVersion = null;
    for (final IRequirementVersion v : requirementVersions)
    {
      if (v.getCurrentVersion() == pVersion)
      {
        returnedVersion = v;
        break;
      }
    }
    return returnedVersion;
  }

  @Override
  public boolean addRequirementVersion(final IRequirementVersion pRequirementversion)
  {
    /**
     * i dont like that.can we have another way to do that ?
     */
    pRequirementversion.setRequirement(this);
    return requirementVersions.add(pRequirementversion);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProjectId()
  {
    return projectId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProjectId(final String pProjectId)
  {
    projectId = pProjectId;
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
  public void setName(final String pName)
  {
    name = pName;
  }

  @Override
  public String getType()
  {
    return type;
  }

  @Override
  public void setType(final String pCategory)
  {
    type = pCategory;
  }

  @Override
  public IDirectory getDirectory()
  {
    return directory;
  }

  @Override
  public void setDirectory(final IDirectory pDirectory)
  {
    directory = pDirectory;
  }

  @Override
  public Long getId()
  {
    return id;
  }

  @Override
  public IRequirementVersion findLastRequirementVersion()
  {
    final TreeSet<IRequirementVersion> s = new TreeSet<IRequirementVersion>(requirementVersions);
    return s.last();
  }

  @Override
  public String getStatus()
  {
    return status;
  }

  @Override
  public void setStatus(final String pStatus)
  {
    status = pStatus;
  }

  @Override
  public String getRationale()
  {
    return rationale;
  }

  @Override
  public void setRationale(final String pRationale)
  {
    rationale = pRationale;
  }

  @Override
  public String getAcceptanceCriteria()
  {
    return acceptanceCriteria;
  }

  @Override
  public void setAcceptanceCriteria(final String pAcceptanceCriteria)
  {
    acceptanceCriteria = pAcceptanceCriteria;
  }

  @Override
  public String getSubType()
  {
    return subType;
  }

  @Override
  public void setSubType(final String pSubType)
  {
    subType = pSubType;
  }

  @Override
  public String getReference()
  {
    return functionalReference;
  }

  @Override
  public void setReference(final String pFunctionalId)
  {
    functionalReference = pFunctionalId;
  }

  @Override
  public String getDescription()
  {
    return description;
  }

  @Override
  public void setDescription(final String pDescription)
  {
    description = pDescription;
  }

  @Override
  public void addChild(final IRequirement pChild)
  {
    children.add(pChild);
  }

  @Override
  public Set<IRequirement> getChildren()
  {
    return children;
  }

  /**
   * @param pChildren
   *          the children to set
   */
  public void setChildren(final Set<IRequirement> pChildren)
  {
    children = pChildren;
  }

  /**
   * @param pRequirementVersions
   *     the requirementVersions to set
   */
  public void setRequirementVersions(final Set<IRequirementVersion> pRequirementVersions)
  {
    requirementVersions = pRequirementVersions;
  }

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
    final RequirementEntity castOther = (RequirementEntity) other;
    return new EqualsBuilder().append(getReference(), castOther.getReference()).isEquals();
  }

  @Override
  public String toString()
  {
    final StringBuilder s = new StringBuilder();
    s.append("[RQ:").append(":NAME").append(name).append(":REF").append(functionalReference);
    for (final IRequirementVersion version : requirementVersions)
    {
      s.append("[V:").append(version.toString()).append("]");
    }
    s.append("]");
    return s.toString();
  }







}
