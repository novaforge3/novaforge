/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.widgets.quality.module;

import java.util.ArrayList;
import java.util.List;

import org.novaforge.forge.dashboard.model.DataSourceOptions;
import org.novaforge.forge.dashboard.model.WidgetAdminComponent;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.ui.dashboard.client.modules.AbstractWidgetModule;
import org.novaforge.forge.widgets.quality.admin.PropertiesFactory;
import org.novaforge.forge.widgets.quality.admin.QualityResource;

import com.google.common.base.Strings;

/**
 * @author Guillaume Lamirand
 */
public abstract class AbstractQualityModule extends AbstractWidgetModule
{

  /**
   * {@inheritDoc}
   */
  @Override
  public WidgetAdminComponent createAdminComponent(final WidgetContext pWidgetContext)
  {
    final WidgetAdminComponent widgetAdminComponent = new AdminComponent(pWidgetContext);
    return widgetAdminComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DataSourceOptions getDataSourceOptions()
  {
    return getDataSourceFactory().buildOptions(true, false, true, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValidProperties(final String pProperties)
  {
    final QualityResource resource = PropertiesFactory.readProperties(pProperties);
    return ((Strings.isNullOrEmpty(resource.getId()) == false) || (Strings.isNullOrEmpty(resource.getName()) == false));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getCategories()
  {
    final List<String> categories = new ArrayList<String>();
    categories.add("quality");
    return categories;
  }

}
