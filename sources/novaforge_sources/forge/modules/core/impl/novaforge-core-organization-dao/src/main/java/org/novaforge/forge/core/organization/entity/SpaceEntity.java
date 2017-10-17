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

import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.ProjectElement;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "NODE")
@NamedQueries({
    @NamedQuery(name = "SpaceEntity.findByProjectElementId",
        query = "SELECT s FROM SpaceEntity s WHERE s.element.elementId = :elementId"),
    @NamedQuery(name = "SpaceEntity.findAppsBySpace",
        query = "SELECT apps FROM SpaceEntity s, in(s.applications) apps WHERE s.uri = :uri"),
    @NamedQuery(name = "SpaceEntity.findSpaceByApp",
        query = "SELECT s FROM SpaceEntity s, IN (s.applications) a WHERE a.uri = :uri") })
@FetchGroup(name = "space_applications", attributes = { @FetchAttribute(name = "applications") })
public class SpaceEntity extends NodeEntity implements Space
{
  private static final long serialVersionUID = 5763152884413151406L;

  @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
      CascadeType.REFRESH }, targetEntity = ApplicationEntity.class, orphanRemoval = true)
  @JoinTable(name = "SPACE_APPLICATION", joinColumns = @JoinColumn(name = "space_id"),
      inverseJoinColumns = @JoinColumn(name = "application_id"))
  private List<Application> applications     = new ArrayList<Application>();

  @ManyToOne(fetch = FetchType.LAZY, targetEntity = ProjectElementEntity.class)
  @JoinColumn(name = "element_id")
  private ProjectElement    element;

  @Enumerated(EnumType.STRING)
  @Column(name = "realmType", nullable = true)
  private RealmType         realmType        = RealmType.USER;

  @Override
  public ProjectElement getProjectElement()
  {
    return element;
  }

  /**
   * @param pProject
   *          the project to set
   */
  public void setProjectElement(final ProjectElement pProject)
  {
    element = pProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RealmType getRealmType()
  {
    return realmType;
  }

  /**
   * @param realmType
   *     the realmType to set
   */
  @Override
  public void setRealmType(final RealmType realmType)
  {
    this.realmType = realmType;
  }

  /**
   * Return the {@link Application} list as unmodification one
   *
   * @return {@link List} of {@link Application}
   */
  public List<Application> getApplications()
  {
    return Collections.unmodifiableList(applications);
  }

  /**
   * @param pApplications
   *          the applications to set
   */
  public void setApplications(final List<Application> pApplications)
  {
    applications = pApplications;
  }

  /**
   * Add the {@link Application} given as parameter to the space
   *
   * @param pApplication
   *          the {@link Application} to add
   */
  public void addApplication(final Application pApplication)
  {
    applications.add(pApplication);
  }

  /**
   * remove the {@link Application} given as parameter from the space
   *
   * @param pApplication
   *          the {@link Application} to remove
   */
  public void removeApplication(final Application pApplication)
  {
    applications.remove(pApplication);
  }

}
