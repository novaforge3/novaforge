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
package org.novaforge.forge.plugins.wiki.dokuwiki.internal.model;

import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiAttachmentInfo;

public class DokuwikiAttachmentInfoImpl implements DokuwikiAttachmentInfo
{
  private String id;
  private String size;
  private String lastModified;
  private String isImg;
  private String writable;
  private String permissions;

  @Override
  public String getId()
  {
    return id;
  }

  @Override
  public void setId(final String id)
  {
    this.id = id;
  }

  @Override
  public String getSize()
  {
    return size;
  }

  @Override
  public void setSize(final String size)
  {
    this.size = size;
  }

  @Override
  public String getLastModified()
  {
    return lastModified;
  }

  @Override
  public void setLastModified(final String lastModified)
  {
    this.lastModified = lastModified;
  }

  @Override
  public String isImg()
  {
    return isImg;
  }

  @Override
  public void setIsImg(final String isImg)
  {
    this.isImg = isImg;
  }

  @Override
  public String isWritable()
  {
    return writable;
  }

  @Override
  public void setIsWritable(final String isWritable)
  {
    writable = isWritable;
  }

  @Override
  public String getPermissions()
  {
    return permissions;
  }

  @Override
  public void setPermissions(final String permissions)
  {
    this.permissions = permissions;
  }

  @Override
  public String toString()
  {
    return "DokuwikiAttachmentInfoImpl{" + "id='" + id + '\'' + ", size='" + size + '\'' + ", lastModified='"
        + lastModified + '\'' + ", isImg='" + isImg + '\'' + ", writable='" + writable + '\''
        + ", permissions='" + permissions + '\'' + '}';
  }
}
