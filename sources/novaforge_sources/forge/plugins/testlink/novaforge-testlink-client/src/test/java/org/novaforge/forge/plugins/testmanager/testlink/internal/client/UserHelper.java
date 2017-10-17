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

package org.novaforge.forge.plugins.testmanager.testlink.internal.client;

import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestLinkParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 10 août 2011
 */
public class UserHelper
{

   public static final String DEV_KEY = "7aa8be0c8f16703fb8db6abb301e0df6";

   public static Map<String, Object> getUser1AccountData()
   {
      final HashMap<String, Object> result = new HashMap<String, Object>();
      result.put(TestLinkParameter.USER_NAME.toString(), "login1");
      result.put(TestLinkParameter.PASSWORD.toString(), "password1");
      result.put(TestLinkParameter.FIRST_NAME.toString(), "firstName1");
      result.put(TestLinkParameter.LAST_NAME.toString(), "lastName1");
      result.put(TestLinkParameter.EMAIL.toString(), "login1@bull.net");
      result.put(TestLinkParameter.LANGUAGE.toString(), "FR");

      return result;
   }

   public static Map<String, Object> getUser2AccountData()
   {
      final HashMap<String, Object> result = new HashMap<String, Object>();
      result.put(TestLinkParameter.USER_NAME.toString(), "login2");
      result.put(TestLinkParameter.PASSWORD.toString(), "password2");
      result.put(TestLinkParameter.FIRST_NAME.toString(), "firstName2");
      result.put(TestLinkParameter.LAST_NAME.toString(), "lastName2");
      result.put(TestLinkParameter.EMAIL.toString(), "login2@bull.net");
      result.put(TestLinkParameter.LANGUAGE.toString(), "FR");

      return result;
   }

   /**
    * @return
    */
   public static Map<String, Object> getUser1UpdateData()
   {
      final HashMap<String, Object> result = new HashMap<String, Object>();
      result.put(TestLinkParameter.USER_NAME.toString(), "login1");
      result.put(TestLinkParameter.PASSWORD.toString(), "password1");
      result.put(TestLinkParameter.FIRST_NAME.toString(), "firstName1Update");
      result.put(TestLinkParameter.LAST_NAME.toString(), "lastName1Update");
      result.put(TestLinkParameter.EMAIL.toString(), "login1");
      result.put(TestLinkParameter.LANGUAGE.toString(), "en");

      return result;
   }

}
