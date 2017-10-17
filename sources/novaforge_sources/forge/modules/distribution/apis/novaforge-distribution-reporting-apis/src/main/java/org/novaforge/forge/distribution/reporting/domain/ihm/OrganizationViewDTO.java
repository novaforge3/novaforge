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

package org.novaforge.forge.distribution.reporting.domain.ihm;

import java.io.Serializable;

/**
 * @author BILET-JC
 *
 */
public class OrganizationViewDTO implements Serializable
{

   /**
    * 
    */
   private static final long serialVersionUID = 2569108323654334765L;

   private String organizationName;

   private Integer numberProject;

   private StatusDataAccessEnum status;

   /**
    * 
    */
   public OrganizationViewDTO()
   {
      super();
   }


   /**
    * @param organizationName
    * @param numberProject
    * @param status
    */
   public OrganizationViewDTO(String organizationName, Integer numberProject, StatusDataAccessEnum status)
   {
      super();
      this.organizationName = organizationName;
      this.numberProject = numberProject;
      this.status = status;
   }


   /**
    * @return the organizationName
    */
   public String getOrganizationName()
   {
      return organizationName;
   }

   /**
    * @param organizationName
    *           the organizationName to set
    */
   public void setOrganizationName(String organizationName)
   {
      this.organizationName = organizationName;
   }

   /**
    * @return the numberProject
    */
   public Integer getNumberProject()
   {
      return numberProject;
   }

   /**
    * @param numberProject
    *           the numberProject to set
    */
   public void setNumberProject(Integer numberProject)
   {
      this.numberProject = numberProject;
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
