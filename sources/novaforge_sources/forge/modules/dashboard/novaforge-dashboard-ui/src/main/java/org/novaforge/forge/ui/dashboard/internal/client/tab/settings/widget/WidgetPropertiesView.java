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
package org.novaforge.forge.ui.dashboard.internal.client.tab.settings.widget;

import com.vaadin.event.LayoutEvents.LayoutClickNotifier;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Form;
import com.vaadin.ui.TextField;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
@SuppressWarnings("deprecation")
public interface WidgetPropertiesView extends ComponentContainer, LayoutClickNotifier
{

  /**
   * Refresh text view
   * 
   * @param pLocale
   *          the locale used to refresh labels
   */
  void refreshLocale(final Locale pLocale);

  /**
   * Returns {@link TextField} used to set widget name
   * 
   * @return {@link TextField}
   */
  TextField getWidgetNameField();

  /**
   * Returns {@link Form} used to set up widget properties
   * 
   * @return {@link Form}
   */
  Form getWidgetForm();

  /**
   * Returns {@link Button} used to save changes
   * 
   * @return {@link Button}
   */
  Button getSaveButton();

}