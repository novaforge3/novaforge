/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this file.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.forge.plugins.scm.svn.agent.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.plugins.scm.svn.domain.Group;
import org.novaforge.forge.plugins.scm.svn.domain.GroupMembership;
import org.novaforge.forge.plugins.scm.svn.domain.User;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "SVN_GROUP_MEMBERSHIP")
public class GroupMembershipEntity implements GroupMembership, Serializable
{
	private static final long serialVersionUID = 9162901638412664180L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long              id;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private User              user;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = GroupEntity.class)
	@JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
	private Group             group;

	public GroupMembershipEntity()
	{
		super();
	}

	/**
	 * @inheritDoc
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @inheritDoc
	 */
	public void setId(final Long id)
	{
		this.id = id;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public User getUser()
	{
		return user;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setUser(final User user)
	{
		this.user = user;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Group getGroup()
	{
		return group;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setGroup(final Group group)
	{
		this.group = group;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String toString()
	{
		return "GroupMembershipEntity [group=" + group + ", id=" + id + ", user=" + user + "]";
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (!(other instanceof GroupMembershipEntity))
		{
			return false;
		}
		GroupMembershipEntity castOther = (GroupMembershipEntity) other;
		return new EqualsBuilder().append(user, castOther.getUser()).append(group, castOther.getGroup())
		    .isEquals();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(user).append(group).toHashCode();
	}

}
