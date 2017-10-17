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
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectRole;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author sbenoist
 * @author BILET-JC
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "MEMBERSHIP")
@NamedQueries({
    // Retrieve all memberships existing on a project
    @NamedQuery(name = "MembershipEntity.findByProject",
        query = "SELECT m FROM MembershipEntity m WHERE m.project.elementId = :projectId"),
    // Retrieve all memberships existing on a project for a given actor
    @NamedQuery(
        name = "MembershipEntity.findByProjectAndActor",
        query = "SELECT m FROM MembershipEntity m WHERE m.actor.uuid = :uuid and m.project.elementId = :projectId"),
    // Retrieve MembershipInfo for a list of actors on a project
    @NamedQuery(
        name = "MembershipEntity.findInfoByProjectAndActors",
        query = "SELECT new org.novaforge.forge.core.organization.data.MembershipInfoImpl(mp.elementId,ma,mr,m.priority) "
            + "FROM MembershipEntity m LEFT OUTER JOIN m.project mp LEFT OUTER JOIN m.actor ma LEFT OUTER JOIN m.role mr "
            + "WHERE mp.elementId = :projectId AND ma.uuid IN :uuids"),
    // Retrieve memberships for user on a project
    @NamedQuery(
        name = "MembershipEntity.findUsersInfoByProject",
        query = "SELECT new org.novaforge.forge.core.organization.data.MembershipInfoImpl(mp.elementId,u,mr,m.priority) "
            + "FROM MembershipEntity m LEFT OUTER JOIN m.project mp LEFT OUTER JOIN m.actor ma LEFT OUTER JOIN m.role mr, UserEntity u "
            + "WHERE mp.elementId = :projectId AND ma.id = u.id"),
    // Retrieve memberships for user on a project
    @NamedQuery(
        name = "MembershipEntity.findGroupsInfoByProject",
        query = "SELECT new org.novaforge.forge.core.organization.data.MembershipInfoImpl(mp.elementId,g,mr,m.priority) "
            + "FROM MembershipEntity m LEFT OUTER JOIN m.project mp LEFT OUTER JOIN m.actor ma LEFT OUTER JOIN m.role mr, GroupEntity g "
            + "WHERE mp.elementId = :projectId AND ma.id = g.id"),

    // count memberships for an actor
    @NamedQuery(name = "MembershipEntity.countForActor",
        query = "SELECT COUNT(m) FROM MembershipEntity m WHERE m.actor.uuid = :uuid"),
    @NamedQuery(
        name = "MembershipEntity.findActorsByRoleAndProject",
        query = "SELECT DISTINCT ma FROM MembershipEntity m LEFT OUTER JOIN m.project mp LEFT OUTER JOIN m.actor ma LEFT OUTER JOIN m.role mr "
            + "WHERE mp.elementId = :projectId and mr.name = :roleName"),
    @NamedQuery(
        name = "MembershipEntity.findActorsByRole",
        query = "SELECT DISTINCT ma FROM MembershipEntity m LEFT OUTER JOIN m.actor ma LEFT OUTER JOIN m.role mr "
            + "WHERE mr.name = :roleName"),
    // Retrieve users which are direct relationship with a project
    @NamedQuery(
        name = "MembershipEntity.findActorsByProject",
        query = "SELECT DISTINCT ma FROM MembershipEntity m LEFT OUTER JOIN m.project mp LEFT OUTER JOIN m.actor ma "
            + "WHERE mp.elementId = :projectId"),
    // Retrieve all project's id where groups have relationship
    @NamedQuery(
        name = "MembershipEntity.findProjectsIdForActors",
        query = "SELECT DISTINCT mp.elementId FROM MembershipEntity m LEFT OUTER JOIN m.project mp LEFT OUTER JOIN m.actor ma "
            + "WHERE ma.uuid IN :uuids"),
    // Retrieve all project on which groups have relationship
    @NamedQuery(
        name = "MembershipEntity.findValidatedProjectsForActorsWithSystem",
        query = "SELECT DISTINCT m.project "
            + "FROM MembershipEntity m LEFT OUTER JOIN m.project mp LEFT OUTER JOIN m.actor ma "
            + "WHERE mp.status = org.novaforge.forge.core.organization.model.enumerations.ProjectStatus.VALIDATED "
            + "AND m.actor.uuid IN :uuids"),
    @NamedQuery(
        name = "MembershipEntity.findValidatedProjectsForActors",
        query = "SELECT DISTINCT mp "
            + "FROM MembershipEntity m LEFT OUTER JOIN m.project mp LEFT OUTER JOIN m.actor ma "
            + "WHERE mp.status = org.novaforge.forge.core.organization.model.enumerations.ProjectStatus.VALIDATED "
            + "AND mp.realmType = org.novaforge.forge.core.organization.model.enumerations.RealmType.USER "
            + "AND ma.uuid IN :uuids"),
    // Retrieve public projects where groups haven't any relationship
    @NamedQuery(
        name = "MembershipEntity.findPublicProjectsForActorsWithSystem",
        query = "SELECT DISTINCT p "
            + "FROM ProjectEntity p "
            + "WHERE p.privateVisibility = false "
            + "AND p.status = org.novaforge.forge.core.organization.model.enumerations.ProjectStatus.VALIDATED "
            + "AND NOT EXISTS "
            + "(SELECT DISTINCT mp FROM MembershipEntity m LEFT OUTER JOIN m.project mp LEFT OUTER JOIN m.actor ma "
            + "WHERE mp.id = p.id m.actor.uuid IN :uuids)"),
    @NamedQuery(
        name = "MembershipEntity.findPublicProjectsForActors",
        query = "SELECT DISTINCT p "
            + "FROM ProjectEntity p "
            + "WHERE p.privateVisibility = false AND p.status = org.novaforge.forge.core.organization.model.enumerations.ProjectStatus.VALIDATED "
            + "AND p.realmType = org.novaforge.forge.core.organization.model.enumerations.RealmType.USER "
            + "AND NOT EXISTS "
            + "(SELECT DISTINCT mp FROM MembershipEntity m LEFT OUTER JOIN m.project mp LEFT OUTER JOIN m.actor ma "
            + "WHERE mp.id = p.id AND ma.uuid IN :uuids)"),
    // Retrieve by project all role for which actors have a membership on a project
    @NamedQuery(name = "MembershipEntity.findRolesByActors", query = "SELECT m.project.elementId, m.role "
        + "FROM MembershipEntity m WHERE m.actor.uuid IN :uuids") })
