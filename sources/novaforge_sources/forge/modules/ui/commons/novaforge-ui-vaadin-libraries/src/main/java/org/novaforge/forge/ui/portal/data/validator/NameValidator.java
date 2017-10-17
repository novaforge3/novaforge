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
public class NameValidator extends StringLengthValidator
{
  /** Default min length for password */
  public static final int       MIN_LENGTH       = 2;
  /** Default max length for password */
  public static final int       MAX_LENGTH       = 50;
  /** Default Special char rejected */
  protected static final String SPECIAL_CHAR     = "!\"#$%&<>\\()*=^+\\]\\[,\\./:;?@`|{}~£";
  /**
   * Serial version id
   */
  private static final long serialVersionUID = 3778344215664069598L;

  /**
   * Creates a new NameValidator with a given error message.
   * 
   * @param pErrorMessage
   *          the message to display in case the value does not validate.
   */
  public NameValidator(final String pErrorMessage)
  {
    this(pErrorMessage, MIN_LENGTH, MAX_LENGTH);
  }

  /**
   * Creates a new NameValidator with a given error message,
   * permissable lengths.
   * 
   * @param pErrorMessage
   *          the message to display in case the value does not validate.
   * @param pMinLength
   *          the minimum permissible length of the string.
   * @param pMaxLength
   *          the maximum permissible length of the string.
   */
  public NameValidator(final String pErrorMessage, final int pMinLength, final int pMaxLength)
  {
    super(pErrorMessage, pMinLength, pMaxLength, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValidValue(final String pValue)
  {
    boolean isValid = super.isValidValue(pValue);
    final String stringValue = pValue;
    if (isValid)
    {
      isValid = hasNoSpecial(stringValue);
    }
    return isValid;
  }

  /**
   * This method check if the name in parameter contains no special character
   * 
   * @param pName
   *          the source name
   * @return a true if parameter contains at no special character, otherwise false
   */
  private boolean hasNoSpecial(final String pName)
  {
    boolean result = false;
    final String regex = "(" + "(?=[^" + SPECIAL_CHAR + "]+$)" + ".{" + MIN_LENGTH + "," + MAX_LENGTH + "})";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(pName);
    if (matcher.matches())
    {
      result = true;
    }

    return result;
  }
}
