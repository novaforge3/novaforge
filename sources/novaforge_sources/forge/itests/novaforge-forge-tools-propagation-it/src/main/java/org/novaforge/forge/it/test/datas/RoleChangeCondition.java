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
package org.novaforge.forge.it.test.datas;

/**
 * @author blachonm
 *
 */
public class RoleChangeCondition
{
   private String projectTestId;
   private String userId;
   private String roleToChange;
   private String roleTarget;


   public String getProjectTestId()
   {
      return projectTestId;
   }

   public void setProjectTestId(String projectTestId)
   {
      this.projectTestId = projectTestId;
   }

   public String getUserId()
  {
    return userId;
  }

  public void setUserId(String userId)
  {
    this.userId = userId;
  }

  public String getRoleToChange()
   {
      return roleToChange;
   }

   public void setRoleToChange(String roleToChange)
   {
      this.roleToChange = roleToChange;
   }

   public String getRoleTarget()
   {
      return roleTarget;
   }

   public void setRoleTarget(String roleTarget)
   {
      this.roleTarget = roleTarget;
   }

}
