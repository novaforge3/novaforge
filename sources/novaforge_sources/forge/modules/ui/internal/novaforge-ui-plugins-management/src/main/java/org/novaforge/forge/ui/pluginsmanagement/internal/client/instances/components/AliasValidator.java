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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components;

import com.vaadin.data.validator.StringLengthValidator;

/**
 * @author Jeremy Casery
 */
public class AliasValidator extends StringLengthValidator
{

  /**
   * Default min length for password
   */
  protected static final int  MIN_LENGTH       = 1;
  /**
   * Default max length for password
   */
  protected static final int  MAX_LENGTH       = 255;
  /**
   *
   */
  private static final long serialVersionUID = 6341406475090922463L;
  /**
   * Default first car of alias
   */
  private static final String ALIAS_STARTWITH  = "/";

  /**
   * @param errorMessage
   */
  public AliasValidator(final String errorMessage)
  {
    super(errorMessage, MIN_LENGTH, MAX_LENGTH, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid(final Object pValue)
  {
    boolean isValid = super.isValid(pValue);
    final String stringValue = pValue.toString();
    if (isValid)
    {
      isValid = validateAlias(stringValue);
    }
    return isValid;
  }

  /**
   * Validate the alias text format
   * 
   * @param pAlias
   *          the alias to validate
   * @return true if pAlias is valid
   */
  private boolean validateAlias(final String pAlias)
  {
    return pAlias.startsWith(ALIAS_STARTWITH);
  }
}
