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
package org.novaforge.forge.core.organization.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.core.organization.model.ProjectElement;
import org.novaforge.forge.core.organization.model.Space;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lamirang
 */
@Entity
@Table(name = "PROJECT_ELEMENT", uniqueConstraints = @UniqueConstraint(columnNames = { "element_id" }))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
    @NamedQuery(name = "ProjectElementEntity.findByElementId",
        query = "SELECT p FROM ProjectElementEntity p WHERE p.elementId = :elementId"),
    @NamedQuery(name = "ProjectElementEntity.findByElementName",
        query = "SELECT p FROM ProjectElementEntity p WHERE p.name = :elementName"),
    @NamedQuery(name = "ProjectElementEntity.updateLastModified",
        query = "UPDATE ProjectElementEntity p SET p.lastModified = :date WHERE p.elementId = :elementId") })
public abstract class ProjectElementEntity implements ProjectElement, Serializable
{
  /**
    * 
    */
  private static final long serialVersionUID = -3448756519920734070L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "element_id", length = 26, nullable = false, unique = true, updatable = false)
  private String            elementId;

  @Column(name = "name", nullable = false, unique = true)
  private String            name;

  @Column(name = "description", length = 250, nullable = false)
  private String            description;

  @OneToMany(mappedBy = "element", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      targetEntity = SpaceEntity.class, orphanRemoval = true)
  private List<Space>       spaces           = new ArrayList<Space>();

  @Column(name = "created", nullable = false, insertable = true, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date              created;

  @Column(name = "last_modified", insertable = false, updatable = true)
  @Temporal(TemporalType.TIMESTAMP)
  private Date              lastModified;

  /**
   * This will be called before a persist and flush event
   */
  @PrePersist
  public void onPersist()
  {
    setCreated(new Date());
  }

  /**
   * This will be called before a merge and flush event
   */
  @PreUpdate
  public void onUpdate()
  {
    setLastModified(new Date());
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(id).append(elementId).toHashCode();
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof ProjectElementEntity))
    {
      return false;
    }
    final ProjectElementEntity castOther = (ProjectElementEntity) other;
    return new EqualsBuilder().append(id, castOther.getId()).append(elementId, castOther.getElementId()).isEquals();
  }

  /**
   * @return technical id
   * @see ProjectElementEntity#id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Size(min = 3, max = 26)
  @Pattern(regexp = "[a-z0-9_]*", message = "Must be composed by alphanumericals characters")
  public String getElementId()
  {
    return elementId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setElementId(final String pElementId)
  {
    elementId = pElementId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Size(min = 1)
  public String getName()
  {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setName(final String name)
  {
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Size(max = 250)
  public String getDescription()
  {
    return description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDescription(final String description)
  {
    this.description = description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getCreated()
  {
    return created;
  }

  /**
   * This method set the date of creation of the project
   *
   * @param pCreated
   *          the date to set
   */
  public void setCreated(final Date pCreated)
  {
    created = pCreated;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getLastModified()
  {
    return lastModified;
  }

  /**
   * This method set the date of the last modification of the project
   *
   * @param pLastModified
   *          the date to set
   */
  public void setLastModified(final Date pLastModified)
  {
    lastModified = pLastModified;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Space> getSpaces()
  {
    return spaces;
  }

  @Override
  public void setSpaces(final List<Space> spaces)
  {
    this.spaces = spaces;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addSpace(final Space pSpace)
  {
    spaces.add(pSpace);
    final SpaceEntity spaceEntity = (SpaceEntity) pSpace;
    spaceEntity.setProjectElement(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeSpace(final Space pSpace)
  {
    spaces.remove(pSpace);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "ProjectElementEntity [id=" + id + ", elementId=" + elementId + ", name=" + name + ", description="
               + description + ", created=" + created + ", lastModified=" + lastModified + "]";
  }
}
