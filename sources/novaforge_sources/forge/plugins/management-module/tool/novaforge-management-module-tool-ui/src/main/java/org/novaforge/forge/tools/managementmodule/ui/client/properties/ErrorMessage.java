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
package org.novaforge.forge.tools.managementmodule.ui.client.properties;

import com.google.gwt.i18n.client.Messages;

/**
 * This interface regroups all fonctions about error messages
 * 
 * @author BILET-JC
 * @author lamirang
 */
public interface ErrorMessage extends Messages
{

   /*
    * TECHNIC ERRORS
    */
   String TECHNICAL_ERROR();

   String AUTHENTIFICATION_ERROR();

   String ERR_PLUGIN_AVAILABILY();

   /* GLOBAL PARAM ERRORS*/
   String ERR_INVALID_CRON_EXPRESSION();
   
   /*
    * PROJECT PLAN ERRORS
    */
   String ERR_PROJECT_PLAN_GET_LIST();
   String ERR_PROJECT_PLAN_DELETE_WRONG_VERSION();
   String ERR_PROJECT_PLAN_GET_ELEMENT();
   
   /*
    * LOT ERRORS
    */
   String ERR_LOT_HAVING_BOTH_LOTS_AND_ITERATIONS();
   String ERR_TWO_LOT_HAVING_SAME_NAME_IN_A_PROJECT_PLAN();
   String ERR_LOT_END_DATE_BEFORE_ITERATION_END_DATE();
   String ERR_LOT_WRONG_SUB_LOT_DATE();
   String ERR_LOT_NO_OVERLAP();
   String ERR_LOT_WRONG_DATE();
   String ERR_LOT_CHILDS_LOT_NOT_IN_PARENT_LOT();
   String ERR_LOT_DELETE_LOT_HAVING_SCOPE_UNITS();
   String ERR_LOT_DELETE_LOT_IS_A_COPY();
   String ERR_LOT_DATE_NOT_COMPLIANT_WITH_ITERATION();
   
   /*
    * CHARGE PLAN ERRORS
    */
   String ERR_CHARGEPLAN_NO_DISCIPLINE();
   String ERR_CHARGEPLAN_WRONG_SUM_PERCENT_DISCIPLINES();
   
   /* Iterations errors */
   String ERR_ITERATION_IN_PAST();
   String ERR_ITERATION_REQUIRED_LOT();
   String ERR_ITERATION_REQUIRED_PHASE_TYPE();
   String ERR_ITERATION_REQUIRED_START_DATE();
   String ERR_ITERATION_REQUIRED_END_DATE();
   String ERR_ITERATION_NOT_CONTAINS_IN_LOT();
   String ERR_ITERATION_NO_OVERLAP();
   String ERR_ITERATION_NO_DELETE_IF_NOT_LAST();
   String ERR_ITERATION_NO_DELETE_IF_HAS_TASKS();
   String ERR_ITERATION_CLOSE_IS_FINISHED();
   String ERR_ITERATION_CLOSE_IS_NOT_OLDEST();
   String ERR_ITERATION_CLOSE_REMAINING_TASKS_AND_NO_MORE_ITERATION();
   
   String ERR_SCOPE_UNIT_LINKED_TO_ITERATION();
   String ERR_SCOPE_UNIT_NAME_ALREADY_EXIST();
   String ERR_SCOPE_UNIT_NOT_LINKED_TO_LOT();

   String ERR_AUTHENTIFICATION_OR_RIGHTS();

   String ERR_LOT_DELETE_LOT_HAVING_ITERATIONS();
   
   String ERR_TASK_CATEGORY_NO_DELETE_IF_HAS_TASKS();
   
   //Bug tracker interplugin messages
   String BUG_TRACKER_COMMUNICATION_PROBLEM();
   String BUG_TRACKER_ASSOCIATION_DOES_NOT_EXIST();

   String ERROR_OUT_OF_DATE();
   String ERROR_SERIALIZATION();
   String ERROR_SERVER_TIMEOUT();
   String ERROR_SERVER_ERROR();

}