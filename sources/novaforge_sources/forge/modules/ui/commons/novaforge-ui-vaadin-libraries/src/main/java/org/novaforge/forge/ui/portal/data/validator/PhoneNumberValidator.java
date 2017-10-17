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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Guillaume Lamirand
 */
public class PhoneNumberValidator extends AbstractValidator<String>
{

  /**
   * Serial version id for serialization
   */
  private static final long serialVersionUID = 1999993607111891092L;
  private boolean           allowNull;

  /**
   * Creates a new PhoneNumberValidator with a given error message and null allowed value
   *
   * @param pErrorMessage
   *          the message to display in case the value does not validate.
   * @param pAllowNull
   *          Are null strings permissible? This can be handled better by
   *          setting a field as required or not.
   */
  public PhoneNumberValidator(final String pErrorMessage, final boolean pAllowNull)
  {
    this(pErrorMessage);
    allowNull = pAllowNull;
  }

  /**
   * Creates a new PhoneNumberValidator with a given error message.
   *
   * @param pErrorMessage
   *     the message to display in case the value does not validate.
   */
  public PhoneNumberValidator(final String pErrorMessage)
  {
    super(pErrorMessage);
    allowNull = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValidValue(final String pValue)
  {
    boolean isValid = true;
    if (Strings.isNullOrEmpty(pValue))
    {
      isValid = allowNull;
    }
    else
    {
      isValid = isPhoneNumber(pValue);
    }
    return isValid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<String> getType()
  {
    return String.class;
  }

  /**
   * This method check if the value in parameter valid phone number regex
   * 
   * @param pPwd
   *          the source pwd
   * @return a true if parameter contains digits or + character
   */
  private boolean isPhoneNumber(final String pPwd)
  {

    boolean hasNoSpecial = false;
    final Pattern pattern = Pattern.compile("^[0-9\\+]{1,}[0-9\\-\\. ]{3,15}$"); // Can start with + or digit
                                                                                 // follow by 3 to 15 digit
                                                                                 // with separator (.,- or
                                                                                 // space)
    final Matcher matcher = pattern.matcher(pPwd);
    if (matcher.matches())
    {
      hasNoSpecial = true;
    }

    return hasNoSpecial;
  }

}
