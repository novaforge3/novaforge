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
 * This validator allows to check if a confirm password field is valid according to the source one.
 * 
 * @author Guillaume Lamirand
 */
public class DualPasswordValidator extends AbstractValidator<String>
{

  /**
   * Serial version id for serialization
   */
  private static final long   serialVersionUID = -5532194317389168083L;
  /**
   * Contains the original password field
   */
  private final Field<String> passwordField;

  /**
   * Creates a new PasswordValidator with a given error message.
   * 
   * @param pErrorMessage
   *          the message to display in case the value does not validate.
   * @param pFirstPwd
   *          the first field used.
   */
  public DualPasswordValidator(final String pErrorMessage, final Field<String> pFirstPwd)
  {
    super(pErrorMessage);
    passwordField = pFirstPwd;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isValidValue(final String pValue)
  {
    final String stringValue    = pValue;
    final String originalString = passwordField.getValue();

    return ((!Strings.isNullOrEmpty(originalString)) && (originalString.equals(stringValue)));
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
