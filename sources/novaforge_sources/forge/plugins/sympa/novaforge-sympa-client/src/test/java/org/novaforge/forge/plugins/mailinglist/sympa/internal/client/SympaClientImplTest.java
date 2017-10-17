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
package org.novaforge.forge.plugins.mailinglist.sympa.internal.client;

import junit.framework.TestCase;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapClient;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapConnector;

/**
 * @author sbenoist
 */
public class SympaClientImplTest extends TestCase
{
  private static final String       WSDL_LOCATION                = "http://127.0.0.1/sympa/wsdl";

  private static final String       ENDPOINT                     = "https://vm-infra-20/sympa-default/sympasoap";

  private static final String       TRUSTED_APPLICATION_NAME     = "novaforge";

  private static final String       TRUSTED_APPLICATION_PASSWORD = "novaforge";

  private static final String       LISTMASTER                   = "novaforge-.net";

  private static final String       ROBOT_NAME                   = "vm-infra-20";

  private static final String       USER_EMAIL                   = "";

  private static final String       USER_GECOS                   = "test user";

  private static final String       USER_PASSWORD                = "testpassword";

  private static final String       USER_LANG                    = "fr";

  private static final String       USER_GECOS_UPD               = "test user updated";

  private static final String       USER_PASSWORD_UPD            = "testpasswordupdated";

  private static final String       USER_LANG_UPD                = "en";

  private static final String       USER_EMAIL_UPD               = "";

  private static final String       TOPIC_NAME                   = "topic1/subtopic";

  private static final String       TOPIC_TITLE                  = "topic1 title";

  private static final String       TOPIC_VISIBILITY             = "unconceal";

  private static final String       TOPIC_TITLE_UPD              = "topics1 title updated";

  private static final String       TOPIC_CATEGORY               = "topic category";
  private static SympaSoapClient    sympaSoapClient;
  private static SympaSoapConnector sympaSoapConnector;
  private boolean sympaProfileActivated = false;

  public SympaClientImplTest(final String name) throws Exception
  {
    super(name);
    final String property = System.getProperty("sympa.profile");
    if ("true".equals(property))
    {
      sympaProfileActivated = true;
      oneTimeSetUp();
    }
  }

  public static void oneTimeSetUp() throws Exception
  {
    sympaSoapClient = new SympaSoapClientImpl();
    sympaSoapConnector = sympaSoapClient.getConnector(ENDPOINT, TRUSTED_APPLICATION_NAME,
        TRUSTED_APPLICATION_PASSWORD, ROBOT_NAME, LISTMASTER);
  }

  /*
   * public void testGetMailingLists() throws Exception
   * {
   * if (sympaProfileActivated)
   * {
   * final List<MailingListBean> lists = sympaSoapClient.getMailingLists(sympaSoapConnector, "test17");
   * for (final MailingListBean mailingListBean : lists)
   * {
   * System.out.println(mailingListBean.toString());
   * }
   * }
   * }
   * public void testIsSubscriber() throws Exception
   * {
   * if (sympaProfileActivated)
   * {
   * boolean ret = sympaSoapClient
   * .isSubscriber(sympaSoapConnector, "test15-team", "");
   * assertEquals(false, ret);
   * ret = sympaSoapClient.isSubscriber(sympaSoapConnector, "test15-team", "");
   * assertEquals(false, ret);
   * }
   * }
   * public void testAddSubscriber() throws Exception
   * {
   * if (sympaProfileActivated)
   * {
   * final boolean ret = sympaSoapClient.addSubscriber(sympaSoapConnector, "test15-team", "",
   * "aaa", false);
   * assertEquals(true, ret);
   * }
   * }
   */

  /*
   * public void testRemoveSubscriber() throws Exception
   * {
   * if (sympaProfileActivated)
   * {
   * final boolean ret = sympaSoapClient.removeSubscriber(sympaSoapConnector, "test15-team",
   * "", false);
   * assertEquals(true, ret);
   * }
   * }
   */

  public void testCloseList() throws Exception
  {
    if (sympaProfileActivated)
    {
      final boolean ret = sympaSoapClient.closeList(sympaSoapConnector, "monprojet5-team");
      assertEquals(true, ret);
    }
  }

}
