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
package org.novaforge.forge.commons.technical.validation.internal;

import org.novaforge.forge.commons.technical.validation.constraints.Condition;
import org.novaforge.forge.commons.technical.validation.constraints.UserPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sbenoist
 */
public class UserPasswordValidator implements ConstraintValidator<UserPassword, String>
{

  private Condition[] conditions;

  @Override
  public void initialize(final UserPassword userPassword)
  {
    conditions = userPassword.conditions();
  }

  @Override
  public boolean isValid(final String password, final ConstraintValidatorContext constraintContext)
  {
    if (password == null)
    {
      return false;
    }

    for (final Condition condition : conditions)
    {
      if (countPatternOccurs(condition.target().getPattern(), password) < condition.min())
      {
        return false;
      }
    }
    return true;
  }

  public static int countPatternOccurs(final String regexp, final String chain)
  {
    int count = 0;
    final Pattern pattern = Pattern.compile(regexp);
    final Matcher matcher = pattern.matcher(chain);
    while (matcher.find())
    {
      count++;
    }

    return count;
  }
}
