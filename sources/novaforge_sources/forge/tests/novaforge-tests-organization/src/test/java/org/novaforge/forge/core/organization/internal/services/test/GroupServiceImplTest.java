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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.novaforge.forge.core.organization.exceptions.GroupServiceException;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.services.CommonUserService;
import org.novaforge.forge.core.organization.services.GroupService;
import org.novaforge.forge.core.organization.services.LanguageService;
import org.novaforge.forge.core.organization.services.ProjectService;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;

/**
 * @author blachonm
 */
@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class GroupServiceImplTest extends BaseUtil
{
  private static final String LANGUAGE_FR_NAME     = "FR";

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

  // group1
  private static final String GROUPNAME1           = "group1";
  private static final String GROUPDESC1           = "'group description 1";

  // group1
  private static final String GROUPNAME2           = "group2";
  private static final String GROUPDESC2           = "'group description 2";

  // project1
  private static final String PROJECT1NAME         = "project 1";
  private static final String PROJECT_DESCRIPTION1 = "description 1";
  private static final String PROJECT1ID           = "project1";
  private static final String LGPL                 = "LGPL";

  @Inject
  private ProjectService      projectService;

  @Inject
  private CommonUserService   commonUserService;

  @Inject
  private LanguageService     languageService;

  @Inject
  private GroupService        groupService;

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.GroupServiceImpl#createGroup(org.novaforge.forge.core.organization.model.Group, java.lang.String)}
   * .
   * 
   * @throws GroupServiceException
   * @throws LanguageServiceException
   * @throws UserServiceException
   * @throws ProjectServiceException
   */
  @Test
  // @Ignore
  public void testCreateGroup() throws GroupServiceException, LanguageServiceException, UserServiceException,
      ProjectServiceException
  {
    assertNotNull(projectService);
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    assertNotNull(groupService);

    // user1, user2
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);
    final User user2 = createUser(NAME2, FIRSTNAME2, EMAIL2, PASSWORD2);
    commonUserService.createUser(user2);

    // project1
    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // group1
    final boolean isGroupVisible = true;
    final Group newGroup = createGroup(GROUPNAME1, GROUPDESC1, isGroupVisible, PROJECT1ID, user1, user2);
    groupService.createGroup(newGroup, PROJECT1ID);
    final Group createdGroup = groupService.getGroup(PROJECT1ID, GROUPNAME1);

    // check
    assertNotNull(createdGroup);
    assertEquals(GROUPNAME1, createdGroup.getLogin());
    assertEquals(GROUPDESC1, createdGroup.getDescription());
    assertTrue(createdGroup.isVisible());
    assertEquals(2, createdGroup.getUsers().size());

    // check raised exception when
    // the group already exists for the project
    try
    {
      final Group alreadyCreatedGroup = createGroup(GROUPNAME1, GROUPDESC1, isGroupVisible, PROJECT1ID,
          user1, user2);
      groupService.createGroup(alreadyCreatedGroup, PROJECT1ID);
      fail("should has raised an exception");
    }
    catch (final Exception pe)
    {
      System.out.println("expected exception!");
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.GroupServiceImpl#getGroup(java.lang.String, java.lang.String)}
   * .
   * 
   * @throws LanguageServiceException
   * @throws UserServiceException
   * @throws ProjectServiceException
   * @throws GroupServiceException
   */
  @Test
  // @Ignore
  public void testGetGroup() throws LanguageServiceException, UserServiceException, ProjectServiceException,
      GroupServiceException
  {
    assertNotNull(projectService);
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    assertNotNull(groupService);

    // user1, user2
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);
    final User user2 = createUser(NAME2, FIRSTNAME2, EMAIL2, PASSWORD2);
    commonUserService.createUser(user2);

    // project1
    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // group1
    final boolean isGroupVisible = true;
    final Group newGroup = createGroup(GROUPNAME1, GROUPDESC1, isGroupVisible, PROJECT1ID, user1, user2);
    groupService.createGroup(newGroup, PROJECT1ID);

    // get group
    final Group createdGroup = groupService.getGroup(PROJECT1ID, GROUPNAME1);

    // check
    assertNotNull(createdGroup);
    assertEquals(GROUPNAME1, createdGroup.getLogin());
    assertEquals(GROUPDESC1, createdGroup.getDescription());
    assertTrue(createdGroup.isVisible());
    assertEquals(2, createdGroup.getUsers().size());
    final ArrayList<String> expectedUsers = new ArrayList<String>();
    expectedUsers.add(LOGIN1);
    expectedUsers.add(LOGIN2);
    final ArrayList<String> currentUsers = new ArrayList<String>();
    for (final User user : createdGroup.getUsers())
    {
      currentUsers.add(user.getLogin());
    }
    assertEquals(expectedUsers, currentUsers);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.GroupServiceImpl#updateGroup(java.lang.String, java.lang.String, org.novaforge.forge.core.organization.model.Group, java.lang.String)}
   * .
   * 
   * @throws LanguageServiceException
   * @throws UserServiceException
   * @throws ProjectServiceException
   * @throws GroupServiceException
   */
  @Test
  // @Ignore
  public void testUpdateGroup() throws LanguageServiceException, UserServiceException,
      ProjectServiceException, GroupServiceException
  {
    assertNotNull(projectService);
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    assertNotNull(groupService);

    // user1, user2
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);
    final User user2 = createUser(NAME2, FIRSTNAME2, EMAIL2, PASSWORD2);
    commonUserService.createUser(user2);

    // project1
    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // group1
    final boolean isGroupVisible = true;
    final Group newGroup = createGroup(GROUPNAME1, GROUPDESC1, isGroupVisible, PROJECT1ID, user1, user2);
    groupService.createGroup(newGroup, PROJECT1ID);

    // update group
    final Group createdGroup = groupService.getGroup(PROJECT1ID, GROUPNAME1);
    createdGroup.setDescription(GROUPDESC2);
    createdGroup.setLogin(GROUPNAME2);
    createdGroup.clearUsers();
    createdGroup.addUser(user1);
    createdGroup.setVisible(false); // assertTrue(pe.getCode().equals(ExceptionCode.ERR_CREATE_ROLE_ROLENAME_ALREADY_EXIST));
    groupService.updateGroup(PROJECT1ID, GROUPNAME1, createdGroup, LOGIN1);

    // check
    final Group updatedGroup = groupService.getGroup(PROJECT1ID, GROUPNAME2);
    assertNotNull(updatedGroup);
    assertEquals(GROUPNAME2, updatedGroup.getLogin());
    assertEquals(GROUPDESC2, updatedGroup.getDescription());
    assertFalse(updatedGroup.isVisible());
    assertEquals(1, updatedGroup.getUsers().size());
    final ArrayList<String> expectedUsers = new ArrayList<String>();
    expectedUsers.add(LOGIN1);
    final ArrayList<String> currentUsers = new ArrayList<String>();
    for (final User user : updatedGroup.getUsers())
    {
      currentUsers.add(user.getLogin());
    }
    assertEquals(expectedUsers, currentUsers);

    // check raised exception when
    // updating to already existing group
    try
    {
      final Group group1 = createGroup(GROUPNAME1, GROUPDESC1, isGroupVisible, PROJECT1ID, user1, user2);
      groupService.createGroup(group1, PROJECT1ID);
      updatedGroup.setLogin(GROUPNAME1);
      groupService.updateGroup(PROJECT1ID, GROUPNAME2, updatedGroup, LOGIN1);
      fail("should has raised an exception");
    }
    catch (final Exception pe)
    {
      System.out.println("expected exception!");
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.GroupServiceImpl#deleteGroup(java.lang.String, java.lang.String)}
   * .
   * 
   * @throws LanguageServiceException
   * @throws UserServiceException
   * @throws ProjectServiceException
   * @throws GroupServiceException
   */
  @Test
  // @Ignore
  public void testDeleteGroup() throws LanguageServiceException, UserServiceException,
      ProjectServiceException, GroupServiceException
  {
    assertNotNull(projectService);
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    assertNotNull(groupService);

    // user1, user2
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);
    final User user2 = createUser(NAME2, FIRSTNAME2, EMAIL2, PASSWORD2);
    commonUserService.createUser(user2);

    // project1
    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // group1
    final boolean isGroupVisible = true;
    final Group newGroup = createGroup(GROUPNAME1, GROUPDESC1, isGroupVisible, PROJECT1ID, user1, user2);
    groupService.createGroup(newGroup, PROJECT1ID);
    final Group createdGroup = groupService.getGroup(PROJECT1ID, GROUPNAME1);

    // check
    assertNotNull(createdGroup);
    assertEquals(GROUPNAME1, createdGroup.getLogin());
    assertEquals(GROUPDESC1, createdGroup.getDescription());
    assertTrue(createdGroup.isVisible());
    assertEquals(2, createdGroup.getUsers().size());

    // delete
    groupService.deleteGroup(PROJECT1ID, GROUPNAME1);
    final List<Group> groups = groupService.getAllGroups(PROJECT1ID, true);
    assertTrue(groups.size() == 0);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.GroupServiceImpl#getAllGroups(java.lang.String, boolean)}
   * .
   * 
   * @throws LanguageServiceException
   * @throws UserServiceException
   * @throws ProjectServiceException
   * @throws GroupServiceException
   */
  @Test
  // @Ignore
  public void testGetAllGroups() throws LanguageServiceException, UserServiceException,
      ProjectServiceException, GroupServiceException
  {
    assertNotNull(projectService);
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    assertNotNull(groupService);

    // user1, user2
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);
    final User user2 = createUser(NAME2, FIRSTNAME2, EMAIL2, PASSWORD2);
    commonUserService.createUser(user2);

    // project1
    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // group1
    final boolean isGroupVisible = true;
    final Group group1 = createGroup(GROUPNAME1, GROUPDESC1, isGroupVisible, PROJECT1ID, user1, user2);
    groupService.createGroup(group1, PROJECT1ID);

    // group2

    final Group group2 = createGroup(GROUPNAME2, GROUPDESC2, isGroupVisible, PROJECT1ID, user1, user2);
    groupService.createGroup(group2, PROJECT1ID);

    // get groups
    final List<Group> groups = groupService.getAllGroups(PROJECT1ID, true);

    // check
    assertNotNull(groups);
    assertEquals(2, groups.size());
    assertTrue(groups.get(0).getLogin().equals(GROUPNAME1));
    assertTrue(groups.get(1).getLogin().equals(GROUPNAME2));
  }

  // private methods
  /**
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
   * @return
   */
  private Project createProject(final String projectId, final String projectName, final String licence,
      final String description)
  {
    final Project project1 = projectService.newProject();
    project1.setProjectId(PROJECT1ID);
    project1.setLicenceType(LGPL);
    project1.setName(PROJECT1NAME);
    project1.setDescription(PROJECT_DESCRIPTION1);
    return project1;
  }

}
