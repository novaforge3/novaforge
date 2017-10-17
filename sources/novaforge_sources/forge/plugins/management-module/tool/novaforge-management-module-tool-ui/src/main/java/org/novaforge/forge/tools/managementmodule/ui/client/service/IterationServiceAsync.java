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
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.PhaseTypeDTO;

import java.util.List;


/**
 * The async counterpart of <code>IterationService</code>.
 */
public interface IterationServiceAsync
{
	void getIterationList(Long projectPlanId, AsyncCallback<List<IterationDTO>> iCb);

	/**
	 * Async getting all finished and current iterations
	 * @param projectPlanId
	 * @param callback
	 */
	void getFinishedAndCurrentIterationList(Long projectPlanId, AsyncCallback<List<IterationDTO>> callback);

	/**
	 * Async getting next iteration
	 * @param pProjectPlanId
	 * @param pSimple
	 * @param callback
	 */
	void getNextIteration(Long pProjectPlanId, boolean pSimple,
			AsyncCallback<IterationDTO> callback);

	/**
	 * Get the current iteration if exists or the last finished one if there is no current iteration
	 * @param pProjectPlanId
	 * @param pSimple
	 * @param callback
	 */
	void getCurrentOrLastFinishedIteration(Long pProjectPlanId, boolean pSimple,
			AsyncCallback<IterationDTO> callback);
	
	void getPhasesTypesForNextIteration(final IterationDTO currentIterationDTO, AsyncCallback<List<PhaseTypeDTO>> cb);

	void createIteration(IterationDTO iteration, AsyncCallback<Boolean> iCb);

	void deleteIteration(long projectPlanId, long pIterationIdToDelete, AsyncCallback<Boolean> cb);

	void updateIteration(long projectPlanId, IterationDTO iteration, AsyncCallback<IterationDTO> iCb);

	void getLotsList(long projectPlanId, AsyncCallback<List<LotDTO>> callback);

	void getIterationById(long pIterationId, boolean pSimple, AsyncCallback<IterationDTO> callback);



}
