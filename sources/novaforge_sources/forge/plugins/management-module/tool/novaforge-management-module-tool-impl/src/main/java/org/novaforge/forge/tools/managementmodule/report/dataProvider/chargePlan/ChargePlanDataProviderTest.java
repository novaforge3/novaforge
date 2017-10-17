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

import org.novaforge.forge.tools.managementmodule.domain.report.DayLoad;
import org.novaforge.forge.tools.managementmodule.report.model.impl.DayLoadImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class is used by Birt Eclipse Plugin Designer to get dummy datas in order to modify the layout of the
 * default rptdesign
 * 
 * @author Falsquelle-e
 */
public class ChargePlanDataProviderTest
{

	/**
	 * Used for Birt Instanciation
	 */
	public ChargePlanDataProviderTest()
	{
		// Nothing to do
	}

	public List<DayLoad> getLoadsByDateByProjectPlanId(final Long projectPlanId)
	{
		List<DayLoad> results = new ArrayList<DayLoad>();

		Calendar c = Calendar.getInstance();

		DayLoad dl1 = new DayLoadImpl();
		dl1.setDate(c.getTime());
		dl1.setLoad(10f);
		results.add(dl1);

		c.add(Calendar.WEEK_OF_YEAR, 1);

		DayLoad dl2 = new DayLoadImpl();
		dl2.setDate(c.getTime());
		dl2.setLoad(25f);
		results.add(dl2);

		c.add(Calendar.WEEK_OF_YEAR, 1);

		DayLoad dl3 = new DayLoadImpl();
		dl3.setDate(c.getTime());
		dl3.setLoad(30f);
		results.add(dl3);

		c.add(Calendar.WEEK_OF_YEAR, 1);

		DayLoad dl4 = new DayLoadImpl();
		dl4.setDate(c.getTime());
		dl4.setLoad(3f);
		results.add(dl4);

		return results;
	}
}
