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
package org.novaforge.forge.ui.portal.data.validator;

import com.google.common.base.Strings;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.Field;

/**
 * This validator allows to check if a new password field is different of the old one
 * 
 * @author Guillaume Lamirand
 */
public class NewPasswordValidator extends AbstractValidator<String>
{

  /**
   * Serial version id for serialization
   */
  private static final long   serialVersionUID = 8349941934112783386L;
  /**
   * Contains the current password field
   */
  private final Field<String> passwordField;

  /**
   * Creates a new PasswordValidator with a given error message.
   * 
   * @param pErrorMessage
   *          the message to display in case the value does not validate.
   * @param pCurrentPwd
   *          the field for current password.
   */
  public NewPasswordValidator(final String pErrorMessage, final Field<String> pCurrentPwd)
  {
    super(pErrorMessage);
    passwordField = pCurrentPwd;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isValidValue(final String pValue)
  {
    final String stringValue    = pValue;
    final String originalString = passwordField.getValue();

    //NH-285 the super administrator is not enable to change its own password
    //return ((Strings.isNullOrEmpty(originalString) == false) && (originalString.equals(stringValue) == false));
    return 	(originalString.equals(stringValue) == false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<String> getType()
  {
    return String.class;
  }

}
