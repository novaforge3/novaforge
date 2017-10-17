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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.cxf.common.util.StringUtils;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;

/**
 * Specific task attribute mapper used by Novastudio plugin to handle project items.
 *
 */
public class ProjectAttributeMapper extends TaskAttributeMapper {

   public static final String NAME = "project.attribute.name";

   public static final String DESCRIPTION = "project.attribute.description";

   public static final String SPACE_PREFIX = "project.attribute.space-";

   public static final String APPLICATION_PREFIX = "project.attribute.space.application-";

   public static final String APPLICATION_ACCESS_INFO = "project.attribute.space.application.access.info";

   public static final String APPLICATION_CATEGORY = "project.attribute.space.application.category";
   
   public static final String APPLICATION_TYPE = "project.attribute.space.application.type";

   public static final String APPLICATION_PLUGIN_UUID = "project.attribute.space.application.plugin.uuid";
   
   public static final String APPLICATION_TOOL_URL = "project.attribute.space.application.tool.url";

   public static final String APPLICATION_PLUGIN_INSTANCE_ID = "project.attribute.space.application.plugin.instance.id";

   private static final String DEFAULT_DATE_FORMAT            = "dd/MM/yyyy";

   public ProjectAttributeMapper(TaskRepository taskRepository) {
      super(taskRepository);
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
