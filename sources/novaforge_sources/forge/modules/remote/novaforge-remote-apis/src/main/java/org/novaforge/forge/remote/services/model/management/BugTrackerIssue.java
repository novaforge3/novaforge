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
package org.novaforge.forge.remote.services.model.management;

import java.io.Serializable;

/**
 * This object represents a link to a bug in a bug tracker TODO à compléter avec les autres propriétés
 * récupérées
 */
public class BugTrackerIssue implements Serializable
{

   /** UID for serialization */
   private static final long serialVersionUID = 9873979812L;

   /** The bug id in the task manager */
   private String            id;

   /** The bug id in the bugtracker (mantis...) */
   private String            bugTrackerId;

   /** The title of the bug */
   private String            title;

   private String            description;

   private String            additionalInfo;

   private String            category;

   private String            reporter;

   private String            severity;

   private String            priority;

   private String            resolution;

   private String            status;

   private String            productVersion;

   private String            assignedTo;

   private String            reproducibility;

   private String            targetVersion;

   private String            fixedInVersion;

   /**
    * Get the bugId
    * 
    * @return the bugId
    */
   public String getBugId()
   {
      return id;
   }

   /**
    * Set the bugId
    * 
    * @param bugId
    *           the bugId to set
    */
   public void setBugId(String bugId)
   {
      this.id = bugId;
   }

   /**
    * Get the bugTrackerId
    * 
    * @return the bugTrackerId
    */
   public String getBugTrackerId()
   {
      return bugTrackerId;
   }

   /**
    * Set the bugTrackerId
    * 
    * @param bugTrackerId
    *           the bugTrackerId to set
    */
   public void setBugTrackerId(String bugTrackerId)
   {
      this.bugTrackerId = bugTrackerId;
   }

   /**
    * Get the title
    * 
    * @return the title
    */
   public String getTitle()
   {
      return title;
   }

   /**
    * Set the title
    * 
    * @param title
    *           the title to set
    */
   public void setTitle(String title)
   {
      this.title = title;
   }

   /**
    * @return description value
    */
   public String getDescription()
   {
      return description;
   }

   /**
    * @param pDescription
    *           represents the description to set
    */
   public void setDescription(final String pDescription)
   {
      description = pDescription;
   }

   /**
    * @return additional information value
    */
   public String getAdditionalInfo()
   {
      return additionalInfo;
   }

   /**
    * @param pInfo
    *           represents the info to set
    */
   public void setAdditionalInfo(final String pInfo)
   {
      additionalInfo = pInfo;
   }

   /**
    * @return category
    */
   public String getCategory()
   {
      return category;
   }

   /**
    * @param pCategory
    *           represents the category to set
    */
   public void setCategory(final String pCategory)
   {
      category = pCategory;
   }

   /**
    * @return reporter
    */
   public String getReporter()
   {
      return reporter;
   }

   /**
    * @param pReporter
    *           represents the reporter to set
    */
   public void setReporter(String pReporter)
   {
      reporter = pReporter;
   }

   /**
    * @return severity
    */
   public String getSeverity()
   {
      return severity;
   }

   /**
    * @param pSeverity
    *           represents the severity to set
    */
   public void setSeverity(String pSeverity)
   {
      severity = pSeverity;
   }

   /**
    * @return Priority
    */
   public String getPriority()
   {
      return priority;
   }

   /**
    * @param pPriority
    *           represents the Priority to set
    */
   public void setPriority(String pPriority)
   {
      priority = pPriority;
   }

   /**
    * @return Resolution
    */
   public String getResolution()
   {
      return resolution;
   }

   /**
    * @param pResolution
    *           represents the Resolution to set
    */
   public void setResolution(String pResolution)
   {
      resolution = pResolution;
   }

   /**
    * @return Status
    */
   public String getStatus()
   {
      return status;
   }

   /**
    * @param pStatus
    *           represents the Status to set
    */
   public void setStatus(String pStatus)
   {
      status = pStatus;
   }

   /**
    * @return ProductVersion
    */
   public String getProductVersion()
   {
      return productVersion;
   }

   /**
    * @param pProductVersion
    *           represents the ProductVersion to set
    */
   public void setProductVersion(String pProductVersion)
   {
      productVersion = pProductVersion;
   }

   /**
    * @return TargetVersion
    */
   public String getTargetVersion()
   {
      return targetVersion;
   }

   /**
    * @param pTargetVersion
    *           represents the TargetVersion to set
    */
   public void setTargetVersion(String pTargetVersion)
   {
      targetVersion = pTargetVersion;
   }

   /**
    * @return FixedInVersion
    */
   public String getFixedInVersion()
   {
      return fixedInVersion;
   }

   /**
    * @param pFixedInVersion
    *           represents the FixedInVersion to set
    */
   public void setFixedInVersion(String pFixedInVersion)
   {
      fixedInVersion = pFixedInVersion;
   }

   /**
    * @return the Assignee
    */
   public String getAssignedTo()
   {
      return assignedTo;
   }

   /**
    * @param pAssignedTo
    *           represents the assignee to set
    */
   public void setAssignedTo(String pAssignedTo)
   {
      assignedTo = pAssignedTo;
   }

   /**
    * @return the Reproducibility
    */
   public String getReproducibility()
   {
      return reproducibility;
   }

   /**
    * @param Reproducibility
    *           represents the reproducibility to set
    */
   public void setReproducibility(String pReproducibility)
   {
      reproducibility = pReproducibility;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
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
      BugTrackerIssue other = (BugTrackerIssue) obj;
      if (id == null)
      {
         if (other.id != null)
         {
            return false;
         }
      }
      else if (!id.equals(other.id))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "BugTrackerIssue [id=" + id + ", bugTrackerId=" + bugTrackerId + ", title=" + title + ", description="
                 + description + ", additionalInfo=" + additionalInfo + ", category=" + category + ", reporter="
                 + reporter + ", severity=" + severity + ", priority=" + priority + ", resolution=" + resolution
                 + ", status=" + status + ", productVersion=" + productVersion + ", assignedTo=" + assignedTo
                 + ", reproducibility=" + reproducibility + "]";
   }

}
