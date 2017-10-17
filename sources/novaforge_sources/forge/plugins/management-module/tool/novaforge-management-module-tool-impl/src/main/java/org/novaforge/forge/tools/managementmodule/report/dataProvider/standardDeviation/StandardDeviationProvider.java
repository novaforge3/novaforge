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
package org.novaforge.forge.tools.managementmodule.report.dataProvider.standardDeviation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.novaforge.forge.tools.managementmodule.business.BusinessObjectFactory;
import org.novaforge.forge.tools.managementmodule.business.IndicatorsManager;
import org.novaforge.forge.tools.managementmodule.business.IterationManager;
import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.report.StandardDeviationIt;
import org.novaforge.forge.tools.managementmodule.domain.transfer.MonitoringIndicators;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.novaforge.forge.tools.managementmodule.report.dataProvider.AbstractDataProvider;

public class StandardDeviationProvider extends AbstractDataProvider
{

	private BusinessObjectFactory businessObjectFactory;

	/**
	 * Used for Birt Instanciation
	 */
	public StandardDeviationProvider()
	{
    if ( businessObjectFactory == null )
    {
      businessObjectFactory =  getService(BusinessObjectFactory.class);
    }
	}

	public List<StandardDeviationIt> getStandardDeviationIt(final Long projectPlanId, final String discipline,
																													final Long lotId, final Long subLotId)
	{
		List<StandardDeviationIt> retour             = new ArrayList<StandardDeviationIt>();
		final IterationManager    iterationManager   = this.getService(IterationManager.class);
		final IndicatorsManager   indicatorManager   = getService(IndicatorsManager.class);
		final ProjectPlanManager  projectPlanManager = this.getService(ProjectPlanManager.class);
		if (projectPlanManager != null && iterationManager != null)
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
				// pour la courbe de tendance (moindre carres ou ordinary least
				// squares regression )
				DescriptiveStatistics statsVelocity = new DescriptiveStatistics();
				DescriptiveStatistics statsFocalisation = new DescriptiveStatistics();
				int nbPoints = 0;
				Float standartDeviationVelocity;
				Float standartDeviationFocalisation;
				for (Iteration it : iterationList)
				{
					MonitoringIndicators selecteddvancementIndicators = null;
					if (it.isFinished())
					{
						MonitoringIndicators indicators = null;
						if (discipline != null && discipline.equals("all"))
						{// discpline all
							indicators = indicatorManager.getIterationMonitoringIndicators(it.getId(), null);
						}
						else
						{
							indicators = indicatorManager.getIterationMonitoringIndicators(it.getId(), discipline);
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
							selecteddvancementIndicators = indicators;
						}
						else if (lotId.intValue() == idLotLinked)
						{
							selecteddvancementIndicators = indicators;
						}
						else if ((lotId.intValue() == subLotId.intValue()) && lotId.intValue() == 0)
						{
							selecteddvancementIndicators = indicators;
						}
					}
					StandardDeviationIt standardDeviationIt = businessObjectFactory.getInstanceStandardDeviationIt();
					if (selecteddvancementIndicators == null)
					{
						standardDeviationIt.setItName(it.getLabel());
						standardDeviationIt.setEcartTypeFocalisation(new Float("0.F"));
						standardDeviationIt.setEcartTypeVelocite(new Float("0.F"));
						standardDeviationIt.setMoyenneErreurEstimation(new Float("0.F"));
					}
					else
					{
						statsVelocity.addValue(selecteddvancementIndicators.getVelocity());
						statsFocalisation.addValue(selecteddvancementIndicators.getFocalisation());
						standartDeviationVelocity = new Float(Double.toString(statsVelocity.getStandardDeviation()));
						standartDeviationFocalisation = new Float(Double.toString(statsFocalisation
								.getStandardDeviation()));
						nbPoints++;
						standardDeviationIt.setEcartTypeFocalisation(standartDeviationFocalisation * 100);
						standardDeviationIt.setEcartTypeVelocite(standartDeviationVelocity * 100
								/ new Float(Double.toString(statsVelocity.getMean())));
						standardDeviationIt.setMoyenneErreurEstimation(selecteddvancementIndicators
								.getAverageEstimationError());
						standardDeviationIt.setItName(it.getLabel());
						retour.add(standardDeviationIt);
					}

				}
			}
			catch (ManagementModuleException e)
			{
			  LOG.error(e.getMessage(),e);
			}

		}
		LOG.debug("getStandardDeviationIt fin");
		return retour;
	}
}
