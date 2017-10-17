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

import com.vaadin.data.validator.AbstractValidator;
import org.novaforge.forge.core.organization.model.Organization;

/**
 * @author sbenoist
 */
public class OrganizationLengthValidator extends AbstractValidator<Organization>
{
  /**
   *
   */
  private static final long    serialVersionUID = -1415214576254963105L;
  private              Integer minLength        = null;
  private              Integer maxLength        = null;
  private              boolean allowNull        = true;

  public OrganizationLengthValidator(final String pErrorMessage, final Integer pMinLength, final Integer pMaxLength,
                                     final boolean pAllowNull)
  {
    this(pErrorMessage);
    minLength = pMinLength;
    maxLength = pMaxLength;
    allowNull = pAllowNull;
  }

  /**
   * @param pErrorMessage
   */
  private OrganizationLengthValidator(final String pErrorMessage)
  {
    super(pErrorMessage);
  }

  /**
   * Checks if the given value is valid.
   *
   * @param value
   *          the value to validate.
   * @return <code>true</code> for valid value, otherwise <code>false</code>.
   */
  @Override
  protected boolean isValidValue(final Organization value)
  {
    if (value == null)
    {
      return allowNull;
    }
    final int len = value.getName().length();
    return !((minLength != null && minLength > -1 && len < minLength) || (maxLength != null && maxLength > -1
                                                                              && len > maxLength));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<Organization> getType()
  {
    return Organization.class;
  }

  public Integer getMinLength()
  {
    return minLength;
  }

  public void setMinLength(final Integer pMinLength)
  {
    minLength = pMinLength;
  }

  public Integer getMaxLength()
  {
    return maxLength;
  }

  public void setMaxLength(final Integer pMaxLength)
  {
    maxLength = pMaxLength;
  }

  public boolean isAllowNull()
  {
    return allowNull;
  }

  public void setAllowNull(final boolean pAllowNull)
  {
    allowNull = pAllowNull;
  }

}
