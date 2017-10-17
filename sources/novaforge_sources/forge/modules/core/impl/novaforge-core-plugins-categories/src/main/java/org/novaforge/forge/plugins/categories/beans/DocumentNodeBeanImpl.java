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
package org.novaforge.forge.plugins.categories.beans;

import org.novaforge.forge.core.plugins.categories.ecm.DocumentNodeBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class DocumentNodeBeanImpl implements DocumentNodeBean
{

  /**
    * 
    */
  private static final long      serialVersionUID = -6807551865543564931L;
  private String                 id;
  private String                 parentId;
  private String                 name;
  private String                 path;
  private String                 author;
  private Date                   createdDate;
  private String                 lastModified;
  private Date                   lastModifiedDate;
  private boolean                isDocument;

  private List<DocumentNodeBean> children         = new ArrayList<DocumentNodeBean>();

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId()
  {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setId(final String pId)
  {
    id = pId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setName(final String pName)
  {
    name = pName;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPath()
  {
    return path;
  }

  /**
   * {@inheritDoc}
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
  public List<DocumentNodeBean> getChildren()
  {
    return children;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setChildren(final List<DocumentNodeBean> pChild)
  {
    children = pChild;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthor()
  {
    return author;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAuthor(final String pAuthor)
  {
    author = pAuthor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getCreatedDate()
  {
    return createdDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCreatedDate(final Date pCreatedDate)
  {
    createdDate = pCreatedDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLastModified()
  {
    return lastModified;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLastModified(final String pLastModified)
  {
    lastModified = pLastModified;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getLastModifiedDate()
  {
    return lastModifiedDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLastModifiedDate(final Date pLastModifiedDate)
  {
    lastModifiedDate = pLastModifiedDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDocument()
  {
    return isDocument;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDocument(final boolean pIsFile)
  {
    isDocument = pIsFile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getParentId()
  {
    return parentId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setParentId(final String parentId)
  {
    this.parentId = parentId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "DocumentNodeBean [id=" + id + ", parentId=" + parentId + ", name=" + name + ", path=" + path
               + ", isDocument=" + isDocument + "]";
  }

}
