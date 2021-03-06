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
package org.novaforge.forge.ui.applications.internal.client.applications;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public interface CreateApplicationsView extends ComponentContainer
{

  /**
   * Get the Form used to display space
   * 
   * @return the space form
   */
  Form getApplicationForm();

  /**
   * Get the field containing application name
   * 
   * @return the {@link TextField}
   */
  TextField getNameField();

  /**
   * Get the field containing application description
   * 
   * @return the {@link TextArea}
   */
  TextArea getDescriptionField();

  /**
   * Get the {@link Field} containing application category
   * 
   * @return the {@link ComboBox}
   */
  ComboBox getCategoriesBox();

  /**
   * Get the {@link Field} containing application type
   * 
   * @return the {@link ComboBox}
   */
  ComboBox getTypesBox();

  /**
   * Get the {@link Button} used cancel form
   * 
   * @return the button
   */
  Button getCancelButton();

  /**
   * Get the {@link Button} used apply form
   * 
   * @return the button
   */
  Button getApplyButton();

  /**
   * Get the {@link Form} containing roles
   * 
   * @return the {@link Form}
   */
  Form getRolesForm();

  /**
   * Should be call to refresh view when locale has changed
   * 
   * @param pLocale
   *          the new locale
   */
  void refreshLocale(Locale pLocale);

  /**
   * @return the footer layout
   */
  HorizontalLayout getFooter();

}
