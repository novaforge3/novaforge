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

import java.util.List;
import java.util.Set;



public interface ReferentielManager
{

   /** Constant which represents the projectId key where are stored default transformation values */
   String TRANSFORMATION_DEFAULT_PROJECT_ID = "default";
   
   
   /** Constant which represents the projectId key where are stored default transformation values */
   String ESTIMATIONCOMPONENTSIMPLE_DEFAULT_PROJECT_ID = "default";
   /**
    * This method returns an instance of UnitTime
    * 
    * @return UnitTime
    */
   UnitTime newUnitTime();

   /**
    * This method creete a UnitTime
    * 
    * @param ut
    * @return UnitTime
    */
   UnitTime createUnitTime(UnitTime ut) throws ManagementModuleException;

   /**
    * This method find and return a UnitTime with a name = pName
    * 
    * @param name
    * @return UnitTime
    */
   UnitTime getUnitTime(String pName) throws ManagementModuleException;
   
   /**
    * This method find and return a UnitTime by IdFunctional
    * 
    * @param dFunctional
    * @return UnitTime
    */   
   UnitTime getUnitTimeByIdFunctional(String name) throws ManagementModuleException;

   /**
    * This method returns all UnitTime
    * 
    * @return UnitTime list
    */
   List<UnitTime> getAllUnitTimes() throws ManagementModuleException;


   /**
    * This method returns an instance of Discipline
    * 
    * @return Discipline
    */
   Discipline newDiscipline();
   
   /**
    * This method returns an instance of DisciplineJointure
    * 
    * @return DisciplineJointure
    */
   ProjectDiscipline newProjectDiscipline();
   
   /**
    * This method create a Discipline
    * 
    * @param discipline the discipline to create
    * @return Discipline
    */
   Discipline createDiscipline(final Discipline discipline) throws ManagementModuleException;

   /**
    * This method find and return a Discipline by its functionalId
    * 
    * @param functionalId the functionalId to look for
    * @return Discipline the discipline which has the functionalId (or null)
    */
   Discipline getDiscipline(final String functionalId) throws ManagementModuleException;

   /**
    * This method returns all Discipline discipline
    * 
    * @return Discipline list
    */
   List<Discipline> getAllDiscipline() throws ManagementModuleException;
   
   /**
    * This method returns all Discipline discipline by default for a project
    * 
    * @return DisciplineJointure list
    */
   Set<ProjectDiscipline>  getDefaultProjectDisciplines(Project project)throws ManagementModuleException;
    

   /************************* MarkerType ********************************************************************/

   /**
    * This method returns an instance of MarkerType
    * 
    * @return MarkerType
    */
   MarkerType newMarkerType();

   /**
    * get a MarkerType
    * 
    * @param string
    * @throws ManagementModuleException
    */
   MarkerType getMarkerType(String name) throws ManagementModuleException;

   /**
    * creete a MarkerType
    * 
    * @param marker
    * @throws ManagementModuleException
    */
   MarkerType getMarkerTypeByFuncionalId(String functionalId) throws ManagementModuleException;
   
   /**
    * Return the list of marker's type 
    * @return
    */
   List<MarkerType> getMarkerTypeList() throws ManagementModuleException;
   
   /**
    * creete a MarkerType
    * 
    * @param marker
    * @throws ManagementModuleException
    */
   MarkerType createMarkerType(MarkerType markerType) throws ManagementModuleException;

   /************************* StatusProjectPlan ********************************************************************/

   /**
    * This method returns an instance of StatusProjectPlan
    * 
    * @return StatusProjectPlan
    */
   StatusProjectPlan newStatusProjectPlan();

   /**
    * creete a StatusProjectPlan
    * 
    * @param marker
    * @throws ManagementModuleException
    */
   StatusProjectPlan createStatusProjectPlan(final StatusProjectPlan spp) throws ManagementModuleException;

