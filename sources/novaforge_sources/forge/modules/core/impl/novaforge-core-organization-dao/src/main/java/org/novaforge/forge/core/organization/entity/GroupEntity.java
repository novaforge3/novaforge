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
/**
 * 
 */
package org.novaforge.forge.core.organization.entity;

import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author BILET-JC
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "ACTOR_GROUP", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "project_id" }))
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
    @NamedQuery(name = "GroupEntity.findByUUID", query = "SELECT g FROM GroupEntity g WHERE g.uuid = :uuid"),
    @NamedQuery(name = "GroupEntity.findByNameAndProjectId",
        query = "SELECT g FROM GroupEntity g WHERE g.name = :name and g.project.elementId = :projectId"),
    @NamedQuery(name = "GroupEntity.findByVisibility",
        query = "SELECT g FROM GroupEntity g WHERE g.visibility = :visibility"),
    @NamedQuery(
        name = "GroupEntity.findByProjectWithPublic",
        query = "SELECT distinct g FROM GroupEntity g WHERE g.project.elementId = :projectId OR g.visibility = true"),
    @NamedQuery(name = "GroupEntity.findByProject",
        query = "SELECT g FROM GroupEntity g WHERE g.project.elementId = :projectId"),
    @NamedQuery(name = "GroupEntity.findGroupsForUser",
        query = "SELECT g FROM GroupEntity g, IN (g.users) u WHERE u.uuid = :uuid"),
    @NamedQuery(
        name = "GroupEntity.findGroupsInfosForUser",
        query = "SELECT new org.novaforge.forge.core.organization.data.GroupInfoImpl(g.project.elementId,g) FROM GroupEntity g, IN (g.users) u WHERE u.uuid = :uuid"),
    @NamedQuery(name = "GroupEntity.findGroupsUUIDForUser",
        query = "SELECT g.uuid FROM GroupEntity g, IN (g.users) u WHERE u.uuid = :uuid") })
public class GroupEntity extends ActorEntity implements Group
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = -6159951581076870677L;

  // Has to refer explicitly ProjectEntity in order to generate correct metamodel
  @ManyToOne(fetch = FetchType.LAZY, targetEntity = ProjectEntity.class)
  @JoinColumn(name = "project_id", nullable = false)
  private ProjectEntity     project;

  @Column(name = "name", nullable = false)
  private String            name;

  @Column(name = "description")
  private String            description;

  @Column(name = "visibility", nullable = false)
  private boolean           visibility;

  @ManyToMany(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
  @JoinTable(name = "ACTOR_GROUP_RELATIONSHIP", joinColumns = @JoinColumn(name = "group_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"),
      uniqueConstraints = @UniqueConstraint(name = "UK_ACTOR_GROUP_RELATIONSHIP", 
                            columnNames = {"group_id", "user_id"}))
  private List<User>        users            = new ArrayList<User>();

  /**
   * Allows to get project reference
   * 
   * @return {@link Project} on which group is associated
   */
  public Project getProject()
  {
    return project;
  }

  /**
   * Allows to set the project
   * 
   * @param pProject
   *          the project to set
   */
  public void setProject(final Project pProject)
  {
    project = (ProjectEntity) pProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "GroupEntity [name=" + getName() + ", description=" + description + ", visibility=" + visibility + ", users="
               + users + "]";
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
  public void setDescription(final String pDescription)
  {
    description = pDescription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isVisible()
  {
    return visibility;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setVisible(final boolean pVisibility)
  {
    visibility = pVisibility;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<User> getUsers()
  {
    return Collections.unmodifiableList(users);
  }

  /**
   * @param pUsers
   *          the users to set
   */
  public void setUsers(final List<User> pUsers)
  {
    users = pUsers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addUser(final User pUser)
  {
    users.add(pUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeUser(final User pUser)
  {
    users.remove(pUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearUsers()
  {
    users.clear();
  }

}
