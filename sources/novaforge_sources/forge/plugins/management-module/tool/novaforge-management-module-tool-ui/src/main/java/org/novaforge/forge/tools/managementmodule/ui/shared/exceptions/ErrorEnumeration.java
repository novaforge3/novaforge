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
package org.novaforge.forge.tools.managementmodule.ui.shared.exceptions;

public enum ErrorEnumeration
{
   /*
    * FORGE ERRORS
    */
	ERR_INVALID_CRON_EXPRESSION,
	
	/*
	 * Project Plan
	 */
	ERR_PROJECT_PLAN_GET_LIST,
	ERR_PROJECT_PLAN_GET_ELEMENT,
	ERR_PROJECT_PLAN_DELETE_WRONG_VERSION,
	
	/*
	 * Lot
	 */
	ERR_LOT_HAVING_BOTH_LOTS_AND_ITERATIONS,
	ERR_LOT_END_DATE_BEFORE_ITERATION_END_DATE,
	ERR_LOT_DATE_NOT_COMPLIANT_WITH_ITERATION,
	ERR_TWO_LOT_HAVING_SAME_NAME_IN_A_PROJECT_PLAN,
	ERR_LOT_WRONG_SUB_LOT_DATE,
	ERR_LOT_NO_OVERLAP,
	ERR_LOT_WRONG_DATE,
	ERR_LOT_DELETE_LOT_HAVING_ITERATIONS,
	ERR_LOT_CHILDS_LOT_NOT_IN_PARENT_LOT,
	ERR_LOT_DELETE_LOT_HAVING_SCOPE_UNITS,
	ERR_LOT_DELETE_LOT_IS_A_COPY,
	
	/*
	 * ChargePlan
	 */
   ERR_CHARGEPLAN_NO_DISCIPLINE,
   ERR_CHARGEPLAN_WRONG_SUM_PERCENT_DISCIPLINES,
   /*
    * Plugin
    */
   ERR_PLUGIN_AVAILABILY,
   /*
    * IHM ERRORS
    */
   TECHNICAL_ERROR, 
   /*
    * ITERATIONS ERRORS
    */
   ERR_ITERATION_IN_PAST,
   ERR_ITERATION_REQUIRED_LOT,
   ERR_ITERATION_REQUIRED_PHASE_TYPE,
   ERR_ITERATION_REQUIRED_START_DATE,
   ERR_ITERATION_REQUIRED_END_DATE,
   ERR_ITERATION_NOT_CONTAINS_IN_LOT,
   ERR_ITERATION_NO_OVERLAP,
   ERR_ITERATION_NO_DELETE_IF_NOT_LAST,
   ERR_ITERATION_NO_DELETE_IF_HAS_TASKS,
   
   ERR_ITERATION_CLOSE_IS_FINISHED,
   ERR_ITERATION_CLOSE_IS_NOT_OLDEST,
   ERR_ITERATION_CLOSE_REMAINING_TASKS_AND_NO_MORE_ITERATION,
   
   ERR_SCOPE_UNIT_LINKED_TO_ITERATION,
   ERR_SCOPE_UNIT_NAME_ALREADY_EXIST,
   ERR_SCOPE_UNIT_NOT_LINKED_TO_LOT,
   //rights error 
   ERR_AUTHENTIFICATION_OR_INSUFFICIENT_RIGHTS,
   
   //Task category errors
   ERR_TASK_CATEGORY_NO_DELETE_IF_HAS_TASKS,
   
   
   //MANTIS INTERPLUGIN
   BUG_TRACKER_COMMUNICATION_PROBLEM,
   BUG_TRACKER_ASSOCIATION_DOES_NOT_EXIST,
   ERROR_OUT_OF_DATE,
   ERROR_SERIALIZATION,
   ERROR_SERVER_TIMEOUT,
   ERROR_SERVER_ERROR

}
