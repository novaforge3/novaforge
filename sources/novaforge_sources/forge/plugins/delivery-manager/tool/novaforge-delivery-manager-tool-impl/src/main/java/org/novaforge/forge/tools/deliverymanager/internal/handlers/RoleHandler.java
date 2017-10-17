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
package org.novaforge.forge.tools.deliverymanager.internal.handlers;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.deliverymanager.dao.RoleDAO;
import org.novaforge.forge.tools.deliverymanager.model.Permission;
import org.novaforge.forge.tools.deliverymanager.model.Role;
import org.osgi.framework.BundleContext;

import java.io.File;

/**
 * @author sbenoist
 *         This aim of this class is to create or update roles and permissions at the bundle start
 */
public class RoleHandler
{
  private static final String JSON_NAME  = "name";

  private static final String JSON_PERMS = "permissions";
  private final static String LOCK_FILE = "initialization.lock";
  private static final Log    log       = LogFactory.getLog(RoleHandler.class);
  private RoleDAO             roleDAO;
  private String              roles;
  private BundleContext       bundleContext;

  public void starting()
  {
    try
    {
      File lock = bundleContext.getDataFile(LOCK_FILE);
      if (!lock.exists())
      {
        log.info("initialization of delivery manager roles...");
        createOrUpdateRoles();
        lock.createNewFile();
        log.info("Delivery Manager Initialization : FINISHED SUCCESSFULLY.");
      }
      else
      {
        log.info("initialization of delivery manager roles has already been done. If you want to rebuild it, remove the lock file.");
      }
    }
    catch (Exception e)
    {
      log.error("Unable to initialize delivery manager roles", e);
    }

  }

  private void createOrUpdateRoles() throws Exception
  {
    JSONArray jsonRoles = JSONArray.fromObject(this.roles);
    for (int index = 0; index < jsonRoles.size(); index++)
    {
      JSONObject roleMapping = jsonRoles.getJSONObject(index);

      Role role = getRole(roleMapping.getString(JSON_NAME));

      JSONArray jsonPerms = roleMapping.getJSONArray(JSON_PERMS);
      for (int i = 0; i < jsonPerms.size(); i++)
      {
        Permission permission = getPermission(jsonPerms.getString(i));
        role.addPermission(permission);
      }

      roleDAO.update(role);
    }
  }

  private Role getRole(final String pRoleName)
  {
    Role role = null;
    if (roleDAO.existRole(pRoleName))
    {
      role = roleDAO.update(roleDAO.findRoleByName(pRoleName));
      role.clearPermissions();
    }
    else
    {
      role = roleDAO.newRole();
      role.setName(pRoleName);
    }
    return role;
  }

  private Permission getPermission(final String pPermissionName)
  {
    Permission permission = null;
    if (roleDAO.existPermission(pPermissionName))
    {
      permission = roleDAO.update(roleDAO.findPermissionByName(pPermissionName));
    }
    else
    {
      permission = roleDAO.newPermission();
      permission.setName(pPermissionName);
    }

    return permission;
  }

  public void setRoleDAO(final RoleDAO pRoleDAO)
  {
    roleDAO = pRoleDAO;
  }

  public void setRoles(final String pRoles)
  {
    log.info("Roles for tool-delivery-manager are : " + pRoles);
    roles = pRoles;
  }

  public void setBundleContext(final BundleContext pBundleContext)
  {
    bundleContext = pBundleContext;
  }

}
