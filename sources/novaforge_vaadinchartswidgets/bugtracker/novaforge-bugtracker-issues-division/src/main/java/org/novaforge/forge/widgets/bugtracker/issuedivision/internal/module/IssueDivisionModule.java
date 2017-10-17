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
package org.novaforge.forge.widgets.bugtracker.issuedivision.internal.module;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.dashboard.model.DataSourceOptions;
import org.novaforge.forge.dashboard.model.WidgetAdminComponent;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.dashboard.model.WidgetDataComponent;
import org.novaforge.forge.ui.dashboard.client.modules.AbstractWidgetModule;

/**
 * @author Gauthier Cart
 */
public class IssueDivisionModule extends AbstractWidgetModule
{

  /**
   * Logger component
   */
  private static final Log LOGGER = LogFactory.getLog(IssueDivisionModule.class);

  /**
   * Return the widget key associated to the current module
   * 
   * @return widget key
   */
  public static String getWidgetKey()
  {
    return "bugtrackerissuedivision";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getKey()
  {
    return getWidgetKey();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WidgetDataComponent createDataComponent(final WidgetContext pWidgetContext)
  {
    final WidgetDataComponent widgetDataComponent = new DataComponent(pWidgetContext);
    return widgetDataComponent;
  }

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
  public byte[] getIcon()
  {
    byte[] byteArray = null;
    try
    {
      final InputStream resource = this.getClass().getResourceAsStream("/issuedivision.png");
      byteArray = IOUtils.toByteArray(resource);
    }
    catch (final IOException e)
    {
      LOGGER.error("Unable to build a byte array from inputstream given", e);
    }
    return byteArray;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DataSourceOptions getDataSourceOptions()
  {
    return getDataSourceFactory().buildOptions(true, true, true, true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValidProperties(final String pProperties)
  {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getPreview()
  {
    byte[] byteArray = null;
    try
    {
      final InputStream resource = this.getClass().getResourceAsStream("/preview.png");
      byteArray = IOUtils.toByteArray(resource);
    }
    catch (final IOException e)
    {
      LOGGER.error("Unable to build a byte array from inputstream given", e);
    }
    return byteArray;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getCategories()
  {
    final List<String> categories = new ArrayList<String>();
    categories.add("bugtracker");
    return categories;
  }

}
