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
import java.util.Date;

/**
 * The IterationTask represents the informations of a task on an iteration
 */
public interface IterationTask extends Serializable
{

	/**
	 * Return the iteration of the iterationTask
	 * @return the iteration
	 */
	Iteration getIteration();

	/**
	 * Set the iteration
	 * @param iteration the iteration to set
	 */
	void setIteration(Iteration iteration);

	/**
    * Return the task of the iterationTask
    * @return the task
    */
	Task getTask();

	  /**
    * Set the task
    * @param task the task to set
    */
	void setTask(Task task);
	
	/**
	 * Get the id
	 * @return the identifier of the IterationTask
	 */
	Long getId();
	
	/**
	 * Get the status of the IterationTask
	 * @return the StatusTask
	 */
	StatusTask getStatus();

   /**
    * Set a new StatusTask
    * @param status the StatusTask
    */
   void setStatus(StatusTask status);
	
	/**
    * @return the lastUpdateDate
    */
   Date getLastUpdateDate();

   /**
    * @param lastUpdateDate the lastUpdateDate to set
    */
   void setLastUpdateDate(Date lastUpdateDate);

   /**
    * @return the consumedTime
    */
   float getConsumedTime();

   /**
    * @param consumedTime the consumedTime to set
    */
   void setConsumedTime(float consumedTime);

   /**
    * @return the remainingTime
    */
   float getRemainingTime();

   /**
    * @param remainingTime the remainingTime to set
    */
   void setRemainingTime(float remainingTime);

   @Override
   int hashCode();

   @Override
   boolean equals(Object obj);

   
	
}
