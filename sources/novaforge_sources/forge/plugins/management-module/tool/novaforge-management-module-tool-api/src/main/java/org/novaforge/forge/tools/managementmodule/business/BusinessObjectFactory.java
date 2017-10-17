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

import org.novaforge.forge.tools.managementmodule.domain.AdjustFactor;
import org.novaforge.forge.tools.managementmodule.domain.AdjustFactorJointure;
import org.novaforge.forge.tools.managementmodule.domain.AdjustWeight;
import org.novaforge.forge.tools.managementmodule.domain.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.domain.ApplicativeRights;
import org.novaforge.forge.tools.managementmodule.domain.Bug;
import org.novaforge.forge.tools.managementmodule.domain.CDOParameters;
import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.Estimation;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentDetail;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentSimple;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.IterationTask;
import org.novaforge.forge.tools.managementmodule.domain.Language;
import org.novaforge.forge.tools.managementmodule.domain.LoadDistributionDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.Marker;
import org.novaforge.forge.tools.managementmodule.domain.MarkerType;
import org.novaforge.forge.tools.managementmodule.domain.Membership;
import org.novaforge.forge.tools.managementmodule.domain.PhaseType;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.ProjectDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.RefCalendar;
import org.novaforge.forge.tools.managementmodule.domain.RefScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.Role;
import org.novaforge.forge.tools.managementmodule.domain.ScopeType;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDisciplineStatus;
import org.novaforge.forge.tools.managementmodule.domain.SharingParam;
import org.novaforge.forge.tools.managementmodule.domain.StatusProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.StatusScope;
import org.novaforge.forge.tools.managementmodule.domain.StatusTask;
import org.novaforge.forge.tools.managementmodule.domain.SteeringParameter;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.TaskCategory;
import org.novaforge.forge.tools.managementmodule.domain.TaskType;
import org.novaforge.forge.tools.managementmodule.domain.Transformation;
import org.novaforge.forge.tools.managementmodule.domain.UnitTime;
import org.novaforge.forge.tools.managementmodule.domain.User;
import org.novaforge.forge.tools.managementmodule.domain.report.DayLoad;
import org.novaforge.forge.tools.managementmodule.domain.report.FocalisationFactorIt;
import org.novaforge.forge.tools.managementmodule.domain.report.GanttLotInfo;
import org.novaforge.forge.tools.managementmodule.domain.report.PfeIdealIt;
import org.novaforge.forge.tools.managementmodule.domain.report.StandardDeviationIt;
import org.novaforge.forge.tools.managementmodule.domain.transfer.GlobalMonitoringIndicators;
import org.novaforge.forge.tools.managementmodule.domain.transfer.IterationTaskIndicators;
import org.novaforge.forge.tools.managementmodule.domain.transfer.MonitoringIndicators;
import org.novaforge.forge.tools.managementmodule.domain.transfer.ScopeUnitIndicators;

/**
 * @author sbenoist
 *
 */
public interface BusinessObjectFactory
{

	/**
	 * Creates and returns an AdjustFactor
	 * 
	 * @return AdjustFactor
	 */
	AdjustFactor getInstanceAdjustFactor();

	/**
	 * Creates and returns an AdjustWeight
	 * 
	 * @return AdjustWeight
	 */
	AdjustWeight getInstanceAdjustWeight();

	/**
	 * Creates and returns an Estimation
	 * 
	 * @return Estimation
	 */
	Estimation getInstanceEstimation();

	/**
	 * Creates and returns an Iteration
	 * 
	 * @return Iteration
	 */
	Iteration getInstanceIteration();

	/**
	 * Creates and returns an IterationTask
	 * 
	 * @return IterationTask
	 */
	IterationTask getInstanceIterationTask();

	/**
	 * Creates and returns a Language
	 * 
	 * @return Language
	 */
	Language getInstanceLanguage();

	/**
	 * Creates and returns a Lot
	 * 
	 * @return Lot
	 */
	Lot getInstanceLot();

	/**
	 * Creates and returns a Marker
	 * 
	 * @return Marker
	 */
	Marker getInstanceMarker();

	/**
	 * Creates and returns a MarkerType
	 * 
	 * @return MarkerType
	 */
	MarkerType getInstanceMarkerType();

	/**
	 * Creates and returns a Membership
	 * 
	 * @return Membership
	 */
	Membership getInstanceMembership();

	/**
	 * Creates and returns a Discipline
	 * 
	 * @return Discipline
	 */
	Discipline getInstanceDiscipline();

	/**
	 * Creates and returns a PhaseType
	 * 
	 * @return PhaseType
	 */
	PhaseType getInstancePhaseType();

	/**
	 * Creates and returns a Project
	 * 
	 * @return Project
	 */
	Project getInstanceProject();

	/**
	 * Creates and returns a ProjectPlan
	 * 
	 * @return ProjectPlan
	 */
	ProjectPlan getInstanceProjectPlan();

	/**
	 * Creates and returns a RefCalendar
	 * 
	 * @return RefCalendar
	 */
	RefCalendar getInstanceRefCalendar();

	/**
	 * Creates and returns a RefScopeUnit
	 * 
	 * @return RefScopeUnit
	 */
	RefScopeUnit getInstanceRefScopeUnit();

	/**
	 * Creates and returns a Role
	 * 
	 * @return Role
	 */
	Role getInstanceRole();

	/**
	 * Creates and returns a ScopeType
	 * 
	 * @return ScopeType
	 */
	ScopeType getInstanceScopeType();

