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

import org.novaforge.forge.tools.deliverymanager.model.Node;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "NODE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AbstractNodeEntity implements Node, Serializable
{

	/**
	 * Serial Version Id used for serialization
	 */
	private static final long serialVersionUID = 2753525488775155798L;

	/**
	 * @see AbstractNodeEntity#getId()
	 * @see AbstractNodeEntity#setId(Long)
	 */
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long              id;

	/**
	 * @see AbstractNodeEntity#setName(String)
	 * @see AbstractNodeEntity#getName()
	 */
	@Column(name = "name")
	private String            name;

	/**
	 * @see AbstractNodeEntity#setPath(String)
	 * @see AbstractNodeEntity#getPath()
	 */
	@Column(name = "path")
	private String            path;

	/**
	 * @see AbstractNodeEntity#id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see AbstractNodeEntity#id
	 */
	public void setId(final Long id)
	{
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see AbstractNodeEntity#name
	 */
	@Override
	@NotNull
	@Size(min = 1)
	public String getName()
	{
		return name;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see AbstractNodeEntity#name
	 */
	@Override
	public void setName(final String pName)
	{
		name = pName;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see AbstractNodeEntity#path
	 */
	@Override
	@NotNull
	@Size(min = 1)
	public String getPath()
	{
		return path;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see AbstractNodeEntity#path
	 */
	@Override
	public void setPath(final String pPath)
	{
		path = pPath;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "NodeEntity [id=" + id + ", name=" + name + ", path=" + path + "]";
	}

}
