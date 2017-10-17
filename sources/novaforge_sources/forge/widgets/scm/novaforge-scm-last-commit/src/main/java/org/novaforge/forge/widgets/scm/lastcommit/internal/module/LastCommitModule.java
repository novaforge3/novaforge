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
package org.novaforge.forge.widgets.scm.lastcommit.internal.module;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.dashboard.model.DataSourceOptions;
import org.novaforge.forge.dashboard.model.WidgetAdminComponent;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.dashboard.model.WidgetDataComponent;
import org.novaforge.forge.ui.dashboard.client.modules.AbstractWidgetModule;
import org.novaforge.forge.widgets.scm.lastcommit.internal.admin.PropertiesFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class LastCommitModule extends AbstractWidgetModule
{

  /**
   * Logger component
   */
  private static final Log     LOGGER = LogFactory.getLog(LastCommitModule.class);
  /**
   * Contains reference to {@link UserPresenter} implementation
   */
  private static UserPresenter USER_PRESENTER;

  /**
   * @return the {@link UserPresenter}
   */
  public static UserPresenter getUserPresenter()
  {
    return USER_PRESENTER;
  }

  /**
   * Use by container to inject {@link UserPresenter}
   *
   * @param pUserPresenter
   *          the userPresenter to set
   */
  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    USER_PRESENTER = pUserPresenter;
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
   * Return the widget key associated to the current module
   *
   * @return widget key
   */
  public static String getWidgetKey()
  {
    return "scmlastcommit";
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
      final InputStream resource = this.getClass().getResourceAsStream("/lastcommit.png");
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
    final List<String> categories = new ArrayList<String>();
    categories.add("scm");
    return categories;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WidgetDataComponent createDataComponent(final WidgetContext pWidgetContext)
  {
    if ((!isValidDataSource(pWidgetContext.getApplicationsByProject())) || (!isValidProperties(pWidgetContext
                                                                                                   .getProperties())))
    {
      throw new IllegalArgumentException(String.format("The given context is not valid for the current widget [key=%s]",
                                                       getWidgetKey()));
    }
    return new DataComponent(pWidgetContext);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WidgetAdminComponent createAdminComponent(final WidgetContext pWidgetContext)
  {
    return new AdminComponent(pWidgetContext);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DataSourceOptions getDataSourceOptions()
  {
    return getDataSourceFactory().buildOptions(true, false, true, true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValidProperties(final String pProperties)
  {
    boolean isValid = false;
    try
    {
      final int commitsNumber = PropertiesFactory.readProperties(pProperties);
      isValid = commitsNumber > 0;
    }
    catch (final Exception e)
    {
      isValid = false;
    }
    return isValid;
  }

}