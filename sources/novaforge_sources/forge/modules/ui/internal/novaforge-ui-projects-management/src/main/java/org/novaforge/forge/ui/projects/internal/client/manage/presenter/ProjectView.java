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
package org.novaforge.forge.ui.projects.internal.client.manage.presenter;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Form;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.novaforge.forge.ui.portal.client.component.UploadFieldCustom;

import java.util.Locale;

/**
 * The interface of {@link ProjectView}
 * 
 * @author Guillaume Lamirand
 */
public interface ProjectView extends ComponentContainer
{

  /**
   * Initialize labels with given locale
   * 
   * @param pLocale
   *          the {@link Locale} to use for label
   */
  void refreshLocale(Locale pLocale);

  /**
   * Get the main {@link Form}
   * 
   * @return the main {@link Form}
   */
  Form getProjectForm();

  /**
   * Get the additional {@link Form}
   * 
   * @return the additional {@link Form}
   */
  Form getAdditionalForm();

  /**
   * Get the user picture
   * 
   * @return {@link Image} the picture
   */
  Image getProjectIcon();

  /**
   * Get the update picture window
   * 
   * @return the {@link Window}
   */
  Window getUpdatePictureWindow();

  /**
   * Get the delete user picture button
   * 
   * @return the {@link Button}
   */
  Button getIconDeleteButton();

  /**
   * Get the confirm update picture button
   * 
   * @return the {@link Button}
   */
  Button getIconConfirmButton();

  /**
   * Get the upload picture field
   * 
   * @return the {@linkplain UploadFieldCustom} field
   */
  UploadFieldCustom getImageUpload();

  /**
   * Return the {@link TextField} associated to the project's id
   * 
   * @return the text field for project's id
   */
  TextField getProjectId();

  /**
   * Return the {@link TextField} associated to the project's name
   * 
   * @return the text field for project's name
   */
  TextField getProjectName();

  /**
   * Get the save button
   * 
   * @return {@link Button} the save button
   */
  Button getApplyButton();

  /**
   * Return the {@link ComboBox} associated to the project's license
   * 
   * @return the ComboBox for project's license
   */
  ComboBox getLicenses();

  /**
   * Return the {@link ComboBox} associated to the project's organism
   * 
   * @return the ComboBox for project's organism
   */
  ComboBox getOrganism();

  /**
   * Return the {@link ComboBox} associated to the project templates
   * 
   * @return the ComboBox for templates
   */
  ComboBox getTemplates();

  /**
   * Return boolean used to defined if ui is used to create a project
   * 
   * @return true if the view is used to create a project
   */
  boolean isCreateMode();

}
