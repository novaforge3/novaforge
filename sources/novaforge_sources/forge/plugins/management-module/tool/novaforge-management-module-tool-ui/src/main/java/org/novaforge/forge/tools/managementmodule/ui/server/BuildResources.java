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

package org.novaforge.forge.tools.managementmodule.ui.server;

import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.domain.AdjustFactor;
import org.novaforge.forge.tools.managementmodule.domain.AdjustFactorJointure;
import org.novaforge.forge.tools.managementmodule.domain.AdjustWeight;
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
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.Marker;
import org.novaforge.forge.tools.managementmodule.domain.MarkerType;
import org.novaforge.forge.tools.managementmodule.domain.Membership;
import org.novaforge.forge.tools.managementmodule.domain.PhaseType;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.ProjectDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.RefScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.ScopeType;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDisciplineStatus;
import org.novaforge.forge.tools.managementmodule.domain.StatusProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.StatusScope;
import org.novaforge.forge.tools.managementmodule.domain.StatusTask;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.TaskCategory;
import org.novaforge.forge.tools.managementmodule.domain.Transformation;
import org.novaforge.forge.tools.managementmodule.domain.User;
import org.novaforge.forge.tools.managementmodule.domain.transfer.GlobalMonitoringIndicators;
import org.novaforge.forge.tools.managementmodule.domain.transfer.IterationTaskIndicators;
import org.novaforge.forge.tools.managementmodule.domain.transfer.MonitoringIndicators;
import org.novaforge.forge.tools.managementmodule.domain.transfer.ScopeUnitIndicators;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustFactorDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustFactorJointureDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustWeightDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeRightsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.BugDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.CDOParametersDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ComponentEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.DateComparator;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationComponentDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationComponentDetailDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationComponentSimpleDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.GlobalMonitoringIndicatorsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationTaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LanguageDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.MarkerDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.MarkerTypeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.MonitoringIndicatorsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.PhaseTypeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeTypeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineStatusDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitGlobalMonitoringDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitIterationMonitoringDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitLightDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitMonitoringDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitMonitoringStatusEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.StatusProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.StatusScopeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskCategoryDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskStatusDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TransformationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.UserDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.chargeplan.ChargePlanLineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.chargeplan.ChargePlanMainDataDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.chargeplan.ChargePlanToolTipDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * This class is used to build DTO UI Object using the domain object
 */
public class BuildResources
{

  /**
   * private constructor
   */
  private BuildResources()
  {
  }
  
	/**
	 * @param projectPlan
	 * @return
	 */
	public static ProjectPlanDTO buildProjectPlanDTOFullFromProjectPlan(final ProjectPlan projectPlan)
	{
		ProjectPlanDTO dto;
		dto = new ProjectPlanDTO();
		dto.setProjectPlanId(projectPlan.getId());
		dto.setDate(new Date(projectPlan.getDate().getTime()));
		dto.setProjectId(projectPlan.getProject().getProjectId());
		dto.setStatusLabel(projectPlan.getStatus().getName());
		dto.setStatus(ServerToClientMapper.getProjectPlanStatusFromServerStringValue(projectPlan.getStatus()
				.getFunctionalId()));
		dto.setVersion(projectPlan.getVersion());
		dto.setProjectName(projectPlan.getProject().getName());
		List<AdjustFactorJointureDTO> adjustFactorJointureDTOList = new LinkedList<AdjustFactorJointureDTO>();
		Set<AdjustFactorJointure> adjustFactorJointurList = projectPlan.getAdjustFactorJointure();
		for (AdjustFactorJointure afj : adjustFactorJointurList)
		{
			AdjustFactorJointureDTO afjdto = new AdjustFactorJointureDTO();
			afjdto.setId(afj.getId());
			afjdto.setAdjustFactor(buildAdjustFactorDTOFromAdjustFactor(afj.getAdjustFactor()));
			afjdto.setAdjustWeight(buildAdjustWeightDTOFromAdjustWeight(afj.getAdjustWeight()));
			adjustFactorJointureDTOList.add(afjdto);
		}
		dto.setAdjustFactorJointureList(adjustFactorJointureDTOList);
		dto.setEstimationComponentDetail(buildEstimationComponentDetailDTOFromEstimationComponentDetail(projectPlan
				.getEstimationComponentDetail()));
		return dto;
	}

	/**
	 * Build a AdjustFactorDTO
	 *
	 * @param AdjustFactor
	 * @return the appropriate AdjustFactorDTO
	 */
	public static AdjustFactorDTO buildAdjustFactorDTOFromAdjustFactor(final AdjustFactor adjustFactor)
	{
		AdjustFactorDTO dto = new AdjustFactorDTO();
		dto.setFunctionalId(adjustFactor.getFunctionalId());
		dto.setName(adjustFactor.getName());

		return dto;
	}

	/**
	 * Build a AdjustWeightDTO
	 *
	 * @param AdjustWeight
	 *
	 * @return the appropriate AdjustWeightDTO
	 */
	public static AdjustWeightDTO buildAdjustWeightDTOFromAdjustWeight(final AdjustWeight adjustWeight)
	{
		AdjustWeightDTO dto = new AdjustWeightDTO();
		dto.setWeight(adjustWeight.getWeight());
		dto.setFunctionalId(adjustWeight.getFunctionalId());
		dto.setName(adjustWeight.getName());
		return dto;
	}

	/**
	 * Build a EstimationComponentDetailDTO
	 *
	 * @param estimationComponentDetail
	 *
	 * @return the appropriate EstimationComponentDetailDTO
	 */
	public static EstimationComponentDetailDTO buildEstimationComponentDetailDTOFromEstimationComponentDetail(final EstimationComponentDetail estimationComponentDetail)
	{
		EstimationComponentDetailDTO dto = new EstimationComponentDetailDTO();
		dto.setFunctionalId(estimationComponentDetail.getFunctionalId());
		dto.setValueAbaChgHomJour(estimationComponentDetail.getValueAbaChgHomJour());
		dto.setValueComplexGDE(estimationComponentDetail.getValueComplexGDE());
		dto.setValueComplexGDI(estimationComponentDetail.getValueComplexGDI());
		dto.setValueComplexIN(estimationComponentDetail.getValueComplexIN());
		dto.setValueComplexINT(estimationComponentDetail.getValueComplexINT());
		dto.setValueComplexOUT(estimationComponentDetail.getValueComplexOUT());
		dto.setValueMoyenGDE(estimationComponentDetail.getValueMoyenGDE());
		dto.setValueMoyenGDI(estimationComponentDetail.getValueMoyenGDI());
		dto.setValueMoyenIN(estimationComponentDetail.getValueMoyenIN());
		dto.setValueMoyenINT(estimationComponentDetail.getValueMoyenINT());
		dto.setValueMoyenOUT(estimationComponentDetail.getValueMoyenOUT());
		dto.setValueSimpleGDE(estimationComponentDetail.getValueSimpleGDE());
		dto.setValueSimpleGDI(estimationComponentDetail.getValueSimpleGDI());
		dto.setValueSimpleIN(estimationComponentDetail.getValueSimpleIN());
		dto.setValueSimpleINT(estimationComponentDetail.getValueSimpleINT());
		dto.setValueSimpleOUT(estimationComponentDetail.getValueSimpleOUT());
		return dto;
	}

