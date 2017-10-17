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
package org.novaforge.forge.widgets.scm.lastcommit.internal.admin;

import com.vaadin.ui.Component;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.ui.dashboard.client.modules.AbstractWidgetPresenter;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author bruno
 */
@SuppressWarnings("serial")
public class AdminPresenter extends AbstractWidgetPresenter implements Serializable
{

  /**
   * View associated to this presenter
   */
  private final AdminView view;

  /**
   * @param pWidgetContext
   */
  public AdminPresenter(final WidgetContext pWidgetContext, final AdminView pView)
  {
    super(pWidgetContext);
    // Init the view
    view = pView;
  }

  public void refresh()
  {
    refreshContent();
    view.refreshLocale(view.getLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    try
    {
      view.getNumberOfCommit().setValue(PropertiesFactory.readProperties(getWidgetContext().getProperties()));
    }
    catch (final Exception e)
    {
      // TODO: handle exception
      e.printStackTrace();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale locale)
  {
    view.refreshLocale(locale);
  }

  public Integer getNumberOfCommit()
  {
    return view.getNumberOfCommit().getValue();
  }

  /**
   * @return
   */
  public boolean isValid()
  {
    boolean isValid = false;
    try
    {
      view.getNumberOfCommit().validate();
      isValid = true;
    }
    catch (final Exception e)
    {
      // Ignore it
    }
    return isValid;
  }
}
