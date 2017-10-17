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
package org.novaforge.forge.tools.deliverymanager.entity;

import org.novaforge.forge.tools.deliverymanager.model.DeliveryType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "DELIVERY_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = { "label", "project_id" }))
@NamedQueries({
    @NamedQuery(name = "DeliveryTypeEntity.findByProjectAndLabel", query = "SELECT t FROM DeliveryTypeEntity t WHERE t.projectId = :projectId AND t.label = :label"),
    @NamedQuery(name = "DeliveryTypeEntity.findByProject", query = "SELECT t FROM DeliveryTypeEntity t WHERE t.projectId = :projectId") })
public class DeliveryTypeEntity implements DeliveryType, Serializable
{

	/**
	 * Serial Version Id used for serialization
	 */
	private static final long serialVersionUID = 6895533850708890314L;

	/**
	 * @see DeliveryTypeEntity#getId()
	 */
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long              id;

	/**
	 * @see DeliveryTypeEntity#getLabel()
	 * @see DeliveryTypeEntity#setLabel(String)
	 */
	@Column(name = "label", nullable = false, unique = false)
	private String            label;

	/**
	 * @see DeliveryTypeEntity#getProjectId()
	 * @see DeliveryTypeEntity#setProjectId(String)
	 */
	@Column(name = "project_id")
	private String            projectId;

	/**
	 * Default contructor
	 */
	public DeliveryTypeEntity()
	{
		// Used by JPA
	}

	public DeliveryTypeEntity(final String pProjectId, final String pLabel)
	{
		projectId = pProjectId;
		label = pLabel;
	}

	/**
	 * @see DeliveryTypeEntity#id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see DeliveryTypeEntity#label
	 */
	@NotNull
	@Size(min = 1, max = 250)
	@Override
	public String getLabel()
	{
		return label;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see DeliveryTypeEntity#label
	 */
	@Override
	public void setLabel(final String pLabel)
	{
		label = pLabel;

	}

	/**
	 * @see DeliveryTypeEntity#projectId
	 */
	@NotNull
	@Size(min = 1)
	@Override
	public String getProjectId()
	{
		return projectId;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see DeliveryTypeEntity#projectId
	 */
	@Override
	public void setProjectId(final String pProjectId)
	{
		projectId = pProjectId;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((id == null) ? 0 : id.hashCode());
		result = (prime * result) + ((label == null) ? 0 : label.hashCode());
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
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		final DeliveryTypeEntity other = (DeliveryTypeEntity) obj;
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
		if (label == null)
		{
			if (other.label != null)
			{
				return false;
			}
		}
		else if (!label.equals(other.label))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "DeliveryTypeEntity [id=" + id + ", label=" + label + "]";
	}

}
