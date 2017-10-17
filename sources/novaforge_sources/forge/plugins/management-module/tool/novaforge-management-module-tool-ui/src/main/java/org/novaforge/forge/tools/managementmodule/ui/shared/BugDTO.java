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
package org.novaforge.forge.tools.managementmodule.ui.shared;

import java.io.Serializable;

/**
 * The DTO use to communicate bugs between client and server
 */
public class BugDTO implements Serializable{

   /** Serial unique id for serialization */
   private static final long serialVersionUID = -6240817566678502553L;

   /** The bug id in the task manager */
   private Long            bugId;

   /** The bug id in the bugtracker (mantis...) */
   private String            bugTrackerId;

   /** The title of the bug */
   private String            title;

   /** The category of the bug */
   private String            category;

   /** The reporter of the bug */
   private String            reporter;

   /** The severity of the bug */
   private String            severity;

   /** The priority of the bug */
   private String            priority;

   /** The status of the bug */
   private String            status;

   /** The user assigned to the bug */
   private String            assignedTo;

   /**
    * Get the bugId
    * @return the bugId
    */
   public Long getBugId() {
      return bugId;
   }

   /**
    * Set the bugId
    * @param bugId the bugId to set
    */
   public void setBugId(Long bugId) {
      this.bugId = bugId;
   }

   /**
    * Get the bugTrackerId
    * @return the bugTrackerId
    */
   public String getBugTrackerId() {
      return bugTrackerId;
   }

   /**
    * Set the bugTrackerId
    * @param bugTrackerId the bugTrackerId to set
    */
   public void setBugTrackerId(String bugTrackerId) {
      this.bugTrackerId = bugTrackerId;
   }

   /**
    * Get the title (can't be null)
    * @return the title
    */
   public String getTitle() {
      if(title == null){
         return "";
      }
      return title;
   }

   /**
    * Set the title
    * @param title the title to set
    */
   public void setTitle(String title) {
      this.title = title;
   }

   /**
    * Get the category (can't be null)
    * @return the category
    */
   public String getCategory() {
      if(category == null){
         return "";
      }
      return category;
   }

   /**
    * Set the category
    * @param category the category to set
    */
   public void setCategory(String category) {
      this.category = category;
   }

   /**
    * Get the reporter (can't be null)
    * @return the reporter
    */
   public String getReporter() {
      if(reporter == null){
         return "";
      }
      return reporter;
   }

   /**
    * Set the reporter
    * @param reporter the reporter to set
    */
   public void setReporter(String reporter) {
      this.reporter = reporter;
   }

   /**
    * Get the severity (can't be null)
    * @return the severity
    */
   public String getSeverity() {
      if(severity == null){
         return "";
      }
      return severity;
   }

   /**
    * Set the severity
    * @param severity the severity to set
    */
   public void setSeverity(String severity) {
      this.severity = severity;
   }

   /**
    * Get the priority (can't be null)
    * @return the priority
    */
   public String getPriority() {
      if(priority == null){
         return "";
      }
      return priority;
   }

   /**
    * Set the priority
    * @param priority the priority to set
    */
   public void setPriority(String priority) {
      this.priority = priority;
   }

   /**
    * Get the status (can't be null)
    * @return the status
    */
   public String getStatus() {
      if(status == null){
         return "";
      }
      return status;
   }

   /**
    * Set the status
    * @param status the status to set
    */
   public void setStatus(String status) {
      this.status = status;
   }

   /**
    * Get the assignedTo (can't be null)
    * @return the assignedTo
    */
   public String getAssignedTo() {
      if(assignedTo == null){
         return "";
      }
      return assignedTo;
   }

   /**
    * Set the assignedTo
    * @param assignedTo the assignedTo to set
    */
   public void setAssignedTo(String assignedTo) {
      this.assignedTo = assignedTo;
   }

   @Override
   public String toString() {
      return "BugDTO [bugId=" + bugId + ", bugTrackerId=" + bugTrackerId + ", title=" + title + ", category="
            + category + ", reporter=" + reporter + ", severity=" + severity + ", priority=" + priority
            + ", status=" + status + ", assignedTo=" + assignedTo + "]";
   }
   
   
}