	/**
	 * @param projectPlan
	 * @return
	 */
	public static ProjectPlanDTO buildProjectPlanDTOFromProjectPlan(final ProjectPlan projectPlan)
	{
		ProjectPlanDTO dto;
		dto = new ProjectPlanDTO();
		dto.setProjectPlanId(projectPlan.getId());
		dto.setDate(new Date(projectPlan.getDate().getTime()));
		dto.setProjectId(projectPlan.getProject().getProjectId());
		dto.setStatusLabel(projectPlan.getStatus().getName());
		dto.setStatus(ServerToClientMapper.getProjectPlanStatusFromServerStringValue(projectPlan.getStatus()
				.getFunctionalId()));
		dto.setVersion(projectPlan.getVersion());
		dto.setProjectName(projectPlan.getProject().getName());
		return dto;
	}

	/**
	 * Build an iterationDTO without its set of IterationTask
	 *
	 * @param iteration
	 * @return IterationDTO
	 */
	public static IterationDTO buildSimpleIterationDTOFromIteration(final Iteration iteration)
	{
		IterationDTO dto = new IterationDTO();
		dto.setIterationId(iteration.getId());
		dto.setLabel(iteration.getLabel());
		dto.setNumIteration(iteration.getNumIteration());
		dto.setStartDate(new Date(iteration.getStartDate().getTime()));
		dto.setEndDate(new Date(iteration.getEndDate().getTime()));
		dto.setFinished(iteration.isFinished());
		dto.setPhaseType(buildPhaseTypeDTOFromPhaseType(iteration.getPhaseType()));
		dto.setLot(buildLotDTOFromLot(iteration.getLot()));
		dto.setComplex(false);
		return dto;
	}

	/**
	 * use buildSimpleIterationDTOFromIteration() instead and
	 * buildIterationTaskDTOFromIterationTask for each iterationTask Build an
	 * iterationDTO with its set of IterationTask
	 *
	 * @param iteration
	 * @return IterationDTO
	 */
	@Deprecated
	public static IterationDTO buildComplexIterationDTOFromIteration(final Iteration iteration)
	{

		IterationDTO dto = buildSimpleIterationDTOFromIteration(iteration);
		Set<IterationTaskDTO> iterationTasks = new HashSet<IterationTaskDTO>();
		for (IterationTask iterationTask : iteration.getIterationTasks())
		{
			iterationTasks.add(buildIterationTaskDTOFromIterationTask(iterationTask));
		}
		dto.setIterationTasks(iterationTasks);
		dto.setComplex(true);
		return dto;
	}

	/**
	 * use IterationTaskDTO buildIterationTaskDTOFromIterationTask(IterationTask
	 * iterationTask, IterationTaskIndicators indicators) instead
	 */
	@Deprecated
	public static IterationTaskDTO buildIterationTaskDTOFromIterationTask(final IterationTask iterationTask)
	{
		IterationTaskDTO dto = new IterationTaskDTO();
		dto.setConsumedTime(iterationTask.getConsumedTime());
		dto.setId(iterationTask.getId());
		dto.setLastUpdateTime(new Date(iterationTask.getLastUpdateDate().getTime()));
		dto.setRemainingTime(iterationTask.getRemainingTime());
		dto.setTask(buildTaskDTOFromTask(iterationTask.getTask()));
		return dto;
	}

	/**
	 * Build an iterationTaskDTO with an IterationTask an its indicators
	 *
	 * @param iterationTask
	 * @param indicators
	 * @return the constructed IterationTaskDTO
	 */
	public static IterationTaskDTO buildIterationTaskDTOFromIterationTask(final IterationTask iterationTask,
			final IterationTaskIndicators indicators)
	{
		IterationTaskDTO dto = new IterationTaskDTO();
		dto.setConsumedTime(iterationTask.getConsumedTime());
		dto.setId(iterationTask.getId());
		dto.setLastUpdateTime(new Date(iterationTask.getLastUpdateDate().getTime()));
		dto.setRemainingTime(iterationTask.getRemainingTime());
		dto.setTask(buildTaskDTOFromTask(iterationTask.getTask()));
		dto.setReestimate(indicators.getReestimate());
		dto.setAdvancement(indicators.getAdvancement());
		dto.setErrorEstimation(indicators.getErrorEstimation());
		return dto;
	}

	/**
	 * Build a TaskDTO (for UI) from a Task (Server)
	 *
	 * @param task
	 *          the task to use to build the TaskDTO
	 * @return the created TaskDTO
	 */
	public static TaskDTO buildTaskDTOFromTask(final Task task)
	{
		final TaskDTO dto = new TaskDTO();
		dto.setId(task.getId());
		dto.setLabel(task.getName());
		dto.setType(ServerToClientMapper.getTaskTypeEnumFromServerValue(task.getTaskType()));
		final DisciplineDTO disciplineDTO = buildDisciplineDTO(task.getDiscipline());
		dto.setDiscipline(disciplineDTO);
		dto.setInitialEstimation(task.getInitialEstimation());
		final TaskStatusDTO status = buildTaskStatusDTO(task.getStatus(), true);
		dto.setStatus(status);
		if (task.getStartDate() != null )
		{
		  dto.setStartDate(new Date(task.getStartDate().getTime()));
		}
		if ( task.getEndDate() != null )
		{
		  dto.setEndDate(new Date(task.getEndDate().getTime()));
		}
		if (task.getTaskCategory() != null)
		{
			final TaskCategoryDTO category = buildTaskCategoryDTO(task.getTaskCategory());
			dto.setCategory(category);
		}
		if (task.getScopeUnit() != null)
		{
			final ScopeUnitDTO scopeUnit = buildScopeUnitDTOFromScopeUnit(task.getScopeUnit());
			dto.setScopeUnit(scopeUnit);
		}
		dto.setRemainingTime(task.getRemainingTime());
		dto.setConsumedTime(task.getConsumedTime());
		dto.setCommentary(task.getComment());
		dto.setDescription(task.getDescription());
		final IterationDTO iteration = buildSimpleIterationDTOFromIteration(task.getCurrentIteration());
		dto.setIteration(iteration);
		if (task.getUser() != null)
		{
			final UserDTO user = buildUserDTO(task.getUser());
			dto.setUser(user);
		}
		if (task.getBug() != null)
		{
			dto.setBug(buildBugDTOFromBug(task.getBug()));
		}
		return dto;
	}