	/**
	 * Creates and returns a ScopeUnit
	 * 
	 * @return ScopeUnit
	 */
	ScopeUnit getInstanceScopeUnit();

	/**
	 * Creates and returns a LoadDistributionDiscipline
	 * 
	 * @return LoadDistributionDiscipline
	 */
	LoadDistributionDiscipline getInstanceLoadDistributionDiscipline();

	/**
	 * Creates and returns a SharingParam
	 * 
	 * @return SharingParam
	 */
	SharingParam getInstanceSharingParam();

	/**
	 * Creates and returns a StatusProjectPlan
	 * 
	 * @return StatusProjectPlan
	 */
	StatusProjectPlan getInstanceStatusProjectPlan();

	/**
	 * Creates and returns a StatusScope
	 * 
	 * @return StatusScope
	 */
	StatusScope getInstanceStatusScope();

	/**
	 * Creates and returns a StatusTask
	 * 
	 * @return StatusTask
	 */
	StatusTask getInstanceStatusTask();

	/**
	 * Creates and returns a EstimationComponentSimple
	 * 
	 * @return EstimationComponentSimple
	 */
	EstimationComponentSimple getInstanceEstimationComponentSimple();

	/**
	 * Creates and returns a EstimationComponentDetail
	 * 
	 * @return v
	 */
	EstimationComponentDetail getInstanceEstimationComponentDetail();

	/**
	 * Creates and returns an ApplicativeFunction
	 * 
	 * @return ApplicativeFunction
	 */
	ApplicativeFunction getInstanceApplicativeFunction();

	/**
	 * Creates and returns a SteeringParameter
	 * 
	 * @return SteeringParameter
	 */
	SteeringParameter getInstanceSteeringParameter();

	/**
	 * Creates and returns an ApplicativeRights
	 * 
	 * @return ApplicativeRights
	 */
	ApplicativeRights getInstanceApplicativeRights();

	/**
	 * Creates and returns a TaskCategory
	 * 
	 * @return TaskCategory
	 */
	TaskCategory getInstanceTaskCategory();

	/**
	 * Creates and returns a Task
	 * 
	 * @return Task
	 */
	Task getInstanceTask();

	/**
	 * Creates and returns a UnitTime
	 * 
	 * @return UnitTime
	 */
	UnitTime getInstanceUnitTime();

	/**
	 * Creates and returns a User
	 * 
	 * @return User
	 */
	User getInstanceUser();

	/**
	 * Creates and returns a Transformation
	 * 
	 * @return User
	 */
	Transformation getInstanceTransformation();

	/**
	 * Creates and returns a TaskType
	 * 
	 * @return TaskType
	 */
	TaskType getInstanceTaskType();

	/**
	 * Creates and returns a ScopeUnitDisciplineStatus
	 * 
	 * @return ScopeUnitDisciplineStatus
	 */
	ScopeUnitDisciplineStatus getInstanceScopeUnitDisciplineStatus();

	/**
	 * Creates and returns a ScopeUnitDiscipline
	 * 
	 * @return ScopeUnitDiscipline
	 */
	ScopeUnitDiscipline getInstanceScopeUnitDiscipline();

	/**
	 * Creates and returns a DisciplineJointure
	 * 
	 * @return DisciplineJointure
	 */
	ProjectDiscipline getInstanceProjectDiscipline();

	/**
	 * Creates and returns a AdjustFactorJointure
	 * 
	 * @return AdjustFactorJointure
	 */
	AdjustFactorJointure getInstanceAdjustFactorJointure();

	/**
	 * Creates and returns a DayLoad
	 * 
	 * @return DayLoad
	 */
	DayLoad getInstanceDayLoad();

	/**
	 * Creates and returns a GanttLotInfo
	 * 
	 * @return GanttLotInfo
	 */
	GanttLotInfo getInstanceGanttLotInfo();

	/**
	 * Creates and returns a Bug
	 * 
	 * @return the created bug
	 */
	Bug getInstanceBug();

	/**
	 * Creates and returns a CDOParameters
	 * 
	 * @return the created CDOParameters
	 */
	CDOParameters getInstanceCDOParameters();

	/**
	 * Creates and returns a DayLoad
	 * 
	 * @return DayLoad
	 */
	PfeIdealIt getInstancePfeIdealIt();

	/**
	 * Creates and returns a FocalisationFactorIt
	 * 
	 * @return FocalisationFactorIt
	 */
	FocalisationFactorIt getInstanceFocalisationFactorIt();

	/**
	 * Creates and returns a StandardDeviationIt
	 * 
	 * @return StandardDeviationIt
	 */
	StandardDeviationIt getInstanceStandardDeviationIt();

	/**
	 * Creates and returns a ScopeUnitIndicators
	 * 
	 * @return ScopeUnitIndicators
	 */
	ScopeUnitIndicators getInstanceScopeUnitIndicators();

	/**
	 * Creates and returns a MonitoringIndicators
	 * 
	 * @return MonitoringIndicators
	 */
	MonitoringIndicators getInstanceMonitoringIndicators();

	/**
	 * Creates and returns a IterationTaskIndicators
	 * 
	 * @return IterationTaskIndicators
	 */
	IterationTaskIndicators getInstanceIterationTaskIndicators();

	/**
	 * Creates and returns a GlobalMonitoringIndicators
	 * 
	 * @return GlobalMonitoringIndicators
	 */
	GlobalMonitoringIndicators getInstanceGlobalMonitoringIndicators();

}