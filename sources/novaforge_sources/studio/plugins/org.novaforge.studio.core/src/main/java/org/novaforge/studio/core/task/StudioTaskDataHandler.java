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


package org.novaforge.studio.core.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
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
import org.novaforge.forge.remote.services.model.management.BugTrackerIssue;
import org.novaforge.forge.remote.services.model.management.Iteration;
import org.novaforge.forge.remote.services.model.management.Task;
import org.novaforge.forge.remote.services.model.management.TaskInfo;
import org.novaforge.forge.remote.services.model.management.TaskStatus;
import org.novaforge.forge.remote.services.model.management.TaskType;
import org.novaforge.studio.core.AbstractStudioDataHandler;
import org.novaforge.studio.core.StudioCorePlugin;
import org.novaforge.studio.core.StudioDataHandler;
import org.novaforge.studio.core.StudioQueryAttribute;
import org.novaforge.studio.core.StudioRepositoryConnector;
import org.novaforge.studio.core.StudioTaskType;
import org.novaforge.studio.core.client.StudioManagementClient;

/**
 * Specific task data handler used by Novastudio to manage task items. 
 *
 */
public class StudioTaskDataHandler extends AbstractStudioDataHandler implements StudioDataHandler
{
   private final StudioRepositoryConnector connector;

   public StudioTaskDataHandler(StudioRepositoryConnector connector)
   {
      this.connector = connector;
   }

   @Override
   public TaskData getTaskData(TaskRepository repository, String taskId) throws CoreException
   {
      TaskData taskData = null;
      Task forgeTask = null;
      try
      {
         StudioManagementClient client = connector.getManagementClient(repository);
         forgeTask = client.getTask(getPluginUUIDfromId(taskId), getInstanceIdFromId(taskId), getTaskIdFromId(taskId));
      }
      catch (Exception e)
      {
         throw new CoreException(StudioCorePlugin.toStatus(e, repository));
      }

      taskData = createTaskDataFromForgeTask(repository, forgeTask, getPluginUUIDfromId(taskId), getInstanceIdFromId(taskId));
      return taskData;
   }

   @Override
   public List<TaskData> getTasks(TaskRepository repository, IRepositoryQuery query) throws CoreException
   {
      String pluginUUID = query.getAttribute(StudioQueryAttribute.QUERY_ATTR_PLUGIN_UUID);
      String instanceId = query.getAttribute(StudioQueryAttribute.QUERY_ATTR_PLUGIN_INSTANCE_ID);
      Set<TaskInfo> taskInfoList = new HashSet<TaskInfo>();
      try
      {
         StudioManagementClient client = connector.getManagementClient(repository);
         taskInfoList = client.getTaskList(pluginUUID, instanceId);
      } catch (Exception e) {
         throw new CoreException(StudioCorePlugin.toStatus(e, repository));
      }

      return createTaskDataFromTaskInfo(repository, pluginUUID, instanceId, taskInfoList);
   }

   /**
    * Gets the list of task data for the given iteration. If none, the current iteration is chosen.
    * @param repository
    * @param query
    * @return
    * @throws CoreException
    */
   public List<TaskData> getTasksByIteration(TaskRepository repository, IRepositoryQuery query) throws CoreException
   {
      String pluginUUID = query.getAttribute(StudioQueryAttribute.QUERY_ATTR_PLUGIN_UUID);
      String instanceId = query.getAttribute(StudioQueryAttribute.QUERY_ATTR_PLUGIN_INSTANCE_ID);
      String iterationId = query.getAttribute(StudioQueryAttribute.QUERY_ATTR_ITERATION_ID);
      Set<TaskInfo> taskInfoList = new HashSet<TaskInfo>();
      try
      {
         StudioManagementClient client = connector.getManagementClient(repository);
         if (StringUtils.isNotEmpty(iterationId))
         {
            taskInfoList = client.getTaskListByIteration(pluginUUID, instanceId, iterationId);
         }
         else
         {
            taskInfoList = client.getTaskListOfCurrentIteration(pluginUUID, instanceId);
         }
      } catch (Exception e) {
         throw new CoreException(StudioCorePlugin.toStatus(e, repository));
      }

      return createTaskDataFromTaskInfo(repository, pluginUUID, instanceId, taskInfoList);
   }

