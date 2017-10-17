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
package org.novaforge.forge.remote.services.internal.service;

import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.core.plugins.categories.management.IterationBean;
import org.novaforge.forge.core.plugins.categories.management.ManagementCategoryService;
import org.novaforge.forge.core.plugins.categories.management.TaskBean;
import org.novaforge.forge.core.plugins.categories.management.TaskInfoBean;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.remote.services.exception.ExceptionCode;
import org.novaforge.forge.remote.services.exception.RemoteServiceException;
import org.novaforge.forge.remote.services.management.RemoteManagementService;
import org.novaforge.forge.remote.services.model.management.Iteration;
import org.novaforge.forge.remote.services.model.management.Task;
import org.novaforge.forge.remote.services.model.management.TaskInfo;
import org.novaforge.forge.remote.services.model.management.TaskStatus;
import org.novaforge.forge.remote.services.model.management.TaskType;

import java.util.Set;
import java.util.UUID;

/**
 * Concrete implementation of the RemoteManagementService.
 * 
 * @author rols-p
 */
@org.apache.cxf.interceptor.InInterceptors(
    interceptors = { "org.novaforge.forge.core.security.cxf.BasicAuthAuthorizationInterceptor" })
public class RemoteManagementServiceImpl extends AbstractRemoteService implements RemoteManagementService
{

  /**
   * PluginsManager injected by the container
   */
  private PluginsManager             pluginsManager;

  /**
   * AuthentificationService injected by the container
   */
  private AuthentificationService    authentificationService;

  /**
   * ForgeIdentificationManager injected by the container
   */
  private ForgeIdentificationService forgeIdentificationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public Task getTask(final String pPluginUUID, final String pInstanceId, final String pTaskId)
      throws RemoteServiceException
  {
    TaskBean taskBean = null;
    authentificationService.getCurrentUser();
    try
    {
      taskBean = getManagementCategoryService(pPluginUUID).getTask(getCurrentUser(), pInstanceId, getForgeId(),
                                                                   pTaskId);
    }
    catch (final Exception e)
    {
      throw new RemoteServiceException(ExceptionCode.ERR_MANAGEMENT_TASK, e);
    }
    return ManagementResourceBuilder.buildTask(taskBean);
  }

  /**
   * Returns the ManagementCategoryService for this plugin.
   *
   * @param pPluginUUID
   *          UUID of the plugin
   * @return a not null instance of ManagementCategoryService
   * @throws RemoteServiceException
   *           if not able to get the Service.
   */
  private ManagementCategoryService getManagementCategoryService(final String pPluginUUID)
      throws RemoteServiceException
  {
    ManagementCategoryService svc = null;
    try
    {
      svc = pluginsManager.getPluginCategoryService(pPluginUUID, ManagementCategoryService.class);
    }
    catch (final PluginManagerException e)
    {
      throw new RemoteServiceException(ExceptionCode.ERR_GET_MANAGEMENT_SERVICE, e);
    }
    if (svc == null)
    {
      throw new RemoteServiceException(ExceptionCode.ERR_GET_MANAGEMENT_SERVICE, String.format(
          "No ManagementCategoryService retrieved for the application instanceId=%s", pPluginUUID));
    }
    return svc;

  }

  /**
   * Returns the login of the current user.
   *
   * @return the login
   */
  private String getCurrentUser()
  {
    return authentificationService.getCurrentUser();
  }

