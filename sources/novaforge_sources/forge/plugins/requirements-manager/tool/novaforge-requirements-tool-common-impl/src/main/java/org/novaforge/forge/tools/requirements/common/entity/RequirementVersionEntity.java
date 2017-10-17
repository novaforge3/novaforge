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
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.tools.requirements.common.model.IRequirementVersion;
import org.novaforge.forge.tools.requirements.common.model.IResourceOOCode;
import org.novaforge.forge.tools.requirements.common.model.ITask;
import org.novaforge.forge.tools.requirements.common.model.ITest;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Guillaume Morin
 */
@Entity
@Table(name = "RVERSION", uniqueConstraints = @UniqueConstraint(columnNames = { "requirement_id",
    "currentVersion" }))
public class RequirementVersionEntity implements IRequirementVersion, Comparable<RequirementVersionEntity>,
    Serializable
{
  private static final long    serialVersionUID = 1452261313936264353L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long                 id;

  @Min(value = 1, message = "A version must be greater than {value}")
  @Column(name = "currentVersion", nullable = false, updatable = false)
  private int                  currentVersion;

  // TODO handle dates
  @Temporal(TemporalType.DATE)
  @Column(name = "startdate", nullable = false, updatable = false)
  private Date                 startDate        = new Date();

  @Temporal(TemporalType.DATE)
  @Column(name = "updatedate", nullable = false, updatable = true)
  private Date                 updateDate       = new Date();

  /***********************************
   * ------- Tables Relations ------
   **********************************/

  // Linked to one requirement
  @ManyToOne(targetEntity = RequirementEntity.class, cascade = CascadeType.ALL)
  @JoinColumn(name = "requirement_id", nullable = false, insertable = true, updatable = true)
  private IRequirement         requirement;

  // Linked to many resource code
  @OneToMany(mappedBy = "version", fetch = FetchType.EAGER, cascade = CascadeType.ALL,
      targetEntity = ResourceOOEntity.class, orphanRemoval = true)
  private Set<IResourceOOCode> resourcesOOCode  = new HashSet<IResourceOOCode>();

  // Linked to many test
  @OneToMany(targetEntity = TestEntity.class, fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE,
      CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, orphanRemoval = true)
  @JoinTable(name = "VERSION_TEST", joinColumns = { @JoinColumn(name = "requirement_id",
      referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "test_id",
      referencedColumnName = "id") })
  private Set<ITest>           tests            = new HashSet<ITest>();

  // Linked to many tasks
  @OneToMany(targetEntity = TaskEntity.class, fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE,
      CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, orphanRemoval = true)
  @JoinTable(name = "VERSION_TASK", joinColumns = { @JoinColumn(name = "requirement_id",
      referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "task_id",
      referencedColumnName = "id") })
  private Set<ITask>           tasks            = new HashSet<ITask>();

  @Column(length = 2048, name = "statement", nullable = true)
  private String               statement;

  @Override
  public Long getId()
  {
    return id;
  }

  @Override
  public int getCurrentVersion()
  {
    return currentVersion;
  }

  @Override
  public void setCurrentVersion(final int pCurrentVersion)
  {
    currentVersion = pCurrentVersion;
  }

  @Override
  public Set<IResourceOOCode> getResourcesOOCode()
  {
    return Collections.unmodifiableSet(resourcesOOCode);
  }

  @Override
  public boolean addResources(final IResourceOOCode pResource)
  {
    pResource.setVersion(this);
    return resourcesOOCode.add(pResource);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeResources(final IResourceOOCode pResource)
  {
    pResource.setVersion(null);
    resourcesOOCode.remove(pResource);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeAllResources()
  {
    resourcesOOCode.clear();
  }

  @Override
  public IRequirement getRequirement()
  {
    return requirement;
  }

  @Override
  public void setRequirement(final IRequirement pRequirement)
  {
    requirement = pRequirement;
  }

  @Override
  public Set<ITest> getTests()
  {
    return Collections.unmodifiableSet(tests);
  }

  @Override
  public boolean addTest(final ITest test)
  {
    test.addRequirementVersion(this);
    return tests.add(test);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeTest(final ITest pTest)
  {
    tests.remove(pTest);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeAllTests()
  {
    tests.clear();
  }

  @Override
  public Set<ITask> getTasks()
  {
    return Collections.unmodifiableSet(tasks);
  }

  @Override
  public boolean addTask(final ITask pTask)
  {
    return tasks.add(pTask);
  }

  /**
   * @Override public ECodeStatus getStatus() { return fStatus; }
   * @Override public void setStatus(final ECodeStatus pStatus) { fStatus = pStatus; }
   **/

  @Override
  public String getStatement()
  {
    return statement;
  }

  @Override
  public void setStatement(final String pStatement)
  {
    statement = pStatement;
  }

  @Override
  public ITest findTestByReference(final String pRefTest)
  {
    ITest testReturned = null;
    for (final ITest t : tests)
    {
      if (pRefTest.equals(t.getReference()))
      {
        testReturned = t;
      }
    }
    return testReturned;
  }

  /**
   * @param pTasks
   *     the tasks to set
   */
  public void setTasks(final Set<ITask> pTasks)
  {
    tasks = pTasks;
  }

  /**
   * @param pTests
   *          the tests to set
   */
  public void setTests(final Set<ITest> pTests)
  {
    tests = pTests;
  }

  /**
   * @param pResourcesOOCode
   *          the resourcesOOCode to set
   */
  public void setResourcesOOCode(final Set<IResourceOOCode> pResourcesOOCode)
  {
    resourcesOOCode = pResourcesOOCode;
  }

  @Override
  public int compareTo(final RequirementVersionEntity version)
  {
    if (getCurrentVersion() == version.getCurrentVersion())
    {
      return 0;
    }
    else if (getCurrentVersion() > version.getCurrentVersion())
    {
      return 1;
    }
    else
    {
      return -1;
    }
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(getRequirement()).append(getCurrentVersion()).toHashCode();
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof RequirementVersionEntity))
    {
      return false;
    }
    final RequirementVersionEntity castOther = (RequirementVersionEntity) other;
    return new EqualsBuilder().append(getRequirement(), castOther.getRequirement()).append(getCurrentVersion(),
                                                                                           castOther
                                                                                               .getCurrentVersion())
                              .isEquals();
  }

  @Override
  public String toString()
  {
    return "[RV:" + id + ":" + currentVersion + "]";
  }

  /**
   * @param pStartDate
   *     the startDate to set
   */
  public void setStartDate(final Date pStartDate)
  {
    startDate = pStartDate;
  }

  /**
   * @param pUpdateDate
   *          the updateDate to set
   */
  public void setUpdateDate(final Date pUpdateDate)
  {
    updateDate = pUpdateDate;
  }

}