   private List<TaskData> createTaskDataFromTaskInfo(TaskRepository repository, String pluginUUID, String instanceId, Set<TaskInfo> taskInfoList)
   {
      List<TaskData> tasks = new ArrayList<TaskData>();
      TaskAttributeMapper attributeMapper = getAttributeMapper(repository);
      if (taskInfoList!=null){
	      for (TaskInfo task : taskInfoList)
	      {
	         TaskData taskData = new TaskData(attributeMapper, connector.getConnectorKind(), repository.getRepositoryUrl(),
	               buildTaskId(pluginUUID, instanceId, task.getId()));
	         taskData.setPartial(true);
	         appendAttribute(taskData, taskData.getRoot(), TaskAttribute.TASK_KEY, task.getId(), true);
	         appendAttribute(taskData, taskData.getRoot(), TaskAttribute.SUMMARY, task.getTitle(), true);
	         // adding last modified date to the taskData (but not displayed by the task ui).
	         appendAttribute(taskData, taskData.getRoot(), TaskAttribute.DATE_MODIFICATION,
	               task.getLastUpdateDate(), "dd/MM/yyyy HH:mm:ss", true);
	         if (task.getType() == TaskType.BUG)
	         {
	            appendAttribute(taskData, taskData.getRoot(), TaskAttribute.TASK_KIND, StudioTaskType.BUG.value(), true);
	         }
	         else
	         {
	            appendAttribute(taskData, taskData.getRoot(), TaskAttribute.TASK_KIND, StudioTaskType.TASK.value(), true);
	         }
	         appendAttribute(taskData, taskData.getRoot(), TaskAttribute.STATUS, toMylynStatus(task.getStatus()), true);
	         appendAttribute(taskData, taskData.getRoot(), TaskAttribute.DATE_DUE,
	               task.getEndDate(), "dd/MM/yyyy HH:mm:ss", true);
	         if (task.getStatus().equals(TaskStatus.DONE)){
	        	 appendAttribute(taskData, taskData.getRoot(), TaskAttribute.DATE_COMPLETION,
	                     task.getLastUpdateDate(), "dd/MM/yyyy HH:mm:ss", true);
	         }
	         tasks.add(taskData);
	      }
      }
      return tasks;
   }

   /**
    * Update the management task. At the moment, only the time spent and to do attributes are updated.
    * @param taskRepository
    * @param taskData
    * @param consumedTime
    * @param remainingTime
    * @throws CoreException
    */
   public void modifyTask(TaskRepository taskRepository, TaskData taskData,
         String consumedTime, String remainingTime) throws CoreException
         {
      String taskId = taskData.getTaskId();

      Task forgeTask = null;
      try
      {
         TaskAttribute pluginUUIDAttribute = taskData.getRoot().getAttribute(StudioTaskAttributeMapper.TASK_PLUGIN_UUID);
         TaskAttribute instanceIdAttribute = taskData.getRoot().getAttribute(StudioTaskAttributeMapper.TASK_INSTANCE_ID);;

         String pluginUUID = null;
         String instanceId = null;
         if (pluginUUIDAttribute != null && instanceIdAttribute != null)
         {
            pluginUUID = pluginUUIDAttribute.getValue();
            instanceId = instanceIdAttribute.getValue();
         }

         if (StringUtils.isNotEmpty(pluginUUID) && StringUtils.isNotEmpty(instanceId))
         {
            StudioManagementClient client = connector.getManagementClient(taskRepository);
            forgeTask = client.getTask(pluginUUID, instanceId, taskId);

            updateForgeTask(forgeTask, consumedTime, remainingTime);

            client.modifyTask(pluginUUID, instanceId, forgeTask);
         }
      }
      catch (Exception e)
      {
         throw new CoreException(StudioCorePlugin.toStatus(e, taskRepository));
      }
         }

   /**
    * Close the management task.
    * @param taskRepository
    * @param taskData
    * @throws CoreException
    */
   public void closeTask(TaskRepository taskRepository, TaskData taskData) throws CoreException
   {
      String taskId = taskData.getTaskId();
      try
      {
         TaskAttribute pluginUUIDAttribute = taskData.getRoot().getAttribute(StudioTaskAttributeMapper.TASK_PLUGIN_UUID);
         TaskAttribute instanceIdAttribute = taskData.getRoot().getAttribute(StudioTaskAttributeMapper.TASK_INSTANCE_ID);;

         String pluginUUID = null;
         String instanceId = null;
         if (pluginUUIDAttribute != null && instanceIdAttribute != null)
         {
            pluginUUID = pluginUUIDAttribute.getValue();
            instanceId = instanceIdAttribute.getValue();
         }

         if (StringUtils.isNotEmpty(pluginUUID) && StringUtils.isNotEmpty(instanceId))
         {
            StudioManagementClient client = connector.getManagementClient(taskRepository);
            client.closeTask(pluginUUID, instanceId, taskId);
         }
      }
      catch (Exception e)
      {
         throw new CoreException(StudioCorePlugin.toStatus(e, taskRepository));
      }
   }

