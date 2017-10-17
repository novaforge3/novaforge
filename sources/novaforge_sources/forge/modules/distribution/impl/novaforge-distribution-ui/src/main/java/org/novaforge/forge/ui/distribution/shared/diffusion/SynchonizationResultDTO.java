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
 * Represents the result of a Synchronization of the Referentiel Project for a given child forge.
 * 
 * @author rols-p
 */
public class SynchonizationResultDTO implements Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = -5404113513050308257L;

   private String                      forgeIP;

   private SynchronizationStatusEnum      status;

   private String                         failedMsg;

   /**
    * 
    */
   public SynchonizationResultDTO()
   {
      super();
   }

   /**
    * @param forgeIP
    * @param status
    */
   public SynchonizationResultDTO(String forgeIP)
   {
      this.forgeIP = forgeIP;
   }

   /**
    * @return the forgeIP
    */
   public String getForgeIP()
   {
      return forgeIP;
   }

   /**
    * @param forgeIP
    *           the forgeIP to set
    */
   public void setForgeIP(String forgeIP)
   {
      this.forgeIP = forgeIP;
   }

   /**
    * @return the status
    */
   public SynchronizationStatusEnum getStatus()
   {
      return status;
   }

   /**
    * @param status
    *           the status to set
    */
   public void setStatus(SynchronizationStatusEnum status)
   {
      this.status = status;
   }

   /**
    * @return the failedMsg
    */
   public String getFailedMsg()
   {
      return failedMsg;
   }

   /**
    * @param failedMsg
    *           the failedMsg to set
    */
   public void setFailedMsg(String failedMsg)
   {
      this.failedMsg = failedMsg;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((forgeIP == null) ? 0 : forgeIP.hashCode());
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
      SynchonizationResultDTO other = (SynchonizationResultDTO) obj;
      if (forgeIP == null)
      {
         if (other.forgeIP != null)
         {
            return false;
         }
      }
      else if (!forgeIP.equals(other.forgeIP))
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
      return "SynchonizationResultDTO [forgeIP=" + forgeIP + ", status=" + status + ", failedMsg=" + failedMsg + "]";
   }

}
