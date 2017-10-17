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

import org.novaforge.forge.tools.managementmodule.domain.Transformation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "transformation")
@NamedQueries({
		@NamedQuery(name = "TransformationEntity.findAll", query = "SELECT t FROM TransformationEntity t"),
		@NamedQuery(name = "TransformationEntity.findByIdProject", query = "SELECT p FROM TransformationEntity p WHERE p.idProject = :idProject") })
public class TransformationEntity implements Transformation, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1069350364469769026L;
	@Column(name = "idProject", nullable = false, unique = true)
	String										idProject;
	@Column(name = "nbJoursAn", nullable = false, unique = false)
	int											 nbJoursAn;
	@Column(name = "nbJoursMois", nullable = false, unique = false)
	int											 nbJoursMois;
	@Column(name = "nbJoursSemaine", nullable = false, unique = false)
	int											 nbJoursSemaine;
	@Column(name = "nbHeuresJour", nullable = false, unique = false)
	int											 nbHeuresJour;
	@Column(name = "nbJoursNonTravail", nullable = false, unique = false)
	int											 nbJoursNonTravail;
	@Id @Column(name = "id", nullable = false, updatable = false) @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public int getNbJoursAn()
	{
		return nbJoursAn;
	}

	@Override
	public void setNbJoursAn(final int nbJoursAn)
	{
		this.nbJoursAn = nbJoursAn;
	}

	@Override
	public int getNbJoursMois()
	{
		return nbJoursMois;
	}

	@Override
	public void setNbJoursMois(final int nbJoursMois)
	{
		this.nbJoursMois = nbJoursMois;
	}

	@Override
	public int getNbJoursSemaine()
	{
		return nbJoursSemaine;
	}

	@Override
	public void setNbJoursSemaine(final int nbJoursSemaine)
	{
		this.nbJoursSemaine = nbJoursSemaine;
	}

	@Override
	public int getNbHeuresJour()
	{
		return nbHeuresJour;
	}

	@Override
	public void setNbHeuresJour(final int nbHeuresJour)
	{
		this.nbHeuresJour = nbHeuresJour;
	}

	@Override
	public int getNbJoursNonTravail()
	{
		return nbJoursNonTravail;
	}

	@Override
	public void setNbJoursNonTravail(final int nbJoursNonTravail)
	{
		this.nbJoursNonTravail = nbJoursNonTravail;
	}

	/**
	 * @return the idProject
	 */
	@Override
	public String getIdProject()
	{
		return idProject;
	}

	/**
	 * @param idProject
	 *          the idProject to set
	 */
	@Override
	public void setIdProject(final String idProject)
	{
		this.idProject = idProject;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idProject == null) ? 0 : idProject.hashCode());
		result = prime * result + nbHeuresJour;
		result = prime * result + nbJoursAn;
		result = prime * result + nbJoursMois;
		result = prime * result + nbJoursNonTravail;
		result = prime * result + nbJoursSemaine;
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
		TransformationEntity other = (TransformationEntity) obj;
		if (idProject == null)
		{
			if (other.idProject != null)
			{
				return false;
			}
		}
		else if (!idProject.equals(other.idProject))
		{
			return false;
		}
		if (nbHeuresJour != other.nbHeuresJour)
		{
			return false;
		}
		if (nbJoursAn != other.nbJoursAn)
		{
			return false;
		}
		if (nbJoursMois != other.nbJoursMois)
		{
			return false;
		}
		if (nbJoursNonTravail != other.nbJoursNonTravail)
		{
			return false;
		}
		return nbJoursSemaine == other.nbJoursSemaine;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		TransformationEntity transformationEntity = new TransformationEntity();
		transformationEntity.setNbHeuresJour(nbHeuresJour);
		transformationEntity.setNbJoursAn(nbJoursAn);
		transformationEntity.setNbJoursMois(nbJoursMois);
		transformationEntity.setNbJoursNonTravail(nbJoursNonTravail);
		transformationEntity.setNbJoursSemaine(nbJoursSemaine);
		return transformationEntity;
	}

	@Override
	public String toString()
	{
		return "TransformationEntity [id=" + id + ", idProject=" + idProject + ", nbJoursAn=" + nbJoursAn + ", nbJoursMois="
							 + nbJoursMois + ", nbJoursSemaine=" + nbJoursSemaine + ", nbHeuresJour=" + nbHeuresJour
							 + ", nbJoursNonTravail=" + nbJoursNonTravail + "]";
	}

}