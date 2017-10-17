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

import org.novaforge.forge.remote.services.core.RemoteProjectService;
import org.novaforge.forge.remote.services.management.RemoteManagementService;

/**
 * List of Novastudio repository types (and also the remote service name 
 * associated used to automatically generate the repository url).
 *
 */
public enum StudioRepositoryType 
{
	PROJECT(RemoteProjectService.REMOTE_PROJECT_SERVICE_NAME), TASK(RemoteManagementService.REMOTE_MANAGEMENT_SERVICE_NAME);
	
	private String remoteServiceName;
	
	private StudioRepositoryType(String remoteServiceName)
	{
		this.remoteServiceName = remoteServiceName;
	}
	
	public String serviceName()
	{
		return this.remoteServiceName;
	}
}
