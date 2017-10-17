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
package org.novaforge.forge.ui.projects.internal.client.manage.presenter.components;

import org.novaforge.forge.core.organization.model.Organization;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Guillaume Lamirand
 */
public class OrganizationValidator extends OrganizationLengthValidator
{

  /**
   * Default min length for password
   */
  protected static final int MIN_LENGTH       = 3;
  /**
   * Default max length for password
   */
  protected static final int MAX_LENGTH       = 26;
  /**
   * Serial version id for serialization
   */
  private static final long serialVersionUID = -490835051915296696L;

  /**
   * Creates a new OrganizationValidator with a given error message.
   * 
   * @param pErrorMessage
   *          the message to display in case the value does not validate.
   */
  public OrganizationValidator(final String pErrorMessage)
  {
    super(pErrorMessage, MIN_LENGTH, MAX_LENGTH, true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid(final Object pValue)
  {
    boolean isValid = false;
    if (pValue instanceof Organization)
    {
      final Organization value = (Organization) pValue;

      isValid = isValidValue(value);
      if (isValid)
      {
        final String stringValue = value.getName();
        isValid = hasNoSpace(stringValue);
      }
    }
    return isValid;
  }

  /**
   * This method check if the organizatio name in parameter doesn't contain any space or blank caracters
   * 
   * @param pName
   *          the source name
   * @return a true if parameter doesn't contain any space or blank caracters, otherwise false
   */
  private boolean hasNoSpace(final String pName)
  {

    boolean hasNoSpecial = false;
    final Pattern pattern = Pattern.compile("[A-Z0-9_\\S]*");
    final Matcher matcher = pattern.matcher(pName);
    if (matcher.matches())
    {
      hasNoSpecial = true;
    }

    return hasNoSpecial;
  }

}