	/**
	 * Build a TaskStatusDTO from a TaskStatus
	 *
	 * @param status
	 *          the status to use to build the correct DTO
	 * @param defaultStatus
	 *          boolean whcih indicates if this status is the default one
	 * @return the DTO
	 */
	public static TaskStatusDTO buildTaskStatusDTO(final StatusTask status, final boolean defaultStatus)
	{
		final TaskStatusDTO statusDTO = new TaskStatusDTO();
		statusDTO.setFunctionalId(status.getFunctionalId());
		statusDTO.setLabel(status.getName());
		statusDTO.setDefaultStatus(defaultStatus);
		statusDTO.setEnumValue(ServerToClientMapper.getTaskStatusEnumFromServerValue(status));
		return statusDTO;
	}

	/**
	 * Build a TaskCategoryDTO from a TaskCategory
	 *
	 * @param status
	 *          the status to use to build the correct DTO
	 * @return the DTO
	 */
	public static TaskCategoryDTO buildTaskCategoryDTO(final TaskCategory category)
	{
		final TaskCategoryDTO categoryDTO = new TaskCategoryDTO();
		categoryDTO.setId(category.getId());
		categoryDTO.setName(category.getName());
		return categoryDTO;
	}

	public static PhaseTypeDTO buildPhaseTypeDTOFromPhaseType(final PhaseType phaseType)
	{
		PhaseTypeDTO dto;
		dto = new PhaseTypeDTO();
		dto.setFunctionalId(phaseType.getFunctionalId());
		dto.setName(phaseType.getName());
		return dto;
	}

	public static LotDTO buildLotWithChildsDTOFromLot(final Lot lot)
	{
		LotDTO dto;
		dto = new LotDTO();
		dto.setLotId(lot.getId());
		dto.setName(lot.getName());
		dto.setStartDate(new Date(lot.getStartDate().getTime()));
		dto.setEndDate(new Date(lot.getEndDate().getTime()));
		dto.setDesc(lot.getDescription());
		dto.setChilds(new ArrayList<LotDTO>());

		if (lot.getParentLot() != null)
		{
			dto.setParentLotId(lot.getParentLot().getId());
			dto.setParentLotName(lot.getParentLot().getName());
		}
		if (lot.getProjectPlan() != null)
		{
			dto.setpPlanId(lot.getProjectPlan().getId());
		}

		if (lot.getChildLots() != null)
		{
			for (Lot childs : lot.getChildLots())
			{
				dto.getChilds().add(buildLotDTOFromLot(childs));
			}
		}

		return dto;
	}

	public static LotDTO buildLotDTOFromLot(final Lot lot)
	{
		LotDTO dto;
		dto = new LotDTO();
		dto.setLotId(lot.getId());
		dto.setName(lot.getName());
		dto.setStartDate(new Date(lot.getStartDate().getTime()));
		dto.setEndDate(new Date(lot.getEndDate().getTime()));
		dto.setDesc(lot.getDescription());

		if (lot.getParentLot() != null)
		{
			dto.setParentLotId(lot.getParentLot().getId());
			dto.setParentLotName(lot.getParentLot().getName());
		}

		if (lot.getProjectPlan() != null)
		{
			dto.setpPlanId(lot.getProjectPlan().getId());
		}

		return dto;
	}

	public static MarkerDTO buildMarkerDTOFromMarker(final Marker marker)
	{
		MarkerDTO dto;
		dto = new MarkerDTO();
		dto.setId(marker.getId());
		dto.setName(marker.getName());
		dto.setDate(new Date(marker.getDate().getTime()));
		dto.setDesc(marker.getDescription());

		dto.setpPlanId(marker.getProjectPlan().getId());
		dto.setMarkerTypeFunctionalId(marker.getType().getFunctionalId());
		dto.setMarkerTypeName(marker.getType().getName());

		return dto;
	}

	public static MarkerTypeDTO buildMarkerTypeDTOFromMarkerType(final MarkerType markerType)
	{
		MarkerTypeDTO dto = new MarkerTypeDTO();
		dto.setId(markerType.getId());
		dto.setFunctionalId(markerType.getFunctionalId());
		dto.setName(markerType.getName());

		return dto;
	}

	/**
	 * This method creates a {@link ScopeUnitDTO} based on a {@link ScopeUnit}
	 *
	 * @param pScopeUnit
	 * @return ScopeUnitDTO
	 */
	public static ScopeUnitDTO buildScopeUnitDTOFromScopeUnit(final ScopeUnit pScopeUnit)
	{
		ScopeUnitDTO dto = new ScopeUnitDTO();
		dto.setUnitId(pScopeUnit.getUnitId());
		dto.setName(pScopeUnit.getName());
		dto.setDate(new Date(pScopeUnit.getDate().getTime()));
		dto.setStatus(getStatusScopeUnit(pScopeUnit));
		// if estimation already attached
		if (pScopeUnit.getDescription() != null)
		{
			dto.setDescription(pScopeUnit.getDescription());
		}
		if (pScopeUnit.getVersion() != null)
		{
			dto.setVersion(pScopeUnit.getVersion());
		}
		dto.setLotName(pScopeUnit.getLot().getName());
		if (pScopeUnit.getLot().getParentLot() != null)
		{
			dto.setParentLotName(pScopeUnit.getLot().getParentLot().getName());
		}
		// about parent scope unit
		if (pScopeUnit.getParentScopeUnit() != null)
		{
			dto.setParentScopeUnit(buildScopeUnitDTOFromScopeUnit(pScopeUnit.getParentScopeUnit()));
		}
		// about child(s) scope unit
		if (pScopeUnit.getChildscopeunit() != null && pScopeUnit.getChildscopeunit().size() != 0)
		{
			dto.setHasChild(true);
		}
		else
		{
			dto.setHasChild(false);
		}
		return dto;
	}

	/**
	 * This method build an {@link EstimationDTO} from an {@link Estimation}
	 *
	 * @param pEstimation
	 *          , an {@link Estimation}
	 * @return {@link EstimationDTO}
	 */
	public static EstimationDTO buildEstimationDTOFromEstimation(final Estimation pEstimation)
	{
		EstimationDTO dto = new EstimationDTO();
		dto.setScopeUnit(buildScopeUnitDTOFromScopeUnit(pEstimation.getScopeUnit()));
		dto.setGlobalSimple(pEstimation.getGlobalSimple());
		dto.setGlobalMedian(pEstimation.getGlobalMedian());
		dto.setGlobalComplex(pEstimation.getGlobalComplex());
		dto.setGDIsimple(pEstimation.getGDIsimple());
		dto.setGDImedian(pEstimation.getGDImedian());
		dto.setGDIcomplex(pEstimation.getGDIcomplex());
		dto.setGDEsimple(pEstimation.getGDEsimple());
		dto.setGDEmedian(pEstimation.getGDEmedian());
		dto.setGDEcomplex(pEstimation.getGDEcomplex());
		dto.setINsimple(pEstimation.getINsimple());
		dto.setINmedian(pEstimation.getINmedian());
		dto.setINcomplex(pEstimation.getINcomplex());
		dto.setOUTsimple(pEstimation.getOUTsimple());
		dto.setOUTmedian(pEstimation.getOUTmedian());
		dto.setOUTcomplex(pEstimation.getOUTcomplex());
		dto.setInterrogationSimple(pEstimation.getINTsimple());
		dto.setInterrogationMedian(pEstimation.getINTmedian());
		dto.setInterrogationComplex(pEstimation.getINTcomplex());
		dto.setPfRaw(pEstimation.getFPRaw());
		dto.setBenefit(pEstimation.getBenefit());
		dto.setInjury(pEstimation.getInjury());
		dto.setRisk(pEstimation.getRisk());
		dto.setWeight(pEstimation.getWeight());
		dto.setSimple(ComponentEnum.getEnum(pEstimation.getSimple()));
		dto.setManual(pEstimation.isManual());
		dto.setLastCharge(checkCharge(pEstimation.getLastCharge()));
		dto.setCharge(checkCharge(pEstimation.getLastCharge()));
		if (pEstimation.getRemaining() != null)
		{
			dto.setRemaining(pEstimation.getRemaining());
		}
		return dto;
	}