   /**
    * Return a StatusProjectPlan
    * 
    * @param name
    * @return
    * @throws ManagementModuleException
    */
   StatusProjectPlan getStatusProjectPlan(final String name) throws ManagementModuleException;
   
   /**
    * Return a StatusProjectPlan
    * 
    * @param name
    * @return
    * @throws ManagementModuleException
    */
   StatusProjectPlan getStatusProjectPlanByFunctionalId(String pFunctionalId) throws ManagementModuleException;

   /**
    * Update a StatusProjectPlan
    * 
    * @param spp
    * @throws ManagementModuleException
    */
   boolean updateStatusProjectPlan(final StatusProjectPlan spp) throws ManagementModuleException;

   /**
    * Return All StatusProjectPlan
    * 
    * @return
    * @throws ManagementModuleException
    */
   List<StatusProjectPlan> getAllStatusProjectPlan() throws ManagementModuleException;

   /************************* ScopeType ********************************************************************/

   /**
    * This method returns an instance of ScopeType
    * 
    * @return ScopeType
    */
   ScopeType newScopeType();

   /**
    * This method creete a ScopeType
    * 
    * @param ScopeType
    * @return ScopeType
    */
   ScopeType createScopeType(final ScopeType st) throws ManagementModuleException;

   boolean deleteScopeType(final String name) throws ManagementModuleException;

   /**
    * This method find and return a ScopeType with name = pName
    * 
    * @param pName
    * @return ScopeType
    */
   ScopeType getScopeType(final String pName) throws ManagementModuleException;

   /**
    * This method update a ScopeType and return false if the update failed
    * 
    * @param st
    * @return boolean
    */
   boolean updateScopeType(final ScopeType st) throws ManagementModuleException;

   /**
    * This method return all ScopeType
    * 
    * @return ScopeType list
    */
   List<ScopeType> getAllScopeType() throws ManagementModuleException;
   
   /**
    * This method find and return a ScopeType with functionalId = pFunctionalId
    * 
    * @param pFunctionalId
    * @return ScopeType
    */
   ScopeType getScopeTypeByfunctionalId(String functionalId) throws ManagementModuleException;

   /************************* StatusScope ********************************************************************/

   StatusScope newStatusScope();

   StatusScope createStatusScope(final StatusScope st) throws ManagementModuleException;

   boolean deleteStatusScope(final String name) throws ManagementModuleException;

   /**
    * find a statusScope for the specified functionalId
    * @param functionalId
    * @return
    * @throws ManagementModuleException
    */
   StatusScope getStatusScope(final String functionalId) throws ManagementModuleException;

   boolean updateStatusScope(final StatusScope st) throws ManagementModuleException;

   List<StatusScope> getAllStatusScope() throws ManagementModuleException;

   /************************* AdjustFactor ********************************************************************/

   AdjustFactor newAdjustFactor();

   AdjustFactor createAdjustFactor(final AdjustFactor adjustF) throws ManagementModuleException;
   
   /**
    * This method returns all AdjustFactor 
    * 
    * @return AdjustFactor list
    */
   List<AdjustFactor> getAllAdjustFactor() throws ManagementModuleException;

   /**
    * This method returns one AdjustFactor by its name
    * 
    * @return AdjustFactor
    */
   AdjustFactor getAdjustFactor(String name)throws ManagementModuleException ;
  
   /************************* PhaseType ********************************************************************/

   /**
    * This method returns an instance of Project
    * 
    * @return Project
    */
   PhaseType newPhaseType();

   PhaseType createPhaseType(final PhaseType phaseType) throws ManagementModuleException;
   
   PhaseType getPhaseType(String functionalId) throws ManagementModuleException;

   List<PhaseType> getAllPhaseTypes() throws ManagementModuleException;


   /************************* SteeringParameter ********************************************************************/
   
