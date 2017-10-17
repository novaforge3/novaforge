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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.novaforge.forge.tools.managementmodule.business.BusinessObjectFactory;
import org.novaforge.forge.tools.managementmodule.business.IterationManager;
import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.business.ReferentielManager;
import org.novaforge.forge.tools.managementmodule.business.TaskManager;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.dao.IterationDAO;
import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.PhaseType;
import org.novaforge.forge.tools.managementmodule.domain.report.AdvancementIndicators;
import org.novaforge.forge.tools.managementmodule.exceptions.ExceptionCode;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.novaforge.forge.tools.managementmodule.internal.utils.Utils;
import org.novaforge.forge.tools.managementmodule.report.model.impl.AdvancementIndicatorsImpl;
import org.novaforge.forge.tools.managementmodule.services.Util;

public class IterationManagerImpl implements IterationManager
{
  private ProjectPlanManager    projectPlanManager;

  private IterationDAO          iterationDAO;

  private ReferentielManager    referentielManager;

  private TaskManager           taskManager;

  private BusinessObjectFactory businessObjectFactory;

  @Override
  public Set<Lot> getsLotForNextIteration(final long projectPlanId) throws ManagementModuleException
  {
    final Set<Lot> lotsWichCanBeTaken = new HashSet<Lot>();
    final List<Lot> allLotsFromProjectPlan = projectPlanManager.getCompleteListLots(projectPlanId);
    for (final Lot lot : allLotsFromProjectPlan)
    {
      lotCanHaveMoreIterationByLot(lot,lotsWichCanBeTaken);
    }
    return lotsWichCanBeTaken;
  }
  
  private void lotCanHaveMoreIterationByLot (Lot lot, Set<Lot> lotsWichCanBeTaken){
    if (lotCanHaveMoreIteration(lot))
    {
      lotsWichCanBeTaken.add(lot);
    }
    for (final Lot childLot : lot.getChildLots()){
      lotCanHaveMoreIterationByLot(childLot, lotsWichCanBeTaken);
    };
  }

  /**
   * This methods indicate if a lot can have more iterations
   * 
   * @param lot
   *          the lot
   * @return true if the lot can have more iterations, false otherwise
   */
  private boolean lotCanHaveMoreIteration(final Lot lot)
  {
    Iteration newestIteration = null;
    // if a lot has child it can't have iterations
    if (!lot.getChildLots().isEmpty())
    {
      return false;
    }
    // if a lot is completely in past it can't have iterations
    if (lot.getEndDate().before(new Date()))
    {
      return false;
    }

    for (final Iteration iteration : lot.getIterations())
    {
      if (newestIteration == null || iteration.getEndDate().before(newestIteration.getEndDate()))
      {
        newestIteration = iteration;
      }
    }
    return true;
  }

  @Override
  public Iteration newIteration()
  {
    return businessObjectFactory.getInstanceIteration();
  }

  @Override
  public Iteration creeteIteration(final Iteration iteration) throws ManagementModuleException
  {
    validateIteration(iteration);
    return iterationDAO.save(iteration);
  }

  @Override
  public List<Iteration> getIterationsList(final Long projectPlanId) throws ManagementModuleException
  {
    return iterationDAO.getIterationsList(projectPlanId);
  }

  @Override
  public List<Iteration> getFinishedAndCurrentIterationList(final Long projectPlanId)
      throws ManagementModuleException
  {
    final List<Iteration> iterations = getIterationsList(projectPlanId);
    for (final Iteration iteration : new ArrayList<Iteration>(iterations))
    {
      if (!(iteration.isFinished() || iteration.getStartDate().before(new Date())))
      {
        iterations.remove(iteration);
      }
    }
    return iterations;
  }

  @Override
  public Iteration getIteration(final long iterationId) throws ManagementModuleException
  {
    return this.iterationDAO.findById(iterationId);
  }

  @Override
  public Iteration updateIteration(final Iteration iteration) throws ManagementModuleException
  {
    validateIteration(iteration);
    return this.iterationDAO.merge(iteration);
  }

