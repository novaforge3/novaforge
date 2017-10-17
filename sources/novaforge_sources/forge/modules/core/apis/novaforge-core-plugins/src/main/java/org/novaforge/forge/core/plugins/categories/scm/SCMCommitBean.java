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

/**
 * This interface describes a commit object
 * 
 * @author Guillaume Lamirand
 */
public interface SCMCommitBean extends PluginExchangeableBean
{
  /**
   * Represents the revision number of this commit
   * 
   * @return the revision number as String value
   */
  String getRevision();

  /**
   * Set the revision number of this commit
   * 
   * @param pRevision
   *          the revision number to set
   */
  void setRevision(final String pRevision);

  /**
   * Represents the author of this commit
   * 
   * @return the author login
   */
  String getAuthor();

  /**
   * Sets the author of this commit
   * 
   * @param pAuthor
   *          the author login to set
   */
  void setAuthor(final String pAuthor);

  /**
   * Represents the author email of this commit
   * 
   * @return the author email
   */
  String getAuthorEmail();

  /**
   * Sets the author of this commit
   * 
   * @param pAuthorEmail
   *          the author email to set
   */
  void setAuthorEmail(final String pAuthorEmail);

  /**
   * Returns the comments of this commit
   * 
   * @return the comments
   */
  String getComment();

  /**
   * Sets the comments of this commit
   * 
   * @param pComment
   *          the comments to set
   */
  void setComment(final String pComment);

  /**
   * Returns the date of the commit
   * 
   * @return commit date
   */
  Date getDate();

  /**
   * Sets the date of the commits
   * 
   * @param pCommitDate
   *          the commit date
   */
  void setDate(final Date pCommitDate);

  /**
   * Representst the number of changes
   * 
   * @return the modifications
   */
  long getChanges();

  /**
   * Sets the number of changes
   * 
   * @param pChanges
   *          the changes to set
   */
  void setChanges(final long pChanges);

}