   /**
    * This method returns all teeringParameter from plan project
    * 
    * @return SteeringParameter list
    */
   List<SteeringParameter> getSteeringParameter() throws ManagementModuleException;
   
   /**
    * This method returns an instance of SteeringParameter
    * 
    * @return SteeringParameter
    */
    SteeringParameter newSteeringParameter();
    
    /**
     * This method creete a SteeringParameter
     * 
     * @param steeringParamater
     * @return SteeringParameter
     */
    SteeringParameter createSteeringParamater(SteeringParameter steeringParamater) throws ManagementModuleException;
    
    /************************* Transformation ********************************************************************/

    /**
     * This method returns all Transformation 
     * 
     * @return Transformation list
     */
    List<Transformation> getAllTransformation() throws ManagementModuleException ;
    
    /**
     * This method returns Transformation bu Id
     * 
     * @param Long id
     * @return Transformation list
     */
    Transformation getTransformation(Long id) throws ManagementModuleException ;
    
    /**
     * This method returns an instance of Transformation
     * 
     * @return Transformation
     */
     Transformation newTransformation();
     
     /**
      * This method creete a Transformation
      * 
      * @param transformation
      * @return Transformation
      */
     Transformation createTransformation(Transformation transformation) throws ManagementModuleException;
     
     /**
      * This method to get a Transformation by project id
      * 
      * @param String
      * @return Transformation
      */
     Transformation getTransformation(String idProject) throws ManagementModuleException;

   /**
    * This method returns the default transformation to use for new project
    * @return the default transformation
    * @throws ManagementModuleException exception during retrieving
    */
   Transformation getDefaultTransformation() throws ManagementModuleException;

     /************************* AdjustWeight ********************************************************************/

     /**
      * This method returns an instance of AdjustWeight
      * 
      * @return AdjustWeight
      */
     AdjustWeight newAdjustWeight();

     /**
      * This method creete a AdjustWeight
      * 
      * @param adjustWeight
      * @return AdjustWeight
      */
     AdjustWeight createAdjustWeight(AdjustWeight sf)
     throws ManagementModuleException;

     /**
      * This method returns all AdjustWeight 
      * 
      * @return AdjustWeight list
      */
     List<AdjustWeight> getAllAdjustWeight() throws ManagementModuleException;
     
     
     /************************* Language ********************************************************************/

     /**
      * This method returns an instance of Language
      * 
      * @return Language
      */
     Language newLanguage();

     /**
      * This method creete a Language
      * 
      * @param Language
      * @return Language
      */
     Language createLanguage(Language lan)
     throws ManagementModuleException;

     /**
      * This method returns all Language 
      * 
      * @return Language list
      */
     List<Language> getAllLanguage() throws ManagementModuleException;
     
	   /************************* EstimationComponentSimple ********************************************************************/

	   EstimationComponentSimple newEstimationComponentSimple();

	   EstimationComponentSimple createEstimationComponentSimple(EstimationComponentSimple sf)
	   throws ManagementModuleException;
	   
	   EstimationComponentSimple getEstimationComponentSimple(String idProject) throws ManagementModuleException;
	   
	   /**
	    * This method returns the default transformation to use for new project
	    * @return the default transformation
	    * @throws ManagementModuleException exception during retrieving
	    */
	   EstimationComponentSimple getDefaultEstimationComponentSimple() throws ManagementModuleException;
	   
     /**
      * This method returns one Language by its name
      * 
      * @return Language
      */
     Language getLanguage(String name)throws ManagementModuleException ;


    /**
     * This method get a AdjustWeight by functionnal ID
     * 
     * @param String
     * @return AdjustWeight
     */
	AdjustWeight getAdjustWeightByFunctionalId(String pFunctionalId)
			throws ManagementModuleException;

   /**
    * get the default unit time
    * @return the default unit time
    * @throws ManagementModuleException problem during recovery
    */
   UnitTime getDefaultUnitTime() throws ManagementModuleException;

}
