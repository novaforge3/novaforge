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

import org.novaforge.forge.tools.deliverymanager.model.CustomFieldType;
import org.novaforge.forge.tools.deliverymanager.model.TemplateCustomField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * This entity describes a custom field used by a report template.
 * 
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "CUSTOM_FIELD")
public class TemplateCustomFieldEntity implements Serializable, TemplateCustomField
{
	/**
    * 
    */
	private static final long serialVersionUID = 2600399186908381083L;
	/**
	 * @see TemplateCustomFieldEntity#getId()
	 * @see TemplateCustomFieldEntity#setId(Long)
	 */
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long              id;
	/**
	 * The name of the template
	 * 
	 * @see TemplateCustomFieldEntity#getName()
	 * @see TemplateCustomFieldEntity#setName(String)
	 */
	@Column(name = "name", nullable = false)
	private String            name;
	/**
	 * Some details about the field
	 * 
	 * @see TemplateCustomFieldEntity#getDescription()
	 * @see TemplateCustomFieldEntity#setDescription(String)
	 */
	@Column(name = "description", nullable = false)
	private String            description;
	/**
	 * The type of the field
	 * 
	 * @see TemplateCustomFieldEntity#getType()
	 * @see TemplateCustomFieldEntity#setType(CustomFieldType)
	 * @see CustomFieldType
	 */
	@Column(name = "type", nullable = false)
	private String            type;

	/**
	 * Default constructor
	 */
	public TemplateCustomFieldEntity()
	{
		// Used by JPA
	}

	/**
	 * @return persistence id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateCustomFieldEntity#name
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
	 * @see TemplateCustomFieldEntity#name
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateCustomFieldEntity#description
	 */
	@Override
	@NotNull
	@Size(min = 1)
	public String getDescription()
	{
		return description;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateCustomFieldEntity#description
	 */
	@Override
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateCustomFieldEntity#type
	 */
	@Override
	public CustomFieldType getType()
	{
		return CustomFieldType.valueOf(type);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateCustomFieldEntity#type
	 */
	@Override
	public void setType(final CustomFieldType type)
	{
		this.type = type.name();
	}

	@Override
	public String toString()
	{
		return "TemplateCustomFieldEntity [id=" + id + ", name=" + name + ", description=" + description + ", type=" + type
							 + "]";
	}

}
