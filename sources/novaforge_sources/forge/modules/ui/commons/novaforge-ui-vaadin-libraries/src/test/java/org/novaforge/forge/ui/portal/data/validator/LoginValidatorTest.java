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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test class for {@link LoginValidator}
 * 
 * @author Guillaume Lamirand
 */
public class LoginValidatorTest
{

  /**
   * Default error message used for test
   */
  private static final String MY_ERROR_MESSAGE = "my error message";

  /**
   * Test method for {@link LoginValidator#LoginValidator(String)}.
   */
  @Test
  public void loginValidator()
  {
    final LoginValidator login = new LoginValidator(MY_ERROR_MESSAGE);
    assertThat(login.getErrorMessage(), is(MY_ERROR_MESSAGE));
    assertThat(login.getMaxLength(), is(LoginValidator.MAX_LENGTH));
    assertThat(login.getMinLength(), is(LoginValidator.MIN_LENGTH));
    assertThat(login.isNullAllowed(), is(false));
  }

  /**
   * Test method for {@link LoginValidator#LoginValidator(String)}.
   */
  @Test
  public void loginValidatorWithLenght()
  {
    final LoginValidator login2 = new LoginValidator(MY_ERROR_MESSAGE, 0, 10);
    assertThat(login2.getErrorMessage(), is(MY_ERROR_MESSAGE));
    assertThat(login2.getMaxLength(), is(10));
    assertThat(login2.getMinLength(), is(0));
    assertThat(login2.isNullAllowed(), is(false));
  }

  /**
   * Test method for {@link LoginValidator#isValid(Object)}.
   */
  @Test
  public void isValid()
  {
    final LoginValidator login = new LoginValidator(MY_ERROR_MESSAGE);
    // all valid
    assertThat(login.isValid("login"), is(true));
    assertThat(login.isValid("login1"), is(true));
    assertThat(login.isValid("login12"), is(true));
    assertThat(login.isValid("login__"), is(true));
    assertThat(login.isValid("login_1"), is(true));
    assertThat(login.isValid("login_12"), is(true));
    assertThat(login.isValid("login--"), is(true));
    // all invalid
    assertThat(login.isValid("login*"), is(false));
    assertThat(login.isValid("login="), is(false));
    assertThat(login.isValid("login+"), is(false));
    assertThat(login.isValid("login}"), is(false));
    assertThat(login.isValid("login`"), is(false));
    assertThat(login.isValid("login^"), is(false));

  }
}
