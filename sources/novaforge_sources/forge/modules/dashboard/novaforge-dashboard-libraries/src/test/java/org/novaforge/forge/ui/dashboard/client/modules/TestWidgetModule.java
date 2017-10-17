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
package org.novaforge.forge.ui.dashboard.client.modules;

import org.novaforge.forge.dashboard.model.DataSourceOptions;
import org.novaforge.forge.dashboard.model.WidgetAdminComponent;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.dashboard.model.WidgetDataComponent;

import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class TestWidgetModule extends AbstractWidgetModule
{

  private final boolean needProjects;
  private final boolean isMultiProject;
  private final boolean isMultiApplications;
  private final boolean needApplications;

  public TestWidgetModule(final boolean needProjects, final boolean isMultiProject,
      final boolean needApplications, final boolean isMultiApplications)
  {
    this.needProjects = needProjects;
    this.isMultiProject = isMultiProject;
    this.needApplications = needApplications;
    this.isMultiApplications = isMultiApplications;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getKey()
  {
    return "test";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getIcon()
  {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getPreview()
  {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getCategories()
  {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WidgetDataComponent createDataComponent(final WidgetContext pWidgetContext)
  {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WidgetAdminComponent createAdminComponent(final WidgetContext pWidgetContext)
  {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DataSourceOptions getDataSourceOptions()
  {
    return new DataSourceOptions()
    {

      @Override
      public boolean needProjects()
      {
        return needProjects;
      }

      @Override
      public boolean isMultiProject()
      {
        return isMultiProject;
      }

      @Override
      public boolean needApplications()
      {
        return needApplications;
      }

      @Override
      public boolean isMultiApplications()
      {
        return isMultiApplications;
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValidProperties(final String pProperties)
  {
    // TODO Auto-generated method stub
    return false;
  }

}
