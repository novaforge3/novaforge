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
package org.novaforge.forge.ui.forge.reference.shared;

import java.io.Serializable;

public class FieldDescriptionDTO implements Serializable
{
	/**
    * 
    */
	private static final long serialVersionUID = 7843858407989601210L;
	private String            name;
	private String            desc;
	private String            fieldName;
	private String            type;
	private int               size;

	public FieldDescriptionDTO()
	{
		// Used for serialization
		super();
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param pName
	 *          the name to set
	 */
	public void setName(final String pName)
	{
		name = pName;
	}

	/**
	 * @return the desc
	 */
	public String getDesc()
	{
		return desc;
	}

	/**
	 * @param desc
	 *          the desc to set
	 */
	public void setDesc(final String desc)
	{
		this.desc = desc;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName()
	{
		return fieldName;
	}

	/**
	 * @param fieldName
	 *          the fieldName to set
	 */
	public void setFieldName(final String fieldName)
	{
		this.fieldName = fieldName;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type
	 *          the type to set
	 */
	public void setType(final String type)
	{
		this.type = type;
	}

	/**
	 * @return the size
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * @param size
	 *          the size to set
	 */
	public void setSize(final int size)
	{
		this.size = size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((desc == null) ? 0 : desc.hashCode());
		result = (prime * result) + ((fieldName == null) ? 0 : fieldName.hashCode());
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + size;
		result = (prime * result) + ((type == null) ? 0 : type.hashCode());
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
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final FieldDescriptionDTO other = (FieldDescriptionDTO) obj;
		if (desc == null)
		{
			if (other.desc != null)
			{
				return false;
			}
		}
		else if (!desc.equals(other.desc))
		{
			return false;
		}
		if (fieldName == null)
		{
			if (other.fieldName != null)
			{
				return false;
			}
		}
		else if (!fieldName.equals(other.fieldName))
		{
			return false;
		}
		if (name == null)
		{
			if (other.name != null)
			{
				return false;
			}
		}
		else if (!name.equals(other.name))
		{
			return false;
		}
		if (size != other.size)
		{
			return false;
		}
		if (type == null)
		{
			if (other.type != null)
			{
				return false;
			}
		}
		else if (!type.equals(other.type))
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
		return "FieldDescriptionDTO [name=" + name + ", desc=" + desc + ", fieldName=" + fieldName + ", type=" + type
							 + ", size=" + size + "]";
	}

}
