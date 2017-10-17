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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.cxf.common.util.StringUtils;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;

/**
 * Specific task attribute mapper used by novastudio plugin to manage Management task items.
 *
 */
public class StudioTaskAttributeMapper extends TaskAttributeMapper
{

   public static final String TASK_EDITABLE = "task.attribute.editable";

   public static final String TASK_PLUGIN_UUID = "task.attribute.plugin-uuid";
   
   public static final String TASK_INSTANCE_ID = "task.attribute.instance-id";
   
   public static final String TASK_CATEGORY = "task.attribute.category";
   
   public static final String TASK_COMMENT = "task.attribute.comment";
   
   public static final String TASK_CONSUMED_TIME = "task.attribute.consumed-time";
   
   public static final String TASK_DESCRIPTION = "task.attribute.description";

   
   public static final String TASK_DISCIPLINE = "task.attribute.discipline";
   
   public static final String TASK_END_DATE = "task.attribute.end-date";
   
   public static final String ID = "task.attribute.id";
   
   public static final String TASK_INITIAL_ESTIMATION = "task.attribute.initial-estimation";
   
   public static final String TASK_ITERATION = "task.attribute.iteration";
   
   public static final String TASK_LAST_UPDATE_DATE = "task.attribute.last-update-date";
   
   public static final String TASK_REMAINING_TIME = "task.attribute.remaining-time";
   
   public static final String TASK_SCOPE_UNIT = "task.attribute.scope-unit";
   
   public static final String TASK_START_DATE = "task.attribute.start-date";
   
   public static final String TASK_STATUS = "task.attribute.status";
   
   public static final String TASK_TITLE = "task.attribute.title";

   public static final String TASK_TYPE = "task.attribute.type";
   
   public static final String ISSUE_TRACKER_ID = "issue.attribute.tracker.id";
   
   public static final String ISSUE_CATEGORY = "issue.attribute.category";
   
   public static final String ISSUE_REPORTER = "issue.attribute.reporter";
   
   public static final String ISSUE_ASSIGNED_TO = "issue.attribute.assigned.to";
   
   public static final String ISSUE_PRIORITY = "issue.attribute.priority";
   
   public static final String ISSUE_SEVERITY = "issue.attribute.severity";
   
   public static final String ISSUE_REPRODUCIBILITY = "issue.attribute.reproducibility";
   
   public static final String ISSUE_STATUS = "issue.attribute.status";
   
   public static final String ISSUE_RESOLUTION = "issue.attribute.resolution";
   
   public static final String ISSUE_PRODUCT_VERSION = "issue.attribute.product.version";
   
   public static final String ISSUE_TITLE = "issue.attribute.title";

   public static final String ISSUE_DESCRIPTION = "issue.attribute.description";

   public static final String ISSUE_ADDITIONAL_INFO = "issue.attribute.additional.info";
   
   private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";


   public StudioTaskAttributeMapper(TaskRepository taskRepository)
   {
      super(taskRepository);
   }

   @Override
   public Date getDateValue(TaskAttribute attribute)
   {
      Date result = null;

      if (attribute == null || StringUtils.isEmpty(attribute.getValue()))
      {
         return result;
      }

      SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
      try
      {
         result = dateFormat.parse(attribute.getValue());
      }
      catch (ParseException e)
      {
         // Do nothing, null value is returned
      }
      return result;
   }

   /**
    * Gets the date value of a task attribute corresponding to the given format.
    * @param attribute
    * @param dateFormat
    * @return
    */
   public Date getDateValue(TaskAttribute attribute, String dateFormat)
   {
      Date date = null;

      if (attribute == null || StringUtils.isEmpty(attribute.getValue()))
      {
         return date;
      }
      SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
      try
      {
         date = formatter.parse(attribute.getValue());
      }
      catch (ParseException e)
      {

      }
      return date;
   }

   @Override
   public void setDateValue(TaskAttribute attribute, Date date)
   {
      setDateValue(attribute, date, DEFAULT_DATE_FORMAT);
   }

   /**
    * Sets the date value to a task attribute
    * @param attribute
    * @param date
    * @param dateFormat
    */
   public void setDateValue(TaskAttribute attribute, Date date, String dateFormat)
   {
      if (date == null || attribute == null || StringUtils.isEmpty(dateFormat))
      {
         return;
      }

      SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
      String formatedDate = formatter.format(date);
      attribute.setValue(formatedDate);
   }
}
