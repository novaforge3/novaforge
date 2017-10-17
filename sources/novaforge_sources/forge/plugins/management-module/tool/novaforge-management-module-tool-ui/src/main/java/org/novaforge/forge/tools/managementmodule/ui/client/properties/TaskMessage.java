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
 * The interface to access task properties
 */
public interface TaskMessage extends Messages {

   /** Get the value of TYPE in properties */
   String type();
   /** Get the value of state in properties */
   String state();
   /** Get the value of startDate in properties */
   String startDate();
   /** Get the value of endDate in properties */
   String endDate();
   /** Get the value of currentUP in properties */
   String UP();
   /** Get the value of parentUP in properties */
   String parentUP();
   /** Get the value of taskName in properties */
   String taskName();
   /** Get the value of user in properties */
   String user();
   /** Get the value of category in properties */
   String category();
   /** Get the value of initial estimation in properties */
   String initialEstimation();
   /** Get the value of remaining time in properties */
   String remainingTime();
   /** Get the value of consumed time in properties */
   String consumedTime();
   /** Get the value of reEstimated in properties */
   String reEstimated();
   /** Get the value of comment in properties */
   String comment();
   /** Get the value of discipline in properties */
   String discipline();
   /** Get the value of suiviFieldSetTitle in properties */
   String suiviFieldSetTitle();
   /** Get the value of workTask in properties */
   String workTask();
   /** Get the value of bugTask in properties */
   String bugTask();
   /** Get the message when task datas are incorrect */
   String illegalTaskDatas();
   /** Get the task edit creation title */
   String creationTitle();
   /** Get the task edit modification title */
   String modificationTitle();
   /** Get the error when data loading failed */
   String errorDuringDataLoading();
   /** Get the message when task is saved*/
   String taskSaved();
   /** Get the message when a required field is empty*/
   String requiredField();
   /** Get the message when a the format is incorrect*/
   String incorrectFormat();
   /** Get the message when there is some incorrect fields*/
   String fieldsIncorrect();
   /** Get the message for button reopen task*/
   String buttonReOpenTask();
   /** Get the message when an user try to close a task which still have remaining time*/
   String closeWithRemainingTime();
   /** Get the label for all Disciplines*/
   String allDisciplines();
   /** Get the message for the title of the choose bug pop up */
   String chooseBugTitle();
   /** Get the message for the button of the choose bug pop up */
   String chooseBug();
   /** Get the message to indicate there is no bug in the list */
   String emptyBugList();
   /** Get the message for the column : bug tracker id */
   String bugTrackerId();
   /** Get the message for the column : bug title */
   String bugTitle();
   /** Get the message for the column : affected user */
   String bugAffectedUser();
   /** Get the message for the column : status */
   String bugStatus();
   /** Get the message for the column : severity */
   String bugSeverity();
   /** Get the message for bug */
   String bug();
   /** Get the message for more bug info */
   String buttonInfos();
   /** Get the message for the button choose a bug */
   String buttonChooseBug();
   /** Get the message for the title of task edit in supervision mode */
   String supervisionTitle();
   /** Get the message for the bug prority */
   String bugPriority();
   /** Get the message for the reporter of a bug */
   String bugReporter();
   /** Get the message to indicate the consumed has not been updated */
   String consumedNotUpdated();
}
