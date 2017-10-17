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
import org.json.JSONArray;
import org.json.JSONObject;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.it.test.datas.ManageACL;
import org.novaforge.forge.it.test.datas.ReportTest;
import org.novaforge.forge.it.test.datas.TestConstants;
import org.novaforge.forge.plugins.ecm.alfresco.library.AlfrescoRestHelperCustom;
import org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestHelper;
import org.novaforge.forge.plugins.ecm.alfresco.rest.exceptions.AlfrescoRestConnectionException;
import org.novaforge.forge.plugins.ecm.alfresco.rest.exceptions.AlfrescoRestException;

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

public class AlfrescoPropagationTest extends ToolsPropagationItBaseTest
{
  private static final Log log = LogFactory.getLog(AlfrescoPropagationTest.class);
  private Properties       rolesAlfresco;
  private Properties       mappingRolesAlfresco;

  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    rolesAlfresco = new Properties();
    InputStream fis = this.getClass().getResourceAsStream("/rolesAlfresco.properties");
    rolesAlfresco.load(fis);

    // mappingRolesAlfresco.properties properties file
    mappingRolesAlfresco = new Properties();
    fis = this.getClass().getResourceAsStream("/mappingRolesAlfresco.properties");
    mappingRolesAlfresco.load(fis);
  }

  public void test01GetRolesMapping() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***************************************************************************");
    log.info("******************           testGetRolesMapping Alfresco       *******************");
    log.info("***************************************************************************");

    // Needed because of authorization checking
    this.securityManager.login(TestConstants.login, TestConstants.pwd);

    // Needed because of authorization checking
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String app_name = xmlData.getApplicationName(project, TestConstants.GED_TYPE);
      assertNotNull("le nom d'appli non renseigne", app_name);

      Map<String, String> roleCvs = xmlData.getAppMapping(project, TestConstants.GED_TYPE);
      assertNotNull("Aucune role trouve dans le fichier csv", roleCvs);

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
        String roleCvsAlfresco = roleCvs.get(roleCvsForge);
        assertEquals("le role n'est pas equivalent d'apres le csv / Plugin de  Forge", roleCvsAlfresco,
            roles.get(roleCvsForge));

      }
    }

    this.securityManager.logout();
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  public void test02LookRolesAtDatabaseAlfresco() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);

    log.info("***************************************************************************");
    log.info("******************           Look Role At Database  Alfresco      ********************");
    log.info("***************************************************************************");
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      // check propagation into Alfresco Db
      checkPropagationIntoAlfrescoDb(project, false, null, null, TestConstants.GED_TYPE);
    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  private void checkPropagationIntoAlfrescoDb(String project, boolean modifyRoleforPropagation, String userFound,
                                              String roleTarget, String appType) throws Exception
  {
    try
    {
      // login
      securityManager.login(TestConstants.login, TestConstants.pwd);

      ManageACL manageACL = new ManageACL();

      String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le csv", projectName);

      String app_name = xmlData.getApplicationName(project, TestConstants.GED_TYPE);
      assertNotNull("le nom d'appli non renseigne", app_name);

      Map<String, String> rolesDatabaseAlfresco = getRoleRestAlfresco(project, app_name);
      assertNotNull("Aucune role trouve dans la DB Alfresco", rolesDatabaseAlfresco);

      // users members and roles
      Map<String, String> userAndRoles = xmlData.getUsersMembership(project);
      assertNotNull("Aucune correspondance dans les csv entre users et roles", userAndRoles);

      // users from group members and roles
      Map<String, String> userFromGroupsAndRoles = xmlData.getGroupsUsersMembership(project);
      assertNotNull("Aucune correspondance dans les csv entre users et roles", userFromGroupsAndRoles);

      Map<String, String> roleCvsAlfresco = xmlData.getAppMapping(project, appType);
      assertNotNull("Aucune correspondance dans les csv entre users et Alfresco", roleCvsAlfresco);

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

      for (Iterator<String> iterator1 = userAndRoles.keySet().iterator(); iterator1.hasNext(); )
      {
        String login = iterator1.next();
        User user = userPresenter.getUser(login);
        String roleDatabase = rolesDatabaseAlfresco.get(user.getName());
        String roleForge = userAndRoles.get(user.getLogin());
        String roleAlfrescoForge = roleCvsAlfresco.get(roleForge);
        // TODO: use forge service to get the mapping between role in base and role into forge tool
        // mapping
        String resolvedRole = (String) mappingRolesAlfresco.get(roleAlfrescoForge);
        manageACL.setIfACLError(login, roleDatabase, resolvedRole);
      }
      ManageACL manageACLGroup = new ManageACL();
      // check roles propagation into tool for users declared into group members
      for (Iterator<String> iterator1 = userFromGroupsAndRoles.keySet().iterator(); iterator1.hasNext(); )
      {
        String login = iterator1.next();
        User user = userPresenter.getUser(login);
        String roleDatabase = rolesDatabaseAlfresco.get(user.getName());
        String roleForge = userFromGroupsAndRoles.get(user.getLogin());
        String roleAlfrescoForge = roleCvsAlfresco.get(roleForge);
        // TODO: use forge service to get the mapping between role in base and role into forge tool
        // mapping
        String resolvedRole = (String) mappingRolesAlfresco.get(roleAlfrescoForge);
        manageACLGroup.setIfACLError(login, roleDatabase, resolvedRole);
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

  private Map<String, String> getRoleRestAlfresco(String projectId, String appName) throws Exception
  {
    JSONArray           memberships;
    List<JSONObject>    membershipList = new ArrayList<JSONObject>();
    Map<String, String> returned       = new HashMap<String, String>();
    AlfrescoRestHelper connector = getConnector(getAlfrescoEndPoint(), TestConstants.ALFRESCO_ADMIN,
                                                TestConstants.ALFRESCO_PWD);

    // construct SITE_ALFRESCO with: projectId + "_" + normalized appName
    String normalizedAppName      = normalizeService.normalize(appName);
    String normalizedSiteAlfresco = projectId + "_" + normalizedAppName;

    String api_target = "api/sites/" + normalizedSiteAlfresco + "/memberships";
    memberships = (JSONArray) connector.getJSON(api_target, null);
    assertNotNull("L'ArrayJSON renvoye par l'appel a l'api REST de Alfreco est vide", memberships);

    for (int i = 0; i < memberships.length(); i++)
    {
      membershipList.add(memberships.getJSONObject(i));
    }
    for (JSONObject membership : membershipList)
    {
      String role = membership.getString("role");
      JSONObject authority = membership.getJSONObject("authority");
      String userName = authority.getString("lastName");
      returned.put(userName, role);
    }

    return returned;
  }

  private AlfrescoRestHelper getConnector(final String pBaseUrl, final String pUsername, final String pPassword)
      throws AlfrescoRestException
  {
    AlfrescoRestHelper clientHelper;
    try
    {
      clientHelper = new AlfrescoRestHelperCustom(pBaseUrl, pUsername, pPassword);
    }
    catch (AlfrescoRestConnectionException e)
    {
      throw new AlfrescoRestException(e.getMessage(), e);
    }
    return clientHelper;
  }

  private String getAlfrescoEndPoint()
  {
    String endpoint;
    String host = getHostNameFromToolUrl(TestConstants.GED_TYPE);
    endpoint = TestConstants.ALFRESCO_ENDPOINT_PROTOCOL + host + ":" + TestConstants.ALFRESCO_ENDPOINT_PORT_NUMBER
                   + TestConstants.ALFRESCO_ENDPOINT_URI;
    return endpoint;
  }

  public void test03LookProjectAtDataseAlfresco() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***************************************************************************");
    log.info("******************           LookProjectAtDatase  Alfresco      ********************");
    log.info("***************************************************************************");
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le csv", projectName);

      String app_name = xmlData.getApplicationName(project, TestConstants.GED_TYPE);
      assertNotNull("le nom d'appli non renseigne", app_name);

      String alfrescoSite = getAlfrescoSite(project, app_name);
      assertNotNull("Projet non trouvé  dans la db Alfresco", alfrescoSite);
      log.info(alfrescoSite);
    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  private String getAlfrescoSite(String projectId, String appName) throws Exception
  {
    String           returned       = null;
    JSONArray        memberships;
    List<JSONObject> membershipList = new ArrayList<JSONObject>();

    AlfrescoRestHelper connector = getConnector(getAlfrescoEndPoint(), TestConstants.ALFRESCO_ADMIN,
                                                TestConstants.ALFRESCO_PWD);
    // construct SITE_ALFRESCO with: projectId + "_" + normalized appName
    String normalizedAppName      = normalizeService.normalize(appName);
    String normalizedSiteAlfresco = projectId + "_" + normalizedAppName;
    String api_target             = "api/sites/" + normalizedSiteAlfresco + "/memberships";

    memberships = (JSONArray) connector.getJSON(api_target, null);
    assertNotNull("L'ArrayJSON renvoye par l'appel a l'api REST de Alfreco est vide", memberships);

    for (int i = 0; i < memberships.length(); i++)
    {
      membershipList.add(memberships.getJSONObject(i));
    }
    for (JSONObject membership : membershipList)
    {
      // for the first found
      String url = membership.getString("url");
      StringTokenizer stringTokenizer = new StringTokenizer(url, "/");
      while (stringTokenizer.hasMoreTokens())
      {
        String token = stringTokenizer.nextToken();
        if (token.equals(normalizedSiteAlfresco))
        {
          returned = normalizedSiteAlfresco;
          break;
        }
      }
    }

    return returned;
  }

  public void test04LookAtUserAlfresco() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);

    List<String> userAlfresco = getUsersDBAlfresco();
    assertNotNull("Aucune ser trouve dans le file de conf Alfresco", userAlfresco);

    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le csv", projectName);

      Map<String, String> userAndRoles = xmlData.getUsersMembership(project);
      assertNotNull("Aucune correspondance dans les csv entre users et roles", userAndRoles);

      for (Iterator<String> iterator1 = userAndRoles.keySet().iterator(); iterator1.hasNext();)
      {
        String user = iterator1.next();
        assertTrue("le user n'existe pas dans Alfresco", userAlfresco.contains(user));
      }
    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  private List<String> getUsersDBAlfresco() throws Exception
  {
    List<String> returned = new ArrayList<String>();
    String       jdbcUrl  = getMySqlJdbcUrl(TestConstants.GED_TYPE);
    Connection   con;
    try
    {
      con = DriverManager.getConnection(jdbcUrl, TestConstants.USER, TestConstants.PASSWORD);
    }
    catch (SQLException ex)
    {
      // try with default url
      con = DriverManager.getConnection(TestConstants.DEFAULT_DATABASE_ALFRESCO, TestConstants.USER,
                                        TestConstants.PASSWORD);
    }
    String    query     = "SELECT authority FROM alf_authority";
    Statement stmt      = con.createStatement();
    ResultSet resultSet = stmt.executeQuery(query);
    while (resultSet.next())
    {
      String username = resultSet.getString(1);
      returned.add(username);
    }
    con.close();
    return returned;
  }

  /*
   * testing propagation into Alfresco when changing forge role.
   */
  public void test05TestRoleChangingPropagationIntoAlfresco() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);

    log.info("***************************************************************************");
    log.info("******************           Look Role change At Database  Alfresco      ********************");
    log.info("***************************************************************************");
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      // changing role for the first user having "developpeur" role
      String projectTestId = xmlData.getRoleChangeCondition().getProjectTestId();
      assertNotNull("le projectId pas trouve dans le csv", projectTestId);
      String roleToChange = xmlData.getRoleChangeCondition().getRoleToChange();
      assertNotNull("le roleToChange pas trouve dans le csv", roleToChange);
      String roleTarget = xmlData.getRoleChangeCondition().getRoleTarget();
      assertNotNull("le roleTarget pas trouve dans le csv", roleTarget);
      String userIdToChange = xmlData.getRoleChangeCondition().getUserId();
      if (project.equals(projectTestId) && userIdToChange != null)
      {
        try
        {
          // changing role on the forge
          changeRoleForUser(projectTestId, userIdToChange, roleToChange, roleTarget);
          assertNotNull("Aucun user trouve avec le role = " + roleToChange, userIdToChange);
          Thread.currentThread().sleep(TestConstants.WAIT_CHANGE_ROLE_PROPAGATION);
          // check propagation into Alfreco Db
          checkPropagationIntoAlfrescoDb(project, true, userIdToChange, roleTarget, TestConstants.GED_TYPE);
          Thread.currentThread().sleep(TestConstants.WAIT_CHANGE_ROLE_PROPAGATION);
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
   * /datas/safran/engines/karaf/bin/client forge:importdatas
   * /datas/safran/datas/karaf/tmp/import_itests_validation.xml
   * testing propagation into Alfresco when changing roles mappings of Alfresco application.
   */
  public void test06TestChangingRoleMappingPropagationIntoAlfresco() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***********************************************************************************");
    log.info("********* Test propagation when changing roles mapping for Alfresco *******************");
    log.info("***********************************************************************************");

    // Needed because of authorization checking
    this.securityManager.login(TestConstants.login, TestConstants.pwd);

    // Needed because of authorization checking
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String app_name = xmlData.getApplicationName(project, TestConstants.GED_TYPE);
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
        Map<String, String> newCvsRoleMapping = xmlData.getAppMapping(project, TestConstants.GED_TYPE2);
        assertNotEquals("Aucun role trouve dans le fichier csv de changement de mapping", true,
            newCvsRoleMapping.isEmpty());
        updateApplication(project, applicationUri, newCvsRoleMapping, TestConstants.GED_TYPE, app_name);
        Thread.currentThread().sleep(TestConstants.WAIT_CHANGE_ROLEMAPPING_PROPAGATION);

        // check propagation according the new roles mapping
        checkPropagationIntoAlfrescoDb(project, false, null, null, TestConstants.GED_TYPE2);
      }
      // always done eveen getting failure
      finally
      {
        // setting back the roles mapping
        Map<String, String> initialCvsRoleMapping = xmlData.getAppMapping(project, TestConstants.GED_TYPE);
        assertNotEquals("Aucun role trouve dans le fichier csv initial de mapping", true,
            initialCvsRoleMapping.isEmpty());
        updateApplication(project, applicationUri, initialCvsRoleMapping, TestConstants.GED_TYPE, app_name);
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
   * testing propagation into Alfresco when changing the role of forge group for a group member
   */
  public void test07TestGroupRoleChangingPropagationIntoAlfresco() throws Exception
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
      assertNotNull("le project pas trouve dans le csv", projectName);

      List<String> userAlfresco = getUsersDBAlfresco();
      assertNotNull("Aucune user trouve dans le file de conf Alfresco", userAlfresco);

      // get changing condition: new role for the group member of a given project
      Map<String, List<String>> groupAndRoleschangingCondition = xmlData
          .getGroupRoleChangingCondition(project);

      // test if this project has group changing condition
      if (groupAndRoleschangingCondition.size() == 1)
      {
        // Get changing conditions
        String oldRole;
        String newRole;
        String groupId;
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

          // check propagation into Alfresco Db
          checkGroupRoleChangingPropagation(project, groupId, newRole, TestConstants.GED_TYPE);
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

      String app_name = xmlData.getApplicationName(project, TestConstants.GED_TYPE);
      assertNotNull("le nom d'appli non renseigne", app_name);

      Map<String, String> rolesDatabaseAlfresco = getRoleRestAlfresco(project, app_name);
      assertNotNull("Aucune role trouve dans la DB Alfresco", rolesDatabaseAlfresco);

      Map<String, String> roleCvsAlfresco = xmlData.getAppMapping(project, app_type);
      assertNotNull("Aucune correspondance dans les csv entre users et Alfresco", roleCvsAlfresco);

      // users from group members and roles
      Map<String, String> userFromGroupsAndRoles = xmlData.getGroupsUsersMembership(project);
      assertNotNull("Aucune correspondance dans les csv entre users et roles", userFromGroupsAndRoles);

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
        User user = userPresenter.getUser(login);
        String roleDatabase = rolesDatabaseAlfresco.get(user.getName());
        String roleForge = userFromGroupsAndRoles.get(user.getLogin());
        String roleAlfrescoForge = roleCvsAlfresco.get(roleForge);
        // TODO: use forge service to get the mapping between role in base and role into forge tool
        // mapping
        String resolvedRole = (String) mappingRolesAlfresco.get(roleAlfrescoForge);
        manageACL.setIfACLError(login, roleDatabase, resolvedRole);
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
