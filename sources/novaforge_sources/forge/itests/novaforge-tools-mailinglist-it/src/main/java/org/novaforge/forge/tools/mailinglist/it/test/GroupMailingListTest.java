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
package org.novaforge.forge.tools.mailinglist.it.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.plugins.categories.Category;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListBean;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListGroup;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListServiceException;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListSubscriber;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListType;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.plugins.categories.beans.MailingListGroupImpl;
import org.novaforge.forge.plugins.categories.beans.MailingListUserImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author blachonm
 */

public class GroupMailingListTest extends MailingListBaseTests
{
  private static final Log LOG = LogFactory.getLog(GroupMailingListTest.class);
  private String           forgeId;
  private String           pluginId;
  private String           projectId;
  private String           pluginInstanceId;

  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    // need importing the xml file added under the resource folder
    projectId = xmlData.getProjects().keySet().iterator().next();

    // initiate pluginInstanceId and forgeId used for mailinglist API
    initForgeId();
    initPluginId();
    initPluginInstanceId(projectId);

    // work around after import because the users member of the project are not subscriber for the project
    // mailing list
    // check expected number of users are subscriber into the project mailing list
    final MailingListBean projectMailingListBeforeSetUp = getProjectMailingList();
    if (TestConstants.TOTAL_PROJECT_MEMBERS_NUMBER != projectMailingListBeforeSetUp.getSubscribers().size())
    {
      System.out.println("*** after import the project mailing list does not contain the right user number!");
      System.out.println("*** Execute a work around to get subscribers within the project mailing list.");
      try
      {
        // login
        login(true, null);

        // remove membership for user1 and user2 from the project
        final UUID user1UUID = userPresenter.getUser(TestConstants.USERTEST1LOGIN).getUuid();
        final UUID user2UUID = userPresenter.getUser(TestConstants.USERTEST2LOGIN).getUuid();

        membershipPresenter.removeUserMembership(projectId, user1UUID, false);
        membershipPresenter.removeUserMembership(projectId, user2UUID, false);
        Thread.currentThread();
        Thread.sleep(TestConstants.WAIT_PROPAGATION);
        // add membership
        final HashSet<String> userRole = new HashSet<String>();
        userRole.add(TestConstants.ROLE_ADDED_PROJECT_GROUP);
        membershipPresenter.addUserMembership(projectId, user1UUID, userRole,
            TestConstants.ROLE_ADDED_PROJECT_GROUP, false);
        Thread.currentThread();
        Thread.sleep(TestConstants.WAIT_PROPAGATION);
        membershipPresenter.addUserMembership(projectId, user2UUID, userRole,
            TestConstants.ROLE_ADDED_PROJECT_GROUP, false);
        Thread.currentThread();
        Thread.sleep(TestConstants.WAIT_PROPAGATION);
      }
      finally
      {
        // logout
        if (authentificationService.checkLogin())
        {
          authentificationService.logout();
        }
      }
    }

