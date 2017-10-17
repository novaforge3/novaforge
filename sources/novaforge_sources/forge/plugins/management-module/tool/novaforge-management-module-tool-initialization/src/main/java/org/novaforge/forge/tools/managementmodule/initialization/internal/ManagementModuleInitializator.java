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
package org.novaforge.forge.tools.managementmodule.initialization.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.managementmodule.business.ManagementModuleManager;
import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.business.ReferentielManager;
import org.novaforge.forge.tools.managementmodule.business.TaskManager;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.domain.AdjustFactor;
import org.novaforge.forge.tools.managementmodule.domain.AdjustWeight;
import org.novaforge.forge.tools.managementmodule.domain.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.domain.ApplicativeRights;
import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentDetail;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentSimple;
import org.novaforge.forge.tools.managementmodule.domain.Language;
import org.novaforge.forge.tools.managementmodule.domain.MarkerType;
import org.novaforge.forge.tools.managementmodule.domain.PhaseType;
import org.novaforge.forge.tools.managementmodule.domain.Role;
import org.novaforge.forge.tools.managementmodule.domain.ScopeType;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDisciplineStatus;
import org.novaforge.forge.tools.managementmodule.domain.StatusProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.StatusScope;
import org.novaforge.forge.tools.managementmodule.domain.StatusTask;
import org.novaforge.forge.tools.managementmodule.domain.SteeringParameter;
import org.novaforge.forge.tools.managementmodule.domain.TaskType;
import org.novaforge.forge.tools.managementmodule.domain.Transformation;
import org.novaforge.forge.tools.managementmodule.domain.UnitTime;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.osgi.framework.BundleContext;

import java.io.File;

/**
 * @author vvigo
 */
public class ManagementModuleInitializator
{

	/**
	 * 
	 */
	private static final Log						LOG												 = LogFactory
																																			.getLog(ManagementModuleInitializator.class);
	private static final String LOC_FILE = "initialization.lock";
	private File												workingDir;
	private BundleContext							 bundleContext;
	private ManagementModuleManager		 managementModuleManager;

	private ProjectPlanManager					projectPlanManager;

	private TaskManager								 taskManager;

	private ReferentielManager					referentielManager;

	private ManagementFileConfiguration managementFileConfiguration = ManagementFileConfiguration.getInstance();

	public void starting()
	{
		workingDir = bundleContext.getDataFile(LOC_FILE);
		try
		{
			if (!workingDir.exists())
			{
				boolean init = true;
				if (init)
				{
					LOG.info("Initializing starting...");
					// Initializator
					createLanguages();
					createRoles();
					createUnitTimes();
					createStatusProjectPlans();
					createMarkerTypes();
					createScopeTypes();
					createStatusScope();
					createAdjustFactors();
					createAjdustWeights();
					createTaskTypes();
					createTaskCategories();
					createTaskStatus();
					createSteeringParameter();
					createTransformation();
					createDisciplines();
					createEstimationComponentSimple();
					createEstimationComponentDetail();
					createPhaseTypes();
					createApplicativeFunctions();
					createApplicativeRights();
					createDisciplinesRights();
					initializeScopeUnitDisciplineStatus();
					// loadParaReferentiel();
				}

				LOG.info("Initializing done.");
				workingDir.createNewFile();
			}
			else
			{
				LOG.info("Initialize Management Module datas already done.");
			}
		}
		catch (Exception e)
		{
			LOG.error("Unable to initialize Management Module datas", e);
		}
	}

	private void createLanguages()
	{
		try
		{
			Language language1 = referentielManager.newLanguage();
			language1.setName(managementFileConfiguration.getLangageFr());
			language1.setFunctionalId(ManagementModuleConstants.LANGUAGE_FR);
			referentielManager.createLanguage(language1);
			Language language2 = referentielManager.newLanguage();
			language2.setName(managementFileConfiguration.getLangageEn());
			language2.setFunctionalId(ManagementModuleConstants.LANGUAGE_EN);
			referentielManager.createLanguage(language2);
		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management", e);
		}
	}

	/**
	 * This method initialize the roles of the application
	 */
	private void createRoles()
	{
		try
		{
			createRole(ManagementModuleConstants.ROLE_ADMIN, managementFileConfiguration.getRoleAdmin());
			createRole(ManagementModuleConstants.ROLE_CUSTOMER, managementFileConfiguration.getRoleCustomer());
			createRole(ManagementModuleConstants.ROLE_PROJECT_MANAGER,
					managementFileConfiguration.getRoleProjectManager());
			createRole(ManagementModuleConstants.ROLE_PROJECT_DIRECTOR,
					managementFileConfiguration.getRoleProjectDirector());
			createRole(ManagementModuleConstants.ROLE_MOA_LEADER, managementFileConfiguration.getRoleMOALeader());
			createRole(ManagementModuleConstants.ROLE_MOA_MEMBER, managementFileConfiguration.getRoleMOAMember());
			createRole(ManagementModuleConstants.ROLE_MOE_LEADER, managementFileConfiguration.getRoleMOELeader());
			createRole(ManagementModuleConstants.ROLE_MOE_MEMBER, managementFileConfiguration.getRoleMOEMember());
			createRole(ManagementModuleConstants.ROLE_SPONSOR, managementFileConfiguration.getRoleSponsor());
			createRole(ManagementModuleConstants.ROLE_OBSERVER, managementFileConfiguration.getRoleObserver());
		}
		catch (Exception e)
		{
			LOG.error("Unable to initialize role", e);
		}
	}

	private void createUnitTimes()
	{
		try
		{
			UnitTime utm = referentielManager.newUnitTime();
			utm.setName(managementFileConfiguration.getUnitTimeMonth());
			utm.setDurationInDays(Integer.parseInt(managementFileConfiguration.getNbJourParMois()));
			utm.setFunctionalId(ManagementModuleConstants.UNIT_TIME_MONTH);
			referentielManager.createUnitTime(utm);
			UnitTime uts = referentielManager.newUnitTime();
			uts.setName(managementFileConfiguration.getUnitTimeWeek());
			uts.setDurationInDays(Integer.parseInt(managementFileConfiguration.getNbJourParSemaine()));
			uts.setFunctionalId(ManagementModuleConstants.UNIT_TIME_WEEK);
			referentielManager.createUnitTime(uts);
		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management", e);
		}
	}

	private void createStatusProjectPlans()
	{
		try
		{
			final StatusProjectPlan spp1 = referentielManager.newStatusProjectPlan();
			spp1.setName(managementFileConfiguration.getProjectPlanDraft());
			spp1.setFunctionalId(ManagementModuleConstants.PROJECT_PLAN_DRAFT);
			referentielManager.createStatusProjectPlan(spp1);
			final StatusProjectPlan spp2 = referentielManager.newStatusProjectPlan();
			spp2.setName(managementFileConfiguration.getProjectPlanValidate());
			spp2.setFunctionalId(ManagementModuleConstants.PROJECT_PLAN_VALIDATE);
			referentielManager.createStatusProjectPlan(spp2);
		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management", e);
		}
	}

