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
package org.novaforge.forge.tools.managementmodule.report.dataProvider.focusingFactor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.novaforge.forge.tools.managementmodule.business.BusinessObjectFactory;
import org.novaforge.forge.tools.managementmodule.business.IndicatorsManager;
import org.novaforge.forge.tools.managementmodule.business.IterationManager;
import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.IterationTask;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.User;
import org.novaforge.forge.tools.managementmodule.domain.report.FocalisationFactorIt;
import org.novaforge.forge.tools.managementmodule.domain.transfer.MonitoringIndicators;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.novaforge.forge.tools.managementmodule.report.dataProvider.AbstractDataProvider;

public class FocusingFactorProvider extends AbstractDataProvider
{
	private BusinessObjectFactory businessObjectFactory;

	/**
	 * Used for Birt Instanciation
	 */
	public FocusingFactorProvider()
	{
	  if ( businessObjectFactory == null )
	  {
	    businessObjectFactory =  getService(BusinessObjectFactory.class);
	  }
	}

	public List<FocalisationFactorIt> getFocalisationByIterationDate(final Long projectPlanId, final String discipline,
																																	 final Long lotId, final Long subLotId)
	{
		List<FocalisationFactorIt> retour             = new ArrayList<FocalisationFactorIt>();
		final IterationManager     iterationManager   = this.getService(IterationManager.class);
		final IndicatorsManager    indicatorManager   = getService(IndicatorsManager.class);
		final ProjectPlanManager   projectPlanManager = this.getService(ProjectPlanManager.class);
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
				// pour la courbe de tendance (moindre carres ou ordinary least
				// squares regression )
				SimpleRegression regression = new SimpleRegression();
				Double abscisseX = 0d;
				int nbPoints = 0;
				for (Iteration it : iterationList)
				{
					MonitoringIndicators selecteddvancementIndicators = null;
					if (it.isFinished())
					{
						MonitoringIndicators indicators;
						if (discipline != null && discipline.equals("all"))
						{// discpline all
							indicators = indicatorManager.getIterationMonitoringIndicators(it.getId(), null);
						}
						else
						{
							indicators = indicatorManager.getIterationMonitoringIndicators(it.getId(), discipline);
						}
						Long idLotLinked;
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
					FocalisationFactorIt focalisationFactorIt = businessObjectFactory.getInstanceFocalisationFactorIt();
					if (selecteddvancementIndicators == null)
					{
						focalisationFactorIt.setBeginingDate(it.getStartDate());
						focalisationFactorIt.setFocusingFactor(Float.valueOf(0));
						focalisationFactorIt.setFocusingFactorIdeal(Float.valueOf(1));
						focalisationFactorIt.setFocusingFactorMedian(Float.valueOf(0));
						focalisationFactorIt.setIterationName(it.getLabel());
						final HashSet<User> listUserit = new HashSet<User>();
						Set<IterationTask> setiterationTask = it.getIterationTasks();
						for (IterationTask iterationTask : setiterationTask)
						{
							listUserit.add(iterationTask.getTask().getUser());
						}
						focalisationFactorIt.setNombreRessources(listUserit.size());
						focalisationFactorIt.setVelocity(Float.valueOf(0));

					}
					else
					{
						focalisationFactorIt.setFocusingFactor(selecteddvancementIndicators.getFocalisation());
						focalisationFactorIt.setVelocity(selecteddvancementIndicators.getVelocity());
						focalisationFactorIt.setBeginingDate(it.getStartDate());
						focalisationFactorIt.setFocusingFactorIdeal(Float.valueOf(1));
						Float focusingFactorMedian = selecteddvancementIndicators.getFocalisation();
						abscisseX = abscisseX + 1d;
						nbPoints++;
						regression.addData(abscisseX, focusingFactorMedian);
						focusingFactorMedian = Float.valueOf(Double.toString(regression.predict(abscisseX)));
						if (focusingFactorMedian.isNaN())
						{// il faut au moins 2
						 // points
							selecteddvancementIndicators.getFocalisation();
						}

						focalisationFactorIt.setIterationName(it.getLabel());
						Set<IterationTask> setiterationTask = it.getIterationTasks();
						// calcul vu avec CP a verifier avec Laura, le HashSet evite
						// les doublons
						HashSet<User> listUserit = new HashSet<User>();
						for (IterationTask iterationTask : setiterationTask)
						{
							listUserit.add(iterationTask.getTask().getUser());
						}
						focalisationFactorIt.setNombreRessources(listUserit.size());
						SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
						focalisationFactorIt.setIterationName(it.getLabel() + "\n"
								+ selecteddvancementIndicators.getVelocity() + "\n" + formatter.format(it.getStartDate())
								+ "\n" + listUserit.size());
						retour.add(focalisationFactorIt);
					}

				}
				// on modifie les points precedents pour la tendance
				Double abscisseXprec = 1.d;
				for (FocalisationFactorIt ffit : retour)
				{
					Double val = regression.predict(abscisseXprec);
					if (!val.isNaN())
					{
						ffit.setFocusingFactorMedian(Float.valueOf(Double.toString(val)));
					}
					abscisseXprec = abscisseXprec + 1d;
				}
			}
			catch (ManagementModuleException e)
			{
				LOG.error(e.getMessage(),e);
			}

		}
		LOG.debug("getFocalisationByIterationDate fin" + retour.size());
		return retour;
	}
}
