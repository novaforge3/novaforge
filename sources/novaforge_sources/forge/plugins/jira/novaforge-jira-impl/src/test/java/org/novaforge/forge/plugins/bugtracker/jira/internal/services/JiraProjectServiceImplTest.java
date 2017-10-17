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
package org.novaforge.forge.plugins.bugtracker.jira.internal.services;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapException;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertThat;

public class JiraProjectServiceImplTest
{
  private static final Integer PROJECT_INDICE_1          = 5;
  private static final Integer CONFIGURATION_ID_INDICE_1 = 5;
  private static final String  PROJECT_ID_1              = "TOTOO";
  private static final String  CONFIGURATION_ID_1        = "POUET";
  private static final String  EXPECTED_RESULT_1         = "TOTOOPOUET";
  private static final Integer PROJECT_INDICE_2          = 5;
  private static final Integer CONFIGURATION_ID_INDICE_2 = 5;
  private static final String  PROJECT_ID_2              = "TOTO--O";
  private static final String  CONFIGURATION_ID_2        = "PO-_UET";
  private static final String  EXPECTED_RESULT_2         = "TOTOOPOUET";
  private static final Integer PROJECT_INDICE_3          = 3;
  private static final Integer CONFIGURATION_ID_INDICE_3 = 4;
  private static final String  PROJECT_ID_3              = "T_!TO--O";
  private static final String  CONFIGURATION_ID_3        = "P?01_UET";
  private static final String  EXPECTED_RESULT_3         = "TTOPUET";
  private static final Integer PROJECT_INDICE_4          = 5;
  private static final Integer CONFIGURATION_ID_INDICE_4 = 5;
  private static final String  PROJECT_ID_4              = "sdd_istp";
  private static final String  CONFIGURATION_ID_4        = "sdd_istptasks";
  private static final String  EXPECTED_RESULT_4         = "SDDISSDDIS";
  private static final Integer PROJECT_INDICE_5          = 4;
  private static final Integer CONFIGURATION_ID_INDICE_5 = 5;
  private static final String  PROJECT_ID_5              = "TO TO (! 4";
  private static final String  CONFIGURATION_ID_5        = "P1 O! U$ET";
  private static final String  EXPECTED_RESULT_5         = "TOTOPOUET";
  private boolean jiraProfileActivated = true;

  public JiraProjectServiceImplTest()
  {
    final String property = System.getProperty("jira.profile");
    if ("true".equals(property))
    {
      jiraProfileActivated = true;
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.service.JiraProjectServiceImpl#generateKeyString(java.lang.String, java.lang.String, Integer, Integer)}
   * .
   * 
   * @throws JiraSoapException
   * @throws SecurityException
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  @Test
  public void test00GenerateKeyString() throws JiraSoapException, NoSuchMethodException, SecurityException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException
  {
    if (jiraProfileActivated)
    {
      JiraProjectServiceImpl jiraProjectServiceImpl = new JiraProjectServiceImpl();

      String key1 = jiraProjectServiceImpl.generateKeyString(PROJECT_ID_1, CONFIGURATION_ID_1, PROJECT_INDICE_1,
          CONFIGURATION_ID_INDICE_1);

      assertThat(key1, CoreMatchers.is(EXPECTED_RESULT_1));

      String key2 = jiraProjectServiceImpl.generateKeyString(PROJECT_ID_2, CONFIGURATION_ID_2,
          PROJECT_INDICE_2, CONFIGURATION_ID_INDICE_2);

      assertThat(key2, CoreMatchers.is(EXPECTED_RESULT_2));

      String key3 = jiraProjectServiceImpl.generateKeyString(PROJECT_ID_3, CONFIGURATION_ID_3,
          PROJECT_INDICE_3, CONFIGURATION_ID_INDICE_3);

      assertThat(key3, CoreMatchers.is(EXPECTED_RESULT_3));
      
      String key4 = jiraProjectServiceImpl.generateKeyString(PROJECT_ID_4, CONFIGURATION_ID_4,
          PROJECT_INDICE_4, CONFIGURATION_ID_INDICE_4);

      assertThat(key4, CoreMatchers.is(EXPECTED_RESULT_4));

      String key5 = jiraProjectServiceImpl.generateKeyString(PROJECT_ID_5, CONFIGURATION_ID_5,
          PROJECT_INDICE_5, CONFIGURATION_ID_INDICE_5);
      
      assertThat(key5, CoreMatchers.is(EXPECTED_RESULT_5));
      
    }
  }

}
