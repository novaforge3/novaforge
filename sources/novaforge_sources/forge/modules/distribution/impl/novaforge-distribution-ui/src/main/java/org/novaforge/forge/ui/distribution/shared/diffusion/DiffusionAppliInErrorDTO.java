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

package org.novaforge.forge.ui.distribution.shared.diffusion;

import java.io.Serializable;

/**
 * @author rols-p
 *
 */
public class DiffusionAppliInErrorDTO implements Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = -7151485350732205214L;

   private String applicationName;

   private String errorMessage;

   /**
    * 
    */
   public DiffusionAppliInErrorDTO()
   {
   }

   /**
    * @param applicationName
    * @param errorMessage
    * @param forgeName
    */
   public DiffusionAppliInErrorDTO(String applicationName, String errorMessage)
   {
      super();
      this.applicationName = applicationName;
      this.errorMessage = errorMessage;
   }


   /**
    * @return the applicationName
    */
   public String getApplicationName()
   {
      return applicationName;
   }

   /**
    * @param applicationName
    *           the applicationName to set
    */
   public void setApplicationName(String applicationName)
   {
      this.applicationName = applicationName;
   }

   /**
    * @return the errorMessage
    */
   public String getErrorMessage()
   {
      return errorMessage;
   }

   /**
    * @param errorMessage
    *           the errorMessage to set
    */
   public void setErrorMessage(String errorMessage)
   {
      this.errorMessage = errorMessage;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((applicationName == null) ? 0 : applicationName.hashCode());
      result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      DiffusionAppliInErrorDTO other = (DiffusionAppliInErrorDTO) obj;
      if (applicationName == null)
      {
         if (other.applicationName != null)
         {
            return false;
         }
      }
      else if (!applicationName.equals(other.applicationName))
      {
         return false;
      }
      if (errorMessage == null)
      {
         if (other.errorMessage != null)
         {
            return false;
         }
      }
      else if (!errorMessage.equals(other.errorMessage))
      {
         return false;
      }
      return true;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return "ApplicationInErrorDTO [applicationName=" + applicationName + ", errorMessage=" + errorMessage
            + "]";
   }


}
