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


package org.novaforge.studio.core.repository;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.novaforge.studio.core.StudioCorePlugin;
import org.novaforge.studio.core.StudioRepositoryType;

/**
 * Util class used to duplicate a task repository instance. It is mainly used to create a Novastudio tasks repository
 * from a project repository. 
 *
 */
public class RepositoryUtil 
{
	/**
	 * Duplicates the given repository into a new one with different configuration (repository type, label, url...).
	 * @param model
	 * @param newRepositoryLabel
	 * @param newRepositoryType
	 * @return
	 */
	public static TaskRepository duplicateRepository(TaskRepository model, String newRepositoryLabel, StudioRepositoryType newRepositoryType)
	{
		TaskRepository newRepository = new TaskRepository(model.getRepositoryLabel(), model.getRepositoryUrl());
		for (Map.Entry<String, String> entry : model.getProperties().entrySet())
		{
			newRepository.setProperty(entry.getKey(), entry.getValue());
		}
		newRepository.setRepositoryLabel(newRepositoryLabel);
		newRepository.setRepositoryUrl(adaptRepositoryUrl(model, newRepositoryType));
		newRepository.setCredentials(AuthenticationType.REPOSITORY, 
				model.getCredentials(AuthenticationType.REPOSITORY), 
				model.getSavePassword(AuthenticationType.REPOSITORY));
		newRepository.setProperty(StudioCorePlugin.REPOSITORY_PROP_TYPE, newRepositoryType.toString());
		
		return newRepository;
	}
	
	/**
	 * Converts a given repository url into an url matching the given repository type.
	 * @param model
	 * @param newRepositoryType
	 * @return
	 */
	public static String adaptRepositoryUrl(TaskRepository model, StudioRepositoryType newRepositoryType)
	{
		String result = model.getRepositoryUrl();
		
		String modelTypeProperty = model.getProperty(StudioCorePlugin.REPOSITORY_PROP_TYPE);
		
		if (StringUtils.isNotEmpty(modelTypeProperty))
		{
			StudioRepositoryType modelType = StudioRepositoryType.valueOf(modelTypeProperty);
			
			result = result.replaceAll(modelType.serviceName(), newRepositoryType.serviceName());
		}
		return result;
	}
	
	/**
	 * Returns the base url of a tool. Returns an empty string if toolUrl is null or empty.
	 * 
	 * @param toolUrl
	 * @return
	 */
	public static String getBaseToolUrl(String toolUrl){
		String url = "";
		if (toolUrl!=null && !toolUrl.isEmpty()){
			int i = toolUrl.lastIndexOf("/");
			if (i>0){
				url = toolUrl.substring(0,i);
			}
		}
		return url;
	}
}
