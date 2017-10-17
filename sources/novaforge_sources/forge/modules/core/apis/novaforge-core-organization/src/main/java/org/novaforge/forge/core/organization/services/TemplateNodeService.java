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
package org.novaforge.forge.core.organization.services;

import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.TemplateApplication;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author sbenoist
 */
public interface TemplateNodeService
{

  /**
   * This method allows to add an application to a project
   * 
   * @param pTemplateId
   * @param parentNodeUri
   * @param applicationLabel
   * @param pluginUUID
   * @param pRolesMapping
   * @throws TemplateServiceException
   */
  TemplateApplication addApplication(String pTemplateId, String parentNodeUri, String applicationLabel,
      UUID pluginUUID, Map<String, String> pRolesMapping) throws TemplateServiceException;

  /**
   * This method allows to remove an application from a project
   * 
   * @param pTemplateId
   * @param parentNodeUri
   * @param applicationUri
   * @param pUsername
   *          the username of the user which make the action
   * @throws TemplateServiceException
   */
  void deleteApplication(String pTemplateId, String parentNodeUri, String applicationUri)
      throws TemplateServiceException;

  /**
   * This method returns an application by its uri
   * 
   * @param applicationUri
   * @return Application
   * @throws TemplateServiceException
   */
  TemplateApplication getApplication(final String pTemplateId, String applicationUri)
      throws TemplateServiceException;

  /**
   * This method returns all the applications of a project
   * 
   * @param pTemplateId
   * @return List<Application>
   * @throws TemplateServiceException
   */
  List<TemplateApplication> getAllTemplateApplications(String pTemplateId) throws TemplateServiceException;

  // methods which concern space
  /**
   * This method returns all the applications of a space
   * 
   * @param pSpaceNodeUri
   * @param pTemplateId
   * @return List<Application>
   * @throws TemplateServiceException
   */
  List<TemplateApplication> getAllSpaceApplications(final String pSpaceNodeUri, String pTemplateId)
      throws TemplateServiceException;

  /**
   * This method returns all the spaces of a project
   * 
   * @param pTemplateId
   * @return List<Space>
   * @throws TemplateServiceException
   */
  List<Space> getAllSpaces(final String pTemplateId) throws TemplateServiceException;

  /**
   * This method returns a space by its uri
   * 
   * @param spaceNodeUri
   * @return Space
   * @throws TemplateServiceException
   */
  Space getSpace(final String pTemplateId, String spaceNodeUri) throws TemplateServiceException;

  /**
   * This method allows to update a space
   * 
   * @param space
   * @throws TemplateServiceException
   */
  void updateSpace(final String pTemplateId, Space space) throws TemplateServiceException;

  /**
   * This method allows to remove a space
   * 
   * @param spaceNodeUri
   * @param parentNodeUri
   * @throws TemplateServiceException
   */
  void deleteSpace(final String pTemplateId, String spaceNodeUri) throws TemplateServiceException;

  /**
   * This method allows to add a space to another space
   * 
   * @param space
   * @throws TemplateServiceException
   */
  Space addSpace(final String pTemplateId, final Space pSpace) throws TemplateServiceException;

  /**
   * This method allows to instanciate a space
   * 
   * @return Space
   */
  Space newSpace();

}
