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
package org.novaforge.forge.tools.requirements.common.internal.services;

import junit.framework.TestCase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sbenoist
 *
 */
public class RequirementCodeServiceTest extends TestCase
{
   private static final String REQUIREMENT_REGEX = "^(\\s*)@Requirement(\\s*)[(](\\s*)(value(\\s*)[=](\\s*))?[{]?(\\s*)(Requirement_Enum.REQ_(\\w*)(\\s*)[,]?(\\s*))+(\\s*)[}]?(\\s*)[)]";

   private static final String REQUIREMENT_ENUM_REGEX = "^(\\s*)REQ_FCT_PERS_01[(](\\s*)[\"](\\w+)[\"][,](\\s*)(\\w*)";
   public void testRegexp()
   {
      // Compile the regular expression
      Pattern pattern = Pattern.compile(REQUIREMENT_REGEX);

      // Check for matching with @Requirement( Requirement_Enum.REQ_FCT_DEV)
      Matcher matcher1 = pattern.matcher("@Requirement( Requirement_Enum.REQ_FCT_DEV)");
      assertEquals(true, matcher1.matches());
      System.out.println("result1 = " + matcher1.group());

      // Check for matching with @Requirement( value = Requirement_Enum.REQ_FCT_DEV)
      Matcher matcher2 = pattern.matcher("@Requirement(   value =  Requirement_Enum.REQ_FCT_DEV)");
      assertEquals(true, matcher2.matches());
      System.out.println("result2 = " + matcher2.group());

      // Check for matching with @Requirement( value = { Requirement_Enum.REQ_FCT_DEV } )
      Matcher matcher3 = pattern.matcher("@Requirement( value = { Requirement_Enum.REQ_FCT_DEV } )");
      assertEquals(true, matcher3.matches());
      System.out.println("result3 = " + matcher3.group());

      // Check for matching with @Requirement(value = { Requirement_Enum.REQ_MY_REQ_ID1,
      // Requirement_Enum.REQ_MY_REQ_ID2 })
      Matcher matcher4 = pattern
            .matcher("@Requirement(value = { Requirement_Enum.REQ_MY_REQ_ID1, Requirement_Enum.REQ_MY_REQ_ID2 })");
      assertEquals(true, matcher4.matches());
      System.out.println("result4 = " + matcher4.group());
      // Check for matching with @Requirement(value = { Requirement_Enum.REQ_MY_REQ_ID1,
      // Requirement_Enum.REQ_MY_REQ_ID2 })
      Matcher matcher5 = pattern
.matcher("@Requirement" + "\n"
            + "(value = { Requirement_Enum.REQ_MY_REQ_ID1, Requirement_Enum.REQ_MY_REQ_ID2 })");
      assertEquals(true, matcher5.matches());
      System.out.println("result5 = " + matcher5.group());
   }

   /*
    * result1 = @Requirement( Requirement_Enum.REQ_FCT_DEV) result2 = @Requirement( value =
    * Requirement_Enum.REQ_FCT_DEV) result3 = @Requirement( value = { Requirement_Enum.REQ_FCT_DEV } ) result4
    * = @Requirement(value = { Requirement_Enum.REQ_MY_REQ_ID1, Requirement_Enum.REQ_MY_REQ_ID2 })
    */
   public void testFindRequirementId1()
   {
      String result1 = "@Requirement( Requirement_Enum.REQ_MY_REQ_ID)";
      result1 = result1.replaceAll("\\s+", "");
      String[] tab = result1.split("Requirement_Enum.REQ_");
      if (tab.length == 2)
      {
         // only one requirement id to extract
         result1 = tab[1];

         // remove the ) or the }
         int index = result1.indexOf('}');
         if (index < 0)
         {
            index = result1.indexOf(')');
         }

         result1 = result1.substring(0, index);
         assertEquals("MY_REQ_ID", result1);
      }
   }

   public void testFindRequirementId2()
   {
      String result1 = "@Requirement( value = Requirement_Enum.REQ_MY_REQ_ID)";
      result1 = result1.replaceAll("\\s+", "");
      String[] tab = result1.split("Requirement_Enum.REQ_");
      if (tab.length == 2)
      {
         // only one requirement id to extract
         result1 = tab[1];

         // remove the ) or the }
         int index = result1.indexOf('}');
         if (index < 0)
         {
            index = result1.indexOf(')');
         }

         result1 = result1.substring(0, index);
         assertEquals("MY_REQ_ID", result1);
      }
   }

   public void testFindRequirementId3()
   {
      String result1 = "@Requirement( value = { Requirement_Enum.REQ_MY_REQ_ID } )";
      result1 = result1.replaceAll("\\s+", "");
      String[] tab = result1.split("Requirement_Enum.REQ_");
      if (tab.length == 2)
      {
         // only one requirement id to extract
         result1 = tab[1];

         // remove the ) or the }
         int index = result1.indexOf('}');
         if (index < 0)
         {
            index = result1.indexOf(')');
         }

         result1 = result1.substring(0, index);
         assertEquals("MY_REQ_ID", result1);
      }
   }

   public void testFindRequirementId4()
   {
      String result1 = "@Requirement(value = { Requirement_Enum.REQ_MY_REQ_ID1, Requirement_Enum.REQ_MY_REQ_ID2 })";
      result1 = result1.replaceAll("\\s+", "");
      String[] tab = result1.split("Requirement_Enum.REQ_");
      if (tab.length == 2)
      {
         // only one requirement id to extract
         result1 = tab[1];

         // remove the ) or the }
         int index = result1.indexOf('}');
         if (index < 0)
         {
            index = result1.indexOf(')');
         }

         result1 = result1.substring(0, index);
         assertEquals("MY_REQ_ID", result1);
      }
      else if (tab.length == 3)
      {
         // 2 requirements
         String id = null;
         int index = 0;
         for (int i = 1; i < tab.length; i++)
         {
            if (i < tab.length - 1)
            {
               index = tab[i].indexOf(',');
               id = tab[i].substring(0, index);
               assertEquals("MY_REQ_ID1", id);
            }
            else
            {
               index = tab[i].indexOf('}');
               id = tab[i].substring(0, index);
               assertEquals("MY_REQ_ID2", id);
            }
         }

      }
   }

   public void testEnumRegex()
   {
      String test = "REQ_FCT_PERS_01(\"FCT_PERS_01\", ";

      // Compile the regular expression
      Pattern pattern = Pattern.compile(REQUIREMENT_ENUM_REGEX);

      // Check for matching with @Requirement( Requirement_Enum.REQ_FCT_DEV)
      Matcher matcher1 = pattern.matcher(test);

      assertEquals(true, matcher1.matches());

      String result = matcher1.group();
      result = result.replaceAll("\\s+", "");
      System.out.println(result);

      String id = result.split("REQ_FCT_PERS_01")[1];
      System.out.println(id.substring(2, id.length() - 2));
   }
}
