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
package org.novaforge.forge.tools.managementmodule.internal;

import org.novaforge.forge.tools.managementmodule.business.BusinessObjectFactory;
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
import org.novaforge.forge.tools.managementmodule.entity.AdjustFactorEntity;
import org.novaforge.forge.tools.managementmodule.entity.AdjustFactorJointureEntity;
import org.novaforge.forge.tools.managementmodule.entity.AdjustWeightEntity;
import org.novaforge.forge.tools.managementmodule.entity.ApplicativeFunctionEntity;
import org.novaforge.forge.tools.managementmodule.entity.ApplicativeRightsEntity;
import org.novaforge.forge.tools.managementmodule.entity.BugEntity;
import org.novaforge.forge.tools.managementmodule.entity.CDOParametersEntity;
import org.novaforge.forge.tools.managementmodule.entity.DisciplineEntity;
import org.novaforge.forge.tools.managementmodule.entity.EstimationComponentDetailEntity;
import org.novaforge.forge.tools.managementmodule.entity.EstimationComponentSimpleEntity;
import org.novaforge.forge.tools.managementmodule.entity.EstimationEntity;
import org.novaforge.forge.tools.managementmodule.entity.IterationEntity;
import org.novaforge.forge.tools.managementmodule.entity.IterationTaskEntity;
import org.novaforge.forge.tools.managementmodule.entity.LanguageEntity;
import org.novaforge.forge.tools.managementmodule.entity.LoadDistributionDisciplineEntity;
import org.novaforge.forge.tools.managementmodule.entity.LotEntity;
import org.novaforge.forge.tools.managementmodule.entity.MarkerEntity;
import org.novaforge.forge.tools.managementmodule.entity.MarkerTypeEntity;
import org.novaforge.forge.tools.managementmodule.entity.MembershipEntity;
import org.novaforge.forge.tools.managementmodule.entity.PhaseTypeEntity;
import org.novaforge.forge.tools.managementmodule.entity.ProjectDisciplineEntity;
import org.novaforge.forge.tools.managementmodule.entity.ProjectEntity;
import org.novaforge.forge.tools.managementmodule.entity.ProjectPlanEntity;
import org.novaforge.forge.tools.managementmodule.entity.RefCalendarEntity;
import org.novaforge.forge.tools.managementmodule.entity.RefScopeUnitEntity;
import org.novaforge.forge.tools.managementmodule.entity.RoleEntity;
import org.novaforge.forge.tools.managementmodule.entity.ScopeTypeEntity;
import org.novaforge.forge.tools.managementmodule.entity.ScopeUnitDisciplineEntity;
import org.novaforge.forge.tools.managementmodule.entity.ScopeUnitDisciplineStatusEntity;
import org.novaforge.forge.tools.managementmodule.entity.ScopeUnitEntity;
import org.novaforge.forge.tools.managementmodule.entity.SharingParamEntity;
import org.novaforge.forge.tools.managementmodule.entity.StatusProjectPlanEntity;
import org.novaforge.forge.tools.managementmodule.entity.StatusScopeEntity;
import org.novaforge.forge.tools.managementmodule.entity.StatusTaskEntity;
import org.novaforge.forge.tools.managementmodule.entity.SteeringParameterEntity;
import org.novaforge.forge.tools.managementmodule.entity.TaskCategoryEntity;
import org.novaforge.forge.tools.managementmodule.entity.TaskEntity;
import org.novaforge.forge.tools.managementmodule.entity.TaskTypeEntity;
import org.novaforge.forge.tools.managementmodule.entity.TransformationEntity;
import org.novaforge.forge.tools.managementmodule.entity.UnitTimeEntity;
import org.novaforge.forge.tools.managementmodule.entity.UserEntity;
import org.novaforge.forge.tools.managementmodule.internal.transfer.GlobalMonitoringIndicatorsImpl;
import org.novaforge.forge.tools.managementmodule.internal.transfer.IterationTaskIndicatorsImpl;
import org.novaforge.forge.tools.managementmodule.internal.transfer.MonitoringIndicatorsImpl;
import org.novaforge.forge.tools.managementmodule.internal.transfer.ScopeUnitIndicatorsImpl;
import org.novaforge.forge.tools.managementmodule.report.model.impl.DayLoadImpl;
import org.novaforge.forge.tools.managementmodule.report.model.impl.FocalisationFactorItImpl;
import org.novaforge.forge.tools.managementmodule.report.model.impl.GanttLotInfoImpl;
import org.novaforge.forge.tools.managementmodule.report.model.impl.PfeIdealItImpl;
import org.novaforge.forge.tools.managementmodule.report.model.impl.StandardDeviationItImpl;

