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
package org.novaforge.forge.tools.requirements.common.services;

import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.model.ECodeStatus;
import org.novaforge.forge.tools.requirements.common.model.ETestStatus;
import org.novaforge.forge.tools.requirements.common.model.IDirectory;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;

import java.util.Set;

/**
 * The RequirementManagerService provides several basics methods to handle requirements. This service is cut
 * in two main side.the first, manages requirement tree, and the second, manages a composite (defined by many
 * hierarchicals nodes) in order to place the requirement tree. To avoid recover the whole of tree from the
 * root node , you can use methods which load the composite with some references , subsequently, load the
 * requirement where you want deep data structure.
 * 
 * @author Guillaume Morin
 * @since 3.1.0
 */

public interface RequirementManagerService
{
  /**
   * This method allows to create a persistent project
   * 
   * @param pProject
   *          the project to create
   * @return the project created
   * @throws RequirementManagerServiceException
   */
  IProject createProject(IProject pProject) throws RequirementManagerServiceException;

  /**
   * This method allows to update a persistent repository
   * 
   * @param pProject
   *          the project to update
   * @return the project updated
   * @throws RequirementManagerServiceException
   */
  IProject updateProject(IProject pProject) throws RequirementManagerServiceException;

  /**
   * This method allows to delete a persistent project
   * 
   * @param pProjectId
   *          the projectId of the project to delete
   * @throws RequirementManagerServiceException
   */
  void deleteProject(String pProjectId) throws RequirementManagerServiceException;

  /**
   * This method returns a project by ID
   * 
   * @param pProjectID
   * @return the project
   * @throws RequirementManagerServiceException
   */
  IProject findProjectByID(String pProjectID) throws RequirementManagerServiceException;

  /**
   * The method <code><b>updateRequirement</b></code> represents a way to update a requirement into the
   * persistance system selected. We must create a requirement before update this one. Note : if you try to
   * update a requirement from a requirement referenced into a directory, you will have a exception. The
   * following code defines how you can update your requirement :<code>
   * <blockquote>
   * <pre>
   * String ref = &quot;#REF_REQUIREMENT&quot;;
   * requirement = loadrequirement(ref);
   * requirement.setName(&quot;newName&quot;);
   * updateRequirement(requirement);
   * </pre>
   * </blockquote>
   * </code> <br>
   * You can obtain a reference directly from a {@link IDirectory#getRequirements() directory} : <code>
   * <blockquote>
   * <pre>
   * 
   * requirementRef = directory.findRequirements().iterator().next();
   * requirement=loadrequirement(requirementRef);
   * requirement.setName(&quot;newName&quot;);
   * updateRequirement(requirement);
   * 
   * </pre>
   * </blockquote>
   * </code>
   * 
   * @param pRequirmement
   *          A Requirement Object (that already exists)
   * @return The requirement updated
   * @throws RequirementManagerServiceException
   *           throw exception if the service can not udpate the requirement
   * @see # createRequirement(IRequirement)
   * @author Guillaume Morin
   */
  IRequirement updateRequirement(IRequirement pRequirmement) throws RequirementManagerServiceException;

  /**
   * The method <code><b>createRequirement</b></code> represents a way to create a requirement into the
   * persistance system selected. </pre> <blockquote> </code>
   * 
   * @param pRequirmement
   *          A valid Requirement Object
   * @return The requirement created
   * @throws RequirementManagerServiceException
   *           throw exception if the service can not create the requirement (for example, this one already
   *           exists)
   * @author Guillaume Morin
   * @see # updateRequirement(IRequirement)
   */
  void createRequirement(IRequirement pRequirmement) throws RequirementManagerServiceException;

  /**
   * The method <code><b>createDirectory</b></code> represents a way to create a directory into the
   * persistance system selected. A directory tree is builded as a composite pattern where the directory
   * created can have saveral children and one parent. Also, the directory may contain many requirements
   * (which are show as the leafs of the current directory). The following schema defines how the tree can be
   * builded <blockquote>
   * 
   * <pre>
   *   - DIR-A-ROOT
   *   -- DIR-B {requirement-1,requirement-2}
   *   --- DIR-C {requirement-3}
   *   -- DIR-D {requirement-4}
   * 
   * </pre>
   * 
   * </blockquote> </code>
   * 
   * @param pDirectory
   *          A Directory Object (that already exists in persistance system)
   * @return The directory created
   * @throws RequirementManagerServiceException
   *           throw exception if the service can not create the directory or if the directory already
   *           exists
   * @see #findDirectoryByReference(String)
   * @see #findAllRootDirectoriesByRepository(String)
   * @see #deleteDirectoryTree(IDirectory)
   * @author Guillaume Morin
   */
  IDirectory createDirectory(IDirectory pDirectory) throws RequirementManagerServiceException;

