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
 * DTO used to propagate the Dokuwiki application attachment. Used by the PluginDataService.
 * 
 * @author salvat-a
 */
public class DokuwikiAttachmentDTO
{
  private String name;

  private String content;

  /**
    *
    */
  public DokuwikiAttachmentDTO()
  {
  }

  /**
   * @param name
   * @param content
   */
  public DokuwikiAttachmentDTO(final String name, final String content)
  {
    this.name = name;
    this.content = content;
  }

  public String getName()
  {
    return name;
  }

  public void setName(final String name)
  {
    this.name = name;
  }

  public String getContent()
  {
    return content;
  }

  public void setContent(final String content)
  {
    this.content = content;
  }

  @Override
  public int hashCode()
  {
    int result = name != null ? name.hashCode() : 0;
    result = (31 * result) + (content != null ? content.hashCode() : 0);
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

    final DokuwikiAttachmentDTO that = (DokuwikiAttachmentDTO) o;

    if (content != null ? !content.equals(that.content) : that.content != null)
    {
      return false;
    }
    return !(name != null ? !name.equals(that.name) : that.name != null);

  }

  @Override
  public String toString()
  {
    return "DokuwikiAttachmentDTO{" + "name='" + name + '\'' + ", content='" + content + '\'' + '}';
  }
}
