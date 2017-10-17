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

import org.novaforge.forge.core.plugins.categories.scm.SCMNodeEntryBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sbenoist
 * @author Guillaume Lamirand
 */
public class SCMNodeEntryBeanImpl implements SCMNodeEntryBean
{
  /**
   * Serial version id
   */
  private static final long      serialVersionUID = 5249956207904587374L;

  /**
   * The author of the current node
   */
  private String                 author;

  /**
   * The revisions of the current node
   */
  private String                 revision;

  /**
   * Path to the current node from the root
   */
  private String                 path;

  /**
   * Last updated date
   */
  private Date                   date;

  /**
   * True if the current node is a directory, false if it's a file
   */
  private boolean                isDirectory;

  /**
   * This contains a list of children only if the node is a directory, otherwise the list is empty
   * 
   * @see SCMNodeEntryBeanImpl@isDirectory
   */
  private List<SCMNodeEntryBean> children         = new ArrayList<SCMNodeEntryBean>();

  /**
   * {@inheritDoc}
   * 
   * @see SCMNodeEntryBeanImpl#author
   */
  @Override
  public String getAuthor()
  {
    return author;
  }

  /**
   * {@inheritDoc}
   * 
   * @see SCMNodeEntryBeanImpl#author
   */
  @Override
  public void setAuthor(final String pAuthor)
  {
    author = pAuthor;
  }

  /**
   * {@inheritDoc}
   * 
   * @see SCMNodeEntryBeanImpl#revision
   */
  @Override
  public String getRevision()
  {
    return revision;
  }

  /**
   * {@inheritDoc}
   * 
   * @see SCMNodeEntryBeanImpl#revision
   */
  @Override
  public void setRevision(final String pRevision)
  {
    revision = pRevision;
  }

  /**
   * {@inheritDoc}
   * 
   * @see SCMNodeEntryBeanImpl#path
   */
  @Override
  public String getPath()
  {
    return path;
  }

  /**
   * {@inheritDoc}
   * 
   * @see SCMNodeEntryBeanImpl#path
   */
  @Override
  public void setPath(final String pPath)
  {
    path = pPath;
  }

  /**
   * {@inheritDoc}
   * 
   * @see SCMNodeEntryBeanImpl#date
   */
  @Override
  public Date getDate()
  {
    return date;
  }

  /**
   * {@inheritDoc}
   * 
   * @see SCMNodeEntryBeanImpl#date
   */
  @Override
  public void setDate(final Date pDate)
  {
    date = pDate;
  }

  /**
   * {@inheritDoc}
   * 
   * @see SCMNodeEntryBeanImpl#isDirectory
   */
  @Override
  public boolean isDirectory()
  {
    return isDirectory;
  }

  /**
   * {@inheritDoc}
   * 
   * @see SCMNodeEntryBeanImpl#isDirectory
   */
  @Override
  public void setDirectory(final boolean pIsDirectory)
  {
    isDirectory = pIsDirectory;
  }

  /**
   * {@inheritDoc}
   * 
   * @see SCMNodeEntryBeanImpl#children
   */
  @Override
  public List<SCMNodeEntryBean> getChildren()
  {
    return children;
  }

  /**
   * {@inheritDoc}
   * 
   * @see SCMNodeEntryBeanImpl#children
   */
  @Override
  public void setChildren(final List<SCMNodeEntryBean> pChildren)
  {
    children = pChildren;
  }

}