	/**
	 * This method makes sure the charge parameter never be null Either it takes
	 * the lastCharge value or 0 by default
	 *
	 * @param pEstimation
	 * @return Integer, the charge
	 */
	private static Integer checkCharge(final Integer pCharge)
	{
		Integer ret = 0;
		if (pCharge != null)
		{
			ret = pCharge;
		}
		return ret;
	}

	/**
	 * This method creates an {@link EstimationComponentDTO} thanks to different
	 * parameters
	 *
	 * @param pDetail
	 * @param pSimple
	 * @param pNbHourByDay
	 * @return {@link EstimationComponentDTO}
	 */
	public static EstimationComponentDTO buildEstimationComponentDetailDTOFromEstimationComponents(
			final EstimationComponentDetail pDetail, final EstimationComponentSimple pSimple,
			final Transformation pTransformation)
	{
		EstimationComponentDTO dto = new EstimationComponentDTO();
		if (pDetail != null)
		{
			dto.setGDIsimple(pDetail.getValueSimpleGDI());
			dto.setGDImedian(pDetail.getValueMoyenGDI());
			dto.setGDIcomplex(pDetail.getValueComplexGDI());
			dto.setGDEsimple(pDetail.getValueSimpleGDE());
			dto.setGDEmedian(pDetail.getValueMoyenGDE());
			dto.setGDEcomplex(pDetail.getValueComplexGDE());
			dto.setINsimple(pDetail.getValueSimpleIN());
			dto.setINmedian(pDetail.getValueMoyenIN());
			dto.setINcomplex(pDetail.getValueComplexIN());
			dto.setOUTsimple(pDetail.getValueSimpleOUT());
			dto.setOUTmedian(pDetail.getValueMoyenOUT());
			dto.setOUTcomplex(pDetail.getValueComplexOUT());
			dto.setINTsimple(pDetail.getValueSimpleINT());
			dto.setINTmedian(pDetail.getValueMoyenINT());
			dto.setINTcomplex(pDetail.getValueComplexINT());
			dto.setAbacusChargeMenByDay(pDetail.getValueAbaChgHomJour());
			dto.setAdjustementCoef(pDetail.getAdjustementCoef());
		}
		if (pSimple != null)
		{
			dto.setGDI(pSimple.getValueGDI());
			dto.setGDE(pSimple.getValueGDE());
			dto.setIN(pSimple.getValueENT());
			dto.setOUT(pSimple.getValueSOR());
			dto.setINT(pSimple.getValueINT());
		}
		if (pTransformation != null)
		{
			dto.setNbHourByDay(new Float(pTransformation.getNbHeuresJour()));
		}
		return dto;
	}

	public static ScopeUnitDTO buildRefScopeUnitDTO(final RefScopeUnit refScopeUnit)
	{
		ScopeUnitDTO newScopeUnit = new ScopeUnitDTO();
		newScopeUnit.setName(refScopeUnit.getName());
		newScopeUnit.setDescription(refScopeUnit.getDescription());
		newScopeUnit.setUnitId(refScopeUnit.getUnitId());
		newScopeUnit.setManual(false);
		newScopeUnit.setRefScopeUnitVersion(refScopeUnit.getVersion());
		newScopeUnit.setRefScopeUnitId(refScopeUnit.getUnitId());

		if (refScopeUnit.getProject() != null)
		{
			newScopeUnit.setProjectId(refScopeUnit.getProject().getProjectId());
		}

		List<ScopeUnitDTO> scopeUnitChildList = new ArrayList<ScopeUnitDTO>();
		if (refScopeUnit.getChildScopeUnit() != null && refScopeUnit.getChildScopeUnit().size() > 0)
		{
			for (RefScopeUnit itRefScopeUnit : refScopeUnit.getChildScopeUnit())
			{
				if (!itRefScopeUnit.getState().getFunctionalId()
						.equalsIgnoreCase(ManagementModuleConstants.SCOPE_STATUS_OBSOLETE))
				{
					ScopeUnitDTO childScopeUnit = buildRefScopeUnitDTO(itRefScopeUnit);
					scopeUnitChildList.add(childScopeUnit);
				}
			}
		}
		newScopeUnit.setChildrenScopeUnit(scopeUnitChildList);

		newScopeUnit.setType(refScopeUnit.getType().getName());

		return newScopeUnit;
	}

	public static ScopeUnitDTO buildRootRefScopeUnitDTO(final CDOParameters cdo, final String projectId)
	{
		ScopeUnitDTO newScopeUnit = new ScopeUnitDTO();

		newScopeUnit.setName(cdo.getProjetCdo() + "-" + cdo.getSystemGraal());
		newScopeUnit.setDescription(cdo.getHost() + ":" + cdo.getPort() + "/" + cdo.getRepository() + "/"
				+ cdo.getSystemGraal() + "/" + cdo.getProjetCdo() + "/" + cdo.getSystemGraal());
		newScopeUnit.setUnitId("ROOT");
		newScopeUnit.setManual(false);
		newScopeUnit.setRefScopeUnitVersion("");
		newScopeUnit.setRefScopeUnitId("ROOT");

		newScopeUnit.setProjectId(projectId);

		List<ScopeUnitDTO> scopeUnitChildList = new ArrayList<ScopeUnitDTO>();
		newScopeUnit.setChildrenScopeUnit(scopeUnitChildList);

		newScopeUnit.setType(Constants.ROOT_REPOSITORY_KEY);

		return newScopeUnit;
	}

