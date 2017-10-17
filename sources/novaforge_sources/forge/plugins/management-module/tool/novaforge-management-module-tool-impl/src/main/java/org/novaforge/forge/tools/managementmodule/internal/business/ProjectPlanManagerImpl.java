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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.managementmodule.business.BusinessObjectFactory;
import org.novaforge.forge.tools.managementmodule.business.ManagementModuleManager;
import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.business.ReferentielManager;
import org.novaforge.forge.tools.managementmodule.business.TaskManager;
import org.novaforge.forge.tools.managementmodule.connectors.CDOPerimeterUnitConnector;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.dao.AdjustWeightDAO;
import org.novaforge.forge.tools.managementmodule.dao.CDOParametersDAO;
import org.novaforge.forge.tools.managementmodule.dao.EstimationComponentDetailDAO;
import org.novaforge.forge.tools.managementmodule.dao.EstimationDAO;
import org.novaforge.forge.tools.managementmodule.dao.LoadDistributionDisciplineDAO;
import org.novaforge.forge.tools.managementmodule.dao.LotDAO;
import org.novaforge.forge.tools.managementmodule.dao.MarkerDAO;
import org.novaforge.forge.tools.managementmodule.dao.ProjectPlanDAO;
import org.novaforge.forge.tools.managementmodule.dao.RefCalendarDAO;
import org.novaforge.forge.tools.managementmodule.dao.RefScopeUnitDAO;
import org.novaforge.forge.tools.managementmodule.dao.ScopeUnitDAO;
import org.novaforge.forge.tools.managementmodule.dao.ScopeUnitDisciplineDAO;
import org.novaforge.forge.tools.managementmodule.dao.ScopeUnitDisciplineStatusDAO;
import org.novaforge.forge.tools.managementmodule.dao.StatusProjectPlanDAO;
import org.novaforge.forge.tools.managementmodule.domain.AdjustFactor;
import org.novaforge.forge.tools.managementmodule.domain.AdjustFactorJointure;
import org.novaforge.forge.tools.managementmodule.domain.AdjustWeight;
import org.novaforge.forge.tools.managementmodule.domain.CDOParameters;
import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.Estimation;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentDetail;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentSimple;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.IterationTask;
import org.novaforge.forge.tools.managementmodule.domain.LoadDistributionDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.Marker;
import org.novaforge.forge.tools.managementmodule.domain.MarkerType;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.RefCalendar;
import org.novaforge.forge.tools.managementmodule.domain.RefScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.ScopeType;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDisciplineStatus;
import org.novaforge.forge.tools.managementmodule.domain.StatusProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.StatusScope;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.UnitTime;
import org.novaforge.forge.tools.managementmodule.domain.report.DayLoad;
import org.novaforge.forge.tools.managementmodule.domain.report.GanttLotInfo;
import org.novaforge.forge.tools.managementmodule.exceptions.CDOPerimeterUnitConnectorException;
import org.novaforge.forge.tools.managementmodule.exceptions.ExceptionCode;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.novaforge.forge.tools.managementmodule.internal.utils.Utils;
import org.novaforge.forge.tools.managementmodule.services.Util;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ProjectPlanManagerImpl implements ProjectPlanManager {
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String VERSION = "Version";
	private static final String CREATED_ON = "CreatedON";
	private static final String MODIFIED_ON = "ModifiedON";
	private static final String EMPTY_STRING = "";
	private static final String BENEFITS_LEVEL = "BenefitsLevel";
	private static final String DRAWBACKS_LEVEL = "DrawbacksLevel";
	private static final String RISK_LEVEL = "RiskLevel";
	private static final String DESCRIPTION = "description";
	private static final String JOB = "_Job";
	private static final String _TRIGGER = "_trigger";
	private static final String INITIAL_LOT_DESCRIPTION = "Lot initial";
	private static final String INITIAL_LOT_NAME = "Lot 1";
	private static final Integer DEFAULT_FP_VALUE = 0;
	private static final Integer DEFAULT_PRIORIZATION_VALUE = 1;
	private static final String DEFAULT_SIMPLE_VALUE = "NONE";
	private static final Log LOG = LogFactory.getLog(ProjectPlanManagerImpl.class);
	private ProjectPlanDAO projectPlanDAO;
	private LotDAO lotDAO;
	private MarkerDAO markerDAO;
	private EstimationComponentDetailDAO estimationComponentDetailDAO;
	private ScopeUnitDAO scopeUnitDAO;
	private RefScopeUnitDAO refScopeUnitDAO;
	private AdjustWeightDAO adjustWeightDAO;
	private LoadDistributionDisciplineDAO loadDistributionDisciplineDAO;
	private EstimationDAO estimationDAO;
	private RefCalendarDAO refCalendarDAO;
	private StatusProjectPlanDAO statusProjectPlanDAO;
	private ScopeUnitDisciplineDAO scopeUnitDisciplineDAO;
	private ScopeUnitDisciplineStatusDAO scopeUnitDisciplineStatusDAO;
	private CDOParametersDAO CDOParametersDAO;
	private ManagementModuleManager managementModuleManager;
	private ReferentielManager referentielManager;
	private TaskManager taskManager;
	private CDOPerimeterUnitConnector connectorCDO;
	private Scheduler sched = null;
	private SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
	private BusinessObjectFactory businessObjectFactory;

	/************************* ProjectPlan ********************************************************************/

	@Override
	public ProjectPlan newProjectPlan() {
		return businessObjectFactory.getInstanceProjectPlan();
	}

	@Override
	public ProjectPlan creeteProjectPlan(final ProjectPlan projectPlan) throws ManagementModuleException {
		return projectPlanDAO.save(projectPlan);
	}

	@Override
	public ProjectPlan creeteProjectPlan(final String pProjectId) throws ManagementModuleException {

		ProjectPlan projectPlan = null;

		if (!this.managementModuleManager.existProjectId(pProjectId)) {
			LOG.error(ExceptionCode.ERR_MEMBERSHIP_PROJECT_DOESNT_EXIST.toString());
			throw new ManagementModuleException(
					String.format("Project doesn't exists with [projectId=%s]", pProjectId));
		}

		final Project project = this.managementModuleManager.getFullProject(pProjectId);

		final ProjectPlan draftVersion = getProjectPlanDraftVersion(pProjectId);
		// check if a draft version of the project plan exist
		if (draftVersion != null) {
			deleteProjectPlan(draftVersion.getId());
		}
		final Long maxVersion = findMaxNumVersion(pProjectId);
		if (maxVersion == null || maxVersion == 0) {
			projectPlan = newProjectPlan();
			projectPlan.setProject(project);
			final Calendar gc = GregorianCalendar.getInstance();
			final Date createDate = gc.getTime();
			projectPlan.setDate(createDate);
			projectPlan.setVersion(ManagementModuleConstants.PROJECT_PLAN_VERSION_DEFAULT);
			// On ne set pas l'unite de temps pour avoir une popup de choix au
			// lancement de l'ecran de gestion du project plan
			final UnitTime uTime = referentielManager
					.getUnitTimeByIdFunctional(ManagementModuleConstants.UNIT_TIME_WEEK);
			if (uTime == null) {
				throw new ManagementModuleException(ExceptionCode.ERR_UNITTIME_DOESNT_EXIST);
			}
			projectPlan.setStatus(referentielManager
					.getStatusProjectPlanByFunctionalId(ManagementModuleConstants.PROJECT_PLAN_DRAFT));

			// put the estimationComponentDetail
			final EstimationComponentDetail estimationComponentDetail = (EstimationComponentDetail) getDefaultEstimationComponentDetail()
					.clone();
			estimationComponentDetail.setFunctionalId(generateEstimationComponentDetailFunctionalId(projectPlan));
			projectPlan.setEstimationComponentDetail(estimationComponentDetail);
			// AdjustFactorJointure management
			final List<AdjustFactorJointure> listAdjustFactorJointureProjetPlan = getDefaultAdjustfactorJointureProjectPlan();
			for (final AdjustFactorJointure adjustFactorJointure : listAdjustFactorJointureProjetPlan) {
				adjustFactorJointure.setProjectPlan(projectPlan);
			}
			projectPlan.setAdjustFactorJointure(new HashSet<AdjustFactorJointure>(listAdjustFactorJointureProjetPlan));

			projectPlan = creeteProjectPlan(projectPlan);

			gc.add(Calendar.YEAR, 1);
			final Date endDate = gc.getTime();

			// create also the initial lot
			projectPlan.addLot(initializeNewLot(projectPlan, INITIAL_LOT_NAME, createDate, endDate,
					INITIAL_LOT_DESCRIPTION, null));
			updateProjectPlan(projectPlan);
		} else {
			final ProjectPlan projectPlanSource = getProjectPlan(pProjectId, maxVersion.intValue());
			if (projectPlanSource == null) {
				LOG.error(ExceptionCode.ERR_PROJECT_PLAN_DOESNT_EXIT.toString());
				throw new ManagementModuleException("Can't find project plan source with [projectId=" + pProjectId
						+ "] and [maxVersion=" + maxVersion.intValue() + "]");
			}
			final ProjectPlan duplicatedProjectPlan = duplicateProjectPlan(projectPlanSource);
			// cause of OneToOne not correctly manage by jpa in cascading we
			// save
			// without estimation and add them after
			final Map<String, Estimation> estimationByScopeUnitFunctionalIdMap = new HashMap<String, Estimation>();
			for (final Lot lot : duplicatedProjectPlan.getLots()) {
				if (lot.getParentLot() != null) {
					continue;
				}
				fillEstimationMapByLot(projectPlanSource, estimationByScopeUnitFunctionalIdMap, lot,
						duplicatedProjectPlan);
			}

			projectPlan = creeteProjectPlan(duplicatedProjectPlan);

			final List<LoadDistributionDiscipline> ldds = getLoadDistributionDisciplineList(projectPlanSource.getId());
			final List<LoadDistributionDiscipline> newLdds = new ArrayList<LoadDistributionDiscipline>();
			for (final LoadDistributionDiscipline ldd : ldds) {
				newLdds.add(duplicateLoadDistributionDiscipline(ldd, projectPlan));
			}

			creeteLoadDistributionDiscipline(newLdds);

			// we add the estimations to the persistent scope unit
			for (final Lot lot : projectPlan.getLots()) {
				if (lot.getParentLot() != null) {
					continue;
				}
				loadEstimationMapByLot(estimationByScopeUnitFunctionalIdMap, lot);
			}

			projectPlan = updateProjectPlan(projectPlan);
		}
		return projectPlan;
	}

	private void fillEstimationMapByLot(ProjectPlan projectPlanSource,
			Map<String, Estimation> estimationByScopeUnitFunctionalIdMap, Lot lot, ProjectPlan duplicatedProjectPlan) {
		fillEstimationMap(projectPlanSource, estimationByScopeUnitFunctionalIdMap, lot, duplicatedProjectPlan);
		if (lot.getChildLots() != null && lot.getChildLots().size() > 0) {
			for (final Lot sousLot : lot.getChildLots()) {
				fillEstimationMapByLot(projectPlanSource, estimationByScopeUnitFunctionalIdMap, sousLot,
						duplicatedProjectPlan);
			}
		}
	}

	private void loadEstimationMapByLot(Map<String, Estimation> estimationByScopeUnitFunctionalIdMap, Lot lot) {
		loadEstimationMap(estimationByScopeUnitFunctionalIdMap, lot);
		if (lot.getChildLots() != null && lot.getChildLots().size() > 0) {
			for (final Lot sousLot : lot.getChildLots()) {
				loadEstimationMapByLot(estimationByScopeUnitFunctionalIdMap, sousLot);
			}
		}
	}

	@Override
	public ProjectPlan updateProjectPlan(final ProjectPlan projectPlan) throws ManagementModuleException {
		// update estimation list with new projectPlan settings
		final List<Estimation> estimations = getEstimations(projectPlan.getId());
		for (final Estimation estimation : estimations) {
			updateEstimation(estimation);
		}
		return projectPlanDAO.merge(projectPlan);
	}

	@Override
	public boolean deleteProjectPlan(final Long projectPlanId) throws ManagementModuleException {
		final ProjectPlan projectPlan = projectPlanDAO.findById(projectPlanId);
		// control that it's the newest PP of its project

		final Long maxVersion = findMaxNumVersion(projectPlan.getProject().getProjectId());

		if (maxVersion > projectPlan.getVersion()) {
			throw new ManagementModuleException(ExceptionCode.ERR_PROJECT_PLAN_DELETE_WRONG_VERSION);
		}

		// 1) Suppression of datas which doesnt have cascading
		// LoadDistributionDisciplines
		deleteLoadDistributionDisciplinesByProjectPlanId(projectPlan.getId());
		// 2) Suppression without cascading of big tables for performance
		taskManager.deleteIterationTaskByProjectPlanId(projectPlan.getId());
		taskManager.deleteTasksByProjectPlanId(projectPlan.getId());
		deleteScopeUnitsByProjectPlanId(projectPlan.getId());
		// 3) delete the project plan (jpa cascading will do the last
		// deletions)
		projectPlanDAO.delete(projectPlan);
		return true;
	}

	@Override
	public ProjectPlan getProjectPlan(final Long projectPlanId) throws ManagementModuleException {
		return projectPlanDAO.findById(projectPlanId);
	}

	@Override
	public ProjectPlan getProjectPlan(final String pProjectId, final int pVersion) throws ManagementModuleException {
		return projectPlanDAO.findByProjectIdAndVersion(pProjectId, pVersion);
	}

	@Override
	public ProjectPlan getProjectPlanDraftVersion(final String pProjectId) throws ManagementModuleException {
		return projectPlanDAO.findDraftByProjectId(pProjectId);
	}

	@Override
	public List<ProjectPlan> getProjectPlanList(final String pProjectId) throws ManagementModuleException {
		final List<ProjectPlan> retour = new ArrayList<ProjectPlan>();
		final List<ProjectPlan> temp = projectPlanDAO.findByProjectId(pProjectId);
		if (temp != null) {
			retour.addAll(temp);
		}
		return retour;
	}

	@Override
	public Long findMaxNumVersion(final String pProjectId) throws ManagementModuleException {
		return projectPlanDAO.findMaxNumVersion(pProjectId);
	}

	@Override
	public ProjectPlan getLastProjectPlan(final String pProjectId) throws ManagementModuleException {
		ProjectPlan projectPlan = getProjectPlanDraftVersion(pProjectId);
		if (projectPlan == null) {
			LOG.debug("getLastProjectPlan pProjectId=" + pProjectId);
			final int version = findMaxNumVersion(pProjectId).intValue();
			LOG.debug("findMaxNumVersion(pProjectId) = " + version);
			projectPlan = getProjectPlan(pProjectId, version);
		}
		return projectPlan;
	}

	@Override
	public ProjectPlan getLastValidatedProjectPlan(final String pProjectId) throws ManagementModuleException {
		return projectPlanDAO.findLastValidated(pProjectId);
	}

	@Override
	public ProjectPlan validateProjectPlan(final String pProjectId) throws ManagementModuleException {

		final ProjectPlan projectPlan = getProjectPlanDraftVersion(pProjectId);

		if (projectPlan == null) {
			throw new ManagementModuleException(
					"there is no draft version of project plan for projectID = " + pProjectId);
		}

		final StatusProjectPlan validateStatus = referentielManager
				.getStatusProjectPlanByFunctionalId(ManagementModuleConstants.PROJECT_PLAN_VALIDATE);

		if (validateStatus == null) {
			throw new ManagementModuleException(
					"Problem with getting status = " + ManagementModuleConstants.PROJECT_PLAN_VALIDATE
							+ " of project plan for projectID =" + pProjectId);
		}

		final Integer newVersion = projectPlan.getVersion();

		// we pass execution datas (iteration / scope unit discipline/ tasks on
		// new lots/scope units)
		for (final Lot lot : projectPlan.getLots()) {
			if (lot.getSrcLot() != null) {
				final List<ScopeUnitDiscipline> scopeUnitDisciplines = new ArrayList<ScopeUnitDiscipline>();
				final List<Long> scopeUnitIds = new ArrayList<Long>();

				final Map<String, ScopeUnit> mapScopeUnits = new HashMap<String, ScopeUnit>();
				for (final ScopeUnit su : lot.getScopeEntities()) {
					mapScopeUnits.put(su.getUnitId(), su);
				}

				for (final ScopeUnit su : lot.getSrcLot().getScopeEntities()) {
					scopeUnitIds.add(su.getId());
				}

				scopeUnitDisciplines.addAll(findScopeUnitDisciplinesByScopeUnitIds(scopeUnitIds));

				int digitsToRemove = 0;

				final String oldVersion = EMPTY_STRING + lot.getSrcLot().getProjectPlan().getVersion().intValue();
				digitsToRemove = oldVersion.length() + 1;

				for (final ScopeUnitDiscipline scopeUnitDiscipline : scopeUnitDisciplines) {
					final String newUnitId = determinateNewScopeUnitIdFromCurrentScopeUnitId(newVersion, digitsToRemove,
							scopeUnitDiscipline.getScopeUnit().getUnitId());
					if (mapScopeUnits.get(newUnitId) != null) {

						scopeUnitDiscipline.setScopeUnit(mapScopeUnits.get(newUnitId));
					}
				}

				// Processing for Iterations and Tasks
				for (final Iteration iteration : lot.getSrcLot().getIterations()) {
					iteration.setLot(lot);
					for (final IterationTask it : iteration.getIterationTasks()) {
						final Task task = it.getTask();
						final ScopeUnit oldScopeUnit = task.getScopeUnit();
						if (oldScopeUnit != null) {
							final String newUnitId = determinateNewScopeUnitIdFromCurrentScopeUnitId(newVersion,
									digitsToRemove, oldScopeUnit.getUnitId());
							final ScopeUnit newScopeUnit = mapScopeUnits.get(newUnitId);
							if (newScopeUnit != null) {
								task.setScopeUnit(newScopeUnit);
							}
						}
					}
				}

				lot.getIterations().addAll(lot.getSrcLot().getIterations());
			}
		}

		projectPlan.setStatus(validateStatus);
		return updateProjectPlan(projectPlan);
	}

	/************************* Lot ********************************************************************/

	@Override
	public Lot newLot() {
		return businessObjectFactory.getInstanceLot();
	}

	@Override
	public Lot creeteLot(final Lot lot) throws ManagementModuleException {
		validateLot(lot);
		return lotDAO.persist(lot);
	}

	/**
	 * @param projectPlan
	 * @param project
	 * @param createDate
	 * @param parentLot
	 * @throws ManagementModuleException
	 */
	@Override
	public Lot creeteLot(final ProjectPlan projectPlan, final String lotName, final Date createDate, final Date endDate,
			final String desc, final Set<Iteration> iterations, final Set<ScopeUnit> scopeUnits, final Lot lotParent)
					throws ManagementModuleException {

		final Lot initialLot = initializeNewLot(projectPlan, lotName, createDate, endDate, desc, lotParent);
		return creeteLot(initialLot);
	}

	@Override
	public Lot getLot(final Long lotId) throws ManagementModuleException {
		return this.lotDAO.completeFindByLotId(lotId);
	}

	@Override
	public Lot getCompleteLot(final Long lotId) throws ManagementModuleException {
		return this.lotDAO.completeFindByLotId(lotId);
	}

	@Override
	public List<Lot> getParentLotsList(final Long projectPlanId) throws ManagementModuleException {
		return this.lotDAO.findByProjectPlanId(projectPlanId);
	}

	@Override
	public List<Lot> getCompleteListLots(final Long projectPlanId) throws ManagementModuleException {
		return this.lotDAO.completeFindByProjectPlanId(projectPlanId);
	}

	@Override
	public boolean updateLot(final Lot lot) throws ManagementModuleException {
		validateLot(lot);
		lotDAO.merge(lot);
		return true;
	}

	@Override
	public boolean deleteLot(final Long lotId) throws ManagementModuleException {
		final Lot lot = getLot(lotId);

		if (lot.getSrcLot() != null) {
			throw new ManagementModuleException(ExceptionCode.ERR_LOT_DELETE_LOT_IS_A_COPY);
		}

		// Verifie que le lot n'a pas d'iterations
		final Set<Iteration> iterationsFromLot = getIterationsFromLot(lot);
		if (iterationsFromLot.size() > 0) {
			throw new ManagementModuleException(ExceptionCode.ERR_LOT_DELETE_LOT_HAVING_ITERATIONS);
		}

		// On ne supprime pas les lot qui possedent des unites de perimetres
		if (lot.getScopeEntities().size() > 0) {
			throw new ManagementModuleException(ExceptionCode.ERR_LOT_DELETE_LOT_HAVING_SCOPE_UNITS);
		} else {
			for (final Lot sousLot : lot.getChildLots()) {
				if (sousLot.getScopeEntities().size() > 0) {
					throw new ManagementModuleException(ExceptionCode.ERR_LOT_DELETE_LOT_HAVING_SCOPE_UNITS);
				}
			}
		}

		final Lot parentLot = lot.getParentLot();

		if (parentLot == null) {
			final ProjectPlan projectPlan = lot.getProjectPlan();

			// si c est un lot parent, retirer les
			// loadDistributionDiscipline sur
			// sa periode.
			final List<LoadDistributionDiscipline> loads = loadDistributionDisciplineDAO
					.findByProjectPlanId(projectPlan.getId());
			for (final LoadDistributionDiscipline ldd : loads) {
				if (ldd.getDate().after(lot.getStartDate()) && ldd.getDate().before(lot.getEndDate())) {
					loadDistributionDisciplineDAO.delete(ldd);
				}
			}

		} else {
			parentLot.getChildLots().remove(lot);
		}

		lotDAO.delete(lot);
		return true;
	}

	@Override
	public Set<Iteration> getIterationsFromLot(final Lot lot) {
		// if we are on a draft over a validated version, the iterations are
		// attached to srcLot
		final Set<Iteration> iterationsToCheck = new HashSet<Iteration>();
		if (lot.getProjectPlan().getStatus().getFunctionalId().equals(ManagementModuleConstants.PROJECT_PLAN_DRAFT)
				&& lot.getSrcLot() != null && lot.getSrcLot().getIterations() != null) {
			iterationsToCheck.addAll(lot.getSrcLot().getIterations());
		} else if (lot.getIterations() != null) {
			iterationsToCheck.addAll(lot.getIterations());
		}
		return iterationsToCheck;
	}

	@Override
	public String getLotMetadatas(final Long lotId, final Long subLotId, final String disciplineFunctionalId)
			throws ManagementModuleException {
		try {
			Lot lot = null;
			Lot lotParent = null;
			Discipline discipline = null;

			if (lotId != null && lotId != 0) {
				if (subLotId != null && subLotId != 0) {
					lotParent = getLot(lotId);
					lot = getLot(subLotId);
				} else {
					lot = getLot(lotId);
				}
			}

			if (disciplineFunctionalId != null && !disciplineFunctionalId.equalsIgnoreCase("all")) {
				discipline = referentielManager.getDiscipline(disciplineFunctionalId);
			}

			return Util.generateMetadatas(discipline, null, false, lot, true, lotParent, true);
		} catch (final ManagementModuleException e) {
			throw new ManagementModuleException(
					"Erreur lors de la recuperation des metadatas for functionalId : " + disciplineFunctionalId, e);
		}
	}

	/************************* Marker ********************************************************************/

	@Override
	public Marker newMarker() {
		return businessObjectFactory.getInstanceMarker();
	}

	@Override
	public Marker creeteMarker(final Marker marker) throws ManagementModuleException {
		return markerDAO.save(marker);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Marker creeteMarker(final ProjectPlan projectPlan, final String name, final Date date, final String desc,
			final MarkerType type) throws ManagementModuleException {
		final Marker marker = initializeNewMarker(projectPlan, date, desc, name, type);
		return creeteMarker(marker);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Marker updateMarker(final Marker marker) throws ManagementModuleException {
		return markerDAO.merge(marker);
	}

	@Override
	public Marker getMarker(final Long id) throws ManagementModuleException {
		return this.markerDAO.findById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteMarker(final Long markerId) throws ManagementModuleException {
		final Marker markerToDelete = markerDAO.findById(markerId);
		markerDAO.delete(markerToDelete);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Marker> getMarkerList(final Long projectPlanId) throws ManagementModuleException {
		ProjectPlan projectPlan;
		final List<Marker> list = new ArrayList<Marker>();
		projectPlan = projectPlanDAO.findById(projectPlanId);
		if (projectPlan != null) {
			list.addAll(projectPlan.getMarkers());
		}

		return list;
	}

	/************************* SteeringParameterDetail ********************************************************************/

	@Override
	public EstimationComponentDetail newEstimationComponentDetail() {
		return businessObjectFactory.getInstanceEstimationComponentDetail();
	}

	@Override
	public EstimationComponentDetail createEstimationComponentDetail(final EstimationComponentDetail sf)
			throws ManagementModuleException {
		return estimationComponentDetailDAO.save(sf);
	}

	@Override
	public EstimationComponentDetail getDefaultEstimationComponentDetail() throws ManagementModuleException {
		return estimationComponentDetailDAO
				.findByFunctionalId(ProjectPlanManager.DEFAULT_ESTIMATION_DETAIL_FUNCTIONAL_ID);
	}

	@Override
	public String generateEstimationComponentDetailFunctionalId(final ProjectPlan projectPlan) {
		return projectPlan.getProject().getProjectId() + "-" + projectPlan.getVersion();
	}

	/************************* ScopeUnit ********************************************************************/

	@Override
	public ScopeUnit newScopeUnit() {
		return businessObjectFactory.getInstanceScopeUnit();
	}

	@Override
	public ScopeUnit creeteScopeUnit(final ScopeUnit scope) throws ManagementModuleException {
		if (scopeUnitDAO.findByUnitId(scope.getUnitId()) != null) {
			throw new ManagementModuleException(ExceptionCode.ERR_SCOPE_UNIT_NAME_ALREADY_EXIST);
		}

		final ScopeUnit scopeUnit = scopeUnitDAO.save(scope);
		scopeUnit.setEstimation(initializeEstimation(scopeUnit));
		return scopeUnit;
	}

	@Override
	public ScopeUnit creeteScopeUnit(final String pScopeName, final String pdescription, final String pScopeUnitId,
			final String pScopeType, final String version, final boolean isManual, final Long pScopeLotId,
			final String pParentScopeUnitId) throws ManagementModuleException {

		final ScopeUnit scope = initializeScopeUnit(pScopeName, pdescription, pScopeUnitId, version, isManual);

		final ScopeType scopeType = referentielManager.getScopeType(pScopeType);

		if (scopeType == null) {
			throw new ManagementModuleException(
					String.format("Scope type doesn't exist with [scopeTypeName=%s]", pScopeType));
		}
		scope.setType(scopeType);

		if (pScopeLotId != null) {
			final Lot lot = getLot(pScopeLotId);
			if (lot == null) {
				throw new ManagementModuleException(
						String.format("Lot doesn't exist with [LotId=%s]", pScopeLotId.toString()));
			}
			scope.setLot(lot);
		}

		if (pParentScopeUnitId != null) {
			final ScopeUnit parentScope = getScopeUnit(pParentScopeUnitId);
			if (parentScope == null) {
				throw new ManagementModuleException(String.format(
						"Problem while linking the scope : parent scope unit doesn't exists with [unitId=%s]",
						pParentScopeUnitId));
			}
			scope.setParentScopeUnit(parentScope);
		}

		return creeteScopeUnit(scope);
	}

	/**
	 * @param pScopeName
	 * @param pdescription
	 * @param pScopeUnitId
	 * @return
	 */
	private ScopeUnit initializeScopeUnit(final String pScopeName, final String pdescription, final String pScopeUnitId,
			final String version, final boolean isManual) {
		final ScopeUnit scope = newScopeUnit();
		scope.setName(pScopeName);
		scope.setDescription(pdescription);
		scope.setUnitId(pScopeUnitId);
		scope.setVersion(version);
		scope.setManual(isManual);
		scope.setInScope(true);
		scope.setDate(new Date());
		return scope;
	}

	/**
	 * This method initializes a scopeUnit's estimation. By default, this
	 * estimation is set manually (in the UI), all its function points are 0,
	 * priorization elements come from obeo
	 * 
	 * @param scope
	 * @return Estimation
	 */
	private Estimation initializeEstimation(final ScopeUnit scope) {

		final Estimation estimation = newEstimation();
		estimation.setScopeUnit(scope);
		estimation.setLastDate(new Date());
		estimation.setManual(false);
		estimation.setSimple(DEFAULT_SIMPLE_VALUE);
		estimation.setGDIsimple(DEFAULT_FP_VALUE);
		estimation.setGDImedian(DEFAULT_FP_VALUE);
		estimation.setGDIcomplex(DEFAULT_FP_VALUE);
		estimation.setGDEsimple(DEFAULT_FP_VALUE);
		estimation.setGDEmedian(DEFAULT_FP_VALUE);
		estimation.setGDEcomplex(DEFAULT_FP_VALUE);
		estimation.setINsimple(DEFAULT_FP_VALUE);
		estimation.setINmedian(DEFAULT_FP_VALUE);
		estimation.setINcomplex(DEFAULT_FP_VALUE);
		estimation.setOUTsimple(DEFAULT_FP_VALUE);
		estimation.setOUTmedian(DEFAULT_FP_VALUE);
		estimation.setOUTcomplex(DEFAULT_FP_VALUE);
		estimation.setINTsimple(DEFAULT_FP_VALUE);
		estimation.setINTmedian(DEFAULT_FP_VALUE);
		estimation.setINTcomplex(DEFAULT_FP_VALUE);
		estimation.setGlobalSimple(DEFAULT_FP_VALUE);
		estimation.setGlobalMedian(DEFAULT_FP_VALUE);
		estimation.setGlobalComplex(DEFAULT_FP_VALUE);
		estimation.setFPRaw(DEFAULT_FP_VALUE);
		estimation.setBenefit(DEFAULT_PRIORIZATION_VALUE);
		estimation.setInjury(DEFAULT_PRIORIZATION_VALUE);
		estimation.setRisk(DEFAULT_PRIORIZATION_VALUE);
		estimation.setWeight(estimation.getBenefit() + estimation.getInjury() + estimation.getRisk());
		estimation.setCharge(DEFAULT_FP_VALUE);
		return estimation;
	}

	@Override
	public String generateManualUnitId(final String projectId, final String scopeName,
			final Integer projectPlanVersion) {
		int version = 1;

		if (projectPlanVersion != null) {
			version = projectPlanVersion.intValue();
		}

		return generateUnitId(generateRootManualUnitId(projectId, scopeName), version);
	}

	@Override
	public ScopeUnit getScopeUnit(final String scopeUnitId) throws ManagementModuleException {
		return this.scopeUnitDAO.findByUnitId(scopeUnitId);
	}

	@Override
	public boolean linkScopeUnitToLot(final String pScopeUnitId, final Long pScopeLotId)
			throws ManagementModuleException {
		final ScopeUnit currentScope = getScopeUnit(pScopeUnitId);
		if (currentScope == null) {
			throw new ManagementModuleException(String.format(
					"Problem while linking the scope : scope unit doesn't exists with [scopeId=%s]", pScopeUnitId));
		}
		final Lot lot = getLot(pScopeLotId);
		if (lot == null) {
			throw new ManagementModuleException(
					String.format("Lot doesn't exist with [scopeTypeName=%s]", pScopeLotId.toString()));
		}
		currentScope.setLot(lot);
		this.scopeUnitDAO.merge(currentScope);

		return true;
	}

	@Override
	public boolean updateScopeUnit(final ScopeUnit scope) throws ManagementModuleException {
		this.scopeUnitDAO.merge(scope);
		return true;
	}

	@Override
	public boolean updateManualScopeUnit(final String pScopeUnitId, final String pScopeName, final String description,
			final String version, final Long pScopeLotId, final String pParentScopeUnitId, final String pType)
					throws ManagementModuleException {

		Lot lot = null;

		final ScopeUnit scopeUnit = getScopeUnit(pScopeUnitId);

		scopeUnit.setName(pScopeName);
		scopeUnit.setDate(new Date());
		scopeUnit.setDescription(description);
		scopeUnit.setVersion(version);

		final ScopeType scopeType = referentielManager.getScopeType(pType);
		if (scopeType == null) {
			throw new ManagementModuleException(String.format("Scope Unit Type doesn't exist with [type=%s]", pType));
		}
		scopeUnit.setType(scopeType);

		if (pScopeLotId == null && pParentScopeUnitId == null) {
			// scope unit must be linked to a lot or a parent scope
			throw new ManagementModuleException(
					String.format("Scope Unit must be linked with a lot or a parent scope unit with [pScopeUnitId=%s]",
							pScopeUnitId));
		}
		if (pScopeLotId != null) {
			lot = getLot(pScopeLotId);
			if (lot == null) {
				throw new ManagementModuleException(
						String.format("Parent lot doesn't exist with [lot id =%s]", pScopeLotId.toString()));
			}
			scopeUnit.setParentScopeUnit(null);
			scopeUnit.setLot(lot);

			if (scopeUnit.getChildscopeunit().size() > 0) {
				for (final ScopeUnit child : scopeUnit.getChildscopeunit()) {
					child.setLot(lot);
					updateScopeUnit(child);
				}
			}

		} else if (pParentScopeUnitId != null) {
			final ScopeUnit pareScopeUnit = getScopeUnit(pParentScopeUnitId);
			if (pareScopeUnit == null) {
				throw new ManagementModuleException(
						String.format("Parent scope unt doesn't exist with [scope unit id =%s]", pParentScopeUnitId));
			} else {
				scopeUnit.setParentScopeUnit(pareScopeUnit);
			}
		}
		try {
			// save the current scope unit
			updateScopeUnit(scopeUnit);
			return true;
			// this.scopeUnitDAO.merge(scopeUnit);
		} catch (final ManagementModuleException e) {
			throw new ManagementModuleException(
					String.format("Problem while saving the scope with [scopeId=%s]", scopeUnit.getUnitId()), e);
		}
	}

	@Override
	public boolean deleteScopeUnit(final String pScopeUnitId) throws ManagementModuleException {
		final ScopeUnit curentScopeUnit = getScopeUnit(pScopeUnitId);

		// une unité de périmètre qui possède des tâches est lancée sur
		// une itération.
		// On ne peux pas supprimé ou délier une unité de périmètre
		// lancée sur une itération
		if (curentScopeUnit.getLot().getSrcLot() != null) {

			// Calculate the number of characters that must be remove for
			// generate
			// the good UnitId.
			int digitsToRemove = 0;

			if (curentScopeUnit.getLot().getProjectPlan().getVersion() > 1) {
				final String currentVersion = EMPTY_STRING
						+ curentScopeUnit.getLot().getProjectPlan().getVersion().intValue();
				digitsToRemove = currentVersion.length() + 1;
			}

			final String oldUnitId = determinateOldUnitIdFromCurrentScopeUnitId(
					curentScopeUnit.getLot().getSrcLot().getProjectPlan().getVersion(), digitsToRemove,
					curentScopeUnit.getUnitId());

			ScopeUnit oldScopeUnit = null;
			for (final ScopeUnit su : curentScopeUnit.getLot().getSrcLot().getScopeEntities()) {
				if (su.getUnitId().equalsIgnoreCase(oldUnitId)) {
					oldScopeUnit = su;
					break;
				}
			}
			if (oldScopeUnit != null && (!oldScopeUnit.getTasks().isEmpty()
					|| !findScopeUnitDisciplinesByScopeUnitId(oldScopeUnit.getId()).isEmpty())) {
				throw new ManagementModuleException(ExceptionCode.ERR_SCOPE_UNIT_LINKED_TO_ITERATION);
			}
		}

		if (!curentScopeUnit.getTasks().isEmpty()
				|| !findScopeUnitDisciplinesByScopeUnitId(curentScopeUnit.getId()).isEmpty()) {
			throw new ManagementModuleException(ExceptionCode.ERR_SCOPE_UNIT_LINKED_TO_ITERATION);
		}

		// delete scope unit
		scopeUnitDAO.deleteScopeUnit(curentScopeUnit.getUnitId());
		return true;
	}

	@Override
	public boolean linkExistingScopeUnit(final ScopeUnit scope, final String existingRefScopeId,
			final String existingRefScopeVersion) throws ManagementModuleException {
		final RefScopeUnit refScope = getRefScopeUnit(existingRefScopeId, existingRefScopeVersion);
		if (refScope != null) {
			scope.setUnitId(generateUnitId(refScope.getUnitId(), scope.getLot().getProjectPlan().getVersion()));
			scope.setRefScopeUnit(refScope);
			scope.setName(refScope.getName());
			scope.setType(refScope.getType());
			scope.setManual(false);
			this.scopeUnitDAO.merge(scope);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean unLinkExistingScopeUnit(final ScopeUnit scope) throws ManagementModuleException {
		scope.setManual(true);
		scope.setRefScopeUnit(null);
		scope.setUnitId(generateManualUnitId(scope.getLot().getProjectPlan().getProject().getProjectId(),
				scope.getName(), scope.getLot().getProjectPlan().getVersion()));
		this.scopeUnitDAO.merge(scope);
		return true;
	}

	@Override
	public ScopeUnit creeteScopeUnitFromRefScopeUnit(final String unitId, final String version, final Long lotId,
			final ScopeUnit parent) throws ManagementModuleException {
		ScopeUnit currentScope = getScopeUnitFromRefScopeUnit(unitId, version, lotId, parent);
		currentScope = this.scopeUnitDAO.merge(currentScope);
		return currentScope;
	}

	/**
	 * return module mgmt scope unit from Ref Scope unit
	 * 
	 * @param unitId
	 *            unit id
	 * @param version
	 *            version
	 * @param lotId
	 *            lotId
	 * @param parent
	 *            parent scope unit
	 * @return scope unit
	 * @throws ManagementModuleException
	 *             exception
	 */
	private ScopeUnit getScopeUnitFromRefScopeUnit(final String unitId, final String version, final Long lotId,
			final ScopeUnit parent) throws ManagementModuleException {
		final Lot lot = getLot(lotId);
		if (lot == null) {
			throw new ManagementModuleException(
					String.format("Lot doesn't exist with [scopeTypeName=%s]", lotId.toString()));
		}

		final RefScopeUnit currentRefScope = getRefScopeUnit(unitId, version);

		final String scopeUnitId = generateUnitId(unitId, lot.getProjectPlan().getVersion());

		// on cherche d abord si le scopeUnit correspondant existe deja
		// recherche d un scopeUnit deja existant avec cette unitId
		ScopeUnit currentScope = getScopeUnit(scopeUnitId);
		if (currentScope == null) {
			// recherche d'une scopeUnit fesant reference a ce refScopeUnit
			currentScope = findScopedRefVersionUnit(unitId, version, lot.getProjectPlan().getId());
		}

		if (currentScope == null) {
			currentScope = newScopeUnit();

			final Estimation est = initializeEstimation(currentScope);
			est.setManual(false);
			est.setBenefit(currentRefScope.getBenefit());
			est.setRisk(currentRefScope.getRisk());
			est.setInjury(currentRefScope.getInjury());
			currentScope.setEstimation(est);
		}
		currentScope.setRefScopeUnit(currentRefScope);
		currentScope.setDate(currentRefScope.getDateModified());
		currentScope.setName(currentRefScope.getName());
		currentScope.setDescription(currentRefScope.getDescription());
		currentScope.setUnitId(scopeUnitId);
		currentScope.setVersion(currentRefScope.getVersion());
		currentScope.setType(currentRefScope.getType());
		currentScope.setManual(false);
		currentScope.setLot(lot);
		if (parent != null) {
			currentScope.setParentScopeUnit(parent);
		}

		// Create scopeUnit for all child RefScopeUnit of currentRefScope
		for (final RefScopeUnit rsu : currentRefScope.getChildScopeUnit()) {
			ScopeUnit childUnit = this.getScopeUnitFromRefScopeUnit(rsu.getUnitId(), rsu.getVersion(), lotId,
					currentScope);
			currentScope.getChildscopeunit().add(childUnit);
		}
		return currentScope;
	}

	@Override
	public List<ScopeUnit> findScopedRefUnit(final String refUnitId) throws ManagementModuleException {
		return this.scopeUnitDAO.findByRefUnitId(refUnitId);
	}

	@Override
	public ScopeUnit findScopedRefVersionUnit(final String refUnitId, final String refVersion, final Long projectPlanId)
			throws ManagementModuleException {
		return this.scopeUnitDAO.findByRefVersion(refUnitId, refVersion, projectPlanId);
	}

	@Override
	public List<ScopeUnit> findScopeUnitListByProjectPlanId(final Long pProjectPlanId)
			throws ManagementModuleException {
		return scopeUnitDAO.findByProjectPlanId(pProjectPlanId);
	}

	@Override
	public String determinateOldUnitIdFromCurrentScopeUnitId(final Integer oldVersion, final int digitsToRemove,
			final String currentUnitId) {

		final String initialId = currentUnitId.substring(0, currentUnitId.length() - digitsToRemove);

		return generateUnitId(initialId, oldVersion);
	}

	@Override
	public String determinateNewScopeUnitIdFromCurrentScopeUnitId(final Integer newVersion, final int digitsToRemove,
			final String currentUnitId) {

		final String initialId = currentUnitId.substring(0, currentUnitId.length() - digitsToRemove);

		return generateUnitId(initialId, newVersion);
	}

	/************************* RefScopeUnit ********************************************************************/

	@Override
	public RefScopeUnit newRefScopeUnit() {
		return businessObjectFactory.getInstanceRefScopeUnit();
	}

	@Override
	public RefScopeUnit creeteRefScopeUnit(final RefScopeUnit refScope) throws ManagementModuleException {
		return refScopeUnitDAO.save(refScope);
	}

	@Override
	public RefScopeUnit getRefScopeUnit(final String refScopeUnitId, final String version)
			throws ManagementModuleException {
		return this.refScopeUnitDAO.findByRefScopeUnitId(refScopeUnitId, version);
	}

	@Override
	public RefScopeUnit getCompleteRefScopeUnit(final String refScopeUnitId, final String version)
			throws ManagementModuleException {
		return this.refScopeUnitDAO.getCompleteRefScopeUnit(refScopeUnitId, version);
	}

	@Override
	public RefScopeUnit getRefScopeUnitByName(final String refScopeUnitName, final String refScopeUnitVersion)
			throws ManagementModuleException {
		return this.refScopeUnitDAO.findByRefScopeUnitName(refScopeUnitName, refScopeUnitVersion);
	}

	@Override
	public boolean updateRefScopeUnit(final RefScopeUnit refScope) throws ManagementModuleException {
		this.refScopeUnitDAO.merge(refScope);
		return true;
	}

	@Override
	public List<RefScopeUnit> getLastVersionRefScopeUnit(final String projectId) throws ManagementModuleException {
		return refScopeUnitDAO.getLastVersionRefScopeUnit(projectId);
	}

	@Override
	public boolean updateFromCDORefScopeUnit(final String host, final String port, final String repoCdo,
			final String projet, final String systemGraal, final Project projetForge, final CDOParameters cdoParameters)
					throws ManagementModuleException {
		connectorCDO.changeParameters(host, port, repoCdo);
		final boolean retour = (updateTasksFromCDORefScopeUnit(host, port, repoCdo, projet, systemGraal, projetForge,
				cdoParameters)
				&& updateUseCasesFromCDORefScopeUnit(host, port, repoCdo, projet, systemGraal, projetForge,
						cdoParameters)
				&& updateUserStoriesFromCDORefScopeUnit(host, port, repoCdo, projet, systemGraal, projetForge,
						cdoParameters));
		this.testObsolescence(projetForge.getProjectId(), projet, systemGraal, cdoParameters, false);
		return retour;
	}

	@Override
	public boolean updateTasksFromCDORefScopeUnit(final String host, final String port, final String repoCdo,
			final String projet, final String systemGraal, final Project projet_forge,
			final CDOParameters cdoParameters) throws ManagementModuleException {
		boolean retour = true;
		try {
			final ScopeType scopeTypeTask = referentielManager
					.getScopeTypeByfunctionalId(ManagementModuleConstants.SCOPE_TYPE_GRAAL_TASK);
			final StatusScope stateOngoing = referentielManager
					.getStatusScope(ManagementModuleConstants.SCOPE_STATUS_ONGOING);
			final List<Element> tasksList = connectorCDO.importTasks(systemGraal, projet);
			for (final Element task : tasksList) {
				final String name = task.getAttribute(NAME);
				final String version = task.getAttribute(VERSION);
				final String createdON = task.getAttribute(CREATED_ON);
				String modifiedON = task.getAttribute(MODIFIED_ON);
				if (modifiedON == null || modifiedON.equals(EMPTY_STRING)) {
					modifiedON = createdON;
				}
				String benefitsLevel = task.getAttribute(BENEFITS_LEVEL);
				if (benefitsLevel == null || benefitsLevel.equals(EMPTY_STRING)) {
					benefitsLevel = "1";
				}
				String drawbacksLevel = task.getAttribute(DRAWBACKS_LEVEL);
				if (drawbacksLevel == null || drawbacksLevel.equals(EMPTY_STRING)) {
					drawbacksLevel = "1";
				}
				String riskLevel = task.getAttribute(RISK_LEVEL);
				if (riskLevel == null || riskLevel.equals(EMPTY_STRING)) {
					riskLevel = "1";
				}
				String description = task.getAttribute(DESCRIPTION);
				if (description == null || description.equals(EMPTY_STRING)) {
					description = EMPTY_STRING;
				}
				final String id = task.getAttribute(ID);
				// on cree le noeud en base
				RefScopeUnit refScopeUnit = this.newRefScopeUnit();
				refScopeUnit.setName(name);
				refScopeUnit.setVersion(version);
				refScopeUnit.setDateCreated(new SimpleDateFormat(Utils.EN_DATE_FORMAT).parse(createdON));
				refScopeUnit.setDateModified(new SimpleDateFormat(Utils.EN_DATE_FORMAT).parse(modifiedON));
				refScopeUnit.setBenefit(Integer.parseInt(benefitsLevel));
				refScopeUnit.setInjury(Integer.parseInt(drawbacksLevel));
				refScopeUnit.setRisk(Integer.parseInt(riskLevel));
				refScopeUnit.setDescription(description);
				refScopeUnit.setProject(projet_forge);
				refScopeUnit.setUnitId(projet_forge.getProjectId() + "_" + id);
				refScopeUnit.setState(stateOngoing);
				refScopeUnit.setType(scopeTypeTask);
				refScopeUnit.setState(stateOngoing);
				refScopeUnit.setCdoParameters(cdoParameters);
				final List<RefScopeUnit> listRefRefScopeUnitfromSGBD = this.refScopeUnitDAO
						.getCompleteAllFromProject(projet_forge.getProjectId(), cdoParameters);
				final RefScopeUnit res = this.containsElementsOfCDO(listRefRefScopeUnitfromSGBD,
						projet_forge.getProjectId() + "_" + id);
				final List<ScopeUnit> listScopeUnit = this.scopeUnitDAO.findByRefUnitId(refScopeUnit.getUnitId());
				ScopeUnit scopeUnit = null;
				if (!listScopeUnit.isEmpty()) {
					scopeUnit = listScopeUnit.get(0);
				}
				if (res != null) {
					// update
					res.setName(name);
					res.setVersion(version);
					res.setDateCreated(new SimpleDateFormat(Utils.EN_DATE_FORMAT).parse(createdON));
					res.setDateModified(new SimpleDateFormat(Utils.EN_DATE_FORMAT).parse(modifiedON));
					res.setBenefit(Integer.parseInt(benefitsLevel));
					res.setInjury(Integer.parseInt(drawbacksLevel));
					res.setRisk(Integer.parseInt(riskLevel));
					res.setDescription(description);
					res.setProject(projet_forge);
					res.setType(scopeTypeTask);
					res.setCdoParameters(cdoParameters);
					refScopeUnit = this.refScopeUnitDAO.merge(res);
					if (scopeUnit != null) {
						scopeUnit.setName(refScopeUnit.getName());
						scopeUnit.setDescription(refScopeUnit.getDescription());
						scopeUnit.setVersion(refScopeUnit.getVersion());
						scopeUnit = this.scopeUnitDAO.merge(scopeUnit);
					}
				} else {
					// save
					refScopeUnit = this.refScopeUnitDAO.save(refScopeUnit);
				}
				// on cherche les filles de premier niveaux
				if (task.hasChildNodes()) {
					final Set<RefScopeUnit> childsRefScopeUnit = new HashSet<RefScopeUnit>();
					final List<ScopeUnit> childsScopeUnit = new ArrayList<ScopeUnit>();
					final NodeList listChilds = task.getChildNodes();
					for (int v = 0; v < listChilds.getLength(); v++) {
						final Element chld = (Element) listChilds.item(v);
						final String namechld = chld.getAttribute(NAME);
						final String versionchld = chld.getAttribute(VERSION);
						final String createdONchld = chld.getAttribute(CREATED_ON);
						String modifiedONchld = chld.getAttribute(MODIFIED_ON);
						if (modifiedONchld == null || modifiedONchld.equals(EMPTY_STRING)) {
							modifiedONchld = createdONchld;
						}
						String benefitsLevelchld = chld.getAttribute(BENEFITS_LEVEL);
						if (benefitsLevelchld == null || benefitsLevelchld.equals(EMPTY_STRING)) {
							benefitsLevelchld = "1";
						}
						String drawbacksLevelchld = chld.getAttribute(DRAWBACKS_LEVEL);
						if (drawbacksLevelchld == null || drawbacksLevelchld.equals(EMPTY_STRING)) {
							drawbacksLevelchld = "1";
						}
						String riskLevelchld = chld.getAttribute(RISK_LEVEL);
						if (riskLevelchld == null || riskLevelchld.equals(EMPTY_STRING)) {
							riskLevelchld = "1";
						}
						String descriptionchld = chld.getAttribute(DESCRIPTION);
						if (descriptionchld == null || descriptionchld.equals(EMPTY_STRING)) {
							descriptionchld = EMPTY_STRING;
						}
						final String idchld = chld.getAttribute(ID);
						// on cree le noeud en base
						RefScopeUnit refScopeUnitChild = this.newRefScopeUnit();
						refScopeUnitChild.setName(namechld);
						refScopeUnitChild.setVersion(versionchld);
						refScopeUnitChild
								.setDateCreated(new SimpleDateFormat(Utils.EN_DATE_FORMAT).parse(createdONchld));
						refScopeUnitChild
								.setDateModified(new SimpleDateFormat(Utils.EN_DATE_FORMAT).parse(modifiedONchld));
						refScopeUnitChild.setBenefit(Integer.parseInt(benefitsLevelchld));
						refScopeUnitChild.setInjury(Integer.parseInt(drawbacksLevelchld));
						refScopeUnitChild.setRisk(Integer.parseInt(riskLevelchld));
						refScopeUnitChild.setDescription(descriptionchld);
						refScopeUnitChild.setProject(projet_forge);
						refScopeUnitChild.setUnitId(projet_forge.getProjectId() + "_" + idchld);
						refScopeUnitChild.setState(stateOngoing);
						refScopeUnitChild.setState(stateOngoing);
						refScopeUnitChild.setType(scopeTypeTask);
						// on precise la reference au parent
						refScopeUnitChild.setCdoParameters(cdoParameters);
						RefScopeUnit reschild = this.containsElementsOfCDO(listRefRefScopeUnitfromSGBD,
								projet_forge.getProjectId() + "_" + idchld);
						refScopeUnitChild.setParentScopeUnit(refScopeUnit);
						if (reschild != null) {
							// update
							reschild.setName(namechld);
							reschild.setVersion(versionchld);
							reschild.setDateCreated(new SimpleDateFormat(Utils.EN_DATE_FORMAT).parse(createdONchld));
							reschild.setDateModified(new SimpleDateFormat(Utils.EN_DATE_FORMAT).parse(modifiedONchld));
							reschild.setBenefit(Integer.parseInt(benefitsLevelchld));
							reschild.setInjury(Integer.parseInt(drawbacksLevelchld));
							reschild.setRisk(Integer.parseInt(riskLevelchld));
							reschild.setDescription(descriptionchld);
							reschild.setProject(projet_forge);
							reschild.setType(scopeTypeTask);
							reschild.setCdoParameters(cdoParameters);
							reschild.setParentScopeUnit(refScopeUnit);
							reschild = this.refScopeUnitDAO.merge(reschild);
							childsRefScopeUnit.add(reschild);
							final List<ScopeUnit> listScopeUnitChild = this.scopeUnitDAO
									.findByRefUnitId(reschild.getUnitId());
							if (!listScopeUnitChild.isEmpty()) {
								ScopeUnit scopeUnitChild = listScopeUnitChild.get(0);
								scopeUnitChild.setName(reschild.getName());
								scopeUnitChild.setDescription(reschild.getDescription());
								scopeUnitChild.setVersion(reschild.getVersion());
								if (scopeUnit != null) {
									scopeUnitChild.setParentScopeUnit(scopeUnit);
								}
								scopeUnitChild = this.scopeUnitDAO.merge(scopeUnitChild);
								childsScopeUnit.add(scopeUnitChild);
							}
						} else {
							// save
							refScopeUnitChild = this.refScopeUnitDAO.save(refScopeUnitChild);
							childsRefScopeUnit.add(refScopeUnitChild);
						}
						refScopeUnit.setChildScopeUnit(childsRefScopeUnit);
						this.refScopeUnitDAO.merge(refScopeUnit);
						// maj scopunit
						if (scopeUnit != null) {
							scopeUnit.setChildscopeunit(childsScopeUnit);
							scopeUnit = this.scopeUnitDAO.merge(scopeUnit);
						}
					}
				}
			}
			// this.testObsolescence(tasksList,
			// projet_forge.getProjectId(),scopeTypeTask);
		} catch (final CDOPerimeterUnitConnectorException e) {
			connectorCDO.toString();
			retour = false;
		} catch (final ParseException e) {
			retour = false;
			throw new ManagementModuleException(
					generateErrorMessageForUpdateXXXFromCDO("Graal Tasks", host, port, repoCdo, projet, systemGraal),
					e);
		}
		return retour;
	}

	@Override
	public boolean updateUseCasesFromCDORefScopeUnit(final String host, final String port, final String repoCdo,
			final String projet, final String systemGraal, final Project projetForge, final CDOParameters cdoParameters)
					throws ManagementModuleException {
		boolean retour = true;
		// ICDOPerimeterUnitConnector connectorCDO = new
		// CDOPerimeterUnitConnectorAPI(repoCdo, host, port);
		try {
			final ScopeType scopeTypeUseCase = referentielManager
					.getScopeTypeByfunctionalId(ManagementModuleConstants.SCOPE_TYPE_USE_CASE);
			final StatusScope stateOngoing = referentielManager
					.getStatusScope(ManagementModuleConstants.SCOPE_STATUS_ONGOING);
			final List<Element> useCasesList = connectorCDO.importUseCases(systemGraal, projet);
			for (final Element useCase : useCasesList) {
				final String name = useCase.getAttribute(NAME);
				final String version = useCase.getAttribute(VERSION);
				final String createdON = useCase.getAttribute(CREATED_ON);
				String modifiedON = useCase.getAttribute(MODIFIED_ON);
				if (modifiedON == null || modifiedON.equals(EMPTY_STRING)) {
					modifiedON = createdON;
				}
				String benefitsLevel = useCase.getAttribute(BENEFITS_LEVEL);
				if (benefitsLevel == null || benefitsLevel.equals(EMPTY_STRING)) {
					benefitsLevel = "1";
				}
				String drawbacksLevel = useCase.getAttribute(DRAWBACKS_LEVEL);
				if (drawbacksLevel == null || drawbacksLevel.equals(EMPTY_STRING)) {
					drawbacksLevel = "1";
				}
				String riskLevel = useCase.getAttribute(RISK_LEVEL);
				if (riskLevel == null || riskLevel.equals(EMPTY_STRING)) {
					riskLevel = "1";
				}
				String description = useCase.getAttribute(DESCRIPTION);
				if (description == null || description.equals(EMPTY_STRING)) {
					description = EMPTY_STRING;
				}
				final String listIdTaskLinked = useCase.getAttribute("listIdTaskLinked");
				final String id = useCase.getAttribute(ID);
				// on cree le noeud en base
				RefScopeUnit refScopeUnit = this.newRefScopeUnit();
				refScopeUnit.setName(name);
				refScopeUnit.setVersion(version);
				SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
				refScopeUnit.setDateCreated(formatter.parse(createdON));
				refScopeUnit.setDateModified(formatter.parse(modifiedON));
				refScopeUnit.setBenefit(Integer.parseInt(benefitsLevel));
				refScopeUnit.setInjury(Integer.parseInt(drawbacksLevel));
				refScopeUnit.setRisk(Integer.parseInt(riskLevel));
				refScopeUnit.setDescription(description);
				refScopeUnit.setProject(projetForge);
				refScopeUnit.setUnitId(projetForge.getProjectId() + "_" + id);
				refScopeUnit.setState(stateOngoing);
				refScopeUnit.setType(scopeTypeUseCase);
				refScopeUnit.setCdoParameters(cdoParameters);
				// on verifie si on merge ou on cree
				final RefScopeUnit refScopeUnitFinded = this.refScopeUnitDAO
						.getLastVersionofThisRefScopeUnit(projetForge.getProjectId() + "_" + id);
				final List<ScopeUnit> listScopeUnit = this.scopeUnitDAO
						.findByRefUnitId(projetForge.getProjectId() + "_" + id);
				ScopeUnit scopeUnit = null;
				if (!listScopeUnit.isEmpty()) {
					scopeUnit = listScopeUnit.get(0);
				}
				if (refScopeUnitFinded == null) {
					refScopeUnit = this.refScopeUnitDAO.save(refScopeUnit);
				} else {
					refScopeUnitFinded.setName(name);
					refScopeUnitFinded.setVersion(version);
					formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
					refScopeUnitFinded.setDateCreated(formatter.parse(createdON));
					refScopeUnitFinded.setDateModified(formatter.parse(modifiedON));
					refScopeUnitFinded.setBenefit(Integer.parseInt(benefitsLevel));
					refScopeUnitFinded.setInjury(Integer.parseInt(drawbacksLevel));
					refScopeUnitFinded.setRisk(Integer.parseInt(riskLevel));
					refScopeUnitFinded.setDescription(description);
					refScopeUnitFinded.setProject(projetForge);
					refScopeUnitFinded.setUnitId(projetForge.getProjectId() + "_" + id);
					refScopeUnitFinded.setType(scopeTypeUseCase);
					refScopeUnit = this.refScopeUnitDAO.merge(refScopeUnitFinded);
					if (scopeUnit != null) {
						scopeUnit.setName(refScopeUnit.getName());
						scopeUnit.setDescription(refScopeUnit.getDescription());
						scopeUnit.setVersion(refScopeUnit.getVersion());
						scopeUnit = this.scopeUnitDAO.merge(scopeUnit);
					}
				}
				// gestion des sous tasks graal reliee au Use Case
				if (listIdTaskLinked != null && !listIdTaskLinked.equals(EMPTY_STRING)) {
					final StringTokenizer subTasks = new StringTokenizer(listIdTaskLinked, "|");
					final Set<RefScopeUnit> childsRefScopeUnit = new HashSet<RefScopeUnit>();
					final List<ScopeUnit> childsScopeUnit = new ArrayList<ScopeUnit>();
					while (subTasks.hasMoreTokens()) {
						final String idChild = subTasks.nextToken();
						RefScopeUnit refScopeUnitFindedChild = this.refScopeUnitDAO
								.getLastVersionofThisRefScopeUnitFils(projetForge.getProjectId() + "_" + idChild);
						if (refScopeUnitFindedChild != null) {
							refScopeUnitFindedChild.setParentScopeUnit(refScopeUnit);
							refScopeUnitFindedChild = this.refScopeUnitDAO.merge(refScopeUnitFindedChild);
							childsRefScopeUnit.add(refScopeUnitFindedChild);
							final List<ScopeUnit> listScopeUnitChild = this.scopeUnitDAO
									.findByRefUnitId(refScopeUnitFindedChild.getUnitId());
							if (!listScopeUnitChild.isEmpty()) {
								ScopeUnit scopeUnitChild = listScopeUnitChild.get(0);
								scopeUnitChild.setName(refScopeUnitFindedChild.getName());
								scopeUnitChild.setDescription(refScopeUnitFindedChild.getDescription());
								scopeUnitChild.setVersion(refScopeUnitFindedChild.getVersion());
								if (scopeUnit != null) {
									scopeUnitChild.setParentScopeUnit(scopeUnit);
								}
								scopeUnitChild = this.scopeUnitDAO.merge(scopeUnitChild);
								childsScopeUnit.add(scopeUnitChild);
							}
						}
					}
					refScopeUnit.setChildScopeUnit(childsRefScopeUnit);
					refScopeUnit = this.refScopeUnitDAO.merge(refScopeUnit);
					// maj scopunit
					if (scopeUnit != null) {
						scopeUnit.setChildscopeunit(childsScopeUnit);
						scopeUnit = this.scopeUnitDAO.merge(scopeUnit);
					}
				}
			}
			// this.testObsolescence(useCasesList,
			// projet_forge.getProjectId(),scopeTypeUseCase);
		} catch (final CDOPerimeterUnitConnectorException e) {
			connectorCDO.toString();
			retour = false;
		} catch (final ParseException e) {
			retour = false;
			throw new ManagementModuleException(
					generateErrorMessageForUpdateXXXFromCDO("UseCases", host, port, repoCdo, projet, systemGraal), e);
		}
		return retour;
	}

	@Override
	public boolean updateUserStoriesFromCDORefScopeUnit(final String host, final String port, final String repoCdo,
			final String projet, final String systemGraal, final Project projetForge, final CDOParameters cdoParameters)
					throws ManagementModuleException {
		boolean retour = true;
		try {
			final ScopeType scopeTypeUserStory = referentielManager
					.getScopeTypeByfunctionalId(ManagementModuleConstants.SCOPE_TYPE_USER_STORY);
			final StatusScope stateOngoing = referentielManager
					.getStatusScope(ManagementModuleConstants.SCOPE_STATUS_ONGOING);
			final List<Element> userStoriesList = connectorCDO.importUserStories(systemGraal, projet);
			for (final Element userStory : userStoriesList) {
				final String name = userStory.getAttribute(NAME);
				final String version = userStory.getAttribute(VERSION);
				final String createdON = userStory.getAttribute(CREATED_ON);
				String modifiedON = userStory.getAttribute(MODIFIED_ON);
				if (modifiedON == null || modifiedON.equals(EMPTY_STRING)) {
					modifiedON = createdON;
				}
				String benefitsLevel = userStory.getAttribute(BENEFITS_LEVEL);
				if (benefitsLevel == null || benefitsLevel.equals(EMPTY_STRING)) {
					benefitsLevel = "1";
				}
				String drawbacksLevel = userStory.getAttribute(DRAWBACKS_LEVEL);
				if (drawbacksLevel == null || drawbacksLevel.equals(EMPTY_STRING)) {
					drawbacksLevel = "1";
				}
				String riskLevel = userStory.getAttribute(RISK_LEVEL);
				if (riskLevel == null || riskLevel.equals(EMPTY_STRING)) {
					riskLevel = "1";
				}
				String description = userStory.getAttribute(DESCRIPTION);
				if (description == null || description.equals(EMPTY_STRING)) {
					description = EMPTY_STRING;
				}
				final String listIdTaskLinked = userStory.getAttribute("listIdTaskLinked");
				final String id = userStory.getAttribute(ID);
				// on cree le noeud en base
				RefScopeUnit refScopeUnit = this.newRefScopeUnit();
				refScopeUnit.setName(name);
				refScopeUnit.setVersion(version);
				final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
				refScopeUnit.setDateCreated(formatter.parse(createdON));
				refScopeUnit.setDateModified(formatter.parse(modifiedON));
				refScopeUnit.setBenefit(Integer.parseInt(benefitsLevel));
				refScopeUnit.setInjury(Integer.parseInt(drawbacksLevel));
				refScopeUnit.setRisk(Integer.parseInt(riskLevel));
				refScopeUnit.setDescription(description);
				refScopeUnit.setProject(projetForge);
				refScopeUnit.setUnitId(projetForge.getProjectId() + "_" + id);
				refScopeUnit.setState(stateOngoing);
				refScopeUnit.setType(scopeTypeUserStory);
				refScopeUnit.setCdoParameters(cdoParameters);
				// on verifie si on merge ou on cree
				final RefScopeUnit refScopeUnitFinded = this.refScopeUnitDAO
						.getLastVersionofThisRefScopeUnit(projetForge.getProjectId() + "_" + id);
				final List<ScopeUnit> listScopeUnit = this.scopeUnitDAO.findByRefUnitId(refScopeUnit.getUnitId());
				ScopeUnit scopeUnit = null;
				if (!listScopeUnit.isEmpty()) {
					scopeUnit = listScopeUnit.get(0);
				}
				if (refScopeUnitFinded == null) {
					refScopeUnit = this.refScopeUnitDAO.save(refScopeUnit);
				} else {
					refScopeUnitFinded.setName(name);
					refScopeUnitFinded.setVersion(version);
					refScopeUnitFinded.setDateCreated(formatter.parse(createdON));
					refScopeUnitFinded.setDateModified(formatter.parse(modifiedON));
					refScopeUnitFinded.setBenefit(Integer.parseInt(benefitsLevel));
					refScopeUnitFinded.setInjury(Integer.parseInt(drawbacksLevel));
					refScopeUnitFinded.setRisk(Integer.parseInt(riskLevel));
					refScopeUnitFinded.setDescription(description);
					refScopeUnitFinded.setProject(projetForge);
					refScopeUnitFinded.setUnitId(projetForge.getProjectId() + "_" + id);
					refScopeUnitFinded.setType(scopeTypeUserStory);
					refScopeUnitFinded.setCdoParameters(cdoParameters);
					refScopeUnit = this.refScopeUnitDAO.merge(refScopeUnitFinded);
					if (scopeUnit != null) {
						scopeUnit.setName(refScopeUnit.getName());
						scopeUnit.setDescription(refScopeUnit.getDescription());
						scopeUnit.setVersion(refScopeUnit.getVersion());
						scopeUnit = this.scopeUnitDAO.merge(scopeUnit);
					}
				}
				// gestion des sous tasks graal reliee au User Story
				if (listIdTaskLinked != null && !listIdTaskLinked.equals(EMPTY_STRING)) {
					final StringTokenizer subTasks = new StringTokenizer(listIdTaskLinked, "|");
					final Set<RefScopeUnit> childsRefScopeUnit = new HashSet<RefScopeUnit>();
					final List<ScopeUnit> childsScopeUnit = new ArrayList<ScopeUnit>();
					while (subTasks.hasMoreTokens()) {
						// String nameChild = subTasks[i];
						final String idChild = subTasks.nextToken();
						RefScopeUnit refScopeUnitFindedChild = this.refScopeUnitDAO
								.getLastVersionofThisRefScopeUnitFils(projetForge.getProjectId() + "_" + idChild);
						if (refScopeUnitFindedChild != null) {
							// on precise la reference au parent
							refScopeUnitFindedChild.setParentScopeUnit(refScopeUnit);
							refScopeUnitFindedChild = this.refScopeUnitDAO.merge(refScopeUnitFindedChild);
							childsRefScopeUnit.add(refScopeUnitFindedChild);
							final List<ScopeUnit> listScopeUnitChild = this.scopeUnitDAO
									.findByRefUnitId(refScopeUnitFindedChild.getUnitId());
							if (!listScopeUnitChild.isEmpty()) {
								ScopeUnit scopeUnitChild = listScopeUnitChild.get(0);
								scopeUnitChild.setName(refScopeUnitFindedChild.getName());
								scopeUnitChild.setDescription(refScopeUnitFindedChild.getDescription());
								scopeUnitChild.setVersion(refScopeUnitFindedChild.getVersion());
								if (scopeUnit != null) {
									scopeUnitChild.setParentScopeUnit(scopeUnit);
								}
								scopeUnitChild = this.scopeUnitDAO.merge(scopeUnitChild);
								childsScopeUnit.add(scopeUnitChild);
							}
						}
					}
					refScopeUnit.setChildScopeUnit(childsRefScopeUnit);
					refScopeUnit = this.refScopeUnitDAO.merge(refScopeUnit);
					// maj scopunit
					if (scopeUnit != null) {
						scopeUnit.setChildscopeunit(childsScopeUnit);
						scopeUnit = this.scopeUnitDAO.merge(scopeUnit);
					}
				}
			}
			// this.testObsolescence(userStoriesList,
			// projet_forge.getProjectId(),scopeTypeUserStory);
		} catch (final CDOPerimeterUnitConnectorException e) {
			connectorCDO.toString();
			retour = false;
		} catch (final ParseException e) {
			retour = false;
			throw new ManagementModuleException(
					generateErrorMessageForUpdateXXXFromCDO("User Stories", host, port, repoCdo, projet, systemGraal),
					e);
		}
		return retour;
	}

	/**
	 * ********************** AdjustWeight
	 * *******************************************************************
	 */

	@Override
	public AdjustWeight newAdjustWeight() {
		return businessObjectFactory.getInstanceAdjustWeight();
	}

	@Override
	public AdjustWeight creeteAdjustWeight(final AdjustWeight adjustW) throws ManagementModuleException {
		return adjustWeightDAO.save(adjustW);
	}

	/**
	 * ********************** LoadDistributionDiscipline
	 * *******************************************************************
	 */

	@Override
	public LoadDistributionDiscipline newLoadDistributionDiscipline() {
		return businessObjectFactory.getInstanceLoadDistributionDiscipline();
	}

	@Override
	public LoadDistributionDiscipline creeteLoadDistributionDiscipline(
			final LoadDistributionDiscipline loadDistributionDiscipline) throws ManagementModuleException {
		return loadDistributionDisciplineDAO.save(loadDistributionDiscipline);
	}

	@Override
	public List<LoadDistributionDiscipline> creeteLoadDistributionDiscipline(
			final List<LoadDistributionDiscipline> loadDistributionDisciplines) throws ManagementModuleException {
		try {
			final List<LoadDistributionDiscipline> created = new ArrayList<LoadDistributionDiscipline>();
			for (final LoadDistributionDiscipline ldd : loadDistributionDisciplines) {
				created.add(creeteLoadDistributionDiscipline(ldd));
			}
			return created;
		} catch (final ManagementModuleException e) {
			throw new ManagementModuleException("Problem while persist a collection of LoadDistributionDiscipline", e);
		}
	}

	@Override
	public boolean updateLoadDistributionDiscipline(final LoadDistributionDiscipline loadDistributionDiscipline)
			throws ManagementModuleException {
		this.loadDistributionDisciplineDAO.merge(loadDistributionDiscipline);
		return true;
	}

	@Override
	public boolean updateLoadDistributionDiscipline(final List<LoadDistributionDiscipline> loadDistributionDisciplines)
			throws ManagementModuleException {
		try {
			for (final LoadDistributionDiscipline ldd : loadDistributionDisciplines) {
				updateLoadDistributionDiscipline(ldd);
			}
			return true;
		} catch (final ManagementModuleException e) {
			throw new ManagementModuleException("Problem while updating a collection of loadDistributionDiscipline ",
					e);
		}
	}

	@Override
	public List<LoadDistributionDiscipline> getLoadDistributionDisciplineList(final Long projectPlanId)
			throws ManagementModuleException {
		return this.loadDistributionDisciplineDAO.findByProjectPlanId(projectPlanId);
	}

	@Override
	public boolean deleteLoadDistributionDiscipline(final Long id) throws ManagementModuleException {
		this.loadDistributionDisciplineDAO.delete(this.loadDistributionDisciplineDAO.findById(id));
		return true;
	}

	@Override
	public boolean deleteLoadDistributionDisciplinesByProjectPlanId(final Long projectPlanId)
			throws ManagementModuleException {
		this.loadDistributionDisciplineDAO.deleteByProjectPlanId(projectPlanId);
		return true;
	}

	@Override
	public boolean deleteLoadDistributionDisciplines(final List<Long> lddToDelete) throws ManagementModuleException {
		for (final Long lddId : lddToDelete) {
			this.loadDistributionDisciplineDAO.delete(this.loadDistributionDisciplineDAO.findById(lddId));
		}
		return true;
	}

	@Override
	public boolean manageLoadDistributionDisciplines(final List<LoadDistributionDiscipline> lddToCreate,
			final List<LoadDistributionDiscipline> lddToUpdate, final List<Long> lddToDelete)
					throws ManagementModuleException {
		try {
			creeteLoadDistributionDiscipline(lddToCreate);
			updateLoadDistributionDiscipline(lddToUpdate);
			deleteLoadDistributionDisciplines(lddToDelete);

			return true;
		} catch (final ManagementModuleException e) {
			throw new ManagementModuleException("Problem while Manage LoadDistributionDisciplines", e);
		}
	}

	/**
	 * ********************** Estimation
	 * *******************************************************************
	 */

	@Override
	public Estimation newEstimation() {
		return businessObjectFactory.getInstanceEstimation();
	}

	@Override
	public Estimation creeteEstimation(final Estimation pEstimation) throws ManagementModuleException {
		return estimationDAO.save(pEstimation);
	}

	@Override
	public Estimation getEstimationByScopeUnit(final String pScopeUnitId) throws ManagementModuleException {
		return estimationDAO.getByScopeUnit(pScopeUnitId);
	}

	@Override
	public boolean existEstimation(final String pScopeUnitId) throws ManagementModuleException {
		return estimationDAO.existEstimation(pScopeUnitId);
	}

	@Override
	public boolean updateEstimation(final Estimation pEstimation) throws ManagementModuleException {
		estimationDAO.merge(pEstimation);
		return true;
	}

	@Override
	public boolean updateEstimations(final List<Estimation> estimations, final EstimationComponentSimple simple,
			final EstimationComponentDetail detail) throws ManagementModuleException {
		try {
			for (final Estimation estimation : estimations) {
				// update pfRaw
				final Integer pfRaw = Math.round(calculateRawFP(estimation, detail, simple));
				estimation.setFPRaw(pfRaw);
				// update charge
				final Float pfAdjusted = pfRaw * detail.getAdjustementCoef();
				final Integer charge = Math.round(pfAdjusted * detail.getValueAbaChgHomJour());
				estimation.setCharge(charge);
				updateEstimation(estimation);
			}
			return true;
		} catch (final ManagementModuleException e) {
			throw new ManagementModuleException("Problem while updating estimation list", e);
		}
	}

	@Override
	public List<Estimation> getEstimations(final Long pProjectPlanId) throws ManagementModuleException {
		try {
			final List<Estimation> ret = new ArrayList<Estimation>();
			final List<ScopeUnit> scopeUnits = findScopeUnitListByProjectPlanId(pProjectPlanId);
			for (final ScopeUnit scopeUnit : scopeUnits) {
				if (scopeUnit.getEstimation() != null) {
					ret.add(scopeUnit.getEstimation());
				}
			}
			return ret;
		} catch (final ManagementModuleException e) {
			throw new ManagementModuleException(
					"Problem while getting estimations with projectPlanId =" + pProjectPlanId, e);
		}
	}

	/**
	 * ********************** RefCalendar
	 * *******************************************************************
	 */

	@Override
	public RefCalendar newRefCalendar() {
		return refCalendarDAO.newRefCalendar();
	}

	@Override
	public RefCalendar creeteRefCalendar(final RefCalendar refCalendar) throws ManagementModuleException {
		return refCalendarDAO.save(refCalendar);
	}

	/**
	 * ********************** StatusProjectPlan
	 * *******************************************************************
	 */
	@Override
	public StatusProjectPlan newStatusProjectPlan() {
		return businessObjectFactory.getInstanceStatusProjectPlan();
	}

	@Override
	public StatusProjectPlan creeteStatusProjectPlan(final StatusProjectPlan statusProjectPlan)
			throws ManagementModuleException {
		return statusProjectPlanDAO.save(statusProjectPlan);
	}

	@Override
	public ScopeUnitDisciplineStatus newScopeUnitDisciplineStatus() {
		return businessObjectFactory.getInstanceScopeUnitDisciplineStatus();
	}

	@Override
	public ScopeUnitDisciplineStatus createScopeUnitDisciplineStatus(
			final ScopeUnitDisciplineStatus scopeUnitDisciplineStatus) throws ManagementModuleException {
		return scopeUnitDisciplineStatusDAO.save(scopeUnitDisciplineStatus);
	}

	@Override
	public ScopeUnitDisciplineStatus getScopeUnitDisciplineStatus(final String functionalId)
			throws ManagementModuleException {
		return scopeUnitDisciplineStatusDAO.findByFunctionalId(functionalId);
	}

	@Override
	public ScopeUnitDiscipline getScopeUnitDiscipline(final long scopeId, final Discipline discipline)
			throws ManagementModuleException {
		return scopeUnitDisciplineDAO.findScopeUnitDiscipline(scopeId, discipline);
	}

	@Override
	public Set<ScopeUnitDiscipline> getScopeUnitDisciplines(final Long pProjectPlanId)
			throws ManagementModuleException {
		final Set<ScopeUnitDiscipline> ret = new HashSet<ScopeUnitDiscipline>();
		final Set<ScopeUnit> scopeUnits = new HashSet<ScopeUnit>(scopeUnitDAO.findByProjectPlanId(pProjectPlanId));
		if (scopeUnits != null && scopeUnits.size() > 0) {
			for (final ScopeUnit scopeUnit : scopeUnits) {
				final List<ScopeUnitDiscipline> scopeUnitDisciplines = scopeUnitDisciplineDAO
						.findScopeUnitDisciplinesByScopeUnit(scopeUnit.getId());
				if (scopeUnitDisciplines != null && scopeUnitDisciplines.size() > 0) {
					for (final ScopeUnitDiscipline scopeUnitDiscipline : scopeUnitDisciplines) {
						ret.add(scopeUnitDiscipline);
					}
				}
			}
		}
		return ret;
	}

	@Override
	public List<ScopeUnitDiscipline> findScopeUnitDisciplinesByScopeUnitId(final Long scopeUnitId)
			throws ManagementModuleException {
		return scopeUnitDisciplineDAO.findScopeUnitDisciplinesByScopeUnit(scopeUnitId);
	}

	@Override
	public List<ScopeUnitDiscipline> findScopeUnitDisciplinesByScopeUnitIds(final List<Long> scopeUnitIds)
			throws ManagementModuleException {
		return scopeUnitDisciplineDAO.findScopeUnitDisciplinesByScopeUnit(scopeUnitIds);
	}

	/**
	 * ********************** ScopeUnitDiscipline
	 * *******************************************************************
	 */
	@Override
	public ScopeUnitDiscipline newScopeUnitDiscipline() {
		return businessObjectFactory.getInstanceScopeUnitDiscipline();
	}

	@Override
	public ScopeUnitDiscipline creeteScopeUnitDiscipline(final ScopeUnitDiscipline scopeUnitDiscipline)
			throws ManagementModuleException {
		return scopeUnitDisciplineDAO.save(scopeUnitDiscipline);
	}

	@Override
	public boolean deleteScopeUnitDiscipline(final String scopeUnitId, final String functionalId)
			throws ManagementModuleException {
		boolean hasTaskStarted = false;
		final ScopeUnit scopeUnit = scopeUnitDAO.findByUnitId(scopeUnitId);
		final Discipline discipline = referentielManager.getDiscipline(functionalId);
		final List<Task> tasks = taskManager.findByScopeUnitIdAndDisciplineId(scopeUnit.getId(), discipline.getId());
		if (tasks != null && tasks.size() > 0) {
			for (final Task task : tasks) {
				if (!task.getStatus().getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_NOT_STARTED)) {
					hasTaskStarted = true;
					break;
				}
			}
		}
		if (!hasTaskStarted) {
			final ScopeUnitDiscipline scopeUnitDiscipline = getScopeUnitDiscipline(scopeUnit.getId(), discipline);
			scopeUnitDisciplineDAO.delete(scopeUnitDiscipline);

		}
		return !hasTaskStarted;

	}

	@Override
	public boolean terminateScopeUnitDiscipline(final String scopeUnitId, final String functionalId)
			throws ManagementModuleException {
		boolean hasTaskNotDone = false;
		final ScopeUnit scopeUnit = scopeUnitDAO.findByUnitId(scopeUnitId);
		final Discipline discipline = referentielManager.getDiscipline(functionalId);
		final List<Task> tasks = taskManager.findByScopeUnitIdAndDisciplineId(scopeUnit.getId(), discipline.getId());
		if (tasks != null && tasks.size() > 0) {
			for (final Task task : tasks) {
				if (!task.getStatus().getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_DONE)) {
					hasTaskNotDone = true;
					break;
				}
			}
		}
		if (!hasTaskNotDone) {
			final ScopeUnitDiscipline scopeUnitDiscipline = scopeUnitDisciplineDAO
					.findScopeUnitDiscipline(scopeUnit.getId(), discipline);
			scopeUnitDiscipline.setStatusUpdatedDate(new Date());
			/* update status */
			final ScopeUnitDisciplineStatus status = scopeUnitDisciplineStatusDAO
					.findByFunctionalId(ManagementModuleConstants.SCOPE_DISCIPLINE_STATUS_CLOSED);
			scopeUnitDiscipline.setStatus(status);
			/* merge scopeunitDiscipline */
			scopeUnitDisciplineDAO.merge(scopeUnitDiscipline);
		}
		return !hasTaskNotDone;
	}

	@Override
	public List<AdjustFactorJointure> getDefaultAdjustfactorJointureProjectPlan() throws ManagementModuleException {
		final List<AdjustFactorJointure> retour = new LinkedList<AdjustFactorJointure>();
		// recuperation de tous les adjustFactor
		final List<AdjustFactor> listAllAdjustFactors = referentielManager.getAllAdjustFactor();
		// recuperation de tous les adjustweight
		final AdjustWeight aw = referentielManager
				.getAdjustWeightByFunctionalId(ManagementModuleConstants.WEIGHT_ADJUSTMENT_NONEXISTENT);
		for (final AdjustFactor af : listAllAdjustFactors) {
			final AdjustFactorJointure afj = this.newAdjustFactorJointure();
			afj.setAdjustFactor(af);
			afj.setAdjustWeight(aw);
			retour.add(afj);
		}
		return retour;
	}

	@Override
	public AdjustFactorJointure newAdjustFactorJointure() {
		return businessObjectFactory.getInstanceAdjustFactorJointure();
	}

	@Override
	public ProjectPlan getLastFullProjectPlan(final String pProjectId) throws ManagementModuleException {
		ProjectPlan projectPlan;
		projectPlan = projectPlanDAO.findLastFullById(pProjectId);
		projectPlan.getAdjustFactorJointure();
		return projectPlan;
	}

	@Override
	public List<DayLoad> getLoadsByDateByProjectPlanId(final Long projectPlanId) throws ManagementModuleException {
		final ProjectPlan projectPlan = getProjectPlan(projectPlanId);
		return this.loadDistributionDisciplineDAO.getLoadsByDateByProjectPlanId(projectPlanId,
				projectPlan.getProject().getUnitTime().getDurationInDays());
	}

	@Override
	public List<GanttLotInfo> getGanttLotInfosByProjectPlanId(final Long projectPlanId)
			throws ManagementModuleException {

		final List<GanttLotInfo> list = new ArrayList<GanttLotInfo>();

		List<Lot> listLot = this.lotDAO.findByProjectPlanId(projectPlanId);

		for (Lot lot : listLot) {
			final GanttLotInfo gli = businessObjectFactory.getInstanceGanttLotInfo();
			gli.setId(lot.getId());
			gli.setDescription(lot.getName());
			if (lot.getParentLot() != null) {
				// child lot
				gli.setStartDateSubLot(lot.getStartDate());
				gli.setEndDateSubLot(lot.getEndDate());
			} else {
				// parent lot
				gli.setEndDateLot(lot.getEndDate());
				gli.setStartDateLot(lot.getStartDate());

			}
			list.add(gli);
		}

		long order = 0;
		final ProjectPlan pp = this.projectPlanDAO.findById(projectPlanId);
		if (pp != null && pp.getMarkers() != null) {

			for (final Marker m : pp.getMarkers()) {
				final GanttLotInfo gli = businessObjectFactory.getInstanceGanttLotInfo();

				gli.setMarkerDate(m.getDate());
				gli.setMarkerName(m.getName());

				list.add(gli);
			}
		}

		Collections.sort(list, new GanttLotInfoComparator());

		for (final GanttLotInfo g : list) {
			g.setOrder(order);
			order++;
		}
		return list;
	}

	@Override
	public CDOParameters newCDOParameters() {
		return businessObjectFactory.getInstanceCDOParameters();
	}

	@Override
	public CDOParameters creeteCDOParameters(final CDOParameters pCDOParameters) throws ManagementModuleException {
		CDOParameters retour;
		checkCronExpression(pCDOParameters);
		retour = CDOParametersDAO.save(pCDOParameters);
		createNewCronRefScopeUnit(pCDOParameters, pCDOParameters.getCronExpression());
		return retour;
	}

	@Override
	public void creeteCDOParameters(final List<CDOParameters> pCDOParameters) throws ManagementModuleException {

		for (final CDOParameters cdo : pCDOParameters) {
			creeteCDOParameters(cdo);
		}
	}

	@Override
	public boolean updateCDOParameters(final CDOParameters pCDOParameters) throws ManagementModuleException {
		boolean retour = false;
		final CDOParameters pCDOParametersFinded = findCDOParameters(pCDOParameters.getId());
		checkCronExpression(pCDOParameters);
		if (pCDOParametersFinded == null) {
			final CDOParameters cDOParametersNew = this.CDOParametersDAO.save(pCDOParameters);
			retour = createNewCronRefScopeUnit(cDOParametersNew, cDOParametersNew.getCronExpression());
		} else {
			final String oldExpCron = pCDOParametersFinded.getCronExpression();
			retour = createNewCronRefScopeUnit(this.CDOParametersDAO.merge(pCDOParameters), oldExpCron);
		}
		return retour;
	}

	@Override
	public boolean updateCDOParameters(final List<CDOParameters> pCDOParameters) throws ManagementModuleException {
		for (final CDOParameters cdo : pCDOParameters) {
			updateCDOParameters(cdo);
		}
		return true;
	}

	@Override
	public CDOParameters findCDOParameters(final Long id) throws ManagementModuleException {
		return this.CDOParametersDAO.findById(id);
	}

	@Override
	public List<CDOParameters> findAllCDOParameters(final String pProjectId) throws ManagementModuleException {
		return this.CDOParametersDAO.findALLByProjectId(pProjectId);
	}

	@Override
	public boolean deleteScopeUnitDisciplinesByProjectPlanId(final Long projectPlanId)
			throws ManagementModuleException {
		this.scopeUnitDisciplineDAO.deleteByProjectPlanId(projectPlanId);
		return true;
	}

	@Override
	public void deleteScopeUnitsByProjectPlanId(final Long projectPlanId) throws ManagementModuleException {
		deleteScopeUnitDisciplinesByProjectPlanId(projectPlanId);
		this.scopeUnitDAO.deleteByProjectPlanId(projectPlanId);
	}

	@Override
	public Float calculateAdjustementCoef(final Integer sumAdjustementFactor) {
		Float ret = DEFAULT_ESTIMATION_DETAIL_ADJUSTEMENT_COEF;
		if (sumAdjustementFactor != null) {
			ret += Utils.floatFormat(Float.parseFloat(String.valueOf(sumAdjustementFactor)) / 100, 2);
		}
		return ret;
	}

	@Override
	public boolean deleteCDOParameters(final List<CDOParameters> cdoParameters) throws ManagementModuleException {
		for (final CDOParameters param : cdoParameters) {
			deleteCDOParameters(param);
		}
		return true;
	}

	@Override
	public boolean deleteCDOParameters(final CDOParameters cdoParameters) throws ManagementModuleException {
		boolean ret = false;
		try {
			// on delie ou efface les RefscopeUnit
			ret = testObsolescence(cdoParameters.getProject().getProjectId(), cdoParameters.getProjetCdo(),
					cdoParameters.getSystemGraal(), cdoParameters, true);
			// on verifier si cron et on le stoppe
			if (sched != null) {
				final Trigger t = sched.getTrigger(cdoParameters.getProject().getName() + "_"
						+ cdoParameters.getSystemGraal() + "_" + cdoParameters.getProjetCdo() + "_"
						+ cdoParameters.getId() + "-" + cdoParameters.getCronExpression() + _TRIGGER,
						Scheduler.DEFAULT_GROUP);
				if (t != null) {
					sched.unscheduleJob(cdoParameters.getProject().getName() + "_" + cdoParameters.getSystemGraal()
							+ "_" + cdoParameters.getProjetCdo() + "_" + cdoParameters.getId() + "-"
							+ cdoParameters.getCronExpression() + _TRIGGER, Scheduler.DEFAULT_GROUP);
					sched.deleteJob(
							cdoParameters.getProject().getName() + "_" + cdoParameters.getSystemGraal() + "_"
									+ cdoParameters.getProjetCdo() + "_" + cdoParameters.getId() + JOB,
							Scheduler.DEFAULT_GROUP);
				}
			}
			CDOParametersDAO.delete(cdoParameters);
		} catch (final SchedulerException e) {
			throw new ManagementModuleException("Problem while instancing Cron of import RefScopeUnit with cron name = "
					+ cdoParameters.getProject().getName() + "_" + cdoParameters.getSystemGraal() + "_"
					+ cdoParameters.getProjetCdo() + "_" + cdoParameters.getId(), e);
		}
		return ret;
	}

	@Override
	public Float calculateIdealScopeUnitFP(final List<ScopeUnit> scopeUnits, final String projectId)
			throws ManagementModuleException {
		try {
			Float fpIdeal = 0f;
			Float consumed = 0f;
			int fp = 0;
			final EstimationComponentSimple simple = referentielManager.getEstimationComponentSimple(projectId);
			final EstimationComponentDetail detail = getLastProjectPlan(projectId).getEstimationComponentDetail();
			for (final ScopeUnit scopeUnit : scopeUnits) {
				// add consumed time for each task
				final List<Task> tasks = taskManager.findByScopeUnitId(scopeUnit.getId());
				for (final Task task : tasks) {
					consumed += taskManager.getTask(task.getId()).getConsumedTime();
				}
				// add sum of function points
				fp += calculateRawFP(scopeUnit.getEstimation(), detail, simple);
			}
			if (fp != 0 && detail.getAdjustementCoef() != 0) {
				fpIdeal = consumed / Float.valueOf(fp) / detail.getAdjustementCoef();
			}
			LOG.info("calculate ideal function point = " + fpIdeal);
			return fpIdeal;
		} catch (final ManagementModuleException e) {
			throw new ManagementModuleException("Problem while calculating ideal function point", e);
		}
	}

	/**
	 * check cron expression
	 * 
	 * @param pCDOParameters
	 *            parameter cdo
	 * @throws ManagementModuleException
	 *             exception for invalid cron expression
	 */
	private void checkCronExpression(CDOParameters pCDOParameters) throws ManagementModuleException {
		if (StringUtils.isNotEmpty(pCDOParameters.getCronExpression())) {
			if (!CronExpression.isValidExpression(pCDOParameters.getCronExpression())) {
				throw new ManagementModuleException(ExceptionCode.ERR_INVALID_CRON_EXPRESSION,
						"Problem of expression (" + pCDOParameters.getCronExpression()
								+ ") of Cron of import RefScopeUnit with cron name = "
								+ pCDOParameters.getProject().getName() + "_" + pCDOParameters.getSystemGraal() + "_"
								+ pCDOParameters.getProjetCdo() + "_" + pCDOParameters.getId());

			}
		}
	}

	private boolean createNewCronRefScopeUnit(final CDOParameters pCDOParameters, final String oldCronExp)
			throws ManagementModuleException {
		boolean retour = false;
		try {
			// TODO supprimer l utilisation des parametres stockes dans la map
			// et n utiliser que le CDOParameters
			// TODO reactoring sur la generation des noms....
			if (sched == null) {// on instancie une fois le scheduler
				sched = schedFact.getScheduler();
			}
			if (pCDOParameters.getCronExpression() != null) {
				if (!pCDOParameters.getCronExpression().trim().equals(EMPTY_STRING)) {
					// On recupere le scheduler et on le demarre si pas demarre
					// creation du joab quartz
					final JobDataMap map = new JobDataMap();
					map.put("pCDOParameters", pCDOParameters);
					map.put("pProjectId", pCDOParameters.getProject().getProjectId());
					map.put("pHost", pCDOParameters.getHost());
					map.put("pPort", pCDOParameters.getPort());
					map.put("pRepository", pCDOParameters.getRepository());
					map.put("pSystemGraal", pCDOParameters.getSystemGraal());
					map.put("pProjet", pCDOParameters.getProject());
					// map.put("pICDOPerimeterUnitConnector", connectorCDO);
					map.put("pProjetCDO", pCDOParameters.getProjetCdo());
					map.put("pProjectPlanManagerImpl", this);
					final JobDetail jobDetail = new JobDetail(
							pCDOParameters.getProject().getName() + "_" + pCDOParameters.getSystemGraal() + "_"
									+ pCDOParameters.getProjetCdo() + "_" + pCDOParameters.getId() + JOB,
							Scheduler.DEFAULT_GROUP,
							org.novaforge.forge.tools.managementmodule.internal.ImportRefScopeUnitJob.class);
					jobDetail.setJobDataMap(map);
					if (sched.isShutdown()) {
						sched.start();
					}
					// on regarde si il ya daja un trigger
					final CronTrigger t = (CronTrigger) sched.getTrigger(pCDOParameters.getProject().getName() + "_"
							+ pCDOParameters.getSystemGraal() + "_" + pCDOParameters.getProjetCdo() + "_"
							+ pCDOParameters.getId() + "-" + oldCronExp + _TRIGGER, Scheduler.DEFAULT_GROUP);
					final CronTrigger trigger = new CronTrigger(
							pCDOParameters.getProject().getName() + "_" + pCDOParameters.getSystemGraal() + "_"
									+ pCDOParameters.getProjetCdo() + "_" + pCDOParameters.getId() + "-"
									+ pCDOParameters.getCronExpression() + _TRIGGER,
							Scheduler.DEFAULT_GROUP, pCDOParameters.getCronExpression());
					if (t == null) {
						// on lance le job quartz
						// creation du trigger quartz

						sched.scheduleJob(jobDetail, trigger);
						if (!sched.isStarted()) {
							sched.start();
						}
					} else { // Sinon on modifie le timer
						t.clearAllTriggerListeners();
						trigger.setCronExpression(pCDOParameters.getCronExpression());
						final JobDetail jobd = sched.getJobDetail(
								pCDOParameters.getProject().getName() + "_" + pCDOParameters.getSystemGraal() + "_"
										+ pCDOParameters.getProjetCdo() + "_" + pCDOParameters.getId() + JOB,
								Scheduler.DEFAULT_GROUP);
						if (jobd != null) {
							sched.deleteJob(
									pCDOParameters.getProject().getName() + "_" + pCDOParameters.getSystemGraal() + "_"
											+ pCDOParameters.getProjetCdo() + "_" + pCDOParameters.getId() + JOB,
									Scheduler.DEFAULT_GROUP);
						}
						sched.scheduleJob(jobDetail, trigger);
						sched.rescheduleJob(pCDOParameters.getProject().getName() + "_"
								+ pCDOParameters.getSystemGraal() + "_" + pCDOParameters.getProjetCdo() + "_"
								+ pCDOParameters.getId() + "-" + oldCronExp + _TRIGGER, Scheduler.DEFAULT_GROUP,
								trigger);
						if (!sched.isStarted()) {
							sched.start();
						}
					}
					retour = true;
				}
			} else {// pas d expression cron, on le supprime
				if (sched != null) {
					final Trigger t = sched.getTrigger(pCDOParameters.getProject().getName() + "_"
							+ pCDOParameters.getSystemGraal() + "_" + pCDOParameters.getProjetCdo() + "_"
							+ pCDOParameters.getId() + "-" + oldCronExp + _TRIGGER, Scheduler.DEFAULT_GROUP);
					if (t != null) {
						sched.unscheduleJob(pCDOParameters.getProject().getName() + "_"
								+ pCDOParameters.getSystemGraal() + "_" + pCDOParameters.getProjetCdo() + "_"
								+ pCDOParameters.getId() + "-" + oldCronExp + _TRIGGER, Scheduler.DEFAULT_GROUP);
						final JobDetail jobd = sched.getJobDetail(
								pCDOParameters.getProject().getName() + "_" + pCDOParameters.getSystemGraal() + "_"
										+ pCDOParameters.getProjetCdo() + "_" + pCDOParameters.getId() + JOB,
								Scheduler.DEFAULT_GROUP);
						if (jobd != null) {
							sched.deleteJob(
									pCDOParameters.getProject().getName() + "_" + pCDOParameters.getSystemGraal() + "_"
											+ pCDOParameters.getProjetCdo() + "_" + pCDOParameters.getId() + JOB,
									Scheduler.DEFAULT_GROUP);
						}
					}
				}
				retour = true;
			}
		} catch (final ParseException e) {
			retour = false;
			throw new ManagementModuleException(ExceptionCode.ERR_INVALID_CRON_EXPRESSION,
					"Problem of expression (" + pCDOParameters.getCronExpression()
							+ ") of Cron of import RefScopeUnit with cron name = "
							+ pCDOParameters.getProject().getName() + "_" + pCDOParameters.getSystemGraal() + "_"
							+ pCDOParameters.getProjetCdo() + "_" + pCDOParameters.getId(),
					e);
		} catch (final SchedulerException e) {
			retour = false;
			throw new ManagementModuleException("Problem while instancing Cron of import RefScopeUnit with cron name = "
					+ pCDOParameters.getProject().getName() + "_" + pCDOParameters.getSystemGraal() + "_"
					+ pCDOParameters.getProjetCdo() + "_" + pCDOParameters.getId(), e);
		}
		return retour;
	}

	/**
	 * Calculate the raw function point with the given estimation Equivalent to
	 * the sum of function points for the given scopeUnit's estimation
	 * 
	 * @param estimation
	 * @return a Float, the sum of raw function point
	 */
	private Float calculateRawFP(final Estimation estimation, final EstimationComponentDetail detail,
			final EstimationComponentSimple simple) {
		float fpRaw = 0;
		if (!estimation.isManual()) {
			if (estimation.getSimple() != null
					&& !estimation.getSimple().equals(ManagementModuleConstants.FUNCTION_POINT_NONE)) {
				final Integer globalSimple = estimation.getGlobalSimple();
				final Integer globalMedian = estimation.getGlobalMedian();
				final Integer globalComplex = estimation.getGlobalComplex();
				// GDI
				if (estimation.getSimple().equals(ManagementModuleConstants.FUNCTION_POINT_GDI)) {
					fpRaw += globalSimple * detail.getValueSimpleGDI();
					fpRaw += globalMedian * detail.getValueMoyenGDI();
					fpRaw += globalComplex * detail.getValueComplexGDI();
					fpRaw = calculateSimpleRawFP(fpRaw, simple.getValueGDI());
				}
				// GDE
				else if (estimation.getSimple().equals(ManagementModuleConstants.FUNCTION_POINT_GDE)) {
					fpRaw += globalSimple * detail.getValueSimpleGDE();
					fpRaw += globalMedian * detail.getValueMoyenGDE();
					fpRaw += globalComplex * detail.getValueComplexGDE();
					fpRaw = calculateSimpleRawFP(fpRaw, simple.getValueGDE());
				}
				// IN
				else if (estimation.getSimple().equals(ManagementModuleConstants.FUNCTION_POINT_IN)) {
					fpRaw += globalSimple * detail.getValueSimpleIN();
					fpRaw += globalMedian * detail.getValueMoyenIN();
					fpRaw += globalComplex * detail.getValueComplexIN();
					fpRaw = calculateSimpleRawFP(fpRaw, simple.getValueENT());
				}
				// OUT
				else if (estimation.getSimple().equals(ManagementModuleConstants.FUNCTION_POINT_OUT)) {
					fpRaw += globalSimple * detail.getValueSimpleOUT();
					fpRaw += globalMedian * detail.getValueMoyenOUT();
					fpRaw += globalComplex * detail.getValueComplexOUT();
					fpRaw = calculateSimpleRawFP(fpRaw, simple.getValueSOR());
				}
				// INT
				else if (estimation.getSimple().equals(ManagementModuleConstants.FUNCTION_POINT_INT)) {
					fpRaw += globalSimple * detail.getValueSimpleINT();
					fpRaw += globalMedian * detail.getValueMoyenINT();
					fpRaw += globalComplex * detail.getValueComplexINT();
					fpRaw = calculateSimpleRawFP(fpRaw, simple.getValueINT());
				}
			} else {
				// IN
				fpRaw += estimation.getINsimple() * detail.getValueSimpleIN();
				fpRaw += estimation.getINmedian() * detail.getValueMoyenIN();
				fpRaw += estimation.getINcomplex() * detail.getValueComplexIN();
				// OUT
				fpRaw += estimation.getOUTsimple() * detail.getValueSimpleOUT();
				fpRaw += estimation.getOUTmedian() * detail.getValueMoyenOUT();
				fpRaw += estimation.getOUTcomplex() * detail.getValueComplexOUT();
				// INT
				fpRaw += estimation.getINTsimple() * detail.getValueSimpleINT();
				fpRaw += estimation.getINTmedian() * detail.getValueMoyenINT();
				fpRaw += estimation.getINTcomplex() * detail.getValueComplexINT();
				// GDI
				fpRaw += estimation.getGDIsimple() * detail.getValueSimpleGDI();
				fpRaw += estimation.getGDImedian() * detail.getValueMoyenGDI();
				fpRaw += estimation.getGDIcomplex() * detail.getValueComplexGDI();
				// GDE
				fpRaw += estimation.getGDEsimple() * detail.getValueSimpleGDE();
				fpRaw += estimation.getGDEmedian() * detail.getValueMoyenGDE();
				fpRaw += estimation.getGDEcomplex() * detail.getValueComplexGDE();
			}
		}
		return fpRaw;
	}

	/**
	 * Calculate the simple raw function point. This way,extrems values (i.e 0)
	 * are manage
	 * 
	 * @param fpRaw
	 * @param componentValue
	 * @return the calculate FPRAw
	 */
	private Float calculateSimpleRawFP(final Float fpRaw, final Float componentValue) {
		Float ret = 0f;
		final Float cent = Float.valueOf(100);
		if (fpRaw != 0 && componentValue != 0) {
			ret = fpRaw / componentValue * cent;
		}
		return ret;
	}

	private boolean testObsolescence(final String projectId, final String projet, final String systemGraal,
			final CDOParameters cdoParameters, final boolean deleteCDOParameters) throws ManagementModuleException {
		final List<Element> listRefScopeUnitfromCDO = new ArrayList<Element>();
		boolean retour = false;
		try {

			if (connectorCDO.getHost() != null && connectorCDO.getPort() != null && connectorCDO.getRepository() != null
					&& !deleteCDOParameters) {
				connectorCDO.changeParameters(cdoParameters.getHost(), EMPTY_STRING + cdoParameters.getPort(),
						cdoParameters.getRepository());
				listRefScopeUnitfromCDO.addAll(connectorCDO.importUseCases(systemGraal, projet));
				listRefScopeUnitfromCDO.addAll(connectorCDO.importUserStories(systemGraal, projet));
				listRefScopeUnitfromCDO.addAll(connectorCDO.importTasks(systemGraal, projet));
			}
			// on recupere tous les refScopeUnit du projet et on verifie si ils
			// sont
			// dans cette liste sinon on merge sur le status
			final List<RefScopeUnit> listRefRefScopeUnitfromSGBD = this.refScopeUnitDAO
					.getCompleteAllFromProject(projectId, cdoParameters, false);
			for (final RefScopeUnit refScopeUnitfromSGBD : listRefRefScopeUnitfromSGBD) {

				// on verifie si il est dans arbre CDO
				if (!this.containsRefScopUNite(listRefScopeUnitfromCDO, refScopeUnitfromSGBD.getUnitId(), projectId)) {
					// on delink le scope unit associe
					final List<ScopeUnit> listScopeUnit = new ArrayList<ScopeUnit>();
					listScopeUnit.addAll(this.scopeUnitDAO.findByRefUnitId(refScopeUnitfromSGBD.getUnitId()));
					for (final ScopeUnit su : listScopeUnit) {
						// il faudrait rendre obso le scope BUG a traiter
						unLinkExistingScopeUnit(su);
					}
					for (final RefScopeUnit child : refScopeUnitfromSGBD.getChildScopeUnit()) {
						listScopeUnit.addAll(this.scopeUnitDAO.findByRefUnitId(child.getUnitId()));
						for (final ScopeUnit su : listScopeUnit) {
							// il faudrait rendre obso le scope BUG a traiter
							unLinkExistingScopeUnit(su);
						}
					}

					refScopeUnitDAO.delete(refScopeUnitfromSGBD);
				}
			}
			retour = true;
		} catch (final ManagementModuleException e) {
			throw new ManagementModuleException("Problem while updating Staus of RefScopeUnit", e);
		} catch (final CDOPerimeterUnitConnectorException e) {
			throw new ManagementModuleException("Problem while updating Staus of RefScopeUnit", e);
		}
		return retour;
	}

	private RefScopeUnit containsElementsOfCDO(final List<RefScopeUnit> listRefRefScopeUnitfromSGBD,
			final String unitIdCdo) {
		final RefScopeUnit retour = null;
		for (final RefScopeUnit eltUnitfromSGBD : listRefRefScopeUnitfromSGBD) {
			final String unitIdSGBD = eltUnitfromSGBD.getUnitId();
			if (unitIdSGBD.equals(unitIdCdo)) {
				return eltUnitfromSGBD;
			}
		}
		return retour;
	}

	/**
	 * @param host
	 * @param port
	 * @param repoCdo
	 * @param projet
	 * @param systemGraal
	 * @return
	 */
	private String generateErrorMessageForUpdateXXXFromCDO(final String upType, final String host, final String port,
			final String repoCdo, final String projet, final String systemGraal) {
		return "Problem while updating " + upType + " from [host= " + host + ", port=" + port + ", repoCdo=" + repoCdo
				+ ", projet=" + projet + ", systemGraal=" + systemGraal + "] can't save";
	}

	private boolean containsRefScopUNite(final List<Element> listRefScopeUnitfromCDO, final String unitId,
			final String projectId) {
		final boolean retour = false;
		for (final Element eltUnitfromCDO : listRefScopeUnitfromCDO) {
			final String id = eltUnitfromCDO.getAttribute(ID);
			final String unitIdCDO = projectId + "_" + id;
			if (unitIdCDO.equals(unitId)) {
				return true;
			}
			if (eltUnitfromCDO.hasChildNodes()) {
				final NodeList listChilds = eltUnitfromCDO.getChildNodes();
				for (int v = 0; v < listChilds.getLength(); v++) {
					final Element chldcdo = (Element) listChilds.item(v);
					final String idchldcdo = chldcdo.getAttribute(ID);
					final String unitchldcdo = projectId + "_" + idchldcdo;
					if (unitchldcdo.equals(unitId)) {
						return true;
					}
				}
			}
		}
		return retour;
	}

	/**
	 * Generate the root of unitId for a manual scopeUnit or an unlinked
	 * scopeUnit
	 * 
	 * @param projectId
	 * @param scopeName
	 * @return
	 */
	private String generateRootManualUnitId(final String projectId, final String scopeName) {
		return projectId + "_" + scopeName;
	}

	/**
	 * @param projectPlan
	 * @param marker
	 * @return
	 */
	private Marker initializeNewMarker(final ProjectPlan projectPlan, final Date date, final String description,
			final String markerName, final MarkerType type) {
		final Marker newMarker = newMarker();
		newMarker.setDate(date);
		newMarker.setDescription(description);
		newMarker.setName(markerName.trim());
		newMarker.setType(type);
		newMarker.setProjectPlan(projectPlan);
		return newMarker;
	}

	/**
	 * Generate UnitId for ScopeUnit with specified parameters
	 * 
	 * @param scope
	 * @param refScope
	 * @return
	 */
	private String generateUnitId(final String refUnitId, final Integer version) {
		return refUnitId + "_" + version;
	}

	/**
	 * @param estimationByScopeUnitFunctionalIdMap
	 * @param lot
	 */
	private void loadEstimationMap(final Map<String, Estimation> estimationByScopeUnitFunctionalIdMap, final Lot lot) {
		for (final ScopeUnit scopeUnit : lot.getScopeEntities()) {
			final Estimation estimation = estimationByScopeUnitFunctionalIdMap.get(scopeUnit.getUnitId());
			estimation.setScopeUnit(scopeUnit);
			scopeUnit.setEstimation(estimation);
		}
	}

	/**
	 * @param projectPlanSource
	 * @param estimationByScopeUnitFunctionalIdMap
	 * @param lot
	 * @param duplicatedProjectPlan
	 */
	private void fillEstimationMap(final ProjectPlan projectPlanSource,
			final Map<String, Estimation> estimationByScopeUnitFunctionalIdMap, final Lot lot,
			final ProjectPlan duplicatedProjectPlan) {

		// Calculate the number of characters that must be remove for generate
		// the good UnitId.
		int digitsToRemove = 0;

		final String oldVersion = EMPTY_STRING + projectPlanSource.getVersion().intValue();
		digitsToRemove = oldVersion.length() + 1;

		for (final ScopeUnit scopeUnit : lot.getScopeEntities()) {

			final String newUnitId = determinateNewScopeUnitIdFromCurrentScopeUnitId(duplicatedProjectPlan.getVersion(),
					digitsToRemove, scopeUnit.getUnitId());

			scopeUnit.setUnitId(newUnitId);
			estimationByScopeUnitFunctionalIdMap.put(scopeUnit.getUnitId(), scopeUnit.getEstimation());
			scopeUnit.setEstimation(null);
		}
	}

	/**
	 * duplicate ProjectPlan without persistence
	 * 
	 * @param source
	 * @return
	 * @throws ManagementModuleException
	 */
	private ProjectPlan duplicateProjectPlan(final ProjectPlan source) throws ManagementModuleException {
		final ProjectPlan projectPlan = newProjectPlan();
		projectPlan.setProject(source.getProject());
		projectPlan.setDate(new Date());
		projectPlan.setVersion(source.getVersion() + 1);
		projectPlan.setStatus(
				referentielManager.getStatusProjectPlanByFunctionalId(ManagementModuleConstants.PROJECT_PLAN_DRAFT));
		projectPlan.setLots(new HashSet<Lot>());
		projectPlan.setMarkers(new HashSet<Marker>());
		projectPlan.setEstimationComponentDetail(source.getEstimationComponentDetail());
		for (final Lot lot : source.getLots()) {
			if (lot.getParentLot() != null) {
				continue;
			}
			final Lot newLot = duplicateLot(lot, projectPlan, null);
			projectPlan.addLot(newLot);
		}

		for (final Marker marker : source.getMarkers()) {
			final Marker newMarker = duplicateMarker(projectPlan, marker);
			projectPlan.addMarker(newMarker);
		}
		// settings duplication
		final EstimationComponentDetail newEstimationComponentDetail = (EstimationComponentDetail) source
				.getEstimationComponentDetail().clone();
		newEstimationComponentDetail.setFunctionalId(generateEstimationComponentDetailFunctionalId(projectPlan));
		projectPlan.setEstimationComponentDetail(newEstimationComponentDetail);

		final Set<AdjustFactorJointure> setNewAdjustFactorJointure = new HashSet<AdjustFactorJointure>();
		for (final AdjustFactorJointure adjustFactorJointure : source.getAdjustFactorJointure()) {
			final AdjustFactorJointure newAdjustFactorJointure = newAdjustFactorJointure();
			newAdjustFactorJointure.setAdjustFactor(adjustFactorJointure.getAdjustFactor());
			newAdjustFactorJointure.setAdjustWeight(adjustFactorJointure.getAdjustWeight());
			newAdjustFactorJointure.setProjectPlan(projectPlan);
			setNewAdjustFactorJointure.add(newAdjustFactorJointure);
		}
		projectPlan.setAdjustFactorJointure(setNewAdjustFactorJointure);

		return projectPlan;
	}

	/**
	 * @param projectPlan
	 * @param lotName
	 * @param createDate
	 * @param endDate
	 * @param desc
	 * @return
	 */
	private Lot initializeNewLot(final ProjectPlan projectPlan, final String lotName, final Date createDate,
			final Date endDate, final String desc, final Lot lotParent) {
		final Lot initialLot = newLot();
		if (createDate != null) {
			initialLot.setStartDate(Util.clearTime(createDate));
		}
		if (endDate != null) {
			initialLot.setEndDate(Util.clearTime(endDate));
		}
		initialLot.setDescription(desc);
		initialLot.setProjectPlan(projectPlan);
		initialLot.setName(lotName.trim());
		initialLot.setIterations(new HashSet<Iteration>());
		initialLot.setScopeEntities(new HashSet<ScopeUnit>());
		initialLot.setParentLot(lotParent);
		return initialLot;
	}

	private Lot duplicateLot(final Lot source, final ProjectPlan projectPlan, final Lot lotParent)
			throws ManagementModuleException {

		final Lot lot = initializeNewLot(projectPlan, source.getName(), source.getStartDate(), source.getEndDate(),
				source.getDescription(), lotParent);
		lot.setSrcLot(source);

		for (final Lot itLot : source.getChildLots()) {
			final Lot newChildLot = duplicateLot(itLot, projectPlan, lot);
			lot.addChildLot(newChildLot);
		}

		final Map<ScopeUnit, String> childScopeUnitAndDuplicateScopeParentUnitId = new HashMap<ScopeUnit, String>();

		final Map<String, ScopeUnit> duplicateScopeUnitByParentOldScopeUnitId = new HashMap<String, ScopeUnit>();

		/**
		 * si c est un enfant, on le stock et on stock le duplicata de son
		 * parent.
		 */

		for (final ScopeUnit su : source.getScopeEntities()) {
			if (su.getParentScopeUnit() == null) {
				final ScopeUnit newSC = duplicateScopeUnit(su, null);
				lot.addScopeEntity(newSC);
				duplicateScopeUnitByParentOldScopeUnitId.put(su.getUnitId(), newSC);
			} else {
				childScopeUnitAndDuplicateScopeParentUnitId.put(su, su.getParentScopeUnit().getUnitId());
			}
		}

		for (final Map.Entry<ScopeUnit, String> entry : childScopeUnitAndDuplicateScopeParentUnitId.entrySet()) {

			final ScopeUnit newSC = duplicateScopeUnit(entry.getKey(),
					duplicateScopeUnitByParentOldScopeUnitId.get(entry.getValue()));
			lot.addScopeEntity(newSC);
		}

		return lot;
	}

	private void validateLot(final Lot lot) throws ManagementModuleException {
		final Set<Iteration> iterationsToCheck = getIterationsFromLot(lot);

		if (lot.getDescription() == null) {
			throw new ManagementModuleException(ExceptionCode.ERR_LOT_DESCRIPTION_CAN_NOT_BE_NULL);
		}
		if (lot.getName() == null) {
			throw new ManagementModuleException(ExceptionCode.ERR_LOT_NAME_CAN_NOT_BE_NULL);
		}
		if (lot.getStartDate() == null) {
			throw new ManagementModuleException(ExceptionCode.ERR_LOT_START_DATE_CAN_NOT_BE_NULL);
		}
		if (lot.getEndDate() == null) {
			throw new ManagementModuleException(ExceptionCode.ERR_LOT_END_DATE_CAN_NOT_BE_NULL);
		}

		if (lot.getStartDate().after(lot.getEndDate())) {
			throw new ManagementModuleException(ExceptionCode.ERR_LOT_WRONG_DATE);
		}

		// a lot can not have both iterations and lots
		if (lot.getChildLots().size() > 0 && iterationsToCheck.size() > 0) {
			throw new ManagementModuleException(ExceptionCode.ERR_LOT_HAVING_BOTH_LOTS_AND_ITERATIONS);
		}
		// same test for the parent Lot
		if (lot.getParentLot() != null) {
			final Set<Iteration> iterationsToCheckForParentLot = getIterationsFromLot(lot.getParentLot());
			if (iterationsToCheckForParentLot.size() > 0) {
				throw new ManagementModuleException(ExceptionCode.ERR_LOT_HAVING_BOTH_LOTS_AND_ITERATIONS);
			}
		}

		// check if childs lot are in the ParentLot
		if (lot.getChildLots().size() > 0) {
			for (final Lot child : lot.getChildLots()) {
				if (child.getStartDate().before(lot.getStartDate()) || child.getEndDate().after(lot.getEndDate())) {
					throw new ManagementModuleException(ExceptionCode.ERR_LOT_CHILDS_LOT_NOT_IN_PARENT_LOT);
				}
			}
		}

		// two lot can't have the same name in a project plan
		boolean sameName = false;
		final Lot firstLotParent = firstParentLot(lot);

		final List<Lot> listParents = lotDAO.findParentLotsByProjectPlanId(firstLotParent.getProjectPlan().getId());
		for (final Lot lotTemp : listParents) {
			sameName = findSameName(lotTemp, lot);
			if (sameName) {
				break;
			}
		}

		if (sameName) {
			throw new ManagementModuleException(ExceptionCode.ERR_TWO_LOT_HAVING_SAME_NAME_IN_A_PROJECT_PLAN);
		}

		// la date de fin du lot ne peux pas être antérieure a la date de fin
		// d'une de ses itérations
		for (final Iteration it : iterationsToCheck) {
			if (lot.getEndDate().before(it.getEndDate())) {
				throw new ManagementModuleException(ExceptionCode.ERR_LOT_END_DATE_BEFORE_ITERATION_END_DATE);
			}
		}

		if (lot.getParentLot() != null) {
			// un sous lot ne doit pas être en dehors de son lot parent
			if (lot.getParentLot().getStartDate().after(lot.getStartDate())
					|| lot.getParentLot().getEndDate().before(lot.getEndDate())) {
				throw new ManagementModuleException(ExceptionCode.ERR_LOT_WRONG_SUB_LOT_DATE);
			}

			// deux lots de même niveau ne doivent pas se chevaucher
			final Lot lotParent = getCompleteLot(lot.getParentLot().getId());
			for (final Lot child : lotParent.getChildLots()) {
				if (lot.getId() != null && lot.getId().longValue() == child.getId().longValue()) {
					continue;
				}

				if ((lot.getEndDate().before(child.getStartDate())
						|| lot.getStartDate().after(child.getEndDate())) == false) {
					throw new ManagementModuleException(ExceptionCode.ERR_LOT_NO_OVERLAP);
				}
			}
		} else {
			// deux lots de même niveau ne doivent pas se chevaucher
			final ProjectPlan projectPlan = getProjectPlan(lot.getProjectPlan().getId());
			if (projectPlan != null && projectPlan.getLots() != null) {
				final Set<Lot> listeLotsToCheck = new HashSet<Lot>();
				for (final Lot itemLot : projectPlan.getLots()) {
					if ((lot.getId() != null && lot.getId().longValue() != itemLot.getId().longValue()
							|| lot.getId() == null) && itemLot.getParentLot() == null) {
						listeLotsToCheck.add(itemLot);
					}
				}
				for (final Lot itemLot : listeLotsToCheck) {
					if ((lot.getEndDate().before(itemLot.getStartDate())
							|| lot.getStartDate().after(itemLot.getEndDate())) == false) {
						throw new ManagementModuleException(ExceptionCode.ERR_LOT_NO_OVERLAP);
					}
				}
			}
		}
	}

	/**
	 * find the first level of lot
	 * 
	 * @param lotSource
	 * @return
	 * @throws ManagementModuleException
	 */
	private Lot firstParentLot(final Lot lotSource) throws ManagementModuleException {
		if (lotSource.getParentLot() != null) {
			return firstParentLot(lotDAO.findById(lotSource.getParentLot().getId()));
		} else {
			return lotSource;
		}
	}

	/**
	 * Return true if the name of lotSource is already used by lot or its childs
	 * 
	 * @param lot
	 * @param lotSource
	 * @return
	 */
	private boolean findSameName(final Lot lot, final Lot lotSource) {

		if (lotSource.getId() != null) {
			return checkOnUpdate(lot, lotSource);
		} else {
			return checkonOnFirtCreation(lot, lotSource);
		}
	}

	/**
	 * Return true if the name of lotSource is already used by lot or its childs
	 * on creation
	 * 
	 * @param lot
	 * @param lotSource
	 * @return
	 */
	private boolean checkonOnFirtCreation(final Lot lot, final Lot lotSource) {
		boolean sameName = false;
		if (lot.getId() != null && lotSource.getName().equalsIgnoreCase(lot.getName())) {
			return true;
		}
		for (final Lot lotTemp : lot.getChildLots()) {
			if (lotTemp.getId() != null && lotSource.getName().equalsIgnoreCase(lotTemp.getName())) {
				return true;
			}
			if (!lotTemp.getChildLots().isEmpty()) {
				sameName = checkonOnFirtCreation(lotTemp, lotSource);
				if (sameName) {
					return true;
				}
			}
		}
		return sameName;
	}

	/**
	 * Return true if the name of lotSource is already used by lot or its childs
	 * on updating
	 * 
	 * @param lot
	 * @param lotSource
	 * @return
	 */
	private boolean checkOnUpdate(final Lot lot, final Lot lotSource) {
		boolean sameName = false;
		if (lot.getId() != null && lotSource.getId().longValue() != lot.getId().longValue()
				&& lotSource.getName().equalsIgnoreCase(lot.getName())) {
			return true;
		}
		for (final Lot lotTemp : lot.getChildLots()) {
			if (lotTemp.getId() != null && lotSource.getId().longValue() != lotTemp.getId().longValue()
					&& lotSource.getName().equalsIgnoreCase(lotTemp.getName())) {
				return true;
			}
			if (!lotTemp.getChildLots().isEmpty()) {
				sameName = checkOnUpdate(lotTemp, lotSource);
				if (sameName) {
					return true;
				}
			}
		}
		return sameName;
	}

	/**
	 * duplicate Marker without persistence
	 * 
	 * @param source
	 * @return
	 * @throws ManagementModuleException
	 */
	private Marker duplicateMarker(final ProjectPlan projectPlan, final Marker marker) {
		return initializeNewMarker(projectPlan, marker.getDate(), marker.getDescription(), marker.getName(),
				marker.getType());
	}

	/**
	 * Duplicate a scope Unit
	 * 
	 * @param source
	 * @return
	 */
	private ScopeUnit duplicateScopeUnit(final ScopeUnit source, final ScopeUnit parentScope) {
		final ScopeUnit scope = initializeScopeUnit(source.getName(), source.getDescription(), source.getUnitId(),
				source.getVersion(), source.isManual());
		scope.setInScope(source.isInScope());
		scope.setDate(source.getDate());
		scope.setType(source.getType());
		scope.setRefScopeUnit(source.getRefScopeUnit());
		scope.setEstimation(duplicateEstimation(source.getEstimation(), scope));
		scope.setParentScopeUnit(parentScope);
		return scope;
	}

	private LoadDistributionDiscipline duplicateLoadDistributionDiscipline(final LoadDistributionDiscipline source,
			final ProjectPlan projectPlan) throws ManagementModuleException {

		final LoadDistributionDiscipline ldd = newLoadDistributionDiscipline();
		ldd.setDate(source.getDate());
		ldd.setLoad(source.getLoad());
		ldd.setProjectDiscipline(source.getProjectDiscipline());
		ldd.setProjectPlan(projectPlan);

		return ldd;
	}

	/**
	 * This method duplicates the {@link Estimation} in parameter
	 * 
	 * @param pSource
	 * @param newScopeUnit
	 * @return {@link Estimation}
	 * @throws ManagementModuleException
	 */
	private Estimation duplicateEstimation(final Estimation pSource, final ScopeUnit newScopeUnit) {
		if (pSource == null) {
			return null;
		}
		final Estimation ret = newEstimation();
		// FP
		ret.setGDIsimple(pSource.getGDIsimple());
		ret.setGDImedian(pSource.getGDImedian());
		ret.setGDIcomplex(pSource.getGDIcomplex());
		ret.setGDEsimple(pSource.getGDEsimple());
		ret.setGDEmedian(pSource.getGDEmedian());
		ret.setGDEcomplex(pSource.getGDEcomplex());
		ret.setINsimple(pSource.getINsimple());
		ret.setINmedian(pSource.getINmedian());
		ret.setINcomplex(pSource.getINcomplex());
		ret.setOUTsimple(pSource.getOUTsimple());
		ret.setOUTmedian(pSource.getOUTmedian());
		ret.setOUTcomplex(pSource.getOUTcomplex());
		ret.setINTsimple(pSource.getINTsimple());
		ret.setINTmedian(pSource.getINTmedian());
		ret.setINTcomplex(pSource.getINTcomplex());
		ret.setGlobalSimple(pSource.getGlobalSimple());
		ret.setGlobalMedian(pSource.getGlobalMedian());
		ret.setGlobalComplex(pSource.getGlobalComplex());
		ret.setFPRaw(pSource.getFPRaw());
		// Priorization
		ret.setBenefit(pSource.getBenefit());
		ret.setInjury(pSource.getInjury());
		ret.setRisk(pSource.getRisk());
		ret.setWeight(pSource.getBenefit() + pSource.getInjury() + pSource.getRisk());
		// others
		ret.setSimple(pSource.getSimple());
		ret.setManual(pSource.isManual());
		ret.setCharge(pSource.getLastCharge());
		ret.setScopeUnit(newScopeUnit);
		ret.setLastDate(pSource.getLastDate());
		ret.setRemaining(pSource.getRemaining());
		return ret;
	}

	public void setProjectPlanDAO(final ProjectPlanDAO pProjectPlanDAO) {
		projectPlanDAO = pProjectPlanDAO;
	}

	public void setLotDAO(final LotDAO pLotDAO) {
		lotDAO = pLotDAO;
	}

	public void setMarkerDAO(final MarkerDAO pMarkerDAO) {
		markerDAO = pMarkerDAO;
	}

	public void setEstimationComponentDetailDAO(final EstimationComponentDetailDAO pEstimationComponentDetailDAO) {
		estimationComponentDetailDAO = pEstimationComponentDetailDAO;
	}

	public void setScopeUnitDAO(final ScopeUnitDAO pScopeUnitDAO) {
		scopeUnitDAO = pScopeUnitDAO;
	}

	public void setRefScopeUnitDAO(final RefScopeUnitDAO pRefScopeUnitDAO) {
		refScopeUnitDAO = pRefScopeUnitDAO;
	}

	public void setAdjustWeightDAO(final AdjustWeightDAO pAdjustWeightDAO) {
		adjustWeightDAO = pAdjustWeightDAO;
	}

	public void setLoadDistributionDisciplineDAO(final LoadDistributionDisciplineDAO pLoadDistributionDisciplineDAO) {
		loadDistributionDisciplineDAO = pLoadDistributionDisciplineDAO;
	}

	public void setEstimationDAO(final EstimationDAO pEstimationDAO) {
		estimationDAO = pEstimationDAO;
	}

	public void setRefCalendarDAO(final RefCalendarDAO pRefCalendarDAO) {
		refCalendarDAO = pRefCalendarDAO;
	}

	public void setStatusProjectPlanDAO(final StatusProjectPlanDAO pStatusProjectPlanDAO) {
		statusProjectPlanDAO = pStatusProjectPlanDAO;
	}

	public void setScopeUnitDisciplineDAO(final ScopeUnitDisciplineDAO pScopeUnitDisciplineDAO) {
		scopeUnitDisciplineDAO = pScopeUnitDisciplineDAO;
	}

	public void setScopeUnitDisciplineStatusDAO(final ScopeUnitDisciplineStatusDAO pScopeUnitDisciplineStatusDAO) {
		scopeUnitDisciplineStatusDAO = pScopeUnitDisciplineStatusDAO;
	}

	public void setCDOParametersDAO(final CDOParametersDAO pCDOParametersDAO) {
		CDOParametersDAO = pCDOParametersDAO;
	}

	public void setManagementModuleManager(final ManagementModuleManager pManagementModuleManager) {
		managementModuleManager = pManagementModuleManager;
	}

	public void setReferentielManager(final ReferentielManager pReferentielManager) {
		referentielManager = pReferentielManager;
	}

	public void setTaskManager(final TaskManager pTaskManager) {
		taskManager = pTaskManager;
	}

	public void setConnectorCDO(final CDOPerimeterUnitConnector pConnectorCDO) {
		connectorCDO = pConnectorCDO;
	}

	public void setSched(final Scheduler pSched) {
		sched = pSched;
	}

	public void setSchedFact(final SchedulerFactory pSchedFact) {
		schedFact = pSchedFact;
	}

	public void setBusinessObjectFactory(final BusinessObjectFactory pBusinessObjectFactory) {
		businessObjectFactory = pBusinessObjectFactory;
	}

	private final class GanttLotInfoComparator implements Comparator<GanttLotInfo> {
		@Override
		public int compare(final GanttLotInfo o1, final GanttLotInfo o2) {
			if (o1.getStartDateLot() != null) {
				if (o2.getStartDateLot() != null) {
					return o1.getStartDateLot().compareTo(o2.getStartDateLot());
				} else {
					if (o2.getStartDateSubLot() != null) {
						return o1.getStartDateLot().compareTo(o2.getStartDateSubLot());
					} else {
						return o1.getStartDateLot().compareTo(o2.getMarkerDate());
					}
				}
			} else {
				if (o1.getStartDateSubLot() != null) {
					if (o2.getStartDateLot() != null) {
						return o1.getStartDateSubLot().compareTo(o2.getStartDateLot());
					} else {
						if (o2.getStartDateSubLot() != null) {
							return o1.getStartDateSubLot().compareTo(o2.getStartDateSubLot());
						} else {
							return o1.getStartDateSubLot().compareTo(o2.getMarkerDate());
						}
					}
				} else {
					if (o2.getStartDateLot() != null) {
						return o1.getMarkerDate().compareTo(o2.getStartDateLot());
					} else {
						if (o2.getStartDateSubLot() != null) {
							return o1.getMarkerDate().compareTo(o2.getStartDateSubLot());
						} else {
							return o1.getMarkerDate().compareTo(o2.getMarkerDate());
						}
					}
				}
			}
		}
	}

}
