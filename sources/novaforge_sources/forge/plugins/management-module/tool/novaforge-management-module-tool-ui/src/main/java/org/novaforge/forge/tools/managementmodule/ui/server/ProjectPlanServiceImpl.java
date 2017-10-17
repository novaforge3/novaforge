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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.managementmodule.domain.AdjustFactorJointure;
import org.novaforge.forge.tools.managementmodule.domain.AdjustWeight;
import org.novaforge.forge.tools.managementmodule.domain.Estimation;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentDetail;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentSimple;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.LoadDistributionDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.Marker;
import org.novaforge.forge.tools.managementmodule.domain.MarkerType;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.Transformation;
import org.novaforge.forge.tools.managementmodule.ui.client.service.ProjectPlanService;
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustFactorJointureDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustWeightDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ComponentEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationComponentDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.MarkerDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ErrorEnumeration;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The server side implementation of the RPC service.
 */
public class ProjectPlanServiceImpl extends SimpleServiceImpl implements ProjectPlanService
{

	private static final Integer DEFAULT_FP_VALUE = 0;
	/**
    * 
    */
	private static final long		serialVersionUID = -8645913663558049472L;

	private static final Log		 LOG							= LogFactory.getLog(ProjectPlanServiceImpl.class);

	@Override
	public List<ProjectPlanDTO> getProjectPlanList(final String projectId) throws ManagementModuleException
	{

		List<ProjectPlanDTO> projectPlansList = new ArrayList<ProjectPlanDTO>();
		ProjectPlanDTO dto;
		try
		{
			List<ProjectPlan> list = getProjectPlanManager().getProjectPlanList(projectId);
			if (list != null)
			{
				for (ProjectPlan projectPlan : list)
				{
					dto = BuildResources.buildProjectPlanDTOFromProjectPlan(projectPlan);
					projectPlansList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get ProjectPlans", e);
		}

		Collections.sort(projectPlansList, new Comparator<ProjectPlanDTO>()
		{

			@Override
			public int compare(final ProjectPlanDTO o1, final ProjectPlanDTO o2)
			{
				if (o1.getDate().after(o2.getDate()))
				{
					return -1;
				}
				else
				{
					return 1;
				}
			}
		});

		return projectPlansList;
	}

	@Override
	public ProjectPlanDTO getProjectPlan(final String projectId, final Integer version)
			throws ManagementModuleException
	{

		ProjectPlanDTO dto = null;
		try
		{
			ProjectPlan projectPlan = getProjectPlanManager().getProjectPlan(projectId, version);
			dto = BuildResources.buildProjectPlanDTOFromProjectPlan(projectPlan);
		}
		catch (Exception e)
		{
			throw manageException("Unable to get ProjectPlan", e);
		}
		return dto;
	}

	@Override
	public ProjectPlanDTO getLastProjectPlan(final String pProjectId) throws ManagementModuleException
	{
		ProjectPlanDTO dto = null;
		try
		{
			ProjectPlan projectPlan = getProjectPlanManager().getLastProjectPlan(pProjectId);
			dto = BuildResources.buildProjectPlanDTOFromProjectPlan(projectPlan);
		}
		catch (Exception e)
		{
			throw manageException("Unable to get last ProjectPlan", e);
		}
		return dto;
	}

	@Override
	public ProjectPlanDTO creeteProjectPlan(final String projectId) throws ManagementModuleException
	{
		ProjectPlanDTO dto = null;
		try
		{
			ProjectPlan projectPlan = getProjectPlanManager().creeteProjectPlan(projectId);
			dto = BuildResources.buildProjectPlanDTOFromProjectPlan(projectPlan);
		}
		catch (Exception e)
		{
			throw manageException("Unable to create ProjectPlan", e);
		}
		return dto;
	}

	@Override
	public ProjectPlanDTO validateProjectPlan(final String projectId) throws ManagementModuleException
	{
		try
		{
			ProjectPlan projectPlan = getProjectPlanManager().validateProjectPlan(projectId);
			return BuildResources.buildProjectPlanDTOFromProjectPlan(projectPlan);
		}
		catch (Exception e)
		{
			throw manageException("Unable to validate ProjectPlan", e);
		}
	}

	@Override
	public List<LotDTO> getLotList(final Long projectPlanId) throws ManagementModuleException
	{
		List<LotDTO> lotList = new ArrayList<LotDTO>();
		LotDTO dto;
		try
		{
			List<Lot> list = getProjectPlanManager().getParentLotsList(projectPlanId);
			if (list != null)
			{
				for (Lot lot : list)
				{
					dto = BuildResources.buildLotWithChildsDTOFromLot(lot);
					lotList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get Lots for projectPlan with id=" + projectPlanId, e);
		}

		return lotList;
	}

	@Override
	public LotDTO creationLot(final String lotName, final Date startDate, final Date endDate,
			final String desc, final Long projectPlanId, final Long parentLotId) throws ManagementModuleException
	{
		LotDTO dto = null;
		try
		{
			ProjectPlan projectPlan = null;
			Lot parentLot = null;

			if (parentLotId != null)
			{
				parentLot = getProjectPlanManager().getLot(parentLotId);
			}
			if (projectPlanId != null)
			{
				projectPlan = getProjectPlanManager().getProjectPlan(projectPlanId);
			}

			Lot lot = getProjectPlanManager().creeteLot(projectPlan, lotName, startDate, endDate, desc, null, null,
					parentLot);

			dto = BuildResources.buildLotDTOFromLot(lot);

		}
		catch (Exception e)
		{
			throw manageException("Unable to create Lot", e);
		}
		return dto;
	}

	@Override
	public boolean saveLot(final Long lotId, final LotDTO dto) throws ManagementModuleException
	{
		Lot lot = null;
		try
		{
			lot = getProjectPlanManager().getLot(lotId);
			Date oldStartDate = null;
			Date oldEndDate = null;
			boolean deleteLDD = false;

			checkLotIterationsDate(lot, dto);
			if (lot.getStartDate().before(dto.getStartDate()))
			{
				oldStartDate = lot.getStartDate();
			}
			if (lot.getEndDate().after(dto.getEndDate()))
			{
				oldEndDate = lot.getEndDate();
			}
			if (!(lot.getParentLot() != null && dto.getParentLotId() != null))
			{
				deleteLDD = true;
			}

			lot.setDescription(dto.getDesc());
			lot.setEndDate(dto.getEndDate());
			lot.setName(dto.getName().trim());
			lot.setStartDate(dto.getStartDate());
			if (dto.getParentLotId() != null)
			{
				lot.setParentLot(getProjectPlanManager().getLot(dto.getParentLotId()));
			}
			else
			{
				lot.setParentLot(null);
			}
			getProjectPlanManager().updateLot(lot);

			if (deleteLDD && (oldStartDate != null || oldEndDate != null))
			{
				List<LoadDistributionDiscipline> ldds = getProjectPlanManager().getLoadDistributionDisciplineList(
						lot.getProjectPlan().getId());

				List<Long> listToDelete = new ArrayList<Long>();

				for (LoadDistributionDiscipline ldd : ldds)
				{
					if (oldStartDate != null && ldd.getDate().before(lot.getStartDate()))
					{
						listToDelete.add(ldd.getId());
					}
					if (oldEndDate != null && ldd.getDate().after(lot.getEndDate()))
					{
						listToDelete.add(ldd.getId());
					}
				}
				getProjectPlanManager().deleteLoadDistributionDisciplines(listToDelete);
			}

			return true;

		}
		catch (Exception e)
		{
			throw manageException("Unable to update Lot", e);
		}
	}
	
	/**
	 * check that new provided date for lot are compliants
	 * with its existing iterations date 
	 * @param lot existing lot in DB
	 * @param lotDto modified lot
	 */
	private void checkLotIterationsDate( final Lot lot,final LotDTO lotDto) throws ManagementModuleException
	{
	    if (lot.getIterations() != null && lot.getIterations().size() > 0 )
	    {
	      for ( Iteration it : lot.getIterations())
	      {
	        if ( lotDto.getStartDate().after(it.getStartDate()) || lotDto.getEndDate().before(it.getEndDate()))
	        {
	          throw new ManagementModuleException(ErrorEnumeration.ERR_LOT_DATE_NOT_COMPLIANT_WITH_ITERATION); 
	        }
	      }
	    }
	}

	@Override
	public LotDTO getLot(final Long lotId) throws ManagementModuleException
	{
		LotDTO dto = null;
		try
		{
			Lot lot = getProjectPlanManager().getLot(lotId);

			dto = BuildResources.buildLotDTOFromLot(lot);

		}
		catch (Exception e)
		{
			throw manageException("Unable to get Lot", e);
		}
		return dto;
	}

	@Override
	public boolean deleteLot(final Long lotId) throws ManagementModuleException
	{
		boolean result = false;
		try
		{
			result = getProjectPlanManager().deleteLot(lotId);
		}
		catch (Exception e)
		{
			throw manageException("Unable to delete Lot with Lotid = " + lotId.longValue(), e);
		}
		return result;
	}

	@Override
	public List<EstimationDTO> getEstimationList(final Long pProjectPlanId) throws ManagementModuleException
	{

		List<EstimationDTO> estimationDTOs = null;
		try
		{
			estimationDTOs = new ArrayList<EstimationDTO>();
			EstimationDTO estimationDTO;
			List<Estimation> estimations = getProjectPlanManager().getEstimations(pProjectPlanId);
			for (Estimation estimation : estimations)
			{
				estimationDTO = BuildResources.buildEstimationDTOFromEstimation(estimation);
				estimationDTOs.add(estimationDTO);
			}
			return estimationDTOs;
		}
		catch (Exception e)
		{
			throw manageException("Unable to get estimation list", e);
		}
	}

	@Override
	public void validateEstimation(final List<EstimationDTO> estimationList) throws ManagementModuleException
	{
		for (EstimationDTO estimationDTO : estimationList)
		{
			try
			{
				Estimation estimation;
				ScopeUnit scopeUnit = getProjectPlanManager().getScopeUnit(estimationDTO.getScopeUnit().getUnitId());

				if (scopeUnit.getEstimation() != null)
				{
					estimation = scopeUnit.getEstimation();
				}
				else
				{
					estimation = getProjectPlanManager().newEstimation();
				}
				estimation.setScopeUnit(scopeUnit);
				estimation.setLastDate(new Date());
				estimation.setBenefit(estimationDTO.getBenefit());
				estimation.setRisk(estimationDTO.getRisk());
				estimation.setInjury(estimationDTO.getInjury());
				estimation.setWeight(estimationDTO.getWeight());
				// if charge is set manually
				if (estimationDTO.isManual())
				{
					estimation.setGDIsimple(DEFAULT_FP_VALUE);
					estimation.setGDImedian(DEFAULT_FP_VALUE);
					estimation.setGDIcomplex(DEFAULT_FP_VALUE);
					estimation.setGDEsimple(DEFAULT_FP_VALUE);
					estimation.setGDEmedian(DEFAULT_FP_VALUE);
					estimation.setGDEcomplex(DEFAULT_FP_VALUE);
					estimation.setINsimple(DEFAULT_FP_VALUE);
					estimation.setINmedian(DEFAULT_FP_VALUE);
					estimation.setINcomplex(DEFAULT_FP_VALUE);
					estimation.setOUTsimple(DEFAULT_FP_VALUE);
					estimation.setOUTmedian(DEFAULT_FP_VALUE);
					estimation.setOUTcomplex(DEFAULT_FP_VALUE);
					estimation.setINTsimple(DEFAULT_FP_VALUE);
					estimation.setINTmedian(DEFAULT_FP_VALUE);
					estimation.setINTcomplex(DEFAULT_FP_VALUE);
					estimation.setGlobalSimple(DEFAULT_FP_VALUE);
					estimation.setGlobalMedian(DEFAULT_FP_VALUE);
					estimation.setGlobalComplex(DEFAULT_FP_VALUE);
					estimation.setManual(true);
					estimation.setSimple(null);
					estimation.setCharge(estimationDTO.getCharge());
				}
				// if charge is set thanks to function points, in simple way
				else if (estimationDTO.getSimple() != ComponentEnum.NONE)
				{
					estimation.setGDIsimple(DEFAULT_FP_VALUE);
					estimation.setGDImedian(DEFAULT_FP_VALUE);
					estimation.setGDIcomplex(DEFAULT_FP_VALUE);
					estimation.setGDEsimple(DEFAULT_FP_VALUE);
					estimation.setGDEmedian(DEFAULT_FP_VALUE);
					estimation.setGDEcomplex(DEFAULT_FP_VALUE);
					estimation.setINsimple(DEFAULT_FP_VALUE);
					estimation.setINmedian(DEFAULT_FP_VALUE);
					estimation.setINcomplex(DEFAULT_FP_VALUE);
					estimation.setOUTsimple(DEFAULT_FP_VALUE);
					estimation.setOUTmedian(DEFAULT_FP_VALUE);
					estimation.setOUTcomplex(DEFAULT_FP_VALUE);
					estimation.setINTsimple(DEFAULT_FP_VALUE);
					estimation.setINTmedian(DEFAULT_FP_VALUE);
					estimation.setINTcomplex(DEFAULT_FP_VALUE);
					estimation.setGlobalSimple(estimationDTO.getGlobalSimple());
					estimation.setGlobalMedian(estimationDTO.getGlobalMedian());
					estimation.setGlobalComplex(estimationDTO.getGlobalComplex());
					estimation.setManual(false);
					estimation.setSimple(estimationDTO.getSimple().toString());
					estimation.setCharge(estimationDTO.getCharge());
				}
				// if charge is set thanks to function points, in detail way
				else
				{
					estimation.setGDIsimple(estimationDTO.getGDIsimple());
					estimation.setGDImedian(estimationDTO.getGDImedian());
					estimation.setGDIcomplex(estimationDTO.getGDIcomplex());
					estimation.setGDEsimple(estimationDTO.getGDEsimple());
					estimation.setGDEmedian(estimationDTO.getGDEmedian());
					estimation.setGDEcomplex(estimationDTO.getGDEcomplex());
					estimation.setINsimple(estimationDTO.getINsimple());
					estimation.setINmedian(estimationDTO.getINmedian());
					estimation.setINcomplex(estimationDTO.getINcomplex());
					estimation.setOUTsimple(estimationDTO.getOUTsimple());
					estimation.setOUTmedian(estimationDTO.getOUTmedian());
					estimation.setOUTcomplex(estimationDTO.getOUTcomplex());
					estimation.setINTsimple(estimationDTO.getInterrogationSimple());
					estimation.setINTmedian(estimationDTO.getInterrogationMedian());
					estimation.setINTcomplex(estimationDTO.getInterrogationComplex());
					estimation.setGlobalSimple(DEFAULT_FP_VALUE);
					estimation.setGlobalMedian(DEFAULT_FP_VALUE);
					estimation.setGlobalComplex(DEFAULT_FP_VALUE);
					estimation.setManual(false);
					estimation.setSimple(ComponentEnum.NONE.toString());
					estimation.setCharge(estimationDTO.getCharge());
				}

				scopeUnit.setEstimation(estimation);
				getProjectPlanManager().updateScopeUnit(scopeUnit);
			}
			catch (Exception e)
			{
				throw manageException(MessageFormat.format("Unable to save estimation with scopeUnitName=%s",
						estimationDTO.getScopeUnit().getName()), e);
			}
		}

	}

	@Override
	public MarkerDTO creeteMarker(final Long projectPlanId, final String name, final Date date,
			final String desc, final String markerTypeFunctionalId) throws ManagementModuleException
	{
		LOG.warn("dans le createMarker");
		MarkerDTO dto = null;
		try
		{
			ProjectPlan projectPlan = getProjectPlanManager().getProjectPlan(projectPlanId);
			MarkerType markerType = getReferentielManager().getMarkerTypeByFuncionalId(markerTypeFunctionalId);

			Marker marker = getProjectPlanManager().creeteMarker(projectPlan, name, date, desc, markerType);

			dto = BuildResources.buildMarkerDTOFromMarker(marker);

		}
		catch (Exception e)
		{
			throw manageException("Unable to create Marker", e);
		}
		return dto;
	}

	@Override
	public MarkerDTO saveMarker(final Long id, final MarkerDTO dto) throws ManagementModuleException
	{
		MarkerDTO result = null;
		Marker marker = null;
		try
		{
			marker = getProjectPlanManager().getMarker(id);

			marker.setDescription(dto.getDesc());
			marker.setDate(new Date(dto.getDate().getTime()));
			marker.setName(dto.getName().trim());
			marker.setType(getReferentielManager().getMarkerTypeByFuncionalId(dto.getMarkerTypeFunctionalId()));

			marker = getProjectPlanManager().updateMarker(marker);
			result = BuildResources.buildMarkerDTOFromMarker(marker);

		}
		catch (Exception e)
		{
			throw manageException("Unable to update Marker", e);
		}
		return result;
	}

	@Override
	public MarkerDTO getMarker(final Long id) throws ManagementModuleException
	{
		MarkerDTO dto = null;
		try
		{
			Marker marker = getProjectPlanManager().getMarker(id);

			dto = BuildResources.buildMarkerDTOFromMarker(marker);

		}
		catch (Exception e)
		{
			throw manageException("Unable to get Marker", e);
		}
		return dto;
	}

	@Override
	public boolean deleteMarker(final Long id) throws ManagementModuleException
	{
		boolean result = false;

		try
		{
			result = getProjectPlanManager().deleteMarker(id);
		}
		catch (Exception e)
		{
			throw manageException("Unable to delete Marker with MarkerId = " + id.longValue(), e);
		}

		return result;
	}

	@Override
	public List<MarkerDTO> getMarkerList(final Long projectPlanId) throws ManagementModuleException
	{
		List<MarkerDTO> markerList = new ArrayList<MarkerDTO>();
		MarkerDTO       dto;
		try
		{
			List<Marker> markers = getProjectPlanManager().getMarkerList(projectPlanId);
			if (markers != null)
			{
				for (Marker marker : markers)
				{
					dto = BuildResources.buildMarkerDTOFromMarker(marker);
					markerList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get Markers for projectPlan with id=" + projectPlanId, e);
		}
		return markerList;
	}

	@Override
	public EstimationComponentDTO getEstimationComponent(final String pProjectId) throws ManagementModuleException
	{
		try
		{
			final EstimationComponentSimple simple = getReferentielManager().getEstimationComponentSimple(pProjectId);
			final EstimationComponentDetail details = getProjectPlanManager().getLastProjectPlan(pProjectId)
																																			 .getEstimationComponentDetail();
			final Transformation transformation = getReferentielManager().getTransformation(pProjectId);
			return BuildResources.buildEstimationComponentDetailDTOFromEstimationComponents(details, simple, transformation);
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to get abacus charge", e);
		}
	}

	@Override
	public ProjectPlanDTO getLastValidatedProjectPlan(final String pProjectId) throws ManagementModuleException
	{
		ProjectPlanDTO dto = null;
		try
		{
			ProjectPlan projectPlan = getProjectPlanManager().getLastValidatedProjectPlan(pProjectId);
			if (projectPlan != null)
			{
				dto = BuildResources.buildProjectPlanDTOFromProjectPlan(projectPlan);
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get last validated ProjectPlan", e);
		}
		return dto;
	}

	@Override
	public ProjectPlanDTO getLastProjectPlanFull(final String projectId) throws ManagementModuleException
	{

		ProjectPlanDTO dto = null;
		try
		{
			ProjectPlan projectPlan = getProjectPlanManager().getLastFullProjectPlan(projectId);
			dto = BuildResources.buildProjectPlanDTOFullFromProjectPlan(projectPlan);
		}
		catch (Exception e)
		{
			throw manageException("Unable to get ProjectPlan", e);
		}
		return dto;
	}

	@Override
	public List<AdjustWeightDTO> getAllAdjustWeightDTOList() throws ManagementModuleException
	{
		LOG.info("in getAdjustWeightDTOList");
		List<AdjustWeightDTO> retourList = new ArrayList<AdjustWeightDTO>();
		AdjustWeightDTO dto;
		try
		{
			List<AdjustWeight> adjustWeights = getReferentielManager().getAllAdjustWeight();
			if (adjustWeights != null)
			{
				for (AdjustWeight adjustWeight : adjustWeights)
				{
					dto = BuildResources.buildAdjustWeightDTOFromAdjustWeight(adjustWeight);
					retourList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get AdjustWeights", e);
		}

		return retourList;
	}

	@Override
	public ProjectPlanDTO saveProjectPlanWithSettings(final ProjectPlanDTO projectPlanDTO)
			throws ManagementModuleException
	{
		try
		{
			// abacus charge men by day
			ProjectPlan projectPlan = getProjectPlanManager().getLastFullProjectPlan(projectPlanDTO.getProjectId());
			projectPlan.getEstimationComponentDetail().setValueAbaChgHomJour(
					projectPlanDTO.getEstimationComponentDetail().getValueAbaChgHomJour());
			// complex function points
			projectPlan.getEstimationComponentDetail().setValueComplexGDE(
					projectPlanDTO.getEstimationComponentDetail().getValueComplexGDE());
			projectPlan.getEstimationComponentDetail().setValueComplexGDI(
					projectPlanDTO.getEstimationComponentDetail().getValueComplexGDI());
			projectPlan.getEstimationComponentDetail().setValueComplexIN(
					projectPlanDTO.getEstimationComponentDetail().getValueComplexIN());
			projectPlan.getEstimationComponentDetail().setValueComplexINT(
					projectPlanDTO.getEstimationComponentDetail().getValueComplexINT());
			projectPlan.getEstimationComponentDetail().setValueComplexOUT(
					projectPlanDTO.getEstimationComponentDetail().getValueComplexOUT());
			// median function points
			projectPlan.getEstimationComponentDetail().setValueMoyenGDE(
					projectPlanDTO.getEstimationComponentDetail().getValueMoyenGDE());
			projectPlan.getEstimationComponentDetail().setValueMoyenGDI(
					projectPlanDTO.getEstimationComponentDetail().getValueMoyenGDI());
			projectPlan.getEstimationComponentDetail().setValueMoyenIN(
					projectPlanDTO.getEstimationComponentDetail().getValueMoyenIN());
			projectPlan.getEstimationComponentDetail().setValueMoyenINT(
					projectPlanDTO.getEstimationComponentDetail().getValueMoyenINT());
			projectPlan.getEstimationComponentDetail().setValueMoyenOUT(
					projectPlanDTO.getEstimationComponentDetail().getValueMoyenOUT());
			// simple function points
			projectPlan.getEstimationComponentDetail().setValueSimpleGDE(
					projectPlanDTO.getEstimationComponentDetail().getValueSimpleGDE());
			projectPlan.getEstimationComponentDetail().setValueSimpleGDI(
					projectPlanDTO.getEstimationComponentDetail().getValueSimpleGDI());
			projectPlan.getEstimationComponentDetail().setValueSimpleIN(
					projectPlanDTO.getEstimationComponentDetail().getValueSimpleIN());
			projectPlan.getEstimationComponentDetail().setValueSimpleINT(
					projectPlanDTO.getEstimationComponentDetail().getValueSimpleINT());
			projectPlan.getEstimationComponentDetail().setValueSimpleOUT(
					projectPlanDTO.getEstimationComponentDetail().getValueSimpleOUT());

			// we get the adjust weight
			List<AdjustWeight> adjustWeights = getReferentielManager().getAllAdjustWeight();
			final Map<String, AdjustWeight> mapAdjustWeightByKey = new HashMap<String, AdjustWeight>();
			for (final AdjustWeight adjustWeight : adjustWeights)
			{
				mapAdjustWeightByKey.put(adjustWeight.getFunctionalId(), adjustWeight);
			}
			// we put the junction object in the map for instant access by key
			final Map<Long, AdjustFactorJointure> mapAdjustJunctionBykey = new HashMap<Long, AdjustFactorJointure>();
			for (AdjustFactorJointure adjustFactorJointure : projectPlan.getAdjustFactorJointure())
			{
				mapAdjustJunctionBykey.put(adjustFactorJointure.getId(), adjustFactorJointure);
			}
			Integer sumAdjustementFactor = 0;
			// for each junction of dto we update the weight with its value
			for (final AdjustFactorJointureDTO adjustFactorJointureDTO : projectPlanDTO
					.getAdjustFactorJointureList())
			{
				final AdjustFactorJointure correspondingAdjustFactorJointure = mapAdjustJunctionBykey
						.get(adjustFactorJointureDTO.getId());
				final AdjustWeight adjustWeight = mapAdjustWeightByKey.get(adjustFactorJointureDTO.getAdjustWeight()
						.getFunctionalId());
				correspondingAdjustFactorJointure.setAdjustWeight(adjustWeight);
				// sum all weight for the adjustement coef
				sumAdjustementFactor += Integer.parseInt(adjustWeight.getWeight());
			}
			// determinate the adjustement coef
			projectPlan.getEstimationComponentDetail().setAdjustementCoef(
					getProjectPlanManager().calculateAdjustementCoef(sumAdjustementFactor));
			ProjectPlan updatedPP = getProjectPlanManager().updateProjectPlan(projectPlan);
			return BuildResources.buildProjectPlanDTOFullFromProjectPlan(updatedPP);
		}
		catch (Exception e)
		{
			throw manageException("Unable to validate ProjectPlan", e);
		}
	}

	@Override
	public ProjectPlanDTO getProjectPlan(final Long projectPlanId) throws ManagementModuleException
	{
		ProjectPlanDTO dto = null;
		try
		{
			ProjectPlan projectPlan = getProjectPlanManager().getProjectPlan(projectPlanId);
			if (projectPlan != null)
			{
				dto = BuildResources.buildProjectPlanDTOFromProjectPlan(projectPlan);
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get last validated ProjectPlan", e);
		}
		return dto;
	}

	@Override
	public boolean updateRemainingScopeUnit(final Map<String, Float> remainingsScopeUnit)
			throws ManagementModuleException
	{

		try
		{
			boolean ret = true;
			for (Map.Entry<String, Float> remainingScopeUnit : remainingsScopeUnit.entrySet())
			{
				Estimation estimation = getProjectPlanManager().getEstimationByScopeUnit(remainingScopeUnit.getKey());
				estimation.setRemaining(remainingScopeUnit.getValue());
				if (!getProjectPlanManager().updateEstimation(estimation))
				{
					ret = false;
					break;
				}
			}
			return ret;
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to update remaining time of scopeUnit list", e);
		}
	}

}
