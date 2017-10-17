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
package org.novaforge.forge.tools.managementmodule.constant;

public interface ManagementModuleConstants {
	String COMMON_EMPTY_TEXT = "";
	
	String LANGUAGE_FR = "fr";
	String LANGUAGE_EN = "en";
	
	String PROJECT_PLAN_DRAFT = "draft";
	String PROJECT_PLAN_VALIDATE = "validate";
	
	int PROJECT_PLAN_VERSION_DEFAULT = 1;
	
	String ETP_DENOMINATOR = "4";
	
	String UNIT_TIME_WEEK = "week";
	String UNIT_TIME_MONTH = "month";
	
	String TASK_TYPE_WORK = "work";
	String TASK_TYPE_BUG = "bug";

	String TASK_STATUS_NOT_STARTED = "notStarted";
	String TASK_STATUS_NOT_AFFECTED = "notAffected";
	String TASK_STATUS_IN_PROGRESS = "inProgress";
	String TASK_STATUS_DONE = "done";
	String TASK_STATUS_CANCELED = "canceled";
	
	String FACTOR_ADJUSTMENT_DATA_TRANSMISSION = "dataTransmision";
	String FACTOR_ADJUSTMENT_DISTRIBUTED_SYSTEM = "distributedSystem";
	String FACTOR_ADJUSTMENT_PERF_CONSTRAINT = "perfConstraint";
	String FACTOR_ADJUSTMENT_CONFIGURATION = "configuration";
	String FACTOR_ADJUSTMENT_TRANSACTION_RATE = "transactionRate";
	String FACTOR_ADJUSTMENT_USABILITY = "usability";
	String FACTOR_ADJUSTMENT_LIVE_UPDATE = "liveupadte";
	String FACTOR_ADJUSTMENT_PROCESS_COMPLEX = "processComplex";
	String FACTOR_ADJUSTMENT_REUSE = "reuse";
	String FACTOR_ADJUSTMENT_EASY_OPERATION = "easyOperation";
	String FACTOR_ADJUSTMENT_APPLI_PORTABILITY = "appliPortability";
	String FACTOR_ADJUSTMENT_SIMULTANEOUS_ACCESS = "simultaneousAcces";
	String FACTOR_ADJUSTMENT_MODIF_FLEXIBILITY = "modifFlexibility";
	String FACTOR_ADJUSTMENT_INTERACTIVE_CAPTURE = "interactiveCapture";
	
	String WEIGHT_ADJUSTMENT_NONEXISTENT = "nonexistent";
	String WEIGHT_ADJUSTMENT_SECONDARY = "secondary";
	String WEIGHT_ADJUSTMENT_RESTRAINT = "restraint";
	String WEIGHT_ADJUSTMENT_AVERAGE = "average";
	String WEIGHT_ADJUSTMENT_IMPORTANT = "important";
	String WEIGHT_ADJUSTMENT_INTENSIVE = "intensive";
	
	String SCOPE_TYPE_USE_CASE = "useCase";
	String SCOPE_TYPE_GRAAL_TASK = "graalTask";
	String SCOPE_TYPE_USER_STORY = "userStory";
	String SCOPE_TYPE_OTHER = "other";

	String SCOPE_DISCIPLINE_STATUS_NOT_STARTED = "notStarted";
	String SCOPE_DISCIPLINE_STATUS_IN_PROGRESS = "inProgress";
	String SCOPE_DISCIPLINE_STATUS_CLOSED = "closed";
	
	String SCOPE_STATUS_MODIFIED = "modified";
	String SCOPE_STATUS_OBSOLETE = "obsolete";
   String SCOPE_STATUS_ONGOING = "ongoing";

	String SCOPE_UNIT_MONITORING_IN_PROGRESS = "inProgress";
	String SCOPE_UNIT_MONITORING_FINISHED = "finished";
	
	String MARKER_TYPE_EARLY = "early";
	String MARKER_TYPE_LATE = "late";
	
