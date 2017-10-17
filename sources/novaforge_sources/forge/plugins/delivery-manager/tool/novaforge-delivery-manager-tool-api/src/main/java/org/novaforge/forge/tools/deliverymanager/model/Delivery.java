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
package org.novaforge.forge.tools.deliverymanager.model;

import java.util.Date;

/**
 * Describes a delivery object
 * 
 * @author Guillaume Lamirand
 */
public interface Delivery
{
   /**
    * Return delivery reference defined as its functional id
    * 
    * @return reference
    */
   String getReference();

   /**
    * Set the reference id
    * 
    * @param reference
    */
   void setReference(final String reference);

   /**
    * Return delivery name
    * 
    * @return name
    */
   String getName();

   /**
    * Set the name
    * 
    * @param name
    */
   void setName(final String pName);

   /**
    * Return delivery type
    * 
    * @return type
    */
   DeliveryType getType();

   /**
    * Return delivery version
    * 
    * @return version
    */
   String getVersion();

   /**
    * Set the delivery version
    * 
    * @param pVersion
    */
   void setVersion(final String pVersion);

   /**
    * Return the delivery status
    * 
    * @return status
    */
   DeliveryStatus getStatus();

   /**
    * Return the project id attached to the delivery
    * 
    * @return project id
    */
   String getProjectId();

   /**
    * Set the project id
    * 
    * @param projectId
    */
   void setProjectId(final String projectId);

   /**
    * Return the delivery generated date
    * 
    * @return date
    */
   Date getDate();

}