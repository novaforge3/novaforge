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
package org.novaforge.forge.tools.managementmodule.report.dataProvider.idealWorkingPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.novaforge.forge.tools.managementmodule.business.BusinessObjectFactory;
import org.novaforge.forge.tools.managementmodule.business.IndicatorsManager;
import org.novaforge.forge.tools.managementmodule.business.IterationManager;
import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.report.PfeIdealIt;
import org.novaforge.forge.tools.managementmodule.domain.transfer.ScopeUnitIndicators;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.novaforge.forge.tools.managementmodule.report.dataProvider.AbstractDataProvider;

public class IdealWorkingPointDataProvider extends AbstractDataProvider
{

	private BusinessObjectFactory businessObjectFactory;

	/**
	 * Used for Birt Instanciation
	 */
	public IdealWorkingPointDataProvider()
	{
    if ( businessObjectFactory == null )
    {
      businessObjectFactory =  getService(BusinessObjectFactory.class);
    }
	}

	/**
	 * To get all IdealScopeUnitFP of iteration of plan project
	 *
	 * @param projectPlanId
	 * @return IdealScopeUnitFP iT
	 */
	public List<PfeIdealIt> getIdealScopeUnitFPByIterationDate(final Long projectPlanId, final String discipline,
																														 final Long lotId, final Long subLotId)
	{
		// refScopeUnitfromSGBD.getName() debut projectPlanId=1 discipline=all lotId=0 subLotId=0
		List<PfeIdealIt>         retour             = new ArrayList<PfeIdealIt>();
		final ProjectPlanManager projectPlanManager = this.getService(ProjectPlanManager.class);
		final IterationManager   iterationManager   = this.getService(IterationManager.class);
		final IndicatorsManager  indicatorsManager  = this.getService(IndicatorsManager.class);
		if (projectPlanManager != null)
		{
			try
			{
				List<Iteration> iterationList = iterationManager.getFinishedAndCurrentIterationList(projectPlanId);
				Collections.sort(iterationList, new Comparator<Iteration>()
				{
					@Override
					public int compare(final Iteration it1, final Iteration it2)
					{
						return it1.getEndDate().compareTo(it2.getEndDate());
					}
				});
				for (Iteration it : iterationList)
				{
					List<ScopeUnit> selecteddlistScopeUnitIteration = new ArrayList<ScopeUnit>();
					if (it.isFinished())
					{
						List<ScopeUnitIndicators> listScopeUnitIndicators = new ArrayList<ScopeUnitIndicators>();
						if (discipline != null && discipline.equals("all"))
						{// discpline all
							listScopeUnitIndicators.addAll(indicatorsManager.getScopeUnitIndicatorsForIteration(it.getId(), null));
						}
						else
						{
							listScopeUnitIndicators.addAll(indicatorsManager.getScopeUnitIndicatorsForIteration(it.getId(),
																																																	discipline));
						}
						Long idLotLinked = null;
						Lot lotParent = it.getLot().getParentLot();
						if (lotParent == null)
						{
							idLotLinked = it.getLot().getId();
						}
						else
						{
							idLotLinked = lotParent.getId();
						}
						if (subLotId.intValue() == idLotLinked)
						{
							for (ScopeUnitIndicators scopeUnitIndicators : listScopeUnitIndicators)
							{
								selecteddlistScopeUnitIteration.add(scopeUnitIndicators.getScopeUnit());
							}
						}
						else if (lotId.intValue() == idLotLinked)
						{
							for (ScopeUnitIndicators scopeUnitIndicators : listScopeUnitIndicators)
							{
								selecteddlistScopeUnitIteration.add(scopeUnitIndicators.getScopeUnit());
							}
						}
						else if ((lotId.intValue() == subLotId.intValue()) && lotId.intValue() == 0)
						{
							for (ScopeUnitIndicators scopeUnitIndicators : listScopeUnitIndicators)
							{
								selecteddlistScopeUnitIteration.add(scopeUnitIndicators.getScopeUnit());
							}
						}
					}
					if (!selecteddlistScopeUnitIteration.isEmpty())
					{
						Float idealScopeUnitFP = projectPlanManager.calculateIdealScopeUnitFP(
								selecteddlistScopeUnitIteration, (projectPlanManager.getProjectPlan(projectPlanId))
										.getProject().getProjectId());
						PfeIdealIt pfeIdIt = businessObjectFactory.getInstancePfeIdealIt();
						pfeIdIt.setNameIt(it.getLabel());
						pfeIdIt.setPfeIdeal(idealScopeUnitFP);
						retour.add(pfeIdIt);
					}
				}
			}
			catch (ManagementModuleException e)
			{
				LOG.error("Error in getting data for IdealWokingPoint Report", e);
			}
		}
		return retour;
	}
}
