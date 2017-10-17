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
 * @author Guillaume Lamirand
 */
public class PhoneNumberValidatorTest
{

  /**
   * Default error message used for test
   */
  private static final String MY_ERROR_MESSAGE = "my error message";

  /**
   * Test method for {@link PhoneNumberValidator#PhoneNumberValidator(String)}.
   */
  @Test
  public void phoneNumberValidator()
  {
    final PhoneNumberValidator number = new PhoneNumberValidator(MY_ERROR_MESSAGE);
    assertThat(number.getErrorMessage(), is(MY_ERROR_MESSAGE));
  }

  /**
   * Test method for {@link PhoneNumberValidator#isValid(Object)}.
   */
  @Test
  public void isValid()
  {
    final PhoneNumberValidator number = new PhoneNumberValidator(MY_ERROR_MESSAGE);
    // all valid
    assertThat(number.isValid("06 12 23 45 56"), is(true));
    assertThat(number.isValid("06.12.23.45.56"), is(true));
    assertThat(number.isValid("06-12-23-45-56"), is(true));
    assertThat(number.isValid("+33 6 12 23 45 56"), is(true));
    assertThat(number.isValid("+33-6-12-23-45-56"), is(true));
    assertThat(number.isValid("+33.6.12.23.45.56"), is(true));
    assertThat(number.isValid("0033 6 12 23 45 56"), is(true));
    assertThat(number.isValid("0033-6-12-23-45-56"), is(true));
    assertThat(number.isValid("0033.6.12.23.45.56"), is(true));
    // all invalid
    assertThat(number.isValid("z"), is(false));
    assertThat(number.isValid("-33 6 12 23 45 56"), is(false));
    assertThat(number.isValid("a06 12 23 45 56"), is(false));

  }
}
