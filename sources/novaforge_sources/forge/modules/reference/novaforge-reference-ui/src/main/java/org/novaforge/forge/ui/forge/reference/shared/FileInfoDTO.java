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

public class FileInfoDTO implements Serializable
{
	/**
    * 
    */
	private static final long serialVersionUID = 597067389956491634L;
	private String            id;
	private String            name;
	private String            path;
	private String            version;
	private String            extension;
	private boolean           isPublic;
	private long              size;

	public FileInfoDTO()
	{
		// Used for serialization
		super();
	}

	public String getId()
	{
		return id;
	}

	public void setId(final String pId)
	{
		id = pId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String pNname)
	{
		name = pNname;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(final String pPath)
	{
		path = pPath;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(final String pVersion)
	{
		version = pVersion;
	}

	public String getExtension()
	{
		return extension;
	}

	public void setExtension(final String extension)
	{
		this.extension = extension;
	}

	public boolean isPublic()
	{
		return isPublic;
	}

	public void setPublic(final boolean isPublic)
	{
		this.isPublic = isPublic;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((id == null) ? 0 : id.hashCode());
		result = (prime * result) + (isPublic ? 1231 : 1237);
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((path == null) ? 0 : path.hashCode());
		result = (prime * result) + ((version == null) ? 0 : version.hashCode());
		result = (prime * result) + ((extension == null) ? 0 : extension.hashCode());
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
		final FileInfoDTO other = (FileInfoDTO) obj;
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
		if (isPublic != other.isPublic)
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
		if (extension == null)
		{
			if (other.extension != null)
			{
				return false;
			}
		}
		else if (!extension.equals(other.extension))
		{
			return false;
		}
		if (path == null)
		{
			if (other.path != null)
			{
				return false;
			}
		}
		else if (!path.equals(other.path))
		{
			return false;
		}
		if (version == null)
		{
			if (other.version != null)
			{
				return false;
			}
		}
		else if (!version.equals(other.version))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "ToolReferenceDTO [id=" + id + ", name=" + name + ", path=" + path + ", version=" + version + ", extension="
							 + extension + ", isPublic=" + isPublic + "]";
	}

	public Long getSize()
	{
		return size;
	}

	public void setSize(final long length)
	{
		// Divide by 1024 to get size in KB
		size = length / 1024;
	}
}
