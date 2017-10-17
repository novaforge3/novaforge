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
import org.novaforge.forge.core.organization.entity.ProjectEntity;
import org.novaforge.forge.core.organization.entity.ProjectRoleEntity;
import org.novaforge.forge.core.organization.entity.RoleEntity;
import org.novaforge.forge.core.organization.model.Role;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RoleDAOImplTest extends OrganizationJPATestCase
{

  private RoleDAOImpl roleDAO;

  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    roleDAO = new RoleDAOImpl();
    roleDAO.setEntityManager(em);
  }

  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    roleDAO = null;
  }

  @Test
  public void testNewRole()
  {
    final Role result = roleDAO.newRole();
    assertNotNull(result);
  }

  @Test
  public void testFindAllRole()
  {
    final String elementId1 = "element1";
    final ProjectEntity projectElement1 = DataCreator.createProjectElement(elementId1);

    final String elementId2 = "element2";
    final ProjectEntity projectElement2 = DataCreator.createProjectElement(elementId2);

    final String roleName1 = "roleName1";
    final ProjectRoleEntity role1 = DataCreator.createRole(roleName1, 0);
    final String roleName2 = "roleName2";
    final ProjectRoleEntity role2 = DataCreator.createRole(roleName2, 0);
    final String roleName3 = "roleName3";
    final ProjectRoleEntity role3 = DataCreator.createRole(roleName3, 0);

    em.getTransaction().begin();
    final ProjectEntity createdProjElem1 = em.merge(projectElement1);
    createdProjElem1.addRole(role1);
    createdProjElem1.addRole(role2);
    em.merge(createdProjElem1);

    final ProjectEntity createdProjElem2 = em.merge(projectElement2);
    createdProjElem2.addRole(role3);
    em.merge(createdProjElem2);
    em.flush();
    em.getTransaction().commit();

    final List<Role> result = roleDAO.findAllRole(elementId1);
    assertNotNull(result);
    assertThat(result.size(), is(2));
    final Role r1 = result.get(0);
    assertTrue(roleName1.equals(r1.getName()) || roleName2.equals(r1.getName()));

    final Role r2 = result.get(1);
    assertTrue(roleName1.equals(r2.getName()) || roleName2.equals(r2.getName()));

  }

  @Test
  public void testFindByNameAndElement()
  {
    final String elementId1 = "element1";
    final ProjectEntity projectElement1 = DataCreator.createProjectElement(elementId1);

    final String elementId2 = "element2";
    final ProjectEntity projectElement2 = DataCreator.createProjectElement(elementId2);

    final String roleName1 = "roleName1";
    final RoleEntity role1 = DataCreator.createRole(roleName1, 0);
    final String roleName2 = "roleName2";
    final RoleEntity role2 = DataCreator.createRole(roleName2, 0);
    final String roleName3 = "roleName3";
    final RoleEntity role3 = DataCreator.createRole(roleName3, 0);

    em.getTransaction().begin();
    final ProjectEntity createdProjElem1 = em.merge(projectElement1);
    role1.setElement(createdProjElem1);
    em.merge(role1);
    role2.setElement(createdProjElem1);
    em.merge(role2);

    final ProjectEntity createdProjElem2 = em.merge(projectElement2);
    role3.setElement(createdProjElem2);
    em.merge(role3);
    em.getTransaction().commit();

    final Role result = roleDAO.findByNameAndElement(elementId1, roleName1);
    assertNotNull(result);
    assertThat(result.getName(), is(roleName1));

  }

  @Test
  public void testGetMaxOrder()
  {
    final String elementId1 = "element1";
    final ProjectEntity projectElement1 = DataCreator.createProjectElement(elementId1);

    final String roleName1 = "roleName1";
    final ProjectRoleEntity role1 = DataCreator.createRole(roleName1, 1);
    final String roleName2 = "roleName2";
    final ProjectRoleEntity role2 = DataCreator.createRole(roleName2, 2);
    final String roleName3 = "roleName3";
    final ProjectRoleEntity role3 = DataCreator.createRole(roleName3, 5);

    em.getTransaction().begin();
    final ProjectEntity createdProjElem1 = em.merge(projectElement1);
    createdProjElem1.addRole(role1);
    createdProjElem1.addRole(role2);
    createdProjElem1.addRole(role3);
    em.merge(createdProjElem1);
    em.flush();
    em.getTransaction().commit();

    final Integer result = roleDAO.getMaxOrder(elementId1);
    assertNotNull(result);
    assertThat(result, is(5));

  }

  @Test
  public void testUpdateOrdersBiggerThan()
  {
    final String elementId1 = "element1";
    final ProjectEntity projectElement1 = DataCreator.createProjectElement(elementId1);

    final String roleName1 = "roleName1";
    final RoleEntity role1 = DataCreator.createRole(roleName1, 1);
    final String roleName2 = "roleName2";
    final RoleEntity role2 = DataCreator.createRole(roleName2, 2);
    final String roleName3 = "roleName3";
    final RoleEntity role3 = DataCreator.createRole(roleName3, 5);

    em.getTransaction().begin();
    final ProjectEntity createdProjElem1 = em.merge(projectElement1);
    role1.setElement(createdProjElem1);
    final RoleEntity createdRole1 = em.merge(role1);
    role2.setElement(createdProjElem1);
    final RoleEntity createdRole2 = em.merge(role2);
    role3.setElement(createdProjElem1);
    final RoleEntity createdRole3 = em.merge(role3);

    // The Test
    roleDAO.updateOrdersBiggerThan(elementId1, new Integer(1));
    em.getTransaction().commit();

    em.find(RoleEntity.class, createdRole1.getId());
    em.find(RoleEntity.class, createdRole2.getId());
    em.find(RoleEntity.class, createdRole3.getId());

  }

  @Test
  public void testFindByElementAndOrder()
  {
    // Not yet implemented
  }

  @Test
  public void testExistRole()
  {
    final String elementId1 = "element1";
    final ProjectEntity projectElement1 = DataCreator.createProjectElement(elementId1);

    final String elementId2 = "element2";
    final ProjectEntity projectElement2 = DataCreator.createProjectElement(elementId2);

    final String roleName1 = "roleName1";
    final RoleEntity role1 = DataCreator.createRole(roleName1, 0);
    final String roleName2 = "roleName2";
    final RoleEntity role2 = DataCreator.createRole(roleName2, 0);
    final String roleName3 = "roleName3";
    final RoleEntity role3 = DataCreator.createRole(roleName3, 0);

    em.getTransaction().begin();
    final ProjectEntity createdProjElem1 = em.merge(projectElement1);
    role1.setElement(createdProjElem1);
    em.merge(role1);
    role2.setElement(createdProjElem1);
    em.merge(role2);

    final ProjectEntity createdProjElem2 = em.merge(projectElement2);
    role3.setElement(createdProjElem2);
    em.merge(role3);
    em.getTransaction().commit();

    final boolean result = roleDAO.existRole(elementId1, roleName1);
    assertNotNull(result);
    assertTrue(result);
  }

  @Test
  public void testChangeOrder()
  {
    // Not yet implemented
  }

  @Test
  public void testUpdateRole()
  {
    // Not yet implemented
  }

  @Test
  public void testDeleteRole()
  {
    // Not yet implemented
  }

  @Test
  public void testFindByName()
  {
    // Not yet implemented
  }

  @Test
  public void testNewPermission()
  {
    // Not yet implemented
  }

  @Test
  public void testExistPermission()
  {
    // Not yet implemented
  }

  @Test
  public void testPersist()
  {
    // Not yet implemented
  }

  @Test
  public void testUpdatePermission()
  {
    // Not yet implemented
  }

  @Test
  public void testDeletePermission()
  {
    // Not yet implemented
  }

}
