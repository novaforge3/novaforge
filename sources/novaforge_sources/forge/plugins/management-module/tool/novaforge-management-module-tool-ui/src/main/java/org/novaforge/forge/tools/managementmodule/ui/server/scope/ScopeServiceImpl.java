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

package org.novaforge.forge.tools.managementmodule.ui.server.scope;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.domain.CDOParameters;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.RefScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.ScopeType;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.transfer.ScopeUnitIndicators;
import org.novaforge.forge.tools.managementmodule.ui.client.service.scope.ScopeService;
import org.novaforge.forge.tools.managementmodule.ui.server.BuildResources;
import org.novaforge.forge.tools.managementmodule.ui.server.SimpleServiceImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineStatusEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitGlobalMonitoringDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitIterationMonitoringDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitLightDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitMonitoringStatusEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ErrorEnumeration;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The server side implementation of the RPC service.
 */
public class ScopeServiceImpl extends SimpleServiceImpl implements ScopeService
{
	/**
    * 
    */
	private static final long	 serialVersionUID	 = 3302005156004946672L;
	private static final String CSV_LINE_SEPARATOR = System.getProperty("line.separator");

	private static final Log		LOG								= LogFactory.getLog(ScopeServiceImpl.class);

	private String getStatusScopeUnit(final ScopeUnit currentScope)
	{
		String ret = ManagementModuleConstants.COMMON_EMPTY_TEXT;
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

	private List<ScopeUnitDTO> getListScopeLot(Lot currentLot, final Lot parentLot, final boolean showChildren,
			final boolean showScopeForSubLot) throws ManagementModuleException
	{
		LOG.debug("Lot  --    " + currentLot.getName());

		List<ScopeUnitDTO> lotScopeUnitList = new ArrayList<ScopeUnitDTO>();
		try
		{
			currentLot = getProjectPlanManager().getCompleteLot(currentLot.getId());

			/*
			 * for each scope unit in current lot we add a line in the result
			 * list
			 */
			for (ScopeUnit itScope : currentLot.getScopeEntities())
			{
				LOG.debug("Scope  --    " + itScope.getName());

				// ScopeUnit current = getProjectPlanManager().getScopeUnit(itScope.getUnitId());

				// skip the sub scope, they will be used later
				if (itScope.getParentScopeUnit() != null)
				{
					continue;
				}

				Long scopeId = 0l;

				if (currentLot.getProjectPlan().getStatus().getFunctionalId()
						.equalsIgnoreCase(ManagementModuleConstants.PROJECT_PLAN_DRAFT)
						&& currentLot.getProjectPlan().getVersion() > 1)
				{
					int digitsToRemove = 0;
					final String newVersion = "" + currentLot.getProjectPlan().getVersion().intValue();
					digitsToRemove = newVersion.length() + 1;

					String oldUnitId = getProjectPlanManager().determinateOldUnitIdFromCurrentScopeUnitId(currentLot
																																																		.getProjectPlan()
																																																		.getVersion() - 1,
																																																digitsToRemove,
																																																itScope.getUnitId());
					ScopeUnit oldScopeUnit = getProjectPlanManager().getScopeUnit(oldUnitId);

					if (oldScopeUnit != null)
					{
						scopeId = oldScopeUnit.getId();
					}
				}
				else
				{
					scopeId = itScope.getId();
				}

				final List<Long> tasksId = new ArrayList<Long>();

				if (scopeId != 0l)
				{
					for (Task t : getTaskManager().findByScopeUnitId(scopeId))
					{
						tasksId.add(t.getId());
					}
				}

				final ScopeUnitDTO newScope = BuildResources.buildScopeUnitDTO(currentLot, parentLot, itScope,
																																			 getStatusScopeUnit(itScope), null, tasksId);
				lotScopeUnitList.add(newScope);

				if (showChildren)
				{
					/*
					 * for each child scope unit of the current scope unit in
					 * current lot we add a line in the result list
					 */
					for (ScopeUnit itChildScope : itScope.getChildscopeunit())
					{
						lotScopeUnitList.addAll(getChildListScopeLot(itChildScope, newScope, currentLot, parentLot,
								newScope));
					}
				}
			}

			if (showScopeForSubLot)
			{
				/*
				 * for each child lot of the current lot we search scope unit to
				 * add in the result list
				 */
				for (Lot itChildLot : currentLot.getChildLots())
				{
					lotScopeUnitList.addAll(getListScopeLot(itChildLot, currentLot, showChildren, showScopeForSubLot));
				}
			}

		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Problem in creation of ScopeUnitList for LotName : " + currentLot.getName(), e);
		}
		return lotScopeUnitList;
	}

	private List<ScopeUnitDTO> getChildListScopeLot(final ScopeUnit curentScope,
			final ScopeUnitDTO parentScope, final Lot currentLot, final Lot parentLot,
			final ScopeUnitDTO scopeParent)
			throws org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException
	{
		List<ScopeUnitDTO> lotScopeUnitList = new ArrayList<ScopeUnitDTO>();

		LOG.debug("childscope   --    : " + curentScope.getName());

		List<Long> tasksId = new ArrayList<Long>();
		for (Task t : getTaskManager().findByScopeUnitId(curentScope.getId()))
		{
			tasksId.add(t.getId());
		}

		ScopeUnitDTO newScope = BuildResources.buildScopeUnitDTO(currentLot, parentLot, curentScope,
				getStatusScopeUnit(curentScope), scopeParent, tasksId);
		lotScopeUnitList.add(newScope);

		for (ScopeUnit itChildScope : curentScope.getChildscopeunit())
		{
			lotScopeUnitList.addAll(getChildListScopeLot(itChildScope, newScope, currentLot, parentLot, newScope));
		}

		return lotScopeUnitList;
	}

	@Override
	public List<ScopeUnitDTO> getProjectRefScopeUnitList(final String projectId)
			throws ManagementModuleException
	{
		List<ScopeUnitDTO> projectScopeUnitList = new ArrayList<ScopeUnitDTO>();

		final Map<String, ScopeUnitDTO> map = new HashMap<String, ScopeUnitDTO>();

		try
		{
			final List<CDOParameters> cdoParameters = getProjectPlanManager().findAllCDOParameters(projectId);
			List<RefScopeUnit> refScopeUnitList = getProjectPlanManager().getLastVersionRefScopeUnit(projectId);

			for (CDOParameters cdo : cdoParameters)
			{
				ScopeUnitDTO scopeUnitRacine = BuildResources.buildRootRefScopeUnitDTO(cdo, projectId);
				map.put("" + cdo.getId(), scopeUnitRacine);
			}

			for (RefScopeUnit itRefScopeUnit : refScopeUnitList)
			{
				ScopeUnitDTO newScopeUnit = BuildResources.buildRefScopeUnitDTO(itRefScopeUnit);
				String id = "" + itRefScopeUnit.getCdoParameters().getId();
				if (itRefScopeUnit.getCdoParameters() != null && map.containsKey(id))
				{
					ScopeUnitDTO su = map.get(id);
					su.getChildrenScopeUnit().add(newScopeUnit);
				}
			}

		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to get project reference scope ", e);
		}

		projectScopeUnitList.addAll(map.values());
		return projectScopeUnitList;
	}

	@Override
	public List<ScopeUnitDTO> getProjectPlanScope(final Long projectPlanId, final Long lotId,
			final boolean showChildsScope, final boolean showScopeForSubLot) throws ManagementModuleException
	{
		List<ScopeUnitDTO> projectScopeUnitList = new ArrayList<ScopeUnitDTO>();

		try
		{
			if (lotId == null)
			{
				List<Lot> projectPlanLotList = getProjectPlanManager().getCompleteListLots(projectPlanId);
				for (Lot itLot : projectPlanLotList)
				{
					projectScopeUnitList.addAll(getListScopeLot(itLot, null, showChildsScope, showScopeForSubLot));
				}
			}
			else
			{
				Lot projectPlanLot = getProjectPlanManager().getCompleteLot(lotId);
				projectScopeUnitList
						.addAll(getListScopeLot(projectPlanLot, null, showChildsScope, showScopeForSubLot));
			}

		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to get project plan scope by lot", e);
		}
		return projectScopeUnitList;
	}

	@Override
	public List<ScopeUnitLightDTO> getScopeUnitLight(final Long projectPlanId) throws ManagementModuleException
	{
		List<ScopeUnitLightDTO> scopeUnitLights = new ArrayList<ScopeUnitLightDTO>();

		try
		{
			Set<ScopeUnit> scopeUnits = new HashSet<ScopeUnit>(getProjectPlanManager()
					.findScopeUnitListByProjectPlanId(projectPlanId));
			if (scopeUnits != null && scopeUnits.size() > 0)
			{
				for (ScopeUnit scopeUnit : scopeUnits)
				{
					scopeUnitLights.add(BuildResources.buildScopeUnitLightDTOFromScopeUnit(scopeUnit));
				}
			}
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to get scope unit for the next iteration", e);
		}
		return scopeUnitLights;
	}

	@Override
	public List<String> getScopeTypeList() throws ManagementModuleException
	{
		List<String> listScopeType = new ArrayList<String>();
		try
		{
			for (ScopeType itType : getReferentielManager().getAllScopeType())
			{
				// TODO must used functionalId
				listScopeType.add(itType.getName());
			}
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to get scope TYPE list", e);
		}
		return listScopeType;
	}

	@Override
	public List<LotDTO> getLotList(final Long projectPlanId) throws ManagementModuleException
	{
		List<LotDTO> listLotDTO = new ArrayList<LotDTO>();
		try
		{
			List<Lot> lotList = getProjectPlanManager().getCompleteListLots(projectPlanId);
			if (lotList != null)
			{
				for (Lot itLot : lotList)
				{
					LotDTO newLotDTO = BuildResources.buildLotDTOFromLot(itLot);
					listLotDTO.add(newLotDTO);
					listLotDTO.addAll(getChildLotDTO(itLot));
				}
			}
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to get project plan lot list", e);
		}
		return listLotDTO;
	}

	@Override
	public ScopeUnitDTO createScopeUnit(final ScopeUnitDTO pScopeUnit, final String projectId)
			throws ManagementModuleException
	{
		try
		{

			if (pScopeUnit.getLotId() == null)
			{
				throw new ManagementModuleException(ErrorEnumeration.ERR_SCOPE_UNIT_NOT_LINKED_TO_LOT);
			}

			final Lot lot = getProjectPlanManager().getLot(Long.valueOf(pScopeUnit.getLotId()));

			pScopeUnit.setUnitId(getProjectPlanManager().generateManualUnitId(projectId, pScopeUnit.getName(),
					lot.getProjectPlan().getVersion()));

			String parentScopeUnitId = null;
			if (pScopeUnit.getParentScopeUnit() != null)
			{
				parentScopeUnitId = pScopeUnit.getParentScopeUnit().getUnitId();
			}

			final ScopeUnit createdScopeUnit = getProjectPlanManager().creeteScopeUnit(pScopeUnit.getName(),
					pScopeUnit.getDescription(), pScopeUnit.getUnitId(), pScopeUnit.getType(), pScopeUnit.getVersion(),
					true, Long.valueOf(pScopeUnit.getLotId()), parentScopeUnitId);

			pScopeUnit.setDate(new Date(createdScopeUnit.getDate().getTime()));

		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to create ScopeUnit", e);
		}
		return pScopeUnit;
	}

	@Override
	public ScopeUnitDTO createScopeUnitFromRef(final ScopeUnitDTO pRefScopeUnit, final Long lotId)
			throws ManagementModuleException
	{
		try
		{
			if (pRefScopeUnit.getType().equalsIgnoreCase(Constants.ROOT_REPOSITORY_KEY))
			{
				for (ScopeUnitDTO dto : pRefScopeUnit.getChildrenScopeUnit())
				{
					createScopeUnitFromRefScopeUnit(dto, lotId);
				}
			}
			else
			{
				createScopeUnitFromRefScopeUnit(pRefScopeUnit, lotId);
			}

		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to create scope unit from ref scope unit", e);
		}
		return pRefScopeUnit;
	}

	/**
	 * @param pRefScopeUnit
	 * @param lotId
	 * @throws ManagementModuleException
	 */
	private void createScopeUnitFromRefScopeUnit(final ScopeUnitDTO pRefScopeUnit, final Long lotId)
			throws org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException
	{

		ScopeUnit parent = null;

		if (!pRefScopeUnit.getType().equalsIgnoreCase(Constants.ROOT_REPOSITORY_KEY))
		{
			RefScopeUnit ref = getProjectPlanManager().getRefScopeUnit(pRefScopeUnit.getUnitId(),
					pRefScopeUnit.getVersion());
			Lot lot = getProjectPlanManager().getLot(lotId);
			if (ref != null && lot != null)
			{
				RefScopeUnit parentRef = ref.getParentScopeUnit();
				if (parentRef != null)
				{
					ScopeUnit parentResult = getProjectPlanManager().findScopedRefVersionUnit(parentRef.getUnitId(),
							parentRef.getVersion(), lot.getProjectPlan().getId());
					if (parentResult != null)
					{
						parent = parentResult;
					}
				}
			}
		}

		ScopeUnit pScopeUnit = getProjectPlanManager().creeteScopeUnitFromRefScopeUnit(
				pRefScopeUnit.getRefScopeUnitId(), pRefScopeUnit.getRefScopeUnitVersion(), lotId, parent);

		pRefScopeUnit.setDate(new Date(pScopeUnit.getDate().getTime()));
	}

	@Override
	public ScopeUnitDTO editScopeUnit(final ScopeUnitDTO pScopeUnit, final String projectId)
			throws ManagementModuleException
	{
		try
		{
			if (pScopeUnit.getLotId() == null)
			{
				throw new ManagementModuleException(ErrorEnumeration.ERR_SCOPE_UNIT_NOT_LINKED_TO_LOT);
			}

			if (pScopeUnit.getParentScopeUnit() == null)
			{
				getProjectPlanManager().updateManualScopeUnit(pScopeUnit.getUnitId(), pScopeUnit.getName(),
						pScopeUnit.getDescription(), pScopeUnit.getVersion(), Long.valueOf(pScopeUnit.getLotId()), null,
						pScopeUnit.getType());
			}
			else
			{
				getProjectPlanManager().updateManualScopeUnit(pScopeUnit.getUnitId(), pScopeUnit.getName(),
						pScopeUnit.getDescription(), pScopeUnit.getVersion(), null,
						pScopeUnit.getParentScopeUnit().getUnitId(), pScopeUnit.getType());
			}

		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to edit ProjectPlan", e);
		}
		return pScopeUnit;
	}

	@Override
	public Boolean deleteManualScopeUnit(final String pScopeUnitId) throws ManagementModuleException
	{
		try
		{
			return Boolean.valueOf(getProjectPlanManager().deleteScopeUnit(pScopeUnitId));

		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to delete scopeUnit manually", e);
		}
	}

	@Override
	public Boolean isAlreadyLinkedRef(final String pRefScopeUnitId) throws ManagementModuleException
	{
		boolean isAlreadyLinked = false;
		try
		{
			List<ScopeUnit> currentRefScope = getProjectPlanManager().findScopedRefUnit(pRefScopeUnitId);
			if (!currentRefScope.isEmpty())
			{
				isAlreadyLinked = true;
			}
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to verify if ref scope unit is already scoped", e);
		}
		return Boolean.valueOf(isAlreadyLinked);
	}

	@Override
	public Boolean linkRefScopeUnitToLot(final String pRefScopeUnitId, final String RefScopeUnitVersion,
			final Long lotId) throws ManagementModuleException
	{
		Boolean isLinked = false;
		try
		{
			getProjectPlanManager().creeteScopeUnitFromRefScopeUnit(pRefScopeUnitId, RefScopeUnitVersion, lotId,
					null);
			isLinked = true;
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to edit ProjectPlan", e);
		}
		return isLinked;
	}

	@Override
	public Boolean removeScopeUnitFromScope(final String pScopeUnitId) throws ManagementModuleException
	{
		boolean isRemoved = false;

		try
		{
			ScopeUnit currentScopeUnit = getProjectPlanManager().getScopeUnit(pScopeUnitId);
			if (currentScopeUnit != null)
			{
				isRemoved = getProjectPlanManager().deleteScopeUnit(pScopeUnitId);
			}

		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to remove scope unit from scope", e);
		}
		return Boolean.valueOf(isRemoved);
	}

	@Override
	public Boolean unlinkScopeUnitFromRef(final String pScopeUnitId) throws ManagementModuleException
	{
		boolean isLinked = false;

		try
		{
			ScopeUnit currentScopeUnit = getProjectPlanManager().getScopeUnit(pScopeUnitId);
			if (currentScopeUnit != null)
			{
				isLinked = getProjectPlanManager().unLinkExistingScopeUnit(currentScopeUnit);
			}

		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to unlink scope unit from ref", e);
		}
		return Boolean.valueOf(isLinked);
	}

	@Override
	public Boolean updateLinkRefScopeUnitToLot(final String pScopeUnitId, final Long lotId)
			throws ManagementModuleException
	{
		boolean isLinked = false;
		try
		{
			isLinked = getProjectPlanManager().linkScopeUnitToLot(pScopeUnitId, lotId);
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to add ref scope unit to lot", e);
		}
		return Boolean.valueOf(isLinked);
	}

	@Override
	public Boolean linkExistingScopeUnit(final String existingRefScopeId, final String existingRefScopeVersion,
																			 final String pScopeUnitId) throws ManagementModuleException
	{
		boolean isLinked = false;

		try
		{
			ScopeUnit currentScopeUnit = getProjectPlanManager().getScopeUnit(pScopeUnitId);
			if (currentScopeUnit != null)
			{
				isLinked = getProjectPlanManager().linkExistingScopeUnit(currentScopeUnit, existingRefScopeId,
																																 existingRefScopeVersion);
			}

		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to replace manual scope unit by ref", e);
		}
		return Boolean.valueOf(isLinked);
	}

	@Override
	public List<ScopeUnitGlobalMonitoringDTO> getScopeUnitMonitoring(final Long projectPlanId)
			throws ManagementModuleException
	{

		try
		{
			List<ScopeUnitGlobalMonitoringDTO> ret = new ArrayList<ScopeUnitGlobalMonitoringDTO>();
			final List<ScopeUnitIndicators> scopeUnitIndicators = getIndicatorsManager().getScopeUnitIndicators(
					projectPlanId);
			for (final ScopeUnitIndicators scopeUnitIndicator : scopeUnitIndicators)
			{
				ret.add(BuildResources.buildScopeUnitGlobalMonitoringDTOFromScopeUnitIndicators(scopeUnitIndicator));
			}
			return ret;
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to get scope unit with monitoring informations", e);
		}
	}

	@Override
	public Boolean finishScopeUnit(final String scopeUnitId) throws ManagementModuleException
	{
		try
		{
			ScopeUnit scopeUnit = getProjectPlanManager().getScopeUnit(scopeUnitId);

			List<ScopeUnitDiscipline> scopeUnitDisciplines = getProjectPlanManager()
					.findScopeUnitDisciplinesByScopeUnitId(scopeUnit.getId());
			for (ScopeUnitDiscipline scopeUnitDiscipline : scopeUnitDisciplines)
			{
				if (!ScopeUnitDisciplineStatusEnum.CLOSED.getFunctionnalId().equals(
						scopeUnitDiscipline.getStatus().getFunctionalId()))
				{
					getProjectPlanManager().terminateScopeUnitDiscipline(
							scopeUnitDiscipline.getScopeUnit().getUnitId(),
							scopeUnitDiscipline.getDiscipline().getFunctionalId());
				}
			}
			scopeUnit.getEstimation().setRemaining(0f);
			scopeUnit.setIsFinished(true);
			return Boolean.valueOf(getProjectPlanManager().updateScopeUnit(scopeUnit));

		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to finish scopeUnit", e);
		}
	}

	@Override
	public List<ScopeUnitIterationMonitoringDTO> getScopeUnitMonitoringByIteration(final long iterationId,
																																								 final String disciplineFunctionalId)
			throws ManagementModuleException
	{
		List<ScopeUnitIterationMonitoringDTO> ret = new ArrayList<ScopeUnitIterationMonitoringDTO>();
		try
		{
			List<ScopeUnitIndicators> scopeUnitIndicators = getIndicatorsManager()
																													.getScopeUnitIndicatorsForIteration(iterationId,
																																															disciplineFunctionalId);
			for (final ScopeUnitIndicators scopeUnitIndicator : scopeUnitIndicators)
			{
				ret.add(BuildResources.buildScopeUnitIterationMonitoringDTOFromScopeUnit(scopeUnitIndicator));
			}
			return ret;
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			LOG.error(e.getMessage());
			throw manageException("Unable to get scope unit with monitoring informations", e);
		}

	}

	@Override
	public Boolean createCSVFormatWithScopeUnitGlobalMonitoringDTOList(
			final List<ScopeUnitGlobalMonitoringDTO> list, final Map<ScopeUnitMonitoringStatusEnum, String> map)
	{
		final StringBuilder sb = new StringBuilder();
		for (ScopeUnitGlobalMonitoringDTO scopeUnitGlobalMonitoring : list)
		{
			sb.append(scopeUnitGlobalMonitoring.getScopeUnitName()).append(Constants.CSV_COLUMN_SEPARATOR)
					.append(checkData(scopeUnitGlobalMonitoring.getParentScopeUnitName()))
					.append(Constants.CSV_COLUMN_SEPARATOR)
					.append(scopeUnitGlobalMonitoring.getProjectPlanEstimation())
					.append(Constants.CSV_COLUMN_SEPARATOR).append(scopeUnitGlobalMonitoring.getConsumed())
					.append(Constants.CSV_COLUMN_SEPARATOR)
					.append(scopeUnitGlobalMonitoring.getRemainingScopeUnit().toString())
					.append(Constants.CSV_COLUMN_SEPARATOR).append(scopeUnitGlobalMonitoring.getRemainingTasks())
					.append(Constants.CSV_COLUMN_SEPARATOR)
					.append(String.valueOf(scopeUnitGlobalMonitoring.getReestimate()))
					.append(Constants.CSV_COLUMN_SEPARATOR)
					.append(String.valueOf(scopeUnitGlobalMonitoring.getAdvancement()))
					.append(Constants.CSV_COLUMN_SEPARATOR).append(map.get(scopeUnitGlobalMonitoring.getStatus()))
					.append(Constants.CSV_COLUMN_SEPARATOR)
					.append(checkData(scopeUnitGlobalMonitoring.getParentLotName()))
					.append(Constants.CSV_COLUMN_SEPARATOR).append(checkData(scopeUnitGlobalMonitoring.getLotName()))
					.append(Constants.CSV_COLUMN_SEPARATOR).append(scopeUnitGlobalMonitoring.getWeight())
					.append(Constants.CSV_COLUMN_SEPARATOR).append(scopeUnitGlobalMonitoring.getBenefit())
					.append(Constants.CSV_COLUMN_SEPARATOR).append(scopeUnitGlobalMonitoring.getRisk())
					.append(Constants.CSV_COLUMN_SEPARATOR).append(scopeUnitGlobalMonitoring.getInjury())
					.append(CSV_LINE_SEPARATOR);
		}
		getSession().setAttribute(Constants.SESSION_EXPORT_CSV_PARAMS, sb.toString());
		return true;
	}

	@Override
	public void createCSVFromScopeUnitIterationMonitoringDTOList(
			final List<ScopeUnitIterationMonitoringDTO> listToExport)
	{
		final StringBuilder sb = new StringBuilder();
		for (final ScopeUnitIterationMonitoringDTO scopeUnitIterationMonitoring : listToExport)
		{
			sb.append(scopeUnitIterationMonitoring.getScopeUnitName()).append(Constants.CSV_COLUMN_SEPARATOR);
			sb.append(scopeUnitIterationMonitoring.getParentScopeUnitName()).append(Constants.CSV_COLUMN_SEPARATOR);
			sb.append(scopeUnitIterationMonitoring.getEstimation()).append(Constants.CSV_COLUMN_SEPARATOR);
			sb.append(scopeUnitIterationMonitoring.getConsumed().toString()).append(Constants.CSV_COLUMN_SEPARATOR);
			sb.append(scopeUnitIterationMonitoring.getRemainingTasks().toString()).append(
					Constants.CSV_COLUMN_SEPARATOR);
			sb.append(scopeUnitIterationMonitoring.getReestimate()).append(Constants.CSV_COLUMN_SEPARATOR);
			sb.append(scopeUnitIterationMonitoring.getAdvancement()).append(CSV_LINE_SEPARATOR);
		}
		getSession().setAttribute(Constants.SESSION_EXPORT_CSV_PARAMS, sb.toString());
	}

	private List<LotDTO> getChildLotDTO(final Lot pLot)
			throws org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException
	{
		List<LotDTO> listLotDTO = new ArrayList<LotDTO>();

		Lot completeLot = getProjectPlanManager().getCompleteLot(pLot.getId());

		if (completeLot.getChildLots() != null && completeLot.getChildLots().size() > 0)
		{
			for (Lot itChildLot : completeLot.getChildLots())
			{
				LotDTO newLotDTO = BuildResources.buildLotDTOFromLot(itChildLot);

				listLotDTO.add(newLotDTO);
				listLotDTO.addAll(getChildLotDTO(itChildLot));
			}
		}
		return listLotDTO;
	}

}
