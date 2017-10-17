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

import com.vaadin.ui.UI;
import org.novaforge.forge.dashboard.model.WidgetContext;

import java.util.Locale;

/**
 * Default behaviour for presenter used by Data part of widget
 * 
 * @author Guillaume Lamirand
 */
public abstract class AbstractWidgetDataChartPresenter extends AbstractWidgetDataPresenter
{

  /**
   * Default constructor
   * 
   * @param pWidgetContext
   *          the widgetContext
   */
  public AbstractWidgetDataChartPresenter(final WidgetContext pWidgetContext)
  {
    super(pWidgetContext);
  }

  /**
   * Called to refresh content when {@link UI} has been refreshed
   */
  @Override
  protected void refreshContent()
  {
    refreshData();
    refreshComponent();
  }

  /**
   * Method to display the graphical component depending on the given locale
   *
   * @param pLocale
   *          given locale
   */
  protected abstract void refreshComponent(final Locale pLocale);

  /** {@inheritDoc} */
  @Override
  public void refreshComponent()
  {
    refreshComponent(getCurrentLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    refreshData();
    refreshComponent(pLocale);
  }

}
