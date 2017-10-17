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

import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerProjectVersionsBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class BugTrackerProjectVersionsBeanImpl implements BugTrackerProjectVersionsBean
{
  /**
    * 
    */
  private static final long  serialVersionUID = 4863863575356972582L;
  /**
   * Contains versions
   */
  private final List<String> versions;

  public BugTrackerProjectVersionsBeanImpl()
  {
    versions = new ArrayList<String>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getVersions()
  {
    return Collections.unmodifiableList(versions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addVersion(final String pVersion)
  {
    versions.add(pVersion);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeVersion(final String pVersion)
  {
    versions.remove(pVersion);
  }

}
