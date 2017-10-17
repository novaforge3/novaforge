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

import com.vaadin.data.validator.AbstractValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Guillaume Lamirand
 */
public class PasswordValidator extends AbstractValidator<String>
{

  /**
   * Default min length for password
   */
  protected static final int    MIN_LENGTH       = 8;
  /**
   * Default max length for password
   */
  protected static final int    MAX_LENGTH       = 20;
  /**
   * Default Special char allowed
   */
  protected static final String SPECIAL_CHAR     = "!\"#$%&'<>\\()*=^+\\]\\[,\\./:;?@_`|{}~-";
  /**
   * Serial version id for serialization
   */
  private static final long serialVersionUID = 1999993607111891092L;
  /**
   * Contains regex used to validate password
   */
  private String                validationRegex;

  /**
   * Creates a new PasswordValidator without regex and error message
   */
  public PasswordValidator()
  {
    super(null);
    validationRegex = null;
  }

  /**
   * Creates a new PasswordValidator with a given error message.
   * 
   * @param pErrorMessage
   *          the message to display in case the value does not validate.
   * @param pValidationRegex
   *          the regex used to validate the value
   */
  public PasswordValidator(final String pErrorMessage, final String pValidationRegex)
  {
    super(pErrorMessage);
    validationRegex = pValidationRegex;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isValidValue(final String pValue)
  {
    final String stringValue = pValue;
    return hasDigitAndSpecial(stringValue);
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
   * This method check if the pwd in parameter contains at least one digit and one special character
   * 
   * @param pPwd
   *          the source pwd
   * @return a true if parameter contains at least one digit and one special character, otherwise false
   */
  private boolean hasDigitAndSpecial(final String pPwd)
  {
    boolean result = false;
    String forgePasswordRegex = validationRegex;
    if ((forgePasswordRegex == null) || ("".equals(forgePasswordRegex)))
    {
      // If no forgePasswordRegex is defined, there is any constrain on the format of the password 
      forgePasswordRegex = ".*";
    }
    final Pattern pattern = Pattern.compile(forgePasswordRegex);
    final Matcher matcher = pattern.matcher(pPwd);
    if (matcher.matches())
    {
      result = true;
    }

    return result;
  }

  /**
   * @param pValidationRegex
   *          the validationRegex to set
   */
  public void setValidationRegex(final String pValidationRegex)
  {
    validationRegex = pValidationRegex;
  }

}
