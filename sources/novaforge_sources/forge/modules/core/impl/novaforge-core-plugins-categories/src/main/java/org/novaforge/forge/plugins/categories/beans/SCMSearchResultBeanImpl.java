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

import org.novaforge.forge.core.plugins.categories.scm.SCMSearchResultBean;

/**
 * @author sbenoist
 */
public class SCMSearchResultBeanImpl implements SCMSearchResultBean
{
  private static final long serialVersionUID = 8752464192561917866L;

  private String            path;

  private String            className;

  private String            packageName;

  private String            snippet;

  private long              lineNumber;

  private String            occurrence;

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
  public String getClassName()
  {
    return className;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setClassName(final String pClassName)
  {
    className = pClassName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPackageName()
  {
    return packageName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPackageName(final String pPackageName)
  {
    packageName = pPackageName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSnippet()
  {
    return snippet;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSnippet(final String pSnippet)
  {
    snippet = pSnippet;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getLineNumber()
  {
    return lineNumber;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLineNumber(final long pLineNumber)
  {
    lineNumber = pLineNumber;
  }

  @Override
  public String getOccurrence()
  {
    return occurrence;
  }

  @Override
  public void setOccurrence(final String pOccurrence)
  {
    occurrence = pOccurrence;
  }

}
