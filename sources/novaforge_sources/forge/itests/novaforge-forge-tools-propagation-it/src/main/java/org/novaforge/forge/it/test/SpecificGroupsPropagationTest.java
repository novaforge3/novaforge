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
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.it.test.datas.ManageACL;
import org.novaforge.forge.it.test.datas.ReportTest;
import org.novaforge.forge.it.test.datas.TestConstants;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author blachon-m
 */
public class SpecificGroupsPropagationTest extends ToolsPropagationItBaseTest
{

  private static final Log log = LogFactory.getLog(SpecificGroupsPropagationTest.class);
  public Properties        rolesMantis;

  /**
   * Constants declaration
   */
  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    rolesMantis = new Properties();
    final InputStream fis = this.getClass().getResourceAsStream("/rolesMantis.properties");
    rolesMantis.load(fis);
  }

  public void test01AddingUserToAddedGroupProject() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), getName(), false);

    final String usertest12Login = TestConstants.USERTEST12LOGIN;
    final String roleForgeGroup = TestConstants.ROLE_ADDED_PROJECT_GROUP;

    final String projectGroupId = TestConstants.ADDED_PROJECT_GROUP;

    final String projectId = xmlData.getProjects().keySet().iterator().next();
    final String projectName = xmlData.getProjects().get(projectId);
    assertNotNull("le project pas trouve dans le XML", projectName);

    try
    {
      securityManager.login(TestConstants.login, TestConstants.pwd);

      final String mantisApplicationName = xmlData.getApplicationName(projectId,
          TestConstants.BUGTRACKER_TYPE);
      assertNotNull("Le nom d'appli non renseigne", mantisApplicationName);

      // create a new group for the project (not created within xml import because of limitation to 1 project
      // group at xml parsing)
      final Group group = groupPresenter.newGroup();
      group.setDescription("desc");
      group.setName(projectGroupId);
      // Warning if boolean set to true => an exception is raised (not possible to create a forge group if a
      // projectId is priaided)
      group.setVisible(false);
      groupPresenter.createGroup(group, projectId);
      assertNotNull("added project group does not exist", groupPresenter.getGroup(projectId, projectGroupId));

      // affiliate role: "Member" from forge project to this new forge group
      // no necessary

      // affiliate this new forge group to the project with role: developpeur
      final HashSet<String> rolesname = new HashSet<String>();
      rolesname.add(roleForgeGroup);
      final UUID groupUUID = groupPresenter.getGroup(projectId, projectGroupId).getUuid();
      membershipPresenter.addGroupMembership(projectId, groupUUID, rolesname, "");

      // add a user to this new group (forge group)
      final Group groupFound = groupPresenter.getGroup(projectId, projectGroupId);
      final User userToAdd = userPresenter.getUser(usertest12Login);
      groupFound.addUser(userToAdd);
      groupPresenter.updateGroup(projectId, projectGroupId, groupFound);

      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_CHANGE_ROLEMAPPING_PROPAGATION);

      // ------- check propagation into mantis ---------------------------
      checkPropagationIntoMantis(mantisApplicationName, projectId, projectName, usertest12Login,
          roleForgeGroup);

    }
    finally
    {
      // test cleaning:
      final HashSet<String> rolesname = new HashSet<String>();
      Group projectGroupExpected = null;
      try
      {
        projectGroupExpected = groupPresenter.getGroup(projectId, projectGroupId);
      }
      catch (Exception e)
      {
        if (securityManager.checkLogin())
        {
          securityManager.logout();
        }
      }
      if (projectGroupExpected != null)
      {
        // 1) remove groupe membership: (developpeur) on forge group used into the project
        final UUID groupUUID = groupPresenter.getGroup(projectId, projectGroupId).getUuid();
        membershipPresenter.updateGroupMembership(projectId, groupUUID, rolesname, "", false);

        // 2) remove user fom forge group
        final Group group = groupPresenter.getGroup(projectId, projectGroupId);
        group.clearUsers();
        groupPresenter.updateGroup(projectId, projectGroupId, group);
        final Group groupCheck = groupPresenter.getGroup(projectId, projectGroupId);
        assertTrue("The added group forge should no have user after cleaning",
            groupCheck.getUsers().size() == 0);

        // 3) remove added project group
        groupPresenter.deleteGroup(projectId, groupUUID);
      }
      // logout
      if (securityManager.checkLogin())
      {
        securityManager.logout();
      }
    }
  }

  private void checkPropagationIntoMantis(String pMantisApplicationName, String pProjectId, String pProjectName,
                                          String pUserLogin, String pRoleGroup) throws Exception
  {
    // ------- check propagation into mantis ---------------------------
    assertNotNull("Le nom d'appli non renseigne", pMantisApplicationName);

    final ManageACL manageACLGroup = new ManageACL();
    // check roles propagation into tool for users declared into group members

    final Map<String, String> rolesDatabaseMantis = getRoleDatabaseMantis(pProjectName);
    assertNotNull("Aucune role trouve dans la DB Mantis", rolesDatabaseMantis);

    final Map<String, String> userFromGroupsAndRoles = new HashMap<String, String>();
    userFromGroupsAndRoles.put(pUserLogin, pRoleGroup);

    final Map<String, String> roleCvs = xmlData.getAppMapping(pProjectId, TestConstants.BUGTRACKER_TYPE);
    assertNotNull("Aucune role trouve dans le fichier XML", roleCvs);

    final List<ProjectApplication> applications = applicationPresenter.getAllProjectApplications(pProjectId);
    assertNotNull("Aucune application trouve dans le projet", applications);

    String uri = null;
    for (final ProjectApplication application : applications)
    {
      if (application.getName().equals(pMantisApplicationName))
      {
        uri = application.getUri();
      }
    }

    final Map<String, String> roles = applicationPresenter.getRoleMapping(pProjectId, uri);
    assertEquals(roleCvs.size(), roles.size());
    for (final String roleCvsForge : roleCvs.keySet())
    {
      final String roleCvsMantis = roleCvs.get(roleCvsForge);
      assertEquals("Le role n'est pas equivalent d'apres le XML / Plugin de Forge", roleCvsMantis,
                   roles.get(roleCvsForge));
    }

    final String roleDatabase = rolesDatabaseMantis.get(pUserLogin);
    assertNotNull("The tool role does not exist for the user " + pUserLogin, roleDatabase);
    final String roleForge       = userFromGroupsAndRoles.get(pUserLogin);
    final String roleMantisForge = roleCvs.get(roleForge);
    manageACLGroup.setIfACLError(pUserLogin, roleDatabase, roleMantisForge);
    manageACLGroup.printACLError();
    assertEquals(0, manageACLGroup.getListErrors().size());
  }

  // --------------------- private methods to be shared with mantis itests ... -------------------
  private Map<String, String> getRoleDatabaseMantis(final String projetName) throws Exception
  {
    final Map<String, String> returned = new HashMap<String, String>();

    String jdbcUrl = getMySqlJdbcUrl(TestConstants.BUGTRACKER_TYPE);
    final Connection con = DriverManager.getConnection(jdbcUrl, TestConstants.USER,
                                                       TestConstants.PASSWORD);
    final String query = "SELECT  p.name, u.username, pu.access_level    "
                             + "FROM  `mantis_user_table` AS u,  `mantis_project_user_list_table` AS pu,  `mantis_project_table` AS p "
                             + "WHERE p.id = pu.project_id AND u.id = pu.user_id AND p.name LIKE  '" + projetName
                             + "%'";
    final Statement stmt      = con.createStatement();
    final ResultSet resultSet = stmt.executeQuery(query);
    while (resultSet.next())
    {
      // String projetname = resultSet.getString(1);
      final String username = resultSet.getString(2);
      final String access_level = resultSet.getString(3);
      final String access_name = (String) rolesMantis.get(access_level);
      returned.put(username, access_name);
    }
    con.close();
    return returned;
  }

  public void test02AddingUserToForgePublicGroup() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), getName(), false);

    final String forgeId = TestConstants.FORGE_PROJECTID;
    final String usertest13Login = TestConstants.USERTEST13LOGIN;
    final String roleForgeGroup = TestConstants.ROLE_FORGE_GROUP;

    final String forgeGroupId = xmlData.getForgeGroups().get(0);

    final String projectId = xmlData.getProjects().keySet().iterator().next();
    final String projectName = xmlData.getProjects().get(projectId);
    assertNotNull("Project not found into XML file", projectName);

    try
    {
      // Needed because of authorization checking
      securityManager.login(TestConstants.login, TestConstants.pwd);

      final String mantisApplicationName = xmlData.getApplicationName(projectId,
          TestConstants.BUGTRACKER_TYPE);
      assertNotNull("Le nom d'appli non renseigne", mantisApplicationName);
      assertNotNull("forge group does not exist", groupPresenter.getGroup(forgeId, forgeGroupId));

      // affiliate this new forge group to the project with role: developpeur
      final HashSet<String> rolesname = new HashSet<String>();
      rolesname.add(roleForgeGroup);
      final UUID groupUUID = groupPresenter.getGroup(forgeId, forgeGroupId).getUuid();
      membershipPresenter.addGroupMembership(projectId, groupUUID, rolesname, "");

      // add a user to this new group (forge group)
      final Group groupFound = groupPresenter.getGroup(forgeId, forgeGroupId);
      final User userToAdd = userPresenter.getUser(usertest13Login);
      groupFound.addUser(userToAdd);
      groupPresenter.updateGroup(forgeId, forgeGroupId, groupFound);

      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_CHANGE_ROLEMAPPING_PROPAGATION);

      // ------- check propagation into mantis ---------------------------
      checkPropagationIntoMantis(mantisApplicationName, projectId, projectName, usertest13Login,
          roleForgeGroup);
    }
    finally
    {
      // test cleaning:
      // 1) remove groupe membership: (developpeur) on forge group used into the project
      final HashSet<String> rolesname = new HashSet<String>();
      final UUID groupUUID = groupPresenter.getGroup(forgeId, forgeGroupId).getUuid();
      membershipPresenter.updateGroupMembership(projectId, groupUUID, rolesname, "", false);

      // 2) remove user fom forge group
      final Group group = groupPresenter.getGroup(forgeId, forgeGroupId);
      group.clearUsers();
      groupPresenter.updateGroup(forgeId, forgeGroupId, group);
      final Group groupCheck = groupPresenter.getGroup(forgeId, forgeGroupId);
      assertTrue("The added group forge should no have user after cleaning",
          groupCheck.getUsers().size() == 0);

      // logout
      if (securityManager.checkLogin())
      {
        securityManager.logout();
      }
    }

  }
}