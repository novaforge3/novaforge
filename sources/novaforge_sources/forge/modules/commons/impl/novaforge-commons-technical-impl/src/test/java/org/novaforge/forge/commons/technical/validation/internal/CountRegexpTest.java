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

import junit.framework.TestCase;
import org.novaforge.forge.commons.technical.validation.constraints.Condition;

/**
 * @author sbenoist
 */
public class CountRegexpTest extends TestCase
{
  public void testCountRegexp()
  {
    final String chain = "&pass1&word2oo3";

    final int nbNum = UserPasswordValidator.countPatternOccurs(Condition.Type.NUMBER.getPattern(), chain);
    assertEquals(3, nbNum);

    final int nbSpecial = UserPasswordValidator.countPatternOccurs(Condition.Type.NON_ALPHANUM.getPattern(),
        chain);
    assertEquals(2, nbSpecial);
  }
}
