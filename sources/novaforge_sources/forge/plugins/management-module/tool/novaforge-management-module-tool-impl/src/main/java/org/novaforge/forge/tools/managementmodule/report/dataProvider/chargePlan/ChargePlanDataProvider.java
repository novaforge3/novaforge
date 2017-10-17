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
package org.novaforge.forge.tools.managementmodule.report.dataProvider.chargePlan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.business.ReferentielManager;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.UnitTime;
import org.novaforge.forge.tools.managementmodule.domain.report.DayLoad;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.novaforge.forge.tools.managementmodule.report.dataProvider.AbstractDataProvider;
import org.novaforge.forge.tools.managementmodule.report.model.impl.DayLoadImpl;
import org.novaforge.forge.tools.managementmodule.services.Util;

/**
 * This class is used by ReportEngine to get the data used to render the report. It is also a iPOJO component
 * in order to use OSGi service to get those datas.
 * 
 * @author Falsquelle-e
 */
public class ChargePlanDataProvider extends AbstractDataProvider
{

	/**
	 * Used for Birt Instanciation
	 */
	public ChargePlanDataProvider()
	{
	}

	public List<DayLoad> getLoadsByDateByProjectPlanId(final Long projectPlanId)
	{
		final ProjectPlanManager projectPlanManager = this.getService(ProjectPlanManager.class);

		final ReferentielManager referentielManager = this.getService(ReferentielManager.class);

		if (projectPlanManager != null)
		{
			try
			{
				List<DayLoad> results = projectPlanManager.getLoadsByDateByProjectPlanId(projectPlanId);

				final Map<Date, DayLoad> dayLoadByDate = new HashMap<Date, DayLoad>();

				for (DayLoad dl : results)
				{
					dayLoadByDate.put(dl.getDate(), dl);
				}

				ProjectPlan projectPlan = projectPlanManager.getProjectPlan(projectPlanId);

				// Récupération des Lots
				List<Lot> listLots = projectPlanManager.getParentLotsList(projectPlanId);

				Collections.sort(listLots, new Comparator<Lot>()
				{

					@Override
					public int compare(final Lot o1, final Lot o2)
					{
						return o1.getStartDate().compareTo(o2.getStartDate());
					}
				});

				// find bounds
				Date firstDate = null;
				Date lastDate = null;
				for (Lot lot : listLots)
				{
					Date start = lot.getStartDate();
					Date end = lot.getEndDate();
					if (firstDate == null || firstDate.after(start))
					{
						firstDate = start;
					}
					if (lastDate == null || lastDate.before(end))
					{
						lastDate = end;
					}
				}
				firstDate = Util.clearTime(firstDate);
				lastDate = Util.clearTime(lastDate);

				UnitTime utWeek = referentielManager.getUnitTimeByIdFunctional(ManagementModuleConstants.UNIT_TIME_WEEK);

				int calendarField = Calendar.WEEK_OF_YEAR;

				// The smaller UnitTime displayed is the week, so, if it's not, it's
				// regrouped for a week.
				if (projectPlan.getProject().getUnitTime().getDurationInDays() > utWeek.getDurationInDays())
				{
					calendarField = Calendar.MONTH;
				}

				Calendar calendarStartDate = Calendar.getInstance();

				calendarStartDate.setTime(firstDate);

				Date startPeriode;
				boolean firstLoop = true;

				do
				{
					startPeriode = calendarStartDate.getTime();

					if (firstLoop)
					{
						if (calendarField == Calendar.WEEK_OF_YEAR)
						{
							calendarStartDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
						}
						else
						{
							calendarStartDate.set(Calendar.DAY_OF_MONTH, 1);
						}
						firstLoop = false;
					}
					calendarStartDate.add(calendarField, 1);

					DayLoad currentDayLoad = dayLoadByDate.get(startPeriode);

					if (currentDayLoad == null)
					{
						currentDayLoad = new DayLoadImpl();
						currentDayLoad.setDate(startPeriode);
						currentDayLoad.setLoad(0.0f);

						dayLoadByDate.put(startPeriode, currentDayLoad);
					}
				}
				while (lastDate.after(calendarStartDate.getTime()));

				return new ArrayList<DayLoad>(dayLoadByDate.values());
			}
			catch (ManagementModuleException e)
			{
				LOG.error(e,e);
				return new ArrayList<DayLoad>();
			}
		}
		else
		{
			return new ArrayList<DayLoad>();
		}

	}
}
