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
package org.novaforge.forge.ui.portal.internal.publicmodule.client;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Window;
import org.novaforge.forge.ui.portal.internal.publicmodule.client.components.CustomLoginForm;
import org.novaforge.forge.ui.portal.internal.publicmodule.client.components.UserRegisterForm;

import java.util.Locale;

/**
 * This view is shown on the public view
 *
 * @author Guillaume Lamirand
 */
public interface HomeView extends ComponentContainer
{

  /**
   * Get the Lost Account Button
   *
   * @return {@link Button} the lost account button
   */
  Button getLostAccountButton();

  /**
   * Get the form used to login
   *
   * @return {@link CustomLoginForm} the login form
   */
  CustomLoginForm getLoginForm();

  /**
   * Get the Form used to register
   *
   * @return {@link UserRegisterForm} the register form
   */
  UserRegisterForm getRegisterForm();

  /**
   * Get the title icon
   *
   * @return {@link Embedded} icon of title
   */
  Embedded getTitleIcon();

  void showConfirmRegistrationWindow(final String login, final Locale pLocale);

  /**
   * Initialize Home Layout
   *
   * @param pIsEnable
   *     true to display register form, false otherwise
   */
  void enableCreateUser(final boolean pIsEnable);

  /**
   * Initialize Home Layout
   *
   * @param pIsEnable
   *     true to display lost account button, false otherwise
   */
  void enableUpdateUser(final boolean pIsEnable);

  /**
   * Get the register confirmation window
   *
   * @return {@link Window} The confirm window
   */
  Window getConfirmRegistrationWindow();

  /**
   * Get the register confirm button
   *
   * @return {@link Button} The confirm button
   */
  Button getConfirmRegistrationButton();

  /**
   * Should be call to refresh view when locale has changed
   *
   * @param pLocale
   *     the new locale
   */
  void refreshLocale(final Locale pLocale);

}