  @Override
  public boolean deleteIteration(final long iterationId) throws ManagementModuleException
  {
    final Iteration iteration = getIteration(iterationId);
    checkIfIterationCanBeDeleted(iteration);
    iterationDAO.delete(iteration);
    return true;
  }

  @Override
  public Set<PhaseType> getPhasesTypesForNextIteration(final Iteration currentIteration)
      throws ManagementModuleException
  {
    // linkedhashset to maintain an order
    final Set<PhaseType> phasesTypesToReturn = new LinkedHashSet<PhaseType>();
    final Map<String, PhaseType> mapPhasesTypesByFunctionalId = new HashMap<String, PhaseType>();
    for (final PhaseType phaseType : referentielManager.getAllPhaseTypes())
    {
      mapPhasesTypesByFunctionalId.put(phaseType.getFunctionalId(), phaseType);
    }
    // if first iteration
    if (currentIteration == null || currentIteration.getNumIteration() == 0
        || currentIteration.getPhaseType() == null)
    {
      phasesTypesToReturn.add(mapPhasesTypesByFunctionalId.get(ManagementModuleConstants.PHASE_TYPE_FRAMING));
    }
    else
    {
      // if there is a previous iteration, in first choice we always have
      // the same phaseType than the previous iteration
      phasesTypesToReturn.add(mapPhasesTypesByFunctionalId.get(currentIteration.getPhaseType()
          .getFunctionalId()));
      // next phase type management
      if (currentIteration.getPhaseType().getFunctionalId()
          .equals(ManagementModuleConstants.PHASE_TYPE_FRAMING))
      {
        phasesTypesToReturn.add(mapPhasesTypesByFunctionalId
            .get(ManagementModuleConstants.PHASE_TYPE_PLANNING));
      }
      else if (currentIteration.getPhaseType().getFunctionalId()
          .equals(ManagementModuleConstants.PHASE_TYPE_PLANNING))
      {
        phasesTypesToReturn.add(mapPhasesTypesByFunctionalId
            .get(ManagementModuleConstants.PHASE_TYPE_CONSTRUCTION));
      }
      else if (currentIteration.getPhaseType().getFunctionalId()
          .equals(ManagementModuleConstants.PHASE_TYPE_CONSTRUCTION))
      {
        phasesTypesToReturn.add(mapPhasesTypesByFunctionalId
            .get(ManagementModuleConstants.PHASE_TYPE_TRANSITION));
      }
      else if (currentIteration.getPhaseType().getFunctionalId()
          .equals(ManagementModuleConstants.PHASE_TYPE_TRANSITION))
      {
        phasesTypesToReturn.add(mapPhasesTypesByFunctionalId
            .get(ManagementModuleConstants.PHASE_TYPE_FRAMING));
      }
    }
    // always "none" but in last choice
    phasesTypesToReturn.add(mapPhasesTypesByFunctionalId.get(ManagementModuleConstants.PHASE_TYPE_NONE));
    return phasesTypesToReturn;
  }

  @Override
  public Iteration getNextIteration(final Long projectPlanId) throws ManagementModuleException
  {
    final List<Iteration> iterations = getIterationsList(projectPlanId);
    Iteration ret = null;
    for (final Iteration it : new ArrayList<Iteration>(iterations))
    {
      if (it.getStartDate().before(new Date()))
      {
        iterations.remove(it);
      }
    }
    if (iterations != null && iterations.size() > 0)
    {
      if (iterations.size() >= 2)
      {
        ret = Collections.min(iterations);
      }
      else if (iterations.size() == 1)
      {
        ret = iterations.get(0);
      }
    }
    return ret;
  }

  @Override
  public Iteration getCurrentIteration(final Long pProjectPlanId) throws ManagementModuleException
  {
    final List<Iteration> iterations = getCurrentIterationList(pProjectPlanId);
    Iteration ret = null;
    if (iterations != null && iterations.size() > 0)
    {
      if (iterations.size() >= 2)
      {
        ret = Collections.min(iterations);
      }
      else if (iterations.size() == 1)
      {
        ret = iterations.get(0);
      }
    }
    return ret;
  }

