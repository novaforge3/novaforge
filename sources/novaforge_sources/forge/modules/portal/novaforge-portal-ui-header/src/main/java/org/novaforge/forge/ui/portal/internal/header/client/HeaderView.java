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
package org.novaforge.forge.ui.portal.internal.header.client;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Image;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;

import java.util.Locale;

/**
 * This interface describes the header element.
 * 
 * @author Guillaume Lamirand
 */
public interface HeaderView extends ComponentContainer
{

  /**
   * Default account icon size
   */
  int ACCOUNT_ICON_SIZE = 40;

  /**
   * Get {@link Button} element related to other project
   *
   * @return {@link Button} related to other project
   */
  Button getOtherProject();

  /**
   * Get {@link ComboBox} element related to user project list
   *
   * @return {@link ComboBox} related to user project list
   */
  ComboBox getProjectCombo();

  /**
   * Get {@link MenuBar} element related to account menu
   *
   * @return {@link MenuBar} related to account menu
   */
  MenuBar getAccountMenu();

  /**
   * Get {@link Layout} which contains languages components
   *
   * @return {@link Layout} related to languages
   */
  Layout getLanguagesLayout();

  /**
   * @return {@link Layout} which contains projects components
   */
  Layout getProjectsLayout();

  /**
   * @return {@link Button} associated to english language
   */
  Button getEnButton();

  /**
   * @return {@link Button} associated to french language
   */
  Button getFrButton();

  /**
   * Should be called when locale has changed
   *
   * @param pLocale
   *          the new locale
   */
  void refreshLocale(Locale pLocale);

  /**
   * Get the Account icon image
   *
   * @return {@link Image} the image
   */
  Image getAccountIcon();
}