	/**
	 * @param currentLot
	 * @param parentLot
	 * @param itScope
	 * @return
	 */
	public static ScopeUnitDTO buildScopeUnitDTO(final Lot currentLot, final Lot parentLot,
			final ScopeUnit itScope, final String statusScopeUnit)
	{
		ScopeUnitDTO newScope = new ScopeUnitDTO();
		newScope.setName(itScope.getName());
		newScope.setDescription(itScope.getDescription());
		newScope.setUnitId(itScope.getUnitId());
		newScope.setDate(new Date(itScope.getDate().getTime()));
		newScope.setInScope(itScope.isInScope());
		newScope.setManual(itScope.isManual());
		newScope.setType(itScope.getType().getName());
		return newScope;
	}

	/**
	 * @param currentLot
	 * @param parentLot
	 * @param itScope
	 * @return
	 */
	public static ScopeUnitDTO buildScopeUnitDTO(final Lot currentLot, final Lot parentLot,
			final ScopeUnit itScope, final String statusScopeUnit, final ScopeUnitDTO scopeUnitParent,
			final List<Long> tasksId)
	{
		ScopeUnitDTO newScope = new ScopeUnitDTO();
		newScope.setName(itScope.getName());
		newScope.setDescription(itScope.getDescription());
		newScope.setVersion(itScope.getVersion());
		newScope.setUnitId(itScope.getUnitId());
		newScope.setDate(new Date(itScope.getDate().getTime()));
		newScope.setInScope(itScope.isInScope());
		newScope.setManual(itScope.isManual());
		newScope.setType(itScope.getType().getName());

		if (!itScope.isManual())
		{
			newScope.setRefScopeUnitVersion(itScope.getVersion());
			newScope.setRefScopeUnitId(itScope.getRefScopeUnit().getUnitId());
		}
		else
		{
			newScope.setRefScopeUnitVersion(ManagementModuleConstants.COMMON_EMPTY_TEXT);
			newScope.setRefScopeUnitId(ManagementModuleConstants.COMMON_EMPTY_TEXT);
		}
		if (!itScope.isManual())
		{
			newScope.setRefScopeUnitVersion(itScope.getRefScopeUnit().getVersion());
			newScope.setRefScopeUnitId(itScope.getRefScopeUnit().getUnitId());
		}
		else
		{
			newScope.setRefScopeUnitVersion(ManagementModuleConstants.COMMON_EMPTY_TEXT);
			newScope.setRefScopeUnitId(ManagementModuleConstants.COMMON_EMPTY_TEXT);
		}
		newScope.setLotName(currentLot.getName());
		newScope.setLotId(currentLot.getId().toString());

		if (parentLot != null)
		{
			newScope.setParentLotName(parentLot.getName());
			newScope.setParentLotId(parentLot.getId().toString());
		}
		else
		{
			newScope.setParentLotName(ManagementModuleConstants.COMMON_EMPTY_TEXT);
			newScope.setParentLotId(ManagementModuleConstants.COMMON_EMPTY_TEXT);
		}

		if (scopeUnitParent != null)
		{
			newScope.setParentScopeUnit(scopeUnitParent);
		}

		newScope.setListTaskId(tasksId);

		newScope.setStatus(statusScopeUnit);
		return newScope;
	}

	/**
	 * Return a status value depending on the given scopeUnit
	 *
	 * @param currentScope
	 * @return the status value
	 */
	private static String getStatusScopeUnit(final ScopeUnit currentScope)
	{
		String ret = ManagementModuleConstants.COMMON_EMPTY_TEXT;
		// status only for imported from obeo scopeUnit
		if (!currentScope.isManual())
		{

			if (currentScope.getRefScopeUnit() == null)
			{
				// status == obsolete
				return ManagementModuleConstants.SCOPE_STATUS_OBSOLETE;
			}
			else if (currentScope.getRefScopeUnit().getDateModified() != currentScope.getDate())
			{
				return ManagementModuleConstants.SCOPE_STATUS_MODIFIED;
			}
		}
		return ret;
	}

	/**
	 * This methods build a ApplicativeRightsDTO from an ApplicativeRights
	 *
	 * @param applicativeRights
	 *          the source
	 * @return the appropriate ApplicativeRightsDTO
	 */
	public static ApplicativeRightsDTO buildApplicativeRightsDTO(final ApplicativeRights applicativeRights)
	{
		final ApplicativeRightsDTO dto = new ApplicativeRightsDTO();
		final String applicativeFunctionName = applicativeRights.getApplicativeFunction().getName();
		final ApplicativeFunction clientApplicativeFunction = ServerToClientMapper
				.getApplicativeFunctionFromServerStringValue(applicativeFunctionName);
		dto.setApplicativeFunction(clientApplicativeFunction);
		final AccessRight accessRight = ServerToClientMapper.getAccessRightFromServerValue(applicativeRights
				.getAccesRight());
		dto.setAccessRights(accessRight);
		return dto;
	}

	/**
	 * This methods build a ChargePlanLineDTO from an ApplicativeRights
	 *
	 * @param idChargePlanLine
	 *          ????????????????
	 * @param discipline
	 *          the discipline concerned
	 * @param idProjectPlan
	 *          the projectPlan referred to
	 * @param totalLoad
	 *          sum of ScopeUnits' loads for a discipline
	 * @param verifiedLoad
	 *          sum of the load of all columns
	 * @param remainingLoad
	 *          the remaining load
	 * @param loadsByDate
	 *          the values order by date
	 * @return the appropriate ChargePlanLineDTO
	 */
	public static ChargePlanLineDTO buildChargePlanLineDTO(final Long idChargePlanLine,
			final String disciplineFunctionalId, final String disciplineName, final int disciplineOrder,
			final Long idProjectPlan, final float totalLoad, final float verifiedLoad, final float remainingLoad,
			final Map<Date, Float> loadsByDate)
	{
		final ChargePlanLineDTO dto = new ChargePlanLineDTO();

		dto.setIdChargePlanLine(idChargePlanLine);
		dto.setIdDiscipline(disciplineFunctionalId);
		dto.setDisciplineName(disciplineName);
		dto.setDisciplineOrder(disciplineOrder);
		dto.setIdProjectPlan(idProjectPlan);
		dto.setTotalLoad(totalLoad);
		dto.setVerifiedLoad(verifiedLoad);
		dto.setRemainingLoad(remainingLoad);

		if (loadsByDate != null)
		{
			dto.setLoadsByDate(loadsByDate);
		}
		else
		{
			dto.setLoadsByDate(new TreeMap<Date, Float>(new DateComparator()));
		}

		return dto;
	}

	/**
	 * Build a ChargePlanMainDataDTO and instanciate List and Map params are
	 * null
	 *
	 * @param listLines
	 *          the list of ChargePlanLineDTO
	 * @param tooltipsByDate
	 *          the ChargePlanToolTipDTO order by date
	 * @return the appropriate ChargePlanMainDataDTO
	 */
	public static ChargePlanMainDataDTO buildChargePlanMainDataDTO(final List<ChargePlanLineDTO> listLines,
			final Map<Date, ChargePlanToolTipDTO> tooltipsByDate)
	{
		final ChargePlanMainDataDTO dto = new ChargePlanMainDataDTO();

		if (listLines != null)
		{
			dto.setListLines(listLines);
		}
		else
		{
			dto.setListLines(new ArrayList<ChargePlanLineDTO>());
		}

		if (tooltipsByDate != null)
		{
			dto.setTooltipByDate(tooltipsByDate);
		}
		else
		{
			dto.setTooltipByDate(new TreeMap<Date, ChargePlanToolTipDTO>(new DateComparator()));
		}

		return dto;
	}

