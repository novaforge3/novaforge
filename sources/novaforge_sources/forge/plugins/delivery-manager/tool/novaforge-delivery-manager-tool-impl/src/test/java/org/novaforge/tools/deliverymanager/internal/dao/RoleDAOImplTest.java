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
package org.novaforge.tools.deliverymanager.internal.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.tools.deliverymanager.entity.PermissionEntity;
import org.novaforge.forge.tools.deliverymanager.entity.RoleEntity;
import org.novaforge.forge.tools.deliverymanager.internal.beans.RoleDAOImpl;
import org.novaforge.forge.tools.deliverymanager.model.Role;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author sbenoist
 */
public class RoleDAOImplTest extends DeliveryJPATestCase
{
  private static final String ROLE_NAME          = "my_role";
  private static final String READ_PERMISSION    = "read";
  private static final String WRITE_PERMISSION   = "write";
  private static final String EXECUTE_PERMISSION = "execute";
  private RoleDAOImpl roleDAOImpl;

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    roleDAOImpl = new RoleDAOImpl();
    roleDAOImpl.setEntityManager(em);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    roleDAOImpl = null;
  }

  @Test
  public void testPersist()
  {
    Role roleBuilt = buildRoleWithPermissions();
    em.getTransaction().begin();
    roleDAOImpl.persist(roleBuilt);
    em.getTransaction().commit();

    roleBuilt = roleDAOImpl.findRoleByName(ROLE_NAME);
    assertNotNull(roleBuilt);
    assertNotNull(roleBuilt.getPermissions());
    assertThat(roleBuilt.getPermissions().size(), is(2));
  }

  private Role buildRoleWithPermissions()
  {
    PermissionEntity permissionRead = new PermissionEntity();
    permissionRead.setName(READ_PERMISSION);

    PermissionEntity permissionWrite = new PermissionEntity();
    permissionWrite.setName(WRITE_PERMISSION);

    RoleEntity role = new RoleEntity();
    role.setName(ROLE_NAME);
    role.addPermission(permissionWrite);
    role.addPermission(permissionRead);

    return role;
  }

  @Test
  public void testUpdate()
  {
    Role roleBuilt = buildRoleWithPermissions();
    em.getTransaction().begin();
    roleDAOImpl.persist(roleBuilt);
    em.getTransaction().commit();

    roleBuilt = roleDAOImpl.findRoleByName(ROLE_NAME);
    assertNotNull(roleBuilt);
    assertNotNull(roleBuilt.getPermissions());
    assertThat(roleBuilt.getPermissions().size(), is(2));

    PermissionEntity permissionExec = new PermissionEntity();
    permissionExec.setName(EXECUTE_PERMISSION);
    roleBuilt.addPermission(permissionExec);
    em.getTransaction().begin();
    roleDAOImpl.update(roleBuilt);
    em.getTransaction().commit();

    roleBuilt = roleDAOImpl.findRoleByName(ROLE_NAME);
    assertNotNull(roleBuilt);
    assertNotNull(roleBuilt.getPermissions());
    assertThat(roleBuilt.getPermissions().size(), is(3));
  }

  @Test
  public void testDelete()
  {
    Role roleBuilt = buildRoleWithPermissions();
    em.getTransaction().begin();
    roleDAOImpl.persist(roleBuilt);
    em.getTransaction().commit();

    roleBuilt = roleDAOImpl.findRoleByName(ROLE_NAME);
    assertNotNull(roleBuilt);
    assertNotNull(roleBuilt.getPermissions());
    assertThat(roleBuilt.getPermissions().size(), is(2));

    em.getTransaction().begin();
    roleDAOImpl.delete(roleBuilt);
    em.getTransaction().commit();

    boolean exists = roleDAOImpl.existRole(ROLE_NAME);
    assertThat(exists, is(false));
  }

  @Test
  public void testClearRoles()
  {
    Role roleBuilt = buildRoleWithPermissions();
    em.getTransaction().begin();
    roleDAOImpl.persist(roleBuilt);
    em.getTransaction().commit();

    roleBuilt = roleDAOImpl.findRoleByName(ROLE_NAME);
    assertNotNull(roleBuilt);
    assertNotNull(roleBuilt.getPermissions());
    assertThat(roleBuilt.getPermissions().size(), is(2));

    roleBuilt.clearPermissions();
    em.getTransaction().begin();
    roleDAOImpl.update(roleBuilt);
    em.getTransaction().commit();

    roleBuilt = roleDAOImpl.findRoleByName(ROLE_NAME);
    assertNotNull(roleBuilt);
    assertNotNull(roleBuilt.getPermissions());
    assertThat(roleBuilt.getPermissions().size(), is(0));
  }
}
