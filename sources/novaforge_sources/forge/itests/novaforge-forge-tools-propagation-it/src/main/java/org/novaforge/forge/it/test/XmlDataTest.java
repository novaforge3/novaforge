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

import org.novaforge.forge.it.test.datas.TestConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XmlDataTest extends ToolsPropagationItBaseTest
{
  // Assuming there's ONE project! (id,name description can be changed)
  // but other elements of the xml can't be changed ! (because of specifics values tested)

  @Override
  public void setUp() throws Exception
  {
    super.setUp();
  }

  public void test01XMLParsingGetUsers() throws Exception
  {
    final Map<String, String> users = xmlData.getUsers();
    assertNotNull(users);
    // Check the XML contains usertest-12 and usertest-13 declared into TestConstants (for specific group
    // tests)
    assertTrue("XML should containsuser: " + TestConstants.USERTEST12LOGIN, users.keySet().contains(TestConstants.USERTEST12LOGIN));
    assertTrue("XML should containsuser: " + TestConstants.USERTEST13LOGIN, users.keySet().contains(TestConstants.USERTEST13LOGIN));
  }

  public void test02XMLParsingGetProjects() throws Exception
  {
    final Map<String, String> projets = xmlData.getProjects();
    assertNotNull(projets);
  }

  public void test03XMLParsinggetUsersMembership() throws Exception
  {
    final Map<String, String> userMemberships = xmlData.getUsersMembership(xmlData.getProjects().keySet()
        .iterator().next());
    assertNotNull(userMemberships);
    assertEquals(10, userMemberships.size());
  }

  public void test04XMLParsinggetGroupsMembership() throws Exception
  {
    final Map<String, String> groupMemberships = xmlData.getGroupsMembership(xmlData.getProjects().keySet()
        .iterator().next());
    assertNotNull(groupMemberships);
    assertEquals(1, groupMemberships.size());
    assertEquals("developpeur", groupMemberships.get("grouptest1"));
  }

  public void test05XMLParsinggetAppName() throws Exception
  {
    final String appName = xmlData.getApplicationName(xmlData.getProjects().keySet().iterator().next(),
        "Mantis");
    assertNotNull(appName);
    assertEquals("mantis appli", appName);
  }

  public void test06XMLParsinggetGroupsUsersMembership() throws Exception
  {
    final Map<String, String> usersFromGroupeRoles = xmlData.getGroupsUsersMembership(xmlData.getProjects()
        .keySet().iterator().next());
    assertNotNull(usersFromGroupeRoles);
    assertEquals(1, usersFromGroupeRoles.size());
    Set<String> users = usersFromGroupeRoles.keySet();
    String userId = null;
    String role = null;
    for (Iterator<String> userIter = users.iterator(); userIter.hasNext();)
    {
      userId = (String) userIter.next();
      role = (String) usersFromGroupeRoles.get(userId);
    }
    assertEquals("usertest11-u", userId);
    assertEquals("developpeur", role);
  }

  public void test07XMLParsinglistRoleChangeCondition() throws Exception
  {
    xmlData.listRoleChangeCondition();
    assertEquals("usertest2-u", xmlData.getRoleChangeCondition().getUserId());
  }

  public void test08XMLParsinggetGroupRoleChangingCondition() throws Exception
  {

    Map<String, List<String>> groupsChangingRoles = xmlData.getGroupRoleChangingCondition(xmlData
        .getProjects().keySet().iterator().next());
    assertEquals(1, groupsChangingRoles.size());
    String groupId = groupsChangingRoles.keySet().iterator().next();
    String roleToChange = groupsChangingRoles.get(groupId).get(0);
    String roleTarget = groupsChangingRoles.get(groupId).get(1);
    assertEquals("developpeur", roleToChange);
    assertEquals("testeur", roleTarget);
  }

  public void test09XMLParsinggetAppMappingInitial() throws Exception
  {
    Map<String, String> map = xmlData.getAppMapping(xmlData.getProjects().keySet().iterator().next(),
        "Mantis");
    assertNotNull("Role mapping is null", map);
    HashMap<String, String> expectedMap = new HashMap<String, String>();
    expectedMap.put("Administrator", "Manager");
    expectedMap.put("developpeur", "Developer");
    expectedMap.put("testeur", "Tester");
    assertEquals("role mapping is KO", expectedMap, map);
  }

  public void test10XMLParsinggetAppMappingChanged() throws Exception
  {
    Map<String, String> map = xmlData.getAppMapping(xmlData.getProjects().keySet().iterator().next(),
        "Mantis2");
    assertNotNull("Role mapping is null", map);
    HashMap<String, String> expectedMap = new HashMap<String, String>();
    expectedMap.put("Administrator", "Manager");
    expectedMap.put("testeur", "Developer");
    expectedMap.put("developpeur", "Tester");
    assertEquals("role mapping is KO", expectedMap, map);
  }

  public void test1XMLParsinggetForgeGroups() throws Exception
  {
    // check there's at least 1 forge group with name declared into the TestConstants.
    // check also its name is: forgegroup1
    final List<String> usersFromGroupeRoles = xmlData.getForgeGroups();
    assertTrue("Expected 1 forge group declared into the XML", usersFromGroupeRoles.size() == 1);
    String forgeGoupId = usersFromGroupeRoles.get(0);
    assertTrue("Expected forge group name = forgegroup1", "forgegroup1".equals(forgeGoupId));
  }
}
