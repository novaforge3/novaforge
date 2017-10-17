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
package org.novaforge.forge.tools.managementmodule.report.dataProvider.ganttDiagram;

import org.novaforge.forge.tools.managementmodule.domain.report.GanttLotInfo;
import org.novaforge.forge.tools.managementmodule.report.model.impl.GanttLotInfoImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class is used by ReportEngine to get the data used to render the report. It is also a iPOJO component
 * in order to use OSGi service to get those datas.
 * 
 * @author Falsquelle-e
 */
public class GanttDiagramOfLotsDataProviderTest
{

	/**
	 * Used for Birt Instanciation
	 */
	public GanttDiagramOfLotsDataProviderTest()
	{
		// Nothing to do
	}

	public List<GanttLotInfo> getGanttLotInfosByProjectPlanId(final Long projectPlanId)
	{
		List<GanttLotInfo> results = new ArrayList<GanttLotInfo>();
		Calendar c = Calendar.getInstance();

		GanttLotInfo g1 = new GanttLotInfoImpl();
		g1.setId(1l);
		g1.setDescription("desc1");
		c.set(2010, 4, 12);
		g1.setStartDateLot(c.getTime());
		c.set(2010, 5, 12);
		g1.setEndDateLot(c.getTime());
		g1.setStartDateSubLot(null);
		g1.setEndDateSubLot(null);
		results.add(g1);

		GanttLotInfo g2 = new GanttLotInfoImpl();
		g2.setId(1l);
		g2.setDescription("desc1");
		c.set(2010, 4, 12);
		g2.setStartDateLot(c.getTime());
		c.set(2010, 5, 12);
		g2.setEndDateLot(c.getTime());
		g2.setStartDateSubLot(null);
		g2.setEndDateSubLot(null);
		results.add(g2);

		return results;
	}
}
