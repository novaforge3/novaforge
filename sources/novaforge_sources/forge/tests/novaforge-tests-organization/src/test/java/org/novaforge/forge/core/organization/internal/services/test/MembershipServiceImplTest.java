/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.core.organization.internal.services.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.novaforge.forge.core.organization.exceptions.GroupServiceException;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.services.CommonUserService;
import org.novaforge.forge.core.organization.services.GroupService;
import org.novaforge.forge.core.organization.services.LanguageService;
import org.novaforge.forge.core.organization.services.MembershipService;
import org.novaforge.forge.core.organization.services.ProjectRoleService;
import org.novaforge.forge.core.organization.services.ProjectService;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;

/**
 * @author blachonm
 */
@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class MembershipServiceImplTest extends BaseUtil
{
  private static final String LANGUAGE_FR_NAME     = "FR";

  // admin1
  private static final String ADMIN1               = "admin1";

  // user1
  private static final String NAME1                = "name1";
  private static final String FIRSTNAME1           = "firstname1";
  private static final String LOGIN1               = "name1-f";
  private static final String EMAIL1               = "mailUser1@toto.fr";
  private static final String PASSWORD1            = "password1";

  // user2
  private static final String NAME2                = "name2";
  private static final String FIRSTNAME2           = "firstname2";
  private static final String LOGIN2               = "name2-f";
  private static final String EMAIL2               = "mailUser2@toto.fr";
  private static final String PASSWORD2            = "password2";

  // user3
  private static final String NAME3                = "name3";
  private static final String FIRSTNAME3           = "firstname3";
  private static final String LOGIN3               = "name3-f";
  private static final String EMAIL3               = "mailUser3@toto.fr";
  private static final String PASSWORD3            = "password3";

  // group1
  private static final String GROUPNAME1           = "group1";
  private static final String GROUPDESC1           = "'group description 1";

  // group1
  private static final String GROUPNAME2           = "group2";
  private static final String GROUPDESC2           = "'group description 2";

  // project1 project
  private static final String PROJECT1NAME         = "project 1";
  private static final String PROJECT_DESCRIPTION1 = "description 1";
  private static final String PROJECT1ID           = "project1";
  private static final String LGPL                 = "LGPL";

  // project1 project
  private static final String PROJECT2NAME         = "project 2";
  private static final String PROJECT_DESCRIPTION2 = "description 2";
  private static final String PROJECT2ID           = "project2";

  // forge project
  private static final String FORGEID              = "forge";

  // project role
  private static final String ROLE1NAME            = "roleName1";
  private static final String ROLE1DESC            = "roleDesc1";
  private static final String ROLE2NAME            = "roleName2";
  private static final String ROLE2DESC            = "roleDesc2";
  private static final String ROLE3NAME            = "roleName3";
  private static final String ROLE3DESC            = "roleDesc3";
  private static final String ADMINISTRATOR        = "Administrator";
  private static final String MEMBER               = "Member";

  @Inject
  private ProjectService      projectService;

  @Inject
  private ProjectRoleService  projectRoleService;

  @Inject
  private CommonUserService   commonUserService;

  @Inject
  private LanguageService     languageService;

  @Inject
  private MembershipService   membershipService;

  @Inject
  private GroupService        groupService;

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#addUserMembership(java.lang.String, java.lang.String, java.util.Set, java.lang.String, java.lang.String, boolean)}
   * .
   * 
   * @throws Exception
   */
  @Test
  // @Ignore
  public void testAddUserMembership() throws Exception
  {
    prepareForUserMembership();

    final HashSet<String> rolesNameForLogin1 = new HashSet<String>();
    rolesNameForLogin1.add(ROLE1NAME);
    rolesNameForLogin1.add(ROLE2NAME);

    final HashSet<String> rolesNameForLogin2 = new HashSet<String>();
    rolesNameForLogin2.add(ROLE1NAME);

    // associate user members for the project
    membershipService.addUserMembership(PROJECT1ID, LOGIN1, rolesNameForLogin1, ROLE1NAME, NAME1, false);
    membershipService.addUserMembership(PROJECT1ID, LOGIN2, rolesNameForLogin2, ROLE1NAME, NAME2, false);

    // check for user1 (3 roles)
    final HashSet<String> expectedRolesName1 = new HashSet<String>();
    final HashSet<String> currentRolesName1 = new HashSet<String>();
    expectedRolesName1.add(ROLE1NAME);
    expectedRolesName1.add(ROLE2NAME);
    expectedRolesName1.add(ADMINISTRATOR);

    final Set<ProjectRole> rolesForLogin1 = projectRoleService.findAllRolesByActorAndProject(PROJECT1ID,
        LOGIN1);
    assertNotNull(rolesForLogin1);
    assertEquals(3, rolesForLogin1.size());
    for (final ProjectRole projectRole : rolesForLogin1)
    {
      currentRolesName1.add(projectRole.getName());
    }
    assertEquals(expectedRolesName1, currentRolesName1);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#addGroupMembership(java.lang.String, java.lang.String, java.util.Set, java.lang.String, java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testAddGroupMembership() throws Exception
  {
    prepareForGroupMembership();

    final HashSet<String> rolesName1 = new HashSet<String>();
    rolesName1.add(ROLE1NAME);

    final HashSet<String> rolesName2 = new HashSet<String>();
    rolesName2.add(ROLE2NAME);

    // associate group members and for the project
    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME1, rolesName1, ROLE1NAME, NAME1);
    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME2, rolesName2, ROLE2NAME, NAME1);

    // check
    List<MembershipInfo> groupsMembership = membershipService.getAllGroupMemberships(PROJECT1ID);
    assertNotNull(groupsMembership);
    assertEquals(2, groupsMembership.size());

    // check rolefor groups
    // check group login name
    ArrayList<String> expectedGroupActorLogins = new ArrayList<String>();
    expectedGroupActorLogins.add(GROUPNAME1);
    expectedGroupActorLogins.add(GROUPNAME2);

    ArrayList<String> expectedGroupRoles = new ArrayList<String>();
    expectedGroupRoles.add(ROLE1NAME);
    expectedGroupRoles.add(ROLE2NAME);

    ArrayList<String> currentGroupActorLogins = new ArrayList<String>();
    ArrayList<String> currentgroupRoleNames = new ArrayList<String>();

    for (MembershipInfo groupMembershipInfo : groupsMembership)
    {
      String groupActorLogin = groupMembershipInfo.getActor().getLogin();
      String groupRoleName = groupMembershipInfo.getRole().getName();
      currentGroupActorLogins.add(groupActorLogin);
      currentgroupRoleNames.add(groupRoleName);
    }
    assertEquals(expectedGroupActorLogins, currentGroupActorLogins);
    assertEquals(expectedGroupRoles, currentgroupRoleNames);

    // check users into groups
    ArrayList<String> currentGroup1 = new ArrayList<String>();
    ArrayList<String> currentGroup2 = new ArrayList<String>();
    List<User> currentGroup1users = groupService.getGroup(PROJECT1ID, GROUPNAME1).getUsers();
    for (User user : currentGroup1users)
    {
      currentGroup1.add(user.getName());
    }
    List<User> currentGroup2users = groupService.getGroup(PROJECT1ID, GROUPNAME2).getUsers();
    for (User user : currentGroup2users)
    {
      currentGroup2.add(user.getName());
    }
    ArrayList<String> expectedGroup1 = new ArrayList<String>();
    expectedGroup1.add(NAME1);
    expectedGroup1.add(NAME2);
    ArrayList<String> expectedGroup2 = new ArrayList<String>();
    expectedGroup2.add(NAME1);
    expectedGroup2.add(NAME3);
    assertEquals(expectedGroup1, currentGroup1);
    assertEquals(expectedGroup2, currentGroup2);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#getAllUserMemberships(java.lang.String, boolean)}
   * .
   */
  @Test
  // @Ignore
  public void testGetAllUserMemberships() throws Exception
  {
    prepareForUserMembership();

    final HashSet<String> rolesNameForLogin1 = new HashSet<String>();
    rolesNameForLogin1.add(ROLE1NAME);
    rolesNameForLogin1.add(ROLE2NAME);

    final HashSet<String> rolesNameForLogin2 = new HashSet<String>();
    rolesNameForLogin2.add(ROLE1NAME);

    // associate user members for the project
    membershipService.addUserMembership(PROJECT1ID, LOGIN1, rolesNameForLogin1, ROLE1NAME, NAME1, false);
    membershipService.addUserMembership(PROJECT1ID, LOGIN2, rolesNameForLogin2, ROLE1NAME, NAME2, false);

    // check for user1 (3 roles)
    final HashSet<String> expectedRolesName1 = new HashSet<String>();
    expectedRolesName1.add(ROLE1NAME);
    expectedRolesName1.add(ROLE2NAME);
    expectedRolesName1.add(ADMINISTRATOR);

    // case 1: without System
    boolean pWithSystem = false;
    List<MembershipInfo> membershipsFalse = membershipService.getAllUserMemberships(PROJECT1ID, pWithSystem);
    assertNotNull(membershipsFalse);
    assertEquals(4, membershipsFalse.size());

    HashMap<String, String> expectedMemberShipsFalse = new HashMap<String, String>();
    expectedMemberShipsFalse.put(LOGIN1, ADMINISTRATOR);
    expectedMemberShipsFalse.put(LOGIN1, ROLE1NAME);
    expectedMemberShipsFalse.put(LOGIN1, ROLE2NAME);
    expectedMemberShipsFalse.put(LOGIN2, ROLE1NAME);

    HashMap<String, String> currentMemberShipsFalse = new HashMap<String, String>();
    for (MembershipInfo membershipInfo : membershipsFalse)
    {
      String actorLogin = membershipInfo.getActor().getLogin();
      String roleName = membershipInfo.getRole().getName();
      currentMemberShipsFalse.put(actorLogin, roleName);
    }
    assertEquals(expectedMemberShipsFalse, currentMemberShipsFalse);

    // case 2 : with System
    pWithSystem = true;
    List<MembershipInfo> membershipsTrue = membershipService.getAllUserMemberships(PROJECT1ID, pWithSystem);
    assertNotNull(membershipsTrue);
    assertEquals(5, membershipsTrue.size());

    HashMap<String, String> expectedMemberShipsTrue = new HashMap<String, String>();
    expectedMemberShipsTrue.put(ADMIN1, ADMINISTRATOR);
    expectedMemberShipsTrue.put(LOGIN1, ADMINISTRATOR);
    expectedMemberShipsTrue.put(LOGIN1, ROLE1NAME);
    expectedMemberShipsTrue.put(LOGIN1, ROLE2NAME);
    expectedMemberShipsTrue.put(LOGIN2, ROLE1NAME);

    HashMap<String, String> currentMemberShipsTrue = new HashMap<String, String>();
    for (MembershipInfo membershipInfo : membershipsTrue)
    {
      String actorLogin = membershipInfo.getActor().getLogin();
      String roleName = membershipInfo.getRole().getName();
      currentMemberShipsTrue.put(actorLogin, roleName);
    }
    assertEquals(expectedMemberShipsTrue, currentMemberShipsTrue);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#getAllGroupMemberships(java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testGetAllGroupMemberships() throws Exception
  {
    prepareForGroupMembership();

    final HashSet<String> rolesName1 = new HashSet<String>();
    rolesName1.add(ROLE1NAME);

    final HashSet<String> rolesName2 = new HashSet<String>();
    rolesName2.add(ROLE2NAME);

    // associate group members and for the project
    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME1, rolesName1, ROLE1NAME, NAME1);
    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME2, rolesName2, ROLE2NAME, NAME1);

    // get all groups memberships
    List<MembershipInfo> groupsMembership = membershipService.getAllGroupMemberships(PROJECT1ID);

    // check
    assertNotNull(groupsMembership);
    assertEquals(2, groupsMembership.size());

    // check rolefor groups
    // check group login name
    ArrayList<String> expectedGroupActorLogins = new ArrayList<String>();
    expectedGroupActorLogins.add(GROUPNAME1);
    expectedGroupActorLogins.add(GROUPNAME2);

    ArrayList<String> expectedGroupRoles = new ArrayList<String>();
    expectedGroupRoles.add(ROLE1NAME);
    expectedGroupRoles.add(ROLE2NAME);

    ArrayList<String> currentGroupActorLogins = new ArrayList<String>();
    ArrayList<String> currentgroupRoleNames = new ArrayList<String>();

    for (MembershipInfo groupMembershipInfo : groupsMembership)
    {
      String groupActorLogin = groupMembershipInfo.getActor().getLogin();
      String groupRoleName = groupMembershipInfo.getRole().getName();
      currentGroupActorLogins.add(groupActorLogin);
      currentgroupRoleNames.add(groupRoleName);
    }
    assertEquals(expectedGroupActorLogins, currentGroupActorLogins);
    assertEquals(expectedGroupRoles, currentgroupRoleNames);

    // check users into groups
    ArrayList<String> currentGroup1 = new ArrayList<String>();
    ArrayList<String> currentGroup2 = new ArrayList<String>();
    List<User> currentGroup1users = groupService.getGroup(PROJECT1ID, GROUPNAME1).getUsers();
    for (User user : currentGroup1users)
    {
      currentGroup1.add(user.getName());
    }
    List<User> currentGroup2users = groupService.getGroup(PROJECT1ID, GROUPNAME2).getUsers();
    for (User user : currentGroup2users)
    {
      currentGroup2.add(user.getName());
    }
    ArrayList<String> expectedGroup1 = new ArrayList<String>();
    expectedGroup1.add(NAME1);
    expectedGroup1.add(NAME2);
    ArrayList<String> expectedGroup2 = new ArrayList<String>();
    expectedGroup2.add(NAME1);
    expectedGroup2.add(NAME3);
    assertEquals(expectedGroup1, currentGroup1);
    assertEquals(expectedGroup2, currentGroup2);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#updateUserMembership(java.lang.String, java.lang.String, java.util.Set, java.lang.String, boolean, java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testUpdateUserMembership() throws Exception
  {
    prepareForUserMembership();

    final HashSet<String> rolesNameForLogin1 = new HashSet<String>();
    rolesNameForLogin1.add(ROLE1NAME);
    rolesNameForLogin1.add(ROLE2NAME);

    final HashSet<String> rolesNameForLogin2 = new HashSet<String>();
    rolesNameForLogin2.add(ROLE1NAME);

    // associate user members for the project
    membershipService.addUserMembership(PROJECT1ID, LOGIN1, rolesNameForLogin1, ROLE1NAME, NAME1, false);
    membershipService.addUserMembership(PROJECT1ID, LOGIN2, rolesNameForLogin2, ROLE1NAME, NAME2, false);

    // check membership for user1 (3 roles)
    final HashSet<String> expectedRolesName1 = new HashSet<String>();
    final HashSet<String> currentRolesName1 = new HashSet<String>();
    expectedRolesName1.add(ROLE1NAME);
    expectedRolesName1.add(ROLE2NAME);
    expectedRolesName1.add(ADMINISTRATOR);

    final Set<ProjectRole> rolesForLogin1 = projectRoleService.findAllRolesByActorAndProject(PROJECT1ID,
        LOGIN1);
    assertNotNull(rolesForLogin1);
    assertEquals(3, rolesForLogin1.size());
    for (final ProjectRole projectRole : rolesForLogin1)
    {
      currentRolesName1.add(projectRole.getName());
    }
    assertEquals(expectedRolesName1, currentRolesName1);

    // ****************** Update user membership ******************************
    final HashSet<String> newRolesNameForLogin1 = new HashSet<String>();
    // remove ROLE1 and add ROLE2,ROLE3, change default role name (1 to 2)
    newRolesNameForLogin1.add(ROLE2NAME);
    newRolesNameForLogin1.add(ROLE3NAME);

    membershipService
        .updateUserMembership(PROJECT1ID, LOGIN1, newRolesNameForLogin1, ROLE2NAME, false, NAME1);

    // check membership for LOGIN1
    final HashSet<String> expectedUpdatedRolesName1 = new HashSet<String>();
    final HashSet<String> currentUpdatedRolesName1 = new HashSet<String>();
    expectedUpdatedRolesName1.add(ROLE2NAME);
    expectedUpdatedRolesName1.add(ROLE3NAME);

    final Set<ProjectRole> updatedRolesForLogin1 = projectRoleService.findAllRolesByActorAndProject(
        PROJECT1ID, LOGIN1);
    assertNotNull(updatedRolesForLogin1);
    assertEquals(2, updatedRolesForLogin1.size());
    for (final ProjectRole projectRole : updatedRolesForLogin1)
    {
      currentUpdatedRolesName1.add(projectRole.getName());
    }
    assertEquals(expectedUpdatedRolesName1, currentUpdatedRolesName1);

    // check exception : NoResultException:Unable to obtain user information with [login=%s]
    try
    {
      membershipService.updateUserMembership(PROJECT1ID, "toto", newRolesNameForLogin1, ROLE2NAME, false,
          NAME1);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }

    // check raised exception: ProjectServiceException, Unable to obtain project applications with
    // [projectId=%s]
    try
    {
      membershipService.updateUserMembership("proj", LOGIN1, newRolesNameForLogin1, ROLE2NAME, false, NAME1);
      fail("should has raised an exception");
    }
    catch (final Exception pe)
    {
      System.out.println("expected exception!");
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#updateGroupMembership(java.lang.String, java.lang.String, java.util.Set, java.lang.String, java.lang.String, boolean)}
   * .
   */
  @Test
  // @Ignore
  public void testUpdateGroupMembership() throws Exception
  {
    prepareForGroupMembership();

    final HashSet<String> rolesName1 = new HashSet<String>();
    rolesName1.add(ROLE1NAME);

    final HashSet<String> rolesName2 = new HashSet<String>();
    rolesName2.add(ROLE2NAME);

    // associate group members and for the project
    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME1, rolesName1, ROLE1NAME, NAME1);
    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME2, rolesName2, ROLE2NAME, NAME1);

    // check
    List<MembershipInfo> groupsMembership = membershipService.getAllGroupMemberships(PROJECT1ID);
    assertNotNull(groupsMembership);
    assertEquals(2, groupsMembership.size());

    // check role for groups
    // check group login name
    ArrayList<String> expectedGroupActorLogins = new ArrayList<String>();
    expectedGroupActorLogins.add(GROUPNAME1);
    expectedGroupActorLogins.add(GROUPNAME2);

    ArrayList<String> expectedGroupRoles = new ArrayList<String>();
    expectedGroupRoles.add(ROLE1NAME);
    expectedGroupRoles.add(ROLE2NAME);

    ArrayList<String> currentGroupActorLogins = new ArrayList<String>();
    ArrayList<String> currentgroupRoleNames = new ArrayList<String>();

    for (MembershipInfo groupMembershipInfo : groupsMembership)
    {
      String groupActorLogin = groupMembershipInfo.getActor().getLogin();
      String groupRoleName = groupMembershipInfo.getRole().getName();
      currentGroupActorLogins.add(groupActorLogin);
      currentgroupRoleNames.add(groupRoleName);
    }
    assertEquals(expectedGroupActorLogins, currentGroupActorLogins);
    assertEquals(expectedGroupRoles, currentgroupRoleNames);

    // check users into groups
    ArrayList<String> currentGroup1 = new ArrayList<String>();
    ArrayList<String> currentGroup2 = new ArrayList<String>();
    List<User> currentGroup1users = groupService.getGroup(PROJECT1ID, GROUPNAME1).getUsers();
    for (User user : currentGroup1users)
    {
      currentGroup1.add(user.getName());
    }
    List<User> currentGroup2users = groupService.getGroup(PROJECT1ID, GROUPNAME2).getUsers();
    for (User user : currentGroup2users)
    {
      currentGroup2.add(user.getName());
    }
    ArrayList<String> expectedGroup1 = new ArrayList<String>();
    expectedGroup1.add(NAME1);
    expectedGroup1.add(NAME2);
    ArrayList<String> expectedGroup2 = new ArrayList<String>();
    expectedGroup2.add(NAME1);
    expectedGroup2.add(NAME3);
    assertEquals(expectedGroup1, currentGroup1);
    assertEquals(expectedGroup2, currentGroup2);

    // ******** Update group membership ************
    // initially group1: user1, user2 with ROLENAME1 role
    // initially group2: user1, user3 with ROLENAME2 role

    // after update:
    // group1: user1, user2 with ROLENAME3 role -> change here!
    // group2: user1, user3 with ROLENAME2 role

    final HashSet<String> newRolesName = new HashSet<String>();
    newRolesName.add(ROLE3NAME);
    membershipService.updateGroupMembership(PROJECT1ID, GROUPNAME1, newRolesName, ROLE3NAME, NAME1, false);

    // check to be validated after solving bug on previous line !!!!!
    List<MembershipInfo> groupsUpdatedMembership = membershipService.getAllGroupMemberships(PROJECT1ID);
    assertNotNull(groupsUpdatedMembership);
    assertEquals(2, groupsUpdatedMembership.size());

    ArrayList<String> expectedUpdatedGroupRoles = new ArrayList<String>();
    expectedUpdatedGroupRoles.add(ROLE3NAME);

    ArrayList<String> currentUpdatedgroupRoleNames = new ArrayList<String>();

    for (MembershipInfo groupMembershipInfo : groupsUpdatedMembership)
    {
      String groupActorLogin = groupMembershipInfo.getActor().getLogin();
      if (groupActorLogin.equals(GROUPNAME1))
      {
        String groupRoleName = groupMembershipInfo.getRole().getName();
        currentUpdatedgroupRoleNames.add(groupRoleName);
        break;
      }
    }
    assertEquals(expectedUpdatedGroupRoles, currentUpdatedgroupRoleNames);

    // check exception : NoResultException:Unable to obtain user information with [login=%s]
    try
    {
      membershipService.updateGroupMembership(PROJECT1ID, "toto", newRolesName, ROLE3NAME, NAME1, false);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }

    // check raised exception: ProjectServiceException, Unable to obtain project applications with
    // [projectId=%s]
    try
    {
      membershipService.updateGroupMembership("proj", GROUPNAME1, newRolesName, ROLE3NAME, NAME1, false);
      fail("should has raised an exception");
    }
    catch (final Exception pe)
    {
      System.out.println("expected exception!");
    }

  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#deleteUserMembership(java.lang.String, java.lang.String, boolean, java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testDeleteUserMembership() throws Exception
  {
    prepareForUserMembership();

    final HashSet<String> rolesNameForLogin1 = new HashSet<String>();
    rolesNameForLogin1.add(ROLE1NAME);
    rolesNameForLogin1.add(ROLE2NAME);

    final HashSet<String> rolesNameForLogin2 = new HashSet<String>();
    rolesNameForLogin2.add(ROLE1NAME);

    // associate user members for the project
    membershipService.addUserMembership(PROJECT1ID, LOGIN1, rolesNameForLogin1, ROLE1NAME, NAME1, false);
    membershipService.addUserMembership(PROJECT1ID, LOGIN2, rolesNameForLogin2, ROLE1NAME, NAME2, false);

    // check membership for user1 (3 roles)
    final HashSet<String> expectedRolesName1 = new HashSet<String>();
    final HashSet<String> currentRolesName1 = new HashSet<String>();
    expectedRolesName1.add(ROLE1NAME);
    expectedRolesName1.add(ROLE2NAME);
    expectedRolesName1.add(ADMINISTRATOR);

    final Set<ProjectRole> rolesForLogin1 = projectRoleService.findAllRolesByActorAndProject(PROJECT1ID,
        LOGIN1);
    assertNotNull(rolesForLogin1);
    assertEquals(3, rolesForLogin1.size());
    for (final ProjectRole projectRole : rolesForLogin1)
    {
      currentRolesName1.add(projectRole.getName());
    }
    assertEquals(expectedRolesName1, currentRolesName1);

    // ***** Delete user membership *****
    membershipService.deleteUserMembership(PROJECT1ID, LOGIN1, false, NAME1);

    // check membership for LOGIN1
    final Set<ProjectRole> updatedRolesForLogin1 = projectRoleService.findAllRolesByActorAndProject(
        PROJECT1ID, LOGIN1);
    assertNotNull(updatedRolesForLogin1);
    assertEquals(0, updatedRolesForLogin1.size());

    // check exception : NoResultException:Unable to obtain user information with [login=%s]
    try
    {
      membershipService.deleteUserMembership(PROJECT1ID, "toto", false, NAME1);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }

    // check raised exception: ProjectServiceException, Unable to obtain project applications with
    // [projectId=%s]
    try
    {
      membershipService.deleteUserMembership("proj", LOGIN2, false, NAME2);
      fail("should has raised an exception");
    }
    catch (final Exception pe)
    {
      System.out.println("expected exception!");
    }

  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#deleteGroupMembership(java.lang.String, java.lang.String, boolean, java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testDeleteGroupMembership() throws Exception
  {
    prepareForGroupMembership();

    final HashSet<String> rolesName1 = new HashSet<String>();
    rolesName1.add(ROLE1NAME);

    final HashSet<String> rolesName2 = new HashSet<String>();
    rolesName2.add(ROLE2NAME);

    // associate group members and for the project
    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME1, rolesName1, ROLE1NAME, NAME1);
    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME2, rolesName2, ROLE2NAME, NAME1);

    // partial check (already tested previuosly!)
    List<MembershipInfo> groupsMembership = membershipService.getAllGroupMemberships(PROJECT1ID);
    assertNotNull(groupsMembership);
    assertEquals(2, groupsMembership.size());

    // ******** Delete group membership ************
    // initially group1: user1, user2 with ROLENAME1 role
    // initially group2: user1, user3 with ROLENAME2 role

    // after delete:
    // No group1 membership !
    // group2: user1, user3 with ROLENAME2 role

    membershipService.deleteGroupMembership(PROJECT1ID, GROUPNAME1, false, NAME1);

    // check to be validated after solving bug on previous line !!!!!
    List<MembershipInfo> groupsUpdatedMembership = membershipService.getAllGroupMemberships(PROJECT1ID);
    assertNotNull(groupsUpdatedMembership);
    assertEquals(1, groupsUpdatedMembership.size());

    ArrayList<String> expectedUpdatedGroupRoles = new ArrayList<String>();
    expectedUpdatedGroupRoles.add(ROLE2NAME);

    ArrayList<String> currentUpdatedgroupRoleNames = new ArrayList<String>();

    for (MembershipInfo groupMembershipInfo : groupsUpdatedMembership)
    {
      String groupActorLogin = groupMembershipInfo.getActor().getLogin();
      if (groupActorLogin.equals(GROUPNAME2))
      {
        String groupRoleName = groupMembershipInfo.getRole().getName();
        currentUpdatedgroupRoleNames.add(groupRoleName);
        break;
      }
    }
    assertEquals(expectedUpdatedGroupRoles, currentUpdatedgroupRoleNames);

    // check exception : NoResultException:Unable to obtain user information with [login=%s]
    try
    {
      membershipService.deleteGroupMembership(PROJECT1ID, "toto", false, NAME1);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }

    // check raised exception: ProjectServiceException, Unable to obtain project applications with
    // [projectId=%s]
    try
    {
      membershipService.deleteGroupMembership("proj", GROUPNAME2, false, NAME1);
      fail("should has raised an exception");
    }
    catch (final Exception pe)
    {
      System.out.println("expected exception!");
    }

  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#getAllMemberships(java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testGetAllMemberships() throws Exception
  {
    prepareForGroupMembership();

    // associate user members for the project
    final HashSet<String> rolesNameForLogin1 = new HashSet<String>();
    rolesNameForLogin1.add(ROLE1NAME);
    rolesNameForLogin1.add(ROLE2NAME);
    final HashSet<String> rolesNameForLogin2 = new HashSet<String>();
    rolesNameForLogin2.add(ROLE1NAME);

    membershipService.addUserMembership(PROJECT1ID, LOGIN1, rolesNameForLogin1, ROLE1NAME, NAME1, false);
    membershipService.addUserMembership(PROJECT1ID, LOGIN2, rolesNameForLogin2, ROLE1NAME, NAME2, false);

    // associate group members and for the project
    final HashSet<String> rolesName1 = new HashSet<String>();
    rolesName1.add(ROLE1NAME);
    final HashSet<String> rolesName2 = new HashSet<String>();
    rolesName2.add(ROLE2NAME);

    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME1, rolesName1, ROLE1NAME, NAME1);
    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME2, rolesName2, ROLE2NAME, NAME1);

    // get all memberships
    List<Membership> allMemberships = membershipService.getAllMemberships(PROJECT1ID);
    assertNotNull(allMemberships);
    assertEquals(7, allMemberships.size());

    // check
    // membership =MembershipEntity [login=admin1, priority=false, projectId=project1, rolename=Administrator]
    // membership =MembershipEntity [login=name1-f, priority=false, projectId=project1,
    // rolename=Administrator]
    // membership =MembershipEntity [login=name1-f, priority=true, projectId=project1, rolename=roleName1]
    // membership =MembershipEntity [login=name1-f, priority=false, projectId=project1, rolename=roleName2]
    // membership =MembershipEntity [login=name2-f, priority=true, projectId=project1, rolename=roleName1]
    // membership =MembershipEntity [login=group1, priority=true, projectId=project1, rolename=roleName1]
    // membership =MembershipEntity [login=group2, priority=true, projectId=project1, rolename=roleName2]

    // check rolenames
    HashMap<String, String> expectedMemberShips = new HashMap<String, String>();
    expectedMemberShips.put(ADMIN1, ADMINISTRATOR);
    expectedMemberShips.put(LOGIN1, ADMINISTRATOR);
    expectedMemberShips.put(LOGIN1, ROLE1NAME);
    expectedMemberShips.put(LOGIN1, ROLE2NAME);
    expectedMemberShips.put(LOGIN2, ROLE1NAME);
    expectedMemberShips.put(GROUPNAME1, ROLE1NAME);
    expectedMemberShips.put(GROUPNAME2, ROLE2NAME);

    HashMap<String, String> currentMemberShips = new HashMap<String, String>();
    for (Membership membership : allMemberships)
    {
      String actorLogin = membership.getActor().getLogin();
      String roleName = membership.getRole().getName();
      currentMemberShips.put(actorLogin, roleName);
    }
    assertEquals(expectedMemberShips, currentMemberShips);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#getAllEffectiveUserMembershipsForProject(java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testGetAllEffectiveUserMembershipsForProject() throws Exception
  {
    // prepare for group to get users (user3) believing only to a group and not registered as user membership.
    // We expect to get all user memberships including those added to groups.
    prepareForGroupMembership();

    final HashSet<String> rolesNameForLogin1 = new HashSet<String>();
    rolesNameForLogin1.add(ROLE1NAME);
    rolesNameForLogin1.add(ROLE2NAME);

    final HashSet<String> rolesNameForLogin2 = new HashSet<String>();
    rolesNameForLogin2.add(ROLE1NAME);

    // associate user members for the project
    membershipService.addUserMembership(PROJECT1ID, LOGIN1, rolesNameForLogin1, ROLE1NAME, NAME1, false);
    membershipService.addUserMembership(PROJECT1ID, LOGIN2, rolesNameForLogin2, ROLE1NAME, NAME2, false);

    // associate group members and for the project
    final HashSet<String> rolesName1 = new HashSet<String>();
    rolesName1.add(ROLE1NAME);
    final HashSet<String> rolesName2 = new HashSet<String>();
    rolesName2.add(ROLE2NAME);

    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME1, rolesName1, ROLE1NAME, NAME1);
    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME2, rolesName2, ROLE2NAME, NAME1);

    List<Membership> allMemberships = membershipService.getAllEffectiveUserMembershipsForProject(PROJECT1ID);
    // **** user membership from users:
    // membership =MembershipEntity [login=admin1, priority=false, projectId=project1, rolename=Administrator]
    // membership =MembershipEntity [login=name1-f, priority=false, projectId=project1,
    // rolename=Administrator]
    // membership =MembershipEntity [login=name1-f, priority=true, projectId=project1, rolename=roleName1]
    // membership =MembershipEntity [login=name1-f, priority=false, projectId=project1, rolename=roleName2]
    // membership =MembershipEntity [login=name2-f, priority=true, projectId=project1, rolename=roleName1]
    // **** user member ships from groups:
    // membership =MembershipEntity [login=name1-f, priority=false, projectId=project1, rolename=roleName1]
    // membership =MembershipEntity [login=name2-f, priority=false, projectId=project1, rolename=roleName1]
    // membership =MembershipEntity [login=name1-f, priority=false, projectId=project1, rolename=roleName2]
    // membership =MembershipEntity [login=name3-f, priority=false, projectId=project1, rolename=roleName2]
    assertNotNull(allMemberships);
    assertEquals(9, allMemberships.size());

    // **** check rolenames
    HashMap<String, String> expectedMemberShips = new HashMap<String, String>();
    // user memberships for user
    expectedMemberShips.put(ADMIN1, ADMINISTRATOR);
    expectedMemberShips.put(LOGIN1, ADMINISTRATOR);
    expectedMemberShips.put(LOGIN1, ROLE1NAME);
    expectedMemberShips.put(LOGIN1, ROLE2NAME);
    expectedMemberShips.put(LOGIN2, ROLE1NAME);

    // user memberships for users into groups
    expectedMemberShips.put(LOGIN1, ROLE1NAME);
    expectedMemberShips.put(LOGIN2, ROLE1NAME);
    expectedMemberShips.put(LOGIN1, ROLE2NAME);
    expectedMemberShips.put(LOGIN3, ROLE2NAME);

    HashMap<String, String> currentMemberShips = new HashMap<String, String>();
    for (Membership membership : allMemberships)
    {
      String actorLogin = membership.getActor().getLogin();
      String roleName = membership.getRole().getName();
      currentMemberShips.put(actorLogin, roleName);
    }
    assertEquals(expectedMemberShips, currentMemberShips);

  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#getAllEffectiveUserMembershipsForUserAndProject(java.lang.String, java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testGetAllEffectiveUserMembershipsForUserAndProject() throws Exception
  {
    // prepare for group to get users (user3) believing only to a group and not registered as user membership.
    // We expect to get all user memberships including those added to groups for the selected project and
    // user.
    prepareForGroupMembership();

    final HashSet<String> rolesNameForLogin1 = new HashSet<String>();
    rolesNameForLogin1.add(ROLE1NAME);
    rolesNameForLogin1.add(ROLE2NAME);

    final HashSet<String> rolesNameForLogin2 = new HashSet<String>();
    rolesNameForLogin2.add(ROLE1NAME);

    // associate user members for the project
    membershipService.addUserMembership(PROJECT1ID, LOGIN1, rolesNameForLogin1, ROLE1NAME, NAME1, false);
    membershipService.addUserMembership(PROJECT1ID, LOGIN2, rolesNameForLogin2, ROLE1NAME, NAME2, false);

    // associate group members and for the project
    final HashSet<String> rolesName1 = new HashSet<String>();
    rolesName1.add(ROLE1NAME);
    final HashSet<String> rolesName2 = new HashSet<String>();
    rolesName2.add(ROLE2NAME);

    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME1, rolesName1, ROLE1NAME, NAME1);
    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME2, rolesName2, ROLE2NAME, NAME1);

    List<Membership> allMemberships = membershipService.getAllEffectiveUserMembershipsForUserAndProject(
        PROJECT1ID, LOGIN1);
    // **** user membership from users:
    // membership =MembershipEntity [login=name1-f, priority=false, projectId=project1,
    // rolename=Administrator]
    // membership =MembershipEntity [login=name1-f, priority=true, projectId=project1, rolename=roleName1]
    // membership =MembershipEntity [login=name1-f, priority=false, projectId=project1, rolename=roleName2]
    // **** user member ships from groups:
    // membership =MembershipEntity [login=name1-f, priority=false, projectId=project1, rolename=roleName1]
    // membership =MembershipEntity [login=name1-f, priority=false, projectId=project1, rolename=roleName2]
    assertNotNull(allMemberships);
    assertEquals(5, allMemberships.size());

    // **** check rolenames
    HashMap<String, String> expectedMemberShips = new HashMap<String, String>();
    // user memberships for user
    expectedMemberShips.put(LOGIN1, ADMINISTRATOR);
    expectedMemberShips.put(LOGIN1, ROLE1NAME);
    expectedMemberShips.put(LOGIN1, ROLE2NAME);

    // user memberships for users into groups
    expectedMemberShips.put(LOGIN1, ROLE1NAME);
    expectedMemberShips.put(LOGIN1, ROLE2NAME);

    HashMap<String, String> currentMemberShips = new HashMap<String, String>();
    for (Membership membership : allMemberships)
    {
      String actorLogin = membership.getActor().getLogin();
      String roleName = membership.getRole().getName();
      currentMemberShips.put(actorLogin, roleName);
    }
    assertEquals(expectedMemberShips, currentMemberShips);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#getAllActorsForRole(java.lang.String, java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testGetAllActorsForRole() throws Exception
  {
    prepareForGroupMembership();

    final HashSet<String> rolesNameForLogin1 = new HashSet<String>();
    rolesNameForLogin1.add(ROLE1NAME);
    rolesNameForLogin1.add(ROLE2NAME);

    final HashSet<String> rolesNameForLogin2 = new HashSet<String>();
    rolesNameForLogin2.add(ROLE1NAME);

    // associate user members for the project
    membershipService.addUserMembership(PROJECT1ID, LOGIN1, rolesNameForLogin1, ROLE1NAME, NAME1, false);
    membershipService.addUserMembership(PROJECT1ID, LOGIN2, rolesNameForLogin2, ROLE1NAME, NAME2, false);

    // associate group members and for the project
    final HashSet<String> rolesName1 = new HashSet<String>();
    rolesName1.add(ROLE1NAME);
    final HashSet<String> rolesName2 = new HashSet<String>();
    rolesName2.add(ROLE2NAME);

    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME1, rolesName1, ROLE1NAME, NAME1);
    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME2, rolesName2, ROLE2NAME, NAME1);

    // get all actors for role
    List<Actor> actors = membershipService.getAllActorsForRole(PROJECT1ID, ROLE1NAME);

    // **** check actors
    assertEquals(3, actors.size());
    ArrayList<String> expectedActors = new ArrayList<String>();
    // user memberships for user
    expectedActors.add(LOGIN1);
    expectedActors.add(LOGIN2);
    expectedActors.add(GROUPNAME1);

    ArrayList<String> currentActors = new ArrayList<String>();
    for (Actor actor : actors)
    {
      currentActors.add(actor.getLogin());
    }
    assertEquals(expectedActors, currentActors);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#getAllEffectiveRolesForUser(java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testGetAllEffectiveRolesForUser() throws Exception
  {
    // prepare for group to get users (user3) believing only to a group and not registered as user membership.
    // We expect to get all user memberships including those added to groups for the selected project and
    // user.
    prepareForGroupMembership();

    final HashSet<String> rolesNameForLogin1 = new HashSet<String>();
    rolesNameForLogin1.add(ROLE1NAME);
    rolesNameForLogin1.add(ROLE2NAME);

    final HashSet<String> rolesNameForLogin2 = new HashSet<String>();
    rolesNameForLogin2.add(ROLE1NAME);

    // associate user members for the project
    membershipService.addUserMembership(PROJECT1ID, LOGIN1, rolesNameForLogin1, ROLE1NAME, NAME1, false);
    membershipService.addUserMembership(PROJECT1ID, LOGIN2, rolesNameForLogin2, ROLE1NAME, NAME2, false);

    // associate group members and for the project
    final HashSet<String> rolesName1 = new HashSet<String>();
    rolesName1.add(ROLE1NAME);
    final HashSet<String> rolesName2 = new HashSet<String>();
    rolesName2.add(ROLE2NAME);

    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME1, rolesName1, ROLE1NAME, NAME1);
    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME2, rolesName2, ROLE2NAME, NAME1);

    Map<String, List<ProjectRole>> roles = membershipService.getAllEffectiveRolesForUser(LOGIN1);

    // check
    TreeSet<String> currentProject1Actors = new TreeSet<String>();
    TreeSet<String> currentForgeActors = new TreeSet<String>();

    for (String key : roles.keySet())
    {
      if (key.equals(PROJECT1ID))
        for (ProjectRole proRole : roles.get(key))
        {
          currentProject1Actors.add(proRole.getName());
        }
      else if (key.equals(FORGEID))
        for (ProjectRole proRole : roles.get(key))
        {
          currentForgeActors.add(proRole.getName());
        }
    }
    // PROJECT1ID
    // role =RoleEntity [id=304,name=Administrator,description=null,order=1]
    // role =RoleEntity [id=305,name=roleName1,description=roleDesc1,order=2]
    // role =RoleEntity [id=306,name=roleName2,description=roleDesc2,order=3]

    // FORGE
    // RoleEntity [id=303,name=Member,description=null,order=3]

    assertEquals(3, currentProject1Actors.size());
    assertEquals(1, currentForgeActors.size());

    TreeSet<String> expectedProject1Actors = new TreeSet<String>();
    expectedProject1Actors.add(ROLE1NAME);
    expectedProject1Actors.add(ROLE2NAME);
    expectedProject1Actors.add(ADMINISTRATOR);

    TreeSet<String> expectedForgeActors = new TreeSet<String>();
    expectedForgeActors.add(MEMBER);

    assertEquals(expectedProject1Actors, currentProject1Actors);
    assertEquals(expectedForgeActors, currentForgeActors);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#getValidatedProjects(java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testGetValidatedProjects() throws Exception
  {
    prepareForGroupMembership();

    // create project2 (and not validate) with user1 as project administrator
    final Project project2 = createProject(PROJECT2ID, PROJECT2NAME, LGPL, PROJECT_DESCRIPTION2, false);
    projectService.createProject(project2, LOGIN1, null);

    List<Project> validatedProjects = membershipService.getValidatedProjects(LOGIN1);
    assertNotNull(validatedProjects);

    ArrayList<String> expectedProjectsId = new ArrayList<String>();
    ArrayList<String> currentProjectsId = new ArrayList<String>();
    expectedProjectsId.add(FORGEID);
    expectedProjectsId.add(PROJECT1ID);
    for (Project project : validatedProjects)
    {
      currentProjectsId.add(project.getProjectId());
    }
    assertEquals(expectedProjectsId, currentProjectsId);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#getPublicProjects(java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testGetPublicProjects() throws Exception
  {
    prepareForGroupMembership();

    // create project2
    final Project project2 = createProject(PROJECT2ID, PROJECT2NAME, LGPL, PROJECT_DESCRIPTION2, true);
    projectService.createProject(project2, LOGIN1, null);
    projectService.validateProject(PROJECT2ID);

    List<Project> publicProjects = membershipService.getPublicProjects(LOGIN1);

    // check
    assertNotNull(publicProjects);
    assertEquals(2, publicProjects.size());
    ArrayList<String> expectedProjectsId = new ArrayList<String>();
    ArrayList<String> currentProjectsId = new ArrayList<String>();
    expectedProjectsId.add(FORGEID);
    expectedProjectsId.add(PROJECT1ID);
    for (Project project : publicProjects)
    {
      currentProjectsId.add(project.getProjectId());
    }
    assertEquals(expectedProjectsId, currentProjectsId);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.MembershipServiceImpl#getEffectiveMembershipsForUserAndProject(java.lang.String, java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testGetEffectiveMembershipsForUserAndProject() throws Exception
  {
    // prepare for group to get users (user3) believing only to a group and not registered as user membership.
    // We expect to get all user memberships including those added to groups.
    prepareForGroupMembership();

    final HashSet<String> rolesNameForLogin1 = new HashSet<String>();
    rolesNameForLogin1.add(ROLE1NAME);
    rolesNameForLogin1.add(ROLE2NAME);

    final HashSet<String> rolesNameForLogin2 = new HashSet<String>();
    rolesNameForLogin2.add(ROLE1NAME);

    // associate user members for the project
    membershipService.addUserMembership(PROJECT1ID, LOGIN1, rolesNameForLogin1, ROLE1NAME, NAME1, false);
    membershipService.addUserMembership(PROJECT1ID, LOGIN2, rolesNameForLogin2, ROLE1NAME, NAME2, false);

    // associate group members and for the project
    final HashSet<String> rolesName1 = new HashSet<String>();
    rolesName1.add(ROLE1NAME);
    final HashSet<String> rolesName2 = new HashSet<String>();
    rolesName2.add(ROLE2NAME);

    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME1, rolesName1, ROLE1NAME, NAME1);
    membershipService.addGroupMembership(PROJECT1ID, GROUPNAME2, rolesName2, ROLE2NAME, NAME1);

    List<Membership> allMemberships = membershipService.getAllEffectiveUserMembershipsForUserAndProject(
        PROJECT1ID, LOGIN1);
    // **** user membership from users:
    // membership =MembershipEntity [login=name1-f, priority=false, projectId=project1,
    // rolename=Administrator]
    // membership =MembershipEntity [login=name1-f, priority=true, projectId=project1, rolename=roleName1]
    // membership =MembershipEntity [login=name1-f, priority=false, projectId=project1, rolename=roleName2]
    // **** user member ships from groups:
    // membership =MembershipEntity [login=name1-f, priority=false, projectId=project1, rolename=roleName1]
    // membership =MembershipEntity [login=name1-f, priority=false, projectId=project1, rolename=roleName2]
    assertNotNull(allMemberships);
    assertEquals(5, allMemberships.size());

    // **** check rolenames
    HashMap<String, String> expectedMemberShips = new HashMap<String, String>();
    // user memberships for user
    expectedMemberShips.put(LOGIN1, ADMINISTRATOR);
    expectedMemberShips.put(LOGIN1, ROLE1NAME);
    expectedMemberShips.put(LOGIN1, ROLE2NAME);

    // user memberships for users into groups
    expectedMemberShips.put(LOGIN1, ROLE1NAME);
    expectedMemberShips.put(LOGIN1, ROLE2NAME);

    HashMap<String, String> currentMemberShips = new HashMap<String, String>();
    for (Membership membership : allMemberships)
    {
      String actorLogin = membership.getActor().getLogin();
      String roleName = membership.getRole().getName();
      currentMemberShips.put(actorLogin, roleName);
    }
    assertEquals(expectedMemberShips, currentMemberShips);
  }

  // private methods:
  /**
   * @param name
   * @param firstName
   * @param eMail
   * @param password
   * @return
   * @throws LanguageServiceException
   */
  private User createUser(final String name, final String firstName, final String eMail, final String password)
      throws LanguageServiceException
  {
    final User user = commonUserService.newUser();
    assertNotNull(user);
    user.setFirstName(firstName);
    user.setName(name);
    user.setEmail(eMail);
    final Language langFR = languageService.getLanguage(LANGUAGE_FR_NAME);
    user.setLanguage(langFR);
    user.setPassword(password);
    return user;
  }

  /**
   * @param pLogin
   * @param pDescription
   * @param pIsVisible
   * @param PprojectId
   * @param user1
   * @param user2
   * @return
   * @throws GroupServiceException
   * @throws LanguageServiceException
   * @throws UserServiceException
   */
  private Group createGroup(final String pLogin, final String pDescription, final boolean pIsVisible,
      final String PprojectId, final User user1, final User user2) throws GroupServiceException,
      LanguageServiceException, UserServiceException
  {
    final Group newGroup = groupService.newGroup();
    newGroup.setLogin(pLogin);
    newGroup.setDescription(pDescription);
    newGroup.setVisible(pIsVisible);
    newGroup.addUser(user1);
    newGroup.addUser(user2);
    return newGroup;
  }

  /**
   * @param projectId
   * @param projectName
   * @param licence
   * @param description
   * @param privateVisibility
   * @return
   */
  private Project createProject(final String projectId, final String projectName, final String licence,
      final String description, boolean privateVisibility)
  {
    final Project project1 = projectService.newProject();
    project1.setProjectId(projectId);
    project1.setLicenceType(licence);
    project1.setName(projectName);
    project1.setDescription(description);
    project1.setPrivateVisibility(privateVisibility);
    return project1;
  }

  /**
   * @param roleName
   * @param roleDesc
   * @param realmType
   * @return
   */
  private ProjectRole createRole(final String roleName, final String roleDesc, final RealmType realmType)
  {
    final ProjectRole projectRole = projectRoleService.newRole();
    projectRole.setName(roleName);
    projectRole.setDescription(roleDesc);
    projectRole.setRealmType(realmType);
    return projectRole;
  }

  /**
   * @throws LanguageServiceException
   * @throws UserServiceException
   * @throws ProjectServiceException
   * @throws GroupServiceException
   */
  private void prepareForGroupMembership() throws LanguageServiceException, UserServiceException,
      ProjectServiceException, GroupServiceException
  {
    assertNotNull(projectService);
    assertNotNull(projectRoleService);
    // user1 and user2
    assertNotNull(commonUserService);
    assertNotNull(languageService);

    // user1
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    // user2
    final User user2 = createUser(NAME2, FIRSTNAME2, EMAIL2, PASSWORD2);
    commonUserService.createUser(user2);

    // user3
    final User user3 = createUser(NAME3, FIRSTNAME3, EMAIL3, PASSWORD3);
    commonUserService.createUser(user3);

    // create and validate project1 with user1 as project administrator
    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1, false);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // group1
    final boolean isGroupVisible = true;
    final Group group1 = createGroup(GROUPNAME1, GROUPDESC1, isGroupVisible, PROJECT1ID, user1, user2);
    groupService.createGroup(group1, PROJECT1ID);

    // group2
    final Group group2 = createGroup(GROUPNAME2, GROUPDESC2, isGroupVisible, PROJECT1ID, user1, user3);
    groupService.createGroup(group2, PROJECT1ID);

    // create roles
    final ProjectRole projectRole1 = createRole(ROLE1NAME, ROLE1DESC, RealmType.USER);
    projectRoleService.createRole(projectRole1, PROJECT1ID);

    final ProjectRole projectRole2 = createRole(ROLE2NAME, ROLE2DESC, RealmType.USER);
    projectRoleService.createRole(projectRole2, PROJECT1ID);

    final ProjectRole projectRole3 = createRole(ROLE3NAME, ROLE3DESC, RealmType.USER);
    projectRoleService.createRole(projectRole3, PROJECT1ID);
  }

  /**
   * @throws LanguageServiceException
   * @throws UserServiceException
   * @throws ProjectServiceException
   */
  private void prepareForUserMembership() throws LanguageServiceException, UserServiceException,
      ProjectServiceException
  {
    assertNotNull(projectService);
    assertNotNull(projectRoleService);
    // user1 and user2
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    final User user2 = createUser(NAME2, FIRSTNAME2, EMAIL2, PASSWORD2);
    commonUserService.createUser(user2);

    // create project1 with user1 as administrator
    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1, false);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // create roles
    final ProjectRole projectRole1 = createRole(ROLE1NAME, ROLE1DESC, RealmType.USER);
    projectRoleService.createRole(projectRole1, PROJECT1ID);

    final ProjectRole projectRole2 = createRole(ROLE2NAME, ROLE2DESC, RealmType.USER);
    projectRoleService.createRole(projectRole2, PROJECT1ID);
    
    final ProjectRole projectRole3 = createRole(ROLE3NAME, ROLE3DESC, RealmType.USER);
    projectRoleService.createRole(projectRole3, PROJECT1ID);
  }

}
