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
package org.novaforge.forge.tools.deliverymanager.internal.services;

import org.novaforge.forge.tools.deliverymanager.dao.MembershipDAO;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryPermissionServiceException;
import org.novaforge.forge.tools.deliverymanager.model.Membership;
import org.novaforge.forge.tools.deliverymanager.model.Permission;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryPermissionService;

import java.util.Set;

/**
 * @author sbenoist
 */
public class DeliveryPermissionServiceImpl implements DeliveryPermissionService
{
  private static final String EXECUTE_PERMISSION = "edit";
  private MembershipDAO membershipDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canEdit(final String pLogin, final String pProjectId)
      throws DeliveryPermissionServiceException
  {
    try
    {
      boolean can = false;

      // Get the membership of the user on the argued project
      Membership membership = membershipDAO.findByUserAndProject(pLogin, pProjectId);

      // Check if the role has any EXECUTION_PERMISSION
      Set<Permission> permissions = membership.getRole().getPermissions();
      for (Permission permission : permissions)
      {
        if (permission.getName().equals(EXECUTE_PERMISSION))
        {
          can = true;
          break;
        }
      }

      return can;
    }
    catch (Exception e)
    {
      throw new DeliveryPermissionServiceException(String.format(
          "an error occurred during checking permissions with [project=%s, login=%s, permission=%s]",
          pProjectId, pLogin, EXECUTE_PERMISSION), e);
    }
  }

  public void setMembershipDAO(final MembershipDAO pMembershipDAO)
  {
    membershipDAO = pMembershipDAO;
  }

}
