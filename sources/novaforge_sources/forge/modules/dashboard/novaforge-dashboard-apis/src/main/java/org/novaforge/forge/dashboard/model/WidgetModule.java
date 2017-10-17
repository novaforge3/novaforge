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
package org.novaforge.forge.dashboard.model;

import java.util.List;
import java.util.Map;

/**
 * This interface describes a widget which can be added to dashboard
 * 
 * @author Guillaume Lamirand
 */
public interface WidgetModule
{
  /**
   * Refere to OSGi property id
   */
  String WIDGET_KEY      = "widget-key";
  /**
   * Key used for unavailable widget
   */
  String UNAVAILABLE_KEY = "unavailable";
  /**
   * Key used for no settings widget
   */
  String NO_SETTINGS_KEY = "nosettings";
  /**
   * Key used for loading widget
   */
  String LOADING_KEY     = "loading";

  /**
   * Return a key which defined this {@link WidgetModule}
   *
   * @return key
   */
  String getKey();

  /**
   * Return a byte array containing icon binary
   *
   * @return icon as a byte array
   */
  byte[] getIcon();

  /**
   * Return a byte array containing preview icon binary
   *
   * @return preview icon as a byte array
   */
  byte[] getPreview();

  /**
   * Return a {@link List} of category id
   *
   * @return {@link List} of category id
   */
  List<String> getCategories();

  /**
   * Return a new {@link WidgetDataComponent} used to displayed this module.You should validate the context to
   * be sure it is ok.
   *
   * @param pWidgetContext
   *          the current context
   * @return a new {@link WidgetDataComponent}
   */
  WidgetDataComponent createDataComponent(final WidgetContext pWidgetContext);

  /**
   * Return a new {@link WidgetAdminComponent} used to displayed this module. You should validate the context
   * to be sure it is ok.
   *
   * @param pWidgetContext
   *          the current context
   * @return a new {@link WidgetAdminComponent}
   */
  WidgetAdminComponent createAdminComponent(final WidgetContext pWidgetContext);

  /**
   * Return {@link DataSourceOptions} which defined if the widget is compatible with one project or more and
   * if
   * it needs 0, one or more applications by pro.jects
   *
   * @return {@link DataSourceOptions}
   */
  DataSourceOptions getDataSourceOptions();

  /**
   * Check if the given project datasource is valid according the datasource option and the widget. The
   * implementation should check carefuly the content of the context.
   *
   * @param pDataSource
   *          the datasource to valid
   * @return true if the projects context is valid, false otherwise
   */
  boolean isValidDataSource(final Map<String, List<String>> pDataSource);

  /**
   * Check if the given properties contains valid configuration for the widget. The implementation
   * should check carefuly the content of the context.
   * 
   * @param pProperties
   *          the properties to valid
   * @return true if the properties is valid, false otherwise
   */
  boolean isValidProperties(String pProperties);
}