  @Override
  public List<Iteration> getCurrentIterationList(final Long projectPlanId) throws ManagementModuleException
  {
    final List<Iteration> iterations = getIterationsList(projectPlanId);
    for (final Iteration iteration : new ArrayList<Iteration>(iterations))
    {
      if (iteration.isFinished() || !iteration.getStartDate().before(new Date()))
      {
        iterations.remove(iteration);
      }
    }
    return iterations;
  }

  @Override
  @Deprecated
  /**
   * TODO JCB delete this and iterationDAORemote attached SQL request
   * use IndicatorsManagerImpl.getIterationMonitoringIndicators(iterationId, null) instead
   */
  public AdvancementIndicators getAdvancementIndicators(final Long iterationId)
      throws ManagementModuleException
  {
    final Long statusId = taskManager.getStatusTask(ManagementModuleConstants.TASK_STATUS_DONE).getId();
    final Object[] indicators = iterationDAO.getAdvancementIndicators(iterationId, statusId);

    final AdvancementIndicators advancementIndicators = new AdvancementIndicatorsImpl();
    if (!(indicators[0] == null || indicators[1] == null || indicators[2] == null))
    {
      final Float estimate = new Float((Double) indicators[0]);
      final Float consumed = new Float((Double) indicators[1]);
      final Float reestimate = new Float((Double) indicators[2]);
      final Float remaining = reestimate - consumed;
      Float focalisationFactor = 0f;
      if (consumed != 0)
      {
        focalisationFactor = estimate / consumed;
      }
      Float estimationError = 0f;
      if (estimate != 0)
      {
        // FIXME Laura formule
        estimationError = (reestimate / estimate - 1) * 100;
      }
      // TODO JCB revoir requete indicator
      advancementIndicators.setEstimate(Utils.floatFormat(estimate, 2));
      advancementIndicators.setConsumed(Utils.floatFormat(consumed, 2));
      advancementIndicators.setReestimate(Utils.floatFormat(reestimate, 2));
      advancementIndicators.setRemaining(Utils.floatFormat(remaining, 2));
      advancementIndicators.setFocalisationFactor(Utils.floatFormat(focalisationFactor, 2));
      advancementIndicators.setEstimationError(Utils.floatFormat(estimationError, 2));
    }
    return advancementIndicators;
  }

  @Override
  public Iteration getLastFinishedIteration(final long projectPlanId) throws ManagementModuleException
  {
    Iteration iterationToReturn = null;
    final List<Iteration> iterations = getIterationsList(projectPlanId);
    final List<Iteration> finishedIterationList = new ArrayList<Iteration>();
    for (final Iteration iteration : iterations)
    {
      if (iteration.isFinished())
      {
        finishedIterationList.add(iteration);
      }
    }
    if (!finishedIterationList.isEmpty())
    {
      iterationToReturn = Collections.max(finishedIterationList);
    }
    return iterationToReturn;
  }

  @Override
  public String getIterationsMetadatas(final Long iterationId, final String disciplineFunctionalId)
      throws ManagementModuleException
  {
    try
    {
      Iteration iteration = null;
      Lot lot = null;
      Lot lotParent = null;
      Discipline discipline = null;
      if (iterationId != null && iterationId != 0)
      {
        // find iteration
        iteration = getIteration(iterationId);
        // find Lot
        lot = iteration.getLot();
        if (lot.getParentLot() != null)
        {
          lotParent = lot.getParentLot();
        }
      }
      if (disciplineFunctionalId != null && !disciplineFunctionalId.equalsIgnoreCase("all"))
      {
        discipline = referentielManager.getDiscipline(disciplineFunctionalId);
      }

      return Util.generateMetadatas(discipline, iteration, true, lot, true, lotParent, true);
    }
    catch (final ManagementModuleException e)
    {
      throw new ManagementModuleException("Erreur lors de la recuperation des metadatas for iterationId : "
          + iterationId + " and functionalId : " + disciplineFunctionalId, e);
    }
  }

