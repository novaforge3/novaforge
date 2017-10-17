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

import org.junit.Test;
import org.novaforge.forge.core.organization.model.Organization;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test class for {@link OrganizationValidator}
 * 
 * @author Guillaume Lamirand
 */
public class OrganizationValidatorTest
{

  /**
   * Default error message used for test
   */
  private static final String MY_ERROR_MESSAGE = "my error message";

  /**
   * Test method for {@link OrganizationValidator#OrganizationValidator(String)}.
   */
  @Test
  public void organizationValidator()
  {
    final OrganizationValidator login = new OrganizationValidator(MY_ERROR_MESSAGE);
    assertThat(login.getErrorMessage(), is(MY_ERROR_MESSAGE));
    assertThat(login.getMaxLength(), is(26));
    assertThat(login.getMinLength(), is(3));
    assertThat(login.isAllowNull(), is(true));
  }

  /**
   * Test method for {@link OrganizationValidator#isValid(Object)}.
   */
  @Test
  public void isValid()
  {
    final OrganizationValidator orga = new OrganizationValidator(MY_ERROR_MESSAGE);
    // all valid
    assertThat(orga.isValid(new OrganizationTest("ORGA")), is(true));
    // all invalid
    assertThat(orga.isValid(new OrganizationTest(" ORGA")), is(false));
    assertThat(orga.isValid(new OrganizationTest("ORGA ")), is(false));
    assertThat(orga.isValid(new OrganizationTest("OR GA")), is(false));

    assertThat(orga.isValid(new OrganizationTest("ORGA")), is(true));

  }

  private class OrganizationTest implements Organization
  {

    /**
     * 
     */
    private static final long serialVersionUID = -7940183063213631447L;
    private String            name;

    public OrganizationTest(final String pName)
    {
      name = pName;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
      return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(final String pArg0)
    {
      name = pArg0;

    }

  }
}
