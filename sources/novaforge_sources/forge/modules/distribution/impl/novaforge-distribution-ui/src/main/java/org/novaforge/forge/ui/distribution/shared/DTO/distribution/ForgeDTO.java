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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author BILET-JC
 *
 */
public class ForgeDTO implements Serializable
{

   /**
    * 
    */
   private static final long serialVersionUID = 1990611335020641581L;

   private String            forgeId;
   private String         label;
   private String         description;
   private int               forgeLevel;
   private Date           affiliationDate;
   private String            url;
   private List<ForgeDTO> children = new ArrayList<ForgeDTO>();
   private ForgeDTO       parent;
   private ForgeRequestDTO   parentRequest;

   /**
    * @param forgeId
    * @param label
    * @param description
    * @param forgeLevel
    * @param affiliationDate
    * @param url
    * @param children
    * @param parent
    * @param parentRequest
    */
   public ForgeDTO(String forgeId, String label, String description, int forgeLevel, Date affiliationDate,
         String url, List<ForgeDTO> children, ForgeDTO parent, ForgeRequestDTO parentRequest)
   {
      super();
      this.forgeId = forgeId;
      this.label = label;
      this.description = description;
      this.forgeLevel = forgeLevel;
      this.affiliationDate = affiliationDate;
      this.url = url;
      this.children = children;
      this.parent = parent;
      this.parentRequest = parentRequest;
   }

   /**
    * 
    */
   public ForgeDTO()
   {
      super();
   }

   /**
    * @return the forgeId
    */
   public String getForgeId()
   {
      return forgeId;
   }

   /**
    * @param forgeId
    *           the forgeId to set
    */
   public void setForgeId(String forgeId)
   {
      this.forgeId = forgeId;
   }

   /**
    * @return the label
    */
   public String getLabel()
   {
      return label;
   }

   /**
    * @param label
    *           the label to set
    */
   public void setLabel(String label)
   {
      this.label = label;
   }

   /**
    * @return the description
    */
   public String getDescription()
   {
      return description;
   }

   /**
    * @param description
    *           the description to set
    */
   public void setDescription(String description)
   {
      this.description = description;
   }

   /**
    * @return the forgeLevel
    */
   public int getForgeLevel()
   {
      return forgeLevel;
   }

   /**
    * @param forgeLevel
    *           the forgeLevel to set
    */
   public void setForgeLevel(int forgeLevel)
   {
      this.forgeLevel = forgeLevel;
   }

   /**
    * @return the affiliationDate
    */
   public Date getAffiliationDate()
   {
      return affiliationDate;
   }

   /**
    * @param affiliationDate
    *           the affiliationDate to set
    */
   public void setAffiliationDate(Date affiliationDate)
   {
      this.affiliationDate = affiliationDate;
   }

   /**
    * @return the url
    */
   public String getUrl()
   {
      return url;
   }

   /**
    * @param url
    *           the url to set
    */
   public void setUrl(String url)
   {
      this.url = url;
   }

   /**
    * @return the children
    */
   public List<ForgeDTO> getChildren()
   {
      return children;
   }

   /**
    * @param children
    *           the children to set
    */
   public void setChildren(List<ForgeDTO> children)
   {
      this.children = children;
   }

   /**
    * @return the parent
    */
   public ForgeDTO getParent()
   {
      return parent;
   }

   /**
    * @param parent
    *           the parent to set
    */
   public void setParent(ForgeDTO parent)
   {
      this.parent = parent;
   }

   /**
    * @return the parentRequest
    */
   public ForgeRequestDTO getParentRequest()
   {
      return parentRequest;
   }

   /**
    * @param parentRequest
    *           the parentRequest to set
    */
   public void setParentRequest(ForgeRequestDTO parentRequest)
   {
      this.parentRequest = parentRequest;
   }

}
