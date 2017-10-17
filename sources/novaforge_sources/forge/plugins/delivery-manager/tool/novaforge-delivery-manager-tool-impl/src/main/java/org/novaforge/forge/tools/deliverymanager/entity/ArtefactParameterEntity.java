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

import org.novaforge.forge.tools.deliverymanager.model.ArtefactParameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "PARAMETER")
public class ArtefactParameterEntity implements Serializable, ArtefactParameter
{

	/**
    * 
    */
	private static final long serialVersionUID = 5635185340601591419L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long              id;

	@Column(name = "parameter", nullable = false)
	private String            key;

	@Column(name = "value", nullable = false)
	private String            value;

	public ArtefactParameterEntity()
	{
		// Used by JPA
	}

	public Long getId()
	{
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getKey()
	{
		return key;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setKey(final String pKey)
	{
		key = pKey;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue()
	{
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(final String pValue)
	{
		value = pValue;
	}

	@Override
	public String toString()
	{
		return "ArtefactParameterEntity [id=" + id + ", key=" + key + ", value=" + value + "]";
	}

}
