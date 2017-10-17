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
package org.novaforge.forge.core.plugins.categories.bugtracker;

import org.novaforge.forge.core.plugins.categories.PluginExchangeableBean;

import java.util.List;

/**
 * This is used to encapsule a list a project version bean returned by BugTracker Application
 * 
 * @author lamirang
 */
public interface BugTrackerProjectVersionsBean extends PluginExchangeableBean
{
  /**
   * You should use add and remove method to manage list content.
   * 
   * @return unmodifiable list of version available for a project
   * @see BugTrackerProjectVersionsBean#addVersion(String)
   * @see BugTrackerProjectVersionsBean#removeVersion(String)
   */
  List<String> getVersions();

  /**
   * @param pVersion
   *          represents the version to add
   */
  void addVersion(final String pVersion);

  /**
   * @param pVersion
   *          represents the version to add
   */
  void removeVersion(final String pVersion);
}
