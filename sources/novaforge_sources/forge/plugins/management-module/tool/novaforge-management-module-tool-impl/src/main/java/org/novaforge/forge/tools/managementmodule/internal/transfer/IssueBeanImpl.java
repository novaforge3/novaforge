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
package org.novaforge.forge.tools.managementmodule.internal.transfer;

import org.novaforge.forge.core.plugins.categories.management.IssueBean;

/**
 * Implementation of an issue bean
 */
public class IssueBeanImpl implements IssueBean{

   /** UID for serialization */
   private static final long serialVersionUID = 9873979812L;

   /** The bug id in the task manager */
   private String            bugId;

   /** The bug id in the bugtracker (mantis...) */
   private String            bugTrackerId;

   /** The title of the bug */
   private String            title;

   /** The description of the bug */
   private String            description;

   /** The additionals informations of the bug */
   private String            additionalInfo;

   /** The category of the bug */
   private String            category;

   /** The reporter of the bug */
   private String            reporter;

   /** The severity of the bug */
   private String            severity;

   /** The priority of the bug */
   private String            priority;

   /** The resolution of the bug */
   private String            resolution;

   /** The status of the bug */
   private String            status;

   /** The product version of the bug */
   private String            productVersion;

   /** The user assigned to the bug */
   private String            assignedTo;

   /** The reproducibility of the bug */
   private String            reproducibility;

   /** The target version of the bug */
   private String            targetVersion;

   /** The version of the fix of the bug */
   private String            fixedInVersion;

   /**
    * Get the bugId
    * @return the bugId
    */
   @Override
   public String getBugId() {
      return bugId;
   }

   /**
    * Set the bugId
    * @param bugId the bugId to set
    */
   @Override
   public void setBugId(String bugId) {
      this.bugId = bugId;
   }

   /**
    * Get the bugTrackerId
    * @return the bugTrackerId
    */
   @Override
   public String getBugTrackerId() {
      return bugTrackerId;
   }

   /**
    * Set the bugTrackerId
    * @param bugTrackerId the bugTrackerId to set
    */
   @Override
   public void setBugTrackerId(String bugTrackerId) {
      this.bugTrackerId = bugTrackerId;
   }

   /**
    * Get the title
    * @return the title
    */
   @Override
   public String getTitle() {
      return title;
   }

   /**
    * Set the title
    * @param title the title to set
    */
   @Override
   public void setTitle(String title) {
      this.title = title;
   }

   /**
    * Get the description
    * @return the description
    */
   @Override
   public String getDescription() {
      return description;
   }

   /**
    * Set the description
    * @param description the description to set
    */
   @Override
   public void setDescription(String description) {
      this.description = description;
   }

   /**
    * Get the additionalInfo
    * @return the additionalInfo
    */
   @Override
   public String getAdditionalInfo() {
      return additionalInfo;
   }

   /**
    * Set the additionalInfo
    * @param additionalInfo the additionalInfo to set
    */
   @Override
   public void setAdditionalInfo(String additionalInfo) {
      this.additionalInfo = additionalInfo;
   }

   /**
    * Get the category
    * @return the category
    */
   @Override
   public String getCategory() {
      return category;
   }

   /**
    * Set the category
    * @param category the category to set
    */
   @Override
   public void setCategory(String category) {
      this.category = category;
   }

   /**
    * Get the reporter
    * @return the reporter
    */
   @Override
   public String getReporter() {
      return reporter;
   }

   /**
    * Set the reporter
    * @param reporter the reporter to set
    */
   @Override
   public void setReporter(String reporter) {
      this.reporter = reporter;
   }

   /**
    * Get the severity
    * @return the severity
    */
   @Override
   public String getSeverity() {
      return severity;
   }

   /**
    * Set the severity
    * @param severity the severity to set
    */
   @Override
   public void setSeverity(String severity) {
      this.severity = severity;
   }

   /**
    * Get the priority
    * @return the priority
    */
   @Override
   public String getPriority() {
      return priority;
   }

   /**
    * Set the priority
    * @param priority the priority to set
    */
   @Override
   public void setPriority(String priority) {
      this.priority = priority;
   }

   /**
    * Get the resolution
    * @return the resolution
    */
   @Override
   public String getResolution() {
      return resolution;
   }

   /**
    * Set the resolution
    * @param resolution the resolution to set
    */
   @Override
   public void setResolution(String resolution) {
      this.resolution = resolution;
   }

   /**
    * Get the status
    * @return the status
    */
   @Override
   public String getStatus() {
      return status;
   }

   /**
    * Set the status
    * @param status the status to set
    */
   @Override
   public void setStatus(String status) {
      this.status = status;
   }

   /**
    * Get the productVersion
    * @return the productVersion
    */
   @Override
   public String getProductVersion() {
      return productVersion;
   }

   /**
    * Set the productVersion
    * @param productVersion the productVersion to set
    */
   @Override
   public void setProductVersion(String productVersion) {
      this.productVersion = productVersion;
   }

   /**
    * Get the targetVersion
    * @return the targetVersion
    */
   @Override
   public String getTargetVersion()
   {
      return targetVersion;
   }

   /**
    * Set the targetVersion
    * @param targetVersion the targetVersion to set
    */
   @Override
   public void setTargetVersion(String targetVersion)
   {
      this.targetVersion = targetVersion;
   }

   /**
    * Get the fixedInVersion
    * @return the fixedInVersion
    */
   @Override
   public String getFixedInVersion()
   {
      return fixedInVersion;
   }

   /**
    * Set the fixedInVersion
    * @param fixedInVersion the fixedInVersion to set
    */
   @Override
   public void setFixedInVersion(String fixedInVersion)
   {
      this.fixedInVersion = fixedInVersion;
   }

   /**
    * Get the assignedTo
    * @return the assignedTo
    */
   @Override
   public String getAssignedTo()
   {
      return assignedTo;
   }

   /**
    * Set the assignedTo
    * @param assignedTo the assignedTo to set
    */
   @Override
   public void setAssignedTo(String assignedTo)
   {
      this.assignedTo = assignedTo;
   }

   /**
    * Get the reproducibility
    * @return the reproducibility
    */
   @Override
   public String getReproducibility()
   {
      return reproducibility;
   }

   /**
    * Set the reproducibility
    * @param reproducibility the reproducibility to set
    */
   @Override
   public void setReproducibility(String reproducibility)
   {
      this.reproducibility = reproducibility;
   }
   
   
}
