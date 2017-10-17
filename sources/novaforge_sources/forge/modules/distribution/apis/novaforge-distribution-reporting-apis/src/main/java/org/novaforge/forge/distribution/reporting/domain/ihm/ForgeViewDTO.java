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
public class ForgeViewDTO implements Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = 8851147823201094992L;

   private String  forgeName;

   private Integer numberProject;

   private Integer numberAccount;
   
   private String  lastUpdated; 

   private StatusDataAccessEnum status;

   /**
    * 
    */
   public ForgeViewDTO()
   {
      super();
   }


   /**
    * @param forgeName
    * @param numberProject
    * @param numberAccount
    * @param status
    */
   public ForgeViewDTO(String forgeName, Integer numberProject, Integer numberAccount,
         StatusDataAccessEnum status)
   {
      super();
      this.forgeName = forgeName;
      this.numberProject = numberProject;
      this.numberAccount = numberAccount;
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
    * @param forgeName
    *           the forgeName to set
    */
   public void setForgeName(String forgeName)
   {
      this.forgeName = forgeName;
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
    * @return the numberAccount
    */
   public Integer getNumberAccount()
   {
      return numberAccount;
   }

   /**
    * @param numberAccount
    *           the numberAccount to set
    */
   public void setNumberAccount(Integer numberAccount)
   {
      this.numberAccount = numberAccount;
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


   public String getLastUpdated() 
   {
	   return lastUpdated;
   }


   public void setLastUpdated(String lastUpdated) 
   {
	   this.lastUpdated = lastUpdated;
   }

}
