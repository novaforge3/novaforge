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

import org.novaforge.forge.commons.reporting.model.OutputFormat;
import org.novaforge.forge.tools.managementmodule.domain.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.domain.ApplicativeRights;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentSimple;
import org.novaforge.forge.tools.managementmodule.domain.Language;
import org.novaforge.forge.tools.managementmodule.domain.Membership;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.Role;
import org.novaforge.forge.tools.managementmodule.domain.Transformation;
import org.novaforge.forge.tools.managementmodule.domain.User;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;


public interface ManagementModuleManager
{


   /**
    * This method creete an instance of Project
    * 
    * @return Project
    */
   Project newProject();

   /**
    * This method creete a Project and returns an instance of Project
    * 
    * @param pProjectName
    * @param pProjectDescription
    * @param pProjectId
    * @param string 
    * @return Project
    */
   Project creeteProject(String pProjectName, String pProjectDescription, String pProjectId,EstimationComponentSimple estimationComponentSimple,Transformation transformation )
   throws ManagementModuleException;
   
   /**
    * This method create a Project and returns an instance of Project
    * 
    * @param toolProject the new project to create
    * @return Project the persistent project with its id
    * @throws ManagementModuleException problem during creation
    */
   Project creeteProject(Project toolProject) throws ManagementModuleException;

   /**
    * This method return a Project with id = pProjectId
    * 
    * @param pProjectId
    * @return Project
    */
   Project getProject(String projectId) throws ManagementModuleException;

   /**
    * This method update a Project
    * 
    * @param project
    */
   Project updateProject(Project project) throws ManagementModuleException;

   /**
    * This method delete a Project
    * 
    * @param projectId
    */
   boolean deleteProject(String projectId) throws ManagementModuleException;

   /**
    * This method verify if a project with id=pProjectId exist
    * 
    * @param projectId
    * @return boolean
    */
   boolean existProjectId(String pProjectId) throws ManagementModuleException;


   /**
    * This method returns an instance of User
    * 
    * @return User
    */
   User newUser();

   boolean existUserLogin(String pLogin) throws ManagementModuleException;

   /**
    * This method creete an User and returns an instance of User
    * 
    * @param pUserFirstName
    * @param pUserLastName
    * @param pUserLastLogin
    * @param pUserLanguage
    * @return User
    */
   User creeteUser(String pUserFirstName, String pUserLastName, String pUserLastLogin, String pUserLanguage)
   throws ManagementModuleException;

   /**
    * This method return an User with login=userLogin
    * 
    * @param userLogin
    * @return User
    */
   User getUser(final String userLogin) throws ManagementModuleException;

   /**
    * This method update an User
    * 
    * @param user
    */
   boolean updateUser(final User user) throws ManagementModuleException;

   /**
    * This method delete an User
    * 
    * @param user
    */
   boolean deleteUser(final User user) throws ManagementModuleException;


   /**
    * This method returns an instance of Role
    * 
    * @return Role
    */
   Role newRole();

   /**
    * This method create a role and returns an instance of role
    * 
    * @param role
    * @return Role
    */
   Role createRole(Role role) throws ManagementModuleException;

   
   /**
    * This methods update the role data
    * @param role the role to update
    * @return the updated role
    * @throws ManagementModuleException exception during modification
    */
   Role updateRole(Role role) throws ManagementModuleException;
   
   /**
    * This method return a role with name=roleName
    * 
    * @param roleName
    * @return Role
    */
   Role getRoleByName(String roleName) throws ManagementModuleException;

   
   /**
    * This method return the role with the functional id of the parameter
    * 
    * @param functionalId the functionalId of the role to look for
    * @return Role the appropriate role
    */
   Role getRoleByFunctionalId(String functionalId) throws ManagementModuleException;
   
   /**
    * This method return all the roles
    * 
    * @return roleList
    */
   List<Role> getAllRoles() throws ManagementModuleException;

