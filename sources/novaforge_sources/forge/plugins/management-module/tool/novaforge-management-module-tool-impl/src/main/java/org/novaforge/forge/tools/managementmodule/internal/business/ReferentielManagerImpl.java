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
import org.novaforge.forge.tools.managementmodule.business.ReferentielManager;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.dao.AdjustFactorDAO;
import org.novaforge.forge.tools.managementmodule.dao.AdjustWeightDAO;
import org.novaforge.forge.tools.managementmodule.dao.DisciplineDAO;
import org.novaforge.forge.tools.managementmodule.dao.EstimationComponentSimpleDAO;
import org.novaforge.forge.tools.managementmodule.dao.LanguageDAO;
import org.novaforge.forge.tools.managementmodule.dao.MarkerTypeDAO;
import org.novaforge.forge.tools.managementmodule.dao.PhaseTypeDAO;
import org.novaforge.forge.tools.managementmodule.dao.ScopeTypeDAO;
import org.novaforge.forge.tools.managementmodule.dao.StatusProjectPlanDAO;
import org.novaforge.forge.tools.managementmodule.dao.StatusScopeDAO;
import org.novaforge.forge.tools.managementmodule.dao.SteeringParamDAO;
import org.novaforge.forge.tools.managementmodule.dao.TransformationDAO;
import org.novaforge.forge.tools.managementmodule.dao.UnitTimeDAO;
import org.novaforge.forge.tools.managementmodule.domain.AdjustFactor;
import org.novaforge.forge.tools.managementmodule.domain.AdjustWeight;
import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentSimple;
import org.novaforge.forge.tools.managementmodule.domain.Language;
import org.novaforge.forge.tools.managementmodule.domain.MarkerType;
import org.novaforge.forge.tools.managementmodule.domain.PhaseType;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.ProjectDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ScopeType;
import org.novaforge.forge.tools.managementmodule.domain.StatusProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.StatusScope;
import org.novaforge.forge.tools.managementmodule.domain.SteeringParameter;
import org.novaforge.forge.tools.managementmodule.domain.Transformation;
import org.novaforge.forge.tools.managementmodule.domain.UnitTime;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReferentielManagerImpl implements ReferentielManager
{
	private UnitTimeDAO									unitTimeDAO;

	private DisciplineDAO								disciplineDAO;

	private MarkerTypeDAO								markerTypeDAO;

	private StatusProjectPlanDAO				 statusProjectPlanDAO;

	private ScopeTypeDAO								 scopeTypeDAO;

	private StatusScopeDAO							 statusScopeDAO;

	private AdjustFactorDAO							adjustFactorDAO;

	private SteeringParamDAO						 steeringParamDAO;

	private TransformationDAO						transformationDAO;

	private PhaseTypeDAO								 phaseTypeDAO;

	private EstimationComponentSimpleDAO estimationComponentSimpleDAO;

	private AdjustWeightDAO							adjustWeightDAO;

	private LanguageDAO									languageDAO;

	private BusinessObjectFactory				businessObjectFactory;

	@Override
	public UnitTime newUnitTime()
	{
		return businessObjectFactory.getInstanceUnitTime();
	}

	@Override
	public UnitTime createUnitTime(final UnitTime ut) throws ManagementModuleException
	{
		return unitTimeDAO.save(ut);
	}

	@Override
	public UnitTime getUnitTime(final String name) throws ManagementModuleException
	{
		return unitTimeDAO.findByName(name);
	}

	@Override
	public UnitTime getUnitTimeByIdFunctional(final String idFunc) throws ManagementModuleException
	{
		return unitTimeDAO.findByIdFunctional(idFunc);
	}

	@Override
	public List<UnitTime> getAllUnitTimes() throws ManagementModuleException
	{
		return unitTimeDAO.findAll();
	}

	@Override
	public Discipline newDiscipline()
	{
		return businessObjectFactory.getInstanceDiscipline();
	}

	@Override
	public ProjectDiscipline newProjectDiscipline()
	{
		return businessObjectFactory.getInstanceProjectDiscipline();
	}

	@Override
	public Discipline createDiscipline(final Discipline discipline) throws ManagementModuleException
	{
		return disciplineDAO.save(discipline);
	}

	@Override
	public Discipline getDiscipline(final String functionalId) throws ManagementModuleException
	{
		return disciplineDAO.findByFunctionalId(functionalId);
	}

	@Override
	public List<Discipline> getAllDiscipline() throws ManagementModuleException
	{
		List<Discipline> result = new ArrayList<Discipline>(disciplineDAO.findAll());
		Collections.sort(result, new Comparator<Discipline>()
		{
			@Override
			public int compare(final Discipline o1, final Discipline o2)
			{
				return o1.getOrder() < o2.getOrder() ? -1 : 1;
			}
		});
		return result;
	}

	@Override
	public Set<ProjectDiscipline> getDefaultProjectDisciplines(final Project project)
			throws ManagementModuleException
	{
		List<Discipline> listDisciplineDefault = disciplineDAO.findAll();

		Set<ProjectDiscipline> retour = new HashSet<ProjectDiscipline>();
		for (final Discipline discipline : listDisciplineDefault)
		{
			final ProjectDiscipline projectDiscipline = this.newProjectDiscipline();
			projectDiscipline.setRepartition(discipline.getDefaultRepartition());
			projectDiscipline.setDiscipline(discipline);
			projectDiscipline.setProject(project);
			retour.add(projectDiscipline);
		}
		return retour;
	}

	/************************* MarkerType ********************************************************************/

	@Override
	public MarkerType newMarkerType()
	{
		return markerTypeDAO.newPhaseType();
	}

	@Override
	public MarkerType getMarkerType(final String name) throws ManagementModuleException
	{
		return markerTypeDAO.findByName(name);
	}

	@Override
	public MarkerType getMarkerTypeByFuncionalId(final String functionalId) throws ManagementModuleException
	{
		return markerTypeDAO.findByFunctionalId(functionalId);
	}

	@Override
	public List<MarkerType> getMarkerTypeList() throws ManagementModuleException
	{
		return markerTypeDAO.findAll();
	}

	@Override
	public MarkerType createMarkerType(final MarkerType markerType) throws ManagementModuleException
	{
		return markerTypeDAO.save(markerType);
	}

	@Override
	public StatusProjectPlan newStatusProjectPlan()
	{
		return businessObjectFactory.getInstanceStatusProjectPlan();
	}

	/************************* StatusProjectPlan ********************************************************************/

	@Override
	public StatusProjectPlan createStatusProjectPlan(final StatusProjectPlan spp)
			throws ManagementModuleException
	{
		return statusProjectPlanDAO.save(spp);
	}

	@Override
	public StatusProjectPlan getStatusProjectPlan(final String name) throws ManagementModuleException
	{
		return statusProjectPlanDAO.findByName(name);
	}

	@Override
	public StatusProjectPlan getStatusProjectPlanByFunctionalId(final String pFunctionalId)
			throws ManagementModuleException
	{
		return statusProjectPlanDAO.findByFunctionalId(pFunctionalId);
	}

	@Override
	public boolean updateStatusProjectPlan(final StatusProjectPlan spp) throws ManagementModuleException
	{
		statusProjectPlanDAO.merge(spp);
		return true;
	}

	@Override
	public List<StatusProjectPlan> getAllStatusProjectPlan() throws ManagementModuleException
	{
		return statusProjectPlanDAO.findAll();
	}

	@Override
	public ScopeType newScopeType()
	{
		return businessObjectFactory.getInstanceScopeType();
	}

	/************************* ScopeType ********************************************************************/

	@Override
	public ScopeType createScopeType(final ScopeType st) throws ManagementModuleException
	{
		return scopeTypeDAO.save(st);
	}

	@Override
	public boolean deleteScopeType(final String name) throws ManagementModuleException
	{
		ScopeType ss = scopeTypeDAO.findByName(name);
		scopeTypeDAO.delete(ss);
		return true;
	}

	@Override
	public ScopeType getScopeType(final String name) throws ManagementModuleException
	{
		return scopeTypeDAO.findByName(name);
	}

	@Override
	public boolean updateScopeType(final ScopeType st) throws ManagementModuleException
	{
		scopeTypeDAO.merge(st);
		return true;
	}

	@Override
	public List<ScopeType> getAllScopeType() throws ManagementModuleException
	{
		return scopeTypeDAO.findAll();
	}

	@Override
	public ScopeType getScopeTypeByfunctionalId(final String pFunctionalId) throws ManagementModuleException
	{
		return scopeTypeDAO.findByfunctionalId(pFunctionalId);
	}

	@Override
	public StatusScope newStatusScope()
	{
		return businessObjectFactory.getInstanceStatusScope();
	}

	/************************* ScopeType ********************************************************************/

	@Override
	public StatusScope createStatusScope(final StatusScope ss) throws ManagementModuleException
	{
		return statusScopeDAO.save(ss);
	}

	@Override
	public boolean deleteStatusScope(final String functionalId) throws ManagementModuleException
	{
		StatusScope ss = statusScopeDAO.findByFunctionalId(functionalId);
		statusScopeDAO.delete(ss);
		return true;
	}

	@Override
	public StatusScope getStatusScope(final String functionalId) throws ManagementModuleException
	{
		return statusScopeDAO.findByFunctionalId(functionalId);
	}

	@Override
	public boolean updateStatusScope(final StatusScope ss) throws ManagementModuleException
	{
		statusScopeDAO.merge(ss);
		return true;
	}

	@Override
	public List<StatusScope> getAllStatusScope() throws ManagementModuleException
	{
		return statusScopeDAO.findAll();
	}

	/************************* AdjustFactor ********************************************************************/

	@Override
	public AdjustFactor newAdjustFactor()
	{
		return businessObjectFactory.getInstanceAdjustFactor();
	}

	@Override
	public AdjustFactor createAdjustFactor(final AdjustFactor adjustF) throws ManagementModuleException
	{
		return adjustFactorDAO.save(adjustF);
	}

	@Override
	public List<AdjustFactor> getAllAdjustFactor() throws ManagementModuleException
	{
		return adjustFactorDAO.findAll();
	}

	@Override
	public AdjustFactor getAdjustFactor(final String name) throws ManagementModuleException
	{
		return adjustFactorDAO.findByName(name);
	}

	/**
	 * ********************** PhaseType *******************************************************************
	 */

	@Override
	public PhaseType newPhaseType()
	{
		return businessObjectFactory.getInstancePhaseType();
	}

	@Override
	public PhaseType createPhaseType(final PhaseType phaseType) throws ManagementModuleException
	{
		return phaseTypeDAO.save(phaseType);
	}

	@Override
	public PhaseType getPhaseType(final String functionalId) throws ManagementModuleException
	{
		return phaseTypeDAO.findByFunctionalId(functionalId);
	}

	@Override
	public List<PhaseType> getAllPhaseTypes() throws ManagementModuleException
	{
		return phaseTypeDAO.findAllPhaseTypes();
	}

	@Override
	public List<SteeringParameter> getSteeringParameter() throws ManagementModuleException
	{
		return steeringParamDAO.findAll();
	}

	@Override
	public SteeringParameter newSteeringParameter()
	{
		return businessObjectFactory.getInstanceSteeringParameter();
	}

	@Override
	public SteeringParameter createSteeringParamater(final SteeringParameter steeringParamater)
			throws ManagementModuleException
	{
		return steeringParamDAO.save(steeringParamater);
	}

	@Override
	public List<Transformation> getAllTransformation() throws ManagementModuleException
	{
		return transformationDAO.findAll();
	}

	@Override
	public Transformation getTransformation(final Long id) throws ManagementModuleException
	{
		return transformationDAO.findById(id);
	}

	@Override
	public Transformation newTransformation()
	{
		return businessObjectFactory.getInstanceTransformation();
	}

	@Override
	public Transformation createTransformation(final Transformation transformation)
			throws ManagementModuleException
	{
		return transformationDAO.save(transformation);
	}

	@Override
	public Transformation getTransformation(final String idProject) throws ManagementModuleException
	{
		return transformationDAO.getTransformationOfProject(idProject);
	}

	@Override
	public Transformation getDefaultTransformation() throws ManagementModuleException
	{
		try
		{
			Transformation ret = transformationDAO
					.getTransformationOfProject(ReferentielManager.TRANSFORMATION_DEFAULT_PROJECT_ID);
			return (Transformation) ret.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new ManagementModuleException("Problem while cloning the default transformation", e);
		}
	}

	@Override
	public AdjustWeight newAdjustWeight()
	{
		return businessObjectFactory.getInstanceAdjustWeight();
	}

	@Override
	public AdjustWeight createAdjustWeight(final AdjustWeight sf) throws ManagementModuleException
	{
		return adjustWeightDAO.save(sf);
	}

	@Override
	public List<AdjustWeight> getAllAdjustWeight() throws ManagementModuleException
	{
		return adjustWeightDAO.findAll();
	}

	@Override
	public Language newLanguage()
	{
		return businessObjectFactory.getInstanceLanguage();
	}

	@Override
	public Language createLanguage(final Language lan) throws ManagementModuleException
	{
		return languageDAO.save(lan);
	}

	@Override
	public List<Language> getAllLanguage() throws ManagementModuleException
	{
		return languageDAO.findAllLanguage();
	}

	/************************* EstimationComponentSimple ********************************************************************/

	@Override
	public EstimationComponentSimple newEstimationComponentSimple()
	{
		return businessObjectFactory.getInstanceEstimationComponentSimple();
	}

	@Override
	public EstimationComponentSimple createEstimationComponentSimple(final EstimationComponentSimple sf)
			throws ManagementModuleException
	{
		return estimationComponentSimpleDAO.save(sf);
	}

	@Override
	public EstimationComponentSimple getEstimationComponentSimple(final String idProject)
			throws ManagementModuleException
	{
		return estimationComponentSimpleDAO.findByIdProjet(idProject);
	}

	@Override
	public EstimationComponentSimple getDefaultEstimationComponentSimple() throws ManagementModuleException
	{
		try
		{
			EstimationComponentSimple ret = estimationComponentSimpleDAO
					.findByIdProjet(ReferentielManager.ESTIMATIONCOMPONENTSIMPLE_DEFAULT_PROJECT_ID);
			return (EstimationComponentSimple) ret.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new ManagementModuleException("Problem while cloning the default transformation", e);
		}

	}

	@Override
	public Language getLanguage(final String name) throws ManagementModuleException
	{
		return languageDAO.findByName(name);
	}

	@Override
	public AdjustWeight getAdjustWeightByFunctionalId(final String pFunctionalId) throws ManagementModuleException
	{
		return adjustWeightDAO.findByFunctionalId(pFunctionalId);
	}

	@Override
	public UnitTime getDefaultUnitTime() throws ManagementModuleException
	{
		try
		{
			return getUnitTimeByIdFunctional(ManagementModuleConstants.UNIT_TIME_WEEK);
		}
		catch (final ManagementModuleException mex)
		{
			throw new ManagementModuleException("Unable to get default unit time", mex);
		}
	}

	public void setUnitTimeDAO(final UnitTimeDAO pUnitTimeDAO)
	{
		unitTimeDAO = pUnitTimeDAO;
	}

	public void setDisciplineDAO(final DisciplineDAO pDisciplineDAO)
	{
		disciplineDAO = pDisciplineDAO;
	}

	public void setMarkerTypeDAO(final MarkerTypeDAO pMarkerTypeDAO)
	{
		markerTypeDAO = pMarkerTypeDAO;
	}

	public void setStatusProjectPlanDAO(final StatusProjectPlanDAO pStatusProjectPlanDAO)
	{
		statusProjectPlanDAO = pStatusProjectPlanDAO;
	}

	public void setScopeTypeDAO(final ScopeTypeDAO pScopeTypeDAO)
	{
		scopeTypeDAO = pScopeTypeDAO;
	}

	public void setStatusScopeDAO(final StatusScopeDAO pStatusScopeDAO)
	{
		statusScopeDAO = pStatusScopeDAO;
	}

	public void setAdjustFactorDAO(final AdjustFactorDAO pAdjustFactorDAO)
	{
		adjustFactorDAO = pAdjustFactorDAO;
	}

	public void setSteeringParamDAO(final SteeringParamDAO pSteeringParamDAO)
	{
		steeringParamDAO = pSteeringParamDAO;
	}

	public void setTransformationDAO(final TransformationDAO pTransformationDAO)
	{
		transformationDAO = pTransformationDAO;
	}

	public void setPhaseTypeDAO(final PhaseTypeDAO pPhaseTypeDAO)
	{
		phaseTypeDAO = pPhaseTypeDAO;
	}

	public void setEstimationComponentSimpleDAO(final EstimationComponentSimpleDAO pEstimationComponentSimpleDAO)
	{
		estimationComponentSimpleDAO = pEstimationComponentSimpleDAO;
	}

	public void setAdjustWeightDAO(final AdjustWeightDAO pAdjustWeightDAO)
	{
		adjustWeightDAO = pAdjustWeightDAO;
	}

	public void setLanguageDAO(final LanguageDAO pLanguageDAO)
	{
		languageDAO = pLanguageDAO;
	}

	public void setBusinessObjectFactory(final BusinessObjectFactory pBusinessObjectFactory)
	{
		businessObjectFactory = pBusinessObjectFactory;
	}

}