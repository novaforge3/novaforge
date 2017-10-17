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
package org.novaforge.forge.ui.user.management.internal.client.securityrules;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.novaforge.forge.ui.user.management.internal.client.components.PeriodField;

import java.util.Locale;

/**
 * @author caseryj
 */
public interface SecurityRulesView extends ComponentContainer
{

  /**
   * Should be called to refresh view according to the {@link Locale} given
   * 
   * @param pLocale
   *          the new locale
   */
  void refreshLocale(Locale pLocale);

  /**
   * Gets {@link CheckBox} used to configure logins generation
   * 
   * @return {@link CheckBox} used to configure logins generation
   */
  CheckBox getLoginGeneratedCheckbox();

  /**
   * Get the password format description label
   * 
   * @return {@link Label} the label
   */
  Label getRulePwdFormatDescription();

  /**
   * Get the password format custom value field
   * 
   * @return {@link TextField} the field
   */
  TextField getRulePwdFormatCustomValue();

  /**
   * Get the password format test button
   * 
   * @return {@link Button} the button
   */
  Button getRulePwdFormatTestButton();

  /**
   * Get the test password result value label
   * 
   * @return {@link Label} the label
   */
  Label getTestResultValueLabel();

  /**
   * Get the update format and close test password window button
   * 
   * @return {@link Button} the button
   */
  Button getTestPwdUpdateAndCloseWindowButton();

  /**
   * Get the close test password window button
   * 
   * @return {@link Button} the button
   */
  Button getTestPwdCloseWindowButton();

  /**
   * Get the password to test field
   * 
   * @return {@link TextField} the field
   */
  TextField getTestPwdToTestField();

  /**
   * Get the test password button
   * 
   * @return {@link Button} the button
   */
  Button getTestPwdTestButton();

  /**
   * Get the password format to test field
   * 
   * @return {@link Textfield} the field
   */
  TextArea getTestPwdFormatField();

  /**
   * Get the test password window
   * 
   * @return {@link Window} the window
   */
  Window getTestPwdFormatWindow();

  /**
   * Get the password alerttime period field
   * 
   * @return {@link PeriodField} the field
   */
  PeriodField getAlertTimePeriodField();

  /**
   * Get the password lifetime period field
   * 
   * @return {@link PeriodField} the field
   */
  PeriodField getLifeTimePeriodField();

  /**
   * Get the password format combobox
   * 
   * @return {@link ComboBox} the combobox
   */
  ComboBox getRulePwdFormatComboBox();

  /**
   * Get the rule password format checkbox
   * 
   * @return {@link CheckBox} the checkbox
   */
  CheckBox getRulePwdFormatCheckbox();

  /**
   * Get the rule password alerttime checkbox
   * 
   * @return {@link CheckBox} the checkbox
   */
  CheckBox getRulePwdAlertTimeCheckbox();

  /**
   * Get the rule password lifetime checkbox
   * 
   * @return {@link CheckBox} the checkbox
   */
  CheckBox getRulePwdLifeTimeCheckbox();

  /**
   * Get the password format custom layout
   * 
   * @return {@link HorizontalLayout} the layout
   */
  HorizontalLayout getPwdFormatCustomLayout();

  /**
   * Get the password format description layout
   * 
   * @return {@link HorizontalLayout} the layout
   */
  HorizontalLayout getPwdFormatDescriptionLayout();

  /**
   * Get the update security rules button
   * 
   * @return {@link Button} the button
   */
  Button getUpdateSecurityRulesButton();

}