public class MembershipEntity implements Membership, Serializable
{

  private static final long  serialVersionUID = 8237082912092521158L;

  @EmbeddedId
  private MembershipEntityId id               = new MembershipEntityId();

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = ActorEntity.class)
  @JoinColumn(name = "actor_id", referencedColumnName = "id", nullable = false, insertable = false,
      updatable = false)
  private Actor              actor;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectRoleEntity.class)
  @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, insertable = false,
      updatable = false)
  private ProjectRole        role;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectEntity.class)
  @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false, insertable = false,
      updatable = false)
  private Project            project;

  @Column(name = "priority", nullable = true)
  private boolean            priority;

  public MembershipEntity()
  {
    super();
  }

  public MembershipEntity(final Actor actor, final ProjectRole role, final Project project,
      final boolean priority)
  {
    super();
    this.actor = actor;
    this.role = role;
    this.project = project;
    this.priority = priority;

    final ActorEntity actorEntity = (ActorEntity) actor;
    final RoleEntity roleEntity = (ProjectRoleEntity) role;
    final ProjectEntity projectEntity = (ProjectEntity) project;
    setId(new MembershipEntityId(actorEntity.getId(), roleEntity.getId(), projectEntity.getId()));
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(id).toHashCode();
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof MembershipEntity))
    {
      return false;
    }
    final MembershipEntity castOther = (MembershipEntity) other;
    return new EqualsBuilder().append(id, castOther.getId()).isEquals();
  }

  public MembershipEntityId getId()
  {
    return id;
  }

  public void setId(final MembershipEntityId id)
  {
    this.id = id;
  }

  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("MembershipEntity [uuid=");
    if (actor != null)
    {
      builder.append(actor.getUuid());
    }
    builder.append(", priority=");
    builder.append(priority);
    builder.append(", projectId=");
    if (project != null)
    {
      builder.append(project.getProjectId());
    }
    builder.append(", rolename=");
    if (role != null)
    {
      builder.append(role.getName());
    }
    builder.append("]");
    return builder.toString();
  }

  @Override
  public Actor getActor()
  {
    return actor;
  }

  @Override
  public void setActor(final Actor actor)
  {
    this.actor = actor;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectRole getRole()
  {
    return role;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRole(final ProjectRole role)
  {
    this.role = role;
  }

  @Override
  public Project getProject()
  {
    return project;
  }

  @Override
  public void setProject(final Project project)
  {
    this.project = project;
  }

  @Override
  public boolean getPriority()
  {
    return priority;
  }

  @Override
  public void setPriority(final boolean priority)
  {
    this.priority = priority;

  }

}