  /**
   * This method checks if the iteration can be deleted
   * 
   * @param iterationToCheck
   *          the iteration
   */
  private void checkIfIterationCanBeDeleted(final Iteration iterationToCheck)
      throws ManagementModuleException
  {
    // only the last iteration can be deleted
    final List<Iteration> fullIterationList = getIterationsList(iterationToCheck.getLot().getProjectPlan()
        .getId());
    for (final Iteration iteration : fullIterationList)
    {
      if (!Objects.equals(iteration.getId(), iterationToCheck.getId())
          && iteration.getEndDate().after(iterationToCheck.getEndDate()))
      {
        throw new ManagementModuleException(ExceptionCode.ERR_ITERATION_NO_DELETE_IF_NOT_LAST);
      }
    }
    // only a iteration without tasks can be deleted
    if (!iterationToCheck.getIterationTasks().isEmpty())
    {
      throw new ManagementModuleException(ExceptionCode.ERR_ITERATION_NO_DELETE_IF_HAS_TASKS);
    }
  }

  /**
   * Method which validate an iteration
   * 
   * @param iterationToValidate
   *          the iteration to validate
   * @throws ManagementModuleException
   *           exception during validation
   */
  private void validateIteration(final Iteration iterationToValidate) throws ManagementModuleException
  {
    // REQUIREMENT NVF_FCT_50T1203 : end date must not be in past
    if (iterationToValidate.getEndDate().before(new Date()))
    {
      throw new ManagementModuleException(ExceptionCode.ERR_ITERATION_IN_PAST);
    }
    // REQUIREMENT NVF_FCT_50T1206 : required/not empty controls
    if (iterationToValidate.getLot() == null)
    {
      throw new ManagementModuleException(ExceptionCode.ERR_ITERATION_REQUIRED_LOT);
    }
    if (iterationToValidate.getPhaseType() == null)
    {
      throw new ManagementModuleException(ExceptionCode.ERR_ITERATION_REQUIRED_PHASE_TYPE);
    }
    if (iterationToValidate.getStartDate() == null)
    {
      throw new ManagementModuleException(ExceptionCode.ERR_ITERATION_REQUIRED_START_DATE);
    }
    if (iterationToValidate.getEndDate() == null)
    {
      throw new ManagementModuleException(ExceptionCode.ERR_ITERATION_REQUIRED_END_DATE);
    }

    if (iterationToValidate.getLot() != null && iterationToValidate.getLot().getProjectPlan() != null)
    {
      // REQUIREMENT NVF_FCT_50T1208 no overlap between iterations
      final List<Iteration> fullIterationList = getIterationsList(iterationToValidate.getLot()
          .getProjectPlan().getId());
      for (final Iteration iteration : fullIterationList)
      {
        if (iteration.getId().equals(iterationToValidate.getId()))
        {
          continue;
        }
        if ((iterationToValidate.getEndDate().before(iteration.getStartDate()) || iterationToValidate
            .getStartDate().after(iteration.getEndDate())) == false)
        {
          throw new ManagementModuleException(ExceptionCode.ERR_ITERATION_NO_OVERLAP);
        }
      }
      // must completely be contains in his lot
      final Lot lot = iterationToValidate.getLot();
      if (iterationToValidate.getStartDate().before(lot.getStartDate())
          || iterationToValidate.getEndDate().after(lot.getEndDate()))
      {
        throw new ManagementModuleException(ExceptionCode.ERR_ITERATION_NOT_CONTAINS_IN_LOT);
      }
    }
  }

  public void setProjectPlanManager(final ProjectPlanManager pProjectPlanManager)
  {
    projectPlanManager = pProjectPlanManager;
  }

  public void setIterationDAO(final IterationDAO pIterationDAO)
  {
    iterationDAO = pIterationDAO;
  }

  public void setReferentielManager(final ReferentielManager pReferentielManager)
  {
    referentielManager = pReferentielManager;
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
