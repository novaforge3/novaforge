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
import org.novaforge.forge.tools.managementmodule.domain.AdjustFactor;
import org.novaforge.forge.tools.managementmodule.domain.AdjustWeight;
import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentSimple;
import org.novaforge.forge.tools.managementmodule.domain.Language;
import org.novaforge.forge.tools.managementmodule.domain.MarkerType;
import org.novaforge.forge.tools.managementmodule.domain.PhaseType;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.ProjectDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ScopeType;
import org.novaforge.forge.tools.managementmodule.domain.StatusProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.StatusScope;
import org.novaforge.forge.tools.managementmodule.ui.client.service.ReferentialService;
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustFactorDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustWeightDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationComponentSimpleDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LanguageDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.MarkerTypeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.PhaseTypeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeTypeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.StatusProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.StatusScopeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The server side implementation of the RPC service.
 */
public class ReferentialServiceImpl extends SimpleServiceImpl implements ReferentialService
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4626527014655648940L;

	private static final Log	LOG							= LogFactory.getLog(ReferentialServiceImpl.class);

	@Override
	public List<MarkerTypeDTO> getMarkerTypeDTOList() throws ManagementModuleException
	{
		LOG.info("in getMarkerTypeDTOList");
		List<MarkerTypeDTO> markerList = new ArrayList<MarkerTypeDTO>();
		MarkerTypeDTO dto;
		try
		{
			List<MarkerType> markerTypes = getReferentielManager().getMarkerTypeList();
			if (markerTypes != null)
			{
				for (MarkerType markerType : markerTypes)
				{
					dto = BuildResources.buildMarkerTypeDTOFromMarkerType(markerType);
					markerList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get MarkerTypes", e);
		}

		return markerList;
	}

	@Override
	public List<LanguageDTO> getLanguageDTOList() throws ManagementModuleException
	{
		LOG.info("in getLanguageDTOList");
		List<LanguageDTO> retourList = new ArrayList<LanguageDTO>();
		LanguageDTO dto;
		try
		{
			List<Language> languages = getReferentielManager().getAllLanguage();
			if (languages != null)
			{
				for (Language language : languages)
				{
					dto = BuildResources.buildLanguageDTOFromLanguage(language);
					retourList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get Languages", e);
		}

		return retourList;
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
	public List<PhaseTypeDTO> getAllPhaseTypesDTOList() throws ManagementModuleException
	{
		LOG.info("in getPhaseTypeDTOList");
		List<PhaseTypeDTO> retourList = new ArrayList<PhaseTypeDTO>();
		PhaseTypeDTO dto;
		try
		{
			List<PhaseType> phaseTypes = getReferentielManager().getAllPhaseTypes();
			if (phaseTypes != null)
			{
				for (PhaseType phaseType : phaseTypes)
				{
					dto = BuildResources.buildPhaseTypeDTOCompletFromPhaseType(phaseType);
					retourList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get PhaseTypes", e);
		}

		return retourList;
	}

	@Override
	public List<AdjustFactorDTO> getAllAdjustFactorDTOList() throws ManagementModuleException
	{
		LOG.info("in getAdjustFactorDTOList");
		List<AdjustFactorDTO> retourList = new ArrayList<AdjustFactorDTO>();
		AdjustFactorDTO dto;
		try
		{
			List<AdjustFactor> adjustFactors = getReferentielManager().getAllAdjustFactor();
			if (adjustFactors != null)
			{
				for (AdjustFactor adjustFactor : adjustFactors)
				{
					dto = BuildResources.buildAdjustFactorDTOFromAdjustFactor(adjustFactor);
					retourList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get AdjustFactors", e);
		}

		return retourList;
	}

	@Override
	public List<StatusScopeDTO> getAllStatusScopeDTOList() throws ManagementModuleException
	{
		LOG.info("in getStatusScopeDTOList");
		List<StatusScopeDTO> retourList = new ArrayList<StatusScopeDTO>();
		StatusScopeDTO dto;
		try
		{
			List<StatusScope> statusScopes = getReferentielManager().getAllStatusScope();
			if (statusScopes != null)
			{
				for (StatusScope statusScope : statusScopes)
				{
					dto = BuildResources.buildStatusScopeDTOFromStatusScope(statusScope);
					retourList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get StatusScopes", e);
		}

		return retourList;
	}

	@Override
	public List<ScopeTypeDTO> getAllScopeTypeDTOList() throws ManagementModuleException
	{
		LOG.info("in getScopeTypeDTOList");
		List<ScopeTypeDTO> retourList = new ArrayList<ScopeTypeDTO>();
		ScopeTypeDTO dto;
		try
		{
			List<ScopeType> scopeTypes = getReferentielManager().getAllScopeType();
			if (scopeTypes != null)
			{
				for (ScopeType scopeType : scopeTypes)
				{
					dto = BuildResources.buildScopeTypeDTOFromScopeType(scopeType);
					retourList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get ScopeTypes", e);
		}

		return retourList;
	}

	@Override
	public List<StatusProjectPlanDTO> getAllStatusProjectPlanDTOList() throws ManagementModuleException
	{
		LOG.info("in getStatusProjectPlanDTOList");
		List<StatusProjectPlanDTO> retourList = new ArrayList<StatusProjectPlanDTO>();
		StatusProjectPlanDTO dto;
		try
		{
			List<StatusProjectPlan> statusProjectPlans = getReferentielManager().getAllStatusProjectPlan();
			if (statusProjectPlans != null)
			{
				for (StatusProjectPlan statusProjectPlan : statusProjectPlans)
				{
					dto = BuildResources.buildStatusProjectPlanDTOFromStatusProjectPlan(statusProjectPlan);
					retourList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get StatusProjectPlans", e);
		}

		return retourList;
	}

	@Override
	public List<DisciplineDTO> getAllPhareDTOList() throws ManagementModuleException
	{
		LOG.info("in getPhareDTOList");
		List<DisciplineDTO> retourList = new ArrayList<DisciplineDTO>();
		DisciplineDTO dto;
		try
		{
			List<Discipline> disciplines = getReferentielManager().getAllDiscipline();
			if (disciplines != null)
			{
				for (Discipline discipline : disciplines)
				{
					dto = BuildResources.buildDisciplineDTO(discipline);
					retourList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get Phares", e);
		}
		return retourList;
	}

	@Override
	public EstimationComponentSimpleDTO getSteeringSimpleFunctionDTO(final String idProject)
			throws ManagementModuleException
	{
		LOG.info("in getSteeringSimpleFunctionDTO");
		EstimationComponentSimpleDTO dto = null;
		try
		{
			EstimationComponentSimple EstimationComponentSimple = getReferentielManager()
																																.getEstimationComponentSimple(idProject);
			if (EstimationComponentSimple != null)
			{
				dto = BuildResources.buildEstimationComponentSimpleDTOFromEstimationComponentSimple(EstimationComponentSimple);
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get SteeringFunctions", e);
		}
		return dto;
	}

	@Override
	public Set<ProjectDisciplineDTO> getProjectDisciplines(final String projectId)
			throws ManagementModuleException
	{
		try
		{
			Set<ProjectDisciplineDTO> ret = new HashSet<ProjectDisciplineDTO>();
			Project project = getManagementModuleManager().getFullProject(projectId);
			for (ProjectDiscipline projectDiscipline : project.getDisciplines())
			{
				ret.add(BuildResources.buildProjectDisciplineDTO(projectDiscipline));
			}
			return ret;
		}
		catch (Exception e)
		{
			throw manageException("Unable to get defaults' disciplines for the projectId=" + projectId, e);
		}
	}
}
