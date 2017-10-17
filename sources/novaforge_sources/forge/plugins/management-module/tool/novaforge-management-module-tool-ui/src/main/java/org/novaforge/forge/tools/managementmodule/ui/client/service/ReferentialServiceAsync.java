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
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustFactorDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustWeightDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationComponentSimpleDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LanguageDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.MarkerTypeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.PhaseTypeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeTypeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.StatusProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.StatusScopeDTO;

import java.util.List;
import java.util.Set;


/**
 * The async counterpart of <code>referentialservice</code>.
 */
public interface ReferentialServiceAsync
{
   void getMarkerTypeDTOList(AsyncCallback<List<MarkerTypeDTO>> callback);

   void getLanguageDTOList(AsyncCallback<List<LanguageDTO>> callback);

   void getAllAdjustFactorDTOList(AsyncCallback<List<AdjustFactorDTO>> callback);

   void getAllAdjustWeightDTOList(AsyncCallback<List<AdjustWeightDTO>> callback);

	void getAllPhareDTOList(AsyncCallback<List<DisciplineDTO>> callback);
	
	void getAllPhaseTypesDTOList(AsyncCallback<List<PhaseTypeDTO>> callback);
	
	void getAllScopeTypeDTOList(AsyncCallback<List<ScopeTypeDTO>> callback);
	
	void getAllStatusScopeDTOList(AsyncCallback<List<StatusScopeDTO>> callback);
	
	void getAllStatusProjectPlanDTOList(
			AsyncCallback<List<StatusProjectPlanDTO>> callback);

	void getSteeringSimpleFunctionDTO(String idProject,
			AsyncCallback<EstimationComponentSimpleDTO> callback);

	/**
	 * Asyn getting default project disciplines
	 * @param projectId
	 * @param callback
	 */
	void getProjectDisciplines(String projectId, AsyncCallback<Set<ProjectDisciplineDTO>> callback);

}
