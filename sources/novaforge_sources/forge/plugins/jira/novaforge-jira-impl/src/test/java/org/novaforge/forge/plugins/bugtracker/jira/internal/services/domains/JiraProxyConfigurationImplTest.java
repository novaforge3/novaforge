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

package org.novaforge.forge.plugins.bugtracker.jira.internal.services.domains;

import org.junit.Test;
import org.novaforge.forge.plugins.bugtracker.jira.internal.servlets.JiraBrowse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JiraProxyConfigurationImplTest
{

  private static final String NOVAFORGE_URL    = "http://public";
  private static final String SERVLET_URL      = "http://host:8181";
  private static final String URL              = "http://host:8083/test";
  private static final String NAME             = "name";
  private static final String TEST             = "/test";
  private static final String JIRA             = "/jira";
  private static final String MY_PROXY         = "My proxy";
  private static final int    PUBLIC_PORT      = 8084;
  private static final String PUBLIC_URL       = "http://host:8084/test";
  private static final String WITHOUT_PORT_URL = "http://host/test";
  private static final  String RETURN_LINE = System.getProperty("line.separator");

  @Test
  public void testName() throws Exception
  {
    final JiraProxyConfigurationImpl conf = new JiraProxyConfigurationImpl(PUBLIC_URL, NAME, MY_PROXY, TEST,
        URL, PUBLIC_PORT, SERVLET_URL);
    assertNotNull(conf);
    assertEquals(NAME, conf.getName());
  }

  @Test
  public void testToken() throws Exception
  {
    final JiraProxyConfigurationImpl conf = new JiraProxyConfigurationImpl(PUBLIC_URL, NAME, MY_PROXY, TEST,
        URL, PUBLIC_PORT, SERVLET_URL);
    assertNotNull(conf);
    assertTrue(conf.getStartToken().contains(MY_PROXY));
    assertTrue(conf.getEndToken().contains(MY_PROXY));
  }

  @Test
  public void testGetProxySettings() throws Exception
  {
    final JiraProxyConfigurationImpl conf = new JiraProxyConfigurationImpl(PUBLIC_URL, NAME, MY_PROXY, TEST,
        URL, PUBLIC_PORT, SERVLET_URL);
    final String proxySettings = conf.getProxySettings();
    System.out.println(proxySettings);
    assertEquals("## Start My proxy" + RETURN_LINE + "<Location " + TEST + JiraBrowse.JIRA_ALIAS_SERVLET + ">" + RETURN_LINE + ""
        + "\tProxyPass " + SERVLET_URL + JiraBrowse.JIRA_ALIAS_SERVLET + "" + RETURN_LINE + "\tProxyPassReverse "
        + SERVLET_URL + JiraBrowse.JIRA_ALIAS_SERVLET + "" + RETURN_LINE + "</Location>" + RETURN_LINE + "<Location " + TEST + JIRA + ">" + RETURN_LINE + ""
        + "\tProxyPass " + PUBLIC_URL + JIRA + "" + RETURN_LINE + "\tProxyPassReverse " + PUBLIC_URL + JIRA
        + "" + RETURN_LINE + "\tProxyPassReverse " + WITHOUT_PORT_URL + JIRA + "" + RETURN_LINE + "</Location>" + RETURN_LINE + "## End My proxy" + RETURN_LINE + "",
        proxySettings);
  }
}
