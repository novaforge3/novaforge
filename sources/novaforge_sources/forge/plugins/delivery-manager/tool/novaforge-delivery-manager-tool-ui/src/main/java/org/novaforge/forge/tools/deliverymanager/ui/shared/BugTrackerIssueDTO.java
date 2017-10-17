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

/**
 * @author Guillaume Lamirand
 */
/**
 * @author "Guillaume Lamirand"
 */
public class BugTrackerIssueDTO implements Serializable
{

   /**
    * 
    */
   private static final long serialVersionUID = -6850231035486292456L;

   private String            id;

   private String            title;

   private String            description;

   private String            addionnalInfo;

   private String            category;

   private String            reporter;
   private String            severity;

   /**
    * @return additional information
    */
   public String getAdditionalInfo()
   {
      return this.addionnalInfo;
   }

   /**
    * @param pInfo
    *           the additional information to set
    */
   public void setAdditionalInfo(final String pInfo)
   {
      this.addionnalInfo = pInfo;
   }

   /**
    * @return category
    */
   public String getCategory()
   {
      return this.category;
   }

   /**
    * @param pCategory
    *           the category to set
    */
   public void setCategory(final String pCategory)
   {
      this.category = pCategory;
   }

   /**
    * @return description
    */
   public String getDescription()
   {
      return this.description;
   }

   /**
    * @param pDescription
    *           the description to set
    */
   public void setDescription(final String pDescription)
   {
      this.description = pDescription;
   }

   /**
    * @return
    */
   public String getId()
   {
      return this.id;
   }

   /**
    * @param pId
    *           the id to set
    */
   public void setId(final String pId)
   {
      this.id = pId;
   }

   /**
    * @return title
    */
   public String getTitle()
   {
      return this.title;
   }

   /**
    * @param pTitle
    *           the title to set
    */
   public void setTitle(final String pTitle)
   {
      this.title = pTitle;
   }

   /**
    * @return reporter
    */
   public String getReporter()
   {
      return this.reporter;
   }

   /**
    * @param reporter
    *           the reporter to set
    */
   public void setReporter(final String reporter)
   {
      this.reporter = reporter;
   }

   /**
    * @return the severity
    */
   public String getSeverity()
   {
      return this.severity;
   }

   /**
    * @param severity
    *           the severity to set
    */
   public void setSeverity(final String severity)
   {
      this.severity = severity;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((this.addionnalInfo == null) ? 0 : this.addionnalInfo.hashCode());
      result = (prime * result) + ((this.category == null) ? 0 : this.category.hashCode());
      result = (prime * result) + ((this.description == null) ? 0 : this.description.hashCode());
      result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
      result = (prime * result) + ((this.reporter == null) ? 0 : this.reporter.hashCode());
      result = (prime * result) + ((this.severity == null) ? 0 : this.severity.hashCode());
      result = (prime * result) + ((this.title == null) ? 0 : this.title.hashCode());
      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (this.getClass() != obj.getClass())
      {
         return false;
      }
      final BugTrackerIssueDTO other = (BugTrackerIssueDTO) obj;
      if (this.addionnalInfo == null)
      {
         if (other.addionnalInfo != null)
         {
            return false;
         }
      }
      else if (!this.addionnalInfo.equals(other.addionnalInfo))
      {
         return false;
      }
      if (this.category == null)
      {
         if (other.category != null)
         {
            return false;
         }
      }
      else if (!this.category.equals(other.category))
      {
         return false;
      }
      if (this.description == null)
      {
         if (other.description != null)
         {
            return false;
         }
      }
      else if (!this.description.equals(other.description))
      {
         return false;
      }
      if (this.id == null)
      {
         if (other.id != null)
         {
            return false;
         }
      }
      else if (!this.id.equals(other.id))
      {
         return false;
      }
      if (this.reporter == null)
      {
         if (other.reporter != null)
         {
            return false;
         }
      }
      else if (!this.reporter.equals(other.reporter))
      {
         return false;
      }
      if (this.severity == null)
      {
         if (other.severity != null)
         {
            return false;
         }
      }
      else if (!this.severity.equals(other.severity))
      {
         return false;
      }
      if (this.title == null)
      {
         if (other.title != null)
         {
            return false;
         }
      }
      else if (!this.title.equals(other.title))
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
      return "BugTrackerIssueDTO [id=" + this.id + ", title=" + this.title + ", description=" + this.description
                 + ", addionnalInfo=" + this.addionnalInfo + ", category=" + this.category + ", reporter="
                 + this.reporter + ", severity=" + this.severity + "]";
   }

}
