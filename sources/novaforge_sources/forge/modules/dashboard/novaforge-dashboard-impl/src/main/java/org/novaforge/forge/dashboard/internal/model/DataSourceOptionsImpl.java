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
package org.novaforge.forge.dashboard.internal.model;

import org.novaforge.forge.dashboard.model.DataSourceOptions;

/**
 * @author Guillaume Lamirand
 */
public class DataSourceOptionsImpl implements DataSourceOptions
{

  /**
   * Defines if widget needs at least one project
   */
  private final boolean needProjects;
  /**
   * Defines if widget can work with more than one projects
   */
  private final boolean multiProjects;
  /**
   * Defines if widget needs at least one app
   */
  private final boolean needApps;
  /**
   * Defines if widget can work with more than one app by projects
   */
  private final boolean multiApps;

  /**
   * Default constructor
   * 
   * @param pNeedProjects
   *          Defines if widget needs at least one project
   * @param pMultiProjects
   *          Defines if widget can work with more than one projects
   * @param pNeedApps
   *          Defines if widget needs at least one app
   * @param pMultiApps
   *          Defines if widget can work with more than one app by projects
   */
  public DataSourceOptionsImpl(final boolean pNeedProjects, final boolean pMultiProjects,
      final boolean pNeedApps, final boolean pMultiApps)
  {
    needProjects = pNeedProjects;
    multiProjects = pMultiProjects;
    needApps = pNeedApps;
    multiApps = pMultiApps;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean needProjects()
  {
    return needProjects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isMultiProject()
  {
    return multiProjects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean needApplications()
  {
    return needApps;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isMultiApplications()
  {
    return multiApps;
  }

}