	private void createMarkerTypes()
	{
		try
		{
			MarkerType mt1 = referentielManager.newMarkerType();
			mt1.setName(managementFileConfiguration.getMarkerTypeEarly());
			mt1.setFunctionalId(ManagementModuleConstants.MARKER_TYPE_EARLY);
			referentielManager.createMarkerType(mt1);
			MarkerType mt2 = referentielManager.newMarkerType();
			mt2.setName(managementFileConfiguration.getMarkerTypeLate());
			mt2.setFunctionalId(ManagementModuleConstants.MARKER_TYPE_LATE);
			referentielManager.createMarkerType(mt2);
		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management", e);
		}
	}

	private void createScopeTypes()
	{
		try
		{
			ScopeType st1 = referentielManager.newScopeType();
			st1.setName(managementFileConfiguration.getScopeTypeA());
			st1.setFunctionalId(ManagementModuleConstants.SCOPE_TYPE_OTHER);
			referentielManager.createScopeType(st1);
			ScopeType st2 = referentielManager.newScopeType();
			st2.setName(managementFileConfiguration.getScopeTypeCu());
			st2.setFunctionalId(ManagementModuleConstants.SCOPE_TYPE_USE_CASE);
			referentielManager.createScopeType(st2);
			ScopeType st3 = referentielManager.newScopeType();
			st3.setName(managementFileConfiguration.getScopeTypeHu());
			st3.setFunctionalId(ManagementModuleConstants.SCOPE_TYPE_USER_STORY);
			referentielManager.createScopeType(st3);
			ScopeType st4 = referentielManager.newScopeType();
			st4.setName(managementFileConfiguration.getScopeTypeTg());
			st4.setFunctionalId(ManagementModuleConstants.SCOPE_TYPE_GRAAL_TASK);
			referentielManager.createScopeType(st4);
		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management", e);
		}
	}

	private void createStatusScope()
	{
		try
		{
			StatusScope ss1 = referentielManager.newStatusScope();
			ss1.setName(managementFileConfiguration.getScopeStatusModified());
			ss1.setFunctionalId(ManagementModuleConstants.SCOPE_STATUS_MODIFIED);
			referentielManager.createStatusScope(ss1);
			StatusScope ss2 = referentielManager.newStatusScope();
			ss2.setName(managementFileConfiguration.getScopeStatusObsolete());
			ss2.setFunctionalId(ManagementModuleConstants.SCOPE_STATUS_OBSOLETE);
			referentielManager.createStatusScope(ss2);
			StatusScope ss3 = referentielManager.newStatusScope();
			ss3.setName(managementFileConfiguration.getScopeStatusOngoing());
			ss3.setFunctionalId(ManagementModuleConstants.SCOPE_STATUS_ONGOING);
			referentielManager.createStatusScope(ss3);
		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management", e);
		}
	}

	private void createAdjustFactors()
	{
		try
		{
			AdjustFactor af1 = referentielManager.newAdjustFactor();
			af1.setName(managementFileConfiguration.getFactorAdjustmentAppliPortability());
			af1.setFunctionalId(ManagementModuleConstants.FACTOR_ADJUSTMENT_APPLI_PORTABILITY);
			referentielManager.createAdjustFactor(af1);
			AdjustFactor af2 = referentielManager.newAdjustFactor();
			af2.setName(managementFileConfiguration.getFactorAdjustmentConfiguration());
			af2.setFunctionalId(ManagementModuleConstants.FACTOR_ADJUSTMENT_CONFIGURATION);
			referentielManager.createAdjustFactor(af2);
			AdjustFactor af3 = referentielManager.newAdjustFactor();
			af3.setName(managementFileConfiguration.getFactorAdjustmentDataTransmission());
			af3.setFunctionalId(ManagementModuleConstants.FACTOR_ADJUSTMENT_DATA_TRANSMISSION);
			referentielManager.createAdjustFactor(af3);
			AdjustFactor af4 = referentielManager.newAdjustFactor();
			af4.setName(managementFileConfiguration.getFactorAdjustmentDistributedSystem());
			af4.setFunctionalId(ManagementModuleConstants.FACTOR_ADJUSTMENT_DISTRIBUTED_SYSTEM);
			referentielManager.createAdjustFactor(af4);
			AdjustFactor af5 = referentielManager.newAdjustFactor();
			af5.setName(managementFileConfiguration.getFactorAdjustmentEasyOperation());
			af5.setFunctionalId(ManagementModuleConstants.FACTOR_ADJUSTMENT_EASY_OPERATION);
			referentielManager.createAdjustFactor(af5);
			AdjustFactor af6 = referentielManager.newAdjustFactor();
			af6.setName(managementFileConfiguration.getFactorAdjustmentLiveupdate());
			af6.setFunctionalId(ManagementModuleConstants.FACTOR_ADJUSTMENT_LIVE_UPDATE);
			referentielManager.createAdjustFactor(af6);
			AdjustFactor af7 = referentielManager.newAdjustFactor();
			af7.setName(managementFileConfiguration.getFactorAdjustmentModifFlexibility());
			af7.setFunctionalId(ManagementModuleConstants.FACTOR_ADJUSTMENT_MODIF_FLEXIBILITY);
			referentielManager.createAdjustFactor(af7);
			AdjustFactor af8 = referentielManager.newAdjustFactor();
			af8.setName(managementFileConfiguration.getFactorAdjustmentPerfConstraint());
			af8.setFunctionalId(ManagementModuleConstants.FACTOR_ADJUSTMENT_PERF_CONSTRAINT);
			referentielManager.createAdjustFactor(af8);
			AdjustFactor af9 = referentielManager.newAdjustFactor();
			af9.setName(managementFileConfiguration.getFactorAdjustmentProcessComplex());
			af9.setFunctionalId(ManagementModuleConstants.FACTOR_ADJUSTMENT_PROCESS_COMPLEX);
			referentielManager.createAdjustFactor(af9);
			AdjustFactor af10 = referentielManager.newAdjustFactor();
			af10.setName(managementFileConfiguration.getFactorAdjustmentReuse());
			af10.setFunctionalId(ManagementModuleConstants.FACTOR_ADJUSTMENT_REUSE);
			referentielManager.createAdjustFactor(af10);
			AdjustFactor af11 = referentielManager.newAdjustFactor();
			af11.setName(managementFileConfiguration.getFactorAdjustmentSimultaneousAccess());
			af11.setFunctionalId(ManagementModuleConstants.FACTOR_ADJUSTMENT_SIMULTANEOUS_ACCESS);
			referentielManager.createAdjustFactor(af11);
			AdjustFactor af12 = referentielManager.newAdjustFactor();
			af12.setName(managementFileConfiguration.getFactorAdjustmentTransactionRate());
			af12.setFunctionalId(ManagementModuleConstants.FACTOR_ADJUSTMENT_TRANSACTION_RATE);
			referentielManager.createAdjustFactor(af12);
			AdjustFactor af13 = referentielManager.newAdjustFactor();
			af13.setName(managementFileConfiguration.getFactorAdjustmentUsability());
			af13.setFunctionalId(ManagementModuleConstants.FACTOR_ADJUSTMENT_USABILITY);
			referentielManager.createAdjustFactor(af13);
			AdjustFactor af14 = referentielManager.newAdjustFactor();
			af14.setName(managementFileConfiguration.getFactorAdjustementInteractiveCapture());
			af14.setFunctionalId(ManagementModuleConstants.FACTOR_ADJUSTMENT_INTERACTIVE_CAPTURE);
			referentielManager.createAdjustFactor(af14);

		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management", e);
		}
	}