public class BusinessObjectFactoryImpl implements BusinessObjectFactory
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AdjustFactor getInstanceAdjustFactor()
	{
		return new AdjustFactorEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AdjustWeight getInstanceAdjustWeight()
	{
		return new AdjustWeightEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Estimation getInstanceEstimation()
	{
		return new EstimationEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iteration getInstanceIteration()
	{
		return new IterationEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IterationTask getInstanceIterationTask()
	{
		return new IterationTaskEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Language getInstanceLanguage()
	{
		return new LanguageEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Lot getInstanceLot()
	{
		return new LotEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Marker getInstanceMarker()
	{
		return new MarkerEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MarkerType getInstanceMarkerType()
	{
		return new MarkerTypeEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Membership getInstanceMembership()
	{
		return new MembershipEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Discipline getInstanceDiscipline()
	{
		return new DisciplineEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PhaseType getInstancePhaseType()
	{
		return new PhaseTypeEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project getInstanceProject()
	{
		return new ProjectEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProjectPlan getInstanceProjectPlan()
	{
		return new ProjectPlanEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RefCalendar getInstanceRefCalendar()
	{
		return new RefCalendarEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RefScopeUnit getInstanceRefScopeUnit()
	{
		return new RefScopeUnitEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Role getInstanceRole()
	{
		return new RoleEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScopeType getInstanceScopeType()
	{
		return new ScopeTypeEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScopeUnit getInstanceScopeUnit()
	{
		return new ScopeUnitEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoadDistributionDiscipline getInstanceLoadDistributionDiscipline()
	{
		return new LoadDistributionDisciplineEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SharingParam getInstanceSharingParam()
	{
		return new SharingParamEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StatusProjectPlan getInstanceStatusProjectPlan()
	{
		return new StatusProjectPlanEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StatusScope getInstanceStatusScope()
	{
		return new StatusScopeEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StatusTask getInstanceStatusTask()
	{
		return new StatusTaskEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EstimationComponentSimple getInstanceEstimationComponentSimple()
	{
		return new EstimationComponentSimpleEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EstimationComponentDetail getInstanceEstimationComponentDetail()
	{
		return new EstimationComponentDetailEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ApplicativeFunction getInstanceApplicativeFunction()
	{
		return new ApplicativeFunctionEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SteeringParameter getInstanceSteeringParameter()
	{
		return new SteeringParameterEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ApplicativeRights getInstanceApplicativeRights()
	{
		return new ApplicativeRightsEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TaskCategory getInstanceTaskCategory()
	{
		return new TaskCategoryEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Task getInstanceTask()
	{
		return new TaskEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UnitTime getInstanceUnitTime()
	{
		return new UnitTimeEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getInstanceUser()
	{
		return new UserEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Transformation getInstanceTransformation()
	{
		return new TransformationEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TaskType getInstanceTaskType()
	{
		return new TaskTypeEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScopeUnitDisciplineStatus getInstanceScopeUnitDisciplineStatus()
	{
		return new ScopeUnitDisciplineStatusEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScopeUnitDiscipline getInstanceScopeUnitDiscipline()
	{
		return new ScopeUnitDisciplineEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProjectDiscipline getInstanceProjectDiscipline()
	{
		return new ProjectDisciplineEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AdjustFactorJointure getInstanceAdjustFactorJointure()
	{
		return new AdjustFactorJointureEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DayLoad getInstanceDayLoad()
	{
		return new DayLoadImpl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GanttLotInfo getInstanceGanttLotInfo()
	{
		return new GanttLotInfoImpl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bug getInstanceBug()
	{
		return new BugEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CDOParameters getInstanceCDOParameters()
	{
		return new CDOParametersEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PfeIdealIt getInstancePfeIdealIt()
	{
		return new PfeIdealItImpl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FocalisationFactorIt getInstanceFocalisationFactorIt()
	{
		return new FocalisationFactorItImpl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StandardDeviationIt getInstanceStandardDeviationIt()
	{
		return new StandardDeviationItImpl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScopeUnitIndicators getInstanceScopeUnitIndicators()
	{
		return new ScopeUnitIndicatorsImpl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MonitoringIndicators getInstanceMonitoringIndicators()
	{
		return new MonitoringIndicatorsImpl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IterationTaskIndicators getInstanceIterationTaskIndicators()
	{
		return new IterationTaskIndicatorsImpl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GlobalMonitoringIndicators getInstanceGlobalMonitoringIndicators()
	{
		return new GlobalMonitoringIndicatorsImpl();
	}
}
