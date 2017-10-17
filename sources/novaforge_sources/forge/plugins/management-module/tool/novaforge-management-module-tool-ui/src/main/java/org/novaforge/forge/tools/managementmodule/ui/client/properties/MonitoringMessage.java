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
public interface MonitoringMessage extends Messages {

	String scopeUnitMonitorings();

	// context
	String projectStartDateL();

	String lotL();

	String subLotL();

	// indicators
	String scopeUnitNumberL();

	String percentScopeUnitDoneL();

	String scopeUnitToDoL();

	String averageFocalisationFactorL();

	String averageErrorEstimationL();

	String lastIdealFPL();

	String averageVelocityL();

	String globalAdvancement();

	// filter
	String scopeUnitParentL();

	String scopeUnitL();

	String scopeUnitStatusL();

	// columns
	String subLot();

	String lot();

	String scopeUnitParent();

	String scopeUnit();

	String estimation();

	String consumed();

	String remainingScopeUnit();

	String remainingTask();

	String reestimate();

	String advancement();

	String priority();

	String injury();

	String risk();

	String benefit();

	String status();

	// status
	String statusNotStarted();

	String statusInProgress();

	String statusFinished();

	// action

	String acronym();

	String finishScopeUnit();

	// box messages
	String messageFinishScopeUnitImpossibleTask();

	String messageFinishScopeUnitImpossibleChild();

	String messageFinishScopeUnitImpossibleRemaining();

	String messageFinishScopeUnitSuccess();

	String messageUpdateRemainingScopeUnitSuccess();

	String messageUpdateRemainingScopeUnitUnsuccess();

	String messageFinishScopeUnitWarning();

	String iterationDetailButton();

	String iterationBurndownButton();

	String iterationLabel();

	String startDateLabel();

	String endDateLabel();

	String nbActorsLabel();

	String consumedLabel();

	String advancementLabel();

	String disciplineLabel();

	String noIterationAvailable();

	// parent details
	String parentDetailsTitle();

	String estimationL();

	String statusL();

	String remainingScopeUnitL();

	String remainingTaskL();

	String reestimateL();

	String parentLotL();

	String parentLot();

	String workToDo();

	String focalisationFactorLabel();

	String velocityLabel();

}