	private void createAjdustWeights()
	{
		try
		{
			AdjustWeight aw1 = referentielManager.newAdjustWeight();
			aw1.setWeight(managementFileConfiguration.getValueweightAdjustmentAverage());
			aw1.setName(managementFileConfiguration.getWeightAdjustmentAverage());
			aw1.setFunctionalId(ManagementModuleConstants.WEIGHT_ADJUSTMENT_AVERAGE);
			referentielManager.createAdjustWeight(aw1);
			AdjustWeight aw2 = referentielManager.newAdjustWeight();
			aw2.setWeight(managementFileConfiguration.getValueweightAdjustmentImportant());
			aw2.setName(managementFileConfiguration.getWeightAdjustmentImportant());
			aw2.setFunctionalId(ManagementModuleConstants.WEIGHT_ADJUSTMENT_IMPORTANT);
			referentielManager.createAdjustWeight(aw2);
			AdjustWeight aw3 = referentielManager.newAdjustWeight();
			aw3.setWeight(managementFileConfiguration.getValueweightAdjustmentIntensive());
			aw3.setName(managementFileConfiguration.getWeightAdjustmentIntensive());
			aw3.setFunctionalId(ManagementModuleConstants.WEIGHT_ADJUSTMENT_INTENSIVE);
			referentielManager.createAdjustWeight(aw3);
			AdjustWeight aw4 = referentielManager.newAdjustWeight();
			aw4.setWeight(managementFileConfiguration.getValueweightAdjustmentNonexistent());
			aw4.setName(managementFileConfiguration.getWeightAdjustmentNonexistent());
			aw4.setFunctionalId(ManagementModuleConstants.WEIGHT_ADJUSTMENT_NONEXISTENT);
			referentielManager.createAdjustWeight(aw4);
			AdjustWeight aw5 = referentielManager.newAdjustWeight();
			aw5.setWeight(managementFileConfiguration.getValueweightAdjustmentRestraint());
			aw5.setName(managementFileConfiguration.getWeightAdjustmentRestraint());
			aw5.setFunctionalId(ManagementModuleConstants.WEIGHT_ADJUSTMENT_RESTRAINT);
			referentielManager.createAdjustWeight(aw5);
			AdjustWeight aw6 = referentielManager.newAdjustWeight();
			aw6.setWeight(managementFileConfiguration.getValueweightAdjustmentSecondary());
			aw6.setName(managementFileConfiguration.getWeightAdjustmentSecondary());
			aw6.setFunctionalId(ManagementModuleConstants.WEIGHT_ADJUSTMENT_SECONDARY);
			referentielManager.createAdjustWeight(aw6);
		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management", e);
		}
	}

	private void createTaskTypes()
	{
		try
		{
			final TaskType taskType1 = taskManager.newTaskType();
			taskType1.setFunctionalId(ManagementModuleConstants.TASK_TYPE_BUG);
			taskType1.setDescription(managementFileConfiguration.getTaskTypeBug());
			taskManager.createTaskType(taskType1);
			final TaskType taskType2 = taskManager.newTaskType();
			taskType2.setFunctionalId(ManagementModuleConstants.TASK_TYPE_WORK);
			taskType2.setDescription(managementFileConfiguration.getTaskTypeWork());
			taskManager.createTaskType(taskType2);
		}
		catch (Exception e)
		{
			LOG.error("Unable to initialize TaskTypes", e);
		}
	}

	private void createTaskCategories()
	{
		// no default value specified for the moment
	}

	private void createTaskStatus()
	{
		try
		{
			StatusTask ts2 = taskManager.newStatusTask();
			ts2.setName(managementFileConfiguration.getTaskStatusCanceled());
			ts2.setFunctionalId(ManagementModuleConstants.TASK_STATUS_CANCELED);
			taskManager.createStatusTask(ts2);
			StatusTask ts4 = taskManager.newStatusTask();
			ts4.setName(managementFileConfiguration.getTaskStatusCurrent());
			ts4.setFunctionalId(ManagementModuleConstants.TASK_STATUS_IN_PROGRESS);
			taskManager.createStatusTask(ts4);
			StatusTask ts6 = taskManager.newStatusTask();
			ts6.setName(managementFileConfiguration.getTaskStatusDone());
			ts6.setFunctionalId(ManagementModuleConstants.TASK_STATUS_DONE);
			taskManager.createStatusTask(ts6);
			StatusTask ts8 = taskManager.newStatusTask();
			ts8.setName(managementFileConfiguration.getTaskStatusNotaAffected());
			ts8.setFunctionalId(ManagementModuleConstants.TASK_STATUS_NOT_AFFECTED);
			taskManager.createStatusTask(ts8);
			StatusTask ts9 = taskManager.newStatusTask();
			ts9.setName(managementFileConfiguration.getTaskStatusNotStarted());
			ts9.setFunctionalId(ManagementModuleConstants.TASK_STATUS_NOT_STARTED);
			taskManager.createStatusTask(ts9);
		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management", e);
		}
	}

	private void createSteeringParameter()
	{
		SteeringParameter sp = referentielManager.newSteeringParameter();
		sp.setAbaque("5");
		sp.setCcomplex("4");
		sp.setCmid("6");
		sp.setCsimple("8");
		sp.setDays("10");
		sp.setEtp("20");
		sp.setGdivalue("11");
		sp.setMcomplex("17");
		sp.setMmid("9");
		sp.setMsimple("3");

		try
		{
			referentielManager.createSteeringParamater(sp);
		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management", e);
		}
	}

	private void createTransformation()
	{
		try
		{
			Transformation transformation = referentielManager.newTransformation();
			transformation.setIdProject(ReferentielManager.TRANSFORMATION_DEFAULT_PROJECT_ID);
			transformation.setNbHeuresJour(Integer.parseInt(managementFileConfiguration.getNbHeureParJour()));
			transformation.setNbJoursAn(Integer.parseInt(managementFileConfiguration.getNbJourParAn()));
			transformation.setNbJoursMois(Integer.parseInt(managementFileConfiguration.getNbJourParMois()));
			transformation.setNbJoursNonTravail(Integer.parseInt(managementFileConfiguration
					.getNbJourNonTravaille()));
			transformation.setNbJoursSemaine(Integer.parseInt(managementFileConfiguration.getNbJourParSemaine()));
			referentielManager.createTransformation(transformation);
		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management" , e);
		}
	}

