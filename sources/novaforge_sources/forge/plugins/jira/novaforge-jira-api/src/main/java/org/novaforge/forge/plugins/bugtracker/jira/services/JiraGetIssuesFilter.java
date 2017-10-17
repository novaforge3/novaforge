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
package org.novaforge.forge.plugins.bugtracker.jira.services;

/**
 * Filter for Jira issues by :
 * * Label
 * * Fix Version
 * * Affected Version
 * 
 * @author Gauthier Cart
 */
public interface JiraGetIssuesFilter
{
  /**
   * Get the label to filter
   * 
   * @return the label to filter
   */
  String getLabel();

  /**
   * Set the label to filter
   * 
   * @param pLabel
   *          the label to filter
   */
  void setLabel(String pLabel);

  /**
   * Get the fix version to filter
   * 
   * @return the fix version to filter
   */
  String getFixVersion();

  /**
   * Set the fix version to filter
   * 
   * @param pFixVersion
   *          the fix version to filter
   */
  void setFixVersion(String pFixVersion);

  /**
   * Get the affected version to filter
   * 
   * @return the affected version to filter
   */
  String getAffectedVersion();

  /**
   * Set the affected version to filter
   * 
   * @param pAffectedVersion
   *          the affected version to filter
   */
  void setAffectedVersion(String pAffectedVersion);

}
