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
import org.novaforge.forge.tools.requirements.common.model.ITest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Guillaume Morin
 */

@Entity
@Table(name = "TEST")
public class TestEntity implements ITest, Serializable
{

  /**
   * Serial version id
   */
  private static final long        serialVersionUID = -2206395811338139699L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long                     id;

  @Column(name = "reference", nullable = false, updatable = false)
  private String                   reference;

  @Column(name = "currentVersion", nullable = false, updatable = true)
  private int                      version;

  /***********************************
   * ------- Tables Relations ------
   **********************************/

  @ManyToMany(mappedBy = "tests", targetEntity = RequirementVersionEntity.class, fetch = FetchType.EAGER)
  private Set<IRequirementVersion> requirementVersions;

  public TestEntity()
  {
    setRequirementVersions(new HashSet<IRequirementVersion>());
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
  public int getCurrentVersion()
  {
    return version;
  }

  @Override
  public void setCurrentVersion(final int pVersion)
  {
    version = pVersion;
  }

  @Override
  public boolean addRequirementVersion(final IRequirementVersion pRequirementVersion)
  {
    return requirementVersions.add(pRequirementVersion);
  }

  @Override
  public Set<IRequirementVersion> getRequirementVersions()
  {
    return Collections.unmodifiableSet(requirementVersions);
  }

  /**
   * @param requirementVersions
   *          the requirementVersions to set
   */
  public void setRequirementVersions(final Set<IRequirementVersion> requirementVersions)
  {
    this.requirementVersions = requirementVersions;
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof TestEntity))
    {
      return false;
    }
    final TestEntity castOther = (TestEntity) other;
    return new EqualsBuilder().append(reference, castOther.getReference()).isEquals();
  }

}
