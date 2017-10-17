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

/**
 * Defined a Java Search Result handled by SCM
 * 
 * @author sbenoist
 */
public interface SCMSearchResultBean extends PluginExchangeableBean
{
  /**
   * @return the path of the file
   */
  String getPath();

  /**
   * @param pPath
   */
  void setPath(final String pPath);

  /**
   * @return the class name
   */
  String getClassName();

  /**
   * @param pClassName
   */
  void setClassName(final String pClassName);

  /**
   * @return the package name
   */
  String getPackageName();

  /**
   * @param pPackageName
   */
  void setPackageName(final String pPackageName);

  /**
   * @return the snippet
   */
  String getSnippet();

  /**
   * @param pSnippet
   */
  void setSnippet(final String pSnippet);

  /**
   * @return the occurrence according to the regex
   */
  String getOccurrence();

  /**
   * @param pOccurrence
   */
  void setOccurrence(String pOccurrence);

  /**
   * @return the line number
   */
  long getLineNumber();

  /**
   * @param pLineNumber
   */
  void setLineNumber(final long pLineNumber);
}
