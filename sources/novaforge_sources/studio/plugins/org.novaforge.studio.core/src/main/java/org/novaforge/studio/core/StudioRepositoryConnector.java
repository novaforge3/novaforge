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


package org.novaforge.studio.core;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.Policy;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.TaskRepositoryLocationFactory;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;
import org.novaforge.studio.core.client.StudioClientFactory;
import org.novaforge.studio.core.client.StudioManagementClient;
import org.novaforge.studio.core.client.StudioProjectClient;
import org.novaforge.studio.core.project.ProjectDataHandler;
import org.novaforge.studio.core.task.StudioTaskDataHandler;

/**
 * Novastudio repository connector. 
 *
 */
public class StudioRepositoryConnector extends AbstractRepositoryConnector {

   private static final String NOVAFORGE_PROJECTS_REPOSITORY_LABEL = "NovaStudio Repository";

private final StudioDataHandler projectDataHandler = new ProjectDataHandler(this);

   private final StudioTaskDataHandler taskDataHandler = new StudioTaskDataHandler(this);

   private final TaskRepositoryLocationFactory taskRepositoryLocationFactory = new TaskRepositoryLocationFactory();

   private File repositoryConfigurationCacheFile;

   public StudioRepositoryConnector()
   {
      if (StudioCorePlugin.getContext() != null)
      {
         StudioCorePlugin.getDefault().setConnector(this);
         IPath path = StudioCorePlugin.getDefault().getRepostioryCachePath();
         this.setRepositoryConfigurationCacheFile(path.toFile());
      }
   }

   @Override
   public boolean canCreateNewTask(TaskRepository repository) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean canCreateTaskFromKey(TaskRepository repository) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public String getConnectorKind() {
      return StudioCorePlugin.CONNECTOR_KIND;
   }

   @Override
   public String getLabel() {
      return NOVAFORGE_PROJECTS_REPOSITORY_LABEL;
   }

   public synchronized StudioProjectClient getProjectClient(TaskRepository taskRepository)
   {
      return StudioClientFactory.getProjectClient(taskRepositoryLocationFactory
            .createWebLocation(taskRepository));
   }

   public synchronized StudioManagementClient getManagementClient(TaskRepository taskRepository)
   {
      return StudioClientFactory.getManagementClient(taskRepositoryLocationFactory
            .createWebLocation(taskRepository));
   }

   @Override
   public String getRepositoryUrlFromTaskUrl(String taskFullUrl) {
      // TODO Auto-generated method stub
      return null;
   }

   public File getRepositoryConfigurationCacheFile()
   {
      return repositoryConfigurationCacheFile;
   }

   public void setRepositoryConfigurationCacheFile(File repositoryConfigurationCacheFile)
   {
      this.repositoryConfigurationCacheFile = repositoryConfigurationCacheFile;
   }

   @Override
   public TaskData getTaskData(TaskRepository repository, String taskId, IProgressMonitor monitor) throws CoreException
   {
      TaskData taskData = null;
      monitor = Policy.monitorFor(monitor);
      try
      {
         monitor.beginTask("getting Task Data", IProgressMonitor.UNKNOWN);
         String repositoryTypeProperty = repository.getProperty(StudioCorePlugin.REPOSITORY_PROP_TYPE);
         StudioRepositoryType repositoryType = StudioRepositoryType.valueOf(repositoryTypeProperty);
         switch (repositoryType)
         {
         case PROJECT:
            taskData = projectDataHandler.getTaskData(repository, taskId);
            break;
         case TASK:
            taskData = taskDataHandler.getTaskData(repository, taskId);
            break;
         }
         return taskData;
      }
      catch (OperationCanceledException e)
      {
         throw e;
      }
      finally
      {
         monitor.done();
      }
   }

