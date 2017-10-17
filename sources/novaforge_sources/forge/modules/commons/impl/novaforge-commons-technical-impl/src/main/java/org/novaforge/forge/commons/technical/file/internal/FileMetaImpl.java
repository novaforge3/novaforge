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
package org.novaforge.forge.commons.technical.file.internal;

import org.novaforge.forge.commons.technical.file.FileMeta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class FileMetaImpl implements Serializable, FileMeta
{
  /**
    * 
    */
  private static final long serialVersionUID = -2028663597803340007L;
  private String            type;
  private String            size;
  private String            url;
  private File              file;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getType()
  {
    return type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setType(final String type)
  {
    this.type = type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSize()
  {
    return size;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSize(final String size)
  {
    this.size = size;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUrl()
  {
    return url;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUrl(final String url)
  {
    this.url = url;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getFile()
  {
    return file;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFile(final File file)
  {
    this.file = file;
  }

  public void setFile(final InputStream inputStream)
  {
    try
    {
      file = File.createTempFile("commonFile", "nfg");

      file.deleteOnExit();
      final OutputStream out = new FileOutputStream(file);
      final byte buf[] = new byte[1024];
      int len;
      while ((len = inputStream.read(buf)) > 0)
      {
        out.write(buf, 0, len);
      }
      out.close();
      inputStream.close();
    }
    catch (final IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((file == null) ? 0 : file.hashCode());
    result = (prime * result) + ((size == null) ? 0 : size.hashCode());
    result = (prime * result) + ((type == null) ? 0 : type.hashCode());
    result = (prime * result) + ((url == null) ? 0 : url.hashCode());
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
    if (this.getClass() != obj.getClass())
    {
      return false;
    }
    final FileMetaImpl other = (FileMetaImpl) obj;
    if (file == null)
    {
      if (other.file != null)
      {
        return false;
      }
    }
    else if (!file.equals(other.file))
    {
      return false;
    }
    if (size == null)
    {
      if (other.size != null)
      {
        return false;
      }
    }
    else if (!size.equals(other.size))
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
    if (url == null)
    {
      if (other.url != null)
      {
        return false;
      }
    }
    else if (!url.equals(other.url))
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
    return "FileMetaImpl [type=" + type + ", size=" + size + ", url=" + url + ", file=" + file + "]";
  }
}