   /**
    * Get the list of iterations (used by the user when choosing the management query filter).
    * @param taskRepository
    * @param pluginUUID
    * @param instanceId
    * @return
    * @throws CoreException
    */
   public List<String> getIterationList(TaskRepository taskRepository, String pluginUUID, String instanceId) throws CoreException
   {
      List<String> result = new ArrayList<String>();
      try
      {
         StudioManagementClient client = connector.getManagementClient(taskRepository);
         Set<Iteration> iterations = client.getIterationList(pluginUUID, instanceId);
         
         if (iterations!=null && !iterations.isEmpty()){
	         for (Iteration iteration : iterations)
	         {
	            result.add(iteration.getId());
	         }
         }
      }
      catch (Exception e)
      {
         throw new CoreException(StudioCorePlugin.toStatus(e, null));
      }
      return result;
   }

   private void updateForgeTask(Task forgeTask, String consumedTime, String remainingTime)
   {
      if (forgeTask != null)
      {
         Float newConsumedTime = Float.parseFloat(consumedTime);
         if (newConsumedTime != null)
         {
            forgeTask.setConsumedTime(newConsumedTime);
         }

         Float newRemainingTime = Float.parseFloat(remainingTime);
         if (newRemainingTime != null)
         {
            forgeTask.setRemainingTime(newRemainingTime);
         }
      }
   }

   private String buildTaskId(String pPluginUUID, String pInstanceId, String pTaskId)
   {
		//A UUID has this kind of format: 550e8400-e29b-41d4-a716-446655440000
		//We replace the "-" by the "_" because a Mylyn taskId can not contain "-" char.
		String pluginUUID = pPluginUUID.replace('-', '_');
		String instanceId = pInstanceId.replace('-', '_');
		StringBuilder taskIdBuilder = new StringBuilder();
		//a UUID
		taskIdBuilder.append(pluginUUID).append(ID_SEPARATOR);
		//a UUID
		taskIdBuilder.append(instanceId).append(ID_SEPARATOR);
		//The project management task ID
		taskIdBuilder.append(pTaskId);
		String id = taskIdBuilder.toString();
		return id;
   }

   private String getPluginUUIDfromId(String id)
   {
	   String uuid = findTokenFromId(id, 0);
	   return (uuid==null)?null:uuid.replace('_', '-');
   }

   private String getInstanceIdFromId(String id)
   {
	   String uuid = findTokenFromId(id, 1);
	   return (uuid==null)?null:uuid.replace('_', '-');
   }

   private String getTaskIdFromId(String id)
   {
      return findTokenFromId(id, 2);
   }

   private String findTokenFromId(String id, int position)
   {
      String result = null;

      String[] tokens = id.split(ID_SEPARATOR);
      if (tokens.length == 3)
      {
         if (position < tokens.length)
         {
            result = tokens[position];
         }
      }
      return result;
   }

