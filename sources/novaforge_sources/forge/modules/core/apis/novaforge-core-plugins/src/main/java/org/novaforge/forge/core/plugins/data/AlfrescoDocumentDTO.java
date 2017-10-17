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

import javax.activation.DataHandler;

public class AlfrescoDocumentDTO
{
  private String      name;

  private String      path;

  private String      parentPath;

  private String      contentSreamFileName;

  private String      contentStreamMimeType;

  private Long        contentStreamLength;

  private String      versionLabel;

  private DataHandler content;

  public String getName()
  {
    return name;
  }

  public void setName(final String name)
  {
    this.name = name;
  }

  public String getPath()
  {
    return path;
  }

  public void setPath(final String path)
  {
    this.path = path;
  }

  public String getParentPath()
  {
    return parentPath;
  }

  public void setParentPath(final String parentPath)
  {
    this.parentPath = parentPath;
  }

  public String getContentSreamFileName()
  {
    return contentSreamFileName;
  }

  public void setContentSreamFileName(final String contentSreamFileName)
  {
    this.contentSreamFileName = contentSreamFileName;
  }

  public String getContentStreamMimeType()
  {
    return contentStreamMimeType;
  }

  public void setContentStreamMimeType(final String contentStreamMimeType)
  {
    this.contentStreamMimeType = contentStreamMimeType;
  }

  public Long getContentStreamLength()
  {
    return contentStreamLength;
  }

  public void setContentStreamLength(final Long contentStreamLength)
  {
    this.contentStreamLength = contentStreamLength;
  }

  public String getVersionLabel()
  {
    return versionLabel;
  }

  public void setVersionLabel(final String versionLabel)
  {
    this.versionLabel = versionLabel;
  }

  public DataHandler getContent()
  {
    return content;
  }

  public void setContent(final DataHandler content)
  {
    this.content = content;
  }

  @Override
  public int hashCode()
  {
    int result = name != null ? name.hashCode() : 0;
    result = (31 * result) + (path != null ? path.hashCode() : 0);
    result = (31 * result) + (parentPath != null ? parentPath.hashCode() : 0);
    result = (31 * result) + (contentSreamFileName != null ? contentSreamFileName.hashCode() : 0);
    result = (31 * result) + (contentStreamMimeType != null ? contentStreamMimeType.hashCode() : 0);
    result = (31 * result) + (contentStreamLength != null ? contentStreamLength.hashCode() : 0);
    result = (31 * result) + (versionLabel != null ? versionLabel.hashCode() : 0);
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

    final AlfrescoDocumentDTO that = (AlfrescoDocumentDTO) o;

    if (content != null ? !content.equals(that.content) : that.content != null)
    {
      return false;
    }
    if (contentSreamFileName != null ? !contentSreamFileName.equals(that.contentSreamFileName)
        : that.contentSreamFileName != null)
    {
      return false;
    }
    if (contentStreamLength != null ? !contentStreamLength.equals(that.contentStreamLength)
        : that.contentStreamLength != null)
    {
      return false;
    }
    if (contentStreamMimeType != null ? !contentStreamMimeType.equals(that.contentStreamMimeType)
        : that.contentStreamMimeType != null)
    {
      return false;
    }
    if (name != null ? !name.equals(that.name) : that.name != null)
    {
      return false;
    }
    if (parentPath != null ? !parentPath.equals(that.parentPath) : that.parentPath != null)
    {
      return false;
    }
    if (path != null ? !path.equals(that.path) : that.path != null)
    {
      return false;
    }
    return !((versionLabel != null) ? !versionLabel.equals(that.versionLabel) : (that.versionLabel != null));

  }

  @Override
  public String toString()
  {
    return "AlfrescoDocumentDTO{" + "name='" + name + '\'' + ", path='" + path + '\'' + ", parentPath='"
        + parentPath + '\'' + ", contentSreamFileName='" + contentSreamFileName + '\''
        + ", contentStreamMimeType='" + contentStreamMimeType + '\'' + ", contentStreamLength="
        + contentStreamLength + ", versionLabel='" + versionLabel + '\'' + ", content=" + content + '}';
  }
}
