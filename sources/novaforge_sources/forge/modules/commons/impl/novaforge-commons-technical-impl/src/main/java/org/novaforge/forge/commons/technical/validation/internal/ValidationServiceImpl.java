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

import org.novaforge.forge.commons.technical.validation.ValidationService;
import org.novaforge.forge.commons.technical.validation.ValidatorResponse;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author benoists
 */
public class ValidationServiceImpl implements ValidationService
{

  private static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> ValidatorResponse validate(final Class<T> clazz, final T bean)
  {
    final ValidatorResponse response = new ValidatorResponse();
    final Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(bean);

    if (constraintViolations.size() > 0)
    {
      response.setValid(false);
      response.setMessage(buildMessage(constraintViolations));
    }

    return response;
  }

  private <T> String buildMessage(final Set<ConstraintViolation<T>> pConstraintViolations)
  {
    final StringBuilder sb = new StringBuilder();
    if (pConstraintViolations.size() == 1)
    {
      sb.append("one violation has been detected : ");
    }
    else
    {
      sb.append("many violations have been detected : ");
    }

    int i = 0;
    for (final ConstraintViolation<T> constraint : pConstraintViolations)
    {
      sb.append(constraint.getRootBeanClass().getSimpleName()).append(".")
          .append(constraint.getPropertyPath());
      sb.append(" ").append(constraint.getMessage());
      if (i < (pConstraintViolations.size() - 1))
      {
        sb.append(" , ");
      }
      else
      {
        sb.append(".");
      }
      i++;
    }

    return sb.toString();
  }

}