	/**
	 * Build a StatusProjectPlanDTO
	 * 
	 * @param StatusProjectPlan
	 * @return the appropriate StatusProjectPlanDTO
	 */
	public static StatusProjectPlanDTO buildStatusProjectPlanDTOFromStatusProjectPlan(
			final StatusProjectPlan statusProjectPlan)
	{
		StatusProjectPlanDTO dto = new StatusProjectPlanDTO();
		dto.setId(statusProjectPlan.getId());
		dto.setFunctionalId(statusProjectPlan.getFunctionalId());
		dto.setName(statusProjectPlan.getName());

		return dto;
	}

	/**
	 * Build a ScopeTypeDTO
	 * 
	 * @param ScopeType
	 * @return the appropriate ScopeTypeDTO
	 */
	public static ScopeTypeDTO buildScopeTypeDTOFromScopeType(final ScopeType scopeType)
	{
		ScopeTypeDTO dto = new ScopeTypeDTO();
		dto.setId(scopeType.getId());
		dto.setFunctionalId(scopeType.getFunctionalId());
		dto.setName(scopeType.getName());

		return dto;
	}

	/**
	 * Build a StatusScopeDTO
	 * 
	 * @param StatusScope
	 * @return the appropriate StatusScopeDTO
	 */
	public static StatusScopeDTO buildStatusScopeDTOFromStatusScope(final StatusScope statusScope)
	{
		StatusScopeDTO dto = new StatusScopeDTO();
		dto.setId(statusScope.getId());
		dto.setFunctionalId(statusScope.getFunctionalId());
		dto.setName(statusScope.getName());

		return dto;
	}

	/**
	 * Build a PhaseTypeDTO
	 *
	 * @param PhaseType
	 * @return the appropriate PhaseTypeDTO
	 */
	public static org.novaforge.forge.tools.managementmodule.ui.shared.PhaseTypeDTO buildPhaseTypeDTOCompletFromPhaseType(
			final PhaseType phaseType)
	{
		org.novaforge.forge.tools.managementmodule.ui.shared.PhaseTypeDTO dto = new org.novaforge.forge.tools.managementmodule.ui.shared.PhaseTypeDTO();
		dto.setFunctionalId(phaseType.getFunctionalId());
		dto.setName(phaseType.getName());

		return dto;
	}

	/**
	 * Build a AdjustFactorJointureDTO
	 *
	 * @param AdjustFactorJointure
	 * @return the appropriate AdjustFactorJointureDTO
	 */
	public static AdjustFactorJointureDTO buildAdjustFactorJointureFromAdjustWeight(
			final AdjustFactorJointure adjustFactorJointure)
	{
		AdjustFactorJointureDTO dto = new AdjustFactorJointureDTO();
		dto.setAdjustFactor(buildAdjustFactorDTOFromAdjustFactor(adjustFactorJointure.getAdjustFactor()));
		dto.setAdjustWeight(buildAdjustWeightDTOFromAdjustWeight(adjustFactorJointure.getAdjustWeight()));
		return dto;
	}

	/**
	 * Build a LanguageDTO
	 *
	 * @param language
	 * @return the appropriate LanguageDTO
	 */
	public static LanguageDTO buildLanguageDTOFromLanguage(final Language language)
	{
		LanguageDTO dto = new LanguageDTO();
		dto.setId(language.getId());
		dto.setFunctionalId(language.getFunctionalId());
		dto.setName(language.getName());
		return dto;
	}

	/**
	 * Build a ProjectDTO
	 *
	 * @param Project
	 *
	 * @return the appropriate ProjectDTO
	 */
	public static ProjectDTO buildProjectDTOFromProject(final Project project, final Integer lastVersionProjectPlan)
	{
		ProjectDTO dto = new ProjectDTO();
		dto.setDescription(project.getDescription());
		dto.setName(project.getName());
		dto.setProjectId(project.getProjectId());
		dto.setLastVersionProjectPlan(lastVersionProjectPlan);
		dto.setUnitTime(ServerToClientMapper.getUnitTimeFromServerStringValue(project.getUnitTime().getFunctionalId()));
		dto.setTransformation(buildTransformationDTOFromTransformation(project.getTransformation()));
		dto.setEstimationComponentSimple(buildEstimationComponentSimpleDTOFromEstimationComponentSimple(project
																																																				.getEstimationComponentSimple()));
		dto.setDisciplines(buildListProjectDisciplineDTO(project.getDisciplines()));
		dto.setTaskCategories(buildListTaskCategoryDTO(project.getTaskCategories()));
		dto.setUserList(new HashSet<UserDTO>());
		for (final Membership memberShip : project.getMemberships())
		{
			final UserDTO user = buildUserDTO(memberShip.getUser());
			dto.getUserList().add(user);
		}
		return dto;
	}

	/**
	 * Build a TransformationDTO
	 *
	 * @param Transformation
	 * @return the appropriate TransformationDTO
	 */
	public static TransformationDTO buildTransformationDTOFromTransformation(final Transformation transformation)
	{
		TransformationDTO dto = new TransformationDTO();
		dto.setNbHeuresJour(transformation.getNbHeuresJour());
		dto.setNbJoursAn(transformation.getNbJoursAn());
		dto.setNbJoursMois(transformation.getNbJoursMois());
		dto.setNbJoursNonTravail(transformation.getNbJoursNonTravail());
		dto.setNbJoursSemaine(transformation.getNbJoursSemaine());
		dto.setId(transformation.getId());
		return dto;
	}

	/**
	 * Build a EstimationComponentSimpleDTO
	 *
	 * @param EstimationComponentSimple
	 *
	 * @return the appropriate EstimationComponentSimpleDTO
	 */
	public static EstimationComponentSimpleDTO buildEstimationComponentSimpleDTOFromEstimationComponentSimple(final EstimationComponentSimple estimationComponentSimple)
	{
		EstimationComponentSimpleDTO dto = new EstimationComponentSimpleDTO();
		dto.setIdProjet(estimationComponentSimple.getIdProjet());
		dto.setValueENT(estimationComponentSimple.getValueENT());
		dto.setValueGDE(estimationComponentSimple.getValueGDE());
		dto.setValueGDI(estimationComponentSimple.getValueGDI());
		dto.setValueINT(estimationComponentSimple.getValueINT());
		dto.setValueSOR(estimationComponentSimple.getValueSOR());
		dto.setId(estimationComponentSimple.getId());
		return dto;
	}

