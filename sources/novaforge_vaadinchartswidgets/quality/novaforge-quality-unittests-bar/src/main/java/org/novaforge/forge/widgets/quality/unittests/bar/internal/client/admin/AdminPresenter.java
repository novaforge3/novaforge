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
package org.novaforge.forge.widgets.quality.unittests.bar.internal.client.admin;

import java.util.Locale;

import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.widgets.quality.unittests.bar.internal.admin.PropertiesFactory;

/**
 * @author Gauthier Cart
 */
public class AdminPresenter extends org.novaforge.forge.widgets.quality.client.admin.AdminPresenter
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 6096329371895150327L;

  /**
   * View associated to this presenter
   */
  private final AdminView   view;

  /**
   * @param pWidgetContext
   * @param pView
   */
  public AdminPresenter(final WidgetContext pWidgetContext, final AdminView pView)
  {
    super(pWidgetContext, pView);
    view = pView;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    super.refreshContent();

    try
    {

      view.getNumberOfBuild().setValue(
          PropertiesFactory.readPropertiesNumberOfBuild(getWidgetContext().getProperties()));

    }
    catch (final Exception e)
    {
      e.printStackTrace();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    super.refreshLocalized(pLocale);

  }

  public Integer getNumberOfBuild()
  {
    return view.getNumberOfBuild().getValue();
  }
}
