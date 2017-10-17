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

import org.novaforge.forge.tools.managementmodule.domain.ApplicativeRights;
import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.Role;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@NamedQueries({
		@NamedQuery(name = "RoleEntity.findByName", query = "SELECT r FROM RoleEntity r WHERE r.name = :name"),
		@NamedQuery(name = "RoleEntity.findByFunctionalId", query = "SELECT r FROM RoleEntity r WHERE r.functionalId = :functionalId"),
		@NamedQuery(name = "RoleEntity.getAllRoles", query = "SELECT r FROM RoleEntity r") })
public class RoleEntity implements Role, Serializable
{

	/**
	 * 
	 */
	private static final long			serialVersionUID			= 3719865472617031945L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long									 id;

	@Column(name = "name", nullable = false, unique = true)
	private String								 name;

	@Column(name = "functionalId", nullable = false, unique = true)
	private String								 functionalId;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = DisciplineEntity.class)
	@JoinTable(name = "role_discipline", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "discipline_id"))
	private Set<Discipline>				listDisciplines			 = new HashSet<Discipline>();

	@OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = ApplicativeRightsEntity.class)
	private Set<ApplicativeRights> functionalAbilityList = new HashSet<ApplicativeRights>();

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	@Override
	public String getFunctionalId()
	{
		return functionalId;
	}

	@Override
	public void setFunctionalId(final String fonctionalId)
	{
		this.functionalId = fonctionalId;
	}

	@Override
	public Set<Discipline> getListDisciplines()
	{
		Set<Discipline> newList = new HashSet<Discipline>(listDisciplines);
		listDisciplines.clear();
		listDisciplines.addAll(newList);
		return listDisciplines;
	}

	@Override
	public void setListDisciplines(final Set<Discipline> listDisciplines)
	{
		this.listDisciplines = listDisciplines;
	}

	@Override
	public Set<ApplicativeRights> getFunctionalAbilityList()
	{
		Set<ApplicativeRights> newList = new HashSet<ApplicativeRights>(functionalAbilityList);
		functionalAbilityList.clear();
		functionalAbilityList.addAll(newList);
		return functionalAbilityList;
	}

	@Override
	public void setFunctionalAbilityList(final Set<ApplicativeRights> functionalAbilityList)
	{
		this.functionalAbilityList = functionalAbilityList;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((functionalId == null) ? 0 : functionalId.hashCode());
		return result;
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
		RoleEntity other = (RoleEntity) obj;
		if (functionalId == null)
		{
			if (other.functionalId != null)
			{
				return false;
			}
		}
		else if (!functionalId.equals(other.functionalId))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "RoleEntity [id=" + id + ", name=" + name + "]";
	}

}
