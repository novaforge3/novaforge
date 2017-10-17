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

import java.util.Date;

import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.novaforge.studio.core.project.ProjectAttributeMapper;
import org.novaforge.studio.core.task.StudioTaskAttributeMapper;

/**
 * Data handler abstract class providing methods to manage task data (add task attributes in a task data instance...) 
 *
 */
public abstract class AbstractStudioDataHandler extends AbstractTaskDataHandler
{
   private static final String NOT_AVAILABLE = "N/A";
protected final static String ID_SEPARATOR = ":";
   
   protected TaskAttribute appendAttribute(TaskData taskData, TaskAttribute parent, String attributeId, Date attributeValue, boolean readOnly)
   {
      TaskAttributeMapper attributeMapper = taskData.getAttributeMapper();
      TaskAttribute newAttribute = initTaskAttribute(parent, attributeId, readOnly);
      attributeMapper.setDateValue(newAttribute, attributeValue);
      return newAttribute;
   }

   protected TaskAttribute appendAttribute(TaskData taskData, TaskAttribute parent, String attributeId,
         Date attributeValue, String dateFormat, boolean readOnly)
   {
      StudioTaskAttributeMapper attributeMapper = (StudioTaskAttributeMapper) taskData.getAttributeMapper();
      TaskAttribute newAttribute = initTaskAttribute(parent, attributeId, readOnly);
      attributeMapper.setDateValue(newAttribute, attributeValue, dateFormat);
      return newAttribute;
   }

   protected TaskAttribute appendProjectAttribute(TaskData taskData, TaskAttribute parent,
         String attributeId, Date attributeValue, String dateFormat, boolean readOnly)
   {
      ProjectAttributeMapper attributeMapper = (ProjectAttributeMapper) taskData.getAttributeMapper();
      TaskAttribute newAttribute = initTaskAttribute(parent, attributeId, readOnly);
      attributeMapper.setDateValue(newAttribute, attributeValue, dateFormat);
      return newAttribute;
   }

   protected TaskAttribute appendAttribute(TaskData taskData, TaskAttribute parent, String attributeId, String attributeValue, boolean readOnly)
   {
      TaskAttributeMapper attributeMapper = taskData.getAttributeMapper();
      TaskAttribute newAttribute = initTaskAttribute(parent, attributeId, readOnly);
      if (attributeValue==null){
    	  attributeMapper.setValue(newAttribute, NOT_AVAILABLE);
      } else {
    	  attributeMapper.setValue(newAttribute, attributeValue);
      }
      return newAttribute;
   }

   private TaskAttribute initTaskAttribute(TaskAttribute parent, String attributeId, boolean readOnly)
   {
      TaskAttribute newAttribute = parent.createAttribute(attributeId);
      TaskAttributeMetaData metaData = newAttribute.getMetaData();
      metaData.setType(TaskAttribute.TYPE_LONG_TEXT);
      metaData.setKind(TaskAttribute.KIND_DEFAULT);
      metaData.setReadOnly(readOnly);
      return newAttribute;
   }

   protected boolean hasChanges(Object existingValue, Object newValue)
   {
      return (existingValue != null) ? !existingValue.equals(newValue) : newValue != null;
   }
}
