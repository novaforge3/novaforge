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
package org.novaforge.forge.remote.services.model.core;

import java.io.Serializable;

/**
 * @author blachonm
 */
public class ForgeApplication implements Serializable
{

   private static final long serialVersionUID = -5225937750098388151L;
   private String            instanceId;
   private String            pluginUUID;

   /**
    * The url to access directly the tool (user interface, not the admin one)
    */
   private String            toolUrl;

   /** TODO will be an Enum **/
   private String            category;

   /** TODO will be an Enum **/
   private String            type;

   /**
    * This information is dependent on the user Locale defined in the Forge.
    */
   private String            accessInfo;

   /**
    * @return the instanceId
    */
   public String getInstanceId()
   {
      return instanceId;
   }

   /**
    * set the instanceID
    * 
    * @param instanceId
    */
   public void setInstanceId(String instanceId)
   {
      this.instanceId = instanceId;
   }

   /**
    * @return url the URL to access directly the tool (user interface, not the admin one)
    */
   public String getToolUrl()
   {
      return toolUrl;
   }

   /**
    * Set the toolUrl
    * 
    * @param toolUrl
    */
   public void setToolUrl(String toolUrl)
   {
      this.toolUrl = toolUrl;
   }

   /**
    * @return the unique Plugin ID
    */
   public String getPluginUUID()
   {
      return pluginUUID;
   }

   /**
    * Set the pluginUUID
    * 
    * @param pluginUUID
    */
   public void setPluginUUID(String pluginUUID)
   {
      this.pluginUUID = pluginUUID;
   }

   /**
    * @return a text localized in function of the user locale defined in the Forge.
    */
   public String getAccessInfo()
   {
      return accessInfo;
   }

   public void setAccessInfo(String accessInfo)
   {
      this.accessInfo = accessInfo;
   }

   /**
    * @return the category of the application (ex: wiki, bugtracker)
    */
   public String getCategory()
   {
      return category;
   }

   public void setCategory(String category)
   {
      this.category = category;
   }

   /**
    * @return the type of the application (ex: dokuwiki, mantis)
    */
   public String getType()
   {
      return type;
   }

   public void setType(String type)
   {
      this.type = type;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((instanceId == null) ? 0 : instanceId.hashCode());
      return result;
   }

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
      ForgeApplication other = (ForgeApplication) obj;
      if (instanceId == null)
      {
         if (other.instanceId != null)
         {
            return false;
         }
      }
      else if (!instanceId.equals(other.instanceId))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "ForgeApplication [instanceId=" + instanceId + ", pluginUUID=" + pluginUUID + ", toolUrl=" + toolUrl
                 + ", category=" + category + ", accessInfo=" + accessInfo + "]";
   }

}
