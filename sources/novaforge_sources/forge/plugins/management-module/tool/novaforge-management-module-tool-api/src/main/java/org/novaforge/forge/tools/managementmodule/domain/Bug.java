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
package org.novaforge.forge.tools.managementmodule.domain;

/**
 * A Bug represents a link to a bug tracker bug
 */
public interface Bug {

   /**
    * Get the id of the bug on the bug tracker
    * @return the bugTrackerId
    */
   String getBugTrackerId();

   /**
    * Set the id of the bug on the bug tracker
    * @param bugTrackerId the bugTrackerId to set
    */
   void setBugTrackerId(String bugTrackerId);

   /**
    * @return the id
    */
   Long getId();

   /**
    * @return the description
    */
   String getDescription();

   /**
    * @param description the description to set
    */
   void setDescription(String description);

   /**
    * Get the title
    * @return the title
    */
   String getTitle();

   /**
    * Set the title
    * @param title the title to set
    */
   void setTitle(String title);

   /**
    * Get the additionalInfo
    * @return the additionalInfo
    */
   String getAdditionalInfo();

   /**
    * Set the additionalInfo
    * @param additionalInfo the additionalInfo to set
    */
   void setAdditionalInfo(String additionalInfo);

   /**
    * Get the category
    * @return the category
    */
   String getCategory();

   /**
    * Set the category
    * @param category the category to set
    */
   void setCategory(String category);

   /**
    * Get the reporter
    * @return the reporter
    */
   String getReporter();

   /**
    * Set the reporter
    * @param reporter the reporter to set
    */
   void setReporter(String reporter);

   /**
    * Get the severity
    * @return the severity
    */
   String getSeverity();

   /**
    * Set the severity
    * @param severity the severity to set
    */
   void setSeverity(String severity);

   /**
    * Get the priority
    * @return the priority
    */
   String getPriority();

   /**
    * Set the priority
    * @param priority the priority to set
    */
   void setPriority(String priority);

   /**
    * Get the resolution
    * @return the resolution
    */
   String getResolution();

   /**
    * Set the resolution
    * @param resolution the resolution to set
    */
   void setResolution(String resolution);

   /**
    * Get the status
    * @return the status
    */
   String getStatus();

   /**
    * Set the status
    * @param status the status to set
    */
   void setStatus(String status);

   /**
    * Get the productVersion
    * @return the productVersion
    */
   String getProductVersion();

   /**
    * Set the productVersion
    * @param productVersion the productVersion to set
    */
   void setProductVersion(String productVersion);

   /**
    * Get the assignedTo
    * @return the assignedTo
    */
   String getAssignedTo();

   /**
    * Set the assignedTo
    * @param assignedTo the assignedTo to set
    */
   void setAssignedTo(String assignedTo);

   /**
    * Get the reproducibility
    * @return the reproducibility
    */
   String getReproducibility();

   /**
    * Set the reproducibility
    * @param reproducibility the reproducibility to set
    */
   void setReproducibility(String reproducibility);

   /**
    * Get the targetVersion
    * @return the targetVersion
    */
   String getTargetVersion();

   /**
    * Set the targetVersion
    * @param targetVersion the targetVersion to set
    */
   void setTargetVersion(String targetVersion);

   /**
    * Get the fixedInVersion
    * @return the fixedInVersion
    */
   String getFixedInVersion();

   /**
    * Set the fixedInVersion
    * @param fixedInVersion the fixedInVersion to set
    */
   void setFixedInVersion(String fixedInVersion);
   
   @Override
   int hashCode();

   @Override
   boolean equals(Object obj);

}