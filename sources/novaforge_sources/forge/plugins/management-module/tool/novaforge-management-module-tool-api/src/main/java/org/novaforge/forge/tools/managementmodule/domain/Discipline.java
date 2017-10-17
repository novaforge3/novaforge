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

import java.io.Serializable;

/**
 * The discipline is the business type of tasks (ie : implementation, project
 * management, analysis...)
 */
public interface Discipline extends Serializable
{
   /**
    * Get the name
    * @return the name
    */
   String getName();

   /**
    * Set the name
    * @param name the name to set
    */
   void setName(String name);

   /**
    * Get the defaultRepartition
    * @return the defaultRepartition
    */
   int getDefaultRepartition();

   /**
    * Set the defaultRepartition
    * @param defaultRepartition the defaultRepartition to set
    */
   void setDefaultRepartition(int defaultRepartition);

   /**
    * Get the functionalId
    * @return the functionalId
    */
   String getFunctionalId();

   /**
    * Set the functionalId
    * @param functionalId the functionalId to set
    */
   void setFunctionalId(String functionalId);

   /**
    * Get the id
    * @return the id
    */
   Long getId();
   
   /**
    * Return the order
    * @return the order
    */
   int getOrder();

   /**
    * Set the order
    *
    * @param order
    */
   void setOrder(int order);

	@Override
   int hashCode();

   @Override
   boolean equals(Object obj);
}
