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
package org.novaforge.forge.it.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.model.ProjectInfo;
import org.novaforge.forge.core.organization.model.User;

import java.util.StringTokenizer;

public class HelperTest extends ToolsPropagationItBaseTest
{
  private static final Log LOG = LogFactory.getLog(HelperTest.class);

  public static String getSpecificNovaHome(String pTmpDir)
  {
    StringTokenizer stk = new StringTokenizer(pTmpDir, "/");
    String          found = "";
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

  public void test01Helper() throws Exception
  {
    System.out.println("***************************************************************************");
    System.out.println("******************           test helper       *******************");
    System.out.println("***************************************************************************");

    try
    {
      String file = null;
      assertEquals(true, true);
      System.out.println(file.toLowerCase());
    }
    finally
    {
      System.out.println("***** finally");
      assertEquals(false, true);
      System.out.println("***** end finally");
    }

  }

  /*
   * utility method in order to delete a project
   */
  public void cleanProject(final String pProjectId) throws Exception
  {
    try
    {
      login();

      final ProjectInfo project = projectPresenter.getProjectInfo(pProjectId);
      if (project != null)
      {
        projectPresenter.deleteProject(pProjectId);
        LOG.info("********************================> projetId = " + pProjectId
            + " has been deleted !!!!!!!!!!!!!!!!!!!!!!!!!!");
      }

    }
    catch (final Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      securityManager.logout();
    }

  }

  public void login() throws Exception
  {
    final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
    final User user = userPresenter.getUser(superAdministratorLogin);
    securityManager.login(superAdministratorLogin, user.getPassword());
  }
}
