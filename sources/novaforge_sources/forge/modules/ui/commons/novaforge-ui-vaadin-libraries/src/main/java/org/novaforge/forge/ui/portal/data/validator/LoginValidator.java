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

import com.vaadin.data.validator.StringLengthValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Guillaume Lamirand
 */
public class LoginValidator extends StringLengthValidator
{

  /** Default min length for login */
  protected static final int MIN_LENGTH       = 3;
  /** Default max length for login */
  protected static final int MAX_LENGTH       = 30;
  /**
   * Serial version id for serialization
   */
  private static final long serialVersionUID = -1274696505073401927L;

  /**
   * Creates a new LoginValidator with a given error message.
   *
   * @param pErrorMessage
   *          the message to display in case the value does not validate.
   */
  public LoginValidator(final String pErrorMessage)
  {
    this(pErrorMessage, MIN_LENGTH, MAX_LENGTH);
  }

  /**
   * Creates a new LoginValidator with a given error message,
   * permissable lengths.
   *
   * @param pErrorMessage
   *          the message to display in case the value does not validate.
   * @param pMinLength
   *          the minimum permissible length of the string.
   * @param pMaxLength
   *          the maximum permissible length of the string.
   */
  public LoginValidator(final String pErrorMessage, final int pMinLength, final int pMaxLength)
  {
    super(pErrorMessage, pMinLength, pMaxLength, false);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isValidValue(final String pValue)
  {
    boolean      isValid     = super.isValidValue(pValue);
    final String stringValue = pValue;
    if (isValid)
    {
      isValid = stringValue.toLowerCase().equals(stringValue) && hasNoSpecial(stringValue);
    }
    return isValid;
  }

  /**
   * This method check if the pwd in parameter contains at least one digit and one special character
   *
   * @param pPwd
   *          the source pwd
   * @return a true if parameter contains at least one digit and one special character, otherwise false
   */
  private boolean hasNoSpecial(final String pPwd)
  {
    boolean       hasNoSpecial = false;
    final Pattern pattern      = Pattern.compile("[\\w-.]*");
    final Matcher matcher      = pattern.matcher(pPwd);
    if (matcher.matches())
    {
      hasNoSpecial = true;
    }

    return hasNoSpecial;
  }

}
