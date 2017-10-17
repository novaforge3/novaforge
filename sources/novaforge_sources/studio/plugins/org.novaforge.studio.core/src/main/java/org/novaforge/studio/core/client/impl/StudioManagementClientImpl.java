/**
 * NovaForge(TM) is a web-based forge offering a Collaborative Development and 
 * Project Management Environment.
 *
 * Copyright (C) 2007-2012  BULL SAS
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.studio.core.client.impl;

import java.util.Set;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.cxf.binding.soap.SoapFault;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.novaforge.forge.remote.services.exception.ExceptionCode;
import org.novaforge.forge.remote.services.exception.RemoteServiceException;
import org.novaforge.forge.remote.services.management.RemoteManagementService;
import org.novaforge.forge.remote.services.model.management.Iteration;
import org.novaforge.forge.remote.services.model.management.Task;
import org.novaforge.forge.remote.services.model.management.TaskInfo;
import org.novaforge.forge.remote.services.model.management.TaskStatus;
import org.novaforge.forge.remote.services.model.management.TaskType;
import org.novaforge.studio.core.client.StudioManagementClient;

public class StudioManagementClientImpl extends AbstractStudioClient implements StudioManagementClient
{

   private RemoteManagementService remoteManagementService;

   public StudioManagementClientImpl(AbstractWebLocation location)
   {
      remoteManagementService = initRemoteService(location, RemoteManagementService.class, location.getUrl());
   }

   @Override
   public boolean isAuthenticated() throws RemoteServiceException
   {
      boolean isAuthenticated = false;
      try
      {
            isAuthenticated = remoteManagementService.isAuthenticated();
         }
      catch (SoapFault f)
      {
         if ((Integer.toString(HttpStatus.SC_UNAUTHORIZED).equals(f.getMessage())))
         {
            throw new RemoteServiceException(ExceptionCode.ERR_GET_FORGE_PROJECT_INFO, f.getMessage());
         }
         else
         {
            // TODO : introduce new error code !
            throw new RemoteServiceException(ExceptionCode.ERR_GET_FORGE_PROJECT_INFO, f.getMessage());
         }
      }
      return isAuthenticated;
   }

   @Override
   public void closeTask(String pluginUUID, String instanceId, String taskId) throws RemoteServiceException
   {
      remoteManagementService.closeTask(pluginUUID, instanceId, taskId);
   }

   @Override
   public Set<Iteration> getIterationList(String pluginUUID, String instanceId) throws RemoteServiceException
   {
      return remoteManagementService.getIterationList(pluginUUID, instanceId);
   }

   @Override
   public Task getTask(String pluginUUID, String instanceId, String taskId) throws RemoteServiceException
   {
      return remoteManagementService.getTask(pluginUUID, instanceId, taskId);
   }

   @Override
   public Set<TaskInfo> getTaskList(String pluginUUID, String instanceId) throws RemoteServiceException
   {
      return remoteManagementService.getTaskList(pluginUUID, instanceId);
   }

   @Override
   public Set<TaskInfo> getTaskListByIteration(String pluginUUID, String instanceId, String iterationId)
         throws RemoteServiceException
   {
      return remoteManagementService.getTaskListByIteration(pluginUUID, instanceId, iterationId);
   }

   @Override
   public Set<TaskInfo> getTaskListByStatus(String pluginUUID, String instanceId, TaskStatus taskStatus)
         throws RemoteServiceException
   {
      return remoteManagementService.getTaskListByStatus(pluginUUID, instanceId, taskStatus);
   }

   @Override
   public Set<TaskInfo> getTaskListByType(String pluginUUID, String instanceId, TaskType taskType)
         throws RemoteServiceException
   {
      return remoteManagementService.getTaskListByType(pluginUUID, instanceId, taskType);
   }

   @Override
   public Set<TaskInfo> getTaskListOfCurrentIteration(String pluginUUID, String instanceId)
         throws RemoteServiceException
   {
      return remoteManagementService.getTaskListOfCurrentIteration(pluginUUID, instanceId);
   }

   @Override
   public void modifyTask(String pluginUUID, String instanceId, Task task) throws RemoteServiceException
   {
      remoteManagementService.modifyTask(pluginUUID, instanceId, task);
   }

   public RemoteManagementService getRemoteManagementService()
   {
      return remoteManagementService;
   }

   public void setRemoteManagementService(RemoteManagementService remoteManagementService)
   {
      this.remoteManagementService = remoteManagementService;
   }

}
