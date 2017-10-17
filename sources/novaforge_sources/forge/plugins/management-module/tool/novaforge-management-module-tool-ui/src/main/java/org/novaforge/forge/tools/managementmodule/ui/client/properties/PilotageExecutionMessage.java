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
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.client.properties;

import com.google.gwt.i18n.client.Messages;

/**
 * @author BILET-JC
 * 
 */
public interface PilotageExecutionMessage extends Messages {

	/* tab */
	String headerBacklogTab();

	String headerBurnDownTab();

	String headerProjectMonitoringGraphTab();

	String headerPreparationTab();

	String headerGlobalMonitoringTab();

	String headerIterationListTab();

	String headerScopeUnitDisciplineTab();

	String buttonCreateWorkTask();

	String buttonCreateBugTask();

	String buttonUpdateTask();

	String buttonFollowTask();

	String buttonDeleteTask();
	
	String buttonEndIT();

	String emptyIterationList();

	String emptyTaskList();

	String refIterationList();

	String iterations();

	String iterationL();

	String lotL();

	String subLotL();

	/* iteration list */
	String buttonReportIteration();

	String buttonUpdateIteration();

	String finished();

	String open();

	String iterationNumber();

	String lot();

	String startDate();

	String endDate();

	String status();

	/* preparation */
	String impossiblePreparationMessage();

	String noMorePreparationMessage();

	String manageScopeUnitTitle();

	String emptyScopeUnitLightMessage();

	String emptyScopeUnitDisciplineMessage();

	String buttonStartScopeUnitDiscipline();

	String buttonCancelScopeUnitDiscipline();

	String buttonStopScopeUnitDiscipline();

	String scopeUnitDisciplineAllowed();

	String currentIterationL();

	String notFinishedDiscipline();

	String createScopeUnitDisciplineMessage();

	String successCancelScopeUnitDisciplineMessage();

	String failCancelScopeUnitDisciplineMessage();

	String noCurrentIteration();

	String successTerminateScopeUnitDisciplineMessage();

	String failTerminateScopeUnitDisciplineMessage();

	String selectScopeUnitDisciplineTitle();

	String scopeUnitDisciplineTitle();

	String deleteTaskSuccessMessage();

	String deleteTaskFailMessage();

	// task list
	String taskListPreparationTitle();

	String taskListBacklogTitle();

	/* backlog */
	String startDateL();

	String startDateTask();

	String endDateL();

	String endDateTask();

	String disciplineL();

	String scopeUnitParentL();

	String scopeUnitL();

	String label();

	String scopeUnitParent();

	String scopeUnit();

	String discipline();

	String responsible();

	String taskEstimation();

	String category();

	String noCategory();

	String reestimateTask();

	String consumedTime();

	String remainingTime();

	String advancement();

	String errorEstimation();

   /** Get the success message on iteration closure */
   String closeIterationSuccessMessage();

    /** Get the message for no iteration available */
   String noIterationAvailable();

   /** Get the tab title for the iteration monitoring tab */
   String headerIterationMonitoringTab();

   /** Get the label of the button which permits to access iteration monitoring */
   String buttonIterationMonitoring();

   /** Get the warning message to display when a user ask to close an iteration */
   String messageFinishIterationWarning();

   /** Get the message for "parent lot" */
   String parentLotL();

   String selectScopeUnitDiscipline();

   /** Get the message do display under the task list to indicate the consumed and remaining time are for the iteration */
   String commentConsumedAndRaf();

   
   
}