	public static List<ProjectDisciplineDTO> buildListProjectDisciplineDTO(
			final Set<ProjectDiscipline> setdisciplineJointure)
	{
		final List<ProjectDisciplineDTO> listdto = new LinkedList<ProjectDisciplineDTO>();
		for (ProjectDiscipline disciplineJointure : setdisciplineJointure)
		{
			listdto.add(buildProjectDisciplineDTO(disciplineJointure));
		}
		return listdto;
	}

	public static List<TaskCategoryDTO> buildListTaskCategoryDTO(final Set<TaskCategory> settaskCategory)
	{
		final List<TaskCategoryDTO> listdto = new LinkedList<TaskCategoryDTO>();
		for (TaskCategory taskCategory : settaskCategory)
		{
			listdto.add(buildTaskCategoryTO(taskCategory));
		}
		return listdto;
	}

	/**
	 * Build a UserDTO from a user
	 *
	 * @param user
	 *          the user source
	 * @return the created UserDTO
	 */
	public static UserDTO buildUserDTO(final User user)
	{
		final UserDTO userDTO = new UserDTO();
		userDTO.setLogin(user.getLogin());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		return userDTO;
	}

	public static ProjectDisciplineDTO buildProjectDisciplineDTO(final ProjectDiscipline disciplineJointure)
	{
		final ProjectDisciplineDTO dto           = new ProjectDisciplineDTO();
		final DisciplineDTO        disciplineDTO = buildDisciplineDTO(disciplineJointure.getDiscipline());
		dto.setDisciplineDTO(disciplineDTO);
		dto.setProjectId(disciplineJointure.getProject().getProjectId());
		dto.setDisciplinePourcentage(disciplineJointure.getRepartition());
		dto.setId(disciplineJointure.getId());
		return dto;
	}

	public static TaskCategoryDTO buildTaskCategoryTO(final TaskCategory taskCategory)
	{
		final TaskCategoryDTO dto = new TaskCategoryDTO();
		dto.setName(taskCategory.getName());
		dto.setId(taskCategory.getId());
		return dto;
	}

	/**
	 * Build a new DisciplineDTO UI Object from a Discipline domain object
	 *
	 * @param phareDiscipline
	 *     the domain discipline
	 *
	 * @return the DTO built
	 */
	public static DisciplineDTO buildDisciplineDTO(final Discipline phareDiscipline)
	{
		final DisciplineDTO dto = new DisciplineDTO();
		dto.setFunctionalId(phareDiscipline.getFunctionalId());
		dto.setLibelle(phareDiscipline.getName());
		return dto;
	}

	/**
	 * Build a {@link ScopeUnitDisciplineDTO}
	 * 
	 * @param scopeUnitDiscipline
	 * @return the appropriate {@link ScopeUnitDisciplineDTO}
	 */
	public static ScopeUnitDisciplineDTO buildScopeUnitDisciplineDTOFromScopeUnitDiscipline(
			final ScopeUnitDiscipline scopeUnitDiscipline)
	{
		ScopeUnitDisciplineDTO dto = new ScopeUnitDisciplineDTO();
		dto.setLastUpdate(new Date(scopeUnitDiscipline.getStatusUpdatedDate().getTime()));
		dto.setDiscipline(buildDisciplineDTO(scopeUnitDiscipline.getDiscipline()));
		dto.setScopeUnit(buildScopeUnitDTOFromScopeUnit(scopeUnitDiscipline.getScopeUnit()));
		dto.setStatus(buildScopeUnitDisciplineStatusDTOFromScopeUnitDisciplineStatus(scopeUnitDiscipline
				.getStatus()));

		return dto;
	}

	/**
	 * Build a StatusScopeDTO
	 * 
	 * @param StatusScope
	 * @return the appropriate StatusScopeDTO
	 */
	public static ScopeUnitDisciplineStatusDTO buildScopeUnitDisciplineStatusDTOFromScopeUnitDisciplineStatus(
			final ScopeUnitDisciplineStatus scopeUnitDisciplineStatus)
	{
		ScopeUnitDisciplineStatusDTO dto = new ScopeUnitDisciplineStatusDTO();
		dto.setId(scopeUnitDisciplineStatus.getId());
		dto.setFunctionalId(scopeUnitDisciplineStatus.getFunctionalId());
		dto.setLabel(scopeUnitDisciplineStatus.getLabel());

		return dto;
	}

	/**
	 * Build a light scope unit containing some estimation informations
	 * 
	 * @param scopeUnit
	 * @return the appropriate ScopeUnitLightDTO
	 */
	public static ScopeUnitLightDTO buildScopeUnitLightDTOFromScopeUnit(final ScopeUnit scopeUnit)
	{
		ScopeUnitLightDTO ret = new ScopeUnitLightDTO();
		ret.setUnitId(scopeUnit.getUnitId());
		ret.setScopeUnitName(scopeUnit.getName());
		if (scopeUnit.getParentScopeUnit() != null)
		{
			ret.setParentScopeUnitName(scopeUnit.getParentScopeUnit().getName());
		}
		ret.setFinished(scopeUnit.isFinished());
		ret.setBenefit(scopeUnit.getEstimation().getBenefit());
		ret.setInjury(scopeUnit.getEstimation().getInjury());
		ret.setRisk(scopeUnit.getEstimation().getRisk());
		ret.setWeight(ret.getBenefit() + ret.getInjury() + ret.getRisk());
		return ret;
	}

	/**
	 * Build a scope unit containing some monitoring informations
	 *
	 * @param scopeUnitDisciplineSize
	 * @param remainingTask
	 * @param consumed
	 * @param taskFinished
	 * @param tasks
	 */
	public static ScopeUnitGlobalMonitoringDTO buildScopeUnitGlobalMonitoringDTOFromScopeUnitIndicators(
			final ScopeUnitIndicators scopeUnitIndicators)
	{
		ScopeUnitGlobalMonitoringDTO ret = new ScopeUnitGlobalMonitoringDTO();
		final ScopeUnit scopeUnit = scopeUnitIndicators.getScopeUnit();
		completeScopeUnitMonitoringDTOFromScopeUnit(ret, scopeUnitIndicators);
		ret.setLotName(scopeUnit.getLot().getName());
		if (scopeUnit.getLot().getParentLot() != null)
		{
			ret.setParentLotName(scopeUnit.getLot().getParentLot().getName());
		}
		else
		{
			ret.setParentLotName(ManagementModuleConstants.COMMON_EMPTY_TEXT);
		}
		if (scopeUnit.isFinished())
		{
			ret.setStatus(ScopeUnitMonitoringStatusEnum.FINISHED);
		}
		else if (ret.getConsumed() == 0F)
		{
			ret.setStatus(ScopeUnitMonitoringStatusEnum.NOT_STARTED);
		}
		else
		{
			ret.setStatus(ScopeUnitMonitoringStatusEnum.IN_PROGRESS);
		}
		ret.setProjectPlanEstimation(scopeUnit.getEstimation().getLastCharge());
		ret.setRemainingScopeUnit(scopeUnitIndicators.getRemainingScopeUnit());
		ret.setBenefit(scopeUnit.getEstimation().getBenefit());
		ret.setInjury(scopeUnit.getEstimation().getInjury());
		ret.setRisk(scopeUnit.getEstimation().getRisk());
		ret.setWeight(scopeUnit.getEstimation().getWeight());
		ret.setAllTaskFinished(scopeUnitIndicators.isAllTaskfinished());
		ret.setAllScopeUnitDisciplineFinished(scopeUnitIndicators.isAllScopeUnitDisciplineFinished());
		ret.setAllChildFinished(scopeUnitIndicators.isAllChildFinished());
		return ret;
	}