   @Override
   public String getTaskIdFromTaskUrl(String taskFullUrl) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getTaskUrl(String repositoryUrl, String taskId) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean hasTaskChanged(TaskRepository taskRepository, ITask task, TaskData taskData)
   {
      boolean changed = false;
      try
      {
         String repositoryTypeProperty = taskRepository.getProperty(StudioCorePlugin.REPOSITORY_PROP_TYPE);
         StudioRepositoryType repositoryType = StudioRepositoryType.valueOf(repositoryTypeProperty);
         switch (repositoryType)
         {
         case PROJECT:
            changed = projectDataHandler.hasTaskModificationDateChanged(taskData, task);
            break;
         case TASK:
            changed = taskDataHandler.hasTaskModificationDateChanged(taskData, task);
            break;
         }
      }
      catch (OperationCanceledException e)
      {
         throw e;
      }
      return changed;
   }


   @Override
   public IStatus performQuery(TaskRepository repository, IRepositoryQuery query, TaskDataCollector resultCollector,
         ISynchronizationSession session, IProgressMonitor monitor)
   {
      monitor.beginTask("Querying repository", IProgressMonitor.UNKNOWN);
      try
      {
         String repositoryTypeProperty = repository.getProperty(StudioCorePlugin.REPOSITORY_PROP_TYPE);
         StudioRepositoryType repositoryType = StudioRepositoryType.valueOf(repositoryTypeProperty);
         switch(repositoryType)
         {
         case PROJECT:
            getList(repository, query, projectDataHandler, resultCollector);
            break;
         case TASK:
            String queryTypeProperty = query.getAttribute(StudioQueryAttribute.QUERY_ATTR_TYPE);
            StudioQueryType queryType = StudioQueryType.valueOf(queryTypeProperty);
            switch(queryType)
            {
            case ITERATION:
               getIterationList(repository, query, taskDataHandler, resultCollector);
               break;
            case NONE:
               getList(repository, query, taskDataHandler, resultCollector);
               break;
            default:
               break;
            }
            break;
         default:
            break;
         }

         return Status.OK_STATUS;
      }
      catch (OperationCanceledException e)
      {
         throw e;
      }
      catch (Throwable e)
      {
         return StudioCorePlugin.toStatus(e, repository);

      } finally {
         monitor.done();
      }
   }

   private void getList(TaskRepository repository, IRepositoryQuery query,
         StudioDataHandler dataHandler, TaskDataCollector resultCollector) throws CoreException
         {
      List<TaskData> taskDataList = dataHandler.getTasks(repository, query);
      for (TaskData taskData : taskDataList)
      {
         resultCollector.accept(taskData);
      }
         }

   private void getIterationList(TaskRepository repository, IRepositoryQuery query,
         StudioDataHandler dataHandler, TaskDataCollector resultCollector) throws CoreException
         {
      List<TaskData> taskDataList = taskDataHandler.getTasksByIteration(repository, query);
      for (TaskData taskData : taskDataList)
      {
         resultCollector.accept(taskData);
      }
         }

   @Override
   public void updateRepositoryConfiguration(TaskRepository taskRepository, IProgressMonitor monitor) throws CoreException
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void updateTaskFromTaskData(TaskRepository taskRepository, ITask task, TaskData taskData)
   {
      TaskMapper mapper = new TaskMapper(taskData);
      mapper.applyTo(task);
      try
      {
         String repositoryTypeProperty = taskRepository.getProperty(StudioCorePlugin.REPOSITORY_PROP_TYPE);
         StudioRepositoryType repositoryType = StudioRepositoryType.valueOf(repositoryTypeProperty);
         switch (repositoryType)
         {
         case PROJECT:
            projectDataHandler.setTaskModificationDate(taskData, task);

            break;
         case TASK:
            taskDataHandler.setTaskModificationDate(taskData, task);
            break;
         }
      }
      catch (OperationCanceledException e)
      {
         throw e;
      }
   }

   public void modifyTask(TaskRepository taskRepository, TaskData taskData, String consumedTime, String remainingTime) throws CoreException
   {
      taskDataHandler.modifyTask(taskRepository, taskData, consumedTime, remainingTime);
   }

   public void closeTask(TaskRepository taskRepository, TaskData taskData) throws CoreException
   {
      taskDataHandler.closeTask(taskRepository, taskData);
   }

   public List<String> getIterationList(TaskRepository taskRepository, String pluginUUID, String instanceId) throws CoreException
   {
      return taskDataHandler.getIterationList(taskRepository, pluginUUID, instanceId);
   }
}
