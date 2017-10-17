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


package org.novaforge.studio.ui;

import org.apache.commons.lang.StringUtils;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;

/**
 * Abstract editor class providing methods to get task attribute values
 *
 */
public abstract class AbstractEditorPart extends AbstractTaskEditorPart 
{
	private final static String NULL_VALUE = "n/a";
	
	protected String getAttributeValue(TaskAttribute parent, String attributeName)
	{
		String result = NULL_VALUE;
		
		if (parent != null)
		{
			TaskAttribute attribute = parent.getAttribute(attributeName);
			result = getAttributeValue(attribute);
		}
		return result;
	}
	
	protected String getAttributeValue(TaskAttribute attribute)
	{
		String result = NULL_VALUE;
		if (attribute != null && StringUtils.isNotEmpty(attribute.getValue()))
		{
			result = attribute.getValue();
		}
		return result;
	}
}