	String VALUE_COLUMN_SIMPLE = "Simple";
	String VALUE_COLUMN_AVERAGE = "Medium";
	String VALUE_COLUMN_HARD = "Complex";
	String VALUE_COLUMN_UNKNOWN = "Unknown";
	
	String PHASE_TYPE_FRAMING = "framing";
	String PHASE_TYPE_PLANNING = "planning";
	String PHASE_TYPE_CONSTRUCTION = "construction";
	String PHASE_TYPE_TRANSITION = "transition";
	String PHASE_TYPE_NONE = "none";
	
	String DISCIPLINE_BUSINESS_MODELING = "businessModeling";
	String DISCIPLINE_REQUIREMENTS_ANALYSIS = "requirementsAnalysis";
	String DISCIPLINE_ARCHITECTURE_DESIGN = "architectureDesign";
	String DISCIPLINE_IMPLEMENTATION = "implementation";
	String DISCIPLINE_RECEIPTS = "receipts";
	String DISCIPLINE_CHANGE_DRIVING = "changeDriving";
	String DISCIPLINE_CONFIGURATION_MANAGEMENT = "configurationManagement";
	String DISCIPLINE_QUALITY_ASSURANCE = "qualityAssurance";
	String DISCIPLINE_PROJECT_MANAGEMENT = "projectManagement";
	
	int DISCIPLINE_ORDER_BUSINESS_MODELING = 1;
   int DISCIPLINE_ORDER_REQUIREMENTS_ANALYSIS = 2;
   int DISCIPLINE_ORDER_ARCHITECTURE_DESIGN = 3;
   int DISCIPLINE_ORDER_IMPLEMENTATION = 4;
   int DISCIPLINE_ORDER_RECEIPTS = 5;
   int DISCIPLINE_ORDER_PROJECT_MANAGEMENT = 6;
   int DISCIPLINE_ORDER_CHANGE_DRIVING = 7;
   int DISCIPLINE_ORDER_QUALITY_ASSURANCE = 8;
   int DISCIPLINE_ORDER_CONFIGURATION_MANAGEMENT = 9;
	
	
	String ROLE_PROJECT_MANAGER = "projectManager";
	String ROLE_CUSTOMER = "customer";
	String ROLE_ADMIN = "admin";
	String ROLE_PROJECT_DIRECTOR = "projectDirector";
	String ROLE_MOE_LEADER = "MOELeader";
	String ROLE_MOE_MEMBER = "MOEMember";
	String ROLE_MOA_LEADER = "MOALeader";
   String ROLE_MOA_MEMBER = "MOAMember";
	String ROLE_OBSERVER = "observer";
	String ROLE_SPONSOR = "sponsor";
	
   String FUNCTION_ESTIMATION = "estimation";
   String FUNCTION_PERIMETER_MANAGEMENT = "perimetre";
   String FUNCTION_CHARGE_PLAN = "plan_charge";
   String FUNCTION_ITERATION_PREPARATION = "preparation_iteration";
   String FUNCTION_ITERATION_SUPERVISION = "suivi_iteration";
   String FUNCTION_TASK_MANAGEMENT = "gerer_taches";
   String FUNCTION_GLOBAL_SUPERVISION = "suivi_global";
   String FUNCTION_ADMINISTRATION = "administrer";
   String FUNCTION_PROJECT_PLAN = "plan_projet";
   String FUNCTION_BURNDOWN = "burndown";
	
	String ACCESS_RIGHTS_READ = "R";
	String ACCESS_RIGHTS_WRITE = "W";

	String FUNCTION_POINT_GDI = "GDI";
	String FUNCTION_POINT_GDE = "GDE";
	String FUNCTION_POINT_IN = "IN";
	String FUNCTION_POINT_OUT = "OUT";
	String FUNCTION_POINT_INT = "INT";
	String FUNCTION_POINT_NONE = "NONE";

}
