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

package org.novaforge.forge.tools.managementmodule.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * An iteration is a time of work, which contains tasks
 */
public interface Iteration extends Serializable, Comparable<Iteration>
{

	Date getStartDate();

	void setStartDate(Date startDate);

	Date getEndDate();

	void setEndDate(Date endDate);

	boolean isFinished();
	
	void setFinished(boolean isFinished);

	Lot getLot();

	void setLot(Lot my_lot);

	Set<IterationTask> getIterationTasks();

	void setIterationTasks(Set<IterationTask> iterationTasks);
	
	void addIterationTask(IterationTask task);

	void removeIterationTask(IterationTask task);

	String getLabel();

	void setLabel(String label);

	int getNumIteration();

	void setNumIteration(int numIteration);

	PhaseType getPhaseType();

	void setPhaseType(PhaseType phaseType);

   Long getId();


}
