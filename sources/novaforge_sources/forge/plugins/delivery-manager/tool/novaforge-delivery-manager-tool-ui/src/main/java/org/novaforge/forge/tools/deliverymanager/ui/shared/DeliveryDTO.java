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
package org.novaforge.forge.tools.deliverymanager.ui.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DeliveryDTO implements Serializable
{

   private static final long           serialVersionUID = 4605469851212626056L;

   private String                      reference;
   private String                      name;
   private String                      version;
   private String                      typeLabel;
   private DeliveryStatusDTO           status;
   private Date                        deliveryDate;
   private Map<ContentTypeDTO, String> contents         = new HashMap<ContentTypeDTO, String>();

   public DeliveryDTO()
   {
      super();
   }

   public String getVersion()
   {
      return this.version;
   }

   public void setVersion(final String version)
   {
      this.version = version;
   }

   public Date getDeliveryDate()
   {
      return this.deliveryDate;
   }

   public void setDeliveryDate(final Date deliveryDate)
   {
      this.deliveryDate = deliveryDate;
   }

   public String getName()
   {
      return this.name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }

   public String getType()
   {
      return this.typeLabel;
   }

   public void setType(final String type)
   {
      this.typeLabel = type;
   }

   public DeliveryStatusDTO getStatus()
   {
      return this.status;
   }

   public void setStatus(final DeliveryStatusDTO status)
   {
      this.status = status;
   }

   public String getReference()
   {
      return this.reference;
   }

   public void setReference(final String pReference)
   {
      this.reference = pReference;
   }

   public Map<ContentTypeDTO, String> getContents()
   {
      return this.contents;
   }

   public void setContents(final Map<ContentTypeDTO, String> pContents)
   {
      this.contents = pContents;
   }

}
