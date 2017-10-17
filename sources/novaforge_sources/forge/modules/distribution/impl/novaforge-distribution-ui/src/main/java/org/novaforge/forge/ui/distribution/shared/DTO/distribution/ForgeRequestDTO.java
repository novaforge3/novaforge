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

package org.novaforge.forge.ui.distribution.shared.DTO.distribution;

import org.novaforge.forge.ui.distribution.shared.enumeration.RequestStatusEnum;
import org.novaforge.forge.ui.distribution.shared.enumeration.TypeDistributionRequestEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author BILET-JC
 *
 */
public class ForgeRequestDTO implements Serializable
{


   /**
    * 
    */
   private static final long serialVersionUID = 5452304871036906825L;

   private String              forgeRequestId;
   private String                      destinationForgeLabel;
   private String                      destinationForgeId;
   private String                      sourceForgeLabel;
   private String                      sourceForgeId;
   private RequestStatusEnum           status;

   private TypeDistributionRequestEnum                           type;

   private String                           reason;

   private Date                           date;

   /**
    * 
    */
   public ForgeRequestDTO()
   {
      // Used for serialization
   }



   /**
    * @param forgeRequestId
    * @param destinationForgeLabel
    * @param destinationForgeId
    * @param sourceForgeLabel
    * @param sourceForgeId
    * @param status
    * @param type
    * @param reason
    * @param date
    */
   public ForgeRequestDTO(String forgeRequestId, String destinationForgeLabel, String destinationForgeId,
         String sourceForgeLabel, String sourceForgeId, RequestStatusEnum status,
         TypeDistributionRequestEnum type, String reason, Date date)
   {
      super();
      this.forgeRequestId = forgeRequestId;
      this.destinationForgeLabel = destinationForgeLabel;
      this.destinationForgeId = destinationForgeId;
      this.sourceForgeLabel = sourceForgeLabel;
      this.sourceForgeId = sourceForgeId;
      this.status = status;
      this.type = type;
      this.reason = reason;
      this.date = date;
   }



   /**
    * @return the forgeRequestId
    */
   public String getForgeRequestId()
   {
      return forgeRequestId;
   }


   /**
    * @param forgeRequestId
    *           the forgeRequestId to set
    */
   public void setForgeRequestId(String forgeRequestId)
   {
      this.forgeRequestId = forgeRequestId;
   }

   /**
    * @return the sourceForgeId
    */
   public String getSourceForgeId()
   {
      return sourceForgeId;
   }

   /**
    * @param sourceForgeId
    *           the sourceForgeId to set
    */
   public void setSourceForgeId(String sourceForgeId)
   {
      this.sourceForgeId = sourceForgeId;
   }

   /**
    * @return the sourceForgeLabel
    */
   public String getSourceForgeLabel()
   {
      return sourceForgeLabel;
   }

   /**
    * @param sourceForgeLabel
    *           the sourceForgeLabel to set
    */
   public void setSourceForgeLabel(String sourceForgeLabel)
   {
      this.sourceForgeLabel = sourceForgeLabel;
   }

   /**
    * @return the destinationForgeId
    */
   public String getDestinationForgeId()
   {
      return destinationForgeId;
   }

   /**
    * @param destinationForgeId
    *           the destinationForgeId to set
    */
   public void setDestinationForgeId(String destinationForgeId)
   {
      this.destinationForgeId = destinationForgeId;
   }

   /**
    * @return the destinationForgeLabel
    */
   public String getDestinationForgeLabel()
   {
      return destinationForgeLabel;
   }

   /**
    * @param destinationForgeLabel
    *           the destinationForgeLabel to set
    */
   public void setDestinationForgeLabel(String destinationForgeLabel)
   {
      this.destinationForgeLabel = destinationForgeLabel;
   }

   /**
    * @return the status
    */
   public RequestStatusEnum getStatus()
   {
      return status;
   }

   /**
    * @param status
    *           the status to set
    */
   public void setStatus(RequestStatusEnum status)
   {
      this.status = status;
   }

   /**
    * @return the type
    */
   public TypeDistributionRequestEnum getType()
   {
      return type;
   }



   /**
    * @param type the type to set
    */
   public void setType(TypeDistributionRequestEnum type)
   {
      this.type = type;
   }




   /**
    * @return the reason
    */
   public String getReason()
   {
      return reason;
   }



   /**
    * @param reason the reason to set
    */
   public void setReason(String reason)
   {
      this.reason = reason;
   }



   /**
    * @return the date
    */
   public Date getDate()
   {
      return date;
   }



   /**
    * @param date the date to set
    */
   public void setDate(Date date)
   {
      this.date = date;
   }





   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return "ForgeAffiliationDTO [destinationForgeId=" + destinationForgeId + ", type=" + type + ", date=" + date
                 + "]";
   }

}