	private void createDisciplines()
	{
		try
		{
			Discipline discipline1 = referentielManager.newDiscipline();
			discipline1.setName(managementFileConfiguration.getPhareArchitectureDesign());
			discipline1.setFunctionalId(ManagementModuleConstants.DISCIPLINE_ARCHITECTURE_DESIGN);
			discipline1.setOrder(ManagementModuleConstants.DISCIPLINE_ORDER_ARCHITECTURE_DESIGN);
			discipline1.setDefaultRepartition(Integer.parseInt(managementFileConfiguration
																														 .getChargePhareArchitectureDesign()));
			referentielManager.createDiscipline(discipline1);
			Discipline discipline2 = referentielManager.newDiscipline();
			discipline2.setName(managementFileConfiguration.getPhareBusinessModeling());
			discipline2.setFunctionalId(ManagementModuleConstants.DISCIPLINE_BUSINESS_MODELING);
			discipline2.setOrder(ManagementModuleConstants.DISCIPLINE_ORDER_BUSINESS_MODELING);
			discipline2.setDefaultRepartition(Integer.parseInt(managementFileConfiguration.getChargePhareBusinessModeling()));
			referentielManager.createDiscipline(discipline2);
			Discipline discipline3 = referentielManager.newDiscipline();
			discipline3.setName(managementFileConfiguration.getPhareChangeDriving());
			discipline3.setFunctionalId(ManagementModuleConstants.DISCIPLINE_CHANGE_DRIVING);
			discipline3.setOrder(ManagementModuleConstants.DISCIPLINE_ORDER_CHANGE_DRIVING);
			discipline3.setDefaultRepartition(Integer.parseInt(managementFileConfiguration.getChargePhareChangeDriving()));
			referentielManager.createDiscipline(discipline3);
			Discipline discipline4 = referentielManager.newDiscipline();
			discipline4.setName(managementFileConfiguration.getPhareConfigurationManagement());
			discipline4.setFunctionalId(ManagementModuleConstants.DISCIPLINE_CONFIGURATION_MANAGEMENT);
			discipline4.setOrder(ManagementModuleConstants.DISCIPLINE_ORDER_CONFIGURATION_MANAGEMENT);
			discipline4.setDefaultRepartition(Integer.parseInt(managementFileConfiguration
																														 .getChargePhareConfigurationManagement()));
			referentielManager.createDiscipline(discipline4);
			Discipline discipline5 = referentielManager.newDiscipline();
			discipline5.setName(managementFileConfiguration.getPhareImplementation());
			discipline5.setFunctionalId(ManagementModuleConstants.DISCIPLINE_IMPLEMENTATION);
			discipline5.setOrder(ManagementModuleConstants.DISCIPLINE_ORDER_IMPLEMENTATION);
			discipline5.setDefaultRepartition(Integer.parseInt(managementFileConfiguration.getChargePhareImplementation()));
			referentielManager.createDiscipline(discipline5);
			Discipline discipline6 = referentielManager.newDiscipline();
			discipline6.setName(managementFileConfiguration.getPhareProjectManagement());
			discipline6.setFunctionalId(ManagementModuleConstants.DISCIPLINE_PROJECT_MANAGEMENT);
			discipline6.setOrder(ManagementModuleConstants.DISCIPLINE_ORDER_PROJECT_MANAGEMENT);
			discipline6.setDefaultRepartition(Integer.parseInt(managementFileConfiguration
																														 .getChargePhareProjectManagement()));
			referentielManager.createDiscipline(discipline6);
			Discipline discipline7 = referentielManager.newDiscipline();
			discipline7.setName(managementFileConfiguration.getPhareQualityAssurance());
			discipline7.setFunctionalId(ManagementModuleConstants.DISCIPLINE_QUALITY_ASSURANCE);
			discipline7.setOrder(ManagementModuleConstants.DISCIPLINE_ORDER_QUALITY_ASSURANCE);
			discipline7.setDefaultRepartition(Integer.parseInt(managementFileConfiguration.getChargePhareQualityAssurance()));
			referentielManager.createDiscipline(discipline7);
			Discipline discipline8 = referentielManager.newDiscipline();
			discipline8.setName(managementFileConfiguration.getPhareReceipts());
			discipline8.setFunctionalId(ManagementModuleConstants.DISCIPLINE_RECEIPTS);
			discipline8.setOrder(ManagementModuleConstants.DISCIPLINE_ORDER_RECEIPTS);
			discipline8.setDefaultRepartition(Integer.parseInt(managementFileConfiguration.getChargePhareReceipts()));
			referentielManager.createDiscipline(discipline8);
			Discipline discipline9 = referentielManager.newDiscipline();
			discipline9.setName(managementFileConfiguration.getPhareRequirementsAnalysis());
			discipline9.setFunctionalId(ManagementModuleConstants.DISCIPLINE_REQUIREMENTS_ANALYSIS);
			discipline9.setOrder(ManagementModuleConstants.DISCIPLINE_ORDER_REQUIREMENTS_ANALYSIS);
			discipline9.setDefaultRepartition(Integer.parseInt(managementFileConfiguration
																														 .getChargePhareRequirementsAnalysis()));
			referentielManager.createDiscipline(discipline9);
		}
		catch (Exception e)
		{
			LOG.error("Error during initialization of disciplines" + e);
		}
	}

	private void createEstimationComponentSimple()
	{
		try
		{
			EstimationComponentSimple sfs = referentielManager.newEstimationComponentSimple();
			sfs.setIdProjet("default");
			sfs.setValueENT(Float.parseFloat(managementFileConfiguration.getValueENT()));
			sfs.setValueGDE(Float.parseFloat(managementFileConfiguration.getValueGDE()));
			sfs.setValueGDI(Float.parseFloat(managementFileConfiguration.getValueGDI()));
			sfs.setValueINT(Float.parseFloat(managementFileConfiguration.getValueINT()));
			sfs.setValueSOR(Float.parseFloat(managementFileConfiguration.getValueSOR()));
			LOG.warn(sfs.toString());
			referentielManager.createEstimationComponentSimple(sfs);
		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management" , e);
		}
	}

	private void createEstimationComponentDetail()
	{
		try
		{
			EstimationComponentDetail sfs = projectPlanManager.newEstimationComponentDetail();
			sfs.setFunctionalId(ProjectPlanManager.DEFAULT_ESTIMATION_DETAIL_FUNCTIONAL_ID);
			sfs.setAdjustementCoef(ProjectPlanManager.DEFAULT_ESTIMATION_DETAIL_ADJUSTEMENT_COEF);
			sfs.setValueAbaChgHomJour(Integer.parseInt(managementFileConfiguration.getValueAbaChgHomJour()));
			sfs.setValueComplexGDE(Integer.parseInt(managementFileConfiguration.getValueComplexGDE()));
			sfs.setValueMoyenGDE(Integer.parseInt(managementFileConfiguration.getValueMoyenGDE()));
			sfs.setValueSimpleGDE(Integer.parseInt(managementFileConfiguration.getValueSimpleGDE()));
			sfs.setValueComplexGDI(Integer.parseInt(managementFileConfiguration.getValueComplexGDI()));
			sfs.setValueMoyenGDI(Integer.parseInt(managementFileConfiguration.getValueMoyenGDI()));
			sfs.setValueSimpleGDI(Integer.parseInt(managementFileConfiguration.getValueSimpleGDI()));
			sfs.setValueComplexIN(Integer.parseInt(managementFileConfiguration.getValueComplexIN()));
			sfs.setValueMoyenIN(Integer.parseInt(managementFileConfiguration.getValueMoyenIN()));
			sfs.setValueSimpleIN(Integer.parseInt(managementFileConfiguration.getValueSimpleIN()));
			sfs.setValueComplexINT(Integer.parseInt(managementFileConfiguration.getValueComplexINT()));
			sfs.setValueMoyenINT(Integer.parseInt(managementFileConfiguration.getValueMoyenINT()));
			sfs.setValueSimpleINT(Integer.parseInt(managementFileConfiguration.getValueSimpleINT()));
			sfs.setValueComplexOUT(Integer.parseInt(managementFileConfiguration.getValueComplexOUT()));
			sfs.setValueMoyenOUT(Integer.parseInt(managementFileConfiguration.getValueMoyenOUT()));
			sfs.setValueSimpleOUT(Integer.parseInt(managementFileConfiguration.getValueSimpleOUT()));
			projectPlanManager.createEstimationComponentDetail(sfs);
		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management" , e);
		}
	}

