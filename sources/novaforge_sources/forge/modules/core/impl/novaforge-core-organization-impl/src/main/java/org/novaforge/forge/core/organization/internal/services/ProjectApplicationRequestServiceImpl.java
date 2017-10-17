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

import org.novaforge.forge.core.organization.dao.NodeDAO;
import org.novaforge.forge.core.organization.dao.ProjectApplicationRequestDAO;
import org.novaforge.forge.core.organization.dao.ProjectDAO;
import org.novaforge.forge.core.organization.delegates.MailDelegate;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.handlers.ApplicationHandler;
import org.novaforge.forge.core.organization.handlers.PICApplicationHandler;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectApplicationRequest;
import org.novaforge.forge.core.organization.model.comparator.ProjectApplicationRequestCreatedComparator;
import org.novaforge.forge.core.organization.services.ProjectApplicationRequestService;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of {@link ProjectApplicationRequestService}
 * 
 * @author Guillaume Lamirand
 * @see ProjectApplicationRequestService
 */
public class ProjectApplicationRequestServiceImpl implements ProjectApplicationRequestService
{

  /**
   * Reference to {@link ProjectApplicationRequestDAO} service injected by the container
   */
  private ProjectApplicationRequestDAO projectApplicationRequestDAO;

  /**
   * Reference to {@link ProjectDAO} service injected by the container
   */
  private ProjectDAO                   projectDAO;

  /**
   * Reference to {@link NodeDAO} service injected by the container
   */
  private NodeDAO                      nodeDAO;

  /**
   * Reference to {@link ApplicationHandler} service injected by the container
   */
  private ApplicationHandler           applicationHandler;

  /**
   * Reference to {@link PICApplicationHandler} service injected by the container
   */
  private PICApplicationHandler        picApplicationHandler;

