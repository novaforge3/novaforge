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
package org.novaforge.forge.tools.managementmodule.internal.business;

import org.novaforge.forge.tools.managementmodule.business.BusinessObjectFactory;
import org.novaforge.forge.tools.managementmodule.business.IndicatorsManager;
import org.novaforge.forge.tools.managementmodule.business.IterationManager;
import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.business.TaskManager;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.IterationTask;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.User;
import org.novaforge.forge.tools.managementmodule.domain.transfer.GlobalMonitoringIndicators;
import org.novaforge.forge.tools.managementmodule.domain.transfer.IterationTaskIndicators;
import org.novaforge.forge.tools.managementmodule.domain.transfer.MonitoringIndicators;
import org.novaforge.forge.tools.managementmodule.domain.transfer.ScopeUnitIndicators;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.novaforge.forge.tools.managementmodule.internal.utils.Utils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class IndicatorsManagerImpl implements IndicatorsManager
{

	private IterationManager			iterationManager;

	private ProjectPlanManager		projectPlanManager;

	private TaskManager					 taskManager;

	private BusinessObjectFactory businessObjectFactory;

	@Override
	public List<ScopeUnitIndicators> getScopeUnitIndicatorsForIteration(final long iterationId,
			final String disciplineFunctionalId) throws ManagementModuleException
	{
		try
		{
			final List<ScopeUnitIndicators> indicatorsList = new ArrayList<ScopeUnitIndicators>();
			final Iteration iteration = taskManager.getIterationWithFullTaskList(iterationId);
			final Map<ScopeUnit, Set<IterationTask>> taskByScopeUnit = new HashMap<ScopeUnit, Set<IterationTask>>();
			// task assignement to ScopeUnit
			for (final IterationTask iterationTask : iteration.getIterationTasks())
			{
				final Task task = iterationTask.getTask();
				final ScopeUnit scopeUnit = task.getScopeUnit();
				// task of type bug
				if (scopeUnit == null)
				{
					continue;
				}
				// if there is a discipline filter which is not the one of the
				// task
				if (disciplineFunctionalId != null
						&& !disciplineFunctionalId.equals(task.getDiscipline().getFunctionalId()))
				{
					continue;
				}
				// parent management
				final ScopeUnit parentScopeUnit = scopeUnit.getParentScopeUnit();
				if (parentScopeUnit != null)
				{
					if (taskByScopeUnit.get(parentScopeUnit) == null)
					{
						taskByScopeUnit.put(parentScopeUnit, new HashSet<IterationTask>());
					}
					taskByScopeUnit.get(parentScopeUnit).add(iterationTask);
				}
				// first level scope unit management
				if (taskByScopeUnit.get(scopeUnit) == null)
				{
					taskByScopeUnit.put(scopeUnit, new HashSet<IterationTask>());
				}
				taskByScopeUnit.get(scopeUnit).add(iterationTask);
			}
			// indicators calculation
			for (final Entry<ScopeUnit, Set<IterationTask>> entry : taskByScopeUnit.entrySet())
			{
				final ScopeUnitIndicators scopeUnitIndicators = businessObjectFactory
						.getInstanceScopeUnitIndicators();
				scopeUnitIndicators.setScopeUnit(entry.getKey());
				for (final IterationTask iterationTask : entry.getValue())
				{
					final Task task = iterationTask.getTask();
					scopeUnitIndicators.setConsumedTime(scopeUnitIndicators.getConsumedTime()
							+ iterationTask.getConsumedTime());
					// the canceled tasks are only use for consumed time - so we
					// jump to next iteration task
					if (task.getStatus().getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_CANCELED))
					{
						continue;
					}
					scopeUnitIndicators.setRemainingTime(scopeUnitIndicators.getRemainingTime()
							+ iterationTask.getRemainingTime());
					scopeUnitIndicators.setPeriodBeginningEstimation(scopeUnitIndicators.getPeriodBeginningEstimation()
							+ calculatePeriodBeginningEstimation(iterationTask));
				}
				final float reestimate = calculateReestimate(scopeUnitIndicators.getConsumedTime(),
						scopeUnitIndicators.getRemainingTime());
				scopeUnitIndicators.setReestimate(reestimate);
				final float advancement = calculateAdvancement(scopeUnitIndicators.getReestimate(),
						scopeUnitIndicators.getConsumedTime());
				scopeUnitIndicators.setAdvancement(advancement);
				indicatorsList.add(scopeUnitIndicators);
			}
			return indicatorsList;
		}
		catch (ManagementModuleException mmex)
		{
			throw new ManagementModuleException(String.format(
					"Problem while gettting the scope units indicators for [iterationId=%s]", iterationId), mmex);
		}
	}

	@Override
	public ScopeUnitIndicators getScopeUnitIndicatorsInstance()
	{
		return businessObjectFactory.getInstanceScopeUnitIndicators();
	}

	@Override
	public MonitoringIndicators getIterationMonitoringIndicators(final long iterationId,
			final String disciplineFunctionalId) throws ManagementModuleException
	{
		try
		{
			final Iteration iteration = taskManager.getIterationWithFullTaskList(iterationId);
			final MonitoringIndicators monitoringIndicators = businessObjectFactory
					.getInstanceMonitoringIndicators();
			final Set<User> userSet = new HashSet<User>();
			float initialEstimation = 0f;
			float totalRemainingTime = 0f;
			float estimationErrorSum = 0f;
			int nbNonCanceledTask = 0;
			if (iteration != null && iteration.getIterationTasks() != null && !iteration.getIterationTasks().isEmpty())
			{
  			for (final IterationTask iterationTask : iteration.getIterationTasks())
  			{
  				final Task task = iterationTask.getTask();
  				// if there is a discipline filter which is not the one of the
  				// task
  				if (disciplineFunctionalId != null
  						&& !disciplineFunctionalId.equals(task.getDiscipline().getFunctionalId()))
  				{
  					continue;
  				}
  				userSet.add(task.getUser());
  				// initial estimation
  				initialEstimation += task.getInitialEstimation();
  				// consumed
  				monitoringIndicators
  						.setConsumed(monitoringIndicators.getConsumed() + iterationTask.getConsumedTime());
  				// the canceled tasks are only use for consumed and active users
  				// - so we jump to next iteration
  				// task
  				if (task.getStatus().getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_CANCELED))
  				{
  					continue;
  				}
  				// remaining
  				totalRemainingTime += iterationTask.getRemainingTime();
  				// velocity
  				float taskIterationVelocity = calculateIterationTaskVelocity(iterationTask);
  				monitoringIndicators.setVelocity(monitoringIndicators.getVelocity() + taskIterationVelocity);
  				// reestimate
  				float reEstimate = iterationTask.getRemainingTime() + iterationTask.getConsumedTime();
  				// estimation error
  				nbNonCanceledTask++;
  				estimationErrorSum += Math.abs(calculateEstimationError(reEstimate, task.getInitialEstimation()));
  			}
			}
			userSet.remove(null);
			monitoringIndicators.setActiveUsersNumber(userSet.size());
			// estimate
			monitoringIndicators.setEstimate(initialEstimation);
			// remaining
			monitoringIndicators.setRemaining(totalRemainingTime);
			// focalisation
			monitoringIndicators.setFocalisation(calculateFocalisation(monitoringIndicators.getVelocity(),
					monitoringIndicators.getConsumed()));
			// avg estimation
			monitoringIndicators.setAverageEstimationError(calculateAvgEstimationError(estimationErrorSum,
					nbNonCanceledTask));
			// advancement
			float reEstimate = monitoringIndicators.getConsumed() + totalRemainingTime;
			// reestimate
			monitoringIndicators.setReestimate(reEstimate);
			monitoringIndicators
					.setAdvancement(calculateAdvancement(reEstimate, monitoringIndicators.getConsumed()));
			return monitoringIndicators;
		}
		catch (ManagementModuleException mmex)
		{
			throw new ManagementModuleException(String.format("Problem while getting the scope units indicators for [iterationId=%s]",
																												iterationId), mmex);
		}
	}

	@Override
	public List<ScopeUnitIndicators> getScopeUnitIndicators(final Long projectPlanId)
			throws ManagementModuleException
	{

		try
		{
			final List<ScopeUnitIndicators> scopeUnitIndicators = new ArrayList<ScopeUnitIndicators>();
			final Set<ScopeUnit> scopeUnits = new HashSet<ScopeUnit>(
					projectPlanManager.findScopeUnitListByProjectPlanId(projectPlanId));
			final Map<ScopeUnit, Set<Task>> taskByScopeUnit = new HashMap<ScopeUnit, Set<Task>>();
			final Map<String, Boolean> childsFinishedByParent = new HashMap<String, Boolean>();
			// create a map with all scopeUnit and tasks and childs' tasks
			// associate
			for (final ScopeUnit scopeUnit : scopeUnits)
			{
				final Set<Task> tasks = scopeUnit.getTasks();
				// parent management
				final ScopeUnit parentScopeUnit = scopeUnit.getParentScopeUnit();
				if (parentScopeUnit != null)
				{
					if (taskByScopeUnit.get(parentScopeUnit) == null)
					{
						taskByScopeUnit.put(parentScopeUnit, new HashSet<Task>());
					}
					taskByScopeUnit.get(parentScopeUnit).addAll(tasks);
					// about finished childs
					if (childsFinishedByParent.get(parentScopeUnit.getUnitId()) == null)
					{
						if (scopeUnit.isFinished())
						{
							childsFinishedByParent.put(parentScopeUnit.getUnitId(), true);
						}
						else
						{
							childsFinishedByParent.put(parentScopeUnit.getUnitId(), false);
						}
					}
					else if (childsFinishedByParent.get(parentScopeUnit.getUnitId()))
					{
						if (!scopeUnit.isFinished())
						{
							childsFinishedByParent.put(parentScopeUnit.getUnitId(), false);
						}
					}
				}
				// first level scope unit management
				if (taskByScopeUnit.get(scopeUnit) == null)
				{
					taskByScopeUnit.put(scopeUnit, new HashSet<Task>());
				}
				taskByScopeUnit.get(scopeUnit).addAll(tasks);
			}
			// for each scopeUnit of the map
			for (final Entry<ScopeUnit, Set<Task>> e : taskByScopeUnit.entrySet())
			{
				ScopeUnitIndicators scopeUnitIndicator = businessObjectFactory.getInstanceScopeUnitIndicators();
				final ScopeUnit scopeUnit = e.getKey();
				scopeUnitIndicator.setScopeUnit(e.getKey());
				scopeUnitIndicator.setAllTaskfinished(true);
				scopeUnitIndicator.setAllScopeUnitDisciplineFinished(true);
				scopeUnitIndicator.setAllChildFinished(true);
				scopeUnitIndicator.setConsumedTime(0f);
				scopeUnitIndicator.setRemainingTime(0f);
				// remaining scopeUnit
				if (scopeUnit.getEstimation().getRemaining() != null)
				{
					scopeUnitIndicator.setRemainingScopeUnit(scopeUnit.getEstimation().getRemaining());
				}
				// for each associate tasks
				for (Task task : e.getValue())
				{
					task = taskManager.initializeBusinessFieldsOnTask(task);
					scopeUnitIndicator.setConsumedTime(scopeUnitIndicator.getConsumedTime() + task.getConsumedTime());
					// do not care about "canceled" tasks
					if (!task.getStatus().getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_CANCELED))
					{
						scopeUnitIndicator.setRemainingTime(scopeUnitIndicator.getRemainingTime()
								+ task.getRemainingTime());
					}
					// check if all tasks are finished or not
					if (scopeUnitIndicator.isAllTaskfinished()
							&& !task.getStatus().getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_CANCELED)
							&& !task.getStatus().getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_DONE))
					{
						scopeUnitIndicator.setAllTaskfinished(false);
					}
				}
				// get all attached scopeUnitDiscipline
				final List<ScopeUnitDiscipline> scopeUnitDisciplines = projectPlanManager
						.findScopeUnitDisciplinesByScopeUnitId(scopeUnit.getId());
				if (scopeUnitDisciplines != null && scopeUnitDisciplines.size() > 0)
				{
					for (ScopeUnitDiscipline scopeUnitDiscipline : scopeUnitDisciplines)
					{
						// determinate if there is not finished
						// scopeUnitDisciples
						// attached
						if (ManagementModuleConstants.SCOPE_DISCIPLINE_STATUS_IN_PROGRESS.equals(scopeUnitDiscipline
								.getStatus().getFunctionalId()))
						{
							scopeUnitIndicator.setAllScopeUnitDisciplineFinished(false);
							break;
						}
					}
				}
				// about finished childs
				if (childsFinishedByParent.containsKey(scopeUnit.getUnitId()))
				{
					scopeUnitIndicator.setAllChildFinished(childsFinishedByParent.get(scopeUnit.getUnitId()));
				}
				// reestimate
				scopeUnitIndicator.setReestimate(scopeUnitIndicator.getConsumedTime()
						+ scopeUnitIndicator.getRemainingTime());
				// advancement
				if (scopeUnitIndicator.getReestimate() != 0f)
				{
					scopeUnitIndicator.setAdvancement((scopeUnitIndicator.getConsumedTime() / scopeUnitIndicator
							.getReestimate()));
				}
				scopeUnitIndicators.add(scopeUnitIndicator);

			}
			return scopeUnitIndicators;
		}
		catch (ManagementModuleException e)
		{
			throw new ManagementModuleException(MessageFormat.format(
					"Problem while getting scopeUnit's indicators", e));
		}

	}

	@Override
	public IterationTaskIndicators getIterationTaskIndicators(final IterationTask iterationTask)
	{
		IterationTaskIndicators indicators  = businessObjectFactory.getInstanceIterationTaskIndicators();
		float                   reestimate  = calculateReestimate(iterationTask.getConsumedTime(),
																															iterationTask.getRemainingTime());
		float                   advancement = calculateAdvancement(reestimate, iterationTask.getConsumedTime());
		float errorEstimation = calculateEstimationError(reestimate, iterationTask.getTask().getInitialEstimation());
		indicators.setReestimate(reestimate);
		indicators.setAdvancement(advancement);
		indicators.setErrorEstimation(errorEstimation);
		return indicators;
	}

	@Override
	public GlobalMonitoringIndicators getGlobalMonitoringIndicators(final Long projectPlanId)
			throws ManagementModuleException
	{

		GlobalMonitoringIndicators ret = businessObjectFactory.getInstanceGlobalMonitoringIndicators();

		/* project start date */
		List<Iteration> iterations = iterationManager.getIterationsList(projectPlanId);
		Comparator<Iteration> dateComparator = new Comparator<Iteration>()
		{

			@Override
			public int compare(final Iteration o1, final Iteration o2)
			{
				return o1.getStartDate().compareTo(o2.getStartDate());
			}
		};
		if (iterations != null && !iterations.isEmpty())
		{
		  ret.setProjectStartDate(new SimpleDateFormat(Utils.FR_DATE_FORMAT_ONLY_DAY).format(Collections.min(
				iterations, dateComparator).getStartDate()));
		}

		/* indicators */
		Float velocity = 0f;
		Float focalisationFactor = 0f;
		Float estimationError = 0f;
		Integer totalIteration = 0;
    if (iterations != null && !iterations.isEmpty())
    {
  		for (Iteration iteration : iterations)
  		{
  			if (iteration.isFinished())
  			{
  				MonitoringIndicators indicator = getIterationMonitoringIndicators(iteration.getId(), null);
  				totalIteration++;
  				velocity += indicator.getVelocity();
  				focalisationFactor += indicator.getFocalisation();
  				estimationError += indicator.getAverageEstimationError();
  
  			}
  		}
    }
		if (totalIteration > 0)
		{
			velocity /= totalIteration;
			focalisationFactor /= totalIteration;
			estimationError /= totalIteration;
		}
		ret.setVelocity(velocity);
		ret.setFocalisationFactor(focalisationFactor);
		ret.setEstimationError(estimationError);

		/* ideal FP */
		List<ScopeUnit> scopeUnits = projectPlanManager.findScopeUnitListByProjectPlanId(projectPlanId);
		// recreate a scopeUnit list with finished scopeUnits
		List<ScopeUnit> scopeUnits2 = new ArrayList<ScopeUnit>();
		for (ScopeUnit scopeUnit : scopeUnits)
		{
			if (scopeUnit.isFinished())
			{
				scopeUnits2.add(scopeUnit);
			}
		}
		ret.setLastCountFP(projectPlanManager.calculateIdealScopeUnitFP(scopeUnits2, projectPlanManager
				.getProjectPlan(projectPlanId).getProject().getProjectId()));

		return ret;
	}

	/**
	 * Get the velocity of a task for an iteration
	 *
	 * @param iterationTask
	 *          the iteration task
	 * @return the velocity
	 */
	private float calculateIterationTaskVelocity(final IterationTask iterationTask)
	{
		return calculatePeriodBeginningEstimation(iterationTask) - iterationTask.getRemainingTime();
	}

	/**
	 * Get the focalisation using the velocity and the consumed
	 *
	 * @param velocity
	 *          the velocity of an iteration for 1 or many tasks
	 * @param consumed
	 *          the consumed time for 1 or many tasks
	 * @return the focalisation
	 */
	private float calculateFocalisation(final float velocity, final float consumed)
	{
		if (consumed != 0)
		{
			return velocity / consumed;
		}
		else
		{
			return 0F;
		}
	}

	/**
	 * Calculate the average estimation error using the sum of the error
	 * (absolute value without canceled task) and the number of non cancled task
	 *
	 * @param estimationErrorSum
	 *          the estimation error sum (absolute value without canceled
	 *          task)
	 * @param nbNonCanceledTask
	 *          the number of non canceled task
	 * @return the average estimation error
	 */
	private float calculateAvgEstimationError(final float estimationErrorSum, final int nbNonCanceledTask)
	{
		if (nbNonCanceledTask != 0)
		{
			return estimationErrorSum / nbNonCanceledTask;
		}
		else
		{
			return 0F;
		}
	}

	/**
	 * Get the estimation error of a task or an iteration
	 *
	 * @param reEstimate
	 *          the reestimate
	 * @param initialEstimation
	 *          the initial estimation
	 * @return the estimation error
	 */
	private float calculateEstimationError(final float reEstimate, final float initialEstimation)
	{
		if (initialEstimation != 0F)
		{
			return ((reEstimate / initialEstimation) - 1) * 100;
		}
		else
		{
			if (reEstimate == 0F)
			{
				return 0F;
			}
			else
			{
				return 100F;
			}
		}
	}

	/**
	 * Get the beginning estimation value for a task and an iteration
	 *
	 * @param iterationTask
	 *          the iterationTask
	 * @return the estimation
	 */
	private float calculatePeriodBeginningEstimation(final IterationTask iterationTask)
	{
		final IterationTask previousIterationTask = getPreviousIterationTask(iterationTask);
		if (previousIterationTask != null)
		{
			return previousIterationTask.getRemainingTime();
		}
		else
		{
			return iterationTask.getTask().getInitialEstimation();
		}
	}

	/**
	 * Calculate the reestimate using the consumed and the remaining time
	 *
	 * @param consumed
	 *          the consumed time on a task/iteration
	 * @param remaining
	 *          the remaining time to finish task / iteration
	 * @return the restimate
	 */
	private float calculateReestimate(final float consumed, final float remaining)
	{
		return consumed + remaining;
	}

	/**
	 * Calculate the advancement of a task, an iteration or a project using its
	 * reestimate and its consumed
	 *
	 * @param reestimate
	 *     the reestimate total time
	 * @param consumed
	 *     the consumed time
	 *
	 * @return the calculated advancement
	 */
	private float calculateAdvancement(final float reestimate, final float consumed)
	{
		if (reestimate == 0f)
		{
			return 100f;
		}
		else
		{
			return consumed * 100 / reestimate;
		}
	}

	/**
	 * Get the previous IterationTask (or null if the argument is the first
	 * IterationTask)
	 *
	 * @return The previous IterationTask (or null if the argument is the first
	 * IterationTask)
	 */
	public IterationTask getPreviousIterationTask(final IterationTask iterationTask)
	{
		IterationTask previousIterationTask = null;
		final Date    iterationStartDate    = iterationTask.getIteration().getStartDate();
		for (final IterationTask tmpIterationTask : iterationTask.getTask().getIterationsTasks())
		{
			final Date tmpStartDate = tmpIterationTask.getIteration().getStartDate();
			if (tmpStartDate.before(iterationStartDate) && (previousIterationTask == null || tmpStartDate
																																													 .after(previousIterationTask
																																																			.getIteration()
																																																			.getStartDate())))
			{
				previousIterationTask = tmpIterationTask;
			}
		}
		return previousIterationTask;
	}

	public void setIterationManager(final IterationManager pIterationManager)
	{
		iterationManager = pIterationManager;
	}

	public void setProjectPlanManager(final ProjectPlanManager pProjectPlanManager)
	{
		projectPlanManager = pProjectPlanManager;
	}

	public void setTaskManager(final TaskManager pTaskManager)
	{
		taskManager = pTaskManager;
	}

	public void setBusinessObjectFactory(final BusinessObjectFactory pBusinessObjectFactory)
	{
		businessObjectFactory = pBusinessObjectFactory;
	}

}
