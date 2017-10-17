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

import java.util.ArrayList;
import java.util.List;

import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.domain.report.GanttLotInfo;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.novaforge.forge.tools.managementmodule.report.dataProvider.AbstractDataProvider;

/**
 * This class is used by ReportEngine to get the data used to render the report. It is also a iPOJO component
 * in order to use OSGi service to get those datas.
 * 
 * @author Falsquelle-e
 */
public class GanttDiagramOfLotsDataProvider extends AbstractDataProvider
{
	/**
	 * Used for Birt Instanciation
	 */
	public GanttDiagramOfLotsDataProvider()
	{
	}

	public List<GanttLotInfo> getGanttLotInfosByProjectPlanId(final Long projectPlanId)
	{
		final ProjectPlanManager projectPlanManager = this.getService(ProjectPlanManager.class);

		if (projectPlanManager != null)
		{
			try
			{
				return projectPlanManager.getGanttLotInfosByProjectPlanId(projectPlanId);
			}
			catch (ManagementModuleException e)
			{
				LOG.error(e,e);
				return new ArrayList<GanttLotInfo>();
			}
		}
		else
		{
			return new ArrayList<GanttLotInfo>();
		}

	}
}
