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

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.it.test.datas.ManageACL;
import org.novaforge.forge.it.test.datas.ReportTest;
import org.novaforge.forge.it.test.datas.TestConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class JenkinsPropagationTest extends ToolsPropagationItBaseTest
{
  private static final Log log = LogFactory.getLog(JenkinsPropagationTest.class);

  @Override
  public void setUp() throws Exception
  {
    super.setUp();
  }

  public void test01GetRolesMapping() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), getName(), false);
    log.info("***************************************************************************");
    log.info("******************           testGetRolesMapping Jenkins  *******************");
    log.info("***************************************************************************");

    // Needed because of authorization checking
    securityManager.login(TestConstants.login, TestConstants.pwd);

    // Needed because of authorization checking
    for (String project : xmlData.getProjects().keySet())
    {
      log.info(" projet = " + project);

      final String app_name = xmlData.getApplicationName(project, TestConstants.BUILDING_TYPE);
      assertNotNull("Le nom d'appli non renseigne", app_name);

      final Map<String, String> roleCvs = xmlData.getAppMapping(project, TestConstants.BUILDING_TYPE);
      assertNotNull("Aucune role trouve dans le fichier csv", roleCvs);

      final List<ProjectApplication> applications = applicationPresenter.getAllProjectApplications(project);
      assertNotNull("Aucune application trouve dans le projet", applications);

      String uri = null;
      for (final ProjectApplication application : applications)
      {
        if (application.getName().equals(app_name))
        {
          uri = application.getUri();
        }
      }
      assertNotNull(uri);
      final Map<String, String> roles = applicationPresenter.getRoleMapping(project, uri);
      assertEquals(roleCvs.size(), roles.size());
      for (String roleCvsForge : roleCvs.keySet())
      {
        final String roleCvsJenkins = roleCvs.get(roleCvsForge);
        assertEquals("Le role n'est pas equivalent d'apres le csv / Plugin de Forge", roleCvsJenkins,
            roles.get(roleCvsForge));
      }
    }
    securityManager.logout();
    ReportTest.writeTestResult(this.getClass().getName(), getName(), true);
  }

  public void test02LookRolesAtFilePermissions() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), getName(), false);

    log.info("***************************************************************************");
    log.info("******************           Look Role At Database  Jenkins      ********************");
    log.info("***************************************************************************");
    for (String project : xmlData.getProjects().keySet())
    {
      log.info(" projet = " + project);

      // check propagation into Jenkins Db
      checkPropagationIntoJenkinsDb(project, false, null, null, TestConstants.BUILDING_TYPE);
    }
    ReportTest.writeTestResult(this.getClass().getName(), getName(), true);
  }

  private void checkPropagationIntoJenkinsDb(final String project, final boolean modifyRoleforPropagation,
                                             final String userFound, final String roleTarget, final String appType)
      throws Exception
  {
    try
    {
      // login
      securityManager.login(TestConstants.login, TestConstants.pwd);

      final ManageACL manageACL = new ManageACL();

      final String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le csv", projectName);

      final Map<String, String> rolesDatabaseJenkins = getRoleJenkins(project);
      assertNotNull("Aucune role trouve  Jenkins", rolesDatabaseJenkins);

      // users members and roles
      final Map<String, String> userAndRoles = xmlData.getUsersMembership(project);
      assertNotNull("Aucune correspondance dans les csv entre users et roles", userAndRoles);

      // users from group members and roles
      final Map<String, String> userFromGroupsAndRoles = xmlData.getGroupsUsersMembership(project);
      assertNotNull("Aucune correspondance dans les csv entre users et roles", userFromGroupsAndRoles);

      final Map<String, String> roleCvsJenkins = xmlData.getAppMapping(project, appType);
      assertNotNull("Aucune correspondance dans les csv entre users et Jenkins", roleCvsJenkins);

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
      for (String user : userAndRoles.keySet())
      {
        final String roleDatabase = rolesDatabaseJenkins.get(user);
        assertNotNull("The tool role does not exist for the user " + user, roleDatabase);
        final String roleForge = userAndRoles.get(user);
        final String roleJenkinsForge = roleCvsJenkins.get(roleForge);
        manageACL.setIfACLError(user, roleDatabase.toLowerCase(), roleJenkinsForge.toLowerCase());
      }
      // check roles propagation into tool for users declared into group members
      for (String user : userFromGroupsAndRoles.keySet())
      {
        final String roleDatabase = rolesDatabaseJenkins.get(user);
        assertNotNull("The tool role does not exist for the user " + user, roleDatabase);
        final String roleForge = userFromGroupsAndRoles.get(user);
        final String roleJenkinsForge = roleCvsJenkins.get(roleForge);
        manageACL.setIfACLError(user, roleDatabase.toLowerCase(), roleJenkinsForge.toLowerCase());
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

  private Map<String, String> getRoleJenkins(final String projet) throws Exception
  {
    final Map<String, String> returned = new HashMap<String, String>();
    File                      fXmlFile = null;
    try
    {
      final HashMap<String, ArrayList<String>> permissions = new HashMap<String, ArrayList<String>>();
      final String hostName = getHostNameFromToolUrl(TestConstants.BUILDING_TYPE);
      final String configPath = TestConstants.JENKINS_JOBS_PATH + projet + "_" + TestConstants.JENKINS_DEFAULT_JOB_NAME;
      final String configFile = TestConstants.JENKINS_CONFIG_FILE_NAME;
      final String localTmpFileName = TestConstants.DIRECTORY_XML + "/" + TestConstants.JENKINS_CONFIG_FILE_NAME;

      // get the remote file and put it into a local file under karaf tmp
      getJenkinsSecurityXmlFile(hostName, configPath, configFile, localTmpFileName);

      fXmlFile = new File(localTmpFileName);
      final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      final Document doc = dBuilder.parse(fXmlFile);
      doc.getDocumentElement().normalize();
      final NodeList nList = doc.getElementsByTagName("permission");
      for (int temp = 0; temp < nList.getLength(); temp++)
      {
        final Node nNode = nList.item(temp);
        if (nNode.getNodeType() == Node.ELEMENT_NODE)
        {

          final Element eElement = (Element) nNode;
          final String permission = eElement.getTextContent();
          final StringTokenizer st = new StringTokenizer(permission, ":");
          final String permissionPart[] = new String[2];

          // iterate through tokens
          int i = 0;
          while (st.hasMoreTokens())
          {
            permissionPart[i] = st.nextToken();
            i++;
          }
          if (permissions.containsKey(permissionPart[1]))
          {
            // add a line to to the hashmap for this key
            permissions.get(permissionPart[1]).add(permissionPart[0]);
          }
          else
          {
            // create first the new entry into the hashmap
            permissions.put(permissionPart[1], new ArrayList<String>());
            permissions.get(permissionPart[1]).add(permissionPart[0]);
          }
        }
      }

      for (final String userId : permissions.keySet())
      {
        final int permNb = permissions.get(userId).size();
        /*
         * viewer: <permission>hudson.model.Item.Read:usertest6</permission>
         */
        if (permNb == 1)
        {
          returned.put(userId, "viewer");
        }
        /*
         * operator: <permission>hudson.model.Item.Workspace:usertest2</permission>
         * <permission>hudson.model.Item.Read:usertest2</permission>
         * <permission>hudson.model.Run.Update:usertest2</permission>
         * <permission>hudson.model.Run.Delete:usertest2</permission> 5 eme rajoutée:
         * <permission>hudson.model.Item.Build:usertest2</permission>
         */
        // patch v3.2 A confrmer: il y a 5 permissions
        if (permNb == 5)
        {
          returned.put(userId, "operator");
        }
        /*
         * Administrator: <permission>hudson.scm.SCM.Tag:usertest1</permission>
         * <permission>hudson.model.Item.Workspace:usertest1</permission>
         * <permission>hudson.model.Item.Delete:usertest1</permission>
         * <permission>hudson.model.Item.Configure:usertest1</permission>
         * <permission>hudson.model.Item.Read:usertest1</permission>
         * <permission>hudson.model.Item.Build:usertest1</permission>
         * <permission>hudson.model.Run.Delete:usertest1</permission>
         * <permission>hudson.model.Run.Update:usertest1</permission>
         */
        if (permNb == 8)
        {
          returned.put(userId, "administrator");
        }
      }
    }
    catch (final Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      if ((fXmlFile != null) && fXmlFile.exists())
      {
        fXmlFile.delete();
      }
    }
    return returned;
  }

  /*
   * get remote config.xml file from the default jenkins job for the project
   */
  private void getJenkinsSecurityXmlFile(final String pHostName, final String pRemotePath, final String pRemoteFile,
                                         final String pTmpLocalFileName) throws Exception
  {
    final String user     = TestConstants.ROOT_LOGIN;
    final String password = TestConstants.ROOT_PWD;
    final int    port     = TestConstants.SSH_PORT;

    ChannelSftp sftpChannel = null;
    Session     session     = null;
    InputStream  in           = null;
    OutputStream outputStream = null;
    try
    {
      final JSch jsch = new JSch();
      session = jsch.getSession(user, pHostName, port);
      session.setPassword(password);
      session.setConfig("StrictHostKeyChecking", "no");
      log.debug("Establishing Connection...");
      session.connect();
      log.debug("Connection established.");
      log.debug("Crating SFTP Channel.");
      sftpChannel = (ChannelSftp) session.openChannel("sftp");
      sftpChannel.connect();
      log.debug("SFTP Channel created.");

      // go from /root to /
      sftpChannel.cd("..");
      sftpChannel.cd(pRemotePath);
      in = sftpChannel.get(pRemoteFile);

      outputStream = new FileOutputStream(new File(pTmpLocalFileName));
      final BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null)
      {
        // System.out.println(line);
        outputStream.write(line.getBytes());
      }
      br.close();
      // System.out.println("Done!");
    }
    finally
    {
      if (sftpChannel != null)
      {
        sftpChannel.disconnect();
      }
      if (session != null)
      {
        session.disconnect();
      }
      if (in != null)
      {
        in.close();
      }
      if (outputStream != null)
      {
        outputStream.close();
      }
    }

  }

  public void test03LookProjectExist() throws Exception
  {
    File fXmlFile = null;
    ReportTest.writeTestResult(this.getClass().getName(), getName(), false);
    log.info("***************************************************************************");
    log.info("******************           LookProjectAtDatase  Jerkins      ********************");
    log.info("***************************************************************************");
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      try
      {
        String project = iterator.next();
        log.info(" projet = " + project);
        final String hostName = getHostNameFromToolUrl(TestConstants.BUILDING_TYPE);
        final String configPath = TestConstants.JENKINS_JOBS_PATH + project + "_"
            + TestConstants.JENKINS_DEFAULT_JOB_NAME;
        final String configFile = TestConstants.JENKINS_CONFIG_FILE_NAME;
        final String localTmpFileName = TestConstants.DIRECTORY_XML + "/"
            + TestConstants.JENKINS_CONFIG_FILE_NAME;

        // get the remote file and put it into a local file under karaf tmp
        getJenkinsSecurityXmlFile(hostName, configPath, configFile, localTmpFileName);

        // check config file has well been created into the default jenkins job
        fXmlFile = new File(localTmpFileName);
        assertTrue("config.xml file has not been added to karaf", fXmlFile.exists());
        assertTrue("config.xml file is not a file type", fXmlFile.isFile());
        assertTrue("length of config.xml file is leass than 10", fXmlFile.length() > 10);
      }
      finally
      {
        if ((fXmlFile != null) && fXmlFile.exists())
        {
          fXmlFile.delete();
        }
      }
    }
    ReportTest.writeTestResult(this.getClass().getName(), getName(), true);
  }

  public void test04LookAtUserJerkins() throws Exception
  {

    ReportTest.writeTestResult(this.getClass().getName(), getName(), false);
    for (String project : xmlData.getProjects().keySet())
    {
      log.info(" projet = " + project);

      final List<String> userJerkins = getUsersJenkins(project);
      assertNotNull("Aucune ser trouve dans le file de conf Jerkins", userJerkins);

      final Map<String, String> userAndRoles = xmlData.getUsersMembership(project);
      assertNotNull("Aucune correspondance dans les csv entre users et roles", userAndRoles);

      for (String user : userAndRoles.keySet())
      {
        assertTrue("le user n'existe pas dans Jerkins", userJerkins.contains(user));
      }
    }
    ReportTest.writeTestResult(this.getClass().getName(), getName(), true);
  }

  private List<String> getUsersJenkins(final String project) throws Exception
  {
    final List<String> returned = new ArrayList<String>();
    File               fXmlFile = null;
    try
    {
      final String hostName = getHostNameFromToolUrl(TestConstants.BUILDING_TYPE);
      final String configPath =
          TestConstants.JENKINS_JOBS_PATH + project + "_" + TestConstants.JENKINS_DEFAULT_JOB_NAME;
      final String configFile = TestConstants.JENKINS_CONFIG_FILE_NAME;
      final String localTmpFileName = TestConstants.DIRECTORY_XML + "/" + TestConstants.JENKINS_CONFIG_FILE_NAME;

      // get the remote file and put it into a local file under karaf tmp
      getJenkinsSecurityXmlFile(hostName, configPath, configFile, localTmpFileName);

      fXmlFile = new File(localTmpFileName);
      final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      final Document doc = dBuilder.parse(fXmlFile);
      doc.getDocumentElement().normalize();

      final NodeList nList = doc.getElementsByTagName("properties");

      for (int temp = 0; temp < nList.getLength(); temp++)
      {

        final Node nNode = nList.item(temp);
        if (nNode.getNodeType() == Node.ELEMENT_NODE)
        {

          final Element eElement = (Element) nNode;
          final NodeList rolesNodes = eElement.getElementsByTagName("permission"); // the tag name
          final int num = rolesNodes.getLength(); // length of list

          for (int i = 0; i < num; i++)
          {

            final Element roleAndUser = (Element) rolesNodes.item(i);
            final String[] tab = roleAndUser.getChildNodes().item(0).getNodeValue().split(":");
            final String user = tab[1];
            if (!returned.contains(user))
            {
              returned.add(user);
            }
          }
        }
      }
    }
    catch (final Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      // remove config.xml under karaf tmp
      if ((fXmlFile != null) && fXmlFile.exists())
      {
        fXmlFile.delete();
      }
    }
    return returned;
  }

  /*
   * testing propagation into Jenkins when changing forge role.
   */
  public void test05TestRoleChangingPropagationIntoJenkins() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), getName(), false);

    log.info("***************************************************************************");
    log.info("******************           Look Role change At Database  Jenkins      ********************");
    log.info("***************************************************************************");
    for (String project : xmlData.getProjects().keySet())
    {
      log.info(" projet = " + project);

      // changing role for the first user having "developpeur" role
      final String projectTestId = xmlData.getRoleChangeCondition().getProjectTestId();
      assertNotNull("le projectId pas trouve dans le csv", projectTestId);
      final String roleToChange = xmlData.getRoleChangeCondition().getRoleToChange();
      assertNotNull("le roleToChange pas trouve dans le csv", roleToChange);
      final String roleTarget = xmlData.getRoleChangeCondition().getRoleTarget();
      assertNotNull("le roleTarget pas trouve dans le csv", roleTarget);
      final String userIdToChange = xmlData.getRoleChangeCondition().getUserId();
      if (project.equals(projectTestId))
      {
        try
        {
          // changing role on the forge
          changeRoleForUser(projectTestId, userIdToChange, roleToChange, roleTarget);
          assertNotNull("Aucun user trouve avec le role = " + roleToChange, userIdToChange);
          Thread.currentThread();
          Thread.sleep(TestConstants.WAIT_CHANGE_ROLE_PROPAGATION);
          // check propagation into Jenkins Db
          checkPropagationIntoJenkinsDb(project, true, userIdToChange, roleTarget,
              TestConstants.BUILDING_TYPE);
        }
        finally
        {
          if (userIdToChange != null)
          {
            changeBackRoleForUser(projectTestId, userIdToChange, roleToChange);
            Thread.currentThread();
            Thread.sleep(TestConstants.WAIT_CHANGE_ROLE_PROPAGATION);
          }
        }
      }
    }
    ReportTest.writeTestResult(this.getClass().getName(), getName(), true);
  }

  /*
   * testing propagation into Jenkins when changing roles mappings of Jenkins application.
   */
  public void test06TestChangingRoleMappingPropagationIntoJenkins() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), getName(), false);
    log.info("***********************************************************************************");
    log.info("********* Test propagation when changing roles mapping for Jenkins *******************");
    log.info("***********************************************************************************");

    // Needed because of authorization checking
    securityManager.login(TestConstants.login, TestConstants.pwd);

    // Needed because of authorization checking
    for (String project : xmlData.getProjects().keySet())
    {
      log.info(" projet = " + project);

      final String app_name = xmlData.getApplicationName(project, TestConstants.BUILDING_TYPE);
      assertNotNull("Le nom d'appli non renseigne", app_name);

      final List<ProjectApplication> applications = applicationPresenter.getAllProjectApplications(project);
      assertNotNull("Aucune application trouve dans le projet", applications);

      String applicationUri = null;
      for (final ProjectApplication application : applications)
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
        final Map<String, String> newCvsRoleMapping = xmlData.getAppMapping(project,
            TestConstants.BUILDING_TYPE2);
        assertNotEquals("Aucun role trouve dans le fichier csv de changement de mapping", true,
            newCvsRoleMapping.isEmpty());
        updateApplication(project, applicationUri, newCvsRoleMapping, TestConstants.BUILDING_TYPE, app_name);

        Thread.currentThread();
        // wait until jms message has been executed by the plugin (mapping) and the tool (update of the
        // tool
        // roles)
        // may be could loop onto targeted tool roles to be more sure than an arbitrary time (?).
        Thread.sleep(TestConstants.WAIT_CHANGE_ROLEMAPPING_PROPAGATION);

        // check propagation according the new roles mapping
        checkPropagationIntoJenkinsDb(project, false, null, null, TestConstants.BUILDING_TYPE2);
      }
      // always done eveen getting failure
      finally
      {
        // setting back the roles mapping
        final Map<String, String> initialCvsRoleMapping = xmlData.getAppMapping(project,
            TestConstants.BUILDING_TYPE);
        assertNotEquals("Aucun role trouve dans le fichier csv initial de mapping", true,
            initialCvsRoleMapping.isEmpty());
        updateApplication(project, applicationUri, initialCvsRoleMapping, TestConstants.BUILDING_TYPE,
            app_name);
        Thread.currentThread();
        Thread.sleep(TestConstants.WAIT_CHANGE_ROLEMAPPING_PROPAGATION);
      }
    }
    if (securityManager.checkLogin())
    {
      securityManager.logout();
    }
    ReportTest.writeTestResult(this.getClass().getName(), getName(), true);
  }

  /*
   * testing propagation into Jenkins when changing the role of forge group for a group member
   */
  public void test07TestGroupRoleChangingPropagationIntoJenkins() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), getName(), false);
    log.info("*****************************************************************************");
    log.info("************** testing propagation when changing group role  ****************");
    log.info("*****************************************************************************");
    for (String project : xmlData.getProjects().keySet())
    {
      log.info(" projet = " + project);

      final String projectName = xmlData.getProjects().get(project);
      assertNotNull("le project pas trouve dans le csv", projectName);

      final List<String> userJenkins = getUsersJenkins(project);
      assertNotNull("Aucune user trouve dans le file de conf Jenkins", userJenkins);

      // get changing condition: new role for the group member of a given project
      final Map<String, List<String>> groupAndRoleschangingCondition = xmlData
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
        final Map<String, String> groupMemberships = xmlData.getGroupsMembership(project);
        for (final String group : groupMemberships.keySet())
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

          Thread.currentThread();
          // wait for propagation to tool.
          Thread.sleep(TestConstants.WAIT_CHANGE_ROLE_PROPAGATION);

          // check propagation into Jenkins Db
          checkGroupRoleChangingPropagation(project, groupId, newRole, TestConstants.BUILDING_TYPE);
        }
        finally
        {
          // back changing the role for the last found user.
          changeRoleForGroup(project, groupId, initialRole);
          Thread.currentThread();
          Thread.sleep(TestConstants.WAIT_CHANGE_ROLE_PROPAGATION);
        }

      }

    }
    ReportTest.writeTestResult(this.getClass().getName(), getName(), true);
  }

  private void checkGroupRoleChangingPropagation(final String project, final String groupId,
      final String newRole, final String app_type) throws Exception
  {
    try
    {
      // login
      securityManager.login(TestConstants.login, TestConstants.pwd);
      final ManageACL manageACL = new ManageACL();
      log.info(" projet = " + project);

      final Map<String, String> rolesDatabaseJenkins = getRoleJenkins(project);
      assertNotNull("Aucune role trouve dans la DB Jenkins", rolesDatabaseJenkins);

      final Map<String, String> roleCvsJenkins = xmlData.getAppMapping(project, app_type);
      assertNotNull("Aucune correspondance dans les csv entre users et Jenkins", roleCvsJenkins);

      // users from group members and roles
      final Map<String, String> userFromGroupsAndRoles = xmlData.getGroupsUsersMembership(project);
      assertNotNull("Aucune correspondance dans les csv entre users et roles", userFromGroupsAndRoles);

      // modify expected role for the given group
      assertNotNull("Le groupe donne pour permettre de modifier le role est null", groupId);
      assertNotEquals("Le groupe donne pour permettre de modifier le role est vide", "", groupId);
      assertNotNull("Le role cible est null", newRole);
      assertNotEquals("Le role cible est vide", "", newRole);
      for (final String user : userFromGroupsAndRoles.keySet())
      {
        userFromGroupsAndRoles.remove(user);
        userFromGroupsAndRoles.put(user, newRole);
      }

      // check roles propagation into tool for users declared into group members
      for (String user : userFromGroupsAndRoles.keySet())
      {
        final String roleDatabase = rolesDatabaseJenkins.get(user);
        assertNotNull("The tool role does not exist for the user " + user, roleDatabase);
        final String roleForge = userFromGroupsAndRoles.get(user);
        final String roleJenkinsForge = roleCvsJenkins.get(roleForge);
        manageACL.setIfACLError(user, roleDatabase.toLowerCase(), roleJenkinsForge.toLowerCase());
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
