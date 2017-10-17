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


package org.novaforge.studio.core.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.RepositoryResponse;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.novaforge.forge.remote.services.model.core.ForgeApplication;
import org.novaforge.forge.remote.services.model.core.ForgeNode;
import org.novaforge.forge.remote.services.model.core.ForgeProject;
import org.novaforge.studio.core.AbstractStudioDataHandler;
import org.novaforge.studio.core.StudioCorePlugin;
import org.novaforge.studio.core.StudioDataHandler;
import org.novaforge.studio.core.StudioRepositoryConnector;
import org.novaforge.studio.core.StudioTaskType;
import org.novaforge.studio.core.client.StudioProjectClient;

/**
 * Specific task data handler used by Novastudio to manage project items. 
 *
 */
public class ProjectDataHandler extends AbstractStudioDataHandler implements StudioDataHandler
{
   private final StudioRepositoryConnector connector;

   public ProjectDataHandler(StudioRepositoryConnector connector)
   {
      this.connector = connector;
   }

   @Override
   public TaskData getTaskData(TaskRepository repository, String taskId) throws CoreException
   {
      TaskData taskData = null;
      ForgeProject forgeProject;
      List<ForgeNode> forgeNodes;
      try
      {
         StudioProjectClient client = connector.getProjectClient(repository);
         forgeProject = client.getForgeProject(taskId);
         forgeNodes = client.getAllForgeProjectNodes(taskId);
      } catch (Exception e) {
         throw new CoreException(StudioCorePlugin.toStatus(e, repository));
      }
      taskData = createTaskDataFromForgeProject(repository, forgeProject, forgeNodes);
      return taskData;
   }

   @Override
   public List<TaskData> getTasks(TaskRepository repository, IRepositoryQuery query) throws CoreException
   {

      List<TaskData> tasks = new ArrayList<TaskData>();

      List<ForgeProject> forgeProjects = new ArrayList<ForgeProject>();
      try
      {
         StudioProjectClient client = connector.getProjectClient(repository);
         forgeProjects = client.getForgeProjects();
      }
      catch (Exception e)
      {
         throw new CoreException(StudioCorePlugin.toStatus(e, repository));
      }

      TaskAttributeMapper attributeMapper = getAttributeMapper(repository);
      for (ForgeProject forgeProject : forgeProjects)
      {
         TaskData taskData = new TaskData(attributeMapper, connector.getConnectorKind(), repository.getRepositoryUrl(), forgeProject.getId());
         taskData.setPartial(true);
         appendAttribute(taskData, taskData.getRoot(), TaskAttribute.TASK_KIND, StudioTaskType.PROJECT.value(), true);
         appendAttribute(taskData, taskData.getRoot(), TaskAttribute.TASK_KEY, forgeProject.getId(), true);
         appendAttribute(taskData, taskData.getRoot(), TaskAttribute.SUMMARY, forgeProject.getName(), true);
         // adding last modified date to the taskData (but not displayed by the task ui).
         appendProjectAttribute(taskData, taskData.getRoot(), TaskAttribute.DATE_MODIFICATION,
               forgeProject.getLastModified(), "dd/MM/yyyy HH:mm:ss", true);

         tasks.add(taskData);
      }

      return tasks;

   }
   