  /**
   * Returns the Id of the Forge
   *
   * @return the Id of the Forge
   * @throws RemoteServiceException
   *           if unable to get the forge Id
   */
  private String getForgeId() throws RemoteServiceException
  {
    String forgeId = null;
    final UUID forgeUUID = forgeIdentificationService.getForgeId();
    if (forgeUUID != null)
    {
      forgeId = forgeUUID.toString();
    }
    return forgeId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<TaskInfo> getTaskList(final String pPluginUUID, final String pInstanceId)
      throws RemoteServiceException
  {
    Set<TaskInfoBean> taskBeans = null;
    authentificationService.getCurrentUser();
    try
    {
      taskBeans = getManagementCategoryService(pPluginUUID).getTaskList(getCurrentUser(), pInstanceId,
          getForgeId());
    }
    catch (final Exception e)
    {
      throw new RemoteServiceException(ExceptionCode.ERR_LIST_MANAGEMENT_TASKS, e);
    }
    return ManagementResourceBuilder.buildTaskInfos(taskBeans);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<TaskInfo> getTaskListOfCurrentIteration(final String pPluginUUID, final String pInstanceId)
      throws RemoteServiceException
  {
    Set<TaskInfoBean> taskBeans = null;
    try
    {
      taskBeans = getManagementCategoryService(pPluginUUID).getTaskListOfCurrentIteration(getCurrentUser(),
          pInstanceId, getForgeId());
    }
    catch (final Exception e)
    {
      throw new RemoteServiceException(ExceptionCode.ERR_LIST_MANAGEMENT_TASKS, e);
    }
    return ManagementResourceBuilder.buildTaskInfos(taskBeans);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Iteration> getIterationList(final String pPluginUUID, final String pInstanceId)
      throws RemoteServiceException
  {
    Set<IterationBean> iterationBeans = null;
    try
    {
      iterationBeans = getManagementCategoryService(pPluginUUID).getIterationList(getCurrentUser(),
          pInstanceId, getForgeId());
    }
    catch (final Exception e)
    {
      throw new RemoteServiceException(ExceptionCode.ERR_LIST_MANAGEMENT_ITERATIONS, e);
    }
    return ManagementResourceBuilder.buildIterations(iterationBeans);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<TaskInfo> getTaskListByIteration(final String pPluginUUID, final String pInstanceId,
      final String pIterationId) throws RemoteServiceException
  {
    Set<TaskInfoBean> taskBeans = null;
    try
    {
      taskBeans = getManagementCategoryService(pPluginUUID).getTaskListByIteration(getCurrentUser(),
          pInstanceId, getForgeId(), pIterationId);
    }
    catch (final Exception e)
    {
      throw new RemoteServiceException(ExceptionCode.ERR_LIST_MANAGEMENT_TASKS, e);
    }
    return ManagementResourceBuilder.buildTaskInfos(taskBeans);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<TaskInfo> getTaskListByType(final String pPluginUUID, final String pInstanceId,
      final TaskType pTaskType) throws RemoteServiceException
  {
    Set<TaskInfoBean> taskBeans = null;
    try
    {
      taskBeans = getManagementCategoryService(pPluginUUID).getTaskListByType(getCurrentUser(), pInstanceId,
          getForgeId(), ManagementResourceBuilder.buildTaskTypeBean(pTaskType));
    }
    catch (final Exception e)
    {
      throw new RemoteServiceException(ExceptionCode.ERR_LIST_MANAGEMENT_TASKS, e);
    }
    return ManagementResourceBuilder.buildTaskInfos(taskBeans);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<TaskInfo> getTaskListByStatus(final String pPluginUUID, final String pInstanceId,
      final TaskStatus pTaskStatus) throws RemoteServiceException
  {
    Set<TaskInfoBean> taskBeans = null;
    try
    {
      taskBeans = getManagementCategoryService(pPluginUUID).getTaskListByStatus(getCurrentUser(),
          pInstanceId, getForgeId(), ManagementResourceBuilder.buildTaskStatusBean(pTaskStatus));
    }
    catch (final Exception e)
    {
      throw new RemoteServiceException(ExceptionCode.ERR_LIST_MANAGEMENT_TASKS, e);
    }
    return ManagementResourceBuilder.buildTaskInfos(taskBeans);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void modifyTask(final String pPluginUUID, final String pInstanceId, final Task pTaskToModify)
      throws RemoteServiceException
  {
    try
    {
      getManagementCategoryService(pPluginUUID).modifyTask(getCurrentUser(), pInstanceId, getForgeId(),
          ManagementResourceBuilder.buildTaskBean(pTaskToModify));
    }
    catch (final Exception e)
    {
      throw new RemoteServiceException(ExceptionCode.ERR_UPDATE_MANAGEMENT_TASK, e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void closeTask(final String pPluginUUID, final String pInstanceId, final String pTaskId)
      throws RemoteServiceException
  {
    try
    {
      getManagementCategoryService(pPluginUUID).closeTask(getCurrentUser(), pInstanceId, getForgeId(),
          pTaskId);
    }
    catch (final Exception e)
    {
      throw new RemoteServiceException(ExceptionCode.ERR_UPDATE_MANAGEMENT_TASK, e);
    }

  }

  /**
   * @param pPluginsManager
   *          the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  /**
   * @param pAuthentificationService
   *          the authentificationService to set
   */
  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  /**
   * @param pForgeIdentificationService
   *          the forgeIdentificationService to set
   */
  public void setForgeIdentificationService(final ForgeIdentificationService pForgeIdentificationService)
  {
    forgeIdentificationService = pForgeIdentificationService;
  }

}
