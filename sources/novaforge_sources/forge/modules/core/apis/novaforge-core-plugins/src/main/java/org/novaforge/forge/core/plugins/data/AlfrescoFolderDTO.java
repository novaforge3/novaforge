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
package org.novaforge.forge.core.plugins.data;

public class AlfrescoFolderDTO
{
  private String name;

  private String path;

  private String parentPath;

  public String getName()
  {
    return name;
  }

  public void setName(final String name)
  {
    this.name = name;
  }

  public String getParentPath()
  {
    return parentPath;
  }

  public void setParentPath(final String parentPath)
  {
    this.parentPath = parentPath;
  }

  public String getPath()
  {
    return path;
  }

  public void setPath(final String path)
  {
    this.path = path;
  }

  @Override
  public int hashCode()
  {
    int result = name != null ? name.hashCode() : 0;
    result = (31 * result) + (parentPath != null ? parentPath.hashCode() : 0);
    result = (31 * result) + (path != null ? path.hashCode() : 0);
    return result;
  }

  @Override
  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass()))
    {
      return false;
    }

    final AlfrescoFolderDTO folderDTO = (AlfrescoFolderDTO) o;

    if (name != null ? !name.equals(folderDTO.name) : folderDTO.name != null)
    {
      return false;
    }
    if (parentPath != null ? !parentPath.equals(folderDTO.parentPath) : folderDTO.parentPath != null)
    {
      return false;
    }
    return !(path != null ? !path.equals(folderDTO.path) : folderDTO.path != null);

  }

  @Override
  public String toString()
  {
    return "AlfrescoFolderDTO{" + "name='" + name + '\'' + ", parentPath='" + parentPath + '\'' + ", path='"
        + path + '\'' + '}';
  }
}
