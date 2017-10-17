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
 * Test class for {@link NameValidator}
 * 
 * @author Guillaume Lamirand
 */
public class NameValidatorTest
{

  /**
   * Default error message used for test
   */
  private static final String MY_ERROR_MESSAGE = "my error message";

  /**
   * Test method for {@link NameValidator#NameValidator(String)}.
   */
  @Test
  public void nameValidator()
  {
    final NameValidator name = new NameValidator(MY_ERROR_MESSAGE);
    assertThat(name.getErrorMessage(), is(MY_ERROR_MESSAGE));
    assertThat(name.getMaxLength(), is(NameValidator.MAX_LENGTH));
    assertThat(name.getMinLength(), is(NameValidator.MIN_LENGTH));
    assertThat(name.isNullAllowed(), is(false));
  }

  /**
   * Test method for {@link NameValidator#NameValidator(String)}.
   */
  @Test
  public void loginValidatorWithLenght()
  {
    final NameValidator name2 = new NameValidator(MY_ERROR_MESSAGE, 0, 10);
    assertThat(name2.getErrorMessage(), is(MY_ERROR_MESSAGE));
    assertThat(name2.getMaxLength(), is(10));
    assertThat(name2.getMinLength(), is(0));
    assertThat(name2.isNullAllowed(), is(false));
  }

  /**
   * Test method for {@link NameValidator#isValid(Object)}.
   */
  @Test
  public void isValid()
  {
    final NameValidator name = new NameValidator(MY_ERROR_MESSAGE);
    // all valid
    assertThat(name.isValid("My Name"), is(true));
    assertThat(name.isValid("Mé name"), is(true));
    assertThat(name.isValid("Maçon"), is(true));
    assertThat(name.isValid("My-name"), is(true));
    assertThat(name.isValid("My_name"), is(true));
    assertThat(name.isValid("My 'name"), is(true));
    assertThat(name.isValid("Myña'me--"), is(true));
    assertThat(name.isValid("my ønme"), is(true));
    // all invalid
    assertThat(name.isValid("my >name"), is(false));
    assertThat(name.isValid("my )name"), is(false));
    assertThat(name.isValid("my (name"), is(false));
    assertThat(name.isValid("my }name"), is(false));
    assertThat(name.isValid("my {name"), is(false));
    assertThat(name.isValid("my `name"), is(false));
    assertThat(name.isValid("my |name"), is(false));
    assertThat(name.isValid("my |n@me"), is(false));
    assertThat(name.isValid("my $nme"), is(false));
    assertThat(name.isValid("my £nme"), is(false));

  }
}