   /************************* membership *******************************/

   /**
    * This method add an user with a specific role to a project
    * 
    * @param pProjectId
    * @param pLogin
    * @param pRoleName
    * @return membership
    */
   Membership addUserMembership(String pProjectId, String pLogin, String pRoleName)
   throws ManagementModuleException;

   /**
    * This method remove all membership of a project
    * 
    * @param pProjectId
    */
   void removeAllProjectMembeship(String pProjectId) throws ManagementModuleException;

   /**
    * This method returns an instance of Membership
    * 
    * @return Membership
    */
   Membership newMembership();

   /**
    * This method return an user membership on a project
    * 
    * @param login
    * @param projectId
    * @return Membership
    */
   Membership getMembership(String login, String projectId) throws ManagementModuleException;

   /**
    * This method update an user membership on a project
    * 
    * @param User
    * @param Project
    * @param Role
    */
   boolean updateMembership(User u, Project p, Role newRole) throws ManagementModuleException;

   /**
    * This method delete an user membership on a project
    * 
    * @param User
    * @param Project
    */
   boolean deleteMembership(User u, Project p) throws ManagementModuleException;

   /**
    * This method delete a membership
    * 
    * @param Membership
    */
   void deleteMembership(Membership m) throws ManagementModuleException;

   /**
    * This method returns an instance of Language
    * 
    * @return Language
    */
   Language newLanguage();

   /**
    * This method create a Language
    * 
    * @param language
    * @return Language
    */
   Language creeteLanguage(final Language language) throws ManagementModuleException;

   /**
    * This method return a Language from his name
    * 
    * @param name
    * @return Language
    */
   Language getLanguage(final String name) throws ManagementModuleException;

   /**
    * This method return all Language
    * 
    * @return Language list
    */
   List<Language> getAllLanguages() throws ManagementModuleException;

   /**
    * This method returns the project wich have the pProjectId (or null) with all collections loaded (lazy includes)
    * @param pProjectId the id of the project to look for
    * @return the project identified by the pProjectId or nullpProjectId
    * @throws ManagementModuleException exception during the recuperation
    */
   Project getFullProject(String projectId) throws ManagementModuleException;

   
   /**
    * Get a project by his instanceId
    * @param instanceId the instanceId
    * @return the appropriate project
    * @throws ManagementModuleException exception during recovery
    */
   Project getProjectByInstanceId(String instanceId) throws ManagementModuleException;

   /************************* ApplicativeFunction ********************************************************************/

   ApplicativeFunction newApplicativeFunction();

   ApplicativeFunction createApplicativeFunction(ApplicativeFunction sf)
         throws ManagementModuleException;

   /**
    * This method returns all SteeringFunction 
    * 
    * @return SteeringFunction list
    */
   List<ApplicativeFunction> getAllApplicativeFunction() throws ManagementModuleException;

   /**
    * This method returns one ApplicativeFunction by its functional Id
    * 
    * @return ApplicativeFunction
    */
   ApplicativeFunction getApplicativeFunction(String functionalId)throws ManagementModuleException ;

   /************************* ApplicativeRights ********************************************************************/

   ApplicativeRights newApplicativeRights();
   
   ApplicativeRights newApplicativeRights(ApplicativeFunction s, Role r, String access);

   ApplicativeRights createApplicativeRights(ApplicativeRights s) throws ManagementModuleException;

   ApplicativeRights getApplicativeRight(String toolRoleName, String steeringFunctionName)
         throws ManagementModuleException;

   /**
    * Generate a report for followings arguments
    * @param rptdesignName Name of the template BIRT 
    * @param format the output's format
    * @param parameters Map of parameters needed for generate the document
    * @param outputStream the outputStream for return the generated document
    * @throws ManagementModuleException
    */
   void renderReport(String rptdesignName, OutputFormat format, Map<String, Object> parameters, OutputStream outputStream) throws ManagementModuleException ;
   
}
