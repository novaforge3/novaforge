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

import org.novaforge.forge.tools.managementmodule.domain.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.domain.ApplicativeRights;
import org.novaforge.forge.tools.managementmodule.domain.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author fdemange
 */
@Entity
@Table(name = "applicativeRights", uniqueConstraints = @UniqueConstraint(columnNames = {
		"applicative_function_id", "role_id" }))
@NamedQueries({ @NamedQuery(name = "ApplicativeRightsEntity.findRightsByRoleAndFunction", query = "SELECT p FROM ApplicativeRightsEntity p WHERE p.applicativeFunction.name = :functionName and p.role.name = :roleName") })
public final class ApplicativeRightsEntity implements ApplicativeRights, Serializable
{

	private static final long	 serialVersionUID = 8237082912092521158L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long								id;

	@ManyToOne(targetEntity = ApplicativeFunctionEntity.class)
	@JoinColumn(name = "applicative_function_id", referencedColumnName = "id", nullable = false)
	private ApplicativeFunction applicativeFunction;

	@ManyToOne(targetEntity = RoleEntity.class)
	@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
	private Role								role;

	@Column(name = "accessRight", nullable = false)
	private String							accessRight;

	public ApplicativeRightsEntity()
	{
		super();
	}

	public ApplicativeRightsEntity(final ApplicativeFunction s, final Role r, final String access)
	{
		super();
		this.setApplicativeFunction(s);
		this.setRole(r);
		this.setAccesRight(access);
	}

	public Long getId()
	{
		return id;
	}

	@Override
	public ApplicativeFunction getApplicativeFunction()
	{
		return applicativeFunction;
	}

	@Override
	public void setApplicativeFunction(final ApplicativeFunction applicativeFunction)
	{
		this.applicativeFunction = applicativeFunction;
	}

	@Override
	public Role getRole()
	{
		return role;
	}

	@Override
	public void setRole(final Role role)
	{
		this.role = role;
	}

	@Override
	@Size(max = 1)
	public String getAccesRight()
	{
		return accessRight;
	}

	@Override
	public void setAccesRight(final String accesRight)
	{
		this.accessRight = accesRight;
	}
}
