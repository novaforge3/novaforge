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
package org.novaforge.forge.ui.user.management.internal.client.userprofile;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Image;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;
import org.novaforge.forge.ui.portal.client.component.UploadFieldCustom;
import org.novaforge.forge.ui.portal.data.validator.PasswordValidator;

import java.util.Locale;

/**
 * The interface of UserProfileView
 * 
 * @author Jeremy Casery
 */
public interface UserProfileView extends ComponentContainer
{

  /**
   * Set if section infos is editing or not
   * 
   * @param pIsEditing
   *          true if editing, false otherwise
   */
  void setSectionInfosEditing(boolean pIsEditing);

  /**
   * Get the section infos edit button
   * 
   * @return {@link Button} the edit button
   */
  Button getEditSectionInfosButton();

  /**
   * Get the section infos save button
   * 
   * @return {@link Button} the save button
   */
  Button getSaveSectionInfosButton();

  /**
   * Get the section infos cancel button
   * 
   * @return {@link Button} the cancel button
   */
  Button getCancelSectionInfosButton();

  /**
   * Get the user firstname field
   * 
   * @return {@link TextField} the field
   */
  TextField getUserFirstnameField();

  /**
   * Get the user lastname field
   * 
   * @return {@link TextField} the field
   */
  TextField getUserLastnameField();

  /**
   * Get the user language combobox
   * 
   * @return {@link ComboBox} the field
   */
  ComboBox getUserLanguage();

  /**
   * Get the user picture
   * 
   * @return {@link Image} the picture
   */
  Image getUserPicture();

  /**
   * Set section contact editing or not
   * 
   * @param pIsEditing
   *          true if editable, false otherwise
   */
  void setSectionContactEditing(boolean pIsEditing);

  /**
   * Get the section projects cancel button
   * 
   * @return {@link Button} the cancel button
   */
  Button getCancelSectionProjectsButton();

  /**
   * Get the section projects save button
   * 
   * @return {@link Button} the save button
   */
  Button getSaveSectionProjectsButton();

  /**
   * Get the section projects edit button
   * 
   * @return {@link Button} the edit button
   */
  Button getEditSectionProjectsButton();

  /**
   * Get the section work cancel button
   * 
   * @return {@link Button} the cancel button
   */
  Button getCancelSectionWorkButton();

  /**
   * Get the section work save button
   * 
   * @return {@link Button} the save button
   */
  Button getSaveSectionWorkButton();

  /**
   * Get the section work edit button
   * 
   * @return {@link Button} the edit button
   */
  Button getEditSectionWorkButton();

  /**
   * Get the section contact cancel button
   * 
   * @return {@link Button} the cancel button
   */
  Button getCancelSectionContactButton();

  /**
   * Get the section contact save button
   * 
   * @return {@link Button} the save button
   */
  Button getSaveSectionContactButton();

  /**
   * Get the section contact edit button
   * 
   * @return {@link Button} the edit button
   */
  Button getEditSectionContactButton();

  /**
   * Get the user email {@link TextField}
   * 
   * @return the email {@link TextField}
   */
  TextField getUserEmailField();

  /**
   * Get the user phone work {@link TextField}
   * 
   * @return the phone work {@link TextField}
   */
  TextField getUserPhoneWorkField();

  /**
   * Get the user phone mobile {@link TextField}
   * 
   * @return the phone mobile {@link TextField}
   */
  TextField getUserPhoneMobileField();

  /**
   * Get the User phone work public checkbox
   * 
   * @return the {@link CheckBox} field
   */
  CheckBox getUserPhoneWorkFieldPublic();

  /**
   * Get the User phone mobile public checkbox
   * 
   * @return the {@link CheckBox} field
   */
  CheckBox getUserPhoneMobileFieldPublic();

  /**
   * Initialize labels with given locale
   * 
   * @param pLocale
   *          the {@link Locale} to use for label
   */
  void refreshLocale(Locale pLocale);

  /**
   * Set if section work is in editing mode
   * 
   * @param pIsEditing
   *          true if editing, false otherwise
   */
  void setSectionWorkEditing(boolean pIsEditing);

  /**
   * Set if section project is in editing mode
   * 
   * @param pIsEditing
   *          true if editing, false otherwise
   */
  void setSectionProjectsEditing(boolean pIsEditing);

  /**
   * Get the user projects field checkbox
   * 
   * @return the {@link CheckBox} field
   */
  CheckBox getUserProjectsFieldPublic();

  /**
   * Get the user company office field checkbox
   * 
   * @return the {@link CheckBox} field
   */
  CheckBox getUserCompanyOfficeFieldPublic();

  /**
   * Get the user company office field
   * 
   * @return the {@link TextField} field
   */
  TextField getUserCompanyOfficeField();

  /**
   * Get the use company address field public
   * 
   * @return the {@link CheckBox} field
   */
  CheckBox getUserCompanyAddressFieldPublic();

  /**
   * Get the user company address field
   * 
   * @return the {@link TextArea} field
   */
  TextArea getUserCompanyAddressField();

