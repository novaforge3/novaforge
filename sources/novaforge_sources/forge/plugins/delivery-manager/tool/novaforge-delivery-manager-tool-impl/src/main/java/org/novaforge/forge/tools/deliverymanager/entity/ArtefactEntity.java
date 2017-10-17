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
package org.novaforge.forge.tools.deliverymanager.entity;

import org.novaforge.forge.tools.deliverymanager.model.Artefact;
import org.novaforge.forge.tools.deliverymanager.model.ArtefactParameter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NODE")
public class ArtefactEntity extends AbstractNodeEntity implements Artefact, Serializable
{

	/**
    * 
    */
	private static final long       serialVersionUID = 1225642482089925754L;

	@Column(name = "identifiant")
	private String                  identifiant;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = ArtefactParameterEntity.class, orphanRemoval = true)
  @JoinTable(name = "ARTEFACT_PARAMETER", joinColumns = @JoinColumn(name = "artefact_id"),
      inverseJoinColumns = @JoinColumn(name = "param_id"))
	private List<ArtefactParameter> parameters       = new ArrayList<ArtefactParameter>();

	public ArtefactEntity()
	{
		// Used by JPA
	}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Size(min = 1)
	@Override
	public String getIdentifiant()
	{
		return identifiant;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIdentifiant(final String pIdentifiant)
	{
		identifiant = pIdentifiant;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ArtefactParameter> getParameters()
	{
		return parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameters(final List<ArtefactParameter> parameters)
	{
		this.parameters = parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + ((identifiant == null) ? 0 : identifiant.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!super.equals(obj))
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		final ArtefactEntity other = (ArtefactEntity) obj;
		if (identifiant == null)
		{
			if (other.identifiant != null)
			{
				return false;
			}
		}
		else if (!identifiant.equals(other.identifiant))
		{
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return super.toString() + "ArtefactEntity [identifiant=" + identifiant + "]";
	}

}
