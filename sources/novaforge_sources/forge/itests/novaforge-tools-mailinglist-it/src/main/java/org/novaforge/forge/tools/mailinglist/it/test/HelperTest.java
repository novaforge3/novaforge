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
package org.novaforge.forge.tools.mailinglist.it.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.StringTokenizer;

/**
 * @author blachonm
 */

public class HelperTest
{
  // directory: /datas/safran/tmp/karaf (with safran or novaforge3)
  public final static String DIRECTORY_XML                = System.getProperty("java.io.tmpdir");
  //specific xml for mailing list tests!!
  public final static String IMPORT_ITESTS_MAILING_LIST_XML = "import_itests_mailinglist.xml";
  private static final Log    LOG                = LogFactory.getLog(HelperTest.class);
  public final         String SPECIFIC_NOVA_HOME = getSpecificNovaHome(DIRECTORY_XML);

  private String getSpecificNovaHome(String pTmpDir)
  {
    StringTokenizer stk = new StringTokenizer(pTmpDir, "/");
    String found = "";
    while (stk.hasMoreTokens())
    {
      String token = stk.nextToken();
      if ("novaforge3".equals(token) || "safran".equals(token))
      {
        found = token;
        break;
      }
    }
    if ("".equals(found))
    {
      return "NotFound";
    }
    return found;

  }
}
