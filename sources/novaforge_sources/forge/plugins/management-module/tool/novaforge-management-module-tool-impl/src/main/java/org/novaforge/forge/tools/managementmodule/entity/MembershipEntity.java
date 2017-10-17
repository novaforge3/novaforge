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

import org.novaforge.forge.tools.managementmodule.domain.Membership;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.Role;
import org.novaforge.forge.tools.managementmodule.domain.User;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author fdemange
 */
@Entity
@Table(name = "membership")
@NamedQueries({
		@NamedQuery(name = "MembershipEntity.findAllMembershipsByProject", query = "SELECT p FROM MembershipEntity p WHERE p.project.projectId = :projectId"),
		@NamedQuery(name = "MembershipEntity.findAllMembershipsByActor", query = "SELECT p FROM MembershipEntity p WHERE p.actor.login = :login"),
		@NamedQuery(name = "MembershipEntity.findAllMembershipsByActorAndProject", query = "SELECT p FROM MembershipEntity p WHERE p.actor.login = :login and p.project.projectId = :projectId"),
		@NamedQuery(name = "MembershipEntity.findAllActorByRoleAndProject", query = "SELECT p.actor FROM MembershipEntity p WHERE p.project.projectId = :projectId and p.role.name = :roleName"),
		@NamedQuery(name = "MembershipEntity.findAllActorByRole", query = "SELECT p.actor FROM MembershipEntity p WHERE p.role.name = :roleName"),
		@NamedQuery(name = "MembershipEntity.findAllActorsByProject", query = "SELECT distinct p.actor FROM MembershipEntity p WHERE p.project.projectId = :projectId") })
public class MembershipEntity implements Membership, Serializable
{

	private static final long	serialVersionUID = 8237082912092521158L;

	@EmbeddedId
	private MembershipIdEntity id							 = new MembershipIdEntity();

	@ManyToOne(targetEntity = UserEntity.class)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private User							 actor;

	@ManyToOne(targetEntity = RoleEntity.class)
	@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private Role							 role;

	@ManyToOne(targetEntity = ProjectEntity.class)
	@JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private Project						project;

	public MembershipEntity()
	{
		super();
	}

	public MembershipEntity(final User actor, final Role role, final Project project)
	{
		super();
		this.setUser(actor);
		this.setRole(role);
		this.setProject(project);

		UserEntity actorEntity = (UserEntity) actor;
		RoleEntity roleEntity = (RoleEntity) role;
		ProjectEntity projectEntity = (ProjectEntity) project;
		this.setId(new MembershipIdEntity(actorEntity.getId(), roleEntity.getId(), projectEntity.getId()));
	}

	public MembershipIdEntity getId()
	{
		return id;
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public Role getRole()
	{
		return role;
	}

	public void setId(final MembershipIdEntity id)
	{
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRole(final Role role)
	{
		this.role = role;
		this.id.setRoleId(role.getId());
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actor == null) ? 0 : actor.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public Project getProject()
	{
		return project;
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
		MembershipEntity other = (MembershipEntity) obj;
		if (actor == null)
		{
			if (other.actor != null)
			{
				return false;
			}
		}
		else if (!actor.equals(other.actor))
		{
			return false;
		}
		if (id == null)
		{
			if (other.id != null)
			{
				return false;
			}
		}
		else if (!id.equals(other.id))
		{
			return false;
		}
		if (role == null)
		{
			if (other.role != null)
			{
				return false;
			}
		}
		else if (!role.equals(other.role))
		{
			return false;
		}
		return true;
	}

	@Override
	public void setProject(final Project project)
	{
		this.project = project;
		this.id.setProjectId(project.getId());
		project.addMembership(this);
	}

	@Override
	public String toString()
	{
		return "MembershipEntity [id=" + id + ", actor=" + actor + ", role=" + role + "]";
	}

	@Override
	public User getUser()
	{
		return actor;
	}

	@Override
	public void setUser(final User user)
	{
		this.actor = user;
		this.id.setuserId(user.getId());

	}







}
