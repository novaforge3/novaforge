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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.novaforge.forge.tools.managementmodule.business.IterationManager;
import org.novaforge.forge.tools.managementmodule.business.TaskManager;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.report.BurnDownPoint;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.novaforge.forge.tools.managementmodule.report.dataProvider.AbstractDataProvider;
import org.novaforge.forge.tools.managementmodule.report.model.impl.BurnDownPointImpl;

/**
 * This class is used by ReportEngine to get the data used to render the report. It is also a iPOJO component
 * in order to use OSGi service to get those datas.
 * 
 * @author Falsquelle-e
 */
public class BurnDownDataProvider extends AbstractDataProvider
{

	/**
	 * Fourniture des points du graphique de burndown pour une iteration ou un plan projet (filtre facultatif
	 * sur discipline)
	 *
	 * @param projectPlanId
	 * @param iterationId
	 * @param disciplineId
	 * @param onlyTask
	 * @return
	 */
	public List<BurnDownPoint> getBurnDownPointsByProjectPlanOrByIteration(final Long projectPlanId,
																																				 final Long iterationId,
																																				 final String disciplineFunctionalId,
																																				 final boolean onlyTask)
	{
		List<BurnDownPoint> results = new ArrayList<BurnDownPoint>();

		// Recuperation des donnees de la base, initialisation des date du chart
		BurnDownRawData rawData = getTasksToProcessAndFillDates(projectPlanId, iterationId, disciplineFunctionalId);

		// No task to process, return empty result
		if (rawData.getTasks().size() <= 0)
		{
			return results;
		}

		// //////////////////////////////////////////////////////////////////////////////////////////
		// Parcours des taches et comptage des jours de charge depenses (total et par date)
		ChargesData chargesData = generateChargeByDate(rawData, onlyTask);

		// Generate and provide burndown chart points
		return genererPoints(chargesData, rawData);
	}

	/**
	 * Recuperation de la liste des taches permettant de generer les points du burndown chart
	 * Recuperation de la date de debut et de la date de fin du chart
	 *
	 * @param projectPlanId
	 * @param iterationId
	 * @param disciplineFunctionalId
	 * @param startDateToFill
	 * @param endDateToFill
	 * @return
	 */
	protected BurnDownRawData getTasksToProcessAndFillDates(final Long projectPlanId, final Long iterationId,
			final String disciplineFunctionalId)
	{
		BurnDownRawData data = new BurnDownRawData();

		Date startDate = null; // date de debut du chart, iteration ou plan projet
		Date endDate = null; // date de fin du chart, iteration ou plan projet
		final IterationManager iterationManager = this.getService(IterationManager.class);
		final TaskManager taskManager = this.getService(TaskManager.class);
		Set<Task> tasks;

		// Initialisation des donnees et recuperation des taches pour un burn down sur le projectPlan
		if (projectPlanId != null && projectPlanId != 0)
		{
			try
			{
				List<Iteration> iterations = iterationManager.getIterationsList(projectPlanId);

				// Recuperation de la date de depart et de fin du plan projet
				for (Iteration iteration : iterations)
				{
					if (startDate == null || startDate.after(iteration.getStartDate()))
					{
						startDate = iteration.getStartDate();
					}
					if (endDate == null || endDate.before(iteration.getEndDate()))
					{
						endDate = iteration.getEndDate();
					}
				}

				// Recuperation des tasks du plan projet
				tasks = taskManager.findByProjectPlanIdAndDisciplineId(projectPlanId, disciplineFunctionalId);

			}
			catch (ManagementModuleException e)
			{
				LOG.error("Erreur lors de la recuperation des iterations du plan projet d'un burnDownChart", e);
				return data;
			}
		}
		// Initialisation des donnees et recuperation des taches pour un burn down sur l'iteration
		else if (iterationId != null && iterationId != 0)
		{
			try
			{
				Iteration iteration = iterationManager.getIteration(iterationId);

				startDate = iteration.getStartDate();
				endDate = iteration.getEndDate();

				// Recuperation des tasks de l'iteration
				tasks = taskManager.getIterationTaskListMergedWithIterationTaskAndDisciplineFunctionalId(iterationId,
						disciplineFunctionalId);
			}
			// Erreur, on renvoie un resultat vide
			catch (ManagementModuleException e)
			{
				LOG.error("Erreur lors de la recuperation de l'iteration d'un burnDownChart", e);
				return data;
			}
		}
		// Ni iteration, ni projectPlan defini, erreur, on ne retourne rien
		else
		{
			LOG.error("Aucun plan projet ni iteration fourni pour la construction du burndownchart");
			return data;
		}

		data.setTasks(tasks);
		data.setStartDate(startDate);
		data.setEndDate(endDate);

		return data;
	}