  /**
   * Reference to {@link MailDelegate} service injected by the container
   */
  private MailDelegate                 mailDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  public void addRequest(final String pLogin, final String pProjectId, final UUID pInstanceId,
      final Map<String, String> pRoleMapping) throws ApplicationServiceException
  {
    try
    {
      final ProjectApplicationRequest request = projectApplicationRequestDAO.newProjectApplicationRequest();

      // rattach the entity to the persistence context
      final Project project = projectDAO.findByProjectId(pProjectId);

      // rattach the entity to the persistence context
      final ProjectApplication app = nodeDAO.findByInstanceId(pInstanceId);

      // add the project to the request
      request.setProject(project);

      // add the app to the request
      request.setApplication(app);

      // Set role mapping
      request.setRolesMapping(pRoleMapping);

      // Set the login
      request.setLogin(pLogin);

      projectApplicationRequestDAO.persist(request);

      // send notification
      Map<String, String> infos = applicationHandler.getPluginInfos(app.getPluginUUID());
      String category = infos.get(ApplicationHandler.PLUGIN_CATEGORY);
      String type = infos.get(ApplicationHandler.PLUGIN_TYPE);
      String version = infos.get(ApplicationHandler.PLUGIN_VERSION);
      mailDelegate.sendProjectApplicationRequest(pProjectId, pLogin, category, type, version);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteRequest(final String pProjectId, final UUID pInstanceId)
      throws ApplicationServiceException
  {
    try
    {
      final ProjectApplicationRequest findByProjectAndApp = projectApplicationRequestDAO.findByProjectAndApp(
          pProjectId, pInstanceId);
      projectApplicationRequestDAO.delete(findByProjectAndApp);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectApplicationRequest> getByPluginUUID(final UUID pPluginUUID)
      throws ApplicationServiceException
  {
    try
    {
      return projectApplicationRequestDAO.findByPlugin(pPluginUUID);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRequest(final String pProjectId, final UUID pPluginUUID)
      throws ApplicationServiceException
  {
    try
    {
      return projectApplicationRequestDAO.existRequest(pProjectId, pPluginUUID);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  @Override
  public void handleRequest(final ProjectApplicationRequest pProjectApplicationRequest,
      final UUID pToolInstanceUUID) throws ApplicationServiceException
  {
    try
    {
      // check if the other PIC toolInstanceUUID is linked to another project application
      checkRequestHandling(pProjectApplicationRequest.getApplication().getPluginUUID(), pToolInstanceUUID,
          pProjectApplicationRequest.getProject().getProjectId());

      handleAndDeleteRequest(pProjectApplicationRequest, pToolInstanceUUID);

      handlePICRequests(pProjectApplicationRequest.getApplication().getPluginUUID(),
          pProjectApplicationRequest.getProject().getProjectId(), pToolInstanceUUID);
    }
    catch (final Exception e)
    {
      if (e instanceof ApplicationServiceException)
      {
        throw (ApplicationServiceException) e;
      }
      else
      {
        throw new ApplicationServiceException("a technical error occured", e);
      }
    }
  }

  private void checkRequestHandling(final UUID pPluginUUID, final UUID pToolInstanceUUID,
      final String pProjectId) throws ApplicationServiceException
  {
    if (picApplicationHandler.isPICType(pPluginUUID) && (!picApplicationHandler
                                                              .canHostPICApplicationForProject(pPluginUUID,
                                                                                               pToolInstanceUUID,
                                                                                               pProjectId)))
    {
      throw new ApplicationServiceException(
          "unable to link the request to the tool instance because the other PIC tool instance already host applications from another project");
    }
  }

  private void handleAndDeleteRequest(final ProjectApplicationRequest pProjectApplicationRequest,
                                      final UUID pToolInstanceUUID) throws ApplicationServiceException
  {

    final String pluginUUID = pProjectApplicationRequest.getApplication().getPluginUUID().toString();
    final String instanceUUID = pProjectApplicationRequest.getApplication().getPluginInstanceUUID().toString();

    // send the application message to the plugin
    applicationHandler.sendApplicationMessage(pluginUUID, instanceUUID,
                                              pProjectApplicationRequest.getApplication().getName(),
                                              pProjectApplicationRequest.getProject().getProjectId(),
                                              pProjectApplicationRequest.getRolesMapping(),
                                              pToolInstanceUUID.toString(), PluginQueueAction.CREATE.getLabel(),
                                              pProjectApplicationRequest.getLogin());

    // delete the request
    deleteRequest(pProjectApplicationRequest.getProject().getProjectId(),
                  pProjectApplicationRequest.getApplication().getPluginInstanceUUID());
  }

  // if there is another PIC Request for the project, we need to handle it automatically
  private void handlePICRequests(final UUID pPluginUUID, final String pProjectId, final UUID pToolInstanceUUID)
      throws ApplicationServiceException
  {
    if (picApplicationHandler.isPICType(pPluginUUID))
    {
      final List<ProjectApplicationRequest> requests = projectApplicationRequestDAO.findByProject(pProjectId);

      UUID pluginUUID = null;
      for (final ProjectApplicationRequest request : requests)
      {
        pluginUUID = request.getApplication().getPluginUUID();
        if (picApplicationHandler.isPICType(pluginUUID))
        {
          // get the tool instance id linked to this application and if exists, assign the request to the
          // demand
          final UUID toolInstanceUUID = picApplicationHandler.getOtherLinkedPICToolInstanceUUID(pPluginUUID,
                                                                                                pToolInstanceUUID,
                                                                                                pluginUUID);
          if (toolInstanceUUID != null)
          {
            handleAndDeleteRequest(request, toolInstanceUUID);
          }
        }
      }
    }
  }

  @Override
  public void handleOldestRequest(final UUID pToolInstanceUUID, final UUID pPluginUUID)
      throws ApplicationServiceException
  {
    try
    {
      if (picApplicationHandler.isPICType(pPluginUUID))
      {
        handlePICOldestRequest(pToolInstanceUUID, pPluginUUID);
      }
      else
      {
        handleStandardOldestRequest(pToolInstanceUUID, pPluginUUID);
      }
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  private void handleStandardOldestRequest(final UUID pToolInstanceUUID, final UUID pPluginUUID)
      throws ApplicationServiceException
  {
    final ProjectApplicationRequest oldestReq = projectApplicationRequestDAO.findOldestByPlugin(pPluginUUID);
    if (oldestReq != null)
    {
      handleAndDeleteRequest(oldestReq, pToolInstanceUUID);
    }
  }

  private void handlePICOldestRequest(final UUID pToolInstanceUUID, final UUID pPluginUUID)
      throws ApplicationServiceException
  {
    final List<ProjectApplicationRequest> requests = projectApplicationRequestDAO.findByPlugin(pPluginUUID);

    if ((requests != null) && (!requests.isEmpty()))
    {
      // copy the read-only result list in order to sort it
      final List<ProjectApplicationRequest> workingRequests = new ArrayList<ProjectApplicationRequest>(
          requests);

      // order the list by date creation
      Collections.sort(workingRequests, new ProjectApplicationRequestCreatedComparator());
      for (final ProjectApplicationRequest request : workingRequests)
      {
        if (picApplicationHandler.canHostPICApplicationForProject(pPluginUUID, pToolInstanceUUID, request
            .getProject().getProjectId()))
        {
          handleAndDeleteRequest(request, pToolInstanceUUID);
          handlePICRequests(request.getApplication().getPluginUUID(), request.getProject().getProjectId(),
              pToolInstanceUUID);

          break;
        }
      }
    }
  }

  /**
   * Use by container to inject {@link ProjectApplicationRequestDAO} implementation
   * 
   * @param pProjectApplicationRequestDAO
   *          the projectApplicationRequestDAO to set
   */
  public void setProjectApplicationRequestDAO(final ProjectApplicationRequestDAO pProjectApplicationRequestDAO)
  {
    projectApplicationRequestDAO = pProjectApplicationRequestDAO;
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
   * Use by container to inject {@link NodeDAO} implementation
   * 
   * @param pNodeDAO
   *          the nodeDAO to set
   */
  public void setNodeDAO(final NodeDAO pNodeDAO)
  {
    nodeDAO = pNodeDAO;
  }

  /**
   * Use by container to inject {@link ApplicationHandler} implementation
   * 
   * @param pApplicationHandler
   *          the applicationHandler to set
   */
  public void setApplicationHandler(final ApplicationHandler pApplicationHandler)
  {
    applicationHandler = pApplicationHandler;
  }

  /**
   * Use by container to inject {@link PICApplicationHandler} implementation
   * 
   * @param pPICApplicationHandler
   *          the PICApplicationHandler to set
   */
  public void setPicApplicationHandler(final PICApplicationHandler pPICApplicationHandler)
  {
    picApplicationHandler = pPICApplicationHandler;
  }

  public void setMailDelegate(final MailDelegate pMailDelegate)
  {
    mailDelegate = pMailDelegate;
  }

}