	/**
	 * This method completes a scopeUnitMonitoringDTO (first argument) with
	 * common datas (between Iteration and Global)
	 *
	 * @param scopeUnitMonitoringDTO
	 *     the ScopeUnitMonitoringDTO to complete
	 * @param scopeUnit
	 *     the scope Unit src
	 * @param remainingTime
	 *     the remaining time
	 * @param consumedTime
	 *     the consumed time
	 */
	public static void completeScopeUnitMonitoringDTOFromScopeUnit(final ScopeUnitMonitoringDTO scopeUnitMonitoringDTO,
																																 final ScopeUnitIndicators scopeUnitIndicators)
	{
		final ScopeUnit scopeUnit = scopeUnitIndicators.getScopeUnit();
		scopeUnitMonitoringDTO.setUnitId(scopeUnit.getUnitId());
		scopeUnitMonitoringDTO.setScopeUnitName(scopeUnit.getName());
		if (scopeUnit.getParentScopeUnit() != null)
		{
			scopeUnitMonitoringDTO.setParentScopeUnitId(scopeUnit.getParentScopeUnit().getUnitId());
			scopeUnitMonitoringDTO.setParentScopeUnitName(scopeUnit.getParentScopeUnit().getName());
		}
		else
		{
			scopeUnitMonitoringDTO.setParentScopeUnitName(ManagementModuleConstants.COMMON_EMPTY_TEXT);
		}
		scopeUnitMonitoringDTO.setConsumed(scopeUnitIndicators.getConsumedTime());
		scopeUnitMonitoringDTO.setRemainingTasks(scopeUnitIndicators.getRemainingTime());
		scopeUnitMonitoringDTO.setAdvancement(scopeUnitIndicators.getAdvancement());
		scopeUnitMonitoringDTO.setReestimate(scopeUnitIndicators.getReestimate());
	}

	public static ScopeUnitIterationMonitoringDTO buildScopeUnitIterationMonitoringDTOFromScopeUnit(
			final ScopeUnitIndicators scopeUnitIndicators)
	{
		final ScopeUnitIterationMonitoringDTO scopeUnitIterationMonitoringDTO = new ScopeUnitIterationMonitoringDTO();
		completeScopeUnitMonitoringDTOFromScopeUnit(scopeUnitIterationMonitoringDTO, scopeUnitIndicators);
		scopeUnitIterationMonitoringDTO.setEstimation(scopeUnitIndicators.getPeriodBeginningEstimation());
		return scopeUnitIterationMonitoringDTO;
	}

	/**
	 * Build a BugDTO from a bug
	 * 
	 * @param bug
	 *          the source
	 * @return the created bug DTO
	 */
	public static BugDTO buildBugDTOFromBug(final Bug bug)
	{
		final BugDTO bugDTO = new BugDTO();
		bugDTO.setBugId(bug.getId());
		bugDTO.setBugTrackerId(bug.getBugTrackerId());
		bugDTO.setAssignedTo(bug.getAssignedTo());
		bugDTO.setCategory(bug.getCategory());
		bugDTO.setPriority(bug.getPriority());
		bugDTO.setReporter(bug.getReporter());
		bugDTO.setSeverity(bug.getSeverity());
		bugDTO.setStatus(bug.getStatus());
		bugDTO.setTitle(bug.getTitle());
		return bugDTO;
	}

	/**
	 * Build a MonitoringIndicatorsDTO from a MonitoringIndicators
	 * 
	 * @param monitoringIndicator
	 *          the MonitoringIndicators src
	 * @return the MonitoringIndicatorsDTO created
	 */
	public static MonitoringIndicatorsDTO buildMonitoringIndicatorsDTOFromMonitoringIndicators(
			final MonitoringIndicators monitoringIndicator)
	{
		final MonitoringIndicatorsDTO monitoringIndicatorsDTO = new MonitoringIndicatorsDTO();
		monitoringIndicatorsDTO.setActiveUsersNumber(monitoringIndicator.getActiveUsersNumber());
		monitoringIndicatorsDTO.setAdvancement(monitoringIndicator.getAdvancement());
		monitoringIndicatorsDTO.setAverageEstimationError(monitoringIndicator.getAverageEstimationError());
		monitoringIndicatorsDTO.setConsumed(monitoringIndicator.getConsumed());
		monitoringIndicatorsDTO.setFocalisation(monitoringIndicator.getFocalisation());
		monitoringIndicatorsDTO.setVelocity(monitoringIndicator.getVelocity());
		return monitoringIndicatorsDTO;
	}

	/**
	 * Build a GlobalMonitoringIndicatorsDTO from a GlobalMonitoringIndicators
	 * 
	 * @param indicators
	 * @return the GlobalMonitoringIndicatorsDTO created
	 */
	public static GlobalMonitoringIndicatorsDTO buildGlobalMonitoringIndicatorsDTOFromGlobalMonitoringIndicators(
			final GlobalMonitoringIndicators indicators)
	{
		final GlobalMonitoringIndicatorsDTO dto = new GlobalMonitoringIndicatorsDTO();
		dto.setProjectStartDate(indicators.getProjectStartDate());
		dto.setEstimationError(indicators.getEstimationError());
		dto.setFocalisationFactor(indicators.getFocalisationFactor());
		dto.setLastCountFP(indicators.getLastCountFP());
		dto.setVelocity(indicators.getVelocity());
		return dto;
	}

	/**
	 * Build a CDOParametersDTO from a CDOParameters
	 * 
	 * @param cdoParameter
	 * @return the CDOParametersDTO created
	 */
	public static CDOParametersDTO buildCDOParametersDTO(final CDOParameters cdoParameter)
	{
		CDOParametersDTO parameterDTO = new CDOParametersDTO();

		parameterDTO.setcDOParametersID(cdoParameter.getId());
		parameterDTO.setHost(cdoParameter.getHost());
		parameterDTO.setPort(cdoParameter.getPort());
		parameterDTO.setRepository(cdoParameter.getRepository());
		parameterDTO.setProjetCdo(cdoParameter.getProjetCdo());
		parameterDTO.setSystemGraal(cdoParameter.getSystemGraal());
		parameterDTO.setCronExpression(cdoParameter.getCronExpression());

		return parameterDTO;
	}

}
