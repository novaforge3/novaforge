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

/**
 * DTO used to propagate the Dokuwiki application pages. Used by the PluginDataService.
 * 
 * @author rols-p
 */
public class DokuwikiPageDTO
{
  private String name;

  private String description;

  private String content;

  /**
    * 
    */
  public DokuwikiPageDTO()
  {
  }

  /**
   * @param name
   * @param description
   * @param content
   */
  public DokuwikiPageDTO(final String name, final String description, final String content)
  {
    this.name = name;
    this.description = description;
    this.content = content;
  }

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(final String name)
  {
    this.name = name;
  }

  /**
   * @return the description
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @param description
   *          the description to set
   */
  public void setDescription(final String description)
  {
    this.description = description;
  }

  /**
   * @return the content
   */
  public String getContent()
  {
    return content;
  }

  /**
   * @param content
   *          the content to set
   */
  public void setContent(final String content)
  {
    this.content = content;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((name == null) ? 0 : name.hashCode());
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
    final DokuwikiPageDTO other = (DokuwikiPageDTO) obj;
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
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "DokuwikiDataDTO [name=" + name + ", description=" + description + ", content=" + content + "]";
  }
}
