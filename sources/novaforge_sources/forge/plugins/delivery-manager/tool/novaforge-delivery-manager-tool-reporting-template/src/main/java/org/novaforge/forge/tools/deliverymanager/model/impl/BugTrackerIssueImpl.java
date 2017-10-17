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
package org.novaforge.forge.tools.deliverymanager.model.impl;

import org.novaforge.forge.tools.deliverymanager.model.BugTrackerIssue;

/**
 * This class is a concrete implementation used to build an issue which can be attached to a delivery
 * 
 * @author Guillaume Lamirand
 */
public class BugTrackerIssueImpl implements BugTrackerIssue
{
   /**
    * 
    */
   private static final long serialVersionUID = -1840403895897744971L;
   /**
    * The Id of issue
    * 
    * @see BugTrackerIssueImpl#getId()
    * @see BugTrackerIssueImpl#setId(String)
    */
   private String            id;
   /**
    * The title of issue
    * 
    * @see BugTrackerIssueImpl#getTitle()
    * @see BugTrackerIssueImpl#setTitle(String)
    */
   private String            title;
   /**
    * The description of issue
    * 
    * @see BugTrackerIssueImpl#getDescription()
    * @see BugTrackerIssueImpl#setDescription(String)
    */
   private String            description;
   /**
    * The additionalInfo of issue
    * 
    * @see BugTrackerIssueImpl#getAdditionalInfo()
    * @see BugTrackerIssueImpl#setAdditionalInfo(String)
    */
   private String            additionalInfo;
   /**
    * The category of issue
    * 
    * @see BugTrackerIssueImpl#getCategory()
    * @see BugTrackerIssueImpl#setCategory(String)
    */
   private String            category;
   /**
    * The reporter of issue
    * 
    * @see BugTrackerIssueImpl#getReporter()
    * @see BugTrackerIssueImpl#setReporter(String)
    */
   private String            reporter;
   /**
    * The severity of issue
    * 
    * @see BugTrackerIssueImpl#getSeverity()
    * @see BugTrackerIssueImpl#setSeverity(String)
    */
   private String            severity;

   /**
    * {@inheritDoc}
    * 
    * @see BugTrackerIssueImpl#additionalInfo
    */
   @Override
   public String getAdditionalInfo()
   {
      return this.additionalInfo;
   }

   /**
    * {@inheritDoc}
    * 
    * @see BugTrackerIssueImpl#category
    */
   @Override
   public String getCategory()
   {
      return this.category;
   }

   /**
    * {@inheritDoc}
    * 
    * @see BugTrackerIssueImpl#description
    */
   @Override
   public String getDescription()
   {
      return this.description;
   }

   /**
    * {@inheritDoc}
    * 
    * @see BugTrackerIssueImpl#id
    */
   @Override
   public String getId()
   {
      return this.id;
   }

   /**
    * {@inheritDoc}
    * 
    * @see BugTrackerIssueImpl#title
    */
   @Override
   public String getTitle()
   {
      return this.title;
   }

   /**
    * {@inheritDoc}
    * 
    * @return reporter
    * @see BugTrackerIssueImpl#reporter
    */
   @Override
   public String getReporter()
   {
      return this.reporter;
   }

   /**
    * {@inheritDoc}
    * 
    * @return severity
    * @see BugTrackerIssueImpl#severity
    */
   @Override
   public String getSeverity()
   {
      return this.severity;
   }

   /**
    * @param pSeverity
    *           represents the severity to set
    * @see BugTrackerIssueImpl#severity
    */
   public void setSeverity(final String pSeverity)
   {
      this.severity = pSeverity;
   }

   /**
    * @param pReporter
    *           represents the reporter to set
    * @see BugTrackerIssueImpl#reporter
    */
   public void setReporter(final String pReporter)
   {
      this.reporter = pReporter;
   }

   /**
    * @param pTitle
    *           represents the title to set
    * @see BugTrackerIssueImpl#title
    */
   public void setTitle(final String pTitle)
   {
      this.title = pTitle;
   }

   /**
    * @param pId
    *           represents the id to set
    * @see BugTrackerIssueImpl#title
    */
   public void setId(final String pId)
   {
      this.id = pId;
   }

   /**
    * @param pDescription
    *           represents the description to set
    * @see BugTrackerIssueImpl#description
    */
   public void setDescription(final String pDescription)
   {
      this.description = pDescription;
   }

   /**
    * @param pCategory
    *           represents the category to set
    * @see BugTrackerIssueImpl#category
    */
   public void setCategory(final String pCategory)
   {
      this.category = pCategory;
   }

   /**
    * @param pInfo
    *           represents the info to set
    * @see BugTrackerIssueImpl#additionalInfo
    */
   public void setAdditionalInfo(final String pInfo)
   {
      this.additionalInfo = pInfo;
   }

}