	/**
	 * Initialization of phase types
	 *
	 * @throws ManagementModuleException
	 *     exception during creation
	 */
	private void createPhaseTypes()
	{
		try
		{
			PhaseType pt1 = referentielManager.newPhaseType();
			pt1.setName(managementFileConfiguration.getPhaseTypeConstruction());
			pt1.setFunctionalId(ManagementModuleConstants.PHASE_TYPE_CONSTRUCTION);
			referentielManager.createPhaseType(pt1);
			PhaseType pt2 = referentielManager.newPhaseType();
			pt2.setName(managementFileConfiguration.getPhaseTypeFraming());
			pt2.setFunctionalId(ManagementModuleConstants.PHASE_TYPE_FRAMING);
			referentielManager.createPhaseType(pt2);
			PhaseType pt3 = referentielManager.newPhaseType();
			pt3.setName(managementFileConfiguration.getPhaseTypeNone());
			pt3.setFunctionalId(ManagementModuleConstants.PHASE_TYPE_NONE);
			referentielManager.createPhaseType(pt3);
			PhaseType pt4 = referentielManager.newPhaseType();
			pt4.setName(managementFileConfiguration.getPhaseTypePlanning());
			pt4.setFunctionalId(ManagementModuleConstants.PHASE_TYPE_PLANNING);
			referentielManager.createPhaseType(pt4);
			PhaseType pt5 = referentielManager.newPhaseType();
			pt5.setName(managementFileConfiguration.getPhaseTypeTransition());
			pt5.setFunctionalId(ManagementModuleConstants.PHASE_TYPE_TRANSITION);
			referentielManager.createPhaseType(pt5);
		}
		catch (Exception e)
		{
			LOG.error("initialisation Module Management" , e);
		}
	}


  private void createApplicativeFunctions()
  {
    ApplicativeFunction sf1 = managementModuleManager.newApplicativeFunction();
    sf1.setName(ManagementModuleConstants.FUNCTION_ESTIMATION);
    sf1.setValue("Plan projet : estimation");
    ApplicativeFunction sf2 = managementModuleManager.newApplicativeFunction();
    sf2.setName(ManagementModuleConstants.FUNCTION_PERIMETER_MANAGEMENT);
    sf2.setValue("Plan projet : périmètre, lots, jalons...");
    ApplicativeFunction sf3 = managementModuleManager.newApplicativeFunction();
    sf3.setName(ManagementModuleConstants.FUNCTION_CHARGE_PLAN);
    sf3.setValue("Plan projet : plan de charge");
    ApplicativeFunction sf4 = managementModuleManager.newApplicativeFunction();
    sf4.setName(ManagementModuleConstants.FUNCTION_ITERATION_PREPARATION);
    sf4.setValue("Préparation des itérations");
    ApplicativeFunction sf5 = managementModuleManager.newApplicativeFunction();
    sf5.setName(ManagementModuleConstants.FUNCTION_ITERATION_SUPERVISION);
    sf5.setValue("Suivi des itérations");
    ApplicativeFunction sf6 = managementModuleManager.newApplicativeFunction();
    sf6.setName(ManagementModuleConstants.FUNCTION_TASK_MANAGEMENT);
    sf6.setValue("Backlog / gestion des tâches");
    ApplicativeFunction sf7 = managementModuleManager.newApplicativeFunction();
    sf7.setName(ManagementModuleConstants.FUNCTION_GLOBAL_SUPERVISION);
    sf7.setValue("Suivi global du projet");
      ApplicativeFunction sf8 = managementModuleManager.newApplicativeFunction();
      sf8.setName(ManagementModuleConstants.FUNCTION_ADMINISTRATION);
      sf8.setValue("Administration");
      ApplicativeFunction sf9 = managementModuleManager.newApplicativeFunction();
      sf9.setName(ManagementModuleConstants.FUNCTION_PROJECT_PLAN);
      sf9.setValue("Plan projet");
      ApplicativeFunction sf10 = managementModuleManager.newApplicativeFunction();
      sf10.setName(ManagementModuleConstants.FUNCTION_BURNDOWN);
      sf10.setValue("Burndown");    
    try
    {
      managementModuleManager.createApplicativeFunction(sf1);
      managementModuleManager.createApplicativeFunction(sf2);
      managementModuleManager.createApplicativeFunction(sf3);
      managementModuleManager.createApplicativeFunction(sf4);
      managementModuleManager.createApplicativeFunction(sf5);
      managementModuleManager.createApplicativeFunction(sf6);
      managementModuleManager.createApplicativeFunction(sf7);
      managementModuleManager.createApplicativeFunction(sf8);
      managementModuleManager.createApplicativeFunction(sf9);
      managementModuleManager.createApplicativeFunction(sf10);
    }
    catch (Exception e)
    {
      LOG.error("initialisation Module Management" , e);
    }
  }

