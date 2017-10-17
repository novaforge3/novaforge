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

import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.PhaseType;
import org.novaforge.forge.tools.managementmodule.ui.client.service.IterationService;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.PhaseTypeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * The server side implementation of the RPC service.
 */
public class IterationServiceImpl extends SimpleServiceImpl implements IterationService
{

	/**
	 * UID
	 */
	private static final long serialVersionUID = 8672598652831185997L;

	@Override
	public List<IterationDTO> getIterationList(final Long projectPlanId) throws ManagementModuleException
	{
		List<IterationDTO> iterationsList = new ArrayList<IterationDTO>();
		try
		{
			List<Iteration> list = getIterationManager().getIterationsList(projectPlanId);
			if (list != null)
			{
				for (Iteration iteration : list)
				{
					final IterationDTO dto = BuildResources.buildSimpleIterationDTOFromIteration(iteration);
					iterationsList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get iterationsList", e);
		}

		return iterationsList;
	}

	@Override
	public List<IterationDTO> getFinishedAndCurrentIterationList(final Long projectPlanId)
			throws ManagementModuleException
	{
		List<IterationDTO> iterationsList = new ArrayList<IterationDTO>();
		try
		{
			List<Iteration> list = getIterationManager().getFinishedAndCurrentIterationList(projectPlanId);
			if (list != null)
			{
				for (Iteration iteration : list)
				{
					final IterationDTO dto = BuildResources.buildSimpleIterationDTOFromIteration(iteration);
					iterationsList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get finished and current iteration list", e);
		}

		return iterationsList;
	}

	@Override
	public IterationDTO getCurrentOrLastFinishedIteration(final Long pProjectPlanId, final boolean pSimple)
			throws ManagementModuleException
	{
		IterationDTO dto = null;
		try
		{
			Iteration it = getIterationManager().getCurrentIteration(pProjectPlanId);
			if (it == null)
			{
				it = getIterationManager().getLastFinishedIteration(pProjectPlanId);
			}
			if (it != null)
			{
				if (pSimple)
				{
					dto = BuildResources.buildSimpleIterationDTOFromIteration(it);
				}
				else
				{
					dto = getComplexIterationDTOFromIteration(it);
				}
			}
			return dto;
		}
		catch (Exception e)
		{
			throw manageException("Unable to get the current iteration", e);
		}
	}

	@Override
	public IterationDTO getNextIteration(final Long pProjectPlanId, final boolean pSimple)
			throws ManagementModuleException
	{
		IterationDTO dto = null;
		try
		{
			Iteration it = getIterationManager().getNextIteration(pProjectPlanId);
			if (it != null)
			{
				if (pSimple)
				{
					dto = BuildResources.buildSimpleIterationDTOFromIteration(it);
				}
				else
				{
					dto = getComplexIterationDTOFromIteration(it);
				}
			}
			return dto;
		}
		catch (Exception e)
		{
			throw manageException("Unable to get next iteration", e);
		}
	}

	@Override
	public List<PhaseTypeDTO> getPhasesTypesForNextIteration(final IterationDTO baseIterationDTO)
			throws ManagementModuleException
	{
		List<PhaseTypeDTO> phaseTypesList = new ArrayList<PhaseTypeDTO>();
		try
		{
			Iteration baseIteration;
			if (baseIterationDTO != null)
			{
				baseIteration = getIterationManager().getIteration(baseIterationDTO.getIterationId());
			}
			else
			{
				baseIteration = null;
			}
			Set<PhaseType> list = getIterationManager().getPhasesTypesForNextIteration(baseIteration);
			if (list != null)
			{
				for (PhaseType phaseType : list)
				{
					PhaseTypeDTO dto = BuildResources.buildPhaseTypeDTOFromPhaseType(phaseType);
					phaseTypesList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get phaseTypesList", e);
		}
		return phaseTypesList;
	}

	@Override
	public boolean createIteration(final IterationDTO iterationDTOSrc) throws ManagementModuleException
	{
		try
		{
			final PhaseTypeDTO phaseTypeDTO = iterationDTOSrc.getPhaseType();

			final PhaseType phaseType = getReferentielManager().getPhaseType(phaseTypeDTO.getFunctionalId());
			final Lot lot = getProjectPlanManager().getLot(iterationDTOSrc.getLot().getLotId());

			final Iteration iteration = getIterationManager().newIteration();
			iteration.setNumIteration(iterationDTOSrc.getNumIteration());
			iteration.setLabel(iterationDTOSrc.getLabel());
			iteration.setStartDate(new Date(iterationDTOSrc.getStartDate().getTime()));
			iteration.setEndDate(new Date(iterationDTOSrc.getEndDate().getTime()));
			iteration.setLot(lot);
			iteration.setPhaseType(phaseType);
			iteration.setFinished(false);

			getIterationManager().creeteIteration(iteration);
			return true;
		}
		catch (Exception e)
		{
			throw manageException("Unable to create Iteration", e);
		}
	}

	@Override
	public boolean deleteIteration(final long projectPlanId, final long iterationId) throws ManagementModuleException
	{

		try
		{
			return getIterationManager().deleteIteration(iterationId);
		}
		catch (Exception e)
		{
			throw manageException("Unable to delete Iteration", e);
		}
	}

	@Override
	public IterationDTO updateIteration(final long projectPlanId, final IterationDTO iterationDTOSrc)
			throws ManagementModuleException
	{
		try
		{
			final PhaseTypeDTO phaseTypeDTO = iterationDTOSrc.getPhaseType();
			final PhaseType phaseType = getReferentielManager().getPhaseType(phaseTypeDTO.getFunctionalId());
			final Lot lot = getProjectPlanManager().getLot(iterationDTOSrc.getLot().getLotId());

			final Iteration iteration = getIterationManager().getIteration(iterationDTOSrc.getIterationId());
			iteration.setNumIteration(iterationDTOSrc.getNumIteration());
			iteration.setLabel(iterationDTOSrc.getLabel());
			iteration.setStartDate(new Date(iterationDTOSrc.getStartDate().getTime()));
			iteration.setEndDate(new Date(iterationDTOSrc.getEndDate().getTime()));
			iteration.setFinished(iterationDTOSrc.isFinished());
			iteration.setPhaseType(phaseType);
			iteration.setLot(lot);
			final Iteration updatedIteration = getIterationManager().updateIteration(iteration);
			return BuildResources.buildSimpleIterationDTOFromIteration(updatedIteration);
		}
		catch (Exception e)
		{
			throw manageException("Unable to update Iteration", e);
		}
	}

	@Override
	public List<LotDTO> getLotsList(final long projectPlanId) throws ManagementModuleException
	{
		List<LotDTO> lotDTOList = new ArrayList<LotDTO>();
		LotDTO dto;
		try
		{
			Set<Lot> list = getIterationManager().getsLotForNextIteration(projectPlanId);
			if (list != null)
			{
				for (Lot lot : list)
				{
					dto = BuildResources.buildLotDTOFromLot(lot);
					lotDTOList.add(dto);
				}
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get lotsList", e);
		}
		return lotDTOList;
	}

	@Override
	public IterationDTO getIterationById(final long pIterationId, final boolean pSimple)
			throws ManagementModuleException
	{
		IterationDTO dto = null;
		try
		{
			if (pSimple)
			{
				Iteration it = getIterationManager().getIteration(pIterationId);
				if (it != null)
				{
					dto = BuildResources.buildSimpleIterationDTOFromIteration(it);
				}
			}
			else
			{
				Iteration it = getTaskManager().getIterationWithFullTaskList(pIterationId);
				if (it != null)
				{
					dto = getComplexIterationDTOFromIteration(it);
				}
			}
			return dto;
		}
		catch (Exception e)
		{
			throw manageException("Unable to get Iteration", e);
		}
	}
}
