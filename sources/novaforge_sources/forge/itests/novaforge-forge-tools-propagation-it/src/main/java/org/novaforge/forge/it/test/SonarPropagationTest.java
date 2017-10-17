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

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.it.test.datas.ManageACL;
import org.novaforge.forge.it.test.datas.ReportTest;
import org.novaforge.forge.it.test.datas.TestConstants;

public class SonarPropagationTest extends ToolsPropagationItBaseTest
{
  private Properties       rolesSonar;
  private static final Log log = LogFactory.getLog(SonarPropagationTest.class);

  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    rolesSonar = new Properties();
    InputStream fis = this.getClass().getResourceAsStream("/rolesSonar.properties");
    rolesSonar.load(fis);
  }

  public void test01GetRolesMapping() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***************************************************************************");
    log.info("******************           testGetRolesMapping Testlink       *******************");
    log.info("***************************************************************************");

    // Needed because of authorization checking
    this.securityManager.login(TestConstants.login, TestConstants.pwd);

    // Needed because of authorization checking
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String app_name = xmlData.getApplicationName(project, TestConstants.QUALIMETRY_TYPE);
      assertNotNull("Le nom d'appli non renseigne", app_name);

      Map<String, String> roleCvs = xmlData.getAppMapping(project, TestConstants.QUALIMETRY_TYPE);
      assertNotNull("Aucun role trouve dans le fichier xml", roleCvs);

      List<ProjectApplication> applications = this.applicationPresenter.getAllProjectApplications(project);
      assertNotNull("Aucune application trouve dans le projet", applications);

      String uri = null;
      for (ProjectApplication application : applications)
      {
        if (application.getName().equals(app_name))
        {
          uri = application.getUri();
        }
      }
      assertNotNull(uri);
      Map<String, String> roles = this.applicationPresenter.getRoleMapping(project, uri);
      assertEquals(roleCvs.size(), roles.size());
      for (Iterator<String> iterator1 = roleCvs.keySet().iterator(); iterator1.hasNext();)
      {
        String roleCvsForge = iterator1.next();
        String roleCvsTestlink = roleCvs.get(roleCvsForge);
        assertEquals("Le role n'est pas equivalent d'apres le xml / Plugin de Forge", roleCvsTestlink,
            roles.get(roleCvsForge));
      }
    }
    this.securityManager.logout();
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  public void test02LookRoleAtDatabaseSonar() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***************************************************************************");
    log.info("******************           LookRolesAtDatase  Sonar      ********************");
    log.info("***************************************************************************");
    ManageACL manageACL = new ManageACL();
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le xml", projectName);

      Map<String, String> rolesDatabaseSonar = getRoleDatabaseSonar(project);
      assertNotNull("Aucune role trouve dans la db Sonar", rolesDatabaseSonar);

      Map<String, String> userAndRoles = xmlData.getUsersMembership(project);
      assertNotNull("Aucune correspondance dans les xml entre users et roles", userAndRoles);

      Map<String, String> roleCvsSonar = xmlData.getAppMapping(project, TestConstants.QUALIMETRY_TYPE);
      assertNotNull("Aucune correspondance dans les xml entre users et Sonar", roleCvsSonar);

      for (Iterator<String> iterator1 = userAndRoles.keySet().iterator(); iterator1.hasNext();)
      {
        String user = iterator1.next();
        String roleDatabase = rolesDatabaseSonar.get(user);
        assertNotNull("The tool role does not exist for the user " + user, roleDatabase);
        String roleForge = userAndRoles.get(user);
        String roleSonarForge = roleCvsSonar.get(roleForge);
        manageACL.setIfACLError(user, roleDatabase, roleSonarForge);
      }
      manageACL.printACLError();
      assertEquals(0, manageACL.getListErrors().size());
    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  public void test03LookProjectAtDataseSonar() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***************************************************************************");
    log.info("******************           LookProjectAtDatase  Sonar      ********************");
    log.info("***************************************************************************");
    /*
     * projects are coupled with group names (table groups). Ex here after when 3 groups from Sonar have been
     * mapped at application creation.
     * projettest24_admin
     * projettest24_user
     * projettest24_codeviewer
     */
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      List<String> groupsList = getProjetDatabaseSonar(project);

      // At least 1 role have to be mapped to create the apprlcation!
      assertFalse("Project never appears into Sonar roles meaning the project has not been propagated!",
          groupsList.size() == 0);
    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  public void test04LookAtUserSonar() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    List<String> userSonar = getUsersSonar();
    assertNotNull("Aucune ser trouve dans le file de conf Sonar", userSonar);

    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le xml", projectName);

      Map<String, String> userAndRoles = xmlData.getUsersMembership(project);
      assertNotNull("Aucune correspondance dans les xml entre users et roles", userAndRoles);

      for (Iterator<String> iterator1 = userAndRoles.keySet().iterator(); iterator1.hasNext();)
      {
        String user = iterator1.next();
        assertTrue("le user n'existe pas dans Sonar", userSonar.contains(user));
      }
    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  /*
   * testing propagation into Sonar when changing forge role for a user member
   */
  public void test05TestRoleChangingPropagationIntoSonar() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***************************************************************************");
    log.info("******************           Look Role change At Database  Sonar      ********************");
    log.info("***************************************************************************");
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      // changing role for the first user having "developpeur" role (for the the given projectId).
      String projectTestId = xmlData.getRoleChangeCondition().getProjectTestId();
      assertNotNull("le projectId pas trouve dans le xml", projectTestId);
      String roleToChange = xmlData.getRoleChangeCondition().getRoleToChange();
      assertNotNull("le roleToChange pas trouve dans le xml", roleToChange);
      String roleTarget = xmlData.getRoleChangeCondition().getRoleTarget();
      assertNotNull("le roleTarget pas trouve dans le xml", roleTarget);
      String userIdToChange = xmlData.getRoleChangeCondition().getUserId();
      if (project.equals(projectTestId) && userIdToChange != null)
      {
        try
        {
          // changing role on the forge
          changeRoleForUser(projectTestId, userIdToChange, roleToChange, roleTarget);
          assertNotNull("Aucun user trouve avec le role = " + roleToChange, userIdToChange);
          Thread.currentThread().sleep(TestConstants.WAIT_CHANGE_ROLE_PROPAGATION);
          // check propagation into Sonar Db
          checkPropagationIntoSonarDb(project, true, userIdToChange, roleTarget,
              TestConstants.QUALIMETRY_TYPE);
        }
        finally
        {
          // back changing the role
          if (userIdToChange != null)
          {
            changeBackRoleForUser(projectTestId, userIdToChange, roleToChange);
            Thread.currentThread().sleep(TestConstants.WAIT_CHANGE_ROLE_PROPAGATION);
          }
        }
      }
    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  /*
   * testing propagation into Sonar when changing roles mappings of Sonar application.
   */
  public void test06TestChangingRoleMappingPropagationIntoSonar() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***********************************************************************************");
    log.info("********* Test propagation when changing roles mapping for Sonar *******************");
    log.info("***********************************************************************************");

    // Needed because of authorization checking
    this.securityManager.login(TestConstants.login, TestConstants.pwd);

    // Needed because of authorization checking
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String app_name = xmlData.getApplicationName(project, TestConstants.QUALIMETRY_TYPE);
      assertNotNull("Le nom d'appli non renseigne", app_name);

      List<ProjectApplication> applications = this.applicationPresenter.getAllProjectApplications(project);
      assertNotNull("Aucune application trouve dans le projet", applications);

      String applicationUri = null;
      for (ProjectApplication application : applications)
      {
        if (application.getName().equals(app_name))
        {
          applicationUri = application.getUri();
          break;
        }
      }
      assertNotNull(applicationUri);

      try
      {
        // updating the roles mapping
        Map<String, String> newCvsRoleMapping = xmlData
            .getAppMapping(project, TestConstants.QUALIMETRY_TYPE2);
        assertNotEquals("Aucun role trouve dans le fichier xml de changement de mapping", true,
            newCvsRoleMapping.isEmpty());
        updateApplication(project, applicationUri, newCvsRoleMapping, TestConstants.QUALIMETRY_TYPE, app_name);

        // wait until jms message has been executed by the plugin (mapping) and the tool (update of the
        // tool
        // roles)
        // may be could loop onto targeted tool roles to be more sure than an arbitrary time (?).
        Thread.currentThread().sleep(TestConstants.WAIT_CHANGE_ROLEMAPPING_PROPAGATION);

        // check propagation according the new roles mapping
        checkPropagationIntoSonarDb(project, false, null, null, TestConstants.QUALIMETRY_TYPE2);
      }
      // always done eveen getting failure
      finally
      {
        // setting back the roles mapping
        Map<String, String> initialCvsRoleMapping = xmlData.getAppMapping(project,
            TestConstants.QUALIMETRY_TYPE);
        assertNotEquals("Aucun role trouve dans le fichier xml initial de mapping", true,
            initialCvsRoleMapping.isEmpty());
        updateApplication(project, applicationUri, initialCvsRoleMapping, TestConstants.QUALIMETRY_TYPE,
            app_name);
        Thread.currentThread().sleep(TestConstants.WAIT_CHANGE_ROLEMAPPING_PROPAGATION);
      }
    }
    if (this.securityManager.checkLogin())
    {
      this.securityManager.logout();
    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  /*
   * testing propagation into Sonar when changing the role of forge group for a group member
   */
  public void test07TestGroupRoleChangingPropagationIntoSonar() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("*****************************************************************************");
    log.info("************** testing propagation when changing group role  ****************");
    log.info("*****************************************************************************");
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le xml", projectName);

      List<String> userSonar = getUsersSonar();
      assertNotNull("Aucune user trouve dans le file de conf Sonar", userSonar);

      // get changing condition: new role for the group member of a given project
      Map<String, List<String>> groupAndRoleschangingCondition = xmlData
          .getGroupRoleChangingCondition(project);

      // test if this project has group changing condition
      if (groupAndRoleschangingCondition.size() == 1)
      {
        // Get changing conditions
        String oldRole = null;
        String newRole = null;
        String groupId = null;
        groupId = groupAndRoleschangingCondition.keySet().iterator().next();
        oldRole = groupAndRoleschangingCondition.get(groupId).get(0);
        newRole = groupAndRoleschangingCondition.get(groupId).get(1);

        assertNotNull("The new group role is null", oldRole);
        assertNotNull("The new group role is null", newRole);
        assertNotNull("The group member is null", groupId);

        // get the initial role for the group
        String initialRole = null;
        Map<String, String> groupMemberships = xmlData.getGroupsMembership(project);
        for (String group : groupMemberships.keySet())
        {
          if (groupId.equals(group))
          {
            initialRole = groupMemberships.get(group);
            break;
          }
        }
        assertNotNull("The initial role is null", initialRole);

        // change the role for the grouptest05TestRoleChangingPropagationIntoSonar
        try
        {
          // changing role on the forge
          changeRoleForGroup(project, groupId, newRole);

          // wait for propagation to tool.
          Thread.currentThread().sleep(TestConstants.WAIT_CHANGE_ROLE_PROPAGATION);

          // check propagation into Sonar Db
          checkGroupRoleChangingPropagation(project, groupId, newRole, TestConstants.QUALIMETRY_TYPE);
        }
        finally
        {
          // back changing the role for the last found user.
          changeRoleForGroup(project, groupId, initialRole);
          Thread.currentThread().sleep(TestConstants.WAIT_CHANGE_ROLE_PROPAGATION);
        }

      }

    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  private void checkGroupRoleChangingPropagation(String project, String groupId, String newRole,
      String app_type) throws Exception
  {
    try
    {
      // login
      securityManager.login(TestConstants.login, TestConstants.pwd);
      ManageACL manageACL = new ManageACL();
      log.info(" projet = " + project);

      Map<String, String> rolesDatabaseSonar = getRoleDatabaseSonar(project);
      assertNotNull("Aucune role trouve dans la DB Sonar", rolesDatabaseSonar);

      Map<String, String> roleCvsSonar = xmlData.getAppMapping(project, app_type);
      assertNotNull("Aucune correspondance dans les xml entre users et Sonar", roleCvsSonar);

      // users from group members and roles
      Map<String, String> userFromGroupsAndRoles = xmlData.getGroupsUsersMembership(project);
      assertNotNull("Aucune correspondance dans les xml entre users et roles", userFromGroupsAndRoles);

      // modify expected role for the given group
      assertNotNull("Le groupe donne pour permettre de modifier le role est null", groupId);
      assertNotEquals("Le groupe donne pour permettre de modifier le role est vide", "", groupId);
      assertNotNull("Le role cible est null", newRole);
      assertNotEquals("Le role cible est vide", "", newRole);
      for (String user : userFromGroupsAndRoles.keySet())
      {
        userFromGroupsAndRoles.remove(user);
        userFromGroupsAndRoles.put(user, newRole);
      }

      // check roles propagation into tool for users declared into group members
      for (Iterator<String> iterator1 = userFromGroupsAndRoles.keySet().iterator(); iterator1.hasNext();)
      {
        String login = iterator1.next();
        String roleDatabase = rolesDatabaseSonar.get(login);
        assertNotNull("The tool role does not exist for the user " + login, roleDatabase);
        String roleForge = userFromGroupsAndRoles.get(login);
        String roleSonarForge = roleCvsSonar.get(roleForge);
        manageACL.setIfACLError(login, roleDatabase, roleSonarForge);
      }
      manageACL.printACLError();
      assertEquals(0, manageACL.getListErrors().size());
    }

    finally
    {
      // logout
      securityManager.logout();
    }
  }

  private Map<String, String> getRoleDatabaseSonar(String pProjectId) throws Exception
  {
    Map<String, String> returned = new HashMap<String, String>();
    String jdbcUrl = getMySqlJdbcUrl(TestConstants.QUALIMETRY_TYPE);
    Connection con = null;
    try
    {
      con = DriverManager.getConnection(jdbcUrl, TestConstants.USER, TestConstants.PASSWORD);
    }
    catch (SQLException ex)
    {
      // try with default url
      con = DriverManager.getConnection(TestConstants.DEFAULT_DATABASE_SONAR, TestConstants.USER,
          TestConstants.PASSWORD);
    }
    // String query = "SELECT u.login, ur.role " + "FROM `user_roles` AS ur, users AS u "
    // + "WHERE u.id = ur.user_id";

    String query = "SELECT u.login, g.name FROM sonar.users AS u , sonar.groups AS g, sonar.groups_users AS gu "
        + "WHERE gu.user_id = u.id AND gu.group_id=g.id AND g.name LIKE '" + pProjectId + "%'";
    Statement stmt = con.createStatement();
    ResultSet resultSet = stmt.executeQuery(query);
    while (resultSet.next())
    {
      String username = resultSet.getString(1);
      String access_base = resultSet.getString(2);

      // 1st transform for ex. projettest24_codeviewer to: codeviewer
      String roleWoProject = "";
      StringTokenizer st = new StringTokenizer(access_base, "_");
      while (st.hasMoreTokens())
      {
        roleWoProject = st.nextToken();
      }

      // 2 nd get the role mapped into xml config: ie. : Code viewers by using the properties file:
      // codeviewer:Code viewers
      String access_mapped = (String) rolesSonar.get(roleWoProject);
      returned.put(username, access_mapped);
    }
    con.close();
    return returned;
  }

  private List<String> getUsersSonar() throws Exception
  {
    List<String> returned = new ArrayList<String>();
    String jdbcUrl = getMySqlJdbcUrl(TestConstants.QUALIMETRY_TYPE);
    Connection con = null;
    try
    {
      con = DriverManager.getConnection(jdbcUrl, TestConstants.USER, TestConstants.PASSWORD);
    }
    catch (SQLException ex)
    {
      // try with default url
      con = DriverManager.getConnection(TestConstants.DEFAULT_DATABASE_SONAR, TestConstants.USER,
          TestConstants.PASSWORD);
    }
    String query = "SELECT u.login FROM  users AS u ";
    Statement stmt = con.createStatement();
    ResultSet resultSet = stmt.executeQuery(query);
    while (resultSet.next())
    {
      String username = resultSet.getString(1);
      returned.add(username);
    }
    con.close();
    return returned;
  }

  private List<String> getProjetDatabaseSonar(String projectId) throws Exception
  {
    /*
     * projects look attached to the group names
     */
    List<String> returned = new ArrayList<String>();
    String jdbcUrl = getMySqlJdbcUrl(TestConstants.QUALIMETRY_TYPE);
    Connection con = null;
    try
    {
      con = DriverManager.getConnection(jdbcUrl, TestConstants.USER, TestConstants.PASSWORD);
    }
    catch (SQLException ex)
    {
      // try with default url
      con = DriverManager.getConnection(TestConstants.DEFAULT_DATABASE_SONAR, TestConstants.USER,
          TestConstants.PASSWORD);
    }
    // SELECT name FROM sonar.groups where name like 'projettest24%';
    String query = "SELECT g.name FROM  groups AS g where g.name LIKE '" + projectId + "%'";
    Statement stmt = con.createStatement();
    ResultSet resultSet = stmt.executeQuery(query);
    while (resultSet.next())
    {
      String groupName = resultSet.getString(1);
      returned.add(groupName);
    }
    con.close();
    return returned;
  }

  private void checkPropagationIntoSonarDb(String project, boolean modifyRoleforPropagation,
      String userFound, String roleTarget, String app_type) throws Exception
  {
    try
    {
      // login
      securityManager.login(TestConstants.login, TestConstants.pwd);
      ManageACL manageACL = new ManageACL();

      String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le xml", projectName);
      Map<String, String> rolesDatabaseSonar = getRoleDatabaseSonar(project);
      assertNotNull("Aucune role trouve dans la DB Sonar", rolesDatabaseSonar);

      // users members and roles
      Map<String, String> userAndRoles = xmlData.getUsersMembership(project);
      assertNotNull("Aucune correspondance dans les xml entre users et roles", userAndRoles);

      // users from group members and roles
      Map<String, String> userFromGroupsAndRoles = xmlData.getGroupsUsersMembership(project);
      assertNotNull("Aucune correspondance dans les xml entre users et roles", userFromGroupsAndRoles);

      Map<String, String> roleXmlSonar = xmlData.getAppMapping(project, app_type);
      assertNotNull("Aucune correspondance dans les xml entre users et Sonar", roleXmlSonar);

      // modify expected role for the given user: userFound
      if (modifyRoleforPropagation && (project.equals(xmlData.getRoleChangeCondition().getProjectTestId())))
      {
        assertNotNull("Le user donne pour permettre de modifier le role est null", userFound);
        assertNotEquals("Le user donne pour permettre de modifier le role est vide", "", userFound);
        assertNotNull("Le role cible est null", roleTarget);
        assertNotEquals("Le role cible est vide", "", roleTarget);
        userAndRoles.remove(userFound);
        userAndRoles.put(userFound, roleTarget);
      }

      // check roles propagation from user members
      for (Iterator<String> iterator1 = userAndRoles.keySet().iterator(); iterator1.hasNext();)
      {
        String user = iterator1.next();
        String roleDatabase = rolesDatabaseSonar.get(user);
        assertNotNull("The tool role does not exist for the user " + user, roleDatabase);
        String roleForge = userAndRoles.get(user);
        String roleSonarForge = roleXmlSonar.get(roleForge);
        manageACL.setIfACLError(user, roleDatabase, roleSonarForge);
      }

      // check roles propagation into tool for users declared into group members
      for (Iterator<String> iterator1 = userFromGroupsAndRoles.keySet().iterator(); iterator1.hasNext();)
      {
        String login = iterator1.next();
        String roleDatabase = rolesDatabaseSonar.get(login);
        assertNotNull("The tool role does not exist for the user " + login, roleDatabase);
        String roleForge = userFromGroupsAndRoles.get(login);
        String roleSonarForge = roleXmlSonar.get(roleForge);
        manageACL.setIfACLError(login, roleDatabase, roleSonarForge);
      }
      manageACL.printACLError();
      assertEquals(0, manageACL.getListErrors().size());
    }

    finally
    {
      // logout
      securityManager.logout();
    }
  }

}
