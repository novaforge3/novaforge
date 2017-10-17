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

import javax.naming.NamingException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test class for {@link PasswordValidator}
 * 
 * @author Guillaume Lamirand
 */
public class PasswordValidatorTest
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
   * Default error message used for test
   */
  private static final String MY_ERROR_MESSAGE = "my error message";

  /**
   * Test method for {@link PasswordValidator#PasswordValidator(String)}.
   */
  @Test
  public void passwordValidator()
  {
    final PasswordValidator pwd = new PasswordValidator(MY_ERROR_MESSAGE, null);
    assertThat(pwd.getErrorMessage(), is(MY_ERROR_MESSAGE));
  }

  /**
   * Test method for {@link PasswordValidator#isValid(Object)}.
   * 
   * @throws NamingException
   */
  @Test
  public void isValid() throws NamingException
  {
    // Mock Services
    final String forgePasswordRegex =
        "(" + "(?=.*\\d)" + "(?=.*[a-zA-Z])" + "(?=.*[" + SPECIAL_CHAR + "])" + "(?=\\S+$)" + ".{" + MIN_LENGTH + ","
            + MAX_LENGTH + "})";

    final PasswordValidator pwd = new PasswordValidator(MY_ERROR_MESSAGE, forgePasswordRegex);
    // all valid
    assertThat(pwd.isValid("monpwd_1"), is(true));
    assertThat(pwd.isValid("monPWD_1"), is(true));
    assertThat(pwd.isValid("_monPWD_1"), is(true));
    assertThat(pwd.isValid("monpwd*1"), is(true));
    assertThat(pwd.isValid("monpwd=1"), is(true));
    assertThat(pwd.isValid("monpwd+1"), is(true));
    assertThat(pwd.isValid("monpwd-1"), is(true));
    assertThat(pwd.isValid("monpwd`1"), is(true));
    assertThat(pwd.isValid("monpwd^1"), is(true));
    assertThat(pwd.isValid("monpwd@1"), is(true));
    assertThat(pwd.isValid("monpwd#1"), is(true));
    assertThat(pwd.isValid("monpwd$1"), is(true));
    assertThat(pwd.isValid("monpwd&1"), is(true));
    assertThat(pwd.isValid("monpwd%1"), is(true));
    assertThat(pwd.isValid("monpwd/1"), is(true));
    assertThat(pwd.isValid("monpwd(1"), is(true));
    assertThat(pwd.isValid("monpwd)1"), is(true));
    assertThat(pwd.isValid("monpwd{1"), is(true));
    assertThat(pwd.isValid("monpwd}1"), is(true));
    assertThat(pwd.isValid("monpwd[1"), is(true));
    assertThat(pwd.isValid("monpwd]1"), is(true));
    assertThat(pwd.isValid("monpwd.1"), is(true));
    assertThat(pwd.isValid("1_monpwd"), is(true));
    // all invalid
    assertThat(pwd.isValid("monpasswd"), is(false));
    assertThat(pwd.isValid("monpawd1"), is(false));
    assertThat(pwd.isValid("monpwd__"), is(false));
    assertThat(pwd.isValid("monpwd**"), is(false));
    assertThat(pwd.isValid("monpwd=="), is(false));
    assertThat(pwd.isValid("monpwd++"), is(false));
    assertThat(pwd.isValid("monpwd--"), is(false));
    assertThat(pwd.isValid("monpwd{}"), is(false));
    assertThat(pwd.isValid("monpwd(`"), is(false));
    assertThat(pwd.isValid("monpwd]^"), is(false));
    assertThat(pwd.isValid("monpwd"), is(false));
    assertThat(pwd.isValid("mon pawd_1"), is(false));
    assertThat(pwd.isValid("mon   pawd_1"), is(false));

  }
}
