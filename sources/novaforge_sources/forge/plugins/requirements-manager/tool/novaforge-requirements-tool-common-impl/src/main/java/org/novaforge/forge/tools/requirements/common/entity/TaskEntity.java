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
import org.novaforge.forge.tools.requirements.common.model.IRequirementVersion;
import org.novaforge.forge.tools.requirements.common.model.ITask;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Guillaume Morin
 */

@Entity
@Table(name = "TASK")
public class TaskEntity implements ITask
{
  @SuppressWarnings("unused")
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long                     id;

  @Column(name = "description", nullable = false, updatable = true)
  private String                   description;

  @Column(name = "reference", nullable = false, updatable = true)
  private String                   reference;

  /***********************************
   * ------- Tables Relations ------
   **********************************/
  @ManyToMany(mappedBy = "tasks", targetEntity = RequirementVersionEntity.class, fetch = FetchType.LAZY)
  private Set<IRequirementVersion> requirementVersions = new HashSet<IRequirementVersion>();

  public boolean addRequirementVersion(final IRequirementVersion pRequirementVersion)
  {
    pRequirementVersion.addTask(this);
    return requirementVersions.add(pRequirementVersion);
  }

  public Set<IRequirementVersion> getRequirementVersions()
  {
    return Collections.unmodifiableSet(requirementVersions);
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(getReference()).toHashCode();
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

  @Override
  public String getDescription()
  {
    return description;
  }

  @Override
  public void setDescrition(final String pDescription)
  {
    description = pDescription;
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof TaskEntity))
    {
      return false;
    }
    final TaskEntity castOther = (TaskEntity) other;
    return new EqualsBuilder().append(getReference(), castOther.getReference()).isEquals();
  }
}
