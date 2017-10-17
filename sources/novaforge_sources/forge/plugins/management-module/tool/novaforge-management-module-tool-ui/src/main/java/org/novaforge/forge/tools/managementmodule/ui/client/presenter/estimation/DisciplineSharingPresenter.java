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
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.estimation;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.view.estimation.DisciplineSharingView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.estimation.DisciplineSharingViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplinePhareEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineSharingDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDisciplineDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author BILET-JC
 * 
 */
public class DisciplineSharingPresenter implements Presenter {

	private final DisciplineSharingView display;
	private List<EstimationDTO> estimations;
	private Set<ProjectDisciplineDTO> projectDisciplines;
	/**
	 * RPC call service to get the list of discipline sharing
	 */
	AbstractManagementRPCCall<Set<ProjectDisciplineDTO>> getProjectDisciplines = new AbstractManagementRPCCall<Set<ProjectDisciplineDTO>>()
	{

		@Override
		protected void callService(AsyncCallback<Set<ProjectDisciplineDTO>> pCb)
		{
			Common.REFERENTIAL_SERVICE.getProjectDisciplines(SessionData.projectId, pCb);
		}

		@Override
		public void onFailure(Throwable pCaught)
		{
			ErrorManagement.displayErrorMessage(pCaught);
		}

		@Override
		public void onSuccess(Set<ProjectDisciplineDTO> pResult)
		{
			if (pResult != null)
			{
				projectDisciplines = pResult;
				display.setSharingHeader(projectDisciplines);
				refreshList();
				display.showWidget();
			}
		}
	};

	/**
	 * @param projectPlanId
	 */
	public DisciplineSharingPresenter()
	{
		super();
		display = new DisciplineSharingViewImpl();
	}

	/**
	 * Refresh the ScopeUnitDiscipline list in the view
	 *
	 * @param pList
	 */
	public void refreshList()
	{
		List<DisciplineSharingDTO> disciplineSharings = new ArrayList<DisciplineSharingDTO>();
		for (EstimationDTO e : estimations)
		{
			DisciplineSharingDTO ds = new DisciplineSharingDTO();
			ds.setScopeUnitName(e.getScopeUnit().getName());
			ds.setCharge(e.getCharge());
			for (ProjectDisciplineDTO projectDiscipline : projectDisciplines)
			{
				Float f = Common.floatFormat(new Float(e.getCharge()) / 100 * projectDiscipline.getDisciplinePourcentage(), 1);
				if (projectDiscipline.getDisciplineDTO().getFunctionalId().equals(DisciplinePhareEnum.architectureDesign
																																							.name()))
				{
					ds.setArchitectureDesign(f);
				}
				if (projectDiscipline.getDisciplineDTO().getFunctionalId().equals(DisciplinePhareEnum.businessModeling.name()))
				{
					ds.setBusinessModeling(f);
				}
				if (projectDiscipline.getDisciplineDTO().getFunctionalId().equals(DisciplinePhareEnum.changeDriving.name()))
				{
					ds.setChangeDriving(f);
				}
				if (projectDiscipline.getDisciplineDTO().getFunctionalId().equals(DisciplinePhareEnum.configurationManagement
																																							.name()))
				{
					ds.setConfigurationManagement(f);
				}
				if (projectDiscipline.getDisciplineDTO().getFunctionalId().equals(DisciplinePhareEnum.implementation.name()))
				{
					ds.setImplementation(f);
				}
				if (projectDiscipline.getDisciplineDTO().getFunctionalId().equals(DisciplinePhareEnum.projectManagement.name()))
				{
					ds.setProjectManagement(f);
				}
				if (projectDiscipline.getDisciplineDTO().getFunctionalId().equals(DisciplinePhareEnum.qualityAssurance.name()))
				{
					ds.setQualityAssurance(f);
				}
				if (projectDiscipline.getDisciplineDTO().getFunctionalId().equals(DisciplinePhareEnum.receipts.name()))
				{
					ds.setReceipts(f);
				}
				if (projectDiscipline.getDisciplineDTO().getFunctionalId().equals(DisciplinePhareEnum.requirementsAnalysis
																																							.name()))
				{
					ds.setRequirementsAnalysis(f);
				}
			}
			disciplineSharings.add(ds);
		}
		display.getDataProvider().setList(disciplineSharings);
		display.updateSortHandler();
	}

	@Override
	public void go(HasWidgets container)
	{
		if (estimations != null)
		{
			getProjectDisciplines.retry(0);
		}
	}

	@Override
	public IsWidget getDisplay()
	{
		return display;
	}

	/**
	 * @return the estimations
	 */
	public List<EstimationDTO> getEstimations()
	{
		return estimations;
	}

	/**
	 * @param estimations the estimations to set
	 */
	public void setEstimations(List<EstimationDTO> estimations)
	{
		this.estimations = estimations;
	}

}
