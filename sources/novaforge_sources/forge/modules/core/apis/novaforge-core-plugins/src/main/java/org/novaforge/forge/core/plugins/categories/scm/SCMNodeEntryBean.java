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
package org.novaforge.forge.core.plugins.categories.scm;

import org.novaforge.forge.core.plugins.categories.PluginExchangeableBean;

import java.util.Date;
import java.util.List;

/**
 * Define a node handled by a SCM repository
 * 
 * @author lamirang
 */
public interface SCMNodeEntryBean extends PluginExchangeableBean
{

  /**
   * @return author of the node
   */
  String getAuthor();

  /**
   * @param pAuthor
   *          represensts the author to set
   */
  void setAuthor(final String pAuthor);

  /**
   * @return revision of the node
   */
  String getRevision();

  /**
   * @param pRevision
   *          represensts the revision to set
   */
  void setRevision(final String pRevision);

  /**
   * @return path of the node
   */
  String getPath();

  /**
   * @param pPath
   *          represensts the path to set
   */
  void setPath(final String pPath);

  /**
   * @return last updated date of the node
   */
  Date getDate();

  /**
   * @param pDate
   *          represensts the date to set
   */
  void setDate(final Date pDate);

  /**
   * @return true if its a directory otherwise false
   */
  boolean isDirectory();

  /**
   * @param pIsDirectory
   *          represensts the boolean to set
   */
  void setDirectory(final boolean pIsDirectory);

  /**
   * @return list of children. This can be null or empty if the node is not a directory
   */
  List<SCMNodeEntryBean> getChildren();

  /**
   * @param pChildren
   *          represensts the children to set
   */
  void setChildren(final List<SCMNodeEntryBean> pChildren);

}