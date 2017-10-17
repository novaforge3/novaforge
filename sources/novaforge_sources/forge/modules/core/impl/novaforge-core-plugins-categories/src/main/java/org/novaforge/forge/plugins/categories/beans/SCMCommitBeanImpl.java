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

import org.novaforge.forge.core.plugins.categories.scm.SCMCommitBean;

import java.util.Date;

/**
 * @author Guillaume Lamirand
 */
public class SCMCommitBeanImpl implements SCMCommitBean
{
  /**
   * Serial version id
   */
  private static final long serialVersionUID = 4096700544186663927L;

  /**
   * The author of the commit. Can be null or empty
   */
  private String            authorLogin;
  /**
   * The author email of the commit. Can be null or empty
   */
  private String            authorEmail;

  /**
   * The revisions of the commit
   */
  private String            revision;

  /**
   * The comment of the commit
   */
  private String            comment;

  /**
   * Commit date
   */
  private Date              date;

  /**
   * Number of changes implies by the commit. Can be null or empty
   */
  private long              changes;

  /**
   * {@inheritDoc}
   *
   * @see SCMCommitBeanImpl#revision
   */
  @Override
  public String getRevision()
  {
    return revision;
  }

  /**
   * {@inheritDoc}
   *
   * @see SCMCommitBeanImpl#revision
   */
  @Override
  public void setRevision(final String pRevision)
  {
    revision = pRevision;
  }

  /**
   * {@inheritDoc}
   *
   * @see SCMCommitBeanImpl#author
   */
  @Override
  public String getAuthor()
  {
    return authorLogin;
  }

  /**
   * {@inheritDoc}
   *
   * @see SCMCommitBeanImpl#author
   */
  @Override
  public void setAuthor(final String pAuthor)
  {
    authorLogin = pAuthor;
  }

  /**
   * {@inheritDoc}
   *
   * @see SCMCommitBeanImpl#authorEmail
   */
  @Override
  public String getAuthorEmail()
  {
    return authorEmail;
  }

  /**
   * {@inheritDoc}
   *
   * @see SCMCommitBeanImpl#authorEmail
   */
  @Override
  public void setAuthorEmail(final String pAuthor)
  {
    authorEmail = pAuthor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getComment()
  {
    return comment;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setComment(final String pComment)
  {
    comment = pComment;

  }

  /**
   * {@inheritDoc}
   *
   * @see SCMCommitBeanImpl#date
   */
  @Override
  public Date getDate()
  {
    return date;
  }

  /**
   * {@inheritDoc}
   *
   * @see SCMCommitBeanImpl#date
   */
  @Override
  public void setDate(final Date pDate)
  {
    date = pDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getChanges()
  {
    return changes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setChanges(final long pChanges)
  {
    changes = pChanges;

  }
}
