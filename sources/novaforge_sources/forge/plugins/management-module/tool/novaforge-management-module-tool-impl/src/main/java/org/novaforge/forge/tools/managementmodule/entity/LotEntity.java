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
package org.novaforge.forge.tools.managementmodule.entity;

import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.apache.openjpa.persistence.FetchGroups;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author fdemange
 */
@Entity
@Table(name = "lot")
@NamedQueries({
    @NamedQuery(name = "LotEntity.findByName", query = "SELECT l FROM LotEntity l WHERE l.name = :name"),
    @NamedQuery(name = "LotEntity.findByProjectPlanId",
        query = "SELECT l FROM LotEntity l WHERE l.projectPlan.id = :projectPlanId"),
    @NamedQuery(
        name = "LotEntity.findParentLotsByProjectPlanId",
        query = "SELECT l FROM LotEntity l WHERE l.projectPlan.id = :projectPlanId and l.parentLot.id is null"),
    @NamedQuery(
        name = "LotEntity.completeFindByProjectPlanId",
        query = "SELECT DISTINCT l FROM LotEntity l WHERE l.projectPlan.id = :projectPlanId and l.parentLot.id is null"),
    @NamedQuery(name = "LotEntity.completeGetLot",
        query = "SELECT DISTINCT l FROM LotEntity l WHERE l.id = :id") })
@FetchGroups({
    @FetchGroup(name = "lots_all", attributes = { @FetchAttribute(name = "childLots", recursionDepth = -1) }),
    @FetchGroup(name = "iterations_all", attributes = { @FetchAttribute(name = "iterations") }),
    @FetchGroup(name = "scopes_all", attributes = { @FetchAttribute(name = "scopeEntities") }) })
public class LotEntity implements Lot, Serializable
{
  /**
    * 
    */
  private static final long serialVersionUID = -3448756519920734070L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "name", nullable = false)
  private String            name;

  @Column(name = "description", nullable = false, length = 2000)
  private String            description;

  @Column(name = "start_date", nullable = false)
  private Date              startDate;

  @Column(name = "end_date", nullable = false)
  private Date              endDate;

  @ManyToOne(targetEntity = ProjectPlanEntity.class)
  private ProjectPlan       projectPlan;

  @ManyToOne(targetEntity = LotEntity.class)
  private Lot               parentLot;

  @OneToMany(mappedBy = "parentLot", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      targetEntity = LotEntity.class)
  private Set<Lot>          childLots        = new HashSet<Lot>();

  @OneToMany(mappedBy = "lot", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      targetEntity = IterationEntity.class)
  private Set<Iteration>    iterations       = new HashSet<Iteration>();

  @OneToMany(mappedBy = "lot", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      targetEntity = ScopeUnitEntity.class)
  private Set<ScopeUnit>    scopeEntities    = new HashSet<ScopeUnit>();

  @OneToOne(fetch = FetchType.LAZY, targetEntity = LotEntity.class)
  @JoinColumn(name = "src_lot_id", referencedColumnName = "id", nullable = true)
  private Lot               srcLot;

  public LotEntity()
  {
    super();
  }

  @Override
  public Long getId()
  {
    return id;
  }

  @Override
  @Size(max = 255)
  public String getName()
  {
    return name;
  }

  @Override
  public void setName(final String name)
  {
    this.name = name;
  }

  @Override
  public String getDescription()
  {
    return description;
  }

  @Override
  public void setDescription(final String description)
  {
    this.description = description;
  }

  @Override
  public Date getStartDate()
  {
    return startDate;
  }

  @Override
  public void setStartDate(final Date startDate)
  {
    this.startDate = startDate;
  }

  @Override
  public Date getEndDate()
  {
    return endDate;
  }

  @Override
  public void setEndDate(final Date endDate)
  {
    this.endDate = endDate;
  }

  @Override
  public ProjectPlan getProjectPlan()
  {
    return projectPlan;
  }

  @Override
  public void setProjectPlan(final ProjectPlan projectPlan)
  {
    this.projectPlan = projectPlan;
  }

  @Override
  public Set<Iteration> getIterations()
  {
    return iterations;
  }

  @Override
  public void setIterations(final Set<Iteration> iterations)
  {
    this.iterations = iterations;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addIteration(final Iteration iteration)
  {
    this.iterations.add(iteration);
    IterationEntity iterationEntity = (IterationEntity) iteration;
    iterationEntity.setLot(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeIterationTask(final Iteration iteration)
  {
    this.iterations.remove(iteration);

  }

  @Override
  public Set<ScopeUnit> getScopeEntities()
  {
    Set<ScopeUnit> newList = new HashSet<ScopeUnit>(scopeEntities);
    scopeEntities.clear();
    scopeEntities.addAll(newList);
    return scopeEntities;
  }

  @Override
  public void setScopeEntities(final Set<ScopeUnit> scopeEntities)
  {
    this.scopeEntities = scopeEntities;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addScopeEntity(final ScopeUnit scope)
  {
    this.scopeEntities.add(scope);
    ScopeUnitEntity scopeUnitEntity = (ScopeUnitEntity) scope;
    scopeUnitEntity.setLot(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeScopeEntity(final ScopeUnit scope)
  {
    this.scopeEntities.remove(scope);

  }

  @Override
  public Set<Lot> getChildLots()
  {
    return childLots;
  }

  @Override
  public void addChildLot(final Lot lot)
  {
    this.childLots.add(lot);

  }

  @Override
  public void removeChildLot(final Lot lot)
  {
    this.childLots.remove(lot);
  }

  @Override
  public Lot getParentLot()
  {
    return parentLot;
  }

  @Override
  public void setParentLot(final Lot parentLot)
  {
    this.parentLot = parentLot;
  }

  @Override
  public Lot getSrcLot()
  {
    return srcLot;
  }

  @Override
  public void setSrcLot(final Lot srcLot)
  {
    this.srcLot = srcLot;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((endDate == null) ? 0 : ((Long) endDate.getTime()).hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((startDate == null) ? 0 : ((Long) startDate.getTime()).hashCode());
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
    LotEntity other = (LotEntity) obj;
    if (description == null)
    {
      if (other.description != null)
      {
        return false;
      }
    }
    else if (!description.equals(other.description))
    {
      return false;
    }
    if (endDate == null)
    {
      if (other.endDate != null)
      {
        return false;
      }
    }
    else if (!endDate.equals(other.endDate))
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
    if (startDate == null)
    {
      if (other.startDate != null)
      {
        return false;
      }
    }
    else if (!startDate.equals(other.startDate))
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "LotEntity [id=" + id + ", name=" + name + ", description=" + description + ", startDate="
        + startDate + ", endDate=" + endDate + ", projectPlan=" + projectPlan + "]";
  }

}
