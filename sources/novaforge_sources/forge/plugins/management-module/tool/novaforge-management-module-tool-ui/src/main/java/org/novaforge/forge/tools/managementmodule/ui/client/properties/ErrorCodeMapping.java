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

import com.google.gwt.core.client.GWT;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ErrorEnumeration;

/**
 * @author FALSQUELLE-E
 */
public final class ErrorCodeMapping {
  
  /**
   * private constructor
   */
  private ErrorCodeMapping()
  {
  }
  
	private static final ErrorMessage ERROR_MESSAGE = GWT
			.create(ErrorMessage.class);

	public static String getLocalizedMessage(final ErrorEnumeration pCode) {
		String returnMessage = "";
		switch (pCode) {
		case ERR_PROJECT_PLAN_GET_ELEMENT:
			returnMessage = ERROR_MESSAGE.ERR_PROJECT_PLAN_GET_ELEMENT();
			break;
		case ERR_PROJECT_PLAN_GET_LIST:
			returnMessage = ERROR_MESSAGE.ERR_PROJECT_PLAN_GET_LIST();
			break;
    case ERR_INVALID_CRON_EXPRESSION:
      returnMessage = ERROR_MESSAGE.ERR_INVALID_CRON_EXPRESSION();
      break;
		case ERR_PROJECT_PLAN_DELETE_WRONG_VERSION:
         returnMessage = ERROR_MESSAGE.ERR_PROJECT_PLAN_DELETE_WRONG_VERSION();
         break;			
		case ERR_LOT_HAVING_BOTH_LOTS_AND_ITERATIONS:
			returnMessage = ERROR_MESSAGE.ERR_LOT_HAVING_BOTH_LOTS_AND_ITERATIONS();
			break;
		case ERR_LOT_END_DATE_BEFORE_ITERATION_END_DATE:
			returnMessage = ERROR_MESSAGE
					.ERR_LOT_END_DATE_BEFORE_ITERATION_END_DATE();
			break;
		case ERR_TWO_LOT_HAVING_SAME_NAME_IN_A_PROJECT_PLAN:
			returnMessage = ERROR_MESSAGE
					.ERR_TWO_LOT_HAVING_SAME_NAME_IN_A_PROJECT_PLAN();
			break;
		case ERR_LOT_WRONG_SUB_LOT_DATE:
			returnMessage = ERROR_MESSAGE.ERR_LOT_WRONG_SUB_LOT_DATE();
			break;
		case ERR_LOT_NO_OVERLAP:
			returnMessage = ERROR_MESSAGE.ERR_LOT_NO_OVERLAP();
			break;
		case ERR_LOT_WRONG_DATE:
			returnMessage = ERROR_MESSAGE.ERR_LOT_WRONG_DATE();
			break;
      case ERR_LOT_DELETE_LOT_HAVING_ITERATIONS:
         returnMessage = ERROR_MESSAGE.ERR_LOT_DELETE_LOT_HAVING_ITERATIONS();
         break;
      case ERR_LOT_CHILDS_LOT_NOT_IN_PARENT_LOT:
         returnMessage = ERROR_MESSAGE.ERR_LOT_CHILDS_LOT_NOT_IN_PARENT_LOT();
         break;     
      case ERR_LOT_DELETE_LOT_HAVING_SCOPE_UNITS:
         returnMessage = ERROR_MESSAGE.ERR_LOT_DELETE_LOT_HAVING_SCOPE_UNITS();
         break;     
      case ERR_LOT_DELETE_LOT_IS_A_COPY:
         returnMessage = ERROR_MESSAGE.ERR_LOT_DELETE_LOT_IS_A_COPY();
         break;     
      case ERR_CHARGEPLAN_NO_DISCIPLINE:
         returnMessage = ERROR_MESSAGE.ERR_CHARGEPLAN_NO_DISCIPLINE();
         break;			
      case ERR_CHARGEPLAN_WRONG_SUM_PERCENT_DISCIPLINES:
         returnMessage = ERROR_MESSAGE.ERR_CHARGEPLAN_WRONG_SUM_PERCENT_DISCIPLINES();
         break;         
		case ERR_PLUGIN_AVAILABILY:
			returnMessage = ERROR_MESSAGE.ERR_PLUGIN_AVAILABILY();
			break;
		case ERR_ITERATION_IN_PAST:
			returnMessage = ERROR_MESSAGE.ERR_ITERATION_IN_PAST();
			break;
		case ERR_ITERATION_REQUIRED_LOT:
			returnMessage = ERROR_MESSAGE.ERR_ITERATION_REQUIRED_LOT();
			break;
		case ERR_ITERATION_REQUIRED_PHASE_TYPE:
			returnMessage = ERROR_MESSAGE.ERR_ITERATION_REQUIRED_PHASE_TYPE();
			break;
		case ERR_ITERATION_REQUIRED_START_DATE:
			returnMessage = ERROR_MESSAGE.ERR_ITERATION_REQUIRED_START_DATE();
			break;
		case ERR_ITERATION_REQUIRED_END_DATE:
			returnMessage = ERROR_MESSAGE.ERR_ITERATION_REQUIRED_END_DATE();
			break;
		case ERR_ITERATION_NOT_CONTAINS_IN_LOT:
			returnMessage = ERROR_MESSAGE.ERR_ITERATION_NOT_CONTAINS_IN_LOT();
			break;
		case ERR_ITERATION_NO_OVERLAP:
			returnMessage = ERROR_MESSAGE.ERR_ITERATION_NO_OVERLAP();
			break;
		case ERR_LOT_DATE_NOT_COMPLIANT_WITH_ITERATION:
      returnMessage = ERROR_MESSAGE.ERR_LOT_DATE_NOT_COMPLIANT_WITH_ITERATION();
      break;
		case ERR_TASK_CATEGORY_NO_DELETE_IF_HAS_TASKS:
			returnMessage = ERROR_MESSAGE.ERR_TASK_CATEGORY_NO_DELETE_IF_HAS_TASKS();
			break;
    case ERR_ITERATION_NO_DELETE_IF_NOT_LAST:
      returnMessage = ERROR_MESSAGE.ERR_ITERATION_NO_DELETE_IF_NOT_LAST();
      break;			
		case ERR_ITERATION_NO_DELETE_IF_HAS_TASKS:
		   returnMessage = ERROR_MESSAGE.ERR_ITERATION_NO_DELETE_IF_HAS_TASKS();
		   break;
      case ERR_SCOPE_UNIT_LINKED_TO_ITERATION:
         returnMessage = ERROR_MESSAGE.ERR_SCOPE_UNIT_LINKED_TO_ITERATION();
         break;
      case ERR_SCOPE_UNIT_NAME_ALREADY_EXIST:
         returnMessage = ERROR_MESSAGE.ERR_SCOPE_UNIT_NAME_ALREADY_EXIST();
         break;
      case ERR_SCOPE_UNIT_NOT_LINKED_TO_LOT:
         returnMessage = ERROR_MESSAGE.ERR_SCOPE_UNIT_NOT_LINKED_TO_LOT();
         break;
		case ERR_AUTHENTIFICATION_OR_INSUFFICIENT_RIGHTS:
		   returnMessage = ERROR_MESSAGE.ERR_AUTHENTIFICATION_OR_RIGHTS();
         break;
		case ERR_ITERATION_CLOSE_IS_FINISHED:
		   returnMessage = ERROR_MESSAGE.ERR_ITERATION_CLOSE_IS_FINISHED();
		   break;
		case ERR_ITERATION_CLOSE_IS_NOT_OLDEST:
		   returnMessage = ERROR_MESSAGE.ERR_ITERATION_CLOSE_IS_NOT_OLDEST();
		   break;
		case ERR_ITERATION_CLOSE_REMAINING_TASKS_AND_NO_MORE_ITERATION:
		   returnMessage = ERROR_MESSAGE.ERR_ITERATION_CLOSE_REMAINING_TASKS_AND_NO_MORE_ITERATION();
		   break;
		case BUG_TRACKER_COMMUNICATION_PROBLEM:
		   returnMessage = ERROR_MESSAGE.BUG_TRACKER_COMMUNICATION_PROBLEM();
         break;
		case BUG_TRACKER_ASSOCIATION_DOES_NOT_EXIST:
		   returnMessage = ERROR_MESSAGE.BUG_TRACKER_ASSOCIATION_DOES_NOT_EXIST();
         break;
		case ERROR_OUT_OF_DATE:
		  returnMessage = ERROR_MESSAGE.ERROR_OUT_OF_DATE();
      break;
		case ERROR_SERIALIZATION:
      returnMessage = ERROR_MESSAGE.ERROR_SERIALIZATION();
      break;
		case ERROR_SERVER_TIMEOUT:
      returnMessage = ERROR_MESSAGE.ERROR_SERVER_TIMEOUT();
      break;
		case ERROR_SERVER_ERROR:
      returnMessage = ERROR_MESSAGE.ERROR_SERVER_ERROR();
      break;
		case TECHNICAL_ERROR:
		default:
			returnMessage = ERROR_MESSAGE.TECHNICAL_ERROR();
			break;
		}
		return returnMessage;
	}
}