	/**
	 * Construction de la map des charges par jour, la map est indexee par date en utilisant le format fourni
	 *
	 * @param tasks
	 *          Taches desquelles on extrait les charges
	 * @param rawData
	 *          Date de fin du chart
	 * @param onlyTask
	 *          Si true on aura le nombre de taches par jour , si false la charge par jour
	 * @return
	 */
	protected ChargesData generateChargeByDate(final BurnDownRawData rawData, final boolean onlyTask)
	{
		ChargesData chargesData = new ChargesData(onlyTask);

		for (Task task : rawData.getTasks())
		{
			// Si la tache n'est pas annulee on la compte dans le total
			if (!task.getStatus().getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_CANCELED))
			{
				// Si la tache est terminee dans la periode de l'iteration, on la place dans la map a sa date de fin
				// On met egalement a jour, la date de fin si elle est superieure
				Date taskEndDate = task.getEndDate();
				if (task.getStatus().getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_DONE))
				{
					chargesData.addTask(task, true);
					if (taskEndDate.compareTo(rawData.getEndDate()) > 0)
					{
						rawData.setEndDate(taskEndDate);
					}
				}
				else
				{
					chargesData.addTask(task, false);
				}
			}
		}

		return chargesData;
	}

	/**
	 * Generation des points du burndown chart
	 *
	 * @param chargesData
	 * @param startCalendar
	 * @param endCalendar
	 * @return
	 */
	protected List<BurnDownPoint> genererPoints(final ChargesData chargesData, final BurnDownRawData rawData)
	{
		List<BurnDownPoint> results = new ArrayList<BurnDownPoint>();

		Calendar today = Calendar.getInstance();
		today = DateUtils.truncate(today, Calendar.DAY_OF_MONTH);

		// /////////////////////////////////////////////////////////////////////////////////////////////////////
		// Suppression des heures/minutes/secondes/ms sur date de debut, date de fin
		// EndDate
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(rawData.getEndDate());
		endCalendar = DateUtils.truncate(endCalendar, Calendar.DAY_OF_MONTH);

		// StartDate
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(rawData.getStartDate());
		startCalendar = DateUtils.truncate(startCalendar, Calendar.DAY_OF_MONTH);

		// /////////////////////////////////////////////////////////
		// Creation des points du burn down jour par jour
		Calendar currentCalendar = (Calendar) startCalendar.clone();
		final int incrementParcours = 1;
		float chargeRestante = chargesData.getChargeTotale();

		// Ajout du jour precedent le premier jour (pour toujours afficher le total)
		BurnDownPoint pointDepart = new BurnDownPointImpl();
		currentCalendar.add(Calendar.DATE, -1);
		pointDepart.setDate(currentCalendar.getTime());
		currentCalendar.add(Calendar.DATE, 1);
		pointDepart.setPrevisionnalRemainingLoad(chargeRestante);
		pointDepart.setRealRemainingLoad(chargeRestante);
		results.add(pointDepart);

		// Ajout de l'ensemble des points du burndown
		while (currentCalendar.compareTo(endCalendar) <= 0)
		{
			// Creation du burnDownPoint pour ce jour
			BurnDownPoint point = new BurnDownPointImpl();
			point.setDate(currentCalendar.getTime());
			results.add(point);

			// Tache terminee a cette date ? --> on modifie la chargeTotale restante
			Float charge = chargesData.getCharge(currentCalendar.getTime());
			if (charge != null)
			{
				chargeRestante -= charge;
				point.setRealRemainingLoad(chargeRestante);
			}
			// Date d'aujourd'hui --> fin du graphique
			else if (currentCalendar.compareTo(today) == 0)
			{
				point.setRealRemainingLoad(chargeRestante);
			}

			// Cas du premier jour ? --> remplace par jour precedent le premier
			// if(currentCalendar.compareTo(startCalendar) == 0){
			// point.setPrevisionnalRemainingLoad(chargeRestante);
			// point.setRealRemainingLoad(chargeRestante);
			// }

			// Cas du dernier jour
			if (currentCalendar.compareTo(endCalendar) == 0)
			{
				point.setPrevisionnalRemainingLoad(0f);
				if (today.after(endCalendar))
				{
					point.setRealRemainingLoad(chargeRestante);
				}
			}

			// Increment du parcours (+1 jour)
			currentCalendar.add(Calendar.DATE, incrementParcours);
		}

		return results;
	}

	/**
	 * Conteneur des donnees extraites de la base pour genrer les points du burnDown
	 * 
	 * @author crapart-f
	 */
	private class BurnDownRawData
	{
		private Set<Task> tasks;
		private Date			startDate;
		private Date			endDate;

		public Set<Task> getTasks()
		{
			return tasks;
		}

		public void setTasks(final Set<Task> newTasks)
		{
			tasks = newTasks;
		}

		public Date getStartDate()
		{
			return startDate;
		}

		public void setStartDate(final Date newDate)
		{
			startDate = newDate;
		}

		public Date getEndDate()
		{
			return endDate;
		}

		public void setEndDate(final Date newDate)
		{
			endDate = newDate;
		}
	}

	private class ChargesData
	{
		private boolean						onlyTask	 = false;
		private SimpleDateFormat	 dateFormat = new SimpleDateFormat("yyyyMMdd");
		private Map<String, Float> mapCharges = new HashMap<String, Float>();
		private float							chargeTotale;

		public ChargesData(final boolean onlyTask)
		{
			this.onlyTask = onlyTask;
		}

		public void addTask(final Task task, final boolean done)
		{
			// Comptage par tache
			if (onlyTask)
			{
				chargeTotale++;
			}
			// Comptage en charge
			else
			{
				chargeTotale += task.getInitialEstimation();
			}

			// Si la tache est terminee dans la periode de l'iteration, on la place dans la map a sa date de fin
			if (done)
			{
				String mapKey = dateFormat.format(task.getEndDate());
				Float valueToAdd;
				// Comptage par tache
				if (onlyTask)
				{
					valueToAdd = 1f;
				}
				// Comptage en charge
				else
				{
					valueToAdd = task.getInitialEstimation();
				}

				Float currentValue = mapCharges.get(mapKey);
				if (currentValue == null)
				{
					mapCharges.put(mapKey, valueToAdd);
				}
				else
				{
					mapCharges.put(mapKey, valueToAdd + currentValue);
				}
			}
		}

		public Float getCharge(final Date date)
		{
			String mapKey = dateFormat.format(date);
			return mapCharges.get(mapKey);
		}

		public float getChargeTotale()
		{
			return chargeTotale;
		}
	}
}