  /**
   * Get the user company name field public
   * 
   * @return the {@link CheckBox} field
   */
  CheckBox getUserCompanyNameFieldPublic();

  /**
   * Get the user company name field
   * 
   * @return the {@link TextArea} field
   */
  TextField getUserCompanyNameField();

  /**
   * Get the update picture window
   * 
   * @return the {@link Window}
   */
  Window getUpdatePictureWindow();

  /**
   * Get the new password again field
   * 
   * @return the {@link PasswordField} field
   */
  PasswordField getNewAgainPasswd();

  /**
   * Get the new password field
   * 
   * @return the {@link PasswordField} field
   */
  PasswordField getNewPasswd();

  /**
   * Get the current password field
   * 
   * @return the {@link PasswordField} field
   */
  PasswordField getCurrentPasswd();

  /**
   * Get the update password window
   * 
   * @return the {@link Window}
   */
  Window getUpdatePasswdWindow();

  /**
   * Get the update password button
   * 
   * @return the {@link Button}
   */
  Button getUpdatePasswdButton();

  /**
   * Get the update picture button
   * 
   * @return {@link Button}
   */
  Button getUpdatePictureButton();

  /**
   * Define if new password and confirmation are matching or not
   * 
   * @param pOneEmpty
   *          true if one of password field is empty or null, false otehrwise
   * @param pIsMatching
   *          true if matching, false otehrwise
   */
  void setNewPasswdMatching(boolean pOneEmpty, boolean pIsMatching);

  /**
   * Set if the new password is valid
   * 
   * @param pIsEmpty
   *          true if empty, false otherwise
   * @param pIsValid
   *          true if valid, false otherwise
   */
  void setNewPasswdValid(boolean pIsEmpty, boolean pIsValid);

  /**
   * Get the confirm update password button
   * 
   * @return the {@link Button}
   */
  Button getConfirmUpdatePasswdButton();

  /**
   * Get the back to users admin button
   * 
   * @return the {@link Button}
   */
  Button getBackToUsersAdminButton();

  /**
   * Set if admin actions are enabled
   * 
   * @param pIsAdmin
   *          true if user is admin, false otherwise
   * @param pIsFromAdminView
   *          true if come from admin view, false otherwise
   */
  void setAdminActionsEnabled(boolean pIsAdmin, boolean pIsFromAdminView);

  /**
   * Get the add section button
   * 
   * @return the {@link Button}
   */
  Button getAddSectionButton();

  /**
   * Get the remove section projects button
   * 
   * @return the {@link Button}
   */
  Button getRemoveSectionProjectsButton();

  /**
   * Get the remove section work button
   * 
   * @return the {@link Button}
   */
  Button getRemoveSectionWorkButton();

  /**
   * Get the remove section contact button
   * 
   * @return the {@link Button}
   */
  Button getRemoveSectionContactButton();

  /**
   * Show or not section contact
   * 
   * @param pIsShow
   *          true to show, false otherwise
   */
  void showSectionContact(boolean pIsShow);

  /**
   * Show or not the section work
   * 
   * @param pIsShow
   *          true to show, false otherwise
   */
  void showSectionWork(boolean pIsShow);

  /**
   * Show or not the section projects
   * 
   * @param pIsShow
   *          true to show, false otherwise
   */
  void showSectionProjects(boolean pIsShow);

  /**
   * Get the user login field
   * 
   * @return the {@link TextField}
   */
  TextField getUserLoginField();

  /**
   * Get the add section window
   * 
   * @return the {@link Window}
   */
  Window getAddSectionWindow();

  /**
   * Get the add section combobox
   * 
   * @return the {@link ComboBox}
   */
  ComboBox getAddSectionWindowCombobox();

  /**
   * Get the add section window button
   * 
   * @return the {@link Button}
   */
  Button getAddSectionWindowButton();

  /**
   * Get the delete user button
   * 
   * @return the {@link Button}
   */
  Button getDeleteUserButton();

  /**
   * Get the delete user picture button
   * 
   * @return the {@link Button}
   */
  Button getDeletePictureButton();

  /**
   * Get the confirm update picture button
   * 
   * @return the {@link Button}
   */
  Button getConfirmUpdatePictureButton();

  /**
   * Get the upload picture field
   * 
   * @return the {@linkplain UploadFieldCustom} field
   */
  UploadFieldCustom getUploadField();

  /**
   * Get the confirm delete user window
   * 
   * @return the {@link DeleteConfirmWindow}
   */
  DeleteConfirmWindow getConfirmDeleteUserWindow();

  // /**
  // * Get the confirm delete user button
  // *
  // * @return the {@link Button}
  // */
  // Button getConfirmDeleteUserButton();
  //
  // /**
  // * Get the confirm delete user label
  // *
  // * @return {@link Label} the label
  // */
  // Label getConfirmDeleteUserLabel();
  //
  // /**
  // * Get the cancel delete user button
  // *
  // * @return {@link Button} the button
  // */
  // Button getCancelDeleteUserButton();

  /**
   * @return
   */
  Button getCancelUpdatePasswdButton();

  /**
   * @return
   */
  PasswordValidator getPasswordRegexValidator();

}
