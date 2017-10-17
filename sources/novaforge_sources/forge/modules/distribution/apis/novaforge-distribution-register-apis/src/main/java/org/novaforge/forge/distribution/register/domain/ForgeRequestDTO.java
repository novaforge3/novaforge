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
package org.novaforge.forge.distribution.register.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author Mohamed IBN EL AZZOUZI
 */
public class ForgeRequestDTO implements Serializable
{

   private static final long serialVersionUID = -5856520846211361430L;


   private UUID              forgeRequestId;

   private RequestType requestType;

   private RequestStatus requestStatus;

   private String requestComment;

   private Date requestDate;

   private Date responseDate;

   private ForgeDTO          destinationForge;

   private ForgeDTO          sourceForge;


   public UUID getForgeRequestId()
   {
      return this.forgeRequestId;
   }

   public void setForgeRequestId(final UUID forgeRequestId)
   {
      this.forgeRequestId = forgeRequestId;
   }

   public RequestType getRequestType() {
      return this.requestType;
   }

   public void setRequestType(final RequestType requestType) {
      this.requestType = requestType;
   }

   public RequestStatus getRequestStatus() {
      return this.requestStatus;
   }

   public void setRequestStatus(final RequestStatus requestStatus) {
      this.requestStatus = requestStatus;
   }

   public String getRequestComment() {
      return this.requestComment;
   }

   public void setRequestComment(final String requestComment) {
      this.requestComment = requestComment;
   }

   public Date getRequestDate() {
      return this.requestDate;
   }

   public void setRequestDate(final Date requestDate) {
      this.requestDate = requestDate;
   }

   public Date getResponseDate() {
      return this.responseDate;
   }

   public void setResponseDate(final Date responseDate) {
      this.responseDate = responseDate;
   }

   public ForgeDTO getDestinationForge()
   {
      return this.destinationForge;
   }

   public void setDestinationForge(final ForgeDTO destinationForge)
   {
      this.destinationForge = destinationForge;

   }

   public ForgeDTO getSourceForge()
   {
      return this.sourceForge;
   }

   public void setSourceForge(final ForgeDTO sourceForge)
   {
      this.sourceForge = sourceForge;
   }

}