	/**
	 * This method creates the application rights
	 */
	private void createApplicativeRights()
	{
		try
		{
			final ApplicativeFunction estimationFunction = managementModuleManager
																												 .getApplicativeFunction(ManagementModuleConstants.FUNCTION_ESTIMATION);
			final ApplicativeFunction perimeterFunction = managementModuleManager
																												.getApplicativeFunction(ManagementModuleConstants.FUNCTION_PERIMETER_MANAGEMENT);
			final ApplicativeFunction chargeFunction = managementModuleManager
																										 .getApplicativeFunction(ManagementModuleConstants.FUNCTION_CHARGE_PLAN);
			final ApplicativeFunction iterationPreparationFunction = managementModuleManager
																																	 .getApplicativeFunction(ManagementModuleConstants.FUNCTION_ITERATION_PREPARATION);
			final ApplicativeFunction globalSupervisionFunction = managementModuleManager
																																.getApplicativeFunction(ManagementModuleConstants.FUNCTION_GLOBAL_SUPERVISION);
			final ApplicativeFunction manageTaskFunction = managementModuleManager
																												 .getApplicativeFunction(ManagementModuleConstants.FUNCTION_TASK_MANAGEMENT);
			final ApplicativeFunction iterationSupervisionFunction = managementModuleManager
																																	 .getApplicativeFunction(ManagementModuleConstants.FUNCTION_ITERATION_SUPERVISION);
      final ApplicativeFunction administrateFunction = managementModuleManager.getApplicativeFunction(ManagementModuleConstants.FUNCTION_ADMINISTRATION);
      final ApplicativeFunction planProjectFunction = managementModuleManager.getApplicativeFunction(ManagementModuleConstants.FUNCTION_PROJECT_PLAN);
      final ApplicativeFunction burndownFunction = managementModuleManager.getApplicativeFunction(ManagementModuleConstants.FUNCTION_BURNDOWN);

      
			final String accessRightsRead = ManagementModuleConstants.ACCESS_RIGHTS_READ;
			final String accessRightsWrite = ManagementModuleConstants.ACCESS_RIGHTS_WRITE;

			// admin rights
			final Role roleAdmin = managementModuleManager.getRoleByFunctionalId(ManagementModuleConstants.ROLE_ADMIN);
			createApplicationRight(roleAdmin, estimationFunction, accessRightsWrite);
			createApplicationRight(roleAdmin, perimeterFunction, accessRightsWrite);
			createApplicationRight(roleAdmin, chargeFunction, accessRightsWrite);
			createApplicationRight(roleAdmin, iterationPreparationFunction, accessRightsWrite);
			createApplicationRight(roleAdmin, globalSupervisionFunction, accessRightsWrite);
			createApplicationRight(roleAdmin, manageTaskFunction, accessRightsWrite);
			createApplicationRight(roleAdmin, iterationSupervisionFunction, accessRightsWrite);
			createApplicationRight(roleAdmin, administrateFunction, accessRightsWrite);
			createApplicationRight(roleAdmin, planProjectFunction, accessRightsWrite);
			createApplicationRight(roleAdmin,burndownFunction, accessRightsRead);
			    
			// sponsor rights
			final Role roleSponsor = managementModuleManager.getRoleByFunctionalId(ManagementModuleConstants.ROLE_SPONSOR);
			createApplicationRight(roleSponsor, estimationFunction, accessRightsRead);
			createApplicationRight(roleSponsor, perimeterFunction, accessRightsRead);
			createApplicationRight(roleSponsor, globalSupervisionFunction, accessRightsRead);
			createApplicationRight(roleSponsor, administrateFunction, accessRightsRead);
			createApplicationRight(roleSponsor, planProjectFunction, accessRightsRead);
			createApplicationRight(roleSponsor,burndownFunction, accessRightsRead);
			
			// customer rights
			final Role roleCustomer = managementModuleManager.getRoleByFunctionalId(ManagementModuleConstants.ROLE_CUSTOMER);
			createApplicationRight(roleCustomer, estimationFunction, accessRightsRead);
			createApplicationRight(roleCustomer, perimeterFunction, accessRightsRead);
			createApplicationRight(roleCustomer, chargeFunction, accessRightsRead);
			createApplicationRight(roleCustomer, globalSupervisionFunction, accessRightsRead);
			createApplicationRight(roleCustomer, administrateFunction, accessRightsRead);
			createApplicationRight(roleCustomer, planProjectFunction, accessRightsRead);
			createApplicationRight(roleCustomer,burndownFunction, accessRightsRead);
			
			// director project rights
			final Role roleProjectDirector = managementModuleManager
																					 .getRoleByFunctionalId(ManagementModuleConstants.ROLE_PROJECT_DIRECTOR);
			createApplicationRight(roleProjectDirector, estimationFunction, accessRightsWrite);
			createApplicationRight(roleProjectDirector, perimeterFunction, accessRightsWrite);
			createApplicationRight(roleProjectDirector, chargeFunction, accessRightsWrite);
			createApplicationRight(roleProjectDirector, iterationPreparationFunction, accessRightsRead);
			createApplicationRight(roleProjectDirector, globalSupervisionFunction, accessRightsRead);
			createApplicationRight(roleProjectDirector, manageTaskFunction, accessRightsRead);
			createApplicationRight(roleProjectDirector, iterationSupervisionFunction, accessRightsRead);
			createApplicationRight(roleProjectDirector, administrateFunction, accessRightsRead);
			createApplicationRight(roleProjectDirector, planProjectFunction, accessRightsWrite);
	    createApplicationRight(roleProjectDirector,burndownFunction, accessRightsRead);

			
			// project manager rights
			final Role roleProjectManager = managementModuleManager
																					.getRoleByFunctionalId(ManagementModuleConstants.ROLE_PROJECT_MANAGER);
			createApplicationRight(roleProjectManager, estimationFunction, accessRightsWrite);
			createApplicationRight(roleProjectManager, perimeterFunction, accessRightsWrite);
			createApplicationRight(roleProjectManager, chargeFunction, accessRightsWrite);
			createApplicationRight(roleProjectManager, iterationPreparationFunction, accessRightsWrite);
			createApplicationRight(roleProjectManager, globalSupervisionFunction, accessRightsWrite);
			createApplicationRight(roleProjectManager, manageTaskFunction, accessRightsWrite);
			createApplicationRight(roleProjectManager, iterationSupervisionFunction, accessRightsWrite);
			createApplicationRight(roleProjectManager, administrateFunction, accessRightsRead);
			createApplicationRight(roleProjectManager, planProjectFunction, accessRightsWrite);
			createApplicationRight(roleProjectManager,burndownFunction, accessRightsRead);
			
			// MOE leader
			final Role roleMOELeader = managementModuleManager
																		 .getRoleByFunctionalId(ManagementModuleConstants.ROLE_MOE_LEADER);
			createApplicationRight(roleMOELeader, estimationFunction, accessRightsRead);
			createApplicationRight(roleMOELeader, perimeterFunction, accessRightsRead);
			createApplicationRight(roleMOELeader, manageTaskFunction, accessRightsWrite);
			createApplicationRight(roleMOELeader, iterationSupervisionFunction, accessRightsWrite);
			createApplicationRight(roleMOELeader, administrateFunction, accessRightsRead);
			createApplicationRight(roleMOELeader, planProjectFunction, accessRightsRead);
			createApplicationRight(roleMOELeader,burndownFunction, accessRightsRead);
			
			// MOA leader
			final Role roleMOALeader = managementModuleManager
																		 .getRoleByFunctionalId(ManagementModuleConstants.ROLE_MOA_LEADER);
			createApplicationRight(roleMOALeader, estimationFunction, accessRightsRead);
			createApplicationRight(roleMOALeader, perimeterFunction, accessRightsRead);
			createApplicationRight(roleMOALeader, manageTaskFunction, accessRightsWrite);
			createApplicationRight(roleMOALeader, iterationSupervisionFunction, accessRightsWrite);
			createApplicationRight(roleMOALeader, administrateFunction, accessRightsRead);
			createApplicationRight(roleMOALeader, planProjectFunction, accessRightsRead);
			createApplicationRight(roleMOALeader,burndownFunction, accessRightsRead);
			
			// MOE member
			final Role roleMOEMember = managementModuleManager
																		 .getRoleByFunctionalId(ManagementModuleConstants.ROLE_MOE_MEMBER);
			createApplicationRight(roleMOEMember, manageTaskFunction, accessRightsWrite);
			createApplicationRight(roleMOEMember, iterationSupervisionFunction, accessRightsRead);
			createApplicationRight(roleMOEMember,burndownFunction, accessRightsRead);
			
			// MOA member
			final Role roleMOAMember = managementModuleManager
																		 .getRoleByFunctionalId(ManagementModuleConstants.ROLE_MOA_MEMBER);
			createApplicationRight(roleMOAMember, manageTaskFunction, accessRightsWrite);
			createApplicationRight(roleMOAMember, iterationSupervisionFunction, accessRightsRead);
			createApplicationRight(roleMOAMember,burndownFunction, accessRightsRead);
			
			// observer
			final Role roleObserver = managementModuleManager.getRoleByFunctionalId(ManagementModuleConstants.ROLE_OBSERVER);
			createApplicationRight(roleObserver, estimationFunction, accessRightsRead);
			createApplicationRight(roleObserver, perimeterFunction, accessRightsRead);
			createApplicationRight(roleObserver, chargeFunction, accessRightsRead);
			createApplicationRight(roleObserver, iterationPreparationFunction, accessRightsRead);
			createApplicationRight(roleObserver, globalSupervisionFunction, accessRightsRead);
			createApplicationRight(roleObserver, manageTaskFunction, accessRightsRead);
			createApplicationRight(roleObserver, iterationSupervisionFunction, accessRightsRead);
			createApplicationRight(roleObserver, administrateFunction, accessRightsRead);
			createApplicationRight(roleObserver, planProjectFunction, accessRightsRead);
			createApplicationRight(roleObserver,burndownFunction, accessRightsRead);
		}
		catch (Exception ex)
		{
			LOG.error("Unable to initialize applicative rights", ex);
		}

	}

