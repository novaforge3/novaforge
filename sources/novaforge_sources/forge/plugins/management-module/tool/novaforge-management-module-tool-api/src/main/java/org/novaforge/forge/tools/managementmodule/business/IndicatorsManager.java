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
package org.novaforge.forge.tools.managementmodule.business;

import org.novaforge.forge.tools.managementmodule.domain.IterationTask;
import org.novaforge.forge.tools.managementmodule.domain.transfer.GlobalMonitoringIndicators;
import org.novaforge.forge.tools.managementmodule.domain.transfer.IterationTaskIndicators;
import org.novaforge.forge.tools.managementmodule.domain.transfer.MonitoringIndicators;
import org.novaforge.forge.tools.managementmodule.domain.transfer.ScopeUnitIndicators;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;

import java.util.List;


/**
 * This manager contains all indicators management
 */
public interface IndicatorsManager {

   /**
    * Get the scope unit indicators for an iteration and a discipline
    * @param iterationId the iteration id
    * @param disciplineFunctionalId the discipline to use for filter (or null if we want all disciplines)
    * @return the indicators list
    * @throws ManagementModuleException problem during indicators get
    */
   List<ScopeUnitIndicators> getScopeUnitIndicatorsForIteration(long iterationId, String disciplineFunctionalId) throws ManagementModuleException;

   /**
    * Get a new instance of scope unit indicator
    * @return the created instance
    */
   ScopeUnitIndicators getScopeUnitIndicatorsInstance();
   
   /**
    * Get the monitoring indicators for an iteration and a discipline
    * @param iterationId the iteration id
    * @param disciplineFunctionalId the discipline to use for filter (or null if we want all disciplines)
    * @return the indicators list
    * @throws ManagementModuleException problem during indicators get
    */
   MonitoringIndicators getIterationMonitoringIndicators(long iterationId, String disciplineFunctionalId)
         throws ManagementModuleException;

   
   /**
    * Get the scopeUnit's indicators
    * @param projectPlanId the project plan id
    * @return the scopeUnit's indicators list
    * @throws ManagementModuleException problem during operation
    */
   List<ScopeUnitIndicators> getScopeUnitIndicators(Long projectPlanId) throws ManagementModuleException;

   /**
    * Get the iterationTask's indicators for a given iterationTask
    * @param iterationTask
    * @return the iterationTask indicator
    */
   IterationTaskIndicators getIterationTaskIndicators(IterationTask iterationTask);

   /**
    * Get the monitoring indicators for a project plan
    * 
    * @param projectPlanId
    * @return the indicators list
    * @throws ManagementModuleException
    */
   GlobalMonitoringIndicators getGlobalMonitoringIndicators(Long projectPlanId) throws ManagementModuleException;
   
}
