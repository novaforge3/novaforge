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
import java.util.Set;

/**
 * This is the behavior of a project into the forge
 * 
 * @author fdemange
 * 
 */
public interface Lot extends Serializable
{

   Long getId();

   String getName();

   void setName(String name);

   String getDescription();

   void setDescription(String description);

   Date getStartDate();

   void setStartDate(Date startDate);

   Date getEndDate();

   void setEndDate(Date endDate);


   ProjectPlan getProjectPlan();

   void setProjectPlan(ProjectPlan projectPlan);

   Set<Iteration> getIterations();

   void setIterations(Set<Iteration> iterations);

   void addIteration(Iteration iteration);

   void removeIterationTask(Iteration iteration);

   Set<ScopeUnit> getScopeEntities();

   void setScopeEntities(Set<ScopeUnit> scopeEntities);

   void addScopeEntity(ScopeUnit scope);

   void removeScopeEntity(ScopeUnit scope);

   Set<Lot> getChildLots();

   /*
    * Lot getParentLot();
    * 
    * void setParentLot(Lot parentLot);
    */

   void addChildLot(Lot lot);

   void removeChildLot(Lot lot);

   Lot getParentLot();
   
   void setParentLot(Lot parentLot);

   /**
    * Get the srcLot
    * @return the srcLot
    */
   Lot getSrcLot();

   /**
    * Set the srcLot
    * @param srcLot the srcLot to set
    */
   void setSrcLot(Lot srcLot);
   
   

}
