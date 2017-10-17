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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DokuWikiPropagationTest extends ToolsPropagationItBaseTest
{

  private static final Log log = LogFactory.getLog(DokuWikiPropagationTest.class);
  private Properties rolesWiki;

  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    rolesWiki = new Properties();

    InputStream fis = this.getClass().getResourceAsStream("/rolesDokuWiki.properties");
    rolesWiki.load(fis);
  }

  public void test01GetRolesMapping() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***************************************************************************");
    log.info("******************           testGetRolesMapping Wiki       *******************");
    log.info("***************************************************************************");

    // Needed because of authorization checking
    this.securityManager.login(TestConstants.login, TestConstants.pwd);

    // Needed because of authorization checking
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String app_name = xmlData.getApplicationName(project, TestConstants.WIKI_TYPE);
      assertNotNull("Le nom d'appli non renseigne", app_name);

      Map<String, String> roleCvs = xmlData.getAppMapping(project, TestConstants.WIKI_TYPE);
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
        String roleCvsWiki = roleCvs.get(roleCvsForge);
        assertEquals("Le role n'est pas equivalent d'apres le csv / Plugin de Forge", roleCvsWiki,
            roles.get(roleCvsForge));
      }
    }
    this.securityManager.logout();
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  public void test02LookRoleAtDatabaseDokuWiki() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***************************************************************************");
    log.info("******************           Look Role At Database  DokuWiki      ********************");
    log.info("***************************************************************************");
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      // check propagation into DokuWiki Db
      checkPropagationIntoDokuWikiDb(project, false, null, null, TestConstants.WIKI_TYPE);
    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  private void checkPropagationIntoDokuWikiDb(String project, boolean modifyRoleforPropagation, String userFound,
                                              String roleTarget, String appType) throws Exception
  {
    try
    {
      // login
      securityManager.login(TestConstants.login, TestConstants.pwd);

      ManageACL manageACL = new ManageACL();

      String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le csv", projectName);

      Map<String, String> rolesDatabaseDokuWiki = getRoleFileDokuWiki(projectName);
      assertNotNull("Aucune role trouve dans le file de conf dokuwiki", rolesDatabaseDokuWiki);
      // decode "%2d" encoding into the key string (user name ) (ex.: usertest11%2du=Developper,
      // .....)
      HashMap<String, String> decodedRolesDatabaseDokuWiki = new HashMap<String, String>();
      for (String enCodedUser : rolesDatabaseDokuWiki.keySet())
      {
        String deCodedUser = URLDecoder.decode(enCodedUser, "UTF-8");
        decodedRolesDatabaseDokuWiki.put(deCodedUser, rolesDatabaseDokuWiki.get(enCodedUser));
      }

      // users members and roles
      Map<String, String> userAndRoles = xmlData.getUsersMembership(project);
      assertNotNull("Aucune correspondance dans les csv entre users et roles", userAndRoles);

      // users from group members and roles
      Map<String, String> userFromGroupsAndRoles = xmlData.getGroupsUsersMembership(project);
      assertNotNull("Aucune correspondance dans les csv entre users et roles", userFromGroupsAndRoles);

      Map<String, String> roleCvsDokuWiki = xmlData.getAppMapping(project, appType);
      assertNotNull("Aucune correspondance dans les csv entre users et Dokuwiki", roleCvsDokuWiki);

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
        String user = iterator1.next();
        String roleDatabase = decodedRolesDatabaseDokuWiki.get(user);
        assertNotNull("The tool role does not exist for the user " + user, roleDatabase);
        String roleForge = userAndRoles.get(user);
        String roleDokuWikiForge = roleCvsDokuWiki.get(roleForge);
        manageACL.setIfACLError(user, roleDatabase, roleDokuWikiForge);
      }

      // check roles propagation into tool for users declared into group members
      for (Iterator<String> iterator1 = userFromGroupsAndRoles.keySet().iterator(); iterator1.hasNext(); )
      {
        String user = iterator1.next();
        String roleDatabase = decodedRolesDatabaseDokuWiki.get(user);
        assertNotNull("The tool role does not exist for the user " + user, roleDatabase);
        String roleForge = userFromGroupsAndRoles.get(user);
        String roleDokuWikiForge = roleCvsDokuWiki.get(roleForge);
        manageACL.setIfACLError(user, roleDatabase, roleDokuWikiForge);
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

  private Map<String, String> getRoleFileDokuWiki(String projectName)
  {
    Map<String, String> returned = new HashMap<String, String>();
    try
    {
      FileInputStream fstream = new FileInputStream(TestConstants.DOKUWIKI_SECURITY_FILE);
      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      String p = projectName.toLowerCase().replace(' ', '_');

      // Read File Line By Line
      while ((strLine = br.readLine()) != null)
      {
        String[] tabString = strLine.split("\\s");

        if (strLine.startsWith(p + "_wikiappli:*"))
        {

          returned.put(tabString[1], rolesWiki.getProperty(tabString[2]));
        }
      }
      in.close();
    }
    catch (Exception e)
    {
      log.error("Error: " + e.getMessage());
    }
    return returned;
  }

  public void test03LookProjectAtDataseDokuWiki() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***************************************************************************");
    log.info("******************           LookProjectAtDatase  DokuWiki      ********************");
    log.info("***************************************************************************");
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le csv", projectName);

      String projetDB = getProjetDatabaseDokuWiki(projectName);
      assertNotNull("Projet non trouvé  dans la db DokuWiki", projetDB);
      log.info(projetDB);
    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  private String getProjetDatabaseDokuWiki(String projectName)
  {
    String returned = null;
    try
    {
      FileInputStream fstream = new FileInputStream(TestConstants.DOKUWIKI_SECURITY_FILE);
      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      String p = projectName.toLowerCase().replace(' ', '_');

      // Read File Line By Line
      while ((strLine = br.readLine()) != null)
      {
        if (strLine.startsWith(p + "_wikiappli:*"))
        {
          returned = strLine.substring(0, strLine.indexOf("_wikiappli"));
        }
      }
      in.close();
    }
    catch (Exception e)
    {
      log.error("Error: " + e.getMessage());
    }
    return returned;
  }

  public void test04LookAtUserWiki() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le csv", projectName);

      List<String> userDokuWiki = getUsersFileDokuWiki(projectName);
      assertNotNull("Aucune ser trouve dans le file de conf dokuwiki", userDokuWiki);

      // decode "%2d" encoding into the key string (user name ) (ex.: usertest11%2du=Developper,
      // .....)
      ArrayList<String> decodedUserDokuWiki = new ArrayList<String>();
      for (String enCodedUser : userDokuWiki)
      {
        String deCodedUser = URLDecoder.decode(enCodedUser, "UTF-8");
        decodedUserDokuWiki.add(deCodedUser);
      }

      Map<String, String> userAndRoles = xmlData.getUsersMembership(project);
      assertNotNull("Aucune correspondance dans les csv entre users et roles", userAndRoles);

      for (Iterator<String> iterator1 = userAndRoles.keySet().iterator(); iterator1.hasNext();)
      {
        String user = iterator1.next();
        assertTrue("le user n'existe pas dans doku", decodedUserDokuWiki.contains(user));
      }
    }
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  private List<String> getUsersFileDokuWiki(String projectName)
  {
    List<String> returned = new ArrayList<String>();
    try
    {
      FileInputStream fstream = new FileInputStream(TestConstants.DOKUWIKI_SECURITY_FILE);
      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      String p = projectName.toLowerCase().replace(' ', '_');

      // Read File Line By Line
      while ((strLine = br.readLine()) != null)
      {
        String[] tabString = strLine.split("\\s");

        if (strLine.startsWith(p + "_wikiappli:*"))
        {
          if (!returned.contains(tabString[1]))
          {
            returned.add(tabString[1]);
          }
        }
      }
      in.close();
    }
    catch (Exception e)
    {
      log.error("Error: " + e.getMessage());
    }
    return returned;
  }

  /*
   * testing propagation into DokuWiki when changing forge role.
   */
  public void test05TestRoleChangingPropagationIntoDokuWiki() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);

    log.info("***************************************************************************");
    log.info("******************           Look Role change At Database  DokuWiki      ********************");
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
          Thread.currentThread().sleep(TestConstants.WAIT_CHANGE_ROLE_PROPAGATION);
          // check propagation into DokuWiki Db
          checkPropagationIntoDokuWikiDb(project, true, userIdToChange, roleTarget, TestConstants.WIKI_TYPE);
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
   * testing propagation into DokuWiki when changing roles mappings of DokuWiki application.
   */
  public void test06TestChangingRoleMappingPropagationIntoDokuWiki() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***********************************************************************************");
    log.info("********* Test propagation when changing roles mapping for DokuWiki *******************");
    log.info("***********************************************************************************");

    // Needed because of authorization checking
    this.securityManager.login(TestConstants.login, TestConstants.pwd);

    // Needed because of authorization checking
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);

      String app_name = xmlData.getApplicationName(project, TestConstants.WIKI_TYPE);
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
        Map<String, String> newCvsRoleMapping = xmlData.getAppMapping(project, TestConstants.WIKI_TYPE2);
        assertNotEquals("Aucun role trouve dans le fichier csv de changement de mapping", true,
            newCvsRoleMapping.isEmpty());
        updateApplication(project, applicationUri, newCvsRoleMapping, TestConstants.WIKI_TYPE, app_name);

        // wait until jms message has been executed by the plugin (mapping) and the tool (update of the
        // tool
        // roles)
        // may be could loop onto targeted tool roles to be more sure than an arbitrary time (?).
        Thread.currentThread().sleep(TestConstants.WAIT_CHANGE_ROLEMAPPING_PROPAGATION);

        // check propagation according the new roles mapping
        checkPropagationIntoDokuWikiDb(project, false, null, null, TestConstants.WIKI_TYPE2);
      }
      // always done eveen getting failure
      finally
      {
        // setting back the roles mapping
        Map<String, String> initialCvsRoleMapping = xmlData.getAppMapping(project, TestConstants.WIKI_TYPE);
        assertNotEquals("Aucun role trouve dans le fichier csv initial de mapping", true,
            initialCvsRoleMapping.isEmpty());
        updateApplication(project, applicationUri, initialCvsRoleMapping, TestConstants.WIKI_TYPE, app_name);
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
   * testing propagation into DokuWiki when changing the role of forge group for a group member
   */
  public void test07TestGroupRoleChangingPropagationIntoDokuWiki() throws Exception
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

      List<String> userDokuWiki = getUsersFileDokuWiki(projectName);
      assertTrue("Aucune user trouve dans le file de conf DokuWiki", !(userDokuWiki.size() == 0));

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

          // check propagation into DokuWiki Db
          checkGroupRoleChangingPropagation(project, groupId, newRole, TestConstants.WIKI_TYPE);
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
      assertNotNull("le project pas trouve dans le csv", projectName);

      Map<String, String> rolesDatabaseDokuWiki = getRoleFileDokuWiki(projectName);
      assertNotNull("Aucune role trouve dans la DB DokuWiki", rolesDatabaseDokuWiki);
      assertNotNull("Aucune role trouve dans le file de conf dokuwiki", rolesDatabaseDokuWiki);
      // decode "%2d" encoding into the key string (user name ) (ex.: usertest11%2du=Developper,
      // .....)
      HashMap<String, String> decodedRolesDatabaseDokuWiki = new HashMap<String, String>();
      for (String enCodedUser : rolesDatabaseDokuWiki.keySet())
      {
        String deCodedUser = URLDecoder.decode(enCodedUser, "UTF-8");
        decodedRolesDatabaseDokuWiki.put(deCodedUser, rolesDatabaseDokuWiki.get(enCodedUser));
      }

      Map<String, String> roleCvsDokuWiki = xmlData.getAppMapping(project, app_type);
      assertNotNull("Aucune correspondance dans les csv entre users et DokuWiki", roleCvsDokuWiki);

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
        String user = iterator1.next();
        String roleDatabase = decodedRolesDatabaseDokuWiki.get(user);
        assertNotNull("The tool role does not exist for the user " + user, roleDatabase);
        String roleForge = userFromGroupsAndRoles.get(user);
        String roleDokuWikiForge = roleCvsDokuWiki.get(roleForge);
        manageACL.setIfACLError(user, roleDatabase, roleDokuWikiForge);
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
