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

package org.novaforge.forge.ui.distribution.shared.DTO.reporting;

import org.novaforge.forge.ui.distribution.shared.enumeration.StatusDataAccessEnum;

import java.io.Serializable;
import java.util.Map;

/**
 * @author BILET-JC
 *
 */
public class ProfilViewDTO implements Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = 4411079312697719057L;

   private String forgeName;
   private String projectName;
   private Map<String, Integer> roles;
   private StatusDataAccessEnum status;

   /**
    * 
    */
   public ProfilViewDTO()
   {
      super();
   }


   /**
    * @param forgeName
    * @param projectName
    * @param roles
    * @param status
    */
   
   public ProfilViewDTO(String forgeName, String projectName, Map<String, Integer> roles,
         StatusDataAccessEnum status)
   {
      super();
      this.forgeName = forgeName;
      this.projectName = projectName;
      this.roles = roles;
      this.status = status;
   }


   /**
    * @return the forgeName
    */
   public String getForgeName()
   {
      return forgeName;
   }

   /**
    * @param forgeName the forgeName to set
    */
   public void setForgeName(String forgeName)
   {
      this.forgeName = forgeName;
   }

   /**
    * @return the projectName
    */
   public String getProjectName()
   {
      return projectName;
   }

   /**
    * @param projectName the projectName to set
    */
   public void setProjectName(String projectName)
   {
      this.projectName = projectName;
   }

   /**
    * @return the roles
    */
   public Map<String, Integer> getRoles()
   {
      return roles;
   }

   /**
    * @param roles the roles to set
    */
   public void setRoles(Map<String, Integer> roles)
   {
      this.roles = roles;
   }


   /**
    * @return the status
    */
   public StatusDataAccessEnum getStatus()
   {
      return status;
   }

   /**
    * @param status
    *           the status to set
    */
   public void setStatus(StatusDataAccessEnum status)
   {
      this.status = status;
   }

}