	/**
	 * This method creates the disciplines rights
	 */
	private void createDisciplinesRights()
	{
		try
		{
			final Discipline businessModelingDiscipline = referentielManager
																												.getDiscipline(ManagementModuleConstants.DISCIPLINE_BUSINESS_MODELING);
			final Discipline requirementsAnalysisDiscipline = referentielManager
																														.getDiscipline(ManagementModuleConstants.DISCIPLINE_REQUIREMENTS_ANALYSIS);
			final Discipline architectureDesignDiscipline = referentielManager
																													.getDiscipline(ManagementModuleConstants.DISCIPLINE_ARCHITECTURE_DESIGN);
			final Discipline implementationDiscipline = referentielManager
																											.getDiscipline(ManagementModuleConstants.DISCIPLINE_IMPLEMENTATION);
			final Discipline receiptDiscipline = referentielManager
																							 .getDiscipline(ManagementModuleConstants.DISCIPLINE_RECEIPTS);
			final Discipline changeDrivingDiscipline = referentielManager
																										 .getDiscipline(ManagementModuleConstants.DISCIPLINE_CHANGE_DRIVING);
			final Discipline configurationManagementDiscipline = referentielManager
																															 .getDiscipline(ManagementModuleConstants.DISCIPLINE_CONFIGURATION_MANAGEMENT);
			final Discipline projectManagementDiscipline = referentielManager
																												 .getDiscipline(ManagementModuleConstants.DISCIPLINE_PROJECT_MANAGEMENT);
			final Discipline qualityAssuranceDiscipline = referentielManager
																												.getDiscipline(ManagementModuleConstants.DISCIPLINE_QUALITY_ASSURANCE);

			final Role roleAdmin = managementModuleManager.getRoleByFunctionalId(ManagementModuleConstants.ROLE_ADMIN);
			roleAdmin.getListDisciplines().add(businessModelingDiscipline);
			roleAdmin.getListDisciplines().add(requirementsAnalysisDiscipline);
			roleAdmin.getListDisciplines().add(architectureDesignDiscipline);
			roleAdmin.getListDisciplines().add(implementationDiscipline);
			roleAdmin.getListDisciplines().add(receiptDiscipline);
			roleAdmin.getListDisciplines().add(changeDrivingDiscipline);
			roleAdmin.getListDisciplines().add(configurationManagementDiscipline);
			roleAdmin.getListDisciplines().add(projectManagementDiscipline);
			roleAdmin.getListDisciplines().add(qualityAssuranceDiscipline);
			managementModuleManager.updateRole(roleAdmin);

			final Role roleSponsor = managementModuleManager.getRoleByFunctionalId(ManagementModuleConstants.ROLE_SPONSOR);
			roleSponsor.getListDisciplines().add(businessModelingDiscipline);
			roleSponsor.getListDisciplines().add(projectManagementDiscipline);
			roleSponsor.getListDisciplines().add(qualityAssuranceDiscipline);
			managementModuleManager.updateRole(roleSponsor);

			final Role roleCustomer = managementModuleManager.getRoleByFunctionalId(ManagementModuleConstants.ROLE_CUSTOMER);
			roleCustomer.getListDisciplines().add(businessModelingDiscipline);
			roleCustomer.getListDisciplines().add(requirementsAnalysisDiscipline);
			roleCustomer.getListDisciplines().add(architectureDesignDiscipline);
			roleCustomer.getListDisciplines().add(receiptDiscipline);
			roleCustomer.getListDisciplines().add(projectManagementDiscipline);
			roleCustomer.getListDisciplines().add(qualityAssuranceDiscipline);
			managementModuleManager.updateRole(roleCustomer);

			final Role roleDirector = managementModuleManager
																		.getRoleByFunctionalId(ManagementModuleConstants.ROLE_PROJECT_DIRECTOR);
			roleDirector.getListDisciplines().add(businessModelingDiscipline);
			roleDirector.getListDisciplines().add(requirementsAnalysisDiscipline);
			roleDirector.getListDisciplines().add(architectureDesignDiscipline);
			roleDirector.getListDisciplines().add(implementationDiscipline);
			roleDirector.getListDisciplines().add(receiptDiscipline);
			roleDirector.getListDisciplines().add(changeDrivingDiscipline);
			roleDirector.getListDisciplines().add(configurationManagementDiscipline);
			roleDirector.getListDisciplines().add(projectManagementDiscipline);
			roleDirector.getListDisciplines().add(qualityAssuranceDiscipline);
			managementModuleManager.updateRole(roleDirector);

			final Role roleProjectManager = managementModuleManager
																					.getRoleByFunctionalId(ManagementModuleConstants.ROLE_PROJECT_MANAGER);
			roleProjectManager.getListDisciplines().add(businessModelingDiscipline);
			roleProjectManager.getListDisciplines().add(requirementsAnalysisDiscipline);
			roleProjectManager.getListDisciplines().add(architectureDesignDiscipline);
			roleProjectManager.getListDisciplines().add(implementationDiscipline);
			roleProjectManager.getListDisciplines().add(receiptDiscipline);
			roleProjectManager.getListDisciplines().add(changeDrivingDiscipline);
			roleProjectManager.getListDisciplines().add(configurationManagementDiscipline);
			roleProjectManager.getListDisciplines().add(projectManagementDiscipline);
			roleProjectManager.getListDisciplines().add(qualityAssuranceDiscipline);
			managementModuleManager.updateRole(roleProjectManager);

			final Role roleMOELeader = managementModuleManager
																		 .getRoleByFunctionalId(ManagementModuleConstants.ROLE_MOE_LEADER);
			roleMOELeader.getListDisciplines().add(architectureDesignDiscipline);
			roleMOELeader.getListDisciplines().add(implementationDiscipline);
			roleMOELeader.getListDisciplines().add(receiptDiscipline);
			roleMOELeader.getListDisciplines().add(configurationManagementDiscipline);
			roleMOELeader.getListDisciplines().add(projectManagementDiscipline);
			roleMOELeader.getListDisciplines().add(qualityAssuranceDiscipline);
			managementModuleManager.updateRole(roleMOELeader);

			final Role roleMOALeader = managementModuleManager
																		 .getRoleByFunctionalId(ManagementModuleConstants.ROLE_MOA_LEADER);
			roleMOALeader.getListDisciplines().add(businessModelingDiscipline);
			roleMOALeader.getListDisciplines().add(requirementsAnalysisDiscipline);
			roleMOALeader.getListDisciplines().add(architectureDesignDiscipline);
			roleMOALeader.getListDisciplines().add(receiptDiscipline);
			roleMOALeader.getListDisciplines().add(changeDrivingDiscipline);
			roleMOALeader.getListDisciplines().add(configurationManagementDiscipline);
			roleMOALeader.getListDisciplines().add(projectManagementDiscipline);
			roleMOALeader.getListDisciplines().add(qualityAssuranceDiscipline);
			managementModuleManager.updateRole(roleMOALeader);

			final Role roleMOEMember = managementModuleManager
																		 .getRoleByFunctionalId(ManagementModuleConstants.ROLE_MOE_MEMBER);
			roleMOEMember.getListDisciplines().add(architectureDesignDiscipline);
			roleMOEMember.getListDisciplines().add(implementationDiscipline);
			roleMOEMember.getListDisciplines().add(receiptDiscipline);
			roleMOEMember.getListDisciplines().add(configurationManagementDiscipline);
			roleMOEMember.getListDisciplines().add(qualityAssuranceDiscipline);
			managementModuleManager.updateRole(roleMOEMember);

			final Role roleMOAMember = managementModuleManager
																		 .getRoleByFunctionalId(ManagementModuleConstants.ROLE_MOA_MEMBER);
			roleMOAMember.getListDisciplines().add(businessModelingDiscipline);
			roleMOAMember.getListDisciplines().add(requirementsAnalysisDiscipline);
			roleMOAMember.getListDisciplines().add(architectureDesignDiscipline);
			roleMOAMember.getListDisciplines().add(receiptDiscipline);
			roleMOAMember.getListDisciplines().add(changeDrivingDiscipline);
			roleMOAMember.getListDisciplines().add(configurationManagementDiscipline);
			roleMOAMember.getListDisciplines().add(qualityAssuranceDiscipline);
			managementModuleManager.updateRole(roleMOAMember);

		}
		catch (Exception ex)
		{
			LOG.error("Unable to initialize disciplines rights", ex);
		}
	}

