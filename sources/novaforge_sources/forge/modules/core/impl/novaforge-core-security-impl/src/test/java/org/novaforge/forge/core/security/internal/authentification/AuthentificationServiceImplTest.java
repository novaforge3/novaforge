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
package org.novaforge.forge.core.security.internal.authentification;

import org.junit.Test;
import org.novaforge.forge.core.security.authentification.AuthentificationService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * This class will test public method of {@link AuthentificationServiceImpl}
 * 
 * @author Guillaume Lamirand
 */
public class AuthentificationServiceImplTest
{
  /**
   * Clear string
   */
  private final static String CLEAR_STRING = "sympa";
  /**
   * Hash result
   */
  private final static String HASH_STRING  = "35bc724730476a47cf18f92f486cd9e7745d4f15";
  /**
   * Validation reg ex for generated pwd
   */
  private final static String REGEX        = "[\\w]*[\\d\\W_]+[\\w]*";

  /**
   * Check the method {@link AuthentificationServiceImpl#hashPassword(String)}
   */
  @Test
  public void encodeSHA1()
  {
    final AuthentificationService authentificationService = new AuthentificationServiceImpl();
    assertThat(authentificationService.hashPassword(CLEAR_STRING), is(HASH_STRING));
  }

  /**
   * Check the method {@link AuthentificationServiceImpl#generateRandomPassword()}
   */
  @Test
  public void generatePwd()
  {
    final AuthentificationService authentificationService = new AuthentificationServiceImpl();

    final Pattern pattern = Pattern.compile(REGEX);
    final String generateRandomPassword = authentificationService.generateRandomPassword();
    System.out.println(generateRandomPassword);
    final Matcher matcher = pattern.matcher(generateRandomPassword);
    assertThat(matcher.matches(), is(true));
  }
}
