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
package org.novaforge.forge.commons.technical.search.internal;

import org.novaforge.forge.commons.technical.search.SearchResult;

import java.util.Date;

/**
 * @author sbenoist
 */
public class SearchResultImpl implements SearchResult
{
  private final String fileName;

  private String       filePath;

  private String       snippet;

  private long         lineNumber;

  private Date         lastModified;

  private String       occurrence;

  public SearchResultImpl(final String pOccurrence, final String pFileName, final String pFilePath,
      final long pLineNumber)
  {
    super();
    fileName = pFileName;
    filePath = pFilePath;
    lineNumber = pLineNumber;
    occurrence = pOccurrence;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFileName()
  {
    return fileName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFilePath()
  {
    return filePath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFilePath(final String pFilePath)
  {
    filePath = pFilePath;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getLastModified()
  {
    return lastModified;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLastModified(final Date pLastModified)
  {
    lastModified = pLastModified;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getOccurrence()
  {
    return occurrence;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setOccurrence(final String occurrence)
  {
    this.occurrence = occurrence;
  }

  @Override
  public String toString()
  {
    return "SearchResultImpl [fileName=" + fileName + ", filePath=" + filePath + ", snippet=" + snippet
               + ", lineNumber=" + lineNumber + ", lastModified=" + lastModified + ", occurrence=" + occurrence + "]";
  }

}
