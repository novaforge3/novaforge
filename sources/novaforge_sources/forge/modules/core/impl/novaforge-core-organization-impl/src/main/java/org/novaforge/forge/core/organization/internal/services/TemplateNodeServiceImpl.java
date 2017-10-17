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
package org.novaforge.forge.core.organization.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.dao.NodeDAO;
import org.novaforge.forge.core.organization.dao.TemplateDAO;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.Node;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.TemplateApplication;
import org.novaforge.forge.core.organization.model.enumerations.NodeType;
import org.novaforge.forge.core.organization.services.TemplateNodeService;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of {@link TemplateNodeService}
 * 
 * @author Guillaume Lamirand
 * @author sbenoist
 * @see TemplateNodeService
 */
public class TemplateNodeServiceImpl implements TemplateNodeService
{
  private static final Log LOGGER = LogFactory.getLog(TemplateNodeServiceImpl.class);
  /**
   * Reference to {@link NodeDAO} service injected by the container
   */
  private NodeDAO          nodeDAO;
  /**
   * Reference to {@link TemplateDAO} service injected by the container
   */
  private TemplateDAO      templateDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public TemplateApplication addApplication(final String pTemplateId, final String parentNodeUri,
      final String applicationLabel, final UUID pluginUUID, final Map<String, String> pRolesMapping)
      throws TemplateServiceException
  {
    TemplateApplication applicationNode = null;
    try
    {
      // Check if an application already exists with the same name
      final List<TemplateApplication> applications = getAllTemplateApplications(pTemplateId);
      for (final TemplateApplication application : applications)
      {
        if (application.getName().equals(applicationLabel))
        {
          throw new TemplateServiceException(ExceptionCode.ERR_CREATE_APP_NAME_ALREADY_EXIST, String.format(
              "The template id=%s already exists.", pTemplateId));
        }
      }

      // instanciate the application
      applicationNode = (TemplateApplication) nodeDAO.newNode(NodeType.TemplateApplication);
      applicationNode.setName(applicationLabel);
      applicationNode.setPluginUUID(pluginUUID);
      applicationNode.setRolesMapping(pRolesMapping);

      // create the application nodetry
      return (TemplateApplication) nodeDAO.addApplicationToSpace(
          parentNodeUri, applicationNode);
    }
    catch (final SpaceServiceException e)
    {
      throw new TemplateServiceException(String.format(
          "Unable to add application to the space given with [parent=%s, app=%s]", parentNodeUri,
          applicationNode), e);
    }
    catch (final Exception e)
    {
      if (e instanceof TemplateServiceException)
      {
        throw (TemplateServiceException) e;
      }
      else
      {
        throw new TemplateServiceException("a technical error occured", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteApplication(final String pTemplateId, final String parentNodeUri, final String applicationUri)
      throws TemplateServiceException
  {
    // remove the application node
    try
    {
      nodeDAO.deleteApplication(parentNodeUri, applicationUri);
    }
    catch (final SpaceServiceException e)
    {
      throw new TemplateServiceException(String
                                             .format("Unable to add application to the space given with [parent=%s, app=%s]",
                                                     parentNodeUri, applicationUri), e);
    }
    catch (final ApplicationServiceException e)
    {
      throw new TemplateServiceException(String
                                             .format("Unable to add application to the space given with [parent=%s, app=%s]",
                                                     parentNodeUri, applicationUri), e);
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TemplateApplication getApplication(final String pTemplateId, final String applicationUri)
      throws TemplateServiceException
  {
    Node node = null;
    try
    {
      node = nodeDAO.findByUri(applicationUri);
    }
    catch (final NoResultException e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }

    if (!(node instanceof TemplateApplication))
    {
      throw new TemplateServiceException("The node uri given do not match with a TemplateApplication");

    }
    return (TemplateApplication) node;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TemplateApplication> getAllTemplateApplications(final String pTemplateId)
      throws TemplateServiceException
  {

    try
    {
      final List<TemplateApplication> applications = new ArrayList<TemplateApplication>();

      final Map<Space, List<Application>> spaces = nodeDAO.findSpacesWithAppsForElementId(pTemplateId);
      final Set<Entry<Space, List<Application>>> entrySet = spaces.entrySet();
      for (final Entry<Space, List<Application>> space : entrySet)
      {
        for (final Application application : space.getValue())
        {
          if (application instanceof TemplateApplication)
          {
            applications.add((TemplateApplication) application);
          }
        }
      }
      return applications;
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TemplateApplication> getAllSpaceApplications(final String pSpaceNodeUri, final String pTemplateId)
      throws TemplateServiceException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Input : uri=%s, template_id=%s", pSpaceNodeUri, pTemplateId));
    }

    try
    {
      final List<Application> findAppsForSpace = nodeDAO.findAppsForSpace(pSpaceNodeUri);

      final List<TemplateApplication> applications = new ArrayList<TemplateApplication>();
      if (findAppsForSpace != null)
      {
        for (final Application application : findAppsForSpace)
        {
          if (application instanceof TemplateApplication)
          {
            applications.add((TemplateApplication) application);
          }
        }
      }
      return applications;
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws TemplateServiceException
   */
  @Override
  public List<Space> getAllSpaces(final String pTemplateId) throws TemplateServiceException
  {
    try
    {
      return nodeDAO.findByProjectElement(pTemplateId);
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Space getSpace(final String pTemplateId, final String pSpaceNodeUri) throws TemplateServiceException
  {
    Node node = null;
    try
    {
      node = nodeDAO.findByUri(pSpaceNodeUri);
    }
    catch (final NoResultException e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }

    if (!(node instanceof Space))
    {
      throw new TemplateServiceException("The node uri given do not match with a Space");

    }
    return (Space) node;
  }

  /**
   * {@inheritDoc}
   *
   * @throws TemplateServiceException
   */
  @Override
  public void updateSpace(final String pTemplateId, final Space space) throws TemplateServiceException
  {
    try
    {
      nodeDAO.update(space);
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteSpace(final String pTemplateId, final String pSpaceUri) throws TemplateServiceException
  {
    try
    {
      final Node spaceNode = nodeDAO.findByUri(pSpaceUri);
      if (!(spaceNode instanceof Space))
      {
        throw new TemplateServiceException(String.format(
            "The given uri doesn't referer a space element [uri=%s]", pSpaceUri));
      }
      final Template template = templateDAO.findByTemplateId(pTemplateId);

      // add the space to the project
      template.removeSpace((Space) spaceNode);

      // persist it
      templateDAO.update(template);
    }
    catch (final Exception e)
    {
      if (e instanceof TemplateServiceException)
      {
        throw (TemplateServiceException) e;
      }
      else
      {
        throw new TemplateServiceException("a technical error occured", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Space addSpace(final String pTemplateId, final Space pSpace) throws TemplateServiceException
  {
    try
    {
      pSpace.setUri(nodeDAO.buildUri(pTemplateId, pSpace.getName()));
      final Template template = templateDAO.findByTemplateId(pTemplateId);

      // add the space to the project
      template.addSpace(pSpace);

      // persist it
      templateDAO.update(template);
      return pSpace;
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Space newSpace()
  {
    return (Space) nodeDAO.newNode(NodeType.Space);
  }

  /**
   * Use by container to inject {@link OrganizatNodeDAOionDAO} implementation
   * 
   * @param pNodeDAO
   *          the nodeDAO to set
   */
  public void setNodeDAO(final NodeDAO pNodeDAO)
  {
    nodeDAO = pNodeDAO;
  }

  /**
   * Use by container to inject {@link TemplateDAO} implementation
   * 
   * @param pTemplateDAO
   *          the templateDAO to set
   */
  public void setTemplateDAO(final TemplateDAO pTemplateDAO)
  {
    templateDAO = pTemplateDAO;
  }

}