   private TaskData createTaskDataFromForgeTask(TaskRepository repository, Task forgeTask, String pluginUUID, String instanceId)
   {
      TaskAttributeMapper attributeMapper = getAttributeMapper(repository);
      TaskData taskData = new TaskData(attributeMapper, connector.getConnectorKind(), repository.getRepositoryUrl(), forgeTask.getId());
      taskData.setPartial(false);

      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_PLUGIN_UUID, pluginUUID, true);
      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_INSTANCE_ID, instanceId, true);

	  
	  appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_CATEGORY,
			  forgeTask.getCategory()==null?null:forgeTask.getCategory().getDescription(), true);

      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_COMMENT,
            forgeTask.getComment(), true);

      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_CONSUMED_TIME,
    		  String.valueOf(forgeTask.getConsumedTime()), false);

      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_DESCRIPTION,
            forgeTask.getDescription(), true);

      
     appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_DISCIPLINE,
    		 forgeTask.getDiscipline()==null?null:forgeTask.getDiscipline().getDescription(), true);
     

      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_END_DATE,
            forgeTask.getEndDate(), true);

      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.ID,
            forgeTask.getId(), true);

      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_INITIAL_ESTIMATION,
            String.valueOf(forgeTask.getInitialEstimation()), true);

      
     appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_ITERATION,
    		 forgeTask.getIteration()==null?null:String.valueOf(forgeTask.getIteration().getIterationNumber()), true);
      

      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_LAST_UPDATE_DATE,
            forgeTask.getLastUpdateDate(), true);

      // adding last modified date to the taskData (but not displayed by the task ui).
      appendAttribute(taskData, taskData.getRoot(), TaskAttribute.DATE_MODIFICATION,
            forgeTask.getLastUpdateDate(), "dd/MM/yyyy HH:mm:ss", true);

      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_REMAINING_TIME,
            String.valueOf(forgeTask.getRemainingTime()), false);

      
     appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_SCOPE_UNIT,
    		 forgeTask.getScopeUnit()==null?null:forgeTask.getScopeUnit().getTitle(), true);
      
      
      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_START_DATE,
            forgeTask.getStartDate(), true);

      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_STATUS,
    		  forgeTask.getStatus()==null?null:forgeTask.getStatus().toString(), true);

      boolean isTaskEditable = forgeTask.getStatus() != TaskStatus.DONE;
      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_EDITABLE,
            Boolean.toString(isTaskEditable), true);

      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_TITLE,
            forgeTask.getTitle(), true);

      appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.TASK_TYPE,
            forgeTask.getType().toString(), true);

      BugTrackerIssue issue = forgeTask.getIssue();
      if (issue != null)
      {
         appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.ISSUE_TRACKER_ID,
               issue.getBugTrackerId(), true);

         appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.ISSUE_CATEGORY,
               issue.getCategory(), true);

         appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.ISSUE_REPORTER,
               issue.getReporter(), true);

         appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.ISSUE_ASSIGNED_TO,
               issue.getAssignedTo(), true);

         appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.ISSUE_PRIORITY,
               issue.getPriority(), true);

         appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.ISSUE_SEVERITY,
               issue.getSeverity(), true);

         appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.ISSUE_REPRODUCIBILITY,
               issue.getReproducibility(), true);

         appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.ISSUE_STATUS,
               issue.getStatus(), true);

         appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.ISSUE_RESOLUTION,
               issue.getResolution(), true);

         appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.ISSUE_PRODUCT_VERSION,
               issue.getProductVersion(), true);

         appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.ISSUE_TITLE,
               issue.getTitle(), true);

         appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.ISSUE_DESCRIPTION,
               issue.getDescription(), true);

         appendAttribute(taskData, taskData.getRoot(), StudioTaskAttributeMapper.ISSUE_ADDITIONAL_INFO,
               issue.getAdditionalInfo(), true);
      }
      return taskData;
   }
   
   public void setTaskModificationDate(TaskData taskData, ITask task)
   {
      StudioTaskAttributeMapper studioTaskAttributeMapper = (StudioTaskAttributeMapper) taskData
            .getAttributeMapper();
      // setting the modification date
      Date modified_date = studioTaskAttributeMapper.getDateValue(
            taskData.getRoot().getAttribute(TaskAttribute.DATE_MODIFICATION), "dd/MM/yyyy HH:mm:ss");
      task.setModificationDate(modified_date);

   }

   public boolean hasTaskModificationDateChanged(TaskData taskData, ITask task)
   {
      boolean changed = false;
      StudioTaskAttributeMapper studioTaskAttributeMapper = (StudioTaskAttributeMapper) taskData
            .getAttributeMapper();
      Date taskDataModifiedDate = studioTaskAttributeMapper.getDateValue(
            taskData.getRoot().getAttribute(TaskAttribute.DATE_MODIFICATION), "dd/MM/yyyy HH:mm:ss");
      changed |= hasChanges(task.getModificationDate(), taskDataModifiedDate);
      return changed;
   }

   @Override
   public TaskAttributeMapper getAttributeMapper(TaskRepository repository) {
      return new StudioTaskAttributeMapper(repository);
   }

   @Override
   public boolean initializeTaskData(TaskRepository arg0, TaskData arg1,
         ITaskMapping arg2, IProgressMonitor arg3) throws CoreException {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public RepositoryResponse postTaskData(TaskRepository arg0, TaskData arg1,
         Set<TaskAttribute> arg2, IProgressMonitor arg3)
               throws CoreException {
      // TODO Auto-generated method stub
      return null;
   }
   
   /**
    * Returns the standard Mylyn Task Status in function of a Novaforge TaskStatus
    * 
    * @param taskStatus
    * @return
    */
   private String toMylynStatus(TaskStatus taskStatus) {
	   String status = "";
	   switch (taskStatus) {
	case NOT_STARTED:
		status = "new";
		break;
	case NOT_AFFECTED:
		status = "new";
		break;
	case IN_PROGRESS:
		status = "assigned";
		break;
	case DONE:
		status = "closed";
		break;
	case CANCELLED:
		status = "closed";
		break;
	default:
		break;
	}
	return status;   
   }

}
