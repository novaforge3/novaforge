/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */

package org.novaforge.forge.tools.managementmodule.ui.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.novaforge.forge.tools.managementmodule.ui.shared.CDOParametersDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

import java.util.List;


/**
 * The async counterpart of <code>ManagementModuleService</code>.
 */
public interface ManagementModuleServiceAsync extends SimpleServiceAsync
{
   /**
    * This methods controls the existence of the instanceId and get the appropriate projectID
    * @param instanceId the instanceId to use to look for the project
    * @return the projectId
    * @throws ManagementModuleException if unable to get a project with the instanceId
    */
	void controlInstanceAndGetProjectId(String instanceId, AsyncCallback<String> asyncCallback);
	
	/**
	 * Get the project with all this collections loaded
	 * @param idProject the idproject to use to identify the project to return
	 * @param callback the callback
	 */
	void getProject(String idProject, AsyncCallback<ProjectDTO> callback);

	/**
	 * Save the project's fields and the cdoParameters
	 * @param projectDTO
	 * @param cdoParametersDTOs
	 * @param callback
	 */
	void updateProject(ProjectDTO projectDTO, List<CDOParametersDTO> cdoParametersDTOs, AsyncCallback<Boolean> callback);

	
	void updateFromCDORefScopeUnit(ProjectDTO projet_forge, AsyncCallback<Boolean> callback);

   /**
    * Load the CDOParameters for a specified projectId
    * @param projectId
    * @return
    * @throws ManagementModuleException
    */
	void loadListParametersCDO(String projectId, AsyncCallback<List<CDOParametersDTO>> callback);
	
	/**
	 * return the name of the unitTime for a project
	 * @param projectId
	 * @param callback
	 */
	void getUnitTimeName(String projectId, AsyncCallback<String> callback);
}