  /**
   * The method <code><b>findRootDirectoryByRepository</b></code> represents a way to get the root
   * directories set from the project repository. This root contains all directories and the requirements
   * datas references. For each requirement reference provided, you can use basics datas (name,ect ...).
   * <b>To have more</b>, load the requirement tree from a requirement reference.
   * 
   * @param pRepository
   *          A repository key Object
   * @return All The root directories
   * @throws RequirementManagerServiceException
   *           throw exception if the service does not find the repository key
   * @see #findDirectoryByReference(String)
   * @see #createDirectory(IDirectory)
   * @see #findDirectoryByReference(String)
   * @see #loadRequirementTree(IRequirement)
   * @author Guillaume Morin
   */
  Set<IDirectory> findAllRootDirectoriesByRepository(IRepository pRepository)
      throws RequirementManagerServiceException;

  /**
   * The method <code><b>loadRootDirectoryTreeByRepository</b></code> represents a way to get the full root
   * directories set from the project repository. This root contains all directories, all the requirements
   * datas references and all the version trees of each requirement whic contains requirement links
   * 
   * @param pRepository
   * @return All The root directory trees
   * @throws RequirementManagerServiceException
   *           throw exception if the service does not find the repository key
   */
  Set<IDirectory> loadAllRootDirectoryTreesByRepository(IRepository pRepository)
      throws RequirementManagerServiceException;

  /**
   * The method <code><b>findRequirementsByRepository</b></code> represents a way to obtain a set of
   * requirement from a repository. <br>
   * <i><b> Note </b>: this function can be very heavy in machine resources (that depend of the
   * implementation) </i>
   * 
   * @param pRepository
   *          A repository key Object (that already exists in persistance system)
   * @return set of requirement tree
   * @throws RequirementManagerServiceException
   *           throw exception if the service does not find the repository key
   * @author Guillaume Morin
   */
  Set<IRequirement> findRequirementsByRepository(IRepository pRepository)
      throws RequirementManagerServiceException;

  /**
   * @param pRootFound
   * @return
   * @throws RequirementManagerServiceException
   */
  IDirectory updateDirectory(IDirectory pRootFound) throws RequirementManagerServiceException;

  /**
   * The method <code><b>loadRequirementTree</b></code> represents a way to load the whole of related datas
   * of a requirement (i.e. requirement version, for each version a context context can have resources, test,
   * tasks, etc ...)
   * 
   * @param pRequirement
   *          A valid Requirement Object (reference filled in directory for example)
   * @return The requirement filled with the whole of datas.
   * @throws RequirementManagerServiceException
   *           throw exception if the service does not find the requirement
   * @author Guillaume Morin
   */
  IRequirement loadRequirementTree(IRequirement pRequirement)
      throws RequirementManagerServiceException;

  /**
   * The method <code><b>loadRequirementTree</b></code> represents a way to load the whole of related datas
   * of a requirement (i.e. requirement version, for each version, a context can have many resources, test,
   * tasks, etc ...)
   * 
   * @param pRequirement
   *          A valid requirement functional reference
   * @return The requirement filled with the whole of datas.
   * @throws RequirementManagerServiceException
   *           throw exception if the service does not find the requirement
   * @author Guillaume Morin
   */
  IRequirement loadRequirementTree(String pFunctionalReference)
      throws RequirementManagerServiceException;

  /**
   * @param pReference
   * @return
   * @throws RequirementManagerServiceException
   */
  IDirectory findDirectoryByReference(String pReference) throws RequirementManagerServiceException;

  /**
   * @param pRequirement
   * @throws RequirementManagerServiceException
   */
  void deleteRequirementTree(IRequirement pRequirement) throws RequirementManagerServiceException;

  /**
   * The method <code><b>getRequirementCodeStatus</b></code> determines the requirement status for the
   * <b>code</b>. This status depends of business rules expressed by the service implementation
   * 
   * @param pRequirement
   *          A valid requirement object
   * @return The rated status
   * @throws RequirementManagerServiceException
   * @author Guillaume Morin
   */
  ECodeStatus getRequirementCodeStatus(IRequirement pRequirement)
      throws RequirementManagerServiceException;

  /**
   * The method <code><b>getRequirementCodeStatus</b></code> determines the requirement status for the
   * <b>code</b>. This status depends of business rules expressed by the service implementation
   * 
   * @param pRequirement
   *          A valid requirement object
   * @return The rated status
   * @throws RequirementManagerServiceException
   * @author Guillaume Morin
   */
  ETestStatus getRequirementTestStatus(IRequirement pRequirement)
      throws RequirementManagerServiceException;

  /**
   * This method allows to delete a root directory and its entire composition
   * 
   * @param pDirectory
   * @throws RequirementManagerServiceException
   */
  void deleteRootDirectoryTree(IDirectory pDirectory) throws RequirementManagerServiceException;

  /**
   * This method allows to load a requirement tree by its name
   * 
   * @param pRepository
   * @param pName
   * @return IRequirement
   * @throws RequirementManagerServiceException
   */
  IRequirement loadRequirementTreeByName(String pRepository, String pName)
      throws RequirementManagerServiceException;

  /**
   * This method allows to load a requirement tree by its id
   * 
   * @param pRequirementId
   * @return IRequirement
   * @throws RequirementManagerServiceException
   */
  IRequirement loadRequirementTreeByID(String pRequirementId)
      throws RequirementManagerServiceException;

}
