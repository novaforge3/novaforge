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
 * @date 11 août 2011
 */
public class ProjectHelper
{
   public static final String ADMIN_LOGIN = "admin";
   public static final String ADMIN_PSWD  = "admin";

   /**
    * @return
    */
   public static Map<String, Object> getProject1Data()
   {
      final HashMap<String, Object> result = new HashMap<String, Object>();
      result.put(TestLinkParameter.TEST_CASE_PREFIX.toString(), "projectpref1");
      result.put(TestLinkParameter.PROJECT_NAME.toString(), "project1");
      result.put(TestLinkParameter.NOTES.toString(), "project 1 description");
      result.put(TestLinkParameter.ACTIVE.toString(), TestLinkParameter.TRUE.toString());
      result.put(TestLinkParameter.PUBLIC.toString(), TestLinkParameter.TRUE.toString());
      return result;
   }

   /**
    * @return
    */
   public static Map<String, Object> getProject1DataUpdated()
   {
      final HashMap<String, Object> result = new HashMap<String, Object>();
      result.put(TestLinkParameter.TEST_CASE_PREFIX.toString(), "projectpref1");
      result.put(TestLinkParameter.PROJECT_NAME.toString(), "project1");
      result.put(TestLinkParameter.NOTES.toString(), "project 1 description updated");
      return result;
   }

}
