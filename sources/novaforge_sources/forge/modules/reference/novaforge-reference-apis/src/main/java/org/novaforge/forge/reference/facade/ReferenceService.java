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
package org.novaforge.forge.reference.facade;

import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.reference.exceptions.ReferenceFacadeException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This facade allows to manage forge reference object.
 * 
 * @author lamirang
 */
public interface ReferenceService
{
  /**
   * This metod return the projet which representing the forge reference
   * 
   * @return project object
   * @throws ReferenceFacadeException
   */
  Project getProjectReference() throws ReferenceFacadeException;

  /**
   * This metod allows to update the projet information. Only project's name and description.
   * 
   * @param pOldName
   *          the preivous project name
   * @param pProject
   *          represents the new project object
   * @return true if succeed
   * @throws ReferenceFacadeException
   */
  boolean updateProjectReference(final String pOldName, final Project pProject)
      throws ReferenceFacadeException;

  /**
   * This method returns a space of reference forge project by its uri
   * 
   * @param spaceNodeUri
   * @return Space
   * @throws ReferenceFacadeException
   */
  Space getSpace(final String spaceNodeUri) throws ReferenceFacadeException;

  /**
   * This method allows to create a space into forge reference project
   * 
   * @param pName
   *          represents space's name
   * @return true if succeed
   * @throws ReferenceFacadeException
   */
  boolean createSpace(final String pName) throws ReferenceFacadeException;

  /**
   * This method allows to update a space into forge reference project
   * 
   * @param pOldName
   *          the previous space name
   * @param pSpace
   *          represents space itself
   * @return true if succeed
   * @throws ReferenceFacadeException
   */
  boolean updateSpace(final String pOldName, final Space pSpace) throws ReferenceFacadeException;

  /**
   * This method allows to delete a space from forge reference project
   * 
   * @param pSpaceUri
   *          represents space uri
   * @return true if succeed
   * @throws ReferenceFacadeException
   */
  boolean deleteSpace(final String pSpaceUri) throws ReferenceFacadeException;

  /**
   * This method allows to create an application into a space of the forge reference project
   * 
   * @param pSpaceUri
   *          represents space uri
   * @param pApplicationName
   *          represents application name
   * @param pPluginUUID
   *          represents plugin uuid which will be used as application
   * @param pRolesMapping
   *          represents a mapping between forge roles and plugin roles
   * @return true if succeed
   * @throws ReferenceFacadeException
   */
  boolean createApplication(final String pSpaceUri, final String pApplicationName, final UUID pPluginUUID,
      final Map<String, String> pRolesMapping) throws ReferenceFacadeException;

  /**
   * This method allows to update an application role mapping into a space of the forge reference project.
   * 
   * @param pUri
   *          represents application uri
   * @param pRolesMapping
   *          represents a mapping between forge roles and plugin roles
   * @return true if succeed
   * @throws ReferenceFacadeException
   */
  boolean updateApplication(final String pUri, final Map<String, String> pRolesMapping)
      throws ReferenceFacadeException;

  /**
   * This method allows to delete an application from a space of the forge reference project.
   * 
   * @param pApplicationUri
   *          represents application uri
   * @return true if succeed
   * @throws ReferenceFacadeException
   */
  boolean deleteApplication(final String pApplicationUri) throws ReferenceFacadeException;

  /**
   * This metod return the whole existing spaces for the forge reference project.
   * 
   * @return list of spaces
   * @throws ReferenceFacadeException
   */
  List<Space> getSpaces() throws ReferenceFacadeException;

  /**
   * This metod return the existing applications for a space of the forge reference project.
   * 
   * @param pSpaceUri
   * @return list of applications
   * @throws ReferenceFacadeException
   */
  List<ProjectApplication> getAllApplicationsForSpace(final String pSpaceUri) throws ReferenceFacadeException;

  /**
   * This method will return the role list available for the forge reference project.
   * 
   * @return list of roles available
   * @throws ReferenceFacadeException
   */
  List<ProjectRole> getRoles() throws ReferenceFacadeException;

  /**
   * @return true if the Reference Project has been created.
   */
  boolean isProjectReferenceCreated();
}