   /**
    * Converts a forge project bean into a Mylyn task data
    * @param repository
    * @param forgeProject
    * @param forgeNodes
    * @return
    */
   TaskData createTaskDataFromForgeProject(TaskRepository repository, ForgeProject forgeProject, List<ForgeNode> forgeNodes)
   {
      TaskAttributeMapper attributeMapper = getAttributeMapper(repository);
      TaskData taskData = new TaskData(attributeMapper, connector.getConnectorKind(), repository.getRepositoryUrl(), forgeProject.getId());
      taskData.setPartial(false);

      appendAttribute(taskData, taskData.getRoot(), ProjectAttributeMapper.NAME, forgeProject.getName(), true);
      appendAttribute(taskData, taskData.getRoot(), ProjectAttributeMapper.DESCRIPTION, forgeProject.getDescription(), true);
      // adding last modified date to the taskData (but not displayed by the task ui).
      appendProjectAttribute(taskData, taskData.getRoot(), TaskAttribute.DATE_MODIFICATION,
            forgeProject.getLastModified(), "dd/MM/yyyy HH:mm:ss", true);

      if (forgeNodes != null)
      {
         for (ForgeNode node : forgeNodes) // space
         {
            String spaceAttributeId = ProjectAttributeMapper.SPACE_PREFIX + node.getUri();
            TaskAttribute spaceAttribute = appendAttribute(taskData, taskData.getRoot(), spaceAttributeId, node.getName(), true);

            for (ForgeNode applicationNode : node.getChilds()) // application
            {
               String applicationAttributeId = ProjectAttributeMapper.APPLICATION_PREFIX + applicationNode.getUri();
               TaskAttribute applicationAttribute = appendAttribute(taskData, spaceAttribute, applicationAttributeId, applicationNode.getName(), true);

               if (applicationNode.getApplication() != null) // application access information
               {
                  ForgeApplication application = applicationNode.getApplication();
                  appendAttribute(taskData, applicationAttribute, ProjectAttributeMapper.APPLICATION_ACCESS_INFO, application.getAccessInfo(), true);
                  appendAttribute(taskData, applicationAttribute, ProjectAttributeMapper.APPLICATION_CATEGORY, application.getCategory(), true);
                  appendAttribute(taskData, applicationAttribute, ProjectAttributeMapper.APPLICATION_TYPE, application.getType(), true);
                  appendAttribute(taskData, applicationAttribute, ProjectAttributeMapper.APPLICATION_PLUGIN_UUID, application.getPluginUUID(), true);
                  appendAttribute(taskData, applicationAttribute, ProjectAttributeMapper.APPLICATION_PLUGIN_INSTANCE_ID, application.getInstanceId(), true);
                  if (application.getToolUrl()!=null){
                	  appendAttribute(taskData, applicationAttribute, ProjectAttributeMapper.APPLICATION_TOOL_URL, application.getToolUrl(), true);
                  }
               }
            }
         }
      }
      return taskData;
   }

   @Override
   public void setTaskModificationDate(TaskData taskData, ITask task)
   {
      ProjectAttributeMapper projectAttributMapper = (ProjectAttributeMapper) taskData.getAttributeMapper();
      // setting the modification date
      Date modified_date = projectAttributMapper.getDateValue(
            taskData.getRoot().getAttribute(TaskAttribute.DATE_MODIFICATION), "dd/MM/yyyy HH:mm:ss");
      task.setModificationDate(modified_date);
   }

   @Override
   public boolean hasTaskModificationDateChanged(TaskData taskData, ITask task)
   {
      boolean changed = false;
      ProjectAttributeMapper projectAttributMapper = (ProjectAttributeMapper) taskData.getAttributeMapper();
      Date taskDataModifiedDate = projectAttributMapper.getDateValue(
            taskData.getRoot().getAttribute(TaskAttribute.DATE_MODIFICATION), "dd/MM/yyyy HH:mm:ss");
      changed |= hasChanges(task.getModificationDate(), taskDataModifiedDate);
      return changed;
   }

   @Override
   public TaskAttributeMapper getAttributeMapper(TaskRepository taskRepository) {
      return new ProjectAttributeMapper(taskRepository);
   }

   @Override
   public boolean initializeTaskData(TaskRepository arg0, TaskData arg1,
         ITaskMapping arg2, IProgressMonitor arg3) throws CoreException {
      // Not needing attribute mapper for our case at now
      return false;
   }

   @Override
   public RepositoryResponse postTaskData(TaskRepository arg0, TaskData arg1,
         Set<TaskAttribute> arg2, IProgressMonitor arg3)
               throws CoreException {
      // TODO Auto-generated method stub
      return null;
   }



}
