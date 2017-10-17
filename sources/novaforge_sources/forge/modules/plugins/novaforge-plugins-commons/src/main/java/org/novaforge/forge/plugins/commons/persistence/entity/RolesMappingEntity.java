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
package org.novaforge.forge.plugins.commons.persistence.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.RolesMapping;

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
import javax.persistence.Table;

/**
 * @author lamirang
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "RolesMappingEntity.findByInstance",
        query = "SELECT rm FROM RolesMappingEntity rm WHERE rm.instance.instanceId = :id"),
    @NamedQuery(
        name = "RolesMappingEntity.findByInstanceAndForgeRole",
        query = "SELECT rm FROM RolesMappingEntity rm WHERE rm.instance.instanceId = :id and rm.forgeRole = :forgeRole"),
    @NamedQuery(
        name = "RolesMappingEntity.findToolRoleByInstanceAndForgeRole",
        query = "SELECT rm.toolRole FROM RolesMappingEntity rm WHERE rm.instance.instanceId = :id and rm.forgeRole = :forgeRole") })
@Table(name = "plugin_roles_mapping")
public class RolesMappingEntity implements RolesMapping
{
	/**
	 * Thid field is the main technical ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private long                  id;

	/**
	 * Field representing plugin instance
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = InstanceConfigurationEntity.class)
	@JoinColumn(name = "instance_id", referencedColumnName = "id", nullable = false)
	private InstanceConfiguration instance;

	/**
	 * Field representing role in forge
	 */
	@Column(name = "forge_role", nullable = false)
	private String                forgeRole;

	/**
	 * Field representing role in tool
	 */
	@Column(name = "tool_role", nullable = false)
	private String                toolRole;

	/**
	 * Constructor public needed by JPA2
	 */
	public RolesMappingEntity()
	{
		// Use by JPA
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getId()
	{
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(final long pId)
	{
		id = pId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InstanceConfiguration getInstance()
	{
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInstance(final InstanceConfiguration pInstance)
	{
		instance = pInstance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getForgeRole()
	{
		return forgeRole;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setForgeRole(final String pForgeRole)
	{
		forgeRole = pForgeRole;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getToolRole()
	{
		return toolRole;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToolRole(final String pToolRole)
	{
		toolRole = pToolRole;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(id).append(instance).append(forgeRole).append(toolRole).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (!(other instanceof RolesMappingEntity))
		{
			return false;
		}
		final RolesMappingEntity castOther = (RolesMappingEntity) other;
		return new EqualsBuilder().append(id, castOther.id).append(instance, castOther.instance)
		    .append(forgeRole, castOther.forgeRole).append(toolRole, castOther.toolRole).isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "RolesMappingEntity [id=" + id + ", instance=" + instance + ", roleForge=" + forgeRole + ", roleTool="
							 + toolRole + "]";
	}

}
