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
package org.novaforge.forge.core.organization.dao;

import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Node;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.TemplateApplication;
import org.novaforge.forge.core.organization.model.enumerations.NodeType;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class defines methods to access to {@link Node},{@link Space}, {@link ProjectApplication} and
 * {@link TemplateApplication} data from persistence
 * 
 * @author sbenoist
 * @author Guillaume Lamirand
 * @see Node
 * @see Space
 * @see ProjectApplication
 * @see TemplateApplication
 */
public interface NodeDAO
{
  /**
   * This method will return a new entity detach of persiste context, the returned object will depend on the
   * type asking
   * 
   * @param pNodeType
   *          the type
   * @return new {@link Node} which will be either a {@link Space}, a {@link ProjectApplication} or
   *         {@link TemplateApplication}
   */
  Node newNode(final NodeType pNodeType);

  /**
   * This method will update the object given in parameter
   * 
   * @param pNode
   *          the node to persist
   * @return {@link Node} attached to persistence context
   */
  Node update(final Node pNode);

  /**
   * Find {@link Node} according to its uri
   * 
   * @param pUri
   *          represents the node urito find
   * @return {@link Node} found, can be a {@link Space}, {@link ProjectApplication} or
   *         {@link TemplateApplication}
   * @throws NoResultException
   *           thrown if no {@link Node} are existing for the uri given
   */
  Node findByUri(final String pUri) throws NoResultException;

  /**
   * Find {@link List} of {@link Space} according to a element id
   * 
   * @param pElementId
   *          represents the element id
   * @return {@link List} of {@link Space} found
   */
  List<Space> findByProjectElement(final String pElementId);

  /**
   * Find {@link List} of {@link Space} with their {@link Application} associated according to a element id
   * 
   * @param pElementId
   *          represents the element id
   * @return {@link Map} contains for each {@link Space} {@link List} of {@link Application}
   */
  Map<Space, List<Application>> findSpacesWithAppsForElementId(final String pElementId);

  /**
   * * Find {@link List} of {@link Application} associated according to a space uri
   * 
   * @param pUri
   *          space uri
   * @return {@link List} of {@link Application}
   */
  List<Application> findAppsForSpace(String pUri);

  /**
   * Return the parent {@link Space} of an {@link Application} from its uri
   * 
   * @param pURI
   *          application uri
   * @return parent {@link Space}
   */
  Space findSpaceForApp(String pURI);

  /**
   * This method will update the object given in parameter
   * 
   * @param pSpace
   *          the space to persist
   * @return {@link Space} attached to persistence context
   */
  Space update(final Space pSpace);

  /**
   * Find {@link ProjectApplication} according to its instance uuid
   * 
   * @param pInstanceUUID
   *          represents the instance id
   * @return {@link ProjectApplication} found
   * @throws NoResultException
   *           thrown if no {@link ProjectApplication} are existing for the uuid given
   */
  ProjectApplication findByInstanceId(final UUID pInstanceUUID) throws NoResultException;

  /**
   * Will update the status of a {@link ProjectApplication} according to its instance uuid
   * 
   * @param pApplicationStatus
   *          represents the new status to set
   * @param pInstanceUUID
   *          represents the instance id
   * @throws NoResultException
   *           thrown if no {@link ProjectApplication} are existing for the uuid given
   */
  void updateApplicationStatus(final ApplicationStatus pApplicationStatus, final UUID pInstanceUUID);

  /**
   * This method will update the object given in parameter
   * 
   * @param pProjectApplication
   *          the app to persist
   * @return {@link ProjectApplication} attached to persistence context
   */
  ProjectApplication update(ProjectApplication pProjectApplication);

  /**
   * This method allows to add a {@link Application} (ie {@link ProjectApplication} or
   * {@link TemplateApplication} to a {@link Space}
   * 
   * @param pApplication
   *          the application to add
   * @param pSpaceUri
   *          the parent space uri
   * @return a {@link Application} persisted
   * @throws SpaceServiceException
   */
  Application addApplicationToSpace(String pSpaceUri, Application pApplication) throws SpaceServiceException;

  /**
   * This method allows to delete an {@link Application} (ie {@link ProjectApplication} or
   * {@link TemplateApplication} from a {@link Space}
   * 
   * @param pSpaceUri
   *          the parent space uri
   * @param pApplicationUri
   *          the application to add
   * @throws SpaceServiceException
   *           thrown if the parent is not a {@link Space}
   * @throws ApplicationServiceException
   *           thrown if the application is not either a {@link ProjectApplication} or
   *           {@link TemplateApplication}
   */
  void deleteApplication(String pSpaceUri, String pApplicationUri) throws SpaceServiceException,
      ApplicationServiceException;

  /**
   * Built a uri from parent and current node
   * 
   * @param pParent
   *          parent uri
   * @param pName
   *          current name
   * @return uri built
   */
  String buildUri(String pParent, String pName);

  /**
   * This method allows to move an application from an initial space to a target space
   * 
   * @param pFromSpaceUri
   *          the initial space
   * @param pToSpaceUri
   *          the target space
   * @param pApplicationUri
   *          the application uri
   * @throws ApplicationServiceException
   * @throws SpaceServiceException
   */
  void moveApplication(final String pFromSpaceUri, final String pToSpaceUri, final String pApplicationUri)
      throws ApplicationServiceException, SpaceServiceException;

}
