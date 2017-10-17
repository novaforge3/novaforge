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
package org.novaforge.forge.core.organization.internal.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.core.organization.entity.GroupEntity;
import org.novaforge.forge.core.organization.entity.ProjectEntity;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.User;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class GroupDAOImplTest extends OrganizationJPATestCase
{

  private GroupDAOImpl groupDAO;

  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    groupDAO = new GroupDAOImpl();
    groupDAO.setEntityManager(em);
  }

  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    groupDAO = null;
  }

  @Test
  public void testFindByNameAndProjectIdNoResult()
  {
    final String name1 = "name1";
    final GroupEntity group1 = DataCreator.createGroup(name1, true);
    final String name2 = "name2";
    final GroupEntity group2 = DataCreator.createGroup(name2, true);
    final String name3 = "name3";
    final GroupEntity group3 = DataCreator.createGroup(name3, false);

    final String projectId = "project1";
    final ProjectEntity project = DataCreator.createProjectElement(projectId);
    final String projectId2 = "project2";
    final ProjectEntity project2 = DataCreator.createProjectElement(projectId2);
    em.getTransaction().begin();
    final ProjectEntity createdProject = em.merge(project);
    final ProjectEntity createdProject2 = em.merge(project2);

    createdProject.addGroup(group1);
    createdProject.addGroup(group2);
    createdProject2.addGroup(group3);
    em.merge(createdProject);
    em.merge(createdProject2);
    em.flush();
    em.getTransaction().commit();

    try
    {
      groupDAO.findByProjectIdAndName(projectId, name3);
      fail("Should throw exception");
    }
    catch (final Exception e)
    {
      // ok
    }
  }

  @Test
  public void testFindByNameAndProjectId()
  {
    final String projectId = "project1";
    final ProjectEntity project = DataCreator.createProjectElement(projectId);

    final String name1 = "name1";
    final GroupEntity group1 = DataCreator.createGroup(name1, true);
    final String name2 = "name2";
    final GroupEntity group2 = DataCreator.createGroup(name2, true);
    final String name3 = "name3";
    final GroupEntity group3 = DataCreator.createGroup(name3, false);

    project.addGroup(group1);
    project.addGroup(group2);
    project.addGroup(group3);

    em.getTransaction().begin();
    em.merge(project);
    em.getTransaction().commit();

    // Test 2
    final Group result = groupDAO.findByProjectIdAndName(projectId, name2);
    assertNotNull(result);
    assertThat(result.getName(), is(name2));

  }

  @Test
  public void testFindByVisibility()
  {
    final String projectId = "project1";
    final ProjectEntity project = DataCreator.createProjectElement(projectId);
    final String name1 = "name1";
    final GroupEntity group1 = DataCreator.createGroup(name1, true);
    final String name2 = "name2";
    final GroupEntity group2 = DataCreator.createGroup(name2, false);
    final String name3 = "name3";
    final GroupEntity group3 = DataCreator.createGroup(name3, true);

    project.addGroup(group1);
    project.addGroup(group2);
    project.addGroup(group3);

    em.getTransaction().begin();
    em.merge(project);
    em.getTransaction().commit();

    final List<Group> result = groupDAO.findByVisibility(true);
    assertNotNull(result);
    assertThat(result.size(), is(2));

    final Group g1 = result.get(0);
    assertTrue(name1.equals(g1.getName()) || name3.equals(g1.getName()));
    final Group g2 = result.get(1);
    assertTrue(name1.equals(g2.getName()) || name3.equals(g2.getName()));

  }

  @Test
  public void testExistGroup()
  {
    final String projectId = "project1";
    final ProjectEntity project = DataCreator.createProjectElement(projectId);
    final ProjectEntity createdProject = em.merge(project);
    final String projectId2 = "project2";
    final ProjectEntity project2 = DataCreator.createProjectElement(projectId2);
    final ProjectEntity createdProject2 = em.merge(project2);

    final String name1 = "name1";
    final GroupEntity group1 = DataCreator.createGroup(name1, true);
    final String name2 = "name2";
    final GroupEntity group2 = DataCreator.createGroup(name2, false);

    createdProject.addGroup(group1);
    createdProject2.addGroup(group2);
    em.getTransaction().begin();
    em.merge(createdProject);
    em.getTransaction().commit();

    final boolean result1 = groupDAO.existGroup(projectId, name1);
    assertThat(result1, is(true));
    final boolean result2 = groupDAO.existGroup(projectId, name2);
    assertThat(result2, is(false));
  }

  @Test
  public void testFindAllGroupsForUser()
  {
    final String projectId = "project1";
    final ProjectEntity project = DataCreator.createProjectElement(projectId);

    final String name1 = "name1";
    final GroupEntity group1 = DataCreator.createGroup(name1, true);
    final String name2 = "name2";
    final GroupEntity group2 = DataCreator.createGroup(name2, false);
    final String name3 = "name3";
    final GroupEntity group3 = DataCreator.createGroup(name3, true);

    final String userLogin = "userLogin";
    final User user = DataCreator.createUser(userLogin);

    em.getTransaction().begin();
    final ProjectEntity merge = em.merge(project);

    merge.addGroup(group1);
    merge.addGroup(group2);
    merge.addGroup(group3);
    em.merge(merge);
    final User createdUser = em.merge(user);
    em.getTransaction().commit();
    em.getTransaction().begin();
    final Group group3Merged = groupDAO.findByProjectIdAndName(projectId, group2.getName());
    final Group group2Merged = groupDAO.findByProjectIdAndName(projectId, group3.getName());
    group2Merged.addUser(createdUser);
    groupDAO.update(group2Merged);
    group3Merged.addUser(createdUser);
    groupDAO.update(group3Merged);
    em.getTransaction().commit();

    // The test
    final List<Group> result = groupDAO.findGroupsForUser(createdUser.getUuid());
    assertNotNull(result);
    assertThat(result.size(), is(2));

    final Group g1 = result.get(0);
    assertTrue(name2.equals(g1.getName()) || name3.equals(g1.getName()));

    final Group g2 = result.get(1);
    assertTrue(name2.equals(g2.getName()) || name3.equals(g2.getName()));

  }

  @Test
  public void testFindByProjectWithPublic()
  {
    final String name1 = "name1";
    final GroupEntity group1 = DataCreator.createGroup(name1, true);
    final String name2 = "name2";
    final GroupEntity group2 = DataCreator.createGroup(name2, true);
    final String name3 = "name3";
    final GroupEntity group3 = DataCreator.createGroup(name3, false);

    final String projectId = "project1";
    final ProjectEntity project = DataCreator.createProjectElement(projectId);

    project.addGroup(group1);
    project.addGroup(group2);
    project.addGroup(group3);

    try
    {
      em.getTransaction().begin();

      em.merge(project);

      em.getTransaction().commit();

      // The test
      final List<Group> result = groupDAO.findByProjectWithPublic(projectId);
      assertNotNull(result);
      assertThat(result.size(), is(3));

    }
    catch (final Exception e)
    {
      e.printStackTrace();
      fail("Should not throw exception: " + e.getMessage());
    }

  }

  @Test
  public void testFindByProjectWithoutPublic()
  {
    final String projectId = "project1";
    final ProjectEntity project = DataCreator.createProjectElement(projectId);
    final String projectId2 = "project2";
    final ProjectEntity project2 = DataCreator.createProjectElement(projectId2);

    final String name1 = "name1";
    final GroupEntity group1 = DataCreator.createGroup(name1, true);
    final String name2 = "name2";
    final GroupEntity group2 = DataCreator.createGroup(name2, false);
    final String name3 = "name3";
    final GroupEntity group3 = DataCreator.createGroup(name3, true);

    project.addGroup(group1);
    project.addGroup(group2);
    project2.addGroup(group3);

    try
    {
      em.getTransaction().begin();
      em.merge(project);
      em.merge(project2);
      em.getTransaction().commit();

      // The test
      final List<Group> result = groupDAO.findByProjectWithoutPublic(projectId);
      assertNotNull(result);
      assertThat(result.size(), is(2));

      final Group g1 = result.get(0);
      assertTrue(name1.equals(g1.getName()) || name2.equals(g1.getName()));

      final Group g2 = result.get(1);
      assertTrue(name1.equals(g2.getName()) || name2.equals(g2.getName()));

    }
    catch (final Exception e)
    {
      e.printStackTrace();
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  @Test
  public void testUpdate()
  {
    final String projectId = "project1";
    final ProjectEntity project = DataCreator.createProjectElement(projectId);
    final String name = "name";
    final String updatedName = "updated name";
    final GroupEntity group = DataCreator.createGroup(name, false);
    em.getTransaction().begin();
    final ProjectEntity merge = em.merge(project);
    merge.addGroup(group);
    em.merge(merge);
    em.getTransaction().commit();
    // Test tested case
    group.setName(updatedName);
    em.getTransaction().begin();
    final Group updated = groupDAO.update(group);
    em.getTransaction().commit();

    assertNotNull(updated);
    assertThat(updated.getName(), is(updatedName));

  }

}
