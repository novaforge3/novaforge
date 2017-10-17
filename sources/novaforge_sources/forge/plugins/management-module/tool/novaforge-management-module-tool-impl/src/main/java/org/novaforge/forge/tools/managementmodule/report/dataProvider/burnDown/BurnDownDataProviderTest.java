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
package org.novaforge.forge.tools.managementmodule.report.dataProvider.burnDown;

import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.report.BurnDownPoint;
import org.novaforge.forge.tools.managementmodule.report.model.impl.BurnDownPointImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This class is used by Birt Eclipse Plugin Designer to get dummy datas in order to modify the layout of the
 * default rptdesign
 * 
 * @author Crapart-f
 */
public class BurnDownDataProviderTest
{

	/**
	 * Used for Birt Instanciation
	 */
	public BurnDownDataProviderTest()
	{
		// Nothing to do
	}

	public List<BurnDownPoint> getBurnDownTaskByProjectPlanAndByIteration(final Long projectPlanId,
			final Long iterationId)
	{
		List<BurnDownPoint> results = new ArrayList<BurnDownPoint>();

		Calendar c = Calendar.getInstance();
		c.set(2012, 1, 1);

		// Estimations
		// T1 : 5j
		// T2 : 7j
		// T3 : 3j
		// Debut : 01/01, fin 15/01

		// Reel :
		// T1 : 9j
		// T2 : 5j
		// T2 : pas finie

		// T0 --> origine du graphique
		// reste 15j
		BurnDownPoint bdt0 = new BurnDownPointImpl();
		bdt0.setRealRemainingLoad(15f);
		bdt0.setPrevisionnalRemainingLoad(15f);
		bdt0.setDate(c.getTime());
		results.add(bdt0);

		// T1 --> fin tache T1
		// reste 6j
		c.set(2012, 1, 10);
		BurnDownPoint bdt1 = new BurnDownPointImpl();
		bdt1.setRealRemainingLoad(6f);
		bdt1.setPrevisionnalRemainingLoad(null);
		bdt1.setDate(c.getTime());
		results.add(bdt1);

		// Test null pour echelle
		c.set(2012, 1, 11);
		bdt1 = new BurnDownPointImpl();
		bdt1.setRealRemainingLoad(null);
		bdt1.setPrevisionnalRemainingLoad(null);
		bdt1.setDate(c.getTime());
		results.add(bdt1);
		c.set(2012, 1, 12);
		bdt1 = new BurnDownPointImpl();
		bdt1.setRealRemainingLoad(null);
		bdt1.setPrevisionnalRemainingLoad(null);
		bdt1.setDate(c.getTime());
		results.add(bdt1);
		c.set(2012, 1, 13);
		bdt1 = new BurnDownPointImpl();
		bdt1.setRealRemainingLoad(null);
		bdt1.setPrevisionnalRemainingLoad(null);
		bdt1.setDate(c.getTime());
		results.add(bdt1);
		c.set(2012, 1, 14);
		bdt1 = new BurnDownPointImpl();
		bdt1.setRealRemainingLoad(null);
		bdt1.setPrevisionnalRemainingLoad(null);
		bdt1.setDate(c.getTime());
		results.add(bdt1);

		// T2 --> fin tache t2
		// reste 1j
		c.set(2012, 1, 15);
		BurnDownPoint bdt2 = new BurnDownPointImpl();
		bdt2.setRealRemainingLoad(1f);
		bdt2.setPrevisionnalRemainingLoad(null);
		bdt2.setDate(c.getTime());
		results.add(bdt2);

		// TFin --> fin iteration (seulement previsionnel ici car t3 par terminee)
		c.set(2012, 1, 16);
		BurnDownPoint bdtFin = new BurnDownPointImpl();
		bdtFin.setRealRemainingLoad(null);
		bdtFin.setPrevisionnalRemainingLoad(0f);
		bdtFin.setDate(c.getTime());
		results.add(bdtFin);

		return results;
	}

	/**
	 * Construction de la liste des points du burnDown, on cree un point par jour
	 * 
	 * @param beginDate
	 * @param endDate
	 * @param tasksList
	 * @return
	 */
	protected List<BurnDownPoint> buildBurnDownTasks(final Date beginDate, final Date endDate,
			final List<Task> tasksList)
	{
		// Recuperation du total de charge previsionnel

		//

		return null;
	}
}
