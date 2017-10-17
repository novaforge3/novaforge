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
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.it.test.datas.ManageACL;
import org.novaforge.forge.it.test.datas.ReportTest;
import org.novaforge.forge.it.test.datas.TestConstants;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author germainc, blachon-m
 */
public class MantisPropagationTest extends ToolsPropagationItBaseTest
{

  private static final Log log = LogFactory.getLog(MantisPropagationTest.class);
  public Properties rolesMantis;

  /**
   * Constants declaration
   */
  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    rolesMantis = new Properties();
    InputStream fis = this.getClass().getResourceAsStream("/rolesMantis.properties");
    rolesMantis.load(fis);
  }

  public void test01GetRolesMapping() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***************************************************************************");
    log.info("******************           testGetRolesMapping        *******************");
    log.info("***************************************************************************");

    // Needed because of authorization checking
    this.securityManager.login(TestConstants.login, TestConstants.pwd);

    // Needed because of authorization checking
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String app_name = xmlData.getApplicationName(project, TestConstants.BUGTRACKER_TYPE);
      assertNotNull("le nom d'appli non renseigne", app_name);

      Map<String, String> roleCvs = xmlData.getAppMapping(project, TestConstants.BUGTRACKER_TYPE);
      assertNotNull("Aucune role trouve dans le fichier XML", roleCvs);

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

      Map<String, String> roles = this.applicationPresenter.getRoleMapping(project, uri);
      assertEquals(roleCvs.size(), roles.size());
      for (Iterator<String> iterator1 = roleCvs.keySet().iterator(); iterator1.hasNext();)
      {
        String roleCvsForge = iterator1.next();
        String roleCvsMantis = roleCvs.get(roleCvsForge);
        assertEquals("Le role n'est pas equivalent d'apres le XML / Plugin de Forge", roleCvsMantis,
            roles.get(roleCvsForge));
      }
    }
    this.securityManager.logout();
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);

  }

  public void test02LookRoleAtDataseMantis() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);

    log.info("***************************************************************************");
    log.info("******************           Look Role At Database  Mantis      ********************");
    log.info("***************************************************************************");
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      // check propagation into Mantis Db
      checkPropagationIntoMantisDb(project, false, null, null, TestConstants.BUGTRACKER_TYPE);
    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  private void checkPropagationIntoMantisDb(String project, boolean modifyRoleforPropagation, String userFound,
                                            String roleTarget, String appType) throws Exception
  {
    try
    {
      // login
      securityManager.login(TestConstants.login, TestConstants.pwd);
      ManageACL manageACL = new ManageACL();

      String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le XML", projectName);

      Map<String, String> rolesDatabaseMantis = getRoleDatabaseMantis(projectName);
      assertNotNull("Aucune role trouve dans la DB Mantis", rolesDatabaseMantis);

      // users members and roles
      Map<String, String> userAndRoles = xmlData.getUsersMembership(project);
      assertNotNull("Aucune correspondance dans les XML entre users et roles", userAndRoles);

      // users from group members and roles
      Map<String, String> userFromGroupsAndRoles = xmlData.getGroupsUsersMembership(project);
      assertNotNull("Aucune correspondance dans les XML entre users et roles", userFromGroupsAndRoles);

      Map<String, String> roleCvsMantis = xmlData.getAppMapping(project, appType);
      assertNotNull("Aucune correspondance dans les XML entre users et Mantis", roleCvsMantis);

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
      for (Iterator<String> iterator1 = userAndRoles.keySet().iterator(); iterator1.hasNext(); )
      {
        String user = iterator1.next();
        String roleDatabase = rolesDatabaseMantis.get(user);
        assertNotNull("The tool role does not exist for the user " + user, roleDatabase);
        String roleForge = userAndRoles.get(user);
        String roleMantisForge = roleCvsMantis.get(roleForge);
        manageACL.setIfACLError(user, roleDatabase, roleMantisForge);
      }

      ManageACL manageACLGroup = new ManageACL();
      // check roles propagation into tool for users declared into group members
      for (Iterator<String> iterator1 = userFromGroupsAndRoles.keySet().iterator(); iterator1.hasNext(); )
      {
        String user = iterator1.next();
        String roleDatabase = rolesDatabaseMantis.get(user);
        assertNotNull("The tool role does not exist for the user " + user, roleDatabase);
        String roleForge = userFromGroupsAndRoles.get(user);
        String roleMantisForge = roleCvsMantis.get(roleForge);
        manageACLGroup.setIfACLError(user, roleDatabase, roleMantisForge);
      }
      manageACLGroup.printACLError();
      assertEquals(0, manageACLGroup.getListErrors().size());

    }
    finally
    {
      // logout
      securityManager.logout();
    }
  }

  private Map<String, String> getRoleDatabaseMantis(String projet) throws Exception
  {
    Map<String, String> returned = new HashMap<String, String>();
    String              jdbcUrl  = getMySqlJdbcUrl(TestConstants.BUGTRACKER_TYPE);
    Connection          con      = DriverManager.getConnection(jdbcUrl, TestConstants.USER, TestConstants.PASSWORD);
    String query = "SELECT  p.name, u.username, pu.access_level    "
                       + "FROM  `mantis_user_table` AS u,  `mantis_project_user_list_table` AS pu,  `mantis_project_table` AS p "
                       + "WHERE p.id = pu.project_id AND u.id = pu.user_id AND p.name LIKE  '" + projet + "%'";
    Statement stmt      = con.createStatement();
    ResultSet resultSet = stmt.executeQuery(query);
    while (resultSet.next())
    {
      // String projetname = resultSet.getString(1);
      String username = resultSet.getString(2);
      String access_level = resultSet.getString(3);
      String access_name = (String) rolesMantis.get(access_level);
      returned.put(username, access_name);
    }
    con.close();
    return returned;
  }

  public void test03LookProjectAtDataseMantis() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***************************************************************************");
    log.info("******************           LookProjectAtDatase  Mantis      ********************");
    log.info("***************************************************************************");
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le XML", projectName);

      String projetDB = getProjetDatabaseMantis(projectName);
      assertNotNull("Projet non trouvé  dans la db Mantis", projetDB);
      log.info(projetDB);
    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  private String getProjetDatabaseMantis(String projet) throws Exception
  {
    String     returned = null;
    String     jdbcUrl  = getMySqlJdbcUrl(TestConstants.BUGTRACKER_TYPE);
    Connection con      = DriverManager.getConnection(jdbcUrl, TestConstants.USER, TestConstants.PASSWORD);
    String query = "SELECT  p.name    " + "FROM `mantis_project_table` AS p " + "WHERE p.name LIKE  '" + projet + "%'";
    Statement stmt      = con.createStatement();
    ResultSet resultSet = stmt.executeQuery(query);
    while (resultSet.next())
    {
      returned = resultSet.getString(1);
    }
    con.close();
    return returned;
  }

  public void test04LookAtUserMantis() throws Exception
  {
    List<String> userMantis = getUsersDBMantis();
    assertNotNull("Aucune ser trouve dans le file de conf Mantis", userMantis);

    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le XML", projectName);

      Map<String, String> userAndRoles = xmlData.getUsersMembership(project);
      assertNotNull("Aucune correspondance dans les XML entre users et roles", userAndRoles);

      for (Iterator<String> iterator1 = userAndRoles.keySet().iterator(); iterator1.hasNext();)
      {
        String user = iterator1.next();
        assertTrue("le user n'existe pas dans Mantis", userMantis.contains(user));
      }
    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  private List<String> getUsersDBMantis() throws Exception
  {
    List<String> returned  = new ArrayList<String>();
    String       jdbcUrl   = getMySqlJdbcUrl(TestConstants.BUGTRACKER_TYPE);
    Connection   con       = DriverManager.getConnection(jdbcUrl, TestConstants.USER, TestConstants.PASSWORD);
    String       query     = "SELECT u.username FROM mantis_user_table AS u";
    Statement    stmt      = con.createStatement();
    ResultSet    resultSet = stmt.executeQuery(query);
    while (resultSet.next())
    {
      String username = resultSet.getString(1);
      returned.add(username);
    }
    con.close();
    return returned;
  }

  public void test05TestRoleChangingPropagationIntoMantis() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***************************************************************************");
    log.info("******************           Look Role change At Database  Mantis      ********************");
    log.info("***************************************************************************");
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String projectTestId = xmlData.getRoleChangeCondition().getProjectTestId();
      assertNotNull("le projectId pas trouve dans le XML", projectTestId);
      String roleToChange = xmlData.getRoleChangeCondition().getRoleToChange();
      assertNotNull("le roleToChange pas trouve dans le XML", roleToChange);
      String roleTarget = xmlData.getRoleChangeCondition().getRoleTarget();
      assertNotNull("le roleTarget pas trouve dans le XML", roleTarget);
      String userIdToChange = xmlData.getRoleChangeCondition().getUserId();

      if (project.equals(projectTestId) && userIdToChange != null)
      {
        try
        {
          // changing role on the forge
          changeRoleForUser(projectTestId, userIdToChange, roleToChange, roleTarget);
          Thread.currentThread().sleep(TestConstants.WAIT_CHANGE_ROLE_PROPAGATION);
          // check propagation into Mantis Db
          checkPropagationIntoMantisDb(project, true, userIdToChange, roleTarget,
              TestConstants.BUGTRACKER_TYPE);
        }
        finally
        {
          // back changing the role for the last found user.
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
   * testing propagation into Mantis when changing roles mappings of Mantis application.
   */
  public void test06TestChangingRoleMappingPropagationIntoMantis() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***********************************************************************************");
    log.info("********* Test propagation when changing roles mapping for Mantis *******************");
    log.info("***********************************************************************************");

    // Needed because of authorization checking
    this.securityManager.login(TestConstants.login, TestConstants.pwd);

    // Needed because of authorization checking
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String app_name = xmlData.getApplicationName(project, TestConstants.BUGTRACKER_TYPE);
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
            .getAppMapping(project, TestConstants.BUGTRACKER_TYPE2);
        assertNotEquals("Aucun role trouve dans le fichier XML de changement de mapping", true,
            newCvsRoleMapping.isEmpty());
        updateApplication(project, applicationUri, newCvsRoleMapping, TestConstants.BUGTRACKER_TYPE, app_name);

        // wait until jms message has been executed by the plugin (mapping) and the tool (update of the
        // tool
        // roles)
        // may be could loop onto targeted tool roles to be more sure than an arbitrary time (?).
        Thread.currentThread().sleep(TestConstants.WAIT_CHANGE_ROLEMAPPING_PROPAGATION);

        // check propagation according the new roles mapping
        checkPropagationIntoMantisDb(project, false, null, null, TestConstants.BUGTRACKER_TYPE2);
      }
      // always done eveen getting failure
      finally
      {
        // setting back the roles mapping
        Map<String, String> initialCvsRoleMapping = xmlData.getAppMapping(project,
            TestConstants.BUGTRACKER_TYPE);
        assertNotEquals("Aucun role trouve dans le fichier XML initial de mapping", true,
            initialCvsRoleMapping.isEmpty());
        updateApplication(project, applicationUri, initialCvsRoleMapping, TestConstants.BUGTRACKER_TYPE,
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
   * testing propagation into Mantis when changing the role of forge group for a group member
   */
  public void test07TestGroupRoleChangingPropagationIntoMantis() throws Exception
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
      assertNotNull("le project pas trouve dans le XML", projectName);

      List<String> userMantis = getUsersDBMantis();
      assertNotNull("Aucun user trouve dans le fichier de conf Mantis", userMantis);

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

        // change the role for the group
        try
        {
          // changing role on the forge
          changeRoleForGroup(project, groupId, newRole);

          // wait for propagation to tool.
          Thread.currentThread().sleep(TestConstants.WAIT_CHANGE_ROLE_PROPAGATION);

          // check propagation into Mantis Db
          checkGroupRoleChangingPropagation(project, groupId, newRole, TestConstants.BUGTRACKER_TYPE);
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

      String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le XML", projectName);

      Map<String, String> rolesDatabaseMantis = getRoleDatabaseMantis(projectName);
      assertNotNull("Aucune role trouve dans la DB Mantis", rolesDatabaseMantis);

      Map<String, String> roleCvsMantis = xmlData.getAppMapping(project, app_type);
      assertNotNull("Aucune correspondance dans les XML entre users et Mantis", roleCvsMantis);

      // users from group members and roles
      Map<String, String> userFromGroupsAndRoles = xmlData.getGroupsUsersMembership(project);
      assertNotNull("Aucune correspondance dans les XML entre users et roles", userFromGroupsAndRoles);

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
        String user = iterator1.next();
        String roleDatabase = rolesDatabaseMantis.get(user);
        assertNotNull("The tool role does not exist for the user " + user, roleDatabase);
        String roleForge = userFromGroupsAndRoles.get(user);
        String roleMantisForge = roleCvsMantis.get(roleForge);
        manageACL.setIfACLError(user, roleDatabase, roleMantisForge);
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