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
package org.novaforge.forge.tools.managementmodule.report.dataProvider.taskCategoriesPie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.novaforge.forge.tools.managementmodule.business.TaskManager;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.TaskCategory;
import org.novaforge.forge.tools.managementmodule.domain.TaskCriteria;
import org.novaforge.forge.tools.managementmodule.domain.report.PieTaskCategory;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.novaforge.forge.tools.managementmodule.report.dataProvider.AbstractDataProvider;
import org.novaforge.forge.tools.managementmodule.report.model.impl.PieTaskCategoryImpl;

/**
 * This class is used by ReportEngine to get the data used to render the report. It is also a iPOJO component
 * in order to use OSGi service to get those datas.
 * 
 * @author Falsquelle-e
 */
public class TaskCategoriesPieDataProvider extends AbstractDataProvider
{

	/**
	 * Used for Birt Instanciation
	 */
	public TaskCategoriesPieDataProvider()
	{

	}

	/**
	 * @param projectPlanId
	 * @param iterationId
	 * @param disciplineId
	 * @param onlyTask
	 * @return
	 */
	public List<PieTaskCategory> getTaskCategoriesByIteration(final Long iterationId, final String disciplineFunctionalId)
	{
		List<PieTaskCategory> results = new ArrayList<PieTaskCategory>();

		final TaskManager taskManager = this.getService(TaskManager.class);

		try
		{
			TaskCriteria filter = new TaskCriteria();
			filter.setDisciplineFunctionalId(disciplineFunctionalId);
			filter.setTaskStatus(taskManager.getStatusTask(ManagementModuleConstants.TASK_STATUS_DONE));
			filter.setIterationId(iterationId);

			Set<Task> tasks = taskManager.getTasksByCriteria(filter);
			Map<String, PieTaskCategory> categories = new HashMap<String, PieTaskCategory>();

			// Parcours des taches de l'iteration pour compter le nombre de tache de categories
			Integer nbTasksTotal = 0;
			for (Task task : tasks)
			{
				TaskCategory category = task.getTaskCategory();

				if (category != null)
				{
					// Increment du nombre de taches pour la categorie
					PieTaskCategory pieCategory = categories.get(category.getName());
					if (pieCategory != null)
					{
						pieCategory.setValue(pieCategory.getValue() + 1f);
					}
					// Initialisation de la categorie
					else
					{
						pieCategory = new PieTaskCategoryImpl();
						pieCategory.setName(category.getName());
						pieCategory.setValue(1f);
					}
					categories.put(category.getName(), pieCategory);
					nbTasksTotal++;
				}
			}

			// Transformation en pourcentage des valeurs
			results = new ArrayList<PieTaskCategory>(categories.values());
			if (results.size() > 0)
			{
				for (PieTaskCategory result : results)
				{
					result.setValue(100 * result.getValue() / (float) nbTasksTotal);
				}
			}
		}
		catch (ManagementModuleException e)
		{
			LOG.error("Erreur lors de la recuperation des taches pour le graphique de taches par categorie pour une iteration",
								e);
			return results;
		}

		return results;
	}
}