	/**
	 * Initialize the ScopeUnitDisciplineStatus
	 */
	public void initializeScopeUnitDisciplineStatus()
	{
		try
		{
			createScopeUnitDisciplineStatus(ManagementModuleConstants.SCOPE_DISCIPLINE_STATUS_NOT_STARTED,
					managementFileConfiguration.getScopeDisciplineStatusNotStarted());
			createScopeUnitDisciplineStatus(ManagementModuleConstants.SCOPE_DISCIPLINE_STATUS_IN_PROGRESS,
					managementFileConfiguration.getScopeDisciplineStatusInProgress());
			createScopeUnitDisciplineStatus(ManagementModuleConstants.SCOPE_DISCIPLINE_STATUS_CLOSED,
					managementFileConfiguration.getScopeDisciplineStatusClosed());
		}
		catch (Exception e)
		{
			LOG.error("Problem during ScopeUnitDisciplineStatus initialization " + e);
		}
	}

	/**
	 * This method build a new Role with arguments and call its creation on ManagementModuleManager
	 *
	 * @param functionalId
	 *     the functional id of the role
	 * @param name
	 *     the name of the role
	 *
	 * @throws ManagementModuleException
	 *     error during creation
	 */
	private void createRole(final String functionalId, final String name) throws ManagementModuleException
	{
		Role role = managementModuleManager.getRoleByFunctionalId(functionalId);
		if (role == null)
		{
			role = managementModuleManager.newRole();
			role.setFunctionalId(functionalId);
			role.setName(name);
			managementModuleManager.createRole(role);
		}
		else
		{
			role.setName(name);
			managementModuleManager.updateRole(role);
		}
	}

	/**
	 * This method build a new ApplicativeRights with arguments and call its creation on managementModuleManager
	 *
	 * @param role
	 *     the Role of the ApplicativeRights to build
	 * @param applicativeFunction
	 *     the ApplicativeFunction of the ApplicativeRights to build
	 * @param accessRight
	 *     the right of the role on the applicativeFunction
	 *
	 * @throws ManagementModuleException
	 *     error during creation
	 */
	private void createApplicationRight(final Role role, final ApplicativeFunction applicativeFunction,
																			final String accessRight) throws ManagementModuleException
	{
		ApplicativeRights applicativeRights = managementModuleManager.newApplicativeRights();
		applicativeRights.setRole(role);
		applicativeRights.setApplicativeFunction(applicativeFunction);
		applicativeRights.setAccesRight(accessRight);
		managementModuleManager.createApplicativeRights(applicativeRights);
	}

	/**
	 * Create a ScopeUnitDisciplineStatus
	 * 
	 * @param functionalId
	 *          the functionalId to use
	 * @param label
	 *          the label to use
	 * @throws ManagementModuleException
	 *           pmroblem during creation
	 */
	public void createScopeUnitDisciplineStatus(final String functionalId, final String label)
			throws ManagementModuleException
	{
		final ScopeUnitDisciplineStatus scopeUnitDisciplineStatus = projectPlanManager
				.newScopeUnitDisciplineStatus();
		scopeUnitDisciplineStatus.setFunctionalId(functionalId);
		scopeUnitDisciplineStatus.setLabel(label);
		projectPlanManager.createScopeUnitDisciplineStatus(scopeUnitDisciplineStatus);
	}

	public void setBundleContext(final BundleContext pBundleContext)
	{
		bundleContext = pBundleContext;
	}

	public void setManagementModuleManager(final ManagementModuleManager pManagementModuleManager)
	{
		managementModuleManager = pManagementModuleManager;
	}

	public void setProjectPlanManager(final ProjectPlanManager pProjectPlanManager)
	{
		projectPlanManager = pProjectPlanManager;
	}

	public void setTaskManager(final TaskManager pTaskManager)
	{
		taskManager = pTaskManager;
	}

	public void setReferentielManager(final ReferentielManager pReferentielManager)
	{
		referentielManager = pReferentielManager;
	}

	public void setManagementFileConfiguration(final ManagementFileConfiguration pManagementFileConfiguration)
	{
		managementFileConfiguration = pManagementFileConfiguration;
	}

}
