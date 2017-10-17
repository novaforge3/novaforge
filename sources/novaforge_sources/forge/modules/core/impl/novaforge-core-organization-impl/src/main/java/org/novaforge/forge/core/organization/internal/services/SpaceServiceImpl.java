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
/**
 * 
 */
package org.novaforge.forge.core.organization.internal.services;

import org.novaforge.forge.core.organization.dao.NodeDAO;
import org.novaforge.forge.core.organization.dao.ProjectDAO;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.enumerations.NodeType;
import org.novaforge.forge.core.organization.services.SpaceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Implementation of {@link SpaceService}
 * 
 * @author Guillaume Lamirand
 * @author sbenoist
 * @see SpaceService
 */
public class SpaceServiceImpl implements SpaceService
{
  /**
   * Reference to {@link NodeDAO} service injected by the container
   */
  private NodeDAO    nodeDAO;
  /**
   * Reference to {@link ProjectDAO} service injected by the container
   */
  private ProjectDAO projectDAO;

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
   * Use by container to inject {@link ProjectDAO} implementation
   *
   * @param pProjectDAO
   *          the projectDAO to set
   */
  public void setProjectDAO(final ProjectDAO pProjectDAO)
  {
    projectDAO = pProjectDAO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Space> getAllSpaces(final String pProjectId) throws SpaceServiceException
  {
    try
    {
      return nodeDAO.findByProjectElement(pProjectId);
    }
    catch (final Exception e)
    {
      throw new SpaceServiceException("a technical error occured", e);
    }
  }



  /**
   * {@inheritDoc}
   * 
   * @throws SpaceServiceException
   */
  @Override
  public Map<Space, List<ProjectApplication>> getAllSpacesWithApplications(final String pProjectId)
      throws SpaceServiceException
  {
    try
    {
      final Map<Space, List<ProjectApplication>> map = new HashMap<Space, List<ProjectApplication>>();

      final Map<Space, List<Application>> findByProject = nodeDAO.findSpacesWithAppsForElementId(pProjectId);
      final Set<Entry<Space, List<Application>>> entrySet = findByProject.entrySet();
      for (final Entry<Space, List<Application>> entry : entrySet)
      {
        final List<ProjectApplication> applications = new ArrayList<ProjectApplication>();
        for (final Application application : entry.getValue())
        {
          applications.add((ProjectApplication) application);
        }
        map.put(entry.getKey(), applications);
      }

      return map;
    }
    catch (final Exception e)
    {
      throw new SpaceServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Space getSpace(final String pProjectId, final String pSpaceNodeUri) throws SpaceServiceException
  {
    try
    {
      return (Space) nodeDAO.findByUri(pSpaceNodeUri);
    }
    catch (final Exception e)
    {
      throw new SpaceServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Space updateSpace(final String pProjectId, final String pOldName, final Space pSpace)
      throws SpaceServiceException
  {
    // FIXME do not check the unicity constraint by this way because between the
    // moment the check is done
    // and the moment you commit the transaction, the unicity can be violated

    try
    {
      checkSpace(pProjectId, pOldName, pSpace);
      return nodeDAO.update(pSpace);
    }
    catch (final Exception e)
    {
      if (e instanceof SpaceServiceException)
      {
        throw (SpaceServiceException) e;
      }
      else
      {
        throw new SpaceServiceException("a technical error occured", e);
      }
    }
  }

  private void checkSpace(final String pProjectId, final String pOldName, final Space pSpace)
      throws SpaceServiceException
  {
    // check the space name doesn't already exists for the project
    if (!pOldName.equals(pSpace.getName()))
    {
      // Check if a space already exists with the same name
      final List<Space> spaces = getAllSpaces(pProjectId);

      if (spaces != null)
      {
        for (final Space space : spaces)
        {
          if (space.getName().equals(pSpace.getName()))
          {
            throw new SpaceServiceException(ExceptionCode.ERR_CREATE_SPACE_NAME_ALREADY_EXIST, String.format(
                "projectId=%s, space_name=%s", pProjectId, pSpace.getName()));
          }
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteSpace(final String pProjectId, final String pSpaceNodeUri) throws SpaceServiceException
  {
    try
    {
      final Space space = (Space) nodeDAO.findByUri(pSpaceNodeUri);

      // Delete all the applications of the space
      final List<Application> apps = nodeDAO.findAppsForSpace(pSpaceNodeUri);
      for (final Application app : apps)
      {
        nodeDAO.deleteApplication(app.getUri(), space.getUri());
      }

      final Project project = projectDAO.findByProjectId(pProjectId);
      project.removeSpace(space);
      projectDAO.update(project);
    }
    catch (final Exception e)
    {
      throw new SpaceServiceException("a technical error occured", e);
    }
  }

  private String buildUri(final String pParent, final String pName)
  {
    return pParent + "/" + pName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Space addSpace(final String pProjectId, final Space pSpace) throws SpaceServiceException
  {
    try
    {
      checkSpace(pProjectId, "", pSpace);
      Space returned = null;

      String uri = pSpace.getUri();
      if ((uri == null) || ("".equals(uri)))
      {
        uri = buildUri(pProjectId, pSpace.getName());
        pSpace.setUri(uri);
      }

      final Project project = projectDAO.findByProjectId(pProjectId);
      project.addSpace(pSpace);
      final Project projectUp = projectDAO.update(project);

      final List<Space> spaces = projectUp.getSpaces();
      for (final Space space : spaces)
      {
        if (space.getUri().equals(uri))
        {
          returned = space;
          break;
        }
      }

      return returned;
    }
    catch (final Exception e)
    {
      if (e instanceof SpaceServiceException)
      {
        throw (SpaceServiceException) e;
      }
      else
      {
        throw new SpaceServiceException("a technical error occured", e);
      }
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

}
