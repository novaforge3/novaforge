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
package org.novaforge.forge.ui.user.management.internal.client.userpublicprofile;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;
import org.vaadin.addon.itemlayout.horizontal.ItemHorizontal;

import java.util.Locale;

/**
 * The UserPublicProfile interface
 * 
 * @author caseryj
 */
public interface UserPublicProfileView extends ComponentContainer
{

  /**
   * Get the return to users list button
   * 
   * @return the button
   */
  Button getBackToUsersAdminButton();

  /**
   * Initialize the label with given {@link Locale}
   */
  void refreshLocale(Locale pLocale);

  /**
   * get the delete user button
   * 
   * @return the deleteUserButton
   */
  Button getDeleteUserButton();

  /**
   * Get the user picture
   * 
   * @return the {@link Embedded} userPicture
   */
  Embedded getUserPicture();

  /**
   * Get the user language field
   * 
   * @return the {@link Label} userLanguage
   */
  Label getUserLanguage();

  /**
   * Get the user firstname field
   * 
   * @return the {@link Label} userFisrtnameField
   */
  Label getUserFisrtnameField();

  /**
   * Get the user lastname field
   * 
   * @return the {@link Label} userLastnameField
   */
  Label getUserLastnameField();

  /**
   * Get the user login field
   * 
   * @return the {@link Label} userLoginField
   */
  Label getUserLoginField();

  /**
   * Get the user email field
   * 
   * @return the {@link HorizontalLayout} userEmailField
   */
  HorizontalLayout getUserEmailLayout();

  /**
   * Get the user phone work field
   * 
   * @return the {@link Label} userPhoneWorkField
   */
  Label getUserPhoneWorkField();

  /**
   * Get the user phone mobile field
   * 
   * @return the {@link Label} userPhoneMobileField
   */
  Label getUserPhoneMobileField();

  /**
   * Get the user company name field
   * 
   * @return the {@link Label} userCompanyNameField
   */
  Label getUserCompanyNameField();

  /**
   * Get the user company address field
   * 
   * @return the {@link Label} userCompanyAddressField
   */
  Label getUserCompanyAddressField();

  /**
   * Get the user company office field
   * 
   * @return the {@link Label} userCompanyOfficeField
   */
  Label getUserCompanyOfficeField();

  /**
   * Show or not the contact section
   * 
   * @param pIsShow
   *          true to show, false otehrwise
   */
  void showSectionContact(boolean pIsShow);

  /**
   * Show or not the work section
   * 
   * @param pIsShow
   *          true to show, false otehrwise
   */
  void showSectionWork(boolean pIsShow);

  /**
   * Show or not the project section
   * 
   * @param pIsShow
   *          true to show, false otehrwise
   */
  void showSectionProjects(boolean pIsShow);

  /**
   * Enable or not the admin features
   * 
   * @param pIsAdmin
   *          true to enable, false otherwise
   * @param pIsFromAdminView
   *          true is called from admin view
   */
  void setAdminActionsEnabled(boolean pIsAdmin, boolean pIsFromAdminView);

  /**
   * Get the layout containing user's projects
   * 
   * @return {@link ItemHorizontal} the layout
   */
  ItemHorizontal getProjectList();

  /**
   * Get the user phone mobile field caption
   * 
   * @return the {@link Label}
   */
  Label getUserPhoneMobileFieldCaption();

  /**
   * Get the user phone work field caption
   * 
   * @return the {@link Label}
   */
  Label getUserPhoneWorkFieldCaption();

  /**
   * Get the user company office field caption
   * 
   * @return the {@link Label}
   */
  Label getUserCompanyOfficeFieldCaption();

  /**
   * Get the user company address field caption
   * 
   * @return the {@link Label}
   */
  Label getUserCompanyAddressFieldCaption();

  /**
   * Get the user company name field caption
   * 
   * @return the {@link Label}
   */
  Label getUserCompanyNameFieldCaption();

  /**
   * Get the confirm delete user window
   * 
   * @return {@link DeleteConfirmWindow} the window
   */
  DeleteConfirmWindow getConfirmDeleteUserWindow();

}