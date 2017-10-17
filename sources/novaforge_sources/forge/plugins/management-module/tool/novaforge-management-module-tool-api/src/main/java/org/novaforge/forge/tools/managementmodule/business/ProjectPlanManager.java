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
package org.novaforge.forge.tools.managementmodule.business;

import org.novaforge.forge.tools.managementmodule.domain.AdjustFactorJointure;
import org.novaforge.forge.tools.managementmodule.domain.AdjustWeight;
import org.novaforge.forge.tools.managementmodule.domain.CDOParameters;
import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.Estimation;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentDetail;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentSimple;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.LoadDistributionDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.Marker;
import org.novaforge.forge.tools.managementmodule.domain.MarkerType;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.RefCalendar;
import org.novaforge.forge.tools.managementmodule.domain.RefScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDisciplineStatus;
import org.novaforge.forge.tools.managementmodule.domain.StatusProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.report.DayLoad;
import org.novaforge.forge.tools.managementmodule.domain.report.GanttLotInfo;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ProjectPlanManager {
//TODO ajouter les javadocs sur les methodes restantes
   
	/**
	 * Constant which represents the projectId key where are stored default
	 * transformation values
	 */
	String DEFAULT_ESTIMATION_DETAIL_FUNCTIONAL_ID = "default";
	Float DEFAULT_ESTIMATION_DETAIL_ADJUSTEMENT_COEF = 0.65f;

	/************************* ProjectPlan ********************************************************************/

	/**
	 * Return a new Instance of ProjectPlan
	 * 
	 * @return
	 */
	ProjectPlan newProjectPlan();

	/**
	 * create the ProjectPlan
	 * 
	 * @param projectPlan
	 * @return
	 * @throws ManagementModuleException
	 */
	ProjectPlan creeteProjectPlan(ProjectPlan projectPlan) throws ManagementModuleException;

	/**
	 * create the ProjectPlan
	 * 
	 * @param String
	 * @return ProjectPlan
	 * @throws ManagementModuleException
	 */
	ProjectPlan creeteProjectPlan(String pProjectId) throws ManagementModuleException;

	/**
	 * Merge projectPlan
	 * 
	 * @param projectPlan
	 * @return
	 * @throws ManagementModuleException
	 */
	ProjectPlan updateProjectPlan(ProjectPlan projectPlan) throws ManagementModuleException;

	/**
	 * Delete the ProjectPlan
	 * 
	 * @param projectPlanId
	 * @throws ManagementModuleException
	 */
	boolean deleteProjectPlan(Long projectPlanId) throws ManagementModuleException;

	/**
	 * 
	 * @param projectPlanId
	 * @return
	 * @throws ManagementModuleException
	 */
	ProjectPlan getProjectPlan(Long projectPlanId) throws ManagementModuleException;

	/**
	 * Return a Project's plan for a project and a version
	 * 
	 * @param pProjectId
	 * @param version
	 * @return
	 * @throws ManagementModuleException
	 */
	ProjectPlan getProjectPlan(String pProjectId, int version) throws ManagementModuleException;

	/**
	 * Return the draft version of a ProjectPlan for the ProjectId
	 * 
	 * @param pProjectId
	 * @return
	 * @throws ManagementModuleException
	 */
	ProjectPlan getProjectPlanDraftVersion(String pProjectId) throws ManagementModuleException;

	/**
	 * Return Project's plans for a project
	 * 
	 * @param pProjectId
	 * @return
	 * @throws ManagementModuleException
	 */
	List<ProjectPlan> getProjectPlanList(String pProjectId) throws ManagementModuleException;

	/**
	 * Return the max version of plans project versionned for a project
	 * 
	 * @param pProjectId
	 * @return
	 * @throws DataAccessException
	 */
	Long findMaxNumVersion(String pProjectId) throws ManagementModuleException;

	/**
	 * Return the last projectPlan of a project, either the draft or the last
	 * version
	 * 
	 * @param pProjectId
	 * @return ProjectPlan
	 * @throws ManagementModuleException
	 */
	ProjectPlan getLastProjectPlan(String pProjectId) throws ManagementModuleException;

	/**
	 * Return the last validated projectPlan of a project
	 * 
	 * @param pProjectId
	 * @return ProjectPlan
	 * @throws ManagementModuleException
	 */
	ProjectPlan getLastValidatedProjectPlan(String pProjectId) throws ManagementModuleException;

	/**
	 * Validate the ProjectPlan of the project
	 * 
	 * @param pProjectId
	 * @return the project plan validated
	 * @throws ManagementModuleException
	 */
	ProjectPlan validateProjectPlan(String pProjectId) throws ManagementModuleException;

	/************************* Lot ********************************************************************/

	/**
	 * return an instance of Lot
	 */
	Lot newLot();

	/**
	 * create a Lot
	 * 
	 * @param lot
	 * @return
	 * @throws ManagementModuleException
	 */
	Lot creeteLot(Lot lot) throws ManagementModuleException;

	/**
	 * create a Lot
	 * 
	 * @param projectPlan
	 * @param projectName
	 * @param createDate
	 * @param parentLot
	 * @param desc
	 * @param iterations
	 * @param scopeEntities
	 * @return
	 * @throws ManagementModuleException
	 */
	Lot creeteLot(ProjectPlan projectPlan, String projectName, Date createDate, Date endDate, String desc,
			Set<Iteration> iterations, Set<ScopeUnit> scopeUnits, Lot lotParent)
			throws ManagementModuleException;

	/**
	 * return a lot
	 * 
	 * @param lotName
	 * @return
	 * @throws ManagementModuleException
	 */
	Lot getLot(Long lotId) throws ManagementModuleException;

	/**
	 * return a lot
	 * 
	 * @param lotName
	 * @return
	 * @throws ManagementModuleException
	 */
	Lot getCompleteLot(Long lotId) throws ManagementModuleException;

	/**
	 * return only parentLot (not sub lots) identified by theres projectPlan
	 * 
	 * @param projectPlanId
	 * @return
	 * @throws ManagementModuleException
	 */
	List<Lot> getParentLotsList(Long projectPlanId) throws ManagementModuleException;

	
	/**
	 * return a lot fully loaded identified by his projectPlan
	 *  
	 * @param projectPlanId
	 * @return
	 * @throws ManagementModuleException
	 */
	List<Lot> getCompleteListLots(Long projectPlanId) throws ManagementModuleException;

	/**
	 * Update a Lot
	 * 
	 * @param lot
	 * @throws ManagementModuleException
	 */
	boolean updateLot(Lot lot) throws ManagementModuleException;

	/**
	 * Delete a Lot
	 * 
	 * @param lotName
	 * @throws ManagementModuleException
	 */
	boolean deleteLot(Long lotId) throws ManagementModuleException;

	/**
	 * Get the iterations for a lot (manage the case of a draft over a validated
	 * version)
	 * 
	 * @param lot
	 *            the lot to use
	 * @return the iteration set
	 */
	Set<Iteration> getIterationsFromLot(Lot lot);
	
	/**
	 * Return infos for reports
	 * @param lotId
	 * @param subLotId
	 * @param disciplineFunctionalId
	 * @return
	 * @throws ManagementModuleException
	 */
   String getLotMetadatas(Long lotId, Long subLotId, String disciplineFunctionalId)
         throws ManagementModuleException;

	/************************* Marker ********************************************************************/

	/**
	 * This method returns an instance of Marker
	 * 
	 * @return Marker
	 */
	Marker newMarker();

	/**
	 * create and validate a New Marker
	 * 
	 * @param marker
	 * @throws ManagementModuleException
	 */
	Marker creeteMarker(Marker marker) throws ManagementModuleException;

	/**
	 * create and validate a New Marker
	 * 
	 * @param projectPlan
	 * @param name
	 * @param date
	 * @param desc
	 * @param type
	 * @return
	 * @throws ManagementModuleException
	 */
	Marker creeteMarker(ProjectPlan projectPlan, String name, Date date, String desc, MarkerType type)
			throws ManagementModuleException;

	/**
	 * update and validate a Marker
	 * 
	 * @param marker
	 * @return
	 * @throws ManagementModuleException
	 */
	Marker updateMarker(Marker marker) throws ManagementModuleException;

	/**
	 * return a marker
	 * 
	 * @param id
	 * @return
	 * @throws ManagementModuleException
	 */
	Marker getMarker(Long id) throws ManagementModuleException;

	/**
	 * Delete a Marker
	 * 
	 * @param markerId
	 * @return
	 * @throws ManagementModuleException
	 */
	boolean deleteMarker(Long markerId) throws ManagementModuleException;

	/**
	 * Return a marker's list for a project plan
	 * 
	 * @param projectPlanId
	 * @return
	 * @throws ManagementModuleException
	 */
	List<Marker> getMarkerList(Long projectPlanId) throws ManagementModuleException;

	/************************* EstimationComponentDetail ********************************************************************/

	/**
	 * return new instance of EstimationComponentDetail
	 * 
	 * @return
	 */
	EstimationComponentDetail newEstimationComponentDetail();

	/**
	 * persist an estimationComponentDetail 
	 * @param sf
	 * @return
	 * @throws ManagementModuleException
	 */
	EstimationComponentDetail createEstimationComponentDetail(EstimationComponentDetail sf)
			throws ManagementModuleException;

	/**
	 * Get the default EstimationComponentDetail to initialize datas
	 * 
	 * @return the default EstimationComponentDetail
	 * @throws ManagementModuleException
	 *             problem during init
	 */
	EstimationComponentDetail getDefaultEstimationComponentDetail() throws ManagementModuleException;

	/**
	 * This method generates the functionalId of an EstimationComponentDetail
	 * 
	 * @param projectPlan
	 *            the projectPlan to use
	 * @return the functionalId
	 */
	String generateEstimationComponentDetailFunctionalId(ProjectPlan projectPlan);

	/************************* ScopeUnit ********************************************************************/

	/**
	 * Return a new Instance of ScopeUnit
	 * 
	 * @return
	 */
	ScopeUnit newScopeUnit();

	/**
	 * Persist a new ScopeUnit
	 * @param scope
	 * @return
	 * @throws ManagementModuleException
	 */
	ScopeUnit creeteScopeUnit(ScopeUnit scope) throws ManagementModuleException;

	/**
	 * Persist a new ScopeUnit with specified parameters
	 * 
	 * @param pScopeName
	 * @param pDescription
	 * @param pScopeUnitId
	 * @param pScopeType
	 * @param version
	 * @param isManual
	 * @param pScopeLotId
	 * @param pParentScopeId
	 * @return
	 * @throws ManagementModuleException
	 */
	ScopeUnit creeteScopeUnit(String pScopeName, String pDescription, String pScopeUnitId, String pScopeType,
			String version, boolean isManual, Long pScopeLotId, String pParentScopeUnitId) throws ManagementModuleException;

	/**
	 * Generate the unitId for a manual scopeUnit or an unlinked scopeUnit
	 * 
	 * @param projectId
	 * @param scopeName
	 * @param projectPlanVersion
	 * @return
	 */
   String generateManualUnitId(String projectId, String scopeName, Integer projectPlanVersion);
	
	/**
	 * return the scopeUnit for specified  unitId
	 * @param scopeUnitId
	 * @return
	 * @throws ManagementModuleException
	 */
	ScopeUnit getScopeUnit(String scopeUnitId) throws ManagementModuleException;

	/**
	 * link a scopeUnit to a lot
	 * @param pScopeUnitId
	 * @param pScopeLotId
	 * @return
	 * @throws ManagementModuleException
	 */
	boolean linkScopeUnitToLot(String pScopeUnitId, Long pScopeLotId) throws ManagementModuleException;

	/**
	 * Update a scopeUnit 
	 * @param scope
	 * @return
	 * @throws ManagementModuleException
	 */
	boolean updateScopeUnit(ScopeUnit scope) throws ManagementModuleException;

	/**
	 * Update fields of a scopeUnit
	 * @param pScopeUnitId
	 * @param pScopeName
	 * @param description
	 * @param version
	 * @param pScopeLotId
	 * @param pParentScopeUnitId
	 * @param pType
	 * @return
	 * @throws ManagementModuleException
	 */
	boolean updateManualScopeUnit(String pScopeUnitId, String pScopeName, String description, String version,
			Long pScopeLotId, String pParentScopeUnitId, String pType) throws ManagementModuleException;

	/**
	 * delete ScopeUnit and its childs
	 * @param pScopeUnitId
	 * @return
	 * @throws ManagementModuleException
	 */
	boolean deleteScopeUnit(String pScopeUnitId) throws ManagementModuleException;

	/**
	 * Return true if a scopeUnit is linked to a refScopeUnit identified by its unitId and version
	 * @param scope
	 * @param existingRefScopeId
	 * @param existingRefScopeVersion
	 * @return
	 * @throws ManagementModuleException
	 */
	boolean linkExistingScopeUnit(ScopeUnit scope, String existingRefScopeId, String existingRefScopeVersion)
			throws ManagementModuleException;

	/**
	 * Delete the link between a scopeUnit and a refScopeUnit
	 * @param scope
	 * @return
	 * @throws ManagementModuleException
	 */
	boolean unLinkExistingScopeUnit(ScopeUnit scope) throws ManagementModuleException;

	/**
	 * Create scopeUnit (and sub scopeUnit) from refScopeUnit
	 * 
	 * @param unitId
	 * @param version
	 * @param lotId
	 * @param ref
	 * @param parent
	 * @return
	 * @throws ManagementModuleException
	 */
	ScopeUnit creeteScopeUnitFromRefScopeUnit(String unitId, String version, Long lotId, ScopeUnit parent)
			throws ManagementModuleException;

	/**
	 * return a list of ScopeUnit link to a RefScopeUnit
	 * 
	 * @param refUnitId
	 * @return
	 * @throws ManagementModuleException
	 */
	List<ScopeUnit> findScopedRefUnit(String refUnitId) throws ManagementModuleException;

	/**
	 * Search a scopeUnit linked to the refScopeUnit identified by its unitId and version
	 * 
	 * @param refUnitId
	 * @param refVersion
	 * @param projectPlanId
	 * @return
	 * @throws ManagementModuleException
	 */
	ScopeUnit findScopedRefVersionUnit(String refUnitId, String refVersion, Long projectPlanId) throws ManagementModuleException;

	/**
	 * Return a list of scopeUnit linked to lot attached to a projectPlan
	 * 
	 * @param pProjectPlanId
	 * @return
	 * @throws ManagementModuleException
	 */
	List<ScopeUnit> findScopeUnitListByProjectPlanId(Long pProjectPlanId) throws ManagementModuleException;
	
	/**
	 * determinate old unitId from the current unitId
	 * 
	 * @param oldVersion
	 * @param digitsToRemove
	 * @param currentUnitId
	 * @return
	 */
   String determinateOldUnitIdFromCurrentScopeUnitId(Integer oldVersion, int digitsToRemove,
         String currentUnitId);

   /**
    * determinate new unitId from the current unitId
    * 
    * @param newVersion
    * @param digitsToRemove
    * @param currentUnitId
    * @return
    */
   String determinateNewScopeUnitIdFromCurrentScopeUnitId(Integer newVersion, int digitsToRemove,
         String currentUnitId);
	
	/************************* RefScopeUnit ********************************************************************/

	RefScopeUnit newRefScopeUnit();

	RefScopeUnit creeteRefScopeUnit(RefScopeUnit refScope) throws ManagementModuleException;

	RefScopeUnit getRefScopeUnit(String refScopeUnitId, String version) throws ManagementModuleException;

	RefScopeUnit getCompleteRefScopeUnit(String refScopeUnitId, String version)
			throws ManagementModuleException;

	RefScopeUnit getRefScopeUnitByName(String refScopeUnitName, String refScopeUnitVersion)
			throws ManagementModuleException;

	boolean updateRefScopeUnit(RefScopeUnit refScope) throws ManagementModuleException;

	/**
	 * Return all refScopeUnit non-obsolete linked to a project in there last version.
	 * @param projectId
	 * @return
	 * @throws ManagementModuleException
	 */
	List<RefScopeUnit> getLastVersionRefScopeUnit(String projectId) throws ManagementModuleException;

	/**
	 * Update table RefScopeUnitfrom CDO server (call
	 * updateTasksFromCDORefScopeUnit(), updateUseCasesFromCDORefScopeUnit() and
	 * updateUserStoriesFromCDORefScopeUnit())
	 * 
	 * @param nom
	 *            ou ip host cdo
	 * @param port
	 *            cdo 2036 par defaut
	 * @param nom
	 *            repository cdo
	 * @param nom
	 *            projet cdo
	 * @param nom
	 *            du system graal ceee dans le projet
	 * @return boolean
	 * @throws ManagementModuleException
	 */
	boolean updateFromCDORefScopeUnit(String host, String port, String repoCdo, String projet,
			String systemGraal, Project projet_forge, CDOParameters cdoParameters) throws ManagementModuleException;

	/**
	 * Update table RefScopeUnitfrom CDO server (Graal Task)
	 * 
	 * @param nom
	 *            projet cdo
	 * @param nom
	 *            du system graal ceee dans le projet
	 * @return boolean
	 * @throws ManagementModuleException
	 */
	boolean updateTasksFromCDORefScopeUnit(String host, String port, String repoCdo, String projet,
			String systemGraal, Project projet_forge, CDOParameters cdoParameters) throws ManagementModuleException;

	/**
	 * Update table RefScopeUnitfrom CDO server (Graal Use Case)
	 * 
	 * @param nom
	 *            projet cdo
	 * @param nom
	 *            du system graal ceee dans le projet
	 * @return boolean
	 * @throws ManagementModuleException
	 */
	boolean updateUseCasesFromCDORefScopeUnit(String host, String port, String repoCdo, String projet,
			String systemGraal, Project projet_forge, CDOParameters cdoParameters) throws ManagementModuleException;

	/**
	 * Update table RefScopeUnitfrom CDO server (Graal User Story)
	 * 
	 * @param nom
	 *            projet cdo
	 * @param nom
	 *            du system graal ceee dans le projet
	 * @return boolean
	 * @throws ManagementModuleException
	 */
	boolean updateUserStoriesFromCDORefScopeUnit(String host, String port, String repoCdo, String projet,
			String systemGraal, Project projet_forge, CDOParameters cdoParameters) throws ManagementModuleException;

	/************************* AdjustWeight ********************************************************************/

	AdjustWeight newAdjustWeight();

	AdjustWeight creeteAdjustWeight(final AdjustWeight adjustW) throws ManagementModuleException;

	/************************* LoadDistributionDiscipline ********************************************************************/

	/**
	 * return a new instance of LoadDistributionDiscipline
	 * 
	 * @return the new instance
	 * @throws ManagementModuleException
	 */
	LoadDistributionDiscipline newLoadDistributionDiscipline();

	/**
	 * Persist a LoadDistributionDiscipline
	 * 
	 * @param loadDistributionDiscipline
	 *            the entity to persist
	 * @return the persisted entity
	 * @throws ManagementModuleException
	 */
	LoadDistributionDiscipline creeteLoadDistributionDiscipline(
			final LoadDistributionDiscipline loadDistributionDiscipline) throws ManagementModuleException;

	/**
	 * Persist a collection of LoadDistributionDiscipline
	 * 
	 * @param collection
	 *            of loadDistributionDiscipline to persist
	 * @return the persisted entity
	 * @throws ManagementModuleException
	 */
	List<LoadDistributionDiscipline> creeteLoadDistributionDiscipline(
			final List<LoadDistributionDiscipline> loadDistributionDisciplines)
			throws ManagementModuleException;

	/**
	 * Update field of a LoadDistributionDiscipline
	 * 
	 * @param loadDistributionDiscipline
	 *            the LoadDistributionDiscipline to update
	 * @return true if updated
	 * @throws ManagementModuleException
	 */
	boolean updateLoadDistributionDiscipline(LoadDistributionDiscipline loadDistributionDiscipline)
			throws ManagementModuleException;

	/**
	 * Update collection of LoadDistributionDiscipline
	 * 
	 * @param collection
	 *            of loadDistributionDiscipline to update
	 * @return true if all instances are updated
	 * @throws ManagementModuleException
	 */
	boolean updateLoadDistributionDiscipline(List<LoadDistributionDiscipline> loadDistributionDisciplines)
			throws ManagementModuleException;

	/**
	 * Return all the LoadDistributionDiscipline for a projectPlan
	 * 
	 * @param projectPlanId
	 * @return a list of LoadDistributionDiscipline
	 * @throws ManagementModuleException
	 */
	List<LoadDistributionDiscipline> getLoadDistributionDisciplineList(final Long projectPlanId)
			throws ManagementModuleException;

	/**
	 * Delete a LoadDistributionDiscipline
	 * 
	 * @param lddId
	 * @throws ManagementModuleException
	 */
	boolean deleteLoadDistributionDiscipline(Long lddId) throws ManagementModuleException;

	/**
	 * Delete all LoadDistributionDiscipline for a projectPlan
	 * 
	 * @param projectPlanId
	 * @throws ManagementModuleException
	 */
	boolean deleteLoadDistributionDisciplinesByProjectPlanId(Long projectPlanId)
			throws ManagementModuleException;

	/**
	 * Delete a collection of LoadDistributionDiscipline
	 * 
	 * @param lddToDelete
	 * @throws ManagementModuleException
	 */
	boolean deleteLoadDistributionDisciplines(List<Long> lddToDelete) throws ManagementModuleException;

	/**
	 * Manage collection of LoadDistributionDisciplines(Create Update Delete)
	 * 
	 * @param lddToCreate
	 *            a collection of LoadDistributionDisciplines to persist
	 * @param lddToUpdate
	 *            a collection of LoadDistributionDisciplines to update
	 * @param lddToDelete
	 *            a collection of LoadDistributionDisciplines to delete
	 * @return
	 * @throws ManagementModuleException
	 */
	boolean manageLoadDistributionDisciplines(List<LoadDistributionDiscipline> lddToCreate,
			List<LoadDistributionDiscipline> lddToUpdate, List<Long> lddToDelete)
			throws ManagementModuleException;

	/************************* Estimation ********************************************************************/

	Estimation newEstimation();

	Estimation creeteEstimation(final Estimation pEstimation) throws ManagementModuleException;

	Estimation getEstimationByScopeUnit(String pScopeUnitId) throws ManagementModuleException;

	boolean existEstimation(String pScopeUnitId) throws ManagementModuleException;

	boolean updateEstimation(Estimation pEstimation) throws ManagementModuleException;

	/**
	 * Update a list of estimations with the given component.
	 * Use when settings are updated
	 * @param estimations
	 * @param estimationComponentSimple
	 * @param estimationComponentDetail
	 * @return
	 * @throws ManagementModuleException
	 */
	boolean updateEstimations(List<Estimation> estimations, EstimationComponentSimple estimationComponentSimple,
			EstimationComponentDetail estimationComponentDetail) throws ManagementModuleException;
	/**
	 * Return all estimation of scopeUnit included in the projectPlan in
	 * parameter
	 * 
	 * @param pProjectPlanId
	 * @return the list of estimation
	 * @throws ManagementModuleException 
	 */
	List<Estimation> getEstimations(Long pProjectPlanId) throws ManagementModuleException;


	/************************* RefCalendar ********************************************************************/

	RefCalendar newRefCalendar();

	RefCalendar creeteRefCalendar(RefCalendar refCalendar) throws ManagementModuleException;

	/************************* StatusProjectPlan ********************************************************************/
	/**
	 * Return a new Instance of StatusProjectPlan
	 * 
	 * @return
	 */
	StatusProjectPlan newStatusProjectPlan();

	StatusProjectPlan creeteStatusProjectPlan(StatusProjectPlan statusProjectPlan)
			throws ManagementModuleException;

	/************************* ScopeUnitDisciplineStatus ********************************************************************/

	/**
	 * Return a new Instance of ScopeUnitDisciplineStatus
	 * 
	 * @return ScopeUnitDisciplineStatus
	 */
	ScopeUnitDisciplineStatus newScopeUnitDisciplineStatus();

	/**
	 * Create a new ScopeUnitDisciplineStatus
	 * 
	 * @param scopeUnitDisciplineStatus
	 *            the status to create
	 * @return the created ScopeUnitDisciplineStatus
	 * @throws ManagementModuleException
	 *             problem during creation
	 */
	ScopeUnitDisciplineStatus createScopeUnitDisciplineStatus(
			ScopeUnitDisciplineStatus scopeUnitDisciplineStatus) throws ManagementModuleException;

	/**
	 * Get a ScopeUnitDisciplineStatus by its functional id
	 * 
	 * @param functionalId
	 *            the functional ID
	 * @return the ScopeUnitDisciplineStatus (or null if not found)
	 * @throws ManagementModuleException
	 *             problem during execution
	 */
	ScopeUnitDisciplineStatus getScopeUnitDisciplineStatus(String functionalId)
			throws ManagementModuleException;

	/**
	 * Get the appropriate ScopeUnitDiscipline for the arguments
	 * 
	 * @param scopeId
	 *            the scope id to use for search
	 * @param discipline
	 *            the discipline to use for search
	 * @return the appropriate ScopeUnitDiscipline or null
	 * @throws ManagementModuleException
	 *             problem during process
	 */
	ScopeUnitDiscipline getScopeUnitDiscipline(long scopeId, Discipline discipline)
			throws ManagementModuleException;

	/**
	 * Return the all list of ScopeUnitDiscipline
	 * 
	 * @param projectPlanId
	 * @return the list of ScopeUnitDiscipline or empty list
	 * @throws ManagementModuleException
	 */
	Set<ScopeUnitDiscipline> getScopeUnitDisciplines(Long pProjectPlanId) throws ManagementModuleException;

	/**
	 * Return all ScopeUnitDiscipline in relation with the given parameter
	 * 
	 * @param scopeUnitId
	 * @return the list of ScopeUnitDiscipline
	 * @throws ManagementModuleException
	 */
	List<ScopeUnitDiscipline> findScopeUnitDisciplinesByScopeUnitId(Long scopeUnitId)
			throws ManagementModuleException;

	/**
	 * Return all ScopeUnitDiscipline in relation with the given parameter
	 * @param scopeUnitIds
	 * @return
	 * @throws ManagementModuleException
	 */
   List<ScopeUnitDiscipline> findScopeUnitDisciplinesByScopeUnitIds(List<Long> scopeUnitIds)
   throws ManagementModuleException;
	
	/**
	 * Return a new Instance of ScopeUnitDiscipline
	 * 
	 * @return ScopeUnitDiscipline
	 */
	ScopeUnitDiscipline newScopeUnitDiscipline();

	/**
	 * Create a new ScopeUnitDiscipline
	 * 
	 * @param scopeUnitDiscipline
	 *            , the ScopeUnitDiscipline to create
	 * @return the created ScopeUnitDiscipline
	 * @throws ManagementModuleException
	 */
	ScopeUnitDiscipline creeteScopeUnitDiscipline(ScopeUnitDiscipline scopeUnitDiscipline)
			throws ManagementModuleException;

	/**
	 * Delete the scope unit - discipline relation with given parameters
	 * 
	 * @param scopeUnitId
	 * @param functionalId
	 * @return a boolean, attesting the success
	 * @throws ManagementModuleException
	 */
	boolean deleteScopeUnitDiscipline(String scopeUnitId, String functionalId)
			throws ManagementModuleException;

	/**
	 * Change the scope unit - discipline relation status with given parameters
	 * 
	 * @param scopeUnitId
	 * @param functionalId
	 * @return a boolean which is true if status has been update or false if
	 *         there is still a not terminated task in relation with this couple
	 * @throws ManagementModuleException
	 */
	boolean terminateScopeUnitDiscipline(String scopeUnitId, String functionalId)
			throws ManagementModuleException;

	/**
	 * This method get the default list of AdjustFactor
	 * 
	 * @param List
	 *            <AdjustFactorJointure>
	 * @return AdjustFactorJointure list
	 */
	List<AdjustFactorJointure> getDefaultAdjustfactorJointureProjectPlan()
			throws ManagementModuleException;

	AdjustFactorJointure newAdjustFactorJointure();

	/**
	 * This method returen the last fullproject plan (draft o validated)
	 * 
	 * @param String
	 * @return ProjectPlan
	 */
	ProjectPlan getLastFullProjectPlan(String pProjectId) throws ManagementModuleException;

	List<DayLoad> getLoadsByDateByProjectPlanId(Long projectPlanId) throws ManagementModuleException;

	/**
	 * return a list of GanttLotInfo for a Gantt diagram
	 * 
	 * @param projectPlanId
	 * @return
	 * @throws ManagementModuleException
	 */
	List<GanttLotInfo> getGanttLotInfosByProjectPlanId(Long projectPlanId) throws ManagementModuleException;

	/************************* CDOParameters ********************************************************************/

	/**
	 * Return a new Instance of CDOParameters
	 * 
	 * @return CDOParameters
	 */
	CDOParameters newCDOParameters();

	/**
	 * Create a new CDOParameters
	 * 
	 * @param pCDOParameters
	 *            , the CDOParameters to create
	 * @return the created CDOParameters
	 * @throws ManagementModuleException
	 */
	CDOParameters creeteCDOParameters(CDOParameters pCDOParameters) throws ManagementModuleException;
   
	/**
    * Create a Collections of new CDOParameters
    * 
    * @param pCDOParameters
    *            , the CDOParameters to create
    * @throws ManagementModuleException
    */
   void creeteCDOParameters(List<CDOParameters> pCDOParameters) throws ManagementModuleException;
   
	/**
	 * update CDOParameters
	 * 
	 * @param pCDOParameters
	 *            , the CDOParameters to update
	 * @return result of merge
	 * @throws ManagementModuleException
	 */
	boolean updateCDOParameters(CDOParameters pCDOParameters) throws ManagementModuleException;

	/**
    * update a collection of CDOParameters
    * 
    * @param pCDOParameters
    *            , the CDOParameters to update
    * @return result of merge
    * @throws ManagementModuleException
    */
   boolean updateCDOParameters(List<CDOParameters> pCDOParameters) throws ManagementModuleException;
	
	/**
	 * load the CDOParameters of the project by id 
	 * 
	 * @param the
	 *            id of base
	 * @return the CDOParameters of the project
	 * @throws ManagementModuleException
	 */
	CDOParameters findCDOParameters(Long cdoParametersId) throws ManagementModuleException;
	
	/**
	 * load the list CDOParameters of the project
	 * 
	 * @param the
	 *            id of the project
	 * @return the list of CDOParameters of the project
	 * @throws ManagementModuleException
	 */
	List<CDOParameters> findAllCDOParameters(String pProjectId) throws ManagementModuleException;

	/**
	 * Delete all the ScopeUnitDisciplines of a project plan
	 * 
	 * @param projectPlanId
	 *            the id
	 * @return true if OK
	 * @throws ManagementModuleException
	 *             problem during operation
	 */
	boolean deleteScopeUnitDisciplinesByProjectPlanId(Long projectPlanId) throws ManagementModuleException;

	/**
	 * Delete all the ScopeUnit of a project plan
	 * 
	 * @param projectPlanId
	 *            the id
	 * @throws ManagementModuleException
	 *             problem during operation
	 */
	void deleteScopeUnitsByProjectPlanId(Long projectPlanId) throws ManagementModuleException;

	/**
	 * Calculate the adjustementCoef with the given sum of adjustement factor's weight
	 * @param sumAdjustementFactor
	 * @return the adjustementCoef
	 */
	Float calculateAdjustementCoef(Integer sumAdjustementFactor);

	/**
    * Delete a collection of cdo parameters and its links in RefScopeUnit and in ScopeUnit (obsolescence or delete)
    * @param cdoParameters
    * @return result
    */
   boolean deleteCDOParameters(List<CDOParameters> cdoParameters) throws ManagementModuleException;
	
	/**
	 * Delete a cdo parameters and its links in RefScopeUnit and in ScopeUnit (obsolescence or delete)
	 * @param cdoParameters
	 * @return result
	 */
	boolean deleteCDOParameters(CDOParameters cdoParameters) throws ManagementModuleException;

	/************************* ********************************************************************/
	
	/**
    * 
    * Return the ideal FP of the given scopeUnits
    * 
    * @param scopeUnits2
    * @return a Float, the ideal FP
    * @throws ManagementModuleException
    */
   Float calculateIdealScopeUnitFP(final List<ScopeUnit> scopeUnits, final String projectId)
         throws ManagementModuleException;
}