    // Check with assert (to raise a failure) before going on to the test case.
    final MailingListBean projectMailingListAfterSetUp = getProjectMailingList();
    assertEquals(
        "The project mailing list does not contain the right user number after the work around !!!!!!",
        TestConstants.TOTAL_PROJECT_MEMBERS_NUMBER, projectMailingListAfterSetUp.getSubscribers().size());
  }

  @Override
  public void tearDown() throws Exception
  {
    super.tearDown();
  }

  private void initForgeId() throws Exception
  {
    forgeId = forgeIdentificationService.getForgeId().toString();
    assertNotNull("The forge ID should not be null", forgeId);
  }

  private void initPluginId() throws Exception
  {
    final List<PluginMetadata> plugins = pluginsManager.getPluginsMetadataByCategory(Category.MAILINGLIST.getId());
    assertNotNull("Plugins list should at least contains MailingList", plugins);
    assertEquals(1, plugins.size());
    pluginId = plugins.get(0).getUUID();
    assertNotNull("The mailinglist pluginId has not been found", pluginId);
  }

  private void initPluginInstanceId(final String pMailingListProjectId) throws Exception
  {
    final List<ProjectApplication> applications = applicationService.getAllProjectApplications(pMailingListProjectId);
    for (final ProjectApplication application : applications)
    {
      final String currentPluginId = application.getPluginUUID().toString();
      if (currentPluginId.equalsIgnoreCase(pluginId))
      {
        pluginInstanceId = application.getPluginInstanceUUID().toString();
      }
    }
    assertNotNull("No pluginInstance exists for mailinglist application", pluginInstanceId);
  }

  private MailingListBean getProjectMailingList() throws MailingListServiceException, UnknownHostException
  {
    final List<MailingListBean> mailingLists = mailingListCategoryService.getMailingLists(forgeId, pluginInstanceId,
                                                                                          TestConstants.ADMIN1LOGIN);

    // assertTrue("there's more than the project mailing list When starting the test", mailingLists.size() ==
    // 1);
    // project mailing list name
    final String hostnameWithDomain = InetAddress.getLocalHost().getCanonicalHostName();

    // TODO: better to split with separator @ ....
    final String expectedProjectMailingListName = projectId + "-team@" + hostnameWithDomain;

    boolean         isProjectMailibgListFound = false;
    MailingListBean projectMailingList        = null;
    for (final MailingListBean mailingListBean : mailingLists)
    {
      final String mailingListName = mailingListBean.getName();
      if (expectedProjectMailingListName.equals(mailingListName))
      {
        isProjectMailibgListFound = true;
        projectMailingList = mailingListBean;
        break;
      }
    }
    assertTrue("project mailing list has not been found", isProjectMailibgListFound);
    return projectMailingList;
  }

  private void login(final boolean isSuperAdmin, final String pLogin) throws Exception
  {
    String login;
    if (isSuperAdmin)
    {
      login = forgeConfigurationService.getSuperAdministratorLogin();
    }
    else
    {
      login = pLogin;
    }
    final User user = userPresenter.getUser(login);
    authentificationService.login(login, user.getPassword());
  }

  public void test01TestMailingListProjectAfterProjectCreation() throws Exception
  {
    System.out.println("Executiong test01TestMailingListProjectAfterProjectCreation .....");
    checkMailinglistBeforeTest();
  }

  private void checkMailinglistBeforeTest() throws MailingListServiceException, UnknownHostException, Exception
  {
    final MailingListBean projectMailingList = getProjectMailingList();

    // mailing list description
    assertEquals(TestConstants.MAILINGLIST_DESCRIPTION, projectMailingList.getDescription());

    // check the project mailinglist
    assertEquals(TestConstants.MAILINGLIST_SUBJECT, projectMailingList.getSubject());

    // check Owners into mailing list
    final ArrayList<String> loginOwnersMailinList = new ArrayList<String>();
    for (final MailingListSubscriber mailingListSubscriber : projectMailingList.getOwners())
    {
      final MailingListUser mailingListUser = (MailingListUser) mailingListSubscriber;
      loginOwnersMailinList.add(mailingListUser.getLogin());
    }
    assertTrue("List creator is not declared as owner",
               loginOwnersMailinList.contains(TestConstants.LOGGED_USER_FOR_LIST_CREATION));

    // check subscribers for initial project mailing list
    checkProjectMailinglistSubscribersInitial(projectMailingList);

    // check propagation into Sympa by connecting to the DB.
    final List<String> subscribersList = getUsersSubscribtionPropagationForProjectMailingList(projectId);
    checkProjectMailingListPropagationIntoSympaInitial(subscribersList);
  }

  private void checkProjectMailinglistSubscribersInitial(final MailingListBean projectMailingList)
  {
    // check subscribers into mailing list
    final ArrayList<String> emailSubscribersMailinList = new ArrayList<String>();
    for (final MailingListSubscriber mailingListSubscriber : projectMailingList.getSubscribers())
    {
      final MailingListUser mailingListUser = (MailingListUser) mailingListSubscriber;
      emailSubscribersMailinList.add(mailingListUser.getEmail());
    }
    // check usertest1, usertest2 have been added to the project mailing list.
    // creator will be admin1 (super admin) and is not added to the project mailing list
    assertTrue("mailingList does not contain: usertest1 email",
               emailSubscribersMailinList.contains(TestConstants.USERTEST1EMAIL));
    assertTrue("mailingList does not contain: usertest2 email",
               emailSubscribersMailinList.contains(TestConstants.USERTEST2EMAIL));
  }

  private List<String> getUsersSubscribtionPropagationForProjectMailingList(final String pProject) throws Exception
  {
    final ArrayList<String> sympaUserSubscribers = new ArrayList<String>();
    final String            listSubscriber       = pProject + "-team";
    final Connection con = DriverManager.getConnection(TestConstants.DATABASE_SYMPA, TestConstants.USER,
                                                       TestConstants.PASSWORD);
    // select user_subscriber from subscriber_table where list_subscriber='marcprojet2-team';
    final String query = "select user_subscriber from subscriber_table where list_subscriber='" + listSubscriber + "'";
    final Statement stmt      = con.createStatement();
    final ResultSet resultSet = stmt.executeQuery(query);
    while (resultSet.next())
    {
      final String email = resultSet.getString(1);
      sympaUserSubscribers.add(email);
    }
    con.close();
    return sympaUserSubscribers;
  }

  private void checkProjectMailingListPropagationIntoSympaInitial(final List<String> subscribersList)
  {
    assertEquals(TestConstants.TOTAL_PROJECT_MEMBERS_NUMBER, subscribersList.size());
    // internal user (not super Admin)
    assertTrue("Error: the new subscriber email usertest1@bull.net has not been add.",
               subscribersList.contains("usertest1@bull.net"));
    assertTrue("Error: the new subscriber email usertest2@bull.net has not been add.",
               subscribersList.contains("usertest2@bull.net"));
  }

  public void test02TestProjectMailingListAffiliateDesaffiliateProjectGroup() throws Exception
  {
    /*------------------------------------------------------------------------------------------------
     *              project group with users external and internal to the project
     *------------------------------------------------------------------------------------------------*/
    final String usertest1Login = TestConstants.USERTEST1LOGIN;
    final String usertest14Login = TestConstants.USERTEST14LOGIN;
    final String roleProjectGroup = TestConstants.ROLE_ADDED_PROJECT_GROUP;
    final String projectGroupId = TestConstants.PROJECT_GROUPID;
    System.out.println("Executiong test02TestProjectMailingListAffiliateDesaffiliateProjectGroup .....");
    try
    {
      login(true, null);

      // check projectMailingList
      final MailingListBean projectMailingListBeforeTest = getProjectMailingList();
      assertEquals(TestConstants.TOTAL_PROJECT_MEMBERS_NUMBER, projectMailingListBeforeTest.getSubscribers()
          .size());

      // add the project group with internal and external user
      final ArrayList<String> userLoginList = new ArrayList<String>();
      // add intenal member already project member)
      userLoginList.add(usertest1Login);
      // add external member
      userLoginList.add(usertest14Login);
      addprojectGroup(projectId, projectGroupId, roleProjectGroup, userLoginList);
      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // check subscribers into mailing list
      final MailingListBean projectMailingList = getProjectMailingList();
      assertEquals(TestConstants.TOTAL_PROJECT_MEMBERS_NUMBER + 1, projectMailingList.getSubscribers().size());

      final ArrayList<String> emailSubscribersMailinList = new ArrayList<String>();
      for (final MailingListSubscriber mailingListSubscriber : projectMailingList.getSubscribers())
      {
        final MailingListUser mailingListUser = (MailingListUser) mailingListSubscriber;
        emailSubscribersMailinList.add(mailingListUser.getEmail());
      }
      // internal: no more added
      assertTrue("mailingList does not contain: usertest1 email",
          emailSubscribersMailinList.contains(TestConstants.USERTEST1EMAIL));
      // external: 1 user added
      assertTrue("mailingList does not contain: usertest14 email",
          emailSubscribersMailinList.contains(TestConstants.USERTEST14EMAIL));

      // check propagation into sympa DB: external user should be added
      final List<String> subscribersList = getUsersSubscribtionPropagationForProjectMailingList(projectId);
      assertEquals(TestConstants.TOTAL_PROJECT_MEMBERS_NUMBER + 1, subscribersList.size());

      // external user should be added
      assertTrue("Error: the new subscriber email usertest14@bull.net has not been add.",
          subscribersList.contains("usertest14@bull.net"));
      // internal user still exist
      assertTrue("Error: the new subscriber email usertest1@bull.net has not been add.",
          subscribersList.contains("usertest1@bull.net"));
    }
    finally
    {
      // delete the added group
      deleteProjectGroup(projectId, projectGroupId);

      // check subscribers for initial project mailing list
      final MailingListBean projectMailingList = getProjectMailingList();
      checkProjectMailinglistSubscribersInitial(projectMailingList);

      // check propagation into Sympa by connecting to the DB.
      final List<String> subscribersList = getUsersSubscribtionPropagationForProjectMailingList(projectId);
      checkProjectMailingListPropagationIntoSympaInitial(subscribersList);

      // the external user
      assertTrue("Error: the new subscriber email usertest14@bull.net has not been add.",
          !subscribersList.contains("usertest14@bull.net"));

      // logout
      if (authentificationService.checkLogin())
      {
        authentificationService.logout();
      }
    }
  }

  private void addprojectGroup(final String pProjectId, final String pProjectGroupId, final String pRoleProjectGroup,
                               final List<String> pListUserLogins) throws Exception
  {

    // create a new group for the project (not created within xml import because of limitation to 1 project
    // group with xml parsing )
    final Group group = groupPresenter.newGroup();
    group.setDescription("desc");
    group.setName(pProjectGroupId);
    group.setVisible(false);
    groupPresenter.createGroup(group, pProjectId);

    assertNotNull("added project group does not exist", groupPresenter.getGroup(pProjectId, pProjectGroupId));

    // affiliate this new forge group to the project with role: developpeur
    final HashSet<String> rolesname = new HashSet<String>();
    rolesname.add(pRoleProjectGroup);
    final UUID groupUUID = groupPresenter.getGroup(pProjectId, pProjectGroupId).getUuid();
    membershipPresenter.addGroupMembership(pProjectId, groupUUID, rolesname, "");

    // add a user to this new group (forge group)
    final Group groupFound = groupPresenter.getGroup(pProjectId, pProjectGroupId);
    for (final String login : pListUserLogins)
    {
      final User userToAdd = userPresenter.getUser(login);
      groupFound.addUser(userToAdd);
    }

    groupPresenter.updateGroup(pProjectId, pProjectGroupId, groupFound);

    Thread.currentThread();
    Thread.sleep(TestConstants.WAIT_PROPAGATION);
  }

  private void deleteProjectGroup(final String pProjectId, final String pProjectGroupId) throws Exception
  {

    // test cleaning:
    // 1) remove groupe membership: (developpeur) on forge group used into the project
    final HashSet<String> rolesname = new HashSet<String>();
    final UUID            groupUUID = groupPresenter.getGroup(pProjectId, pProjectGroupId).getUuid();
    membershipPresenter.updateGroupMembership(pProjectId, groupUUID, rolesname, "", false);
    Thread.currentThread();
    Thread.sleep(TestConstants.WAIT_PROPAGATION);

    // 2) remove user from project group
    final Group group = groupPresenter.getGroup(pProjectId, pProjectGroupId);
    group.clearUsers();
    groupPresenter.updateGroup(pProjectId, pProjectGroupId, group);
    final Group groupCheck = groupPresenter.getGroup(pProjectId, pProjectGroupId);
    assertTrue("The added group forge should no have user after cleaning", groupCheck.getUsers().size() == 0);
    Thread.currentThread();
    Thread.sleep(TestConstants.WAIT_PROPAGATION);

    // 3) remove added project group
    LOG.debug("************** deleting groupID= " + pProjectGroupId + " group UUID= " + groupUUID + " for projectID = "
                  + pProjectId);
    groupPresenter.deleteGroup(pProjectId, groupUUID);
    Thread.currentThread();
    Thread.sleep(TestConstants.WAIT_PROPAGATION);
  }

  public void test03TestProjectMailingListAffiliateDesaffiliateForgeGroup() throws Exception
  {
    /*------------------------------------------------------------------------------------------------
     *              forge group with users external
     *------------------------------------------------------------------------------------------------*/
    final String usertest1Login = TestConstants.USERTEST1LOGIN;
    final String usertest14Login = TestConstants.USERTEST14LOGIN;
    final String roleForgeGroup = TestConstants.ROLE_FORGE_GROUP;
    String forgeGroupId = TestConstants.FORGE_GROUPID;
    System.out.println("Executiong test03TestProjectMailingListAffiliateDesaffiliateForgeGroup .....");
    try
    {
      login(true, null);

      // check projectMailingList
      final MailingListBean projectMailingListBeforeTest = getProjectMailingList();
      assertEquals(TestConstants.TOTAL_PROJECT_MEMBERS_NUMBER, projectMailingListBeforeTest.getSubscribers()
          .size());

      final ArrayList<String> userLoginList = new ArrayList<String>();
      // add intenal member already project member)
      userLoginList.add(usertest1Login);
      // ad external member
      userLoginList.add(usertest14Login);

      assertNotNull("forge group does not exist",
          groupPresenter.getGroup(TestConstants.FORGE_PROJECTID, forgeGroupId));

      // add membership
      addMembershipForForgeGroupWithExternalUser(projectId, forgeGroupId, roleForgeGroup, userLoginList);

      // check projectMailingList
      final MailingListBean projectMailingList = getProjectMailingList();
      assertEquals(TestConstants.TOTAL_PROJECT_MEMBERS_NUMBER + 1, projectMailingList.getSubscribers().size());

      // check subscribers into mailing list
      final ArrayList<String> emailSubscribersMailinList = new ArrayList<String>();
      assertEquals(TestConstants.TOTAL_PROJECT_MEMBERS_NUMBER + 1, projectMailingList.getSubscribers().size());
      for (final MailingListSubscriber mailingListSubscriber : projectMailingList.getSubscribers())
      {
        final MailingListUser mailingListUser = (MailingListUser) mailingListSubscriber;
        emailSubscribersMailinList.add(mailingListUser.getEmail());
      }
      // internal: no more added
      assertTrue("mailingList does not contain: usertest1 email",
          emailSubscribersMailinList.contains(TestConstants.USERTEST1EMAIL));
      // external: 1 user added
      assertTrue("mailingList does not contain: usertest14 email",
          emailSubscribersMailinList.contains(TestConstants.USERTEST14EMAIL));

      // check propagation into sympa DB: external user should be added
      final List<String> subscribersList = getUsersSubscribtionPropagationForProjectMailingList(projectId);
      assertEquals(TestConstants.TOTAL_PROJECT_MEMBERS_NUMBER + 1, subscribersList.size());

      // external user should be added
      assertTrue("Error: the new subscriber email usertest14@bull.net has not been add.",
          subscribersList.contains("usertest14@bull.net"));
      // internal user still exist
      assertTrue("Error: the new subscriber email usertest1@bull.net has not been add.",
          subscribersList.contains("usertest1@bull.net"));
    }
    finally
    {
      // delete the added membership for forge group, and clean the forge group
      deleteMembershipForForgeGroup(projectId, forgeGroupId);

      // check subscribers for initial project mailing list
      final MailingListBean projectMailingList = getProjectMailingList();
      checkProjectMailinglistSubscribersInitial(projectMailingList);

      // check propagation into Sympa by connecting to the DB.
      final List<String> subscribersList = getUsersSubscribtionPropagationForProjectMailingList(projectId);
      checkProjectMailingListPropagationIntoSympaInitial(subscribersList);

      // the external user
      assertTrue("Error: the new subscriber email usertest14@bull.net has not been add.",
          !subscribersList.contains("usertest14@bull.net"));

      // logout
      if (authentificationService.checkLogin())
      {
        authentificationService.logout();
      }
    }
  }

  private void addMembershipForForgeGroupWithExternalUser(final String pProjectId, final String pForgeGroupId,
                                                          final String pRoleForgeGroup,
                                                          final List<String> pListUserLogins) throws Exception
  {
    final String forgeId = TestConstants.FORGE_PROJECTID;

    // affiliate this new forge group to the project with role: developpeur
    final HashSet<String> rolesname = new HashSet<String>();
    rolesname.add(pRoleForgeGroup);
    final UUID groupUUID = groupPresenter.getGroup(forgeId, pForgeGroupId).getUuid();
    membershipPresenter.addGroupMembership(pProjectId, groupUUID, rolesname, "");

    // add a user to this new group (forge group)
    final Group groupFound = groupPresenter.getGroup(forgeId, pForgeGroupId);
    for (final String login : pListUserLogins)
    {
      final User userToAdd = userPresenter.getUser(login);
      groupFound.addUser(userToAdd);
    }
    groupPresenter.updateGroup(forgeId, pForgeGroupId, groupFound);

    Thread.currentThread();
    Thread.sleep(TestConstants.WAIT_PROPAGATION);

  }

  private void deleteMembershipForForgeGroup(final String pProjectId, final String pForgeGroupId) throws Exception
  {
    // test cleaning:
    // 1) remove groupe membership: (developpeur) on forge group used into the project
    final HashSet<String> rolesname = new HashSet<String>();
    final UUID            groupUUID = groupPresenter.getGroup(TestConstants.FORGE_PROJECTID, pForgeGroupId).getUuid();
    membershipPresenter.updateGroupMembership(pProjectId, groupUUID, rolesname, "", false);

    // 2) remove user fom forge group
    final Group group = groupPresenter.getGroup(TestConstants.FORGE_PROJECTID, pForgeGroupId);
    group.clearUsers();
    groupPresenter.updateGroup(TestConstants.FORGE_PROJECTID, pForgeGroupId, group);
    final Group groupCheck = groupPresenter.getGroup(TestConstants.FORGE_PROJECTID, pForgeGroupId);
    assertTrue("The added group forge should no have user after cleaning", groupCheck.getUsers().size() == 0);
  }

  public void test04TestNewMailingListSubscribeProjectGroupDeleteGroup() throws Exception
  /*------------------------------------------------------------------------------------------------
   *      Testing new mailing list when adding and deleting project group
   *      (with external and internal users to the project, and getting users both subscribed
   *      as user or within a project group)
   *
   *              - creation of a new mailing list (different from project mailing list)
   *              - add 2 users subscription:
   *                usertest2 (internal), usertest15 (external)
   *              - adding group subscription with 4 users:
   *               (usertest1,usertest2/internal, usertest15, usertest14/external
   *              - check propagation after group subscription
   *              - remove the project group
   *              - check propagation
   *              - close the mailing list,
   *------------------------------------------------------------------------------------------------*/
  {
    final String roleProjectGroup = TestConstants.ROLE_ADDED_PROJECT_GROUP;
    final String projectGroupId = TestConstants.PROJECT_GROUPID;
    String mailingListName = "";
    System.out.println("Executiong test04TestNewMailingListSubscribeProjectGroupDeleteGroup .....");
    try
    {
      login(true, null);

      // add project group with internal and external user
      final ArrayList<String> userLoginList = new ArrayList<String>();
      // add internal members
      userLoginList.add(TestConstants.USERTEST1LOGIN);
      userLoginList.add(TestConstants.USERTEST2LOGIN);
      // add external member
      userLoginList.add(TestConstants.USERTEST14LOGIN);
      userLoginList.add(TestConstants.USERTEST15LOGIN);

      // project group creation
      addprojectGroup(projectId, projectGroupId, roleProjectGroup, userLoginList);

      final Group projectGroup = groupPresenter.getGroup(projectId, projectGroupId);
      final UUID groupUUID = projectGroup.getUuid();

      // create the mailing list
      final MailingListBean mailingList1 = mailingListCategoryService.newMailingList();

      // set mailing list name with prefix and a timestamp
      mailingListName = getTimeStampedMailingListName();
      mailingList1.setName(mailingListName);
      mailingList1.setDescription(TestConstants.MAILINGLIST_DESCRIPTION);
      mailingList1.setSubject(TestConstants.MAILINGLIST1SUBJECT);
      mailingList1.setType(MailingListType.PUBLIC_LIST);

      // ****************** create the mailing list !!!!!!!!!!!!!!!!!!!!!!
      mailingListCategoryService.createMailingList(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingList1);
      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // create the mailinglist group mapped to the created project group (based on UUID)
      final MailingListGroup mailingListGroup1 = new MailingListGroupImpl();
      mailingListGroup1.setName(projectGroupId);
      mailingListGroup1.setUUID(groupUUID);

      // create mailing list group associated to the projectGroup (same UUID)
      final List<MailingListUser> subscribersToAdd = new LinkedList<MailingListUser>();
      for (final User user : projectGroup.getUsers())
      {
        final MailingListUser mailListUser1 = new MailingListUserImpl(user.getLogin(), user.getEmail());
        subscribersToAdd.add(mailListUser1);
      }
      mailingListGroup1.setMembers(subscribersToAdd);

      final LinkedList<MailingListGroup> mailingListGroups = new LinkedList<MailingListGroup>();
      mailingListGroups.add(mailingListGroup1);

      // subscribe users to the new mailing list
      final LinkedList<MailingListUser> mailingListUsers = new LinkedList<MailingListUser>();
      mailingListUsers
          .add(new MailingListUserImpl(TestConstants.USERTEST2LOGIN, TestConstants.USERTEST2EMAIL));

      mailingListUsers.add(new MailingListUserImpl(TestConstants.USERTEST15LOGIN,
          TestConstants.USERTEST15EMAIL));

      // ************* subscribe users: user2test-u et user15test-u to the mailing list **********
      mailingListCategoryService.addSubscribers(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingListName, mailingListUsers, false);

      // ************** subscribe the project group to the new mailing list *********************
      mailingListCategoryService.addGroupsSubscriptions(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingListName, mailingListGroups, false);
      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // check the created mailing list
      final MailingListBean newMailingListBean = getMailingList(mailingListName);
      assertNotNull("The created mailing list has not been found", newMailingListBean);

      // check propagation into sympa DB: internal and external user should be added
      checkNewMailingListPropagationIntoSympaAfterGroupAdded(mailingListName);

      // check subscribers has been added to the mailing list (group or user type)
      checkSubscribersWithAddedProjectGroup(newMailingListBean, projectGroupId);

    }
    finally
    {
      deleteProjectGroupAndCheck(mailingListName, projectGroupId);
      closeMailingListAndCheck(mailingListName);
      if (authentificationService.checkLogin())
      {
        authentificationService.logout();
      }
    }
  }

  private String getTimeStampedMailingListName()
  {
    String          mailingListName;
    final Calendar  calendar         = Calendar.getInstance();
    final Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
    mailingListName = TestConstants.MAILING_LIST_NAME_PREFIX + currentTimestamp.toString().replaceAll(" ", "-")
                                                                               .replaceAll(":", "-");
    return mailingListName;
  }

  private MailingListBean getMailingList(final String forgeMailingListName)
      throws MailingListServiceException, UnknownHostException
  {
    final List<MailingListBean> mailingLists = mailingListCategoryService.getMailingLists(forgeId, pluginInstanceId,
                                                                                          TestConstants.ADMIN1LOGIN);

    // assertTrue("there's more than the project mailing list When starting the test", mailingLists.size() ==
    // 1);
    // project mailing list name
    final String hostnameWithDomain = InetAddress.getLocalHost().getCanonicalHostName();
    // TODO: better to split with separator @ ....
    final String expectedMailingListName = forgeMailingListName + "@" + hostnameWithDomain;

    MailingListBean newMailingList = null;
    for (final MailingListBean mailingListBean : mailingLists)
    {
      final String mailingListName = mailingListBean.getName();
      if (expectedMailingListName.equals(mailingListName))
      {
        newMailingList = mailingListBean;
        break;
      }
    }
    return newMailingList;
  }

  private void checkNewMailingListPropagationIntoSympaAfterGroupAdded(final String pMailingListName) throws Exception
  {
    final List<String> subscribersList = getUsersSubscribtionPropagationForMailingList(pMailingListName);
    assertEquals(TestConstants.TOTAL_SYMPA_NEW_MAILING_SUSCRIBERS_NUMBER, subscribersList.size());

    // mailing list creator
    assertTrue("Error: the email for the creator of the mailing list has not been added",
               subscribersList.contains(TestConstants.ADMIN1EMAIL));
    // external user should be added
    assertTrue("Error: the  subscriber has not been add.", subscribersList.contains(TestConstants.USERTEST14EMAIL));
    assertTrue("Error: the  subscriber has not been add.", subscribersList.contains(TestConstants.USERTEST15EMAIL));
    // internal user still exist
    assertTrue("Error: the  subscriberhas not been add.", subscribersList.contains(TestConstants.USERTEST1EMAIL));
    assertTrue("Error: the  subscriber has not been add.", subscribersList.contains(TestConstants.USERTEST2EMAIL));
  }

  private void checkSubscribersWithAddedProjectGroup(final MailingListBean newMailingListBean, final String pGroupId)
  {
    final ArrayList<String> groupLoginsList = new ArrayList<String>();
    final ArrayList<String> userEmailsList  = new ArrayList<String>();
    final ArrayList<String> userLoginsList  = new ArrayList<String>();
    assertEquals(TestConstants.TOTAL_MAILING_LIST_SUBSCRIBERS, newMailingListBean.getSubscribers().size());
    for (final MailingListSubscriber mailingListSubscriber : newMailingListBean.getSubscribers())
    {
      // check for the group type subscriber
      if (mailingListSubscriber instanceof MailingListGroupImpl)
      {
        final MailingListGroup mailingListGroup = (MailingListGroup) mailingListSubscriber;
        assertTrue("The group subscriber does not exist", pGroupId.equals(mailingListGroup.getName()));
        final List<MailingListUser> userMembers = mailingListGroup.getMembers();
        for (final MailingListUser mailingListUser : userMembers)
        {
          groupLoginsList.add(mailingListUser.getLogin());
        }
      }

      // check for user type subscriber
      if (mailingListSubscriber instanceof MailingListUserImpl)
      {
        userEmailsList.add(((MailingListUserImpl) mailingListSubscriber).getEmail());
        userLoginsList.add(((MailingListUserImpl) mailingListSubscriber).getLogin());
      }

    }

    LOG.debug("***********After project group added, groupEmailsList= " + groupLoginsList.toString());
    LOG.debug("***********After project group added, userEmailsList ***********************************"
                  + userEmailsList.toString());
    LOG.debug("*********** After project group added, userLoginsList ***********************************"
                  + userLoginsList.toString());

    // expected results:
    // After project group added, groupEmailsList= [usertest2-u, usertest15-u, usertest14-u, usertest1-u]
    // After project group added, userEmailsList
    // ***********************************[novaforge-admin@bmt.bull.net, usertest2@bull.net,
    // usertest15@bull.net]
    // After project group added, userLoginsList ***********************************[admin1, usertest2-u,
    // usertest15-u]

    // check with asserts:
    assertEquals("the subscriber type group has not the right number of users",
                 TestConstants.TOTAL_PROJECT_GROUP_MEMBERS, groupLoginsList.size());
    assertTrue("mailingList does not contain: usertest14-u login",
               groupLoginsList.contains(TestConstants.USERTEST14LOGIN));
    assertTrue("mailingList does not contain: usertest1-u login",
               groupLoginsList.contains(TestConstants.USERTEST1LOGIN));
    assertTrue("mailingList does not contain: usertest2-u login",
               groupLoginsList.contains(TestConstants.USERTEST2LOGIN));
    assertTrue("mailingList does not contain: usertest15-u login",
               groupLoginsList.contains(TestConstants.USERTEST15LOGIN));

    // check all users subscribers users
    assertEquals("the subscriber type group has not the right number of users",
                 TestConstants.TOTAL_MAILING_LIST_USER_SUBSCRIBERS, userLoginsList.size());
    assertTrue("mailingList does not contain: admin1 login", userLoginsList.contains(TestConstants.ADMIN1LOGIN));
    assertTrue("mailingList does not contain: usertest15-u login",
               userLoginsList.contains(TestConstants.USERTEST15LOGIN));
    assertTrue("mailingList does not contain: usertest2-u login",
               userLoginsList.contains(TestConstants.USERTEST2LOGIN));

  }

  // -------------------------- Private functions --------------------------------------------------------
  // -------------------------- Private functions --------------------------------------------------------
  // -------------------------- Private functions --------------------------------------------------------
  private void deleteProjectGroupAndCheck(final String pMailingListName, final String pProjectGroupId) throws Exception
  {
    // remove group subscription
    deleteProjectGroup(projectId, pProjectGroupId);

    // check the users subscriptions already exist for user2test and user15test
    final MailingListBean newMailingListBean = getMailingList(pMailingListName);
    checkSubscribersAfterProjectGroupRemoval(newMailingListBean);

    // check propagation into Sympa for the new mailing list: usertest2 and usertest15 should be kept !!!!
    final List<String> subscribersList = getUsersSubscribtionPropagationForMailingList(pMailingListName);
    LOG.debug("**************** After group removal subscribersList into Sympa= " + subscribersList.toString());
    checkNewMailingListPropagationIntoSympaAfterGroupRemoval(subscribersList);
  }

  private void closeMailingListAndCheck(final String pMailingListName) throws Exception
  {

    // *************** close the mailing list ****************************************
    mailingListCategoryService.closeMailingList(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN, pMailingListName);

    Thread.currentThread();
    Thread.sleep(TestConstants.WAIT_PROPAGATION);

    final boolean isExisting = mailingListCategoryService.existMailingList(forgeId, pluginInstanceId,
                                                                           TestConstants.ADMIN1LOGIN, pMailingListName);
    assertTrue("The closed mailing list is still existing", isExisting);

    // check closed status propagation into Sympa
    final String status = getMailingListStatusIntoSympaDB(pMailingListName);
    assertEquals("The status of the mailing list after closing it in not closed into Sympa", "closed", status);
  }

  private List<String> getUsersSubscribtionPropagationForMailingList(final String pMailingListName) throws Exception
  {
    final ArrayList<String> sympaUserSubscribers = new ArrayList<String>();
    final Connection con = DriverManager.getConnection(TestConstants.DATABASE_SYMPA, TestConstants.USER,
                                                       TestConstants.PASSWORD);
    // select user_subscriber from subscriber_table where list_subscriber='marcprojet2-team';
    final String query =
        "select user_subscriber from subscriber_table where list_subscriber='" + pMailingListName + "'";
    final Statement stmt      = con.createStatement();
    final ResultSet resultSet = stmt.executeQuery(query);
    while (resultSet.next())
    {
      final String email = resultSet.getString(1);
      sympaUserSubscribers.add(email);
    }
    con.close();
    return sympaUserSubscribers;
  }

  private void checkSubscribersAfterProjectGroupRemoval(final MailingListBean newMailingListBean)
  {
    final ArrayList<String> userEmailsList = new ArrayList<String>();
    final ArrayList<String> userLoginsList = new ArrayList<String>();

    assertEquals(TestConstants.TOTAL_MAILING_LIST_SUBSCRIBERS_AFTER_GROUP_REMOVAL,
                 newMailingListBean.getSubscribers().size());
    for (final MailingListSubscriber mailingListSubscriber : newMailingListBean.getSubscribers())
    {
      if (mailingListSubscriber instanceof MailingListGroupImpl)
      {
        fail("no group subscriber should exist after project group removal");
      }
      if (mailingListSubscriber instanceof MailingListUserImpl)
      {
        userEmailsList.add(((MailingListUserImpl) mailingListSubscriber).getEmail());
        userLoginsList.add(((MailingListUserImpl) mailingListSubscriber).getLogin());
      }
    }

    LOG.debug("***********After group removal userEmailsList ***********************************" + userEmailsList
                                                                                                        .toString());
    LOG.debug("*********** After group removal userLoginsList ***********************************" + userLoginsList
                                                                                                         .toString());

    // check email@ and logins exist for users: usertest2, usertest15, admin1
    assertTrue("mailingList does not contain: usertest2 email", userEmailsList.contains(TestConstants.USERTEST2EMAIL));
    assertTrue("mailingList does not contain: usertest2 login", userLoginsList.contains(TestConstants.USERTEST2LOGIN));

    assertTrue("mailingList does not contain: usertest15 email",
               userEmailsList.contains(TestConstants.USERTEST15EMAIL));
    assertTrue("mailingList does not contain: usertest15 login",
               userLoginsList.contains(TestConstants.USERTEST15LOGIN));

    assertTrue("mailingList does not contain: admin1 email", userEmailsList.contains(TestConstants.ADMIN1EMAIL));
    assertTrue("mailingList does not contain: admin1 login", userLoginsList.contains(TestConstants.ADMIN1LOGIN));

  }

  private void checkNewMailingListPropagationIntoSympaAfterGroupRemoval(final List<String> subscribersList)
  {
    // For debugging
    LOG.debug("************* checkNewMailingListPropagationIntoSympaAfterGroupRemoval:subscribersList= "
                  + subscribersList.toString());
    assertEquals(TestConstants.TOTAL_MAILING_LIST_SUBSCRIBERS_AFTER_GROUP_REMOVAL, subscribersList.size());

    // check usertest2, usertest15, admin1 has been kept
    assertTrue("Error: the new subscriber email usertest2@bull.net has not been kept.",
               subscribersList.contains("usertest2@bull.net"));

    assertTrue("Error: the new subscriber email usertest15@bull.net has not been kept.",
               subscribersList.contains("usertest15@bull.net"));

    assertTrue("Error: the new subscriber email novaforge-admin@bmt.bull.net has not been kept.",
               subscribersList.contains("novaforge-admin@bmt.bull.net"));

    // check usertest1 & usertest14 has been removed
    assertFalse("Error: the new subscriber email usertest14@bull.net has not removed.",
                subscribersList.contains("usertest14@bull.net"));
    assertFalse("Error: the new subscriber email usertest1@bull.net has not removed.",
                subscribersList.contains("usertest1@bull.net"));
  }

  private String getMailingListStatusIntoSympaDB(final String pMailingListName) throws Exception
  {
    String       status          = "";
    final Connection con = DriverManager.getConnection(TestConstants.DATABASE_SYMPA, TestConstants.USER,
                                                       TestConstants.PASSWORD);
    // select user_subscriber from subscriber_table where list_subscriber='marcprojet2-team';
    final String query = "select status_list from list_table where name_list='" + pMailingListName + "'";
    final Statement stmt      = con.createStatement();
    final ResultSet resultSet = stmt.executeQuery(query);
    while (resultSet.next())
    {
      status = resultSet.getString(1);
    }

    con.close();
    return status;
  }

  public void test05TestNewMailingListSubscribeProjectGroupUnsuscribeGroup() throws Exception
  /*------------------------------------------------------------------------------------------------
   *      Testing new mailing list when adding and deleting project group
   *      (with external and internal users to the project, and getting users both subscribed
   *      as user or within a project group)
   *
   *              - creation of a new mailing list (different from project mailing list)
   *              - add 2 users subscription:
   *                usertest2 (internal), usertest15 (external)
   *              - adding group subscription with 4 users:
   *               (usertest1,usertest2/internal, usertest15, usertest14/external
   *              - check propagation after subscription
   *              - remove group subscription
   *              - check propagation after removal of the group subscription
   *              - remove the project group close the mailing list,
   *------------------------------------------------------------------------------------------------*/
  {
    final String roleProjectGroup = TestConstants.ROLE_ADDED_PROJECT_GROUP;
    final String projectGroupId = TestConstants.PROJECT_GROUPID;
    String mailingListName = "";
    System.out.println("Executiong test05TestNewMailingListSubscribeProjectGroupUnsuscribeGroup .....");
    try
    {
      login(true, null);

      // add project group with internal and external user
      final ArrayList<String> userLoginList = new ArrayList<String>();
      // add internal members
      userLoginList.add(TestConstants.USERTEST1LOGIN);
      userLoginList.add(TestConstants.USERTEST2LOGIN);
      // add external member
      userLoginList.add(TestConstants.USERTEST14LOGIN);
      userLoginList.add(TestConstants.USERTEST15LOGIN);

      // project group creation
      addprojectGroup(projectId, projectGroupId, roleProjectGroup, userLoginList);

      final Group projectGroup = groupPresenter.getGroup(projectId, projectGroupId);
      final UUID groupUUID = projectGroup.getUuid();

      // create the mailing list
      final MailingListBean mailingList1 = mailingListCategoryService.newMailingList();

      // set mailing list name with prefix and a timestamp
      mailingListName = getTimeStampedMailingListName();
      mailingList1.setName(mailingListName);
      mailingList1.setDescription(TestConstants.MAILINGLIST_DESCRIPTION);
      mailingList1.setSubject(TestConstants.MAILINGLIST1SUBJECT);
      mailingList1.setType(MailingListType.PUBLIC_LIST);

      // ****************** create the mailing list !!!!!!!!!!!!!!!!!!!!!!
      mailingListCategoryService.createMailingList(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingList1);
      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // create the mailinglist group mapped to the created project group (based on UUID)
      final MailingListGroup mailingListGroup1 = new MailingListGroupImpl();
      mailingListGroup1.setName(projectGroupId);
      mailingListGroup1.setUUID(groupUUID);

      // create mailing list group associated to the projectGroup (same UUID)
      final List<MailingListUser> subscribersToAdd = new LinkedList<MailingListUser>();
      for (final User user : projectGroup.getUsers())
      {
        final MailingListUser mailListUser1 = new MailingListUserImpl(user.getLogin(), user.getEmail());
        subscribersToAdd.add(mailListUser1);
      }
      mailingListGroup1.setMembers(subscribersToAdd);

      final LinkedList<MailingListGroup> mailingListGroups = new LinkedList<MailingListGroup>();
      mailingListGroups.add(mailingListGroup1);

      // subscribe users to the new mailing list
      final LinkedList<MailingListUser> mailingListUsers = new LinkedList<MailingListUser>();
      mailingListUsers
          .add(new MailingListUserImpl(TestConstants.USERTEST2LOGIN, TestConstants.USERTEST2EMAIL));

      mailingListUsers.add(new MailingListUserImpl(TestConstants.USERTEST15LOGIN,
          TestConstants.USERTEST15EMAIL));

      // ************* subscribe users: user2test-u et user15test-u to the mailing list **********
      mailingListCategoryService.addSubscribers(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingListName, mailingListUsers, false);

      // ************** subscribe the project group to the new mailing list *********************
      mailingListCategoryService.addGroupsSubscriptions(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingListName, mailingListGroups, false);
      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // check the created mailing list
      final MailingListBean newMailingListBean = getMailingList(mailingListName);
      assertNotNull("The created mailing list has not been found", newMailingListBean);

      // check propagation into sympa DB: internal and external user should be added
      checkNewMailingListPropagationIntoSympaAfterGroupAdded(mailingListName);

      // remove group subscription
      mailingListCategoryService.removeGroupSubscription(forgeId, pluginInstanceId,
          TestConstants.ADMIN1LOGIN, mailingListName, mailingListGroup1, false);
      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // check the users subscriptions already exist for user2test and user15test
      final MailingListBean mailingListBeanAfterGroupSubscriptionRemove = getMailingList(mailingListName);
      checkSubscribersAfterProjectGroupRemoval(mailingListBeanAfterGroupSubscriptionRemove);

      // check propagation into Sympa : usertest2 and usertest15 should be kept !!!!
      final List<String> subscribersListAfterGroupSubscriptionRemove = getUsersSubscribtionPropagationForMailingList(mailingListName);
      LOG.debug("After group removal subscribersList into Sympa= "
          + subscribersListAfterGroupSubscriptionRemove.toString());
      checkNewMailingListPropagationIntoSympaAfterGroupRemoval(subscribersListAfterGroupSubscriptionRemove);

    }
    finally
    {
      deleteProjectGroupAndCheck(mailingListName, projectGroupId);
      closeMailingListAndCheck(mailingListName);
      if (authentificationService.checkLogin())
      {
        authentificationService.logout();
      }
    }
  }

  public void test06TestNewMailingListSubscribeProjectGroupUpdateGroup() throws Exception
  /*------------------------------------------------------------------------------------------------
   *      Testing new mailing list when adding and deleting project group
   *      (with external and internal users to the project, and getting users both subscribed
   *      as user or within a project group)
   *
   *              - creation of a new mailing list (different from project mailing list)
   *              - add 2 users subscription:
   *                usertest2 (internal), usertest15 (external)
   *              - adding group subscription with 4 users:
   *               (usertest1,usertest2/internal, usertest15, usertest14/external
   *              - check propagation after subscription
   *              - remove group subscription
   *              - check propagation after removal of the group subscription
   *              - remove the project group close the mailing list,
   *------------------------------------------------------------------------------------------------*/
  {
    final String roleProjectGroup = TestConstants.ROLE_ADDED_PROJECT_GROUP;
    final String projectGroupId = TestConstants.PROJECT_GROUPID;
    String mailingListName = "";
    System.out.println("Executiong test06TestNewMailingListSubscribeProjectGroupUpdateGroup .....");
    try
    {
      login(true, null);

      // add project group with internal and external user
      final ArrayList<String> userLoginList = new ArrayList<String>();
      // add internal members
      userLoginList.add(TestConstants.USERTEST1LOGIN);
      userLoginList.add(TestConstants.USERTEST2LOGIN);
      // add external member
      userLoginList.add(TestConstants.USERTEST14LOGIN);
      userLoginList.add(TestConstants.USERTEST15LOGIN);

      // project group creation
      addprojectGroup(projectId, projectGroupId, roleProjectGroup, userLoginList);

      final Group projectGroup = groupPresenter.getGroup(projectId, projectGroupId);
      final UUID groupUUID = projectGroup.getUuid();

      // create the mailing list
      final MailingListBean mailingList1 = mailingListCategoryService.newMailingList();

      // set mailing list name with prefix and a timestamp
      mailingListName = getTimeStampedMailingListName();
      mailingList1.setName(mailingListName);
      mailingList1.setDescription(TestConstants.MAILINGLIST_DESCRIPTION);
      mailingList1.setSubject(TestConstants.MAILINGLIST1SUBJECT);
      mailingList1.setType(MailingListType.PUBLIC_LIST);

      // ****************** create the mailing list !!!!!!!!!!!!!!!!!!!!!!
      mailingListCategoryService.createMailingList(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingList1);
      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // create the mailinglist group mapped to the created project group (based on UUID)
      final MailingListGroup mailingListGroup1 = new MailingListGroupImpl();
      mailingListGroup1.setName(projectGroupId);
      mailingListGroup1.setUUID(groupUUID);

      // create mailing list group associated to the projectGroup (same UUID)
      final List<MailingListUser> subscribersToAdd = new LinkedList<MailingListUser>();
      for (final User user : projectGroup.getUsers())
      {
        final MailingListUser mailListUser1 = new MailingListUserImpl(user.getLogin(), user.getEmail());
        subscribersToAdd.add(mailListUser1);
      }
      mailingListGroup1.setMembers(subscribersToAdd);

      final LinkedList<MailingListGroup> mailingListGroups = new LinkedList<MailingListGroup>();
      mailingListGroups.add(mailingListGroup1);

      // subscribe users to the new mailing list
      final LinkedList<MailingListUser> mailingListUsers = new LinkedList<MailingListUser>();
      mailingListUsers
          .add(new MailingListUserImpl(TestConstants.USERTEST2LOGIN, TestConstants.USERTEST2EMAIL));

      mailingListUsers.add(new MailingListUserImpl(TestConstants.USERTEST15LOGIN,
          TestConstants.USERTEST15EMAIL));

      // ************* subscribe users: user2test-u et user15test-u to the mailing list **********
      mailingListCategoryService.addSubscribers(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingListName, mailingListUsers, false);

      // ************** subscribe the project group to the new mailing list *********************
      mailingListCategoryService.addGroupsSubscriptions(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingListName, mailingListGroups, false);
      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // check the created mailing list
      final MailingListBean newMailingListBean = getMailingList(mailingListName);
      assertNotNull("The created mailing list has not been found", newMailingListBean);

      // check propagation into sympa DB: internal and external user should be added
      checkNewMailingListPropagationIntoSympaAfterGroupAdded(mailingListName);

      // check returned subscribers after adding the group
      checkSubscribersWithAddedProjectGroup(newMailingListBean, projectGroupId);

      // removing members of group and updating with groupPresenter will call
      // by delegation updateGroupSubscription() into camel route.
      final Group projectGroupForMemberRemoval = groupPresenter.getGroup(projectId, projectGroupId);
      projectGroupForMemberRemoval.clearUsers();
      assertTrue("The group has not been cleared (in order to test updateGroupSubscription)",
          projectGroupForMemberRemoval.getUsers().size() == 0);
      groupPresenter.updateGroup(projectId, projectGroupId, projectGroupForMemberRemoval);

      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // check mailing list members (users and group after group member clearing)
      final MailingListBean mailingListBeanAfterGroupSubscriptionRemove = getMailingList(mailingListName);
      checkSubscribersAfterGroupClearing(mailingListBeanAfterGroupSubscriptionRemove, projectGroupId);

      // check propagation into Sympa : usertest2 and usertest15 should be kept !!!!
      final List<String> subscribersListAfterGroupSubscriptionRemove = getUsersSubscribtionPropagationForMailingList(mailingListName);
      LOG.debug("**************** After group removal subscribersList into Sympa= "
          + subscribersListAfterGroupSubscriptionRemove.toString());
      checkNewMailingListPropagationIntoSympaAfterGroupRemoval(subscribersListAfterGroupSubscriptionRemove);

    }
    finally
    {
      // cleaning test environment
      deleteProjectGroupAndCheck(mailingListName, projectGroupId);
      closeMailingListAndCheck(mailingListName);
      if (authentificationService.checkLogin())
      {
        authentificationService.logout();
      }
    }
  }

  private void checkSubscribersAfterGroupClearing(final MailingListBean newMailingListBean, final String pGroupId)
  {
    final ArrayList<String> userEmailsList = new ArrayList<String>();
    final ArrayList<String> userLoginsList = new ArrayList<String>();

    assertEquals(TestConstants.TOTAL_MAILING_LIST_SUBSCRIBERS, newMailingListBean.getSubscribers().size());
    for (final MailingListSubscriber mailingListSubscriber : newMailingListBean.getSubscribers())
    {
      if (mailingListSubscriber instanceof MailingListGroupImpl)
      {
        final MailingListGroup mailingListGroup = (MailingListGroup) mailingListSubscriber;
        assertTrue("The group subscriber does not exist", pGroupId.equals(mailingListGroup.getName()));
        final List<MailingListUser> userMembers = mailingListGroup.getMembers();
        assertTrue("the subscribed group that has been cleared always contains members", userMembers.size() == 0);
      }
      if (mailingListSubscriber instanceof MailingListUserImpl)
      {
        userEmailsList.add(((MailingListUserImpl) mailingListSubscriber).getEmail());
        userLoginsList.add(((MailingListUserImpl) mailingListSubscriber).getLogin());
      }
    }

    LOG.debug("***********After group removal userEmailsList ***********************************" + userEmailsList
                                                                                                        .toString());
    LOG.debug("*********** After group removal userLoginsList ***********************************" + userLoginsList
                                                                                                         .toString());

    // check email@ and logins exist for users: usertest2, usertest15, admin1
    assertTrue("mailingList does not contain: usertest2 email", userEmailsList.contains(TestConstants.USERTEST2EMAIL));
    assertTrue("mailingList does not contain: usertest2 login", userLoginsList.contains(TestConstants.USERTEST2LOGIN));

    assertTrue("mailingList does not contain: usertest15 email",
               userEmailsList.contains(TestConstants.USERTEST15EMAIL));
    assertTrue("mailingList does not contain: usertest15 login",
               userLoginsList.contains(TestConstants.USERTEST15LOGIN));

    assertTrue("mailingList does not contain: admin1 email", userEmailsList.contains(TestConstants.ADMIN1EMAIL));
    assertTrue("mailingList does not contain: admin1 login", userLoginsList.contains(TestConstants.ADMIN1LOGIN));

  }

  public void test07TestNewMailingListSubscribeForgeGroupUnsubscribeForgeGroup() throws Exception
  {
    /*------------------------------------------------------------------------------------------------
     *              forge group with users external
     *------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------
     *      Testing new mailing list when subscribing and the un-subscribing forge group
     *      (with external and internal users to the project, and getting users both subscribed
     *      as user within a forge group)
     *
     *              - creation of a new mailing lists (different from project mailing list)
     *              - add 2 users subscription:
     *                usertest2 (internal), usertest15 (external)
     *              - adding to forge group 4 user members:
     *               (usertest1,usertest2/internal, usertest15, usertest14/external
     *              - subscribing forge group as a group for the mailing list
     *              - check propagation after subscription
     *              - un-subscribe forge group for the mailing list
     *              - check propagation after removal of the group subscription
     *              - clear user from the forge group
     *              - remove membership on the forge group within the project
     *              - close the mailing list
     *------------------------------------------------------------------------------------------------*/
    String mailingListName = "";
    MailingListGroup mailingListGroup1;
    UUID groupUUID = null;
    System.out.println("Executiong test07TestNewMailingListSubscribeForgeGroupUnsubscribeForgeGroup .....");
    try
    {
      login(true, null);

      // affiliate this new forge group to the project with role: developpeur
      final HashSet<String> rolesname = new HashSet<String>();
      rolesname.add(TestConstants.ROLE_FORGE_GROUP);
      final Group forgeGroup = groupPresenter.getGroup(TestConstants.FORGE_PROJECTID,
          TestConstants.FORGE_GROUPID);
      groupUUID = forgeGroup.getUuid();
      membershipPresenter.addGroupMembership(projectId, groupUUID, rolesname, "");

      // users members for the forge project
      final ArrayList<String> userLoginList = new ArrayList<String>();
      // add internal members
      userLoginList.add(TestConstants.USERTEST1LOGIN);
      userLoginList.add(TestConstants.USERTEST2LOGIN);
      // add external member
      userLoginList.add(TestConstants.USERTEST14LOGIN);
      userLoginList.add(TestConstants.USERTEST15LOGIN);

      // add a user to this new group (forge group)
      for (final String userLogin : userLoginList)
      {
        final User userToAdd = userPresenter.getUser(userLogin);
        forgeGroup.addUser(userToAdd);
      }

      // update the forge group (not yet subscribed to the mailing list !!!
      groupPresenter.updateGroup(TestConstants.FORGE_PROJECTID, TestConstants.FORGE_GROUPID, forgeGroup);

      // create the mailing list
      final MailingListBean mailingList1 = mailingListCategoryService.newMailingList();

      // set mailing list name with prefix and a timestamp
      mailingListName = getTimeStampedMailingListName();
      mailingList1.setName(mailingListName);
      mailingList1.setDescription(TestConstants.MAILINGLIST_DESCRIPTION);
      mailingList1.setSubject(TestConstants.MAILINGLIST1SUBJECT);
      mailingList1.setType(MailingListType.PUBLIC_LIST);

      // ****************** create the mailing list !!!!!!!!!!!!!!!!!!!!!!
      mailingListCategoryService.createMailingList(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingList1);
      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // create the mailinglist group mapped to the created project group (based on UUID)
      mailingListGroup1 = new MailingListGroupImpl();
      mailingListGroup1.setName(TestConstants.FORGE_GROUPID);
      mailingListGroup1.setUUID(groupUUID);

      // create mailing list group associated to the projectGroup (same UUID)
      final List<MailingListUser> subscribersToAdd = new LinkedList<MailingListUser>();
      for (final User user : forgeGroup.getUsers())
      {
        final MailingListUser mailListUser1 = new MailingListUserImpl(user.getLogin(), user.getEmail());
        subscribersToAdd.add(mailListUser1);
      }
      mailingListGroup1.setMembers(subscribersToAdd);

      final LinkedList<MailingListGroup> mailingListGroups = new LinkedList<MailingListGroup>();
      mailingListGroups.add(mailingListGroup1);

      // subscribe users to the new mailing list
      final LinkedList<MailingListUser> mailingListUsers = new LinkedList<MailingListUser>();
      mailingListUsers
          .add(new MailingListUserImpl(TestConstants.USERTEST2LOGIN, TestConstants.USERTEST2EMAIL));

      mailingListUsers.add(new MailingListUserImpl(TestConstants.USERTEST15LOGIN,
          TestConstants.USERTEST15EMAIL));

      // ************* subscribe users: user2test-u et user15test-u to the mailing list **********
      mailingListCategoryService.addSubscribers(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingListName, mailingListUsers, false);

      // ************** subscribe the project group to the new mailing list *********************
      mailingListCategoryService.addGroupsSubscriptions(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingListName, mailingListGroups, false);
      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // check the created mailing list
      final MailingListBean newMailingListBean = getMailingList(mailingListName);
      assertNotNull("The created mailing list has not been found", newMailingListBean);

      // check propagation into sympa DB: internal and external user should be added
      checkNewMailingListPropagationIntoSympaAfterGroupAdded(mailingListName);

      // check returned subscribers after adding the group
      checkSubscribersWithAddedProjectGroup(newMailingListBean, TestConstants.FORGE_GROUPID);

      // remove subscription for the groupmailingList associated to the forge group
      mailingListCategoryService.removeGroupSubscription(forgeId, pluginInstanceId,
          TestConstants.ADMIN1LOGIN, mailingListName, mailingListGroup1, false);
      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // check the users subscriptions already exist for user2test and user15test
      final MailingListBean mailingListBeanAfterGroupSubscriptionRemove = getMailingList(mailingListName);
      checkSubscribersAfterProjectGroupRemoval(mailingListBeanAfterGroupSubscriptionRemove);

      // check propagation into Sympa : usertest2 and usertest15 should be kept !!!!
      final List<String> subscribersListAfterGroupSubscriptionRemove = getUsersSubscribtionPropagationForMailingList(mailingListName);
      LOG.debug("**************** After group removal subscribersList into Sympa= "
          + subscribersListAfterGroupSubscriptionRemove.toString());
      checkNewMailingListPropagationIntoSympaAfterGroupRemoval(subscribersListAfterGroupSubscriptionRemove);
    }
    finally
    {
      // delete users for the forge group
      final Group forgeGroup1 = groupPresenter.getGroup(TestConstants.FORGE_PROJECTID,
          TestConstants.FORGE_GROUPID);
      forgeGroup1.clearUsers();
      groupPresenter.updateGroup(TestConstants.FORGE_PROJECTID, TestConstants.FORGE_GROUPID, forgeGroup1);

      // remove the role subscribtion
      membershipPresenter.removeGroupMembership(projectId, groupUUID, false);

      // close the mailing list
      closeMailingListAndCheck(mailingListName);

      if (authentificationService.checkLogin())
      {
        authentificationService.logout();
      }
    }
  }

  public void test08TestNewMailingListSubscribeForgeGroupClearUpdateForgeGroup() throws Exception
  {
    /*------------------------------------------------------------------------------------------------
     *              forge group with users external
     *------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------
     *      Testing new mailing list when subscribing and the un-subscribing forge group
     *      (with external and internal users to the project, and getting users both subscribed
     *      as user within a forge group)
     *
     *              - creation of a new mailing lists (different from project mailing list)
     *              - add 2 users subscription:
     *                usertest2 (internal), usertest15 (external)
     *              - adding to forge group 4 user members:
     *               (usertest1,usertest2/internal, usertest15, usertest14/external
     *              - subscribing forge group as a group for the mailing list
     *              - check propagation after subscription
     *              - clear user from the forge group and update it
     *              - check propagation after clearing and updating the forge group
     *              - remove membership on the forge group within the project
     *              - close the mailing list
     *------------------------------------------------------------------------------------------------*/
    String mailingListName = "";
    MailingListGroup mailingListGroup1;
    UUID groupUUID = null;
    System.out.println("Executiong test08TestNewMailingListSubscribeForgeGroupClearUpdateForgeGroup .....");
    try
    {
      login(true, null);

      // affiliate this new forge group to the project with role: developpeur
      final HashSet<String> rolesname = new HashSet<String>();
      rolesname.add(TestConstants.ROLE_FORGE_GROUP);
      final Group forgeGroup = groupPresenter.getGroup(TestConstants.FORGE_PROJECTID,
          TestConstants.FORGE_GROUPID);
      groupUUID = forgeGroup.getUuid();
      membershipPresenter.addGroupMembership(projectId, groupUUID, rolesname, "");

      // users members for the forge project
      final ArrayList<String> userLoginList = new ArrayList<String>();
      // add internal members
      userLoginList.add(TestConstants.USERTEST1LOGIN);
      userLoginList.add(TestConstants.USERTEST2LOGIN);
      // add external member
      userLoginList.add(TestConstants.USERTEST14LOGIN);
      userLoginList.add(TestConstants.USERTEST15LOGIN);

      // add a user to this new group (forge group)
      for (final String userLogin : userLoginList)
      {
        final User userToAdd = userPresenter.getUser(userLogin);
        forgeGroup.addUser(userToAdd);
      }

      // update the forge group (not yet subscribed to the mailing list !!!
      groupPresenter.updateGroup(TestConstants.FORGE_PROJECTID, TestConstants.FORGE_GROUPID, forgeGroup);

      // create the mailing list
      final MailingListBean mailingList1 = mailingListCategoryService.newMailingList();

      // set mailing list name with prefix and a timestamp
      mailingListName = getTimeStampedMailingListName();
      mailingList1.setName(mailingListName);
      mailingList1.setDescription(TestConstants.MAILINGLIST_DESCRIPTION);
      mailingList1.setSubject(TestConstants.MAILINGLIST1SUBJECT);
      mailingList1.setType(MailingListType.PUBLIC_LIST);

      // ****************** create the mailing list !!!!!!!!!!!!!!!!!!!!!!
      mailingListCategoryService.createMailingList(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingList1);
      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // create the mailinglist group mapped to the created project group (based on UUID)
      mailingListGroup1 = new MailingListGroupImpl();
      mailingListGroup1.setName(TestConstants.FORGE_GROUPID);
      mailingListGroup1.setUUID(groupUUID);

      // create mailing list group associated to the projectGroup (same UUID)
      final List<MailingListUser> subscribersToAdd = new LinkedList<MailingListUser>();
      for (final User user : forgeGroup.getUsers())
      {
        final MailingListUser mailListUser1 = new MailingListUserImpl(user.getLogin(), user.getEmail());
        subscribersToAdd.add(mailListUser1);
      }
      mailingListGroup1.setMembers(subscribersToAdd);

      final LinkedList<MailingListGroup> mailingListGroups = new LinkedList<MailingListGroup>();
      mailingListGroups.add(mailingListGroup1);

      // subscribe users to the new mailing list
      final LinkedList<MailingListUser> mailingListUsers = new LinkedList<MailingListUser>();
      mailingListUsers
          .add(new MailingListUserImpl(TestConstants.USERTEST2LOGIN, TestConstants.USERTEST2EMAIL));

      mailingListUsers.add(new MailingListUserImpl(TestConstants.USERTEST15LOGIN,
          TestConstants.USERTEST15EMAIL));

      // ************* subscribe users: user2test-u et user15test-u to the mailing list **********
      mailingListCategoryService.addSubscribers(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingListName, mailingListUsers, false);

      // ************** subscribe the project group to the new mailing list *********************
      mailingListCategoryService.addGroupsSubscriptions(forgeId, pluginInstanceId, TestConstants.ADMIN1LOGIN,
          mailingListName, mailingListGroups, false);
      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // check the created mailing list
      final MailingListBean newMailingListBean = getMailingList(mailingListName);
      assertNotNull("The created mailing list has not been found", newMailingListBean);

      // check propagation into sympa DB: internal and external user should be added
      checkNewMailingListPropagationIntoSympaAfterGroupAdded(mailingListName);

      // check returned subscribers after adding the group
      checkSubscribersWithAddedProjectGroup(newMailingListBean, TestConstants.FORGE_GROUPID);

      // - clear user from the forge group and update it
      // delete users for the forge group
      final Group forgeGroup1 = groupPresenter.getGroup(TestConstants.FORGE_PROJECTID,
          TestConstants.FORGE_GROUPID);
      forgeGroup1.clearUsers();
      groupPresenter.updateGroup(TestConstants.FORGE_PROJECTID, TestConstants.FORGE_GROUPID, forgeGroup1);

      Thread.currentThread();
      Thread.sleep(TestConstants.WAIT_PROPAGATION);

      // check the users subscriptions already exist for user2test and user15test
      final MailingListBean mailingListBeanAfterGroupClearingUsers = getMailingList(mailingListName);
      checkSubscribersAfterGroupClearing(mailingListBeanAfterGroupClearingUsers, TestConstants.FORGE_GROUPID);

      // check propagation into Sympa : usertest2 and usertest15 should be kept !!!!
      final List<String> subscribersListAfterGroupSubscriptionRemove = getUsersSubscribtionPropagationForMailingList(mailingListName);
      LOG.debug("**************** After group removal subscribersList into Sympa= "
          + subscribersListAfterGroupSubscriptionRemove.toString());
      checkNewMailingListPropagationIntoSympaAfterGroupRemoval(subscribersListAfterGroupSubscriptionRemove);
    }
    finally
    {
      // remove the role subscribtion
      membershipPresenter.removeGroupMembership(projectId, groupUUID, false);

      // close the mailing list
      closeMailingListAndCheck(mailingListName);

      if (authentificationService.checkLogin())
      {
        authentificationService.logout();
      }
    }
  }

}
